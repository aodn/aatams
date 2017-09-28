<g:if test="${!formats.isEmpty()}" >
    <div id="exportPane" class="exportPane">

        <g:if test="${name == 'detection'}" >

            <g:javascript src="exportDownloadDetections.js"/>

            <div class="smartmodal" id="acknowledgement" style="display:none">
            <g:render template="/acknowledgement"></g:render>
            </div>

        </g:if>
        <g:else>
            <g:javascript src="exportDownload.js"/>
        </g:else>

        <div class="buttons" style="padding-top: 2px; padding-bottom: 2px;">
    
            <span class="button">
              <label style="padding-left: 10px;">Export data as:</label> 
                <g:each in="${formats}" var="format">

                    <g:actionSubmit name="${format}"
                                    class="${format}"
                                    value="${format}"
                                    action="export" />
                </g:each>
            </span>
            
            <!--  Send a unique token to the server, which we can then check for to see if a report download has commenced
                  (the server will send back this token in a cookie).
             -->
            <g:hiddenField name="downloadTokenValue"  /> 
            
        </div>
    </div>
</g:if>