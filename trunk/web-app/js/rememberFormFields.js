/**
 * Form fields (which are of the appropriate) class will have their values stored, 
 * and this value will be used as default value in subsequent visits to that form.
 */
$(function()
{
	$(".remember").each(function()
	{
		restoreValue($(this));
		
		$(this).change(function()
		{
			storeValue($(this));
		});		
	});
});

function restoreValue(input)
{
	var name = input.attr("name");
	
	if ($.cookie(name))
	{
		input.val($.cookie(name));
	}
};

function storeValue(input)
{
	var name = input.attr("name");
	$.cookie(name, input.val(), { path: '/', expires: 30 });
};
