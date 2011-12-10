<tr id="${propertyName}" class="ajaxMultiSelectReportParameter prop">
    <g:hiddenField name="${propertyName}_lookupPath" value="${lookupPath}" />
    <g:hiddenField name="${propertyName}_textFieldId" value="filter.in.${propertyName}" />
    
    <td valign="top" class="name">
        <label for="filter.in.${propertyName}">${label}</label>
    </td>
    <td valign="top" class="value">
    
        <g:if test="${filter}">
            <g:set var="value" value="${filter['in.' + propertyName]}" />
        </g:if>
        <g:else>
            <g:set var="value" value="" />
        </g:else>
    
        <g:textField name="filter.in.${propertyName}" placeholder="autocomplete - start typing" value="${value}" />
    </td>
</tr>
