<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html >
  <head>
    <title><g:layoutTitle default="${message(code: 'default.application.title', default: 'AATAMS')}" /></title>
    <link rel="shortcut icon" href="${resource(dir:'images',file:'fish.ico')}" type="image/x-icon" />

    <script type="text/javascript" src="${resource(dir:'js',file:'utils.js')}"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'jquery-1.4.4.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery-ui-1.8.10.custom.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.layout.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.highlight.js')}"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.selectlist.js')}"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'addorganisationtoproject.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'addpersontoproject.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'addsensortotag.js')}"></script>

    <g:javascript src="asAbove.js"/>
    
    
    <g:javascript src="jquery.qtip-1.0.0-rc3.min.js"/>
    <g:javascript src="jquery.cookie.js"/>
    <g:javascript src="notification.js"/>
    <link ref="stylesheet" type="text/css" href="${resource(dir:'js/bubbletip', file:'bubbletip.css')}" />
    
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'jquery-ui.css')}"/>
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom-theme/jquery-ui-1.8.13.custom.css')}"/>
    <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />

    <!-- Shiro tags, used for security -->
    <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
    
    <script>
    // important. leave here for main_extras.js
    var grailsServerURL = "${grailsApplication.config.grails.serverURL}";

    <g:if test="${params.popup}" >
    $(document).ready(function () {
    // this is a popup than we dont want all the links and logos etc
    // the popup code 'modalMask()' apends the variable popup=true to the iframe URL
    $('#boxes').hide();
    $('#logo').hide();
    $('.nav').hide();
    });
    </g:if>
    </script>
    
    <script>
      $(document).ready(function()
      {
          // Set focus to first visible element.
          $("input:text:visible:first").focus();   
      });
    </script>

    <g:layoutHead />

    <script type="text/javascript">

    var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method

    $(document).ready(function () {

      myLayout = $('body').layout({
      north__resizable:  false,
      north__closable: false,
      spacing_closed:			10,
      // enable showOverflow on west-pane so popups will overlap north pane
      west__showOverflowOnHover: false,
      west__resizable:  false,
      west__closable: false
    });


    });

    </script>


    <style type="text/css">
      /**
      *	Basic Layout Theme
      *
      *	This theme uses the default layout class-names for all classes
      *	Add any 'custom class-names', from options: paneClass, resizerClass, togglerClass
      */

      .ui-layout-pane { /* all 'panes' */
        background: #FFF;
        border: 1px solid #eeeeee;
        padding: 10px;
        overflow: auto;
      }

      .ui-layout-resizer { /* all 'resizer-bars' */
        background: #fafafa;
      }

      .ui-layout-toggler { /* all 'toggler-buttons' */
        background: #dddddd;
      }


    </style>

  </head>
  <body>

    
   <div id="boxes">
      <div id="dialog" class="window fixed">
        <a href="#" class="close right">Close</a>
        <iframe id="dialogIframe" frameborder="0" width="760" height="700" ></iframe>

      </div>
      <div id="mask"></div>
    </div>
    
    <div id="spinner" class="spinner" style="display:none;">
      <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
    </div>

    <div id="notifications" style="display:none;">
      <g:each in="${notifications}">
        <g:notification value="${it}" />
      </g:each>
    </div>
    
    <div class="ui-layout-north">
      <div class="mainTitle">${message(code: 'default.application.detailedTitle', default: 'AATAMS data entry')}</div>

      <div id="userlogin">
        <!-- Shown if not logged in. -->
        <shiro:guest>
            <g:link controller="auth" params="[targetUri:request.getRequestURI() - request.getContextPath()]">Login</g:link> |
            <g:link controller="person" action="create">Register</g:link>

        </shiro:guest>

        <!-- Shown if logged in. -->
        <shiro:user>
          <div id="userlogout">
            Logged in as <shiro:principal/> (<g:link controller="auth" action="signOut">logout</g:link>)
          </div>
        </shiro:user>
      </div>

    </div>

    <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
    <div class="ui-layout-west" >


      <div id="controllerList" >
        <g:include controller="navigationMenu" action="index" />
      </div>

    </div>

    <div class="ui-layout-center">
      
      <g:layoutBody />
      
    </div>
    
    <div class="ui-layout-south"> 

      <div id="footerLogo">
        <a href="http://imos.org.au/"><img src="${resource(dir:'images',file:'IMOS-logo.png')}" alt="IMOS Logo"  /></a>
        <a href="http://www.innovation.gov.au/"><img src="${resource(dir:'images',file:'aus.jpg')}" width="121" height="78" alt="" /></a>
        <a href="http://www.utas.edu.au/"><img src="${resource(dir:'images',file:'utas.jpg')}" width="61" height="76" alt="" /></a>       
        <p>IMOS is supported by the Australian Government through the National Collaborative Research Infrastructure Strategy and the Super Science Initiative.
        It is led by the University of Tasmania on behalf of the Australian marine &amp; climate science community.</p>
      </div>
      
    </div>
    
    
</html>
