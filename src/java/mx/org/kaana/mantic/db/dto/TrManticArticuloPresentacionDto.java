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
@Table(name="tr_mantic_articulo_presentacion")
public class TrManticArticuloPresentacionDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_presentacion")
  private Long idArticuloPresentacion;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_registro")
  private Long idRegistro;
  @Column (name="cantidad")
  private Long cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_presentacion_unidad_medida")
  private Long idPresentacionUnidadMedida;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticArticuloPresentacionDto() {
    this(new Long(-1L));
  }

  public TrManticArticuloPresentacionDto(Long key) {
    this(new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TrManticArticuloPresentacionDto(Long idArticuloPresentacion, String observaciones, Long idRegistro, Long cantidad, Long idArticulo, Long idPresentacionUnidadMedida) {
    setIdArticuloPresentacion(idArticuloPresentacion);
    setObservaciones(observaciones);
    setIdRegistro(idRegistro);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setIdPresentacionUnidadMedida(idPresentacionUnidadMedida);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdArticuloPresentacion(Long idArticuloPresentacion) {
    this.idArticuloPresentacion = idArticuloPresentacion;
  }

  public Long getIdArticuloPresentacion() {
    return idArticuloPresentacion;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdRegistro(Long idRegistro) {
    this.idRegistro = idRegistro;
  }

  public Long getIdRegistro() {
    return idRegistro;
  }

  public void setCantidad(Long cantidad) {
    this.cantidad = cantidad;
  }

  public Long getCantidad() {
    return cantidad;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setIdPresentacionUnidadMedida(Long idPresentacionUnidadMedida) {
    this.idPresentacionUnidadMedida = idPresentacionUnidadMedida;
  }

  public Long getIdPresentacionUnidadMedida() {
    return idPresentacionUnidadMedida;
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
  	return getIdArticuloPresentacion();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloPresentacion = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdArticuloPresentacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPresentacionUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idArticuloPresentacion", getIdArticuloPresentacion());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idRegistro", getIdRegistro());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idPresentacionUnidadMedida", getIdPresentacionUnidadMedida());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdArticuloPresentacion(), getObservaciones(), getIdRegistro(), getCantidad(), getIdArticulo(), getIdPresentacionUnidadMedida(), getRegistro()
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
    regresar.append("idArticuloPresentacion~");
    regresar.append(getIdArticuloPresentacion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloPresentacion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticArticuloPresentacionDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloPresentacion()!= null && getIdArticuloPresentacion()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticArticuloPresentacionDto other = (TrManticArticuloPresentacionDto) obj;
    if (getIdArticuloPresentacion() != other.idArticuloPresentacion && (getIdArticuloPresentacion() == null || !getIdArticuloPresentacion().equals(other.idArticuloPresentacion))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloPresentacion() != null ? getIdArticuloPresentacion().hashCode() : 0);
    return hash;
  }

}


