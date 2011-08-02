$(function() {

    $('#dialog-form-add-measurement').dialog({
        autoOpen: false,
        height: 390,
        width: 420,
        modal: true,
        buttons: {
            'Add': function() 
            {
                
                var animalReleaseId = $("#animalReleaseId").val();
                var typeId = $("#measurementTypeId option:selected").val();
                var value = $("#value").val();
                var unitId = $("#unitId option:selected").val();
                var estimate = $("#estimate").is(':checked');
                var comments = $("#measurementComments").val();
                var projectId = $("#project\\.id option:selected").val();
                
                var event = $("#id").val();

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
                           
                           var tableRow = $("<tr>");
                           
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
                           
                       },
                       'json');
                       
                $(this).dialog('close');
                
            },
            Cancel: function() {
                $(this).dialog('close');
            }

        }        
    });
    
    $('#add_measurement_to_animal_release').click(function() {
        $('#dialog-form-add-measurement').dialog('open');
    });
});