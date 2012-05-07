$(document).ready(function () 
{
	$.blockUI.defaults.css = {

		padding:	0,
		margin:		0,
		top:		'40%',
		left:		'40%',
		textAlign:	'center',
		cursor:		'wait',
		

			
		'-webkit-border-radius': '10px',
		'-moz-border-radius':	 '10px',
		'border-radius': 		 '10px',
		
		border:         '3px solid #aaa', 
		backgroundColor:'#fff',
		opacity: 0.7,
		width: '150px',
		height: '150px'
	};
});

var blockUIConfig = new function() {
	this.defaultMessage = function() {
		return '<div style="position: relative; top: 50px"><img src="../images/spinner-big.gif" /><br/><br/><h3>Loading...</h3></div>';
	};	
}
