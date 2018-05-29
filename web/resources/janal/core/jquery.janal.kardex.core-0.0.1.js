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
      $(document).on('focus', this.focus, function() {
				$kardex.id= $(this).attr('id');
  			janal.console('Focus: '+ $kardex.id);
				$kardex.current= $(this).val().trim();
				$kardex.index();
			});  
      $(document).on('keydown', this.focus, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				if($kardex.change.indexOf(key)>= 0)
					$kardex.leavePage= false;
				switch(key) {
					case $kardex.VK_ENTER:
						if($kardex.id.endsWith('costos'))
							return $kardex.calculate(this);
						else
						  return $kardex.id.endsWith('limites')? false: $kardex.id.endsWith('utilidades')? $kardex.utilidad(this): $kardex.calculate(this);
						break;
					case $kardex.VK_UP:
						if($kardex.id.endsWith('costos'))
							return true;
						else
  						return $kardex.up();
						break;
					case $kardex.VK_DOWN:
						if($kardex.id.endsWith('costos'))
							return true;
						else
	    				return $kardex.down();
						break;
					case $kardex.VK_ESC:
						if($kardex.id.endsWith('costos'))
							return true;
						else
  						return $kardex.back();
						break;
				} // switch
			});	
      $(document).on('blur', this.focus, function(e) {
				$kardex.leavePage= false;
				return $kardex.id.endsWith('limites')? false: $kardex.id.endsWith('utilidades')? $kardex.utilidad(this): $kardex.calculate(this);
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
			var $id= this.id.replace(/:/gi, '\\:');
			$('#'+ $id).val(this.current);
		},
		index: function() {
			var $id= this.id.replace(/:/gi, '\\:');
			var start= $id.indexOf(this.joker)>= 0? this.joker.length: -1;
			if(start> 0)
				this.cursor.index= parseInt($id.substring(start, $id.lastIndexOf(':')), 10);
			janal.console('Index: '+ this.id+ " => "+ this.cursor.index);
		},
		move: function() {
			var name= '#'+ this.joker+ this.cursor.index+ '\\'+ this.id.substring(this.id.lastIndexOf(':'));
			janal.console('Move: '+ this.id+ " => "+ name);
			if($(name))
				$(name).focus();
			return false;
		},
		up: function() {
			if(this.cursor.index> 0)
				this.cursor.index--;
			else
				this.cursor.index= this.cursor.top;
			return this.move();
		},
		down: function() {
			if(this.cursor.index< this.cursor.top)
				this.cursor.index++;
			else
				this.cursor.index= 0;
			return this.move();
		},
		find: function() {
			var value = $('#codigos_input').val().trim();
			if(value.length> 0)
			  locate(value);
			return false;
		},
		close: function() {
		  replace();
			return false;
		},
	  callback: function(code) {
			janal.console('Callback: '+ code);
		  return false;
		},
		different: function(value) {
			if(this.current==='')
				this.current= '0';
			if(typeof(value)=== 'undefined' || value==='')
				value= '0';
 			janal.console('Different: '+ parseFloat(this.current, 10).toFixed(2)+ " => "+ parseFloat(value, 10).toFixed(2));
			return this.current!== value && parseFloat(this.current, 10).toFixed(2)!= parseFloat(value, 10).toFixed(2);
		},
		calculate: function(name) {
			var value= $(name).val().trim();
 			janal.console('Calculate: '+ this.id+ " => "+ value);
			if(this.different(value)) {
				this.current= value;
			  calculate(this.cursor.index);
			} // if
			return false;
		},
		utilidad: function(name) {
			var value= $(name).val().trim();
 			janal.console('Utilidad: '+ this.id+ " => "+ value);
			if(this.different(value)) {
				this.current= value;
  			utilidad(this.cursor.index, value);
			} // if	
			return false;
		}
	});
	
	console.info('Iktan.Control.Kardex initialized');
})(window);

$(document).ready(function() {
  jsKardex= new Janal.Control.Kardex.Core();
});			


