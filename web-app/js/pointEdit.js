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
        width: 300,
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
                var srid = $(this).find('#editSrid').val();
                
                parentDiv.find('input[name$="_lon"]').val(lon);
                parentDiv.find('input[name$="_lat"]').val(lat);
                parentDiv.find('input[name$="_srid"]').val(srid);

                // Save the point as a "coded" string.  This is then parsed on 
                // by the PointEditor.
                var pointInput = parentDiv.find('#' + parentName);
                var pointCodedString = genCodedPointString(lon, lat, srid);
                pointInput.val(pointCodedString)
//                console.log(pointInput);
                
                // Update textfield.
                var pointAsString = genPointString(lon, lat, srid);
                parentDiv.find('#pointInputTextField').val(pointAsString);
                $(this).dialog('close');

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
//            console.warn("pointEdit script is already loaded")
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
//            console.log("parentName: " + parentName);
            var pointInput = $(this).find('#' + parentName);
            var pointCodedString = genCodedPointString(lon, lat, srid);
            pointInput.val(pointCodedString);
//            console.log(pointInput);
            
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
                
                editPointDialog.find('#editLon').val(lon);
                editPointDialog.find('#editLat').val(lat);
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
    
    return pointAsString;
}

function genCodedPointString(lon, lat, srid)
{
    return "POINT(" + lon + " " + lat + ") , " + srid;
}
