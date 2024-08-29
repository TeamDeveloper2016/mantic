package mx.org.kaana.mantic.catalogos.almacenes.multiples.backing;

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
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.multiples.beans.Multiple;
import mx.org.kaana.mantic.catalogos.almacenes.multiples.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.almacenes.multiples.reglas.AdminMultiples;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.comun.IBaseStorage;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasMultiplesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.TabChangeEvent;


@Named(value= "manticCatalogosAlmacenesMultiplesAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements IBaseStorage, Serializable {

	private static final Log LOG              = LogFactory.getLog(Accion.class);
  private static final long serialVersionUID= 321393188565639367L;
	private EAccion accion;
	
	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			this.attrs.put("xcodigo", JsfBase.getFlashAttribute("xcodigo"));	
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idTransferenciaMultiple", JsfBase.getFlashAttribute("idTransferenciaMultiple")== null? -1L: JsfBase.getFlashAttribute("idTransferenciaMultiple"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
      this.attrs.put("cantidad", 0D);
      this.attrs.put("caja", 1L);
			this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("seleccionado", null);
			if(this.accion!= EAccion.AGREGAR && (Long)this.attrs.get("idTransferenciaMultiple")<= 0) 
				this.accion= EAccion.AGREGAR;
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminMultiples(new Multiple(-1L)));
    			this.attrs.put("sinIva", false);
          break;
        case MODIFICAR:			
        case CONSULTAR:											
          this.setAdminOrden(new AdminMultiples((Multiple)DaoFactory.getInstance().toEntity(Multiple.class, "TcManticTransferenciasMultiplesDto", "detalle", this.attrs)));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
          break;
      } // switch
			this.attrs.put("paginator", this.getAdminOrden().getArticulos().size()> Constantes.REGISTROS_POR_LOTE);
			this.doResetDataTable();
			this.toLoadCatalog();
			this.attrs.put("before", this.getAdminOrden().getIdAlmacen());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			transaccion = new Transaccion((TcManticTransferenciasMultiplesDto)this.getAdminOrden().getOrden(), this.getAdminOrden().getArticulos());
			this.getAdminOrden().toAdjustArticulosAlmacen();
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
   			  UIBackingUtilities.execute("janal.back(' gener\\u00F3 la transferencia multiple', '"+ ((Multiple)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
		  		JsfBase.addMessage("Se registró la transferencia multiple de forma correcta", ETipoMensaje.INFORMACION);
 				  regresar = ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
    			JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la transferencia multiple de articulos"), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idTransferenciaMultiple", ((Multiple)this.getAdminOrden().getOrden()).getIdTransferenciaMultiple());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la transferencia de articulos", ETipoMensaje.ALERTA);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idTransferenciaMultiple", ((Multiple)this.getAdminOrden().getOrden()).getIdTransferenciaMultiple());
		JsfBase.setFlashAttribute("xcodigo", this.attrs.get("xcodigo"));	
    return (String)this.attrs.get("retorno");
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
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
  				((Multiple)this.getAdminOrden().getOrden()).setIkEmpresa(empresas.get(0));
			  else
				  ((Multiple)this.getAdminOrden().getOrden()).setIkEmpresa(empresas.get(empresas.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkEmpresa())));
				this.attrs.put("idPedidoSucursal", ((Multiple)this.getAdminOrden().getOrden()).getIkEmpresa());
			} // if	
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) {
				List<UISelectEntity> destinos= (List<UISelectEntity>)((ArrayList<UISelectEntity>)almacenes).clone();
				if(this.accion.equals(EAccion.AGREGAR))
				  ((Multiple)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
				else 
				  ((Multiple)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(almacenes.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkAlmacen())));
        this.attrs.put("destinos", destinos);
  			int index = destinos.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkAlmacen());
  			if(index>= 0)
	  			destinos.remove(index);
				if(this.accion.equals(EAccion.AGREGAR))
				  ((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(0));
				else {
          index= destinos.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkDestino());
          if(index>= 0)
				    ((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(index));
          else
				    ((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(0));
        } // else  
			} // if
			columns.clear();
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> personas= UIEntity.build("VistaAlmacenesTransferenciasDto", "solicito", params, columns);
      this.attrs.put("personas", personas);
			if(personas!= null && !this.accion.equals(EAccion.AGREGAR) && ((Multiple)this.getAdminOrden().getOrden()).getIdSolicito()!= null && ((Multiple)this.getAdminOrden().getOrden()).getIdSolicito()> 0L){
        int index= personas.indexOf(new UISelectEntity(((Multiple)this.getAdminOrden().getOrden()).getIdSolicito()));
        if(index>= 0)
				  ((Multiple)this.getAdminOrden().getOrden()).setIkSolicito(personas.get(index));
        else
          ((Multiple)this.getAdminOrden().getOrden()).setIkSolicito(personas.get(0));
      } // if  
			this.doUpdateAlmacenDestino(true);
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

	public void doUpdateAlmacen() {
    try {
      List<UISelectEntity> destinos= (List<UISelectEntity>)((ArrayList<UISelectEntity>)this.attrs.get("almacenes")).clone();
      this.attrs.put("destinos", destinos);
      int index = destinos.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkAlmacen());
      if(index>= 0)
        destinos.remove(index);
      if(!destinos.isEmpty())
        ((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(0));
      this.getAdminOrden().getArticulos().clear();
      this.getAdminOrden().getArticulos().add(new Articulo(-1L));
      this.getAdminOrden().toCalculate();
      this.attrs.put("paginator", this.getAdminOrden().getArticulos().size()> Constantes.REGISTROS_POR_LOTE);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
	public void doUpdateAlmacenDestino() {
		this.doUpdateAlmacenDestino(false);
	}
	
	public void doUpdateAlmacenDestino(boolean recuperar) {
		Map<String, Object> params= new HashMap<>();
		Value origen              = null;
		try {
  		List<UISelectEntity> destinos= (List<UISelectEntity>)((ArrayList<UISelectEntity>)this.attrs.get("destinos"));
			int index= destinos.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkDestino());
			if(index>= 0)
				((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(index));
			for (Articulo articulo: this.getAdminOrden().getArticulos()) {
				params.put("idArticulo", articulo.getIdArticulo());
				if(articulo.getIdArticulo()> 0L) {
					if(recuperar) {
					  params.put("idAlmacen", this.getAdminOrden().getIdAlmacen());
					  // recuperar el stock de articulos en el almacen origen
					  origen= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
					  articulo.setStock(origen== null || origen.toDouble()< 0D? 0D: origen.toDouble());
					  // el almacen origen no tiene conteo 
					  articulo.setSolicitado(origen== null);
					} // if	
          params.put("idAlmacen", articulo.getIdAlmacen());
					// recuperar el stock de articulos en el almacen destino
					origen= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
					articulo.setValor(origen== null || origen.toDouble()< 0D? 0D: origen.toDouble());
					// el almacen destino no tiene conteo
					articulo.setCostoLibre(origen== null);
					origen= (Value)DaoFactory.getInstance().toField("TcManticAlmacenesArticulosDto", "umbral", params, "maximo");
					// recuperar el maximo del catalogo de articulos
					if(origen== null) {
						TcManticArticulosDto item= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
						articulo.setCosto(item.getMaximo());
					} // if
					else	
						articulo.setCosto(origen.toDouble());
					// el stock del almacen destino es superior al maximo permitido en el almacen
					articulo.setUltimo(articulo.getValor()> articulo.getCosto());
					articulo.setModificado(true);
          
			    index= destinos.indexOf(new UISelectEntity(articulo.getIdAlmacen()));
			    if(index>= 0)
            articulo.setAlmacen(destinos.get(index).toString("nombre"));
				} // if	
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Articulos")) {
			if(this.attrs.get("articulos")== null) 
				this.toLoadArticulos("almacen");
		} // if	
		else 
			if(event.getTab().getTitle().equals("Faltantes almacen")) 
        this.doLoadFaltantes();
			else 
			  if(event.getTab().getTitle().equals("Ventas perdidas") && this.attrs.get("perdidos")== null) {
          List<UISelectEntity> destinos= (List<UISelectEntity>)((ArrayList<UISelectEntity>)this.attrs.get("almacenes"));
          int index= destinos.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkDestino());
          if(index>= 0)
            ((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(index));
          this.doLoadPerdidas(((Multiple)this.getAdminOrden().getOrden()).getIkDestino()== null? -1L: ((Multiple)this.getAdminOrden().getOrden()).getIkDestino().toLong("idEmpresa"));
				} // if	
	}

	@Override
	public void doLoadFaltantes() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
			params.put("idAlmacen", ((Multiple)this.getAdminOrden().getOrden()).getIkDestino().getKey());
			if(Cadena.isVacio(this.attrs.get("lookForFaltantes")))
			  params.put("codigoFaltante", "");
			else {
				String nombre= ((String)this.attrs.get("lookForFaltantes")).replaceAll(Constantes.CLEAN_SQL, "").trim();
				params.put("codigoFaltante", nombre.toUpperCase());
			} // else
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      this.attrs.put("faltantes", UIEntity.build("VistaAlmacenesTransferenciasDto", "faltantes", params, columns, Constantes.SQL_TODOS_REGISTROS));
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    }// finally
	}
	
	public void doLoadFaltantesAlmacenDestino() {
		this.attrs.put("lookForPerdidos", "");
    this.doLoadPerdidas(((Multiple)this.getAdminOrden().getOrden()).getIkDestino().getKey()== null? -1L: ((Multiple)this.getAdminOrden().getOrden()).getIkDestino().getKey());
	}
	
	public void toLoadArticulos(String idXml) {
		List<Articulo> articulos  = null;
    Map<String, Object> params= new HashMap<>();
		try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.putAll(((Multiple)this.getAdminOrden().getOrden()).toMap());
			articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaOrdenesComprasDto", idXml, params);
      if(articulos!= null && this.getAdminOrden().getArticulos().isEmpty())
				for (Articulo articulo : articulos) 
					this.getAdminOrden().add(articulo);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
    finally {
      Methods.clean(params);
    } // finally
	}

  @Override
	public void doRecoverArticulo(Integer index) {
		try {
			if(index>= 0 && index< this.getAdminOrden().getArticulos().size()) {
				this.attrs.put("seleccionado", this.getAdminOrden().getArticulos().get(index).toEntity());
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
	
	@Override
  protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		Articulo temporal         = this.getAdminOrden().getArticulos().get(index);
		Map<String, Object> params= new HashMap<>();
    StringBuilder sb          = new StringBuilder(); 
		try {
			if(articulo.size()> 1) {
        // RECUPERAR EL ELEMENTO DE LA POSICION DEL ID_ALMACEN_DESTINO 
     		List<UISelectEntity> destinos= (List<UISelectEntity>)this.attrs.get("destinos");
        if(destinos!= null && !destinos.isEmpty() && ((Multiple)this.getAdminOrden().getOrden()).getIkDestino().size()<= 1) {
          int position= destinos.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkDestino());
          if(position>= 0) 
            ((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(position));
          else  
            ((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(0));
        } // if  
				params.put("idArticulo", articulo.toLong("idArticulo"));
				params.put("idAlmacen", this.getAdminOrden().getIdAlmacen());
				temporal.setKey(articulo.toLong("idArticulo"));
				temporal.setIdArticulo(articulo.toLong("idArticulo"));
        temporal.setIdCompleto(articulo.toLong("idCompleto"));
				temporal.setIdProveedor(-1L);
				temporal.setCodigo(articulo.toString("propio"));
				temporal.setPropio(articulo.toString("propio"));
				temporal.setNombre(articulo.toString("nombre"));
				temporal.setPrecio(articulo.toDouble("precio"));				
				temporal.setIva(articulo.toDouble("iva"));				
				temporal.setSat("");				
				temporal.setDescuento("");
				temporal.setExtras("");				
				// recuperar el stock de articulos en el almacen origen
				Entity source= (Entity)DaoFactory.getInstance().toEntity("TcManticInventariosDto", "stock", params);
        if(Objects.equals(source, null) || Objects.equals(source.toLong("idAutomatico"), 1L) || !Objects.equals(source.toDouble("inicial"), 0D))
          sb.append("El almacen fuente no tiene un conteo, ");
				temporal.setStock(source== null || source.isEmpty()? 0D: source.toDouble("stock"));
				// el almacen origen no tiene conteo 
				temporal.setSolicitado(source== null || source.isEmpty() || Objects.equals(source.toDouble("inicial"), 0D));
				// recuperar el stock de articulos en el almacen destino
				params.put("idAlmacen", ((Multiple)this.getAdminOrden().getOrden()).getIkDestino().getKey());
				Entity target= (Entity)DaoFactory.getInstance().toEntity("TcManticInventariosDto", "stock", params);
        if(Objects.equals(target, null) || Objects.equals(target.toLong("idAutomatico"), 1L) || !Objects.equals(target.toDouble("inicial"), 0D))
          sb.append("El almacen destino no tiene un conteo  ");
				temporal.setValor(target== null|| target.isEmpty()? 0D: target.toDouble("stock"));
				// el almacen destino no tiene conteo
				temporal.setCostoLibre(target== null || target.isEmpty() || Objects.equals(target.toDouble("inicial"), 0D));
				Value origen= (Value)DaoFactory.getInstance().toField("TcManticAlmacenesArticulosDto", "umbral", params, "maximo");
				// recuperar el maximo del catalogo de articulos
				if(origen== null) {
					TcManticArticulosDto item= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.toLong("idArticulo"));
				  temporal.setCosto(item.getMaximo());
				} // if
			  else	
				  temporal.setCosto(origen.toDouble());
				// calcular el valor sugerido para la cantidad
        // stock: es el stock del almacen origen
        // valor: es el stock del almacen destino
        // costo: es el valor maximo para el articulo
        temporal.setCantidad(temporal.getCosto()- temporal.getValor()< 0D? 0D: temporal.getCosto()- temporal.getValor());
				// el stock del almacen destino es superior al maximo permitido en el almacen
				temporal.setUltimo(temporal.getValor()> temporal.getCosto());
				// agregar el paquete o caja donde se encuentra referenciado el articulo
				temporal.setCaja(this.attrs.get("caja")!= null? (Long)this.attrs.get("caja"): 1L);
				temporal.setIdAlmacen(((Multiple)this.getAdminOrden().getOrden()).getIkDestino().getKey());
				temporal.setAlmacen(((Multiple)this.getAdminOrden().getOrden()).getIkDestino().toString("nombre"));
				if(index== this.getAdminOrden().getArticulos().size()- 1) {
					this.getAdminOrden().getArticulos().add(new Articulo(-1L));
					this.getAdminOrden().toAddUltimo(this.getAdminOrden().getArticulos().size()- 1);
					UIBackingUtilities.execute("jsArticulos.update("+ (this.getAdminOrden().getArticulos().size()- 1)+ ");");
				} // if	
				UIBackingUtilities.execute("jsArticulos.erase();jsArticulos.callback('"+ articulo.getKey()+ "');");
				this.getAdminOrden().toCalculate(index);
        
        temporal.setComentarios(sb.length()== 0? null: sb.substring(0, sb.length()- 2));
			} // if	
			else
				temporal.setNombre("<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	@Override
  public void doFindArticulo(Integer index) {
		try {
    	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
      if(articulos!= null && !articulos.isEmpty()) {
        UISelectEntity articulo  = (UISelectEntity)this.attrs.get("articulo");
        UISelectEntity encontrado= (UISelectEntity)this.attrs.get("encontrado");
        if(encontrado!= null) {
          articulo= encontrado;
          this.attrs.remove("encontrado");
        } // else
        else 
          if(articulo== null)
            articulo= new UISelectEntity(new Entity(-1L));
          else
            if(articulos.indexOf(articulo)>= 0)
              articulo= articulos.get(articulos.indexOf(articulo));
        if(articulo.size()> 1) {
          int position= this.toIndexOf(this.getAdminOrden().getArticulos(), articulo);
          if(articulo.size()> 1 && position>= 0) {
            if(index!= position) {
              if(this.attrs.get("omitirMensaje")!= null)
                this.attrs.remove("omitirMensaje");
              else
                UIBackingUtilities.execute("jsArticulos.exists('"+ articulo.toString("propio")+ "', '"+ articulo.toString("nombre")+ "', "+ position+ ","+ Constantes.REGISTROS_POR_LOTE+ ","+ (this.attrs.get("paginator")== null? false: this.attrs.get("paginator"))+ ");");
            } // if	
          } // if	
          else
            this.toMoveData(articulo, index);
        } // else
        else 
            this.toMoveData(articulo, index);
        this.attrs.put("paginator", this.getAdminOrden().getArticulos().size()> Constantes.REGISTROS_POR_LOTE);
        DataTable dataTable= (DataTable)JsfUtilities.findComponent("contenedorGrupos:tabla");
        if (dataTable!= null) 
          dataTable.setRows((boolean)this.attrs.get("paginator") || this.getAdminOrden().getTotales().getArticulos()>  Constantes.REGISTROS_LOTE_TOPE? Constantes.REGISTROS_POR_LOTE: 10000);		
        int position= articulos.indexOf((UISelectEntity)this.attrs.get("articulo"));
        if(position>= 0) 
          this.attrs.put("seleccionado", articulos.get(position));
      } // if  
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

  private int toIndexOf(List<Articulo> articulos, UISelectEntity articulo) {
    int regresar= -1;
    if(articulos!= null && !articulos.isEmpty()) {
      regresar= 0;
      while (regresar< articulos.size()) {
        if(Objects.equals(articulos.get(regresar).getIdArticulo(), articulo.toLong("idArticulo")) && Objects.equals(((Multiple)this.getAdminOrden().getOrden()).getIkDestino().getKey(), articulos.get(regresar).getIdAlmacen()))
          break;
        regresar++;
      } // while 
      regresar= regresar>= articulos.size()? -1: regresar;
    } // if
    return regresar;
  }
  
	@Override
	public void toSaveRecord() {
    Transaccion transaccion= null;
    try {			
			transaccion = new Transaccion((TcManticTransferenciasMultiplesDto)this.getAdminOrden().getOrden(), this.getAdminOrden().getArticulos());
			this.getAdminOrden().toAdjustArticulos();
			if (transaccion.ejecutar(EAccion.MOVIMIENTOS)) {
   			UIBackingUtilities.execute("jsArticulos.back('guard\\u00F3 la transferencia multiple de articulos ', '"+ ((Multiple)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
				this.accion= EAccion.MODIFICAR;
				this.getAdminOrden().getArticulos().add(new Articulo(-1L));
				this.attrs.put("autoSave", Global.format(EFormatoDinamicos.FECHA_HORA, Fecha.getRegistro()));
			} // if	
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}

	public void doGlobalEvent(Boolean isViewException) {
		LOG.error("ESTO ES UN MENSAJE GLOBAL INVOCADO POR UNA EXCEPCION QUE NO FUE ATRAPADA");
		if(isViewException && this.getAdminOrden().getArticulos().size()> 0)
		  this.toSaveRecord();
    //UIBackingUtilities.execute("alert('ESTO ES UN MENSAJE GLOBAL INVOCADO POR UNA EXCEPCION QUE NO FUE ATRAPADA');");
	}
	
	public void doLoadAlmacenes() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", ((Multiple)this.getAdminOrden().getOrden()).getIdEmpresa());
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
			  ((Multiple)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
			this.doUpdateAlmacen();
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
	
	@Override
	public void doFaltanteArticulo() {
		try {
			UISelectEntity faltante= (UISelectEntity)this.attrs.get("faltante");
      this.attrs.put("articulo", faltante);
      super.doFindArticulo(this.getAdminOrden().getArticulos().size()- 1);        
      List<UISelectEntity> faltantes= (List<UISelectEntity>)this.attrs.get("faltantes");
      if(faltante!= null && faltantes!= null)
        faltantes.remove(faltantes.indexOf(faltante));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	@Override
	public void doAgregarPerdido() {
		try {
			UISelectEntity perdido= (UISelectEntity)this.attrs.get("perdido");
      this.attrs.put("articulo", perdido);
      super.doFindArticulo(this.getAdminOrden().getArticulos().size()- 1);        
 		  List<UISelectEntity> perdidos= (List<UISelectEntity>)this.attrs.get("perdidos");
      if(perdido!= null && perdidos!= null)
  		  perdidos.remove(perdidos.indexOf(perdido));		
    } // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
	public void doCleanLookForFaltantes() {
		this.attrs.put("lookForFaltantes", "");
		this.doLoadFaltantes();
	} 
	
	public void doLookForFaltantes() {
		this.doLoadFaltantes();
	} 
	
	public void doLookForPerdidos() {
    if(((Multiple)this.getAdminOrden().getOrden()).getIkDestino()!= null && ((Multiple)this.getAdminOrden().getOrden()).getIkDestino().size()< 2) {
  	  List<UISelectEntity> destinos= (List<UISelectEntity>)((ArrayList<UISelectEntity>)this.attrs.get("almacenes"));
      int index= destinos.indexOf(((Multiple)this.getAdminOrden().getOrden()).getIkDestino());
      if(index>= 0)
        ((Multiple)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(index));
    } // if  
		this.doLoadPerdidas(((Multiple)this.getAdminOrden().getOrden()).getIkDestino()== null? -1L: ((Multiple)this.getAdminOrden().getOrden()).getIkDestino().toLong("idEmpresa"));
	} 

  @Override
  public String getRecordCount() {
		return this.attrs.get("perdidos")== null || ((List<UISelectEntity>)this.attrs.get("perdidos")).isEmpty()? String.valueOf(Constantes.REGISTROS_MAX_TABLA): Constantes.REGISTROS_MAX_CADA_PAGINA+ ","+ ((List<UISelectEntity>)this.attrs.get("perdidos")).size();
	}

}