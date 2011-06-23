$(function() {

    $('#dialog-form-add-person').dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            'Add': function() 
            {
                var projectId = $("#projectId").val();
                var personId = $("#personId option:selected").val();
                var roleTypeId = $("#roleTypeId option:selected").val();
                var access = $("#access option:selected").val();
                var event = $("#id").val();
                
                $.post('../projectRole/save', 
                       {'event.id':event, 
                        "project.id":projectId, 
                        "person.id":personId,
                        "roleType.id":roleTypeId,
                        "access":access},
                       function(data) 
                       {
                           console.log(data);
                           
                           var tableRow = $("<tr>");
                           
                           var personColumn = $("<td>").attr("class", "value");
                           tableRow.append(personColumn);
                           var personLink = $("<a>").attr("href", '../person/show/' + data.person.id).html(data.person.name);
                           personColumn.append(personLink);
                           tableRow.append(personColumn);
                           
                           var roleColumn = $("<td>").attr("class", "value").html(data.roleType.displayName)
                           tableRow.append(roleColumn);

                           // TODO: should really be displaying "displayName", but it's not being marshalled in the JSON response
                           // (despite attempting to register a custom marshaller in BootStrap).
                           var accessColumn = $("<td>").attr("class", "value").html(data.access.name)
                           tableRow.append(accessColumn);
                           
                           var lastRow = $("#people_table_body > tr:last")
                           lastRow.prev().before(tableRow);
                       }, 
                       'json');
                       
                $(this).dialog('close');
                
            },
            Cancel: function() {
                $(this).dialog('close');
            }

        }        
    });
    
    $('#add_person_to_project').click(function() {
        $('#dialog-form-add-person').dialog('open');
    });
});