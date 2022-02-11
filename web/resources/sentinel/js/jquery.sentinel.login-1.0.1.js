$(function() {
    var loginBox = $('#login-box');
    if(loginBox.length) {
      var tabHeaders = loginBox.find('.TabBtn'),
      tabContents = loginBox.find('.TabContent');
      tabHeaders.on('click', function(e) {
        tabHeaders.removeClass('TabBtnActiveLeft TabBtnActiveRight');
        tabContents.addClass('DispNone');
        if($(this).hasClass('left')) {
          $(this).addClass('TabBtnActiveLeft');
          $('#tab_'+ $(this).attr('panel')).removeClass('DispNone');
        } // if
        else {
          $(this).addClass('TabBtnActiveRight');
          $('#tab_'+ $(this).attr('panel')).removeClass('DispNone');
        } // else
        e.preventDefault();
      });
    } // login
});