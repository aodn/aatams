/**
 * Thanks to:
 * 
 * http://misterdai.wordpress.com/2010/06/04/jquery-form-changed-warning/
 */

var catcher = function() {
  var changed = false;
    $('form').each(function() {
    if ($(this).data('initialForm') != $(this).serialize()) {
      changed = true;
      $(this).addClass('changed');
    } else {
      $(this).removeClass('changed');
    }
  });
  if (changed) {
    return 'One or more forms have changed!';
  }
};

$(function() {
  $('form').each(function() {
	  $(this).data('initialForm', $(this).serialize());
  }).submit(function(e) {
    var formEl = this;
    var changed = false;
    $('form').each(function() {
      if (this != formEl && $(this).data('initialForm') != $(this).serialize()) {
        changed = true;
        $(this).addClass('changed');
      } else {
        $(this).removeClass('changed');
      }
    });
    $(window).unbind('beforeunload', catcher);
  });
  $(window).bind('beforeunload', catcher);
});
