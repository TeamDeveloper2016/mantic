package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Lob;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_articulos_impuestos")
public class TcManticArticulosImpuestosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="codigo")
  private String codigo;
  @Column (name="orden")
  private Long orden;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_impuesto")
  private Long idArticuloImpuesto;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticArticulosImpuestosDto() {
    this(new Long(-1L));
  }

  public TcManticArticulosImpuestosDto(Long key) {
    this(null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticArticulosImpuestosDto(String codigo, Long orden, Long idArticuloImpuesto, String nombre) {
    setCodigo(codigo);
    setOrden(orden);
    setIdArticuloImpuesto(idArticuloImpuesto);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdArticuloImpuesto(Long idArticuloImpuesto) {
    this.idArticuloImpuesto = idArticuloImpuesto;
  }

  public Long getIdArticuloImpuesto() {
    return idArticuloImpuesto;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdArticuloImpuesto();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloImpuesto = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloImpuesto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("orden", getOrden());
		regresar.put("idArticuloImpuesto", getIdArticuloImpuesto());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCodigo(), getOrden(), getIdArticuloImpuesto(), getNombre(), getRegistro()
    };
    return regresar;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idArticuloImpuesto~");
    regresar.append(getIdArticuloImpuesto());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloImpuesto());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticArticulosImpuestosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloImpuesto()!= null && getIdArticuloImpuesto()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticArticulosImpuestosDto other = (TcManticArticulosImpuestosDto) obj;
    if (getIdArticuloImpuesto() != other.idArticuloImpuesto && (getIdArticuloImpuesto() == null || !getIdArticuloImpuesto().equals(other.idArticuloImpuesto))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloImpuesto() != null ? getIdArticuloImpuesto().hashCode() : 0);
    return hash;
  }

}


