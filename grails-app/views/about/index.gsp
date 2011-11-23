<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <title>About AATAMS</title>
  </head>
  <body>
      <div class="nav">
          <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
      </div>
    
      <div class="body">
        <h1>Australian Animal Tagging and Monitoring System (AATAMS)</h1>
        
        <div class="dialog">
          <div style="width: 80%; padding:20px 20px 20px 20px;" >
            <p>The Australian Animal Tagging and Monitoring System (AATAMS)
              is one of eleven facilities of the Integrated Marine Observing System (IMOS). 
              AATAMS represents the higher biological monitoring of the marine environment 
              for the IMOS program.<br/><br/>
              Currently AATAMS uses acoustic technology, CTD satellite trackers and bio loggers 
              to monitor costal and oceanic movements of marine animals from the Australian 
              mainland to the sub-Antarctic islands and as far south as the Antarctic continent.<br/><br/>
              AATAMS is set up to collect data over a long period of time. This sustained approach
              will enable researchers to assess the effects of climate change, ocean acidification
              and other physical changes that affect animals within the marine environment.<br/><br/>
              Currently a large range of fish, sharks and mammals are collecting a wide range of data.
              This includes behavioural and physical data such as the depth, temperature, salinity
              and movement effort of individual marine animals.</p>
          </div>
        </div>
        
        <div class="dialog">
            <table>
                <tbody>
                    <tr class="prop">
                        <td valign="top" class="name">IMOS AATAMS site</td>

                        <td valign="top" class="value"><a href="http://imos.org.au/aatams.html">http://imos.org.au/aatams.html</a></td>

                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">Support</td>

                        <td valign="top" class="value">aatams_admin(at)emii.org.au</td>

                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">Application version</td>

                        <td valign="top" class="value"><g:meta name="app.version"/></td>

                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">Grails version</td>

                        <td valign="top" class="value"><g:meta name="app.grails.version"/></td>

                    </tr>
                    
                </tbody>
            </table>
        </div>
        
        <div class="dialog">
          <br/><br/>
          <div id="footerLogo">
            <a href="http://imos.org.au/"><img src="${resource(dir:'images',file:'IMOS-logo.png')}" alt="IMOS Logo"  /></a>
            <a href="http://www.innovation.gov.au/"><img src="${resource(dir:'images',file:'aus.jpg')}" width="121" height="78" alt="" /></a>
            <a href="http://www.utas.edu.au/"><img src="${resource(dir:'images',file:'utas.jpg')}" width="61" height="76" alt="" /></a>       
            <p>IMOS is supported by the Australian Government through the National Collaborative Research Infrastructure Strategy and the Super Science Initiative.
            It is led by the University of Tasmania on behalf of the Australian marine &amp; climate science community.</p>
          </div>
          <br/><br/>
        </div>
        
      </div>
    
  </body>
</html>
