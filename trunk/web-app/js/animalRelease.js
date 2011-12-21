$(function() 
{
    $(document).ready(function()
    {
        $("#captureLocality").change(function()
        {
            if ($("#releaseLocality").val() == "")
            {
                $("#releaseLocality").val($(this).val())
            }
        });

        $("#captureLocation_dialog-form-edit-point").bind('dialogclose', function(event)
        {
            if ($("#releaseLocation_pointInputTextField").val() == "")
            {
                $("#releaseLocation_pointInputTextField").val($("#captureLocation_pointInputTextField").val())
                $("#releaseLocation").val($("#captureLocation").val())
                $("#releaseLocation_lon").val($("#captureLocation_lon").val())
                $("#releaseLocation_lat").val($("#captureLocation_lat").val())
                $("#releaseLocation_srid").val($("#captureLocation_srid").val())
            }
        });

        // Capture Date/Time is a "remembered" field, so make sure we init the release data/time accordingly.
        $("[id^='captureDateTime_']").each(function()
        {
            var srcId = $(this).attr("id")
            var destId = srcId.replace("capture", "release")
            
            $("#" + destId).val($("#" + srcId).val())
        });
        
        $("[id^='captureDateTime_']").change(function()
        {
            var srcId = $(this).attr("id")
            var destId = srcId.replace("capture", "release")
            
            $("#" + destId).val($("#" + srcId).val())
        });
    });
});
