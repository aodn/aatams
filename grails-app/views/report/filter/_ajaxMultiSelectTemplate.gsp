<tr id="${qualifiedParameterName + '.' + propertyName}" class="ajaxMultiSelectReportParameter prop">

    <g:hiddenField name="${qualifiedParameterName + '.' + propertyName}_lookupPath" value="${lookupPath}" />
    <g:hiddenField name="${qualifiedParameterName + '.' + propertyName}_textFieldId" value="${qualifiedParameterName}" />
    
    <td valign="top" class="name">
        <label for="${qualifiedParameterName}">${label}</label>
    </td>
    
    <g:hiddenField name="${qualifiedParameterName}" value="${propertyName}" />
    
    <td valign="top" class="value">
    
        <g:set var="value" value="${params[qualifiedParameterName]?.getAt(1)}" />
    
        <g:textField name="${qualifiedParameterName}" placeholder="autocomplete - start typing" value="${value}" />
    </td>
</tr>
