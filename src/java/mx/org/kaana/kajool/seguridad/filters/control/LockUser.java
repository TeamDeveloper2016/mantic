package mx.org.kaana.kajool.seguridad.filters.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project  KAJOOL (Control system polls)
 *@date 29/11/2016
 *@time 07:31:29 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class LockUser {
  private static final Log LOG      = LogFactory.getLog(LockUser.class);	
  
  public static final String MENSAJE= "<span class=\"FontBold Fs16\">Aviso:</span>  "
    + "Por cuestiones de mantenimiento esta aplicacion se dara de baja en <span class=\"FontBold Fs14\">{minutos} minutos</span> apartir de este momento<br/>"
    + ", por favor tome sus precauciones porque a las <span class=\"FontBold Fs14\">{horas} horas</span> se le negará el acceso a la aplicación y disculpe las<br/>"
    + "molestias que esto le pueda generar en sus actividades.<br/><br/> Atentamente el Administrador.";
  
  private Message message;
  private String text;
  private List<Integer> intervals;
  private Integer count;
  private Calendar start;

  public LockUser() {
    this(MENSAJE);
  }
  
  public LockUser(String text) {
    this.text     = text;
    this.start    = Calendar.getInstance();
    this.message  = new Message(Boolean.FALSE);
    this.intervals= Collections.EMPTY_LIST;
  }

  private void init() {
    Collections.sort(this.intervals);
    if(intervals.indexOf(0)< 0 && this.intervals instanceof ArrayList)
      this.intervals.add(0);
  }

  public String getText() {
    return text;
  }

  public Calendar getStart() {
    return start;
  }

  public void start(String text, Calendar until, List<Integer> intervals) {
    this.text     = text;
    this.intervals= intervals;
    init();
    this.start    = Calendar.getInstance();
    this.message.start(until);
    LOG.info("LockUser: "+ this.toHourEnd()+ "  -> "+ Fecha.formatear(Fecha.HORA_LARGA, this.message.getUntil()));
  }

  public void start(Calendar until) {
    this.start(this.text, until, this.intervals);  
  }
  
  public void end() {
    this.message.end();  
  }

  @Override
  public String toString() {
    return "LockUser{" + "message=" + message + ", text=" + text + ", intervals=" + intervals + ", count=" + count + '}';
  }
  
  public Integer toIndexMessage() {
    Integer regresar= -1;
    int minutes= this.message.toMinutes().intValue();
    int index  = 0;
    while(index< this.intervals.size() && minutes<= this.intervals.get(index)) 
      index++;
    if(index< this.intervals.size())
      regresar= index;
    return regresar;
  }
  
  public String toHourEnd() {
    return Fecha.formatear(Fecha.HORA_LARGA, this.message.getUntil());
  }
  
  public String toMessage() {
    String regresar= null;
    Map<String, String> params= new HashMap<>();
    try {
      params.put("minutos", this.message.toFormatMinutes());
      params.put("horas", this.toHourEnd());
      regresar= Cadena.replace(this.text, params, true);
    }
    finally {
      Methods.clean(params);
    }
    return regresar;
  }

  public Boolean isLock() {
    return this.message.getActive() && this.message.toMinutes()<= 0;
  } 
  
  public Boolean isActived() {
    return this.message.getActive();
  } 
  
  public Boolean isActivedAndMoreMessage() {
    return this.message.getActive() && this.message.toMinutes()> 0;
  } 
  
  public String toStart() {
    return Fecha.formatear(Fecha.HORA_LARGA, this.start);
  }
  
  public String toStarted() {
    return Fecha.toFormatSecondsToHour(Fecha.diferenciaSegundo(this.start.getTimeInMillis(), this.message.getUntil().getTimeInMillis()));
  }
  
  public String toElapsed() {
    return Fecha.toFormatSecondsToHour(Fecha.diferenciaSegundo(this.start.getTimeInMillis(), Calendar.getInstance().getTimeInMillis()));
  }
  
  public String toFinished() {
    return Fecha.toFormatSecondsToHour(Fecha.diferenciaSegundo(Calendar.getInstance().getTimeInMillis(), this.message.getUntil().getTimeInMillis()));
  }
  
  public String toIntervals(){
    return this.intervals.toString();
  }
    
  public static void main(String ... args) {
    LockUser lock    = new LockUser();
    Calendar calendar= Calendar.getInstance();
    calendar.add(Calendar.MINUTE, 65);
    lock.start(calendar);
    System.out.println(lock.toFinished());
  }
  
}
