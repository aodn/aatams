
// JS for suckerfish menus in IE
// from http://snippets.dzone.com/posts/show/2150

// patch IE's :hover & :focus problems with tags other than <a>

sfHover = function() {
  var sfEls = document.getElementById("nav").getElementsByTagName("li");
  for (var i=0; i<sfEls.length; i++) 
    {
    sfEls[i].onmouseover=function() 
      {
      sel = document.getElementsByTagName("select");
      for (var i=0; i<sel.length; i++) 
        {
        sel[i].style.visibility = 'hidden';	
        }
      this.className+=" sfhover";
      }
    sfEls[i].onmouseout=function() 
      {
      sel = document.getElementsByTagName("select");
      for (var i=0; i<sel.length; i++) 
        {
        sel[i].style.visibility = 'visible';	
        }
      this.className=this.className.replace(new RegExp(" sfhover\\b"), "");
      }
    }


if(document.getElementById("nav2"))
{
  var sfEls = document.getElementById("nav2").getElementsByTagName("li");
  for (var i=0; i<sfEls.length; i++) 
    {
    sfEls[i].onmouseover=function() 
      {
      sel = document.getElementsByTagName("select");
      for (var i=0; i<sel.length; i++) 
        {
        sel[i].style.visibility = 'hidden';	
        }
      this.className+=" sfhover";
      }
    sfEls[i].onmouseout=function() 
      {
      sel = document.getElementsByTagName("select");
      for (var i=0; i<sel.length; i++) 
        {
        sel[i].style.visibility = 'visible';	
        }
      this.className=this.className.replace(new RegExp(" sfhover\\b"), "");
      }
    }
} // end if nav2
}

// only win:
if (window.attachEvent) window.attachEvent("onload", sfHover);

