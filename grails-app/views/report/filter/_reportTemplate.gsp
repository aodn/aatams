<div class="report">
  <g:form action="execute">
    <div class="dialog">
      <g:reportFilter name="${name}"/>
    </div>

<%--    
    <div class="buttons">
      <g:jasperReport jasper="${jrxmlFilename}"
                      format="${format}"
                      controller="${controller}"
                      action="${action}"
                      reportName="${name}"
                      language="groovy" />
    </div>
--%> 
    <%-- _format:PDF, _name:, _file:receiverList, action:execute, controller:report --%>
    <div class="buttons">
      <g:hiddenField name="_format" value="${format}"/>
      <g:hiddenField name="_file" value="${jrxmlFilename}"/>
      <g:hiddenField name="_name" value="${name}"/>
      
      <span class="button">
        <g:submitButton name="pdf" 
                        class="pdf" 
                        value="${message(code: 'default.button.report.PDF.label', default: 'PDF')}" />
      </span>
    </div>
  </g:form>  
</div>
