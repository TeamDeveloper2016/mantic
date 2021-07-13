package mx.org.kaana.libs.wassenger;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.mantic.db.dto.TcManticMensajesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/06/2021
 *@time 02:34:02 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Message extends TcManticMensajesDto implements Serializable {

  private static final long serialVersionUID = -6510759858245467836L;
  
/*
  {
   "id":"60d6073758a1a1100820e16c",
   "waId":"3EB03ABE887CDD0CFC6B",
   "phone":"+5214492090586",
   "wid":"5214492090586@c.us",
   "status":"queued",
   "deliveryStatus":"queued",
   "createdAt":"2021-06-25T16:41:27.973Z",
   "deliverAt":"2021-06-25T16:41:27.961Z",
   "message":"Hola _Fulano_",
   "priority":"normal",
   "retentionPolicy":"plan_defaults",
   "retry":{"count":0},
   "webhookStatus":"pending",
   "media":{"format":"native"},
   "device":"60d4987e48b592aa82f09e92"
  }
*/
  
  private String createdAt;
  private String deliverAt;
  private Retry retry;
  private Medias media;

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getDeliverAt() {
    return deliverAt;
  }

  public void setDeliverAt(String deliverAt) {
    this.deliverAt = deliverAt;
  }

  public Retry getRetry() {
    return retry;
  }

  public void setRetry(Retry retry) {
    this.retry = retry;
  }

  public Medias getMedia() {
    return media;
  }

  public void setMedia(Medias media) {
    this.media = media;
  }

  @Override
  public String toString() {
    return super.toString()+ " Message{" + "createdAt=" + createdAt + ", deliverAt=" + deliverAt + ", retry=" + retry + ", media=" + media + '}';
  }

  @Override
  public Class toHbmClass() {
    return TcManticMensajesDto.class;
  }

  public void init() {
    if(this.deliverAt!= null)
      super.setDeliver(Fecha.toTimestamp(this.deliverAt));
    if(this.createdAt!= null)
      super.setCreated(Fecha.toTimestamp(this.createdAt));
  }  
  
}
