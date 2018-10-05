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
		requested   : '\\:solicitados',
		prices      : '\\:precios',
		keys        : '\\:keys',
		locks       : '\\:locks',
		values      : '\\:values',
		selector    : '.key-down-event',
		focus       : '.key-focus-event',
		lookup      : '.key-up-event',
		averages    : '.key-press-enter',
		filter      : '.key-filter-event',
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
		VK_CTRL     : 17,
		VK_MAYOR    : 226,
		VK_F7       : 118,
		VK_F8       : 119,
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
				  return 'Es probable que los cambios no se hayan guardado\n¿Aun asi deseas salir de esta opción?.';
			});			
      $(document).on('keyup', this.lookup, function(e) {
				clearTimeout($articulos.typingTimer);
        if ($(this).val() && $(this).val().trim().length> 0) 
          $articulos.typingTimer= setTimeout($articulos.look($(this)), $articulos.doneInterval);
				return false;
			});  
      $(document).on('focus', this.focus+ ',.key-move-event', function() {
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
						return $articulos.moveup('\\'+ $(this).attr('id').substring($(this).attr('id').lastIndexOf(':')));
						break;
					case $articulos.VK_DOWN:
					case $articulos.VK_ENTER:
						return $articulos.movedown('\\'+$(this).attr('id').substring($(this).attr('id').lastIndexOf(':')));
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
					case $articulos.VK_COMA:
						return $articulos.point();
						break;
					case $articulos.VK_REST:
						var txt  = $(this).val().trim().length<= 0;
						var token= $($articulos.lock())? $articulos.remove(): true;
						if(txt && $('ul.ui-autocomplete-items:visible').length<= 0  && token)
						  return $articulos.clean();
						break;
					case $articulos.VK_PIPE:
						return $articulos.search();
						break;						
					case $articulos.VK_MINUS:
						if(parseInt($('#articulos').val())===0){
							if(PF('dlgCloseTicket')) {
								janal.bloquear();
								userUpdate();
							} // if
						} // if
						else{
							var ok= janal.partial('articulo');
							if(ok){
								$articulos.leavePage= true;
								var txt= $(this).val().trim().length<= 0;
								if(txt && $('ul.ui-autocomplete-items:visible').length<= 0 && confirm('¿ Esta seguro que desea terminar con la captura ?')) {
									$('#aceptar').click();
									return false;
								} // if
							} // if
						} // else
						break;
					case $articulos.VK_MAYOR:
						return $articulos.show($(this));
						break;
					case $articulos.VK_F7:
						return $articulos.detail();
						break;
					case $articulos.VK_F8:
						return $articulos.locationArt();
						break;
					default:
						break;
				} // switch
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
			janal.console('jsArticulo.set: '+ this.name()+ ' ->'+ $(this.name()).val());
		  if($(this.name()))
				$(this.name()).val(value);	
		},
		get: function() {
			janal.console('jsArticulo.get: '+ this.name()+ ' ->'+ $(this.name()).val());
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
			janal.console('jsArticulo.valid: ');
			return $(this.key()) && parseInt($(this.key()).val(), 10)> 0;
		}, 
		remove: function() {
			janal.console('jsArticulo.remove: ');
			return this.valid() && $(this.lock()) && ($(this.lock()).val().length=== 0 || parseInt($(this.lock()).val(), 10)<= 0);
		}, 
		refresh: function() {
			if(this.valid()) {
  			janal.console("jsArticulos.refresh: "+ this.cursor.index);
				refresh(this.cursor.index);
			} // if
			return false;
		},
		refreshGarantia: function(index) {			
  		janal.console("jsArticulos.refreshGarantia: " + index);
			refresh(index);			
			return false;
		},
		refreshAsterisk: function() {			
  		janal.console("jsArticulos.refresh: "+ this.cursor.index);
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
			janal.console('jsArticulo.div: ');		
			/*
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
			*/
			return true;
		},
		autorizedDiscount: function() {
			janal.console('jsArticulo.autorizedDiscount: ');					
			autorized(this.cursor.index);
		}, // autorizedDiscount
		autorizedPrecio: function() {
			janal.console('jsArticulo.autorizedDiscount: ');					
			autorizedModificacionPrecio(this.cursor.index);
		}, // autorizedDiscount
		divDiscount: function(value) {
			janal.console('jsArticulo.div: ');					
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
			janal.console('jsArticulo.asterisk: ');
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
			janal.console('jsArticulo.plus: ');			
			/*
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
			*/
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
			janal.console('jsArticulo.find: ');
			var value = this.get().trim();
			if(value.startsWith('='))
				this.set(eval(value.substring(1)));
			else
			  if(value.length> 0 && !this.valid())
			    locate(value, this.cursor.index);
			  else
					this.down(true);
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
		calculateGarantia: function(active, index) {
			janal.console('jsArticulo.calculate: '+ this.current+ ' => '+ $(active).val());
			if($(active).val()!== this.current)
				if(parseFloat($(active).val(), 10)!== parseFloat(this.current, 10))
  				this.refreshGarantia(index);
			  else
  				if($(active).val().indexOf(',')>= 0 || this.current.indexOf(',')>= 0)
    				this.refreshGarantia(index);
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
			lookup($(name).val());
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
		locationArt: function() {
			if(this.valid())
				locationArt($(this.key()).val(), this.cursor.index);
			return false;
		},
		compare: function(index) {
			janal.console('jsArticulos.compare: '+ index);
			var msg= [];
			if(typeof(index)=== 'undefined') {
				for(var x= 0; x<= this.cursor.top; x++) {
					this.cursor.index= x;
					if(parseFloat($(this.amount()).val(), 10)> parseFloat($(this.request()).val(), 10))
						msg.push({summary: 'Informativo:', detail: 'La cantidad de la fila '+ (this.cursor.index+ 1)+ ' tiene que ser menor o igual a '+$(this.request()).val()+ '.', severity: 'error'});
				} // for
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
			for(var x= 0; x<= this.cursor.top; x++) {
				this.cursor.index= x;
				if(parseFloat($(this.amount()).val(), 10)=== parseFloat($(this.request()).val(), 10))
					count++;
			} // for
			janal.console('jsArticulos.zeros: '+ count+ ' => '+ this.cursor.top);
			return this.cursor.top=== count;
		},
		invalidate: function(top) {
			janal.console('jsArticulos.invalidate: '+ top);
			if(top>= 0)
			  this.cursor.top= top;
			janal.reset();
			if($('#contenedorGrupos\\:tabla\\:filterCode').val().trim().length> 0 || $('#contenedorGrupos\\:tabla\\:filterName').val().trim().length> 0)
			  PF('listado').deactivate();
			else
			  PF('listado').activate();
			janal.desbloquear();
		},
		activeLogin: function(){				
			janal.readingMode('CONSULTAR');
			$('#cancelar').prop('disabled', 'disabled').addClass('ui-state-disabled'); 
			$('#cancelarIcon').prop('disabled', 'disabled').addClass('ui-state-disabled');
			janal.desbloquear();
			$('.janal-login-view').attr('style', 'display: ');				
			$('.janal-login-block').attr('style', 'display: none;');				
			$('#cuenta').prop('disabled', '').removeClass('ui-state-disabled'); 				
			$('#cuenta').val('');
			$('#password').val('');
			setTimeout("$('#cuenta').focus();", 500);						
		},			
		disabledLogin: function() {
			janal.readingMode('AGREGAR');
			$('.janal-login-view').attr('style', 'display: none');				
			$('.janal-login-block').attr('style', 'display: ');				
			$('#cancelar').prop('disabled', '').removeClass('ui-state-disabled'); 
			$('#cancelarIcon').prop('disabled', '').removeClass('ui-state-disabled');
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
		refreshCobroValidate: function(){
			var limiteCredito= parseFloat($('#contenedorGrupos\\:limiteCredito').text());
			var limiteCheque = parseFloat($('#contenedorGrupos\\:limiteCheque').text());
			var limiteTransferencia = parseFloat($('#contenedorGrupos\\:limiteTransferencia').text());
			var credito= parseFloat($('#contenedorGrupos\\:credito_input').val());
			var cheque = parseFloat($('#contenedorGrupos\\:cheque_input').val());
			var transferencia = parseFloat($('#contenedorGrupos\\:transferencia_input').val());
			var totalVenta = parseFloat($('#contenedorGrupos\\:totalVenta').text());
			this.refreshValidationsPagos(limiteCredito, limiteCheque, totalVenta, limiteTransferencia);
			this.refreshCredito(credito);
			this.refreshCheque(cheque);
			this.refreshTransferencia(transferencia);
			janal.refresh();
		}, // refreshCobroValidate		
		validateApartado: function(minPago){				
			this.refreshCredito(0);
			this.refreshCheque(0);
			this.refreshTransferencia(0);
			janal.fields.credito.validaciones= 'libre';
			janal.fields.cheque.validaciones= 'libre';
			janal.fields.pago.validaciones= 'requerido|min-valor({"cuanto":'+minPago+'})';
			janal.fields.transferencia.validaciones= 'libre';
			janal.refresh();
		},			
		validateCredito: function(){							
			this.refreshCredito(0);
			this.refreshCheque(0);
			this.refreshTransferencia(0);
			this.refreshFreeValidationsPagos();
			janal.refresh();
		}, // validateCredito
		refreshValidationsPagos: function(limiteCredito, limiteCheque, totalVenta, limiteTransferencia){
			janal.fields.credito.validaciones= 'libre|max-valor({"cuanto":'+limiteCredito+'})';
			janal.fields.cheque.validaciones= 'libre|max-valor({"cuanto":'+limiteCheque+'})';
			janal.fields.pago.validaciones= 'requerido|min-valor({"cuanto":'+totalVenta+'})';
			janal.fields.transferencia.validaciones= 'libre|max-valor({"cuanto":'+limiteTransferencia+'})';
		}, // refreshValidationsPagos
		refreshFreeValidationsPagos: function(){
			janal.fields.credito.validaciones= 'libre';
			janal.fields.cheque.validaciones= 'libre';
			janal.fields.pago.validaciones= 'libre';
			janal.fields.transferencia.validaciones= 'libre';
		}, // refreshValidationsPagos		
		refreshCredito: function(total){
			if(total > 0){
				janal.fields.referenciaCredito.validaciones= "requerido";
				janal.fields.bancoCredito.validaciones= "requerido";										
			} // if
			else{
				janal.fields.referenciaCredito.validaciones= "libre";
				janal.fields.bancoCredito.validaciones= "libre";										
			} // else
		}, // refreshCredito
		refreshCheque: function(total){
			if(total > 0){
				janal.fields.referenciaCheque.validaciones= "requerido";
				janal.fields.bancoCheque.validaciones= "requerido";					
			} // if
			else{
				janal.fields.referenciaCheque.validaciones= "libre";
				janal.fields.bancoCheque.validaciones= "libre";					
			} // else
		}, // refreshCheque
		refreshTransferencia: function(total){
			if(total > 0){
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
		initArrayArt: function(size){
			this.cursor.top= size;
		}, // initArrayArt
		process: function() {
			janal.console('jsArticulos.process: ');
			janal.desbloquear(); 
			setTimeout('jsArticulos.next()', 500);
			$('#source-image').attr('href', $('#icon-image').attr('src'));
		}
	});
	
	console.info('Iktan.Control.Articulos initialized');
})(window);			
			
			
