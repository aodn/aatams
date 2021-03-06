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
        height: 270,
        width: 525,
        modal: true,
        buttons: 
        {
            Ok: function() 
            {
                // Get a reference to the div with id of parentName.
                var pointName = $(this).attr("pointName");
                
                // Update the hidden field values.
                var editLon = $('#' + pointName + '_editLon')
                editLon.parent().attr("class", "value")
                var editLat = $('#' + pointName + '_editLat')
                editLat.parent().attr("class", "value")
                
                // Validate...
                if (!isValid(editLat, editLon))
               	{

               	}
                else
                {
                    var lon = editLon.val();
                    var lat = editLat.val();
                    
                    // Negate the lat and lon values if necessary.
                    if ($(this).find('#' + pointName + '_editEastWest').val() == 'W')
                    {
                        lon = -lon
                    }

                    if ($(this).find('#' + pointName + '_editNorthSouth').val() == 'S')
                    {
                        lat = -lat
                    }

                    var srid = $(this).find('#' + pointName + '_editSrid').val();

                    $('#' + pointName + '_lon').val(lon);
                    $('#' + pointName + '_lat').val(lat);
                    $('#' + pointName + '_srid').val(srid);

                    // Save the point as a "coded" string.  This is then parsed on 
                    // by the PointEditor.
                    var pointInput = $('#' + pointName);
                    var pointCodedString = genCodedPointString(lon, lat, srid);

                    pointInput.val(pointCodedString)

                    // Update textfield (some other scripts depend on the change event (e.g. rememberFormFields.js)).
                    var pointAsString = genPointString(lon, lat, srid);
                    $('#' + pointName + '_pointInputTextField').val(pointAsString).change();
                    
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
        
        initPoints()
    });
});

function isValid(editLat, editLon)
{
	var lat = removeTrailingDegreeSymbol(editLat);
	var lon = removeTrailingDegreeSymbol(editLon);
	
    if (("" != lat) && !isNumber(lat))
    {
        editLat.parent().attr("class", "value errors");
    	alert("Invalid latitude: " + lat + ", latitude must be expressed in decimal degrees.");
        editLat.focus();
        return false;
    }
    else if ((lat > 90) || (lat < -90))
    {
        editLat.parent().attr("class", "value errors");
        alert("Latitude must be in the range +/- 90");
        editLat.focus();
        return false;
    }
    else if (("" != lon) && !isNumber(lon))
    {
        editLon.parent().attr("class", "value errors");
    	alert("Invalid longitude: " + lon + ", longitude must be expressed in decimal degrees.");
        editLon.focus();
        return false;
    }
    else if ((lon > 180) || (lon < -180))
    {
        editLon.parent().attr("class", "value errors");
        alert("Longitude must be in the range +/- 180");
        editLon.focus();
        return false;
    }
    
    return true;
}

function removeTrailingDegreeSymbol(valueTextField)
{
	if (!valueTextField.val().endsWith("\u00b0"))
	{
		return valueTextField.val();
	}
	
	var trimmedVal = valueTextField.val().slice(0, -1);
	valueTextField.val(trimmedVal);
	return trimmedVal;
}

function initPoints()
{
    $(".pointEdit").each(function()
    {
        pointName = $(this).attr("pointName");
        
        var lon = $('#' + pointName + '_lon').val();
        var lat = $('#' + pointName + '_lat').val();
        var srid = $('#' + pointName + '_srid').val();

        // Save the point as a "coded" string.  This is then parsed on 
        // by the PointEditor.
        var pointInput = $('#' + pointName);
        var pointCodedString = genCodedPointString(lon, lat, srid);
        pointInput.val(pointCodedString);

        var pointAsString = genPointString(lon, lat, srid);
        var pointInputTextField = $('#' + pointName + '_pointInputTextField')
        pointInputTextField.val(pointAsString);

        pointInputTextField.bind('focus click', function()
        {
            showDialog($(this).parent())
        })
    });
}

function showDialog(pointDiv)
{
    // Set the parent name so that in the dialog handling we can
    // refer back to it.
    var pointName = pointDiv.attr("pointName");

    // Find the exact dialog...
    var editPointDialog = $('.pointEditDialog').filter('[pointName="' + pointName + '"]')

    // Update the dialog.
    var lon = $('#' + pointName + '_lon').val();
    var lat = $('#' + pointName + '_lat').val();
    var srid = $('#' + pointName + '_srid').val();
    
    // Clear css class in case there was an error previously.
    var editLon = $('#' + pointName + '_editLon')
    editLon.parent().attr("class", "value")
    var editLat = $('#' + pointName + '_editLat')
    editLat.parent().attr("class", "value")

    editLon.val(abs(lon));
    // If longitude is non-negative, select 'E'
    if (lon >= 0)
    {
        editPointDialog.find('#' + pointName + '_editEastWest').val('E')
    }
    else
    {
        editPointDialog.find('#' + pointName + '_editEastWest').val('W')
    }

    editLat.val(abs(lat));
    // If latitude is positive, select 'N'
    if (lat > 0)
    {
        editPointDialog.find('#' + pointName + '_editNorthSouth').val('N')
    }
    else
    {
        editPointDialog.find('#' + pointName + '_editNorthSouth').val('S')
    }

    editPointDialog.find('#' + pointName + '_editSrid').val(srid);
    editPointDialog.dialog('open');

    editLat.select();
}

function abs(val)
{
	if ((val == null) || (val == ""))
	{
		return null
	}
	
	return Math.abs(val)
}

function genPointString(lon, lat, srid)
{
    // Construct the string from lon, lat, srid values.
    var pointAsString = "";
    
    if ((String(lon) == "") || (String(lat) == "") || (String(srid) == ""))
    {
        return pointAsString;
    }
    
    pointAsString += Math.abs(lat) + "\u00b0"
    if (lat >= 0)
    {
        pointAsString += 'N'
    }
    else
    {
        pointAsString += 'S'
    }

    pointAsString += ' ' + Math.abs(lon) + "\u00b0"
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
    if ((String(lon) == "") || (String(lat) == "") || (String(srid) == ""))
    {
        return "";
    }
    
    return "POINT(" + lon + " " + lat + ") , " + srid;
}

function getName(srid)
{
    return $('#' + pointName + "_editSrid option[value='" + srid + "']").first().text()
}
