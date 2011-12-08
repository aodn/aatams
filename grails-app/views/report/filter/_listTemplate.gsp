<!-- GSP template for a filter parameter whose value is chosen from a select list -->
<tr class="prop">

    <td valign="top" class="name">
        <label for="filter.eq.${propertyName}">${label}</label>
    </td>
    <td valign="top" class="value">
        
        <g:if test="${filter}">
            <g:set var="selectedValue" value="${filter['eq.' + propertyName]}" />
        </g:if>
        <g:else>
            <g:set var="selectedValue" value="" />
        </g:else>
        
        <g:select name="filter.eq.${propertyName}" 
                  from="${range}"
                  value="${selectedValue}"
                  noSelection="['':' - all - ']" />
  
    </td>
</tr>
