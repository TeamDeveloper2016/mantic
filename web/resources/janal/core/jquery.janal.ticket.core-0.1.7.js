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
		clearLocalStorage: function() {
			localStorage.clear();
		}, // clearLocalStorage
		imprimirMoreTicket: function(noTicket, ticket) {
			var noTickets= noTicket.split('~');
			var tickets= ticket.split('~');
			for (var i= 0; i < noTickets.length; i++) {				
				if(this.imprimirOneTicket(noTickets[i], tickets[i], i))
					setTimeout(this.loadTicketItem(i), 2000);														
			} // for
		}, // imprimirMoreTicket
		imprimirTicket: function(noTicket, ticket) {
			this.clearLocalStorage();
			this.imprimirOneTicket(noTicket, ticket, 1);
		}, // imprimirTicket
		imprimirOneTicket: function(noTicket, ticket, number) {			
			localStorage.setItem('noTicket'+number, noTicket);
			localStorage.setItem('ticket'+number, ticket);
			if(number=== 1){
				JsBarcode("#barcode", localStorage.getItem('noTicket'+number), {				
					width: 2,
					height: 60,
					displayValue: false
				});			
			} // if
			else{
				JsBarcode("#barcode"+number, localStorage.getItem('noTicket'+number), {				
					width: 2,
					height: 60,
					displayValue: false
				});		
			} // else
			return true;
		}, // imprimirTicket
		clicTicket: function() {
			setTimeout("$('#printTicket').click();", 2000);										
		}, // clicTicket		
		process: function(pagina) {
			setTimeout("$('#printTicket').click(); window.location.href='"+ pagina+ "'", 2000);										
		}, // clicTicket		
		loadTicket: function(){				
			this.loadTicketItem(1);
		}, // loadTicket
		loadTicketItem: function(number){				
			// <![CDATA[				
				var win = window.open();		
				win.opener= null;
				win.document.open();
				win.document.write('<'+'html'+'><'+'body'+'>');
				win.document.write(localStorage.getItem('ticket'+number));		
				win.document.write('<div style="text-align:center;">');					
				win.document.write('<div>');					
				win.document.write('<svg id=\"barcode\">');			
				if(number=== 1)
					win.document.write(document.getElementById("barcode").innerHTML);
				else
					win.document.write(document.getElementById("barcode"+number).innerHTML);
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
			this.createBarCodeMoreOne(1);
		}, // createBarCode
		createBarCodeMoreOne: function(number){
			JsBarcode("#barcode", localStorage.getItem('noTicket'+number), {				
				width: 1.4,
				height: 40,
				displayValue: false
			});
		} // createBarCodeMoreOne
	});
	
	console.info('Iktan.Control.Ticket initialized');
})(window);

$(document).ready(function() {
  jsTicket= new Janal.Control.Ticket.Core();
});			
			
