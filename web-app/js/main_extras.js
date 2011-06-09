
   $(document).ready(function () {

     // all forms to have highlighting of rows
     $('table').highlight();

     // all date fields should have a class of 'datePicker'
      $( ".datePicker" ).datepicker({ dateFormat:   'dd/mm/yy', constrainInput: true, changeYear: true,  yearRange: '2006:2018'});
      $('.datePicker').attr("size", "10");

      modalMask();
      dressInputs();


       $(window).scroll(function() {
           centreDialog();
       });
      $(window).resize(function() {
           centreDialog();
       });

        // input 'q' is for the searchable plugin
        $('#q').keyup(function() {
            $(this).parent("form").submit();
        });
        // COMMENT THIS !!!!!!!
        $('#s').live('click', function() {
            $('#q').val($(this).text());
            $('#q').parent("form").submit();
            return false;
        });
        $('#clear').live('click', function() {
            $('#q').val('');
            $('#q').parent("form").submit();
            return false;
        });





  }); // end document ready


 // modal mask
  (function($){
         $.fn.extend({
              center: function (options) {
                   var options =  $.extend({ // Default values
                        inside:window, // element, center into window
                        transition: 0, // millisecond, transition time
                        minX:0, // pixel, minimum left element value
                        minY:0, // pixel, minimum top element value
                        withScrolling:true, // booleen, take care of the scrollbar (scrollTop)
                        vertical:true, // booleen, center vertical
                        horizontal:true // booleen, center horizontal
                   }, options);
                   return this.each(function() {
                        var props = {position:'absolute'};
                        if (options.vertical) {
                             var top = ($(options.inside).height() - $(this).outerHeight()) / 2;
                             if (options.withScrolling) top += $(options.inside).scrollTop() || 0;
                             top = (top > options.minY ? top : options.minY);
                             $.extend(props, {top: top+'px'});
                        }
                        if (options.horizontal) {
                              var left = ($(options.inside).width() - $(this).outerWidth()) / 2;
                              if (options.withScrolling) left += $(options.inside).scrollLeft() || 0;
                              left = (left > options.minX ? left : options.minX);
                              $.extend(props, {left: left+'px'});
                        }
                        if (options.transition > 0) $(this).animate(props, options.transition);
                        else $(this).css(props);

                        //$(this).animate(props, 300);
                        return $(this);
                   });
              }
         });
    })(jQuery);


   function clearDatePickerInput (id) {
      $('#datePicker' + id).val("");
  }

  function dressInputs() {
          // all input fields that need to be larger in size
      $('input[name*="name"],\n\
           input[name*="Name"],\n\
           input[name*="partners"],\n\
           input[name*="emailAddress"],\n\
           input[name*="comment"]\n\
          ').attr("size", "45");
      $('input[name*="title"],\n\
           input[name*="Title"]\n\
          ').attr("size", "75");
    // all input fields that need to be smaller in size
    // 'No' should match pageNo IssueNo etc
      $('input[name="initials"],\n\
           input[name="salutation"],\n\
           input[name="shortName"],\n\
           input[name="code"],\n\
           input[name*="No"],\n\
           input[name="acronym"]\n\
          ').attr("size", "10");

      $('div.showItems').prepend("<h5>Select items then sort by dragging</h5>\n");
  }

  function modalMask() {


     // The modal mask function
     //select all the a tag with name equal to modal
    $('a[name=modal]').click(function(e) {
        //Cancel the link behavior
        e.preventDefault();
        
        // Get the A href value as the URL
        var src =   grailsServerURL +   $(this).attr('href') + "?popup=true";
        $("#dialogIframe").attr('src',src);

        //transition effect
        $('#mask').fadeIn("fast");
        $('#mask').fadeTo("slow",0.8);

        centreDialog();
        
        //transition effect
        $('#dialog').fadeIn(100);


    });

    //if close button is clicked
    $('.window .close').click(function (e) {
        //Cancel the link behavior
        e.preventDefault();   
       closeMask();
        
    });

    //if mask is clicked
    $('#mask').click(function () {
        closeMask();
    });   
    
    
  }
   function centreDialog() {

        //Get the screen height and width
        var theHeight = $(document).height();
        var theWidth = $(window).width();
        //Set height and width to mask to fill up the whole screen
        $('#mask').css({'width':theWidth,'height':theHeight});
        var dialogHeight = $(window).height() - 80
        $('#dialog').css({'height':dialogHeight});
        $('#dialogIframe').css({'height':dialogHeight-20});

        $('#dialog').center({minY:20, transition:50}); //extended jQuery function

 }
  
  function closeMask() {      
      $('#mask, .window').hide();
      $("#dialogIframe").attr('src',null);
      // submit form so that it reloads values
      $('form').submit()
  }

