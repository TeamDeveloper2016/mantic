package mx.org.kaana.kajool.procesos.importacion.reglas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.procesos.importacion.beans.Importado;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 31/01/2013
 *@time 12:54:46 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class UIArchivo implements Serializable{

  private static final long serialVersionUID=-8690181538652446792L;
  private static final int BUFFER_SIZE = 6124;
  private static final Log LOG =  LogFactory.getLog(UIArchivo.class);
  private String titulo;
  private String regresar;
  private String formatos;
  private String continuar;
  private IValidar validar;
  private List<Importado> archivos;
  private boolean multiple;
  private String update;


  public UIArchivo() {
    this("titulo", null, null, null, null, new ArrayList<Importado>(), true, ":datos");

  }

  public UIArchivo(String titulo, String regresar, String formatos, String continuar, String update) {
    this(titulo, regresar, formatos, continuar, new Contenido(), new ArrayList<Importado>(), true, update);
  }

  public UIArchivo(String titulo, String regresar, String formatos, String continuar, boolean multiple, String update) {
    this(titulo, regresar, formatos, continuar, new Contenido(), new ArrayList<Importado>(), multiple, update);
  }
  public UIArchivo(String titulo, String regresar, String formatos, String continuar, IValidar validar, List<Importado> archivos, boolean multiple, String update) {
    this.titulo=titulo;
    this.regresar=regresar;
    this.formatos=formatos;
    this.continuar=continuar;
    this.validar=validar;
    this.archivos=archivos;
    this.multiple=multiple;
    this.update = update;
  }

  public List<Importado> getArchivos() {
    return archivos;
  }

  public void setArchivos(List<Importado> archivos) {
    this.archivos=archivos;
  }

  public String getContinuar() {
    return continuar;
  }

  public void setContinuar(String continuar) {
    this.continuar=continuar;
  }

  public String getFormatos() {
    return formatos;
  }

  public void setFormatos(String formatos) {
    this.formatos=formatos;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public void setMultiple(boolean multiple) {
    this.multiple=multiple;
  }

  public String getRegresar() {
    return regresar;
  }

  public void setRegresar(String regresar) {
    this.regresar=regresar;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo=titulo;
  }

  public IValidar getValidar() {
    return validar;
  }

  public void setValidar(IValidar validar) {
    this.validar=validar;
  }

  public String getUpdate() {
    return update;
  }

  public void setUpdate(String update) {
    this.update=update;
  }

   public void doFileUpload(FileUploadEvent event) {
    String nombreArchivo      = Archivo.toFormatNameFile(event.getFile().getFileName().toLowerCase());
    ExternalContext extContext= FacesContext.getCurrentInstance().getExternalContext();
    File source               = new File(extContext.getRealPath(Constantes.RUTA_IMPORTADOS));
    File result               = new File(extContext.getRealPath(Constantes.RUTA_IMPORTADOS+ nombreArchivo));
    LOG.info(extContext.getRealPath(Constantes.RUTA_IMPORTADOS+ nombreArchivo));
		FileOutputStream fileOutputStream = null;
		InputStream inputStream           = null;
    try {
      if(!(source.exists()))
        source.mkdirs();
      fileOutputStream=new FileOutputStream(result);
      byte[] buffer=new byte[BUFFER_SIZE];
      int bulk;
      inputStream=event.getFile().getInputstream();
      while (true) {
        bulk=inputStream.read(buffer);
        if (bulk<0) {
          break;
        } // if
        fileOutputStream.write(buffer, 0, bulk);
        fileOutputStream.flush();
      } // while
      fileOutputStream.close();
      inputStream.close();
      FacesMessage msg= new FacesMessage("Descripción del archivo", "Nombre: "+nombreArchivo+ "\nTamaño: "+
                            (event.getFile().getSize()/1024)+ " Kb\nContenido: " +
                             event.getFile().getContentType()+ "\nEl archivo fue importado con éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      Importado importado=  new Importado(nombreArchivo, event.getFile().getContentType(), EFormatos.valueOf(event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf(".")+1).toUpperCase()), event.getFile().getSize());
      if(this.validar.single(importado))
        this.archivos.add(importado);
      else
        doEliminar(importado);
    } // try
    catch (IOException e) {
      Error.mensaje(e);
      FacesMessage error=new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El archivo no fue importado, verifiquelo por favor !\n"+ e.getMessage());
      FacesContext.getCurrentInstance().addMessage(null, error);
    } // catch
  }

   public void doEliminar(Importado importado) {
    if(!importado.getName().isEmpty()) {
      File file= new File(JsfBase.getExternalContext().getRealPath(Constantes.RUTA_IMPORTADOS+ importado.getName()));
      file.delete();
      importado= new Importado("", "", EFormatos.FREE, 0L);
    } // if
  }

   public void doEliminarLista(Importado importado){
     doEliminar(importado);
     this.archivos.remove(importado);
   }

   public void doAceptar(){
      this.validar.all(this.archivos) ;
      RequestContext.getCurrentInstance().execute("validateDialog(".concat(Cadena.getString(regresar, "")).concat(")"));
   }
	
   public void doAceptar(ActionEvent event){
      this.validar.all(this.archivos) ;
      RequestContext.getCurrentInstance().execute("validateDialog(".concat(Cadena.getString(regresar, "")).concat(")"));
   }
	
	 public void doLimpiarArchivos(){
		 getArchivos().clear();
	 }
}
