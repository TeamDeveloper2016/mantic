/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 07/06/2018
 *time 09:39:15 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
	var jsCalculator;
	
	Janal.Control.Calculadora= {};
	
	Janal.Control.Calculadora.Core= Class.extend({
		VK_ENTER    : 13, 
		VK_MINUS    : 109,
		VK_REST     : 189,
		VK_PLUS     : 107,
		VK_DIV      : 111,
		VK_ASTERISK : 106,
		VK_CLEAN    : 67, // c
		VK_MOVE     : 73, // i
		result      : '',
		last        : '',
		init: function() { // constructor
			$calculadora= this;
	    $(document).on('keydown', '.janal-key-calculator', function(e) {
				var key   = e.keyCode ? e.keyCode : e.which;
				janal.console('Keydown: '+ key);
				switch(key) {
					case $calculadora.VK_ENTER:
						return $calculadora.calculate(this, '=');
						break;
					case $calculadora.VK_MINUS:
					case $calculadora.VK_REST:
						return $calculadora.calculate(this, '-');
						break;
					case $calculadora.VK_PLUS:
						return $calculadora.calculate(this, '+');
						break;
					case $calculadora.VK_DIV:
						return $calculadora.calculate(this, '/');
						break;
					case $calculadora.VK_ASTERISK:
						return $calculadora.calculate(this, '*');
						break;
					case $calculadora.VK_CLEAN:
						return $calculadora.clean();
						break;
					case $calculadora.VK_MOVE:
						return $calculadora.move();
						break;
				} // swtich
			});	
		},
		clean: function() {
			$('#results').val(this.last+ '=\n');
			return false;
		},
		move: function() {
			janal.console('jsCalculator.move: '+ this.last);
			// janal.valueLastFocus(this.last);
			if($(janal.lastNameFocus) && !$(this.lastNameFocus).is('[readonly="readonly"]')) {
				$(janal.lastNameFocus).val(this.last);
				$(janal.lastNameFocus).addClass('add-value-calculator');
			} // if
			else
				janal.console('jsCalculator.move: '+ janal.lastNameFocus+ ' esta de solo lectura, no se puede pasar el valor ! ');
			PF('dlgCalculadora').hide();
			return false;
		},
		calculate: function(element, operator) {
			var number = {value: '0'};
			var concat = false;
			var comodin= $(element).val().trim().endsWith('%');
			if($(element).val().trim().length> 0)
			  number= janal.precio(element, $(element).val().trim(), 4);
			if(this.result.length=== 0)
				this.result+= number.value;
			else {
				if(isNaN(this.result)) {
					if(this.result.endsWith('+') || this.result.endsWith('-') || this.result.endsWith('*') || this.result.endsWith('/')) {
						if($(element).val().trim().length<= 0) {
   						number.value= this.result.substring(0, this.result.length- 1);
							this.result = '';  
						} // if	
					} // if	
				} // if
				else
					if(operator!== '=')
						if($(element).val().trim().length<= 0) {
						  number.value= this.result;
				      this.result= '';	
					  } // if	
				    else
						  this.result= '';
					else
						if($(element).val().trim().length<= 0)
					    number.value= this.result;
				janal.console('jsCalculator.calculate: '+ this.result+ ' number: '+ number.value);
				if(operator=== '=' && !(this.result.endsWith('+') || this.result.endsWith('-') || this.result.endsWith('*') || this.result.endsWith('/') || this.result.endsWith('%')))
					this.result= parseFloat(number.value, 10);
				else {
					janal.console('jsCalculator.calculate: '+ this.result+ number.value);
					if(comodin) {
						var value= this.result.substring(0, this.result.length- 1);
						var op   = this.result.substring(this.result.length- 1, this.result.length);
  					janal.console('jsCalculator.calculate: [('+ value+ '/100)* (100'+ op+ number.value+ ')]');
					  this.result= eval('('+ value+ '/100)* (100'+ op+ number.value+ ')');	
						number.value= number.value+ '%';
					} // if
					else
			      this.result= eval(this.result+ number.value);
					concat= true;
				} // else
				this.result= this.result.toFixed(4);
			} // else
			this.last= this.result;
			if(operator!== '=')
				this.result+= operator;
			$(element).val('');
			$('#results').val($('#results').val()+ (concat? number.value+  (operator=== '='? operator: '')+ '\n'+ this.result+ '': this.result+ (operator=== '='? operator: ''))+ '\n');
			$('#results').scrollTop($('#results')[0].scrollHeight);
			$(element).focus();
			return false;
		}
	});
	
	console.info('Iktan.Control.Calculadora initialized');
})(window);			

$(document).ready(function() {
  jsCalculator= new Janal.Control.Calculadora.Core();
});				
