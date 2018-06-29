/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 27/06/2018
 *time 11:54:15 PM
 *author One Developer 2016 <team.developer@kaana.org.mx>
 */

(function(window) {
	var jsVentas;
	
	Janal.Control.Ventas= {};
	
	Janal.Control.Ventas.Core= Class.extend({		
		init: function() { // constructor
			janal.console('jsVentas init');	   
		},		
		activeLogin: function(){				
			$parent.readingMode('CONSULTAR');
			$('#cancelar').prop('disabled', 'disabled').addClass('ui-state-disabled'); 
			$('#cancelarIcon').prop('disabled', 'disabled').addClass('ui-state-disabled');
			$parent.desbloquear();
			$('.janal-login-view').attr('style', 'display: ');				
			$('.janal-login-block').attr('style', 'display: none;');				
			$('#cuenta').prop('disabled', '').removeClass('ui-state-disabled'); 				
			$('#cuenta').val('');
			$('#password').val('');
			$('#cuenta').focus();
		},			
		disabledLogin: function() {
			$parent.readingMode('AGREGAR');
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
				$parent.toPassword();
		}, // toLoginEnter			
		toPasswordEnter: function() {
			if (window.event.keyCode === 13) {
				$parent.bloquear();
				var ok= $parent.partial('login');
				if(ok) 
					loginValidate();
				else
					$parent.desbloquear();
			} // if
		}, // toPasswordEnter		
		refreshCobroValidate: function(){
			var limiteDebito = parseFloat($('#contenedorGrupos\\:limiteDebito').text());
			var limiteCredito= parseFloat($('#contenedorGrupos\\:limiteCredito').text());
			var limiteCheque = parseFloat($('#contenedorGrupos\\:limiteCheque').text());
			var debito = parseFloat($('#contenedorGrupos\\:debito').val());
			var credito= parseFloat($('#contenedorGrupos\\:credito').val());
			var cheque = parseFloat($('#contenedorGrupos\\:cheque').val());
			var totalVenta = parseFloat($('#contenedorGrupos\\:totalVenta').text());
			$parent.fields.debito.validaciones= 'libre|max-valor({"cuanto":'+limiteDebito+'})';
			$parent.fields.credito.validaciones= 'libre|max-valor({"cuanto":'+limiteCredito+'})';
			$parent.fields.cheque.validaciones= 'libre|max-valor({"cuanto":'+limiteCheque+'})';
			$parent.fields.pago.validaciones= 'requerido|min-valor({"cuanto":'+totalVenta+'})';
			this.refreshDebito(limiteDebito, totalVenta, debito);
			this.refreshCredito(limiteCredito, totalVenta, credito);
			this.refreshCheque(limiteCheque, totalVenta, cheque);
			$parent.refresh();
		}, // refreshCobroValidate			
		refreshDebito: function(value, totalVenta, total){
			if((value > 0 && value!== totalVenta)|| (total > 0)){
				$parent.fields.referenciaDebito.validaciones= "requerido";
				$parent.fields.bancoDebito.validaciones= "requerido";					
			} // if
			else{
				$parent.fields.referenciaDebito.validaciones= "libre";
				$parent.fields.bancoDebito.validaciones= "libre";															
			} // else
		}, // refreshDebito
		refreshCredito: function(value, totalVenta, total){
			if((value > 0 && value!== totalVenta) || (total > 0)){
				$parent.fields.referenciaCredito.validaciones= "requerido";
				$parent.fields.bancoCredito.validaciones= "requerido";										
			} // if
			else{
				$parent.fields.referenciaCredito.validaciones= "libre";
				$parent.fields.bancoCredito.validaciones= "libre";										
			} // else
		}, // refreshCredito
		refreshCheque: function(value, totalVenta, total){
			if((value > 0 && value!== totalVenta) || (total > 0)){
				$parent.fields.referenciaCheque.validaciones= "requerido";
				$parent.fields.bancoCheque.validaciones= "requerido";					
			} // if
			else{
				$parent.fields.referenciaCheque.validaciones= "libre";
				$parent.fields.bancoCheque.validaciones= "libre";					
			} // else
		} // refreshCheque
	});	
	console.info('Iktan.Control.Ventas initialized');
})(window);			

$(document).ready(function() {
  jsVentas= new Janal.Control.Ventas.Core();
});			



