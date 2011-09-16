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

    <div class="nav">
        <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
    </div>

    <div class="body">
      
      <h1>
        <g:message code="default.report.create.label" args="[displayName]" />        
      </h1>

      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>

      <g:report name="${name}" formats="${formats}"/>
      
    </div>

  </body>
  
</html>
