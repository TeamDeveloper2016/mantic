package mx.org.kaana.mantic.taller.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.taller.beans.ContactoCliente;
import mx.org.kaana.mantic.taller.reglas.Transaccion;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;
import mx.org.kaana.mantic.taller.reglas.MotorBusqueda;

@Named(value = "manticTallerAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -7668104942302148046L;
  private RegistroServicio registroServicio;
	private UISelectEntity clienteBusqueda;

	public RegistroServicio getRegistroServicio() {
		return registroServicio;
	}

	public void setRegistroServicio(RegistroServicio registroServicio) {
		this.registroServicio = registroServicio;
	}  

	public UISelectEntity getClienteBusqueda() {
		return clienteBusqueda;
	}

	public void setClienteBusqueda(UISelectEntity clienteBusqueda) {
		this.clienteBusqueda = clienteBusqueda;
	}	
	
	public String getAgregar() {
		return ((EAccion)this.attrs.get("accion")).equals(EAccion.AGREGAR)? "none": "";
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idServicio", JsfBase.getFlashAttribute("idServicio"));
			this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());			
      doLoad();      					
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadCollections(){
		loadTiposMediosPagos();
	} // loadCollections
	
  public void doLoad() {
    EAccion eaccion    = null;
    Long idServicio    = -1L;
		MotorBusqueda motor= null;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
			motor= new MotorBusqueda(-1L);
      switch (eaccion) {
        case AGREGAR:
					this.attrs.put("clienteRegistrado", false);
          this.registroServicio = new RegistroServicio();					
					this.registroServicio.setCliente(motor.toCliente(((Entity)motor.toClienteDefault()).getKey()));
					loadCollections();
          break;
        case MODIFICAR:
        case CONSULTAR:
          idServicio = Long.valueOf(this.attrs.get("idServicio").toString());
          this.registroServicio = new RegistroServicio(idServicio);
					loadCollections();					
					this.attrs.put("clienteRegistrado", this.registroServicio.getServicio().getIdCliente()!= null && this.registroServicio.getServicio().getIdCliente()>-1L && !this.registroServicio.getCliente().getIdCliente().equals(motor.toClienteDefault().getKey()));
          break;
      } // switch 			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {
    Transaccion transaccion = null;
    String regresar = null;
    try {
			this.registroServicio.setRegistrarCliente((Boolean)this.attrs.get("clienteRegistrado"));
      transaccion = new Transaccion(this.registroServicio);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro el servicio de taller de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else {
        JsfBase.addMessage("Ocurrió un error al registrar el registro de taller", ETipoMensaje.ERROR);
      } // else
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
	private void loadTiposMediosPagos(){
		List<UISelectItem> medioPagos= null;
		Map<String, Object>params    = null;
		List<String> campos          = null;
		try {
			params= new HashMap<>();
			campos= new ArrayList<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos.add("nombre");
			medioPagos= UISelect.build("TcManticTiposMediosPagosDto", "row", params, campos, " ", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("mediosPagos", medioPagos);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // loadEstatusServicios
	
	public void doLoadAtributos(){
		MotorBusqueda motor= null;
		try {
			if(this.registroServicio.getClienteSeleccion()!= null && !this.registroServicio.getClienteSeleccion().getKey().equals(-1L)){
				motor= new MotorBusqueda(null);
				this.registroServicio.setCliente(motor.toCliente(this.registroServicio.getClienteSeleccion().getKey()));
				this.registroServicio.setContactoCliente(motor.toContactoCliente(this.registroServicio.getClienteSeleccion().getKey()));
			} // if
			else{
				this.registroServicio.setCliente(new TcManticClientesDto());
				this.registroServicio.setContactoCliente(new ContactoCliente());
			} // else		
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doLoadAtributos
	
	public void doBusquedaClientes(){
		List<UISelectEntity> clientes= null;
    Map<String, Object> params   = null;
		List<Columna>columns         = null;
    try {
			columns= new ArrayList<>();
			if(this.attrs.get("busqueda")!= null && this.attrs.get("busqueda").toString().length()> 3){
				params = new HashMap<>();      
				params.put(Constantes.SQL_CONDICION, "upper(razon_social) like upper('%".concat(this.attrs.get("busqueda").toString()).concat("%')"));
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
				columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));			
				columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));			
				clientes = UIEntity.build("VistaClientesDto", "findRazonSocial", params, columns, Constantes.SQL_TODOS_REGISTROS);      
				this.attrs.put("clientesBusqueda", clientes);      
				this.attrs.put("resultados", clientes.size());      
			} // if
			else{
				JsfBase.addMessage("Cliente", "Favor de teclear por lo menos 3 caracteres.", ETipoMensaje.ALERTA);
			} // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
	} // doBusquedaClientes
	
	public void doAsignaCliente(){
		List<UISelectEntity> clientes        = null;
		List<UISelectEntity> clientesBusqueda= null;
		UISelectEntity cliente               = null;
		MotorBusqueda motor                  = null;
		try {
			clientesBusqueda= (List<UISelectEntity>) this.attrs.get("clientesBusqueda");
			cliente= clientesBusqueda.get(clientesBusqueda.indexOf(this.clienteBusqueda));
			clientes= new ArrayList<>();
			clientes.add(cliente);
			motor= new MotorBusqueda(null);
			this.registroServicio.setCliente(motor.toCliente(cliente.getKey()));
			this.registroServicio.setContactoCliente(motor.toContactoCliente(cliente.getKey()));
			this.registroServicio.setClienteSeleccion(cliente);
			this.attrs.put("clientes", clientes);						
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
}
