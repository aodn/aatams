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

        <h1><g:message code="default.application.fullTitle" /></h1>

        <div class="dialog">
            <div style="width: 80%; padding:20px 20px 20px 20px;">
                <p>The <g:message code="default.application.fullTitle" />
                is one of eleven facilities of the Integrated Marine Observing System (IMOS).
                <g:message code="default.application.title" /> represents the higher biological monitoring of the marine environment
                for the IMOS program.<br/><br/>
                    Currently <g:message code="default.application.title" /> uses acoustic technology, CTD satellite trackers and bio loggers
                    to monitor costal and oceanic movements of marine animals from the Australian
                    mainland to the sub-Antarctic islands and as far south as the Antarctic continent.<br/><br/>
                    <g:message code="default.application.title" /> is set up to collect data over a long period of time. This sustained approach
                    will enable researchers to assess the effects of climate change, ocean acidification
                    and other physical changes that affect animals within the marine environment.<br/><br/>
                    Currently a large range of fish, sharks and mammals are collecting a wide range of data.
                    This includes behavioural and physical data such as the depth, temperature, salinity
                    and movement effort of individual marine animals.</p>
            </div>
        </div>

        <h1>Acknowledgements</h1>

        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">Codes for Australian Aquatic Biota (CAAB)</td>

                    <td valign="top" class="value"><b>Rees, A.J.J., Yearsley, G.K., Gowlett-Holmes, K. and Pogonoski, J.</b><br/>
                        Codes for Australian Aquatic Biota (on-line version).<br/>
                        CSIRO Marine and Atmospheric Research, World Wide Web electronic publication, 1999 onwards.<br/>
                        Available at: <a href="http://www.cmar.csiro.au/caab/">http://www.cmar.csiro.au/caab/</a></td>

                </tr>

                </tbody>
            </table>
        </div>

        <h1>Miscellaneous</h1>

        <div class="dialog">
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
            <img src="https://static.emii.org.au/images/logo/NCRIS_Initiative_inline200.png"  height="80" alt="Australian Government funding entity logo" />
            <a href="http://www.utas.edu.au/"><img src="https://static.emii.org.au/images/logo/utas/UTAS_MONO_Stacked_208w.png"   height="80" alt="Utas Logo" /></a>

            <p><a title="Integrated Marine Observing System" href="http://www.imos.org.au">IMOS</a>
                is a national collaborative research infrastructure, supported by Australian Government. It is led by
                <a href="http://www.utas.edu.au/">University of Tasmania</a>
                in partnership with the Australian marine & climate science community.
            </p>

            <br/>

            <p><a href="http://help.aodn.org.au/help/?q=node/90" target="_blank" class="external">Acknowledgement</a>
             | 
             <a href="http://help.aodn.org.au/help/?q=node/80" target="_blank" class="external">Disclaimer</a></p>
        </div>

    </div>

</body>
</html>
