<!-- GSP template for a filter parameter representing a related entity being null (or not) -->
<tr class="prop">

    <td valign="top" class="name">
        <label for="${qualifiedParameterName}">${label}</label>
    </td>
    <td valign="top" class="value">
    
        <g:set var="selectedValue" value="${params[qualifiedParameterName]?.getAt(1)}" />
 
        <g:checkBox name="${qualifiedParameterName}" 
                    value="${value}" />
  
    </td>
</tr>
