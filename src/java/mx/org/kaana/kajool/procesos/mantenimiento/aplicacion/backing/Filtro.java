package mx.org.kaana.kajool.procesos.mantenimiento.aplicacion.backing;

/**
 *@company KAANA
 *@project  KAJOOL (Control system polls)
 *@date 29/11/2016
 *@time 07:31:29 PM
 *@author One Developer 2016 <one.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.seguridad.filters.control.LockUser;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;

@ManagedBean(name="kajoolMantenimientoAplicacionFiltro")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= -7786871029191417048L;

  @PostConstruct
  @Override
	protected void init() {
    try {			      
      Calendar calendar= Calendar.getInstance();
      calendar.add(Calendar.MINUTE, 10);
      this.attrs.put("text", LockUser.MENSAJE); 
      this.attrs.put("hours", calendar.get(Calendar.HOUR_OF_DAY));
      this.attrs.put("hour", new Time(calendar.getTimeInMillis()));      
      doCalculate();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init
  
  public void doAceptar() {        
    Calendar calendar     = null;
    List<Integer>intervals= null;
    LockUser lockUser     = null;
    try {
      lockUser= JsfBase.toLockUsers();
      intervals= new ArrayList<>();
      for(String intervalo: (String[])this.attrs.get("intervals")) 
        intervals.add(Integer.valueOf(intervalo));
      calendar= DateUtils.toCalendar((Time)this.attrs.get("hour"));
      calendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
      JsfBase.getUsuariosSitio().clearMessage();
      lockUser.start((String)this.attrs.get("text"), calendar, intervals);                  
      if(lockUser.isActived())
        RequestContext.getCurrentInstance().execute("show(true);");
      this.attrs.put("active", lockUser.isActived());
      this.attrs.put("activeIntervals", lockUser.isActived()? lockUser.toIntervals().substring(1, lockUser.toIntervals().length()-4).concat(" minutos") : "");
      LOG.info("Se comenzo con el bloqueo de usuarios: "+ JsfBase.toLockUsers().toHourEnd());
      doCalculate();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch    
  } // doAceptar
  
  public void doCancelar() {
    try {      
      init();
      JsfBase.getUsuariosSitio().clearMessage();
      JsfBase.toLockUsers().end();      
      this.attrs.put("active", JsfBase.toLockUsers().isActived());
      LOG.info("Se canceló el bloqueo de usuarios: "+ Fecha.getHoraExtendida());
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch    
  } // doAceptar

  public void doCalculate() {
    Calendar calendar= DateUtils.toCalendar((Time)this.attrs.get("hour"));
    calendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
    this.attrs.put("calculate", Fecha.toFormatSecondsToHour(Fecha.diferenciaSegundo(Calendar.getInstance().getTimeInMillis(), calendar.getTimeInMillis())));
    
    String[] intervals= {"1","2","3","4","5","10","15","20","30","60"};
    LockUser lockUsers= JsfBase.toLockUsers();
    this.attrs.put("start", lockUsers.toStart());
    this.attrs.put("end", lockUsers.toHourEnd());
    this.attrs.put("started", lockUsers.toStarted());
    this.attrs.put("elapsed", lockUsers.toElapsed());
    this.attrs.put("finished", lockUsers.toFinished());
    this.attrs.put("activeMessage", lockUsers.toMessage());
    this.attrs.put("activeIntervals", lockUsers.isActived() ? lockUsers.toIntervals().substring(1, lockUsers.toIntervals().length()-4).concat(" minutos") : "");
    this.attrs.put("intervals", intervals);
    this.attrs.put("active", lockUsers.isActived());
  } // doCalculate
  
}
