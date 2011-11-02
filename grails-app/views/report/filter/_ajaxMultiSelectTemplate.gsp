<tr id="${propertyName}" class="ajaxMultiSelectReportParameter prop">
    <g:hiddenField name="${propertyName}_lookupPath" value="${lookupPath}" />
    <g:hiddenField name="${propertyName}_textFieldId" value="filter.in.${propertyName}" />
    
    <td valign="top" class="name">
        <label for="filter.in.${propertyName}">${label}</label>
    </td>
    <td valign="top" class="value">
        <g:textField name="filter.in.${propertyName}" placeholder="autocomplete - start typing"/>
    </td>
</tr>
