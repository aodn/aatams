<!-- GSP template for a filter parameter representing a related entity being null (or not) -->
<tr class="prop">

    <td valign="top" class="name">
        <label for="filter.isNull.${propertyName}">${label}</label>
    </td>
    <td valign="top" class="value">
    
        <g:if test="${filter}">
            <g:set var="value" value="${filter['isNull.' + propertyName]}" />
        </g:if>
        <g:else>
            <g:set var="value" value="${false}" />
        </g:else>
        
        <g:checkBox name="filter.isNull.${propertyName}" 
                    value="${value}" />
  
    </td>
</tr>
