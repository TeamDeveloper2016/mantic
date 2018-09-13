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
		VK_MINUS    : 109,
		VK_REST     : 189,
		fields      : {
			'faltantesCantidad'  : {validaciones: 'requerido|mayor-igual({"cuanto": 0})', mascara: 'entero', formatos: 'precio', grupo: 'faltantes'},
			'faltantesIdArticulo': {validaciones: 'requerido', mascara: 'libre', grupo: 'faltantes'}
		},
		init: function() { // constructor
			$precios= this;
			this.hide();
	    $(document).on('keydown', '.janal-key-event', function(e) {
				var key= e.keyCode? e.keyCode: e.which;
				janal.console('Keydown: '+ key);
				switch(key) {
					case $precios.VK_UP:	
					case $precios.VK_DOWN:	
					case $precios.VK_TAB:
					case $precios.VK_ENTER:
						janal.console('jsPrecios.keydown: '+ $(this).attr('alt'));
						if($('#'+ $(this).attr('alt'))) {
						  $('#'+ $(this).attr('alt')).focus();
							if('agregarFaltantes'=== $(this).attr('id'))
                $(this).click();								
						} // if	
            return false;  					
					case $precios.VK_ESC:
            PF('dlgFaltantes').hide();
					  break;
   		    case $precios.VK_MINUS:
		      case $precios.VK_REST:
						if('codigosFaltantes_input'=== $(this).attr('id')) {
							$('.janal-clean-input').val('');
							return false;
						} // if	
					default: 
						if('faltantesCantidad'=== $(this).attr('id'))
						  return (key>=48 && key<=57) || (key>=96 && key<=105) || key===8 || key===37 || key===39 || key===110 || key===190;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-precios', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+ key);
				switch(key) {
					case $precios.VK_UP:	
					case $precios.VK_DOWN:	
					case $precios.VK_TAB:
						return $precios.next(true);
					  break;
					case $precios.VK_ESC:
            PF('dlgVerificador').hide();
					  break;
					case $precios.VK_ENTER:
						return $precios.lookup();
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
	    $(document).on('keydown', '.janal-key-articulo', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+ key);
				switch(key) {
					case $precios.VK_UP:	
					case $precios.VK_DOWN:	
					case $precios.VK_ENTER:
					case $precios.VK_TAB:
						return $precios.goto(true);
					  break;
					case $precios.VK_ESC:
            PF('dialogo').hide();
						break;
					case $precios.VK_PAGE_NEXT:
						if($('#buscados_paginator_top > a.ui-paginator-next')) {
						  $('#buscados_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($precios.goto(false), 1000);
						} // if
						else
							return false;
						break;
					case $precios.VK_PAGE_PREV:
						if($('#buscados_paginator_top > a.ui-paginator-prev')) {
	  					$('#buscados_paginator_top > a.ui-paginator-prev').click();
  						return setTimeout($precios.goto(false), 1000);
						} // if
						else
							return false;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-registros', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+ key);
				switch(key) {
					case $precios.VK_TAB:
					  $('#verificadorValue').focus();
						return false;
					  break;
					case $precios.VK_ESC:
            PF('dlgVerificador').hide();
						break;
					case $precios.VK_F7:
					case $precios.VK_ENTER:
						return $precios.show();
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
				} // swtich
			});	
	    $(document).on('keydown', '.janal-row-articulos', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+ key);
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
						// return $precios.hide();
						break;
					case $precios.VK_PAGE_NEXT:
						if($('#buscados_paginator_top > a.ui-paginator-next')) {
						  $('#buscados_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($precios.next(false), 1000);
					  } // if
						else
							return false;
						break;
					case $precios.VK_PAGE_PREV:
						if($('#buscados_paginator_top > a.ui-paginator-prev')) {
  						$('#buscados_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($precios.next(false), 1000);
					  } // if
						else
							return false;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-run', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+ key);
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
		lookup: function() {
			janal.console('jsPrecios.lookup');
			verificadorLookup();
			return false;
		},
		show: function()  {
 			janal.console('jsPrecios.show');
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
			PF('widgetVerificador').clearSelection();
			PF('widgetVerificador').writeSelections();
			PF('widgetVerificador').selectRow(0, true);	
			if(focus)
			  $('#verificadorTabla .ui-datatable-data').focus();
			return false;
		},
		clear: function() {
			janal.console('jsPrecios.clear');
			if(PF('widgetBuscados')) 
			  PF('widgetBuscados').clearSelection();
		},
		goto: function(focus) {
			janal.console('jsPrecios.goto');
			PF('widgetBuscados').clearSelection();
			PF('widgetBuscados').writeSelections();
			PF('widgetBuscados').selectRow(0, true);	
			if(focus)
			  $('#buscados .ui-datatable-data').focus();
			return false;
		},
		ask: function(text) {
			janal.console('jsPrecios.ask');
      return confirm('¿ Esta seguro que desea eliminar el articulo ?\n ['+ text+ ']');			
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
		},
		update: function() {
			janal.console('jsPrecios.update');
			janal.restore();
		}
	});
	
	console.info('Iktan.Control.Precios initialized');
})(window);	

$(document).ready(function() {
	jsPrecios= new Janal.Control.Precios.Core();
});			
