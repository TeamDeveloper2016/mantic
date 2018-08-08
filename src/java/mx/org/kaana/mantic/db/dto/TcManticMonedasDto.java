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
@Table(name="tc_mantic_monedas")
public class TcManticMonedasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="denominacion")
  private Double denominacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="orden")
  private Long orden;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_moneda")
  private Long idMoneda;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticMonedasDto() {
    this(new Long(-1L));
  }

  public TcManticMonedasDto(Long key) {
    this(null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticMonedasDto(Double denominacion, Long idUsuario, Long orden, Long idMoneda, String nombre) {
    setDenominacion(denominacion);
    setIdUsuario(idUsuario);
    setOrden(orden);
    setIdMoneda(idMoneda);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDenominacion(Double denominacion) {
    this.denominacion = denominacion;
  }

  public Double getDenominacion() {
    return denominacion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdMoneda(Long idMoneda) {
    this.idMoneda = idMoneda;
  }

  public Long getIdMoneda() {
    return idMoneda;
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
  	return getIdMoneda();
  }

  @Override
  public void setKey(Long key) {
  	this.idMoneda = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDenominacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMoneda());
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
		regresar.put("denominacion", getDenominacion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("orden", getOrden());
		regresar.put("idMoneda", getIdMoneda());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDenominacion(), getIdUsuario(), getOrden(), getIdMoneda(), getNombre(), getRegistro()
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
    regresar.append("idMoneda~");
    regresar.append(getIdMoneda());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMoneda());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticMonedasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMoneda()!= null && getIdMoneda()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticMonedasDto other = (TcManticMonedasDto) obj;
    if (getIdMoneda() != other.idMoneda && (getIdMoneda() == null || !getIdMoneda().equals(other.idMoneda))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMoneda() != null ? getIdMoneda().hashCode() : 0);
    return hash;
  }

}


