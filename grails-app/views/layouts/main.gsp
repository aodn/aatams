<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="au.org.emii.aatams.PermissionUtilsService" %>
<!DOCTYPE html>
<html >
  <head>
    <title><g:layoutTitle default="${message(code: 'default.application.title', default: 'AATAMS')}" /></title>
    <link rel="shortcut icon" href="${resource(dir:'images',file:'fish.ico')}" type="image/x-icon" />

    <script type="text/javascript" src="${resource(dir:'js',file:'utils.js')}"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'jquery-1.7.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery-ui-1.8.10.custom.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery-ui-combobox.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.layout.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.highlight.js')}"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.selectlist.js')}"></script>

    <g:javascript src="asAbove.js"/>

    <g:javascript src="jquery.blockUI.js"/>
    <g:javascript src="jquery.qtip-1.0.0-rc3.min.js"/>
    <g:javascript src="jquery.cookie.js"/>
    <g:javascript src="notification.js"/>
    <g:javascript src="rememberFormFields.js"/>
    <g:javascript src="autocompleteForLargeSelects.js"/>


    <g:javascript src="blockUIConfig.js"/>

    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'jquery-ui.css')}"/>
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'custom-theme/jquery-ui-1.8.13.custom.css')}"/>
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'jquery-ui-combobox.css')}"/>

    <g:set var="versionedMainCssName" value="main.css?v=${grailsApplication.metadata.'app.version'}" />
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: versionedMainCssName)}" />

    <!-- Shiro tags, used for security -->
    <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

    <script>

      var contextPath = "${request.contextPath}";

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

    <g:layoutHead />

    <script type="text/javascript">

    var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method

    $(document).ready(function ()
    {
      myLayout = $('body').layout(
      {
        north: {size: 130, resizable: false, closable: false},
        spacing_closed:            10,
        // enable showOverflow on west-pane so popups will overlap north pane
        west: {showOverflowOnHover: false, resizable:  false, closable: false, size: 230}
      });
    });

    </script>


    <style type="text/css">
      /**
      *    Basic Layout Theme
      *
      *    This theme uses the default layout class-names for all classes
      *    Add any 'custom class-names', from options: paneClass, resizerClass, togglerClass
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

    <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

      ga('create', 'UA-54091417-4', 'auto');
      ga('send', 'pageview');

    </script>

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

    <div id="spinner-big" class="spinner-big" style="display:none;">
      <img src="${resource(dir:'images',file:'spinner-big.gif')}" alt="${message(code:'roller.alt',default:'Loading...')}" />
    </div>

    <div id="notifications" style="display:none;">
      <g:each in="${notifications}">
        <g:notification value="${it}" />
      </g:each>
    </div>

    <div class="ui-layout-north">
      <div id="logo">
        <a href="http://imos.org.au/"><img src="${resource(dir:'images',file:'IMOS_Facility_8.png')}" alt="IMOS Logo"  /></a>

        <div id="userlogin">
          <!-- Shown if not logged in. -->
          <shiro:guest>
              <g:link controller="auth" params="[targetUri:request.getRequestURI() - request.getContextPath() + '?' + request.getQueryString()]">Login</g:link> |
              <g:link controller="person" action="create">Register</g:link>
          </shiro:guest>

          <!-- Shown if logged in. -->
          <shiro:user>
            <g:set var="principal" value="${PermissionUtilsService.principal()}" />

            <div id="userlogout">
              Logged in as <g:link controller="person" action="show" id="${principal.id}">${principal.name}</g:link> (<g:link controller="auditLogEvent" action="list">activity</g:link> | <g:link controller="auth" action="signOut">logout</g:link>)
            </div>
          </shiro:user>
        </div>
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

     <g:listFilterIncludes/>
    <script>
      $(document).ready(function()
      {
          if (window.location.href.indexOf("list") === -1)
          {
              // Set focus to second visible element (the first is the search
              // textfield at top left of navigation panel).
              $(':input:enabled:visible:eq(1)').focus();
          }
      });
    </script>


  </body>

</html>
