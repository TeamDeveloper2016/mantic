package mx.org.kaana.kajool.mantenimiento.gestion.asistente.acciones.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.kajool.enums.EComponente;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/03/2015
 *@time 03:15:46 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Campo implements Serializable {
	
  private static final long serialVersionUID=4523213545394401799L;
	
	private String nombre;
	private String mascara;
	private String [] validacion;
	private Long   orden;
	private String   alias;
	private EComponente  tipoComponente;
	private String initSelectOneMenu;
	private String converter;
  private Class tipoDato;
  private Class dtoAlQuePertenece;
  private String llave;
  private String valorContenido;

	public Campo(String nombre) {
		this(nombre, "no-aplica",null,nombre, EComponente.TEXT_FIELD, "Fecha",null, null, null, "");
	}

	public Campo(String nombre, Class tipoDato, Class dtoAlQuePertenece, String llave ) {
		this(nombre, "no-aplica",null,nombre, EComponente.TEXT_FIELD, "Fecha",tipoDato, dtoAlQuePertenece, llave, "");
	}
	
	public Campo(String nombre, String mascara, String [] validacion,String alias, EComponente tipoComponente, String converter, Class tipoDato, Class dtoAlQuePertenece, String llave, String valorContenido) {
		this.nombre           = nombre;
		this.mascara          = mascara;
		this.validacion       = validacion;
		this.alias            = alias;
		this.tipoComponente   = tipoComponente;
		this.converter        = converter;
		this.tipoDato         = tipoDato;
        this.dtoAlQuePertenece= dtoAlQuePertenece;
        this.llave            = llave;
        this.valorContenido= valorContenido;
	}

  public String getLlave() {
    return llave;
  }

  public void setLlave(String llave) {
    this.llave = llave;
  }

	public String getConverter() {
		return converter;
	}

	public void setConverter(String converter) {
		this.converter=converter;
	}
	
	public EComponente getTipoComponente() {
		return tipoComponente;
	}

	public void setTipoComponente(EComponente tipoComponente) {
		this.tipoComponente=tipoComponente;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias=alias;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public String getMascara() {
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara=mascara;
	}

	public String [] getValidacion() {
		return validacion;
	}

	public void setValidacion(String [] validacion) {
		this.validacion=validacion;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden=orden;
	}
	
	public String getInitSelectOneMenu() {
		return initSelectOneMenu;
	}

	public void setInitSelectOneMenu(String initSelectOneMenu) {
		this.initSelectOneMenu=initSelectOneMenu;
	}

  public Class getTipoDato() {
    return tipoDato;
  }

  public void setTipoDato(Class tipoDato) {
    this.tipoDato = tipoDato;
  }

  public Class getDtoAlQuePertenece() {
    return dtoAlQuePertenece;
  }

  public void setDtoAlQuePertenece(Class dtoAlQuePertenece) {
    this.dtoAlQuePertenece = dtoAlQuePertenece;
  }

    public String getValorContenido() {
    return valorContenido;
  }

  public void setValorContenido(String valorContenido) {
    this.valorContenido = valorContenido;
  }

  @Override
  public String toString() {
    return "Campo{" + "nombre=" + nombre + ", mascara=" + mascara + ", validacion=" + validacion + ", orden=" + orden + ", alias=" + alias + ", tipoComponente=" + tipoComponente + ", initSelectOneMenu=" + initSelectOneMenu + ", converter=" + converter + ", tipoDato=" + tipoDato + ", dtoAlQuePertenece=" + dtoAlQuePertenece + ", llave=" + llave + '}';
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.nombre);
    hash = 53 * hash + Objects.hashCode(this.tipoDato);
    hash = 53 * hash + Objects.hashCode(this.dtoAlQuePertenece);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Campo other = (Campo) obj;
    if (!Objects.equals(this.nombre, other.nombre)) {
      return false;
    }
    if (!Objects.equals(this.tipoDato, other.tipoDato)) {
      return false;
    }
    if (!Objects.equals(this.dtoAlQuePertenece, other.dtoAlQuePertenece)) {
      return false;
    }
    return true;
  }
}
