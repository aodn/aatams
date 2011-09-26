var CtxAlwaysOn = false;
function LoadSld( slideId )
{
	if( !g_supportsPPTHTML ) return
	if( slideId )
		parent.base.SldUpdated(slideId)
	g_origSz=parseInt(SlideObj.style.fontSize)
	g_origH=SlideObj.style.posHeight
	g_origW=SlideObj.style.posWidth
	g_scaleHyperlinks=(document.all.tags("AREA").length>0)
	if ( IsWin("PPTSld") && !parent.IsFullScrMode() )
		parent.base.highlite();	
	if( g_scaleHyperlinks )
		InitHLinkArray()
	if( g_scaleInFrame||(IsWin("PPTSld") && parent.IsFullScrMode() ) )
		document.body.scroll="no"
	_RSW()
	if( IsWin("PPTSld") && (parent.IsFullScrMode() || CtxAlwaysOn ) )	{
		document.oncontextmenu=parent._CM;
	self.focus(); 

	}
}
function MakeSldVis( fTrans ) 
{
	fTrans=fTrans && g_showAnimation
	if( fTrans )
	{
		if( g_bgSound ) {
			idx=g_bgSound.indexOf(",");
			pptSound.src=g_bgSound.substr( 0, idx );
			pptSound.loop= -(parseInt(g_bgSound.substr(idx+1)));
		}
		SlideObj.filters.revealtrans.Apply()
	}
	SlideObj.style.visibility="visible"
	if( fTrans )
		SlideObj.filters.revealtrans.Play()
}
function MakeNotesVis() 
{
	if( !IsNts() ) return false 
	SlideObj.style.display="none"
	nObj = document.all.item("NotesObj")
	parent.SetHasNts(0)
	if( nObj ) { 
		nObj.style.display=""
		parent.SetHasNts(1)
	}
	return 1
}
function Redirect( frmId,sId )
{
	var str=document.location.hash,idx=str.indexOf('#')
	if(idx>=0) str=str.substr(1);
	if( window.name != frmId && ( sId != str) ) {
		obj = document.all.item("Main-File")
		window.location.href=obj.href+"#"+sId
		return 1
	}
	return 0
}
function HideMenu() { if( frames["PPTSld"] && PPTSld.document.all.item("ctxtmenu") && PPTSld.ctxtmenu.style.display!="none" ) { PPTSld.ctxtmenu.style.display='none'; return true } return false }
function IsWin( name ) { return window.name == name }
function IsNts() { return IsWin("PPTNts") }
function IsSldOrNts() { return( IsWin("PPTSld")||IsWin("PPTNts") ) }
function SupportsPPTAnimation() { return( navigator.platform == "Win32" && navigator.appVersion.indexOf("Windows")>0 ) }
function SupportsPPTHTML()
{
	var appVer=navigator.appVersion, msie=appVer.indexOf( "MSIE " ), inex = appVer.indexOf( "Internet Explorer " ), ver=0
	if( msie >= 0 )
		ver=parseFloat( appVer.substring( msie+5, appVer.indexOf(";",msie) ) )
	else if( inex >= 0 )
		ver=parseFloat( appVer.substring( inex+18, appVer.indexOf(";",inex) ) )
	else
		ver=parseInt(appVer)

	return( ver >= 4  )
}
var MHTMLPrefix = CalculateMHTMLPrefix(); 
function CalculateMHTMLPrefix()
{
	if ( document.location.protocol == 'mhtml:') { 
		href=new String(document.location.href) 
		Start=href.indexOf('!')+1 
		End=href.lastIndexOf('/')+1 
		if (End < Start) 
			return href.substring(0, Start) 
		else 
		return href.substring(0, End) 
	}
	return '';
}

function LoadNavSld(slideId) {
playList();

	if( !g_supportsPPTHTML ) return
	if( IsWin("PPTSld") && slideId )
		parent.base.SldUpdated(slideId)
	self.focus(); 

}
var hasNarration = false;
function _RSW()
{
	if( !g_supportsPPTHTML || IsNts() ||
	  ( !g_scaleInFrame && (( window.name != "PPTSld" ) ) ) )
		return

	cltWidth=document.body.clientWidth
	cltHeight=document.body.clientHeight
	factor=(1.0*cltWidth)/g_origW
	if( cltHeight < g_origH*factor )
		factor=(1.0*cltHeight)/g_origH

	newSize = g_origSz * factor
	if( newSize < 1 ) newSize=1

	s=SlideObj.style
	s.fontSize=newSize+"px"
	s.posWidth=g_origW*factor
	s.posHeight=g_origH*factor
	s.posLeft=(cltWidth-s.posWidth)/2
	s.posTop=(cltHeight-s.posHeight)/2

	if ( hasNarration ) {
		obj = document.all.NSPlay.style;
		mySld = document.all.SlideObj.style;
		obj.position = 'absolute';
		obj.posTop = mySld.posTop + mySld.posHeight - 20;
		obj.posLeft = mySld.posLeft + mySld.posWidth - 20;
	}
	if( g_scaleHyperlinks )
		ScaleHyperlinks( factor );	
}

var g_HLinkArray = new Array();
 
function IMapAreaObj( areaObj, coords )
{
  this.areaObj = areaObj;
  this.coords = coords;
}

function InitHLinkArray()
{
  var appVer = navigator.appVersion;
  var msie = appVer.indexOf( "MSIE " ) + appVer.indexOf( "Internet Explorer " );
  var ver = 0;
  if ( msie >= 0 )
    ver = parseInt ( appVer.substring( msie+5 ) );

  linkNum = 0;

  for (i=0; i<document.all.tags("AREA").length; i++) {
    areaObj = document.all.tags("AREA").item(i);
    if( ( areaObj.id != "pptxscale" ) || ( ver > 4 ) )
      g_HLinkArray[linkNum++] = new IMapAreaObj( areaObj, ParseCoords( areaObj.coords ) );
  }
}

function ScaleHyperlinks( factor )
{   
  for ( ii=0; ii< g_HLinkArray.length; ii++) {
    coordsStr="";
    imaObj = g_HLinkArray[ii];
    for ( jj=0; jj < imaObj.coords.length-1; jj++ )
  	  coordsStr += (Math.round(imaObj.coords[jj]*factor)) + ",";
    coordsStr += (Math.round(imaObj.coords[jj]*factor));
    imaObj.areaObj.coords = coordsStr;
  }
}

function ParseCoords( coordsStr )
{
  var numArray = new Array();
  var i = curPos = commaPos = 0;

  while ( ( commaPos = coordsStr.indexOf(",", curPos) ) != -1 ) { 
    numArray[i++] = parseInt( coordsStr.substr( curPos, commaPos ) );
    curPos = commaPos + 1;
  }
  numArray[i] = parseInt( coordsStr.substr( curPos ) );

  return numArray;
}



var g_supportsPPTHTML = SupportsPPTHTML(), g_scaleInFrame = true, gId="", g_bgSound="",
    g_scaleHyperlinks = false, g_allowAdvOnClick = true, g_showInBrowser = false, g_doAdvOnClick = false;

 var g_showAnimation = 0;
var g_hasTrans = false, g_autoTrans = false, g_transSecs = 0;
var g_animManager = null;

var END_SHOW_HREF         = "endshow.htm",
    OUTLINE_EXPAND_HREF   = "outline_expanded.htm",
    OUTLINE_COLLAPSE_HREF = "outline_collapsed.htm",
    OUTLINE_NAVBAR_HREF  = "outline_navigation_bar.htm",
    NAVBAR_HREF           = "navigation_bar.htm",
    BLANK_NOTES_HREF	  = "blank_notes.htm",
    NUM_VISIBLE_SLIDES    = 0,
    SIMPLE_FRAMESET       = 0,
    SLIDE_FRAME	        = "PPTSld",
    NOTES_FRAME           = "PPTNts",
    OUTLINE_FRAME         = "PPTOtl",
    OUTLINE_NAVBAR_FRAME  = "PPTOtlNav",
    NAVBAR_FRAME          = "PPTNav",
	MAIN_FRAME			  = "MainFrame",
	FS_NAVBAR_HREF		  = "fs_navigation_bar.htm",
	isIEFiles 			= 0,
	isNAVFiles 			= 0,
	isFLATFiles 			= 16,
	includeNotes			= 0,
	PPTPRESENTATION     = 1;
var  INITSLIDENUM   = -1;

var EndSlideShow = 0;
var g_outline_href = OUTLINE_COLLAPSE_HREF;	
var g_fullscrMode = 0;	
var FSWin = null;
var gtmpstr = document.location.href;
var g_baseURL = gtmpstr.substr(0, gtmpstr.lastIndexOf("/") ) + "/" + "background_data_flowchart_files";
var g_showoutline = 1;
var g_shownotes = includeNotes;
var g_currentSlide = INITSLIDENUM, g_prevSlide = INITSLIDENUM;
var saveFSSlideNum = saveTPSlideNum = g_currentSlide;
var saveFSprevSlide = saveTPprevSlide = g_prevSlide;
var g_slideType="ie";
var appVer = navigator.appVersion;
var msie = appVer.indexOf( "MSIE " ) + appVer.indexOf( "Internet Explorer " );
var isnav = ( navigator.appName.indexOf( "Netscape" ) >= 0 );
var msieWin31 = (appVer.indexOf( "Windows 3.1" ) > 0);
var ver = 0;
var g_done = 0;
var g_prevotlobjidx = 0;
var g_ShowFSDefault = 0;
var g_lastVisibleSld = 1;
var g_allHidden = false;
function IsIE() {
	return (msie >= 0 );
}

function IsNav() {
	return (isnav);
}
var msiePos = appVer.indexOf( "MSIE " );
var inexPos = appVer.indexOf( "Internet Explorer " );
if ( msiePos >= 0 )
  ver = parseFloat( appVer.substring( msiePos+5, appVer.indexOf ( ";", msiePos ) ) );
else if( inexPos >= 0 )
  ver=parseFloat( appVer.substring( inexPos+18, appVer.indexOf(";",inexPos) ) )
else
  ver = parseInt( appVer );

//var g_supportsPPTHTML = 0; //!msieWin31 && ( ( msie >= 0 && ver >= 3.02 ) || ( msie < 0 && ver >= 3 ) );

function GetCurrentSlideNum()
{   
  obj = GetHrefObj( g_currentSlide );
  if ( GetHrefObj( g_currentSlide ).m_origVisibility == 1 )
    return obj.m_slideIdx;
  else   
    return g_currentSlide;
}

function GetNumSlides()
{
  if ( GetHrefObj( g_currentSlide ).m_origVisibility == 1 )
    return NUM_VISIBLE_SLIDES;
  else
    return g_docTable.length;
}

function GetHrefObj( slideIdx )
{ return g_docTable[slideIdx - 1];
}

function GetSlideNum( slideHref )
{
  for (ii=0; ii<g_docTable.length; ii++) {
    if ( g_docTable[ii].m_slideHref == slideHref )
      return ii+1;
  }
  return 1;
}

function GoToNextSld()
{   
  targetIdx = g_currentSlide + 1;
  if ( GetHrefObj( targetIdx-1 ).m_origVisibility == 0 ) {
    if ( targetIdx<=g_docTable.length ) {
      obj = GetHrefObj( targetIdx );
      obj.m_visibility = 1;
      GoToSld( obj.m_slideHref );
    }
  }
  else {
    obj = GetHrefObj( targetIdx );
    while ( obj && ( obj.m_origVisibility == 0 ) && ( targetIdx<=g_docTable.length ) )
      obj = GetHrefObj( targetIdx++ );
    if( obj && obj.m_origVisibility )
      GoToSld( obj.m_slideHref );
  }
}

function GoToPrevSld()
{
  targetIdx = g_currentSlide - 1;
  if ( targetIdx > 0 ) {
    obj = GetHrefObj( targetIdx );
    while ( ( obj.m_visibility == 0 ) && ( targetIdx>0 ) )
      obj = GetHrefObj( targetIdx-- );
    GoToSld( obj.m_slideHref );
  }
}

function GoToLast()
{
  targetIdx = g_docTable.length;
  if ( targetIdx != g_currentSlide )
    GoToSld( GetHrefObj( targetIdx ).m_slideHref );
}

function GoToFirst()
{ GoToSld( GetHrefObj(1).m_slideHref );
}

function highlite() {
	if ( IsFullScrMode() )
		return;
	index = GetCurrentSlideNum();
	if ( !frames[MAIN_FRAME].frames[OUTLINE_FRAME] )
		return;
	if ( msie < 0 ) {
		if ( g_prevotlobjidx != 0 ) {
			eval( "otlobj = frames[MAIN_FRAME].frames[OUTLINE_FRAME].document.LAYERID" + g_prevotlobjidx );
			otlobj.hidden = true;
		}
		else
			index = GetCurrentSlideNum();
		eval( "otlobj = frames[MAIN_FRAME].frames[OUTLINE_FRAME].document.LAYERID" + index );
		otlobj.hidden = false;
	
		g_prevotlobjidx = index;
		
		return;
	}
	if ( !g_showoutline )
		return;
		
		backclr = frames[MAIN_FRAME].frames[OUTLINE_FRAME].document.body.bgColor;
		textclr = frames[MAIN_FRAME].frames[OUTLINE_FRAME].document.body.text;
	if ( g_prevotlobjidx != 0 ) {
		eval( "otlobj = frames[MAIN_FRAME].frames[OUTLINE_FRAME].document.all.p" + g_prevotlobjidx );
		otlobj.style.backgroundColor = backclr;
		otlobj.style.color = textclr;
		otlobj.all.AREF.style.color = textclr;
	}
	else
		index = GetCurrentSlideNum();
	eval( "otlobj = frames[MAIN_FRAME].frames[OUTLINE_FRAME].document.all.p" + index );
	otlobj.style.backgroundColor = textclr;
	otlobj.style.color = backclr;
	otlobj.all.AREF.style.color = backclr;
	g_prevotlobjidx = index;
}

function ChangeFrame( frame, href )
{
if ( IsFramesMode() ) {
  if ( NAVBAR_FRAME == frame || OUTLINE_NAVBAR_FRAME ==  frame ) {
	    frames[frame].location.replace(href);
  }
  else if( ! ( ( OUTLINE_FRAME == frame && !g_showoutline)  || (NOTES_FRAME == frame && !g_shownotes ) ) ){
	    frames[MAIN_FRAME].frames[frame].location.href = href;
  }
 }
 else {
 	if ( frame == NAVBAR_FRAME || frame == SLIDE_FRAME ) {
 	  if( frame == NAVBAR_FRAME ) {
 	  	 href = FS_NAVBAR_HREF;
 	  	
 	  }	    
 	  if( frame == NAVBAR_FRAME ) 
	      window.frames[frame].location.replace(href);
	 else
	      window.frames[frame].location.href = href;
 	}
 }
  
}

function shutEventPropagation() {
	if ( IsNav() )
		return;
		
	var slideFrame;
	if ( IsFramesMode() )
		slideFrame = frames[MAIN_FRAME].frames[SLIDE_FRAME];
	else
		slideFrame = window.frames[SLIDE_FRAME];
	if ( slideFrame.event ) 
		slideFrame.event.cancelBubble=true;
}
				
function GoToSld( slideHref )
{
  shutEventPropagation();
  if ( slideHref != GetHrefObj( g_currentSlide ).m_slideHref || g_slideType != GetHrefObj( g_currentSlide ).type) {
    g_prevSlide = g_currentSlide;
    g_currentSlide = GetSlideNum( slideHref );
	g_slideType = GetHrefObj( g_currentSlide ).type;
    obj = GetHrefObj( g_currentSlide );
    obj.m_visibility = 1;
    ChangeFrame( SLIDE_FRAME, slideHref );
    if( !SIMPLE_FRAMESET )
      ChangeFrame( NOTES_FRAME, obj.m_notesHref );
    ChangeFrame( NAVBAR_FRAME, NAVBAR_HREF );
	    
  }
}

function PrevSldViewed()
{ GoToSld( GetHrefObj( g_prevSlide ).m_slideHref );
}

function NoHref() {}

function ExpandOutline( )
{ 
 g_outline_href = OUTLINE_EXPAND_HREF;
 ChangeFrame( OUTLINE_FRAME, OUTLINE_EXPAND_HREF );
 frames[OUTLINE_NAVBAR_FRAME].location.replace( OUTLINE_NAVBAR_HREF);
}

function CollapseOutline()
{ 
 g_outline_href = OUTLINE_COLLAPSE_HREF;
 ChangeFrame( OUTLINE_FRAME, OUTLINE_COLLAPSE_HREF );
 frames[OUTLINE_NAVBAR_FRAME].location.replace( OUTLINE_NAVBAR_HREF);
 }

function SlideUpdated( id )
{
  if ( id != GetHrefObj( g_currentSlide ).m_slideHref )  {
    g_prevSlide = g_currentSlide;
    g_currentSlide = GetSlideNum( id );
    obj = GetHrefObj( g_currentSlide );
    if( !SIMPLE_FRAMESET )
      ChangeFrame( NOTES_FRAME, obj.m_notesHref );
    ChangeFrame( NAVBAR_FRAME, NAVBAR_HREF );
  }
}

function hrefList( slideHref, notesHref, visible, slideIdx, type )
{
  this.m_slideHref  = slideHref;
  this.m_notesHref  = notesHref;
  this.m_navbarHref = NAVBAR_HREF;
  this.m_origVisibility = visible;
  this.m_visibility = visible;
  this.m_slideIdx = slideIdx;
  this.type = type;
}

function IsFullScrMode() {
	return g_fullscrMode;
}


function IsFramesMode() {
	return (1 - g_fullscrMode);
}

function SldUpdated( id )
{
  if ( ( id != GetHrefObj( g_currentSlide ).m_slideHref )  || ( g_currentSlide == g_lastVisibleSld ) ){
    g_prevSlide = g_currentSlide;
    g_currentSlide = GetSlideNum( id );
    obj = GetHrefObj( g_currentSlide );
    if( !SIMPLE_FRAMESET )
      ChangeFrame( NOTES_FRAME, obj.m_notesHref );
    ChangeFrame( NAVBAR_FRAME, NAVBAR_HREF );
  }
}

function ToggleOutline() {
	g_showoutline = 1 - g_showoutline;
	writeMyFrame();
}

function ShowHideNotes() {
	g_shownotes = 1 - g_shownotes;
	writeMyFrame();
}

function writeMyFrame() {
		SetFSMode(0);
		obj = frames[MAIN_FRAME];
		
		var curslide = g_baseURL + "/" +  GetHrefObj( g_currentSlide ).m_slideHref;
		var curnotes = g_baseURL + "/" +  GetHrefObj( g_currentSlide ).m_notesHref;
		var otlhref = g_baseURL + "/" +  g_outline_href;
		if ( msie < 0 ) {			
		if ( ! g_showoutline && g_shownotes ) {
			obj.document.write( '<HTML><HEAD><SCRIPT language=JavaScript src=' + g_baseURL + '/script.js></SCRIPT><SCRIPT> base = parent; <\/SCRIPT><\/HEAD> \
				<frameset rows=\"*,20%\" id=\"frameset2\" > \
				<frame src=\"' + curslide + '\" name=PPTSld marginheight=0 marginwidth=0> \
				<frame src=\"' + curnotes + '\" name=PPTNts marginheight=0 marginwidth=0> \
				</frameset> </html>' );
		}
		else if( g_showoutline && g_shownotes  ){
			obj.document.write( '<HTML><HEAD><SCRIPT language=JavaScript src=' + g_baseURL + '/script.js></SCRIPT><SCRIPT> base = parent; <\/SCRIPT><\/HEAD> \
				<frameset cols=\"20%,*\" id=\"frameset1\"> \
				<frame src=\"' + otlhref + '\" name=PPTOtl> \
				<frameset rows=\"*,20%\" id=\"frameset2\" > \
				<frame src=\"' + curslide + '\" name=PPTSld marginheight=0 marginwidth=0> \
				<frame src=\"' + curnotes + '\" name=PPTNts marginheight=0 marginwidth=0> \
				</frameset> </frameset></html>' );
		}		
		else if ( !g_shownotes && !g_showoutline ) {
			obj.document.write( '<HTML><HEAD><SCRIPT language=JavaScript src=' + g_baseURL + '/script.js></SCRIPT><SCRIPT> base = parent; <\/SCRIPT><\/HEAD> \
				<frameset rows="*,0" frameborder=0 > \
				<frame src=\"' + curslide + '\" name=PPTSld marginheight=0 marginwidth=0> \
				</frameset> </html>' );
		}
		else if( !g_shownotes  && g_showoutline ) {
			obj.document.write( '<HTML><HEAD><SCRIPT language=JavaScript src=' + g_baseURL + '/script.js></SCRIPT><SCRIPT> base = parent; <\/SCRIPT><\/HEAD> \
				<frameset cols=\"20%,*\" id=\"frameset1\"> \
				<frame src=\"' + otlhref + '\" name=PPTOtl> \
				<frame src=\"' + curslide + '\" name=PPTSld marginheight=0 marginwidth=0> \
				</frameset></html>' );
		}
		obj.document.close();
		}
		else {
			if ( g_showoutline ) {
				obj.PPTHorizAdjust.cols = "20%,*";
				obj.PPTOtl.location.reload();
			}
			else {
				obj.PPTHorizAdjust.cols = "0,*";
			}
			if ( g_shownotes ) {
				obj.PPTVertAdjust.rows = "*,20%";
				obj.PPTNts.location.href = curnotes;
			}
			else {
				obj.PPTVertAdjust.rows = "*,0";
			}
		}
		ChangeFrame( OUTLINE_NAVBAR_FRAME, OUTLINE_NAVBAR_HREF );
}

function FullScreen() {
	g_done = 0;

	
	SetFSMode(1);
	if ( msie >= 0 )
		FSWin = window.open( g_baseURL + "/" + "fullscreen.htm", null, "fullscreen=yes");
	else {
		var height = screen.availHeight;
		if ( window.navigator.platform.indexOf( "Mac" ) >= 0 ) {
			height -= 30;
		}
		FSWin = window.open( g_baseURL + "/" + "fullscreen.htm", "null", "height="+ height + ",width=" + screen.availWidth + ",screenX=0,screenY=0");
	}
}

function SetFSMode( i ) {

}

function Slide( i ) {
	SetFSMode(0);
	GoToSld(GetHrefObj(i).m_slideHref);
}

function TP_GoToNextSld() {
	SetFSMode(0);
	GoToNextSld();
}

function TP_GoToPrevSld() {
	SetFSMode(0);
	GoToPrevSld();
}

function CloseFullScreen() {
	g_done = 0;	
	
	if ( IsNav() ){
		if ( self.opener )
			opener.FSWin = null;
	}
	window.close();
}

function slidenum(i) {
	var slidename = "slide";
	if ( i < 10 )
		return ( slidename + "000" + i);
	else if ( i < 100 )
		return ( slidename + "00" + i );
	else if ( i < 1000 ) 
		return (slidename + "0" + i );
	else
		return (slidename + i );
}
function UpdateLastVisibleSlide( index ) {
	if ( g_lastVisibleSld < index ) 
		g_lastVisibleSld = index;
}

function jpegArray( numSlides ) {
count_hidden = 0;
	g_docTable = new Array();
  for( i=0; i<numSlides; i++ ) {
    j = 2 * numSlides + i + 1;
    var str = slidenum( j ) +".htm";
	if( g_notesTable[i] == 1 )
		g_docTable[i] = new hrefList( str, slidenum(i+1 ) + "_notes_pane.htm", g_hiddenSlide[i], i+1-count_hidden, "jpeg" );
	else
		g_docTable[i] = new hrefList( str, BLANK_NOTES_HREF, g_hiddenSlide[i], i+1-count_hidden, "jpeg" );
    if ( !g_hiddenSlide[i] ) count_hidden++;
    else UpdateLastVisibleSlide( i+1 );
  }
}

function ieArray( numSlides ) {
count_hidden = 0;
	g_docTable = new Array();
  for( i=0; i<numSlides; i++ ) {
    var str = slidenum(i+1) +".htm";
	if( g_notesTable[i] == 1 )
		g_docTable[i] = new hrefList( str, slidenum( i+1 ) + "_notes_pane.htm", g_hiddenSlide[i], i+1-count_hidden, "ie" );
	else
		g_docTable[i] = new hrefList( str, BLANK_NOTES_HREF, g_hiddenSlide[i], i+1-count_hidden, "ie" );
    if ( !g_hiddenSlide[i] ) count_hidden++;
    else UpdateLastVisibleSlide( i+1 );
  }
}

function navArray( numSlides ) {
count_hidden = 0;
	g_docTable = new Array();
  for( i=0; i<numSlides; i++ ) {
    j = numSlides + i + 1;
    var str = slidenum( j ) +".htm";
	if( g_notesTable[i] == 1 )
		g_docTable[i] = new hrefList( str, slidenum(i+1 ) + "_notes_pane.htm", g_hiddenSlide[i], i+1-count_hidden, "nav" );
	else
		g_docTable[i] = new hrefList( str, BLANK_NOTES_HREF, g_hiddenSlide[i], i+1-count_hidden, "nav" );
    if ( !g_hiddenSlide[i] ) count_hidden++;
    else UpdateLastVisibleSlide( i+1 );
  }
}

function LoadHTMLVersion() {
  var os = window.navigator.platform.indexOf("Mac");
  if ( (msie || isnav ) && ( (os < 0 && ver >= 4 ) || ( os >= 0 && ver >= 5 ) || (os >=0 && msie < 0 && ver >= 4 ) )  ){
	if ( msie >= 0 )  {
		if ( isIEFiles > 0 )
			ieArray( 1 );
		else if ( isFLATFiles > 0 ){
			/*if ( IsFramesMode() )
				StatusPlay("This presentation is optimized for use with older versions of your browser. Since you are using a more recent version of Microsoft Internet Explorer or Netscape Navigator, consider optimizing this presentation to take advantage of your current version's advanced capabilities."); */
			jpegArray( 1 );
		}
		else
			window.location.replace(  "background_data_flowchart_files/error.htm" );
	}
	else {
		if ( isNAVFiles > 0 && ver < 5) 
			navArray( 1 );
		else if ( isFLATFiles > 0 ) {
			/* if ( IsFramesMode() )
				StatusPlay("This presentation is optimized for use with older versions of your browser. Since you are using a more recent version of Microsoft Internet Explorer or Netscape Navigator, consider optimizing this presentation to take advantage of your current version's advanced capabilities."); */
			jpegArray( 1 );
		}
		else
			window.location.replace(  "background_data_flowchart_files/error.htm" );
	}
}
else {
	/*
	if ( IsFramesMode() && !isWebTV() )
		StatusPlay("This presentation contains content that your browser may not be able to show properly. This presentation was optimized for more recent versions of Microsoft Internet Explorer or Netscape Navigator.");
	*/
	if ( isFLATFiles <= 0 ) {
	/*	if ( IsFramesMode() )
			window.alert("This presentation contains content that your browser may not be able to display properly. This presentation is optimized for more recent versions of Microsoft Internet Explorer or Netscape Navigator." );
	*/
		window.location.replace(  "background_data_flowchart_files/error.htm" );
	}		
	/*
	else if ( IsFramesMode()  && !isWebTV() )
		StatusPlay( "This presentation contains content that your browser may not be able to show properly. This presentation was optimized for more recent versions of Microsoft Internet Explorer or Netscape Navigator." ); 
	*/	
	jpegArray ( 1 );
  }
  
}

function isWebTV() {
	if ( window.navigator.appName.indexOf( 'WebTV' ) >= 0 )
		return true;
	return false;
}
		
var count;
var statusText;
var statusfirst = 0;
 function display50( text ) 
{
	len = text.length;
	if ( len < 50 && count < 2) {
		window.status = text;
		window.setTimeout( "repeat()", 300 );
	}	
	else {
		var period = 200;
		window.status = text;
		newtext = text.substring( 1, len );
		if ( statusfirst ) {
			statusfirst = 0;
			period = 2000;
		}	
		window.setTimeout( "display50( newtext )", period );
	}
}
function repeat(  ) {
	count++;		
	statusfirst = 1;
	display50( statusText );
}

function StatusPlay( text ) {
	count = 0;
	statusText = text;
	repeat( );		
 }
function makeSlide( i, notes, visible ) {
	g_notesTable[i] = notes;
	g_hiddenSlide[i] = visible;
}	

