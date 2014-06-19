package module

import geb.Module;

class PointEditDialog extends Module 
{
    static content =
    {
        rows { $("tr") }
        
        value { it.find("td.value").children()[0] }
        
        latitudeTextField { value(rows[0]) }
        longitudeTextField { value(rows[1]) }
        datumSelect { value(rows[2]) }
        
        okButton { $("span", text: "Ok") }
    }
}
