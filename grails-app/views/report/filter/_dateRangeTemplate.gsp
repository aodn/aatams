<tr class="prop">

    <td valign="top" class="name">
        <label for="${qualifiedParameterName}">${label} (min)</label>
    </td>
    <td valign="top" class="value">
    
        <g:if test="${params.filter}">
            <g:set var="value" value="${params[qualifiedParameterName]?.getAt(1)}" />
        </g:if>
        <g:else>
            <g:set var="value" value="${minRange}" />
        </g:else>
    
        <g:datePicker name="${qualifiedParameterName}" value="${value}" precision="minute"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="${qualifiedParameterName}">${label} (max)</label>
    </td>
    <td valign="top" class="value">
    
        <g:if test="${params.filter}">
            <g:set var="value" value="${params[qualifiedParameterName]?.getAt(1)}" />
        </g:if>
        <g:else>
            <g:set var="value" value="${maxRange}" />
        </g:else>
    
        <g:datePicker name="${qualifiedParameterName}" value="${value}" precision="minute"/>
    </td>
</tr>
