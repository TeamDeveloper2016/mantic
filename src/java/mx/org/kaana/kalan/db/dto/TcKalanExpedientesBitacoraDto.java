package mx.org.kaana.kalan.db.dto;

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
@Table(name="tc_kalan_expedientes_bitacora")
public class TcKalanExpedientesBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_expediente_estatus")
  private Long idExpedienteEstatus;
  @Column (name="id_expediente")
  private Long idExpediente;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_expediente_bitacora")
  private Long idExpedienteBitacora;
  @Column (name="registro")
  private Timestamp registro;

  public TcKalanExpedientesBitacoraDto() {
    this(new Long(-1L));
  }

  public TcKalanExpedientesBitacoraDto(Long key) {
    this(null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcKalanExpedientesBitacoraDto(Long idUsuario, Long idExpedienteEstatus, Long idExpediente, String observaciones, Long idExpedienteBitacora) {
    setIdUsuario(idUsuario);
    setIdExpedienteEstatus(idExpedienteEstatus);
    setIdExpediente(idExpediente);
    setObservaciones(observaciones);
    setIdExpedienteBitacora(idExpedienteBitacora);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdExpedienteEstatus(Long idExpedienteEstatus) {
    this.idExpedienteEstatus = idExpedienteEstatus;
  }

  public Long getIdExpedienteEstatus() {
    return idExpedienteEstatus;
  }

  public void setIdExpediente(Long idExpediente) {
    this.idExpediente = idExpediente;
  }

  public Long getIdExpediente() {
    return idExpediente;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdExpedienteBitacora(Long idExpedienteBitacora) {
    this.idExpedienteBitacora = idExpedienteBitacora;
  }

  public Long getIdExpedienteBitacora() {
    return idExpedienteBitacora;
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
  	return getIdExpedienteBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idExpedienteBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdExpedienteEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdExpediente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdExpedienteBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idExpedienteEstatus", getIdExpedienteEstatus());
		regresar.put("idExpediente", getIdExpediente());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idExpedienteBitacora", getIdExpedienteBitacora());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdExpedienteEstatus(), getIdExpediente(), getObservaciones(), getIdExpedienteBitacora(), getRegistro()
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
    regresar.append("idExpedienteBitacora~");
    regresar.append(getIdExpedienteBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdExpedienteBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanExpedientesBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdExpedienteBitacora()!= null && getIdExpedienteBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanExpedientesBitacoraDto other = (TcKalanExpedientesBitacoraDto) obj;
    if (getIdExpedienteBitacora() != other.idExpedienteBitacora && (getIdExpedienteBitacora() == null || !getIdExpedienteBitacora().equals(other.idExpedienteBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdExpedienteBitacora() != null ? getIdExpedienteBitacora().hashCode() : 0);
    return hash;
  }

}


