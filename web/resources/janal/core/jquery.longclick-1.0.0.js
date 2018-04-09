/**
 * jQuery Longclick Event
 * ======================
 * Press & hold mouse button "long click" special event for jQuery 1.4.x
 *
 * @license Longclick Event
 * Copyright (c) 2010 Petr Vostrel (http://petr.vostrel.cz/)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * Version: 1.0.0
 * Updated: 2010-06-22
 */
(function($) {
    $.fn.longClick = function(callback, timeout) {
      var timer;
      timeout = timeout || 500;
      $(this).mousedown(function(e) {
        var cursor= {pageX: e.pageX, pageY: e.pageY};
        timer = setTimeout(function() { callback(e, cursor); }, timeout);
        return false;
      });
      $(document).mouseup(function() {
        clearTimeout(timer);
        return false;
      });
    };
})(jQuery);