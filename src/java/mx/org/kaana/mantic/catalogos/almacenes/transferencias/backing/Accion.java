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
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.usuarios.reglas.CargaInformacionUsuarios;
import mx.org.kaana.kajool.procesos.usuarios.reglas.beans.CriteriosBusqueda;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosAlmacenesTransferenciasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {
  
  private StreamedContent image;
  private CriteriosBusqueda criteriosBusqueda;
  private TcManticTransferenciasDto transferencia;
  
  public StreamedContent getImage() {
		return image;
	}
  
  public CriteriosBusqueda getCriteriosBusqueda() {
    return criteriosBusqueda;
  }

	@PostConstruct
	@Override
	protected void init() {
		try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.criteriosBusqueda = new CriteriosBusqueda();
      this.attrs.put("idTransferencia", JsfBase.getFlashAttribute("idTransferencia"));
      this.attrs.put("buscaPorCodigo", false);
      this.attrs.put("cantidad", 0);
      this.attrs.put("observaciones", "");
      this.attrs.put("idUsuario", JsfBase.getFlashAttribute("idUsuario")!= null? (Long)JsfBase.getFlashAttribute("idUsuario"): -1L);		
      doLoad();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
  
  public void doLoad() {
    EAccion eaccion= null;
    Long idTransferencia = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
         loadAlmacenes();
         loadPersonas(); 
         loadUsuario();
         break;
        case MODIFICAR:
        case CONSULTAR:
          idTransferencia = Long.valueOf(this.attrs.get("idTransferencia").toString());
          this.transferencia = (TcManticTransferenciasDto) DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, idTransferencia);
					loadAlmacenes();
          this.attrs.put("idAlmacenOrigen", (((List<UISelectEntity>)this.attrs.get("almacenesOrigen")).get(((List<UISelectEntity>)this.attrs.get("almacenesOrigen")).indexOf(new UISelectEntity(this.transferencia.getIdAlmacen().toString())))));
          doUpdateAlmacenOrigen();
          this.attrs.put("idArticulo",this.transferencia.getIdArticulo());
          this.attrs.put("idAlmacenDestino", (((List<UISelectEntity>)this.attrs.get("almacenesDestino")).get(((List<UISelectEntity>)this.attrs.get("almacenesDestino")).indexOf(new UISelectEntity(this.transferencia.getIdDestino().toString())))));
          this.attrs.put("articulo", (((List<UISelectEntity>)this.attrs.get("articulos")).get(((List<UISelectEntity>)this.attrs.get("articulos")).indexOf(new UISelectEntity(this.transferencia.getIdArticulo().toString())))));
           this.image= LoadImages.getImage(((UISelectEntity)this.attrs.get("articulo")).toString("archivo"));
          this.attrs.put("cantidad", this.transferencia.getCantidad());
          doUpdateAlmacenDestino();
          loadPersonas(); 
          this.attrs.put("idUsuario", this.transferencia.getIdSolicito());
          loadUsuario();
          this.criteriosBusqueda.setPersona(this.criteriosBusqueda.getListaPersonas().get(this.criteriosBusqueda.getListaPersonas().indexOf(new UISelectEntity(this.transferencia.getIdSolicito().toString()))));
          this.attrs.put("observaciones", this.transferencia.getObservaciones());
          break;
      } // switch      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad
  
  private void loadPersonas() {
    List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
      columns= new ArrayList<>();
      params = new HashMap<>();
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      params.put(Constantes.SQL_CONDICION, "id_tipo_persona in (1,2,6)");
      this.criteriosBusqueda.getListaPersonas().addAll(UIEntity.build("TcManticPersonasDto", "row", params, columns));
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally 
  }
  
  private void loadUsuario() {
		Long idUsuario= (Long)this.attrs.get("idUsuario")!=null?(Long)this.attrs.get("idUsuario"):0L;
    try {
      if (idUsuario> 0) {
        this.attrs.put("tcJanalUsuariosDto", (TcJanalUsuariosDto) DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, idUsuario));
        this.attrs.put("tcManticPersonasDto", (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuariosDto")).getIdPersona()));
      } // if
			else {
        this.attrs.put("tcJanalUsuariosDto", new TcJanalUsuariosDto(-1L, JsfBase.getIdUsuario(), -1L, 1L, -1L));
        this.attrs.put("tcManticPersonasDto", new TcManticPersonasDto());
      } // else
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  }
  
  public void doBuscar() {
		CargaInformacionUsuarios ciu= null;
		try {
			ciu= new CargaInformacionUsuarios(this.criteriosBusqueda);
			ciu.cargarPerfilesDisponible();
			buscar();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doBuscar
  
  public void buscar() {
    TcJanalUsuariosDto usuario= null;
    int index                 = -1;
    UISelectEntity personaSel = null;
    try {
			TcManticPersonasDto persona= (TcManticPersonasDto)this.attrs.get("tcManticPersonasDto");
      if (persona.isValid()) {
				persona.setContrasenia(BouncyEncryption.decrypt(persona.getContrasenia()));
        index = this.getCriteriosBusqueda().getListaPersonas().indexOf(new UISelectEntity(new Entity(persona.getIdPersona())));
				if(index>= 0){
          personaSel = this.getCriteriosBusqueda().getListaPersonas().get(index);
          this.criteriosBusqueda.setPersona(personaSel);
        }
			} // if
      usuario = (TcJanalUsuariosDto)this.attrs.get("tcJanalUsuariosDto");
      if (usuario.isValid()) {
        index = this.getCriteriosBusqueda().getListaPerfiles().indexOf(new UISelectEntity(new Entity(usuario.getIdPerfil())));
				if(index>= 0)
          this.criteriosBusqueda.setPerfil(this.getCriteriosBusqueda().getListaPerfiles().get(index));
      } // if
			this.attrs.put("confirmar", persona.getContrasenia());
    }  // try
		catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }
  
  private void loadAlmacenes() {
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
    this.attrs.put("nuevaExistenciaOrigen", (Float.valueOf(this.attrs.get("articulo")!=null?((UISelectEntity)this.attrs.get("articulo")).get("stock").toString():"0")-Float.valueOf(this.attrs.get("cantidad").toString()))) ;
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
      articuloDestino = (Entity) DaoFactory.getInstance().toEntity("VistaAlmacenesTransferenciasDto", "articuloAlmacen", this.attrs);
      this.attrs.put("articuloDestino", articuloDestino);
      this.attrs.put("nuevaExistenciaOrigen", (Float.valueOf(this.attrs.get("articulo")!=null?((UISelectEntity)this.attrs.get("articulo")).get("stock").toString():"0")-Float.valueOf(this.attrs.get("cantidad").toString()))) ;
      this.attrs.put("nuevaExistenciaDestino", (Float.valueOf(this.attrs.get("articuloDestino")!=null?((UISelectEntity)this.attrs.get("articuloDestino")).get("stock").toString():"0")+Float.valueOf(this.attrs.get("cantidad").toString()))) ;
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
  		params.put("idProveedor", -1L);
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
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
      this.attrs.put("idArticulo",seleccion.get("idArticulo"));
      this.image= LoadImages.getImage(seleccion.toString("archivo"));
      this.attrs.put("cantidad", 0);
      this.attrs.put("nuevaExistenciaOrigen", Float.valueOf("0"));
      this.attrs.put("nuevaExistenciaDestino", Float.valueOf("0"));
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

	public String doAceptar(){
		TcManticTransferenciasDto dto= null;
		Transaccion transaccion      = null;
    UISelectEntity datosPersona  = null;
    String regresar              = null;
		try {
      if(this.transferencia != null)
        dto = this.transferencia;
      else
        dto = new TcManticTransferenciasDto();
      datosPersona = this.criteriosBusqueda.getListaPersonas().get(this.criteriosBusqueda.getListaPersonas().indexOf(this.criteriosBusqueda.getPersona()));
      dto.setIdAlmacen(Long.valueOf(this.attrs.get("idAlmacenOrigen").toString()));
      dto.setIdArticulo(Long.valueOf(this.attrs.get("idArticulo").toString()));
      dto.setCantidad(Float.valueOf(this.attrs.get("cantidad").toString()));
      dto.setIdDestino(Long.valueOf(this.attrs.get("idAlmacenDestino").toString()));
      dto.setIdSolicito(this.criteriosBusqueda.getPersona().getKey());
      dto.setObservaciones(this.attrs.get("observaciones").toString());
      dto.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdPersona());
      dto.setIdTransferenciaEstatus(1L);
			transaccion= new Transaccion(dto, datosPersona.get("nombres").toString().concat(" ").concat(datosPersona.get("paterno").toString().concat(" ").concat(datosPersona.get("materno").toString())));
			if(transaccion.ejecutar((EAccion) this.attrs.get("accion"))){
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se registró la transferencia de correcta", ETipoMensaje.INFORMACION);
      }
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
