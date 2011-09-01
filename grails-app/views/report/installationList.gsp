<!--
  Execute the installations report (including specifying filter parameters.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="main" />
    <title>Sample title</title>
  </head>
  <body>
    <h1>Sample line</h1>
<%--
    <g:jasperForm controller="report"
                  action="installationListExecute"
                  jasper="installationList">

      <!-- Filter controls go here -->

      <g:jasperButton format="pdf" jasper="jasper-test" text="PDF" />

    </g:jasperForm>    
--%>
      <g:jasperReport jasper="installationList" 
                      format="PDF" 
                      controller="report"
                      action="installationListExecute"
                      language="groovy"/>
    
  </body>
</html>
