// Keep count of the number of surgeries (so we can index the array sent to
// controller when form is submitted).
var numSurgeries = 0;

$(function() 
{
    $('#dialog-form-add-surgery').dialog({
        autoOpen: false,
        height: 350,
        width: 750,
        modal: true,
        buttons: {
            'Add': function() 
            {
                var animalReleaseId = $("#animalReleaseId").val();
                var timestamp_year = $("#surgeryTimestamp_year").val();
                var timestamp_month = $("#surgeryTimestamp_month").val();
                var timestamp_day = $("#surgeryTimestamp_day").val();
                var timestamp_hour = $("#surgeryTimestamp_hour").val();
                var timestamp_minute = $("#surgeryTimestamp_minute").val();
                var timestamp_zone = $("#surgeryTimestamp_zone").val();
                var tagId = $("#tagId option:selected").val();
                var typeId = $("#surgeryTypeId option:selected").val();
                var treatmentTypeId = $("#treatmentTypeId option:selected").val();
                var comments = $("#surgeryComments").val();
                var projectId = $("#project\\.id option:selected").val();
                var event = $("#id").val();
                
                // 
                // Is this script being called from create or edit page (which
                // determines whether we store user entered data in hidden fields
                // or send an AJAX post respectively).
                //
                var mainForm = $("div.body > form")
                var isCreate = mainForm.attr("action") == "/aatams/animalRelease/save"
                 
                if (isCreate)
                {
                    // Create a JSON map which can be passed to the "updateSurgeryTable" function.
                    var dateTime = timestamp_day + "/" + 
                                   timestamp_month + "/" + 
                                   timestamp_year + " " + 
                                   timestamp_hour + ":" + 
                                   timestamp_minute + " " + 
                                   timestamp_zone;
                    var tag = {id:tagId, codeName:$("#tagId option:selected").text()};
                    var type = {type:$("#surgeryTypeId option:selected").text()};
                    var treatmentType = {type:$("#treatmentTypeId option:selected").text()};
                    var instance = {timestamp:dateTime, 
                                    tag:tag,
                                    type:type,
                                    treatmentType:treatmentType,
                                    comments:comments};
                    var data = {instance:instance};
                    console.log(data);
                    
                    updateSurgeryTable(data);
                    
                    // Add variables as hidden fields so that the parameters are
                    // sent to the controller on form submit.
                    var idPrefix = "surgery." + numSurgeries + ".";

                    mainForm.append(hiddenField(idPrefix + "timestamp_day", timestamp_day));
                    mainForm.append(hiddenField(idPrefix + "timestamp_month", timestamp_month));
                    mainForm.append(hiddenField(idPrefix + "timestamp_year", timestamp_year));
                    mainForm.append(hiddenField(idPrefix + "timestamp_hour", timestamp_hour));
                    mainForm.append(hiddenField(idPrefix + "timestamp_minute", timestamp_minute));
                    mainForm.append(hiddenField(idPrefix + "timestamp_zone", timestamp_zone));
                    mainForm.append(hiddenField(idPrefix + "tag.id", tagId));
                    mainForm.append(hiddenField(idPrefix + "type.id", typeId));
                    mainForm.append(hiddenField(idPrefix + "treatmentType.id", treatmentTypeId));
                    mainForm.append(hiddenField(idPrefix + "comments", comments));
                }
                else
                {
                    $.post('/aatams/surgery/save',
                    {
                       'event.id':event,
                       'projectId':projectId,
                       'release.id':animalReleaseId,
                       'timestamp_year':timestamp_year,
                       'timestamp_month':timestamp_month,
                       'timestamp_day':timestamp_day,
                       'timestamp_hour':timestamp_hour,
                       'timestamp_minute':timestamp_minute,
                       'timestamp_zone':timestamp_zone,
                       'tag.id':tagId,
                       'type.id':typeId,
                       'treatmentType.id':treatmentTypeId,
                       'comments':comments
                    },
                    function(data)
                    {
                       updateHeader(data);
                       updateSurgeryTable(data);

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
        var addSurgeryUrl = '/aatams/animalRelease/addSurgery';
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
            $('#dialog-form-add-surgery').dialog('open');
        });
    });
});

function updateSurgeryTable(data)
{
    var tableRow = $("<tr>");

    // Only show info colum in edit screen (i.e. when the surgery instance
    // has actually been persisted).
    if (data.instance.id)
    {
        var infoColumn = $("<td>").attr("class", "rowButton");
        var infoLink = $("<a>").attr("href", '/aatams/surgery/show/' + data.instance.id);
        infoLink.attr("class", "show");
        infoColumn.append(infoLink);
        tableRow.append(infoColumn);
    }
    
    var dateTimeColumn = $("<td>").attr("class", "value").html(data.instance.timestamp);
    tableRow.append(dateTimeColumn);

    var tagColumn = $("<td>").attr("class", "value");
    tableRow.append(tagColumn);
    var tagLink = $("<a>").attr("href", '../tag/show/' + data.instance.tag.id).html(data.instance.tag.codeName);
    tagColumn.append(tagLink);

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
