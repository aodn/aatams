<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Getting Started</title>
        <g:javascript src="jquery.iframe-auto-height.plugin.1.5.0.min.js" />
        <script>
            $(document).ready(function() 
            {
                $("#accordion").accordion({collapsible:true, 
                    active:false, 
                    clearStyle: true,
                    header:".accordion_header"});
                $("#accordion").accordion({active:0});
            });
            
        </script>
    </head>
    
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div style="width:99%">
          <h1>Getting Started</h1>
          <br/>

          <div id="accordion" style="height: 100%;">

            <h3 class="accordion_header">
              <a href="#">Background Data</a>
              <a>View or create organisations, projects and people</a> </h3>
            <div>
              <iframe src="${resource(dir:'html/gettingStarted',file:'background_data.html')}" 
                      frameborder="0" style="width:100%; height:600px;" >
                Background Data Diagram
              </iframe>
            </div>

            <h3 class="accordion_header">
              <a href="#">Installation Data</a>
              <a>View or create installations and installation stations</a>
            </h3>
            <div>
              <iframe src="${resource(dir:'html/gettingStarted',file:'installation_data.html')}" 
                      frameborder="0" style="width:100%; height:600px;" >
                Installation Data Diagram
              </iframe>
            </div>

            <h3 class="accordion_header">
              <a href="#">Receiver Data</a>
              <a>View or create receivers, receiver deployments and recoveries and upload receiver export data</a>
            </h3>
            <div>
              <iframe src="${resource(dir:'html/gettingStarted',file:'receiver.html')}" 
                      frameborder="0" style="width:100%; height:750px;" >
                Receiver Data Diagram
              </iframe>
            </div>

            <h3 class="accordion_header">
              <a href="#">Tag Data</a>
              <a>View or create tags and tag releases</a>
            </h3>
            <div>
              <iframe src="${resource(dir:'html/gettingStarted',file:'tag.html')}" 
                      frameborder="0" style="width:100%; height:600px;" >
                Tag Data Diagram
              </iframe>
            </div>

          </div>
        </div>
    </body>
</html>
