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
@Table(name="tc_mantic_alertas")
public class TcManticAlertasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_alerta")
  private Long idAlerta;
  @Column (name="id_notifica")
  private Long idNotifica;
  @Column (name="mensaje")
  private String mensaje;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticAlertasDto() {
    this(new Long(-1L));
  }

  public TcManticAlertasDto(Long key) {
    this(null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticAlertasDto(Long idUsuario, Long idAlerta, Long idNotifica, String mensaje) {
    setIdUsuario(idUsuario);
    setIdAlerta(idAlerta);
    setIdNotifica(idNotifica);
    setMensaje(mensaje);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdAlerta(Long idAlerta) {
    this.idAlerta = idAlerta;
  }

  public Long getIdAlerta() {
    return idAlerta;
  }

  public void setIdNotifica(Long idNotifica) {
    this.idNotifica = idNotifica;
  }

  public Long getIdNotifica() {
    return idNotifica;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public String getMensaje() {
    return mensaje;
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
  	return getIdAlerta();
  }

  @Override
  public void setKey(Long key) {
  	this.idAlerta = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlerta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotifica());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlerta", getIdAlerta());
		regresar.put("idNotifica", getIdNotifica());
		regresar.put("mensaje", getMensaje());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdAlerta(), getIdNotifica(), getMensaje(), getRegistro()
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
    regresar.append("idAlerta~");
    regresar.append(getIdAlerta());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdAlerta());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticAlertasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdAlerta()!= null && getIdAlerta()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticAlertasDto other = (TcManticAlertasDto) obj;
    if (getIdAlerta() != other.idAlerta && (getIdAlerta() == null || !getIdAlerta().equals(other.idAlerta))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdAlerta() != null ? getIdAlerta().hashCode() : 0);
    return hash;
  }

}


