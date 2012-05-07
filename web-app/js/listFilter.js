$(function()
{
	function submit()
	{
		$(".body").block({ message: blockUIConfig.defaultMessage });
		$("#listControlForm").submit();
	}
	
    function split( val ) {
        return val.split( /,\s*/ );
    }
	
	$(".reportFilter").find(".ui-autocomplete-input").autocomplete({
        // Override the version in ajaxMultiSelectReportParameter.js, to also submit the form
        // (because we don't want to do it in change() below, see: http://redmine.emii.org.au/issues/937
        select: function( event, ui ) 
        {
            var terms = split( this.value );
            // remove the current input
            terms.pop();
            // add the selected item
            terms.push( ui.item.value );
            // add placeholder to get the comma-and-space at the end
            terms.push( "" );
            this.value = terms.join( ", " );
            
            submit();
            
            return false;
        },
	    change: function(event, ui) 
	    {
			submit();
	    	
	        return false;
	    }
	});
	
	$(".reportFilter").find(":input").not(".ui-autocomplete-input").change(function()
	{
		submit();
	});
});
