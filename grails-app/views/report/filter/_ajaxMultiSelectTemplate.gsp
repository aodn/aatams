<!--  
<style>
.ui-autocomplete-loading { background: white url('images/ui-anim_basic_16x16.gif') right center no-repeat; }
</style>
-->
<script>
$(function() {
    function split( val ) {
        return val.split( /,\s*/ );
    }
    function extractLast( term ) {
        return split( term ).pop();
    }

    $(escapeId($("#textFieldId").val()))
         // don't navigate away from the field on tab when selecting an item
        .bind( "keydown", function( event ) {
            if ( event.keyCode === $.ui.keyCode.TAB &&
                    $( this ).data( "autocomplete" ).menu.active ) {
                event.preventDefault();
            }
        })
        .autocomplete({
            source: function( request, response ) {

                $.getJSON( contextPath + $("#lookupPath").val(), {
                    term: extractLast( request.term )
                }, response );
            },
            search: function() {
                // custom minLength
                var term = extractLast( this.value );
                if ( term.length < 2 ) {
                    return false;
                }
            },
            focus: function() {
                // prevent value inserted on focus
                return false;
            },
            select: function( event, ui ) {
                var terms = split( this.value );
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push( ui.item.value );
                // add placeholder to get the comma-and-space at the end
                terms.push( "" );
                this.value = terms.join( ", " );
                return false;
            }
        });
});
</script>

<tr class="prop">
    <g:hiddenField name="lookupPath" value="${lookupPath}" />
    <g:hiddenField name="textFieldId" value="filter.in.${propertyName}" />
    
    <td valign="top" class="name">
        <label for="filter.in.${propertyName}">${label}</label>
    </td>
    <td valign="top" class="value">
        <g:textField name="filter.in.${propertyName}" placeholder="autocomplete - start typing"/>
    </td>
</tr>
