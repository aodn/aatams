$(function() 
{
    $(document).ready(function()
    {
        updateLocation()
    });
    
    $('#station\\.id').change(function()
    {
        updateLocation()
    })
});

function updateLocation()
{
    $.post('/aatams/installationStation/show', 
          {'id':$('#station\\.id').val(),
           'encoding':'json'},
          function(data) 
          {
                $('input[name$="_lon"]').val(data.installationStationInstance.location.x);
                $('input[name$="_lat"]').val(data.installationStationInstance.location.y);
                $('input[name$="_srid"]').val(data.installationStationInstance.location.srid);

                // TODO: cut and paste from pointEdit.js, refactor.
                $(".pointEdit").each(function()
                {
                    var lon = $(this).find('input[name$="_lon"]').val();
                    var lat = $(this).find('input[name$="_lat"]').val();
                    var srid = $(this).find('input[name$="_srid"]').val();

                    // Save the point as a "coded" string.  This is then parsed on 
                    // by the PointEditor.
                    var parentName = $(this).attr("id");
                    var pointInput = $(this).find('#' + parentName);
                    var pointCodedString = genCodedPointStringX(lon, lat, srid);
                    
                    pointInput.val(pointCodedString);

                    var pointAsString = genPointStringX(lon, lat, srid);
                    var pointInputTextField = $(this).find('#pointInputTextField')
                    pointInputTextField.val(pointAsString);
                });
          },
          'json');
}

function genPointStringX(lon, lat, srid)
{
    // Construct the string from lon, lat, srid values.
    var pointAsString = "";
    
    if ((lon == "") || (lat == "") || (srid == ""))
    {
        return pointAsString;
    }
    
    pointAsString += Math.abs(lat) + "°"
    if (lat >= 0)
    {
        pointAsString += 'N'
    }
    else
    {
        pointAsString += 'S'
    }

    pointAsString += ' ' + Math.abs(lon) + "° "
    if (lon >= 0)
    {
        pointAsString += 'E'
    }
    else
    {
        pointAsString += 'W'
    }

    pointAsString += " (datum:" + getName(srid) + ")"
    
    return pointAsString;
}

function genCodedPointStringX(lon, lat, srid)
{
    if ((lon == "") || (lat == "") || (srid == ""))
    {
        return "";
    }
    
    return "POINT(" + lon + " " + lat + ") , " + srid;
}
