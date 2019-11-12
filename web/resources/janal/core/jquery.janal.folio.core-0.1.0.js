/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 14/02/2019
 *time 13:15:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
			
(function(window) {
	var jsTicket;
	
	Janal.Control.Ticket= {};
	Janal.Control.Ticket.Core= Class.extend({
		cselector   : '.key-down-cliente',
		cfocus      : '.key-focus-cliente',
		aselector   : '.key-down-articulo',
		afocus      : '.key-focus-articulo',
		creference  : '#razonSocial_input', 
		cpanels     : 'razonSocial_panel', 
		citemtips   : 'razonSocial_itemtip', 
		areference  : '#articulo_input', 
		apanels     : 'articulo_panel', 
		aitemtips   : 'articulo_itemtip', 
		VK_ENTER    : 13, 
		VK_MINUS    : 109,
		VK_PLUS     : 107,
		VK_MAS      : 187,
		VK_REST     : 189,
	  change      : [13, 106, 111, 107, 110, 27, 226, 189, 220],
		control     : [9, 13, 17, 27, 38, 40, 220, 118, 121, 122],			
		init: function() { // Constructor
			$ticket= this;
			this.events();
		}, // init
		events: function() {
 			janal.console('jsTicket.events');
      $(document).on('keydown', this.cselector, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				if(($ticket.change.indexOf(key)>=0)) {
					$ticket.leavePage= false;
				  setTimeout("$('div[id$='+ jsTicket.cpanels+ ']').hide();$('div[id$='+ jsTicket.citemtips+ ']').hide();", 500);
				} // if	 
				switch(key) {
					case $ticket.VK_ENTER:
						return $ticket.cfind();
						break;
				} // switch
			}); // keydownd	
      $(document).on('keydown', this.aselector, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				if(($ticket.change.indexOf(key)>= 0)) {
					$ticket.leavePage= false;
				  setTimeout("$('div[id$='+ jsTicket.apanels+ ']').hide();$('div[id$='+ jsTicket.aitemtips+ ']').hide();", 500);
				} // if	 
				switch(key) {
					case $ticket.VK_ENTER:
						return $ticket.afind();
						break;
				} // switch
			}); // keydownd	
      $(document).on('keydown', this.aselector, function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
				if(($ticket.change.indexOf(key)>= 0)) {
					$ticket.leavePage= false;
				  setTimeout("$('div[id$='+ jsTicket.apanels+ ']').hide();$('div[id$='+ jsTicket.aitemtips+ ']').hide();", 500);
				} // if	 
				switch(key) {
					case $ticket.VK_ENTER:
						return $ticket.afind();
						break;
				} // switch
			}); // keydownd	
      $(document).on('keydown', '.key-down-consecutivo', function(e) {
				var key= e.keyCode ? e.keyCode : e.which;
   			janal.console('jsTicket.keydown: '+ key);
				switch(key) {
					case $ticket.VK_ENTER:
					case $ticket.VK_PLUS:
					case $ticket.VK_MAS:
						return $ticket.add($(this));
						break;
					case $ticket.VK_MINUS:
					case $ticket.VK_REST:
						return $ticket.remove($(this));
						break;
					default:
						return (key>=48 && key<=57) || (key>=96 && key<=105) || key===8 || key===9 || (key>=36 && key<=39) || key===46;
						break;
				} // switch
			}); // keydownd	
		},
		clocate: function() {
 			janal.console('jsTicket.locate: '+ $ticket.creference);
			$($ticket.creference).focus();			
		},
		cfind: function() {
 			janal.console('jsTicket.find');
			var value = $($ticket.creference).val().trim();
			if(value.length> 0)
			  clocate(value);
			return false;
		},
		alocate: function() {
 			janal.console('jsTicket.locate: '+ $ticket.areference);
			$($ticket.areference).focus();			
		},
		focus: function() {
 			janal.console('jsTicket.focus: consecutivo');
			$('#consecutivo').val('');			
			$('#consecutivo').focus();			
		},
		afind: function() {
 			janal.console('jsTicket.find');
			var value = $($ticket.areference).val().trim();
			if(value.length> 0)
			  alocate(value);
			return false;
		},
		add: function(item) {
			console.log('jsTicket.add:'+ $(item).val());
			var value= $ticket.consecutivo($(item).val().trim());
		  addItem(value);
			return true;
		},
		remove: function(item) {
			console.log('jsTicket.remove:'+ $(item).val());
			var clean= $(item).val().trim().length<= 0;
			if(clean)
  		  cleanItems();
			else {
  			var value= $ticket.consecutivo($(item).val().trim());
				removeItem(value);
			} // else	
			return true;
		},
		consecutivo: function(value) {
		  var regresar= value;
      var val     = value;
		  var year    = (new Date()).getFullYear();
			var params  = {cuantos: 6, cual: '0'};
			for(var x= 0; x< params.cuantos- $.trim(value).length; x++)
				val= params.cual+ val;
      if (!janal.empty(value))
				if(value.length< 6)
          regresar= year+ val; 
			  else 
  				if(value.length>= 6) {
						val= value.substring(4);
						var count= val.length;
					  for(var x= 0; x< params.cuantos- count; x++)
		     		  val= params.cual+ val;
					  regresar= value.substring(0, 4)+ val.substring(0, 6); 
					} // if
      return regresar;			
		}
	});

	console.info('Iktan.Control.Ticket initialized');
})(window);

$(document).ready(function() {
  jsTicket= new Janal.Control.Ticket.Core();
});			
