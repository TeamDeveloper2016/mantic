/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 11/06/2014
 *time 06:17:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
PrimeFaces.widget.AutoComplete.prototype.alignPanel = function() {
	var fixedPosition = this.panel.css('position') === 'fixed';
	var win           = $(window);
	var positionOffset= fixedPosition? '-'+ win.scrollLeft()+ ' -'+ win.scrollTop(): null;
	var panelWidth    = null;
	if(this.cfg.multiple) 
		panelWidth= this.multiItemContainer.innerWidth()- (this.input.position().left- this.multiItemContainer.position().left)+ 150;
	else 
		panelWidth= this.input.innerWidth()+ 150;
	this.panel.css({left: '', top: '', width: panelWidth, 'z-index': 1}).position({my: 'left top', at: 'left bottom', of: this.input, collision: 'none', offset: positionOffset});
};

PrimeFaces.widget.CustomSticky = PrimeFaces.widget.Sticky.extend({
  init : function(cfg) {
    this._super(cfg);
    var $top= $('#'+ this.id).position().top, $this = this;
    $(this.cfg.source).scroll(function() {
      //console.log($('#'+ $this.id).position().top+ "  "+ $top+ "  "+ $this.fixed);
      if($this.initialState.width!== $this.target.width())
        $this.initialState.width= $this.target.width();
      if($top> $('#'+ $this.id).position().top)
        $this.fix();
       else
        $this.restore();
    });
  }, // init
  refresh: function(cfg) {
    $(this.cfg.source).off('scroll');
    this.init(cfg);
  },
  restore: function() {
    if(this.fixed) {
      this.target.css({
        position: 'static',
        top: 'auto',
        width: 'auto'
      })
      .removeClass('ui-shadow ui-sticky');
      this.target.prev('.ui-sticky-ghost').remove();
      this.fixed = false;
    } // if
  }
});
PrimeFaces.locales['es'] = {
    backLabel: 'Atras',
    nextLabel: 'Siguiente',
    closeText: 'Cerrar',
    prevText: 'Anterior',
    nextText: 'Siguiente',
    monthNames: ['Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun','Jul','Ago','Sep','Oct','Nov','Dic'],
    dayNames: ['Domingo','Lunes','Martes','Mi\u00E9rcoles','Jueves','Viernes','S\u00E1bado'],
    dayNamesShort: ['Dom','Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
    dayNamesMin: ['D','L','M','M','J','V','S'],
    weekHeader: 'Semana',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'S\u00F3lo hora',
    timeText: 'Tiempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    currentText: 'Fecha actual',
    ampm: false,
    month: 'Mes',
    week: 'Semana',
    day: 'D\u00EDa',
    allDayText : 'Todo el d\u00EDa'
};
try {
  if(typeof(PrimeFacesExt)!== 'undefined') {
    PrimeFacesExt.locales.TimePicker['es'] = {
        hourText: 'Hora',
        minuteText: 'Minuto',
        amPmText: ['AM', 'PM'],
        closeButtonText: 'Hecho',
        nowButtonText: 'Ahora',
        deselectButtonText: 'Deseleccionar'
    };
    PrimeFacesExt.locales.Timeline['es'] = {
        MONTHS: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
        MONTHS_SHORT: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
        DAYS: ['Domingo', 'Lunes', 'Martes', 'Mi\u00E9rcoles', 'Jueves', 'Viernes', 'S\u00E1bado'],
        DAYS_SHORT: ['Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
        ZOOM_IN: 'Aumentar zoom',
        ZOOM_OUT: 'Disminuir zoom',
        MOVE_LEFT: 'Mover izquierda',
        MOVE_RIGHT: 'Mover derecha',
        NEW: 'Nuevo',
        CREATE_NEW_EVENT: 'Crear nuevo evento'
    };
  } // if    
} // try
catch(e) {
  console.info('No se encuentran definidos los componentes de Primefaces extensions');
} // else
/*
 *  Rules and names mask's defined for Janal validations
 */
$.mask.rules = $.extend($.mask.rules, {
  'a': /[a-zA-Z\u00C1\u00E4\u00E9\u00EB\u00ED\u00EF\u00F3\u00F6\u00FA\u00FC \u00F1]/,
  'b': /[a-zA-Z\u00E1\u00C4\u00C9\u00CB\u00CD\u00CF\u00D3\u00D6\u00DA\u00DC \u00D1]/,
//                Á     á     ä     Ä     É     é     ë     Ë     Í     í    ï     Ï      Ó     ó    ö     Ö      Ú     ú    ü     Ü      ñ     Ñ
  'c': /[a-zA-Z\u00C1\u00E1\u00C4\u00E4\u00C9\u00E9\u00CB\u00EB\u00CD\u00ED\u00CF\u00EF\u00D3\u00F3\u00D6\u00F6\u00DA\u00FA\u00DC\u00FC \u00F1\u00D1]/,
  'd': /[a-zA-Z\u00D10-9]/,
  'e': /[a-zA-Z\u00D10-9.]/,
  'f': /[HM]/,
  'g': /[0-9a-zA-Z\u00F1\u00D1]/,
  'h': /[0-9aA]/,
  'i': /[0-9a-zA-Z\u00C1\u00E1\u00C4\u00E4\u00C9\u00E9\u00CB\u00EB\u00CD\u00ED\u00CF\u00EF\u00D3\u00F3\u00D6\u00F6\u00DA\u00FA\u00DC\u00FC \u00F1\u00D1,.;:¿?¡!"(){}@#%&_]/,
  'j': /[0-9a-zA-Z\u00C1\u00E1\u00C4\u00E4\u00C9\u00E9\u00CB\u00EB\u00CD\u00ED\u00CF\u00EF\u00D3\u00F3\u00D6\u00F6\u00DA\u00FA\u00DC\u00FC \u00F1\u00D1,.;:¿?¡!"(){}@+-=*_%#|~^&]/,
  'k': /[0-9a-zA-Z]/,
  'l': /[aeiou\u00E1\u00E9\u00ED\u00F3\u00FA]/,
  'm': /[0-7A-C]/,
  'n': /[0-7A]/,
  'o': /[0-9a-zA-Z\u00C1\u00E1\u00C4\u00E4\u00C9\u00E9\u00CB\u00EB\u00CD\u00ED\u00CF\u00EF\u00D3\u00F3\u00D6\u00F6\u00DA\u00FA\u00DC\u00FC \u00F1\u00D1]/,
  'p': /[0-9a-zA-Z\u00C1\u00E1\u00C4\u00E4\u00C9\u00E9\u00CB\u00EB\u00CD\u00ED\u00CF\u00EF\u00D3\u00F3\u00D6\u00F6\u00DA\u00FA\u00DC\u00FC \u00F1\u00D1]/,
	'q': /[0-9a-zA-Z.@_]/,
	'r': /[0-9,.-]/,
	's':/[^\u0025\u0027]/,
	't': /[0-9a-zA-Z_]/,
	'v': /[0-9a-zA-Z ._-]/
});

$.mask.masks = $.extend($.mask.masks, {
  'fecha': {mask: '39/19/9999', fixedChars: '[/]'},
  'fecha-hora': {mask: '39/19/9999 24:59', fixedChars: '[/ :]'},
  'registro': {mask: '39/19/9999 29:59:59', fixedChars: '[/:]'},
  'hora': {mask: '29:59', fixedChars: '[:]'},
  'hora-completa': {mask: '29:59:59', fixedChars: '[:]'},
  'tarjeta-credito': {mask: '9999 9999 9999 9999', fixedChars: '[ ]'},
  'decimal': {mask: '99.999,999,999,999', type: 'reverse',defaultValue: '000', fixedChars: '[,.]'},
  'decimal-signo': {mask: '99.999,999,999,999',type: 'reverse', defaultValue: '+000', fixedChars: '[,.]'},
  'letras': {mask: 'c', type: 'repeat', maxLength: -1, fixedChars: '[]'},		
  'vocales': {mask: 'l', type: 'repeat', maxLength: -1, fixedChars: '[]'},		
  'libre': {mask: 'j', type: 'repeat', maxLength: -1, fixedChars: '[]'},
  'texto': {mask: 'i', type: 'repeat', maxLength: -1, fixedChars: '[]'},
  'numero': {mask: '999999999', type: 'reverse'},
  'un-digito': {mask: '9', type: 'reverse'},
  'dos-digitos': {mask: '99', type: 'reverse'},
  'tres-digitos': {mask: '999', type: 'reverse'},
  'tres-digitos-default': {mask: '999', type: 'reverse', defaultValue: '0'},
  'cuatro-digitos': {mask : '9999', type: 'reverse'},
  'cinco-digitos': {mask: '99999', type: 'reverse'},
  'seis-digitos': {mask: '999999', type: 'reverse'},
  'siete-digitos': {mask: '9999999', type: 'reverse'},
  'ocho-digitos': {mask: '99999999', type: 'reverse'},
  'nueve-digitos': {mask: '999999999', type: 'reverse'},
  'diez-digitos': {mask : '9999999999', type: 'reverse'},
  'entero': {mask: '999,999,999', type: 'reverse', defaultValue: '0', fixedChars: '[,]'},
  'entero-blanco': {mask: '999,999,999', type: 'reverse', defaultValue: '', fixedChars: '[,]'},
  'entero-signo': {mask: '999,999,999', type: 'reverse', defaultValue: '+0', fixedChars: '[,]'},
  'entero-sin-signo': {mask: '999999999', type: 'reverse', defaultValue: '0'},
  'sat': {mask: '9999.999,999,999', type: 'reverse', defaultValue: '00000', fixedChars: '[,.]'},
  'flotante': {mask: '99.999,999,999', type: 'reverse', defaultValue: '000', fixedChars: '[,.]'},
  'flotante-signo': {mask : '99.999,999,999', type: 'reverse', defaultValue: '+000', fixedChars: '[,.]'},
  'rfc': {mask: 'bbbb991939ddh', type: 'fixed'},
  'fiscal': {mask: 'bbbk99999kkkk', type: 'fixed'},
  'moral': {mask: 'bbb991939ddh', type: 'fixed'},
  'curp': {mask: 'bbbb991939fbbbbbdd', type: 'fixed', defaultValue: '', fixedChars: '[]'},
  'moneda': {mask: '$999,999,999', type : 'reverse', fixedChars: '[$,]'},
  'moneda-decimal': {mask: '$99.999,999,999', type: 'reverse', fixedChars: '[$,.]'},
  'mayusculas': {mask: 'b', type: 'repeat', maxLength: -1},
  'minusculas': {mask: 'a', type: 'repeat', maxLength: -1},
  'cuenta': {mask: 'e', type: 'repeat', maxLength: -1},
  'numeros-letras': {mask: 'o', type: 'repeat', maxLength: -1},
  'nombre-dto': {mask: 'g', type: 'repeat', maxLength: 30},
  'telefono': {mask: '(999)999-99-99', fixedChars: '[()-]'},
  'ip': {mask: '999.999.999.999', type: 'reverse', fixedChars: '[.]'},
  'version': {mask: '9.9.9.9', type: 'reverse', fixedChars: '[.]'},
  'clave-ct-call-center': {mask: '99bbb9999b', type: 'fixed', maxLength: 10},
  'clave-ct': {mask: '99bbb9999b9', type: 'fixed', maxLength: 11},		
  'clave-operativa': {mask: '99b9999o9', type: 'fixed', maxLength: 9},
  'resultado-entrevista-basico': {mask: 'm', type: 'repeat', maxLength: 2},
  'resultado-entrevista-modulo': {mask: 'n', type: 'repeat', maxLength: 2},
	'correo':{mask:'q', type:'repeat'},
	'alfanumerico':{mask:'t', type:'repeat'},
	'valor-simple':{mask:'r', type:'repeat'},
	'acceso':{mask:'s', type:'repeat'},
	'codigo':{mask:'v', type:'repeat'}
});

(function() {
  $.validator.addMethod('precio', function(value, element, params) {
      if(typeof(params)=== 'undefined')
				params= {initialize: '0.0'};
		  if(typeof(params.initialize)=== 'undefined')
				params.initialize= '0.0';
      if (janal.empty(value))
				$(element).val(parseFloat(params.initialize).toFixed(janal.decimals));
		  else		
        if(Number.isNaN(parseFloat(value, 10)) || parseFloat(value, 10)< parseFloat(params.initialize, 10))
					$(element).val(parseFloat(params.initialize).toFixed(janal.decimals));
				else
					$(element).val(parseFloat(value, 10).toFixed(janal.decimals)); 
      return true;
		}, function(params, element) {
      return 'No se logro verificar el valor del porcentaje.';
    });
    
  $.validator.addMethod('cantidad', function(value, element, params) {
		  if(typeof(params)=== 'undefined')
				params= {initialize: 0};
		  if(typeof(params.initialize)=== 'undefined')
				params.initialize= 0;
      if (janal.empty(value) || $(element).hasClass('ignore'))
				$(element).val(params.initialize);
		  else		
        if(Number.isNaN(parseInt(value, 10)) || parseInt(value, 10)< parseInt(params.initialize, 10))
				  $(element).val(params.initialize);
				else
					$(element).val(parseInt(value, 10)); 
      return true;
    }, function(params, element) {
      return 'No se logro verificar el número de la cantidad.';
    });
    
  $.validator.addMethod('descuentos', function(value, element, params) {
			var values= '';
      if(typeof(params)=== 'undefined')
				params= {initialize: '0.0'};
		  if(typeof(params.initialize)=== 'undefined')
				params.initialize= '0.0';
			if(janal.empty(value))
				$(element).val(params.initialize);
			else {
				var items= value.split(',');
				for (item in items) {
					if(Number.isNaN(parseFloat(items[item], 10))) 
						items[item]= '0';
					else {
						items[item]= parseFloat(items[item], 10);
						values+= items[item]+ ', ';
					} // else	
				} // for
				$(element).val(values.length=== 0? params.initialize: values.substring(0, values.length- 2));
			} // else	
      return true;
    }, function(params, element) {
      return 'No se logro verificar los valores de los descuentos.';
    });
    
  $.validator.addMethod('consecutivo', function(value, element, params) {
		  var year= (new Date()).getFullYear();
      var val= $.trim(value);
			var params;
			if(typeof(params)=== 'undefined')
				params= {cuantos: 6, cual: '0'};
			if(typeof(params.cuantos)=== 'undefined')
				params.cuantos= 6;
			if(typeof(params.cual)=== 'undefined')
				params.cual= '0';
			for(var x= 0; x< params.cuantos- $.trim(value).length; x++)
				val= params.cual+ val;
      if (!janal.empty(value))
				if(value.length< 6)
          $(element).val(year+ val); 
			  else 
  				if(value.length>= 6) {
						val= value.substring(4);
						var count= val.length;
					  for(var x= 0; x< params.cuantos- count; x++)
		     		  val= params.cual+ val;
					  $(element).val(value.substring(0, 4)+ val.substring(0, 6)); 
					} // if
      return true;
    }, function(params, element) {
      return 'No se logro generar el valor del consecutivo.';
    });
    
  $.validator.addMethod('cambiar-mayusculas', function(value, element, params) {
      if (!janal.empty(value))
        $(element).val($(element).val().toUpperCase()); 
      return true;
    }, function(params, element) {
      return 'No se logro convertir a mayusculas el texto.';
    });

  $.validator.addMethod('especial-mayusculas', function(value, element, params) {
      if (!janal.empty(value))
        $(element).val(janal.specialCharacters($(element).val()).toUpperCase()); 
      return true;
    }, function(params, element) {
      return 'No se logro convertir a mayusculas el texto.';
    });

  $.validator.addMethod('sat', function(value, element, params) {
      if (!janal.empty(value)) 
        $(element).val(parseFloat($(element).val(), 10).toFixed(4)); 
      return true;
    }, function(params, element) {
      return 'No se logro convertir a n\u00FAmero y por lo tanto no se ajustar\u00F3n los decimales.';
    });
		    
  $.validator.addMethod('cambiar-minusculas', function(value, element, params) {
      if (!janal.empty(value))
        $(element).val($(element).val().toLowerCase()); 
      return true;
    }, function(params, element) {
      return 'No se logro convertir a minusculas el texto.';
    });

  $.validator.addMethod('rellenar-caracter', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuantos)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: rellenar-caracter', detail: 'falta el parametro {cuantos}'}]);
          return false;
				} // if	
        else { 
          if(typeof(params.cual)=== 'undefined') {
  					janal.programmer([{summary: 'Funci\u00F3n: rellenar-caracter', detail: 'falta el parametro {cual}'}]);
            params.cual= '0';  
					} // if	
          var val= $.trim(value);
          for(var x= 0; x< params.cuantos- $.trim(value).length; x++)
            val= params.cual+ val;
          $(element).val(val); 
          return true;
        } // else   
    }, function(params, element) {
      return 'No se definio el parametro de cuantos caracteres.';
    });
	
	$.validator.addMethod('porcentaje', function(value, element, params) {
			var values= '';
      if(typeof(params)=== 'undefined')
				params= {initialize: '0.0'};
		  if(typeof(params.initialize)=== 'undefined')
				params.initialize= '0.0';
			params.suma= 0;
			if(janal.empty(value))
				$(element).val(params.initialize);
			else {
				var items= value.split(',');
				for (item in items) {
					if(Number.isNaN(parseFloat(items[item], 10))) 
						items[item]= 0;
					else {
						items[item]= parseFloat(items[item], 10);
						values+= items[item]+ ', ';
					} // else	
					params.suma+= items[item];
				} // for
				$(element).val(values.length=== 0? params.initialize: values.substring(0, values.length- 2));
			} // else	
      return params.suma<= 100;
    }, function(params, element) {
      return janal.parser(element)+ ' el porcentaje ('+ params.suma+ ') no puede ser mayor al 100%.';
    });
    
	$.validator.addMethod('max-caracteres', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else 
        if(typeof(params.cuantos)=== 'undefined')
          return false;
        else
         return $.trim(value).length<= params.cuantos;
    }, function(params, element) {
      return 'N\u00FAmero de caracteres '+ $(element).val().length+ ' es mayor al permitido, el m\u00E1ximo es '+ params.cuantos+ '.';
    });
    
  $.validator.addMethod('min-caracteres', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuantos)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: min-caracteres', detail: 'falta el parametro {cuantos}'}]);
          return false;
				} // if	
        else
          return $.trim(value).length>= params.cuantos;
    }, function(params, element) {
      return 'N\u00FAmero de caracteres '+ $(element).val().length+ ' es menor al permitido, el m\u00EDnimo es '+ params.cuantos+ '.';
    });

  $.validator.addMethod('igual-caracteres', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuantos)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: igual-caracteres', detail: 'falta el parametro {cuantos}'}]);
          return false;
				} // if	
        else
          return $.trim(value).length=== params.cuantos;
    }, function(params, element) {
      return 'N\u00FAmero de caracteres '+ $(element).val().length+ ' no es igual al permitido, el n\u00FAmero de caracteres debe ser '+ params.cuantos+ '.';
    });

  $.validator.addMethod('mayor', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuanto)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: mayor', detail: 'falta el parametro {cuanto}'}]);
          return false;
				} // if	
        else
         return parseFloat(janal.cleanToken(value), 10)> params.cuanto;
    }, function(params, element) {
      return janal.parser(element)+ ' el valor ('+ $(element).val()+ ') tiene que ser mayor al permitido, el valor tiene que ser mayor '+ params.cuanto+ '.';
    });
		
  $.validator.addMethod('mayor-igual', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuanto)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: mayor', detail: 'falta el parametro {cuanto}'}]);
          return false;
				} // if	
        else
         return parseFloat(janal.cleanToken(value), 10)>= params.cuanto;
    }, function(params, element) {
      return janal.parser(element)+ ' el valor ('+ $(element).val()+ ') tiene que ser mayor al permitido, el valor tiene que ser mayor o igual '+ params.cuanto+ '.';
    });
		
  $.validator.addMethod('max-valor', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuanto)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: max-valor', detail: 'falta el parametro {cuanto}'}]);
          return false;
				} // if	
        else
         return parseFloat(janal.cleanToken(value), 10)<= params.cuanto;
    }, function(params, element) {
      return janal.parser(element)+ ' el valor ('+ $(element).val()+ ') tiene que ser mayor al permitido, el valor m\u00E1ximo es '+ params.cuanto+ '.';
    });

  $.validator.addMethod('menor', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuanto)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: menor', detail: 'falta el parametro {cuanto}'}]);
          return false;
				} // if	
        else
          return parseFloat(janal.cleanToken(value), 10)< params.cuanto;
    }, function(params, element) {
      return janal.parser(element)+ ' el valor ('+ $(element).val()+ ') tiene que ser menor al permitido, el valor tiene que ser menor '+ params.cuanto+ '.';
    });
		
  $.validator.addMethod('menor-igual', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuanto)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: menor', detail: 'falta el parametro {cuanto}'}]);
          return false;
				} // if	
        else
          return parseFloat(janal.cleanToken(value), 10)<= params.cuanto;
    }, function(params, element) {
      return janal.parser(element)+ ' el valor ('+ $(element).val()+ ') tiene que ser menor al permitido, el valor tiene que ser menor o igual '+ params.cuanto+ '.';
    });
		
  $.validator.addMethod('min-valor', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        if(typeof(params.cuanto)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: min-valor', detail: 'falta el parametro {cuanto}'}]);
          return false;
				} // if	
        else
          return parseFloat(janal.cleanToken(value), 10)>= params.cuanto;
    }, function(params, element) {
      return janal.parser(element)+ ' el valor ('+ $(element).val()+ ') debe ser menor al permitido, el valor m\u00EDnimo es '+ params.cuanto+ '.';
    });

  $.validator.addMethod('requerido', function(value, element, params) {
      if($(element).is(':disabled'))
        return true;
      else
        switch(element.nodeName.toLowerCase()) {
          case 'select':
            var options = $('option:selected', element);
            if(options=== '-1' || value=== '-1')
              return false;
            else
              return options.length> 0 && (element.type=== 'select-multiple' || ($.browser.msie && !(options[0].attributes['value'].specified)? options[0].text : options[0].value).length> 0);
            break;
          case 'input':
            if (this.checkable(element))
              return this.getLength(value, element)> 0;
            else
              return $(element).hasClass('ignore') || !janal.empty(value);
            break;
          default:
             return $(element).hasClass('ignore') || !janal.empty(value);
        } // switch
    }, function(params, element) {
      var msg= 'El dato es obligatorio.';
      switch(element.nodeName.toLowerCase()) {
        case 'select':
          msg= 'Se requiere al menos un elemento seleccionado.';
        case 'input':
          if(element.type=== 'hidden')
            msg= 'Se requiere al menos un elemento seleccionado.';
      } // switch
      return janal.parser(element)+ ' '+ msg;
    });

	$.validator.addMethod('entero', function(value, element, params) {
	  	if (janal.empty(value) || $(element).hasClass('ignore'))
		  	return true;
		  else
			  return /^([0-9])*$/.test(janal.cleanToken(value));
		}, 'Solo acepta n\u00FAmeros enteros.');

  $.validator.addMethod('entero-signo', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        return /^[-|+|]([0-9])*$/.test(janal.cleanToken(value));
    }, 'Solo acepta n\u00FAmeros enteros.');

  $.validator.addMethod('valor-simple', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        return /^([0-9]|[-]|[.]|[,])+$/.test(janal.cleanToken(value));
    }, 'Solo acepta n\u00FAmeros enteros con decimales y signo.');

	$.validator.addMethod('telefono', function(value, element, params) {
      value= janal.cleanToken(value);
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else {
        value= janal.remove(value, ['\\\ ', '\\\,', '\\\$', '\\\(', '\\\)', '\\\-']);
				return (value.length=== 7 || value.length=== 10);
      } // else
		}, 'No es v\u00E1lido el n\u00FAmero de tel\u00E9fono.');

	$.validator.addMethod('contiene-a', function(value, element, params) {
		  if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
        if(typeof(params.valores)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: contiene-a', detail: 'falta el parametro {valores}'}]);
          return false;
				} // if	
        else
			    return params.valores.indexOf(value)>= 0;
		}, function(params, element) {
      return 'El valor '+ janal.value($(element).attr('id'))+ ' no se encuentra en '+ params.valores+ '.';
    });

	$.validator.addMethod('igual-a', function(value, element, params) {
		  if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
        if(typeof(params.cual)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: igual-a', detail: 'falta el parametro {cual}'}]);
          return false;
				} // if	
        else
  			  return value=== janal.value(janal.cross($(element).attr('id'), params.cual));
		}, function(params, element) {
      return 'El valor '+ janal.value($(element).attr('id'))+ ' debe ser igual a '+ janal.value(janal.cross($(element).attr('id'), params.cual))+ '.';
    });

	$.validator.addMethod('menor-a', function(value, element, params) {
		  if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
        if(typeof(params.cual)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: menor-a', detail: 'falta el parametro {cual}'}]);
          return false;
				} // if	
        else {
					janal.console('janal.menor-a: value ['+ value+ '] cual: ['+ janal.value(janal.cross($(element).attr('id'), params.cual))+ ']');
  			  return janal.double(janal.cleanToken(value), 0)<= janal.double(janal.cleanToken(janal.value(janal.cross($(element).attr('id'), params.cual))), 0);
				} // else
		}, function(params, element) {
      return 'El valor '+ janal.value($(element).attr('id'))+ ' debe ser menor o igual '+ janal.value(janal.cross($(element).attr('id'), params.cual))+ '.';
    });

	$.validator.addMethod('mayor-a', function(value, element, params) {
		  if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
        if(typeof(params.cual)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: mayor-a', detail: 'falta el parametro {cual}'}]);
          return false;
				}	// if
        else 
  			  return janal.double(janal.cleanToken(value), 0)>= janal.double(janal.cleanToken(janal.value(janal.cross($(element).attr('id'), params.cual))), 0);
		}, function(params, element) {
      return 'El valor '+ janal.value($(element).attr('id'))+ ' debe ser mayor o igual '+ janal.value(janal.cross($(element).attr('id'), params.cual))+ '.';
    });

	$.validator.addMethod('asterisco', function(value, element, params) {
			return !/[*|''']+/.test(value);
		}, 'El caracter * (asterisco) no es permitido.');

	$.validator.addMethod('moneda', function(value, element, params) {
			return /^(\d{1,3}[ ,]?)*$/.test(value);
		}, 'No es v\u00E1lido, debe ser cantidad monetaria.');

	$.validator.addMethod('moneda-decimal', function(value, element, params){
			return /^(\d{1,3}[ ,]?)*(\.\d{0,2})$/.test(value);
		}, 'No es v\u00E1lido, debe ser cantidad monetaria con decimales.');

	$.validator.addMethod('flotante', function(value, element, params) {
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
        return (/^[0-9]+[\.]{0,1}\d*$/.test(janal.cleanToken(value)) && value.search(/^[0-9]+[\.]$/));
    }, 'Solo acepta n\u00FAmeros flotantes sin signo.');

  $.validator.addMethod('flotante-signo', function(value, element, params){
      if (janal.empty(value) || $(element).hasClass('ignore'))
        return true;
      else
			  return (/^[-|+|]([1-9])+[\.]{0,1}\d*$/.test(janal.cleanToken(value)) && value.search(/^[-|+|]([1-9])+[\.]$/));
		}, 'Solo acepta n\u00FAmeros flotantes con signo.');

	$.validator.addMethod(
		'mayusculas', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
				return /^[A-Z]|[\s]+$/.test(value);
		}, 'Solo acepta texto en may\u00FAsculas.');
	
	$.validator.addMethod('minusculas', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
			  return true;
			else
			  return /^[a-z]|[\s]+$/.test(value);
		}, 'Solo acepta texto en min\u00FAsculas.');

  $.validator.addMethod('vocales', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
			  return true;
			else
			  return /^[a|e|i|o|u]+$/.test(value);
		}, 'Solo acepta un texto de puras vocales.');

	$.validator.addMethod('rango', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
				return (parseInt(value, 10)>= params.min && parseInt(value,10)<= params.max);	
		}, function(params) {
      return 'El valor no se encuentra en el rango permitido, el rango es '+ params.min+ ' al '+ params.max+ '.';
    });

	$.validator.addMethod('secuencia-palabra', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else {
				var count = 0;
				var before= value[0];
				for(var i= 0; i < value.length; i++) {
					var caracter= value[i];
					if(before=== caracter)
						count= count+ 1;
					else {
						before= caracter;
						count= 0;
					} // else
					if(count=== 4)
						return false;
				} // for
				return true;
			} // else
		}, 'El valor no se encuentra en el rango permitido.');

	$.validator.addMethod('longitud', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
        if(typeof(params.min)=== 'undefined' || typeof(params.max)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: longitud', detail: 'falta el parametro {min o max}'}]);
          return false;
				} // if	
        else
  				return (value.length>= params.min && value.length<= params.max);	
		}, function(params) {
      return 'La longitud de caracteres no se encuentra en el rango permitido, el rango es '+ params.min+ ' al '+ params.max+ '.';
    });
		
	$.validator.addMethod('letras', function(value, element, params) {
		   if (janal.empty(value) || $(element).hasClass('ignore'))
				 return true;
		   else
				 return /^([a-z]|[A-Z])+$/.test(value);
		}, 'Solo se permiten caracteres alfab\u00E9ticos.');

	$.validator.addMethod(
		'texto', function(value, element, params) {
  		 if (janal.empty(value) || $(element).hasClass('ignore'))
	  			return true;
		   else
			   return /^([0-9]|[A-Z]|[a-z]|á|é|í|ó|ú|Á|É|Í|Ó|Ú|ñ|Ñ|\ |,|.|;|:)+$/.test(value);
		}, 'No se permite caracteres alfanum\u00E9ricos y algunos caracteres, ejemplo: ,;:.');

  $.validator.addMethod(
		'libre', function(value, element, params) {
  		 if (janal.empty(value) || $(element).hasClass('ignore'))
	  			return true;
		   else
			   return /^([0-9]|[A-Z]|[a-z]|á|é|í|ó|ú|Á|É|Í|Ó|Ú|ñ|Ñ|\ |,|.|;|:|(|)|@|-|=|%|#|~|^|&|{|}|[|]|")+$/.test(value);
		}, 'No se permite caracteres alfanum\u00E9ricos y algunos caracteres, ejemplo: ,;:.');
		
	$.validator.addMethod('curp', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
		  else
			  return /^([A-Za-z]{4})([\d]{6})([Hh|Mm])([A-Za-z]{5})([A|0-9]{1})([0-9]{1})$/.test(value);
		}, 'Formato de la CURP no es v\u00E1lido.');

	$.validator.addMethod('rfc', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
	    else
  			return (/^([A-Za-z]{3})([\d]{6})([\dA-Za-z]{3})$/.test(value) && janal.isDate('19'+ value.substring(3,5), value.substring(5,7), value.substring(7,9))) || ((/^([A-Za-z]{4})([\d]{6})$/.test(value) || /^([A-Za-z]{4})([\d]{6})([\dA-Za-z]{3})$/.test(value)) && janal.isDate('19'+ value.substring(4,6), value.substring(6,8), value.substring(8,10)));
		}, 'Formato del RFC no es v\u00E1lido.');

	$.validator.addMethod('moral', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
	    else
  			return /^([A-Za-z]{3})([\d]{6})([\dA-Za-z]{3})$/.test(value) && janal.isDate('19'+ value.substring(3,5), value.substring(5,7), value.substring(7,9));
		}, 'Formato del RFC fiscal no es v\u00E1lido.');

	$.validator.addMethod('texto-especial', function(value, element, params) {
 		  if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
		  else
				return /^([a-z]|[A-Z]|[0-9]|[?|+|-|\/|?|:|-|{|}|;|,|_|ñ|Ñ|@|\[|\]|\ ])+$/.test(value);
	 }, 'No se permite caracteres alfanum\u00E9ricos y algunos caracteres, ejemplo: +-*/{}[]@|,;:.');

	$.validator.addMethod('boleano',function(value, element, params){
			if (janal.empty(value) || $(element).hasClass('ignore'))
		 		return true;
		  else
			  return /^([Ss|Nn])$/.test(value);
		}, 'Solo se permiten los caracteres S,s o N,n.');

	$.validator.addMethod('resultado-entrevista-modulo', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
				return '01|02|A1|A2|A3|A4|A5|A6|A7'.indexOf(value)>= 0;
		}, 'No es un resultado de entrevista valido del cuestionario modulo.');
			
	$.validator.addMethod('resultado-entrevista-basico', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
				return '01|02|A1|A2|A3|A4|A5|A6|A7|B1|B2|B3|C1|C2|C3|C4'.indexOf(value)>= 0;
		}, 'No es un resultado de entrevista v\u00E1lido del cuestionario b\u00E1sico.');

	$.validator.addMethod('fecha', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
			  return janal.isCustomDate(value);
		}, function(params, element) {
      'Formato de la fecha '+ janal.value($(element).attr('id'))+ ' es inv\u00E1lida, el formato es [dd/mm/yyyy].';
    });	

	$.validator.addMethod('fecha-menor', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
        if(janal.isCustomDate(value))
          if(typeof(params.cual)=== 'undefined') {
						janal.programmer([{summary: 'Funci\u00F3n: fecha-menor', detail: 'falta el parametro {cual}'}]);
            return false;
					} // if	
          else {
            var before= janal.value(janal.cross($(element).attr('id'), params.cual));
            if(janal.empty(before))
              return true;
            else
              return janal.isCustomDate(before) && janal.checksDates(value, before);
          } // else
        else
			    return false;
		}, function(params, element) {
      var before= $(element).val();
      var msg   = 'La fecha '+ $(element).val()+ ' con la que se compara es mayor a '+ janal.value(janal.cross($(element).attr('id'), params.cual))+ '.';
      if(!janal.isCustomDate(before))
        msg= 'Formato de la fecha '+ $(element).val()+ ' es inv\u00E1lida, el formato es [dd/mm/yyyy].';
      return msg;
    });	

	$.validator.addMethod('fecha-mayor', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
        if(janal.isCustomDate(value))
          if(typeof(params.cual)=== 'undefined') {
						janal.programmer([{summary: 'Funci\u00F3n: fecha-mayor', detail: 'falta el parametro {cual}'}]);
            return false;
					} // if	
          else {
            var before= janal.value(janal.cross($(element).attr('id'), params.cual));
            if(janal.empty(before))
              return true;
            else
              return janal.isCustomDate(before) && janal.checksDates(before, value);
          } // else
        else
			    return false;
		}, function(params, element) {
      var before= $(element).val();
      var msg   = 'La fecha '+ $(element).val()+ ' con la que se compara es menor a '+ janal.value(janal.cross($(element).attr('id'), params.cual))+ '.';
      if(!janal.isCustomDate(before))
        msg= 'Formato de la fecha '+ $(element).val()+ ' es inv\u00E1lida, el formato es [dd/mm/yyyy].';
      return msg;
    });	

	$.validator.addMethod('registro', function(value, element, params) {
			if(janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
				if(/^([\d]{2})\/([\d]{2})\/([\d]{4})[\s]([\d]{2}):([\d]{2}):([\d]{2})$/.test(value)) {
					var values= value.split(' ');
					return janal.isCustomDate(values[0]) && janal.isCustomHour(values[1]);
				} // if
				else
					return false;
		}, 'Formato de la fecha y hora es inv\u00E1lida, el formato es [dd/mm/yyyy HH:MM:SS].');

	$.validator.addMethod('hora', function(value, element, params) {
			if(janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
				return /^(0[0-9]|1\d|2[0-3]):([0-5]\d)$/.test(value);
		}, 'Formato de la hora es incorrecta, el formato es a 24 hrs [HH:MM].');

	$.validator.addMethod('hora-completa', function(value, element, params) {
			if(janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
				return /^(0[0-9]|1\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(value);
		}, 'Formato de la hora es incorrecta, el formato es 24 hrs [HH:MM:SS].');

	$.validator.addMethod('hora-menor', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else {
        if(value.length=== 5)
          value= value+ ':00';
        if(/^(0[0-9]|1\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(value))
          if(typeof(params.cual)=== 'undefined') {
						janal.programmer([{summary: 'Funci\u00F3n: hora-menor', detail: 'falta el parametro {cual}'}]);
            return false;
					}	// if
          else {
            var before= janal.value(janal.cross($(element).attr('id'), params.cual));
            if(typeof(before)!== 'undefined' && before.length=== 5)
               before= before+ ':00';
            if(janal.empty(before))
              return true;
            else
              return /^(0[0-9]|1\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(before) && (parseInt(janal.remove(value, [' ', ':']), 10)<= parseInt(janal.remove(before, [' ', ':']), 10));
          } // else
        else
			    return false;
      } // else  
		}, function(params, element) {
      var before= $(element).val();
      var msg   = 'La hora '+ before+ ' con la que se compara es mayor a '+ janal.value(janal.cross($(element).attr('id'), params.cual))+ '.';
      var zeros = '';
      if(typeof(before)!== 'undefined' && before.length=== 5)
        zeros= zeros+ ':00';
      if(!/^(0[0-9]|1\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(before+ zeros))
        msg= 'Formato de la hora '+ before+ ' es inv\u00E1lida, el formato es de 24 hrs [23:59].';
      return msg;
    });	

	$.validator.addMethod('hora-mayor', function(value, element, params) {
			if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else {
        if(value.length=== 5)
          value= value+ ':00';
        if(/^(0[0-9]|1\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(value))
          if(typeof(params.cual)=== 'undefined') {
						janal.programmer([{summary: 'Funci\u00F3n: hora-mayor', detail: 'falta el parametro {cual}'}]);
            return false;
					} // if	
          else {
            var before= janal.value(janal.cross($(element).attr('id'), params.cual));
            if(typeof(before)!== 'undefined' && before.length=== 5)
               before= before+ ':00';
            if(janal.empty(before))
              return true;
            else
              return /^(0[0-9]|1\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(before) && (parseInt(janal.remove(value, [' ', ':']), 10)>= parseInt(janal.remove(before, [' ', ':']), 10));
          } // else
        else
			    return false;
      } // else  
		}, function(params, element) {
      var before= $(element).val();
      var msg   = 'La hora '+ before+ ' con la que se compara es menor a '+ janal.value(janal.cross($(element).attr('id'), params.cual))+ '.';
      var zeros = '';
      if(typeof(before)!== 'undefined' && before.length=== 5)
        zeros= zeros+ ':00';
      if(!/^(0[0-9]|1\d|2[0-3]):([0-5]\d):([0-5]\d)$/.test(before+ zeros))
        msg= 'Formato de la hora '+ before+ ' es inv\u00E1lida, el formato es de 24 hrs [23:59].';
      return msg;
    });	

	$.validator.addMethod('comodin', function(value, element, params) {
		  if (janal.empty(value) || $(element).hasClass('ignore'))
			  return true;
		  else
        if(typeof(params.expresion)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: comodin', detail: 'falta el parametro {expresion}'}]);
          return false;
				} // if	
        else
				  return eval('/^'+ params.expresion+ '$/').test(value);	
		}, 'Uno de los caracter no se encuentra en la cadena especificada.');

	$.validator.addMethod('no-permitir', function(value, element, params) {
		  if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
		  else
        if(typeof(params.valor)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: no-permitir', detail: 'falta el parametro {valor}'}]);
          return false;
				} // if	
        else
				  return parseInt(params.valor, 10)!== parseInt(value, 10);	
		}, function(params, element) {
      return 'Valor '+ $(element).val()+ ' no es permitido porque es diferente a '+ params.valor + '.';
    });

  $.validator.addMethod('ipv4', function(value, element) {
    return this.optional(element) || /^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$/i.test(value);
  }, 'Direcci\u00F3n IPv4 es inv\u00E1lida.');

  $.validator.addMethod('ipv6', function(value, element) {
    return this.optional(element) || /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i.test(value);
  }, 'Direcci\u00F3n IPv6 es inv\u00E1lida.');

	$.validator.addMethod('esta-en', function(value, element, params) {
		  if (janal.empty(value) || $(element).hasClass('ignore'))
				return true;
			else
        if(typeof(params.valores)=== 'undefined') {
					janal.programmer([{summary: 'Funci\u00F3n: esta-en', detail: 'falta el parametro {valores}'}]);
          return false;
				} // if	
        else {
          var values= janal.vector($.trim(params.valores), ['\\\,', '\\\|']);
          var ok    = false;
          $.each(values, function(id, item) {
            var range= janal.vector($.trim(item), ['\\\-']);
            if(!ok)
              if(range.length===1)
                ok= parseInt(janal.cleanToken(value), 10)=== parseInt(range[0], 10);
              else
                ok= (parseInt(janal.cleanToken(value), 10)>= Math.min(parseInt(range[0], 10), parseInt(range[1], 10))) && (parseInt(janal.cleanToken(value), 10)<= Math.max(parseInt(range[0], 10), parseInt(range[1], 10)));
          }); // each
			    return ok;
        } // else
		}, function(params, element) {
      return 'El valor '+ janal.value($(element).attr('id'))+ ' no se encuentra en '+ params.valores+ '.';
    });
	
	$.validator.addMethod('correo', function(value, element) {				
		if(janal.empty(value) || $(element).hasClass('ignore'))
			return true;
		else {
			expresionCorreo= /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			return expresionCorreo.test(value);
		} // else
   }, 'Correo es inv\u00E1lido.');
   
	$.validator.addMethod('acceso', function(value, element) {
	  	if (janal.empty(value) || $(element).hasClass('ignore'))
		  	return true;
		  else
			  return /^([^%'])*$/.test(value);
		}, 'La contrase\u00F1a tiene caracteres inv\u00E1lidos');
}());
