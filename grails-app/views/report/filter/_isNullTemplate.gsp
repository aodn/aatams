<!-- GSP template for a filter parameter representing a related entity being null (or not) -->
<tr class="prop">

    <td valign="top" class="name">
        <label for="${qualifiedParameterName}">${label}</label>
    </td>
    <td valign="top" class="value">
    
      <g:if test="${params[qualifiedParameterName] == 'on'}">
        <g:set var="value" value="${true}" />
      </g:if>

      <g:checkBox name="${qualifiedParameterName}" 
                  value="${value}" />
    </td>
</tr>
