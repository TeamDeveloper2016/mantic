package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
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
@Table(name="tc_mantic_transferencias_personas")
public class TcManticTransferenciasPersonasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_persona")
  private Long idPersona;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column (name="id_transferencia_persona")
  private Long idTransferenciaPersona;
  @Column (name="id_transferencia")
  private Long idTransferencia;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTransferenciasPersonasDto() {
    this(new Long(-1L));
  }

  public TcManticTransferenciasPersonasDto(Long key) {
    this(null, null, null, null);
    setKey(key);
  }

  public TcManticTransferenciasPersonasDto(Long idPersona, Long idUsuario, Long idTransferenciaPersona, Long idTransferencia) {
    setIdPersona(idPersona);
    setIdUsuario(idUsuario);
    setIdTransferenciaPersona(idTransferenciaPersona);
    setIdTransferencia(idTransferencia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdPersona(Long idPersona) {
    this.idPersona = idPersona;
  }

  public Long getIdPersona() {
    return idPersona;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdTransferenciaPersona(Long idTransferenciaPersona) {
    this.idTransferenciaPersona = idTransferenciaPersona;
  }

  public Long getIdTransferenciaPersona() {
    return idTransferenciaPersona;
  }

  public void setIdTransferencia(Long idTransferencia) {
    this.idTransferencia = idTransferencia;
  }

  public Long getIdTransferencia() {
    return idTransferencia;
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
  	return getIdTransferenciaPersona();
  }

  @Override
  public void setKey(Long key) {
  	this.idTransferenciaPersona = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idPersona", getIdPersona());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTransferenciaPersona", getIdTransferenciaPersona());
		regresar.put("idTransferencia", getIdTransferencia());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdPersona(), getIdUsuario(), getIdTransferenciaPersona(), getIdTransferencia(), getRegistro()
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
    regresar.append("idTransferenciaPersona~");
    regresar.append(getIdTransferenciaPersona());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTransferenciaPersona());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasPersonasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTransferenciaPersona()!= null && getIdTransferenciaPersona()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTransferenciasPersonasDto other = (TcManticTransferenciasPersonasDto) obj;
    if (getIdTransferenciaPersona() != other.idTransferenciaPersona && (getIdTransferenciaPersona() == null || !getIdTransferenciaPersona().equals(other.idTransferenciaPersona))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTransferenciaPersona() != null ? getIdTransferenciaPersona().hashCode() : 0);
    return hash;
  }

}


