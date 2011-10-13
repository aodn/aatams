$(function() {

    $('#dialog-form-add-person').dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            'Create': function() 
            {
                var projectId = $("#projectId").val();
                var personId = $("#personId option:selected").val();
                var roleTypeId = $("#roleTypeId option:selected").val();
                var access = $("#access option:selected").val();
                var event = $("#id").val();
                
                $.post(contextPath + '/projectRole/save', 
                       {'event.id':event, 
                        "project.id":projectId, 
                        "person.id":personId,
                        "roleType.id":roleTypeId,
                        "access":access},
                       function(data) 
                       {
                           updateHeader(data);
                           
                           var tableRow = $("<tr>");
                           
                           var infoColumn = $("<td>").attr("class", "rowButton");
                           var infoLink = $("<a>").attr("href", contextPath + '/projectRole/show/' + data.instance.id);
                           infoLink.attr("class", "show");
                           infoColumn.append(infoLink);
                           tableRow.append(infoColumn);
                           
                           var deleteColumn = $("<td>").attr("class", "rowButton");
                           var deleteLink = $("<a>").attr("href", contextPath + '/projectRole/delete/' + data.instance.id + "?projectId=" + projectId);
                           deleteLink.attr("class", "delete");
                           deleteLink.click(function() { return confirm('Are you sure?'); });
                           deleteColumn.append(deleteLink);
                           tableRow.append(deleteColumn);

                           var personColumn = $("<td>").attr("class", "value");
                           tableRow.append(personColumn);
                           var personLink = $("<a>").attr("href", contextPath + '/person/show/' + data.instance.person.id).html(data.instance.person.name);
                           personColumn.append(personLink);
                           tableRow.append(personColumn);
                           
                           var roleColumn = $("<td>").attr("class", "value").html(data.instance.roleType.displayName)
                           tableRow.append(roleColumn);

                           var accessColumn = $("<td>").attr("class", "value").html(data.instance.access.displayStatus)
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