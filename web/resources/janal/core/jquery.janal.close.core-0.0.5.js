/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 21/08/2018
 *time 11:15:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
	var jsCierres;
	
	Janal.Control.Cierres= {};
	
	Janal.Control.Cierres.Core= Class.extend({
		VK_ENTER    : 13, 
		VK_UP       : 38,
		VK_DOWN     : 40,
		before      : '',
		current     : '',
		method      : 0,
		enter       : 0,
		amounts     : {
			name: 'importe',
			top : 0
		},		
		counts      : {
			name: 'cantidad',
			top : 0
		},	
		init: function(amounts, counts) { // Constructor
			$cierres= this;
			this.amounts.top= amounts- 1;
			this.counts.top = counts- 1;
			this.events();
 		}, // init
		events: function() {
      $(document).on('focus', '.key-down-event', function() {
				janal.console('jsCierres.focus: '+ $(this).attr('id')+ ' value:['+ $(this).val().trim()+ ']');
				$cierres.before= $(this).val().trim();
				janal.lastNameFocus= this;
			});  
      $(document).on('keydown', '.key-down-event', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+  key);
				switch(key) {
					case $cierres.VK_ENTER:
						if($cierres.enter=== 0) {
					    $(this).blur();
							$cierres.down();
						} // if	
						$cierres.enter= 0;
						return false;
						break;
					case $cierres.VK_UP:
					  return $cierres.up();
						break;
					case $cierres.VK_DOWN:
						return $cierres.down();
						break;
				} // switch
      });
		},
		name: function(tokens, move, top) {
  		janal.console('jsCierres.name: index['+ move+ '] top:['+ top+ ']');
			var position= parseInt(tokens[2], 10);
			if(position=== 0 && move< 0)
				tokens[2]= top;
			else
  			if(position=== top && move>= 0)
  				tokens[2]= 0;
			  else
					tokens[2]= position+ move;  
			var id= '';
			for(var token in tokens)	
			  id+= tokens[token]+ '\\:';	
  		janal.console('jsCierres.name: #'+ id.substring(0, id.length- 2));
			return '#'+ id.substring(0, id.length- 2);
		},	
		move: function(move) {
			var id    = $(janal.lastNameFocus).attr('id');
  		janal.console('jsCierres.move: #'+ id+ ' ['+ move+ ']');
			var tokens= id.split(':');
			if(tokens.length> 2) {
				if(tokens[3]=== this.amounts.name) {
			  	id= this.name(tokens, move, this.amounts.top);
					this.method= 0;
				} // if
				else
   				if(tokens[3]=== this.counts.name) {
	  		  	id= this.name(tokens, move, this.counts.top);
					  this.method= 1;
					} // if
			} // if	
			if(move!== 0 && $(id))
				$(id).focus();
		},		
		up: function() {
  		janal.console('jsCierres.up: ');
			this.move(-1);
			return false;
		},
		down: function() {
  		janal.console('jsCierres.down: ');
			this.move(1);
			return false;
		},
		calculate: function() {
  		janal.console('jsCierres.calculate: '+ this.current+ ' -- '+ this.before+ ' ['+ this.method+ ']');
  		if(parseFloat(this.current, 10)!== parseFloat(this.before, 10))
				if(this.method=== 0)
					summary();
				else
	  		  calculate();
			return false;
		},
		precio: function(id) {
      $cierres.enter= 1;
  		janal.console('jsCierres.precio: '+ id);
			janal.precio($(id), '0'); 
		  this.comun($(id));
		},	
		cantidad: function(id) {
      $cierres.enter= 1;
  		janal.console('jsCierres.cantidad: '+ id);
			janal.cantidad($(id), '0'); 
		  this.comun($(id));
		},
		comun: function(id) {
  		janal.console('jsCierres.comun: '+ $(id).attr('id'));
			this.current= $(id).val().trim(); 
			this.move(0);	
			return this.calculate();
		}	
	});
	
	console.info('Iktan.Control.Cierres initialized');
})(window);			