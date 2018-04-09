/*
 *company KAANA
 *project KAJOOL (Control system polls)
 *date 17/09/2019
 *time 20:42:55 PM
 *author Team Developer 2016 <team.developer@kaana.org.mx>
 */
	Janal.Control.fields = {
		'cuestionario\\:pagina1_cuestionario'					: { validaciones: 'requerido|rango({"min":1,"max":1})|menor-a({"cual":"pagina1_cuestionarios"})', mascara: {mask: '1', type: 'reverse'}, mensaje: 'El numero de cuestionario es requerido, no debe ser mayor al numero total de cuestionarios y debe capturarse entre 1 y 4'},
		'cuestionario\\:pagina1_cuestionarios'				: { validaciones: 'requerido|rango({"min":1,"max":1})', mascara: {mask: '1', type: 'reverse'}, mensaje: 'El total de cuestionarios es requerido y debe capturarse entre 1 y 4'},
		'cuestionario\\:pagina1_nombre'								: { validaciones: 'cambiar-mayusculas', mascara: 'letras', mensaje: 'Capture nombre', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_apellido_paterno'			: { validaciones: 'cambiar-mayusculas', mascara: 'letras', mensaje: 'Capture apellido paterno', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_apellido_materno'			: { validaciones: 'cambiar-mayusculas', mascara: 'letras', mensaje: 'Capture apellido materno', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_consecutivo_familia'	: { validaciones: 'requerido', mascara: 'diez-digitos', mensaje: 'Capture consecutivo de familia', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_upm'									: { validaciones: 'requerido|rango({"min":1,"max":108})|rellenar-caracter({"cuantos":3,"cual":"0"})', mascara: 'tres-digitos', mensaje: 'Capture upm', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_domicilo'							: { validaciones: 'libre|cambiar-mayusculas', mascara: 'libre', mensaje: 'Capture domicilio', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_numero_exterior'			: { validaciones: 'libre', mascara: 'libre', mensaje: 'Capture número exterior', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_numero_interior'			: { validaciones: 'libre', mascara: 'libre', mensaje: 'Capture número interior', grupo: 'cuestionario'},	
		'cuestionario\\:pagina1_colonia'							: { validaciones: 'libre', mascara: 'libre', mensaje: 'Capture colonia', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_cp'										: { validaciones: 'libre|rango({"min":1,"max":99999})|rellenar-caracter({"cuantos":5,"cual":"0"})', mascara: 'cinco-digitos', mensaje: 'Capture C.P.', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_telefono'							: { validaciones: 'libre', mascara: 'telefono', mensaje: 'Capture telefono', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_referencia'						: { validaciones: 'requerido', mascara: 'libre', mensaje: 'Capture referencia', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_entre_calle_1'				: { validaciones: 'libre|cambiar-mayusculas', mascara: 'libre', mensaje: 'Capture entre calle 1', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_entre_calle_2'				: { validaciones: 'libre|cambiar-mayusculas', mascara: 'libre', mensaje: 'Capture entre calle 2', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_calle_atras'					: { validaciones: 'libre|cambiar-mayusculas', mascara: 'libre', mensaje: 'Capture calle atras', grupo: 'cuestionario'},		
		'cuestionario\\:pagina1_entidad_descripcion'	: { validaciones: 'requerido', mascara: 'letras', mensaje: 'Capture la descripción de entidad', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_entidad_clave'				: { validaciones: 'requerido', mascara: 'dos-digitos', mensaje: 'Capture la clave de entidad', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_municipio_descripcion': { validaciones: 'requerido', mascara: 'letras', mensaje: 'Capture la descripción de municipio', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_municipio_clave'			: { validaciones: 'requerido', mascara: 'tres-digitos', mensaje: 'Capture la clave de municipio', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_localidad_descripcion': { validaciones: 'requerido', mascara: 'letras', mensaje: 'Capture la descripción de localidad', grupo: 'cuestionario'},
		'cuestionario\\:pagina1_localidad_clave'			: { validaciones: 'requerido', mascara: 'cuatro-digitos', mensaje: 'Capture la clave de localidad', grupo: 'cuestionario'},
		
		'cuestionario\\:pagina2_apellido_paterno'			: { validaciones: 'requerido|cambiar-mayusculas', mascara: 'letras', mensaje: 'Capture el apellido paterno', grupo: 'pagina2'},
		'cuestionario\\:pagina2_apellido_materno'			: { validaciones: 'libre|cambiar-mayusculas', mascara: 'letras', mensaje: 'Capture el apellido materno', grupo: 'pagina2'},
		'cuestionario\\:pagina2_nombre'								: { validaciones: 'requerido|cambiar-mayusculas', mascara: 'letras', mensaje: 'Capture el nombre', grupo: 'pagina2'},
		
		'cuestionario\\:pagina3_vive'				  : { validaciones: 'requerido|rango({"min":1,"max":8})', mascara: {mask: '8', type: 'reverse'}, mensaje: 'La pregunta 2 solo puede tener como respuesta entre 1 y 8', grupo: 'cuestionario'},					
		'cuestionario\\:pagina3_genero'			  : { validaciones: 'requerido|rango({"min":1,"max":2})', mascara: {mask: '2', type: 'reverse'}, mensaje: 'La pregunta 3 solo puede tener como respuesta 1 o 2', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_edad'			    : { validaciones: 'requerido|rango({"min":0,"max":99})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_escuela'			: { validaciones: 'requerido|rango({"min":1,"max":2})', mascara: {mask: '2', type: 'reverse'}, mensaje: 'La pregunta 5 solo puede tener como respuesta 1 o 2', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_parentesco'		: { validaciones: 'requerido|rango({"min":1,"max":12})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', mensaje: 'La pregunta 6 solo puede tener como respuesta entre 1 y 12', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_contemplado'	: { validaciones: 'requerido|rango({"min":1,"max":2})', mascara: {mask: '2', type: 'reverse'}, mensaje: 'La pregunta 7 solo puede tener como respuesta entre 1 y 2', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_apoyo_1'			: { validaciones: 'rango({"min":1,"max":1})', mascara: {mask: '1', type: 'reverse'}, mensaje: 'La pregunta 8.1 solo puede tener como respuesta 1', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_apoyo_2'			: { validaciones: 'rango({"min":2,"max":2})', mascara: {mask: '2', type: 'reverse'}, mensaje: 'La pregunta 8.2 solo puede tener como respuesta 2', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_apoyo_3'			: { validaciones: 'rango({"min":3,"max":3})', mascara: {mask: '3', type: 'reverse'}, mensaje: 'La pregunta 8.3 solo puede tener como respuesta 3', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_apoyo_4'			: { validaciones: 'rango({"min":4,"max":4})', mascara: {mask: '4', type: 'reverse'}, mensaje: 'La pregunta 8.4 solo puede tener como respuesta 4', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_apoyo_5'			: { validaciones: 'rango({"min":5,"max":5})', mascara: {mask: '5', type: 'reverse'}, mensaje: 'La pregunta 8.5 solo puede tener como respuesta 5', grupo: 'cuestionario'},				
		'cuestionario\\:pagina3_beneficio'		: { validaciones: 'requerido|rango({"min":0,"max":99})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', grupo: 'cuestionario'},				
				
		'cuestionario\\:pagina4_10'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 10 es requerida.', grupo: 'cuestionario'},
		'cuestionario\\:pagina4_11'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 11 es requerida.', grupo: 'cuestionario'},
		'cuestionario\\:pagina4_12'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 12 es requerida.', grupo: 'cuestionario'},
		'cuestionario\\:pagina4_13'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 13 es requerida.', grupo: 'cuestionario'},
		'cuestionario\\:pagina4_13_especifique'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La especificación de la pregunta 13 es requerida.', grupo: 'cuestionario'},
		'cuestionario\\:pagina4_14'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 14 es requerida.', grupo: 'cuestionario'},
		'cuestionario\\:pagina4_15'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 15 es requerida.', grupo: 'cuestionario'},
		
		'cuestionario\\:pagina5_16'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 16 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina5_17'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 17 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina5_18'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 18 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina5_19'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 19 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina5_20'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 20 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina5_21'	: { validaciones: 'requerido|rango({"min":1,"max":10})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', mensaje: 'La respuesta de la pregunta 21 es requerida', grupo: 'cuestionario'},
		
		'cuestionario\\:pagina6_22'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 22 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina6_23'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 23 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina6_24'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 24 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina6_25'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 25 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina6_26'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 26 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina6_27'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 27 es requerida', grupo: 'cuestionario'},
		
		'cuestionario\\:pagina7_28'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 28 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina7_29'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 29 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina7_30'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 30 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina7_31'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 31 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina7_32'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 32 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina7_33'	: { validaciones: 'requerido|rango({"min":1,"max":10})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', mensaje: 'La respuesta de la pregunta 33 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina7_34'	: { validaciones: 'requerido|rango({"min":1,"max":10})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', mensaje: 'La respuesta de la pregunta 34 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina7_35'	: { validaciones: 'requerido|rango({"min":1,"max":10})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', mensaje: 'La respuesta de la pregunta 35 es requerida', grupo: 'cuestionario'},
		
		'cuestionario\\:pagina8_36'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 36 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina8_37'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 37 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina8_38'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 38 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina8_39'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 39 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina8_40'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 40 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina8_41'	: { validaciones: 'requerido|rango({"min":1,"max":10})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', mensaje: 'La respuesta de la pregunta 41 es requerida', grupo: 'cuestionario'},		
		
		'cuestionario\\:pagina9_42'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 42 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina9_43'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 43 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina9_44'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 44 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina9_45'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 45 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina9_46'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 46 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina9_47'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 47 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina9_48'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 48 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina9_49'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 49 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina9_50'	: { validaciones: 'requerido|rango({"min":1,"max":10})|rellenar-caracter({"cuantos":2,"cual":"0"})', mascara: 'dos-digitos', mensaje: 'La respuesta de la pregunta 50 es requerida', grupo: 'cuestionario'},
	
		'cuestionario\\:pagina10_51'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 51 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina10_52'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 52 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina10_53'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 53 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina10_54'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 54 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina10_55'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 55 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina10_56'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 56 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina10_57'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 57 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina10_58'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 58 es requerida', grupo: 'cuestionario'},		
		'cuestionario\\:pagina10_59'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 59 es requerida', grupo: 'cuestionario'},
		
		'cuestionario\\:pagina11_60'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 60 es requerida.', grupo: 'cuestionario'},
		'cuestionario\\:pagina11_61'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 61 es requerida.', grupo: 'cuestionario'},
		'cuestionario\\:pagina11_62'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 62 es requerida.', grupo: 'cuestionario'},
		
		'cuestionario\\:pagina12_1'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 1 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina12_2'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 2 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina12_3'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 3 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina12_4'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 4 es requerida', grupo: 'cuestionario'},
		'cuestionario\\:pagina12_5'	: { validaciones: 'requerido', mascara: 'libre', mensaje: 'La respuesta de la pregunta 5 es requerida', grupo: 'cuestionario'}
		
	};
	
  function deshabilitar(pregunta) {
    $(pregunta).val(null);				
    $(pregunta).addClass('ui-state-disabled');
    $(pregunta).attr('disabled','disabled');								
  }

  function deshabilitarMultiple(preguntas) {
    var pregunta = preguntas.split("|");
    $.each(pregunta, function(index, value) {
      deshabilitar('#cuestionario\\:'+ value);
    });
  }

  function habilitar(pregunta) {
    $(pregunta).removeClass('ui-state-disabled');
    $(pregunta).removeAttr('disabled','disabled');
  }

  function habilitarMultiple(preguntas) {
    var pregunta = preguntas.split("|");
    $.each(pregunta, function(index, value) {
      habilitar('#cuestionario\\:'+ value);
    });
  }
  
	function deshabilitarPreguntasInput(pregunta, listaRespuestas, listaPreguntas) {				
		var regresar  = false;
		var respuestas= listaRespuestas.split("|");
		var preguntas = listaPreguntas.split("|");
		for(var x = 0; x < respuestas.length; x++) {				
		  var preguntasHabilita= preguntas[x].split(",");
			if(respuestas[x].indexOf($('#cuestionario\\:'+ pregunta).val())> 0) {
				for(var i = 0; i < preguntasHabilita.length; i++) {
          deshabilitar('#cuestionario\\:'+preguntasHabilita[i]);
					regresar= true;
				} // for													
			}
			else {
				for(var i = 0; i < preguntasHabilita.length; i++) {	
          habilitar('#cuestionario\\:'+preguntasHabilita[i]);
				} // for													
			} // else		
			if(regresar)
				break;
		} // for			
	};
	
	function deshabilitarPreguntasRadio(pregunta, listaRespuestas, listaPreguntas, radio, input) {				
		var regresar  = false;
		var respuestas= listaRespuestas.split("|");
		var preguntas = listaPreguntas.split("|");
		for(var x = 0; x < respuestas.length; x++) {				
		  var preguntasHabilita= preguntas[x].split(",");
			if($('input:radio[id=cuestionario\\:'+ pregunta+ '\\:'+ radio+ ']:checked').val()!== undefined) {
				var valor= $('input:radio[id=cuestionario\\:'+ pregunta+ '\\:'+ radio+ ']:checked').val();
				if(respuestas[x].indexOf(valor)> 0) {
					for(var i = 0; i < preguntasHabilita.length; i++) {
            deshabilitar('#cuestionario\\:'+preguntasHabilita[i]);
						cleanRadio(preguntasHabilita[i]);
						regresar= true;
					} // for													
				}
				else
					for(var i = 0; i < preguntasHabilita.length; i++) {				
            habilitar('#cuestionario\\:'+preguntasHabilita[i]);
						cleanRadio(preguntasHabilita[i]);
					} // for
			} // if
			else {								
				for(var i = 0; i < preguntasHabilita.length; i++) {				
          habilitar('#cuestionario\\:'+preguntasHabilita[i]);
				} // for																														
			} // else			
			if(regresar)
				break;
		} // for			
	};
	
	function deshabilitarPreguntasRadioInput(pregunta, listaRespuestas, listaPreguntas, radio) {
		var regresar  = false;
		var respuestas= listaRespuestas.split("|");
		var preguntas = listaPreguntas.split("|");
		for(var x = 0; x < respuestas.length; x++) {				
		  var preguntasHabilita= preguntas[x].split(",");
			if($('input:radio[id=cuestionario\\:'+ pregunta+ '\\:'+ radio).val()!== undefined) {
				var valor= $('input:radio[id=cuestionario\\:'+ pregunta+ '\\:'+ radio).val();
				if(respuestas[x].indexOf(valor)> 0) {
					for(var i = 0; i < preguntasHabilita.length; i++) {
            deshabilitar('#cuestionario\\:'+preguntasHabilita[i]);
						cleanRadio(preguntasHabilita[i], false);
						regresar= true;
					} // for													
				} // if
				else {
					for(var i = 0; i < preguntasHabilita.length; i++) {				
            habilitar('#cuestionario\\:'+preguntasHabilita[i]);
					} // for
				} // else
			} // if
			else {
				for(var i = 0; i < preguntasHabilita.length; i++) {				
          habilitar('#cuestionario\\:'+preguntasHabilita[i]);
				} // for													
			} // else			
			if(regresar)
				break;
		} // for	
	}
  
	function deshabilitarPreguntasCheckInput(pregunta, listaRespuestas, listaPreguntas, radio) {
		var regresar  = false;
		var respuestas= listaRespuestas.split("|");
		var preguntas = listaPreguntas.split("|");
		for(var x = 0; x < respuestas.length; x++) {				
		  var preguntasHabilita= preguntas[x].split(",");
			if($('#cuestionario\\:'+ pregunta+ '\\:'+ radio).prop('checked')) {								
				for(var i = 0; i < preguntasHabilita.length; i++) {				
          habilitar('#cuestionario\\:'+preguntasHabilita[i]);
				} // for				
			} // if
			else {
				for(var i = 0; i < preguntasHabilita.length; i++) {
          deshabilitar('#cuestionario\\:'+preguntasHabilita[i]);
					cleanRadio(preguntasHabilita[i], false);
					regresar= true;
				} // for													
			} // else						
			if(regresar)
				break;
		} // for	
	}
	
	function cleanRadioPregunta13() {
		var preguntas= 'pagina4_13_1,pagina4_13_2,pagina4_13_3,pagina4_13_4,pagina4_13_5,pagina4_13_6,pagina4_13_7'.split(",");
		for(var x= 0; x< preguntas.length; x++) {
			$('#cuestionario\\:'+ preguntas[x]).find('.ui-radiobutton-icon').removeClass("ui-state-bullet");				
			$('#cuestionario\\:'+ preguntas[x]).find('.ui-radiobutton-icon').addClass("ui-icon-blank");
			$('#cuestionario\\:'+ preguntas[x]).find('.ui-radiobutton-box').removeClass("ui-state-active");				
			$('#cuestionario\\:'+preguntas[x]).val(null);
			$('#cuestionario\\:'+preguntas[x]+ '\\:0').removeAttr('checked','checked');			
		} // for
    deshabilitar('#cuestionario\\:pagina4_13_especifique');
	}
	
	function cleanRadio(componente){
		var i= 0;
		$('#cuestionario\\:'+ componente).find('.ui-radiobutton-icon').removeClass("ui-state-bullet");				
		$('#cuestionario\\:'+ componente).find('.ui-radiobutton-icon').addClass("ui-icon-blank");
		$('#cuestionario\\:'+ componente).find('.ui-radiobutton-box').removeClass("ui-state-active");				
		$('#cuestionario\\:'+componente).val(null);
		while($('#cuestionario\\:'+ componente+ '\\:'+ i).val()!== undefined) {			
			$('#cuestionario\\:'+componente+ '\\:'+ i).removeAttr('checked','checked');			
			i++;
		};			
	}; // cleanRadio
	
$(document).ready(function(){	
	console.info("Janal.Cuestionario inicializado");
});
