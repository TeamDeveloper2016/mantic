package mx.org.kaana.kajool.procesos.cargastrabajo.reasignacion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 11/10/2016
 *@time 01:31:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.beans.SelectionItem;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.procesos.cargastrabajo.reasignacion.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.ETipoConsulta;
import mx.org.kaana.kajool.procesos.cargastrabajo.reasignacion.reglas.GestorSQL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@ManagedBean(name="kajoolReasignacionCargasTrabajoFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= 4015822355223178904L;
  private static final String USUARIO_ORIGEN= "usOrigen";		
  private List<UISelectEntity> usuariosEncontrados;
	private List<SelectionItem> temporalOrigen;
	private List<SelectionItem> temporalDestino;
	private List<SelectionItem> movimientos;		
	private UISelectEntity usuarioEncontrado;
	private DualListModel model;		

  public DualListModel getModel() {
		return model;
	}	

	public void setModel(DualListModel model) {
		this.model= model;
	}

	public List<UISelectEntity> getUsuariosEncontrados() {
		return usuariosEncontrados;
	}

	public UISelectEntity getUsuarioEncontrado() {
		return usuarioEncontrado;
	}

	public void setUsuarioEncontrado(UISelectEntity usuarioEncontrado) {
		this.usuarioEncontrado= usuarioEncontrado;
	}

  @PostConstruct
  @Override
	protected void init() {
    try {			      									
      this.movimientos    = new ArrayList<>();
			this.temporalOrigen = new ArrayList<>();
			this.temporalDestino= new ArrayList<>();
			this.attrs.put("curpOrigen", "");
			this.attrs.put("curpDestino", "");			
			this.attrs.put("nombreOrigen", "");			
			this.attrs.put("nombreDestino", "");			
			this.attrs.put("habilitar", true);			
			this.attrs.put("habilitarRefresh", true);	
			this.attrs.put("habilitarGrafica", true);
			this.attrs.put("mensajeMultiseleccion", false);
			this.attrs.put("contadorMovimientos", 0L);	
      this.attrs.put("idCapturista", TcConfiguraciones.getInstance().getPropiedad(CONFIG_CAPTURISTA));
			this.attrs.put("totala", "0");
			this.attrs.put("totalb", "0");						
			this.model= new DualListModel<>();
			loadEntidades();						
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {		
    Map<String, List<SelectionItem>> reasignaciones= null;				
    Transaccion transaccion                        = null;		
    Long contador                                  = 0L;
    try {
      contador= (Long) this.attrs.get("contadorMovimientos");
      if(contador> 0L){
        if(!this.movimientos.isEmpty()){							
          reasignaciones= loadReasignaciones();
          transaccion= new Transaccion(reasignaciones, Long.valueOf(this.attrs.get("idUsuarioOrigen").toString()), Long.valueOf(this.attrs.get("idUsuarioDestino").toString()));
          transaccion.ejecutar(EAccion.PROCESAR);						
          JsfBase.addMessage("Reasignacion", "Se realizarón las reasignaciones correctamente", ETipoMensaje.INFORMACION);
          this.movimientos.clear();				
        }//if
        else
          JsfBase.addMessage("Reasignaciones", "No se detectaron movimientos", ETipoMensaje.ERROR);	
        doLimpiar();
      } // if
      else
        JsfBase.addMessage("Reasignación", "No hay movimientos registrados", ETipoMensaje.ALERTA);
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  } // doLoad    			

  public void onTransfer(TransferEvent event) {				
		List<SelectionItem>transfers= (List<SelectionItem>) event.getItems();							
		try {
			if(!(transfers.size()>1)){
				updateMovimientos(transfers);							
				this.attrs.put("totala", String.valueOf(this.model.getSource().size()));
				this.attrs.put("totalb", String.valueOf(this.model.getTarget().size()));
			}// if
			else{				
				this.attrs.put("habilitarRefresh", false);	
				this.attrs.put("mensajeMultiseleccion", true);
        RequestContext.getCurrentInstance().execute("refresh()");				
			} //else					
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch					
	} // onTransfer 	

  public void doConsultar(ActionEvent event){
		ETipoConsulta tipo              = ETipoConsulta.valueOf(event.getComponent().getId().toUpperCase());
		List<SelectionItem> folioAgregar= null;
		Entity usuario                  = null;
		String curp                     = null;		
		GestorSQL gestor                = null;
		try {						
			if(validaCurp()){
        curp= tipo.equals(ETipoConsulta.ORIGEN)? this.attrs.get("curpOrigen").toString() : this.attrs.get("curpDestino").toString();																						
        gestor= new GestorSQL(curp, Long.valueOf(this.attrs.get("idGrupo").toString()), Long.valueOf(this.attrs.get("entidad").toString()));
        usuario= gestor.getInformacionUsuario(Long.valueOf(this.attrs.get("idCapturista").toString()));
        if(usuario!= null){
          this.attrs.put(tipo.equals(ETipoConsulta.ORIGEN)? "nombreOrigen" : "nombreDestino", Cadena.isVacio(usuario.get("nombreCompleto"))? "Sin asignar" : usuario.get("nombreCompleto").toString());
          this.attrs.put(tipo.equals(ETipoConsulta.ORIGEN)? "idUsuarioOrigen" : "idUsuarioDestino", usuario.getKey());													
          folioAgregar= gestor.cargarFolios(usuario.getKey());
          if(tipo.equals(ETipoConsulta.ORIGEN)){
            this.model.setSource(folioAgregar);
            this.temporalOrigen= folioAgregar;
            this.attrs.put("totala", String.valueOf(folioAgregar.size()));
          } // if
          else{
            this.model.setTarget(folioAgregar);
            this.temporalDestino= folioAgregar;
            this.attrs.put("totalb", String.valueOf(folioAgregar.size()));
          }//else						
        }	// if					
        else{
          JsfBase.addMsgsProperties("clave_no_existe", "Consulta enumerador", ETipoMensaje.ERROR);				
          if(tipo.equals(ETipoConsulta.ORIGEN))
            limpiarOrigen();
          else
            limpiarDestino();
        } // else				
      } // if
      else
        JsfBase.addMessage("Busqueda usuario", "No es posible seleccionar el mismo usuario", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	}	// doConsultar

  public void doRefrescar(){		
    boolean mensajeMultiseleccion= false;
		try {	
      mensajeMultiseleccion= (boolean) this.attrs.get("mensajeMultiseleccion");
      if(mensajeMultiseleccion){
        JsfBase.addMessage("Reasignar", "Solo se permite reasignar un folio a la vez", ETipoMensaje.ERROR);
        this.attrs.put("mensajeMultiseleccion", false);
      } // if
			this.model= new DualListModel<>(this.temporalOrigen, this.temporalDestino);			
			this.attrs.put("totala", String.valueOf(this.model.getSource().size()));
			this.attrs.put("totalb", String.valueOf(this.model.getTarget().size()));
			inicializaValores();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // doRefrescar

  public void doReiniciarCargas(){
		try {
			doRefrescar();
			JsfBase.addMsgsProperties("error_movimientos", "Reasignación de folios", ETipoMensaje.ERROR);				
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch				
	} // doReiniciarCargas

  public void doLimpiar(){		
		try {						
			limpiarOrigen();
			limpiarDestino();
			this.attrs.put("totala", String.valueOf(this.model.getSource().size()));
			this.attrs.put("totalb", String.valueOf(this.model.getTarget().size()));
			this.model= new DualListModel<>();						
			inicializaValores();			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // doLimpiar

  private void limpiarOrigen(){
		this.attrs.put("curpOrigen", "");			
		this.attrs.put("nombreOrigen", "");					
		this.attrs.put("totala", "0");
		this.model.setSource(new ArrayList());
	} // limpiarOrigen
	
	private void limpiarDestino(){
		this.attrs.put("curpDestino", "");			
		this.attrs.put("nombreDestino", "");			
		this.attrs.put("totalb", "0");
		this.model.setTarget(new ArrayList());
	} // limpiarDestino
	
	private void inicializaValores(){
		this.movimientos= new ArrayList<>();
		this.attrs.put("habilitar", true);			
		this.attrs.put("habilitarRefresh", true);			
		this.attrs.put("contadorMovimientos", 0L);				
	} // inicializaValores

  private void updateMovimientos(List<SelectionItem> transfers) throws Exception{
		Long contador= 0L;	
		try {
			contador = (Long) this.attrs.get("contadorMovimientos");
			for (SelectionItem item : transfers) {
        if (this.movimientos.indexOf(item)== -1) {
          this.movimientos.add(item);
          contador= contador + 1;
				} // if	
				else {
          this.movimientos.remove(item);
					contador= contador - 1;
				} // else
			} // for		
			if(contador > 0)					
        this.attrs.put("habilitar", false);			
			else					
        this.attrs.put("habilitar", true);			
			this.attrs.put("habilitarRefresh", false);	
			this.attrs.put("contadorMovimientos", contador);	
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // updateMovimientos

  private Map<String, List<SelectionItem>> loadReasignaciones() throws Exception{
		Map<String, List<SelectionItem>> regresar= null;
		List<SelectionItem> movimientosOrigen		 = null;
		List<SelectionItem> movimientosDestino	 = null;
		try {
			regresar          = new HashMap<>();
			movimientosOrigen = new ArrayList<>();
			movimientosDestino= new ArrayList<>();			
			for(SelectionItem itemAct: this.movimientos){
				if(this.temporalOrigen.indexOf(itemAct)!= -1)
					movimientosOrigen.add(itemAct);				
			}//for				
			regresar.put(this.attrs.get("idUsuarioDestino").toString(), movimientosOrigen);
			for(SelectionItem itemAct: this.movimientos){
				if(this.temporalDestino.indexOf(itemAct)!= -1)
					movimientosDestino.add(itemAct);				
			}//for						
			regresar.put(this.attrs.get("idUsuarioOrigen").toString(), movimientosDestino);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	}	// loadReasignaciones

  public void doAsignaUsuario(ActionEvent event){
		String idComponente= event.getComponent().getId();
		try {
			this.attrs.put("habilitarGrafica", false);
			this.attrs.put("usuario", idComponente);
			this.attrs.put("update", idComponente.equals(USUARIO_ORIGEN)? ":datos:tabla, nombreOrigen, totala, curpOrigen" : ":datos:tabla, nombreDestino, totalb, curpDestino");				
			this.attrs.put("criterio", "");
			LOG.info("doAsignaUsuario [attrs[" + this.attrs + "]]");
			this.usuariosEncontrados= new ArrayList<>();
			this.usuarioEncontrado  = new UISelectEntity("");		
			LOG.info("Inicializando lista usuarios y usuario encontrado");
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch				
	} // doAsignaUsuario

  public void doBuscarUsuario(){
		List<Columna>campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("nombreCompleto", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("curp", EFormatoDinamicos.MAYUSCULAS));
			this.attrs.put("criterio", this.attrs.get("criterio").toString().toUpperCase());
			LOG.info("Consultando usuarios...");
			this.usuariosEncontrados= UIEntity.build("VistaReasignacionCargasTrabajoDto", "busquedaUsuario", this.attrs, campos);
			if(this.usuariosEncontrados!= null && !this.usuariosEncontrados.isEmpty())
				this.usuarioEncontrado= (UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(this.usuariosEncontrados);
			LOG.info("Usuarios encontrados [" + this.usuariosEncontrados.size() + "]");
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch				
	} // doBuscarUsuario
	
	public void doAceptarUsuario(){
		String usuarioEvalua           = null;		
		String curp                    = null;
		List<SelectionItem>folioAgregar= null;
    GestorSQL gestor               = null;
		try {		
			LOG.info("Asignando usuario...");
			if(this.usuarioEncontrado!= null){
				curp = this.usuariosEncontrados.get(this.usuariosEncontrados.indexOf(this.usuarioEncontrado)).toString("curp");				
				LOG.info("CURP [".concat(curp).concat("]"));
				LOG.info("Usuario a evaluar [".concat(String.valueOf(this.attrs.get("usuario"))).concat("]"));
				usuarioEvalua= String.valueOf(this.attrs.get("usuario"));				
				this.attrs.put(usuarioEvalua.equals(USUARIO_ORIGEN)? "curpOrigen" : "curpDestino", curp);	
        if(validaCurp()){
          this.attrs.put(usuarioEvalua.equals(USUARIO_ORIGEN)? "nombreOrigen" : "nombreDestino", this.usuariosEncontrados.get(this.usuariosEncontrados.indexOf(this.usuarioEncontrado)).toString("nombreCompleto"));
          LOG.info("Nombre usuario[" + this.usuariosEncontrados.get(this.usuariosEncontrados.indexOf(this.usuarioEncontrado)).toString("nombreCompleto") + "]");
          this.attrs.put(usuarioEvalua.equals(USUARIO_ORIGEN)? "idUsuarioOrigen" : "idUsuarioDestino", this.usuarioEncontrado.getKey());													
          gestor= new GestorSQL(curp, Long.valueOf(this.attrs.get("idGrupo").toString()), Long.valueOf(this.attrs.get("entidad").toString()));
          folioAgregar= gestor.cargarFolios(this.usuarioEncontrado.getKey());
          if(usuarioEvalua.equals(USUARIO_ORIGEN)){					
            this.model.setSource(folioAgregar);
            this.temporalOrigen= folioAgregar;
            this.attrs.put("totala", String.valueOf(folioAgregar.size()));
          } // if
          else{
            this.model.setTarget(folioAgregar);
            this.temporalDestino= folioAgregar;
            this.attrs.put("totalb", String.valueOf(folioAgregar.size()));
          }//else					
        } // if
        else
          JsfBase.addMessage("Busqueda usuario", "No es posible seleccionar el mismo usuario", ETipoMensaje.ERROR);
			}//if
		} // try		
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			JsfBase.addMessage(this.attrs.toString());
			Error.mensaje(e);			
		} // catch				
	} // doAceptarUsuario

  private boolean validaCurp() throws Exception{
		boolean regresar= false;		
		try {
			regresar= !this.attrs.get("curpOrigen").toString().equals(this.attrs.get("curpDestino").toString());				
		} // try
		catch (Exception e) {						
			throw e;
		} // catch				
		return regresar;
	} // validaCurp

  @Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.temporalDestino);
		Methods.clean(this.temporalOrigen);
		Methods.clean(this.movimientos);		
		Methods.clean(this.usuariosEncontrados);		
		Methods.clean(this.model);		
	} // finalize		
}
