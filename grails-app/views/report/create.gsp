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
      
      <g:report name="${name}"/>
      
    </div>

  </body>
  
</html>
