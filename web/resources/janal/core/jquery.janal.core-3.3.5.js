/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 11/06/2014
 *time 06:17:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
(function(window) {
  var janal;
  var currentEvent= $.Event('onJanal');
  window.isMobile= {
    Android: function() {
      return navigator.userAgent.match(/Android/i)!== null? true: false;
    },
    BlackBerry: function() {
      return navigator.userAgent.match(/BlackBerry/i)!== null? true: false;
    },
    iOS: function() {
      return navigator.userAgent.match(/iPhone|iPad|iPod/i)!== null? true: false;
    },
    Opera: function() {
      return navigator.userAgent.match(/Opera Mini/i)!== null? true: false;
    },
    Windows: function() {
      return navigator.userAgent.match(/IEMobile/i)!== null? true: false;
    },
    any: function() {
      return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    }
  };    
  
  if(window.Janal) {
    Console.debug("Janal already loaded, ignoring duplicate execution.");
    return;
  }

  /*
   * Defined namespace for Janal
   */
  Janal= {};
  Janal.Control= {
    name: [],
    stage: [100,101,115,97,114,114,111,108,108,111],
    form: 'datos', 
    display: 'inline',
    growl: 'mensajes',
    lock: 'bloqueo',
    errors: 3
  };
  
  /*
   * Defined functions core for Janal
   */
  Janal.Control.Functions= Class.extend({
  	daysInMonth : [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
    root        : [47,73,75,84,65,78],
    logger      : false,
    stage       : 'desarrollo',
		source      : '/resources/janal/', /* /janal/ inside of jar file */ /* '/resources/janal/ inside of webapp */
		decimals    : 2,
    init: function(root) {
      $control= this;
      $control.console('Janal.Control.Function.init');
      if(typeof(root)!== 'undefined')
        this.root   = root;
      $control.load(0, ['/resources/janal/core/jquery.shortcut.core.0.3.8.js','/resources/janal/core/jquery.janal.sticky.min-1.0.0.js','/resources/janal/js/jquery.janal.menu-2.0.1.js','/resources/janal/core/jquery.longclick-1.0.0.js', '/resources/janal/core/jquery.validate.min-1.15.0.js', '/resources/janal/core/jquery.meio.mask.min-1.1.15.js', '/resources/janal/core/jquery.janal.fns-1.4.5.js']);
      $control.console('Janal.Control.Function.init resource loaded');
    },
    dateFormat: function(format) {
      var date= new Date();
      var yyyy = date.getFullYear().toString();
      format = format.replace(/yyyy/g, yyyy);
      var mm = (date.getMonth()+1).toString(); 
      format = format.replace(/mm/g, (mm[1]?mm:"0"+mm[0]));
      var dd  = date.getDate().toString();
      format = format.replace(/dd/g, (dd[1]?dd:"0"+dd[0]));
      var hh = date.getHours().toString();
      format = format.replace(/hh/g, (hh[1]?hh:"0"+hh[0]));
      var ii = date.getMinutes().toString();
      format = format.replace(/ii/g, (ii[1]?ii:"0"+ii[0]));
      var ss  = date.getSeconds().toString();
      format = format.replace(/ss/g, (ss[1]?ss:"0"+ss[0]));
      return format;
    }, // dateFormat
    console: function(msg) {
      if("produccion"!== $control.toUnicodeString($control.stage) || $control.logger)
        console.log('INFO '+ this.dateFormat('yyyy-mm-dd hh:ii:ss')+ ': janal.console: '+ msg);
    }, // console
    cache: function(url, options, count, items) {
      // Allow user to set any option except for dataType, cache, and url
      options = $.extend(options || {}, {
        dataType: "script",
        cache: false,
        async: false,
        url  : url,
        complete: function(jqXHR, textStatus) {
          if(count< items.length) 
            $control.load(count, items);
        }
      });
      // Use $.ajax() since it is more flexible than $.getScript
      // Return the jqXHR object so we can chain callbacks
      return $.ajax(options);      
    }, // cache
    load: function(count, items) {  
      var url= items[count]; 
      $control.cache(this.toContext()+ url, {}, count+ 1, items)
      .done(function(script, status) {
         $control.console(url+ ' loaded');
      }) // done
      .fail(function(jqxhr, settings, exception) {
        $control.console(url+ ' load error '+ exception);
        alert('Desarrollador:\n    Fallo la carga de las librerias de Janal \n    revisar por favor la consola del navegador\n    y notifique a su jefe del error.!\n'+ url+ '\n\n     Error: '+ exception);
      }); // fail
    }, // load
    loadJS: function(items) {  
      $control.load(0, items);
    },
    empty: function(value) {
      return value=== null || $.trim(value).length=== 0;
    }, // empty
    vector: function(data, separators) {
      return data.split(new RegExp(separators.join('|'), 'g'));
    }, // vector
    remove: function(value, characters) {
      return value.replace(new RegExp(characters.join('|'), 'g'), '');
    }, // remove
    cleanToken: function(value) {
      var separators= ['\\\ ', '\\\,', '\\\$', '\\\(', '\\\)'];
      return this.remove(value, separators);
    }, // cleanToken
    isNonnegativeInteger: function(s) {
      var args = false;
      if(arguments.length > 1)
        args= arguments[1];
      return(this.isSignedInteger(s, args) && ((this.empty(s) && args)  ||(parseInt(s, 10)>= 0)));
    }, // isNonnegativeInteger
    isSignedInteger: function(s) {
      if(this.empty(s))
        if(arguments.length === 1)
          return false;
        else
          return(arguments[1] === true);
      else {
        var start= 0;
        var args = false;
        if(arguments.length > 1)
          args = arguments[1];
        if((s.charAt(0) === "-") ||(s.charAt(0) === "+"))
          start = 1;
        return(this.isInteger(s.substring(start, s.length), args));
      }
    }, // isSignedInteger
    isInteger: function(s) {
      if(this.empty(s))
        if(arguments.length === 1)
          return false;
        else
          return(arguments[1] === true);
      for(var i= 0; i< s.length; i++) {
        var c= s.charAt(i);
        if(!this.isDigit(c))
          return false;
      } // for
      return true;
    }, // isInteger
    isDouble: function(s) {
      if(this.empty(s))
        if(arguments.length === 1)
          return false;
        else
          return(arguments[1] === true);
		  var count= 0;
      for(var i= 0; i< s.length; i++) {
        var c= s.charAt(i);
        if(!this.isDigit(c))
          return false;
				else
					if(!(c==='.' && count=== 0))
						count++;
				  else
						return false;
      } // for
      return true;
    }, // isDouble
    isDigit: function(value) {
      return (value >= 0 && value <= 9);
    }, // isDigit
    isIntegerInRange: function(s, a, b) {
      if(this.empty(s))
        if(arguments.length === 1)
          return true;
        else
          return(arguments[1] === true);
       if(!this.isInteger(s, false))
         return false;
       return((parseInt(s, 10) >= a) &&(parseInt(s, 10) <= b));
    }, // isIntegerInRange
    isYear: function(s) {
       if(this.empty(s))
         if(arguments.length === 1)
           return false;
         else
           return(arguments[1] === true);
       if(!this.isNonnegativeInteger(s))
         return false;
       return((s.length=== 2) ||(s.length === 4));
     }, // isYear
     isMonth: function(s) {
       if(this.empty(s))
         if(arguments.length === 1)
           return false;
       else
         return(arguments[1] === true);
       return this.isIntegerInRange(s, 1, 12);
     }, // isMonth
     isDay: function(s) {
       if(this.empty(s))
         if(arguments.length === 1)
           return false;
         else
           return(arguments[1] === true);
       return this.isIntegerInRange(s, 1, 31);
     }, // isDay
     februaryDays: function(year) {
       return(((year % 4 === 0) &&((!(year % 100 === 0)) ||(year % 400 === 0)))? 29: 28);
     }, // februaryDays
     isDate: function(year, month, day) {
       if(!(this.isYear(year, false) && this.isMonth(month, false) && this.isDay(day, false)))
         return false;
       var $year = parseInt(year, 10);
       var $month= parseInt(month, 10);
       var $day  = parseInt(day, 10);
       if($day> this.daysInMonth[$month- 1]) {
         if(!($month=== 2 && $day> this.februaryDays($year)))
           return true;
         else   
           return false;
       } // if
       return true;
     }, // isDate
     isCustomDate: function(s) {
       var patron = /^([\d]{2})\/([\d]{2})\/([\d]{4})$/;
       return patron.test(s) && this.isDate(s.substring(6), s.substring(3,5), s.substring(0,2));
     }, // isCustomDate
     isHour: function(hours, minutes, seconds) {  
		   if(hours>= 0 && hours <= 23) {
		     if(minutes>= 0 && minutes <= 59)
		       if(seconds>= 0 && seconds <= 59)
		         return true;
		     else
		       return false;
		   } // if
		   else
		     return false;
		 }, // isHour
     isCustomHour: function(s) {
       return this.isHour(parseInt(s.substring(0,2), 10), parseInt(s.substring(3,5), 10), parseInt(s.substring(6,8), 10));
     }, // isCustomHour
	   checksDates: function(start, end) {
	     return (this.reverseDate(start)<= this.reverseDate(end));
	   }, // checksDates
	   reverseDate: function(value) {
	     return value.substr(6, 4)+ value.substr(3, 2)+ value.substr(0, 2);
     }, // reverseDate
     build: function(kind, id, father, css) {
       var value= $('<'+ kind.toLowerCase()+ '>');
       if (!this.empty(id))
         value.attr('id', id);				    
       $.each(css.split(","), function() {
         value.addClass(String(this));
       });
       return value.appendTo(father);
     }, // build
     toUnicodeString: function(array) {
       if("string"===typeof(array))
         return array;
       else {
         var result = '';
         for (var x= 0; x< array.length; x++) 
           result+= String.fromCharCode(array[x]);
         return result;
       } // else  
     }, // toUnicodeString
     toContext: function() {
       return this.toUnicodeString(this.root);
     }, // toContext
     labels: function(id) {
       return $('label[for$="'+ id+ '"], label[for$="'+ id+ $janal.SELECT_FOCUS+ '"], label[for$="'+ id+ $janal.INPUT_RESERVE+ '"], th.'+ (id.indexOf(':')> 0? id.substring(id.indexOf(':')+ 1): id)+ '>span, a.'+ id);
     }, // labels
     selector: function(multiple, id) {
       return 'input[id'+ multiple+ '="'+ id+ '"], input[id'+ multiple+ '="'+ id+ $janal.INPUT_RESERVE+ '"], select[id'+ multiple+ '="'+ id+ $janal.INPUT_RESERVE+ '"], textarea[id'+ multiple+ '="'+ id+ '"], input[id^="'+ id+ ':"], input[id*=":'+ id+ ':"], input[id'+ multiple+ '=":'+ id+ '"]';       
     },
     components: function(multiple, id) {
       return $(this.selector(multiple, id));
     }, // components
     inputs: function(multiple, id) {
       return $('input[id'+ multiple+ '="'+ id+ '"], input[id'+ multiple+ '="'+ id+ $janal.INPUT_RESERVE+ '"]');
     }, // inputs
     search: function(id) {
       return this.components('$', id);
     }, // search
     tabView: function(id) {
       return $('input[id$="'+ id+ '_activeIndex"]').val();
     }, // tabView
		 capitalLetterTable: function(content, parent, index, name) {				
		 	 var mayusculas= $('#'+ content+ '\\:'+ parent+'\\:'+ index+'\\:'+ name).val().toUpperCase();
			 $('#'+ content+ '\\:'+ parent+'\\:'+ index+'\\:'+ name).val(mayusculas);
	   }, // capitalLetterTable
		 mayusculas: function(id) {
				var input= $('#'+ id);
				input.val(input.val().toUpperCase());
		 },
     cross: function(source, target) {
				var result= '';
				if(target.indexOf('?')>= 0) {
					var one= source.split(":");
					var two= target.split("\\:");
					$.each(two, function(index, item) {
						if(item=== '?')
							result+= one[index]+ (index< two.length- 1? '\\:': '');
						else
							result+= item+ (index< two.length- 1? '\\:': '');
					});
					$janal.console('janal.cross: source ['+ source+ '] target: ['+ target+ '] result: '+ result);
				} // if
				else
					result= target;
				return result;
		 },		 
     value: function(id) {
       var values= '';
       var items= $('input[id$="'+ id+ '"], input[id$="'+ id+ $janal.INPUT_RESERVE+ '"], select[id$="'+ id+ $janal.INPUT_RESERVE+ '"] option:selected, textarea[id$="'+ id+ '"], input[id^="'+ id+ ':"]:checked, input[id*=":'+ id+ ':"]:checked, input[id$="'+ id+ ':"]:checked');
       $.each(items, function(idx) {
         values+= $(this).val()+ (items.length-1!== idx? '|': '');
       });
       return values;
     }, // value
     text: function(id) {
       return this.search(id).text();
     }, // text
		 descuentos: function(element, value) {
				var regresar= 0.0;
				var val     = $(element)? $(element).val().trim(): value;
				var text    = ''; 
				var values  = '';
				if(val=== 'undefined' || val.length=== 0)
					$(element).val(value);
				else {
				  var items= val.split(',');
					for (item in items) {
						if(Number.isNaN(parseFloat(items[item], 10))) {
							text+= items[item]+ ', ';
							items[item]= '0.00';
						}	// if
						else {
							items[item]= parseFloat(items[item], 10);
							regresar+= items[item];
							values+= items[item]+ ', ';
						} // else	
					} // for
					if(regresar> 99)
						$(element).val('0.00');
					else	
					  $(element).val(values.length=== 0? '0.00': values.substring(0, values.length- 2));
				} // else	
				if(text.trim().length> 0)
					text= text.substring(0, text.length- 2);
				return {"suma": regresar.toFixed(this.decimals), "text": text, "error": text.length> 0};
		 },
		 cantidad: function(element, value) {
				var val= $(element)? $(element).val().trim(): value;
				if(val=== 'undefined' || val.length=== 0) {
					$(element).val(value);
				} // if	
				else {
					if(Number.isNaN(parseInt(val, 10)) || parseInt(val, 10)< parseInt(value, 10))
						$(element).val(value);
					else
						$(element).val(parseInt(val, 10));
				} // else
				return {"value": $(element).val(), "error": false};	
		 },
		 cantidadGarantia: function(element, value) {
				var val= $(element)? $(element).val().trim(): value;
				if(val=== 'undefined') {
					$(element).val(value);
				} // if	
				else {
					if(Number.isNaN(parseInt(val, 10)) || parseInt(val, 10)< parseInt(value, 10))
						$(element).val(value);
					else
						$(element).val(parseInt(val, 10));
				} // else
				return {"value": $(element).val(), "error": false};	
		 },
		 precio: function(element, value, decimal) {
				var val= $(element)? $(element).val().trim(): value;
				if(typeof(decimal)=== 'undefined')
					decimal= this.decimals;
				if(val=== 'undefined' || val.length=== 0) {
					$(element).val(parseFloat(value).toFixed(decimal));
				} // if	
				else {
					if(Number.isNaN(parseFloat(val, 10)) || parseFloat(val, 10)< parseFloat(value, 10))
						$(element).val(parseFloat(value).toFixed(decimal));
					else
						$(element).val(parseFloat(val, 10).toFixed(decimal));
				} // else
			  return {"value": $(element).val(), "error": false};	
		 },
     fecha: function() {
       var meses= new Array ("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre");
       var data = new Date();
       var index= data.getMonth();
       var an   = data.getYear();
       var Hora;
       var Minutos;
       var Segundos;
       var AmPm;
       if (an < 1000)
         an = 1900+ an;
       Hora= data.getHours();
       AmPm= " a.m.";
       if(Hora>= 12) 
         AmPm = " p.m.";
       if (Hora > 12)
         Hora -= 12;
       if (Hora === 0)
         Hora = 12;
       Minutos = data.getMinutes();
       if(Minutos< 10) 
         Minutos = "0" + Minutos;
       Segundos= data.getSeconds();
       if(Segundos< 10)
         Segundos = "0" + Segundos;
       var texto="Hoy es "+ data.getDate()+ " de " + meses[index] +" de " + an+ "   	"+ Hora+ ":"+ Minutos+ ":"+ Segundos+ " "+ AmPm;
       $('#reloj').text( texto ).addClass("fecha	");  
       if (document.layers){
         document.layers.reloj.document.write(texto);
         document.layers.reloj.document.close();
       } // if
       else 
         if (document.all) 
           reloj.innerHTML = texto;
       setTimeout('janal.fecha();', 1500);
     }, // fecha     
     monitor:	function (value) {
      var count   = 11;
      var contador= count;	
      if(value === 0)
        clearInterval(interval);										
      else {																							
        segundos.innerHTML = "Tiempo restante: 00:00:10";
        interval = setInterval(function() {																		
          contador--;									
          if(contador> -1) {
            count= contador+ 1;
            if(contador >= 10) 
              segundos.innerHTML = "Tiempo restante: 00:00:".concat(contador.toString());				
            else 
              segundos.innerHTML = "Tiempo restante: 00:00:0".concat(contador.toString());				
          } // if
          else {						
            clearInterval(interval);
            PF('dialogoInactivo').hide();
          } // else
        }, 1000);							
      } // else
	  } // monitor
  });
  
  /*
   * Defined validation for Janal
   */
  Janal.Control.Validations= Janal.Control.Functions.extend({
    // privates atributtes for class
    form          : 'datos',
    fields        : {},
		backup        : {},
    kind          : 'inline',
    message       : 'mensajes',
    stage         : 'desarrollo',
    locked        : 'bloqueo',
    INPUT_RESERVE : '_input',
    TABLE_RESERVE : '_selection',
    SELECT_FOCUS  : '_focus',
    JANAL_RESERVE : '\u0049\u004B\u0054\u0041\u004E',
    initialized   : false,
    offContextMenu: true,
		lastNameFocus : null,
		idNameFocus   : null,
		reference     : null,
		lastReference : null,
		globalTimeout : true,
		cleanString   : /[(|)|'|*|!||<|>|?|¿|!|&|%|$|#|;|:|{|}|\[|\]|~|\\]/g,
  	errors        : {
      inputs      : [],
      masks       : [],
      validations : [],
      customs     : [],
      show        : 0
    },
    names         : {
      validations : ['libre', 'max-caracteres', 'min-caracteres', 'igual-caracteres', 'mayor', 'mayor-igual', 'max-valor', 'menor', 'menor-igual', 'min-valor', 'requerido', 'entero', 'entero-signo', 'valor-simple', 'telefono', 'contiene-a', 'igual-a', 'menor-a', 'mayor-a', 'asterisco', 'moneda', 'moneda-decimal', 'sat', 'flotante', 'flotante-signo', 'mayusculas', 'minusculas', 'vocales', 'rango', 'secuencia-palabra', 'longitud', 'letras', 'texto', 'curp', 'rfc', 'moral', 'texto-especial', 'boleano', 'fecha', 'fecha-menor', 'fecha-mayor', 'registro', 'hora', 'hora-completa', 'hora-mayor', 'hora-menor', 'comodin', 'no-permitir', 'ipv4', 'ipv6', 'no-aplica', 'esta-en', 'correo', 'acceso', 'porcentaje'],
      masks       : ['libre', 'fecha', 'fecha-hora', 'registro', 'hora', 'hora-completa', 'tarjeta-credito', 'decimal', 'decimal-signo', 'letras', 'vocales', 'texto', 'numero', 'un-digito', 'dos-digitos', 'tres-digitos', 'tres-digitos-default', 'cuatro-digitos', 'cinco-digitos', 'siete-digitos', 'ocho-digitos', 'nueve-digitos', 'diez-digitos', 'entero', 'entero-blanco', 'entero-signo', 'entero-sin-signo', 'sat', 'flotante', 'flotante-signo', 'rfc', 'fiscal', 'moral', 'curp', 'moneda', 'moneda-decimal', 'mayusculas', 'minusculas', 'cuenta', 'numeros-letras', 'nombre-dto', 'telefono', 'ip', 'version', 'no-aplica','correo', 'valor-simple', 'acceso', 'alfanumerico', 'codigo'],
      watermarks  : ['entero', 'entero-signo', 'valor-simple', 'decimal', 'decimal-signo', 'sat', 'flotante', 'flotante-signo', 'moneda', 'moneda-decimal', 'mayor', 'mayor-igual', 'mayor-a', 'max-valor', 'menor', 'menor-a', 'menor-igual', 'min-valor'],
      formats     : ['libre', 'cambiar-mayusculas', 'especial-mayusculas', 'cambiar-minusculas', 'rellenar-caracter', 'precio', 'cantidad', 'consecutivo', 'descuentos', 'sat'],
      customs     : []
    },
    // methods publics for class
    init: function(root, form, fields, kind, stage, growl, lock, showMaxError) {
      this._super(root, stage);
      this.console('janal.init');
      $janal        = this;
  		$janal.backup = fields;
      $janal.kind   = kind;
      $janal.stage  = stage;
      $janal.message= growl;
      $janal.lock   = lock;
      $janal.offContextMenu= "desarrollo"!==$janal.toUnicodeString(stage);
      $janal.lockContextMenu($janal.offContextMenu);
      $janal.overrideContextMenu();
      $janal.prepare(form, fields, showMaxError);  
      $janal.ready();
			$(document).on('focus', 'input, select, textarea', function(e) {
				if(!$(this).hasClass('janal-not-focus') && ($(this).attr('readonly')=== 'false' || $(this).attr('disabled')=== 'false' || !$(this).attr('readonly') || !$(this).attr('disabled'))) {
  				$janal.console('janal.focus: '+ $(this).attr('id'));
				  $janal.lastNameFocus= this;
				} // if	
			});
    }, // init
    prepare: function(form, fields, showMaxError) {
      $janal.console('janal.prepare');
      if (typeof(form)!== 'undefined')
        this.form= form;
      if (typeof(fields)!== 'undefined') {
        this.fields= fields;  
			};
      if ((typeof(showMaxError)!== 'undefined') && ($janal.isNonnegativeInteger(''+showMaxError)))
        this.errors.show= showMaxError;
      $form= $('#'+ $janal.form).validate({	ignore: "ignore", onkeyup: false, onfocusout: false, focusInvalid: true, onsubmit: false, 
        errorPlacement: function(error, element) {
          $janal.format(error.text(), element.is(':checkbox')||element.is(':radio')? element.attr('name'): element.attr('id'), $janal.errors.inputs);
        }
      });      
      if(typeof($janal.lazy)!== 'undefined')
        $janal.lazy($form);
      $janal.reset(false);
      this.addTabIndex();
      this.initialized= true;
    }, // startUp
    display: function(kind) {
      if (typeof(kind)!== 'undefined' && 'growl|inline|classic'.indexOf(kind)>= 0)
        $janal.kind= kind;
    }, // display
    add: function(name, method, message) {
      var tokens= name;
      if(name.constructor!== Array)
        tokens= $janal.vector(name, ['\\\|', '\\\ ', '\\\,']);
      $.each(tokens, function() {
        if($janal.validate(String(this), $janal.names.validations.concat($janal.names.customs)))
          $janal.format('Ya existe la validaci\u00F3n ['+ String(this)+ '], cambie el nombre.', 'Personalizada', $janal.errors.validations);
      }); // each
      if($janal.errors.validations.length> 0)
        $janal.programmer($janal.errors.validations);
      else {
        $janal.names.customs= $janal.names.customs.concat(tokens);
        $.validator.addMethod(name, method, message);
      } // else  
      $janal.errors.validations= [];
    }, // add
    format: function(error, id, items) {
      //$janal.console('janal.format: '+ id+ ': '+ String(error));
			var name= id.indexOf(':')> 0? id.substring(id.lastIndexOf(':')+ 1): id;
					name=	name.indexOf($janal.INPUT_RESERVE)> 0? name.substring(0, name.lastIndexOf($janal.INPUT_RESERVE)): name;
			if(!$('#'+ name).hasClass('janal-input-error'))
			  $('#'+ name).addClass('janal-input-error');
      var titles= $janal.labels(name);
      if(titles.length=== 0)
        items.push({summary: 'Informativo:', detail: error, severity: 'error'});		  
      else
        $.each(titles, function() {
          if(!$(this).hasClass('janal-field-error')) {
            $(this).addClass('janal-field-error');
            items.push({summary: $(this).text().indexOf(':')> 0? $(this).text(): $(this).text()+ ':', detail: ' '+ error, severity: 'error'});		  
          } // if
        });
    }, // format
    blank: function(id, value) {
      if(typeof(value.multiple)==='undefined') {
        if(typeof($janal.fields[id].multiple)==='undefined')
          $janal.fields[id].multiple= '$';
      } // if
      else
        $janal.fields[id].multiple= value.multiple;
      if(typeof(value.mensaje)==='undefined') {
        if(typeof($janal.fields[id].mensaje)==='undefined')
          $janal.fields[id].mensaje= '';
      } // if
      else
        $janal.fields[id].mensaje= value.mensaje;
      if(typeof(value.grupo)==='undefined') {
        if(typeof($janal.fields[id].grupo)==='undefined')
          $janal.fields[id].grupo= $janal.JANAL_RESERVE;
      } // if
      else
        $janal.fields[id].grupo= value.grupo;
      if(typeof(value.mascara)==='undefined') {
        if(typeof($janal.fields[id].mascara)==='undefined')
          $janal.fields[id].mascara= 'libre';
      } // if 
      else
        $janal.fields[id].mascara= value.mascara;
      if(typeof(value.validaciones)==='undefined') {
        if(typeof($janal.fields[id].validaciones)==='undefined')
          $janal.fields[id].validaciones= 'libre';
      } // if 
      else
        $janal.fields[id].validaciones= value.validaciones;
      if(typeof(value.formatos)==='undefined') {
        if(typeof($janal.fields[id].formatos)==='undefined')
          $janal.fields[id].formatos= 'libre';
      } // if 
      else
        $janal.fields[id].formatos= value.formatos;
      if(typeof(value.individual)==='undefined') {
        if(typeof($janal.fields[id].individual)==='undefined')
          $janal.fields[id].individual= false;
      } // if 
      else
        $janal.fields[id].individual= value.individual;
    },
    cleanForm: function() {
      $janal.console('janal.cleanForm');
      $.each($janal.fields, function(id, value) {
        var $components= $janal.components(value.multiple, id);
        $.each($components, function() {
          $(this).val("");
        });
      }); // each
    }, // cleanForm
    data: function(id, value) {
      $janal.hide();
      var $components= $janal.components(value.multiple, id);
      $janal.console('janal.data: '+ id+ ' encontrados: '+ $components.length);
      $.each($components, function() {
        $janal.console('janal.data: '+ id+ ' formatos: '+ value.formatos+ '  individual: '+ value.individual);
        $janal.mask(id, $(this), value.mascara);
        $janal.required(id, value.validaciones, true);
        if(value.formatos!== 'libre' || value.individual || value.formatos!== 'libre')
          $(this).on('blur', function() {
            $janal.reference= this;
            $janal.element(false, id);
          });
      });
      $janal.programmer($janal.errors.masks);
    },
    reset: function(clean) {
      $janal.console('janal.reset');
			if(clean)
        $janal.clean();
      $janal.errors.masks= [];
      $.each($janal.fields, function(id, value) {
        $janal.blank(id, value);
        $janal.data(id, value);
      }); // each
    }, // reset
    refresh: function(items) {
      $janal.console('janal.refresh');
      $janal.errors.masks= [];
      if (typeof(items)=== "string")
        items= $janal.vector(items, ['\\\|', '\\\,', '\\\ ']);
      $.each($janal.fields, function(id, value) {
        if(typeof(items)=== 'undefined' || items.length=== 0 || items.indexOf(id)>= 0 || items.indexOf(value.grupo)>= 0) 
          $janal.data(id, value);
      }); // each
    }, // refresh
    renovate: function(id, value) {
      $janal.console('janal.renovate');
      if($janal.fields[id]) {
        $janal.blank(id, value);
        $janal.refresh(id);
      } // if  
      else
        $janal.show([{summary: 'No existe el ID', detail: 'El elemento llamado['+ id+ '] no existe !!!'}]);
    }, // refresh
    whatIsIt: function(object) {
      var stringConstructor = "janal".constructor;
      var arrayConstructor = [].constructor;
      var objectConstructor = {}.constructor;
        if (object === null)
            return 'null';
        else 
          if (object === undefined) 
            return 'undefined';
          else 
            if (object.constructor === stringConstructor) 
              return 'String';
           else 
             if (object.constructor === arrayConstructor) 
               return 'Array';
             else 
               if (object.constructor === objectConstructor) 
                 return 'Object';
               else
                 return 'don t know';
    }, // whatIsIt
    mask: function(id, component, mask) {
      if($janal.whatIsIt(mask)=== 'String' && mask.indexOf('{')=== 0) {
        try {
          component.setMask($.parseJSON(mask));
        } // try
        catch(e) {
          $janal.format('Los parametros de la mascara ['+ mask+ '] son incorrectos ', id, $janal.errors.masks);
        } // catch
      } // if
      else
        if($janal.whatIsIt(mask)=== 'Object') {
          try {
            component.setMask(mask);
          } // try
          catch(e) {
            $janal.format('Los parametros de la mascara ['+ mask+ '] son incorrectos ', id, $janal.errors.masks);
          } // catch
        } // if
        else
          if($janal.validate(mask, $janal.names.masks)) {
            if(mask!== 'no-aplica') {
              component.attr('alt', mask);
              component.setMask();
            } // if  
          } // if  
          else
            $janal.format('No existe la mascara ['+ mask+ ']', id, $janal.errors.masks);
    }, // mask     
    cut: function(name) {
      return $.trim(name.indexOf('(')>= 0? name.substring(0, name.indexOf('(')): name);
    }, // cut
    complete: function(name) {
      return $.trim(name.indexOf('(')>= 0? name: name+ '({"default": "'+ $janal.JANAL_RESERVE+ '"})');
    }, // complete
    // Converts a simple string to a {string: true} rule, e.g., "required" to {required:true}
    normalize: function(id, component, data, message) {
      if (typeof data === "string") {
        var tokens= $janal.vector(data, ['\\\(', '\\\)']);
        var $json = {messages: {}};
        try {
          $json[tokens[0]]= $.parseJSON(tokens[1]);
        } // try
        catch(e) {
          $janal.format('Los parametros de la validaci\u00F3n ['+ tokens[0]+ '] son incorrectos '+ tokens[1], id, $janal.errors.validations);
        } // catch
        if(!$janal.empty(message))
          $json.messages[tokens[0]]= message;
        data = $json;
      } // if
      return data;
    }, // normalize
    rules: function(id, component, methods, message) {
      $.each(methods, function() {
        var method= $janal.cut(this);
        if($janal.validate(method, $janal.names.validations.concat($janal.names.formats).concat($janal.names.customs))) {
          if(method!== 'no-aplica')
            component.rules('add', $janal.normalize(id, component, $janal.complete(String(this)), message));
        } // if  
        else
          $janal.format('No existe la validaci\u00F3n ['+ method+ ']', id, $janal.errors.validations);
      }); // each
    }, // rules
    required: function(id, method, colocate) {
      if(method.indexOf('requerido')>= 0) {
        var titles= $janal.labels(id);
        $.each(titles, function() {
          if(colocate)
            $(this).text(($(this).text().indexOf('*')< 0? '*': '')+ $(this).text());
          else
            $(this).text($(this).text().indexOf('*')>= 0? $(this).text().substring(1): $(this).text());
        });
      } // if
    }, // required
    validate: function(value, names) {
      return names.indexOf(value)>= 0;
    }, // validate
    hide: function() {
      $('input, select, textarea').removeClass('janal-input-error');
      $('label, th>span').removeClass('janal-field-error');
      $('#growl_container').remove();
      if(typeof(PF($janal.message))!== 'undefined')
        PF($janal.message).removeAll();
      $janal.errors.inputs= [];
    }, // hide
    clean: function() {
      $janal.console('janal.clean');
      $.each($janal.fields, function(id, value) {
        $janal.blank(id, value);
        var $components= $janal.components(value.multiple, id);
        $.each($components, function() {
          $(this).unsetMask();
          //$(this).css('text-align', 'left');
          $(this).rules('remove');
          $janal.required(id, value.validaciones, false);
        });
      }); // each
      $janal.hide();
    }, // clean
    isWaterMark: function(methods) {
			var count= 0;
      $.each(methods, function() {
        if($janal.names.watermarks.indexOf($janal.cut(this))>= 0)
          count++;
      });
      return count> 0;
    }, // isWaterMark
    cleanMarks: function() {
      $.each($janal.fields, function(id, value) {
        if(typeof(value.multiple)==='undefined')
          value.multiple= '$';
        var $components= $janal.inputs(value.multiple, id);
        $.each($components, function() {
          if($janal.isWaterMark($janal.vector(value.validaciones, ['\\\|'])))
            $(this).val($janal.cleanToken($(this).val()));
        });
      }); // each
    }, // cleanMarks
    show: function(items, type) {
      switch($janal.kind) {
        case 'classic':
          $janal.classic(items, type);
          break;
        case 'inline':
          $janal.inline(items, type);
          break;
        case 'growl':
          $janal.growl(items, type);
          break;
      }; // switch
    }, // show
    classic: function(items) {
      if(typeof(PF($janal.message))!== 'undefined') {
        PF($janal.message).removeAll();			
				$.each($janal.errors.customs, function() {
          if(typeof(this.id)!== 'undefined')
	  				$janal.addClassError($janal.vector(this.id, ['\\\|']));
				}); // customs
				var all= items.concat($janal.errors.customs);
				if(($janal.errors.show> 0) && (all.length> $janal.errors.show)) {
					all= all.slice(0, $janal.errors.show);
					all.push({severity: 'Informativo', summary: 'Total de errores:', detail: items.concat($janal.errors.customs).length});
				} // if
        PF($janal.message).show(all);
      } // if  
    }, // classic
    growl: function(items, type) {
			if(typeof(type)==='undefined')
				type= 'error';
      var container= $janal.build('div', 'growl_container', $('#'+ $janal.form), 'ui-growl, ui-widget, ui-growl-'+ type);
      container= $janal.build('div', '', container, 'ui-growl-item-container, ui-state-highlight, ui-corner-all, ui-helper-hidden, ui-shadow, ui-growl-'+ type+ '-container');
      container.css({'display': 'block'});
      container= $janal.build('div', '', container, 'ui-growl-item, ui-growl-'+ type+ '-item');
      $('<a href="#" onclick="$(this).parent().parent().slideUp();" style="float:right;cursor:pointer"><span class="ui-icon ui-growl-'+ type+ '-icon-close"></span></a>').appendTo(container);
      $janal.build('span', '', container, 'ui-growl-image, ui-growl-'+ type+ '-image, ui-growl-image-error');
      $janal.detail(items, container);
    }, // growl
    inline: function(items, type) {
			if(typeof(type)==='undefined')
				type= 'error';
      var container= $janal.build('div', 'growl_container', $('body'), 'ui-messages, ui-widget, ui-messages-'+ type);
      container.insertBefore($('#'+ $janal.form));
      container= $janal.build('div', '', container, 'ui-corner-all, ui-'+ type+ '-messages');
      $('<a href="#" class="ui-messages-close" onclick="$(this).parent().parent().slideUp();return false;"><span class="ui-icon ui-messages-'+ type+ '-icon-close"></span></a>').appendTo(container);
      // $janal.build('span', '', container, 'ui-messages-'+ type+ '-icon');
      $janal.detail(items, container);
    }, // inline
		addClassError: function(id) {
			$.each(id, function() {
  			if(!$('#'+ this).hasClass('janal-input-error'))
			    $('#'+ this).addClass('janal-input-error')
				var titles= $janal.labels(this.indexOf(':')> 0? this.substring(this.lastIndexOf(':')+ 1): this.indexOf($janal.INPUT_RESERVE)> 0? this.substring(0, this.lastIndexOf($janal.INPUT_RESERVE)): this);
				if(titles.length!== 0) {
					$.each(titles, function() {
						if(!$(this).hasClass('janal-field-error')) {
							$(this).addClass('janal-field-error');
						} // if
					}); // titles
				} // if			
			}); // ids
		},
    detail: function(items, container) {
      $janal.build('br', '', container, '');
      var $container= $janal.build('ul', '', container, '');
      var allItems= items.concat($janal.errors.customs);
      $.each(allItems, function(index) {
        if(typeof(this.severity)=== 'undefined')
          this.severity= 'error';
        if(typeof(this.id)!== 'undefined')
					$janal.addClassError($janal.vector(this.id, ['\\\|']));
        var $li  = $janal.build('li', '', $container, '');
        var $span= $janal.build('span', '', $li, 'ui-messages-'+ this.severity+ '-summary');
        $span.text(this.summary);
        $span= $janal.build('span', '', $li, 'ui-messages-'+ this.severity+ '-detail');
        $span.text(this.detail);
        if(($janal.errors.show> 0) && ((index+ 1)>= $janal.errors.show)) {
          $janal.build('br', '', container, '');
          var $count= $janal.build('div', '', container, 'janal-align-right janal-font-bold');
          $count.text('Total de errores: '+ allItems.length+ '');
          $janal.build('br', '', container, '');
          return false;
        };
      });
			$janal.build('br', '', container, '');
    }, // detail
    programmer: function(items) {
      if(items.length> 0) {
        var msg= 'Errores encontrados en Janal.Control.fields:\n\n';
        $.each(items, function(idx, value){
          msg += (idx+ 1)+ '.- ['+ value.summary+ '] '+ value.detail+ '\n';
        });
        msg += '\nDesarrollador:\n    Favor de corregir los errores ortogr\u00E1ficos.';
        alert(msg);
      } // if  
    }, // programmer
    addTabIndex: function() {
      var idx= 1;	   
      $('#'+ $janal.form).each(function() {
        $('*', this).not('*[for]').each(function() {              
          if ($(this).is('input:text, input:checkbox, input:radio, select, textarea, button')) {         
            if (!$(this).is(':disabled'))
              $(this).attr('tabIndex', idx++);
          } // if
        });
      });        
      $janal.keyEnter();
      setTimeout(function(){$('*[tabIndex=1]').focus();}, 1000);
	  }, // addTabIndex
    keyEnter: function() {
      $('input:text, input:radio, input:checkbox, select, button').on('keydown', function(e) {
	  		$janal.move(e, $(this)); 
  		});
    }, // keyEnter
    move: function(event, element) {
      var keyCode= event.keyCode? event.keyCode: event.which? event.which: event.charCode;
      if (keyCode=== 13) {
        var $components= $('*[tabIndex='+ (parseInt(element.attr('tabIndex'), 10)+ 1)+'], *[tabIndex=1]');
        $.each($components, function() {
          if($components.length=== 1)
            $(this).focus();
          else
            if(parseInt($(this).attr('tabIndex'), 10)!== 1)
              $(this).focus();
        });
        event.preventDefault();
      } // if
    }, // move    
    apply: function(group, clear) {
      $janal.console('janal.apply');
      $janal.errors.validations= [];
      $.each($janal.fields, function(id, value) {
        var $components= $janal.components(value.multiple, id);
				if(typeof($components)!== 'undefined') {
					$.each($components, function() {
						try {
						  if(typeof(clear) === 'undefined' || clear)
							  $(this).rules('remove');
						  if(group=== $janal.JANAL_RESERVE || value.grupo.indexOf(group)>= 0)
							  $janal.rules(id, $(this), $janal.vector(value.validaciones, ['\\\|']), value.mensaje);
						} // try
						catch(error) {
              $janal.console('error: '+ error);
						} // catch	
					});
				} // if	
      }); // each
      $janal.programmer($janal.errors.validations);
    }, // apply
    check: function(customs, blockui) {
      $janal.console('janal.check');
      $janal.hide();
      if(typeof(customs)!== 'undefined' && typeof(customs)!== 'boolean')
        $janal.errors.customs= customs;
      if((typeof(customs)!== 'undefined' && typeof(customs)=== 'boolean' && customs) || (typeof(blockui)!== 'undefined' && blockui))
        $janal.bloquear();
      var ok= $('#'+ $janal.form).valid() && $janal.errors.customs.length=== 0;
      if(ok) 
        $janal.cleanMarks();
      else
        $janal.show($janal.errors.inputs);
      $janal.errors.customs= [];
      if((typeof(customs)!== 'undefined' && typeof(customs)=== 'boolean' && customs) || (typeof(blockui)!== 'undefined' && blockui))
        $janal.desbloquear();
      return ok;
    }, // check    
    update: function(fields) {
      $janal.console('janal.update '+ fields);
      $janal.clean();
      $janal.fields= fields;
      $janal.reset(false);
    }, // update
		restore: function() {
			$janal.change('datos', $janal.backup);
		},
    change: function(form, fields) {
      $janal.console('janal.change '+ fields);
      $janal.clean();
      $janal.prepare(form, fields, $janal.errors.show);
    }, // update
    execute: function(customs, blockui) {
      $janal.console('janal.execute');
      $janal.apply($janal.JANAL_RESERVE);
      return $janal.check(customs, blockui);
    }, // execute
    partial: function(group, customs, blockui) {
      $janal.console('janal.partial');
      var tokens= $janal.vector(group, ['\\\|', '\\\ ', '\\\,']);
      $.each(tokens, function(idx) {
        $janal.apply(String(this), idx=== 0);
      }); // each
      return $janal.check(customs, blockui);
    }, // partial
    element: function(all, field, blockui) {
      $janal.console('janal.element');
      $janal.errors.validations= [];
      $.each($janal.fields, function(id, value) {
        if(id=== field) {
          // search all components with same selector
          $janal.data(false, id, value);
          var $components= $janal.components(value.multiple, id);
					if(typeof($components)!== 'undefinded') {
						$.each($components, function() {
							try {
								$(this).rules('remove');
								var complete= value.formatos;
								if(all || value.individual)
									if(complete.endsWith('|'))
										complete+= value.validaciones;
									else
										complete+= '|'+ value.validaciones;
								$janal.rules(id, $(this), $janal.vector(complete, ['\\\|']), '');
							} // try
							catch(error) {
								$janal.console('error: '+ error);
							} // catch	
						});
					} // if	
        } // if  
      }); // each
      $janal.programmer($janal.errors.validations);
      $janal.hide();
      if(typeof(blockui)!== 'undefined' && blockui)
        $janal.bloquear();
      var validator= $('#'+ $janal.form).validate();
      var ok= false;
			if($janal.reference!== null) 
				validator.element('#'+ $($janal.reference).attr('id').replace(/:/gi, '\\:'));
			else
				validator.element($('#'+ $janal.form).find($janal.selector('$', field)));
      if(ok) 
        $janal.cleanMarks();
      else
				if($janal.errors.inputs.length> 0)
          $janal.show($janal.errors.inputs);
      if(typeof(blockui)!== 'undefined' && blockui)
        $janal.desbloquear();
      $janal.reference= null;
      return ok;
    }, 
    individual: function(field, blockui) {
      $janal.console('janal.individual');
      var ok= true;
      if($janal.fields[field] && !$janal.fields[field].individual)
        ok= $janal.element(true, field, blockui);
      return ok;
    },
    exportTable: function(name, data) {
      $janal.bloquear();
      var ok= $("#"+ data+ " div.ui-dt-c:contains('No existen registros')").text().length> 0;
      if(ok) {
        $janal.hide();
        $janal.show([{summary: "Error del sistema", detail: "No existen registro que exportar"}]);
        $janal.desbloquear();
      } // else
      else	
        $('#'+ name).click(); 
      return !ok;
    }, // exportTable
	  overrideContextMenu: function () {
      $(document).on("click", "span.janal-context-menu", function(event) {
        event.stopImmediatePropagation();
        var row = parseInt($(this).attr('data-ri'));
        var menu= $(this).is('[data-cm]')? $(this).attr('data-cm'): $(this).attr('janal-menu')!== undefined ? $(this).attr('janal-menu'): 'kajoolContextMenu';
        var data= $(this).is('[data-dt]')? $(this).attr('data-dt'): 'kajoolTable';
        if(PF(data)) {
          PF(data).onRowClick(event, PF(data).findRow(row), true);
          PF(data).writeSelections();
        } // if 
        if(PF(menu))
          PF(menu).show(event);
      });
    },// overrideContextMenu  
    lockContextMenu: function(server) {
      $janal.console('janal.lockContextMenu');
      if(typeof(server)!== 'undefined')
        $janal.offContextMenu= server;
      $(document).bind('contextmenu', function(e) { 
        return !($janal.offContextMenu);
      });       
    },
    hideStackMenu: function() {
      $janal.console('janal.hideStackMenu');
      if(PF('stackMenu'))
        PF('stackMenu').collapse(PF('stackMenu').jq.children('img'));
      $('.ui-stack').bind('mousedown', function(e) { 
        if(e.button=== 2 || e.button=== 3) { 
          $('.ui-stack').hide();        
          return false;
        } // if
        else
          return true;
      }); 
    },
		toCorreoHtml : function() {		
      $janal.bloquear();
      var styles       = "";
      var clasesABuscar= [];
      var clasesSplit  = [];
      var encontrado   = false;
      var newForm = $("#tablaCorreos").clone();
      newForm.find('.ui-menubutton').remove();
      newForm.find("*").each(function () {
        if (typeof $(this).attr("class") !== "undefined" ) {
          clasesSplit =$(this).attr("class").split(" ");
          $.each(clasesSplit, function(index, value) {
            if (clasesABuscar.indexOf(value) === -1)
              clasesABuscar.push(value);
          });
        } // if
      });
      $.each(document.styleSheets,function (key,value) {
          $.each(value.cssRules,function (keyRuleCss, valueRuleCss) {
            if (typeof valueRuleCss.selectorText !== "undefined" ) {
              encontrado = false;
              $.each(valueRuleCss.selectorText.split(" "), function( index, value ) {
                if ( clasesABuscar.indexOf( value.substring(1,value.length))!==  -1)
                  encontrado= true;
              });
              if (encontrado)
                styles =styles+valueRuleCss.cssText+" ";
            } // if
          });
      }); 
      remoteTablaCorreos([{name:'text',value:newForm.html()},{name:'styleText',value:styles}]);
      //remoteTabla({text:'$("#tabla").text()'});
    }, // toCorreoHtml
    bloquear: function() {
      if (typeof($janal.locked)!== 'undefined')
        PF($janal.locked).show();
      return true;
    }, // bloquear
    desbloquear: function() {
      if (typeof($janal.locked)!== 'undefined')
        PF($janal.locked).hide();
    }, // desbloquear
		cerrarDialogo: function() {
			this.desbloquear();
			PF('dialogoConfirmacion').hide();
		}, // cerrarDialogo
    custom: function(item) {
      if(typeof(item.severity)=== 'undefined')
        item.severity= 'error';
      var container= $janal.build('div', 'growl_container', $('#'+ $janal.form), 'ui-growl, ui-widget, ui-growl-'+ item.severity);
      container= $janal.build('div', '', container, 'ui-growl-item-container, ui-state-highlight, ui-corner-all, ui-helper-hidden, ui-shadow, ui-growl-'+ item.severity+ '-container');
      container.css({'display': 'block'});
      container= $janal.build('div', '', container, 'ui-growl-item, ui-growl-'+ item.severity+ '-item');
      $('<a href="#" onclick="$(this).parent().parent().slideUp();" style="float:right;cursor:pointer"><span class="ui-icon ui-growl-'+ item.severity+ '-icon-close"></span></a>').appendTo(container);
      $janal.build('span', '', container, 'ui-growl-image, ui-growl-'+ item.severity+ '-image');
      var $span= $janal.build('span', '', container, 'ui-messages-'+ item.severity+ '-summary');
      $span.text(item.summary);
      $janal.build('br', '', container, '');
      $span= $janal.build('span', '', container, 'ui-messages-'+ item.severity+ '-detail');
      $span.text(item.detail);
    }, // custom    
    notify: function(title, type, id, msg) {
			$janal.clean();
			switch (arguments.length) {
        case 3: 
					$janal.custom({summary: title, detail: id, severity: type});
					break;
				case 4:	
					$janal.show([{id: id, summary: title, detail: msg, severity: type}], type);
					break;
	    } // switch
    }, // info
    info: function(id, msg) {
			if(arguments.length=== 1)
			  $janal.notify('Informaci\u00F3n:', 'info', id);
			else
			  $janal.notify('Informaci\u00F3n:', 'info', id, msg);
    }, // info
    warn: function(id, msg) {
			if(arguments.length=== 1)
  			$janal.notify('Precauci\u00F3n:', 'warn', id);
			else
  			$janal.notify('Precauci\u00F3n:', 'warn', id, msg);
    }, // warn
    error: function(id, msg) {
			if(arguments.length=== 1)
			  $janal.notify('Error:', 'error', id);
		  else
			  $janal.notify('Error:', 'error', id, msg);
    }, // error
    alert: function(msg) {
			$janal.console('janal.alert: ');
			alert(msg);
    }, // alert
    version: function() {
      return '0.3.1.8';
    }, // version
    align: function(pixels) {
      try {
        if(typeof(pixels)=== 'undefined')
          pixels= 15;
        $('div.sticky-wrapper').css('top', ($('div.sticky-wrapper').position().top- pixels)+ 'px');      
      } // try  
      catch(e) {
      } // catch
    }, // align
    integer: function(value, start) {
      return isNaN(parseInt(value, 10))? typeof(start)!== 'undefined'? start: 0: parseInt(value, 10);
    },
    double: function(value, start) {
      return isNaN(parseFloat(value, 10))? typeof(start)!== 'undefined'? start: 0: parseFloat(value, 10);
    },
		collapsePanel: function(id) {
      var $divContent= $('#'+ id+ '-content');
			$('#'+ id+ '-find').toggleClass('ui-grid-col-3');
			if($divContent.hasClass('ui-grid-col-9')) 
				$divContent.removeClass('ui-grid-col-9').addClass('ui-grid-col-12');
			else
				$divContent.removeClass('ui-grid-col-12').addClass('ui-grid-col-9');			
		},
		togglePanel: function(id) {
      if ($('#' + id).hasClass('lg-pantalla')) 
		  	$('#' + id).removeClass('lg-pantalla').addClass('xs-pantalla');
		  else
			  $('#' + id).removeClass('xs-pantalla').addClass('lg-pantalla');			
		},
    ready: function() {
      $janal.console('janal.ready');
      if(typeof($janal.start)!== 'undefined')
        $janal.start();
      else
        $janal.console('janal.start not implemented !');
    }, // ready		
		maxDescuentos: function(descuento, extra) {
		 $janal.hide();
		 var importeDescuento= $janal.descuentos($('#'+ descuento)).suma;
		 var importeExtra    = $janal.descuentos($('#'+ extra)).suma;
		 var total           = parseFloat(importeDescuento, 10)+ parseFloat(importeExtra, 10);
		 if(total> 100) 
			 $janal.info(descuento, 'El importe de los descuentos excede el 100% ['+ total+ ']');
	  }, 
		back: function(title, count) {
      $janal.console('janal.back');
			alert('Se '+ title+ ' con consecutivo: '+ count);
		},
		readingMode: function(action) {
			var actionValidate= action!== null && action!== undefined ? action.toUpperCase() : 'CONSULTAR';
			$('input:text,input:checkbox,input:file,textarea,button,a.ui-commandlink,div.ui-selectonemenu,div.ui-chkbox,span.ui-button,div.ui-inputswitch').each(function(index) {  
				if(actionValidate=== 'CONSULTAR') {
					if(this.id!== 'verificadorValue' && this.id!== 'verificadorBuscarPor'){
						if(!(this.tagName=== 'BUTTON' && (this.id=== "cancelar" || this.id=== "cancelarIcon"))) {
							if(this.tagName=== 'A') {
								$(this).attr('iktan', $(this).attr('href'));
								$(this).removeAttr('href').addClass('ui-state-disabled'); 
							} // if
							else 
								if(this.tagName=== 'DIV') {		
									if(this.selector=== 'div.ui-selectonemenu')
										PF('widget_' + this.id).disable();
									else
										$(this).prop('disabled', 'disabled').addClass('ui-state-disabled'); 
								} // if
								else 
									$(this).prop('disabled', 'disabled').addClass('ui-state-disabled'); 
							//$janal.console('janal.readingMode: '+ this.tagName+ ' => '+ this.id+ ' => '+ $(this).attr('disabled')+ ' -> '+ $(this).attr('class'));
						} // if
					} // if
				} // if
				else {
					if(this.tagName=== 'A') {
						$(this).removeClass('ui-state-disabled'); 
						$(this).attr('href', $(this).attr('iktan'));
					} // if
					else 
						if(this.tagName=== 'DIV') {		
							if(this.selector=== 'div.ui-selectonemenu')
								PF('widget_' + this.id).enabled();
							else
								$(this).prop('disabled', '').removeClass('ui-state-disabled'); 
						} // if
						else 
							$(this).prop('disabled', '').removeClass('ui-state-disabled'); 
				} // else
			});
		}, // readingMode
		valueLastFocus: function(value) {
			this.console('janal.valueLastFocus: '+ $(this.lastNameFocus).attr('id')+ ' => '+ value);
			if($(this.lastNameFocus) && !$(this.lastNameFocus).is('[readonly="readonly"]'))	
				$(this.lastNameFocus).val(value);
			else
				janal.console('janal.focus: '+ this.lastNameFocus+ ' esta de solo lectura ');
		},
		sendLastFocus: function() {
			this.console('janal.sendLastFocus: '+ this.lastNameFocus);
			if($(this.lastNameFocus)) {
				this.idNameFocus= $(this.lastNameFocus).attr('id').replace(/:/gi, '\\:');
  			this.console('janal.sendLastFocus: '+ this.idNameFocus);
				setTimeout("$('#'+ $janal.idNameFocus).focus();", 2000);
			} // if	
		},				
  	isPostBack: function(name) {
	  	setTimeout("$('#'+ name).click();", 50);
		},
		notificacion: function() {
			if(typeof(PF('retiroEfectivo'))!== 'undefined') {
				PF('retiroEfectivo').show();
			};
			setTimeout("PF('retiroEfectivo').hide();", 15000);
		},
		tableId: function(name) {
			return $('#'+ name+ $janal.TABLE_RESERVE);
		},
		keyboard: function() {
      $('input.janal-mask-random').each(function() {
        janal.mask($(this).attr('id'), $(this), $(this).attr('alt'));
      });			
		},
		truncate: function(value) {
		  if (value >= 0)
			  return Math.floor(value);
		  else
			  return Math.ceil(value);
	  },
		forward: function() {
			$janal.console('janal.forward: Se activo el evento de redireccionar para salir');
			$janal.desbloquear();
			document.location.href= $janal.toContext().concat("/Exclusiones/salir.jsf?faces-redirect=true");
		},
		pivot: function() {
			$janal.console('janal.pivot: '+ $($janal.lastNameFocus)? $($janal.lastNameFocus).attr('id'): 'undefined');
			$janal.lastReference= $janal.lastNameFocus;
		},
		recover: function() {
			$janal.console('janal.recover: '+ $($janal.lastReference)? $($janal.lastReference).attr('id'): 'undefined');
			$janal.lastNameFocus= $janal.lastReference;
			$janal.sendLastFocus();
		},
		parser: function(element) {
			var name= '';
			if($(element).attr('id').split(':').length> 2) {
			  if($(element).attr('id').endsWith($janal.INPUT_RESERVE)) {
					var name= $(element).attr('id').substring(0, $(element).attr('id').indexOf($janal.INPUT_RESERVE));
					element= $('#'+ name.replace(/:/gi, '\\:'));
				} // if	
			} // if	
			if(typeof($(element).attr('class'))!== 'undefined' && $(element).attr('class').length> 0)
				$.each($(element).attr('class').split(' '), function(index, value) { if(value.startsWith('janal-name')) { name= value.substring(11).replace(/_/g, ' '); } });
			return name;
		},
    specialCharacters: function(input) {
			$janal.console('janal.specialCharacters');
			var c=input.toLowerCase();
			c = c.replace(new RegExp("¨", 'g'),"");
			c = c.replace(new RegExp("[\u00E0\u00E1\u00E2\u00E3\u00E4\u00E5]", 'g'),"a");
			c = c.replace(new RegExp("\u00E6", 'g'),"ae");
			c = c.replace(new RegExp("\u00E7", 'g'),"c");
			c = c.replace(new RegExp("[\u00E8\u00E9\u00EA\u00EB]", 'g'),"e");
			c = c.replace(new RegExp("[\u00EC\u00ED\u00EE\u00EF]", 'g'),"i");                           
			c = c.replace(new RegExp("[\u00F2\u00F3\u00F4\u00F5\u00F6]", 'g'),"o");
			c = c.replace(new RegExp("\u0153", 'g'),"oe");
			c = c.replace(new RegExp("[\u00F9\u00FA\u00FB\u00FC]", 'g'),"u");
			c = c.replace(new RegExp("[\u00FD\u00FF]", 'g'),"y");
			c = c.replace(new RegExp("\u00F1", 'g'),"n");
			return c;
    },
		contains: function(itemLabel, filterValue) {
			$janal.console('janal.contains');
      return itemLabel.includes(filterValue) || $janal.specialCharacters(itemLabel).includes(filterValue);
    },
		session: function() {
			$janal.console('janal.session');
			return $('#janalAccessControl').length=== 0 || $('#janalAccessControl').val()=== '1';
		},
		number: function(name) {
			$janal.console('janal.calulator: '+ $(name).attr('id')+ ' -> '+ $(name).val().trim());
			var value= $(name).val().trim();
			if(typeof(value)=== 'undefined' || value==='' || Number.isNaN(parseFloat(value, 10)) || parseFloat(value, 10)=== 0) 
			  value= '1';
			var id= $(name).attr('id');
			if(id.indexOf(':')>= 0)
				id= id.replace(/:/gi, '\\:');
			$('#'+ id).attr('value', value);
			$(name).val(value);
			$janal.console('janal.calulator ['+ id+ ']  value: ['+ value+ '] set ['+ $(name).val().trim()+ ']');
			return value;
		},
		calculator: function(name) {
			$janal.console('janal.calculator: '+ $(name).attr('id')+ ' value: '+ $(name).val());
			var result= true;
			if($(name).hasClass('add-value-calculator')) {
				$janal.console('janal.calculator: El componente fue modificado por jsCalculator');
				$(name).removeClass('add-value-calculator');
				result= false;
			} // if
			return result;
		}, 
		onLoadCallBack: function() {
			$janal.console('janal.onLoadCallBack');
		},
		sendDataEChart: function (params) {
			var json= {
				chart: params.chart,
				color: params.color,
				name: params.name,
				value: params.value,
				percent: params.percent,
				dataIndex: params.dataIndex,
				dataType: params.dataType,
				seriesId: params.seriesId,
				seriesIndex: params.seriesIndex,
				seriesName: params.seriesName,
				seriesType: params.seriesType,
				event: params.type
			};
			$janal.console('janal.sendDataEChart: '+ json.chart);
			refreshEChart(JSON.stringify(json));
		},
		updateDataEChart: function (name, json) {
			$janal.console('janal.updateDataEChart: '+ name);
			if(window[name]) {
				window[name].clear();
				window[name].setOption(json);
			} // if	
			else
				$janal.console('El marco ['+ name+ '] de la grafica no existe !');
		},
		customFormatLabel: function (params, type) {
			$janal.console('janal.customFormatLabel: '+ type);
			// params.seriesName
			// params.name
			// params.value
			// params.data is Object
			// params.color
			if(typeof(type)=== 'undefined')
				type= '';
			var text= '';
			var data= parseFloat(params.value);
			switch (type) {
				case 'integer':
					text= params.name+ "\n"+ data.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 0, maximumFractionDigits: 0}); // 1,234,567
					break;
				case 'stack-integer':
					text= data.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 0, maximumFractionDigits: 0}); // 1,234,567
					break;
				case 'double':
					text= params.name+ "\n"+ data.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 1, maximumFractionDigits: 2}); // 1,234,567.12
					break;
				case 'stack-double':
					text= data.toLocaleString('en-US', {style: 'decimal', minimumFractionDigits: 1, maximumFractionDigits: 2}); // 1,234,567.12
					break;
				case 'money':
					text= params.name+ "\n"+ data.toLocaleString('en-US', {style: 'currency', currency: 'USD', minimumFractionDigits: 1, maximumFractionDigits: 2}); // $1,242.50
					break;
				case 'stack-money':
					text= data.toLocaleString('en-US', {style: 'currency', currency: 'USD', minimumFractionDigits: 1, maximumFractionDigits: 2}); // $1,242.50
					break;
				case 'percent':
					text= params.name+ "\n("+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 2})+ ")"; // (37.53%)
					break;
				case 'cgor-double':
					text= params.name+ "\n"+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 1}); // 1,234,567.1
					break;
				case 'cgor-percent':
					text= params.name+ "\n("+ data.toLocaleString('en-US', {style: 'percent', minimumFractionDigits: 1, maximumFractionDigits: 1})+ ")"; // 37.5%
					break;
				default:
					text= params.name+ "\n"+ data.toLocaleString('en-US'); // 1,234,567.123
			} // switch
			return text;
		}		
  });
  window.Janal= Janal;
})(window);

$(document).mousedown(function(e) {
  currentEvent= e;
});

$(document).ready(function() {
  janal= new Janal.Control.Validations(Janal.Control.name, Janal.Control.form, Janal.Control.fields, Janal.Control.display, Janal.Control.stage, Janal.Control.growl, Janal.Control.lock, Janal.Control.errors);
	janal.keyboard();
  janal.tableId('tabla').val('');
  console.info('Janal.Control.Validations.initialized: '+ janal.initialized);
});


