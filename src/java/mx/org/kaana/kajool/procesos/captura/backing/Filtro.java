package mx.org.kaana.kajool.procesos.captura.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.db.dto.TrJanalMovimientosDto;
import mx.org.kaana.kajool.db.dto.TrJanalVisitasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.captura.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.EEstatus;
import mx.org.kaana.kajool.procesos.enums.EEstatusSemaforos;
import mx.org.kaana.kajool.procesos.enums.EPerfiles;
import mx.org.kaana.kajool.procesos.enums.ERespuestas;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolCapturaFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= -3565375733676226695L;
	private FormatLazyModel lazyModelVisitas;
	private List<UISelectItem> capturistas;

	public FormatLazyModel getLazyModelVisitas() {
		return lazyModelVisitas;
	}

	public List<UISelectItem> getCapturistas() {
		return capturistas;
	}

	public void setLazyModelVisitas(FormatLazyModel lazyModelVisitas) {
		this.lazyModelVisitas=lazyModelVisitas;
	}
	
  @PostConstruct
  @Override
	protected void init() {
    try {			
      loadEntidades();
      this.attrs.put("control", "");
      this.attrs.put("folio", "");
      this.attrs.put("idUsuario", JsfBase.getIdUsuario());
      this.attrs.put("condicionUsuario", JsfBase.isAdminEncuestaOrAdmin()? "" : "and tr_janal_movimientos.id_usuario=" + JsfBase.getIdUsuario());
      this.attrs.put("idEstatus", EEstatus.DISPONIBLE.getKey());
			doActualizaCapturistas();     
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {
		List<Columna>campos= null;
    try {
      campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
			this.attrs.put("condicionUsuario", "and tc_janal_usuarios.id_usuario=".concat(this.attrs.get("capturista").toString()));
      this.lazyModel= new FormatLazyModel("VistaCapturaDto", "captura", this.attrs, campos);
			UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(campos);
		} // finally
  } // doLoad
	
	public void loadVisitas(boolean visita) {
		List<Columna>campos= null;
    try {
			if(!visita) {
				this.attrs.put("idMuestra", ((Entity)this.attrs.get("selected")).getKey());							
				this.attrs.put("seleccionado", ((Entity)this.attrs.get("selected")));
			} // if
      campos= new ArrayList<>();
			campos.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));
			campos.add(new Columna("hrIni", EFormatoDinamicos.HORA_CORTA));
			campos.add(new Columna("hrTer", EFormatoDinamicos.HORA_CORTA));
			campos.add(new Columna("nomEnt", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModelVisitas= new FormatLazyModel("TrJanalVisitasDto", "visitas", this.attrs, campos);
			UIBackingUtilities.resetDataTable("tablaVisitas");
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(campos);
		} // finally
	}

  public String doSemaforo(Value estatus){		
		EEstatusSemaforos semaforo= EEstatusSemaforos.toIdEstatus(estatus.toLong());		
	  return semaforo.getSemaforo().getNombre();											
  } // doSemaforo	

  public String doCaptura(){
    String regresar            = null;
    TrJanalMovimientosDto movimiento= null;
    Entity seleccionado        = null;
    try {
      seleccionado= (Entity) this.attrs.get("selected");
      if(seleccionado!= null){
        movimiento= new TrJanalMovimientosDto();
        movimiento.setIdMuestra(seleccionado.getKey());
        movimiento.setIdUsuario(seleccionado.toLong("idUsuario"));
        movimiento.setIdSupervisa(seleccionado.toLong("idSupervisa"));
        JsfBase.setFlashAttribute("movimiento", movimiento);
				JsfBase.setFlashAttribute("idMuestra", ((Entity)this.attrs.get("selected")).getKey());
        regresar= "agregar".concat(Constantes.REDIRECIONAR);
      } // if
    } // try // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  } // doCaptura
	
	public void doAgregarVisita() {
		TrJanalVisitasDto trJanalVisitasDto= null;
		Transaccion transaccion            = null;
		Value value                        = null;
		TrJanalMovimientosDto movimiento	 = null;
    Entity seleccionado                = null;
		try {			trJanalVisitasDto= new TrJanalVisitasDto();
			value= DaoFactory.getInstance().toField("TrJanalVisitasDto", "consecutivo", this.attrs, "visita");
			trJanalVisitasDto.setVisita(value!= null? Numero.getLong(value.toString())+ 1L: 1L);
			trJanalVisitasDto.setHrIni(new Timestamp(((Date) this.attrs.get("horaInicio")).getTime()));
			trJanalVisitasDto.setHrTer(new Timestamp(((Date) this.attrs.get("horaTermino")).getTime()));
			trJanalVisitasDto.setIdMuestra((Long)this.attrs.get("idMuestra"));
			trJanalVisitasDto.setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
			trJanalVisitasDto.setObservaciones(this.attrs.get("observaciones")!= null? this.attrs.get("observaciones").toString(): null);
			trJanalVisitasDto.setIdResultado(Numero.getLong(this.attrs.get("respuesta").toString()));
			trJanalVisitasDto.setFecha(new java.sql.Date(((Date) this.attrs.get("fecha")).getTime()));
			trJanalVisitasDto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			trJanalVisitasDto.setNomEnt(JsfBase.getAutentifica().getEmpleado().getNombreCompleto());
			// Movimiento
			seleccionado= (Entity) this.attrs.get("seleccionado");
      if(seleccionado!= null){
        movimiento= new TrJanalMovimientosDto();
        movimiento.setIdMuestra(seleccionado.getKey());
        movimiento.setIdUsuario(seleccionado.toLong("idUsuario"));
        movimiento.setIdSupervisa(seleccionado.toLong("idSupervisa"));
				if(Numero.getLong(this.attrs.get("respuesta").toString()).equals(ERespuestas.ENTREVISTA_COMPLETA.getIdResultado())) 
					movimiento.setIdEstatus(EEstatus.COMPLETO.getKey());											
				else {						
					if(Numero.getLong(this.attrs.get("respuesta").toString()).equals(ERespuestas.ENTREVISTA_INCOMPLETA.getIdResultado())) {
						movimiento.setIdEstatus(EEstatus.PARCIAL.getKey());
					} // if
					else {
						movimiento.setIdEstatus(EEstatus.LIBERADO_CAMPO.getKey());
					} // else		
				} // else
			} // if
			transaccion= new Transaccion(true, movimiento, trJanalVisitasDto, null);
			if(transaccion.ejecutar(EAccion.JUSTIFICAR)) {
				this.attrs.put("horaInicio", null);
				this.attrs.put("horaTermino", null);
				this.attrs.put("fecha", null);
				this.attrs.put("observaciones", null);
				JsfBase.addMessage("Se agregó la visita con éxito.");
			} // if
			else
				JsfBase.addMessage("Ocurrió un error al agregar la visita.");
			loadVisitas(true);
		} // try
		catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch    
	}

  public String doEstatus(Entity row) {
    Value regresar= null;
    this.attrs.put("idMuestra", row.toLong("idKey"));
    try {
      regresar= DaoFactory.getInstance().toField("VistaCapturaDto", "estatus", attrs, "descripcion");
    } // try
    catch(Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar!= null? regresar.toString().toUpperCase(): "";
  }
	
	public void doActualizaCapturistas(){
    try {
      this.attrs.put("idCapturista", TcConfiguraciones.getInstance().getPropiedad(CONFIG_CAPTURISTA));
      this.attrs.put("condicionUsuario", JsfBase.getAutentifica().getEmpleado().getDescripcionPerfil().equals(EPerfiles.CAPTURISTA.name()) ? "and tc_janal_usuarios.id_usuario=" + JsfBase.getIdUsuario() : "");
      this.capturistas= UISelect.build("VistaCargasTrabajoDto", "capturistasCuestionarios", this.attrs, "nombreCompleto", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);			
      this.attrs.put("capturista", UIBackingUtilities.toFirstKeySelectItem(this.capturistas));			
      doLoad();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doActualizaCapturistas
  
}
