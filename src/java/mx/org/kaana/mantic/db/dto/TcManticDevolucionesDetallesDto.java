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
@Table(name="tc_mantic_devoluciones_detalles")
public class TcManticDevolucionesDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_devolucion")
  private Long idDevolucion;
  @Column (name="id_nota_detalle")
  private Long idNotaDetalle;
  @Column (name="cantidad")
  private Double cantidad;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_devolucion_detalle")
  private Long idDevolucionDetalle;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticDevolucionesDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticDevolucionesDetallesDto(Long key) {
    this(null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticDevolucionesDetallesDto(Long idDevolucion, Long idNotaDetalle, Double cantidad, Long idDevolucionDetalle) {
    setIdDevolucion(idDevolucion);
    setIdNotaDetalle(idNotaDetalle);
    setCantidad(cantidad);
    setIdDevolucionDetalle(idDevolucionDetalle);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdDevolucion(Long idDevolucion) {
    this.idDevolucion = idDevolucion;
  }

  public Long getIdDevolucion() {
    return idDevolucion;
  }

  public void setIdNotaDetalle(Long idNotaDetalle) {
    this.idNotaDetalle = idNotaDetalle;
  }

  public Long getIdNotaDetalle() {
    return idNotaDetalle;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdDevolucionDetalle(Long idDevolucionDetalle) {
    this.idDevolucionDetalle = idDevolucionDetalle;
  }

  public Long getIdDevolucionDetalle() {
    return idDevolucionDetalle;
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
  	return getIdDevolucionDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idDevolucionDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdDevolucion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDevolucionDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idDevolucion", getIdDevolucion());
		regresar.put("idNotaDetalle", getIdNotaDetalle());
		regresar.put("cantidad", getCantidad());
		regresar.put("idDevolucionDetalle", getIdDevolucionDetalle());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdDevolucion(), getIdNotaDetalle(), getCantidad(), getIdDevolucionDetalle(), getRegistro()
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
    regresar.append("idDevolucionDetalle~");
    regresar.append(getIdDevolucionDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDevolucionDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDevolucionesDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDevolucionDetalle()!= null && getIdDevolucionDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDevolucionesDetallesDto other = (TcManticDevolucionesDetallesDto) obj;
    if (getIdDevolucionDetalle() != other.idDevolucionDetalle && (getIdDevolucionDetalle() == null || !getIdDevolucionDetalle().equals(other.idDevolucionDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDevolucionDetalle() != null ? getIdDevolucionDetalle().hashCode() : 0);
    return hash;
  }

}


