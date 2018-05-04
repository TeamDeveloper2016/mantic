package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
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
@Table(name="tr_mantic_persona_domicilio")
public class TrManticPersonaDomicilioDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_persona")
  private Long idPersona;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column (name="id_persona_domicilio")
  private Long idPersonaDomicilio;  
	@Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_tipo_domicilio")
  private Long idTipoDomicilio;
  @Column (name="id_domicilio")
  private Long idDomicilio;
  @Column (name="id_principal")
  private Long idPrincipal;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticPersonaDomicilioDto() {
    this(new Long(-1L));
  }

  public TrManticPersonaDomicilioDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TrManticPersonaDomicilioDto(Long idPersona, Long idPersonaDomicilio, Long idUsuario, Long idTipoDomicilio, Long idDomicilio, Long idPrincipal, String observaciones) {
    setIdPersona(idPersona);
    setIdPersonaDomicilio(idPersonaDomicilio);
    setIdUsuario(idUsuario);
    setIdTipoDomicilio(idTipoDomicilio);
    setIdDomicilio(idDomicilio);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdPersona(Long idPersona) {
    this.idPersona = idPersona;
  }

  public Long getIdPersona() {
    return idPersona;
  }

  public void setIdPersonaDomicilio(Long idPersonaDomicilio) {
    this.idPersonaDomicilio = idPersonaDomicilio;
  }

  public Long getIdPersonaDomicilio() {
    return idPersonaDomicilio;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdTipoDomicilio(Long idTipoDomicilio) {
    this.idTipoDomicilio = idTipoDomicilio;
  }

  public Long getIdTipoDomicilio() {
    return idTipoDomicilio;
  }

  public void setIdDomicilio(Long idDomicilio) {
    this.idDomicilio = idDomicilio;
  }

  public Long getIdDomicilio() {
    return idDomicilio;
  }

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
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
  	return getIdPersonaDomicilio();
  }

  @Override
  public void setKey(Long key) {
  	this.idPersonaDomicilio = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPersonaDomicilio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoDomicilio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDomicilio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idPersona", getIdPersona());
		regresar.put("idPersonaDomicilio", getIdPersonaDomicilio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoDomicilio", getIdTipoDomicilio());
		regresar.put("idDomicilio", getIdDomicilio());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdPersona(), getIdPersonaDomicilio(), getIdUsuario(), getIdTipoDomicilio(), getIdDomicilio(), getIdPrincipal(), getObservaciones(), getRegistro()
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
    regresar.append("idPersonaDomicilio~");
    regresar.append(getIdPersonaDomicilio());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPersonaDomicilio());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticPersonaDomicilioDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPersonaDomicilio()!= null && getIdPersonaDomicilio()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticPersonaDomicilioDto other = (TrManticPersonaDomicilioDto) obj;
    if (getIdPersonaDomicilio()!= other.idPersonaDomicilio && (getIdPersonaDomicilio()== null || !getIdPersonaDomicilio().equals(other.idPersonaDomicilio))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPersonaDomicilio()!= null ? getIdPersonaDomicilio().hashCode() : 0);
    return hash;
  }
}