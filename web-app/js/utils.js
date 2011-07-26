/* 
 * Utility functions available to other javascripts.
 */

/**
 * Update the header section of view (i.e. the info message on update/save of 
 * entity and/or an error message).
 */
function updateHeader(data)
{
    console.log(data)

    // Remove any existing flash message.
    $(".body > .message").remove();

    // Remove any existing errors.
    $(".body > .errors").remove();

    // Insert flash message (if there is one).
    if (data.flash && data.flash.message)
    {
        var heading = $(".body > h1")
        heading.after('<div class=\"message\">' + data.flash.message + '</div>');
    }
    // Exception.
    else if (data.errors)
    {
       var insertAfterElement = $(".body > h1");

       data.errors.forEach(function(item)
       {
           var errorElement = '<div class=\"errors\"><ul><li>' + item.message + '</li></ul>'; 
           insertAfterElement.after(errorElement);
           insertAfterElement = insertAfterElement.after();

       });
    }                               
    else if (data.localizedMessage)
    {
        var errorElement = '<div class=\"errors\"><ul><li>' + data.localizedMessage + '</li></ul>'; 
        var heading = $(".body > h1");
        heading.after(errorElement);
    }
}
