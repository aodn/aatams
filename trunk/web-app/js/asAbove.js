/**
 * Populate the address fields "as above".
 */
$(function() 
{
    $("[id*=asAbove]").click(function(e)
    {
        var addressTable = $(this).parents("table").first()
        
        var selected = $(this).is(':checked')
        
        if (selected)
        {
            addressTable.find("input:text").attr("readonly", "readonly");
            
            // Populate from above.
            var from = addressTable.find("[id*=asAboveElement]").val();

            ["streetAddress", "suburbTown", "state", "postcode", "country"].forEach(function(item)
            {
                var fromValue = $("#" + from + "\\." + item)
                addressTable.find("[id*=" + item + "]").val(fromValue.val())
            });
        }
        else
        {
            var elements = addressTable.find("input:text")
            elements.attr("readonly", "");
            elements.val("");
        }
    });
});



