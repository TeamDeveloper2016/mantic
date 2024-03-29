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
@Table(name="tc_mantic_conteos_bitacora")
public class TcManticConteosBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_conteo")
  private Long idConteo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_conteo_bitacora")
  private Long idConteoBitacora;
  @Column (name="id_conteo_estatus")
  private Long idConteoEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticConteosBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticConteosBitacoraDto(Long key) {
    this(null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticConteosBitacoraDto(String justificacion, Long idUsuario, Long idConteo, Long idConteoBitacora, Long idConteoEstatus) {
    setJustificacion(justificacion);
    setIdUsuario(idUsuario);
    setIdConteo(idConteo);
    setIdConteoBitacora(idConteoBitacora);
    setIdConteoEstatus(idConteoEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
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

  public void setIdConteo(Long idConteo) {
    this.idConteo = idConteo;
  }

  public Long getIdConteo() {
    return idConteo;
  }

  public void setIdConteoBitacora(Long idConteoBitacora) {
    this.idConteoBitacora = idConteoBitacora;
  }

  public Long getIdConteoBitacora() {
    return idConteoBitacora;
  }

  public void setIdConteoEstatus(Long idConteoEstatus) {
    this.idConteoEstatus = idConteoEstatus;
  }

  public Long getIdConteoEstatus() {
    return idConteoEstatus;
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
  	return getIdConteoBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idConteoBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConteo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConteoBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConteoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idConteo", getIdConteo());
		regresar.put("idConteoBitacora", getIdConteoBitacora());
		regresar.put("idConteoEstatus", getIdConteoEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdUsuario(), getIdConteo(), getIdConteoBitacora(), getIdConteoEstatus(), getRegistro()
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
    regresar.append("idConteoBitacora~");
    regresar.append(getIdConteoBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdConteoBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticConteosBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdConteoBitacora()!= null && getIdConteoBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticConteosBitacoraDto other = (TcManticConteosBitacoraDto) obj;
    if (getIdConteoBitacora() != other.idConteoBitacora && (getIdConteoBitacora() == null || !getIdConteoBitacora().equals(other.idConteoBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdConteoBitacora() != null ? getIdConteoBitacora().hashCode() : 0);
    return hash;
  }

}


