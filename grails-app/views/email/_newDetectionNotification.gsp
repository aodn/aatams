<%@ page contentType="text/html"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>${title}</title>
    </head>
    <body>
        <div class="body"><p>Dear ${recipient.name},<br/>
<br/>        
The following tags have new detections as a result of a recent upload:<br/></p>

            <div class="dialog">
                <table>
                    <thead>
                        <tr>
                        
                            <th>Tag</th>
                            <th>Link to the data</th>
                            
                        </tr>
                        
                    </thead>
                    <tbody>
                        <g:each in="${sensors}" status="i" var="sensor">
                            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            
                                <td><a href="${g.createLink(controller: 'tag', action: 'show', id: sensor.tag.id, absolute: true)}">${sensor.transmitterId}</a></td>
                                <td><a href="${g.createLink(controller: 'detection', action: 'list', absolute: true, params: ['filter.in': ['transmitterId', sensor.transmitterId]])}">data...</a></td>
    
                            </tr>
                        </g:each>
                        </tbody>
                </table>
            </div>
            
            <p>
                Regards,<br/>
                The AATAMS team
            </p>            
        </div>
    </body>
</html>
