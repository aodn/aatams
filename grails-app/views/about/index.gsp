<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main"/>
        <title>About AATAMS</title>
    </head>

    <body>
    <div class="nav">
        <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
        </span>
    </div>

    <div class="body">

        <h1>Australian Animal Tagging and Monitoring System (AATAMS)</h1>

        <div class="dialog">
            <div style="width: 80%; padding:20px 20px 20px 20px;">
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
                    and movement effort of individual marine animals.</p><br/>

                <p>IMOS data is made freely available under the
                    <a href="http://imos.org.au/fileadmin/user_upload/shared/IMOS%20General/documents/internal/IMOS_Policy_documents/Policy-Acknowledgement_of_use_of_IMOS_data_11Jun09.pdf">Conditions of Use</a>.
                </p>

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
                    <td valign="top" class="name">IMOS AATAMS site</td>

                    <td valign="top" class="value"><a href="http://imos.org.au/aatams.html">http://imos.org.au/aatams.html</a>
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

                <tr class="prop">
                    <td valign="top" class="name">Grails version</td>

                    <td valign="top" class="value"><g:meta name="app.grails.version"/></td>

                </tr>

                </tbody>
            </table>
        </div>

        <div id="footer">
            <a href="http://imos.org.au/"><img src="${resource(dir: 'images', file: 'IMOS-logo.png')}" alt="IMOS Logo"/>
            </a>
            <img src="http://static.emii.org.au/images/logo/NCRIS_Initiative_inline200.png" alt="Australian Government logo"/>
            <a href="http://www.utas.edu.au/"><img src="http://static.emii.org.au/images/logo/Utas_vert.png" alt="UTAS Logo" height="50"/>
            </a>

            <p><a title="Integrated Marine Observing System" href="http://www.imos.org.au">IMOS</a>
                is a national collaborative research infrastructure, supported by the Australian Government. It is led by the
                <a href="http://www.utas.edu.au/">University of Tasmania</a>
                in partnership with the Australian marine & climate science community.
            </p>

            <p>You accept all risks and responsibility for losses, damages, costs and other consequences resulting directly or
            indirectly from using this site and any information or material available from it. If you have any concerns about
            the veracity of the data, please make enquiries via <a href="mailto:${grailsApplication.config.grails.mail.supportEmailAddress}>${grailsApplication.config.grails.mail.supportEmailAddress}"</a> to be directed to the data custodian.
            </p>

        </div>

    </div>

</body>
</html>
