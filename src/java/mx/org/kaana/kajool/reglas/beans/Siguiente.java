package mx.org.kaana.kajool.reglas.beans;

import java.io.Serializable;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.recurso.Configuracion;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/05/2019
 *@time 09:14:34 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Siguiente implements Serializable {

	private static final long serialVersionUID=-5723123128448265321L;

	private String consecutivo;
	private String temporal;
	private Long orden;

	public Siguiente(Long orden) {
	  this(Cadena.rellenar(String.valueOf(orden), Constantes.LENGTH_CONSECUTIVO, '0', true), orden);
	}

	public Siguiente(String consecutivo, Long orden) {
		this.consecutivo= Fecha.getAnioActual()+ consecutivo;
		this.temporal   = consecutivo;
		this.orden      = orden;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public String toConsecutivo() {
		return temporal;
	}

	public Long getOrden() {
		return orden;
	}

	@Override
	public String toString() {
		return "Siguiente{"+"consecutivo="+consecutivo+", orden="+orden+'}';
	}
	
}
