/**
 * Functions related to the tagging dialog that pops up in AnimalRelease
 * entry, including tag ID autocomplete and auto-population of the serial number
 * and device model fields.
 */
$(function()
{
    $("#serialNumber").autocomplete(
    {
    	source:contextPath + "/tag/lookupNonDeployedBySerialNumber",
    	select:function(event, ui)
	    {
    		console.log(ui.item)
    		
    		// Update the code name, ping code and model select when a tag ID is selected.
	        $("#tagCodeMapId").val(ui.item.codeMap.id);
	        $("#pingCode").val(ui.item.pingCode);
	        $("#modelId").val(ui.item.model.id);
	    },
	    change:function(event, ui)
	    {
	        setExistingTag(ui.item != null);
	    }
	});
});

function setExistingTag(existing)
{
    if (existing)
    {
        $("#tagCodeMapId").attr("disabled", "disabled");
        $("#pingCode").attr("disabled", "disabled");
        $("#modelId").attr("disabled", "disabled");
    }
    else
    {
        $("#tagCodeMapId").attr("disabled", "");
        $("#pingCode").attr("disabled", "");
        $("#modelId").attr("disabled", "");
    	
        $("#tagCodeMapId").val("");
        $("#pingCode").val("");
        $("#modelId").val("");
    }
}
