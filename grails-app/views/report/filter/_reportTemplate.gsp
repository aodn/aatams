<div class="report">
  
  <div class="dialog">
    <g:reportFilter name="${name}"/>
  </div>

  <div class="buttons">
    <g:jasperReport jasper="${jrxmlFilename}"
                    format="${format}"
                    controller="${controller}"
                    action="${action}"
                    reportName="${name}"
                    language="groovy" />
  </div>
</div>
