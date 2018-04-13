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
@Table(name="tr_mantic_empresa_domicilio")
public class TrManticEmpresaDomicilioDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
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
  @Column (name="id_empresa_domicilio")
  private Long idEmpresaDomicilio;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticEmpresaDomicilioDto() {
    this(new Long(-1L));
  }

  public TrManticEmpresaDomicilioDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TrManticEmpresaDomicilioDto(Long idUsuario, Long idTipoDomicilio, Long idDomicilio, Long idPrincipal, String observaciones, Long idEmpresaDomicilio, Long idEmpresa) {
    setIdUsuario(idUsuario);
    setIdTipoDomicilio(idTipoDomicilio);
    setIdDomicilio(idDomicilio);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setIdEmpresaDomicilio(idEmpresaDomicilio);
    setIdEmpresa(idEmpresa);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
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

  public void setIdEmpresaDomicilio(Long idEmpresaDomicilio) {
    this.idEmpresaDomicilio = idEmpresaDomicilio;
  }

  public Long getIdEmpresaDomicilio() {
    return idEmpresaDomicilio;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
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
  	return getIdUsuario();
  }

  @Override
  public void setKey(Long key) {
  	this.idUsuario = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
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
		regresar.append(getIdEmpresaDomicilio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoDomicilio", getIdTipoDomicilio());
		regresar.put("idDomicilio", getIdDomicilio());
		regresar.put("idPrincipal", getIdPrincipal());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresaDomicilio", getIdEmpresaDomicilio());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdTipoDomicilio(), getIdDomicilio(), getIdPrincipal(), getObservaciones(), getIdEmpresaDomicilio(), getIdEmpresa(), getRegistro()
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
    regresar.append("idUsuario~");
    regresar.append(getIdUsuario());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdUsuario());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticEmpresaDomicilioDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdUsuario()!= null && getIdUsuario()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticEmpresaDomicilioDto other = (TrManticEmpresaDomicilioDto) obj;
    if (getIdUsuario() != other.idUsuario && (getIdUsuario() == null || !getIdUsuario().equals(other.idUsuario))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdUsuario() != null ? getIdUsuario().hashCode() : 0);
    return hash;
  }

}


