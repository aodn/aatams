$(function() {

    $('#dialog-form-add-organisation').dialog({
        autoOpen: false,
        height: 200,
        width: 600,
        modal: true,
        buttons: {
            'Add': function() 
            {
                var projectId = $("#projectId").val();
                var organisationId = $("#organisationId option:selected").val();
                var event = $("#id").val();
                
                $.post('/aatams/organisationProject/save', 
                       {'event.id':event, "project.id":projectId, "organisation.id":organisationId},
                       function(data) 
                       {
                           var item = $("<li>");
                           var link = $("<a>").attr("href", '../organisation/show/' + data.organisation.id).html(data.organisation.name);
                           item.append(link);
                           var lastListElement = $("#organisation_list > li:last")
                           
                           // Second last element is a <br>, last element is a link to "Add..."
                           lastListElement.prev().before(item)
                       }, 
                       'json');
                       
                $(this).dialog('close');
                
            },
            Cancel: function() {
                $(this).dialog('close');
            }

        }        
    });
    
    $('#add_organisation_to_project').click(function() {
        $('#dialog-form-add-organisation').dialog('open');
    });
});