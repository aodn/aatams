<g:hiddenField name="${qualifiedParameterName}.0" value="${propertyName}" />

<tr class="prop">

    <td valign="top" class="name">
        <label for="${qualifiedParameterName}">${label} (min)</label>
    </td>
    
    <td valign="top" class="value">

        <g:if test="${params[qualifiedParameterName + '.1']}">
            <g:set var="value" value="${params[qualifiedParameterName + '.1']}" />
        </g:if>
        <g:else>
            <g:set var="value" value="${minRange}" />
        </g:else>

        <g:datePicker name="${qualifiedParameterName}.1" value="${value}" precision="minute"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="${qualifiedParameterName}">${label} (max)</label>
    </td>
    <td valign="top" class="value">
    
        <g:if test="${params[qualifiedParameterName + '.2']}">
            <g:set var="value" value="${params[qualifiedParameterName + '.2']}" />
        </g:if>
        <g:else>
            <g:set var="value" value="${maxRange}" />
        </g:else>
    
        <g:datePicker name="${qualifiedParameterName}.2" value="${value}" precision="minute"/>
    </td>
</tr>
