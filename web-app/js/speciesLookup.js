$(function()
{
    var dataSource = contextPath + '/species/lookupByName'

    $('#placeSelect').select2({
        width: 500,
        minimumInputLength: 2,
        placeholder: "Click here and start typing to search.",
        ajax: {
            url: dataSource,
            dataType: 'json',
            data: function (params) {
                return {
                    term: params.term
                };
            },
            processResults: function (data) {
                return {
                    results: $.map(data, function (obj) {
                        return {id: obj.id, text: obj.name};
                    })
                };
            },
            id: function(object) {
                return object.text;
            }
        }
    }).on("change", function(e) {
        var selectedText = $(this).select2('data')[0].text;
        $("#speciesName").attr("value", selectedText);
    });

    $('#placeSelect').select2("data", { id: "${animalReleaseInstance?.animal?.species?.id}", text: "${animalReleaseInstance?.animal?.species?.name}" });
});
        
