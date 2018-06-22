/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 07/06/2018
 *time 09:39:15 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
	var jsPrecios;
	
	Janal.Control.Precios= {};
	
	Janal.Control.Precios.Core= Class.extend({
		VK_TAB      : 9, 
		VK_ESC      : 27, 
		VK_ENTER    : 13, 
		VK_UP       : 38,
		VK_DOWN     : 40,
		VK_F7       : 118,
		init: function() { // constructor
			$precios= this;
			this.hide();
	    $(document).on('keydown', '.janal-key-precios', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+ key);
				switch(key) {
					case $precios.VK_TAB:
            $('#verificadorTabla .ui-datatable-data > tr').first().find('.ui-inputfield').first().focus();		
					  break;
					case $precios.VK_ESC:
            PF('dlgVerificador').hide();
					  break;
					case $precios.VK_ENTER:
						return $precios.lookup();
						break;
				} // swtich
			});	
	    $(document).on('keydown', '.janal-key-registros', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+ key);
				switch(key) {
					case $precios.VK_ESC:
            PF('dlgVerificador').hide();
						break;
					case $precios.VK_F7:
					case $precios.VK_ENTER:
						return $precios.show();
						break;
					case $precios.VK_UP:
					case $precios.VK_DOWN:
						return $precios.hide();
						break;
				} // swtich
			});	
		},
		lookup: function() {
			janal.console('jsPrecios.lookup');
			verificadorLookup();
			return false;
		},
		show: function()  {
 			janal.console('jsPrecios.enter');
      setTimeout("$('#verificadorPrecio').attr('style', 'display:');", 500);			
			return false;
		},
		hide: function() {
			janal.console('jsPrecios.hide');
			$('#verificadorPrecio').attr('style', 'display:none');
			return false;
		}
	});
	
	console.info('Iktan.Control.Precios initialized');
})(window);	