package mx.org.kaana.kajool.procesos.importacion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jan 22, 2013
 *@time 12:00:03 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.procesos.importacion.beans.Importado;
import mx.org.kaana.kajool.procesos.importacion.reglas.IImportar;
import mx.org.kaana.kajool.procesos.importacion.reglas.IValidar;
import mx.org.kaana.kajool.procesos.importacion.reglas.Imagenes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.FileUploadEvent;

@ManagedBean(name="kajoolImportacionArchivo")
@ViewScoped
public class Archivo implements Serializable {

  private static final long serialVersionUID=3380993927929028309L;


  private static final Log LOG = LogFactory.getLog(Archivo.class);
  private static final int BUFFER_SIZE = 6124;

  private IImportar importar;
  private Importado seleccionado;

  public Importado getSeleccionado() {
    return seleccionado;
  }

  public void setSeleccionado(Importado seleccionado) {
    this.seleccionado=seleccionado;
  }


  public IImportar getImportar() {
    return importar;
  }

  @PostConstruct
	public void init() {
    try {
      this.importar= new Imagenes();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
	}

	public String doAceptar() {
    String regresar= null;
    try {
      IValidar validate= this.importar.validate();
      if(validate.all(this.importar.getFiles()))
        regresar= this.importar.getContinue();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
	}

  public void doFileUpload(FileUploadEvent event) {
    ExternalContext extContext= FacesContext.getCurrentInstance().getExternalContext();
    File result=new File(extContext.getRealPath(Constantes.RUTA_IMPORTADOS+ event.getFile().getFileName()));
    LOG.info(extContext.getRealPath(Constantes.RUTA_IMPORTADOS+ event.getFile().getFileName()));
    try {
      FileOutputStream fileOutputStream=new FileOutputStream(result);
      byte[] buffer=new byte[BUFFER_SIZE];
      int bulk;
      InputStream inputStream=event.getFile().getInputstream();
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
      FacesMessage msg= new FacesMessage("Descripción del archivo", "Nombre: "+event.getFile().getFileName()+ "\nTamaño: "+
                            (event.getFile().getSize()/1024)+ " Kb\nContenido: " +
                             event.getFile().getContentType()+ "\nEl archivo fue importado con éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      Importado importado=  new Importado(event.getFile().getFileName(), event.getFile().getContentType(), EFormatos.FREE, event.getFile().getSize());
      this.importar.getFiles().add(importado);
      IValidar validate= this.importar.validate();
      validate.single(importado);
    } // try
    catch (IOException e) {
      Error.mensaje(e);
      FacesMessage error=new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El archivo no fue importado, verifiquelo por favor !\n"+ e.getMessage());
      FacesContext.getCurrentInstance().addMessage(null, error);
    } // catch
  }

  public void doEliminarArchivo (Importado fila){
    try {
     ExternalContext extContext= FacesContext.getCurrentInstance().getExternalContext();
     File file=new File(extContext.getRealPath(Constantes.RUTA_IMPORTADOS+ fila.getName()));
     if(file.delete())
       this.importar.getFiles().remove(this.seleccionado);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }
}
