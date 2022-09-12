package mx.org.kaana.mantic.productos.categorias.backing;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticArchivosDto;
import mx.org.kaana.mantic.productos.backing.Contenedor;
import mx.org.kaana.mantic.productos.categorias.reglas.Transaccion;
import mx.org.kaana.mantic.productos.beans.Categoria;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.TreeNode;

@Named(value = "manticProductosCategoriasFiltro")
@ViewScoped
public class Filtro extends Contenedor implements Serializable {

  private static final long serialVersionUID= 8793667741599428369L;
	private static final String BYTES         = " Bytes";	
	private static final String K_BYTES       = " Kb";	

  private Categoria categoria;
  private EAccion accion;
  private String path;

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }
  
  public Boolean getEliminar() {
    return Objects.equals(this.accion, EAccion.ELIMINAR);  
  }
  
  public Boolean getAgregar() {
    return Objects.equals(this.accion, EAccion.AGREGAR);  
  }
  
  public String getTitulo() {
    return this.accion.getName();
  }
  
	public String getPath() {
    return path;
  }
  
  public String getCodigo() {
    String url= "";
    try {
      if(this.categoria.getNombre()!= null)
        url= "https://ferreteriabonanza.com/Control/galeria.jsf?zOxAi=".concat(Cifrar.cifrar(this.categoria.getNombre()));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    return url;  
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("idActivo", -1L);
      this.doLoad("", 1L, null);
      this.accion= EAccion.AGREGAR;
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/").concat(String.valueOf(JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende())).concat("/categorias/");      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  public void doAgregar(UISelectEntity father) {
    this.categoria= new Categoria(
      father.toString("padre").length()== 0? father.toString("nombre").concat(Constantes.SEPARADOR): father.toString("padre").concat(father.toString("nombre")).concat(Constantes.SEPARADOR), // String padre, 
      1L, // Long ultimo, 
      1L, // Long idActivo, 
      JsfBase.getIdUsuario(), // Long idUsuario, 
      -1L, // Long idProductoCategoria, 
      father.toLong("porcentaje"), // Long porcentaje, 
      "", // String nombre, 
      father.toLong("nivel")+ 1L, // Long nivel, 
      1L, // Long orden
      father.toLong("idProductoCategoria"),  // Long idPadre
      Constantes.SEPARADOR,
      null      
    );
  }
  
  public void doModificar(UISelectEntity father) {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idProductoCategoria", father.toLong("idProductoCategoria"));      
      this.categoria= (Categoria)DaoFactory.getInstance().toEntity(Categoria.class, "TcManticProductosCategoriasDto", "existe", params);
      if(this.categoria.getIdProductoCategoriaArchivo()!= null) {
        params.put("idProductoCategoriaArchivo", this.categoria.getIdProductoCategoriaArchivo());
        this.categoria.setImportado((Importado)DaoFactory.getInstance().toEntity(Importado.class, "TcManticProductosCategoriasArchivosDto", "igual", params));
      } // if
      else
        this.categoria.setImportado(new Importado());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }

  public void doAccion() {
		Transaccion transaccion = null;
		try {
      while(this.categoria.getNombre().startsWith(this.categoria.getSeparador()))
        this.categoria.setNombre(this.categoria.getNombre().substring(1));
      while(this.categoria.getNombre().endsWith(this.categoria.getSeparador()))
        this.categoria.setNombre(this.categoria.getNombre().substring(0, this.categoria.getNombre().length()- 1));
			transaccion= new Transaccion(this.categoria);
			if(transaccion.ejecutar(this.accion)) {
				JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "La categoría se "+ this.accion.getName().toLowerCase()+ " correctamente", ETipoMensaje.ERROR);
        this.getRoot().clearParent();
        this.init();
      } // if  
			else
				JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "Ocurrió un error al "+ this.accion.getName().toLowerCase()+ " el registro de la categoría", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doTransaccion	
 
  public String doGaleria() {
		try {
      UISelectEntity data= (UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData();
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Productos/Categorias/filtro");		
			JsfBase.setFlashAttribute("producto", data);
			JsfBase.setFlashAttribute("categoria", data.toString("padre").concat(data.toString("nombre")));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Productos/galeria".concat(Constantes.REDIRECIONAR);
  } // doGaleria
 
  public void doOperacion(String item) {
    this.accion= EAccion.valueOf(item);
 		Map<String, Object> params= new HashMap<>();
    try {
      switch(this.accion) {
        case AGREGAR:
          this.doAgregar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
          break;
        case MODIFICAR:
        case ELIMINAR:
           if(Objects.equals(((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData()).toLong("nivel"), 1L)) {
             JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "Esta categoría no se puede "+ this.accion.getName().toLowerCase(), ETipoMensaje.ALERTA);
             UIBackingUtilities.execute("janal.desbloquear();");
           } // if  
           else {
             this.doModificar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
             TreeNode father= ((TreeNode)this.attrs.get("seleccionado")).getParent();
             this.categoria.setIdPadre(((UISelectEntity)father.getData()).toLong("idProductoCategoria"));
             UIBackingUtilities.execute("PF('widgetCategoria').show();");
          } // else  
          break;
        case SUBIR:
          if(Objects.equals(((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData()).toLong("orden"), 1L)) 
            JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "Esta categoría no se puede "+ this.accion.getName().toLowerCase(), ETipoMensaje.ALERTA);
          else {
            this.doModificar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
            TreeNode father= ((TreeNode)this.attrs.get("seleccionado")).getParent();
            this.categoria.setIdPadre(((UISelectEntity)father.getData()).toLong("idProductoCategoria"));
            this.doAccion();
          } // else  
          break;
        case BAJAR:
          Long maximo= -1L;
    			params.put("padre", ((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData()).toString("padre"));
          Value next= DaoFactory.getInstance().toField("TcManticProductosCategoriasDto", "maximo", params, "siguiente");
          if(next!= null && next.getData()!= null)
            maximo= next.toLong();
          if(Objects.equals(((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData()).toLong("orden"), maximo)) 
            JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "Esta categoría no se puede "+ this.accion.getName().toLowerCase(), ETipoMensaje.ALERTA);
          else {
            this.doModificar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
            TreeNode father= ((TreeNode)this.attrs.get("seleccionado")).getParent();
            this.categoria.setIdPadre(((UISelectEntity)father.getData()).toLong("idProductoCategoria"));
            this.doAccion();
          } // else  
          break;
        case PROCESAR:
          this.doModificar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
          TreeNode father= ((TreeNode)this.attrs.get("seleccionado")).getParent();
          this.categoria.setIdPadre(((UISelectEntity)father.getData()).toLong("idProductoCategoria"));
          break;
      } // switch
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
  public void doLoad() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
 
  public String doProductos() {
		try {
      UISelectEntity data= (UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData();
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Productos/Categorias/filtro");		
			JsfBase.setFlashAttribute("categoria", data.toString("padre").concat(data.toString("nombre")));
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
			JsfBase.setFlashAttribute("idProducto", -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Productos/accion".concat(Constantes.REDIRECIONAR);
  } // doProductos
 
	public void doFileUpload(FileUploadEvent event) {
		String root    = null;  
		String nameFile= Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result    = null;		
		Long fileSize  = 0L;
		File filePath  = null;
		try {
			root= Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(String.valueOf(JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende())).concat("/categorias/");
			result  = new File(root.concat(nameFile));		
			filePath= new File(root);
			if (!filePath.exists())
				filePath.mkdirs();
			if (result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			this.categoria.setImportado(new Importado(nameFile, event.getFile().getContentType(), EFormatos.JPG, event.getFile().getSize(), fileSize.equals(0L)? fileSize: fileSize/1024, event.getFile().equals(0L)? BYTES: K_BYTES, root, root.concat(nameFile), event.getFile().getFileName().toUpperCase()));      
      //**
			this.toSaveFileRecord(event.getFile().getFileName().toUpperCase(), this.categoria.getImportado().getRuta(), this.categoria.getImportado().getRuta().concat(this.categoria.getImportado().getName()), this.categoria.getImportado().getName());
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
  
}