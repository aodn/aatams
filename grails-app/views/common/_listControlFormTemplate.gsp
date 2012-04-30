<div class="listControlForm ">

    <g:form name="listControlForm" action="list" >
    
        <g:listFilter name="${name}" />
        
        <g:exportPane name="${name}" formats="${formats}" jrxmlFilename="${jrxmlFilename}" type="${type}" />
    
    </g:form>

</div>
