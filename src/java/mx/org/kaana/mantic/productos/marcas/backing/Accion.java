package mx.org.kaana.mantic.productos.marcas.backing;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.productos.marcas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticArchivosDto;
import mx.org.kaana.mantic.productos.marcas.beans.Marca;
import org.primefaces.event.FileUploadEvent;


@Named(value = "manticProductosMarcasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private static final String BYTES         = " Bytes";	
	private static final String K_BYTES       = " Kb";	
  
	private Marca marca;
  private String path;

  public Marca getMarca() {
    return marca;
  }

  public void setMarca(Marca marca) {
    this.marca = marca;
  }

	public String getPath() {
    return path;
  }
  
	@PostConstruct
  @Override
  protected void init() {		
    try {
      if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idProductoMarca", JsfBase.getFlashAttribute("idProductoMarca")== null? -1L: JsfBase.getFlashAttribute("idProductoMarca"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.doLoad();
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/").concat(String.valueOf(JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende())).concat("/marcas/");      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion           = null;
    Map<String, Object> params= null;
    try {
      params = new HashMap<>();      
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.marca= new Marca();
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          params.put("idProductoMarca", this.attrs.get("idProductoMarca"));
          this.marca= (Marca)DaoFactory.getInstance().toEntity(Marca.class, "TcManticProductosMarcasDto", "existe", params);
          if(this.marca.getIdProductoMarcaArchivo()!= null) {
            params.put("idProductoMarcaArchivo", this.marca.getIdProductoMarcaArchivo());
            this.marca.setImportado((Importado)DaoFactory.getInstance().toEntity(Importado.class, "TcManticProductosMarcasArchivosDto", "igual", params));
          } // if  
          break;
      } // switch
    } // try // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
    try {			
			eaccion= (EAccion) this.attrs.get("accion");
  		transaccion = new Transaccion(this.marca);
			if (transaccion.ejecutar(eaccion)) {
				regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" el registro de la marca de forma correcta."), ETipoMensaje.INFORMACION);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el IVA.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    JsfBase.setFlashAttribute("idProductoMarca", this.attrs.get("idProductoMarca"));
    return (String)this.attrs.get("retorno");
  } // doAccion

	public void doFileUpload(FileUploadEvent event) {
		String root    = null;  
		String nameFile= Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result    = null;		
		Long fileSize  = 0L;
		File filePath  = null;
		try {
			root= Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(String.valueOf(JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende())).concat("/marcas/");
			result  = new File(root.concat(nameFile));		
			filePath= new File(root);
			if (!filePath.exists())
				filePath.mkdirs();
			if (result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			this.marca.setImportado(new Importado(nameFile, event.getFile().getContentType(), EFormatos.JPG, event.getFile().getSize(), fileSize.equals(0L)? fileSize: fileSize/1024, event.getFile().equals(0L)? BYTES: K_BYTES, root, root.concat(nameFile), event.getFile().getFileName().toUpperCase()));      
      //**
			this.toSaveFileRecord(event.getFile().getFileName().toUpperCase(), this.marca.getImportado().getRuta(), this.marca.getImportado().getRuta().concat(this.marca.getImportado().getName()), this.marca.getImportado().getName());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar archivo", "El archivo no pudo ser importado.", ETipoMensaje.ERROR);
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