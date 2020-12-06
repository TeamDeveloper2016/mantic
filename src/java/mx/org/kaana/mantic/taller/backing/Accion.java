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
	private EAccion accion;
	private UISelectEntity ikEmpresa;
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikProveedor;

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
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}

  public UISelectEntity getIkEmpresa() {
    return ikEmpresa;
  }

  public void setIkEmpresa(UISelectEntity ikEmpresa) {
    this.ikEmpresa = ikEmpresa;
    if(this.ikEmpresa!= null)
      registroServicio.getServicio().setIdEmpresa(this.ikEmpresa.getKey());
  }

  public UISelectEntity getIkAlmacen() {
    return ikAlmacen;
  }

  public void setIkAlmacen(UISelectEntity ikAlmacen) {
    this.ikAlmacen = ikAlmacen;
    if(this.ikAlmacen!= null)
      registroServicio.getServicio().setIdAlmacen(this.ikAlmacen.getKey());
  }

  public UISelectEntity getIkProveedor() {
    return ikProveedor;
  }

  public void setIkProveedor(UISelectEntity ikProveedor) {
    this.ikProveedor = ikProveedor;
    if(this.ikProveedor!= null)
      registroServicio.getServicio().setIdProveedor(this.ikProveedor.getKey());
  }
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("idServicio", JsfBase.getFlashAttribute("idServicio"));
			this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());			
      this.accion = (EAccion)JsfBase.getFlashAttribute("accion");
      this.doLoad();      					
      this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadCollections() {
		loadTiposMediosPagos();
	} // loadCollections
	
  public void doLoad() {
    Long idServicio    = -1L;
		MotorBusqueda motor= null;
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
			motor= new MotorBusqueda(-1L);
      switch (this.accion) {
        case AGREGAR:
					this.attrs.put("clienteRegistrado", false);
          this.registroServicio = new RegistroServicio();					
					this.registroServicio.setCliente(motor.toCliente(((Entity)motor.toClienteDefault()).getKey()));
					this.loadCollections();
          this.registroServicio.getServicio().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
          this.setIkEmpresa(new UISelectEntity(new Entity(this.registroServicio.getServicio().getIdEmpresa())));
          this.setIkEmpresa(new UISelectEntity(new Entity(-1L)));
          this.setIkAlmacen(new UISelectEntity(new Entity(-1L)));
          break;
        case MODIFICAR:
        case CONSULTAR:
          idServicio = Long.valueOf(this.attrs.get("idServicio").toString());
          this.registroServicio = new RegistroServicio(idServicio);
					this.loadCollections();					
					this.attrs.put("clienteRegistrado", this.registroServicio.getServicio().getIdCliente()!= null && this.registroServicio.getServicio().getIdCliente()>-1L && !this.registroServicio.getCliente().getIdCliente().equals(motor.toClienteDefault().getKey()));
          this.setIkEmpresa(new UISelectEntity(new Entity(this.registroServicio.getServicio().getIdEmpresa())));
          this.setIkAlmacen(new UISelectEntity(new Entity(this.registroServicio.getServicio().getIdAlmacen())));
          this.setIkProveedor(new UISelectEntity(new Entity(this.registroServicio.getServicio().getIdProveedor())));
          break;
      } // switch 			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad
  
	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
 			List<UISelectEntity> empresas= (List<UISelectEntity>)this.attrs.get("empresas");
			if(!empresas.isEmpty()) {
				if(this.accion.equals(EAccion.AGREGAR))
  				this.setIkEmpresa(empresas.get(0));
			  else 
				  this.setIkEmpresa(empresas.get(empresas.indexOf(this.getIkEmpresa())));
			} // if	
  		params.put("sucursales", this.getIkEmpresa());
      this.attrs.put("almacenes", UIEntity.seleccione("TcManticAlmacenesDto", "almacenes", params, columns, "clave"));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) {
				if(this.accion.equals(EAccion.AGREGAR))
				  this.setIkAlmacen(almacenes.get(0));
			  else
				  this.setIkAlmacen(almacenes.get(almacenes.indexOf(this.getIkAlmacen())));
			} // if
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("proveedores", UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			if(!proveedores.isEmpty()) { 
				if(this.accion.equals(EAccion.AGREGAR))
				  this.setIkProveedor(proveedores.get(0));
				else
				  this.setIkProveedor(proveedores.get(proveedores.indexOf(this.getIkProveedor())));
			} // if	
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}  

  public String doAceptar() {
    Transaccion transaccion = null;
    String regresar = null;
    try {
			this.registroServicio.setRegistrarCliente((Boolean)this.attrs.get("clienteRegistrado"));
      transaccion = new Transaccion(this.registroServicio);
      if (transaccion.ejecutar(this.accion)) {
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
				// params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
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
  
  public void doLoadAlmacenes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", this.registroServicio.getServicio().getIdEmpresa());
      this.attrs.put("almacenes", UIEntity.seleccione("TcManticAlmacenesDto", "almacenes", params, columns, "clave"));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
			  this.setIkAlmacen(almacenes.get(0));
   } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}

}
