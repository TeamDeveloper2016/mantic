/**
 * http://www.openjs.com/scripts/events/keyboard_shortcuts/
 * Version : 2.01.B
 * By Binny V A
 * License : BSD
 */
shortcut = {
	'all_shortcuts':{},//All the shortcuts are stored in this array
	'add': function(shortcut_combination,callback,opt) {
		//Provide a set of default options
		var default_options = {
			'type':'keydown',
			'propagate':false,
			'disable_in_input':false,
			'target':document,
			'keycode':false
		}
		if(!opt) opt = default_options;
		else {
			for(var dfo in default_options) {
				if(typeof opt[dfo] == 'undefined') opt[dfo] = default_options[dfo];
			}
		}

		var ele = opt.target;
		if(typeof opt.target == 'string') ele = document.getElementById(opt.target);
		var ths = this;
		shortcut_combination = shortcut_combination.toLowerCase();

		//The function to be called at keypress
		var func = function(e) {
			e = e || window.event;
			
			if(opt['disable_in_input']) { //Don't enable shortcut keys in Input, Textarea fields
				var element;
				if(e.target) element=e.target;
				else if(e.srcElement) element=e.srcElement;
				if(element.nodeType==3) element=element.parentNode;

				if(element.tagName == 'INPUT' || element.tagName == 'TEXTAREA') return;
			}
	
			//Find Which key is pressed
			if (e.keyCode) code = e.keyCode;
			else if (e.which) code = e.which;
			if(typeof(code)=== 'undefined')
			  code= 0;
			var character = String.fromCharCode(code).toLowerCase();
			
			if(code == 188) character=","; //If the user presses , when the type is onkeydown
			if(code == 190) character="."; //If the user presses , when the type is onkeydown

			var keys = shortcut_combination.split("+");
			//Key Pressed - counts the number of valid keypresses - if it is same as the number of keys, the shortcut function is invoked
			var kp = 0;
			
			//Work around for stupid Shift key bug created by using lowercase - as a result the shift+num combination was broken
			var shift_nums = {
				"`":"~",
				"1":"!",
				"2":"@",
				"3":"#",
				"4":"$",
				"5":"%",
				"6":"^",
				"7":"&",
				"8":"*",
				"9":"(",
				"0":")",
				"-":"_",
				"=":"+",
				";":":",
				"'":"\"",
				",":"<",
				".":">",
				"/":"?",
				"\\":"|"
			}
			//Special Keys - and their codes
			var special_keys = {
				'esc':27,
				'escape':27,
				'tab':9,
				'space':32,
				'return':13,
				'enter':13,
				'backspace':8,
	
				'scrolllock':145,
				'scroll_lock':145,
				'scroll':145,
				'capslock':20,
				'caps_lock':20,
				'caps':20,
				'numlock':144,
				'num_lock':144,
				'num':144,
				
				'pause':19,
				'break':19,
				
				'insert':45,
				'home':36,
				'delete':46,
				'end':35,
				
				'pageup':33,
				'page_up':33,
				'pu':33,
	
				'pagedown':34,
				'page_down':34,
				'pd':34,
	
				'left':37,
				'up':38,
				'right':39,
				'down':40,
	
				'f1':112,
				'f2':113,
				'f3':114,
				'f4':115,
				'f5':116,
				'f6':117,
				'f7':118,
				'f8':119,
				'f9':120,
				'f10':121,
				'f11':122,
				'f12':123,
				
				'minus': 109
			}
	
			var modifiers = { 
				shift: { wanted:false, pressed:false},
				ctrl : { wanted:false, pressed:false},
				alt  : { wanted:false, pressed:false},
				meta : { wanted:false, pressed:false}	//Meta is Mac specific
			};
                        
			if(e.ctrlKey)	modifiers.ctrl.pressed = true;
			if(e.shiftKey)	modifiers.shift.pressed = true;
			if(e.altKey)	modifiers.alt.pressed = true;
			if(e.metaKey)   modifiers.meta.pressed = true;
                        
			for(var i=0; k=keys[i],i<keys.length; i++) {
				//Modifiers
				if(k == 'ctrl' || k == 'control') {
					kp++;
					modifiers.ctrl.wanted = true;

				} else if(k == 'shift') {
					kp++;
					modifiers.shift.wanted = true;

				} else if(k == 'alt') {
					kp++;
					modifiers.alt.wanted = true;
				} else if(k == 'meta') {
					kp++;
					modifiers.meta.wanted = true;
				} else if(k.length > 1) { //If it is a special key
					if(special_keys[k] == code) kp++;
					
				} else if(opt['keycode']) {
					if(opt['keycode'] == code) kp++;

				} else { //The special keys did not match
					if(character == k) kp++;
					else {
						if(shift_nums[character] && e.shiftKey) { //Stupid Shift key bug created by using lowercase
							character = shift_nums[character]; 
							if(character == k) kp++;
						}
					}
				}
			}
			
			if(kp == keys.length && 
						modifiers.ctrl.pressed == modifiers.ctrl.wanted &&
						modifiers.shift.pressed == modifiers.shift.wanted &&
						modifiers.alt.pressed == modifiers.alt.wanted &&
						modifiers.meta.pressed == modifiers.meta.wanted) {
				callback(e);
	
				if(!opt['propagate']) { //Stop the event
					//e.cancelBubble is supported by IE - this will kill the bubbling process.
					e.cancelBubble = true;
					e.returnValue = false;
	
					//e.stopPropagation works in Firefox.
					if (e.stopPropagation) {
						e.stopPropagation();
						e.preventDefault();
					}
					return false;
				}
			}
		}
		this.all_shortcuts[shortcut_combination] = {
			'callback':func, 
			'target':ele, 
			'event': opt['type']
		};
		//Attach the function with the event
		if(ele.addEventListener) ele.addEventListener(opt['type'], func, false);
		else if(ele.attachEvent) ele.attachEvent('on'+opt['type'], func);
		else ele['on'+opt['type']] = func;
	},

	//Remove the shortcut - just specify the shortcut and I will remove the binding
	'remove':function(shortcut_combination) {
		shortcut_combination = shortcut_combination.toLowerCase();
		var binding = this.all_shortcuts[shortcut_combination];
		delete(this.all_shortcuts[shortcut_combination])
		if(!binding) return;
		var type = binding['event'];
		var ele = binding['target'];
		var callback = binding['callback'];

		if(ele.detachEvent) ele.detachEvent('on'+type, callback);
		else if(ele.removeEventListener) ele.removeEventListener(type, callback, false);
		else ele['on'+type] = false;
	}
};

shortcut.add("Alt+C", function() {
	janal.console('Shortcut calculadora:');
	if(PF('dlgCalculadora') && !PF('dlgCalculadora').isVisible())
	  PF('dlgCalculadora').show();
});

shortcut.add("f9", function() {
	janal.console('Shortcut lista de precios:');
	if(janal.session() && $('#listadoPrecios'))
		$('#listadoPrecios').click();
});

shortcut.add("f2", function() {
	janal.console('Shortcut catalogo de lista de precios:');
	if(janal.session() && $('#catalogoPrecios'))
		$('#catalogoPrecios').click();
});

shortcut.add("f10", function() {
	janal.console('Asignación de descuento a articulo:');
	if(janal.session() && PF('dlgDescuentos') && !PF('dlgDescuentos').isVisible()){
		janal.bloquear();
	  PF('dlgDescuentos').show();				
		setTimeout("$('#descuentousr').focus();", 1000);						
	} // if
});

shortcut.add("f12", function() {
	janal.console('Ocultar los datos facturacion:');
	if($("#janal-contenedor-datos").length> 0) 
	  $('#janal-icon-plus').hide();
	  $('#janal-icon-minus').hide();
		if($('#janal-contenedor-datos').is(":visible"))
		 $('#janal-icon-plus').show();
    else
		 $('#janal-icon-minus').show();
		$("#janal-contenedor-datos").fadeToggle(1000, "linear");
});

shortcut.add("Shift+C", function() {
	janal.console('Cambio de precio a articulo:');
	if(janal.session() && PF('dlgCambioPrecio') && !PF('dlgCambioPrecio').isVisible()) {
		janal.bloquear();
	  PF('dlgCambioPrecio').show();				
		setTimeout("$('#cambiopreciousr').focus();", 1000);						
	} // if
});

shortcut.add("Ctrl+M", function() {
	janal.console('Shortcut lista de precios de los articulos:');
	if(janal.session() && PF('dlgListaPrecios') && !PF('dlgListaPrecios').isVisible())
	  PF('dlgListaPrecios').show();
});

shortcut.add("Ctrl+K", function() {
	janal.console('Shortcut catalogos de articulos de los proveedores:');
	if(janal.session() && PF('dlgCatalogoArticulos') && !PF('dlgCatalogoArticulos').isVisible())
	  PF('dlgCatalogoArticulos').show();
});

shortcut.add("Shift+f9", function() {
	janal.console('Shortcut registro de faltantes:');
	if(janal.session() && PF('dlgFaltantes') && !PF('dlgFaltantes').isVisible())
	  PF('dlgFaltantes').show();
});

shortcut.add("Shift+f10", function() {
	janal.console('Shortcut verificador de precios:');
	if(janal.session() && PF('dlgVerificador') && !PF('dlgVerificador').isVisible())
	  PF('dlgVerificador').show();
});

shortcut.add("Ctrl+P", function() {
	janal.console('Shortcut tickets abiertos:');
	if(janal.session() && PF('dlgOpenTickets') && !PF('dlgOpenTickets').isVisible()) {
		if(!($('#aceptar').prop("disabled")=== true && $('#cuenta').prop("disabled")=== false)){
			janal.bloquear();
			loadTicketAbiertos();
		} // if
	} // if
});

shortcut.add("Ctrl+A", function() {
	janal.console('Shortcut apartados:');
	if(janal.session() && PF('dlgApartados') && !PF('dlgApartados').isVisible()) {
		if(!($('#aceptar').prop("disabled")=== true && $('#cuenta').prop("disabled")=== false)){
			janal.bloquear();
			loadApartados();
		} // if
	} // if
});

shortcut.add("f4", function() {
	janal.console('Shortcut cotizaciones:');
	if(janal.session() && PF('dlgCotizaciones')) {
		if(!($('#aceptar').prop("disabled")=== true && $('#cuenta').prop("disabled")=== false)){
			janal.bloquear();
			loadCotizaciones();
		} // if
	} // if
});

shortcut.add("Ctrl+U", function() {
	janal.console('Shortcut cierre ticket:');
	if(janal.session() && PF('dlgCloseTicket')) {
		janal.bloquear();
	  userUpdate();
	} // if
});

shortcut.add("Ctrl+H", function() {
	janal.console('Shortcut dialogo de ayuda:');
	if(janal.session() && PF('dlgTeclasAyuda') && !PF('dlgTeclasAyuda').isVisible()) {
		PF('dlgTeclasAyuda').show();
	} // if
});

shortcut.add("Ctrl+L", function() {
	janal.console('Shortcut para terminar con la sesión activa:');
	janal.forward();
});

shortcut.add("Ctrl+R", function() {
	janal.console('Shortcut para ejecutar una opción en especifico:');
	if(janal.session() && PF('dlgEjecutar') && !PF('dlgEjecutar').isVisible()) {
		PF('dlgEjecutar').show();
	} // if
});

shortcut.add("escape", function() {
	janal.console('Shortcut para cerrar dialogo finalizar cobro de venta');
	if(janal.session() && PF('dlgCerrarVenta') && PF('dlgCerrarVenta').isVisible()) {
		PF('dlgCerrarVenta').hide();
	} // if
	else if(janal.session() && PF('dlgCotizaciones') && PF('dlgCotizaciones').isVisible()) {
		PF('dlgCotizaciones').hide();
	} // else if
	else if(janal.session() && PF('dlgApartados') && PF('dlgApartados').isVisible()) {
		PF('dlgApartados').hide();
	} // else if
	else if(janal.session() && PF('dlgOpenTickets') && PF('dlgOpenTickets').isVisible()) {
		PF('dlgOpenTickets').hide();
	} // else if
	if(janal.session() && PF('dlgTeclasAyuda') && PF('dlgTeclasAyuda').isVisible()) {
		PF('dlgTeclasAyuda').hide();
	} // if
	if(janal.session() && PF('dlgVerificador') && PF('dlgVerificador').isVisible()) {
	  PF('dlgVerificador').hide();
  } // if
	if(janal.session() && PF('dlgListaPrecios') && PF('dlgListaPrecios').isVisible()) {
	  PF('dlgListaPrecios').hide();
	}// if
	if(janal.session() && PF('dlgFaltantes') && PF('dlgFaltantes').isVisible()) {
	  PF('dlgFaltantes').hide();
  } // if
	if(janal.session() && PF('dlgListaPrecios') && PF('dlgListaPrecios').isVisible()) {
	  PF('dlgListaPrecios').hide();
	} // if	
	if(janal.session() && PF('dlgCatalogoArticulos') && PF('dlgCatalogoArticulos').isVisible()) {
	  PF('dlgCatalogoArticulos').hide();
	} // if	
	if(janal.session() && PF('dlgEjecutar') && PF('dlgEjecutar').isVisible()) {
		PF('dlgEjecutar').hide();
	} // if
});

shortcut.add("Shift+P", function() {
	janal.console('Shortcut cobrar venta:');
	if(janal.session() && PF('dlgOpenTickets') && !PF('dlgOpenTickets').isVisible()) {
		if(!($('#aceptar').prop("disabled")=== true && $('#cuenta').prop("disabled")=== false)){
			janal.bloquear();
			loadTicketAbiertos();
		} // if
	} // if
	else
		if(janal.session() && PF('contenedorCaja')) {
			cobrarVenta();
			setTimeout(function(){			
				$('#contenedorGrupos\\:efectivo_input').addClass('ui-state-focus');
				$('#contenedorGrupos\\:efectivo_input').focus();
			}, 1000);						
		} // if
});

shortcut.add("minus", function() {
	janal.console('Shortcut finalizar cobro venta:');	
	if((janal.session() && PF('wAceptarCompra') && $('#' + PF('wAceptarCompra').id).css('display')!== 'none') || (PF('wAceptarCompraIcon') && $('#' + PF('wAceptarCompraIcon').id).css('display')!== 'none')) {
	  $('#' + ((PF('wAceptarCompra') && $('#' + PF('wAceptarCompra').id).css('display')!== 'none') ? 'aceptar' : 'aceptarIcon')).click();
	} // if
	else if((PF('wAceptarCotizacion') && $('#' + PF('wAceptarCotizacion').id).css('display')!== 'none') || (PF('wAceptarCotizacionIcon') && $('#' + PF('wAceptarCotizacionIcon').id).css('display')!== 'none')) {
		cleanSelection();		
	} // else if
});

shortcut.add("Ctrl+Z", function() {
	janal.console('Shortcut limpiar seleccionado de venta:');
	if((janal.session() && (PF('wAceptarCompra') && $('#' + PF('wAceptarCompra').id).css('display')!== 'none')) || (PF('wAceptarCompraIcon') && $('#' + PF('wAceptarCompraIcon').id).css('display')!== 'none')||(PF('wAceptarCotizacion') && $('#' + PF('wAceptarCotizacion').id).css('display')!== 'none') || (PF('wAceptarCotizacionIcon') && $('#' + PF('wAceptarCotizacionIcon').id).css('display')!== 'none')) {
		cleanInitPage();
	} // if	
});
