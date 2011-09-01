<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Getting Started</title>
            
            
        <script>
            $(document).ready(function() 
            {
//                $("#accordion").accordion({fillSpace:true, collapsible:true, active:false, autoHeight:true});
                $("#accordion").accordion({collapsible:true, 
                                           active:false, 
                                           //autoHeight:true, 
                                           //fillSpace: true,
                                           clearStyle: true});
                $("#accordion").accordion({active:0});
            });
        </script>
            
    </head>
    <body>
        <h2>Getting Started</h2>
        <br/>
        
            <div id="accordion" style="height: 75%">
                    <h3><a href="#">Background Data</a></h3>
                    <div>
                            <p>
                                <map name="map_s29"><area href="/aatams/person/create" target="_top"
                                onclick="window.event.cancelBubble=true;" shape=polygon 
                                coords="418, 396, 415, 397, 413, 398, 412, 402, 412, 438, 413, 443, 418, 444, 477, 444, 481, 443, 483, 438, 483, 402, 481, 398, 480, 397, 477, 396, 418, 396"><area
                                href="/aatams/project/create" target="_top"
                                onclick="window.event.cancelBubble=true;" shape=polygon 
                                coords="418, 249, 413, 251, 412, 255, 412, 291, 413, 295, 415, 297, 418, 297, 477, 297, 480, 297, 481, 295, 483, 291, 483, 255, 481, 251, 477, 249, 418, 249"><area
                                href="/aatams/organisation/create" target="_top"
                                onclick="window.event.cancelBubble=true;" shape=polygon 
                                coords="418, 89, 415, 90, 413, 91, 412, 95, 412, 131, 413, 136, 418, 137, 477, 137, 481, 136, 483, 131, 483, 95, 481, 91, 480, 90, 477, 89, 418, 89"><area
                                href="/aatams/person/list" target="_top"
                                onclick="window.event.cancelBubble=true;" shape=polygon 
                                coords="246, 323, 246, 371, 318, 371, 318, 323"><area
                                href="/aatams/project/list" target="_top"
                                onclick="window.event.cancelBubble=true;" shape=polygon 
                                coords="246, 168, 246, 216, 318, 216, 318, 168"><area
                                href="/aatams/organisation/list" target="_top"
                                onclick="window.event.cancelBubble=true;" shape=polygon 
                                coords="246, 10, 246, 58, 318, 58, 318, 10"></map> 

                                <center><img src="${resource(dir:'html/background_data_flowchart_files',file:'Slide0001.gif')}" border=0 usemap="#map_s29"></center> 
                            </p>
                    </div>
                    <h3><a href="#">Installation Data</a></h3>
                    <div>
                            <p>
                            Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit amet
                            purus. Vivamus hendrerit, dolor at aliquet laoreet, mauris turpis porttitor
                            velit, faucibus interdum tellus libero ac justo. Vivamus non quam. In
                            suscipit faucibus urna.
                            </p>
                    </div>
                    <h3><a href="#">Field Data</a></h3>
                    <div>
                            <p>
                            Nam enim risus, molestie et, porta ac, aliquam ac, risus. Quisque lobortis.
                            Phasellus pellentesque purus in massa. Aenean in pede. Phasellus ac libero
                            ac tellus pellentesque semper. Sed ac felis. Sed commodo, magna quis
                            lacinia ornare, quam ante aliquam nisi, eu iaculis leo purus venenatis dui.
                            </p>
                            <ul>
                                    <li>List item one</li>
                                    <li>List item two</li>
                                    <li>List item three</li>
                            </ul>
                    </div>
            </div>
        
    </body>
</html>
