<%@ page contentType="text/html;charset=UTF-8" %>
<html >
  <head>
    <title><g:layoutTitle default="${message(code: 'default.application.title', default: 'AATAMS')}" /></title>
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />

    <script type="text/javascript" src="${resource(dir:'js',file:'jquery-1.4.4.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery-ui-1.8.10.custom.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.layout.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.highlight.js')}"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'jquery.selectlist.js')}"></script>

    <script type="text/javascript" src="${resource(dir:'js',file:'addorganisationtoproject.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'addpersontoproject.js')}"></script>
    <script type="text/javascript" src="${resource(dir:'js',file:'addsensortotag.js')}"></script>

    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'jquery-ui.css')}"/>
<!--    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'ui-lightness/jquery-ui-1.8.10.custom.css')}"/>-->
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom-theme/jquery-ui-1.8.13.custom.css')}"/>
    <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />

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

    <g:layoutHead />
<!--    <g:javascript library="application" />-->
    <g:javascript library="jquery" />
    <g:javascript library="main_extras" />

    <g:setProvider library="jquery"/>



    <script type="text/javascript">

    var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method

    $(document).ready(function () {

    myLayout = $('body').layout({
    north__resizable:  false,
    north__closable: false,
    spacing_closed:			10,
    // enable showOverflow on west-pane so popups will overlap north pane
    west__showOverflowOnHover: false,
    west__size: 250,
    west__minSize: 250,
    west__maxSize: 950,
    west__closable: false
    //togglerLength_closed:  "100%",
    //west__togglerTip_open:		"Close Menu",
    //west__togglerTip_closed:		"Open Menu"

    //west__fxSettings_open: { easing: "easeOutBounce", duration: 750 },

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


    <div class="ui-layout-north">
      <div id="logo">
        <img src="${resource(dir:'images',file:'IMOS-logo.png')}" alt="IMOS Logo"  />
        <h1>${message(code: 'default.application.detailedTitle', default: 'AATAMS data entry')}</h1>

        <div id="userlogin">
          <g:link controller='login'>Login</g:link>
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

  </body>
  
</html>
