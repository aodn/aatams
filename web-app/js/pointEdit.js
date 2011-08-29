//
// This variable is used to ensure that this script is only run one (in the case
// that there are multiple point tags on one page.
//
var loaded = false;

$(function()
{
    //
    // Point editing dialog.
    //
    $('.pointEditDialog').dialog(
    {
        autoOpen: false,
        height: 225,
        width: 350,
        modal: true,
        buttons: 
        {
            Ok: function() 
            {
                // Get a reference to the div with id of parentName.
                var parentName = $(this).attr("parent");
                var parentDiv = $('#' + parentName);
                
                // Update the hidden field values.
                var lon = $(this).find('#editLon').val();
                var lat = $(this).find('#editLat').val();
                
                var editLon = $(this).find('#editLon')
                editLon.parent().attr("class", "value")
                var editLat = $(this).find('#editLat')
                editLat.parent().attr("class", "value")
                
                // Validate...
                if ((lon > 180) || (lon < -180))
                {
                    editLon.parent().attr("class", "value errors")
                    alert("Longitude must be in the range +/- 180")
                    editLon.focus();
                }
                else if ((lat > 90) || (lat < -90))
                {
                    editLat.parent().attr("class", "value errors")
                    alert("Latitude must be in the range +/- 90")
                    editLat.focus();
                }
                else
                {
                    // Negate the lat and lon values if necessary.
                    if ($(this).find('#editEastWest').val() == 'W')
                    {
                        lon = -lon
                    }

                    if ($(this).find('#editNorthSouth').val() == 'S')
                    {
                        lat = -lat
                    }

                    var srid = $(this).find('#editSrid').val();

                    parentDiv.find('input[name$="_lon"]').val(lon);
                    parentDiv.find('input[name$="_lat"]').val(lat);
                    parentDiv.find('input[name$="_srid"]').val(srid);

                    // Save the point as a "coded" string.  This is then parsed on 
                    // by the PointEditor.
                    var pointInput = parentDiv.find('#' + parentName);
                    var pointCodedString = genCodedPointString(lon, lat, srid);

                    pointInput.val(pointCodedString)

                    // Update textfield.
                    var pointAsString = genPointString(lon, lat, srid);
                    parentDiv.find('#pointInputTextField').val(pointAsString);
                    $(this).dialog('close');
                }
            },
            Cancel: function() 
            {
                $(this).dialog('close');
            }
        }
    }) 
            
    $(document).ready(function()
    {
        // Guard against running the script multiple times.
        if (loaded)
        {
            return;
        }
        
        loaded = true;
        
        $(".pointEdit").each(function()
        {
            var lon = $(this).find('input[name$="_lon"]').val();
            var lat = $(this).find('input[name$="_lat"]').val();
            var srid = $(this).find('input[name$="_srid"]').val();

            // Save the point as a "coded" string.  This is then parsed on 
            // by the PointEditor.
            var parentName = $(this).attr("id");
            var pointInput = $(this).find('#' + parentName);
            var pointCodedString = genCodedPointString(lon, lat, srid);
            pointInput.val(pointCodedString);
            
            var pointAsString = genPointString(lon, lat, srid);
            var pointInputTextField = $(this).find('#pointInputTextField')
            pointInputTextField.val(pointAsString);
            
            //
            // Register click event listener.
            //
            pointInputTextField.click(function()
            {
                // Set the parent name so that in the dialog handling we can
                // refer back to it.
                parentName = $(this).parent().attr("id");
                
                // Find the exact dialog...
//                var editPointDialog = $('#dialog-form-edit-point').filter('[parent="' + parentName + '"]')
                var editPointDialog = $('.pointEditDialog').filter('[parent="' + parentName + '"]')
                
                // Update the dialog.
                var lon = $(this).parent().find('input[name$="_lon"]').val();
                var lat = $(this).parent().find('input[name$="_lat"]').val();
                var srid = $(this).parent().find('input[name$="_srid"]').val();
                
                // Clear css class in case there was an error previously.
                var editLon = editPointDialog.find('#editLon')
                editLon.parent().attr("class", "value")
                var editLat = editPointDialog.find('#editLat')
                editLat.parent().attr("class", "value")

                editLon.val(Math.abs(lon));
                // If longitude is non-negative, select 'E'
                if (lon >= 0)
                {
                    editPointDialog.find('#editEastWest').val('E')
                }
                else
                {
                    editPointDialog.find('#editEastWest').val('W')
                }
                
                editLat.val(Math.abs(lat));
                // If latitude is positive, select 'N'
                if (lat > 0)
                {
                    editPointDialog.find('#editNorthSouth').val('N')
                }
                else
                {
                    editPointDialog.find('#editNorthSouth').val('S')
                }

                editPointDialog.find('#editSrid').val(srid);

//                $('#dialog-form-edit-point').dialog('open');
                editPointDialog.dialog('open');
            })
        });
    })
});

function genPointString(lon, lat, srid)
{
    // Construct the string from lon, lat, srid values.
    var pointAsString = "";
    
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

function genCodedPointString(lon, lat, srid)
{
    return "POINT(" + lon + " " + lat + ") , " + srid;
}

function getName(srid)
{
    return $("#editSrid option[value='" + srid + "']").first().text()
}
