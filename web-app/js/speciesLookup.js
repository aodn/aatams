$(function()
{
    var dataSource = "/aatams/species/lookupByName"
    $("#speciesName").autocomplete({source:dataSource});

    $("#speciesName").autocomplete({
        select:function(event, ui)
        {
            console.log("species selected:");
            console.log(ui);

            // Update hidden field speciesId
            $("#speciesId").attr("value", ui.item.id);
        }
    });

    $("#speciesName").autocomplete({
        change:function(event, ui)
        {
            if (ui.item == null)
            {
                // Remove value attribute from species ID hidden field.
                $("#speciesId").removeAttr("value");
            }
        }
    });
});
        
