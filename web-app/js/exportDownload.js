$(document).ready(function () 
{
	if ($('#totalMatches').val() > 300000) 
	{
		$('input.KMZ, input.KML').prop('disabled', true);
	}
	
	$('#listControlForm').find(":submit").click(function(event) 
	{
		// Don't block for filter updates, only export downloads.
		if (this.name === "_action_export")
		{
			blockUIForDownload();
		}	
    });
});

var fileDownloadCheckTimer;

function blockUIForDownload() 
{
    var token = new Date().getTime(); //use the current timestamp as the token value
    $('#downloadTokenValue').val(token);
    
	$.blockUI({ 
		message: blockUIConfig.defaultMessage
	});
	
    fileDownloadCheckTimer = window.setInterval(function () 
    {
    	var cookieValue = $.cookie('fileDownloadToken');
    	if (cookieValue == token)
    	{
    		finishDownload();
    	}
    }, 1000);
}

function finishDownload() 
{
	window.clearInterval(fileDownloadCheckTimer);
	$.cookie('fileDownloadToken', null); //clears this cookie value
	$.unblockUI();
}

  