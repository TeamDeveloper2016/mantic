package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.libs.reflection.Methods;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/10/2018
 *@time 11:15:22 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class CodigoArticulo implements IBaseDto, Serializable {

	private static final long serialVersionUID=795752101228019032L;
	
	private Long idArticulo;
	private String propio;
	private String nombre;
	private String alias;
	private Long cantidad;
	private Double tope;
	private String codigo;
	private Boolean agregado;

	public CodigoArticulo() {
		this(-1L);
	}
	
	public CodigoArticulo(Long idArticulo) {
		this(idArticulo, null, null);
	}

	public CodigoArticulo(Long idArticulo, String propio, String nombre) {
		this(idArticulo, propio, nombre, 1L, 9999D, "code128", true);
	}
	
	public CodigoArticulo(Long idArticulo, String propio, String nombre, Long cantidad, Double tope, String codigo, Boolean agregado) {
		this.idArticulo=idArticulo;
		this.propio=propio;
		this.nombre=nombre;
		this.alias= "";
		this.cantidad=cantidad;
		this.tope=tope;
		this.codigo=codigo;
		this.agregado=agregado;
	}

	public Long getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Long idArticulo) {
		this.idArticulo=idArticulo;
	}

	public String getPropio() {
		return propio;
	}

	public void setPropio(String propio) {
		this.propio=propio;
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

	public Long getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(Long cantidad) {
		this.cantidad=cantidad;
	}

	public Double getTope() {
		return tope;
	}

	public void setTope(Double tope) {
		this.tope=tope;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodio(String codigo) {
		this.codigo=codigo;
	}

	public Boolean getAgregado() {
		return agregado;
	}

	public void setAgregado(Boolean agregado) {
		this.agregado=agregado;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=97*hash+Objects.hashCode(this.idArticulo);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final CodigoArticulo other=(CodigoArticulo) obj;
		if (!Objects.equals(this.idArticulo, other.idArticulo)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "CodigoArticulo{"+"idArticulo="+idArticulo+", propio="+propio+", nombre="+nombre+", alias="+alias+", cantidad="+cantidad+", tope="+tope+", codigo="+codigo+", agregado="+agregado+'}';
	}

	@Override
	public Long getKey() {
		return this.idArticulo;
	}

	@Override
	public void setKey(Long key) {
		this.idArticulo= key;
	}

	@Override
	public Map<String, Object> toMap() {
    Map regresar = new HashMap();
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("nombre", getNombre());
		regresar.put("alias", getAlias());
		regresar.put("propio", getPropio());
		regresar.put("cantidad", getCantidad());
		regresar.put("tope", getTope());
		regresar.put("agregado", getAgregado());
		regresar.put("codigo", getCodigo());
  	return regresar;
	}

	@Override
	public Object[] toArray() {
    Object[] regresar = new Object[] {
      getNombre(), getAlias(), getIdArticulo(), getCodigo(), getPropio(), getAgregado(), getCantidad(), getTope()
    };
    return regresar;
	}

	@Override
	public boolean isValid() {
		return this.idArticulo> 0;
	}

	@Override
	public Object toValue(String name) {
    return Methods.getValue(this, name);
	}

	@Override
	public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idArticulo~");
    regresar.append(this.idArticulo);
    regresar.append("|");
    return regresar.toString();
	}

	@Override
	public String toKeys() {
		return String.valueOf(this.idArticulo);
	}

	@Override
	public Class toHbmClass() {
		return null;
	}

}
