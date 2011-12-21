/* 
 * Update the list whenever one of the filter inputs changes.
 */
$(function()
{
    $(":input").change(function()
    {
        $("#filterForm").submit();
    });
});

