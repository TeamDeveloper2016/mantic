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
@Table(name="tc_mantic_conteo_detalles")
public class TcManticConteoDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="fecha")
  private String fecha;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_conteo_detalle")
  private Long idConteoDetalle;
  @Column (name="id_conteo")
  private Long idConteo;
  @Column (name="procesado")
  private Timestamp procesado;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticConteoDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticConteoDetallesDto(Long key) {
    this(null, new Long(-1L), null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null);
    setKey(key);
  }

  public TcManticConteoDetallesDto(String fecha, Long idConteoDetalle, Long idConteo, Timestamp procesado, Double cantidad, Long idArticulo, String nombre) {
    setFecha(fecha);
    setIdConteoDetalle(idConteoDetalle);
    setIdConteo(idConteo);
    setProcesado(procesado);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }

  public void setIdConteoDetalle(Long idConteoDetalle) {
    this.idConteoDetalle = idConteoDetalle;
  }

  public Long getIdConteoDetalle() {
    return idConteoDetalle;
  }

  public void setIdConteo(Long idConteo) {
    this.idConteo = idConteo;
  }

  public Long getIdConteo() {
    return idConteo;
  }

  public void setProcesado(Timestamp procesado) {
    this.procesado = procesado;
  }

  public Timestamp getProcesado() {
    return procesado;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
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
  	return getIdConteoDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idConteoDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConteoDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConteo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProcesado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
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
		regresar.put("fecha", getFecha());
		regresar.put("idConteoDetalle", getIdConteoDetalle());
		regresar.put("idConteo", getIdConteo());
		regresar.put("procesado", getProcesado());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getFecha(), getIdConteoDetalle(), getIdConteo(), getProcesado(), getCantidad(), getIdArticulo(), getNombre(), getRegistro()
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
    regresar.append("idConteoDetalle~");
    regresar.append(getIdConteoDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdConteoDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticConteoDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdConteoDetalle()!= null && getIdConteoDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticConteoDetallesDto other = (TcManticConteoDetallesDto) obj;
    if (getIdConteoDetalle() != other.idConteoDetalle && (getIdConteoDetalle() == null || !getIdConteoDetalle().equals(other.idConteoDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdConteoDetalle() != null ? getIdConteoDetalle().hashCode() : 0);
    return hash;
  }

}


