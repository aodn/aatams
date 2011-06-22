$(function()
{
//    ${createLink(controller:"species", action:"lookupByName")}
    var dataSource = "/aatams/species/lookupByName"
    console.log("dataSource: " + dataSource)
    
    $("#speciesName").autocomplete({source:dataSource});
});
        
