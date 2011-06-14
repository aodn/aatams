$(function() {

    $('#dialog-form').dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            'Add': function() {
               
            },
            Cancel: function() {
                $(this).dialog('close');
            }

        }        
    });
    
    $('#add_organisation_to_project').click(function() {
        $('#dialog-form').dialog('open');
    });
});