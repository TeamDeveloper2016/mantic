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
@Table(name="tc_mantic_control_descargas")
public class TcManticControlDescargasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_respaldo")
  private Long idRespaldo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_control_descarga")
  private Long idControlDescarga;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticControlDescargasDto() {
    this(new Long(-1L));
  }

  public TcManticControlDescargasDto(Long key) {
    this(null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticControlDescargasDto(Long idUsuario, Long idRespaldo, Long idControlDescarga) {
    setIdUsuario(idUsuario);
    setIdRespaldo(idRespaldo);
    setIdControlDescarga(idControlDescarga);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdRespaldo(Long idRespaldo) {
    this.idRespaldo = idRespaldo;
  }

  public Long getIdRespaldo() {
    return idRespaldo;
  }

  public void setIdControlDescarga(Long idControlDescarga) {
    this.idControlDescarga = idControlDescarga;
  }

  public Long getIdControlDescarga() {
    return idControlDescarga;
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
  	return getIdControlDescarga();
  }

  @Override
  public void setKey(Long key) {
  	this.idControlDescarga = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRespaldo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdControlDescarga());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idRespaldo", getIdRespaldo());
		regresar.put("idControlDescarga", getIdControlDescarga());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdRespaldo(), getIdControlDescarga(), getRegistro()
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
    regresar.append("idControlDescarga~");
    regresar.append(getIdControlDescarga());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdControlDescarga());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticControlDescargasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdControlDescarga()!= null && getIdControlDescarga()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticControlDescargasDto other = (TcManticControlDescargasDto) obj;
    if (getIdControlDescarga() != other.idControlDescarga && (getIdControlDescarga() == null || !getIdControlDescarga().equals(other.idControlDescarga))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdControlDescarga() != null ? getIdControlDescarga().hashCode() : 0);
    return hash;
  }

}


