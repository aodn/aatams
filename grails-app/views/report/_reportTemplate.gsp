<div class="report">
  <g:form >
    <g:if test="${showFilter}">
      <div class="dialog">
        <g:reportFilter name="${name}"/>
      </div>
    </g:if>

    <div class="buttons">
      
      <g:hiddenField name="_file" value="${jrxmlFilename}"/>
      <g:hiddenField name="_name" value="${name}"/>
      <g:hiddenField name="_type" value="${type}"/>
      
      <span class="button">
        <g:each in="${params.list('formats')}" var="format">
          
          <g:actionSubmit name="${format}" 
                          class="${format}" 
                          value="${format}"
                          action="execute" />
        </g:each>
      </span>
    </div>
  </g:form>  
</div>
