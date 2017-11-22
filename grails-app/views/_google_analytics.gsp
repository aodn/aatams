<g:if test="${grailsApplication.config.googleAnalytics.trackingId}">
    <script>
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
        ga('create', '${grailsApplication.config.googleAnalytics.trackingId}', 'auto');
        ga('require', 'displayfeatures');
        ga('send', 'pageview');
    </script>
</g:if>

<script>
    // wrapper to the Google Analytics function
    function trackUsage(eventCategory, eventAction, eventLabel, eventValue, dimension) {

        if (typeof ga == 'function') {
            ga('send', 'event', eventCategory, eventAction, eventLabel, eventValue, {
                dimension1: dimension
            });
        }
        else {
            // https://developers.google.com/analytics/devguides/collection/analyticsjs/command-queue-reference#send
            console.log("DEBUG GA: " +['send', 'event',eventCategory, eventAction, eventLabel, eventValue].join())
        }
    }

    function trackDownloadUsage(actionKey, label) {
        trackUsage(
            "Download",
            actionKey,
            label
        );
    }

    function trackDownloadFilterUsage(actionKey, label) {
        trackUsage(
            "Filtering",
            actionKey,
            label
        );
    }
</script>
