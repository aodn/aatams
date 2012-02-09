<!-- GSP template for a filter parameter whose value is chosen from a select list -->
<tr class="prop">
    
    <td valign="top" class="name">
        <label for="${qualifiedParameterName}">${label}</label>
    </td>
    
    <g:hiddenField name="${qualifiedParameterName}" value="${propertyName}" />
    
    <td valign="top" class="value">

        <g:set var="selectedValue" value="${params[qualifiedParameterName]?.getAt(1)}" />

        <g:select name="${qualifiedParameterName}" 
                  from="${range}"
                  value="${selectedValue}"
                  noSelection="['':' - all - ']" />
  
    </td>
</tr>
