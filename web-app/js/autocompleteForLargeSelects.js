$(function() {
   
    $("select").each(function() {
        
        // Except for date/time fields...
        if (!isDateField(this)) {
            $(this).combobox();
        }
    });
});

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

function isDateField(element) {
    
    var isDate = false;
    
    $(["day", "month", "year", "hour", "minute", "second", "editNorthSouth", "editEastWest"]).each(function(index, elementId) {
        
        if ($(element).attr("id").endsWith("_" + elementId)) {
            isDate = true;
        }
    })
    
    return isDate;
}