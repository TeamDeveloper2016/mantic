package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/11/2016
 *@time 10:33:27 AM 
 *@author Usuario <usuario.usuario@inegi.org.mx>
 */

public enum ERespuestas {
		
	ENTREVISTA_COMPLETA		(1L ,"Entrevista completa"),
	AUSENCIA_INFORMANTES	(2L ,"Ausencia de informante adecuado al momento de la visita"),                 
	NEGO_INFORMACION			(3L ,"Se neg� a dar informaci�n"),
	ENTREVISTA_INCOMPLETA	(4L ,"Entrevista incompleta"),	
	NO_VIVE_DOMICILIO			(5L ,"La persona titular del Programa en el hogar no vive en el domicilio y no se localiz�"),
	NO_RECIBE_APOYO				(6L ,"La persona titular del Programa en el hogar vive en el domicilio pero dijo que no recibe ni recibi� apoyo(s)"),
	VIVIENDA_DESHABITADA	(7L ,"Vivienda deshabitada o de uso temporal"),
	NO_EXISTE_DOMICILIO		(8L ,"No existe o no se localiz� el domicilio"),                  
	PROBLEMAS_ACCESO			(9L ,"Problemas de acceso al �rea"),  	
	NADIE_CASA      			(10L ,"Nadie en casa");  	

  private Long idResultado;  
	private String descripcion;

  private ERespuestas(Long idResultado, String descripcion) {
    this.idResultado= idResultado;
		this.descripcion= descripcion;
	}

	public Long getIdResultado() {
		return idResultado;
	}

	public String getDescripcion() {
		return descripcion;
	}

}
