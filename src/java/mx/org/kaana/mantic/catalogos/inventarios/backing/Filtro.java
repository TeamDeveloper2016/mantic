package mx.org.kaana.mantic.catalogos.inventarios.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.inventarios.beans.ArticuloInventario;
import mx.org.kaana.mantic.catalogos.inventarios.reglas.ContadoresListas;
import mx.org.kaana.mantic.catalogos.inventarios.reglas.Transaccion;


@Named(value = "manticCatalogosInventariosFiltro")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {
	
	private static final long serialVersionUID = 5570593377763068163L;	
	private List<ArticuloInventario> inventarios;
	private ArticuloInventario inventario;
	private UISelectEntity encontrado;  
		
	public UISelectEntity getEncontrado() {
		return encontrado;
	}

	public void setEncontrado(UISelectEntity encontrado) {
		this.encontrado=encontrado;
	}	

	public List<ArticuloInventario> getInventarios() {
		return inventarios;
	}

	public void setInventarios(List<ArticuloInventario> inventarios) {
		this.inventarios = inventarios;
	}	

	public ArticuloInventario getInventario() {
		return inventario;
	}

	public void setInventario(ArticuloInventario inventario) {
		this.inventario = inventario;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
			this.attrs.put("contador", 1L);
			loadAlmacenes();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idAlmacen", this.attrs.get("almacen"));						
			params.put("idArticulo", this.attrs.get("articulo"));			      
			this.inventarios= DaoFactory.getInstance().toEntitySet(ArticuloInventario.class, "TcManticInventariosDto", "inventario", params);
			this.attrs.put("vigente", !this.inventarios.isEmpty() ? this.inventarios.get(0) : new ArticuloInventario());
      UIBackingUtilities.resetDataTable();		
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally		
  } // doLoad	
	
	private void loadAlmacenes(){
		List<UISelectEntity> almacenes= null;
		Map<String, Object> params    = null;
		List<Columna> columns         = null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			columns= new ArrayList<>();
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));							
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));							
			columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));							
			almacenes= UIEntity.build("VistaAlmacenesDto", "almacenesEmpresa", params, columns, Constantes.SQL_TODOS_REGISTROS);      
			this.attrs.put("almacenes", almacenes);
			this.attrs.put("almacen", UIBackingUtilities.toFirstKeySelectEntity(almacenes));			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadAlmacenes
	
	public void doArticulos() {
		List<UISelectEntity> articulos= null;
    Map<String, Object> params   = null;
		List<Columna> columns        = null;
    try {
			columns= new ArrayList<>();
			if(this.attrs.get("busqueda")!= null && this.attrs.get("busqueda").toString().length()> 2) {
				params = new HashMap<>();      
				params.put("nombre", this.attrs.get("busqueda"));
				params.put("codigo", this.attrs.get("busqueda"));
				params.put("idEmpresa", this.attrs.get("idEmpresa"));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));							
				articulos = UIEntity.build("VistaArticulosDto", "inventario", params, columns, Constantes.SQL_TODOS_REGISTROS);      
				this.attrs.put("articulos", articulos);      
				this.attrs.put("resultados", articulos.size());      
			} // if
			else 
				JsfBase.addMessage("Cliente", "Favor de teclear por lo menos 3 caracteres.", ETipoMensaje.ALERTA);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	} // doArticulos	
	
	public void doSeleccionado() {
		List<UISelectEntity> listado= null;
		List<UISelectEntity> unico  = null;
		UISelectEntity articulo     = null;
		try {
			listado= (List<UISelectEntity>) this.attrs.get("articulos");
			articulo= listado.get(listado.indexOf(this.encontrado));
			this.attrs.put("articulo", articulo);						
			unico  = new ArrayList<>();
			unico.add(articulo);
			this.attrs.put("unico", unico);							
			doLoad();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doSeleccionado						
	
	public void doAgregarInventario(){
		ArticuloInventario clienteTipoContacto= null;
		ContadoresListas contadores           = null;
		Long contador                         = 0L;
		try {					
			contador= (Long) this.attrs.get("contador");
			contadores= new ContadoresListas();
			clienteTipoContacto= new ArticuloInventario(contadores.getTotalInventariosArticulos() + contador, ESql.INSERT, true);							
			clienteTipoContacto.setIdAlmacen(((UISelectEntity)this.attrs.get("almacen")).getKey());
			clienteTipoContacto.setIdArticulo(((UISelectEntity)this.attrs.get("articulo")).getKey());
			this.inventarios.add(clienteTipoContacto);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
		finally{
			contador++;
			this.attrs.put("contador", contador);
		} // finally
	} // doAgregarClienteTipoContacto
	
	public void doEliminarInventario(){
		try {			
			if(this.inventarios.remove(this.inventario)){
				if(!this.inventario.getNuevo())
					addDeleteList(this.inventario);
				JsfBase.addMessage("Se eliminó correctamente el tipo de contacto", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el tipo de contacto", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarClienteTipoContacto
	
	private void addDeleteList(IBaseDto dto) throws Exception{
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(dto);
			transaccion.ejecutar(EAccion.DEPURAR);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // addDeleteList
	
	public void doAceptar(){
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(this.inventarios);
			if(transaccion.ejecutar(EAccion.AGREGAR)){
				JsfBase.addMessage("Inventarios", "Se agrego de forma correcta el inventario", ETipoMensaje.INFORMACION);
				doSeleccionado();
			} // if
			else
				JsfBase.addMessage("Inventarios", "Ocurrió un error al agregar el inventario", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAceptar
}