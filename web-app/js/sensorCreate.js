$(function()
{
    var dataSource = contextPath + '/tag/lookupBySerialNumber';
    $("#tag\\.serialNumber").autocomplete({source:dataSource});
    
    // If known serial number, fill in tag properties
    $("#tag\\.serialNumber").autocomplete(
    {
    	select: function(event, ui)
    	{
    		updateTagFields(ui.item);
    	}
    });
});

function setUnit() {
	var allowedUnits = {
		"PRESSURE": "m",
		"TEMPERATURE": "°C",
		"ACCELEROMETER": "m/s²"
	};
	var selectedTransmitterTypeLabel = $("#transmitterType\\.id option:selected").text();
	var option = allowedUnits[selectedTransmitterTypeLabel];

    $('input[name=unit]').val(option);
}

function updateTagFields(tag)
{
	$("#tag\\.project\\.id").val(tag.project.id);
	$("#tag\\.model\\.id").val(tag.model.id);
	$("#tag\\.codeMap\\.id").val(tag.codeMap.id);
	$("#tag\\.expectedLifeTimeDays").val(tag.expectedLifeTimeDays);
	$("#tag\\.status\\.id").val(tag.status.id);
}

$(function ()
{
	setSensorFieldsEnabled();
	$("#transmitterType\\.id").change(function() 
	{
		setUnit();
		setSensorFieldsEnabled();
	});
});

function setSensorFieldsEnabled()
{
	var sensorFields = ["slope", "intercept", "unit"];
	$.each(sensorFields, function(index, fieldSelector)
	{
		var cssClass =  (fieldSelector != "unit") ? "compulsory" : "compulsoryReadonly";

		if (['PINGER','RANGE TEST'].indexOf($("#transmitterType\\.id option:selected").text()) > -1 )
		{
            $("#" + fieldSelector).attr("readonly", "readonly");
            $("#" + fieldSelector).val("");
			$("#" +fieldSelector).attr("placeholder", "not applicable");
            $("label[for=" + fieldSelector + "]").removeClass();

        }
		else
		{
			if (fieldSelector != "unit") {
                $("#" + fieldSelector).removeAttr("readonly");
			}
			$("#" + fieldSelector).removeAttr("placeholder");
            $("label[for=" + fieldSelector + "]").addClass(cssClass);
        }
	});
}