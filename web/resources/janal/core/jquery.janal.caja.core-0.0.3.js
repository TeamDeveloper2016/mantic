/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 21/08/2018
 *time 11:15:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
	var jsCaja;
	
	Janal.Control.Caja= {};
	
	Janal.Control.Caja.Core= Class.extend({
		init: function() { // Constructor
			$caja= this;
			this.events();
		}, // init
		events: function() {
		},
		jumpNextReference: function(e) {
			var name= $(e.currentTarget).attr('id');
			$.each($(e.currentTarget).attr('class').split(' '), function(index, value) { 
				if(value.startsWith('janal-name')) { 
					name= value.substring(11).replace(/_/g, ' '); 
					$('#contenedorGrupos\\:'+ name).focus();
					janal.console('#contenedorGrupos\\:'+ name+ '.focus()');
				} 
			});
		},
		jumpNextControl: function(e) {
			var name  = $(e.currentTarget).attr('id');
			var id    = name.substring(name.indexOf(':')+ 1);
			var letter= id.substring(0, 1).toUpperCase()+ id.substring(1);
			var input = $(e.currentTarget).val().trim();
			var value = parseFloat(input, 10);
			if(value> 0) { 
				if(name.indexOf('efectivo')< 0) {
					letter= letter.replace(/_input/g, '_label');
					$('#contenedorGrupos\\:banco'+ letter).click();
					janal.console('#contenedorGrupos\\:banco'+ letter+ '.click()');
					letter= letter.replace(/_label/g, '_filter');
					$('#contenedorGrupos\\:banco'+ letter).focus();
					janal.console('#contenedorGrupos\\:banco'+ letter+ '.focus()');
				} // if
				else {
					$('#contenedorGrupos\\:debito'+ janal.INPUT_RESERVE).focus();
					janal.console('#contenedorGrupos\\:debito'+ janal.INPUT_RESERVE+ '.focus()');
				} // else	
			} // if
			else {
				if($(e.currentTarget)[0].tagName==='INPUT')
					$(e.currentTarget).val('0.00');
				$.each($(e.currentTarget).attr('class').split(' '), function(index, value) { 
					if(value.startsWith('janal-name')) { 
						name= value.substring(11).replace(/_/g, ' '); 
						$('#contenedorGrupos\\:'+ name+ janal.INPUT_RESERVE).focus();
						janal.console('#contenedorGrupos\\:'+ name+ janal.INPUT_RESERVE+ '.focus()');
					} 
				});
			} // else	
		},
		validateCapturaDescuentos: function() {
			var ok= janal.partial('descuento');
			if(ok){
				PF('dlgDescuentos').hide();					
				jsArticulos.autorizedDiscount();
				janal.restore();
				janal.refresh();
			} // if
			return ok;
		}, // validateCapturaDescuentos
		validateCapturaCambioPrecio: function() {
			var ok= janal.partial('descuento');
			if(ok){
				PF('dlgCambioPrecio').hide();					
				jsArticulos.autorizedPrecio();
				janal.restore();
				janal.refresh();
			} // if
			return ok;
		}, // validateCapturaCambioPrecio
		aceptarFinalizarFocus: function() {				
			setTimeout("$('#aceptarFinalizar').focus();", 100);
		},
		doFocusCuenta: function() {				
			setTimeout("$('#descuentousr').focus();", 1000);
		},
		doFocusPrecio: function() {				
			setTimeout("$('#cambiopreciousr').focus()", 1000);
		},
		markedDays: function(date) {				
			var minDays= [1,2,3,4,5,6,7,8,9];
			var disabledDays = $('#contenedorGrupos\\:days').val();
			var arrayDays = disabledDays.split(',');
			var m = date.getMonth(), d = date.getDate(), y = date.getFullYear();
			for (i = 0; i < arrayDays.length; i++) {
				m= $.inArray(m, minDays) !== -1 ? ('0'+ (m+1)) : m;
				d= $.inArray(d, minDays) !== -1 ? ('0'+ d) : d;
				if($.inArray((y + m + d), arrayDays) !== -1) {
					return [true, 'janal-color-calendar'];
				} // if
			} // for
			return [true];
		},
		validaAccionCaptura: function() {
			//var ok= confirm('\u00BF Desea guardar los cambios de la captura ?');
			jsArticulos.refreshCobroValidate();
			cobrarVenta();
		}, // validaAccionCaptura
		validateFacturacion: function(xhr, status, args){
			if (args.facturacionOk) 
				doFacturarPendiente(JSON.stringify(args.facturacion));
		} // validateFacturacion						
	});
	
	console.info('Iktan.Control.Caja initialized');
})(window);			
jsCaja= new Janal.Control.Caja.Core();

			
			
