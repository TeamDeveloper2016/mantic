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
		datacontrol : 'kajoolTable',  
		datapaginate: 'widgetPaginator',  
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
		origins     : '\\:origins',
		values      : '\\:values',
		selector    : '.key-down-event',
		focus       : '.key-focus-event',
		findout     : '.key-find-event',
		averages    : '.key-press-enter',
		filter      : '.key-filter-event',
		current     : '',
		typingTimer : null,
		doneInterval: 10000,
		continue    : false,
		leavePage   : true,
		paginator   : false,
		VK_ENTER    : 13, 
		VK_FIRST_PAGE: 36, 
		VK_LAST_PAGE : 35, 
		VK_PREV_PAGE : 33, 
		VK_NEXT_PAGE : 34, 
		VK_ESC       : 27,
		VK_ASTERISK  : 106,
		VK_EQUALS    : 48,
		VK_MINUS     : 109,
		VK_COMA      : 191,
		VK_OPEN      : 122,
		VK_CLOSE     : 123,
		VK_PLUS      : 107,
		VK_DIV       : 111,
		VK_POINT     : 110,
		VK_UP        : 38,
		VK_DOWN      : 40,
		VK_REST      : 189,
		VK_PIPE      : 220,
		VK_CTRL      : 17,
		VK_MAYOR     : 226,
		VK_F7        : 118,
		VK_F10       : 121,
		VK_BRACKET   : 222,
		VK_FIN       : 35,
		VK_PAGINATOR : 19,
	  change       : [13, 19, 27, 106, 107, 110, 111, 188, 121, 189, 191, 220, 222, 226],
	  teclas       : [9, 13, 17, 27, 33, 34, 35, 36, 38, 40, 220, 118, 121, 122],
		cursor: {
			top: 1, // el top debera ser elementos que van de 0 a n-1
			index: 0,
			tmp: 0
		},
		temporal: '',
		init: function(top, content, paginator) { // Constructor
			$articulos= this;
			this.cursor.top= top-1;
			if(typeof(content)!== 'undefined')
			  this.joker= content;
			if(typeof(paginator)!== 'undefined')
			  this.paginator= paginator;
			this.events();
		}, // init
		events: function() {
      $(document).on('focus', this.filter, function() {
				janal.console('jsArticulos.focus: '+ $(this).attr('id')+ ' value:['+ $(this).val().trim()+ ']');
				$articulos.current= $(this).val().trim();
				janal.lastNameFocus= this;
			});  
      $(document).on('keyup', this.filter, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keyup: '+ $(this).attr('id')+ ' current: ['+ $articulos.current+ '] value: ['+ $(this).val().trim()+ ']');
				if($articulos.current!== $(this).val().trim()) {
					$articulos.current= $(this).val().trim();
					filterRows();
				} // if	
			});  
			$(window).bind('beforeunload', function() { 
				if(typeof(jsArticulos)=== 'undefined' || jsArticulos.leavePage)
					return ;
				else
				  return 'Es probable que los cambios no se hayan guardado\n\u00BF Aun asi deseas salir de esta opción ?';
			});			
      $(document).on('keydown', '.key-buscados-event', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keydown [key-buscados-event]: '+ key);
				switch(key) {
					case $articulos.VK_UP:	
					case $articulos.VK_DOWN:	
					case $articulos.VK_TAB:
						if($articulos.temporal!== $('#codigo').val().trim()) {
        			janal.console('jsArticulos.lookup '+ + $(this).val());
  						lookup($(this).val().replace(janal.cleanString, '').trim());
						} // if
						return $articulos.jump(true);
					  break;
					case $articulos.VK_ESC:
            PF('dialogo').hide();
					  break;
					case $articulos.VK_ENTER:
      			janal.console('jsArticulos.lookup '+ + $(this).val());
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
      $(document).on('keyup', this.findout, function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keyup: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_UP:	
					case $articulos.VK_DOWN:	
					case $articulos.VK_ENTER:
					case $articulos.VK_TAB:
						return $articulos.go(true);
					  break;
					case $articulos.VK_ESC:
            PF('buscador').hide();
						break;
					case $articulos.VK_PAGE_NEXT:
						if($('#encontrados_paginator_top > a.ui-paginator-next')) {
						  $('#encontrados_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($articulos.go(false), 1000);
						} // if
						else
							return false;
						break;
					case $articulos.VK_PAGE_PREV:
						if($('#encontrados_paginator_top > a.ui-paginator-prev')) {
	  					$('#encontrados_paginator_top > a.ui-paginator-prev').click();
  						return setTimeout($articulos.go(false), 1000);
						} // if
						else
							return false;
						break;
					default:
						clearTimeout($articulos.typingTimer);
						if ($(this).val() && $(this).val().trim().length> 0) 
							$articulos.typingTimer= setTimeout($articulos.relocate($(this)), $articulos.doneInterval);
						break;
				} // swtich
				return false;
			});  
	    $(document).on('keydown', '.janal-find-articulos', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keydown: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_TAB:
					  $('#auxiliar').focus();
						return false;
					  break;
					case $articulos.VK_ESC:
            PF('buscador').hide();
						break;
					case $articulos.VK_F7:
					case $articulos.VK_ENTER:
				    return $articulos.enter();
						break;
					case $articulos.VK_UP:
					case $articulos.VK_DOWN:
						// return $articulos.hide();
						break;
					case $articulos.VK_PAGE_NEXT:
						if($('#encontrados_paginator_top > a.ui-paginator-next')) {
						  $('#encontrados_paginator_top > a.ui-paginator-next').click();
						  return setTimeout($articulos.go(false), 1000);
					  } // if
						else
							return false;
						break;
					case $articulos.VK_PAGE_PREV:
						if($('#encontrados_paginator_top > a.ui-paginator-prev')) {
  						$('#encontrados_paginator_top > a.ui-paginator-prev').click();
	  					return setTimeout($articulos.go(false), 1000);
					  } // if
						else
							return false;
						break;
				} // swtich
			});			
      $(document).on('focus', this.focus, function() {
				janal.console('jsArticulos.focus: '+ $(this).attr('id')+ ' value: '+ $(this).val());
				$articulos.current= $(this).val();
				$articulos.index($(this).attr('id'));
				janal.lastNameFocus= this;
			});  
      $(document).on('focus', '.key-move-event', function() {
				janal.console('jsArticulos.focus: '+ $(this).attr('id')+ ' value: '+ $(this).val());
				$articulos.current= $(this).val();
				$articulos.index($(this).attr('id'));
				janal.lastNameFocus= this;
			});  
      $(document).on('focus', this.selector, function() {
				janal.console('jsArticulos.focus: '+ $(this).attr('id')+ ' value: '+ $(this).val());
				$articulos.index($(this).attr('id'));
				janal.lastNameFocus= this;
			});  
      $(document).on('keydown', this.averages, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keydown: '+ $(this).attr('id')+ ' key: '+ key);
				if(($articulos.change.indexOf(key)>= 0)) 
					$articulos.leavePage= false;
				switch(key) {
					case $articulos.VK_ENTER:
						$(this).blur();
						return false;
						break;
				} // switch
			});	
			$(document).on('keydown', '.key-event-sat', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keydown: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_UP:
						return $articulos.moveup('\\'+ $(this).attr('id').substring($(this).attr('id').lastIndexOf(':')));
						break;
					case $articulos.VK_DOWN:
					case $articulos.VK_ENTER:
						return $articulos.movedown('\\'+$(this).attr('id').substring($(this).attr('id').lastIndexOf(':')));
						break;
					case $articulos.VK_F7:
						return $articulos.detail();
						break;
				} // switch
			});	
      $(document).on('keydown', this.focus, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keydown: '+ $(this).attr('id')+ ' key: '+ key);
				if(($articulos.change.indexOf(key)>= 0))
					$articulos.leavePage= false;
				switch(key) {
					case $articulos.VK_ENTER:
						return $articulos.calculate($(this));
						break;
					case $articulos.VK_MINUS:
						return $articulos.reset($(this));
						break;
					case $articulos.VK_REST:
						return $articulos.show($(this));
						break;
				} // switch
			});	
      $(document).on('keydown', this.selector, function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keydown [key-down-event]: '+ $(this).attr('id')+ ' key: '+ key);
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
					case $articulos.VK_REST:
						if(calculate)
						  return true;
						else {
							var txt  = $(this).val().trim().length<= 0;
							var token= $($articulos.lock())? $articulos.remove(): true;
							if(txt && $('ul.ui-autocomplete-items:visible').length<= 0)
								if(token)
									return $articulos.clean();
								else
									return $articulos.recover();
						} // else		
						break;
					case $articulos.VK_PIPE:
						return $articulos.search();
						break;						
					case $articulos.VK_MINUS:
						var ok= janal.partial('articulo');
						if(ok) {
							$articulos.leavePage= true;
							var txt= $(this).val().trim().length<= 0;
							if(txt && $('ul.ui-autocomplete-items:visible').length<= 0 && confirm('\u00BF Esta seguro que desea terminar con la captura ?')) {
								$('#aceptar').click();
								return false;
							} // if
						} // if
						break;
					case $articulos.VK_MAYOR:
						return $articulos.show(this);
						break;
					case $articulos.VK_F7:
						return $articulos.detail();
						break;
					case $articulos.VK_F10:
						if(calculate)
						  $articulos.set(eval($articulos.get().trim().substring(1)));
						return false;
						break;
				  case $articulos.VK_BRACKET:
						if(calculate)
						  return true;
						else {
							if($('ul.ui-autocomplete-items:visible').length<= 0)
								return $articulos.recover();
						} // else		
					  break;
					case $articulos.VK_PREV_PAGE:
						return $articulos.pagePrev();
						break;
					case $articulos.VK_NEXT_PAGE:
						return $articulos.pageNext();
						break;
					case $articulos.VK_FIRST_PAGE:
						return $articulos.pageFirst();
						break;
					case $articulos.VK_LAST_PAGE:
						return $articulos.pageLast();
						break;
					case $articulos.VK_PAGINATOR:
						if(!$(PF($articulos.datapaginate).jqId).hasClass('disabled'))
						  PF($articulos.datapaginate).toggle();
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
				janal.console('jsArticulos.keyup: '+ $(this).attr('id')+ ' key: '+ key);
				switch(key) {
					case $articulos.VK_MAYOR:
						return $articulos.display($(this));
						break;
				} // switch
			});  
      $(document).on('keyup', '.key-up-clientes', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keyup: '+ $(this).attr('id')+ ' key: '+ key);
				clearTimeout($articulos.typingTimer);
				if ($(this).val() && $(this).val().trim().length> 0 && $articulos.teclas.indexOf(key)< 0) 
					$articulos.typingTimer= setTimeout($articulos.clientes($(this)), $articulos.doneInterval);
				return false;
			});  
	    $(document).on('keydown', '.janal-key-clientes', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsArticulos.keydown: '+ key);
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
				janal.console('jsArticulos.keydown: '+ $(this).attr('id')+ ' key: '+ key);
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
			setTimeout('$articulos.goto()', 1000);
		},
		moveup: function(which) {
			janal.console('jsArticulos.moveup: '+ this.cursor.index+ ' =>'+ which);
			this.up(false);
			var id= '#'+ this.joker+ this.cursor.index+ which;
			if($(id))
				$(id).focus();
			return false;
		},
		movedown: function(which) {
			janal.console('jsArticulos.movedown: '+ this.cursor.index+ ' =>'+ which);
			this.down(false);
			var id= '#'+ this.joker+ this.cursor.index+ which;
			if($(id))
				$(id).focus();
			return false;
		},
		index: function(id) {
			janal.console('jsArticulos.index: '+ this.cursor.index+ ' =>'+ id);
			id= id.replace(/:/gi, '\\:');
			var start= id.indexOf(this.joker)>= 0? this.joker.length: -1;
			if(start> 0)
				this.cursor.index= parseInt(id.substring(start, id.lastIndexOf('\\:')), 10);
		},
		move: function() {
			janal.console('jsArticulos.move: '+ this.name());
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
		origin: function() {
			return '#'+ this.joker+ this.cursor.index+ this.origins;
		},
		set: function(value) {
			janal.console('jsArticulo.set: '+ this.name()+ ' ->'+ $(this.name()).val());
		  if($(this.name()))
				$(this.name()).val(value);	
		},
		get: function() {
			janal.console('jsArticulo.get: '+ this.name()+ ' ->'+ $(this.name()).val());
			return $(this.name())? $(this.name()).val(): '';
		},
		visible: function(index) {
			var id= '#'+ this.joker+ index+ this.codes;
			return $(id).length> 0;
		},
		up: function(jump) {
			janal.console("jsArticulos.up: "+ this.cursor.index);
			if(this.cursor.index> 0) {
				if(this.visible(this.cursor.index- 1))
				  this.cursor.index--;
			} // if	
			else
				if(this.visible(this.cursor.top))
  				this.cursor.index= this.cursor.top;
			if(jump)
			  this.move();
			return false;
		},
		down: function(jump) {
			janal.console("jsArticulos.down: "+ this.cursor.index+ ' '+ jump);
			if(this.cursor.index< this.cursor.top)
				if(this.visible(this.cursor.index+ 1))
  				this.cursor.index++;
			else
				if(this.visible(0))
  				this.cursor.index= 0;
			if(jump)
  			this.move();
			return false;
		},
		valid: function() {
			janal.console('jsArticulo.valid: '+ $(this.key()).attr('id'));
			return $(this.key()) && parseInt($(this.key()).val(), 10)> 0;
		},
		pagePrev: function() {
			if(this.paginator) 
				if(PF(this.datacontrol).paginator.cfg.page> 0) {
					PF(this.datacontrol).paginator.setPage(PF(this.datacontrol).paginator.cfg.page- 1);
					setTimeout('$articulos.cursor.index= PF($articulos.datacontrol).paginator.cfg.page* PF($articulos.datacontrol).paginator.cfg.rows; $articulos.goto();', 1000);
				} // if
			return false;
		},
		pageNext: function() {
			if(this.paginator) 
				if(PF(this.datacontrol).paginator.cfg.page< (PF(this.datacontrol).paginator.cfg.pageCount- 1)) {
					PF(this.datacontrol).paginator.setPage(PF(this.datacontrol).paginator.cfg.page+ 1);
					setTimeout('$articulos.cursor.index= PF($articulos.datacontrol).paginator.cfg.page* PF($articulos.datacontrol).paginator.cfg.rows; $articulos.goto();', 1000);
				} // if
			return false;
		},
		pageFirst: function() {
			if(this.paginator) {
				PF(this.datacontrol).paginator.setPage(0);
				setTimeout('$articulos.cursor.index= 0; $articulos.goto();', 1000);
			} // if
			return false;
		},
		pageLast: function() {
			if(this.paginator) {
				PF(this.datacontrol).paginator.setPage(PF(this.datacontrol).paginator.cfg.pageCount- 1);
				setTimeout('$articulos.cursor.index= $articulos.cursor.top; $articulos.goto();', 1000);
			} // if
			return false;
		},
		remove: function() {
			janal.console('jsArticulo.remove: '+ $(this.lock()).attr('id'));
			return this.valid() && $(this.lock()) && ($(this.lock()).val().length=== 0 || parseInt($(this.lock()).val(), 10)<= 0);
		}, 
		refresh: function() {
			if(this.valid()) {
  			janal.console("jsArticulos.refresh: "+ this.cursor.index);
				refresh(this.cursor.index);
			} // if
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
			janal.console('jsArticulo.div: ');
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
			return true;
		},
		asterisk: function() {
			janal.console('jsArticulo.asterisk: ');
			var value = this.get().trim();
			var temp = $(this.amount()).val();
			if($(this.amount()) && value.length> 0 && this.isFlotante(value)) {
			  $(this.amount()).val(value);
				var ok= janal.precio($(this.amount()), value);
				if(ok.error)
				  $(this.amount()).val(temp);
				else {
    			janal.console('jsArticulo.refresh: ');
					this.set('');
	 				refresh(this.cursor.index);
				} // if
			  return ok.error;
			} // if	
			return true;
		},
		plus: function() {
			janal.console('jsArticulo.plus: ');
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
			return true;
		},
		point: function() {
			janal.console('jsArticulo.point: ');
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
			janal.console('jsArticulo.find: '+ this.get().trim());
			var value = this.get().trim();
			if(value.startsWith('='))
				this.set(eval(value.substring(1)));
			else
				if($('ul.ui-autocomplete-items:visible').length<= 0 && value.length<= 0)
					this.down(true);
			return false;
		},
		exists: function(codigo, nombre, index, top, paginator) {
			janal.console('jsArticulo.exists: '+ index);
			this.cursor.tmp= index;
			if(paginator)
        PF(this.datacontrol).paginator.setPage(Math.trunc(index/ top));
			alert('El articulo ['+ codigo+ ' - '+ nombre+' ]\nya existe en la orden y se encuentra en la fila '+ (index+ 1)+ ',\n la cantidad de articulos solicitados se aumento\n con la cantidad del articulo seleccionado\n por favor verifique y corriga el valor !');
			setTimeout('$articulos.cursor.index= $articulos.cursor.tmp; $articulos.goto();', 1000);
			janal.desbloquear();
 		}, 
		skip: function(paginator) {
			this.paginator= paginator;
			if(paginator) {
				if(PF(this.datacontrol).paginator.cfg.page!== PF(this.datacontrol).paginator.cfg.pageCount) {
					PF(this.datacontrol).paginator.setPage(PF(this.datacontrol).paginator.cfg.pageCount- 1);
    			setTimeout('$articulos.cursor.index= $articulos.cursor.top; $articulos.goto();', 1000);
				} // if
			} 	
			else 
    	 setTimeout('$articulos.cursor.index= $articulos.cursor.top; $articulos.goto();', 1000); 
		},
		control: function(paginator) {
			this.paginator= paginator;
			if(paginator)
				if(PF(this.datacontrol).paginator.cfg.page!== PF(this.datacontrol).paginator.cfg.pageCount) {
					PF(this.datacontrol).paginator.setPage(PF(this.datacontrol).paginator.cfg.pageCount- 1);
    			setTimeout('$articulos.cursor.index= $articulos.cursor.top; $articulos.goto();', 1000);
				} // if
		},
		goto: function() {
			janal.console('jsArticulo.goto: '+ this.name());
			if($(this.name())) 
				$(this.name()).focus();
		},
		goon: function(focus) {
			janal.console('jsArticulo.goon');
			if(!PF('widgetClientes').isEmpty()) {
				PF('widgetClientes').clearSelection();
				PF('widgetClientes').writeSelections();
				PF('widgetClientes').selectRow(0, true);	
				if(focus)
					$('#compradores .ui-datatable-data').focus();
			} // if	
			return false;
		},
		clean: function() {
			janal.console('jsArticulos.clean: '+ this.cursor.index+ ' => '+ this.cursor.top);
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
		recover: function() {
			janal.console('jsArticulos.recover: '+ this.cursor.index+ ' => '+ this.cursor.top);
			if($(this.origin()) && ($(this.origin()).val().length> 0)) {
			  recover(this.cursor.index);
			} // if
			return false;
		},
		update: function(top) {
			janal.console('jsArticulos.update: '+ top);
			this.cursor.top= top;
		},
		calculate: function(active) {
			janal.console('jsArticulo.calculate: '+ this.current+ ' => '+ $(active).val());
			if($(active).val()!== this.current)
				if(parseFloat($(active).val(), 10)!== parseFloat(this.current, 10))
  				this.refresh();
			  else
  				if($(active).val().indexOf(',')>= 0 || this.current.indexOf(',')>= 0)
    				this.refresh();
			return false;	
		},
		jump: function(focus) {
			janal.console('jsArticulos.jump');
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
			janal.console('jsArticulo.next: '+ this.cursor.index);
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
			janal.console('jsArticulo.reset: ');
			if($(name).attr('id').endsWith(this.prices.substring(2)))
				$(name).val($(this.value()).val());
			return false;
		},
		search: function() {
			janal.console('jsArticulo.search: ');
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
			janal.console('jsArticulo.callback: '+ code);
		  return false;
		},
		close: function() {
			janal.console('jsArticulo.close: ');
		  replace(this.cursor.index);
			return false;
		},
		look: function(name) {
			console.log('jsArticulo.look: '+ $(name).val());
			var search= $(name).val().replace(janal.cleanString, '').trim();
			if(search.length> 2)
			  lookup(search);
		},
		clientes: function(name) {
			console.log('jsArticulo.clientes: '+ $(name).val());
			var search= $(name).val().replace(janal.cleanString, '').trim();
			if(search.length> 2)
  			listado(search);
		},
		relocate: function(name) {
			console.log('jsArticulo.relocate: '+ $(name).val());
			var search= $(name).val().replace(janal.cleanString, '').trim();
			if(search.length> 2)
  			findout(search);
		},
		back: function(title, count) {
			janal.console('jsArticulo.back: ');
			janal.bloquear();
			alert('Se '+ title+ ' con consecutivo: '+ count);
		},
		detail: function() {
			if(this.valid())
				detail($(this.key()).val(), this.cursor.index);
			return false;
		},
		compare: function(index) {
			janal.console('jsArticulos.compare: '+ index);
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
			janal.console('jsArticulos.individual: '+ index);
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
			janal.console('jsArticulos.zeros: '+ count+ ' => '+ this.cursor.top);
			return this.cursor.top=== count;
		},
		invalidate: function(top) {
			janal.console('jsArticulos.invalidate: '+ top);
			if(top>= 0)
			  this.cursor.top= top;
			janal.reset();
			if(PF('listado'))
			  if($('#contenedorGrupos\\:tabla\\:filterCode').val().trim().length> 0 || $('#contenedorGrupos\\:tabla\\:filterName').val().trim().length> 0)
			    PF('listado').deactivate();
			  else
			    PF('listado').activate();
			janal.desbloquear();
		},
		process: function(paginator) {
			this.paginator= paginator;
			janal.console('jsArticulos.process: ');
			janal.refresh();
			janal.desbloquear(); 
			PF('listado').hide();
			$('div[id$='+ this.panels+ ']').hide();
			$('div[id$='+ this.itemtips+ ']').hide();
			$('#source-image').attr('href', $('#icon-image').attr('src'));
			setTimeout('$articulos.goto();', 1000); 
			this.control(paginator);
		},
		go: function(focus) {
			janal.console('jsArticulos.go: ');
			PF('widgetEncontrados').clearSelection();
			PF('widgetEncontrados').writeSelections();
			PF('widgetEncontrados').selectRow(0, true);	
			if(focus)
			  $('#encontrados .ui-datatable-data').focus();
			return false;
		},
		enter: function()  {
 			janal.console('jsArticulos.enter');
      $('#encontrado').click();		
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
		}
	});
	
	console.info('Iktan.Control.Articulos initialized');
})(window);			
