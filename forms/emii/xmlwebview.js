
    var xmlDoc;
    var tags = new Array();
    var current_tree_node = null;
    var max_table_rows = 100;
    var display_position = 1;
    var nodes = null;
    function display(uri,rows_path,namespaces) {
       try{
           xmlDoc = Sarissa.getDomDocument();
          // the following two lines are needed for IE
          xmlDoc.setProperty("SelectionNamespaces", namespaces);
          xmlDoc.setProperty("SelectionLanguage", "XPath");
          xmlDoc.async = false;
          xmlDoc.load(uri);

          nodes = xmlDoc.selectNodes( rows_path );
          //alert(nodes.length);
          //display feature member list
          build_tree();

          //parse feature members for properties list
          for(var i=0;i<nodes.length && i<100;i++){
             find_node_names(nodes[i],tags);
          }
          //create properties table header
          var table = document.getElementById("xmlwebview_table");
          var head = table.getElementsByTagName("thead")[0];
          var header = document.createElement("tr");
          head.appendChild(header);
          header.appendChild(document.createElement("th"));
          for(var name in tags){
             var col = document.createElement("th");
             col.innerHTML = tags[name][1];
             header.appendChild(col);
          }
          //fill table
          var body = table.getElementsByTagName("tbody")[0];
          for(var i=0;i<nodes.length && i<max_table_rows;i++){
             var row = document.createElement("tr");
             body.appendChild(row);
             row.appendChild(document.createElement("td")).innerHTML = i+1;
             for(var name in tags){
                var cell = document.createElement("td");
                if(tags[name][0]==Node.TEXT_NODE){
                   var tnodes = nodes[i].selectNodes(name);
		   if(tnodes.length>0 && tnodes[0].childNodes.length>0){
                      cell.innerHTML = tnodes[0].childNodes[0].nodeValue;
                      cell.className = "value";
                   }
                   else{
                      cell.innerHTML = "";
                      cell.className = "";
                   }
                }
                else{
                   cell.innerHTML =  "<div class='node'>"+name+"</div>";
                   cell.className = "node";
                }
                row.appendChild(cell);
             }
          }
          //load remaining rows into tree in batches
          //so that user can see and interact with screen
          window.setTimeout(build_tree,1000);
       }
       catch(e){
          alert(e.message);
       }
    }

    /**
     
     */
    function build_tree(){
       var tree = document.getElementById("xmlwebview_tree");
       for(var i=0;display_position<=nodes.length && i<1000;i++){
          var div = document.createElement("div");
          div.id = "tree_node_" + i;
          div.node = nodes[display_position-1];
          div.position = display_position;
          div.onclick = select_node;
          div.innerHTML = "<div class='node_number' onclick='open_node(this)'>"+display_position+"<img class='toggle' src='plus.gif'/></div>"+nodes[i].nodeName;
          div.className = "node tree_node";
          tree.appendChild(div);
          //create another div for tree
          var div = document.createElement("div");
          div.className = "node_tree_container";
          tree.appendChild(div);
          display_position++;
       }
       if(display_position<=nodes.length){
          window.setTimeout(build_tree,1000);
       }
    }

    function fill_table(start_pos){
       var table = document.getElementById("xmlwebview_table");
       var body = table.getElementsByTagName("tbody")[0];
       var node_idx = start_pos-1;
       for(var i=0;i<body.childNodes.length;i++){
          var row = body.childNodes[i];
          var cell_counter = 0;
          if(node_idx<nodes.length){
              row.childNodes[cell_counter++].innerHTML = node_idx+1;         
              for(var name in tags){   
                 var cell = row.childNodes[cell_counter++];
                 if(tags[name][0]==Node.TEXT_NODE){
                    var tnodes = nodes[node_idx].selectNodes(name);
                    if(tnodes.length>0){
                       cell.innerHTML = tnodes[0].childNodes[0].nodeValue;
                       cell.className = "";
                    }
                    else{
                       cell.innerHTML = "";
                       cell.className = "";
                    }
                 }
                 else{
                    cell.innerHTML =  name;
                    cell.className = "node";
                 }
              }
           }
           else{
              row.childNodes[cell_counter++].innerHTML = "";         
              for(var name in tags){   
                 var cell = row.childNodes[cell_counter++];
                 cell.innerHTML =  "&nbsp;";
                 cell.className = "";
              } 
           }
           node_idx++;
       }
    }


 /*   function fill_table(start_pos){
       var table = document.getElementById("table");
       var body = table.getElementsByTagName("tbody")[0];
       var node_idx = start_pos-1;
       for(var i=0;i<body.childNodes.length;i++){
          var row = body.childNodes[i];
          var cell_counter = 0;
          if(node_idx<nodes.length){
              row.childNodes[cell_counter++].innerHTML = node_idx+1;         
              for(var name in tags){   
                 var cell = row.childNodes[cell_counter++];
                 if(tags[name][0]==Node.TEXT_NODE){
                    var tnodes = nodes[node_idx].selectNodes(name);
                    if(tnodes.length>0 && tnodes[0].childNodes.length>0){
                       cell.innerHTML = tnodes[0].childNodes[0].nodeValue;
                       cell.className = "";
                    }
                    else{
                       cell.innerHTML = "";
                       cell.className = "";
                    }
                 }
                 else{
                    cell.innerHTML =  name;
                    cell.className = "node";
                 }
              }
           }
           else{
              row.childNodes[cell_counter++].innerHTML = "";         
              for(var name in tags){   
                 var cell = row.childNodes[cell_counter++];
                 cell.innerHTML =  "&nbsp;";
                 cell.className = "";
              } 
           }
           node_idx++;
       }
    }*/

    function find_node_names(node,names){    
       var nodes = node.selectNodes("*");
       for(var i=0;i<nodes.length;i++){
          if(nodes[i].nodeType == Node.ELEMENT_NODE){
             var name = nodes[i].nodeName;
             if(nodes[i].childNodes.length > 0 && nodes[i].childNodes[0].nodeType == Node.TEXT_NODE){
                //alert(name+"="+nodes[i].childNodes[0].nodeValue);
                if(!names[name]){
                   names[name] = [Node.TEXT_NODE,name,null];
                }
             }
             else{
                //go to next level
                if(!names[name]){
                   names[name] = [Node.ELEMENT_NODE,name,[]];
                   find_node_names(nodes[i],names[name][2]);
                }
             }
          }
          else if(nodes[i].nodeType == Node.ATTRIBUTE_NODE){
            
          }
       }
    }

    function select_node(){
       this.style.backgroundColor = 'red';
       if(current_tree_node != null){
          current_tree_node.style.backgroundColor = "#DDDDDD";
       }
       current_tree_node = this;
       fill_table(this.position);
    }

    function open_node(element){
       if(typeof(element.open) != 'undefined'){
          if(element.open == true){
             element.parentNode.nextSibling.style.display = "none";
             element.open = false;
          }
          else{
             element.parentNode.nextSibling.style.display = "block";
             element.open = true;
          }
       }
       else{
          var node = element.parentNode.node;
          //alert(new XMLSerializer().serializeToString(node));
          var list = document.createElement("ul");
          list.className = "mktree";
          //recursive routine to build sub-tree
          build_sub_tree(node, list, tags);
          element.parentNode.nextSibling.appendChild(list);
          element.parentNode.nextSibling.style.display = "block";
          processList(list);
          element.open = true;
       }
    }

    function build_sub_tree(xmlNode, listNode, levelTags){
       for(var name in levelTags){
          if(levelTags[name][0]==Node.TEXT_NODE){
             var xmlNodes = xmlNode.selectNodes(name);
	     if(xmlNodes.length>0 && xmlNodes[0].childNodes.length>0){ //create a list item
                var item = document.createElement("li");
                item.innerHTML = "<span style='font-weight:bold;'>"+name+": </span>"+xmlNodes[0].childNodes[0].nodeValue;
                listNode.appendChild(item);
             }
             else{
                  //empty node
             }
          }
          else{ //recurse    
             var xmlNodes = xmlNode.selectNodes(name);
             for(var i=0;i<xmlNodes.length;i++){
                var item = document.createElement("li");
                item.className = "liClosed";
                item.innerHTML = "<div class='node_tree_node'>"+name+"</div>";
                listNode.appendChild(item);
                var list = document.createElement("ul");
                build_sub_tree(xmlNodes[i], list, levelTags[name][2]);
                item.appendChild(list);
             }
          }
       }
    }

    function display_node(node){     
       alert(new XMLSerializer().serializeToString(node));
    }

    function toggle(){
       var tree = document.getElementById('xmlwebview_tree'); 
       if(tree.style.display == 'none'){
           tree.style.display = 'block';
	   document.getElementById('xmlwebview_toggle_button').value = '-';
       }else{
           tree.style.display = 'none';
	   document.getElementById('xmlwebview_toggle_button').value = '+';
       }
    }

