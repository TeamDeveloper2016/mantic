package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
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
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Transferencia;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosAlmacenesTransferenciasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-3509185709407306573L;
  
  private StreamedContent image;
  private EAccion accion;
  private Transferencia transferencia;
  private Articulo detalle;
  
  public StreamedContent getImage() {
		return image;
	}
  
	public TcManticTransferenciasDto getTransferencia() {
		return transferencia;
	}

	public Articulo getDetalle() {
		return detalle;
	}

	public void setDetalle(Articulo detalle) {
		this.detalle=detalle;
	}

	@PostConstruct
	@Override
	protected void init() {
		try {
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.ACTIVAR: (EAccion)JsfBase.getFlashAttribute("accion");
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("idTransferencia", JsfBase.getFlashAttribute("idTransferencia"));
      this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("sugerido", 0D);
      this.doLoad();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
  
	private void toInitTransferencia() {
		this.accion= EAccion.ACTIVAR;
    this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
		Transferencia backup= this.transferencia;
		this.transferencia= new Transferencia(
			null, // Long idSolicito, 
			8L, // Long idTransferenciaEstatus, 
			1L, // Long idTransferenciaTipo, 
			new Long(Calendar.getInstance().get(Calendar.YEAR)), // Long ejercicio, 
			Calendar.getInstance().get(Calendar.YEAR)+ "00000", // String consecutivo, 
			JsfBase.getIdUsuario(), // Long idUsuario, 
			-1L, // Long idAlmacen, 
			"", // String observaciones, 
			-1L, // Long idDestino, 
			JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende(), // Long idEmpresa, 
			1L, // Long orden, 
			-1L // Long idTransferencia					
		);
		if(backup!= null) {
		  this.transferencia.setIdAlmacen(backup.getIdAlmacen());
		  this.transferencia.setIkAlmacen(backup.getIkAlmacen());
		  this.transferencia.setIdDestino(backup.getIdDestino());
		  this.transferencia.setIkDestino(backup.getIkDestino());
		} // if
		this.detalle= new Articulo(-1L);
		this.detalle.setCalculado(1D);
  	this.attrs.put("sugerido", 0D);
		this.attrs.put("nuevaExistenciaOrigen", 0D);
		this.attrs.put("nuevaExistenciaDestino", 0D);
		this.attrs.put("nuevaExistenciaOrigen", 0D);
		this.attrs.put("nuevaExistenciaDestino", 0D);
	}
	
  public void doLoad() {
    Map<String, Object> params= null;
    try {
 			params = new HashMap<>();
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case ACTIVAR:
				 	this.toInitTransferencia();
          break;
        case MODIFICAR:
        case CONSULTAR:
          this.transferencia= (Transferencia) DaoFactory.getInstance().toEntity(Transferencia.class, "TcManticTransferenciasDto", "detalle", this.attrs);
					if(this.transferencia!= null) {
						this.detalle    = (Articulo)DaoFactory.getInstance().toEntity(Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", this.attrs);
  					if(this.detalle!= null) {
							this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
							this.attrs.put("idArticulo", this.detalle.getIdArticulo());
							UISelectEntity articulo= this.detalle.toUISelectEntity();
							this.attrs.put("origen", articulo);
							this.image= LoadImages.getImage(this.detalle.getIdArticulo());
							params.put("idArticulo", this.detalle.getIdArticulo());
							params.put("idAlmacen", this.transferencia.getIdAlmacen());
							// recuperar el stock de articulos en el almacen origen
							Value origen= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
							articulo.put("stock", origen== null? new Value("stock", 0D): origen);
							articulo.put("vacio", new Value("vacio", origen== null));
							Entity umbral= (Entity)DaoFactory.getInstance().toEntity("TcManticAlmacenesArticulosDto", "almacenArticulo", params);
							if(umbral== null) 
								umbral= (Entity)DaoFactory.getInstance().toEntity("TcManticArticulosDto", "detalle", params);
							articulo.put("minimo", umbral.get("minimo"));
							articulo.put("maximo", umbral.get("maximo"));
							this.attrs.put("nuevaExistenciaOrigen", 0D);
							this.attrs.put("nuevaExistenciaDestino", 0D);
						} // if
						else
							this.toInitTransferencia();
					} // if
					else
  				 	this.toInitTransferencia();
          break;
      } // switch    
      this.loadAlmacenes();
      this.loadPersonas(); 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		finally {
      Methods.clean(params);
    } // finally
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
      almacenes = (List<UISelectEntity>) UIEntity.seleccione("TcManticAlmacenesDto", "almacenes", params, columns, "clave");
      this.attrs.put("almacenes", almacenes);
			if(almacenes!= null) {
				if(this.accion.equals(EAccion.ACTIVAR))
		      this.transferencia.setIkAlmacen(almacenes.get(0));
				else {
					int index= almacenes.indexOf(new UISelectEntity(this.transferencia.getIdAlmacen()));
					if(index>= 0)
		        this.transferencia.setIkAlmacen(almacenes.get(index));
					else
  		      this.transferencia.setIkAlmacen(almacenes.get(0));
				} // if
        this.doUpdateAlmacenOrigen();
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
			if(personas!= null && this.transferencia.getIdSolicito()!= null && this.transferencia.getIdSolicito()> 0L && !this.accion.equals(EAccion.ACTIVAR)) 
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
		try {
			UISelectEntity articulo= (UISelectEntity)this.attrs.get("origen");
			Entity destino         = (Entity)this.attrs.get("destino");
			Double stock   = articulo!= null && articulo.containsKey("stock")? articulo.toDouble("stock"): 0D;
			Double calculo = 0D;
			Double maximo  = 0D;
			Double sugerido= 0D;
			if(destino== null) {
				TcManticArticulosDto item= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.detalle.getIdArticulo());
				calculo= 0D;
				maximo = item== null? 0D: item.getMaximo();
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
				case 8:
					sugerido= maximo- calculo< 0? 0D: maximo- calculo;
					this.attrs.put("sugerido", sugerido);
					if(stock> sugerido && sugerido> 0D)
						this.detalle.setCantidad(sugerido);
					else
						if(stock< sugerido && stock> 0)
							this.detalle.setCantidad(stock);
					this.attrs.put("nuevaExistenciaOrigen", stock- this.detalle.getCantidad());
					this.attrs.put("nuevaExistenciaDestino", calculo+ this.detalle.getCantidad());
					break;
				case 3:
					this.attrs.put("nuevaExistenciaOrigen", stock);
					this.attrs.put("nuevaExistenciaDestino", calculo);
					break;
			} // switch
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
  } 
  
  public void doUpdateAlmacenOrigen() {
		if(this.attrs.get("almacenes")!= null) {
			List<UISelectEntity> destinos=  (List<UISelectEntity>)((ArrayList<UISelectEntity>)this.attrs.get("almacenes")).clone();
			int index = destinos.indexOf(this.transferencia.getIkAlmacen());
			if(index>= 0) {
				UISelectEntity origen= destinos.get(index);
				destinos.remove(index);
				// eliminar todos los almacenes que no corresponda con la idEmpresa
				int count= 0;
				while(count< destinos.size()) {
          if(Objects.equals(destinos.get(count).toLong("idEmpresa"), origen.toLong("idEmpresa")))
						count++;
					else
						destinos.remove(count);
				} // while
			} // if
			if(!destinos.isEmpty()) 
				if(this.accion.equals(EAccion.ACTIVAR))
  				this.transferencia.setIkDestino(destinos.get(0));
				else {
					index= destinos.indexOf(new UISelectEntity(this.transferencia.getIdDestino()));
					if(index>= 0)
		        this.transferencia.setIkDestino(destinos.get(index));
					else
		        this.transferencia.setIkDestino(destinos.get(0));
				} // if
			this.attrs.put("destinos", destinos);
			this.doUpdateAlmacenDestino();
		} // if	
  }
  
  public void doUpdateAlmacenDestino() {
		try {
      this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("idDestino", this.transferencia.getIdDestino());
      this.attrs.put("destino", (Entity) DaoFactory.getInstance().toEntity("VistaAlmacenesTransferenciasDto", "articulo", this.attrs));
			this.doCalculate();
    } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
  }
  
  public void doUpdateArticulos() {
		List<Columna> columns         = null;
    Map<String, Object> params    = new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo        = false;
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
  		params.put("idAlmacen", this.transferencia.getIdAlmacen());
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
		Map<String, Object> params    = new HashMap<>();
		try {
			articulos= (List<UISelectEntity>) this.attrs.get("articulos");
			int index= articulos.indexOf((UISelectEntity)event.getObject());
			if(index>= 0) {
			  seleccion= articulos.get(index);
				this.attrs.put("origen", seleccion);
				this.attrs.put("idArticulo", seleccion.get("idArticulo"));
				this.detalle.setIdArticulo(seleccion.toLong("idArticulo"));
				this.detalle.setPropio(seleccion.toString("propio"));
				this.detalle.setNombre(seleccion.toString("nombre"));
				this.image= LoadImages.getImage(seleccion.toLong("idArticulo"));
				params.put("idArticulo", seleccion.toLong("idArticulo"));
				params.put("idAlmacen", this.transferencia.getIdAlmacen());
				// recuperar el stock de articulos en el almacen origen
				Value origen= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				seleccion.put("stock", origen== null? new Value("stock", 0D): origen);
				seleccion.put("vacio", new Value("vacio", origen== null));
				Entity umbral= (Entity)DaoFactory.getInstance().toEntity("TcManticAlmacenesArticulosDto", "almacenArticulo", params);
				if(umbral== null) 
					umbral= (Entity)DaoFactory.getInstance().toEntity("TcManticArticulosDto", "detalle", params);
				seleccion.put("minimo", umbral.get("minimo"));
				seleccion.put("maximo", umbral.get("maximo"));
				this.attrs.put("nuevaExistenciaOrigen", 0D);
				this.attrs.put("nuevaExistenciaDestino", 0D);
				this.doUpdateAlmacenDestino();
			} // if	
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
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
		Transaccion transaccion = null;
    String regresar         = null;
		List<Articulo> articulos= new ArrayList<>();
		try {
			articulos.add(this.detalle);
			transaccion= new Transaccion(this.transferencia, articulos);
			if(transaccion.ejecutar(this.accion)) {
 			  UIBackingUtilities.execute("janal.back(' gener\\u00F3 la transferencia ', '"+ this.transferencia.getConsecutivo()+ "');");
        // regresar = "filtro".concat(Constantes.REDIRECIONAR);
				this.doLoad();
				JsfBase.addMessage("Se registró la transferencia de correcta", ETipoMensaje.INFORMACION);
      } // if
			else
				JsfBase.addMessage("Ocurrió un error al registrar la transferencia", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		finally {
			Methods.clean(articulos);
		} // finally
		return regresar;
	} // doAccion
  
  public String doCancelar() {
		return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
	} 

	public String getNoTieneConteoOrigen() {
		String color= "janal-color-orange";
		UISelectEntity articulo= (UISelectEntity)this.attrs.get("origen");
		if(articulo!= null && !articulo.containsKey("vacio"))
			articulo.put("vacio", new Value("vacio", true));
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(articulo== null || !articulo.toBoolean("vacio")? "none": "").concat("' title='El articulo no tiene un conteo en el almacen origen !'></i>");
	}

	public String getNoTieneConteoDestino() {
		String color= "janal-color-blue";
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(this.attrs.get("origen")!= null && this.attrs.get("destino")== null? "": "none").concat("' title='El articulo no tiene un conteo en el almacen destino !'></i>");
	}

	public void doRecoverArticulo(Integer index) {
		try {
			if(this.attrs.get("origen")!= null) {
				this.attrs.put("seleccionado", this.attrs.get("origen"));
				Object backing= JsfBase.ELAsObject("manticCatalogosArticulosExpress");
				if(backing!= null)
					((IBaseAttribute)backing).getAttrs().put("seleccionado", this.attrs.get("seleccionado"));
			} // if	
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch				
	} // doRecoveryArticulo

}
