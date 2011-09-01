$(function() {

    $('#dialog-form-add-sensor').dialog({
        autoOpen: false,
        height: 375,
        width: 350,
        modal: true,
        buttons: {
            'Add': function() 
            {
                var event = $("#id").val();
                var tagId = $("#id").val();
                var transmitterTypeId = $("#transmitterTypeId option:selected").val();
                var codeMap = $("#sensorCodeMap").val();
                var pingCode = $("#sensorPingCode").val();
                var slope = $("#slope").val();
                var intercept = $("#intercept").val();
                var unit = $("#unit").val();
                var status = $("#statusId option:selected").val();
                var projectId = $("#project\\.id option:selected").val();
                
                $.post('/aatams/sensor/save', 
                       {'event.id':event,
                        'projectId':projectId,
                        'tag.id':tagId,
                        'transmitterType.id':transmitterTypeId,
                        'codeMap':codeMap,
                        'pingCode':pingCode,
                        'slope':slope,
                        'intercept':intercept,
                        'unit':unit,
                        'status.id':status},
                       function(data) 
                       {
                           updateHeader(data);
                           
                           var tableRow = $("<tr>");
                           
                           var infoColumn = $("<td>").attr("class", "rowButton");
                           var infoLink = $("<a>").attr("href", '/aatams/sensor/show/' + data.instance.id);
                           infoLink.attr("class", "show");
                           infoColumn.append(infoLink);
                           tableRow.append(infoColumn);

                           var deleteColumn = $("<td>").attr("class", "rowButton");
                           var deleteLink = $("<a>").attr("href", '/aatams/sensor/delete/' + data.instance.id + "?projectId=" + projectId);
                           deleteLink.attr("class", "delete");
                           deleteLink.click(function() { return confirm('Are you sure?'); });
                           deleteColumn.append(deleteLink);
                           tableRow.append(deleteColumn);

                           var tagTypeColumn = $("<td>").attr("class", "value");
                           tableRow.append(tagTypeColumn);
                           var sensorLink = $("<a>").attr("href", '../sensor/show/' + data.instance.id).html(data.instance.transmitterType.transmitterTypeName);
                           tagTypeColumn.append(sensorLink);
                           
                           var codeMapColumn = $("<td>").attr("class", "value").html(data.instance.codeMap);
                           tableRow.append(codeMapColumn);

                           var pingCodeColumn = $("<td>").attr("class", "value").html(data.instance.pingCode);
                           tableRow.append(pingCodeColumn);

                           var slopeColumn = $("<td>").attr("class", "value").html(data.instance.slope);
                           tableRow.append(slopeColumn);
                           
                           var interceptColumn = $("<td>").attr("class", "value").html(data.instance.intercept);
                           tableRow.append(interceptColumn);
                           
                           var unitColumn = $("<td>").attr("class", "value").html(data.instance.unit);
                           tableRow.append(unitColumn);
                           
                           var statusColumn = $("<td>").attr("class", "value").html(data.instance.status.status);
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