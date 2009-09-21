function upload(){
         	
	var model1 = document.getElementById('model1').xfElement;
	var binding = document.getElementById('success_message').xfElement.binding;
	var result = binding.evaluate(model1.getInstanceDocument('inst_response'));
	if(result.length > 0){
		alert(result[0].nodeValue);
	}else{
		alert("no new feature id could be obtained");
	}

}



