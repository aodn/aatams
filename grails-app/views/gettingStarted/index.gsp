<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Getting Started</title>
            
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
        <h2>Getting Started</h2>
        <br/>
        
        <div id="accordion" style="height: 75%">
          
          <h3 class="accordion_header"><a href="#">Register</a></h3>
          <div>
            
              <p>You must be a registered user to add data to AATAMS.</p>
              <br/>
              <p>Register <a href="/aatams/person/create">here</a>...</p>
              <br/>
            
          </div>

          <h3 class="accordion_header">
            <a href="#">Background Data</a>
            <a>View or create organisations, projects and people</a> </h3>
          <div>
            <iframe src="${resource(dir:'html/gettingStarted',file:'background_data.html')}" 
                    width="99%" height="100%" frameborder="0">
              Background Data Diagram
            </iframe>
          </div>

          <h3 class="accordion_header">
            <a href="#">Installation Data</a>
            <a>View or create installations and installation stations</a>
          </h3>
          <div>
            <iframe src="${resource(dir:'html/gettingStarted',file:'installation_data.html')}" 
                    width="99%" height="100%" frameborder="0">
              Installation Data Diagram
            </iframe>
          </div>
          
          <h3 class="accordion_header">
            <a href="#">Receiver Data</a>
            <a>View or create receivers, receiver deployments and recoveries and upload receiver export data</a>
          </h3>
          <div>
            <iframe src="${resource(dir:'html/gettingStarted',file:'receiver.html')}" 
                    width="99%" height="100%" frameborder="0">
              Installation Data Diagram
            </iframe>
          </div>
          
          <h3 class="accordion_header">
            <a href="#">Tag Data</a>
            <a>View or create tags and tag releases</a>
          </h3>
          <div>
            <iframe src="${resource(dir:'html/gettingStarted',file:'tag.html')}" 
                    width="99%" height="100%" frameborder="0">
              Installation Data Diagram
            </iframe>
          </div>

        </div>
    </body>
</html>
