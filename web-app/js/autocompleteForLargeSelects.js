$(function() {
   
    $("select").each(function() {
        
        // Except certain inputs...
        if (!isExcludedInput(this)) {
            $(this).combobox();
        }
    });
});

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

String.prototype.startsWith = function (str) {
    return this.indexOf(str) == 0;
};

function isExcludedInput(element) {
    
    var isExcluded = false;
    
    // Exclude dates...
    $(["day", "month", "year", "hour", "minute", "second", "editNorthSouth", "editEastWest"]).each(function(index, elementId) {
        
        if ($(element).attr("id").endsWith("_" + elementId)) {
            isExcluded = true;
        }
    })
    
    // ... and filters.
    $(["filter"]).each(function(index, elementId) {
        
        if ($(element).attr("id").startsWith(elementId)) {
            isExcluded = true;
        }
    })
    
    return isExcluded;
}