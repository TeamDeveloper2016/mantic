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
@Table(name="tc_mantic_mensajes")
public class TcManticMensajesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="retention_policy")
  private String retentionPolicy;
  @Column (name="id_send_status")
  private Long idSendStatus;
  @Column (name="device")
  private String device;
  @Column (name="created")
  private Timestamp created;
  @Column (name="webhook_status")
  private String webhookStatus;
  @Column (name="wa_id")
  private String waId;
  @Column (name="message")
  private String message;
  @Column (name="priority")
  private String priority;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="deliver")
  private Timestamp deliver;
  @Column (name="wid")
  private String wid;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_mensaje")
  private Long idMensaje;
  @Column (name="phone")
  private String phone;
  @Column (name="id_tipo_mensaje")
  private Long idTipoMensaje;
  @Column (name="send_status")
  private String sendStatus;
  @Column (name="id")
  private String id;
  @Column (name="delivery_status")
  private String deliveryStatus;
  @Column (name="status")
  private String status;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="telefono")
  private String telefono;

  public TcManticMensajesDto() {
    this(new Long(-1L));
  }

  public TcManticMensajesDto(Long key) {
    this(null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, new Long(-1L), null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticMensajesDto(String retentionPolicy, Long idSendStatus, String device, Timestamp created, String webhookStatus, String waId, String message, String priority, Timestamp deliver, String wid, Long idMensaje, String phone, Long idTipoMensaje, String sendStatus, String id, String deliveryStatus, String status, Long idUsuario, String telefono) {
    setRetentionPolicy(retentionPolicy);
    setIdSendStatus(idSendStatus);
    setDevice(device);
    setCreated(created);
    setWebhookStatus(webhookStatus);
    setWaId(waId);
    setMessage(message);
    setPriority(priority);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setDeliver(deliver);
    setWid(wid);
    setIdMensaje(idMensaje);
    setPhone(phone);
    setIdTipoMensaje(idTipoMensaje);
    setSendStatus(sendStatus);
    setId(id);
    setDeliveryStatus(deliveryStatus);
    setStatus(status);
    this.idUsuario= idUsuario;
    this.telefono= telefono;
  }
	
  public void setRetentionPolicy(String retentionPolicy) {
    this.retentionPolicy = retentionPolicy;
  }

  public String getRetentionPolicy() {
    return retentionPolicy;
  }

  public void setIdSendStatus(Long idSendStatus) {
    this.idSendStatus = idSendStatus;
  }

  public Long getIdSendStatus() {
    return idSendStatus;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public String getDevice() {
    return device;
  }

  public void setCreated(Timestamp created) {
    this.created = created;
  }

  public Timestamp getCreated() {
    return created;
  }

  public void setWebhookStatus(String webhookStatus) {
    this.webhookStatus = webhookStatus;
  }

  public String getWebhookStatus() {
    return webhookStatus;
  }

  public void setWaId(String waId) {
    this.waId = waId;
  }

  public String getWaId() {
    return waId;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getPriority() {
    return priority;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setDeliver(Timestamp deliver) {
    this.deliver = deliver;
  }

  public Timestamp getDeliver() {
    return deliver;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public String getWid() {
    return wid;
  }

  public void setIdMensaje(Long idMensaje) {
    this.idMensaje = idMensaje;
  }

  public Long getIdMensaje() {
    return idMensaje;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPhone() {
    return phone;
  }

  public void setIdTipoMensaje(Long idTipoMensaje) {
    this.idTipoMensaje = idTipoMensaje;
  }

  public Long getIdTipoMensaje() {
    return idTipoMensaje;
  }

  public void setSendStatus(String sendStatus) {
    this.sendStatus = sendStatus;
  }

  public String getSendStatus() {
    return sendStatus;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setDeliveryStatus(String deliveryStatus) {
    this.deliveryStatus = deliveryStatus;
  }

  public String getDeliveryStatus() {
    return deliveryStatus;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMensaje();
  }

  @Override
  public void setKey(Long key) {
  	this.idMensaje = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getRetentionPolicy());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSendStatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDevice());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCreated());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getWebhookStatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getWaId());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMessage());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPriority());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDeliver());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getWid());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPhone());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSendStatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getId());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDeliveryStatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getStatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTelefono());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("retentionPolicy", getRetentionPolicy());
		regresar.put("idSendStatus", getIdSendStatus());
		regresar.put("device", getDevice());
		regresar.put("created", getCreated());
		regresar.put("webhookStatus", getWebhookStatus());
		regresar.put("waId", getWaId());
		regresar.put("message", getMessage());
		regresar.put("priority", getPriority());
		regresar.put("registro", getRegistro());
		regresar.put("deliver", getDeliver());
		regresar.put("wid", getWid());
		regresar.put("idMensaje", getIdMensaje());
		regresar.put("phone", getPhone());
		regresar.put("idTipoMensaje", getIdTipoMensaje());
		regresar.put("sendStatus", getSendStatus());
		regresar.put("id", getId());
		regresar.put("deliveryStatus", getDeliveryStatus());
		regresar.put("status", getStatus());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("telefono", getTelefono());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getRetentionPolicy(), getIdSendStatus(), getDevice(), getCreated(), getWebhookStatus(), getWaId(), getMessage(), getPriority(), getRegistro(), getDeliver(), getWid(), getIdMensaje(), getPhone(), getIdTipoMensaje(), getSendStatus(), getId(), getDeliveryStatus(), getStatus(), getIdUsuario(), getTelefono()
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
    regresar.append("idMensaje~");
    regresar.append(getIdMensaje());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMensaje());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticMensajesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMensaje()!= null && getIdMensaje()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticMensajesDto other = (TcManticMensajesDto) obj;
    if (getIdMensaje() != other.idMensaje && (getIdMensaje() == null || !getIdMensaje().equals(other.idMensaje))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMensaje() != null ? getIdMensaje().hashCode() : 0);
    return hash;
  }

}


