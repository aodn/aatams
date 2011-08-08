/**
 * Functions related to the tagging dialog that pops up in AnimalRelease
 * entry, including tag ID autocomplete and auto-population of the serial number
 * and device model fields.
 */
$(function()
{
    var dataSource = "/aatams/tag/lookupByCodeName"
    $("#tagCodeName").autocomplete({source:dataSource});
});

$("#tagCodeName").autocomplete({
    select:function(event, ui)
    {
        // Update the serialNumber and model select when a tag ID is selecetd.
        $("#serialNumber").val(ui.item.serialNumber);
        $("#modelId").val(ui.item.model.id);
    }
});

$("#tagCodeName").autocomplete({
    change:function(event, ui)
    {
        setExistingTag(ui.item != null)
    }
});

function setExistingTag(existing)
{
    if (existing)
    {
        $("#serialNumber").attr("disabled", "disabled");
        $("#modelId").attr("disabled", "disabled");
    }
    else
    {
        $("#serialNumber").attr("disabled", "");
        $("#modelId").attr("disabled", "");
        
        $("#serialNumber").val("");
        $("#modelId").val("");
    }
}
