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
@Table(name="tc_kalan_citas_bitacora")
public class TcKalanCitasBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cita_bitacora")
  private Long idCitaBitacora;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_cita_estatus")
  private Long idCitaEstatus;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_cita")
  private Long idCita;
  @Column (name="registro")
  private Timestamp registro;

  public TcKalanCitasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcKalanCitasBitacoraDto(Long key) {
    this(new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcKalanCitasBitacoraDto(Long idCitaBitacora, Long idUsuario, Long idCitaEstatus, String observaciones, Long idCita) {
    setIdCitaBitacora(idCitaBitacora);
    setIdUsuario(idUsuario);
    setIdCitaEstatus(idCitaEstatus);
    setObservaciones(observaciones);
    setIdCita(idCita);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdCitaBitacora(Long idCitaBitacora) {
    this.idCitaBitacora = idCitaBitacora;
  }

  public Long getIdCitaBitacora() {
    return idCitaBitacora;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdCitaEstatus(Long idCitaEstatus) {
    this.idCitaEstatus = idCitaEstatus;
  }

  public Long getIdCitaEstatus() {
    return idCitaEstatus;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdCita(Long idCita) {
    this.idCita = idCita;
  }

  public Long getIdCita() {
    return idCita;
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
  	return getIdCitaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idCitaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCitaBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCitaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCita());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idCitaBitacora", getIdCitaBitacora());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idCitaEstatus", getIdCitaEstatus());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idCita", getIdCita());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdCitaBitacora(), getIdUsuario(), getIdCitaEstatus(), getObservaciones(), getIdCita(), getRegistro()
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
    regresar.append("idCitaBitacora~");
    regresar.append(getIdCitaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCitaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanCitasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCitaBitacora()!= null && getIdCitaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanCitasBitacoraDto other = (TcKalanCitasBitacoraDto) obj;
    if (getIdCitaBitacora() != other.idCitaBitacora && (getIdCitaBitacora() == null || !getIdCitaBitacora().equals(other.idCitaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCitaBitacora() != null ? getIdCitaBitacora().hashCode() : 0);
    return hash;
  }

}


