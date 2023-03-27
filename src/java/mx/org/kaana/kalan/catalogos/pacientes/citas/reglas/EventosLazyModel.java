package mx.org.kaana.kalan.catalogos.pacientes.citas.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/03/2023
 *@time 02:24:37 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class EventosLazyModel extends LazyScheduleModel implements Serializable {

  private static final long serialVersionUID = -6949392046860584417L;

  private String process;
  private String idXml;
  private List<Columna> columns;
  private Map<String, Object> params;

  public EventosLazyModel(String process, String idXml, Map<String, Object> params, List<Columna> columns) {
    this.process= process;
    this.idXml  = idXml;
    this.columns= new ArrayList(columns);
    this.params = new HashMap(params);
  }
  
  @Override
  public void loadEvents(Date start, Date end) {
    DefaultScheduleEvent cita= null; 
    try {      
      this.params.put("inicio", Fecha.formatear(Fecha.FECHA_ESTANDAR, start));
      this.params.put("termino", Fecha.formatear(Fecha.FECHA_ESTANDAR, end));
      List<Entity> eventos= (List<Entity>)DaoFactory.getInstance().toEntitySet(this.process, this.idXml, this.params);
      if(eventos!= null && !eventos.isEmpty()) {
        UIBackingUtilities.toFormatEntitySet(eventos, this.columns);
        for (Entity item: eventos) {
          String cliente= item.toString("cliente");
          cita= new DefaultScheduleEvent(Objects.equals(cliente.trim().length(), 0)? item.toString("servicios"): cliente, item.toTimestamp("inicio"), item.toTimestamp("termino"), item);
          cita.setStyleClass("janal-cita-".concat(Objects.equals(cliente.trim().length(), 0)? "extras": item.toString("estatus").toLowerCase()));
          cita.setDescription(item.toString("servicios"));
          cita.setEditable(Boolean.TRUE);
          this.addEvent(cita);
        } // for
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }

  public Date getRandomDate(Date base) {
    Calendar date = Calendar.getInstance();
    date.setTime(base);
    date.add(Calendar.DATE, ((int) (Math.random() * 30)) + 1);
    return new Date(date.getTimeInMillis());
  }

  @Override
  protected void finalize() throws Throwable {
    Methods.clean(this.columns);
    Methods.clean(this.params);
    super.finalize(); 
  }
  
}
