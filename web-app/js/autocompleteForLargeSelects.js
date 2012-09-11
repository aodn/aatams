$(function() {
   
    var maxOptions = 10;
    
    $("select").each(function() {
       
        if ($(this).children().size() > maxOptions) {
            
            $(this).combobox();
        }
    });
});