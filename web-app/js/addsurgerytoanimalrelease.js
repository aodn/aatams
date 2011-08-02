$(function() {

    $('#dialog-form-add-surgery').dialog({
        autoOpen: false,
        height: 350,
        width: 700,
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
                           
                           var tableRow = $("<tr>");
                           
                           var dateTimeColumn = $("<td>").attr("class", "value");
                           tableRow.append(dateTimeColumn);
                           
                           var surgeryLink = $("<a>").attr("href", '../surgery/show/' + data.instance.id).html(data.instance.timestamp);
                           dateTimeColumn.append(surgeryLink);
                           
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
