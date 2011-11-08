/* 
 * Utility functions available to other javascripts.
 */

/**
 * Update the header section of view (i.e. the info message on update/save of 
 * entity and/or an error message).
 */
function updateHeader(data)
{
    // Remove any existing flash message.
    $(".body > .message").remove();

    // Remove any existing errors.
    $(".body > .errors").remove();

    // Insert flash message (if there is one).
    if (data.message && data.message.message)
    {
        var heading = $(".body > h1")
        heading.after('<div class=\"message\">' + data.message.message + '</div>');
    }
    // Exception.
    else if (data.errors && data.errors.errors)
    {
       var insertAfterElement = $(".body > h1");

       data.errors.errors.forEach(function(item)
       {
           var errorElement = '<div class=\"errors\"><ul><li>' + item.message + '</li></ul>'; 
           insertAfterElement.after(errorElement);
           insertAfterElement = insertAfterElement.after();

       });
    }                               
    else if (data.errors && data.errors.localizedMessage)
    {
        var errorElement = '<div class=\"errors\"><ul><li>' + data.errors.localizedMessage + '</li></ul>'; 
        var heading = $(".body > h1");
        heading.after(errorElement);
    }
}

function escapeId(myid) 
{ 
	return '#' + myid.replace(/(:|\.)/g,'\\$1');
}

function isNumber(n) 
{
	return !isNaN(parseFloat(n)) && isFinite(n);
}