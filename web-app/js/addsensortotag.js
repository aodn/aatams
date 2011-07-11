$(function() {

    $('#dialog-form-add-sensor').dialog({
        autoOpen: false,
        height: 350,
        width: 350,
        modal: true,
        buttons: {
            'Add': function() 
            {
                var event = $("#id").val();
                var tagId = $("#id").val();
                var transmitterTypeId = $("#transmitterTypeId option:selected").val();
                var pingCode = $("#sensorPingCode").val();
                var slope = $("#slope").val();
                var intercept = $("#intercept").val();
                var unit = $("#unit").val();
                var status = $("#statusId option:selected").val();
                
                $.post('/aatams/sensor/save', 
                       {'event.id':event,
                        'tag.id':tagId,
                        'transmitterType.id':transmitterTypeId,
                        'pingCode':pingCode,
                        'slope':slope,
                        'intercept':intercept,
                        'unit':unit,
                        'status.id':status},
                       function(data) 
                       {
                           console.log(data);
                           
                           var tableRow = $("<tr>");
                           
                           var tagTypeColumn = $("<td>").attr("class", "value");
                           tableRow.append(tagTypeColumn);
                           var sensorLink = $("<a>").attr("href", '../sensor/show/' + data.id).html(data.transmitterType);
                           tagTypeColumn.append(sensorLink);
                           
                           var pingCodeColumn = $("<td>").attr("class", "value").html(data.pingCode);
                           tableRow.append(pingCodeColumn);

                           var slopeColumn = $("<td>").attr("class", "value").html(data.slope);
                           tableRow.append(slopeColumn);
                           
                           var interceptColumn = $("<td>").attr("class", "value").html(data.intercept);
                           tableRow.append(interceptColumn);
                           
                           var unitColumn = $("<td>").attr("class", "value").html(data.unit);
                           tableRow.append(unitColumn);
                           
                           var statusColumn = $("<td>").attr("class", "value").html(data.status.status);
                           tableRow.append(statusColumn);
                           
                           var lastRow = $("#sensor_table_body > tr:last");
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
    
    $('#add_sensor_to_tag').click(function() {
        $('#dialog-form-add-sensor').dialog('open');
    });
});