package mx.org.kaana.kajool.procesos.cargastrabajo.asignacion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/10/2016
 *@time 08:15:26 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.cargastrabajo.asignacion.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.db.dto.TrJanalMovimientosDto;
import mx.org.kaana.kajool.procesos.enums.EEstatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@ManagedBean(name="kajoolAsignacionCargasTrabajoFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

	private static final Log LOG                  = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID    = 1210194203528109866L;
  private DualListModel<UISelectItem> selectionItems;
  private List<UISelectItem> responsables;
  private List<UISelectItem> capturistas;
  private List<UISelectItem> controles;

  public DualListModel<UISelectItem> getSelectionItems() {
    return selectionItems;
  }

  public void setSelectionItems(DualListModel<UISelectItem> selectionItems) {
    this.selectionItems= selectionItems;
  }

  public List<UISelectItem> getResponsables() {
    return responsables;
  }

  public List<UISelectItem> getCapturistas() {
    return capturistas;
  }

  public List<UISelectItem> getControles() {
    return controles;
  }

  @PostConstruct
  @Override
	protected void init() {
    try {			
      this.attrs.put("sizeDisponibles", 0L);
      this.attrs.put("sizeAsignados", 0L);
      this.selectionItems= new DualListModel<>();
      loadEntidades();
      doActualizaCapturistas();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {
    UISelectItem control= null;
    try {
      this.attrs.put("idEstatus", EEstatus.DISPONIBLE.getKey());
      control= toControl();
      if(control!= null)
        this.attrs.put("controlBusqueda", control.getLabel());
      loadFoliosDisponibles();
      loadFoliosAsignados();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doLoad

  private UISelectItem toControl(){
    UISelectItem regresar= null;
    Long controlSeleccion= Long.valueOf(this.attrs.get("control").toString());
    for(UISelectItem control: this.controles)
      if(control.getValue().equals(controlSeleccion)){
        regresar= control;
    } // for
    return regresar;
  } // toControl

  public void doActualizaResponsables(){
    try {
      this.attrs.put("idResponsable", TcConfiguraciones.getInstance().getPropiedad(CONFIG_RESPONSABLE));
      this.responsables= UISelect.build("VistaCargasTrabajoDto", "responsables", this.attrs, "nombreCompleto", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("responsable", UIBackingUtilities.toFirstKeySelectItem(this.responsables));
      doActualizaCapturistas();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doActualizaSupervisores

  public void doActualizaCapturistas(){
    try {
      this.attrs.put("idCapturista", TcConfiguraciones.getInstance().getPropiedad(CONFIG_CAPTURISTA));
      this.capturistas= UISelect.build("VistaCargasTrabajoDto", "capturistas", this.attrs, "nombreCompleto", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("capturista", UIBackingUtilities.toFirstKeySelectItem(this.capturistas));
      this.controles= UISelect.build("VistaCargasTrabajoDto", "controles", this.attrs, "control", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("control", UIBackingUtilities.toFirstKeySelectItem(this.controles));
      doLoad();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doActualizaEntrevistadores

  public void onTransfer(TransferEvent event) {
    List<UISelectItem> seleccionados= null;
    Long estatusMovimiento          = null;
    Long estatusAnterior            = null;
    Long asignado                   = null;
    Long disponible                 = null;
    Boolean asignar                 = false;
    try {
      seleccionados=(List<UISelectItem>) event.getItems();
      asignado= EEstatus.ASIGNADO.getKey();
      disponible= EEstatus.DISPONIBLE.getKey();
      asignar= event.isAdd();
      estatusMovimiento= asignar ? asignado : disponible;
      estatusAnterior= asignar ? disponible : asignado;
      doEjecutar(seleccionados, estatusMovimiento, estatusAnterior, asignar);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
    }  // catch
    finally {
      Methods.clean(seleccionados);
    } // finally
  } // onTransfer

  private void loadFoliosDisponibles(){
      loadFolios(true);
  } // loadFoliosDisponibles

  private void loadFoliosAsignados(){
      loadFolios(false);
  } // loadFoliosAsignados

  private void loadFolios(boolean isDisponibles){
    List<UISelectItem>folios= null;
    List<String> fields     = null;
    try {
      fields= new ArrayList<>();
			fields.add("control");
			fields.add("folio");
      this.attrs.put("idUsuario", this.attrs.get("capturista"));
      this.attrs.put("idSupervisa", this.attrs.get("responsable"));
      folios= UISelect.build("VistaCargasTrabajoDto", isDisponibles ? "disponibles" : "asignados", this.attrs, fields, " - ", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
      if(isDisponibles){
        this.selectionItems.setSource(folios);
        this.attrs.put("sizeDisponibles", folios.size());
      } // if
      else{
        this.selectionItems.setTarget(folios);
        this.attrs.put("sizeAsignados", folios.size());
      } // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // loadFolios

  public void doEjecutar(List<UISelectItem> seleccionados, Long estatusMovimiento, Long estatusAnterior, boolean asignar){
    Transaccion transaccion           = null;
    TrJanalMovimientosDto pivote           = null;
    List<TrJanalMovimientosDto> movimientos= null;
    try {
      movimientos=new ArrayList<>();
      if (seleccionados!=null && seleccionados.size()> 0) {
        for (UISelectItem movimiento : seleccionados) {
          this.attrs.put("idMuestra", movimiento.getValue());
          this.attrs.put("idEstatus", estatusAnterior);
          pivote= (TrJanalMovimientosDto) DaoFactory.getInstance().findFirst(TrJanalMovimientosDto.class, "rowDec", this.attrs);
          pivote.setIdMovimiento(-1L);
          pivote.setIdEstatus(estatusMovimiento);
          pivote.setIdUsuario(asignar ? Numero.getLong(this.attrs.get("capturista").toString()) : null);
          pivote.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          movimientos.add(pivote);
        } // for
        transaccion= new Transaccion(movimientos);
        if (transaccion.ejecutar(EAccion.AGREGAR))
          JsfBase.addMessage("Se ".concat(asignar ? "asignaron" : "eliminaron").concat(" con éxito los folios"), ETipoMensaje.INFORMACION);
        else
          JsfBase.addMessage("No se pudieron ".concat(asignar ? "asignar" : "eliminar").concat(" los folios"), ETipoMensaje.INFORMACION);
        loadFoliosAsignados();
        loadFoliosDisponibles();
      } // if
      else
        JsfBase.addMessage("Debe de seleccionar por lo menos 1 registro para ".concat(asignar ? "asignar" : "eliminar"), ETipoMensaje.ERROR);
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage("Error: No se ".concat(asignar ? "asignaron" : "eliminaron").concat(" los folios de la carga de trabajo"), ETipoMensaje.ERROR);
    } // catch
    finally {
      Methods.clean(movimientos);
    } // finally
  } // doEjecutar
}
