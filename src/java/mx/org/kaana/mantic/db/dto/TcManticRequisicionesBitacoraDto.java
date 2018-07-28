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
@Table(name="tc_mantic_requisiciones_bitacora")
public class TcManticRequisicionesBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_requisicion_bitacora")
  private Long idRequisicionBitacora;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_requisicion_estatus")
  private Long idRequisicionEstatus;
  @Column (name="id_requisicion")
  private Long idRequisicion;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticRequisicionesBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticRequisicionesBitacoraDto(Long key) {
    this(new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticRequisicionesBitacoraDto(Long idRequisicionBitacora, String justificacion, Long idUsuario, Long idRequisicionEstatus, Long idRequisicion) {
    setIdRequisicionBitacora(idRequisicionBitacora);
    setJustificacion(justificacion);
    setIdUsuario(idUsuario);
    setIdRequisicionEstatus(idRequisicionEstatus);
    setIdRequisicion(idRequisicion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdRequisicionBitacora(Long idRequisicionBitacora) {
    this.idRequisicionBitacora = idRequisicionBitacora;
  }

  public Long getIdRequisicionBitacora() {
    return idRequisicionBitacora;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdRequisicionEstatus(Long idRequisicionEstatus) {
    this.idRequisicionEstatus = idRequisicionEstatus;
  }

  public Long getIdRequisicionEstatus() {
    return idRequisicionEstatus;
  }

  public void setIdRequisicion(Long idRequisicion) {
    this.idRequisicion = idRequisicion;
  }

  public Long getIdRequisicion() {
    return idRequisicion;
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
  	return getIdRequisicionBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idRequisicionBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdRequisicionBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRequisicionEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRequisicion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idRequisicionBitacora", getIdRequisicionBitacora());
		regresar.put("justificacion", getJustificacion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idRequisicionEstatus", getIdRequisicionEstatus());
		regresar.put("idRequisicion", getIdRequisicion());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdRequisicionBitacora(), getJustificacion(), getIdUsuario(), getIdRequisicionEstatus(), getIdRequisicion(), getRegistro()
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
    regresar.append("idRequisicionBitacora~");
    regresar.append(getIdRequisicionBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdRequisicionBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticRequisicionesBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdRequisicionBitacora()!= null && getIdRequisicionBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticRequisicionesBitacoraDto other = (TcManticRequisicionesBitacoraDto) obj;
    if (getIdRequisicionBitacora() != other.idRequisicionBitacora && (getIdRequisicionBitacora() == null || !getIdRequisicionBitacora().equals(other.idRequisicionBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdRequisicionBitacora() != null ? getIdRequisicionBitacora().hashCode() : 0);
    return hash;
  }

}


