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
		selector    : '.key-down-event',
		focus       : '.key-focus-event',
		reference   : '#codigos_input', 
		panels      : 'codigos_panel', 
		itemtips    : 'codigos_itemtip', 
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
      $(document).on('focus', this.focus, function() {
				janal.lastNameFocus= this;
				$kardex.id= $(this).attr('id');
				$kardex.current= $(this).val().trim();
				$kardex.index();
  			janal.console('jsKardex.focus: '+ $kardex.id+ ' value: '+ $kardex.current);
			});  
      $(document).on('keydown', this.focus, function(e) {
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
      $(document).on('blur', this.focus, function(e) {
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
			var start= $id.indexOf(this.joker)>= 0? this.joker.length: -1;
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
		different: function(value) {
			if($kardex.current==='')
				$kardex.current= '0';
			if(typeof(value)=== 'undefined' || value==='')
				value= '0';
 			janal.console('jsKardex.different ['+ $kardex.id+ '] value: '+ parseFloat($kardex.current, 10).toFixed(2)+ " => "+ parseFloat(value, 10).toFixed(2));
			return $kardex.current!== value && parseFloat($kardex.current, 10).toFixed(2)!= parseFloat(value, 10).toFixed(2);
		},
		calculate: function(name) {
			var value= $(name).val().trim();
 			janal.console('jsKardex.calculate: '+ name+ " => "+ value);
			if($kardex.different(value)) {
				$kardex.current= value;
			  calculate(this.cursor.index);
			} // if
			return false;
		},
		costo: function(name) {
			var value= $(name).val().trim();
 			janal.console('jsKardex.costo: '+ name+ ' value: '+ value);
			if($kardex.different(value)) {
				$kardex.current= value;
  			costo(value);
			} // if	
			return false;
		},
		utilidad: function(name) {
			var value= $(name).val().trim();
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
		}
	});
	
	console.info('Iktan.Control.Kardex initialized');
})(window);

$(document).ready(function() {
  jsKardex= new Janal.Control.Kardex.Core();
});			
			
