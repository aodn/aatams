      //global variables
      var wfs = '../../deegree-wfs/services';
      var feature_type = null;
      var params_div = null;
      var params_header_div = null;
      var predicate_count = 0;
      var pending_request = null;
      var pending_xslt = null;
      var pending_download = null;
      var is_count = false; 
      var help_open = false; //for ToggleHelp()
      var query_max = true; //for ToggleQuery()
      var citation_open = true; //for ToggleCitation()
      var show_filter = false; //for testing;

      /**
      Initialisation, called on body onload event
      */
      function Init(){
         try{
            //see if required json lists are available from the WFS server
            if(project_person_list == null){
                setTimeout('Init()',1000);
                return;
            } 
            document.getElementById('wfs_uri').value = wfs;
            //document.getElementById('access_date').innerHTML = new Date().format("d MMM yyyy");
            params_div = document.getElementById('params');
            if(!params_div)
               throw new Error("initialisation failed to find 'params' div element");
            params_header_div = document.getElementById('params_header');
            if(!params_header_div)
               throw new Error("initialisation failed to find 'params_header' div element");
            //is this page being setup via URI
            if(parent.location.search.match(/\?/)){
               SetPageFromSearch();
               Reset();
               //only load data if filtered
               if(parent.location.search.match(/\&/)){
                  DisplayData(parent.location.search.match(/count=true/i));
               }
            }
            else{
               SelectType(0);
               SelectFormat(0);
               Reset();
            }
         }
         catch(e){
            alert(e);
            throw(e);
         }
      }

      /**
      Controls the display of filter parameters for search type
      */
      function SetPageFromSearch(){
         var kvp = parent.location.search.replace(/^\?/,"").split('&');
         var pairs = new Array();
         var pair,i;
         for(i=0; i<kvp.length; i++){
            pair = kvp[i];
            pairs.push([pair.substring(0,pair.indexOf('=')).toLowerCase().replace(/\%20/g,'_'),pair.substring(pair.indexOf('=')+1)]);
         }
         //find _dataset, _max, _count, _format
         is_count = false;
         SelectFormat(0);
         var params = new Array();
         for(i=0; i<pairs.length; i++){
            if(pairs[i][0]=="_dataset"){
               switch(pairs[i][1]){
               case "deployments":
                  SelectType(0);
                  break;
               case "tag_releases":
                  SelectType(1);
                  break;
               case "receivers":
                  SelectType(2);
                  break;
               case "tags":
                  SelectType(3);
                  break;
               case "detections":
                  SelectType(4);
                  break;
               default:
                  throw new Error("unknown _dataset parameter value: " + pairs[i][1] + 
                        ", it should be one of (deployments, tag_releases, tags, receivers, detections)"); 
               }
            }
            else if(pairs[i][0]=="_max"){
               if(parseInt(pairs[i][1]))
                  document.getElementById('max_members').value = pairs[i][1];
               else
                  throw new Error("invalid non-integer _max parameter value: " + pairs[i][1]);
            }
            else if(pairs[i][0]=="_count"){
                      if(pairs[i][1].match(/true/i))
                  is_count=true;
               else if(pairs[i][1].match(/false/))
                  is_count=false;
               else
                  throw new Error("the _count parameter must be either 'true' or 'false'");
            }
            else if(pairs[i][0]=="_format"){
               switch(pairs[i][1]){
               case 'xml':
                  SelectFormat(2);
                  break;
               case 'html':
                  SelectFormat(0);
                  break;
               case 'csv':
                  SelectFormat(1);
                  break;
               default:
                  throw new Error("unknown _format parameter value: " + pairs[i][1] + ", it should be one of (xml, html, csv)"); 
               }
            }
            else if(pairs[i][0]!="")
               params.push(pairs[i]);
         }
         SetFilterParameters(params);
      }

      /**
      Sets filter parameters values associated with the selected type.
      */ 
      function SetFilterParameters(values){
         switch (feature_type) {
            case "deployments":
               SetDeploymentsFilter(values);
               break;
            case "tag_releases":
               SetTagReleasesFilter(values);
               break;
            case "receivers":
               SetReceiversFilter(values);
               break;
            case "tags":
               SetTagsFilter(values);
               break;
            case "detections":
               SetDetectionsFilter(values);
               break;
            default:
               throw new Error("unknown dataset type: " + feature_type);
         }
      }

      /**
      Controls the display of filter parameters for search types
      */
      function SetFeatureType(type){
         if(type)
            feature_type = type;
         else
            throw Error("search 'type' parameter is null");

         switch(feature_type){
         case "deployments":
            params_header_div.innerHTML = "Filter Parameters for RECEIVER DEPLOYMENTS";   
            SetDeploymentsFilterParametersHtml();
            break;
         case "tag_releases":
            params_header_div.innerHTML = "Filter Parameters for TAG RELEASES";
            SetTagReleasesFilterParametersHtml();
            break;
         case "tags":
            params_header_div.innerHTML = "Filter Parameters for TAGS";
            SetTagsFilterParametersHtml();
            break;
         case "receivers":
            params_header_div.innerHTML = "Filter Parameters for RECEIVERS";
            SetReceiversFilterParametersHtml();
            break;
         case "detections":
            params_header_div.innerHTML = "Filter Parameters for DETECTIONS";
            SetDetectionsFilterParametersHtml();
            break;
         default: throw new Error("unknown dataset type: " + feature_type);
         }
      }

      /**
      Get the dataset from the WFS
       */
      function DisplayData(do_count){
         if(feature_type == 'detections' && GetDetectionsFilter() == ''){
            alert("Sorry - You cannot send an unfiltered detections request!");
            return;
         }    
         try{
            var uri = document.getElementById('wfs_uri').value + BuildWFSRequest(do_count);
            switch(GetCheckedValue('output_format')){
            case 'xml':
               uri += "&outputformat=text%2fxml;%20subtype%3dgml%2f3.1.1";
               break;
            case 'html':
               uri += "&outputformat=text%2fxml;%20subtype%3dxhtml_cb";
               break;
            case 'csv':
               uri += "&outputformat=text%2fxml;%20subtype%3dcsv_cb";
               break;
            }
            pending_request = uri;
            parent.frames[1].location = "results.html";
         }
         catch(e){
            alert(e);
         }         
      }

      /**
       Builds the WFS key-value pairs Get request URL.
       */ 
      function BuildWFSRequest(do_count){
         predicate_count = 0;
         var filter;
         var uri = "?service=WFS&version=1.0.0&request=GetFeature&namespace=xmlns(aatams=http://www.imos.org.au/aatams)";
         switch(feature_type){
         case "deployments":
            uri += "&typename=aatams:receiver_deployment&propertyname=aatams:installation_ref/aatams:installation/aatams:name,aatams:station_ref,aatams:mooring_type_ref,aatams:acoustic_release,aatams:comments" + GetDeploymentsFilter();
            break;
         case "tag_releases":
            uri += "&typename=aatams:tag_release" + GetTagReleasesFilter();
            break;
         case "tags":
            uri += "&typename=aatams:device" + GetTagsFilter();
            break;
         case "receivers":
            uri += "&typename=aatams:device" + GetReceiversFilter();
            break;
         case "detections":
            uri += "&typename=aatams:detection" + GetDetectionsFilter();
            break;
         default: throw new Error("unknown search type: " + feature_type);
         }
         var max = parseInt(document.getElementById('max_members').value);
         if(max && max > 0)
            uri += "&maxfeatures=" + max;
         if(do_count) 
            uri += "&resultType=hits";
         document.getElementById('query').value=uri;
         return uri;
      }

      /**
      Resets Download request
      */
      function Reset(){
         try{
            var uri = document.getElementById('wfs_uri').value;
            if(document.getElementById('zipped').checked === true){
                uri = uri.replace(/services/, "services/zip") + BuildWFSRequest(false);
            }
            else{
                uri = uri.replace(/services/, "services/text") + BuildWFSRequest(false);
            }

            switch(GetCheckedValue('output_format')){
            case 'xml':
               uri += "&outputformat=text%2fxml;%20subtype%3dgml%2f3.1.1";
               break;
            case 'html':
               uri += "&outputformat=text%2fxml;%20subtype%3dxhtml";
               break;
            case 'csv':
               uri += "&outputformat=text%2fxml;%20subtype%3dcsv";
               break;
            }
            pending_download = uri;
         }
         catch(e){
            alert(e);
         }
      }

      /**
      Get the data from server as an attachment
       */ 
      function DownloadData(){
         if(feature_type == 'detections' && GetDetectionsFilter() == ''){
            alert("Sorry - You cannot send an unfiltered detections request!");
            return;
         } 
         if(pending_download != null){ window.location.href = pending_download; }
      }

      /**
      Inserts deployments search parameters markup
       */
      function SetDeploymentsFilterParametersHtml(){
         params_div.innerHTML = "<table class='params'><tbody>" +
            "<tr><td class='param_name'>Installation:</td><td>" + OperatorList('=','installation_id_operator') + 
               " " + InstallationsSelectList() + "</td></tr>" +
            "<tr><td class='param_name'>Installation Station:</td><td>" + OperatorList('=','installation_station_id_operator') + 
               " " + InstallationStationsSelectList(false) + "</td></tr>" +
            "<tr><td class='param_name'>Receiver Deployment ID:</td><td>" + OperatorList('=','deployment_id_operator') + 
               " <input id='deployment_id' type='text' size='10' onblur='Reset()'/></td></tr>" +
            "<tr><td class='param_name'>Receiver ID: </td><td>" + OperatorList('=|<=|>=|<|>','receiver_id_operator') +
               " <input id='receiver_id' type='text' size='10' onblur='Reset()'/></td></tr>" +
            "<tr><td class='param_name'>Receiver Model:</td><td class='last_row'>" + OperatorList('=','receiver_model_name_operator') +
               " " + ReceiverModelSelectList() + "</td></tr>" +
            "<tr><td class='param_name'>Receiver Code Name:</td><td>" + OperatorList('=','receiver_name_operator') +
               " <input id='receiver_name' type='text' size='20'/></td></tr>" +
            "<tr><td class='param_name last_row'>Location Bounding Box: </td><td class='last_row'>" + LocationBoundingBox() + "</td></tr>" +
            "</tbody></table>";
      }

      /**
      Sets deployments search parameters
       */
      function SetDeploymentsFilter(values){
         for(var i=0; i<values.length; i++){
            switch(values[i][0]){
            case "installation_id":
               document.getElementById("installation_id").value = values[i][1];
               break;
            case "installation_id_operator":
               document.getElementById("installation_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "deployment_id":
               document.getElementById("deployment_id").value = values[i][1];
               break;
            case "deployment_id_operator":
               document.getElementById("deployment_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "bbox":
               SetLocationBoundingBox(values[i][1]);
               break;
            default:
               throw new Error("unknown deployments filter parameter: " + values[i][0]);
            }
         }
      }

      /**
      Inserts detections search parameters markup
       */
      function SetDetectionsFilterParametersHtml(){
         params_div.innerHTML = "<table class='params'><tbody>" +
            //installation            
            "<tr><td class='param_name'>Installation Name:</td><td>" + OperatorList('=','installation_id_operator') + 
               " " + InstallationsSelectList(true) + "</td></tr>" +        
            "<tr><td class='param_name'>Installation Station:</td><td>" + OperatorList('=','installation_station_id_operator') + 
               " " + InstallationStationsSelectList(false) + "</td></tr>" +              
            //receiver
            "<tr><td class='param_name'>Receiver Deployment ID:</td><td>" + OperatorList('=|<=|>=|<|>','deployment_id_operator') +
               " <input id='deployment_id' type='text' size='10' onblur='Reset()'/></td></tr>" +
            "<tr><td class='param_name'>Receiver ID:</td><td>" + OperatorList('=|<=|>=|<|>','receiver_id_operator') +
               " <input id='receiver_id' type='text' size='10' onblur='Reset()'/></td></tr>" +
            "<tr><td class='param_name'>Receiver Code Name:</td><td>" + OperatorList('=|<=|>=|<|>','receiver_name_operator') +
               " <input id='receiver_name' type='text' size='10' onblur='Reset()'/></td></tr>" +
            //tag
            "<tr><td class='param_name'>Tag ID: </td><td>" + OperatorList('=|<=|>=|<|>','tag_id_operator') +
               " <input id='tag_id' type='text' size='10' onblur='Reset()' onblur='Reset()'/></td></tr>" +
            "<tr><td class='param_name'>Tag Code Name: </td><td>" + OperatorList('=|<=|>=|<|>','tag_name_operator') +
               " <input id='tag_name' type='text' size='10' onblur='Reset()' onblur='Reset()'/></td></tr>" +
            //release   
            "<tr><td class='param_name'>Tag Release ID:</td><td>" + OperatorList('=|<=|>=|<|>','release_id_operator') +
               " <input id='release_id' type='text' size='10' onblur='Reset()'/></td></tr>" +    
            "<tr><td class='param_name'>Release UTC DateTime:</td><td>" + OperatorList('>=|>','release_start_date_operator') + " " + DateSelect('release_start_date') + " and <br/>" +
                OperatorList('<=|<','release_end_date_operator') + " " + DateSelect('release_end_date') + "</td></tr>" + 
            "<tr><td class='param_name'>Release Location Bounding Box: </td><td>" + LocationBoundingBox('release') + "</td></tr>" +
            //classification            
            "<tr><td class='param_name'>Family:</td><td>" + OperatorList('=','family_id_operator') +
            " " + FamilySelectList() + "</td></tr>" +
            "<tr><td class='param_name'>Genus:</td><td>" + OperatorList('=','genus_id_operator') +
            " " + GenusSelectList(true) + "</td></tr>" +
            "<tr><td class='param_name'>Species:</td><td>" + OperatorList('=','species_id_operator') +
            " " + SpeciesSelectList(true) + "</td></tr>" +
            //detection
            "<tr><td class='param_name'>Detection ID:</td><td>" + OperatorList('=|<=|>=|<|>','detection_id_operator') +
               " <input id='detection_id' type='text' size='10' onblur='Reset()'/></td></tr>" +    
            "<tr><td class='param_name'>Detection UTC DateTime:</td><td>" + OperatorList('>=|>','detection_start_date_operator') + " " + DateSelect('detection_start_date') + " and <br/>" +
                OperatorList('<=|<','detection_end_date_operator') + " " + DateSelect('detection_end_date') + "</td></tr>" + 
            "<tr><td class='param_name last_row'>Detection Location Bounding Box: </td><td class='last_row'>" + LocationBoundingBox('detection') + "</td></tr>" +
            "</tbody></table>";      
      }

      /**
      Sets detections search parameters
       */
      function SetDetectionsFilter(values){
         for(var i=0; i<values.length; i++){
            switch(values[i][0]){
            case "deployment_id":
               document.getElementById("deployment_id").value = values[i][1];
               break;
            case "deployment_id_operator":
               document.getElementById("deployment_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "tag_id":
               document.getElementById("tag_id").value = values[i][1];
               break;
            case "tag_id_operator":
               document.getElementById("tag_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "detection_id":
               document.getElementById("detection_id").value = values[i][1];
               break;
            case "detection_id_operator":
               document.getElementById("detection_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "start_date":
               SetDateSelect('start_date',values[i][1]);
               break;
            case "start_date_operator":
               document.getElementById("start_date_operator").value = DecodeOperator(values[i][1]);
               break;
            case "end_date":
               SetDateSelect("end_date",values[i][1]);
               break;
            case "end_date_operator":
               document.getElementById("end_date_operator").value = DecodeOperator(values[i][1]);
               break;
            case "bbox":
               SetLocationBoundingBox(values[i][1]);
               break;
            default:
               throw new Error("unknown detections filter parameter: " + values[i][0]);
            }
         }
      }

      /**
      Inserts detections search parameters markup
       */
      function SetTagReleasesFilterParametersHtml(){
         params_div.innerHTML = "<table class='params'><tbody>" +
            "<tr><td class='param_name'>Family:</td><td>" + OperatorList('=','family_id_operator') +
            " " + FamilySelectList() + "</td></tr>" +
            "<tr><td class='param_name'>Genus:</td><td>" + OperatorList('=','genus_id_operator') +
            " " + GenusSelectList(true) + "</td></tr>" +
            "<tr><td class='param_name'>Species:</td><td>" + OperatorList('=','species_id_operator') +
            " " + SpeciesSelectList(true) + "</td></tr>" +            
            "<tr><td class='param_name'>Tag ID: </td><td>" + OperatorList('=|<=|>=|<|>','tag_id_operator') +
               " <input id='tag_id' type='text' size='10' onblur='Reset()'/></td></tr>" +
            "<tr><td class='param_name'>Tag Code Name:</td><td>" + OperatorList('=','tag_name_operator') +
               " <input id='tag_name' type='text' size='20'/></td></tr>" +
            "<tr><td class='param_name'>Release UTC DateTime:</td><td>" + OperatorList('>=|>','start_date_operator') + " " + DateSelect('start_date') + " and <br/>" +
                OperatorList('<=|<','end_date_operator') + " " + DateSelect('end_date') + "</td></tr>" +
            "<tr><td class='param_name last_row'>Location Bounding Box: </td><td class='last_row'>" + LocationBoundingBox() + "</td></tr>" +
            "</tbody></table>";      
      }

      /**
      Sets detections search parameters
       */
      function SetTagReleasesFilter(values){
         for(var i=0; i<values.length; i++){
            switch(values[i][0]){
            case "species_id":
               document.getElementById("species_id").value = values[i][1];
               break;
            case "species_id_operator":
               document.getElementById("species_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "animal_id":
               document.getElementById("animal_id").value = values[i][1];
               break;
            case "animal_id_operator":
               document.getElementById("animal_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "surgery_id":
               document.getElementById("surgery_id").value = values[i][1];
               break;
            case "surgery_id_operator":
               document.getElementById("surgery_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "installation_id":
               document.getElementById("installation_id").value = values[i][1];
               break;
            case "installation_id_operator":
               document.getElementById("installation_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "deployment_id":
               document.getElementById("deployment_id").value = values[i][1];
               break;
            case "deployment_id_operator":
               document.getElementById("deployment_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "tag_id":
               document.getElementById("tag_id").value = values[i][1];
               break;
            case "tag_id_operator":
               document.getElementById("tag_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "tag_model":
               document.getElementById("tag_model_id").value = GetTagModelId(values[i][1]);
               break;
            case "tag_model_operator":
               document.getElementById("tag_model_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "tag_name":
               document.getElementById("tag_name").value = values[i][1];
               break;
            case "tag_name_operator":
               document.getElementById("tag_name_operator").value = DecodeOperator(values[i][1]);
               break;
            case "receiver_id":
               document.getElementById("receiver_id").value = values[i][1];
               break;
            case "receiver_id_operator":
               document.getElementById("receiver_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "receiver_name":
               document.getElementById("receiver_name").value = values[i][1];
               break;
            case "receiver_name_operator":
               document.getElementById("receiver_name_operator").value = DecodeOperator(values[i][1]);
               break;
            case "receiver_model":
               document.getElementById("receiver_model_id").value = GetReceiverModelId(values[i][1]);
               break;
            case "receiver_model_operator":
               document.getElementById("receiver_model_id_operator").value = DecodeOperator(values[i][1]);
               break
            case "start_date":
               SetDateSelect('start_date',values[i][1]);
               break;
            case "start_date_operator":
               document.getElementById("start_date_operator").value = DecodeOperator(values[i][1]);
               break;
            case "end_date":
               SetDateSelect("end_date",values[i][1]);
               break;
            case "end_date_operator":
               document.getElementById("end_date_operator").value = DecodeOperator(values[i][1]);
               break;
            case "bbox":
               SetLocationBoundingBox(values[i][1]);
               break;
            default:
               throw new Error("unknown detections filter parameter: " + values[i][0]);
            }
         }
      }

      /**
      Inserts tags search parameters markup
       */
      function SetTagsFilterParametersHtml(){

                params_div.innerHTML = "<table class='params'><tbody>" +
            //project
            "<tr><td class='param_name'>Project:</td><td>" + OperatorList('=','project_id_operator') +
               " " + ProjectsSelectList() + "</td></tr>" +
            "<tr><td class='param_name'>Person(Role):</td><td>" + OperatorList('=','project_person_id_operator') +
               " " + ProjectPersonsSelectList() + "</td></tr>" +
            //device
            "<tr><td class='param_name'>Device ID:</td><td>" + OperatorList('=|<=|>=|<|>','device_id_operator') +
               " <input id='device_id' type='text' size='10' onblur='Reset()'/></td></tr>" +
            "<tr><td class='param_name'>Device Name:</td><td>" + OperatorList('=','device_name_operator') +
               " <input id='device_name' type='text' size='20'/></td></tr>" +
            "<tr><td class='param_name last_row'>Model:</td><td class='last_row'>" + OperatorList('=','model_id_operator') +
               " " + TagModelSelectList() + "</td></tr>" +
               "</tbody></table>";
      }

      /**
      Sets tags search parameters
       */
      function SetTagsFilter(values){
         for(var i=0; i<values.length; i++){
            switch(values[i][0]){
            case "device_id":
               document.getElementById("device_id").value = values[i][1];
               break;
            case "device_id_operator":
               document.getElementById("device_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "device_name":
               document.getElementById("device_name").value = values[i][1];
               break;
            case "device_name_operator":
               document.getElementById("device_name_operator").value = DecodeOperator(values[i][1]);
               break;
            case "model_name":
               document.getElementById("model_id").value = GetTagModelId(values[i][1]);
               break;
            case "model_name_operator":
               document.getElementById("model_id_operator").value = DecodeOperator(values[i][1]);
               break;
            default:
               throw new Error("unknown tags filter parameter: " + values[i][0]);
            }
         }
      }

      /**
      Inserts receivers search parameters markup
      */
      function SetReceiversFilterParametersHtml(){
           params_div.innerHTML = "<table class='params'><tbody>" +
            //project
            "<tr><td class='param_name'>Project:</td><td>" + OperatorList('=','project_id_operator') +
               " " + ProjectsSelectList() + "</td></tr>" +
            "<tr><td class='param_name'>Person(Role):</td><td>" + OperatorList('=','project_person_id_operator') +
               " " + ProjectPersonsSelectList() + "</td></tr>" +
            //device
            "<tr><td class='param_name'>Device ID:</td><td>" + OperatorList('=|<=|>=|<|>','device_id_operator') +
               " <input id='device_id' type='text' size='10' onblur='Reset()'/></td></tr>" +
            "<tr><td class='param_name'>Device Name:</td><td>" + OperatorList('=','device_name_operator') +
               " <input id='device_name' type='text' size='20'/></td></tr>" +
            "<tr><td class='param_name last_row'>Model:</td><td class='last_row'>" + OperatorList('=','model_id_operator') +
               " " + ReceiverModelSelectList() + "</td></tr>" +
               "</tbody></table>";
      }

      /**
      Sets receivers search parameters
       */
      function SetReceiversFilter(values){
         for(var i=0; i<values.length; i++){
            switch(values[i][0]){
            case "device_id":
               document.getElementById("device_id").value = values[i][1];
               break;
            case "device_id_operator":
               document.getElementById("device_id_operator").value = DecodeOperator(values[i][1]);
               break;
            case "device_name":
               document.getElementById("device_name").value = values[i][1];
               break;
            case "device_name_operator":
               document.getElementById("device_name_operator").value = DecodeOperator(values[i][1]);
               break;
            case "model_name":
               document.getElementById("model_id").value = GetReceiverModelId(values[i][1]);
               break;
            case "model_name_operator":
               document.getElementById("model_id_operator").value = DecodeOperator(values[i][1]);
               break;
            default:
               throw new Error("unknown receivers filter parameter: " + values[i][0]);
            }
         }
      }

      /*** List Generation functions ***/

      /**
      Makes a list of Operators by splitting symbols param
      */
      function OperatorList(symbols, id){
         var operators = symbols.split("|");
         var tmp = "<select id='" + id + "' onblur='Reset()' class='operator_list'>";
         for(var i = 0; i<operators.length; i++){
            if(operators[i] == '=' )
               tmp += "<option value='1' selected='selected'>=</option>";
            else if(operators[i] == '<=')
               tmp += "<option value='2'>&lt;=</option>";
            else if(operators[i] == '>=')
               tmp += "<option value='3'>&gt;=</option>";
            else if(operators[i] == '<')
               tmp += "<option value='4'>&lt;</option>";
            else if(operators[i] == '>')
               tmp += "<option value='5'>&gt;</option>";
            else if(operators[i] == '<>')
               tmp += "<option value='6'>&lt;&gt;</option>";
            else if(operators[i] == 'NULL')
               tmp += "<option value='7'>NULL</option>";
         }
         tmp += "</select>";
         return tmp;
      }

      /**
       Decodes from html encoding an operator symbol
       */
       function DecodeOperator(operator){
         operator = operator.replace(/%3E/,">");
         operator = operator.replace(/%3C/,"<");
         if(operator === '=' )
            return 1;
         else if(operator === '<=')
            return 2;
         else if(operator === '>=')
            return 3;
         else if(operator === '<')
            return 4;
         else if(operator === '>')
            return 5;
         else if(operator === '<>')
            return 6;
         else if(operator === 'NULL')
            return 7;
         return operator;
       }

      /**
      Inserts an Projects list
      */
      function ProjectsSelectList(){
         var tmp = "<select id='project_id' onblur='Reset()'  onchange='ResetProjectPersons(this.value)'>" + 
            "<option value=''></option>" 
         for(var i=0; i<project_list.length;i++){
            tmp += "<option value="+project_list[i][0]+">"+project_list[i][1]+"</option>";
         }
         return tmp += "</select>";
      }

      /**
      Inserts an PersonRoles list
      */
      function ProjectPersonsSelectList(){
          return "<select id='project_person_id' onblur='Reset()'>" + 
            "<option value=''></option></select>";
      }

      /**
       Resets the ProjectPersons List to be those in a particular Project
       */
      function ResetProjectPersons(project){
         var person_roles = document.getElementById('project_person_id');
         person_roles.options.length = 0;
         person_roles.options[0] = new Option('','');
         for(var i=0; i<project_person_list.length; i++){
            if(project_person_list[i][2]==project){
               person_roles.options[person_roles.options.length] = new Option(project_person_list[i][1], project_person_list[i][0]);
            }
         }
      }
 
      /**
      Inserts an Receivers list
      */
      function ReceiversSelectList(){
         var tmp = "<select id='receiver_id' onblur='Reset()'>" + 
            "<option value=''></option>" 
         for(var i=0; i<receiver_device_list.length;i++){
            tmp += "<option value="+receiver_device_list[i][0]+">"+receiver_device_list[i][1]+"</option>";
         }
         return tmp += "</select>";
      }

      /**
      Inserts an Tags list
      */
      function TagsSelectList(){
         var tmp = "<select id='tag_id' onblur='Reset()'>" + 
            "<option value=''></option>" 
         for(var i=0; i<transmitter_device_list.length;i++){
            tmp += "<option value="+transmitter_device_list[i][0]+">"+transmitter_device_list[i][1]+"</option>";
         }
         return tmp += "</select>";
      }


      /**
      Inserts an Installations list
      */
      function InstallationsSelectList(skip_blank){
         var tmp = "<select id='installation_id' onblur='Reset()' onchange='ResetStations(this.value)'>";
         //if(!skip_blank){
           tmp += "<option value=''></option>" 
         //}
         for(var i=0; i<installation_list.length;i++){
            tmp += "<option value="+installation_list[i][0]+">"+installation_list[i][1]+"</option>";
         }
         return tmp += "</select>";
      }

      /**
       Resets the Stations List to be those in a particular Installation
       */
      function ResetStations(installation){
         var stations = document.getElementById('installation_station_id');
         stations.options.length = 0;
         stations.options[0] = new Option('','');
         for(var i=0; i<installation_list.length; i++){
            if(installation_list[i][0]==installation){
               var tmp = installation_list[i][2];            
               for(var j=0; j<tmp.length; j++){            
                  stations.options[stations.options.length] = new Option(tmp[j][1], tmp[j][0]);
               }   
            }
         }
      }

      /**
      Inserts an Installation Stations list
      */
      function InstallationStationsSelectList(fill){
         var tmp = "<select id='installation_station_id' onblur='Reset()'>" + 
            "<option value=''></option>" ;
         if(fill){
            for(var i=0; i<installation_station_list.length;i++){
               tmp += "<option value="+installation_station_list[i][0]+">"+installation_station_list[i][1]+"</option>";
            }
         }
         return tmp += "</select>";
      }

      /**
      Inserts a Family list
      */
      function FamilySelectList(){
         var tmp = "<select id='family_id' onblur='Reset()' onchange='ResetGeneraSelectList(this.value)'>" + 
            "<option value=''></option>"; 
         for(var i=0; i<family_list.length;i++){
            tmp += "<option value="+family_list[i][0]+">"+family_list[i][1]+"</option>";
         }
         return tmp += "</select>";
      }

      /**
      Resets the Genus list based on selected family
      */
      function ResetGeneraSelectList(family){
         var genera = document.getElementById('genus_id');
         genera.options.length = 0;
         genera.options[0] = new Option('','');
         for(var i=0; i<genus_list.length; i++){
            if(family != ''){
               if(genus_list[i][2]==family){
                  genera.options[genera.options.length] = new Option(genus_list[i][1], genus_list[i][0]);
               }
            }else{
               genera.options[genera.options.length] = new Option(genus_list[i][1], genus_list[i][0]);
            }
         }
         ResetSpeciesSelectList('');
      }

      /**
      Inserts a Genus list
      */
      function GenusSelectList(fill){
         var tmp = "<select id='genus_id' onblur='Reset()'  onchange='ResetSpeciesSelectList(this.value)'>" + 
            "<option value=''></option>";
         if(fill){
            for(var i=0; i<genus_list.length;i++){
               tmp += "<option value="+genus_list[i][0]+">"+genus_list[i][1]+"</option>";
            }
         }
         return tmp += "</select>";
      }

      /**
      Resets the Species list based on selected genus
      */
      function ResetSpeciesSelectList(genus){
         var species = document.getElementById('species_id');
         species.options.length = 0;
         species.options[0] = new Option('','');
         for(var i=0; i<species_list.length; i++){
            if(genus != ''){ 
               if(species_list[i][2]==genus){
                  species.options[species.options.length] = new Option(species_list[i][1], species_list[i][0]);
               }
            } else {
               species.options[species.options.length] = new Option(species_list[i][1], species_list[i][0]);
            }
         }
      }

      /**
      Inserts a Genus list
      */
      function SpeciesSelectList(fill){
         var tmp = "<select id='species_id' onblur='Reset()'>" + 
            "<option value=''></option>";
         if(fill){
            for(var i=0; i<species_list.length;i++){
               tmp += "<option value="+species_list[i][0]+">"+species_list[i][1]+"</option>";
            }
         }
         return tmp += "</select>";
      }

      /**
      Inserts a Tag Model list
      */
      function TagModelSelectList(){
         var tmp = "<select id='tag_model_id' onblur='Reset()'>" + 
            "<option value=''></option>" 
         for(var i=0; i<transmitter_model_list.length;i++){
            tmp += "<option value="+transmitter_model_list[i][0]+">"+transmitter_model_list[i][1]+"</option>";
         }
         return tmp += "</select>";
      }

      /**
       Gets the id for the tag model name
      */
      function GetTagModelId(name){
         if(name){
            name = name.toUpperCase();
            for(var i=0; i<transmitter_model_list.length; i++){
               if(transmitter_model_list[i][1].toUpperCase() === name){
                  return transmitter_model_list[i][0];
               }
            }
            return "";
         }
         else
            throw new Error("name parameter of GetTagModelId is empty");
      }


      /**
      Inserts an Receiver Model list
      */
      function ReceiverModelSelectList(){
         var tmp = "<select id='receiver_model_name' onblur='Reset()'>" + 
            "<option value=''></option>" 
         for(var i=0; i<receiver_model_list.length;i++){
            tmp += "<option value="+receiver_model_list[i][1]+">"+receiver_model_list[i][1]+"</option>";
         }
         return tmp += "</select>";
      }

      /**
       Gets the id for the receiver model name
      */
      function GetReceiverModelId(name){
         if(name){
            name = name.toUpperCase();
            for(var i=0; i<receiver_model_list.length; i++){
               if(receiver_model_list[i][1].toUpperCase() === name){
                  return receiver_model_list[i][0];
               }
            }
            return "";
         }
         else
            throw new Error("name parameter of GetTagModelId is empty");
      }

      /**
      Makes a date selection group (input + calendar + validation)
      */   
      function DateSelect(id){
         return "<span class='date_group'>" + YearSelectList(id) + "-" + MonthSelectList(id) + "-" + DaySelectList(id) + "&#160;&#160;" + Hour24SelectList(id) + ":" +
                MinutesSelectList(id) + ":" + SecondsSelectList(id) + "</span>";     
      }


      /**
       Set respective parts of date-time select
       */
      function SetDateSelect(id, value){
         if(id != null && value != null){
            var tmp = value.replace(/\%20/,' ');
            tmp = tmp.replace(/-/g,'/');
            tmp = tmp.replace(/[,;]/g,':');
            tmp = tmp.replace(/T/ig,' ');
            var dateTime;
            //enforce dd/MMM/yyyy to avoid system setting problem
            var regex = new RegExp(/\/\d{1,2}\//);
            var match = regex.exec(tmp);
            if(match != null){ 
               if (match.length > 0 && parseInt(match[0].replace(/\//,'')) < 13) {
                  tmp = tmp.replace(/\/(1|01)\//,'/Jan/');
                  tmp = tmp.replace(/\/(2|02)\//,'/Feb/');
                  tmp = tmp.replace(/\/(3|03)\//,'/Mar/');
                  tmp = tmp.replace(/\/(4|04)\//,'/Apr/');
                  tmp = tmp.replace(/\/(5|05)\//,'/May/');
                  tmp = tmp.replace(/\/(6|06)\//,'/Jun/');
                  tmp = tmp.replace(/\/(7|07)\//,'/Jul/');
                  tmp = tmp.replace(/\/(8|08)\//,'/Aug/');
                  tmp = tmp.replace(/\/(9|09)\//,'/Sep/');
                  tmp = tmp.replace(/\/10\//,'/Oct/');
                  tmp = tmp.replace(/\/11\//,'/Nov/');
                  tmp = tmp.replace(/\/12\//,'/Dec/');
               }
            }
            else{ //check for a valid character month
               var regex = new RegExp(/\/(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\//i);
               var match = regex.exec(tmp);
               if(match == null){
                  throw new Error("cannot find a valid month in date: " + value);
               }
            }
            //see if we have a valid date now
            dateTime = new Date(tmp);
            if(dateTime){
               var year = new String(dateTime.getFullYear());
               var month = new String(dateTime.getMonth()+1);
               var day = new String(dateTime.getDate());
               var hours = new String(dateTime.getHours());
               var minutes = new String(dateTime.getMinutes());
               var seconds = new String(dateTime.getSeconds());
               document.getElementById(id+"_year").value = year;
               document.getElementById(id+"_month").value = (month.length==1) ? "0"+month : month;
               document.getElementById(id+"_day").value = (day.length==1) ? "0"+day : day;
               document.getElementById(id+"_hour").value = (hours.length==1) ? "0"+hours : hours;
               document.getElementById(id+"_minute").value = (minutes.length==1) ? "0"+minutes : minutes;
               document.getElementById(id+"_second").value = (seconds.length==1) ? "0"+seconds : seconds;
            }else
               throw new Error("invalid date-time string: " + value);
         }
         else
            throw new Error("invalid parameters passed to SetDateSelect");
      }

      /**
   
      */   
      function YearSelectList(id){
         return "<select id='" + id + "_year'>" +
            "<option value=''></option>" +
            "<option value='2006'>2006</option>" +
            "<option value='2007'>2007</option>" +
            "<option value='2008'>2008</option>" +
            "<option value='2009'>2009</option>" +           
            "</select>";
      }   
      /**
      
      */   
      function MonthSelectList(id){
         return "<select id='" + id + "_month'>" +
            "<option value=''></option>" +            
            "<option value='01'>Jan</option>" +
            "<option value='02'>Feb</option>" +
            "<option value='03'>Mar</option>" +
            "<option value='04'>Apr</option>" +
            "<option value='05'>May</option>" +
            "<option value='06'>Jun</option>" +
            "<option value='07'>Jul</option>" +
            "<option value='08'>Aug</option>" +
            "<option value='09'>Sep</option>" +
            "<option value='10'>Oct</option>" +
            "<option value='11'>Nov</option>" +
            "<option value='12'>Dec</option>" +
            "</select>";
      }
      /**
      
      */   
      function DaySelectList(id){
         return "<select id='" + id + "_day'>" +
            "<option value=''></option>" +
            "<option value='01'>01</option>" +
            "<option value='02'>02</option>" +
            "<option value='03'>03</option>" +
            "<option value='04'>04</option>" +
            "<option value='05'>05</option>" +
            "<option value='06'>06</option>" +
            "<option value='07'>07</option>" +
            "<option value='08'>08</option>" +
            "<option value='09'>09</option>" +
            "<option value='10'>10</option>" +
            "<option value='11'>11</option>" +
            "<option value='12'>12</option>" +
            "<option value='13'>13</option>" +
            "<option value='14'>14</option>" +
            "<option value='15'>15</option>" +
            "<option value='16'>16</option>" +
            "<option value='17'>17</option>" +
            "<option value='18'>18</option>" +
            "<option value='19'>19</option>" +
            "<option value='20'>20</option>" +
            "<option value='21'>21</option>" +
            "<option value='22'>22</option>" +
            "<option value='23'>23</option>" +
            "<option value='24'>24</option>" +
            "<option value='25'>25</option>" +
            "<option value='26'>26</option>" +
            "<option value='27'>27</option>" +
            "<option value='28'>28</option>" +
            "<option value='29'>29</option>" +
            "<option value='30'>30</option>" +
            "<option value='31'>31</option>" +
            "</select>";
      }
      /**
      
      */   
      function Hour24SelectList(id){
         return "<select id='" + id + "_hour'>" +
            "<option value='00'>00</option>" +
            "<option value='01'>01</option>" +
            "<option value='02'>02</option>" +
            "<option value='03'>03</option>" +
            "<option value='04'>04</option>" +
            "<option value='05'>05</option>" +
            "<option value='06'>06</option>" +
            "<option value='07'>07</option>" +
            "<option value='08'>08</option>" +
            "<option value='09'>09</option>" +
            "<option value='10'>10</option>" +
            "<option value='11'>11</option>" +
            "<option value='12'>12</option>" +
            "<option value='13'>13</option>" +
            "<option value='14'>14</option>" +
            "<option value='15'>15</option>" +
            "<option value='16'>16</option>" +
            "<option value='17'>17</option>" +
            "<option value='18'>18</option>" +
            "<option value='19'>19</option>" +
            "<option value='20'>20</option>" +
            "<option value='21'>21</option>" +
            "<option value='22'>22</option>" +
            "<option value='23'>23</option>" +
            "</select>";      
      }
      /**
      
      */   
      function MinutesSelectList(id){
         return "<select id='" + id + "_minute'>" +
            "<option value='00'>00</option>" +
            "<option value='01'>01</option>" +
            "<option value='02'>02</option>" +
            "<option value='03'>03</option>" +
            "<option value='04'>04</option>" +
            "<option value='05'>05</option>" +
            "<option value='06'>06</option>" +
            "<option value='07'>07</option>" +
            "<option value='08'>08</option>" +
            "<option value='09'>09</option>" +
            "<option value='10'>10</option>" +
            "<option value='11'>11</option>" +
            "<option value='12'>12</option>" +
            "<option value='13'>13</option>" +
            "<option value='14'>14</option>" +
            "<option value='15'>15</option>" +
            "<option value='16'>16</option>" +
            "<option value='17'>17</option>" +
            "<option value='18'>18</option>" +
            "<option value='19'>19</option>" +
            "<option value='20'>20</option>" +
            "<option value='21'>21</option>" +
            "<option value='22'>22</option>" +
            "<option value='23'>23</option>" +
            "<option value='24'>24</option>" +
            "<option value='25'>25</option>" +
            "<option value='26'>26</option>" +
            "<option value='27'>27</option>" +
            "<option value='28'>28</option>" +
            "<option value='29'>29</option>" +
            "<option value='30'>30</option>" +
            "<option value='31'>31</option>" +
            "<option value='32'>32</option>" +
            "<option value='33'>33</option>" +
            "<option value='34'>34</option>" +
            "<option value='35'>35</option>" +
            "<option value='36'>36</option>" +
            "<option value='37'>37</option>" +
            "<option value='38'>38</option>" +
            "<option value='39'>39</option>" +
            "<option value='40'>40</option>" +
            "<option value='41'>41</option>" +
            "<option value='42'>42</option>" +
            "<option value='43'>43</option>" +
            "<option value='44'>44</option>" +
            "<option value='45'>45</option>" +
            "<option value='46'>46</option>" +
            "<option value='47'>47</option>" +
            "<option value='48'>48</option>" +
            "<option value='49'>49</option>" +
            "<option value='50'>50</option>" +
            "<option value='51'>51</option>" +
            "<option value='52'>52</option>" +
            "<option value='53'>53</option>" +
            "<option value='54'>54</option>" +
            "<option value='55'>55</option>" +
            "<option value='56'>56</option>" +
            "<option value='57'>57</option>" +
            "<option value='58'>58</option>" +
            "<option value='59'>59</option>" +
            "</select>";      
      }
      /**
      
      */   
      function SecondsSelectList(id){
         return "<select id='" + id + "_second'>" +
            "<option value='00'>00</option>" +
            "<option value='01'>01</option>" +
            "<option value='02'>02</option>" +
            "<option value='03'>03</option>" +
            "<option value='04'>04</option>" +
            "<option value='05'>05</option>" +
            "<option value='06'>06</option>" +
            "<option value='07'>07</option>" +
            "<option value='08'>08</option>" +
            "<option value='09'>09</option>" +
            "<option value='10'>10</option>" +
            "<option value='11'>11</option>" +
            "<option value='12'>12</option>" +
            "<option value='13'>13</option>" +
            "<option value='14'>14</option>" +
            "<option value='15'>15</option>" +
            "<option value='16'>16</option>" +
            "<option value='17'>17</option>" +
            "<option value='18'>18</option>" +
            "<option value='19'>19</option>" +
            "<option value='20'>20</option>" +
            "<option value='21'>21</option>" +
            "<option value='22'>22</option>" +
            "<option value='23'>23</option>" +
            "<option value='24'>24</option>" +
            "<option value='25'>25</option>" +
            "<option value='26'>26</option>" +
            "<option value='27'>27</option>" +
            "<option value='28'>28</option>" +
            "<option value='29'>29</option>" +
            "<option value='30'>30</option>" +
            "<option value='31'>31</option>" +
            "<option value='32'>32</option>" +
            "<option value='33'>33</option>" +
            "<option value='34'>34</option>" +
            "<option value='35'>35</option>" +
            "<option value='36'>36</option>" +
            "<option value='37'>37</option>" +
            "<option value='38'>38</option>" +
            "<option value='39'>39</option>" +
            "<option value='40'>40</option>" +
            "<option value='41'>41</option>" +
            "<option value='42'>42</option>" +
            "<option value='43'>43</option>" +
            "<option value='44'>44</option>" +
            "<option value='45'>45</option>" +
            "<option value='46'>46</option>" +
            "<option value='47'>47</option>" +
            "<option value='48'>48</option>" +
            "<option value='49'>49</option>" +
            "<option value='50'>50</option>" +
            "<option value='51'>51</option>" +
            "<option value='52'>52</option>" +
            "<option value='53'>53</option>" +
            "<option value='54'>54</option>" +
            "<option value='55'>55</option>" +
            "<option value='56'>56</option>" +
            "<option value='57'>57</option>" +
            "<option value='58'>58</option>" +
            "<option value='59'>59</option>" +
            "</select>";
      }

      /**
      Makes lat/long bounding box inputs
      */
      function LocationBoundingBox(id){ 
         if(id == null) id = "";
         return "<table class='lon_lat'>" + 
            "<tr><td>Long.</td><td><input id='" + id + "lon_1' type='text' size='6' onblur='ValidateLongitude(this);Reset();'/></td>" + 
            "<td><input id='" + id + "lon_2' type='text' size='6' onblur='ValidateLongitude(this);Reset();'/></td></tr>" +
            "<tr><td>Lat.</td><td><input id='" + id + "lat_1' type='text' size='6' onblur='ValidateLatitude(this);Reset();'/></td>" + 
            "<td><input id='" + id + "lat_2' type='text' size='6' onblur='ValidateLatitude(this);Reset();'/></td></tr>" +
            "</table><span id='location_status_message'/>";
      }

      /**
      Sets lat/long bounding box inputs
      */
      function SetLocationBoundingBox(value, id){
         var values = value.split(',');
         if(id == null) id = "";
         if(values.length==4){
            document.getElementById(id+'lon_1').value = values[0];
            document.getElementById(id+'lat_1').value = values[1];
            document.getElementById(id+'lon_2').value = values[2];
            document.getElementById(id+'lat_2').value = values[3];
         }
         else
            throw new Error("bbox parameter must have the format 'bbox=min_longitude,min_latitude,max_longitude,max_latitude'")
      }

      /**
      Is between -180 and 180 degrees
       */
      function ValidateLongitude(element){
         var value = element.value;
         var valid = true;
         if(value.match(/\S/g) && isNaN(value)){
            alert("value is not numeric");
            valid = false;
         }
         if(valid && (value > 180 || value < -180)){
            alert("invalid longitude value");
            valid = false;
         }
         if(!valid) element.value = "";
         return valid;
      }

      /**
      Is between -90 and 90 degrees
       */
      function ValidateLatitude(element){
         var value = element.value;
         var valid = true;
         if(value.match(/\S/g) && isNaN(value)){
            alert("latitude value is not numeric");
            valid = false;
         }
         if(valid && (value > 90 || value < -90)){
            alert("invalid longitude value");
            valid = false;
         }
         if(!valid) element.value = "";
         return valid;
      }

      /*** OGC filter clause generation functions ***/
      function GetDeploymentsFilter(){
         var element;
         var bbox;
         var filter = "";
         if(element = document.getElementById('installation_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:installation_ref/aatams:installation/@gml:id',
                            document.getElementById('installation_id_operator').value, value.replace('aatams.installation.',''));   
         }
         if(element = document.getElementById('installation_station_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:station_ref/aatams:station/@gml:id', document.getElementById('installation_station_id_operator').value, value.replace('aatams.installation_station.',''));
         }
         if(element = document.getElementById('deployment_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('@gml:id', document.getElementById('deployment_id_operator').value, value);
         }
         if(element = document.getElementById('receiver_id')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:receiver_device_ref/aatams:receiver_device/@gml:id', document.getElementById('receiver_id_operator').value, value);   
         }
         if(element = document.getElementById('receiver_model_name')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:receiver_device_ref/aatams:receiver_device/aatams:model_name', document.getElementById('receiver_model_name_operator').value, value);   
         }
         if(element = document.getElementById('receiver_name')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:receiver_device_ref/aatams:receiver_device/aatams:code_name', document.getElementById('receiver_name_operator').value, value);  
         }         
         if(bbox = GetBoundingBoxFilter('aatams:location')){
            filter += bbox;   
         }
         return BuildFilterKVP(filter);
      }

      function GetDetectionsFilter(){
         var element;
         var bbox;
         var filter = "";
         //installation
         if(element = document.getElementById('installation_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:installation_id',
                            document.getElementById('installation_id_operator').value, value.replace('aatams.installation.',''));   
         }
         if(element = document.getElementById('installation_station_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:station_id', document.getElementById('installation_station_id_operator').value,
                               value.replace('aatams.installation_station.',''));
         }
         //receiver deployment
         if(element = document.getElementById('deployment_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:deployment_id', document.getElementById('deployment_id_operator').value, value);
         }
         if(element = document.getElementById('receiver_id')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:receiver_id', document.getElementById('receiver_id_operator').value, value);   
         }
         if(element = document.getElementById('receiver_name')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:receiver_code_name', document.getElementById('receiver_name_operator').value, value);   
         }
         //tag
         if(element = document.getElementById('tag_id')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:tag_id', document.getElementById('tag_id_operator').value, value);   
         }
         if(element = document.getElementById('tag_name')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:tag_code_name', document.getElementById('tag_name_operator').value, value);   
         }
         //release
         if(element = document.getElementById('start_date')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:release_timetamp', document.getElementById('release_start_date_operator').value, DateTimeFormat(value));   
         }
         if(element = document.getElementById('end_date')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:release_timestamp', document.getElementById('release_end_date_operator').value, DateTimeFormat(value));   
         }
         if(bbox = GetBoundingBoxFilter('aatams:release_location','release')){
            filter += bbox;   
         }
         //classification
         var classification_filter = ""
         if(element = document.getElementById('species_id')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               classification_filter = Comparison('aatams:species_id', document.getElementById('species_id_operator').value, value.replace('aatams.species.',''));   
         }
         if(classification_filter == "" && (element = document.getElementById('genus_id'))){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               classification_filter = Comparison('aatams:genus_id', document.getElementById('genus_id_operator').value, value.replace('aatams.genus.',''));
         }
         if(classification_filter == "" && (element = document.getElementById('family_id'))){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               classification_filter = Comparison('aatams:family_id', document.getElementById('family_id_operator').value, value.replace('aatams.family.',''));
         }
         filter += classification_filter;
         //detection
         if(element = document.getElementById('detection_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:detectionId', document.getElementById('detection_id_operator').value, value);
         }
         if(element = document.getElementById('start_date')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:detectionUtcTimeStamp', document.getElementById('start_date_operator').value, DateTimeFormat(value));   
         }
         if(element = document.getElementById('end_date')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:detectionUtcTimeStamp', document.getElementById('end_date_operator').value, DateTimeFormat(value));   
         }
         if(bbox = GetBoundingBoxFilter('aatams:detection/aatams:location','detection')){
            filter += bbox;   
         }
         return BuildFilterKVP(filter);
      }

      function GetTagReleasesFilter(){
         var element;
         var value;
         var bbox;
         var filter = "";
         if(element = document.getElementById('species_id')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter = Comparison('aatams:species_ref/aatams:species/@gml:id', document.getElementById('species_id_operator').value, value.replace('aatams.species.',''));   
         }
         if(filter == "" && (element = document.getElementById('genus_id'))){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter = Comparison('aatams:genus_ref/aatams:genus/@gml:id', document.getElementById('genus_id_operator').value, value.replace('aatams.genus.',''));
         }
         if(filter == "" && (element = document.getElementById('family_id'))){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter = Comparison('aatams:family_ref/aatams:family/@gml:id', document.getElementById('family_id_operator').value, value.replace('aatams.family.',''));
         }
         if(element = document.getElementById('tag_name')){
            value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:tag_device_ref/aatams:tag_device/aatams:code_name', document.getElementById('tag_name_operator').value, value);   
         }
         if(value = GetDateTimeFilter('start_date')){
            if(value != "")
               filter += Comparison('aatams:release_timestamp', document.getElementById('start_date_operator').value, DateTimeFormat(value));   
         }
         if(value = GetDateTimeFilter('end_date')){
            if(value != "")
               filter += Comparison('aatams:release_timestamp', document.getElementById('end_date_operator').value, DateTimeFormat(value));   
         }
         if(bbox = GetBoundingBoxFilter('aatams:location')){
            filter += bbox;   
         }
         return BuildFilterKVP(filter);
      }

      function GetTagsFilter(){
         var filter =Comparison('aatams:device_type_ref/aatams:device_type/aatams:name', 1, 'TRANSMITTER');
         if(element = document.getElementById('project_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:project_person_ref/aatams:project_person/aatams:project_fid', document.getElementById('project_id_operator').value, value);
         }
         if(element = document.getElementById('project_person_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:project_person_ref/aatams:project_person/@gml:id', document.getElementById('project_person_id_operator').value, value.replace('aatams.project_person.',''));
         }
         if(element = document.getElementById('device_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('@gml:id', document.getElementById('device_id_operator').value, value);
         }
         if(element = document.getElementById('tag_model_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:device_model_ref/aatams:device_model/@gml:id', document.getElementById('model_id_operator').value, value.replace('aatams.transmitter_model.',''));
         }
         if(element = document.getElementById('device_name')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:code_name', document.getElementById('device_name_operator').value, value);
         }
         return BuildFilterKVP(filter);
      }

      function GetReceiversFilter(){
         var filter =Comparison('aatams:device_type_ref/aatams:device_type/aatams:name', 1, 'RECEIVER');
         if(element = document.getElementById('project_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:project_person_ref/aatams:project_person/aatams:project_fid', document.getElementById('project_id_operator').value, value);
         }
         if(element = document.getElementById('project_person_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:project_person_ref/aatams:project_person/@gml:id', document.getElementById('project_person_id_operator').value, value.replace('aatams.project_person.',''));
         }
         if(element = document.getElementById('device_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('@gml:id', document.getElementById('device_id_operator').value, value);
         }
         if(element = document.getElementById('tag_model_id')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:device_model_ref/aatams:device_model/@gml:id', document.getElementById('model_id_operator').value, value.replace('aatams.receiver_model.',''));
         }
         if(element = document.getElementById('device_name')){
            var value = element.value.replace(/^\s+|\s+$/g, '');
            if(value != "")
               filter += Comparison('aatams:code_name', document.getElementById('device_name_operator').value, value);
         }
         return BuildFilterKVP(filter);
      }

      /**
      Constructs the OGC Filter KVP for restricting the features returned
      */
      function BuildFilterKVP(filter){
         if(filter != null && filter.match(/\S/g)){
            if(predicate_count > 1)
                                   filter = "<ogc:And>" + filter + "</ogc:And>";
            filter = "&filter=<ogc:Filter xmlns:ogc='http://www.opengis.net/ogc' xmlns:aatams='http://www.imos.org.au/aatams' xmlns:gml='http://www.opengis.net/gml'>" + filter + "</ogc:Filter>";
            if(show_filter){ alert(filter); }
            return filter.replace(/\s/g, '%20');
         }
         else
            return "";
      }

      /**
      Generates an OGC filter comparison clause
      */
      function Comparison(name, operator, value){
         predicate_count++;
         switch(parseInt(operator)){
         case 1:
            return "<ogc:PropertyIsEqualTo><ogc:PropertyName>" + name + "</ogc:PropertyName>" +
               "<ogc:Literal>" + Sanitize(value) + "</ogc:Literal></ogc:PropertyIsEqualTo>";
            break;
         case 2:
            return "<ogc:PropertyIsLessThanOrEqualTo><ogc:PropertyName>" + name + "</ogc:PropertyName>" +
               "<ogc:Literal>" + Sanitize(value) + "</ogc:Literal></ogc:PropertyIsLessThanOrEqualTo>";
            break;
         case 3:
            return "<ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>" + name + "</ogc:PropertyName>" +
               "<ogc:Literal>" + Sanitize(value) + "</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo>";
            break;
         case 4:
            return "<ogc:PropertyIsLessThan><ogc:PropertyName>" + name + "</ogc:PropertyName>" +
               "<ogc:Literal>" + Sanitize(value) + "</ogc:Literal></ogc:PropertyIsLessThan>";
            break;
         case 5:
            return "<ogc:PropertyIsGreaterThan><ogc:PropertyName>" + name + "</ogc:PropertyName>" +
               "<ogc:Literal>" + Sanitize(value) + "</ogc:Literal></ogc:PropertyIsGreaterThan>";
            break;
         case 6:
            return "<ogc:PropertyIsNotEqualTo><ogc:PropertyName>" + name + "</ogc:PropertyName>" +
               "<ogc:Literal>" + Sanitize(value) + "</ogc:Literal></ogc:PropertyIsNotEqualTo>";
            break;
         case 7:
            return "<ogc:PropertyIsNull><ogc:PropertyName>" + name + "</ogc:PropertyName></ogc:PropertyIsNull>";
            break;            
         case 8:
            return "<ogc:GmlObjectId gml:id='" + Sanitize(value) + "'/>";
            break;            
         default:
            throw new Error("unknown operator symbol: " + operator);
         }
      }

      /**
      Generates an OGC Bounding Box filter clause
       */
      function GetBoundingBoxFilter(name, prefix){
         var element;
         var lat_1 = '';
         var lat_2 = '';
         var lon_1 = '';
         var lon_2 = '';
         prefix = (prefix) ? prefix : "";
         if(element = document.getElementById(prefix+'lat_1'))
            lat_1 = element.value;
         else
            throw new Error(prefix+'lat_1 element not found');
         if(element = document.getElementById(prefix+'lat_2'))
            lat_2 = element.value;
         else
            throw new Error(prefix+'lat_2 element not found');
         if(element = document.getElementById(prefix+'lon_1'))
            lon_1 = element.value;
         else
            throw new Error(prefix+'lon_1 element not found');
         if(element = document.getElementById(prefix+'lon_2'))
            lon_2 = element.value;
         else
            throw new Error(prefix+'lon_2 element not found');
         //are any values set?
         if(lat_1.match(/\S/g) || lat_2.match(/\S/g) || lon_1.match(/\S/g) || lon_2.match(/\S/g) ){
            //are all values set?
            if(lat_1.match(/\S/g) && lat_2.match(/\S/g) && lon_1.match(/\S/g) && lon_2.match(/\S/g) ){
               predicate_count++;
               var v1, v2;
               //find smallest lat.
               v1 = parseFloat(lat_1);
               v2 = parseFloat(lat_2);
               if(v1 < v2){
                  lat_1 = v1;
                  lat_2 = v2;
               }
               else{
                  lat_1 = v2,
                  lat_2 = v1;
               }
               //find smallest lon.
               v1 = parseFloat(lon_1);
               v2 = parseFloat(lon_2);
               if(v1 < v2){
                  lon_1 = v1;
                  lon_2 = v2;
               }
               else{
                  lon_1 = v2,
                  lon_2 = v1;
               }
               document.getElementById("location_status_message").innerHTML = "";
               //build the filter clause
               return "<ogc:BBOX><ogc:PropertyName>" + name + "</ogc:PropertyName><gml:Envelope>" + 
                  "<gml:lowerCorner>" + lon_1 + " " + lat_1 + "</gml:lowerCorner>" + 
                  "<gml:upperCorner>" + lon_2 + " " + lat_2 + "</gml:upperCorner></gml:Envelope></ogc:BBOX>";

            }
            else{
               document.getElementById("location_status_message").innerHTML = "All Latitude and Longitude boxes must contain a valid value for bounding box filter to be used";
               return '';
            }   
         }
         else{
            document.getElementById("location_status_message").innerHTML = "";
            return ''; //no bbox
         }
      }

      /**
      Assembles the entered year, month, day and times values into a single string
       */
      function GetDateTimeFilter(id){
         var year = document.getElementById( id + '_year').value;
         var month = document.getElementById( id + '_month').value;
         var day = document.getElementById( id + '_day').value;
         var hour = document.getElementById( id + '_hour').value;
         var minute = document.getElementById( id + '_minute').value;
         var second = document.getElementById( id + '_second').value;
         if(year != '' || month != '' || day != ''){
            if(year != '' && month != '' && day != '')
               return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second ;
            else
               throw new Error("The year-month-day values (" + year + "-" + month + "-" + day + ") selected are invalid, please check");
         }
         return null; 
      }

      /**
      Replaces illegal XML characters in strings
       */
      function Sanitize(value){
         if(value != null){
            value = value.replace(/&/g, '&amp;');
            value = value.replace(/</g, '&lt;');
            value = value.replace(/'/g, '&apos;');
            value = value.replace(/>/g, '&gt;');
         }
         return value;
      }

      /**
      Gets the checked value of a group of radio buttons   
      */
      function GetCheckedValue(name) {
         var radio = document.getElementsByName(name);
         if(!radio)
            return "";
         if(radio.checked)
            return radio.value;
         else{
            for(var i = 0; i < radio.length; i++){
               if(radio[i].checked){
                  return radio[i].value;
               }
            }
         }
         return "";
      }

      /**
      Formats a date string as an xsd:dateTime 
      */
      function DateTimeFormat(val) {
         if(!val){
            throw new Error("datetime param is null");
         }
         else{
            var tmp = val.replace(/\-/g,"/");
            if(!isNaN(Date.parse(tmp))){
               var date = new Date(tmp);
               return date.format("yyyy-MM-ddTHH:mm:ss");
            }
            else{
               throw new Error("invalid datetime: " + val);
            }
         }
      }

      /**
      Selects a particular feature type radio button
       */
      function SelectType(index){
         SetFeatureType(search_type[index]);
      }

      /**
      Selects a particular format type radio button
      */
      function SelectFormat(index){
         document.getElementsByName('output_format')[index].checked = true;
      }

      /**
      Loads an URL into a DOM Document object.
      (see http://blogs.msdn.com/xmlteam/archive/2006/10/23/using-the-right-version-of-msxml-in-internet-explorer.aspx)
      */
      function LoadXMLDoc(url){
         var xmlDoc;
         if(window.ActiveXObject){
            try{
               xmlDoc=new ActiveXObject("Msxml2.DOMDocument.6.0");
            }
            catch(e){
               try{
                  xmlDoc=new ActiveXObject("Msxml.DOMDocument");
               }
               catch(e){
                  throw new Error("Cannot instantiate Msxml.DOMDocument object");
               }
            } 
         }
         else if(document.implementation && document.implementation.createDocument){
              xmlDoc=document.implementation.createDocument("","",null);
           }
         else{
              throw new Error('Your browser cannot handle this script');
           }
         xmlDoc.async=false;
         xmlDoc.load(url);
         return(xmlDoc);
      }

      /**
       Sets up a tranformation to be initiated when clean results page loads,
      (need to find 'results' div in results page to replace it). 
      */
      function DisplayDataNew(do_count)
      {
         switch( GetCheckedValue('output_format')){
         case 'xml':
            pending_request = null;
            DisplayData(do_count); //no transformation needed
            return;
            break;
         case 'html':
            pending_request = BuildWFSRequest(do_count);
            pending_xslt = document.getElementById('wfs_uri').value.replace('/services','/xslt/xhtml_1.xsl');
            break;
         case 'csv':
            pending_request = BuildWFSRequest(do_count);
            pending_xslt = document.getElementById('wfs_uri').value.replace('/services','/xslt/csv_1.xsl');
            break;
         default:
            alert('unknown format');
         }
         //load blank results page and wait;
         //onload function in results page calls LoadResults() below.
         parent.frames[1].location = "results.html";
      }

      /**
       On client transformation routine
       */
      function LoadResults(){
         HideQuery();
         if(pending_request != null)
            parent.frames[1].location = pending_request;
         return;
         /**try{
            if(pending_request != null && pending_xslt != null){
               var xml = LoadXMLDoc(pending_request);
               var xsl = LoadXMLDoc(pending_xslt)
               var div = parent.frames[1].document.getElementById('results');
               if(window.ActiveXObject){
                  div.innerHTML = xml.transformNode(xsl);
                 }
               else if(document.implementation && document.implementation.createDocument)
               {
                  xsltProcessor = new XSLTProcessor();
                  xsltProcessor.importStylesheet(xsl);
                  resultDocument = xsltProcessor.transformToFragment(xml,document);
                  div.parentNode.replaceChild(resultDocument,div);
               }
            }
         }
         catch(e){
            alert(e);
         }*/
      }

      /**
       Toggles the help display
       */
      function ToggleHelp(){
         if(help_open == true){
            //hide
            document.getElementById('help_button').src = "img/plus.jpg";
            document.getElementById('help_toggle').style.height = "15px";
            document.getElementById('help_label').innerHTML = "Show Help";
            document.getElementById('data_source').style.display = "none";
            document.getElementById('help_content').style.display = "none";
            document.getElementById('query_display').style.display = "block";
            help_open = false;
         }
         else{
            //show
            ShowQuery();
            document.getElementById('help_button').src = "img/minus.jpg";
            document.getElementById('help_toggle').style.height = "25px";
            document.getElementById('help_label').innerHTML = "Hide Help";
            document.getElementById('data_source').style.display = "block";
            document.getElementById('help_content').style.display = "block";
            document.getElementById('query_display').style.display = "none";
            help_open = true;
         }
      }

      /**
       Toggles the citation display
       */
      function ToggleCitation(){
         if(citation_open == true){
            //hide
            document.getElementById('citation_aatams').style.display = "none";
            document.getElementById('params').style.display = "block";
            citation_open = false;
         }
         else{
            //show
            ShowQuery();
            document.getElementById('citation_aatams').style.display = "block";
            document.getElementById('access_date').innerHTML = new Date().format("d MMM yyyy");
            document.getElementById('params').style.display = "none";
            citation_open = true;
         }
      }

      /**
       Toggles the query display, to have most of the page as query or as results
       */
      function ToggleQuery(){
          if(query_max == true){
            //hide
            parent.document.getElementById("frames").rows = "145px,*";
            document.getElementById("query_display").innerHTML = "QUERY SHOW";
            query_max = false;
         }
         else{
            //show
            parent.document.getElementById("frames").rows = "90%,*";
            document.getElementById("query_display").innerHTML = "QUERY HIDE";
            query_max = true;
         }
      }

      /**
       Forces the query to be displayed
       */
      function ShowQuery(){
         query_max = false;
         ToggleQuery();
      }


      /**
       Forces the query to be hidden
       */
      function HideQuery(){
         query_max = true;
         ToggleQuery();
      }

