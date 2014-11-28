$(function() {
    $(".ajaxMultiSelectReportParameter").each(function() {
        applyAutoComplete(this)
    });

    $(".reportFilter").find(".ui-autocomplete-input").autocomplete({
        // Override the version in ajaxMultiSelectReportParameter.js, to also submit the form
        // (because we don't want to do it in change() below, see: http://redmine.emii.org.au/issues/937
        select: function(event, ui) {
            addTerm.call(this, ui.item.value);
            submit();
            return false;
        },

        change: function(event, ui) {
            submit();

            return false;
        }
    });

    $(".reportFilter").find(":input").not(".ui-autocomplete-input").change(function() {
        submit();
    });

    function submit() {
      $(".body").block({ message: blockUIConfig.defaultMessage });
      $("#listControlForm").submit();
    }

    function split(val) {
        return val.split(/ \|\s*/);
    }

    function extractLastTerm(terms) {
        return split(terms).pop();
    }

    function addTerm(newTerm) {
        var terms = split(this.value);
        // remove the current input
        terms.pop();
        // add the selected item
        terms.push(newTerm);
        // add placeholder to get the pipe-and-space at the end
        terms.push("");
        this.value = terms.join(" | ");
    }

    function applyAutoComplete(element) {
        var textFieldIdId = $(element).attr("id") + "_textFieldId"
        var escTextFieldIdId = escapeId(textFieldIdId)
        var textFieldId = $(escTextFieldIdId).val()
        var textField = $(escapeId(textFieldId))

        var lookupPathId = $(element).attr("id") + "_lookupPath"
        var escLookupPathId = escapeId(lookupPathId)
        var lookupPath = $(escLookupPathId).val()

        textField
        // don't navigate away from the field on tab when selecting an item
            .bind("keydown", function(event) {
                if (event.keyCode === $.ui.keyCode.TAB &&
                     $(this).data("autocomplete").menu.active) {
                    event.preventDefault();
                }
            })
            .autocomplete({
                source: function(request, response) {
                    $.getJSON(contextPath + lookupPath, {
                        term: extractLastTerm(request.term)
                    }, response);
                },
                search: function() {
                    // custom minLength
                    var term = extractLastTerm(this.value);
                    if (term.length < 1) {
                        return false;
                    }
                },
                focus: function() {
                    // prevent value inserted on focus
                    return false;
                },
                select: function(event, ui) {
                    addTerm.call(this, ui.item.value);
                    return false;
                },
                change: function()
                {
                    return false;
                }
            });
    }
});
