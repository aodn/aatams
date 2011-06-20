$(function() {

    $('#dialog-form-add-surgery').dialog({
        autoOpen: false,
        height: 390,
        width: 420,
        modal: true,
        buttons: {
            'Add': function() 
            {
                var animalReleaseId = $("#animalReleaseId").val();
                var timestamp_year = $("#surgeryTimestamp_year").val();
                var timestamp_month = $("#surgeryTimestamp_month").val();
                var timestamp_day = $("#surgeryTimestamp_day").val();
                var tagId = $("#tagId option:selected").val();
                var typeId = $("#surgeryTypeId option:selected").val();
                var sutures = $("#sutures").is(':checked');
                var treatmentTypeId = $("#treatmentTypeId option:selected").val();
                var surgeonId = $("#surgeonId option:selected").val();
                var comments = $("#surgeryComments").val();
                var event = $("#id").val();
                
                $.post('../surgery/save',
                       {
                           'event.id':event,
                           'release.id':animalReleaseId,
                           'timestamp_year':timestamp_year,
                           'timestamp_month':timestamp_month,
                           'timestamp_day':timestamp_day,
                           'tag.id':tagId,
                           'type.id':typeId,
                           'sutures':sutures,
                           'treatmentType.id':treatmentTypeId,
                           'surgeon.id':surgeonId,
                           'comments':comments
                       },
                       function(data)
                       {
                           var tableRow = $("<tr>");
                           
                           var dateTimeColumn = $("<td>").attr("class", "value");
                           tableRow.append(dateTimeColumn);
                           var surgeryLink = $("<a>").attr("href", '../surgery/show/' + data.id).html(data.timestamp);
                           dateTimeColumn.append(surgeryLink);
                           
                           var tagColumn = $("<td>").attr("class", "value");
                           tableRow.append(tagColumn);
                           var tagLink = $("<a>").attr("href", '../tag/show/' + data.tag.id).html(data.tag.codeName);
                           tagColumn.append(tagLink);
                           
                           var typeColumn = $("<td>").attr("class", "value").html(data.type.type);
                           tableRow.append(typeColumn);
                           
                           var suturesColumn = $("<td>").attr("class", "value").html(data.sutures.toString());
                           tableRow.append(suturesColumn);
                           
                           var treatmentColumn = $("<td>").attr("class", "value").html(data.treatmentType.type);
                           tableRow.append(treatmentColumn);
                           
                           var personColumn = $("<td>").attr("class", "value");
                           tableRow.append(personColumn);
                           var personLink = $("<a>").attr("href", '../person/show/' + data.surgeon.id).html(data.surgeon.name);
                           personColumn.append(personLink);
                           
                           var commentsColumn = $("<td>").attr("class", "value").html(data.comments);
                           tableRow.append(commentsColumn);
                           
                           var lastRow = $("#surgeries_table_body > tr:last");
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
    
    $('#add_surgery_to_animal_release').click(function() {
        $('#dialog-form-add-surgery').dialog('open');
    });
});