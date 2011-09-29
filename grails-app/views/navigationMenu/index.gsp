<!-- 
     This file deliberately does not have header/body elements, as it's being inserted inside the elements
     from elsewhere.
-->

    <div id="controllerList" >

      <h3>${message(code: 'navigationMenu.section.background.label', default: 'Background Data')} </h3>
      
      <ul>
          <li class="backgroundDataControllers">
            <g:link controller="organisation">Organisations</g:link>
            
            <shiro:user>
              <span class="inline">
                <g:link controller="organisation" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
              </span>
            </shiro:user>
            
          </li>
          
          <li class="backgroundDataControllers">
            <g:link controller="project">Projects</g:link>
            
            <shiro:user>
              <span class="inline">
                <g:link controller="project" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
              </span>
            </shiro:user>
            
          </li>
          <li class="backgroundDataControllers">
            <g:link controller="person">People</g:link>
            
            <shiro:hasPermission permission="personWriteAny">
              <span class="inline">
                <g:link controller="person" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
              </span>
            </shiro:hasPermission>
            
          </li>
      </ul>

      <h3>${message(code: 'navigationMenu.section.installation.label', default: 'Location Data')} </h3>
      <ul>
        <g:each var="c" in="${installationDataControllers}">
          
          <li class="installationDataControllers">
            <g:link controller="${c.key}">${c.value}</g:link>
            
            <g:if test="${c.key == 'receiver'}">
              <shiro:hasPermission permission="receiverCreate">
                <span class="inline">
                  <g:link controller="${c.key}" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
                </span>
              </shiro:hasPermission>
            </g:if>
            <g:else>
              <shiro:hasPermission permission="projectWriteAny">
                <span class="inline">
                  <g:link controller="${c.key}" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
                </span>
              </shiro:hasPermission>
            </g:else>
          </li>
        </g:each>
      </ul>
     
      <h3>${message(code: 'navigationMenu.section.field.label', default: 'Field Data')} </h3>
      <ul>
        <g:each var="c" in="${fieldDataControllers}">
          
          <li class="fieldDataControllers">
            <g:link controller="${c.controllerName}">${c.label}</g:link>

            <g:if test="${c.canCreateNew}">
              <span class="inline">
                <g:link controller="${c.controllerName}" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
              </span>
            </g:if>
          </li>
        </g:each>
      </ul>
      
      <h3>${message(code: 'navigationMenu.section.report.label', default: 'Reports')} </h3>
      <ul>
        <g:each var="c" in="${reportControllers}">
          
          <li class="reportControllers">
            <g:link controller="${c.key}">${c.value}</g:link>
            <span class="inline">
              <g:link controller="${c.key}" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
            </span>
          </li>
        </g:each>
      </ul>
      
      <h3>${message(code: 'navigationMenu.section.fieldSheets.label', default: 'Field Sheets')} </h3>
      <ul/></ul>
    
      <h3>${message(code: 'navigationMenu.section.help.label', default: 'Help')} </h3>
      <ul>
        <g:each var="c" in="${helpControllers}">
          
          <li class="helpControllers">
            <g:link controller="${c.key}">${c.value}</g:link>
          </li>
        </g:each>
      </ul>

    <!-- Do this last, so that the code which updates point tag fields on page 
         doesn't cause a warning to be issued even when the user hasn't actually
         changed the form. -->
    <g:javascript src="formChangedWarning.js"/>
  </body>
  

    </div>

