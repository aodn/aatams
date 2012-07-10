$(function() {
	
	if ($("#status").val() === "PROCESSING") {
			
		$("#progressbar").progressbar({ value: 0 });
		
	    var progressIntervalID = window.setInterval(function() {
	    	
	    	var id = $("#receiverDownloadId").val();
	    			
	    	// Get the status.
	    	$.getJSON(contextPath + '/receiverDownloadFile/status',
	    		      { id: id },
	    		      function(data) {
	    			    
	    		    	  $("#status").val(data.status.name);
	    		    	  $("#progressbar").progressbar({ value: data.percentComplete });
	    			  
	    		    	  if (data.status.name != "PROCESSING") {
	    		    		  window.clearInterval(progressIntervalID);
	
	    		    		  location.reload();
	    		    	  }
	    		      });
	    }, 1000);
	}
});
