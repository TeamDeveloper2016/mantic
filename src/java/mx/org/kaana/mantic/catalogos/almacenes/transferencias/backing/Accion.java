package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.mantic.db.dto.TcManticCategoriasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.categorias.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosAlmacenesTransferenciasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {
  
  private StreamedContent image;
  
  public StreamedContent getImage() {
		return image;
	}

	@PostConstruct
	@Override
	protected void init() {
		try {
      this.attrs.put("buscaPorCodigo", false);
      this.attrs.put("cantidad", 0);
			this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));										
			toLoadAlmacenes();
			//doLoad();			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
  
  private void toLoadAlmacenes() {
    List<UISelectEntity> almacenes= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      almacenes = (List<UISelectEntity>) UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns);
      this.attrs.put("almacenesOrigen",almacenes);
      this.attrs.put("almacenesDestino", ((ArrayList<UISelectEntity>)almacenes).clone());
      //doUpdateArticulos();
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}//toLoadAlmacenes
  
  public void doCalculate() {
    this.attrs.put("nuevaExistenciaOrigen", (Numero.getInteger(((UISelectEntity)this.attrs.get("articulo")).get("stock").toString())-Numero.getInteger(this.attrs.get("cantidad").toString()))) ;
  } // doCalculate
  
  public void doUpdateAlmacenOrigen() {
    this.attrs.put("almacenesDestino", ((ArrayList<UISelectEntity>)this.attrs.get("almacenesOrigen")).clone());
		int index = ((List<UISelectEntity>)this.attrs.get("almacenesDestino")).indexOf(new UISelectEntity(new Entity(Long.valueOf(this.attrs.get("idAlmacenOrigen").toString()))));
    ((List<UISelectEntity>)this.attrs.get("almacenesDestino")).remove(index);
    doUpdateArticulos();
  }
  
  public void doUpdateAlmacenDestino() {
    Entity articuloDestino = null;
		try {
      this.attrs.put("almacenesDestino", ((ArrayList<UISelectEntity>)this.attrs.get("almacenesOrigen")).clone());
      int index = ((List<UISelectEntity>)this.attrs.get("almacenesDestino")).indexOf(new UISelectEntity(new Entity(Long.valueOf(this.attrs.get("idAlmacenOrigen").toString()))));
      ((List<UISelectEntity>)this.attrs.get("almacenesDestino")).remove(index);
      articuloDestino = (Entity) DaoFactory.getInstance().toEntity("VistaAlmacenesTransferenciasDto", "articuloAlmacen", this.attrs);
      this.attrs.put("articuloDestino", articuloDestino);
      this.attrs.put("nuevaExistenciaDestino", (Numero.getInteger(((UISelectEntity)this.attrs.get("articuloDestino")).get("stock").toString())+Numero.getInteger(this.attrs.get("cantidad").toString()))) ;
    } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
  }
  
  public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idAlmacenOrigen", this.attrs.get("idAlmacenOrigen"));
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
        articulos= (List<UISelectEntity>) UIEntity.build("VistaAlmacenesTransferenciasDto", "porCodigo", params, columns, 20L);
			else
        articulos= (List<UISelectEntity>) UIEntity.build("VistaAlmacenesTransferenciasDto", "porNombre", params, columns, 20L);
      this.attrs.put("articulos", articulos);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
  public void doAsignaArticulo(SelectEvent event){
		UISelectEntity seleccion       = null;
		List<UISelectEntity> articulos = null;
		try {
			articulos= (List<UISelectEntity>) this.attrs.get("articulos");
			seleccion= articulos.get(articulos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("articulo", seleccion);
      this.attrs.put("idArticulo",seleccion.get("idArticulo"));
      this.image= LoadImages.getImage(seleccion.toString("archivo"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
  
  public List<UISelectEntity> doCompleteArticulo(String query) {
		try {
      this.attrs.put("codigo", query);
      this.doUpdateArticulos();		
    } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
    return (List<UISelectEntity>)this.attrs.get("articulos");
	}	
         	
	public void doLoad() {
		EAccion eaccion       = null;
		MotorBusqueda busqueda= null;		
		Long idCategoriaParent= null;
		try {
			eaccion= (EAccion) this.attrs.get("accion");
			this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
			switch(eaccion){
				case AGREGAR:
					this.attrs.put("dto", new TcManticCategoriasDto());
					break;
				case MODIFICAR:
				case CONSULTAR:
					busqueda= new MotorBusqueda(Long.valueOf(this.attrs.get("idCategoria").toString()));
					this.attrs.put("dto", busqueda.toCategoria());					
					idCategoriaParent= busqueda.toParent();
					this.attrs.put("nodo", idCategoriaParent);
					this.attrs.put("parentNodo", idCategoriaParent);
					break;
			} // switch
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doLoad
	
	public String doAceptar(){
		TcManticTransferenciasDto dto= null;
		//Transaccion transaccion  = null;
		try {
			//transaccion= new Transaccion(dto);
			//if(transaccion.ejecutar((EAccion) this.attrs.get("accion")))
      if(true)
				JsfBase.addMessage("Se aplico el cambio de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Ocurrió un error al registrar el cambio", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "filtro";
	} // doAccion
  
  public String doCancelar() {
		return "filtro";
	} 

}
