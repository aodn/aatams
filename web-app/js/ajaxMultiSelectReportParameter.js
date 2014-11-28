$(function() {
    function split( val ) {
        return val.split( /,\s*/ );
    }

    function extractLast( term ) {
        return split( term ).pop();
    }

    $(".ajaxMultiSelectReportParameter").each(function() {
        applyAutoComplete(this)
    });

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
            .bind( "keydown", function( event ) {
                if ( event.keyCode === $.ui.keyCode.TAB &&
                     $( this ).data( "autocomplete" ).menu.active ) {
                    event.preventDefault();
                }
            })
            .autocomplete({
                source: function( request, response ) {
                    $.getJSON( contextPath + lookupPath, {
                        term: extractLast( request.term )
                    }, response );
                },
                search: function() {
                    // custom minLength
                    var term = extractLast( this.value );
                    if ( term.length < 1 ) {
                        return false;
                    }
                },
                focus: function() {
                    // prevent value inserted on focus
                    return false;
                },
                select: function( event, ui ) {
                    var terms = split( this.value );
                    // remove the current input
                    terms.pop();
                    // add the selected item
                    terms.push( ui.item.value );
                    // add placeholder to get the comma-and-space at the end
                    terms.push( "" );
                    this.value = terms.join( ", " );

                    return false;
                },
                change: function()
                {
                    return false;
                }
            });
    }
});
