package mx.org.kaana.mantic.productos.backing;

import java.io.File;
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
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.ArticuloImagen;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticArchivosDto;
import mx.org.kaana.mantic.productos.reglas.Transaccion;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.TreeNode;


@Named(value = "manticProductosAccion")
@ViewScoped
public class Accion extends Contenedor implements Serializable {

  private static final long serialVersionUID= 327393488565639367L;
  private static final Log LOG = LogFactory.getLog(Accion.class);
	private static final String BYTES         = " Bytes";	
	private static final String K_BYTES       = " Kb";	
  
  private ArticuloImagen imagen;
	private Producto producto;
  private String path;

  public Producto getProducto() {
    return producto;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

	public String getPath() {
    return path;
  }

	public String getBrand() {
    return path.concat(String.valueOf(JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende())).concat("/marcas/");
  }

	public String getSource() {
    return path.concat(String.valueOf(JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende())).concat("/");
  }

  public ArticuloImagen getImagen() {
    return imagen;
  }
  
  @PostConstruct
  @Override
  protected void init() {		
    try {
      if(JsfBase.getFlashAttribute("accion")== null)
		  	UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("categoria", JsfBase.getFlashAttribute("categoria")== null? "": JsfBase.getFlashAttribute("categoria"));
      this.attrs.put("idProducto", JsfBase.getFlashAttribute("idProducto")== null? -1L: JsfBase.getFlashAttribute("idProducto"));
		  this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("codigo", "");
      this.attrs.put("buscaPorCodigo", false);
  	  this.attrs.put("total", 0);
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");      
			this.doLoad();
      this.toLoadCatalog();
      this.doLoad("", 1L, null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    EAccion eaccion           = null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.producto= new Producto();
          this.producto.setIkEmpresa(new UISelectEntity(JsfBase.getAutentifica().getEmpresa().getIdEmpresa()));
          this.producto.getProducto().setIdActivo(1L);
          this.producto.getProducto().setCategoria((String)this.attrs.get("categoria"));
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.producto= new Producto((Long)this.attrs.get("idProducto"));
          this.toAsignImage();
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
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> sucursales= UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
      this.attrs.put("sucursales", sucursales);			
      if(sucursales!= null && !sucursales.isEmpty())
        if(Objects.equals((EAccion)this.attrs.get("accion"), EAccion.AGREGAR))
          this.producto.setIkEmpresa(sucursales.get(0));
        else {
          int index= sucursales.indexOf(new UISelectEntity(this.producto.getProducto().getIdEmpresa()));
          if(index>= 0)
            this.producto.setIkEmpresa(sucursales.get(index));
          else
            this.producto.setIkEmpresa(sucursales.get(0));
        } // if  
      List<UISelectEntity> categorias= (List<UISelectEntity>)UIEntity.build("TcManticProductosCategoriasDto", "categorias", params, "categoria");
      this.attrs.put("categorias", categorias);			
      if(categorias!= null && !categorias.isEmpty())
        if(Objects.equals((EAccion)this.attrs.get("accion"), EAccion.AGREGAR))
          this.producto.setIkCategoria(categorias.get(0));
        else {
          int index= categorias.indexOf(new UISelectEntity(this.producto.getProducto().getIdProductoCategoria()));
          if(index>= 0)
            this.producto.setIkCategoria(categorias.get(index));
          else
            this.producto.setIkCategoria(categorias.get(0));
        } // if  
      List<UISelectEntity> marcas= UIEntity.build("TcManticProductosMarcasDto", "todos", params, "nombre");
      if(marcas!= null && !marcas.isEmpty())
        if(Objects.equals((EAccion)this.attrs.get("accion"), EAccion.AGREGAR))
          this.producto.setIkMarca(marcas.get(0));
        else {
          int index= marcas.indexOf(new UISelectEntity(this.producto.getProducto().getIdProductoMarca()));
          if(index>= 0)
            this.producto.setIkMarca(marcas.get(index));
          else
            this.producto.setIkMarca(marcas.get(0));
        } // if  
      this.attrs.put("marcas", marcas);			
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
    
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
    try {			
			eaccion= (EAccion) this.attrs.get("accion");
      if(Objects.equals(this.producto.getProducto().getIdImagen(), -1L))
        this.producto.getProducto().setIdImagen(null);
			transaccion = new Transaccion(this.producto, this.imagen);
			if (transaccion.ejecutar(eaccion)) {
    		JsfBase.setFlashAttribute("idProducto", this.producto.getProducto().getIdProducto());
				regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR)? "agregó": "modificó").concat(" el registro del producto."), ETipoMensaje.INFORMACION);
        this.imagen= null;
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el producto", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
		JsfBase.setFlashAttribute("idProducto", this.producto!=null? this.producto.getProducto().getIdProducto(): -1L);
    return this.attrs.get("retorno")== null? "filtro".concat(Constantes.REDIRECIONAR): ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

	public void doLoadPartidas() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		int buscarCodigoPor       = 1;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);
			String search= (String) this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				if((boolean)this.attrs.get("buscaPorCodigo"))
			    buscarCodigoPor= 0;
				if(search.startsWith("."))
					buscarCodigoPor= 2;
				else 
					if(search.startsWith(":"))
						buscarCodigoPor= 1;
				if(search.startsWith(".") || search.startsWith(":"))
					search= search.trim().substring(1);				
				search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);	
			switch(buscarCodigoPor) {      
				case 0: 
				case 2: 
          this.lazyModel= new FormatCustomLazy("VistaOrdenesComprasDto", "porCodigo", params, columns);
          break;
				case 1: 
          this.lazyModel= new FormatCustomLazy("VistaOrdenesComprasDto", "porNombre", params, columns);
          break;
			} // switch
      UIBackingUtilities.resetDataTable("encontrados");
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
 
	public void doAgregarPartida(Entity articulo) {
    Map<String, Object> params= new HashMap<>();
		try {
      Partida partida= new Partida(articulo.toLong("idArticulo"), articulo.toString("codigo"), articulo.toString("propio"), articulo.toString("nombre"), articulo.toLong("idImagen"), articulo.toString("archivo"), "menudeo");
			if(this.producto.getArticulos().indexOf(partida)>= 0) 
        this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO YA ESTA EN LA LISTA</span>");
      else {
        params.put("idArticulo", articulo.toLong("idArticulo"));
        params.put("idProducto", this.producto.getProducto().getIdProducto());
        Entity entity= (Entity)DaoFactory.getInstance().toEntity("TcManticProductosDetallesDto", "existe", params);
        if(entity!= null && !entity.isEmpty()) {
          this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO YA ESTA EN OTRO PRODUCTO ["+ entity.toString("nombre")+ "]</span>");
        } // if
        else {
          this.producto.addPartida(partida);
          UIBackingUtilities.execute("jsKardex.cursor.top= "+ (this.producto.getArticulos().size()- 1)+"; jsKardex.callback("+ articulo+");");
          this.attrs.put("total", this.producto.getArticulos().size());
        } // else  
      } // else  
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
    finally {
      Methods.clean(params);
    }// finally    
	}

	public void doEliminarPartida(Partida partida) {
		try {
      this.producto.removePartida(partida);
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		this.attrs.put("total", this.producto.getArticulos().size());
	}
  
  public void doRecuperarPartida(Partida partida) {
 		try {
      this.producto.doRecoverPartida(partida);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
  } 
  
	public void doSubirPartida(Partida partida) {
		try {
      this.producto.upPartida(partida);
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
  
	public void doBajarPartida(Partida partida) {
		try {
      this.producto.downPartida(partida);
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
  
  public String toColorPartida(Partida partida) {
    return ""; // Objects.equals(partida.getAction(), ESql.DELETE)? "janal-display-none": "";
  }
  
  public String toColorCaracteristica(Caracteristica caracteristica) {
    return ""; // Objects.equals(caracteristica.getAction(), ESql.DELETE)? "janal-display-none": "";
  }

  public String toColorExiste(Entity row) {
    int index= this.producto.getArticulos().indexOf(new Partida(row.toLong("idArticulo")));
    return index>= 0? "janal-display-none": "";
  }
  
	public void doAgregarCaracteristica() {
		try {
      this.producto.addCaracteristica(new Caracteristica("CARACTERISTICA_"+ (this.producto.getCaracteristicas().size()+ 1)));
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
  
	public void doEliminarCaracteristica(Caracteristica caracteristica) {
		try {
      this.producto.removeCaracteristica(caracteristica);
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
 
  public void doRecuperarCaracteristica(Caracteristica caracteristica) {
 		try {
      this.producto.doRecoverCaracteristica(caracteristica);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
  } 
  
  public void doSubirCaracteristica(Caracteristica caracteristica) {
		try {
      this.producto.upCaracteristica(caracteristica); 
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}

  public void doBajarCaracteristica(Caracteristica caracteristica) {
		try {
      this.producto.downCaracteristica(caracteristica);  
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}

  public void doUpdatePrinicipal(Partida partida) {
 		try {
      this.producto.toUpdatePrincipal(partida);
      this.toAsignImage();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
  }
 
  public void doMarcaOpcion() {
    
  }
  
  public void doConcatMarca() {
    
  }

  public void doCheckPartida(Partida partida) {
    partida.setDescripcion(partida.getDescripcion().trim().toUpperCase());
    partida.setMedida(partida.getMedida().trim().toUpperCase());
    if(!Objects.equals(partida.getAction(), ESql.INSERT))
      partida.setAction(ESql.UPDATE);
  }
  
  public void doCheckEspecificacion(Caracteristica caracteristica) {
    caracteristica.setDescripcion(caracteristica.getDescripcion().trim().toUpperCase());
    if(!Objects.equals(caracteristica.getAction(), ESql.INSERT))
      caracteristica.setAction(ESql.UPDATE);
  }
  
  public String doLetraCapital(Caracteristica caracteristica) {
    return Cadena.letraCapital(caracteristica.getDescripcion());
  }  
 
  public void onSelectCategoria(NodeSelectEvent event) {
    this.attrs.put("data", event.getTreeNode());
    this.doGaleria();
  }  

  public void doGaleria() {
    UISelectEntity data= (UISelectEntity)((TreeNode)this.attrs.get("data")).getData();
    this.producto.getProducto().setCategoria(data.toString("padre").concat(data.toString("nombre")));
    this.producto.setIkCategoria(new UISelectEntity(data.toLong("idProductoCategoria")));
  }  
 
	public void onTabChange(TabChangeEvent event) {
		try {
      LOG.info("Actualiza todo !");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
  } // omnTabChange  
  
	public void doFileUpload(FileUploadEvent event) {
		String root    = null;  
		String nameFile= Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result    = null;		
		Long fileSize  = 0L;
		File filePath  = null;
		try {
			root= Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat("/");
			result  = new File(root.concat(nameFile));		
			filePath= new File(root);
			if(!filePath.exists())
				filePath.mkdirs();
			if(result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			Importado importado= new Importado(nameFile, event.getFile().getContentType(), EFormatos.JPG, event.getFile().getSize(), fileSize.equals(0L) ? fileSize : fileSize/1024, event.getFile().equals(0L)? BYTES: K_BYTES, root, root.concat(nameFile), event.getFile().getFileName().toUpperCase());      
      this.imagen= new ArticuloImagen(importado);
      //**
			this.toSaveFileRecord(event.getFile().getFileName().toUpperCase(), importado.getRuta(), importado.getRuta().concat(importado.getName()), importado.getName());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar archivo", "El archivo no pudo ser importado", ETipoMensaje.ERROR);
		} // catch
	} // doFileUpload
  
  private void toSaveFileRecord(String archivo, String ruta, String alias, String nombre) throws Exception {
		TcManticArchivosDto registro= new TcManticArchivosDto(
			archivo, // String archivo, 
			2L, // Long idEliminado, 
			ruta, // String ruta, 
			JsfBase.getIdUsuario(), // Long idUsuario, 
			alias, // String alias, 
			-1L, // Long idArchivo, 
			nombre // String nombre
		);
		DaoFactory.getInstance().insert(registro);
	}
 
  private void toAsignImage() {
    Map<String, Object> params= new HashMap<>();
    try {
      if(Objects.equals(this.producto.getProducto().getIdImagen(), -1L))
        this.imagen= null;
      else 
        if(this.imagen== null || (this.imagen.getImportado()!= null && !Objects.equals(this.producto.getProducto().getIdImagen(), this.imagen.getImportado().getId()))) {
          params.put("idImagen", this.producto.getProducto().getIdImagen());
          this.imagen= new ArticuloImagen((Importado)DaoFactory.getInstance().toEntity(Importado.class, "TcManticImagenesDto", "igual", params));
          if(this.imagen!= null && this.imagen.getImportado()!= null) {
            String ruta= this.imagen.getImportado().getRuta();
            this.imagen.getImportado().setRuta(ruta.substring(0, ruta.indexOf(this.imagen.getImportado().getName())));
            this.imagen.getImportado().setFormat(EFormatos.JPG);
            this.imagen.getImportado().setMedicion(BYTES);
            this.imagen.setId(this.imagen.getImportado().getId());
            this.imagen.setOriginal(this.imagen.getImportado().getOriginal());
            this.imagen.setNombre(this.imagen.getImportado().getName());
            this.imagen.setRuta(this.imagen.getImportado().getRuta());
            this.imagen.setTamanio(this.imagen.getImportado().getFileSize());
            this.imagen.setArchivo(this.imagen.getImportado().getOriginal());
            this.imagen.setAlias(ruta);
            this.imagen.setSqlAccion(ESql.SELECT);
          } // if  
        } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
}