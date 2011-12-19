<!-- GSP template for a filter parameter representing a related entity being null (or not) -->
<tr class="prop">

    <td valign="top" class="name">
        <label for="filter.isNull.${propertyName}">${label}</label>
    </td>
    <td valign="top" class="value">
        
        <g:checkBox name="filter.isNull.${propertyName}" 
                    value="${selectedValue}" />
  
    </td>
</tr>
