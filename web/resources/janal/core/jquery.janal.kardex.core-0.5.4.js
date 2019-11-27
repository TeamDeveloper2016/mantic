/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 28/05/2018
 *time 18:28:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */

(function(window) {
	var jsKardex;
	
	Janal.Control.Kardex= {};
	Janal.Control.Kardex.Core= Class.extend({
		joker       : 'contenedorGrupos\\:tabla\\:',
		comodin     : 'contenedorGrupos\\\\:tabla\\\\:',
		selector    : '.key-down-event',
		focus       : '.key-focus-event',
		reference   : '#codigos_input', 
		panels      : 'codigos_panel', 
		itemtips    : 'codigos_itemtip', 
		typingTimer : null,
		doneInterval: 10000,
		leavePage   : true,
		VK_ENTER    : 13, 
		VK_ESC      : 27,
		VK_ASTERISK : 106,
		VK_MINUS    : 109,
		VK_PLUS     : 107,
		VK_DIV      : 111,
		VK_POINT    : 110,
		VK_UP       : 38,
		VK_DOWN     : 40,
		VK_REST     : 189,
		VK_PIPE     : 220,
		VK_CTRL     : 17,
		VK_MAYOR    : 226,
	  change      : [13, 106, 111, 107, 110, 27, 226, 189, 220],
		control     : [9, 13, 17, 27, 38, 40, 220, 118, 121, 122],
		current     : 0,
		id          : '',
    cursor      : {
			top  : 2,
			index: 0
		},    
		init: function() { // Constructor
			$kardex= this;
			this.events();
		}, // init
		events: function() {
 			janal.console('jsKardex.events');
      $(document).on('keydown', '.key-buscados-event', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsKardex.keydown [key-buscados-event]: '+ key);
				switch(key) {
					case $kardex.VK_UP:	
					case $kardex.VK_DOWN:	
					case $kardex.VK_TAB:
						if($kardex.temporal!== $('#codigo').val().trim()) {
        			janal.console('jsKardex.lookup '+ + $(this).val());
  						lookup($(this).val().replace(janal.cleanString, '').trim());
						} // if
						return $kardex.jump(true);
					  break;
					case $kardex.VK_ESC:
            PF('dialogo').hide();
					  break;
					case $kardex.VK_ENTER:
      			janal.console('jsKardex.lookup '+ + $(this).val());
						$kardex.temporal= $('#codigo').val().trim();
						lookup($(this).val().replace(janal.cleanString, '').trim());
						return false;
						break;
					case $kardex.VK_PAGE_NEXT:
						$('#buscados_paginator_top > a.ui-paginator-next').click();
						return setTimeout($kardex.jump(false), 1000);
						break;
					case $kardex.VK_PAGE_PREV:
						$('#buscados_paginator_top > a.ui-paginator-prev').click();
						return setTimeout($kardex.jump(false), 1000);
						break;
				} // swtich
			});  
	    $(document).on('keydown', '.janal-key-kardex', function(e) {
				var key= e.keyCode? e.keyCode: e.which;
				janal.console('jsKardex.keydown: '+ key);
				switch(key) {
					case $kardex.VK_MINUS:
					case $kardex.VK_REST:
						janal.bloquear();
						$('#contenedorGrupos\\:agregarInventario').click();
						return false;
						break;
					case $kardex.VK_TAB:
					case $kardex.VK_ENTER:
						janal.console('jsKardex.keydown: '+ $(this).attr('id'));
						if('codigos_input'=== $(this).attr('id'))
              $('#contenedorGrupos\\:inicial').focus();								
						if('contenedorGrupos:inicial'=== $(this).attr('id'))
              $('#contenedorGrupos\\:agregarInventario').focus();								
						if('contenedorGrupos:agregarInventario'=== $(this).attr('id'))
              $(this).click();								
            return false;  					
				} // swtich
			});	
      $(document).on('keyup', '.key-up-event', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('jsKardex.keyup: '+ $(this).attr('id')+ ' key: '+ key);
				clearTimeout($kardex.typingTimer);
				if ($(this).val() && $(this).val().trim().length> 0 && $kardex.control.indexOf(key)< 0) 
					$kardex.typingTimer= setTimeout($kardex.look($(this)), $kardex.doneInterval);
				return false;
			});  
      $(document).on('focus', this.focus, function() {
				janal.lastNameFocus= this;
  			janal.console('jsKardex.focus: '+ $(this).attr('id')+ ' value: '+ $(this).val());
				if(janal.calculator(this))
  				$kardex.current= $kardex.number($(this));
				$kardex.id= $(this).attr('id');
				if($kardex.id.indexOf(':')>= 0)
					$kardex.id= $kardex.id.replace(/:/gi, '\\:');
				$kardex.index();
  			janal.console('jsKardex.focus: '+ $kardex.id+ ' value: '+ $kardex.current);
			});  
      $(document).on('keydown', '.key-change-event', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
  			janal.console('jsKardex.keydown: '+ key);
				if($kardex.change.indexOf(key)>= 0)
					$kardex.leavePage= false;
				switch(key) {
					case $kardex.VK_ENTER:
						if($kardex.id.endsWith('costos'))
							return $kardex.costo(this);
						else
							if($kardex.id.endsWith('utilidades'))
								$kardex.utilidad(this);
						  else 
								return $kardex.calculate(this);
						break;
					case $kardex.VK_UP:
						if($kardex.id.endsWith('precios') || $kardex.id.endsWith('utilidades') || $kardex.id.endsWith('limites'))
  						return $kardex.up();
						else
							return true;
						break;
					case $kardex.VK_DOWN:
						if($kardex.id.endsWith('precios') || $kardex.id.endsWith('utilidades') || $kardex.id.endsWith('limites'))
	    				return $kardex.down();
						else
							return true;
						break;
					case $kardex.VK_ESC:
						if($kardex.id.endsWith('precios') || $kardex.id.endsWith('utilidades') || $kardex.id.endsWith('limites'))
  						return $kardex.back();
						else
							return true;
						break;
				} // switch
			});	
      $(document).on('keydown', '.key-move-event', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
  			janal.console('jsKardex.keydown: '+ key);
				if($kardex.change.indexOf(key)>= 0)
					$kardex.leavePage= false;
				$kardex.joker= 'tabla\\:';
				switch(key) {
					case $kardex.VK_UP:
 						return $kardex.up();
						break;
					case $kardex.VK_ENTER:
					case $kardex.VK_DOWN:
    				return $kardex.down();
						break;
					case $kardex.VK_ESC:
 						return $kardex.back();
						break;
				} // switch
			});	
      $(document).on('blur', '.key-change-event', function(e) {
				$kardex.leavePage= false;
				if($kardex.id.endsWith('costos'))
					return $kardex.costo(this);
				else
					if($kardex.id.endsWith('utilidades'))
						$kardex.utilidad(this);
					else
						return $kardex.calculate(this);
			});	
      $(document).on('keydown', this.selector, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				if(($kardex.change.indexOf(key)>= 0)) {
					$kardex.leavePage= false;
				  setTimeout("$('div[id$='+ jsKardex.panels+ ']').hide();$('div[id$='+ jsKardex.itemtips+ ']').hide();", 500);
				} // if	 
				switch(key) {
					case $kardex.VK_ENTER:
						return $kardex.find();
						break;
					case $kardex.VK_MAYOR:
						return $kardex.show($(this));
						break;
				} // switch
			}); // keydownd	
		},
		back: function() {
			janal.console('jsKardex.back: '+ this.id+ ' value: '+ this.current);
			var $id= this.id.replace(/:/gi, '\\:');
			$('#'+ $id).val(this.current);
		},
		index: function() {
			var $id= this.id.replace(/:/gi, '\\:');
			var start= $id.indexOf(this.comodin)>= 0? this.comodin.length: -1;
			if(start> 0)
				$kardex.cursor.index= parseInt($id.substring(start, $id.lastIndexOf(':')), 10);
			janal.console('jsKardex.index: '+ $kardex.cursor.index);
		},
		move: function() {
			var name= '#'+ $kardex.joker+ $kardex.cursor.index+ '\\'+ $kardex.id.substring($kardex.id.lastIndexOf(':'));
			janal.console('jsKardex.move: '+ name);
			if($(name))
				$(name).focus();
			return false;
		},
		up: function() {
 			janal.console('jsKardex.down '+ this.cursor.index);
			if(this.cursor.index> 0)
				this.cursor.index--;
			else
				this.cursor.index= this.cursor.top;
			return this.move();
		},
		down: function() {
 			janal.console('jsKardex.up '+ this.cursor.index);
			if(this.cursor.index< this.cursor.top)
				this.cursor.index++;
			else
				this.cursor.index= 0;
			return this.move();
		},
		find: function() {
 			janal.console('jsKardex.find');
			var value = $('#codigos_input').val().trim();
			if(value.length> 0)
			  locate(value);
			return false;
		},
		close: function() {
 			janal.console('jsKardex.close');
		  replace();
			return false;
		},
	  callback: function(code) {
			janal.console('jsKardex.callback: '+ code);
		  return false;
		},
		number: function(name) {
			janal.console('jsKardex.number: '+ $(name).attr('id')+ ' -> '+ $(name).val().trim());
			var value= $(name).val().trim();
			if(typeof(value)=== 'undefined' || value==='' || Number.isNaN(parseFloat(value, 10)) || parseFloat(value, 10)=== 0) 
			  value= '1';
			var id   = $(name).attr('id');
			if(id.indexOf(':')>= 0)
				id= id.replace(/:/gi, '\\:');
			$('#'+ id).attr('value', value);
			$(name).val(value);
			janal.console('jsKardex.number ['+ id+ ']  value: ['+ value+ '] set ['+ $(name).val().trim()+ ']');
			return value;
		},
		different: function(value) {
 			janal.console('jsKardex.different ['+ $kardex.id+ '] value: '+ parseFloat($kardex.current, 10).toFixed(2)+ " => "+ parseFloat(value, 10).toFixed(2));
			return ($kardex.current!== value && parseFloat($kardex.current, 10).toFixed(2)!= parseFloat(value, 10).toFixed(2));
		},
		calculate: function(name) {
			var value= $kardex.number(name);
 			janal.console('jsKardex.calculate: '+ $(name).attr('id')+ ' => '+ value);
			if($kardex.different(value)) {
				$kardex.current= value;
			  calculate(this.cursor.index);
			} // if
			return false;
		},
		costo: function(name) {
			var value= $kardex.number(name);
 			janal.console('jsKardex.costo: '+ $(name).attr('id')+ ' value: '+ value);
			if($kardex.different(value)) {
				$kardex.current= value;
			  //var keep= confirm('\u00BF Quieres manter el porcentaje de utilidad ?\n\n Si se presiona el boton de cancelar se aplicara el\n  50% al menudeo\n  40% al medio mayoreo\n  30% al mayoreo');
        $.confirm({
					title: 'Favor de confirmar',
					content: '\u00BF Quieres manter el porcentaje de utilidad ?',
					theme: 'modern',
					boxWidth: '30%',
					useBootstrap: false,
					draggable: true,
					backgroundDismiss: false,
					backgroundDismissAnimation: 'shake',
					escapeKey: true,
					buttons: {
						Si: function () {
							costo(value, true);
						},
						No: function () {
							costo(value, false);
						}
					}
				});				
			} // if	
			return false;
		},
		utilidad: function(name) {
			var value= $kardex.number(name);
 			janal.console('jsKardex.utilidad: '+ name+ ' value '+ value);
			if($kardex.different(value)) {
				$kardex.current= value;
  			utilidad($kardex.cursor.index, value);
			} // if	
			return false;
		},
		locate: function() {
 			janal.console('jsKardex.locate: '+ $kardex.reference);
      $($kardex.reference).val(''); 
			$($kardex.reference).focus();			
		},
		process: function()  {
 			janal.console('jsKardex.process: '+ $kardex.reference);
			janal.desbloquear();
			janal.refresh();
			setTimeout($kardex.locate(), 500);
			$('#source-image').attr('href', $('#contenedorGrupos\\:icon-image').attr('src'));
		},
		show: function(name) {
			console.log('jsKardex.show: '+ $(name).val());
  	  janal.bloquear();
		  PF('dialogo').show();
			return false;
		},
		look: function(name) {
			console.log('jsKardex.look: '+ $(name).val());
			var search= $(name).val().replace(janal.cleanString, '').trim();
			if(search.length> 2)
			  lookup(search);
		},
		parche: function() {
			var ok= true;
			if($('#buscados_selection') && $('#buscados_selection').val().trim().length> 0 && PF('widgetBuscados')) {
			  PF('widgetBuscados').fireRowSelectEvent($('#buscados_selection').val(), 'rowDblselect'); 
				ok= false;
			} // if	
			return ok;
		},
		jump: function(focus) {
			janal.console('jsKardex.jump');
			if(!PF('widgetBuscados').isEmpty()) {
				PF('widgetBuscados').clearSelection();
				PF('widgetBuscados').writeSelections();
				PF('widgetBuscados').selectRow(0, true);	
				if(focus)
					$('#buscados .ui-datatable-data').focus();
			} // if	
			return false;
		}
	});
	
	console.info('Iktan.Control.Kardex initialized');
})(window);

$(document).ready(function() {
  jsKardex= new Janal.Control.Kardex.Core();
});			