package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Periodo;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticControlRespaldosDto;
import mx.org.kaana.mantic.db.dto.TcManticRespaldosDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.component.schedule.Schedule;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "kajoolCalendario")
@ViewScoped
public class Calendario extends Comun implements Serializable {

  private static final long serialVersionUID= 5323749709626263801L;
  private static final Log LOG              = LogFactory.getLog(Calendario.class);
  
  protected FormatLazyModel lazyModelPagar;
  protected FormatLazyModel lazyModelAgendar;
  protected FormatLazyModel lazyModelVentas;  
  private ScheduleModel lazyEventModel;
  
  public FormatLazyModel getLazyModelPagar() {
    return lazyModelPagar;
  }

  public FormatLazyModel getLazyModelAgendar() {
    return lazyModelAgendar;
  }

  public ScheduleModel getLazyEventModel() {
    return lazyEventModel;
  }

  public FormatLazyModel getLazyModelVentas() {
    return lazyModelVentas;
  }

  public void setLazyEventModel(ScheduleModel lazyEventModel) {
    this.lazyEventModel = lazyEventModel;
  }

  @PostConstruct
  @Override
  protected void init() {
    try {
      Calendar calendar= new GregorianCalendar(2023, 4, 14);
			this.attrs.put("calendario", Calendar.getInstance());
      this.attrs.put("isAdmin", JsfBase.isAdminEncuestaOrAdmin());
      this.attrs.put("fechaInicio", new Date(calendar.getTimeInMillis()));
      this.attrs.put("hoy", Fecha.formatear(Fecha.DIA_FECHA));
      this.lazyEventModel= new DefaultScheduleModel();
      this.doLoad();
			if(JsfBase.isAdminEncuestaOrAdmin())
			  this.checkDownloadBackup();
      this.toLoadCuentasCobrar();
      this.toLoadCuentasPagar(Fecha.getHoyEstandar());
      this.toLoadCuentasAgendar();
      this.doLoadCuentasVentas();
      this.toLoadCalendario(Fecha.formatear(Fecha.FECHA_ESTANDAR, new Date(calendar.getTimeInMillis())));
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch        
  } // init

  @Override
  public void doLoad() {
    try {
      this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idUsuario", JsfBase.getIdUsuario());
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
    } // catch
  } 

  private void checkDownloadBackup() {
	  Map<String, Object> params= new HashMap<>();
		try {
			TcManticRespaldosDto respaldo= (TcManticRespaldosDto)DaoFactory.getInstance().toEntity(TcManticRespaldosDto.class, "TcManticRespaldosDto", "ultimo", Collections.EMPTY_MAP);
			if(respaldo!= null) {
			  params.put("idRespaldo", respaldo.getIdRespaldo());
				TcManticControlRespaldosDto control= (TcManticControlRespaldosDto)DaoFactory.getInstance().toEntity(TcManticControlRespaldosDto.class, "TcManticControlRespaldosDto", "ultimo", params);
			  if(control!= null) {
					if(!control.getIdRespaldo().equals(respaldo.getIdRespaldo())) {
					  this.attrs.put("messageBackup", "NO se ha DESCARGADO el respaldo de la 'Base de Datos', desde "+ Global.format(EFormatoDinamicos.DIA_FECHA_HORA, control.getRegistro()));
  				  UIBackingUtilities.execute("PF('downloadBackup').show()");
	  			} // if
				} // if	
				else {
					this.attrs.put("messageBackup", "NUNCA se ha DESCARGADO el respaldo de la 'Base de Datos', por favor realice una descarga a su equipo de trabajo !");
				  UIBackingUtilities.execute("PF('downloadBackup').show()");
				} // else	
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally	
	} // checkDonwloadBackup
  
  public void toLoadCuentasCobrar() {
    List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);      
      columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("cuentas", EFormatoDinamicos.MILES_SIN_DECIMALES));
      columns.add(new Columna("dias", EFormatoDinamicos.MILES_SIN_DECIMALES));
      params.put("sortOrder", "order by dias desc");
      this.lazyModel = new FormatCustomLazy("VistaIndicadoresTableroDto", "cobrar", params, columns);
      UIBackingUtilities.resetDataTable("cobrar");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
  }
  
  public void toLoadCuentasPagar(String hoy) {
    List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put(Constantes.SQL_CONDICION, "date_format(tc_mantic_empresas_deudas.limite, '%Y%m%d')= ".concat(hoy));      
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      params.put("sortOrder", "order by dias desc");
      this.lazyModelPagar = new FormatCustomLazy("VistaIndicadoresTableroDto", "pagar", params, columns);
      UIBackingUtilities.resetDataTable("pagar");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
  }
  
  public void toLoadCuentasAgendar() {
    List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("id", 1L);      
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      params.put("sortOrder", "order by tc_mantic_empresas_deudas.registro");
      this.lazyModelAgendar = new FormatCustomLazy("VistaIndicadoresTableroDto", "agendar", params, columns);
      UIBackingUtilities.resetDataTable("pagar");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
  }
  
  public void doLoadCuentasVentas() {
    List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    StringBuilder sb          = new StringBuilder();
    try {      
      sb.append("tc_mantic_ventas.id_venta_estatus in (").append(EEstatusVentas.PAGADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.TIMBRADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.TERMINADA.getIdEstatusVenta()).append(") and ");
      sb.append("(date_format(tc_mantic_ventas.registro, '%Y%m%d')= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') ");			
      params.put(Constantes.SQL_CONDICION, sb.toString());
      params.put("apartado", Constantes.SQL_VERDADERO);
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("nombreEmpresa", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      this.lazyModelVentas= new FormatCustomLazy("VistaConsultasDto", "ventas", params, columns);
      UIBackingUtilities.resetDataTable("ventas");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
      sb= null;
    } // finally
  }
  
  public void doSelect(SelectEvent selectEvent) {
    DefaultScheduleEvent item= (DefaultScheduleEvent)selectEvent.getObject();
    if(Objects.equals(item, null)) {
      Schedule calendario= (Schedule)selectEvent.getSource();
      this.attrs.put("hoy", Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, (Date)calendario.getInitialDate()));
      this.toLoadCuentasPagar(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)calendario.getInitialDate()));
    } // if
    else {
      this.attrs.put("hoy", Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, item.getStartDate()));
      this.toLoadCuentasPagar(Fecha.formatear(Fecha.FECHA_ESTANDAR, item.getStartDate()));
    } // if  
  }

  public void doDate(SelectEvent selectEvent) {
    this.attrs.put("hoy", Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, (java.util.Date)selectEvent.getObject()));
    this.toLoadCuentasPagar(Fecha.formatear(Fecha.FECHA_ESTANDAR, (java.util.Date)selectEvent.getObject()));
  }
  
  public void doMove(ScheduleEntryMoveEvent event) {
    LOG.info(event);
  } 
  
  public void doResize(ScheduleEntryResizeEvent event) {
    LOG.info(event);
  }

  public void doView(SelectEvent selectEvent) {
    LOG.info("View:" + selectEvent.getObject());
    Schedule calendario= (Schedule)selectEvent.getSource();
    this.toLoadCalendario(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)calendario.getInitialDate()));
  }
  
  private void toLoadCalendario(String date) {
    List<Columna> columns        = new ArrayList<>();    
    Map<String, Object> params   = new HashMap<>();
    DefaultScheduleEvent registro= null;
    try {      
      this.lazyEventModel.clear();
      Periodo periodo= new Periodo(date);
      params.put("inicio", periodo.getInicioMes().toString());      
      params.put("termino", periodo.getTerminoMes().toString());      
      columns.add(new Columna("cantidad", EFormatoDinamicos.MILES_SIN_DECIMALES));
      List<Entity> cuentas= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaIndicadoresTableroDto", "calendario", params);
      for (Entity item: cuentas) {
        String color= "amarillo";
        if(Objects.equals(item.toLong("cantidad"), item.toLong("iniciada")))
          color= "rojo";
        else
          if(Objects.equals(item.toLong("cantidad"), item.toLong("liquidadas")))
            color= "verde";
        registro= new DefaultScheduleEvent(item.toLong("cantidad")+ " CUENTAS", item.toDate("limite"), item.toDate("limite"), item);
        registro.setStyleClass("janal-semaforo-".concat(color));
        registro.setId(item.getKey().toString());
        registro.setEditable(Boolean.FALSE);
				registro.setAllDay(Boolean.TRUE);
				registro.setDescription("Cuentas por pagar ["+ item.toLong("cantidad")+ " ]");
  			this.lazyEventModel.addEvent(registro);        
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
  }
  
}