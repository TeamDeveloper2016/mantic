package mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans;

import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.enums.EComponente;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoDato;
import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 3/12/2014
 *@time 10:12:20 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Campo implements Serializable {
	
	private static final long serialVersionUID=7518455407626393020L;		
	
	private String nombre;
	private String alias;
	private Boolean busqueda;
	private Boolean tabla;
	private EComponente componente;
	private String alineacion;
	private Long ordenBusqueda;
	private Long ordenTabla;
	private EFormatoDinamicos formato;
	private ETipoDato tipo;
	private String initSelectOneMenu;
	private String converter;
	private String contenido;
	private String valorContenido;

	public Campo() {	
	}
	
	public Campo(String nombre, ETipoDato tipo) {	
		this(nombre, Cadena.letraCapital(nombre), false,false, null, "alinear-izquierda", null, null, EFormatoDinamicos.LIBRE, tipo, null, "Fecha", "", "");
	}
	
	private Campo(String nombre, String alias, Boolean busqueda, Boolean tabla, EComponente componente, String alineacion, Long ordenBusqueda, Long ordenTabla, EFormatoDinamicos formato, ETipoDato tipo, String initSelectOneMenu, String converter, String contenido, String valorContenido) {
		this.nombre						= nombre;
		this.alias						= alias;
		this.busqueda					= busqueda;
		this.tabla						= tabla;
		this.componente				= componente;
		this.alineacion				= alineacion;
		this.ordenBusqueda		= ordenBusqueda;
		this.ordenTabla				= ordenTabla;
		this.formato				  = formato;
		this.tipo             = tipo;
		this.initSelectOneMenu=initSelectOneMenu;
		this.converter        = converter;
		this.contenido        = contenido;
        this.valorContenido   = valorContenido;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido=contenido;
	}

	public String getConverter() {
		return converter;
	}

	public void setConverter(String converter) {
		this.converter=converter;
	}

	public ETipoDato getTipo() {
		return tipo;
	}

	public void setTipo(ETipoDato tipo) {
		this.tipo=tipo;
	}

	public Boolean getBusqueda() {
		return busqueda;
	}

	public Long getOrdenBusqueda() {
		return ordenBusqueda;
	}

	public void setOrdenBusqueda(Long ordenBusqueda) {
		this.ordenBusqueda=ordenBusqueda;
	}

	public Long getOrdenTabla() {
		return ordenTabla;
	}

	public void setOrdenTabla(Long ordenTabla) {
		this.ordenTabla=ordenTabla;
	}

	public void setBusqueda(Boolean busqueda) {
		this.busqueda=busqueda;
	}

	public Boolean getTabla() {
		return tabla;
	}

	public void setTabla(Boolean tabla) {
		this.tabla=tabla;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias=alias;
	}

	public EComponente getComponente() {
		return componente;
	}

	public void setComponente(EComponente componente) {
		this.componente=componente;
	}

	public String getAlineacion() {
		return alineacion;
	}

	public void setAlineacion(String alineacion) {
		this.alineacion=alineacion;
	}

	public EFormatoDinamicos getFormato() {
		return formato;
	}

	public void setFormato(EFormatoDinamicos formato) {
		this.formato=formato;
	}

	public String getInitSelectOneMenu() {
		return initSelectOneMenu;
	}

	public void setInitSelectOneMenu(String initSelectOneMenu) {
		this.initSelectOneMenu=initSelectOneMenu;
	}

  public String getValorContenido() {
    return valorContenido;
  }

  public void setValorContenido(String valorContenido) {
    this.valorContenido = valorContenido;
  }

	@Override
	public String toString() {
		StringBuilder regresar = new StringBuilder();
		regresar.append("|");
		regresar.append(this.nombre);
		regresar.append("|");
		regresar.append(this.alias);
		regresar.append("|");
		regresar.append(this.busqueda);
		regresar.append("|");
		regresar.append(this.tabla);
		regresar.append("|");
		regresar.append(this.componente);
		regresar.append("|");
		regresar.append(this.ordenBusqueda);
		regresar.append("|");
		regresar.append(this.ordenTabla);	
  	regresar.append("|");
		regresar.append(this.formato);	
  	regresar.append("|");
		regresar.append(this.tipo);	
  	regresar.append("|");
		regresar.append(this.initSelectOneMenu);	
  	regresar.append("|");			
		return regresar.toString();
	}
}
