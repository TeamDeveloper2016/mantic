package mx.org.kaana.mantic.contadores.backing;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.mantic.contadores.beans.Contador;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.contadores.reglas.Transaccion;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.contadores.beans.Producto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/08/2025
 *@time 01:24:37 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "manticContadoresAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final Log LOG              = LogFactory.getLog(Accion.class);
  private static final long serialVersionUID= -216275933066745916L;
  
  private EAccion accion;
  private Contador contador;
  private Producto producto;
  private Long idContador;

  public Contador getContador() {
    return contador;
  }

  public void setContador(Contador contador) {
    this.contador = contador;
  }

  public Producto getProducto() {
    return producto;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  public Boolean getAplicar() {
    return Objects.equals(this.accion, EAccion.AGREGAR) || Objects.equals(this.accion, EAccion.MODIFICAR);
  }
  
  public Boolean getEdit() {
    return Objects.equals(this.contador.getIdContadorEstatus(), 3L); // INTEGRANDO
  }
  
  @PostConstruct
  @Override
  public void init() {
    try {
//  		if(Objects.equals(JsfBase.getFlashAttribute("accion"), null))
//				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.accion    = Objects.equals(JsfBase.getFlashAttribute("accion"), null)? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.idContador= Objects.equals(JsfBase.getFlashAttribute("idContador"), null)? -1L: (Long)JsfBase.getFlashAttribute("idContador");
      this.attrs.put("automatico", Boolean.TRUE);
      this.attrs.put("retorno", Objects.equals(JsfBase.getFlashAttribute("retorno"), null)? "/Paginas/Mantic/Contadores/filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("buscaPorCodigo", Boolean.FALSE);
      this.doLoad(); 
      this.toLoadEmpresas();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } 
	
  public void doLoad() {
    Map<String, Object> params= new HashMap<>();
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      params.put(Constantes.SQL_CONDICION, "id_contador= "+ this.idContador);      
      this.producto= new Producto();
      switch (this.accion) {
        case AGREGAR:
          this.contador= new Contador();
          this.contador.setIdUsuario(JsfBase.getIdUsuario());
          break;
        case MODIFICAR:
        case CONSULTAR:
          this.contador= (Contador)DaoFactory.getInstance().toEntity(Contador.class, "TcManticContadoresDto", params);
          this.contador.toLoadProductos();
          this.contador.setIkEmpresa(new UISelectEntity(this.contador.getIdEmpresa()));
          this.contador.setIkAlmacen(new UISelectEntity(this.contador.getIdAlmacen()));
          this.contador.setIkTrabaja(new UISelectEntity(this.toLoadEmpleados(this.contador.getIdTrabaja())));
          this.doLoadAlmacenes();
          break;
      } // switch      
      this.contador.setArticulos(new Long(this.contador.getProductos().size()));
    } // try 
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } 

	public String doAplicar() {
    String regresar = null;
    try {      
      if(Objects.equals(this.contador.getIdContadorEstatus(), null) || this.contador.getIdContadorEstatus()< 2L)
        this.contador.setIdContadorEstatus(2L);
      regresar= this.doAceptar();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    return regresar;
  }

	public String doAceptar() {
    Transaccion transaccion= null;
    String regresar        = null;
    try {
      transaccion= new Transaccion(this.contador);
      if(transaccion.ejecutar(this.accion)) {
        regresar= this.doCancelar();
        JsfBase.addMessage("Se registro el conteo de forma correcta", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar el conteo", ETipoMensaje.ERROR);      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } 

  public String doCancelar() {
		JsfBase.setFlashAttribute("idContadorProcess", this.contador.getIdContador());
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } 
  
	public List<UISelectEntity> doCompleteEmpleado(String query) {
 		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = Boolean.FALSE;
    try {
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("curp", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(query)) {
        buscaPorCodigo= query.startsWith(".");
  			query= query.replaceAll(Constantes.CLEAN_SQL, "").trim();
				query= query.toUpperCase().replaceAll("(,| |\\t)+", ".*");
			} // if	
			else
				query= "WXYZ";
  		params.put("codigo", query);
			if(buscaPorCodigo)
        this.attrs.put("usuarios", UIEntity.build("TcJanalUsuariosDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("usuarios", UIEntity.build("TcJanalUsuariosDto", "porNombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("usuarios");
	}	
  
	private Entity toLoadEmpleados(Long idUsuario) {
    Entity regresar           = null;
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("cuenta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("curp", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
 			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      if(Objects.equals(this.accion, EAccion.AGREGAR))
   			params.put(Constantes.SQL_CONDICION, "tc_janal_usuarios.id_usuario= "+ idUsuario+ " and tr_mantic_empresa_personal.id_activo= 1");
      else    
   			params.put(Constantes.SQL_CONDICION, "tc_janal_usuarios.id_usuario= "+ idUsuario);
  		regresar= (Entity)DaoFactory.getInstance().toEntity("TcJanalUsuariosDto", "empleados", params);
      if(Objects.equals(regresar, null))
        regresar= new Entity(-1L);
      else
        UIBackingUtilities.toFormatEntity(regresar, columns);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
    return regresar;
	}

	private void toLoadEmpresas() {
		List<Columna> columns        = new ArrayList<>();
    Map<String, Object> params   = new HashMap<>();
    List<UISelectEntity> empresas= null;
    try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			empresas= (List<UISelectEntity>) UIEntity.seleccione("TcManticEmpresasDto", "empresas", params, columns, "clave");
      this.attrs.put("empresas", empresas);
      if(Objects.equals(this.accion, EAccion.AGREGAR)) 
        this.doLoadAlmacenes();
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
  
	public void doLoadAlmacenes() throws Exception {
		List<UISelectEntity> almacenes= null;
		Map<String, Object> params    = new HashMap<>();
		List<Columna> columns         = new ArrayList<>();
		try {
  	  params.put("idEmpresa", this.contador.getIdEmpresa());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));							
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));							
			columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));							
			almacenes= UIEntity.seleccione("VistaAlmacenesDto", "almacenesEmpresa", params, columns, Constantes.SQL_TODOS_REGISTROS, "clave");      
			this.attrs.put("almacenes", almacenes);
		} // try
		catch (Exception e) {			
      Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
	} 
 
  public void doAdd() {  
    List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
    try {
      if(!Objects.equals(articulos, null) && !articulos.isEmpty()) {
        int index= articulos.indexOf(this.producto.getIkArticulo());
        if(index>= 0)
          this.producto.setIkArticulo(articulos.get(index));
      } // if
      if(!Objects.equals(this.producto.getIkArticulo(), null) && !this.producto.getIkArticulo().isEmpty()) {
        this.producto.setIdContador(this.contador.getIdContador());
        this.producto.setCodigo(this.producto.getIkArticulo().toString("propio"));
        this.producto.setNombre(this.producto.getIkArticulo().toString("nombre"));
        if(this.contador.add(producto)) 
          JsfBase.addMessage("Alerta", "El articulo se encuentra en la lista !");          
        // UIBackingUtilities.execute("jsContadores.exists('"+ this.producto.getIkArticulo().toString("propio")+ "', '"+ this.producto.getIkArticulo().toString("nombre")+ "');");
        this.producto= new Producto();
        this.contador.setArticulos(new Long(this.contador.getProductos().size()));
      } // if  
		} // try
		catch (Exception e) {			
      Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
  }

  public void doRowDblselect(SelectEvent event) {
		this.producto.setIkArticulo(new UISelectEntity((Entity)event.getObject()));
    this.doAdd();
	}	

  public void doRemove(Producto producto) {  
    try {
      this.contador.remove(producto);
      this.contador.setArticulos(new Long(this.contador.getProductos().size()));
		} // try
		catch (Exception e) {			
      Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
  }
  
  public void doRecover(Producto producto) {  
    try {
      this.contador.recover(producto);
		} // try
		catch (Exception e) {			
      Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
  }

	public List<UISelectEntity> doCompleteArticulo(String query) {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
		int buscarCodigoPor       = 2;
    try {
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", this.contador.getIdAlmacen());
  		params.put("sucursales", this.contador.getIdEmpresa());
  		params.put("idProveedor", -1L);
			if(!Cadena.isVacio(query)) {
				if((Boolean)this.attrs.get("buscaPorCodigo"))
			    buscarCodigoPor= 1;
				if(query.startsWith("."))
					buscarCodigoPor= 1;
				else 
					if(query.startsWith(":"))
						buscarCodigoPor= 0;
				if(query.startsWith(".") || query.startsWith(":"))
					query= query.trim().substring(1);				
				query= query.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*");
        if(Cadena.isVacio(query))
          query= ".*";        
			} // if	
			else
				query= "WXYZ";
  		params.put("codigo", query);	
			switch(buscarCodigoPor) {      
				case 0: 
					this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigoIgual", params, columns, 20L));
					break;
				case 1: 
					this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
					break;
				case 2:
          this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
          break;
			} // switch
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	

  public void doAddItem(SelectEvent event) {
    try {      
      this.producto.setIkArticulo((UISelectEntity)event.getObject());
      if((Boolean)this.attrs.get("automatico"))
        this.doAdd();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }  

  public void doUpdateArticulo(Integer index) {
    this.doAdd();
	}
  
	public void doUpdateDialogArticulos(String codigo) {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = Boolean.FALSE;
    try {
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("original", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
  		params.put("idProveedor", new UISelectEntity(new Entity(-1L)));
			if(!Cadena.isVacio(codigo)) {
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
			} // if	
			else
				codigo= "WXYZ";
			params.put("codigo", codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*"));
			if(buscaPorCodigo)
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porCodigo", params, columns));
			else
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porNombre", params, columns));
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

	public void doChangeBuscado() {
		if(this.attrs.get("encontrado")== null) {
			FormatCustomLazy list= (FormatCustomLazy)this.attrs.get("lazyModel");
			if(list!= null) {
				List<Entity> items= (List<Entity>)list.getWrappedData();
				if(items.size()> 0) 
					this.attrs.put("encontrado", new UISelectEntity(items.get(0)));
			} // if
		} // if
	}
  
}
