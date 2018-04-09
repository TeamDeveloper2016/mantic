/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 11/09/2015
 *time 14:20:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */

(function(window){
	
	var jsSentinel;
	
	Janal.Control.SentinelMenu= {};
	
	Janal.Control.SentinelMenu.Core= Class.extend({
		ID_MENU: 'sm_leftmenu',
		ID_NO_RESULTS: 'noResults',
		CLASS_DISPLAY: 'DispNone',
		ID_INPUT		 : 'searchArea input',
		searchText:	'',
		init: function(){	}, // init
		preventKeypressSearch: function(code){
			switch(code){
				case 9:		//	tab
				case 13:	//	enter
				case 16:	//	shift
				case 20:  //  mayus
				case 17:	//	control
				case 18:	//	alt
				case 35:	//  end
				case 36:	//  begin
				case 37:	//	left
				case 38:	//	top
				case 39:	//	right
				case 40:	//	bottom
				case 45:	//	insert
					return true;
			} // switch
			return false;
		}, // preventKeypressSearch
		searchSentinel: function(e){
			if(!this.preventKeypressSearch(e.keyCode)){
				this.searchText= $("#"+ this.ID_INPUT).val().toUpperCase();
				this.searchMenu($("#"+ this.ID_MENU));
				if($("#"+ this.ID_MENU).hasClass(this.CLASS_DISPLAY))
					$("#"+ this.ID_NO_RESULTS).removeClass(this.CLASS_DISPLAY);
				else
					$("#"+ this.ID_NO_RESULTS).addClass(this.CLASS_DISPLAY);
			} // if
		}, // search
		//Busca conincidencias en todos los nodos hoja
		searchMenu: function(padre){
			$main= this;
			padre.children().each(function(){
				var submenu=$(this).children().get(1);		// Obtener elemento <ul con submenu
				if(typeof submenu=== "undefined"){
					var text= $(this).children().get(0);		// Obtener elemento <a con texto del menu
					if(text.text.toUpperCase().indexOf($main.searchText)!== -1)
						$(this).removeClass($main.CLASS_DISPLAY);
					else
						$(this).addClass($main.CLASS_DISPLAY);
				} // if undefined
				else{
					$main.searchMenu($(submenu));
					if(!$main.validarHijos($(this), "hoja"))
						$(this).removeClass($main.CLASS_DISPLAY);
					else
						$(this).addClass($main.CLASS_DISPLAY);
				} // else
				if($main.validarHijos(padre, "padre"))
					padre.removeClass($main.CLASS_DISPLAY);
				else
					padre.addClass($main.CLASS_DISPLAY);
			});
		}, // searchMenu
		//Valida si aun existen nodos hijo visibles segun el tipo de menu
		validarHijos: function(padre, tipo){
			var regresar= false;
			if(tipo=== "hoja"){
				if($($(padre).children().get(1)).hasClass($main.CLASS_DISPLAY))
					regresar= true;
			} // if
			else{
				$(padre).children().each(function(){
					if(!$(this).hasClass($main.CLASS_DISPLAY))
						regresar= true;
				});
			} // else
			return regresar;
		} // validarHijos	
	});
	
})(window);

$(document).ready(function(){
	jsSentinel= new Janal.Control.SentinelMenu.Core();
	$("#searchArea input").on("keyup", function(e){
		jsSentinel.searchSentinel(e);
	});
	console.info("Janal.Control.SentinelMenu.Core initialized")
});
