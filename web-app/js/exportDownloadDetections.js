$(document).ready(function () {
    if ($('#totalMatches').val() > 300000) {
        $('input.KMZ, input.KML').prop('disabled', true);
    }

    $('#listControlForm').find(":submit").on('click', function () {

        // Show acknowledgement for export downloads.
        if (this.name === "_action_export" ) {

            if ($('#listControlForm').attr('is_acknowledged') != 'true'){
                showAcknowledgement();
                return false;
            }
        }
    });

    function showAcknowledgement() {

        setCanDownload(true);

        $("#acknowledgement").dialog({
            resizable: false,
            height: "auto",
            width: 600,
            modal: true,
            buttons: {
                "I Acknowledge": function () {
                    $("#acknowledgement").dialog("close");
                    $('#listControlForm').find(":submit").click();
                    setCanDownload(false);
                },
                Cancel: function () {
                    setCanDownload(false);
                    $(this).dialog("close");
                }
            }
        });
    };

    function setCanDownload(action) {
        $('#listControlForm').attr('is_acknowledged', action);
    }
});
