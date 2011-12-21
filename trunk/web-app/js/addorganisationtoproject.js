$(function() {

    $('#dialog-form-add-organisation').dialog({
        autoOpen: false,
        height: 200,
        width: 600,
        modal: true,
        buttons: {
            'Create': function() 
            {
                var projectId = $("#projectId").val();
                var organisationId = $("#organisationId option:selected").val();
                var event = $("#id").val();
                
                $.post(contextPath + '/organisationProject/save', 
                       {'event.id':event, "project.id":projectId, "organisation.id":organisationId},
                       function(data) 
                       {
                           updateHeader(data);
                           
                           var tableRow = $("<tr>");
                           
                           var infoColumn = $("<td>").attr("class", "rowButton");
                           var infoLink = $("<a>").attr("href", contextPath + '/organisationProject/show/' + data.instance.id);
                           infoLink.attr("class", "show");
                           infoColumn.append(infoLink);
                           tableRow.append(infoColumn);

                           var deleteColumn = $("<td>").attr("class", "rowButton");
                           var deleteLink = $("<a>").attr("href", contextPath + '/organisationProject/delete/' + data.instance.id + "?projectId=" + projectId);
                           deleteLink.attr("class", "delete");
                           deleteLink.click(function() { return confirm('Are you sure?'); });
                           deleteColumn.append(deleteLink);
                           tableRow.append(deleteColumn);

                           var orgColumn = $("<td>").attr("class", "value");
                           var orgLink = $("<a>").attr("href", contextPath + '/organisation/show/' + data.instance.organisation.id).html(data.instance.organisation.name);
                           orgColumn.append(orgLink);
                           tableRow.append(orgColumn);
                           
                           var lastRow = $("#organisation_table_body > tr:last")
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
    
    $('#add_organisation_to_project').click(function() {
        $('#dialog-form-add-organisation').dialog('open');
    });
});