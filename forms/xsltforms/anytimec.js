/* anytimec.js 3.1007 (anytime.js 3.1007)
Copyright 2008-2010 Andrew M. Andrews III (www.AMA3.com). Some Rights 
Reserved. This work licensed under the Creative Commons Attribution-
Noncommercial-Share Alike 3.0 Unported License except in jurisdicitons
for which the license has been ported by Creative Commons International,
where the work is licensed under the applicable ported license instead.
For a copy of the unported license, visit
http://creativecommons.org/licenses/by-nc-sa/3.0/
or send a letter to Creative Commons, 171 Second Street, Suite 300,
San Francisco, California, 94105, USA.  For ported versions of the
license, visit http://creativecommons.org/international/
Any+Time is a trademark of Andrew M. Andrews III. */
var AnyTime=(function()
{var __acb='.AtwCurrentBtn';var __daysIn=[31,28,31,30,31,30,31,31,30,31,30,31];var __iframe=null;var __initialized=false;var __msie6=(navigator.userAgent.indexOf('MSIE 6')>0);var __msie7=(navigator.userAgent.indexOf('MSIE 7')>0);var __widgets=[];jQuery.prototype.AtwHeight=function(inclusive)
{return(__msie6?Number(this.css('height').replace(/[^0-9]/g,'')):this.outerHeight(inclusive));};jQuery.prototype.AtwWidth=function(inclusive)
{return(__msie6?(1+Number(this.css('width').replace(/[^0-9]/g,''))):this.outerWidth(inclusive));};$(document).ready(function()
{if(window.location.hostname.length&&(window.location.hostname!='www.ama3.com'))
$(document.body).append('<img src="http://www.ama3.com/anytime/ping/?3.1007" width="0" height="0" />');if(__msie6)
{__iframe=$('<iframe frameborder="0" scrolling="no"></iframe>');__iframe.src="javascript:'<html></html>';";$(__iframe).css({display:'block',height:'1px',left:'0',top:'0',width:'1px',zIndex:0});$(document.body).append(__iframe);}
for(var id in __widgets)
__widgets[id].onReady();__initialized=true;});return{Converter:function(options)
{var _flen=0;var _longDay=9;var _longMon=9;var _shortDay=6;var _shortMon=3;this.fmt='%Y-%m-%d %T';this.dAbbr=['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];this.dNames=['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];this.dNums=[];this.eAbbr=['BCE','CE'];this.mAbbr=['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];this.mNames=['January','February','March','April','May','June','July','August','September','October','November','December'];this.mNums=[];this.baseYear=null;this.dAt=function(str,pos)
{return((str.charCodeAt(pos)>='0'.charCodeAt(0))&&(str.charCodeAt(pos)<='9'.charCodeAt(0)));};this.format=function(date)
{var t;var str='';for(var f=0;f<_flen;f++)
{if(this.fmt.charAt(f)!='%')
str+=this.fmt.charAt(f);else
{switch(this.fmt.charAt(f+1))
{case'a':str+=this.dAbbr[date.getDay()];break;case'B':if(date.getFullYear()<0)
str+=this.eAbbr[0];break;case'b':str+=this.mAbbr[date.getMonth()];break;case'C':if(date.getFullYear()>0)
str+=this.eAbbr[1];break;case'c':str+=date.getMonth()+1;break;case'd':t=date.getDate();if(t<10)str+='0';str+=String(t);break;case'D':t=String(date.getDate());str+=t;if((t.length==2)&&(t.charAt(0)=='1'))
str+='th';else
{switch(t.charAt(t.length-1))
{case'1':str+='st';break;case'2':str+='nd';break;case'3':str+='rd';break;default:str+='th';break;}}
break;case'E':str+=this.eAbbr[(date.getFullYear()<0)?0:1];break;case'e':str+=date.getDate();break;case'H':t=date.getHours();if(t<10)str+='0';str+=String(t);break;case'h':case'I':t=date.getHours()%12;if(t==0)
str+='12';else
{if(t<10)str+='0';str+=String(t);}
break;case'i':t=date.getMinutes();if(t<10)str+='0';str+=String(t);break;case'k':str+=date.getHours();break;case'l':t=date.getHours()%12;if(t==0)
str+='12';else
str+=String(t);break;case'M':str+=this.mNames[date.getMonth()];break;case'm':t=date.getMonth()+1;if(t<10)str+='0';str+=String(t);break;case'p':str+=((date.getHours()<12)?'AM':'PM');break;case'r':t=date.getHours()%12;if(t==0)
str+='12:';else
{if(t<10)str+='0';str+=String(t)+':';}
t=date.getMinutes();if(t<10)str+='0';str+=String(t)+':';t=date.getSeconds();if(t<10)str+='0';str+=String(t);str+=((date.getHours()<12)?'AM':'PM');break;case'S':case's':t=date.getSeconds();if(t<10)str+='0';str+=String(t);break;case'T':t=date.getHours();if(t<10)str+='0';str+=String(t)+':';t=date.getMinutes();if(t<10)str+='0';str+=String(t)+':';t=date.getSeconds();if(t<10)str+='0';str+=String(t);break;case'W':str+=this.dNames[date.getDay()];break;case'w':str+=date.getDay();break;case'Y':str+=AnyTime.pad(date.getFullYear(),4);break;case'y':t=date.getFullYear()%100;str+=AnyTime.pad(t,2);break;case'Z':str+=AnyTime.pad(Math.abs(date.getFullYear()),4);break;case'z':str+=Math.abs(date.getFullYear());break;case'%':str+='%';break;case'f':case'j':case'U':case'u':case'V':case'v':case'X':case'x':throw'%'+this.fmt.charAt(f)+' not implemented by AnyTime.Converter';default:str+=this.fmt.substr(f,2);}
f++;}}
return str;};this.parse=function(str)
{var era=1;var time=new Date();var slen=str.length;var s=0;var i,matched,sub,sublen,temp;for(var f=0;f<_flen;f++)
{if(this.fmt.charAt(f)=='%')
{switch(this.fmt.charAt(f+1))
{case'a':matched=false;for(sublen=0;s+sublen<slen;sublen++)
{sub=str.substr(s,sublen);for(i=0;i<12;i++)
if(this.dAbbr[i]==sub)
{matched=true;s+=sublen;break;}
if(matched)
break;}
if(!matched)
throw'unknown weekday: '+str.substr(s);break;case'B':sublen=this.eAbbr[0].length;if((s+sublen<=slen)&&(str.substr(s,sublen)==this.eAbbr[0]))
{era=(-1);s+=sublen;}
break;case'b':matched=false;for(sublen=0;s+sublen<slen;sublen++)
{sub=str.substr(s,sublen);for(i=0;i<12;i++)
if(this.mAbbr[i]==sub)
{time.setMonth(i);matched=true;s+=sublen;break;}
if(matched)
break;}
if(!matched)
throw'unknown month: '+str.substr(s);break;case'C':sublen=this.eAbbr[1].length;if((s+sublen<=slen)&&(str.substr(s,sublen)==this.eAbbr[1]))
s+=sublen;break;case'c':if((s+1<slen)&&this.dAt(str,s+1))
{time.setMonth((Number(str.substr(s,2))-1)%12);s+=2;}
else
{time.setMonth((Number(str.substr(s,1))-1)%12);s++;}
break;case'D':if((s+1<slen)&&this.dAt(str,s+1))
{time.setDate(Number(str.substr(s,2)));s+=4;}
else
{time.setDate(Number(str.substr(s,1)));s+=3;}
break;case'd':time.setDate(Number(str.substr(s,2)));s+=2;break;case'E':sublen=this.eAbbr[0].length;if((s+sublen<=slen)&&(str.substr(s,sublen)==this.eAbbr[0]))
{era=(-1);s+=sublen;}
else if((s+(sublen=this.eAbbr[1].length)<=slen)&&(str.substr(s,sublen)==this.eAbbr[1]))
s+=sublen;else
throw'unknown era: '+str.substr(s);break;case'e':if((s+1<slen)&&this.dAt(str,s+1))
{time.setDate(Number(str.substr(s,2)));s+=2;}
else
{time.setDate(Number(str.substr(s,1)));s++;}
break;case'f':s+=6;break;case'H':time.setHours(Number(str.substr(s,2)));s+=2;break;case'h':case'I':time.setHours(Number(str.substr(s,2)));s+=2;break;case'i':time.setMinutes(Number(str.substr(s,2)));s+=2;break;case'k':if((s+1<slen)&&this.dAt(str,s+1))
{time.setHours(Number(str.substr(s,2)));s+=2;}
else
{time.setHours(Number(str.substr(s,1)));s++;}
break;case'l':if((s+1<slen)&&this.dAt(str,s+1))
{time.setHours(Number(str.substr(s,2)));s+=2;}
else
{time.setHours(Number(str.substr(s,1)));s++;}
break;case'M':matched=false;for(sublen=_shortMon;s+sublen<=slen;sublen++)
{if(sublen>_longMon)
break;sub=str.substr(s,sublen);for(i=0;i<12;i++)
{if(this.mNames[i]==sub)
{time.setMonth(i);matched=true;s+=sublen;break;}}
if(matched)
break;}
break;case'm':time.setMonth((Number(str.substr(s,2))-1)%12);s+=2;break;case'p':if(str.charAt(s)=='P')
{if(time.getHours()==12)
time.setHours(0);else
time.setHours(time.getHours()+12);}
s+=2;break;case'r':time.setHours(Number(str.substr(s,2)));time.setMinutes(Number(str.substr(s+3,2)));time.setSeconds(Number(str.substr(s+6,2)));if(str.substr(s+8,1)=='P')
{if(time.getHours()==12)
time.setHours(0);else
time.setHours(time.getHours()+12);}
s+=10;break;case'S':case's':time.setSeconds(Number(str.substr(s,2)));s+=2;break;case'T':time.setHours(Number(str.substr(s,2)));time.setMinutes(Number(str.substr(s+3,2)));time.setSeconds(Number(str.substr(s+6,2)));s+=8;break;case'W':matched=false;for(sublen=_shortDay;s+sublen<=slen;sublen++)
{if(sublen>_longDay)
break;sub=str.substr(s,sublen);for(i=0;i<7;i++)
{if(this.dNames[i]==sub)
{matched=true;s+=sublen;break;}}
if(matched)
break;}
break;case'Y':i=4;if(str.substr(s,1)=='-')
i++;time.setFullYear(Number(str.substr(s,i)));s+=i;break;case'y':i=2;if(str.substr(s,1)=='-')
i++;temp=Number(str.substr(s,i));if(typeof(this.baseYear)=='number')
temp+=this.baseYear;else if(temp<70)
temp+=2000;else
temp+=1900;time.setFullYear(temp);s+=i;break;case'Z':time.setFullYear(Number(str.substr(s,4)));s+=4;break;case'z':i=0;while((s<slen)&&this.dAt(str,s))
i=(i*10)+Number(str.charAt(s++));time.setFullYear(i);break;case'j':case'U':case'u':case'V':case'v':case'w':case'X':case'x':throw'%'+this.fmt.charAt(f+1)+' not implemented by AnyTime.Converter';case'%':default:s++;break;}
f++;}
else if(this.fmt.charAt(f)!=str.charAt(s))
throw str+' is not in "'+this.fmt+'" format';else
s++;}
if(era<0)
time.setFullYear(0-time.getFullYear());return time;};(function(_this)
{var i;if(!options)
options={};if(options['baseYear'])
_this.baseYear=Number(options['baseYear']);if(options['format'])
_this.fmt=options['format'];_flen=_this.fmt.length;if(options['dayAbbreviations'])
_this.dAbbr=$.makeArray(options['dayAbbreviations']);if(options['dayNames'])
{_this.dNames=$.makeArray(options['dayNames']);_longDay=1;_shortDay=1000;for(i=0;i<7;i++)
{var len=_this.dNames[i].length;if(len>_longDay)
_longDay=len;if(len<_shortDay)
_shortDay=len;}}
if(options['eraAbbreviations'])
_this.eAbbr=$.makeArray(options['eraAbbreviations']);if(options['monthAbbreviations'])
_this.mAbbr=$.makeArray(options['monthAbbreviations']);if(options['monthNames'])
{_this.mNames=$.makeArray(options['monthNames']);_longMon=1;_shortMon=1000;for(i=0;i<12;i++)
{var len=_this.mNames[i].length;if(len>_longMon)
_longMon=len;if(len<_shortMon)
_shortMon=len;}}
for(i=0;i<12;i++)
{_this.mNums[_this.mAbbr[i]]=i;_this.mNums[_this.mNames[i]]=i;}
for(i=0;i<7;i++)
{_this.dNums[_this.dAbbr[i]]=i;_this.dNums[_this.dNames[i]]=i;}})(this);},pad:function(val,len)
{var str=String(Math.abs(val));while(str.length<len)
str='0'+str;if(val<0)
str='-'+str;return str;},widget:function(id,options)
{if(__widgets[id])
throw'Cannot create another AnyTime widget for "'+id+'"';var _this=null;__widgets[id]={twelveHr:false,ajaxOpts:null,denyTab:true,askEra:false,cloak:null,conv:null,bMinW:0,bMinH:0,dMinW:0,dMinH:0,div:null,dB:null,dD:null,dY:null,dMo:null,dDoM:null,hDoM:null,hMo:null,hTitle:null,hY:null,dT:null,dH:null,dM:null,dS:null,earliest:null,fBtn:null,fDOW:0,id:null,inp:null,latest:null,lastAjax:null,lostFocus:false,lX:'X',lY:'X',pop:true,time:null,tMinW:0,tMinH:0,url:null,wMinW:0,wMinH:0,yAhead:null,y0XXX:null,yCur:null,yDiv:null,yLab:null,yNext:null,yPast:null,yPrior:null,initialize:function(id)
{_this=this;this.id='Atw_'+id;if(!options)
options={};this.conv=new AnyTime.Converter(options);if(options.placement)
{if(options.placement=='inline')
this.pop=false;else if(options.placement!='popup')
throw'unknown placement: '+options.placement;}
if(options.ajaxOptions)
{this.ajaxOpts=jQuery.extend({},options.ajaxOptions);if(!this.ajaxOpts.success)
this.ajaxOpts.success=function(data,status){_this.inp[0].value=data;};}
if(options.earliest)
{if(typeof options.earliest.getTime=='function')
this.earliest=options.earliest.getTime();else
this.earliest=this.conv.parse(options.earliest.toString());}
if(options.firstDOW)
{if((options.firstDOW<0)||(options.firstDOW>6))
throw new Exception('illegal firstDOW: '+options.firstDOW);this.fDOW=options.firstDOW;}
if(options.latest)
{if(typeof options.latest.getTime=='function')
this.latest=options.latest.getTime();else
this.latest=this.conv.parse(options.latest.toString());}
this.lX=options.labelDismiss||'X';this.lY=options.labelYear||'Year';var i;var t;var lab;var shownFields=0;var format=this.conv.fmt;if(typeof options.askEra!='undefined')
this.askEra=options.askEra;else
this.askEra=(format.indexOf('%B')>=0)||(format.indexOf('%C')>=0)||(format.indexOf('%E')>=0);var askYear=(format.indexOf('%Y')>=0)||(format.indexOf('%y')>=0)||(format.indexOf('%Z')>=0)||(format.indexOf('%z')>=0);var askMonth=(format.indexOf('%b')>=0)||(format.indexOf('%c')>=0)||(format.indexOf('%M')>=0)||(format.indexOf('%m')>=0);var askDoM=(format.indexOf('%D')>=0)||(format.indexOf('%d')>=0)||(format.indexOf('%e')>=0);var askDate=askYear||askMonth||askDoM;this.twelveHr=(format.indexOf('%h')>=0)||(format.indexOf('%I')>=0)||(format.indexOf('%l')>=0)||(format.indexOf('%r')>=0);var askHour=this.twelveHr||(format.indexOf('%H')>=0)||(format.indexOf('%k')>=0)||(format.indexOf('%T')>=0);var askMinute=(format.indexOf('%i')>=0)||(format.indexOf('%r')>=0)||(format.indexOf('%T')>=0);var askSec=((format.indexOf('%r')>=0)||(format.indexOf('%S')>=0)||(format.indexOf('%s')>=0)||(format.indexOf('%T')>=0));if(askSec&&(typeof options.askSecond!='undefined'))
askSec=options.askSecond;var askTime=askHour||askMinute||askSec;this.inp=$('#'+id);this.div=$('<div class="AtwWindow AtwWidget ui-widget ui-widget-content ui-corner-all" style="width:0;height:0" id="'+this.id+'"/>');this.inp.after(this.div);this.wMinW=this.div.outerWidth(!$.browser.safari);this.wMinH=this.div.AtwHeight(true);this.hTitle=$('<h5 class="AtwTitle ui-widget-header ui-corner-top"/>');this.div.append(this.hTitle);this.dB=$('<div class="AtwBody" style="width:0;height:0"/>');this.div.append(this.dB);this.bMinW=this.dB.outerWidth(true);this.bMinH=this.dB.AtwHeight(true);if(options.hideInput)
this.inp.css({border:0,height:'1px',margin:0,padding:0,width:'1px'});var t=null;var xDiv=null;if(this.pop)
{xDiv=$('<div class="AtwDismissBtn ui-state-default">'+this.lX+'</div>');this.hTitle.append(xDiv);xDiv.click(function(e){_this.dismiss(e);});}
var lab='';if(askDate)
{this.dD=$('<div class="AtwDate" style="width:0;height:0"/>');this.dB.append(this.dD);this.dMinW=this.dD.outerWidth(true);this.dMinH=this.dD.AtwHeight(true);if(askYear)
{this.yLab=$('<h6 class="AtwLbl AtwLblYr">'+this.lY+'</h6>');this.dD.append(this.yLab);this.dY=$('<ul class="AtwYrs ui-helper-reset" />');this.dD.append(this.dY);this.yPast=this.btn(this.dY,'<',this.newYear,['AtwYrsPast'],'- '+this.lY);this.yPrior=this.btn(this.dY,'1',this.newYear,['AtwYrPrior'],'-1 '+this.lY);this.yCur=this.btn(this.dY,'2',this.newYear,['AtwYrCurrent'],this.lY);this.yCur.removeClass('ui-state-default');this.yCur.addClass('AtwCurrentBtn ui-state-default ui-state-highlight');this.yNext=this.btn(this.dY,'3',this.newYear,['AtwYrNext'],'+1 '+this.lY);this.yAhead=this.btn(this.dY,'>',this.newYear,['AtwYrsAhead'],'+ '+this.lY);shownFields++;}
if(askMonth)
{lab=options.labelMonth||'Month';this.hMo=$('<h6 class="AtwLbl AtwLblMonth">'+lab+'</h6>');this.dD.append(this.hMo);this.dMo=$('<ul class="AtwMons" />');this.dD.append(this.dMo);for(i=0;i<12;i++)
this.btn(this.dMo,this.conv.mAbbr[i],function(event)
{var elem=$(event.target);var mo=this.conv.mNums[elem.text()];var t=new Date(this.time.getTime());if(t.getDate()>__daysIn[mo])
t.setDate(__daysIn[mo])
t.setMonth(mo);this.set(t);this.upd(elem);},['AtwMon','AtwMon'+String(i+1)],lab+' '+this.conv.mNames[i]);shownFields++;}
if(askDoM)
{lab=options.labelDayOfMonth||'Day of Month';this.hDoM=$('<h6 class="AtwLbl AtwLblDoM">'+lab+'</h6>');this.dD.append(this.hDoM);this.dDoM=$('<table border="0" cellpadding="0" cellspacing="0" class="AtwDoMTable"/>');this.dD.append(this.dDoM);t=$('<thead class="AtwDoMHead" role="columnheader"/>');this.dDoM.append(t);var tr=$('<tr class="AtwDoW"/>');t.append(tr);for(i=0;i<7;i++)
tr.append('<th class="AtwDoW AtwDoW'+String(i+1)+'">'+this.conv.dAbbr[(this.fDOW+i)%7]+'</th>');var tbody=$('<tbody class="AtwDoMBody" />');this.dDoM.append(tbody);for(var r=0;r<6;r++)
{tr=$('<tr class="AtwWk AtwWk'+String(r+1)+'"/>');tbody.append(tr);for(i=0;i<7;i++)
this.btn(tr,'x',function(event)
{var elem=$(event.target);var dom=Number(elem.html());if(dom)
{var t=new Date(this.time.getTime());t.setDate(dom);this.set(t);this.upd(elem);}},['AtwDoM'],lab);}
shownFields++;}}
if(askTime)
{this.dT=$('<div class="AtwTime" style="width:0;height:0" />');this.dB.append(this.dT);this.tMinW=this.dT.outerWidth(true);this.tMinH=this.dT.AtwHeight(true);if(askHour)
{this.dH=$('<div class="AtwHrs"/>');this.dT.append(this.dH);lab=options.labelHour||'Hour';this.dH.append($('<h6 class="AtwLbl AtwLblHr">'+lab+'</h6>'));var amDiv=$('<ul class="AtwHrsAm"/>');this.dH.append(amDiv);var pmDiv=$('<ul class="AtwHrsPm"/>');this.dH.append(pmDiv);for(i=0;i<12;i++)
{if(this.twelveHr)
{if(i==0)
t='12am';else
t=String(i)+'am';}
else
t=AnyTime.pad(i,2);this.btn(amDiv,t,this.newHour,['AtwHr','AtwHr'+String(i)],lab+' '+t);if(this.twelveHr)
{if(i==0)
t='12pm';else
t=String(i)+'pm';}
else
t=i+12;this.btn(pmDiv,t,this.newHour,['AtwHr','AtwHr'+String(i+12)],lab+' '+t);}
shownFields++;}
if(askMinute)
{this.dM=$('<div class="AtwMins"/>');this.dT.append(this.dM);lab=options.labelMinute||'Minute';this.dM.append($('<h6 class="AtwLbl AtwLblMin">'+lab+'</h6>'));var tensDiv=$('<ul class="AtwMinsTens"/>');this.dM.append(tensDiv);for(i=0;i<6;i++)
this.btn(tensDiv,i,function(event)
{var elem=$(event.target);var t=new Date(this.time.getTime());t.setMinutes((Number(elem.text())*10)+(this.time.getMinutes()%10));this.set(t);this.upd(elem);},['AtwMinTen','AtwMin'+i+'0'],lab+' '+i+'0');var onesDiv=$('<ul class="AtwMinsOnes"/>');this.dM.append(onesDiv);for(i=0;i<10;i++)
this.btn(onesDiv,i,function(event)
{var elem=$(event.target);var t=new Date(this.time.getTime());t.setMinutes((Math.floor(this.time.getMinutes()/10)*10)+Number(elem.text()));this.set(t);this.upd(elem);},['AtwMinOne','AtwMin'+i],lab+' '+i);shownFields++;}
if(askSec)
{this.dS=$('<div class="AtwSecs"/>');this.dT.append(this.dS);lab=options.labelSecond||'Second';this.dS.append($('<h6 class="AtwLbl AtwLblSec">'+lab+'</h6>'));var tensDiv=$('<ul class="AtwSecsTens"/>');this.dS.append(tensDiv);for(i=0;i<6;i++)
this.btn(tensDiv,i,function(event)
{var elem=$(event.target);var t=new Date(this.time.getTime());t.setSeconds((Number(elem.text())*10)+(this.time.getSeconds()%10));this.set(t);this.upd(elem);},['AtwSecTen','AtwSec'+i+'0'],lab+' '+i+'0');var onesDiv=$('<ul class="AtwSecsOnes"/>');this.dS.append(onesDiv);for(i=0;i<10;i++)
this.btn(onesDiv,i,function(event)
{var elem=$(event.target);var t=new Date(this.time.getTime());t.setSeconds((Math.floor(this.time.getSeconds()/10)*10)+Number(elem.text()));this.set(t);this.upd(elem);},['AtwSecOne','AtwSec'+i],lab+' '+i);shownFields++;}}
if(options.labelTitle)
this.hTitle.append(options.labelTitle);else if(shownFields>1)
this.hTitle.append('Select a '+(askDate?(askTime?'Date and Time':'Date'):'Time'));else
this.hTitle.append(' ');try
{this.time=this.conv.parse(this.inp.get(0).value);}
catch(e)
{this.time=new Date();}
this.lastAjax=this.time;if(this.pop)
{this.div.hide();if(__iframe)
__iframe.hide();this.div.css('position','absolute');}
this.inp.keydown(function(e)
{_this.key(e);});this.inp.keypress(function(e)
{if($.browser.opera&&_this.denyTab)
e.preventDefault();});this.inp.focus(function(e)
{if(_this.lostFocus)
_this.showCal(e);_this.lostFocus=false;});this.inp.blur(function(e)
{_this.inpBlur(e);});this.inp.click(function(e)
{_this.showCal(e);});this.div.click(function(e)
{_this.lostFocus=false;_this.inp.focus();});$(window).resize(function(e)
{_this.pos(e);});if(__initialized)
this.onReady();},ajax:function()
{if(this.ajaxOpts&&(this.time.getTime()!=this.lastAjax.getTime()))
{try
{var opts=jQuery.extend({},this.ajaxOpts);if(typeof opts.data=='object')
opts.data[this.inp[0].name||this.inp[0].id]=this.inp[0].value;else
{var opt=(this.inp[0].name||this.inp[0].id)+'='+encodeURI(this.inp[0].value);if(opts.data)
opts.data+='&'+opt;else
opts.data=opt;}
$.ajax(opts);this.lastAjax=this.time;}
catch(e)
{}}
return;},askYear:function(event)
{if(!this.yDiv)
{this.cloak=$('<div class="AtwCloak" style="position:absolute" />');this.div.append(this.cloak);this.yDiv=$('<div class="AtwWindow AtwYrSelector ui-widget ui-widget-content ui-corner-all" style="position:absolute" />');this.div.append(this.yDiv);var title=$('<h5 class="AtwTitle AtwTitleYrSelector ui-widget-header ui-corner-top" />');this.yDiv.append(title);var xDiv=$('<div class="AtwDismissBtn ui-state-default">'+this.lX+'</div>');title.append(xDiv);xDiv.click(function(e){_this.dismissYDiv(e);});this.cloak.click(function(e){_this.dismissYDiv(e);});title.append(this.lY);var yBody=$('<div class="AtwBody AtwBodyYrSelector" />');var yW=yBody.AtwWidth(true);var yH=0;this.yDiv.append(yBody);cont=$('<ul class="AtwYrMil" />');yBody.append(cont);this.y0XXX=this.btn(cont,0,this.newYPos,['AtwMil','AtwMil0'],this.lY+' '+0+'000');for(i=1;i<10;i++)
this.btn(cont,i,this.newYPos,['AtwMil','AtwMil'+i],this.lY+' '+i+'000');yW+=cont.AtwWidth(true);if(yH<cont.AtwHeight(true))
yH=cont.AtwHeight(true);cont=$('<ul class="AtwYrCent" />');yBody.append(cont);for(i=0;i<10;i++)
this.btn(cont,i,this.newYPos,['AtwCent','AtwCent'+i],this.lY+' '+i+'00');yW+=cont.AtwWidth(true);if(yH<cont.AtwHeight(true))
yH=cont.AtwHeight(true);cont=$('<ul class="AtwYrDec" />');yBody.append(cont);for(i=0;i<10;i++)
this.btn(cont,i,this.newYPos,['AtwDec','AtwDec'+i],this.lY+' '+i+'0');yW+=cont.AtwWidth(true);if(yH<cont.AtwHeight(true))
yH=cont.AtwHeight(true);cont=$('<ul class="AtwYrYr" />');yBody.append(cont);for(i=0;i<10;i++)
this.btn(cont,i,this.newYPos,['AtwYr','AtwYr'+i],this.lY+' '+i);yW+=cont.AtwWidth(true);if(yH<cont.AtwHeight(true))
yH=cont.AtwHeight(true);if(this.askEra)
{cont=$('<ul class="AtwYrEra" />');yBody.append(cont);this.btn(cont,this.conv.eAbbr[0],function(event)
{var t=new Date(this.time.getTime());var year=t.getFullYear();if(year>0)
t.setFullYear(0-year);this.set(t);this.updYDiv($(event.target));},['AtwEra','AtwBCE'],this.conv.eAbbr[0]);this.btn(cont,this.conv.eAbbr[1],function(event)
{var t=new Date(this.time.getTime());var year=t.getFullYear();if(year<0)
t.setFullYear(0-year);this.set(t);this.updYDiv($(event.target));},['AtwEra','AtwCE'],this.conv.eAbbr[1]);yW+=cont.AtwWidth(true);if(yH<cont.AtwHeight(true))
yH=cont.AtwHeight(true);}
if($.browser.msie)
yW+=1;yH+=yBody.AtwHeight(true);yBody.css('width',String(yW)+'px');if(!__msie7)
yBody.css('height',String(yH)+'px');if(__msie6||__msie7)
title.width(String(yBody.outerWidth(true)+'px'));yH+=title.AtwHeight(true);if(title.AtwWidth(true)>yW)
yW=title.AtwWidth(true);this.yDiv.css('width',String(yW)+'px');if(!__msie7)
this.yDiv.css('height',String(yH)+'px');}
else
{this.cloak.show();this.yDiv.show();}
this.pos(event);this.updYDiv(null);this.setFocus(this.yDiv.find('.AtwYrBtn.AtwCurrentBtn:first'));},inpBlur:function(event)
{this.lostFocus=true;setTimeout(function()
{if(_this.lostFocus)
{_this.div.find('.AtwFocusBtn').removeClass('AtwFocusBtn ui-state-focus');if(_this.pop)
_this.dismiss(event);else
_this.ajax();}},334);},btn:function(parent,text,handler,classes,title)
{var tagName=((parent[0].nodeName.toLowerCase()=='ul')?'li':'td');var div$='<'+tagName+' class="AtwBtn';for(var i=0;i<classes.length;i++)
div$+=' '+classes[i]+'Btn';var div=$(div$+' ui-state-default">'+text+'</'+tagName+'>');parent.append(div);div.click(function(e)
{_this.tempFunc=handler;_this.tempFunc(e);});return div;},dismiss:function(event)
{this.ajax();this.div.hide();if(__iframe)
__iframe.hide();if(this.yDiv)
this.dismissYDiv();this.lostFocus=true;},dismissYDiv:function(event)
{this.yDiv.hide();this.cloak.hide();this.setFocus(this.yCur);},setFocus:function(btn)
{if(!btn.hasClass('AtwFocusBtn'))
{this.div.find('.AtwFocusBtn').removeClass('AtwFocusBtn ui-state-focus');this.fBtn=btn;btn.removeClass('ui-state-default ui-state-highlight');btn.addClass('AtwFocusBtn ui-state-default ui-state-highlight ui-state-focus');}},key:function(event)
{var t=null;var elem=this.div.find('.AtwFocusBtn');var key=event.keyCode||event.which;this.denyTab=true;if(key==27)
{if(this.yDiv&&this.yDiv.is(':visible'))
this.dismissYDiv(event);else if(this.pop)
this.dismiss(event);}
else if((key==33)||((key==9)&&event.shiftKey))
{if(this.fBtn.hasClass('AtwMilBtn'))
{if(key==9)
this.dismissYDiv(event);}
else if(this.fBtn.hasClass('AtwCentBtn'))
this.yDiv.find('.AtwMilBtn.AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwDecBtn'))
this.yDiv.find('.AtwCentBtn.AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwYrBtn'))
this.yDiv.find('.AtwDecBtn.AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwEraBtn'))
this.yDiv.find('.AtwYrBtn.AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.parents('.AtwYrs').length)
{if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwMonBtn'))
{if(this.dY)
this.yCur.triggerHandler('click');else if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwDoMBtn'))
{if((key==9)&&event.shiftKey)
{this.denyTab=false;return;}
else
{t=new Date(this.time.getTime());if(event.shiftKey)
t.setFullYear(t.getFullYear()-1);else
{var mo=t.getMonth()-1;if(t.getDate()>__daysIn[mo])
t.setDate(__daysIn[mo])
t.setMonth(mo);}
this.keyDateChange(t);}}
else if(this.fBtn.hasClass('AtwHrBtn'))
{t=this.dDoM||this.dMo;if(t)
t.find(__acb).triggerHandler('click');else if(this.dY)
this.yCur.triggerHandler('click');else if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwMinTenBtn'))
{t=this.dH||this.dDoM||this.dMo;if(t)
t.find(__acb).triggerHandler('click');else if(this.dY)
this.yCur.triggerHandler('click');else if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwMinOneBtn'))
this.dM.find(__acb).triggerHandler('click');else if(this.fBtn.hasClass('AtwSecTenBtn'))
{if(this.dM)
t=this.dM.find('.AtwMinsOnes');else
t=this.dH||this.dDoM||this.dMo;if(t)
t.find(__acb).triggerHandler('click');else if(this.dY)
this.yCur.triggerHandler('click');else if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwSecOneBtn'))
this.dS.find(__acb).triggerHandler('click');}
else if((key==34)||(key==9))
{if(this.fBtn.hasClass('AtwMilBtn'))
this.yDiv.find('.AtwCentBtn.AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwCentBtn'))
this.yDiv.find('.AtwDecBtn.AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwDecBtn'))
this.yDiv.find('.AtwYrBtn.AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwYrBtn'))
{t=this.yDiv.find('.AtwEraBtn.AtwCurrentBtn');if(t.length)
t.triggerHandler('click');else if(key==9)
this.dismissYDiv(event);}
else if(this.fBtn.hasClass('AtwEraBtn'))
{if(key==9)
this.dismissYDiv(event);}
else if(this.fBtn.parents('.AtwYrs').length)
{t=this.dDoM||this.dMo||this.dH||this.dM||this.dS;if(t)
t.find(__acb).triggerHandler('click');else if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwMonBtn'))
{t=this.dDoM||this.dH||this.dM||this.dS;if(t)
t.find(__acb).triggerHandler('click');else if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwDoMBtn'))
{if(key==9)
{t=this.dH||this.dM||this.dS;if(t)
t.find(__acb).triggerHandler('click');else
{this.denyTab=false;return;}}
else
{t=new Date(this.time.getTime());if(event.shiftKey)
t.setFullYear(t.getFullYear()+1);else
{var mo=t.getMonth()+1;if(t.getDate()>__daysIn[mo])
t.setDate(__daysIn[mo])
t.setMonth(mo);}
this.keyDateChange(t);}}
else if(this.fBtn.hasClass('AtwHrBtn'))
{t=this.dM||this.dS;if(t)
t.find(__acb).triggerHandler('click');else if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwMinTenBtn'))
this.dM.find('.AtwMinsOnes .AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwMinOneBtn'))
{if(this.dS)
this.dS.find(__acb).triggerHandler('click');else if(key==9)
{this.denyTab=false;return;}}
else if(this.fBtn.hasClass('AtwSecTenBtn'))
this.dS.find('.AtwSecsOnes .AtwCurrentBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwSecOneBtn'))
{if(key==9)
{this.denyTab=false;return;}}}
else if(key==35)
{if(this.fBtn.hasClass('AtwMilBtn')||this.fBtn.hasClass('AtwCentBtn')||this.fBtn.hasClass('AtwDecBtn')||this.fBtn.hasClass('AtwYrBtn')||this.fBtn.hasClass('AtwEraBtn'))
{t=this.yDiv.find('.AtwCEBtn');if(!t.length)
t=this.yDiv.find('.AtwYr9Btn');t.triggerHandler('click');}
else if(this.fBtn.hasClass('AtwDoMBtn'))
{t=new Date(this.time.getTime());t.setDate(1);t.setMonth(t.getMonth()+1);t.setDate(t.getDate()-1);if(event.ctrlKey)
t.setMonth(11);this.keyDateChange(t);}
else if(this.dS)
this.dS.find('.AtwSec9Btn').triggerHandler('click');else if(this.dM)
this.dM.find('.AtwMin9Btn').triggerHandler('click');else if(this.dH)
this.dH.find('.AtwHr23Btn').triggerHandler('click');else if(this.dDoM)
this.dDoM.find('.AtwDoMBtnFilled:last').triggerHandler('click');else if(this.dMo)
this.dMo.find('.AtwMon12Btn').triggerHandler('click');else if(this.dY)
this.yAhead.triggerHandler('click');}
else if(key==36)
{if(this.fBtn.hasClass('AtwMilBtn')||this.fBtn.hasClass('AtwCentBtn')||this.fBtn.hasClass('AtwDecBtn')||this.fBtn.hasClass('AtwYrBtn')||this.fBtn.hasClass('AtwEraBtn'))
{this.yDiv.find('.AtwMil0Btn').triggerHandler('click');}
else if(this.fBtn.hasClass('AtwDoMBtn'))
{t=new Date(this.time.getTime());t.setDate(1);if(event.ctrlKey)
t.setMonth(0);this.keyDateChange(t);}
else if(this.dY)
this.yCur.triggerHandler('click');else if(this.dMo)
this.dMo.find('.AtwMon1Btn').triggerHandler('click');else if(this.dDoM)
this.dDoM.find('.AtwDoMBtnFilled:first').triggerHandler('click');else if(this.dH)
this.dH.find('.AtwHr0Btn').triggerHandler('click');else if(this.dM)
this.dM.find('.AtwMin00Btn').triggerHandler('click');else if(this.dS)
this.dS.find('.AtwSec00Btn').triggerHandler('click');}
else if(key==37)
{if(this.fBtn.hasClass('AtwDoMBtn'))
this.keyDateChange(new Date(this.time.getTime()-(24*60*60*1000)));else
this.keyBack();}
else if(key==38)
{if(this.fBtn.hasClass('AtwDoMBtn'))
this.keyDateChange(new Date(this.time.getTime()-(7*24*60*60*1000)));else
this.keyBack();}
else if(key==39)
{if(this.fBtn.hasClass('AtwDoMBtn'))
this.keyDateChange(new Date(this.time.getTime()+(24*60*60*1000)));else
this.keyAhead();}
else if(key==40)
{if(this.fBtn.hasClass('AtwDoMBtn'))
this.keyDateChange(new Date(this.time.getTime()+(7*24*60*60*1000)));else
this.keyAhead();}
else
this.showCal(null);event.preventDefault();},keyAhead:function()
{if(this.fBtn.hasClass('AtwMil9Btn'))
this.yDiv.find('.AtwCent0Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwCent9Btn'))
this.yDiv.find('.AtwDec0Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwDec9Btn'))
this.yDiv.find('.AtwYr0Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwYr9Btn'))
this.yDiv.find('.AtwBCEBtn').triggerHandler('click');else if(this.fBtn.hasClass('AtwSec9Btn'))
{}
else if(this.fBtn.hasClass('AtwSec50Btn'))
this.dS.find('.AtwSec0Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwMin9Btn'))
{if(this.dS)
this.dS.find('.AtwSec00Btn').triggerHandler('click');}
else if(this.fBtn.hasClass('AtwMin50Btn'))
this.dM.find('.AtwMin0Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwHr23Btn'))
{if(this.dM)
this.dM.find('.AtwMin00Btn').triggerHandler('click');else if(this.dS)
this.dS.find('.AtwSec00Btn').triggerHandler('click');}
else if(this.fBtn.hasClass('AtwHr11Btn'))
this.dH.find('.AtwHr12Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwMon12Btn'))
{if(this.dDoM)
this.dDoM.find(__acb).triggerHandler('click');else if(this.dH)
this.dH.find('.AtwHr0Btn').triggerHandler('click');else if(this.dM)
this.dM.find('.AtwMin00Btn').triggerHandler('click');else if(this.dS)
this.dS.find('.AtwSec00Btn').triggerHandler('click');}
else if(this.fBtn.hasClass('AtwYrsAheadBtn'))
{if(this.dMo)
this.dMo.find('.AtwMon1Btn').triggerHandler('click');else if(this.dH)
this.dH.find('.AtwHr0Btn').triggerHandler('click');else if(this.dM)
this.dM.find('.AtwMin00Btn').triggerHandler('click');else if(this.dS)
this.dS.find('.AtwSec00Btn').triggerHandler('click');}
else if(this.fBtn.hasClass('AtwYrCurrentBtn'))
this.yNext.triggerHandler('click');else
this.fBtn.next().triggerHandler('click');},keyBack:function()
{if(this.fBtn.hasClass('AtwCent0Btn'))
this.yDiv.find('.AtwMil9Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwDec0Btn'))
this.yDiv.find('.AtwCent9Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwYr0Btn'))
this.yDiv.find('.AtwDec9Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwBCEBtn'))
this.yDiv.find('.AtwYr9Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwYrCurrentBtn'))
this.yPrior.triggerHandler('click');else if(this.fBtn.hasClass('AtwMon1Btn'))
{if(this.dY)
this.yCur.triggerHandler('click');}
else if(this.fBtn.hasClass('AtwHr0Btn'))
{if(this.dDoM)
this.dDoM.find(__acb).triggerHandler('click');else if(this.dMo)
this.dMo.find('.AtwMon12Btn').triggerHandler('click');else if(this.dY)
this.yNext.triggerHandler('click');}
else if(this.fBtn.hasClass('AtwHr12Btn'))
this.dH.find('.AtwHr11Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwMin00Btn'))
{if(this.dH)
this.dH.find('.AtwHr23Btn').triggerHandler('click');else if(this.dDoM)
this.dDoM.find(__acb).triggerHandler('click');else if(this.dMo)
this.dMo.find('.AtwMon12Btn').triggerHandler('click');else if(this.dY)
this.yNext.triggerHandler('click');}
else if(this.fBtn.hasClass('AtwMin0Btn'))
this.dM.find('.AtwMin50Btn').triggerHandler('click');else if(this.fBtn.hasClass('AtwSec00Btn'))
{if(this.dM)
this.dM.find('.AtwMin9Btn').triggerHandler('click');else if(this.dH)
this.dH.find('.AtwHr23Btn').triggerHandler('click');else if(this.dDoM)
this.dDoM.find(__acb).triggerHandler('click');else if(this.dMo)
this.dMo.find('.AtwMon12Btn').triggerHandler('click');else if(this.dY)
this.yNext.triggerHandler('click');}
else if(this.fBtn.hasClass('AtwSec0Btn'))
this.dS.find('.AtwSec50Btn').triggerHandler('click');else
this.fBtn.prev().triggerHandler('click');},keyDateChange:function(newDate)
{if(this.fBtn.hasClass('AtwDoMBtn'))
{this.set(newDate);this.upd(null);this.setFocus(this.dDoM.find(__acb));}},newHour:function(event)
{var h;var t;var elem=$(event.target);if(!this.twelveHr)
h=Number(elem.text());else
{var str=elem.text();t=str.indexOf('a');if(t<0)
{t=Number(str.substr(0,str.indexOf('p')));h=((t==12)?12:(t+12));}
else
{t=Number(str.substr(0,t));h=((t==12)?0:t);}}
t=new Date(this.time.getTime());t.setHours(h);this.set(t);this.upd(elem);},newYear:function(event)
{var elem=$(event.target);var txt=elem.text();if((txt=='<')||(txt=='<'))
this.askYear(event);else if((txt=='>')||(txt=='>'))
this.askYear(event);else
{var t=new Date(this.time.getTime());t.setFullYear(Number(txt));this.set(t);this.upd(this.yCur);}},newYPos:function(event)
{var elem=$(event.target);var era=1;var year=this.time.getFullYear();if(year<0)
{era=(-1);year=0-year;}
year=AnyTime.pad(year,4);if(elem.hasClass('AtwMilBtn'))
year=elem.html()+year.substring(1,4);else if(elem.hasClass('AtwCentBtn'))
year=year.substring(0,1)+elem.html()+year.substring(2,4);else if(elem.hasClass('AtwDecBtn'))
year=year.substring(0,2)+elem.html()+year.substring(3,4);else
year=year.substring(0,3)+elem.html();if(year=='0000')
year=1;var t=new Date(this.time.getTime());t.setFullYear(era*year);this.set(t);this.updYDiv(elem);},onReady:function()
{this.lostFocus=true;if(!this.pop)
this.upd(null);else
{if(this.div.parent()!=document.body)
this.div.appendTo(document.body);}},pos:function(event)
{if(this.pop)
{var off=this.inp.offset();var bodyWidth=$(document.body).outerWidth(true);var widgetWidth=this.div.outerWidth(true);var left=off.left;if(left+widgetWidth>bodyWidth-20)
left=bodyWidth-(widgetWidth+20);var top=off.top-this.div.outerHeight(true);if(top<0)
top=off.top+this.inp.outerHeight(true);this.div.css({top:String(top)+'px',left:String(left<0?0:left)+'px'});}
if(this.yDiv)
{var wOff=this.div.offset();var yOff=this.yLab.offset();if(this.div.css('position')=='absolute')
{yOff.top-=wOff.top;yOff.left=yOff.left-wOff.left;wOff={top:0,left:0};}
yOff.left+=((this.yLab.outerWidth(true)-this.yDiv.outerWidth(true))/2);this.cloak.css({top:wOff.top+'px',left:wOff.left+'px',height:String(this.div.outerHeight(true)-2)+'px',width:String(this.div.outerWidth(!$.browser.safari)-2)+'px'});this.yDiv.css({top:yOff.top+'px',left:yOff.left+'px'});}},set:function(newTime)
{var t=newTime.getTime();if(this.earliest&&(t<this.earliest))
this.time=new Date(this.earliest);else if(this.latest&&(t>this.latest))
this.time=new Date(this.latest);else
this.time=newTime;},showCal:function(event)
{try
{this.time=this.conv.parse(this.inp.get(0).value);}
catch(e)
{this.time=new Date();}
this.upd(null);fBtn=null;var cb='.AtwCurrentBtn:first';if(this.dDoM)
fBtn=this.dDoM.find(cb);else if(this.yCur)
fBtn=this.yCur;else if(this.dMo)
fBtn=this.dMo.find(cb);else if(this.dH)
fBtn=this.dH.find(cb);else if(this.dM)
fBtn=this.dM.find(cb);else if(this.dS)
fBtn=this.dS.find(cb);this.setFocus(fBtn);this.pos(event);if(this.pop&&__iframe)
setTimeout(function()
{var pos=_this.div.offset();__iframe.css({height:String(_this.div.outerHeight(true))+'px',left:String(pos.left)+'px',position:'absolute',top:String(pos.top)+'px',width:String(_this.div.outerWidth(true))+'px'});__iframe.show();},300);},upd:function(fBtn)
{var current=this.time.getFullYear();if(this.yPrior)
this.yPrior.text(AnyTime.pad((current==1)?(-1):(current-1),4));if(this.yCur)
this.yCur.text(AnyTime.pad(current,4));if(this.yNext)
this.yNext.text(AnyTime.pad((current==-1)?1:(current+1),4));var i=0;current=this.time.getMonth();$('#'+this.id+' .AtwMonBtn').each(function()
{_this.updCur($(this),i==current);i++;});current=this.time.getDate();var currentMonth=this.time.getMonth();var dom=new Date(this.time.getTime());dom.setDate(1);var dow1=dom.getDay();if(this.fDOW>dow1)
dow1+=7;var wom=0,dow=0;$('#'+this.id+' .AtwWk').each(function()
{dow=_this.fDOW;$(this).children().each(function()
{if(dow-_this.fDOW<7)
{var td=$(this);if(((wom==0)&&(dow<dow1))||(dom.getMonth()!=currentMonth))
{td.html(' ');td.removeClass('AtwDoMBtnFilled AtwCurrentBtn ui-state-default ui-state-highlight');td.addClass('AtwDoMBtnEmpty');if(wom)
{if((dom.getDate()==1)&&(dow!=1))
td.addClass('AtwDoMBtnEmptyAfterFilled');else
td.removeClass('AtwDoMBtnEmptyAfterFilled');if(dom.getDate()<=7)
td.addClass('AtwDoMBtnEmptyBelowFilled');else
td.removeClass('AtwDoMBtnEmptyBelowFilled');dom.setDate(dom.getDate()+1);}
else
{td.addClass('AtwDoMBtnEmptyAboveFilled');if(dow==dow1-1)
td.addClass('AtwDoMBtnEmptyBeforeFilled');else
td.removeClass('AtwDoMBtnEmptyBeforeFilled');}
td.addClass('ui-state-default ui-state-disabled');}
else
{td.text(dom.getDate());td.removeClass('AtwDoMBtnEmpty AtwDoMBtnEmptyAboveFilled AtwDoMBtnEmptyBeforeFilled '+'AtwDoMBtnEmptyAfterFilled AtwDoMBtnEmptyBelowFilled '+'ui-state-default ui-state-disabled');td.addClass('AtwDoMBtnFilled ui-state-default');_this.updCur(td,dom.getDate()==current);dom.setDate(dom.getDate()+1);}}
dow++;});wom++;});var not12=!this.twelveHr;var h=this.time.getHours();$('#'+this.id+' .AtwHrBtn').each(function()
{_this.updCur($(this),(not12&&(Number(this.innerHTML)==h))||(this.innerHTML==String((h==0)?12:((h<=12)?h:(h-12)))+((h<12)?'am':'pm')));});var mi=this.time.getMinutes();var tens=Math.floor(mi/10);var ones=mi%10;$('#'+this.id+' .AtwMinTenBtn').each(function()
{_this.updCur($(this),this.innerHTML==String(tens));});$('#'+this.id+' .AtwMinOneBtn').each(function()
{_this.updCur($(this),this.innerHTML==String(ones));});var mi=this.time.getSeconds();var tens=Math.floor(mi/10);var ones=mi%10;$('#'+this.id+' .AtwSecTenBtn').each(function()
{_this.updCur($(this),this.innerHTML==String(tens));});$('#'+this.id+' .AtwSecOneBtn').each(function()
{_this.updCur($(this),this.innerHTML==String(ones));});if(fBtn)
this.setFocus(fBtn);this.inp.get(0).value=this.conv.format(this.time);this.div.show();var d,totH=0,totW=0,dYW=0,dMoW=0,dDoMW=0;if(this.dY)
{totW=dYW=this.dY.outerWidth(true);totH=this.yLab.AtwHeight(true)+this.dY.AtwHeight(true);}
if(this.dMo)
{dMoW=this.dMo.outerWidth(true);if(dMoW>totW)
totW=dMoW;totH+=this.hMo.AtwHeight(true)+this.dMo.AtwHeight(true);}
if(this.dDoM)
{dDoMW=this.dDoM.outerWidth(true);if(dDoMW>totW)
totW=dDoMW;if(__msie6||__msie7)
{if(dMoW>dDoMW)
this.dDoM.css('width',String(dMoW)+'px');else if(dYW>dDoMW)
this.dDoM.css('width',String(dYW)+'px');}
totH+=this.hDoM.AtwHeight(true)+this.dDoM.AtwHeight(true);}
if(this.dD)
{this.dD.css({width:String(totW)+'px',height:String(totH)+'px'});totW+=this.dMinW;totH+=this.dMinH;}
var w=0,h=0,timeH=0,timeW=0;if(this.dH)
{w=this.dH.outerWidth(true);timeW+=w+1;h=this.dH.AtwHeight(true);if(h>timeH)
timeH=h;}
if(this.dM)
{w=this.dM.outerWidth(true);timeW+=w+1;h=this.dM.AtwHeight(true);if(h>timeH)
timeH=h;}
if(this.dS)
{w=this.dS.outerWidth(true);timeW+=w+1;h=this.dS.AtwHeight(true);if(h>timeH)
timeH=h;}
if(this.dT)
{this.dT.css({width:String(timeW)+'px',height:String(timeH)+'px'});timeW+=this.tMinW+1;timeH+=this.tMinH;totW+=timeW;if(timeH>totH)
totH=timeH;}
this.dB.css({height:String(totH)+'px',width:String(totW)+'px'});totH+=this.bMinH;totW+=this.bMinW;totH+=this.hTitle.AtwHeight(true)+this.wMinH;totW+=this.wMinW;if(this.hTitle.outerWidth(true)>totW)
totW=this.hTitle.outerWidth(true);this.div.css({height:String(totH)+'px',width:String(totW)+'px'});if(!this.pop)
this.ajax();},updCur:function(btn,cur)
{if(cur)
{btn.removeClass('ui-state-default ui-state-highlight');btn.addClass('AtwCurrentBtn ui-state-default ui-state-highlight');}
else
btn.removeClass('AtwCurrentBtn ui-state-highlight');},updYDiv:function(fBtn)
{var era=1;var yearValue=this.time.getFullYear();if(yearValue<0)
{era=(-1);yearValue=0-yearValue;}
yearValue=AnyTime.pad(yearValue,4);this.yDiv.find('.AtwMilBtn').each(function()
{_this.updCur($(this),this.innerHTML==yearValue.substring(0,1));});this.yDiv.find('.AtwCentBtn').each(function()
{_this.updCur($(this),this.innerHTML==yearValue.substring(1,2));});this.yDiv.find('.AtwDecBtn').each(function()
{_this.updCur($(this),this.innerHTML==yearValue.substring(2,3));});this.yDiv.find('.AtwYrBtn').each(function()
{_this.updCur($(this),this.innerHTML==yearValue.substring(3));});this.yDiv.find('.AtwBCEBtn').each(function()
{_this.updCur($(this),era<0);});this.yDiv.find('.AtwCEBtn').each(function()
{_this.updCur($(this),era>0);});this.inp.get(0).value=this.conv.format(this.time);this.upd(fBtn);}};__widgets[id].initialize(id);}};})();

if(window.location.hostname.length&&(window.location.hostname!='www.ama3.com'))alert('REMOVE THE LAST LINE FROM anytimec.js!');