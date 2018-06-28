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
		} // toPasswordEnter
	});
	
	console.info('Iktan.Control.Ventas initialized');
})(window);			

$(document).ready(function() {
  jsVentas= new Janal.Control.Ventas.Core();
});			



