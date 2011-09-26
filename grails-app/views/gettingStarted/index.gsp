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
                                           clearStyle: true});
                $("#accordion").accordion({active:0});
            });
        </script>

    </head>
    <body>
        <h2>Getting Started</h2>
        <br/>
        
        <div id="accordion" style="height: 75%">
          
          <h3><a href="#">Register</a></h3>
          <div>
            
              <p>You must be a registered user to add data to AATAMS.</p>
              <br/>
              <p>Request registration <a href="/aatams/person/create">here</a>...</p>
              <br/>
            
          </div>

          <h3><a href="#">Background Data</a></h3>
          <div>
            <iframe src="${resource(dir:'html/gettingStarted',file:'background_data_flowchart.htm')}" 
                    width="99%" height="100%" frameborder="0">
              Background Data Diagram
            </iframe>
          </div>

          <h3><a href="#">Installation Data</a></h3>
          <div>
            <iframe src="${resource(dir:'html/gettingStarted',file:'installation_data_flowchart.htm')}" 
                    width="99%" height="100%" frameborder="0">
              Installation Data Diagram
            </iframe>
          </div>
          <h3><a href="#">Receiver Data</a></h3>
          <div>
            <iframe src="${resource(dir:'html/gettingStarted',file:'receiver_flowchart.htm')}" 
                    width="99%" height="100%" frameborder="0">
              Installation Data Diagram
            </iframe>
          </div>
          <h3><a href="#">Tag Data</a></h3>
          <div>
            <iframe src="${resource(dir:'html/gettingStarted',file:'tag_flowchart.htm')}" 
                    width="99%" height="100%" frameborder="0">
              Installation Data Diagram
            </iframe>
          </div>

        </div>
    </body>
</html>
