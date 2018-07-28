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
@Table(name="tc_mantic_requisiciones_detalles")
public class TcManticRequisicionesDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_requisicion_detalle")
  private Long idRequisicionDetalle;
  @Column (name="propio")
  private String propio;
  @Column (name="unidad_medida")
  private String unidadMedida;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_requisicion")
  private Long idRequisicion;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticRequisicionesDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticRequisicionesDetallesDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticRequisicionesDetallesDto(Long idRequisicionDetalle, String propio, String unidadMedida, Double cantidad, Long idArticulo, Long idRequisicion, String nombre) {
    setIdRequisicionDetalle(idRequisicionDetalle);
    setPropio(propio);
    setUnidadMedida(unidadMedida);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setIdRequisicion(idRequisicion);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdRequisicionDetalle(Long idRequisicionDetalle) {
    this.idRequisicionDetalle = idRequisicionDetalle;
  }

  public Long getIdRequisicionDetalle() {
    return idRequisicionDetalle;
  }

  public void setPropio(String propio) {
    this.propio = propio;
  }

  public String getPropio() {
    return propio;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public String getUnidadMedida() {
    return unidadMedida;
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

  public void setIdRequisicion(Long idRequisicion) {
    this.idRequisicion = idRequisicion;
  }

  public Long getIdRequisicion() {
    return idRequisicion;
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
  	return getIdRequisicionDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idRequisicionDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdRequisicionDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPropio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRequisicion());
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
		regresar.put("idRequisicionDetalle", getIdRequisicionDetalle());
		regresar.put("propio", getPropio());
		regresar.put("unidadMedida", getUnidadMedida());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idRequisicion", getIdRequisicion());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdRequisicionDetalle(), getPropio(), getUnidadMedida(), getCantidad(), getIdArticulo(), getIdRequisicion(), getNombre(), getRegistro()
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
    regresar.append("idRequisicionDetalle~");
    regresar.append(getIdRequisicionDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdRequisicionDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticRequisicionesDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdRequisicionDetalle()!= null && getIdRequisicionDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticRequisicionesDetallesDto other = (TcManticRequisicionesDetallesDto) obj;
    if (getIdRequisicionDetalle() != other.idRequisicionDetalle && (getIdRequisicionDetalle() == null || !getIdRequisicionDetalle().equals(other.idRequisicionDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdRequisicionDetalle() != null ? getIdRequisicionDetalle().hashCode() : 0);
    return hash;
  }

}


