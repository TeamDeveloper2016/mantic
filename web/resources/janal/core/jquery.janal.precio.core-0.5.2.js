/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 07/06/2018
 *time 09:39:15 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */

(function(window) {
	var jsPrecios;	

	Janal.Control.Precios= {};
	
	Janal.Control.Precios.Core= Class.extend({
		VK_TAB      : 9, 
		VK_ESC      : 27, 
		VK_ENTER    : 13, 
		VK_UP       : 38,
		VK_DOWN     : 40,
		VK_PAGE_NEXT: 34,
		VK_PAGE_PREV: 33,
		VK_F7       : 118,
		VK_REST     : 189,
		VK_MINUS    : 109,
		VK_REST     : 189,
		fields      : {
			'faltantesCantidad'  : {validaciones: 'requerido|mayor-igual({"cuanto": 0})', mascara: 'entero', formatos: 'precio', grupo: 'faltantes'},
			'faltantesIdArticulo': {validaciones: 'requerido', mascara: 'libre', grupo: 'faltantes'}
		},
		temporal: '',
		init: function() { // constructor
			$precios= this;
			this.hide();
	    $(document).on('keydown', '.janal-key-faltante', function(e) {
				var key= e.keyCode? e.keyCode: e.which;
				janal.console('jsPrecios.keydown [janal-key-faltante]: '+ key);
				switch(key) {
					case $precios.VK_TAB:
					case $precios.VK_ENTER:
						janal.console('jsPrecios.keydown [janal-key-faltante]: '+ $(this).attr('alt'));
						if($('#'+ $(this).attr('alt'))) {
						  $('#'+ $(this).attr('alt')).focus();
							if('agregarFaltantes'=== $(this).attr('id'))
                $(this).click();								
						} // if	
            return false;  					
					case $precios.VK_ESC:
            PF('dlgFaltantes').hide();
					  break;
					default: 
						if('faltantesCantidad'=== $(this).attr('id'))
						  return (key>=48 && key<=57) || (key>=96 && key<=105) || key===8 || key===37 || key===39 || key===110 || key===190;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-verificador', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-key-verificador]: '+ key);
				switch(key) {
					case $precios.VK_UP:	
					case $precios.VK_DOWN:	
					case $precios.VK_TAB:
						if($precios.temporal!== $('#verificadorValue').val().trim())
						  verificadorKeyEnter();
						return $precios.next(true);
					  break;
					case $precios.VK_ESC:
            PF('dlgVerificador').hide();
					  break;
					case $precios.VK_ENTER:
      			janal.console('jsPrecios.lookup');
						$precios.temporal= $('#verificadorValue').val().trim();
						verificadorKeyEnter();
						return false;
						break;
					case $precios.VK_PAGE_NEXT:
						$('#verificadorTabla_paginator_top > a.ui-paginator-next').click();
						return setTimeout($precios.next(false), 1000);
						break;
					case $precios.VK_PAGE_PREV:
						$('#verificadorTabla_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($precios.next(false), 1000);
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-event-verificador', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-event-verificador]: '+ key);
				switch(key) {
					case $precios.VK_UP:	
					case $precios.VK_DOWN:	
						return $precios.hide();
					  break;
					case $precios.VK_ESC:
            PF('dlgVerificador').hide();
					  break;
					case $precios.VK_F7:
					case $precios.VK_ENTER:
						if($('#verificadorPrecio').is(":hidden"))
						  return $precios.show();
						else 
							return false;
						break;
					case $precios.VK_PAGE_NEXT:
						$('#verificadorTabla_paginator_top > a.ui-paginator-next').click();
						return setTimeout($precios.next(false), 1000);
						break;
					case $precios.VK_PAGE_PREV:
						$('#verificadorTabla_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($precios.next(false), 1000);
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-lista', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-key-lista]: '+ key);
				switch(key) {
					case $precios.VK_UP:	
					case $precios.VK_DOWN:	
					case $precios.VK_TAB:
						return $precios.forward(true);
					  break;
					case $precios.VK_ESC:
            PF('dlgListaPrecios').hide();
					  break;
					case $precios.VK_ENTER:
      			janal.console('jsPrecios.lookup');
						listaPreciosLookup();
						return false;
						break;
					case $precios.VK_PAGE_NEXT:
						$('#listaPreciosTabla_paginator_top > a.ui-paginator-next').click();
						return setTimeout($precios.forward(false), 1000);
						break;
					case $precios.VK_PAGE_PREV:
						$('#listaPreciosTabla_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($precios.forward(false), 1000);
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-catalogo', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-key-lista]: '+ key);
				switch(key) {
					case $precios.VK_UP:	
					case $precios.VK_DOWN:	
					case $precios.VK_TAB:
						return $precios.advancing(true);
					  break;
					case $precios.VK_ESC:
            PF('dlgCatalogoArticulos').hide();
					  break;
					case $precios.VK_ENTER:
      			janal.console('jsPrecios.lookup');
						catalogoArticulosLookup();
						return false;
						break;
					case $precios.VK_PAGE_NEXT:
						$('#catalogoArticulosTabla_paginator_top > a.ui-paginator-next').click();
						return setTimeout($precios.advancing(false), 1000);
						break;
					case $precios.VK_PAGE_PREV:
						$('#catalogoArticulosTabla_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($precios.advancing(false), 1000);
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-articulo', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-key-articulo]: '+ key);
				switch(key) {
					case $precios.VK_UP:	
					case $precios.VK_DOWN:	
					case $precios.VK_ENTER:
					case $precios.VK_TAB:
						return $precios.move(true);
					  break;
					case $precios.VK_ESC:
						if(PF('dialogo'))
              PF('dialogo').hide();
						break;
					case $precios.VK_PAGE_NEXT:
						if($('#buscados_paginator_top > a.ui-paginator-next')) {
						  $('#buscados_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($precios.move(false), 1000);
						} // if
						else
							return false;
						break;
					case $precios.VK_PAGE_PREV:
						if($('#buscados_paginator_top > a.ui-paginator-prev')) {
	  					$('#buscados_paginator_top > a.ui-paginator-prev').click();
  						return setTimeout($precios.move(false), 1000);
						} // if
						else
							return false;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-row-verificador', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-row-verificador]: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $precios.VK_ESC:
            PF('dlgVerificador').hide();
						break;
					case $precios.VK_F7:
					case $precios.VK_ENTER:
						if($('#verificadorPrecio').is(":hidden"))
						  return $precios.show();
						else 
							return false;
						break;
					case $precios.VK_UP:
					case $precios.VK_DOWN:
						return $precios.hide();
						break;
					case $precios.VK_PAGE_NEXT:
						if($('#verificadorTabla_paginator_top > a.ui-paginator-next')) {
						  $('#verificadorTabla_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($precios.next(false), 1000);
						} // if
						else
							return false;
						break;
					case $precios.VK_PAGE_PREV:
						if($('#verificadorTabla_paginator_top > a.ui-paginator-prev')) {
  						$('#verificadorTabla_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($precios.next(false), 1000);
						} // if
						else
							return false;
						break;
					default:
						if(key>= 32)
					    $('#verificadorValue').val($('#verificadorValue').val()+ String.fromCharCode(key));
					  $('#verificadorValue').focus();
						var event = jQuery.Event("keyup");
						event.keyCode= key;
						event.which  = key;
						$('#verificadorValue').trigger(event);
						return false;
					  break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-row-faltantes', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-row-faltantes]: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $precios.VK_TAB:
					  $('#codigosFaltantes').focus();
						return false;
					  break;
					case $precios.VK_ESC:
            PF('dlgFaltantes').hide();
						break;
   		    case $precios.VK_MINUS:
		      case $precios.VK_REST:
            //$('#faltantesTabla\\:0\\:faltanteEliminar').click();
						break;
					case $precios.VK_PAGE_NEXT:
						if($('#faltantesTabla_paginator_top > a.ui-paginator-next')) {
						  $('#faltantesTabla_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($precios.jump(false), 1000);
						} // if
						else
							return false;
						break;
					case $precios.VK_PAGE_PREV:
						if($('#faltantesTabla_paginator_top > a.ui-paginator-prev')) {
  						$('#faltantesTabla_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($precios.jump(false), 1000);
						} // if
						else
							return false;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-row-lista', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-row-lista]: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $precios.VK_TAB:
					case $precios.VK_ENTER:
					  $('#listaPreciosValue').focus();
						return false;
					  break;
					case $precios.VK_ESC:
            PF('dlgListaPrecios').hide();
						break;
					case $precios.VK_UP:
					case $precios.VK_DOWN:
						break;
					case $precios.VK_PAGE_NEXT:
						if($('#listaPreciosTabla_paginator_top > a.ui-paginator-next')) {
						  $('#listaPreciosTabla_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($precios.forward(false), 1000);
						} // if
						else
							return false;
						break;
					case $precios.VK_PAGE_PREV:
						if($('#listaPreciosTabla_paginator_top > a.ui-paginator-prev')) {
  						$('#listaPreciosTabla_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($precios.forward(false), 1000);
						} // if
						else
							return false;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-row-catalogo', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-row-catalogo]: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $precios.VK_TAB:
					case $precios.VK_ENTER:
					  $('#catalogoArticulosValue').focus();
						return false;
					  break;
					case $precios.VK_ESC:
            PF('dlgCatalogoArticulos').hide();
						break;
					case $precios.VK_UP:
					case $precios.VK_DOWN:
						break;
					case $precios.VK_PAGE_NEXT:
						if($('#catalogoArticulosTabla_paginator_top > a.ui-paginator-next')) {
						  $('#catalogoArticulosTabla_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($precios.advancing(false), 1000);
						} // if
						else
							return false;
						break;
					case $precios.VK_PAGE_PREV:
						if($('#catalogoArticulosTabla_paginator_top > a.ui-paginator-prev')) {
  						$('#catalogoArticulosTabla_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($precios.advancing(false), 1000);
						} // if
						else
							return false;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-row-articulos', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-row-articulos]: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $precios.VK_TAB:
					  $('#codigo').focus();
						return false;
					  break;
					case $precios.VK_ESC:
            PF('dialogo').hide();
						break;
					case $precios.VK_F7:
					case $precios.VK_ENTER:
				    return $precios.enter();
						break;
					case $precios.VK_UP:
					case $precios.VK_DOWN:
						break;
					case $precios.VK_PAGE_NEXT:
						if($('#buscados_paginator_top > a.ui-paginator-next')) {
						  $('#buscados_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($precios.move(false), 1000);
					  } // if
						else
							return false;
						break;
					case $precios.VK_PAGE_PREV:
						if($('#buscados_paginator_top > a.ui-paginator-prev')) {
  						$('#buscados_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($precios.move(false), 1000);
					  } // if
						else
							return false;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-run', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsPrecios.keydown [janal-key-run]: '+ key);
				switch(key) {
					case $precios.VK_ENTER:
					case $precios.VK_TAB:
						janal.mayusculas('kajoolOpcion');
						if($('#kajoolEjecutar'))
							$('#kajoolEjecutar').click();
						return false;
						break;
				} // swtich
			});	
		},
		show: function()  {
 			janal.console('jsPrecios.show');
      $('#source-gallery').attr('href', $('#icon-gallery').attr('src'));
      setTimeout("$('#verificadorPrecio').attr('style', 'display:');", 500);			
			return false;
		},
		enter: function()  {
 			janal.console('jsPrecios.enter');
      $('#localizado').click();		
			return false;
		},
		hide: function() {
			janal.console('jsPrecios.hide');
			$('#verificadorPrecio').attr('style', 'display:none');
			return false;
		},
		next: function(focus) {
			janal.console('jsPrecios.next');
			if(!PF('widgetVerificador').isEmpty()) {
				PF('widgetVerificador').clearSelection();
				PF('widgetVerificador').writeSelections();
				PF('widgetVerificador').selectRow(0, true);	
				if(focus)
					$('#verificadorTabla .ui-datatable-data').focus();
			} // if	
			return false;
		},
		forward: function(focus) {
			janal.console('jsPrecios.forward');
			if(!PF('widgetListaPrecios').isEmpty()) {
				PF('widgetListaPrecios').clearSelection();
				PF('widgetListaPrecios').writeSelections();
				PF('widgetListaPrecios').selectRow(0, true);	
				if(focus)
					$('#listaPreciosTabla .ui-datatable-data').focus();
			} // if	
			return false;
		},
		advancing: function(focus) {
			janal.console('jsPrecios.advancing');
			if(!PF('widgetCatalogoArticulos').isEmpty()) {
				PF('widgetCatalogoArticulos').clearSelection();
				PF('widgetCatalogoArticulos').writeSelections();
				PF('widgetCatalogoArticulos').selectRow(0, true);	
				if(focus)
					$('#catalogoArticulosTabla .ui-datatable-data').focus();
			} // if	
			return false;
		},
		clear: function() {
			janal.console('jsPrecios.clear');
			if(PF('widgetBuscados')) 
			  PF('widgetBuscados').clearSelection();
		},
		move: function(focus) {
			janal.console('jsPrecios.move');
			if(!PF('widgetBuscados').isEmpty()) {
				PF('widgetBuscados').clearSelection();
				PF('widgetBuscados').writeSelections();
				PF('widgetBuscados').selectRow(0, true);	
				if(focus)
					$('#buscados .ui-datatable-data').focus();
			} // if	
			return false;
		},
		ask: function(text) {
			janal.console('jsPrecios.ask');
      return confirm('Esta seguro que desea eliminar el articulo ?\n ['+ text+ ']');			
		},
		refresh: function() {
			janal.console('jsPrecios.refresh');
  		$('.janal-clean-input').val('');
			janal.change('faltantes', $precios.fields);
		},
		execute: function() {
			var ok= janal.partial('faltantes');
			janal.console('jsPrecios.execute: '+ ok);
			if(ok)
				faltantesVerificar();
			this.start();
		},
		update: function() {
			janal.console('jsPrecios.update');
			janal.restore();
		},
		jump: function(focus) {
			janal.console('jsPrecios.next');
			if(!PF('widgetFaltantes').isEmpty()) {
				PF('widgetFaltantes').clearSelection();
				PF('widgetFaltantes').writeSelections();
				PF('widgetFaltantes').selectRow(0, true);	
				if(focus)
					$('#faltantesTabla .ui-datatable-data').focus();
			} // if	
			return false;
		},
		start: function() {
			setTimeout("$('#codigosFaltantes_input').focus();", 1000);
		}
	});
	
	console.info('Iktan.Control.Precios initialized');
})(window);	

$(document).ready(function() {
	jsPrecios= new Janal.Control.Precios.Core();
});			
