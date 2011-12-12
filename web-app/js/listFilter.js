$(function()
{
	$(".reportFilter").find(":input").change(function()
	{
		// Hack, allow time for the textfield to be updated.
		if ($(this).attr("class") == "ui-autocomplete-input")
		{
			window.setTimeout(function ()
			{
				$("#listFilterForm").submit();
			}, 100);
		}
		else
		{
			$("#listFilterForm").submit();
		}
	});
});
