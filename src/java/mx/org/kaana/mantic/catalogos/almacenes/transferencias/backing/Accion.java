package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Transferencia;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosAlmacenesTransferenciasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-3509185709407306573L;
  
  private StreamedContent image;
  private EAccion accion;
  private Transferencia transferencia;
  
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
			this.attrs.put("sugerido", 0D);
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
				 	this.transferencia= new Transferencia(
            null, // Long idSolicito, 
						5L, // Long idTransferenciaEstatus, 
						1L, // Long idTransferenciaTipo, 
						new Long(Calendar.getInstance().get(Calendar.YEAR)), // Long ejercicio, 
						Calendar.getInstance().get(Calendar.YEAR)+ "00000", // String consecutivo, 
						JsfBase.getIdUsuario(), // Long idUsuario, 
						-1L, // Long idAlmacen, 
						"", // String observaciones, 
						-1L, // Long idDestino, 
						JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende(), // Long idEmpresa, 
						0D, // Double cantidad, 
						1L, // Long orden, 
						-1L, // Long idArticulo, 
						-1L // Long idTransferencia					
					);
          break;
        case MODIFICAR:
        case CONSULTAR:
          this.transferencia= (Transferencia) DaoFactory.getInstance().toEntity(Transferencia.class, "TcManticTransferenciasDto", "detalle", this.attrs);
          this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
          this.attrs.put("idArticulo", this.transferencia.getIdArticulo());
          this.attrs.put("idAlmacen", this.transferencia.getIdAlmacen());
					List<UISelectEntity> articulos= (List<UISelectEntity>)UIEntity.build("VistaAlmacenesTransferenciasDto", "porNombre", this.attrs);
					if(articulos!= null && !articulos.isEmpty()) {
					  this.attrs.put("articulo", articulos.get(0));
            this.image= LoadImages.getImage(((UISelectEntity)this.attrs.get("articulo")).toString("archivo"));
					} // if	
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
      this.attrs.put("almacenes", almacenes);
			if(almacenes!= null && !this.accion.equals(EAccion.AGREGAR)) {
				this.transferencia.setIkAlmacen(almacenes.get(almacenes.indexOf(new UISelectEntity(this.transferencia.getIdAlmacen()))));
				this.transferencia.setIkDestino(almacenes.get(almacenes.indexOf(new UISelectEntity(this.transferencia.getIdDestino()))));
        this.doUpdateAlmacenOrigen();
			}	// if
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
				this.transferencia.setIkSolicito(personas.get(personas.indexOf(new UISelectEntity(this.transferencia.getIdSolicito()))));
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
    Entity destino = null;
    UISelectEntity articulo= null;
		try {
      this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("idDestino", this.transferencia.getIdDestino());
      destino = (Entity) DaoFactory.getInstance().toEntity("VistaAlmacenesTransferenciasDto", "articulo", this.attrs);
			articulo= (UISelectEntity)this.attrs.get("articulo");
      this.attrs.put("destino", destino);
			Double stock  = articulo!= null? articulo.toDouble("stock"): 0D;
			Double calculo = 0D;
			Double maximo  = 0D;
			Double sugerido= 0D;
			if(destino== null) {
				TcManticArticulosDto item= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.transferencia.getIdArticulo());
   			calculo= 0D;
			  maximo = item.getMaximo();
			} // if
			else {
   			calculo= destino.toDouble("stock");
			  maximo = destino.toDouble("maximo");
			} // else
			switch(this.transferencia.getIdTransferenciaEstatus().intValue()) {
				case 1:
				case 2:
				case 4:
				case 5:
					this.attrs.put("nuevaExistenciaOrigen", stock- this.transferencia.getCantidad());
					this.attrs.put("nuevaExistenciaDestino", calculo+ this.transferencia.getCantidad());
					sugerido= maximo- calculo< 0? 0D: maximo- calculo;
					this.attrs.put("sugerido", sugerido);
					if(this.transferencia.getCantidad()== 0D)
						this.transferencia.setCantidad(sugerido);
					break;
				case 3:
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
  public void doAsignaArticulo(SelectEvent event) {
		UISelectEntity seleccion      = null;
		List<UISelectEntity> articulos= null;
		try {
			articulos= (List<UISelectEntity>) this.attrs.get("articulos");
			seleccion= articulos.get(articulos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("articulo", seleccion);
      this.attrs.put("idArticulo", seleccion.get("idArticulo"));
			this.transferencia.setIdArticulo(seleccion.toLong("idArticulo"));
      this.image= LoadImages.getImage(seleccion.toString("archivo"));
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
 			  RequestContext.getCurrentInstance().execute("janal.back(' gener\\u00F3 la transferencia ', '"+ this.transferencia.getConsecutivo()+ "');");
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
