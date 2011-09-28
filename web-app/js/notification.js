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
            
            if (anchorSelector == "#userlogin > [href^='/aatams/person/create']")
            {
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
                
                anchor.qtip('api').show();
            }
        });
    })
});

function acknowledge(key)
{
    console.log("key: " + key)
    
    $.post('/aatams/notification/acknowledge',
           {'key':key},
           function()
           {
               // Nothing to do.
           },
           'json');
}
