/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 16/10/2018
 *time 16:28:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
	var jsTicket;
	
	Janal.Control.Ticket= {};
	Janal.Control.Ticket.Core= Class.extend({		
		init: function() { // Constructor
			$ticket= this;			
		}, // init		
		imprimirTicket: function(noTicket, ticket) {
			localStorage.clear();
			localStorage.setItem('noTicket', noTicket);
			localStorage.setItem('ticket', ticket);				
			JsBarcode("#barcode", localStorage.getItem('noTicket'), {				
				width: 1.4,
				height: 40,
				displayValue: false
			});
		}, // imprimirTicket
		clicTicket: function(){
			setTimeout("$('#printTicket').click();", 2000);										
		}, // clicTicket
		loadTicket: function(){				
			// <![CDATA[				
				var win = window.open();				
				win.document.open();
				win.document.write('<'+'html'+'><'+'body'+'>');
				win.document.write(localStorage.getItem('ticket'));		
				win.document.write('<div style="text-align:center;">');					
				win.document.write('<div>');					
				win.document.write('<svg id=\"barcode\">');					
				win.document.write(document.getElementById("barcode").innerHTML);
				win.document.write('</svg>');					
				win.document.write('</div>');					
				win.document.write('</div>');					
				win.document.write('<'+'/body'+'><'+'/html'+'>');
				win.setTimeout(function(){
					win.document.close();
					win.print();
					win.close();					
				}, 100);												
			// ]]>				
		}, // loadTicket
		createBarCode: function(){
			JsBarcode("#barcode", localStorage.getItem('noTicket'), {				
				width: 1.4,
				height: 40,
				displayValue: false
			});
		} // createBarCode
	});
	
	console.info('Iktan.Control.Ticket initialized');
})(window);

$(document).ready(function() {
  jsTicket= new Janal.Control.Ticket.Core();
});			
			
