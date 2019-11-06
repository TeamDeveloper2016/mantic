/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 21/08/2018
 *time 11:15:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
	var jsArticulos;
	
	Janal.Control.Articulos= {};
	
	Janal.Control.Articulos.Core= Class.extend({
		joker       : 'contenedorGrupos\\:tabla\\:', // Attributes 
		codes       : '\\:codigos_input', 
		panels      : 'codigos_panel', 
		itemtips    : 'codigos_itemtip', 
		discounts   : '\\:descuentos',
		additionals : '\\:extras',
		amounts     : '\\:cantidades',
		requested   : '\\:solicitados',
		prices      : '\\:precios',
		keys        : '\\:keys',
		locks       : '\\:locks',
		values      : '\\:values',
		sats        : '\\:sat',
		descriptions: '\\:nombres',
		selector    : '.key-down-event',
		focus       : '.key-focus-event',
		averages    : '.key-press-enter',
		filter      : '.key-filter-event',
		ctrlPlus    : false,
		ctrlDiv     : false,
		current     : '',
		typingTimer : null,
		doneInterval: 10000,
		continue    : false,
		leavePage   : true,
		VK_TAB      : 9, 
		VK_ENTER    : 13, 
		VK_ESC      : 27,
		VK_ASTERISK : 106,
		VK_EQUALS   : 48,
		VK_MINUS    : 109,
		VK_COMA     : 191,
		VK_OPEN     : 122,
		VK_CLOSE    : 123,
		VK_PLUS     : 107,
		VK_DIV      : 111,
		VK_POINT    : 110,
		VK_UP       : 38,
		VK_DOWN     : 40,
		VK_REST     : 189,
		VK_PIPE     : 220,
		VK_BRACKET  : 222,
		VK_CTRL     : 17,
		VK_MAYOR    : 226,
		VK_F7       : 118,
		VK_F8       : 119,
		VK_F10       : 121,
		VK_SAT	    : 188,
	  change      : [13, 27, 106, 107, 110, 111, 121, 188, 189, 191, 220, 222, 226],
	  control     : [9, 13, 17, 27, 38, 40, 220, 118, 121, 122],
		cursor: {
			top: 1, // el top debera ser elementos que van de 0 a n-1
			index: 0,
			tmp: 0
		},
		temporal: '',
		init: function(top, content, plus, div) { // Constructor
			$articulos= this;
			this.cursor.top= top-1;
			if(typeof(content)!== 'undefined')
			  this.joker= content;
			if(typeof(plus)!== 'undefined')
			  this.ctrlPlus= typeof(plus)=== 'boolean'? plus: false;
			if(typeof(div)!== 'undefined')
			  this.ctrlDiv= typeof(div)=== 'boolean'? div: false;
			this.events();
			this.lastCursorAt();
		}, // init
		events: function() {
			$(document).on('keydown', '.event-keydown-enter', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keydown [event-keydown-enter]: '+  key);
				switch(key) {
					case $articulos.VK_TAB: 
					case $articulos.VK_ENTER: 
					case $articulos.VK_DOWN: 
						var name= janal.parser(this);
						if(name) {
							var id= $(this).attr('id');
							if(id.startsWith('tipoDescuento')) {
								var index= parseInt(PF(id).getJQ().find(':checked').val());
								if(index===0) 
									name= 'descuentoGlobal_input';
								else
									if(index===2 || index=== 3 || index=== 4) 
										name= 'descuentoAceptar';
							}	// if
							$('#'+ name).focus();
						} // if	
						janal.console('focus: '+ $(this).attr('id')+ ' target: '+ name);
						if(key=== $articulos.VK_ENTER && $(this).attr('id')=== 'descuentoAceptar')
							$('#descuentoAceptar').click();
						return false;
						break;						
					case $articulos.VK_UP:  
						return false;
						break;
				} // switch
			});
      $(document).on('focus', this.filter, function() {
				janal.console('jsVentas.focus: '+ $(this).attr('id')+ ' value:['+ $(this).val().trim()+ ']');
				$articulos.current= $(this).val().trim();
				janal.lastNameFocus= this;
			});  
      $(document).on('keyup', this.filter, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keyup: '+ $(this).attr('id')+ ' current: ['+ $articulos.current+ '] value: ['+ $(this).val().trim()+ ']');
				if($articulos.current!== $(this).val().trim()) {
					$articulos.current= $(this).val().trim();
					filterRows();
				} // if	
			});  
			$(window).bind('beforeunload', function() { 
				unlockVenta();
				if(typeof(jsArticulos)=== 'undefined' || jsArticulos.leavePage)
					return ;
				else
				  return 'Es probable que los cambios no se hayan guardado\n\u00BF Aun asi deseas salir de esta opción ?';
			});			
      $(document).on('keydown', '.key-buscados-event', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown [key-buscados-event]: '+ key);
				switch(key) {
					case $articulos.VK_UP:	
					case $articulos.VK_DOWN:	
					case $articulos.VK_TAB:
						if($articulos.temporal!== $('#codigo').val().trim()) {
        			janal.console('jsVentas.lookup '+ + $(this).val());
  						lookup($(this).val().replace(janal.cleanString, '').trim());
						} // if
						return $articulos.jump(true);
					  break;
					case $articulos.VK_ESC:
            PF('dialogo').hide();
					  break;
					case $articulos.VK_ENTER:
      			janal.console('jsVentas.lookup '+ + $(this).val());
						$articulos.temporal= $('#codigo').val().trim();
						lookup($(this).val().replace(janal.cleanString, '').trim());
						return false;
						break;
					case $articulos.VK_PAGE_NEXT:
						$('#buscados_paginator_top > a.ui-paginator-next').click();
						return setTimeout($articulos.jump(false), 1000);
						break;
					case $articulos.VK_PAGE_PREV:
						$('#buscados_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($articulos.jump(false), 1000);
						break;
				} // swtich
			});  
	    $(document).on('keydown', '.janal-buscados-articulos', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown [janal-buscados-articulos]: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_ESC:
            PF('dialogo').hide();
						break;
					case $articulos.VK_F7:
					case $articulos.VK_ENTER:
						PF('dialogo').hide(); 
						$articulos.close();
						return false;
						break;
					case $articulos.VK_UP:
					case $articulos.VK_DOWN:
						break;
					case $articulos.VK_PAGE_NEXT:
						if($('#buscados_paginator_top > a.ui-paginator-next')) {
						  $('#buscados_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($articulos.jump(false), 1000);
						} // if
						else
							return false;
						break;
					case $articulos.VK_PAGE_PREV:
						if($('#buscados_paginator_top > a.ui-paginator-prev')) {
  						$('#buscados_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($articulos.jump(false), 1000);
						} // if
						else
							return false;
						break;
					default:
						if(key>= 32)
					    $('#codigo').val($('#codigo').val()+ String.fromCharCode(key));
					  $('#codigo').focus();
						var event = jQuery.Event("keyup");
						event.keyCode= key;
						event.which  = key;
						$('#codigo').trigger(event);
						return false;
					  break;
				} // swtich
			});	
      $(document).on('focus', '.key-focus-event,.key-move-event', function() {
				$articulos.current= $(this).val();
				$articulos.index($(this).attr('id'));
				janal.lastNameFocus= this;
			});  
      $(document).on('focus', this.selector, function() {
				$articulos.index($(this).attr('id'));
				janal.lastNameFocus= this;
			});  
      $(document).on('keydown', this.averages, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown [key-press-enter]: '+  key);
				if(($articulos.change.indexOf(key)>= 0)) 
					$articulos.leavePage= false;
				switch(key) {
					case $articulos.VK_ENTER:
						$(this).blur();
						return false;
						break;
				} // switch
			});	
      $(document).on('keydown', '.janal-key-search', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown [janal-key-search]: '+  key);
				if(($articulos.change.indexOf(key)>= 0)) 
					$articulos.leavePage= false;
				switch(key) {
					case $articulos.VK_ENTER:
						$('#contenedorGrupos\\:buscarTicket').click();
						return false;
						break;
				} // switch
			});	
			$(document).on('keydown', '.key-event-sat', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown [key-event-sat]: '+  key);
				switch(key) {
					case $articulos.VK_UP:
						return $articulos.moveup('\\'+ $(this).attr('id').substring($(this).attr('id').lastIndexOf(':')));
						break;
					case $articulos.VK_DOWN:
					case $articulos.VK_ENTER:
						return $articulos.movedown('\\'+$(this).attr('id').substring($(this).attr('id').lastIndexOf(':')));
						break;
				} // switch
			});	
      $(document).on('keydown', '.key-focus-event', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+  key);
				if(($articulos.change.indexOf(key)>= 0))
					$articulos.leavePage= false;
				switch(key) {
					case $articulos.VK_ENTER:
						return $articulos.calculate($(this));
						break;
					case $articulos.VK_MINUS:
						if(!PF('wAceptarCompra'))
							return $articulos.reset($(this));
						else
							return true;
						break;
					case $articulos.VK_REST:
						return $articulos.show($(this));
						break;
				} // switch
			});	
      $(document).on('keydown', this.selector, function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+  key);
				if(($articulos.change.indexOf(key)>= 0)) {
					$articulos.leavePage= false;
				  setTimeout("$('div[id$='+ jsArticulos.panels+ ']').hide();$('div[id$='+ jsArticulos.itemtips+ ']').hide();", 500);
				} // if	 
				var calculate= $articulos.get().trim().startsWith('=');
				switch(key) {
					case $articulos.VK_ENTER:
						return $articulos.find();
						break;
					case $articulos.VK_UP:
   				  if($('ul.ui-autocomplete-items:visible').length<=0)
						  return $articulos.up(true);
						break;
					case $articulos.VK_DOWN:
   				  if($('ul.ui-autocomplete-items:visible').length<= 0)
  						return $articulos.down(true);
						break;
					case $articulos.VK_ASTERISK:
						if(calculate)
						  return true;
						else
  						return $articulos.asterisk();
						break;
					case $articulos.VK_DIV:
						if(calculate)
						  return true;
						else
              return $articulos.div();
						break;
					case $articulos.VK_PLUS:
						if(calculate)
						  return true;
						else
	    				return $articulos.plus();
						break;
					case $articulos.VK_COMA:
						if(calculate)
						  return true;
						else
  						return $articulos.point();
						break;
					case $articulos.VK_SAT:
						janal.console('jsVentas.sat: ');
						var ok= true;
						var value= $(this).val().trim();
						if(janal.isInteger(value) && value.length=== 8) {
							$($articulos.sat()).val(value);
							$articulos.set('');
							$articulos.refresh();
							ok= false;
						} // if	
						return ok;
						break;
					case $articulos.VK_REST:
						var txt  = $(this).val().trim().length<= 0;
						if(txt && $('ul.ui-autocomplete-items:visible').length<= 0 && $articulos.remove())
						  return $articulos.clean();
						break;
					case $articulos.VK_PIPE:
						return $articulos.search();
						break;						
					case $articulos.VK_MINUS:
						if(calculate)
						  return true;
						else {
							var value= $(this).val().trim();
							if(value.length=== 0) {
								if(parseInt($('#articulos').val())===0) {
									if(PF('dlgCloseTicket')) {
										janal.bloquear();
										userUpdate();
									} // if
								} // if
								else {
									var ok= janal.partial('articulo');
									if(ok) {
										$articulos.leavePage= true;
										var txt= $(this).val().trim().length<= 0;
										if(txt && $('ul.ui-autocomplete-items:visible').length<= 0 && !PF('wAceptarCompra')) {
											$('#aceptar').click();
											return false;
										} // if									
									} // if
								} // else
							} // if
						} // else
						break;
					case $articulos.VK_MAYOR:
						return $articulos.show(this);
						break;
					case $articulos.VK_F7:
						return $articulos.detail();
						break;
					case $articulos.VK_F8:
						return $articulos.locationArt();
						break;
					case $articulos.VK_F10:
						if(calculate)
						  $articulos.set(eval($articulos.get().trim().substring(1)));
						return false;
						break;
					case $articulos.VK_EQUALS:
						var value= $articulos.get().trim();
					  if(calculate && value.length> 2 && e.key=== '=') {
						  $articulos.set(eval(value.substring(1)));
							return false;
						} // if	
					  else
						  return true;
						break;
					default:
						break;
				} // switch
      });
      $(document).on('keydown', '.key-down-clientes', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keyup: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_MAYOR:
						return $articulos.display($(this));
						break;
				} // switch
			});  
      $(document).on('keyup', '.key-up-clientes', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keyup: '+ $(this).attr('id')+ ' key: '+ key);
				clearTimeout($articulos.typingTimer);
				if ($(this).val() && $(this).val().trim().length> 0 && $articulos.control.indexOf(key)< 0) 
					$articulos.typingTimer= setTimeout($articulos.clientes($(this)), $articulos.doneInterval);
				return false;
			});  
			$(document).on('keydown', '.janal-key-tickets-abiertos', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+ key);
				switch(key) {
					case $articulos.VK_UP:	
					case $articulos.VK_DOWN:	
					case $articulos.VK_TAB:
						return $articulos.nextOpenTicket(true);
					  break;
					case $articulos.VK_ESC:
            PF('dlgOpenTickets').hide();
					  break;
					case $articulos.VK_ENTER:
      			janal.console('jsVentas.lookup');
						lookup();
						return false;
						break;
					case $articulos.VK_PAGE_NEXT:
						$('#tablaTicketsAbiertos_paginator_top > a.ui-paginator-next').click();
						return setTimeout($articulos.next(false), 1000);
						break;
					case $articulos.VK_PAGE_PREV:
						$('#tablaTicketsAbiertos_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($articulos.next(false), 1000);
						break;
				} // swtich
			});	
			$(document).on('keydown', '.janal-row-tickets-abiertos', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_TAB:
					  $('#busquedaTicketAbierto').focus();
						return false;
					  break;
					case $articulos.VK_ESC:
            PF('dlgOpenTickets').hide();
						break;
					case $articulos.VK_F7:
					case $articulos.VK_ENTER:
			      $('#aceptarTicketAbierto').click();		
				    return false;
						break;
					case $articulos.VK_UP:
					case $articulos.VK_DOWN:
						break;
					case $articulos.VK_PAGE_NEXT:
						if($('#tablaTicketsAbiertos_paginator_top > a.ui-paginator-next')) {
						  $('#tablaTicketsAbiertos_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($articulos.goon(false), 1000);
					  } // if
						else
							return false;
						break;
					case $articulos.VK_PAGE_PREV:
						if($('#tablaTicketsAbiertos_paginator_top > a.ui-paginator-prev')) {
  						$('#tablaTicketsAbiertos_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($articulos.goon(false), 1000);
					  } // if
						else
							return false;
						break;
				} // swtich
			});	
			$(document).on('keydown', '.janal-key-cotizaciones', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+ key);
				switch(key) {
					case $articulos.VK_UP:	
					case $articulos.VK_DOWN:	
					case $articulos.VK_TAB:
						return $articulos.nextCotizacion(true);
					  break;
					case $articulos.VK_ESC:
            PF('dlgCotizaciones').hide();
					  break;
					case $articulos.VK_ENTER:
      			janal.console('jsVentas.lookup');
						lookup();
						return false;
						break;
					case $articulos.VK_PAGE_NEXT:
						$('#tablaCotizaciones_paginator_top > a.ui-paginator-next').click();
						return setTimeout($articulos.next(false), 1000);
						break;
					case $articulos.VK_PAGE_PREV:
						$('#tablaCotizaciones_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($articulos.next(false), 1000);
						break;
				} // swtich
			});	
			$(document).on('keydown', '.janal-row-cotizaciones', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_TAB:
					  $('#busquedaCotizacion').focus();
						return false;
					  break;
					case $articulos.VK_ESC:
            PF('widgetTablaCotizaciones').hide();
						break;
					case $articulos.VK_F7:
					case $articulos.VK_ENTER:
			      $('#aceptarCotizacion').click();		
				    return false;
						break;
					case $articulos.VK_UP:
					case $articulos.VK_DOWN:
						break;
					case $articulos.VK_PAGE_NEXT:
						if($('#tablaCotizaciones_paginator_top > a.ui-paginator-next')) {
						  $('#tablaCotizaciones_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($articulos.goon(false), 1000);
					  } // if
						else
							return false;
						break;
					case $articulos.VK_PAGE_PREV:
						if($('#tablaCotizaciones_paginator_top > a.ui-paginator-prev')) {
  						$('#tablaCotizaciones_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($articulos.goon(false), 1000);
					  } // if
						else
							return false;
						break;
				} // swtich
			});	
			$(document).on('keydown', '.janal-key-apartados', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+ key);
				switch(key) {
					case $articulos.VK_UP:	
					case $articulos.VK_DOWN:	
					case $articulos.VK_TAB:
						return $articulos.nextApartados(true);
					  break;
					case $articulos.VK_ESC:
            PF('dlgApartados').hide();
					  break;
					case $articulos.VK_ENTER:
      			janal.console('jsVentas.lookup');
						lookup();
						return false;
						break;
					case $articulos.VK_PAGE_NEXT:
						$('#tablaApartados_paginator_top > a.ui-paginator-next').click();
						return setTimeout($articulos.next(false), 1000);
						break;
					case $articulos.VK_PAGE_PREV:
						$('#tablaApartados_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($articulos.next(false), 1000);
						break;
				} // swtich
			});	
			$(document).on('keydown', '.janal-row-apartados', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_TAB:
					  $('#busquedaApartados').focus();
						return false;
					  break;
					case $articulos.VK_ESC:
            PF('widgetTablaApartados').hide();
						break;
					case $articulos.VK_F7:
					case $articulos.VK_ENTER:
			      $('#aceptarApartados').click();		
				    return false;
						break;
					case $articulos.VK_UP:
					case $articulos.VK_DOWN:
						break;
					case $articulos.VK_PAGE_NEXT:
						if($('#tablaApartados_paginator_top > a.ui-paginator-next')) {
						  $('#tablaApartados_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($articulos.goon(false), 1000);
					  } // if
						else
							return false;
						break;
					case $articulos.VK_PAGE_PREV:
						if($('#tablaApartados_paginator_top > a.ui-paginator-prev')) {
  						$('#tablaApartados_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($articulos.goon(false), 1000);
					  } // if
						else
							return false;
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-clientes', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+ key);
				switch(key) {
					case $articulos.VK_UP:	
					case $articulos.VK_DOWN:	
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
						return $articulos.goon(true);
					  break;
					case $articulos.VK_ESC:
						if(PF('dialogoClientes'))
              PF('dialogoClientes').hide();
						break;
					case $articulos.VK_PAGE_NEXT:
						if($('#compradores_paginator_top > a.ui-paginator-next')) {
						  $('#compradores_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($articulos.goon(false), 1000);
						} // if
						else
							return false;
						break;
					case $articulos.VK_PAGE_PREV:
						if($('#compradores_paginator_top > a.ui-paginator-prev')) {
	  					$('#compradores_paginator_top > a.ui-paginator-prev').click();
  						return setTimeout($articulos.goon(false), 1000);
						} // if
						else
							return false;
						break;
				} // swtich
			});
	    $(document).on('keydown', '.janal-row-clientes', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.keydown: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_TAB:
					  $('#rfcClientes').focus();
						return false;
					  break;
					case $articulos.VK_ESC:
            PF('dialogoClientes').hide();
						break;
					case $articulos.VK_F7:
					case $articulos.VK_ENTER:
			      $('#comprador').click();		
				    return false;
						break;
					case $articulos.VK_UP:
					case $articulos.VK_DOWN:
						break;
					case $articulos.VK_PAGE_NEXT:
						if($('#compradores_paginator_top > a.ui-paginator-next')) {
						  $('#compradores_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($articulos.goon(false), 1000);
					  } // if
						else
							return false;
						break;
					case $articulos.VK_PAGE_PREV:
						if($('#compradores_paginator_top > a.ui-paginator-prev')) {
  						$('#compradores_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($articulos.goon(false), 1000);
					  } // if
						else
							return false;
						break;
				} // swtich
			});	
			$(document).on('keydown', '.key-caja-dialogo-finalizar', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.cajaDialogoFinalizar');
				switch(key) {					
					case $articulos.VK_ENTER:
						$('#aceptarFinalizar').click();
						break;
				} // switch
			});
			$(document).on('keydown', '.key-caja-efectivo', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.cajaEfectivo');
				var valor= 0;
				switch(key) {					
					case $articulos.VK_MINUS:
						$("#contenedorGrupos\\:debito_input").focus();
						break;
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
					case $articulos.VK_DOWN:
						$("#contenedorGrupos\\:debito_input").focus();
						valor= parseFloat($("#contenedorGrupos\\:debito_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:debito_input").val('');
						valor= parseFloat($("#contenedorGrupos\\:efectivo_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:efectivo_input").val('0.00');
						break;
					case $articulos.VK_UP:
						$("#contenedorGrupos\\:cheque_input").focus();
						valor= parseFloat($("#contenedorGrupos\\:cheque_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:cheque_input").val('');
						valor= parseFloat($("#contenedorGrupos\\:efectivo_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:efectivo_input").val('0.00');
						break;
				} // switch				
			});
			$(document).on('keydown', '.key-caja-debito', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.cajaDebito');
				var valor= 0;
				switch(key) {					
					case $articulos.VK_MINUS:
						$("#contenedorGrupos\\:credito_input").focus();
						break;
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
					case $articulos.VK_DOWN:						
						valor= parseFloat($("#contenedorGrupos\\:debito_input").val()); 
						if(isNaN(valor) || valor=== 0){
							$("#contenedorGrupos\\:debito_input").val('0.00');
							$("#contenedorGrupos\\:credito_input").focus();
							valor= parseFloat($("#contenedorGrupos\\:credito_input").val()); 
							if(isNaN(valor) || valor=== 0){								
								$("#contenedorGrupos\\:credito_input").val('');
							} // if
						} // if						
						else{							
							$('#contenedorGrupos\\:bancoDebito_label').click();
							$('#contenedorGrupos\\:bancoDebito_label').focus();
						} // else						
						break;
					case $articulos.VK_UP:
						$("#contenedorGrupos\\:efectivo_input").focus();
						valor= parseFloat($("#contenedorGrupos\\:efectivo_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:efectivo_input").val('');
						valor= parseFloat($("#contenedorGrupos\\:debito_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:debito_input").val('0.00');
						break;
				} // switch				
			});
			$(document).on('keydown', '.key-caja-credito', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.cajaCredito');
				var valor= 0;
				switch(key) {					
					case $articulos.VK_MINUS:
						$("#contenedorGrupos\\:transferencia_input").focus();
						break;
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
					case $articulos.VK_DOWN:						
						valor= parseFloat($("#contenedorGrupos\\:credito_input").val()); 
						if(isNaN(valor) || valor=== 0){
							$("#contenedorGrupos\\:credito_input").val('0.00');
							$("#contenedorGrupos\\:transferencia_input").focus();
							valor= parseFloat($("#contenedorGrupos\\:transferencia_input").val()); 
							if(isNaN(valor) || valor=== 0)							
								$("#contenedorGrupos\\:transferencia_input").val('');
						} // if												
						else{
							$('#contenedorGrupos\\:bancoCredito_label').click();
							$('#contenedorGrupos\\:bancoCredito_label').focus();
						} // else						
						break;
					case $articulos.VK_UP:
						$("#contenedorGrupos\\:debito_input").focus();
						valor= parseFloat($("#contenedorGrupos\\:debito_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:debito_input").val('');
						valor= parseFloat($("#contenedorGrupos\\:credito_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:credito_input").val('0.00');
						break;
				} // switch				
			});
			$(document).on('keydown', '.key-caja-transferencia', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.cajaTransferencia');
				var valor= 0;
				switch(key) {					
					case $articulos.VK_MINUS:
						$("#contenedorGrupos\\:cheque_input").focus();
						break;
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
					case $articulos.VK_DOWN:						
						valor= parseFloat($("#contenedorGrupos\\:transferencia_input").val()); 
						if(isNaN(valor) || valor=== 0){
							$("#contenedorGrupos\\:transferencia_input").val('0.00');
							$("#contenedorGrupos\\:cheque_input").focus();
							valor= parseFloat($("#contenedorGrupos\\:cheque_input").val()); 
							if(isNaN(valor) || valor=== 0)
								$("#contenedorGrupos\\:cheque_input").val('');						
						} // if						
						else{
							$('#contenedorGrupos\\:bancoTransferencia_label').click();
							$('#contenedorGrupos\\:bancoTransferencia_label').focus();
						} // else						
						break;
					case $articulos.VK_UP:
						$("#contenedorGrupos\\:credito_input").focus();
						valor= parseFloat($("#contenedorGrupos\\:credito_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:credito_input").val('');
						valor= parseFloat($("#contenedorGrupos\\:transferencia_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:transferencia_input").val('0.00');
						break;
				} // switch				
			});
			$(document).on('keydown', '.key-caja-cheque', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsVentas.cajaCheque');
				var valor= 0;
				switch(key) {					
					case $articulos.VK_MINUS:
						$("#contenedorGrupos\\:efectivo_input").focus();
						break;
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
					case $articulos.VK_DOWN:						
						valor= parseFloat($("#contenedorGrupos\\:cheque_input").val()); 
						if(isNaN(valor) || valor=== 0){
							$("#contenedorGrupos\\:cheque_input").val('0.00');
							$("#contenedorGrupos\\:efectivo_input").focus();
							valor= parseFloat($("#contenedorGrupos\\:efectivo_input").val()); 
							if(isNaN(valor) || valor=== 0)							
								$("#contenedorGrupos\\:efectivo_input").val('');						
						} // if						
						else{
							$('#contenedorGrupos\\:bancoCheque_label').click();
							$('#contenedorGrupos\\:bancoCheque_label').focus();
						} // else						
						//$("#aceptar").click();													
						break;
					case $articulos.VK_UP:
						$("#contenedorGrupos\\:transferencia_input").focus();
						valor= parseFloat($("#contenedorGrupos\\:transferencia_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:transferencia_input").val('');
						valor= parseFloat($("#contenedorGrupos\\:cheque_input").val()); 
						if(isNaN(valor) || valor=== 0)
							$("#contenedorGrupos\\:cheque_input").val('0.00');
						break;
				} // switch				
			});
			$(document).on('keydown', '.janal-select-type', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				switch(key) {					
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
					case $articulos.VK_DOWN:
						$articulos.jumpNextTypeReference(e);
						break;
					case $articulos.VK_UP:
						$articulos.jumpPreviousTypeReference(e);
						break;
				} // switch																
			});
			$(document).on('blur, keydown', '.janal-select-referencia', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				switch(key) {					
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
						$articulos.jumpNextBankReference(e);
						break;
				} // switch											
			});
			setTimeout('$articulos.goto()', 1000);
		},
		jumpNextBankReference: function(e) {
			var name= $(e.currentTarget).attr('id');
			var reference= name.substring(name.indexOf('banco') + 5, name.length);
			$('#contenedorGrupos\\:referencia' + reference).focus();				
		},	// jumpNextBankReference
		jumpNextTypeReference: function(e) {
			var name= $(e.currentTarget).attr('id');
			var reference= name.substring(name.indexOf('referencia') + 10, name.length).toLowerCase();
			var jump= '';
			switch(reference){
				case 'debito':
					jump= 'credito';
					break;
				case 'credito':
					jump= 'transferencia';
					break;
				case 'transferencia':
					jump= 'cheque';
					break;
				case 'cheque':
					jump= 'efectivo';
					break;
			} // switch
			$("#contenedorGrupos\\:" + jump + "_input").focus();
			var valor= parseFloat($("#contenedorGrupos\\:" + jump + "_input").val()); 
			if(isNaN(valor) || valor=== 0)
				$("#contenedorGrupos\\:" + jump + "_input").val('');				
		},	// jumpNextTypeReference
		jumpPreviousTypeReference: function(e) {
			var name= $(e.currentTarget).attr('id');
			var reference= name.substring(name.indexOf('referencia') + 10, name.length).toLowerCase();
			var jump= '';
			switch(reference){
				case 'debito':
					jump= 'efectivo';
					break;
				case 'credito':
					jump= 'debito';
					break;
				case 'transferencia':
					jump= 'credito';						
					break;
				case 'cheque':
					jump= 'transferencia';						
					break;
			} // switch
			$("#contenedorGrupos\\:" + jump + "_input").focus();
			var valor= parseFloat($("#contenedorGrupos\\:" + jump + "_input").val()); 
			if(isNaN(valor) || valor=== 0)
				$("#contenedorGrupos\\:" + jump + "_input").val('');				
		},	// jumpPreviousTypeReference
		nextOpenTicket: function(focus) {
			janal.console('jsVentas.nextOpenTicket');
			if(!PF('widgetTablaTicketsAbiertos').isEmpty()) {
				PF('widgetTablaTicketsAbiertos').clearSelection();
				PF('widgetTablaTicketsAbiertos').writeSelections();
				//PF('widgetTablaTicketsAbiertos').selectRow(0, true);	
				if(focus)
					$('#tablaTicketsAbiertos .ui-datatable-data').focus();
			} // if	
			return false;
		},
		nextCotizacion: function(focus) {
			janal.console('jsVentas.nextCotizacion');
			if(!PF('widgetTablaCotizaciones').isEmpty()) {
				PF('widgetTablaCotizaciones').clearSelection();
				PF('widgetTablaCotizaciones').writeSelections();
				//PF('widgetTablaTicketsAbiertos').selectRow(0, true);	
				if(focus)
					$('#tablaCotizaciones .ui-datatable-data').focus();
			} // if	
			return false;
		},
		nextApartados: function(focus) {
			janal.console('jsVentas.nextApartados');
			if(!PF('widgetTablaApartados').isEmpty()) {
				PF('widgetTablaApartados').clearSelection();
				PF('widgetTablaApartados').writeSelections();				
				if(focus)
					$('#tablaApartados .ui-datatable-data').focus();
			} // if	
			return false;
		},
		moveup: function(which) {
			janal.console('jsVentas.moveup: '+ this.cursor.index+ ' =>'+ which);
			this.up(false);
			var id= '#'+ this.joker+ this.cursor.index+ which;
			if($(id))
				$(id).focus();
			return false;
		},
		movedown: function(which) {
			janal.console('jsVentas.movedown: '+ this.cursor.index+ ' =>'+ which);
			this.down(false);
			var id= '#'+ this.joker+ this.cursor.index+ which;
			if($(id))
				$(id).focus();
			return false;
		},
		index: function(id) {
			janal.console('jsVentas.index: '+ this.cursor.index+ ' =>'+ id);
			id= id.replace(/:/gi, '\\:');
			var start= id.indexOf(this.joker)>= 0? this.joker.length: -1;
			if(start> 0)
				this.cursor.index= parseInt(id.substring(start, id.lastIndexOf('\\:')), 10);
		},
		move: function() {
			var id= this.name();
			if($(id))
				$(id).focus();
			$('div[id$='+ this.panels+ ']').hide();
			$('div[id$='+ this.itemtips+ ']').hide();
		},
		name: function() {
			return '#'+ this.joker+ this.cursor.index+ this.codes;
		},
		amount: function() {
			return '#'+ this.joker+ this.cursor.index+ this.amounts;
		},
		description: function() {
			return '#'+ this.joker+ this.cursor.index+ this.descriptions;
		},
		sat: function() {
			return '#'+ this.joker+ this.cursor.index+ this.sats;
		},
		request: function() {
			return '#'+ this.joker+ this.cursor.index+ this.requested;
		},
		discount: function() {
			return '#'+ this.joker+ this.cursor.index+ this.discounts;
		},
		additional: function() {
			return '#'+ this.joker+ this.cursor.index+ this.additionals;
		},
		price: function() {
			return '#'+ this.joker+ this.cursor.index+ this.prices;
		},
		key: function() {
			return '#'+ this.joker+ this.cursor.index+ this.keys;
		},
		lock: function() {
			return '#'+ this.joker+ this.cursor.index+ this.locks;
		},
		value: function() {
			return '#'+ this.joker+ this.cursor.index+ this.values;
		},
		set: function(value) {
			janal.console('jsVentas.set: '+ this.name()+ ' ->'+ $(this.name()).val());
		  if($(this.name()))
				$(this.name()).val(value);	
		},
		get: function() {
			janal.console('jsVentas.get: '+ this.name()+ ' ->'+ $(this.name()).val());
			return $(this.name())? $(this.name()).val(): '';
		},
		up: function(jump) {
			janal.console("jsVentas.up: "+ this.cursor.index);
			if(this.cursor.index> 0)
				this.cursor.index--;
			else
				this.cursor.index= this.cursor.top;
			if(jump)
			  this.move();
			return false;
		},
		down: function(jump) {
			janal.console("jsVentas.down: "+ this.cursor.index);
			if(this.cursor.index< this.cursor.top)
				this.cursor.index++;
			else
				this.cursor.index= 0;
			if(jump)
  			this.move();
			return false;
		},
		valid: function() {
			janal.console('jsVentas.valid: ');
			return $(this.key()) && parseInt($(this.key()).val(), 10)> 0;
		}, 
		remove: function() {
			janal.console('jsVentas.remove: ');
			return this.valid() && $(this.lock()) && ($(this.lock()).val().length=== 0 || parseInt($(this.lock()).val(), 10)<= 0);
		}, 
		refresh: function() {
			if(this.valid()) {
  			janal.console("jsVentas.refresh: "+ this.cursor.index);
				refresh(this.cursor.index);
			} // if
			return false;
		},
		refreshGarantia: function(index) {			
  		janal.console("jsVentas.refreshGarantia: " + index);
			refresh(index);			
			return false;
		},
		refreshAsterisk: function() {			
  		janal.console("jsVentas.refresh: "+ this.cursor.index);
			refresh(this.cursor.index);
			return false;
		},
		isPorcentaje: function(s) {
      if(janal.empty(s))
        if(arguments.length === 1)
          return false;
        else
          return(arguments[1] === true);
      for(var i= 0; i< s.length; i++) {
        var c= s.charAt(i);
        if(!janal.isDigit(parseInt(c, 10)) && (c!==' ') && (c!==',') && (c!=='.'))
          return false;
      } // for
      return true;
		},
		isFlotante: function(s) {
      if(janal.empty(s))
        if(arguments.length === 1)
          return false;
        else
          return(arguments[1] === true);
      for(var i= 0; i< s.length; i++) {
        var c= s.charAt(i);
        if(!janal.isDigit(parseInt(c, 10)) && (c!=='.'))
          return false;
      } // for
      return true;
		},
		div: function() {
			janal.console('jsVentas.div: ');		
		  if(this.ctrlDiv) {
				var value= this.get().trim();
				var temp = $(this.discount()).val();
				if($(this.discount()) && value.length> 0 && this.isPorcentaje(value)) {
					$(this.discount()).val(value);
					var ok= janal.descuentos($(this.discount()));
					if(ok.error)
						$(this.discount()).val(temp);
					else {
						this.set('');
						this.refresh();
					} // if
					return ok.error;
				} // if	 
			} // if	 
			return true;
		},
		autorizedDiscount: function() {
			janal.console('jsVentas.autorizedDiscount: ');					
			autorized(this.cursor.index);
		}, // autorizedDiscount
		autorizedPrecio: function() {
			janal.console('jsVentas.autorizedDiscount: ');					
			autorizedModificacionPrecio(this.cursor.index);
		}, // autorizedDiscount
		divDiscount: function(value) {
			janal.console('jsVentas.div: ');					
			//var value= this.get().trim();
			var temp = $(this.discount()).val();
			if($(this.discount()) && value.length> 0 && this.isPorcentaje(value)) {
			  $(this.discount()).val(value);
				var ok= janal.descuentos($(this.discount()));
				if(ok.error)
				  $(this.discount()).val(temp);
				else {
					this.set('');
  				this.refresh();
				} // if
			  return ok.error;
			} // if	 			 
			return true;
		},
		asterisk: function() {
			janal.console('jsVentas.asterisk: ');
			var value = this.get().trim();
			var temp = $(this.amount()).val();
			if($(this.amount()) && value.length> 0 && this.isFlotante(value)) {
			  $(this.amount()).val(value);
				var ok= janal.precio($(this.amount()), value);
				if(ok.error)
				  $(this.amount()).val(temp);
				else {
					this.set('');
	 				this.refreshAsterisk();
				} // if
			  return ok.error;
			} // if	
			return true;
		},
		plus: function() {
			janal.console('jsVentas.plus: ');			
		  if(this.ctrlPlus) {
				var value = this.get().trim();
				var temp = $(this.price()).val();
				if($(this.price()) && value.length> 0 && this.isFlotante(value)) {
					$(this.price()).val(value);
					var ok= janal.precio($(this.price()), value);
					if(ok.error)
						$(this.price()).val(temp);
					else {
						this.set('');
						this.refresh();
					} // if
					return ok.error;
				} // if	 
			} // if	 
			return true;
		},
		point: function() {
			janal.console('jsVentas.point: ');
			var value = this.get().trim();
			var temp = $(this.additional()).val();
			if($(this.additional()) && value.length> 0 && this.isPorcentaje(value)) {
			  $(this.additional()).val(value);
				var ok= janal.descuentos($(this.additional()));
				if(ok.error)
				  $(this.additional()).val(temp);
				else {
					this.set('');
	 				this.refresh();
				} // if
		  return ok.error;
			} // if	
			return true;
		},
		find: function() {
			janal.console('jsVentas.find: '+ this.get().trim());
			var value = this.get().trim();
			if(value.startsWith('='))
				this.set(eval(value.substring(1)));
			else
				if($('ul.ui-autocomplete-items:visible').length<= 0 && value.length<= 0)
					this.down(true);
			return false;
		},
		exists: function(codigo, nombre, index) {
			janal.console('jsVentas.exists: '+ index);
			alert('El articulo ['+ codigo+ ' - '+ nombre+' ]\nya existe en la venta y se encuentra en la fila '+ (index+ 1)+ ',\npor favor verifique y corriga la cantidad !');
			this.cursor.tmp= index;
			setTimeout('$articulos.cursor.index= $articulos.cursor.tmp;$articulos.goto();', 1000);
			janal.desbloquear();
		}, 
		goto: function() {
			janal.console('jsVentas.goto: '+ this.name());
			if($(this.name())) 
				$(this.name()).focus();
		},
		clean: function() {
			janal.console('jsVentas.clean: '+ this.cursor.index+ ' => '+ this.cursor.top);
			if(this.cursor.top> 0 && this.valid()) {
			  suppress(this.cursor.index);
				$(this.price()).val('0');
				$(this.amount()).val('0');
				$(this.discount()).val('0');
				$(this.additional()).val('0');
			  $(this.key()).val('-1');
			} // if
			return false;
		},
		update: function(top) {
			janal.console('jsVentas.update: '+ top);
			this.cursor.top= top;
		},
		calculate: function(active) {
			janal.console('jsVentas.calculate: '+ this.current+ ' => '+ $(active).val());
			if($(active).val()!== this.current)
				if(parseFloat($(active).val(), 10)!== parseFloat(this.current, 10))
  				this.refresh();
			  else
  				if($(active).val().indexOf(',')>= 0 || this.current.indexOf(',')>= 0)
    				this.refresh();
			return false;	
		},
		calculateGarantia: function(active, index) {
			janal.console('jsVentas.calculate: '+ this.current+ ' => '+ $(active).val());
			if($(active).val()!== this.current)
				if(parseFloat($(active).val(), 10)!== parseFloat(this.current, 10))
  				this.refreshGarantia(index);
			  else
  				if($(active).val().indexOf(',')>= 0 || this.current.indexOf(',')>= 0)
    				this.refreshGarantia(index);
			return false;	
		},
		jump: function(focus) {
			janal.console('jsVentas.jump');
			if(!PF('widgetBuscados').isEmpty()) {
				PF('widgetBuscados').clearSelection();
				PF('widgetBuscados').writeSelections();
				PF('widgetBuscados').selectRow(0, true);	
				if(focus)
					$('#buscados .ui-datatable-data').focus();
			} // if	
			return false;
		},
		next: function() {
			janal.console('jsVentas.next: '+ this.cursor.index);
			if(($(this.key()) && parseInt($(this.key()).val(), 10)> 0)) {
				if($('ul.ui-autocomplete-items:visible').length> 0) {
          $('ul.ui-autocomplete-items:visible').hide();
					refresh(this.cursor.index);
				} // if	
				else 
  			  this.down(true);
			} // if	
		},
		reset: function(name) {
			janal.console('jsVentas.reset: ');
			if($(name).attr('id').endsWith(this.prices.substring(2)))
				$(name).val($(this.value()).val());
			return false;
		},
		search: function() {
			janal.console('jsVentas.search: ');
			if(this.valid())
				search($(this.key()).val(), this.cursor.index);
			return false;
		},
		show: function(name) {
			janal.lastReference= name;
			if(!this.valid()) {
			  janal.bloquear();
   			PF('dialogo').show();
			} // if
			return false;
		},
	  callback: function(code) {
			janal.console('jsVentas.callback: '+ code);
		  return false;
		},
		close: function() {
			janal.console('jsVentas.close: ');
		  replace(this.cursor.index);
			return false;
		},
		look: function(name) {
			console.log('jsVentas.look: '+ $(name).val());
			var search= $(name).val().replace(janal.cleanString, '').trim();
			if(search.length> 2)
			  lookup(search);
		},
		back: function(title, count) {
			janal.console('jsVentas.back: ');
			janal.bloquear();
			alert('Se '+ title+ ' con consecutivo: '+ count);
		},
		detail: function() {
			if(this.valid())
				detail($(this.key()).val(), this.cursor.index);
			return false;
		},
		locationArt: function() {
			if(this.valid())
				locationArt($(this.key()).val(), this.cursor.index);
			return false;
		},
		compare: function(index) {
			janal.console('jsVentas.compare: '+ index);
			var tmp= this.cursor.index;
			var msg= [];
			if(typeof(index)=== 'undefined') {
				for(var x= 0; x<= this.cursor.top; x++) {
					this.cursor.index= x;
					if(parseFloat($(this.amount()).val(), 10)> parseFloat($(this.request()).val(), 10))
						msg.push({summary: 'Informativo:', detail: 'La cantidad de la fila '+ (this.cursor.index+ 1)+ ' tiene que ser menor o igual a '+$(this.request()).val()+ '.', severity: 'error'});
				} // for
				this.cursor.index= tmp;
			} // if
			else {
				this.cursor.index= index;
				if(parseFloat($(this.amount()).val(), 10)> parseFloat($(this.request()).val(), 10))
					msg.push({summary: 'Informativo:', detail: 'La cantidad de la fila '+ (this.cursor.index+ 1)+ ' tiene que ser menor o igual a '+$(this.request()).val()+ '.', severity: 'error'});
			} // else
			return msg;
		},
		individual: function(index) {
			janal.console('jsVentas.individual: '+ index);
			janal.precio($('#contenedorGrupos\\:tabla\\:'+ index+ '\\:cantidades'), '0'); 
			janal.hide();
			var error= this.compare(index);
			if(error.length> 0) {
				$(this.amount()).val($(this.request()).val());
				this.calculate($(this.amount()));
				janal.show(error);
			} // if	
			else
        this.calculate($(this.amount()));
			return error.length=== 0;
		},
		zeros: function() {
			var count= 0;
			var tmp  = this.cursor.index;
			for(var x= 0; x<= this.cursor.top; x++) {
				this.cursor.index= x;
				if(parseFloat($(this.amount()).val(), 10)=== parseFloat($(this.request()).val(), 10))
					count++;
			} // for
			this.cursor.index= tmp; 
			janal.console('jsVentas.zeros: '+ count+ ' => '+ this.cursor.top);
			return this.cursor.top=== count;
		},
		invalidate: function(top) {
			janal.console('jsVentas.invalidate: '+ top);
			if(top>= 0)
			  this.cursor.top= top;
			janal.reset();
			if($('#contenedorGrupos\\:tabla\\:filterCode').val().trim().length> 0 || $('#contenedorGrupos\\:tabla\\:filterName').val().trim().length> 0)
			  PF('listado').deactivate();
			else
			  PF('listado').activate();
			janal.desbloquear();
		},
		activeLogin: function() {	
			janal.readingMode('CONSULTAR');
			$('#cancelar').prop('disabled', 'disabled').addClass('ui-state-disabled'); 
			$('#cancelarIcon').prop('disabled', 'disabled').addClass('ui-state-disabled');
			janal.desbloquear();
			$('.janal-login-view').attr('style', 'display: ');				
			$('.janal-login-block').attr('style', 'display: none;');				
			$('#cuenta').prop('disabled', '').removeClass('ui-state-disabled'); 				
			$('#cuenta').val('');
			$('#password').val('');
			if($('#iktan-slide-menu').length> 0) { 
				$('#iktan-slide-menu').hide(); 
				if($('#janalAccessControl').length> 0) {
					$('#janalAccessControl').val('0');
				} // if
			}  // if
			setTimeout("$('#cuenta').focus();", 500);						
		},			
		disabledLogin: function() {
			janal.readingMode('AGREGAR');
			$('.janal-login-view').attr('style', 'display: none');				
			$('.janal-login-block').attr('style', 'display: ');				
			$('#cancelar').prop('disabled', '').removeClass('ui-state-disabled'); 
			$('#cancelarIcon').prop('disabled', '').removeClass('ui-state-disabled');
			if($('#iktan-slide-menu').length> 0) { 
				$('#iktan-slide-menu').show(); 
				if($('#janalAccessControl').length> 0) {
					$('#janalAccessControl').val('1');
				} // if
			} 
		},
		toPassword: function() {
			$('#password').focus();
		},			
		toLoginEnter: function() {
			if (window.event.keyCode === 13)
				toPassword();
		}, // toLoginEnter			
		toPasswordEnter: function() {
			if (window.event.keyCode === 13) {
				janal.bloquear();
				var ok= janal.partial('login');
				if(ok) 
					loginValidate();
				else
					janal.desbloquear();
			} // if
		}, // toPasswordEnter		
		refreshCobroValidate: function() {
			var limiteCredito= parseFloat(parseFloat($('#contenedorGrupos\\:limiteCredito').text()).toFixed(2));
			var limiteDebito= parseFloat(parseFloat($('#contenedorGrupos\\:limiteDebito').text()).toFixed(2));
			var limiteCheque = parseFloat(parseFloat($('#contenedorGrupos\\:limiteCheque').text()).toFixed(2));
			var limiteTransferencia = parseFloat(parseFloat($('#contenedorGrupos\\:limiteTransferencia').text()).toFixed(2));
			var credito= parseFloat(parseFloat($('#contenedorGrupos\\:credito_input').val()).toFixed(2));
			var debito= parseFloat(parseFloat($('#contenedorGrupos\\:debito_input').val()).toFixed(2));
			var cheque = parseFloat(parseFloat($('#contenedorGrupos\\:cheque_input').val()).toFixed(2));
			var transferencia = parseFloat(parseFloat($('#contenedorGrupos\\:transferencia_input').val()).toFixed(2));
			var totalVenta = parseFloat(parseFloat($('#contenedorGrupos\\:totalVenta').text()).toFixed(2));
			this.refreshValidationsPagos(limiteCredito, limiteDebito, limiteCheque, totalVenta, limiteTransferencia);
			this.refreshDebito(debito);
			this.refreshCredito(credito);
			this.refreshCheque(cheque);
			this.refreshTransferencia(transferencia);
			janal.refresh();
			this.lastCursorAt();
		}, // refreshCobroValidate		
		validateApartado: function(minPago) {			
			var limiteCredito= parseFloat(parseFloat($('#contenedorGrupos\\:limiteCredito').text()).toFixed(2));
			var limiteDebito= parseFloat(parseFloat($('#contenedorGrupos\\:limiteDebito').text()).toFixed(2));
			var limiteCheque= parseFloat(parseFloat($('#contenedorGrupos\\:limiteCheque').text()).toFixed(2));
			var limiteTransferencia= parseFloat(parseFloat($('#contenedorGrupos\\:limiteTransferencia').text()).toFixed(2));
			var credito= parseFloat(parseFloat($('#contenedorGrupos\\:credito_input').val()).toFixed(2));
			var debito= parseFloat(parseFloat($('#contenedorGrupos\\:debito_input').val()).toFixed(2));
			var cheque = parseFloat(parseFloat($('#contenedorGrupos\\:cheque_input').val()).toFixed(2));
			var transferencia = parseFloat(parseFloat($('#contenedorGrupos\\:transferencia_input').val()).toFixed(2));
			this.refreshValidationsPagos(limiteCredito, limiteDebito, limiteCheque, minPago, limiteTransferencia);
			this.refreshDebito(debito);
			this.refreshCredito(credito);
			this.refreshCheque(cheque);
			this.refreshTransferencia(transferencia);			
			janal.refresh();
		},			
		validateCredito: function() {
			this.refreshDebito(0);
			this.refreshCredito(0);
			this.refreshCheque(0);
			this.refreshTransferencia(0);
			this.refreshFreeValidationsPagos();
			janal.refresh();
		}, // validateCredito
		refreshValidationsPagos: function(limiteCredito, limiteDebito, limiteCheque, totalVenta, limiteTransferencia){
			janal.fields.debito.validaciones= 'libre|max-valor({"cuanto":'+limiteDebito+'})';
			janal.fields.credito.validaciones= 'libre|max-valor({"cuanto":'+limiteCredito+'})';
			janal.fields.cheque.validaciones= 'libre|max-valor({"cuanto":'+limiteCheque+'})';
			janal.fields.pago.validaciones= 'requerido|min-valor({"cuanto":'+totalVenta+'})';
			janal.fields.transferencia.validaciones= 'libre|max-valor({"cuanto":'+limiteTransferencia+'})';
		}, // refreshValidationsPagos
		refreshFreeValidationsPagos: function() {
			janal.fields.debito.validaciones= 'libre';
			janal.fields.credito.validaciones= 'libre';
			janal.fields.cheque.validaciones= 'libre';
			janal.fields.pago.validaciones= 'libre';
			janal.fields.transferencia.validaciones= 'libre';
		}, // refreshFreeValidationsPagos		
		refreshCredito: function(total) {
			if(total > 0 && $('#contenedorGrupos\\:switchFacturar_input').is(':checked')) {
				janal.fields.referenciaCredito.validaciones= "requerido";
				janal.fields.bancoCredito.validaciones= "requerido";										
			} // if
			else{
				janal.fields.referenciaCredito.validaciones= "libre";
				janal.fields.bancoCredito.validaciones= "libre";										
			} // else
		}, // refreshCredito
		refreshDebito: function(total) {
			if(total > 0 && $('#contenedorGrupos\\:switchFacturar_input').is(':checked')) {
				janal.fields.referenciaDebito.validaciones= "requerido";
				janal.fields.bancoDebito.validaciones= "requerido";										
			} // if
			else {
				janal.fields.referenciaDebito.validaciones= "libre";
				janal.fields.bancoDebito.validaciones= "libre";										
			} // else
		}, // refreshCredito
		refreshCheque: function(total){
			if(total > 0 && $('#contenedorGrupos\\:switchFacturar_input').is(':checked')){
				janal.fields.referenciaCheque.validaciones= "requerido";
				janal.fields.bancoCheque.validaciones= "requerido";					
			} // if
			else{
				janal.fields.referenciaCheque.validaciones= "libre";
				janal.fields.bancoCheque.validaciones= "libre";					
			} // else
		}, // refreshCheque
		refreshTransferencia: function(total){
			if(total > 0 && $('#contenedorGrupos\\:switchFacturar_input').is(':checked')){
				janal.fields.referenciaTransferencia.validaciones= "requerido";
				janal.fields.bancoTransferencia.validaciones= "requerido";					
			} // if
			else{
				janal.fields.referenciaTransferencia.validaciones= "libre";
				janal.fields.bancoTransferencia.validaciones= "libre";					
			} // else
		}, // refreshCheque
		restoreAutenticate: function(){
			$('#cuenta').val('');
			$('#password').val('');
			setTimeout("$('#cuenta').focus();", 500);			
		}, // restoreAutenticate
		initArrayArt: function(size) {
			this.cursor.top= size;
		}, // initArrayArt
		process: function() {
			janal.console('jsVentas.process: ');
			janal.refresh();
			janal.desbloquear(); 
			PF('listado').hide();
			$('div[id$='+ this.panels+ ']').hide();
			$('div[id$='+ this.itemtips+ ']').hide();
			$('#source-image').attr('href', $('#icon-image').attr('src'));
			setTimeout('$articulos.lastCursorAt();', 500); 
		},
		clientes: function(name) {
			console.log('jsVentas.clientes: '+ $(name).val());
			var search= $(name).val().replace(janal.cleanString, '').trim();
			if(search.length> 2)
  			listado(search);
		},
		goon: function(focus) {
			janal.console('jsVentas.goon');
			if(!PF('widgetClientes').isEmpty()) {
				PF('widgetClientes').clearSelection();
				PF('widgetClientes').writeSelections();
				PF('widgetClientes').selectRow(0, true);	
				if(focus)
					$('#compradores .ui-datatable-data').focus();
			} // if	
			return false;
		},
		display: function(name) {
			janal.bloquear();
			PF('dialogoClientes').show();
			return false;
		},
		parche: function() {
			var ok= true;
			if($('#buscados_selection') && $('#buscados_selection').val().trim().length> 0 && PF('widgetBuscados')) {
			  PF('widgetBuscados').fireRowSelectEvent($('#buscados_selection').val(), 'rowDblselect'); 
				ok= false;
			} // if	
			return ok;
		},
		comprador: function() {
			var ok= true;
			if($('#compradores_selection') && $('#compradores_selection').val().trim().length> 0 && PF('widgetClientes')) {
			  PF('widgetClientes').fireRowSelectEvent($('#compradores_selection').val(), 'rowDblselect'); 
				ok= false;
			} // if	
			return ok;
		},
		focusCobro: function() {
			$('#contenedorGrupos\\:efectivo_input').addClass('ui-state-focus');
			$('#contenedorGrupos\\:efectivo_input').focus();
			$("#contenedorGrupos\\:efectivo_input").val('');
		},
		restoreCursorAt: function() {
			janal.console('jsArticulos.restoreCursorAt: '+ this.name());
			setTimeout("jsArticulos.goto();", 1000);
		},
		lastCursorAt: function() {
			janal.console('jsArticulos.lastCursorAt: '+ $("input[id$="+ this.codes+ "]").last().attr('id'));
			setTimeout('$("input[id$="+ jsArticulos.codes+ "]").last().focus();', 1000);
		},
		ventaFinished: function() {
			// janal.refresh();
			var ok= janal.partial('general');
			if(ok) {																		
				PF('dlgCerrarVenta').show();	
				// setTimeout("$('#dialogoCerrarVenta').focus();", 1000);						
			} // if				
			else
				janal.desbloquear();
			return ok;
		}, // ventaFinished
		applyValidacionesSwitch: function() {				
			var isCredito = $('#contenedorGrupos\\:creditoCliente_input').is(':checked');
			var isApartado= $('#contenedorGrupos\\:switchApartado_input').is(':checked');
			if(isApartado) {
				var anticipo= 10;
				var total   = parseFloat($('#contenedorGrupos\\:total').val());
				var minAbono= (total * anticipo) / 100;
				this.validateApartado(minAbono.toFixed(2));
			} // if
			else if(isCredito)
				this.validateCredito();
			else
				this.refreshCobroValidate();
		}, // applyValidacionesGeneral
		applyValidacionesGeneral: function(){				
			this.applyValidacionesSwitch();
			this.ventaFinished();
		}, // applyValidacionesGeneral
		applyValidacionesCredito: function() {
			this.applyValidacionesSwitch();		
			janal.desbloquear();
		}, // applyValidacionesCredito
		validateCapturaDescuentos: function() {
			var ok= janal.partial('descuento');
			if(ok){
				PF('dlgDescuentos').hide();					
				this.autorizedDiscount();
				janal.restore();
				janal.refresh();
			} // if
			return ok;
		}, // validateCapturaDescuentos		
		validateFinishVenta: function() {
			janal.bloquear();
			var ok= janal.partial('articulo');
			if(!ok)
				janal.desbloquear();
			return ok;
		}, // validateFinishVenta		
    descuentoUserFocus: function() {
			setTimeout("$('#descuentousr').focus();", 1000);						
		}		
	});
	
	console.info('Iktan.Control.Articulos initialized');
})(window);			
			
			
