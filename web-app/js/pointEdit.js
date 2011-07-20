$(function()
{
    $('#dialog-form-edit-point').dialog(
    {
        autoOpen: false,
        height: 225,
        width: 300,
        modal: true,
        buttons: {
            Ok: function() 
            {
                // Update the hidden field values.
                $('#lon').val($('#editLon').val());
                $('#lat').val($('#editLat').val());
                $('#srid').val($('#editSrid').val());
                
                updatePointString()
                
                $(this).dialog('close');

            },
            Cancel: function() {
                $(this).dialog('close');
            }
        }
    }) 

    $(document).ready(function()
    {
        // For each div of class "pointEdit", update the value of the first
        // child (which is a textField).
        $()
        
        updatePointString();
    })
    
    $('#pointInputTextField').click(function()
    {
        // Update the values in the edit form.
        $('#editLon').val($('#lon').val());
        $('#editLat').val($('#lat').val());
        $('#editSrid').val($('#srid').val());
       
        $('#dialog-form-edit-point').dialog('open');
    }) 
});

function updatePointString()
{
    // Construct the string from lon, lat, srid values.
    var pointAsString = "";
    var lon = $('#lon').val();
    var lat = $('#lat').val();
    var srid = $('#srid').val();

    pointAsString += Math.abs(lon) + "°"
    if (lon >= 0)
    {
        pointAsString += 'N'
    }
    else
    {
        pointAsString += 'S'
    }

    pointAsString += ' ' + Math.abs(lat) + "° "
    if (lat >= 0)
    {
        pointAsString += 'E'
    }
    else
    {
        pointAsString += 'W'
    }

    pointAsString += " (EPSG:" + srid + ")"
    
    $('#pointInputTextField').val(pointAsString)
}