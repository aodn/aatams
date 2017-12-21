<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main"/>
        <title>About <g:message code="default.application.title" /> Database</title>
    </head>

    <body>
    <div class="nav">
        <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
        </span>
    </div>

    <div class="body">

        <div class="dialog">
            <h1><g:message code="default.application.fullTitle" /></h1>
            <p>The <g:message code="default.application.fullTitle" /> is a centralised passive acoustic telemetry repository maintained by the <g:message code="default.application.fullTitle" /> (<a href="http://imos.org.au/facilities/animaltracking/">http://imos.org.au/facilities/animaltracking/</a>) and the Australian Ocean Data Network (AODN, <a href="https://portal.aodn.org.au/">https://portal.aodn.org.au/</a>). The data it contains were compiled from a nation-wide array of over 1,000 receivers and document movements of over 100 marine species within Australian coastal waters between 2007-present.</p>

            <p>The <g:message code="default.application.fullTitle" /> is one of eleven facilities of the Integrated Marine Observing System (IMOS). One of its main objectives is to oversee the development and maintenance of a national marine acoustic telemetry network and a public database for the Australian research community, thereby facilitating regional- to national-scale collaborative research and helping the management of Australia’s marine resources.</p>

            <p>Researchers involved in the field of acoustic telemetry are encouraged to join the  <g:message code="default.application.title" /> community by sharing their tag and receiver data and metadata, thereby contributing to this critical national infrastructure and benefitting from automated email notifications when their tags are detected within the continent-wide network.
                </p>
        </div>

        <h1>Acknowledgements</h1>
        <div class="dialog">
            <table>
                <tbody>
                <tr class="prop">
                    <g:render template="/acknowledgement"></g:render>
                </tr>
                <br>
                <tr class="prop">
                    <h3>Codes for Australian Aquatic Biota (CAAB)</h3>
                    <p>Rees, A.J.J., Yearsley, G.K., Gowlett-Holmes, K. and Pogonoski, J.</b><br/>
                        Codes for Australian Aquatic Biota (on-line version).<br/>
                        CSIRO Marine and Atmospheric Research, World Wide Web electronic publication, 1999 onwards.<br/>
                        Available at: <a href="http://www.cmar.csiro.au/caab/">http://www.cmar.csiro.au/caab/</a></p>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="dialog">
            <h1>Miscellaneous</h1>
            <table>
                <tbody>
                <tr class="prop">
                    <td valign="top" class="name">IMOS Animal Tracking site</td>

                    <td valign="top" class="value"><a href="http://imos.org.au/animaltracking.html">http://imos.org.au/animaltracking.html</a>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">Support</td>

                    <td valign="top" class="value"><a href="mailto:${grailsApplication.config.grails.mail.supportEmailAddress}">${grailsApplication.config.grails.mail.supportEmailAddress}</a></td>

                </tr>

                <tr class="prop">
                    <td valign="top" class="name">Application version</td>

                    <td valign="top" class="value"><g:meta name="app.version"/></td>

                </tr>

                </tbody>
            </table>
        </div>

        <div id="footer">
            <a href="http://imos.org.au/"><img src="https://static.emii.org.au/images/logo/IMOS_logo-stacked.png" height="80" alt="IMOS Logo"  /></a>
            <img src="https://static.emii.org.au/images/logo/NCRIS_2017_110.png" height="80" alt="Australian Government funding entity logo" />
            <a href="http://www.utas.edu.au/"><img src="https://static.emii.org.au/images/logo/utas/UTAS_2017_110.png" height="80" alt="UTAS Logo" /></a>

            <p><a title="Integrated Marine Observing System" href="http://www.imos.org.au">IMOS</a>
                is a national collaborative research infrastructure, supported by Australian Government.
                It is operated by a consortium of institutions as an unincorporated joint venture, with the
                <a href="http://www.utas.edu.au/">University of Tasmania</a> as Lead Agent.
            </p>

            <br/>

            <p><a href="https://help.aodn.org.au/user-guide-introduction/aodn-portal/data-use-acknowledgement/" target="_blank" class="external">Acknowledgement</a>
             | 
             <a href="https://help.aodn.org.au/user-guide-introduction/aodn-portal/disclaimer/" target="_blank" class="external">Disclaimer</a></p>
        </div>

    </div>

</body>
</html>
