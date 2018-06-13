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
		itemtips    : 'codigos_itemtip', 
		discounts   : '\\:descuentos',
		additionals : '\\:extras',
		amounts     : '\\:cantidades',
		prices      : '\\:precios',
		keys        : '\\:keys',
		locks       : '\\:locks',
		values      : '\\:values',
		selector    : '.key-down-event',
		focus       : '.key-focus-event',
		lookup      : '.key-up-event',
		porcentajes : '.key-press-enter',
		current     : '',
		dialog      : 'dialogo',
		typingTimer : null,
		doneInterval: 10000,
		continue    : false,
		leavePage   : true,
		VK_ENTER    : 13, 
		VK_ESC      : 27,
		VK_ASTERISK : 106,
		VK_MINUS    : 109,
		VK_COMA     : 188,
		VK_OPEN     : 122,
		VK_CLOSE    : 123,
		VK_PLUS     : 107,
		VK_DIV      : 111,
		VK_POINT    : 110,
		VK_UP       : 38,
		VK_DOWN     : 40,
		VK_REST     : 189,
		VK_PIPE     : 220,
		VK_CTRL     : 17,
		VK_MAYOR    : 226,
	    change      : [13, 27, 106, 107, 110, 111, 188, 189, 191, 220, 222, 226],
		cursor: {
			top: 1, // el top debera ser elementos que van de 0 a n-1
			index: 0
		},
		init: function(top, content) { // Constructor
			$articulos= this;
			this.cursor.top= top-1;
			if(typeof(content)!== 'undefined')
			  this.joker= content;
			this.events();
		}, // init
		events: function() {
			$(window).bind('beforeunload', function() { 
				if(typeof(jsArticulos)=== 'undefined' || jsArticulos.leavePage)
					return ;
				else
				  return 'Es probable que los cambios no se hayan guardado\n¿Aun asi deseas salir de esta opción?.';
			});			
      $(document).on('keyup', this.lookup, function(e) {
				clearTimeout($articulos.typingTimer);
        if ($(this).val() && $(this).val().trim().length> 0) 
          $articulos.typingTimer= setTimeout($articulos.look($(this)), $articulos.doneInterval);
				return false;
			});  
      $(document).on('focus', this.focus, function() {
				$articulos.current= $(this).val();
				$articulos.index($(this).attr('id'));
				janal.lastNameFocus= this;
			});  
      $(document).on('focus', this.selector, function() {
				$articulos.index($(this).attr('id'));
				janal.lastNameFocus= this;
			});  
      $(document).on('keydown', this.porcentajes, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
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
				switch(key) {
					case $articulos.VK_UP:
						return $articulos.xup();
						break;
					case $articulos.VK_DOWN:
						return $articulos.xdown();
						break;
				} // switch
			});	
      $(document).on('keydown', this.focus, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
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
				janal.console('Keydown: '+  key);
				if(($articulos.change.indexOf(key)>= 0)) {
					$articulos.leavePage= false;
				  setTimeout("$('div[id$='+ jsArticulos.panels+ ']').hide();$('div[id$='+ jsArticulos.itemtips+ ']').hide();", 500);
				} // if	 
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
						return $articulos.asterisk();
						break;
					case $articulos.VK_DIV:
            return $articulos.div();
						break;
					case $articulos.VK_PLUS:
						return $articulos.plus();
						break;
					case $articulos.VK_REST:
						return $articulos.point();
						break;
					case $articulos.VK_MINUS:
						var txt  = $(this).val().trim().length<= 0;
						var token= $($articulos.lock())? $articulos.remove(): true;
						if(txt && $('ul.ui-autocomplete-items:visible').length<= 0  && token)
						  return $articulos.clean();
						break;
					case $articulos.VK_PIPE:
						return $articulos.search();
						break;
					case $articulos.VK_COMA:
						$articulos.leavePage= true;
						var txt= $(this).val().trim().length<= 0;
						if(txt && $('ul.ui-autocomplete-items:visible').length<= 0 && confirm('¿ Esta seguro que desea terminar con la captura ?')) {
						  $('#aceptar').click();
						  return false;
						} // if	
						break;
					case $articulos.VK_MAYOR:
						return $articulos.show($(this));
						break;
					default:
						break;
				} // switch
      });
			setTimeout('$articulos.goto()', 1000);
		},
		xup: function() {
			this.up(false);
			var id= '#'+ this.joker+ this.cursor.index+ '\\:sat';
			if($(id))
				$(id).focus();
			return false;
		},
		xdown: function() {
			this.down(false);
			var id= '#'+ this.joker+ this.cursor.index+ '\\:sat';
			if($(id))
				$(id).focus();
			return false;
		},
		index: function(id) {
			janal.console('jsArticulos.index: '+ this.cursor.index+ ' =>'+ id+ ' =>'+ this.continue);
			if(!this.continue) {
				id= id.replace(/:/gi, '\\:');
				var start= id.indexOf(this.joker)>= 0? this.joker.length: -1;
				if(start> 0)
					this.cursor.index= parseInt(id.substring(start, id.lastIndexOf('\\:')), 10);
			} // if	
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
		  if($(this.name()))
				$(this.name()).val(value);	
		},
		get: function() {
			return $(this.name())? $(this.name()).val(): '';
		},
		up: function(jump) {
			janal.console("jsArticulos.up: "+ this.cursor.index);
			if(this.cursor.index> 0)
				this.cursor.index--;
			else
				this.cursor.index= this.cursor.top;
			if(jump)
			  this.move();
			return false;
		},
		down: function(jump) {
			janal.console("jsArticulos.down: "+ this.cursor.index);
			if(this.cursor.index< this.cursor.top)
				this.cursor.index++;
			else
				this.cursor.index= 0;
			if(jump)
  			this.move();
			return false;
		},
		valid: function() {
			return $(this.key()) && parseInt($(this.key()).val(), 10)> 0;
		}, 
		remove: function() {
			return this.valid() && $(this.lock()) && ($(this.lock()).val().length=== 0 || parseInt($(this.lock()).val(), 10)<= 0);
		}, 
		refresh: function() {
			janal.console("jsArticulos.refresh: "+ this.cursor.index);
			if(this.valid()) {
				refresh(this.cursor.index);
			} // if
			return false;
		},
		asterisk: function() {
			var value = this.get().trim();
			if($(this.amount()) && value.length> 0 && janal.isInteger(value)) {
				$(this.amount()).val(value);
				this.set('');
				this.refresh();
			  return false;
			} // if	
			return true;
		},
		isOk: function(s) {
      if(janal.empty(s))
        if(arguments.length === 1)
          return false;
        else
          return(arguments[1] === true);
      for(var i= 0; i< s.length; i++) {
        var c= s.charAt(i);
        if(!janal.isDigit(c) && (c!==' ') && (c!==',') && (c!=='.'))
          return false;
      } // for
      return true;
		},
		div: function() {
			var value= this.get().trim();
			var temp = $(this.discount()).val();
			if($(this.discount()) && value.length> 0 && this.isOk(value)) {
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
		plus: function() {
			var value = this.get().trim();
			var temp = $(this.price()).val();
			if($(this.price()) && value.length> 0 && this.isOk(value)) {
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
			var value = this.get().trim();
			var temp = $(this.additional()).val();
			if($(this.additional()) && value.length> 0 && this.isOk(value)) {
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
			janal.console('jsArticulo.find: ');
			var value = this.get().trim();
			if(value.startsWith('='))
				this.set(eval(value.substring(1)));
			else
			  if(value.length> 0 && !this.valid())
			    locate(value, this.cursor.index);
			return false;
		},
		exists: function(index) {
			janal.console('jsArticulo.exists: '+ index);
			alert('El articulo ya existe en la orden y se encuentra en la fila '+ (index+ 1)+ '.');
			if(index>= 0 && index< this.cursor.top) {
				if(index=== 0)
					this.cursor.index= this.cursor.top;
				else
			    this.cursor.index= index- 1;
				this.continue= true;
			} // if	
			janal.console('jsArticulo.exists: '+ this.cursor.index);
		}, 
		goto: function() {
			janal.console('jsArticulo.goto: '+ this.name());
			if($(this.name())) 
				$(this.name()).focus();
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
		update: function(top) {
			this.cursor.top= top;
		},
		calculate: function(active) {
			janal.console('jsArticulo.calculate: ');
			if($(active).val()!== this.current)
				if(parseFloat($(active).val(), 10)!== parseFloat(this.current, 10))
  				this.refresh();
			  else
  				if($(active).val().indexOf(',')>= 0 || this.current.indexOf(',')>= 0)
    				this.refresh();
			return false;	
		},
		next: function() {
			janal.console('jsArticulo.next: '+ this.cursor.index+ ' => '+ this.continue);
			if(($(this.key()) && parseInt($(this.key()).val(), 10)> 0) || this.continue) {
				if($('ul.ui-autocomplete-items:visible').length> 0) {
          $('ul.ui-autocomplete-items:visible').hide();
					refresh(this.cursor.index);
				} // if	
				else {	
				  this.continue= false;
  			  this.down(true);
				} // else	
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
			janal.bloquear();
			PF(this.dialog).show();
			return false;
		},
	  callback: function(code) {
			janal.console('jsArticulo.callback: '+ code);
		  return false;
		},
		close: function() {
			janal.console('jsArticulo.close: ');
		  replace(this.cursor.index);
      this.continue= true;
			return false;
		},
		look: function(name) {
			console.log('jsArticulo.look: '+ $(name).val());
			lookup();
		},
		back: function(title, count) {
			janal.console('jsArticulo.back: ');
			alert('Se '+ title+ ' con consecutivo: '+ count);
		}
	});
	
	console.info('Iktan.Control.Articulos initialized');
})(window);			
			
			
