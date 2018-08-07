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
@Table(name="tc_mantic_garantias_estatus")
public class TcManticGarantiasEstatusDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_justificacion")
  private Long idJustificacion;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_garantia_estatus")
  private Long idGarantiaEstatus;
  @Column (name="estatus_asociados")
  private String estatusAsociados;
  @Column (name="decripcion")
  private String decripcion;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticGarantiasEstatusDto() {
    this(new Long(-1L));
  }

  public TcManticGarantiasEstatusDto(Long key) {
    this(null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticGarantiasEstatusDto(Long idJustificacion, Long idGarantiaEstatus, String estatusAsociados, String decripcion, String nombre) {
    setIdJustificacion(idJustificacion);
    setIdGarantiaEstatus(idGarantiaEstatus);
    setEstatusAsociados(estatusAsociados);
    setDecripcion(decripcion);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdJustificacion(Long idJustificacion) {
    this.idJustificacion = idJustificacion;
  }

  public Long getIdJustificacion() {
    return idJustificacion;
  }

  public void setIdGarantiaEstatus(Long idGarantiaEstatus) {
    this.idGarantiaEstatus = idGarantiaEstatus;
  }

  public Long getIdGarantiaEstatus() {
    return idGarantiaEstatus;
  }

  public void setEstatusAsociados(String estatusAsociados) {
    this.estatusAsociados = estatusAsociados;
  }

  public String getEstatusAsociados() {
    return estatusAsociados;
  }

  public void setDecripcion(String decripcion) {
    this.decripcion = decripcion;
  }

  public String getDecripcion() {
    return decripcion;
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
  	return getIdGarantiaEstatus();
  }

  @Override
  public void setKey(Long key) {
  	this.idGarantiaEstatus = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGarantiaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstatusAsociados());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDecripcion());
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
		regresar.put("idJustificacion", getIdJustificacion());
		regresar.put("idGarantiaEstatus", getIdGarantiaEstatus());
		regresar.put("estatusAsociados", getEstatusAsociados());
		regresar.put("decripcion", getDecripcion());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdJustificacion(), getIdGarantiaEstatus(), getEstatusAsociados(), getDecripcion(), getNombre(), getRegistro()
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
    regresar.append("idGarantiaEstatus~");
    regresar.append(getIdGarantiaEstatus());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdGarantiaEstatus());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticGarantiasEstatusDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdGarantiaEstatus()!= null && getIdGarantiaEstatus()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticGarantiasEstatusDto other = (TcManticGarantiasEstatusDto) obj;
    if (getIdGarantiaEstatus() != other.idGarantiaEstatus && (getIdGarantiaEstatus() == null || !getIdGarantiaEstatus().equals(other.idGarantiaEstatus))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdGarantiaEstatus() != null ? getIdGarantiaEstatus().hashCode() : 0);
    return hash;
  }

}


