<!--
  Execute the names report (including specifying filter parameters).
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="main" />
    <title>${name} report</title>
  </head>
  
  <body>
    <div class="body">
      <div class="nav">
          <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
      </div>
      
      <h1>
        <g:message code="default.report.create.label" args="[name]" />        
      </h1>
      
      <g:report name="${name}"/>
      
    </div>

  </body>
  
</html>
