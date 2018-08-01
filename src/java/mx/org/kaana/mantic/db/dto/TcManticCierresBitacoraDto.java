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
@Table(name="tc_mantic_cierres_bitacora")
public class TcManticCierresBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cierre_bitacora")
  private Long idCierreBitacora;
  @Column (name="id_cierre")
  private Long idCierre;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_cierre_estatus")
  private Long idCierreEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticCierresBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticCierresBitacoraDto(Long key) {
    this(null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticCierresBitacoraDto(String justificacion, Long idCierreBitacora, Long idCierre, Long idUsuario, Long idCierreEstatus) {
    setJustificacion(justificacion);
    setIdCierreBitacora(idCierreBitacora);
    setIdCierre(idCierre);
    setIdUsuario(idUsuario);
    setIdCierreEstatus(idCierreEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdCierreBitacora(Long idCierreBitacora) {
    this.idCierreBitacora = idCierreBitacora;
  }

  public Long getIdCierreBitacora() {
    return idCierreBitacora;
  }

  public void setIdCierre(Long idCierre) {
    this.idCierre = idCierre;
  }

  public Long getIdCierre() {
    return idCierre;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdCierreEstatus(Long idCierreEstatus) {
    this.idCierreEstatus = idCierreEstatus;
  }

  public Long getIdCierreEstatus() {
    return idCierreEstatus;
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
  	return getIdCierreBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idCierreBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierreBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierreEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idCierreBitacora", getIdCierreBitacora());
		regresar.put("idCierre", getIdCierre());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idCierreEstatus", getIdCierreEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdCierreBitacora(), getIdCierre(), getIdUsuario(), getIdCierreEstatus(), getRegistro()
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
    regresar.append("idCierreBitacora~");
    regresar.append(getIdCierreBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCierreBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCierresBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCierreBitacora()!= null && getIdCierreBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCierresBitacoraDto other = (TcManticCierresBitacoraDto) obj;
    if (getIdCierreBitacora() != other.idCierreBitacora && (getIdCierreBitacora() == null || !getIdCierreBitacora().equals(other.idCierreBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCierreBitacora() != null ? getIdCierreBitacora().hashCode() : 0);
    return hash;
  }

}


