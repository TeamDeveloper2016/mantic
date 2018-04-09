/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 11/06/2014
 *time 06:17:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */

(function (window) {
  var mmenu;
  Janal.Control.Mmenu = {};
  Janal.Control.Mmenu.Core = Class.extend({
    ID_MENU: 'collapseMenu',
    ID_PANEL_MENU: 'panelMmenu',
    ID_MENU_SENTINEL: 'menuSentinel',
    init: function () {
      if ($('#' + this.ID_MENU).length > 0) {
        $("#" + this.ID_MENU).mmenu({
          counters: true,
          extensions: ["effect-zoom-menu", "effect-slide-listitems", "effect-zoom-panels", "multiline", "theme-dark"],
          searchfield: {
            add: true,
            noResults: "Sin resultados",
            placeholder: "Buscar ...",
            showTextItems: true,
            showSubPanels: true
          },
          navbar: {
            title: 'Menu principal'
          },
          navbars: [{
              position: 'top',
              content: ['searchfield']
            },
            {
              position: 'top',
              content: ['prev', 'title', 'close']
            }]
        });
      } // if  
    }, // init
    toggleMenu: function () {
      if ($("#" + this.ID_PANEL_MENU).is(":visible")) {
        $("#" + this.ID_PANEL_MENU).hide();
        $("#headerSlide").hide();
        $("#" + this.ID_MENU_SENTINEL).show();
      } // if
      else {
        $("#" + this.ID_PANEL_MENU).show();
        $("#headerSlide").show();
        $("#" + this.ID_MENU_SENTINEL).hide();
      } // else
    }, // toggleMenu
    open: function (theme) {
      $("#" + this.ID_MENU).data("mmenu").open();
      this.change(theme);
    }, // open
    change: function (theme) {
      PrimeFaces.changeTheme(theme);
      $("#" + this.ID_MENU).removeClass(function (index, name) {
        items = name.split(' ');
        $.each(items, function (index, value) {
          if (value.indexOf('mm-theme-') >= 0)
            name = value;
        });
        return name;
      });
      $("#" + this.ID_MENU).addClass("mm-theme-" + theme);
    }
  });
})(window);
$(document).ready(function () {
  mmenu = new Janal.Control.Mmenu.Core();
  console.info("Janal.Control.Mmenu.Core initialized");
});