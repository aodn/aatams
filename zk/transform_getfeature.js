
var service = "../../deegree-wfs/services?service=WFS&version=1.1.0&request=GetFeature&namespace=xmlns(aatams=http://www.imos.org.au/aatams)";

function loadXMLDoc(dname){
	if(window.XMLHttpRequest){
		xhttp=new XMLHttpRequest();
	}else{
		xhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhttp.open("GET",dname,false);
	xhttp.send("");
	return xhttp.responseXML;
}

function displayResult(type,stylesheet)
{
	xml=loadXMLDoc(service+"&typename="+type);
	xsl=loadXMLDoc(stylesheet);
	//codeforIE
	if(window.ActiveXObject){
		ex=xml.transformNode(xsl);
		document.getElementById("content").innerHTML=ex;
	}
	//codeforMozilla,Firefox,Opera,etc.
	else if(document.implementation && document.implementation.createDocument){
		xsltProcessor = new XSLTProcessor();
		xsltProcessor.importStylesheet(xsl);
		resultDocument = xsltProcessor.transformToFragment(xml,document);
		document.getElementById("content").appendChild(resultDocument);
	}
	document.getElementById('waiting').style.display='none';
}
