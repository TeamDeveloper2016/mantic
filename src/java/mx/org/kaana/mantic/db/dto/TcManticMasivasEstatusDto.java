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
@Table(name="tc_mantic_masivas_estatus")
public class TcManticMasivasEstatusDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_justificacion")
  private Long idJustificacion;
  @Column (name="estatus_asociados")
  private String estatusAsociados;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_masiva_estatus")
  private Long idMasivaEstatus;
  @Column (name="decripcion")
  private String decripcion;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticMasivasEstatusDto() {
    this(new Long(-1L));
  }

  public TcManticMasivasEstatusDto(Long key) {
    this(null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticMasivasEstatusDto(Long idJustificacion, String estatusAsociados, Long idMasivaEstatus, String decripcion, String nombre) {
    setIdJustificacion(idJustificacion);
    setEstatusAsociados(estatusAsociados);
    setIdMasivaEstatus(idMasivaEstatus);
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

  public void setEstatusAsociados(String estatusAsociados) {
    this.estatusAsociados = estatusAsociados;
  }

  public String getEstatusAsociados() {
    return estatusAsociados;
  }

  public void setIdMasivaEstatus(Long idMasivaEstatus) {
    this.idMasivaEstatus = idMasivaEstatus;
  }

  public Long getIdMasivaEstatus() {
    return idMasivaEstatus;
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
  	return getIdMasivaEstatus();
  }

  @Override
  public void setKey(Long key) {
  	this.idMasivaEstatus = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstatusAsociados());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMasivaEstatus());
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
		regresar.put("estatusAsociados", getEstatusAsociados());
		regresar.put("idMasivaEstatus", getIdMasivaEstatus());
		regresar.put("decripcion", getDecripcion());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdJustificacion(), getEstatusAsociados(), getIdMasivaEstatus(), getDecripcion(), getNombre(), getRegistro()
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
    regresar.append("idMasivaEstatus~");
    regresar.append(getIdMasivaEstatus());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMasivaEstatus());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticMasivasEstatusDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMasivaEstatus()!= null && getIdMasivaEstatus()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticMasivasEstatusDto other = (TcManticMasivasEstatusDto) obj;
    if (getIdMasivaEstatus() != other.idMasivaEstatus && (getIdMasivaEstatus() == null || !getIdMasivaEstatus().equals(other.idMasivaEstatus))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMasivaEstatus() != null ? getIdMasivaEstatus().hashCode() : 0);
    return hash;
  }

}


