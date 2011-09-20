// Keep count of the number of measurements (so we can index the array sent to
// controller when form is submitted).
var numMeasurements = 0;

$(function() {

    $('#dialog-form-add-measurement').dialog({
        autoOpen: false,
        height: 390,
        width: 420,
        modal: true,
        buttons: {
            'Create': function() 
            {
                
                var animalReleaseId = $("#id").val();
                var typeId = $("#measurementTypeId option:selected").val();
                var value = $("#value").val();
                var unitId = $("#unitId option:selected").val();
                var estimate = $("#estimate").is(':checked');
                var comments = $("#measurementComments").val();
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
                    var type = {type:$("#measurementTypeId option:selected").text()};
                    var unit = {unit:$("#unitId option:selected").text()};
                    
                    var instance = {type:type,
                                    value:value,
                                    unit:unit,
                                    estimate:estimate,
                                    comments:comments}
                    var data = {instance:instance};
                    
                    var idPrefix = "measurement." + numMeasurements + ".";
                    updateMeasurementTable(data, projectId, idPrefix);
                    
                    // Add variables as hidden fields so that the parameters are
                    // sent to the controller on form submit.

                    mainForm.append(hiddenField(idPrefix + "type.id", typeId));
                    mainForm.append(hiddenField(idPrefix + "value", value));
                    mainForm.append(hiddenField(idPrefix + "unit.id", unitId));
                    mainForm.append(hiddenField(idPrefix + "estimate", estimate));
                    mainForm.append(hiddenField(idPrefix + "comments", comments));
                }
                else
                {
                    $.post('/aatams/animalMeasurement/save',
                    {
                       'event.id':event,
                       'projectId':projectId,
                       'release.id':animalReleaseId,
                       'type.id':typeId,
                       'value':value,
                       'unit.id':unitId,
                       'estimate':estimate,
                       'comments':comments
                    },
                    function(data)
                    {
                       updateHeader(data);
                       updateMeasurementTable(data, projectId, null);
                    },
                    'json');
                }
                   
                numMeasurements++;
                $(this).dialog('close');
                
            },
            Cancel: function() {
                $(this).dialog('close');
            }

        }        
    });
    
    $('#add_measurement_to_animal_release').click(function() 
    {
        var addMeasurementUrl = '/aatams/animalRelease/addMeasurement';
        var animalReleaseId = $("#animalReleaseId").val();
        if (animalReleaseId)
        {
            addMeasurementUrl += "/" + animalReleaseId
        }
        
        var projectId = $("#project\\.id option:selected").val();
        $('#dialog-form-add-measurement').load(addMeasurementUrl, 
                                               {projectId:projectId}, 
                                               function()
        {
            $('#dialog-form-add-measurement').dialog('open');
        });
    });
});

function isEditView(data)
{
    return (data.instance.id)
}

function updateMeasurementTable(data, projectId, idPrefix)
{
    var tableRow = $("<tr>");

    // Only show info and delete colums in edit screen (i.e. when the measurement instance
    // has actually been persisted).
    if (isEditView(data))
    {
        var infoColumn = $("<td>").attr("class", "rowButton");
        var infoLink = $("<a>").attr("href", '/aatams/animalMeasurement/show/' + data.instance.id);
        infoLink.attr("class", "show");
        infoColumn.append(infoLink);
        tableRow.append(infoColumn);

        var deleteColumn = $("<td>").attr("class", "rowButton");
        var deleteLink = $("<a>").attr("href", '/aatams/animalMeasurement/delete/' + data.instance.id + "?projectId=" + projectId);
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
                $("[id*=" + idPrefix + "]").remove();
            }
                
        });
        deleteColumn.append(deleteLink);
        tableRow.append(deleteColumn);
    }
    
    var typeColumn = $("<td>").attr("class", "value").html(data.instance.type.type);
    tableRow.append(typeColumn);

    var valueColumn = $("<td>").attr("class", "value").html(data.instance.value);
    tableRow.append(valueColumn);

    var unitsColumn = $("<td>").attr("class", "value").html(data.instance.unit.unit);
    tableRow.append(unitsColumn);

    var estimateColumn = $("<td>").attr("class", "value").html(data.instance.estimate.toString());
    tableRow.append(estimateColumn);

    var commentsColumn = $("<td>").attr("class", "value").html(data.instance.comments);
    tableRow.append(commentsColumn);

    var lastRow = $("#measurements_table_body > tr:last");
    lastRow.prev().before(tableRow);
}

function hiddenField(id, value)
{
    return $("<input>").attr("type", "hidden").attr("name", id).attr("id", id).attr("value", value);
}
