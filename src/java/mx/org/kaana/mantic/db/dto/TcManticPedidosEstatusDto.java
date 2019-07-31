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
@Table(name="tc_mantic_pedidos_estatus")
public class TcManticPedidosEstatusDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="id_justificacion")
  private Long idJustificacion;
  @Column (name="estatus_asociado")
  private String estatusAsociado;
  @Column (name="nombre")
  private String nombre;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_pedido_estatus")
  private Long idPedidoEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticPedidosEstatusDto() {
    this(new Long(-1L));
  }

  public TcManticPedidosEstatusDto(Long key) {
    this(null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticPedidosEstatusDto(String descripcion, Long idJustificacion, String estatusAsociado, String nombre, Long idPedidoEstatus) {
    setDescripcion(descripcion);
    setIdJustificacion(idJustificacion);
    setEstatusAsociado(estatusAsociado);
    setNombre(nombre);
    setIdPedidoEstatus(idPedidoEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdJustificacion(Long idJustificacion) {
    this.idJustificacion = idJustificacion;
  }

  public Long getIdJustificacion() {
    return idJustificacion;
  }

  public void setEstatusAsociado(String estatusAsociado) {
    this.estatusAsociado = estatusAsociado;
  }

  public String getEstatusAsociado() {
    return estatusAsociado;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setIdPedidoEstatus(Long idPedidoEstatus) {
    this.idPedidoEstatus = idPedidoEstatus;
  }

  public Long getIdPedidoEstatus() {
    return idPedidoEstatus;
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
  	return getIdPedidoEstatus();
  }

  @Override
  public void setKey(Long key) {
  	this.idPedidoEstatus = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstatusAsociado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPedidoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("idJustificacion", getIdJustificacion());
		regresar.put("estatusAsociado", getEstatusAsociado());
		regresar.put("nombre", getNombre());
		regresar.put("idPedidoEstatus", getIdPedidoEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getIdJustificacion(), getEstatusAsociado(), getNombre(), getIdPedidoEstatus(), getRegistro()
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
    regresar.append("idPedidoEstatus~");
    regresar.append(getIdPedidoEstatus());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPedidoEstatus());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticPedidosEstatusDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPedidoEstatus()!= null && getIdPedidoEstatus()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticPedidosEstatusDto other = (TcManticPedidosEstatusDto) obj;
    if (getIdPedidoEstatus() != other.idPedidoEstatus && (getIdPedidoEstatus() == null || !getIdPedidoEstatus().equals(other.idPedidoEstatus))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPedidoEstatus() != null ? getIdPedidoEstatus().hashCode() : 0);
    return hash;
  }

}


