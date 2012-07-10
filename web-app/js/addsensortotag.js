$(function() {

    $('#dialog-form-add-sensor').dialog({
        autoOpen: false,
        height: 375,
        width: 500,
        modal: true,
        buttons: {
            'Create': function() 
            {
                var event = $("#id").val();
                var tagId = $("#id").val();
                var tagSerialNumber = $("#serialNumber").val();
                var transmitterTypeId = $("#transmitterTypeId option:selected").val();
                var pingCode = $("#sensorPingCode").val();
                var slope = $("#slope").val();
                var intercept = $("#intercept").val();
                var unit = $("#unit").val();
                var projectId = $("#project\\.id option:selected").val();
                
                $.post(contextPath + '/sensor/save', 
                       {'event.id':event,
                    	'projectId':projectId,
                        'tag.project.id':projectId,
                        'tag.serialNumber':tagSerialNumber,
                        'tag.id':tagId,
                        'transmitterType.id':transmitterTypeId,
                        'pingCode':pingCode,
                        'slope':slope,
                        'intercept':intercept,
                        'unit':unit,
                        'responseType': 'json'},
                       function(data) 
                       {
                        	updateHeader(data);
                        	
							$('form').each(function () 
							{
								$(this).data('initialForm', $(this).serialize());
							});
							
							if (data.errors)
							{
								
							}
							else
							{	
								window.location = contextPath + '/tag/show/' + data.tag.id;
							}
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