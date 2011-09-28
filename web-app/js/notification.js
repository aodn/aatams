$(function() 
{
    $(window).bind('load', function() 
    { 
/**        
        $("#notifications > *").each(function ()
        {
            var anchorSelector = $(this).attr("anchorSelector")
            
            // Anchor element may not be visible on this page.
            var anchor = $(anchorSelector)
            
            if (anchor != null)
            {
                console.log("anchor: ")
                console.log(anchor)
                console.log("$(this)")
                console.log($(this))
                
                anchor.bubbletip($(this))
            }
        });
        */
       
//        $("[href='/aatams/gettingStarted/index']").bubbletip($("#notification_1"), { deltaDirection: 'right' })
        $("[href='/aatams/gettingStarted/index']").qtip(
        {
           content: 'This is an active list element',
           position: {
              corner: {
                 target: 'rightMiddle',
                 tooltip: 'leftMiddle'
              }
           },
           style: { 
              width: 200,
              padding: 5,
              background: '#FFFF66',
              color: 'black',
              textAlign: 'center',
              border: {
                 width: 7,
                 radius: 5,
                 color: '#FFFF66'
              },
              tip: 'leftMiddle',
              name: 'dark' // Inherit the rest of the attributes from the preset dark style
           },
           show: 'mouseover',
           hide: 'mouseout'
        });
        
        $("#userlogout").qtip(
        {
           content: 'This is an active list element',
           show: 'mouseover',
           hide: 'mouseout'
        })
        
    })
});
