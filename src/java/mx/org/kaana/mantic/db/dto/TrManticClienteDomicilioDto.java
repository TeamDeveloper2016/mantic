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
@Table(name="tr_mantic_cliente_domicilio")
public class TrManticClienteDomicilioDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_cliente")
  private Long idCliente;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cliente_domicilio")
  private Long idClienteDomicilio;
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

  public TrManticClienteDomicilioDto() {
    this(new Long(-1L));
  }

  public TrManticClienteDomicilioDto(Long key) {
    this(null, new Long(-1L), null, null, null, 2L, null);
    setKey(key);
  }

  public TrManticClienteDomicilioDto(Long idCliente, Long idClienteDomicilio, Long idUsuario, Long idTipoDomicilio, Long idDomicilio, Long idPrincipal, String observaciones) {
    setIdCliente(idCliente);
    setIdClienteDomicilio(idClienteDomicilio);
    setIdUsuario(idUsuario);
    setIdTipoDomicilio(idTipoDomicilio);
    setIdDomicilio(idDomicilio);
    setIdPrincipal(idPrincipal);
    setObservaciones(observaciones);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setIdClienteDomicilio(Long idClienteDomicilio) {
    this.idClienteDomicilio = idClienteDomicilio;
  }

  public Long getIdClienteDomicilio() {
    return idClienteDomicilio;
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
  	return getIdClienteDomicilio();
  }

  @Override
  public void setKey(Long key) {
  	this.idClienteDomicilio = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdClienteDomicilio());
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
		regresar.put("idCliente", getIdCliente());
		regresar.put("idClienteDomicilio", getIdClienteDomicilio());
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
    getIdCliente(), getIdClienteDomicilio(), getIdUsuario(), getIdTipoDomicilio(), getIdDomicilio(), getIdPrincipal(), getObservaciones(), getRegistro()
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
    regresar.append("idClienteDomicilio~");
    regresar.append(getIdClienteDomicilio());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdClienteDomicilio());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticClienteDomicilioDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdClienteDomicilio()!= null && getIdClienteDomicilio()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticClienteDomicilioDto other = (TrManticClienteDomicilioDto) obj;
    if (getIdClienteDomicilio() != other.idClienteDomicilio && (getIdClienteDomicilio() == null || !getIdClienteDomicilio().equals(other.idClienteDomicilio))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdClienteDomicilio() != null ? getIdClienteDomicilio().hashCode() : 0);
    return hash;
  }

}


