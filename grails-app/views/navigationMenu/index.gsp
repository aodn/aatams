<!-- 
     This file deliberately does not have header/body elements, as it's being inserted inside the elements
     from elsewhere.
-->

    <div id="controllerList" >

      <h3>${message(code: 'navigationMenu.section.background.label', default: 'Background Data')} </h3>
      
      <ul>
        <g:each var="c" in="${backgroundDataControllers}">
          
          <li class="backgroundDataControllers">
            <g:link controller="${c.key}">${c.value}</g:link>
            
            <shiro:hasRole name="SysAdmin">
              <span class="inline">
                <g:link controller="${c.key}" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
              </span>
            </shiro:hasRole>
            
          </li>
        </g:each>
      </ul>

      <h3>${message(code: 'navigationMenu.section.installation.label', default: 'Installation Data')} </h3>
      <ul>
        <g:each var="c" in="${installationDataControllers}">
          
          <li class="installationDataControllers">
            <g:link controller="${c.key}">${c.value}</g:link>
            <span class="inline">
              <g:link controller="${c.key}" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
            </span>
          </li>
        </g:each>
      </ul>
      
      <h3>${message(code: 'navigationMenu.section.field.label', default: 'Field Data')} </h3>
      <ul>
        <g:each var="c" in="${fieldDataControllers}">
          
          <li class="fieldDataControllers">
            <g:link controller="${c.key}">${c.value}</g:link>
            <span class="inline">
              <g:link controller="${c.key}" action="create" class="modal ui-icon ui-icon-newwin" >create</g:link>
            </span>
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

      <%--
      <h3>${message(code: 'navigationMenu.section.admin.label', default: 'Administration')} </h3>
      <ul>
        <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
          <g:unless test="${c.name == 'Home'}" >
            <g:if test="${adminControllers.contains(c.name)}">
              <li class="adminControllers"><g:link controller="${c.logicalPropertyName}">${c.name}</g:link>
              <span class="inline">
                <g:link controller="${c.logicalPropertyName}" action="create" class="modal ui-icon ui-icon-circlesmall-plus" >create</g:link>
              </span>
              </li>
            </g:if>
          </g:unless>
        </g:each>
      </ul>
      --%>
         
    </div>

