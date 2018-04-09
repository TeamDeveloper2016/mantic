/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 17/09/2019
 *time 20:42:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */

(function(window){
	var jsMenu;
	Janal.Control.LeftMenu= {};
	Janal.Control.LeftMenu.Core= Class.extend({
		ID_MENU					: 'my-menu',
		ID_PANEL_MENU		: 'panelMmenu',
		ID_MENU_SENTINEL: 'menuSentinel',
		init: function(){
      if($('#'+ this.ID_MENU).length> 0) {
        $("#"+this.ID_MENU).mmenu({
          counters: true,
          extensions: ["effect-zoom-menu","effect-slide-listitems","effect-zoom-panels","multiline","theme-sentinel","pagedim-black"],
          searchfield: {
            add: true,
            noResults: "Sin resultados",
            placeholder: "Buscar",
            showTextItems: true
          },
          navbars: {
            content: ["searchfield"]
          }
        });
      } // if
		}, // init
		toggleMenu: function(){
			if ($("#"+ this.ID_PANEL_MENU).is(":visible")) {
				$("#"+ this.ID_PANEL_MENU).hide();
				$("#"+ this.ID_MENU_SENTINEL).show();
			} // if
			else {
				$("#"+ this.ID_PANEL_MENU).show();
				$("#"+ this.ID_MENU_SENTINEL).hide();
			} // else
		}, // toggleMenu
		open: function(){
			$("#"+ this.ID_MENU).data("mmenu").open();
		} // open
	});
})(window);
$(document).ready(function(){
	jsMenu= new Janal.Control.LeftMenu.Core();
	console.info("Janal.Control.LeftMenu.Core initialized");
});
