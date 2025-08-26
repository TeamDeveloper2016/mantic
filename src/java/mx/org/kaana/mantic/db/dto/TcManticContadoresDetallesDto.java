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
@Table(name="tc_mantic_contadores_detalles")
public class TcManticContadoresDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="fecha")
  private String fecha;
  @Column (name="codigo")
  private String codigo;
  @Column (name="procesado")
  private Timestamp procesado;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_contador_detalle")
  private Long idContadorDetalle;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_contador")
  private Long idContador;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticContadoresDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticContadoresDetallesDto(Long key) {
    this(null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), new Long(-1L), 0D, null, null, null);
    setKey(key);
  }

  public TcManticContadoresDetallesDto(String fecha, String codigo, Timestamp procesado, Long idContadorDetalle, Double cantidad, Long idContador, Long idArticulo, String nombre) {
    setFecha(fecha);
    setCodigo(codigo);
    setProcesado(procesado);
    setIdContadorDetalle(idContadorDetalle);
    setCantidad(cantidad);
    setIdContador(idContador);
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

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setProcesado(Timestamp procesado) {
    this.procesado = procesado;
  }

  public Timestamp getProcesado() {
    return procesado;
  }

  public void setIdContadorDetalle(Long idContadorDetalle) {
    this.idContadorDetalle = idContadorDetalle;
  }

  public Long getIdContadorDetalle() {
    return idContadorDetalle;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdContador(Long idContador) {
    this.idContador = idContador;
  }

  public Long getIdContador() {
    return idContador;
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
  	return getIdContadorDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idContadorDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProcesado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdContadorDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdContador());
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
		regresar.put("codigo", getCodigo());
		regresar.put("procesado", getProcesado());
		regresar.put("idContadorDetalle", getIdContadorDetalle());
		regresar.put("cantidad", getCantidad());
		regresar.put("idContador", getIdContador());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getFecha(), getCodigo(), getProcesado(), getIdContadorDetalle(), getCantidad(), getIdContador(), getIdArticulo(), getNombre(), getRegistro()
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
    regresar.append("idContadorDetalle~");
    regresar.append(getIdContadorDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdContadorDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticContadoresDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdContadorDetalle()!= null && getIdContadorDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticContadoresDetallesDto other = (TcManticContadoresDetallesDto) obj;
    if (getIdContadorDetalle() != other.idContadorDetalle && (getIdContadorDetalle() == null || !getIdContadorDetalle().equals(other.idContadorDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdContadorDetalle() != null ? getIdContadorDetalle().hashCode() : 0);
    return hash;
  }

}


