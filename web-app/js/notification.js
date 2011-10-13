$(function() 
{
    $(window).bind('load', function() 
    { 
        $("#notifications > *").each(function ()
        {
            var anchorSelector = $(this).attr("anchorSelector")
            
            // Anchor element may not be visible on this page.
            var anchor = $(anchorSelector)
            
            var target = 'rightMiddle'
            var tooltip = 'leftMiddle'
            var tip = 'leftMiddle'
            
            if (anchorSelector == "#userlogin > [href$='/person/create']")
            {
                // If the (unauthenticated) user has previously acknowledged the
                // register notification, there will be a register cookie.
                if ($.cookie('REGISTER'))
                {
                    return
                }
                    
                // Special case for register, as this tip must be displayed below the link.
                target = 'bottomLeft'
                tooltip = 'topRight'
                tip = 'topRight'
            }
            
            if (anchor != null)
            {
                anchor.qtip(
                {
                   content: $(this).attr("htmlFragment"),
                   position: {
                      corner: {
                         target: target,
                         tooltip: tooltip
                      }
                   },
                   style: { 
                      width: 200,
                      padding: 5,
                      background: '#FFFF66',
                      color: 'black',
                      textAlign: 'center',
                      border: {
                         width: 5,
                         radius: 7,
                         color: '#FFFF66'
                      },
                      tip: tip,
                      name: 'dark' // Inherit the rest of the attributes from the preset dark style
                   },
                   show: {
                      when: 'click',
                      ready: true
                   },
                   hide: {
                       when: 'click',
                       target: anchor
                   },
                   data: {'key':$(this).attr("key")},
                   api : 
                   {
                       onHide : function()
                       {
                           acknowledge($(this)[0].options.data.key)
                       },
                       
                       onRender: function() 
                       {
                            this.elements.tooltip.click(this.hide)
                       }
                   }
                });
            }
        });
    })
});

function acknowledge(key)
{
    // Special case for register - store this in a cookie if user has acknowledged
    // this one.
    if (key == "REGISTER")
    {
        $.cookie('REGISTER', "acknowledged", { expires: 7, path: '/'});    
    }
    else
    {
        $.post(contextPath + '/notification/acknowledge',
               {'key':key},
               function(data)
               {
                   // Nothing to do.
                   console.log(data)
               },
               'json');
               
        console.log("posted")
    }
}
