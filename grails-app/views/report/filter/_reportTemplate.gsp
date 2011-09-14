<div class="report">
  <g:form action="execute">
    <div class="dialog">
      <g:reportFilter name="${name}"/>
    </div>

    <div class="buttons">
      
      <g:hiddenField name="_file" value="${jrxmlFilename}"/>
      <g:hiddenField name="_name" value="${name}"/>
      
      <span class="button">
        <g:each in="${formats}" var="format">
          
          <g:submitButton name="${format}" 
                          class="${format}" 
                          value="${format}"/>
        </g:each>
      </span>
    </div>
  </g:form>  
</div>
