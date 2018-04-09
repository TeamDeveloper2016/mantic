package mx.org.kaana.kajool.servicios.ws.publicar.beans;

import java.io.Serializable;

/**
 *@company INEGI
 *@project IKTAN (Sistema de seguimiento y control de proyectos)
 *@date 23/11/2016
 *@time 04:25:52 PM 
 *@author Usuario <usuario.usuario@inegi.org.mx>
 */

public class RespuestaMovil implements Serializable {
	
	private String codigo;
	private String mensaje;		
	
	public RespuestaMovil(String codigo, String mensaje) {
		this.codigo = codigo;
		this.mensaje= mensaje;
	}
	
	/* CODIGOS
				01	ENTREVISTA_COMPLETA
				94	FALTA_CUESTIONARIO
				95	FALTAN_VISITAS
				96	FALTAN_VISITAS_CUESTTIONARIO
				97	ERROR_PARAMETROS
				98	ERROR_JSON
				99	ERROR_INTEGRACION
	*/

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo=codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje=mensaje;
	}

}
