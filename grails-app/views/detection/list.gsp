<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'detection.label', default: 'Detection')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.upload.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>

            <g:listControlForm name="detection"
                               formats="${['CSV']}" />

            <div class="list">
                <table>
                    <thead>
                        <tr>

                            <td/>

                            <g:sortableColumn property="timestamp" title="${message(code: 'detection.timestamp.label', default: 'Timestamp')}" params="${params}"/>

                            <g:sortableColumn property="receiverName" title="${message(code: 'detection.receiverName.label', default: 'Receiver Name')}" params="${params}"/>

                            <g:sortableColumn property="receiverDeployment" title="${message(code: 'detection.receiverDeployment.label', default: 'Receiver Deployment')}" params="${params}"/>

                            <g:sortableColumn property="transmitterId" title="${message(code: 'detection.transmitterId.label', default: 'Transmitter ID')}" params="${params}"/>

                            <g:sortableColumn property="transmitterSerialNumber" title="${message(code: 'detection.transmitterSerialNumber.label', default: 'Transmitter Serial Number')}" params="${params}"/>

                            <g:sortableColumn property="stationName" title="${message(code: 'detection.stationName.label', default: 'Station Name')}" params="${params}"/>

                            <g:sortableColumn property="receiverDownload.requestingUser" title="${message(code: 'receiverDownloadFile.requestingUser.label', default: 'Uploader')}" params="${params}"/>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${entityList}" status="i" var="detectionInstance">

                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td class="rowButton"><g:link class="show" action="show" id="${detectionInstance.id}">.</g:link></td>

                            <td><joda:format value="${detectionInstance.timestamp}" /></td>

                            <td>${detectionInstance.receiverName}</td>

                            <td><g:link controller="receiverDeployment" action="show" id="${detectionInstance?.receiverDeployment?.id}">${detectionInstance.receiverDeployment}</g:link></td>

                            <td>${detectionInstance.transmitterId}</td>

                            <td>${detectionInstance.transmitterSerialNumber}</td>

                            <td>${detectionInstance.stationName}</td>

                            <td><g:link controller="person" action="show" id="${detectionInstance?.receiverDownload?.requestingUser?.id}">${detectionInstance.receiverDownload.requestingUser}</g:link></td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <g:nonPaginatedWarning />
        </div>
    </body>
</html>
