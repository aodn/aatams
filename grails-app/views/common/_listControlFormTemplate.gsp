<div class="listControlForm ">

    <g:form name="listControlForm" action="list" >

        <g:hiddenField name="totalMatches" value="${total}"/>
        
        <g:listFilter name="${name}" />
        
        <g:exportPane name="${name}" formats="${formats}" subFormats="${subFormats}" />
    
    </g:form>

</div>
