$(function() {

    $('#dialog-form-change-password').dialog({
        autoOpen: false,
        height: 250,
        width: 350,
        modal: true,
        buttons: {
            'Update': function() 
            {
                var personId = $("#personId").val();
                var password = $("#password").val();
                var passwordConfirm = $("#passwordConfirm").val();
                
                console.log("password: " + $("#password"))
                var event = $("#id").val();
                
                $.post('/aatams/person/updatePassword', 
                       {'event.id':event, 
                        "id":personId, 
                        "password":password, 
                        "passwordConfirm":passwordConfirm},

                       function(data) 
                       {
//                           var item = $("<li>");
//                           var link = $("<a>").attr("href", '../organisation/show/' + data.organisation.id).html(data.organisation.name);
//                           item.append(link);
//                           var lastListElement = $("#organisation_list > li:last")
//                           
//                           // Second last element is a <br>, last element is a link to "Add..."
//                           lastListElement.prev().before(item)
                       }, 
                       'json');
                       
                $(this).dialog('close');
                
            },
            Cancel: function() {
                $(this).dialog('close');
            }

        }        
    });
    
    $('#change_password').click(function() 
    {
        $('#dialog-form-change-password').dialog('open');
    });
});