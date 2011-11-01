<tr class="prop">
    <td valign="top" class="name">
        <label for="filter.between.min.${propertyName}">${label} (min)</label>
    </td>
    <td valign="top" class="value">
        <g:datePicker name="filter.between.min.${propertyName}" value="${minRange}" precision="minute"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="filter.between.max.${propertyName}">${label} (max)</label>
    </td>
    <td valign="top" class="value">
        <g:datePicker name="filter.between.max.${propertyName}" value="${maxRange}" precision="minute"/>
    </td>
</tr>
