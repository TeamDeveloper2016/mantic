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
@Table(name="tr_mantic_empresa_personal")
public class TrManticEmpresaPersonalDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_persona")
  private Long idPersona;
  @Column (name="id_puesto")
  private Long idPuesto;
  @Column (name="fecha_contratacion")
  private Date fechaContratacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_empresa_persona")
  private Long idEmpresaPersona;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticEmpresaPersonalDto() {
    this(new Long(-1L));
  }

  public TrManticEmpresaPersonalDto(Long key) {
    this(null, null, new Date(Calendar.getInstance().getTimeInMillis()), null, new Long(-1L), null, null);
    setKey(key);
  }

  public TrManticEmpresaPersonalDto(Long idPersona, Long idPuesto, Date fechaContratacion, Long idUsuario, Long idEmpresaPersona, String observaciones, Long idEmpresa) {
    setIdPersona(idPersona);
    setIdPuesto(idPuesto);
    setFechaContratacion(fechaContratacion);
    setIdUsuario(idUsuario);
    setIdEmpresaPersona(idEmpresaPersona);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdPersona(Long idPersona) {
    this.idPersona = idPersona;
  }

  public Long getIdPersona() {
    return idPersona;
  }

  public void setIdPuesto(Long idPuesto) {
    this.idPuesto = idPuesto;
  }

  public Long getIdPuesto() {
    return idPuesto;
  }

  public void setFechaContratacion(Date fechaContratacion) {
    this.fechaContratacion = fechaContratacion;
  }

  public Date getFechaContratacion() {
    return fechaContratacion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdEmpresaPersona(Long idEmpresaPersona) {
    this.idEmpresaPersona = idEmpresaPersona;
  }

  public Long getIdEmpresaPersona() {
    return idEmpresaPersona;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
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
  	return getIdEmpresaPersona();
  }

  @Override
  public void setKey(Long key) {
  	this.idEmpresaPersona = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPuesto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFechaContratacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresaPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idPersona", getIdPersona());
		regresar.put("idPuesto", getIdPuesto());
		regresar.put("fechaContratacion", getFechaContratacion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEmpresaPersona", getIdEmpresaPersona());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdPersona(), getIdPuesto(), getFechaContratacion(), getIdUsuario(), getIdEmpresaPersona(), getObservaciones(), getIdEmpresa(), getRegistro()
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
    regresar.append("idEmpresaPersona~");
    regresar.append(getIdEmpresaPersona());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEmpresaPersona());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticEmpresaPersonalDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEmpresaPersona()!= null && getIdEmpresaPersona()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticEmpresaPersonalDto other = (TrManticEmpresaPersonalDto) obj;
    if (getIdEmpresaPersona() != other.idEmpresaPersona && (getIdEmpresaPersona() == null || !getIdEmpresaPersona().equals(other.idEmpresaPersona))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEmpresaPersona() != null ? getIdEmpresaPersona().hashCode() : 0);
    return hash;
  }

}


