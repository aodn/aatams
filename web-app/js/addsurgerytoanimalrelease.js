// Keep count of the number of surgeries (so we can index the array sent to
// controller when form is submitted).
var numSurgeries = 0;

$(function() 
{
    $('#dialog-form-add-surgery').dialog({
        autoOpen: false,
        width: 755,
        height: 630,
        modal: true,
        buttons: {
            'Create': function() 
            {
                var animalReleaseId = $("#id").val();
                var timestamp_year = $("#surgeryTimestamp_year").val();
                var timestamp_month = $("#surgeryTimestamp_month").val();
                var timestamp_day = $("#surgeryTimestamp_day").val();
                var timestamp_hour = $("#surgeryTimestamp_hour").val();
                var timestamp_minute = $("#surgeryTimestamp_minute").val();
                var timestamp_second = $("#surgeryTimestamp_second").val();
                var timestamp_zone = $("#surgeryTimestamp_zone").val();
                var typeId = $("#surgeryTypeId option:selected").val();
                var treatmentTypeId = $("#treatmentTypeId option:selected").val();
                var comments = $("#surgeryComments").val();

                //
                // Tag related variables (user has the option of selecting an 
                // existing tag or entering new tag as part of the surgery
                // process).
                //
                // Leave it up to the controller to determine whether or not to
                // create a new tag.
                //
                var tagCodeMapId = $("#tagCodeMapId").val();
                var tagCodeMapValue = $("#tagCodeMapId option:selected").text();
                var tagSerialNumber = $("#serialNumber").val();
                var tagPingCode = $("#pingCode").val();
                var tagModelId = $("#modelId option:selected").val();

                var projectId = $("#project\\.id option:selected").val();
                var event = $("#id").val();
                
                // 
                // Is this script being called from create or edit page (which
                // determines whether we store user entered data in hidden fields
                // or send an AJAX post respectively).
                //
                if (isCreate())
                {
                    // Create a JSON map which can be passed to the "updateSurgeryTable" function.
                    var dateTime = timestamp_day + "/" + 
                                   timestamp_month + "/" + 
                                   timestamp_year + " " + 
                                   timestamp_hour + ":" + 
                                   timestamp_minute + ":" + 
                                   timestamp_second + " " + 
                                   timestamp_zone;
                    var tag = {codeMap:{id:tagCodeMapId, value:tagCodeMapValue}, serialNumber:tagSerialNumber, pingCode:tagPingCode};
                    var type = {type:$("#surgeryTypeId option:selected").text()};
                    var treatmentType = {type:$("#treatmentTypeId option:selected").text()};
                    var instance = {timestamp:dateTime, 
                                    tag:tag,
                                    type:type,
                                    treatmentType:treatmentType,
                                    comments:comments};
                    var data = {instance:instance};
                    
                    var idPrefix = "surgery." + numSurgeries + ".";
                    updateSurgeryTable(data, projectId, idPrefix);
                    
                    // Add variables as hidden fields so that the parameters are
                    // sent to the controller on form submit.
                    var mainForm = $("div.body > form")

                    mainForm.append(hiddenField(idPrefix + "timestamp_day", timestamp_day));
                    mainForm.append(hiddenField(idPrefix + "timestamp_month", timestamp_month));
                    mainForm.append(hiddenField(idPrefix + "timestamp_year", timestamp_year));
                    mainForm.append(hiddenField(idPrefix + "timestamp_hour", timestamp_hour));
                    mainForm.append(hiddenField(idPrefix + "timestamp_minute", timestamp_minute));
                    mainForm.append(hiddenField(idPrefix + "timestamp_second", timestamp_second));
                    mainForm.append(hiddenField(idPrefix + "timestamp_zone", timestamp_zone));
                    mainForm.append(hiddenField(idPrefix + "type.id", typeId));
                    mainForm.append(hiddenField(idPrefix + "treatmentType.id", treatmentTypeId));
                    mainForm.append(hiddenField(idPrefix + "comments", comments));
                    mainForm.append(hiddenField(idPrefix + "tag.codeMap.id", tagCodeMapId));
                    mainForm.append(hiddenField(idPrefix + "tag.pingCode", tagPingCode));
                    mainForm.append(hiddenField(idPrefix + "tag.serialNumber", tagSerialNumber));
                    mainForm.append(hiddenField(idPrefix + "tag.model.id", tagModelId));
                }
                else
                {
                    $.post(contextPath + '/surgery/save',
                    {
                       'event.id':event,
                       'projectId':projectId,
                       'release.id':animalReleaseId,
                       'timestamp_year':timestamp_year,
                       'timestamp_month':timestamp_month,
                       'timestamp_day':timestamp_day,
                       'timestamp_hour':timestamp_hour,
                       'timestamp_minute':timestamp_minute,
                       'timestamp_second':timestamp_second,
                       'timestamp_zone':timestamp_zone,
                       'tag.codeMap.id':tagCodeMapId,
                       'tag.pingCode':tagPingCode,
                       'tag.serialNumber':tagSerialNumber,
                       'tag.model.id':tagModelId,
                       'type.id':typeId,
                       'treatmentType.id':treatmentTypeId,
                       'comments':comments
                    },
                    function(data)
                    {
                       updateHeader(data);
                       updateSurgeryTable(data, projectId, null);

                    },
                    'json');
                }
                
                numSurgeries++;
                $(this).dialog('close');
            },
            Cancel: function() {
                $(this).dialog('close');
            }

        }        
    });
    
    $('#add_surgery_to_animal_release').click(function() 
    {
        var addSurgeryUrl = contextPath + '/animalRelease/addSurgery';
        var animalReleaseId = $("#animalReleaseId").val();
        if (animalReleaseId)
        {
            addSurgeryUrl += "/" + animalReleaseId
        }
        
        var projectId = $("#project\\.id option:selected").val();
        $('#dialog-form-add-surgery').load(addSurgeryUrl, 
                                           {projectId:projectId}, 
                                           function()
        {
            if (isCreate())
            {
				$("#surgeryTimestamp_year").val($("#captureDateTime_year").val());
				$("#surgeryTimestamp_month").val($("#captureDateTime_month").val());
				$("#surgeryTimestamp_day").val($("#captureDateTime_day").val());
				$("#surgeryTimestamp_hour").val($("#captureDateTime_hour").val());
				$("#surgeryTimestamp_minute").val($("#captureDateTime_minute").val());
				$("#surgeryTimestamp_second").val($("#captureDateTime_second").val());
				$("#surgeryTimestamp_zone").val($("#captureDateTime_zone").val());
            }
            
            $('#dialog-form-add-surgery').dialog('open');
        });
    });
});

function isCreate()
{
    var mainForm = $("div.body > form")
    return mainForm.attr("action") == contextPath + '/animalRelease/save'
}

function isEditView(data)
{
    return (data.instance.id)
}

function updateSurgeryTable(data, projectId, idPrefix)
{
    if (!data.instance)
    {
        // Error condition, no instance was returned.
        return
    }
    
    var tableRow = $("<tr>");

    // Only show info colum in edit screen (i.e. when the surgery instance
    // has actually been persisted).
    if (isEditView(data))
    {
        var infoColumn = $("<td>").attr("class", "rowButton");
        var infoLink = $("<a>").attr("href", contextPath + '/surgery/show/' + data.instance.id);
        infoLink.attr("class", "show");
        infoColumn.append(infoLink);
        tableRow.append(infoColumn);

        var deleteColumn = $("<td>").attr("class", "rowButton");
        var deleteLink = $("<a>").attr("href", contextPath + '/surgery/delete/' + data.instance.id + "?projectId=" + projectId);
        deleteLink.attr("class", "delete");
        deleteLink.click(function() { return confirm('Are you sure?'); });
        deleteColumn.append(deleteLink);
        tableRow.append(deleteColumn);
    }
    //
    // create view - no entities are actually persisted until "create"
    // is pressed.
    //
    else    
    {
        // We want to enable users to delete (but not edit).
        deleteColumn = $("<td>").attr("class", "rowButton");
        deleteLink = $("<a>").attr("href", '#');
        deleteLink.attr("class", "delete");
        deleteLink.click(function() 
        {
            if (confirm('Are you sure?'))
            {
                // Remove this row.
                tableRow.remove();
                
                // Remove hidden fields.
                $("[id*='" + idPrefix + "']").remove();
            }
                
        });
        deleteColumn.append(deleteLink);
        tableRow.append(deleteColumn);
    }
    
    var dateTimeColumn = $("<td>").attr("class", "value").html(data.instance.timestamp);
    tableRow.append(dateTimeColumn);

    var tagColumn = $("<td>").attr("class", "value");
    
    if (data.instance.tag.id != null)
    {
        var tagLink = $("<a>").attr("href", '../tag/show/' + data.instance.tag.id)
        tagLink.html(data.instance.tag.deviceID);    
        tagColumn.append(tagLink);
    }
    else
    {
        tagColumn.html(data.instance.tag.codeMap.value + "-" + data.instance.tag.pingCode);    
    }
    
    tableRow.append(tagColumn);

    var typeColumn = $("<td>").attr("class", "value").html(data.instance.type.type);
    tableRow.append(typeColumn);

    var treatmentColumn = $("<td>").attr("class", "value").html(data.instance.treatmentType.type);
    tableRow.append(treatmentColumn);

    var commentsColumn = $("<td>").attr("class", "value").html(data.instance.comments);
    tableRow.append(commentsColumn);

    var lastRow = $("#surgeries_table_body > tr:last");
    lastRow.prev().before(tableRow);
}

function hiddenField(id, value)
{
    return $("<input>").attr("type", "hidden").attr("name", id).attr("id", id).attr("value", value);
}
