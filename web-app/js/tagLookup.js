/**
 * Functions related to the tagging dialog that pops up in AnimalRelease
 * entry, including tag ID autocomplete and auto-population of the serial number
 * and device model fields.
 */
$(function()
{
    var dataSource = contextPath + "/tag/lookupNonDeployedBySerialNumber";
    $("#serialNumber").autocomplete({source:dataSource});
});

$("#serialNumber").autocomplete({
    select:function(event, ui)
    {
        // Update the serialNumber and model select when a tag ID is selecetd.
        $("#tagCodeName").val(ui.item.codeName);
        $("#modelId").val(ui.item.model.id);
    }
});

$("#serialNumber").autocomplete({
    change:function(event, ui)
    {
        setExistingTag(ui.item != null);
    }
});

function setExistingTag(existing)
{
    if (existing)
    {
        $("#tagCodeName").attr("disabled", "disabled");
        $("#modelId").attr("disabled", "disabled");
    }
    else
    {
        $("#tagCodeName").attr("disabled", "");
        $("#modelId").attr("disabled", "");
        
        $("#tagCodeName").val("");
        $("#modelId").val("");
    }
}
