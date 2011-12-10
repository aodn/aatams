<tr class="prop">
    <td valign="top" class="name">
        <label for="filter.between.min.${propertyName}">${label} (min)</label>
    </td>
    <td valign="top" class="value">
    
        <g:if test="${filter}">
	       <g:set var="value" value="${filter['between.min.' + propertyName]}" />
        </g:if>
        <g:else>
            <g:set var="value" value="${minRange}" />
        </g:else>
    
        <g:datePicker name="filter.between.min.${propertyName}" value="${value}" precision="minute"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="filter.between.max.${propertyName}">${label} (max)</label>
    </td>
    <td valign="top" class="value">
    
        <g:if test="${filter}">
           <g:set var="value" value="${filter['between.max.' + propertyName]}" />
        </g:if>
        <g:else>
            <g:set var="value" value="${maxRange}" />
        </g:else>
    
        <g:datePicker name="filter.between.max.${propertyName}" value="${value}" precision="minute"/>
    </td>
</tr>
