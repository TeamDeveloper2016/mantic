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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Traspases;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosAlmacenesTransferenciasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-3509185709407306573L;
  
  private StreamedContent image;
  private EAccion accion;
  private Traspases transferencia;
  
  public StreamedContent getImage() {
		return image;
	}
  
	public TcManticTransferenciasDto getTransferencia() {
		return transferencia;
	}

	@PostConstruct
	@Override
	protected void init() {
		try {
      this.accion= (EAccion)JsfBase.getFlashAttribute("accion");
			if(this.accion== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.attrs.put("idTransferencia", JsfBase.getFlashAttribute("idTransferencia"));
      this.attrs.put("buscaPorCodigo", false);
      doLoad();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
  
  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:
				 	this.transferencia= new Traspases(
						-1L, // Long idTransferencia, 
						-1L, // Long idAlmacen, 
						-1L, // Long idDestino, 
						-1L, // Long idArticulo, 
						-1L, // Long idSolicito, 
						JsfBase.getIdUsuario(), // Long idUsuario, 
						1L, // Long idTransferenciaEstatus, 
						0D, // Double cantidad, 
						"" // String observaciones
					);
          break;
        case MODIFICAR:
        case CONSULTAR:
          this.transferencia = (Traspases) DaoFactory.getInstance().findById(Traspases.class, (Long)this.attrs.get("idTransferencia"));
          this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
          this.attrs.put("idArticulo", this.transferencia.getIdArticulo());
					List<UISelectEntity> articulos= (List<UISelectEntity>)UIEntity.build("VistaAlmacenesTransferenciasDto", "porNombre", this.attrs);
					this.attrs.put("articulo", articulos.get(0));
          this.image= LoadImages.getImage(((UISelectEntity)this.attrs.get("articulo")).toString("archivo"));
          break;
      } // switch      
      this.loadAlmacenes();
      this.loadPersonas(); 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad
  
  private void loadAlmacenes() {
    List<UISelectEntity> almacenes= null;
		Map<String, Object>params     = null;
		List<Columna> columns         = null;
		try {
			columns= new ArrayList<>();
			params = new HashMap<>();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      almacenes = (List<UISelectEntity>) UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns);
			if(almacenes!= null && !this.accion.equals(EAccion.AGREGAR)) {
				this.transferencia.setIkAlmacen(almacenes.get(almacenes.indexOf(new UISelectEntity(this.transferencia.getIdAlmacen()))));
				this.transferencia.setIkDestino(almacenes.get(almacenes.indexOf(new UISelectEntity(this.transferencia.getIdDestino()))));
        this.attrs.put("destinos", ((ArrayList<UISelectEntity>)almacenes).clone());
			}	// if
      this.attrs.put("almacenes", almacenes);
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
  
  private void loadPersonas() {
    List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
      columns= new ArrayList<>();
      params = new HashMap<>();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> personas= UIEntity.build("VistaAlmacenesTransferenciasDto", "solicito", params, columns);
      this.attrs.put("personas", personas);
			if(personas!= null && !this.accion.equals(EAccion.AGREGAR)) 
				this.transferencia.setIkAlmacen(personas.get(personas.indexOf(new UISelectEntity(this.transferencia.getIdSolicito()))));
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally 
  }
  
  public void doCalculate() {
		Double stock= this.attrs.get("articulo")!= null? ((UISelectEntity)this.attrs.get("articulo")).toDouble("stock"): 0D;
    this.attrs.put("nuevaExistenciaOrigen", !this.transferencia.getIdTransferenciaEstatus().equals(3L) || !this.transferencia.getIdTransferenciaEstatus().equals(5L)? stock- this.transferencia.getCantidad(): stock);
  } // doCalculate
  
  public void doUpdateAlmacenOrigen() {
    this.attrs.put("destinos", ((ArrayList<UISelectEntity>)this.attrs.get("almacenes")).clone());
		int index = ((List<UISelectEntity>)this.attrs.get("destinos")).indexOf(this.transferencia.getIkAlmacen());
		if(index>= 0)
      ((List<UISelectEntity>)this.attrs.get("destinos")).remove(index);
		this.doUpdateAlmacenDestino();
  }
  
  public void doUpdateAlmacenDestino() {
    Entity destino= null;
		try {
      this.attrs.put("idDestino", this.transferencia.getIdDestino());
      destino = (Entity) DaoFactory.getInstance().toEntity("VistaAlmacenesTransferenciasDto", "articulo", this.attrs);
      this.attrs.put("destino", destino);
			Double stock   = this.attrs.get("articulo")!= null? ((UISelectEntity)this.attrs.get("articulo")).toDouble("stock"): 0D;
			Double calculo = destino!= null? destino.toDouble("stock"): 0D;
			switch(this.transferencia.getIdTransferenciaEstatus().intValue()) {
				case 1:
				case 2:
				case 4:
					this.attrs.put("nuevaExistenciaOrigen", stock- this.transferencia.getCantidad());
					this.attrs.put("nuevaExistenciaDestino", calculo+ this.transferencia.getCantidad());
					break;
				case 3:
				case 5:
					this.attrs.put("nuevaExistenciaOrigen", stock);
					this.attrs.put("nuevaExistenciaDestino", calculo);
					break;
			} // switch
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
  		params.put("idAlmacen", this.transferencia.getIdAlmacen());
  		params.put("idProveedor", -1L);
			String search= this.attrs.get("codigo")== null? "WXYZ": (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "";
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
      this.attrs.put("idArticulo", seleccion.get("idArticulo"));
      this.image= LoadImages.getImage(seleccion.toString("archivo"));
      this.attrs.put("cantidad", 0D);
      this.attrs.put("nuevaExistenciaOrigen", 0D);
      this.attrs.put("nuevaExistenciaDestino", 0D);
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

	public String doAceptar() {
		Transaccion transaccion= null;
    String regresar        = null;
		try {
			transaccion= new Transaccion(this.transferencia);
			if(transaccion.ejecutar(this.accion)) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se registró la transferencia de correcta", ETipoMensaje.INFORMACION);
      } // if
			else
				JsfBase.addMessage("Ocurrió un error al registrar la transferencia", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
	} // doAccion
  
  public String doCancelar() {
		return "filtro";
	} 

}
