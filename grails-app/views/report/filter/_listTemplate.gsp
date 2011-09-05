<!-- GSP template for a filter parameter whose value is chosen from a select list -->
<tr class="prop">
    <td valign="top" class="name">
        <label for="filter.${propertyName}">${label}</label>
    </td>
    <td valign="top" class="value">
        <g:select name="filter.${propertyName}" from="${range}" noSelection="['':' - any - ']" />
    </td>
</tr>
