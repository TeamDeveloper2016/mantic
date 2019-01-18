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
@Table(name="tc_mantic_egresos_notas")
public class TcManticEgresosNotasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_egreso")
  private Long idEgreso;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_egreso_nota")
  private Long idEgresoNota;
  @Column (name="comentario")
  private String comentario;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticEgresosNotasDto() {
    this(new Long(-1L));
  }

  public TcManticEgresosNotasDto(Long key) {
    this(null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticEgresosNotasDto(Long idEgreso, Long idUsuario, Long idEgresoNota, String comentario) {
    setIdEgreso(idEgreso);
    setIdUsuario(idUsuario);
    setIdEgresoNota(idEgresoNota);
    setComentario(comentario);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdEgreso(Long idEgreso) {
    this.idEgreso = idEgreso;
  }

  public Long getIdEgreso() {
    return idEgreso;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdEgresoNota(Long idEgresoNota) {
    this.idEgresoNota = idEgresoNota;
  }

  public Long getIdEgresoNota() {
    return idEgresoNota;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public String getComentario() {
    return comentario;
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
  	return getIdEgresoNota();
  }

  @Override
  public void setKey(Long key) {
  	this.idEgresoNota = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdEgreso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEgresoNota());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getComentario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idEgreso", getIdEgreso());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEgresoNota", getIdEgresoNota());
		regresar.put("comentario", getComentario());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdEgreso(), getIdUsuario(), getIdEgresoNota(), getComentario(), getRegistro()
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
    regresar.append("idEgresoNota~");
    regresar.append(getIdEgresoNota());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEgresoNota());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEgresosNotasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEgresoNota()!= null && getIdEgresoNota()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEgresosNotasDto other = (TcManticEgresosNotasDto) obj;
    if (getIdEgresoNota() != other.idEgresoNota && (getIdEgresoNota() == null || !getIdEgresoNota().equals(other.idEgresoNota))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEgresoNota() != null ? getIdEgresoNota().hashCode() : 0);
    return hash;
  }

}


