package mx.org.kaana.kajool.seguridad.filters.control;

import java.util.Calendar;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;

/**
 *@company KAANA
 *@project  KAJOOL (Control system polls)
 *@date 29/11/2016
 *@time 07:31:29 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Message {

  private Calendar until;
  private Boolean active;

  public Message(Boolean active) {
    this(Calendar.getInstance(), active);
  }

  public Message(Calendar until, Boolean active) {
    this.until  = until;
    this.active = active;
    //this.until.add(Calendar.MINUTE, 30);
  }

  public Calendar getUntil() {
    return until;
  }

  public Boolean getActive() {
    return active;
  }

  public void start(Calendar until) {
    this.until = until;
    this.active= Boolean.TRUE;
  }
  
  public void end() {
    this.active= Boolean.FALSE;
  }
  
  @Override
  public String toString() {
    return "MsgUser{until=" + until + ", active=" + active + '}';
  }
  
  public Long toMinutes() {
    return Fecha.diferenciaMinutos(Calendar.getInstance().getTimeInMillis(), this.until.getTimeInMillis());
  }
  
  public Long toSeconds() {
    return Fecha.diferenciaSegundo(Calendar.getInstance().getTimeInMillis(), this.until.getTimeInMillis());
  }
  
  public String toFormatMinutes() {
    return Global.format(EFormatoDinamicos.MILES_SIN_DECIMALES, this.toMinutes());
  }
  
  public String toFormatHours() {
    return Fecha.toFormatSecondsToHour(this.toSeconds());
  }
  
}
