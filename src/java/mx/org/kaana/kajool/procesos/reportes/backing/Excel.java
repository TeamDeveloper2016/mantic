package mx.org.kaana.kajool.procesos.reportes.backing;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.kajool.procesos.reportes.reglas.IXls;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Xls;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "kajoolReportesExcel")
@ViewScoped
public class Excel implements Serializable {

	private static final long serialVersionUID = 4446536384405049607L;    
  private IXls ixls;
  private String automatico;
  private Long total;
  private String nombre;
  private EFormatos idFormato;  
  private Boolean reporteGenerado;

  public Long getTotal() {
    return total;
  }

  public EFormatos getIdFormato() {
    return idFormato;
  }

  public Boolean getVer() {
		return Boolean.FALSE;
	}
  
  public String getArchivo() {
    return this.nombre.substring(this.nombre.lastIndexOf(File.separatorChar)+ 1);
	}
  
  public String getPatron() {
    return "*".concat(ixls.getPatron().concat(".").concat(EFormatos.XLS.name().toLowerCase()));
  }
  
  public String getAutomatico() {
    return automatico;
  }

  public void setAutomatico(String automatico) {
    this.automatico=automatico;
  }

  @PostConstruct
	private void init() {
    try {
			if(JsfBase.getFlashAttribute(Constantes.REPORTE_REFERENCIA)!= null) 
        this.ixls   = (IXls)JsfBase.getFlashAttribute(Constantes.REPORTE_REFERENCIA);       
      this.idFormato = EFormatos.XLS;
      this.total     = DaoFactory.getInstance().toSize(this.ixls.getModelo().getProceso(), this.ixls.getModelo().getIdXml(), this.ixls.getModelo().getParams());
      this.nombre    = EFormatos.XLS.toPath().concat(Archivo.toFormatNameFile(this.ixls.getNombre().concat("."))).concat(EFormatos.ZIP.name().toLowerCase());
      this.automatico= "setTimeout(\"$('#aceptar').click()\", 1000);"; 
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}

  public void doAceptar() throws Exception {
    String fileName= null;
    try {
      fileName= JsfBase.getRealPath("").concat(EFormatos.XLS.toPath().concat(Archivo.toFormatNameFile(this.ixls.getModelo().getNombre()).concat(".")).concat(EFormatos.XLS.name().toLowerCase()));
      Xls xls= new Xls(fileName, new Modelo(this.ixls.getModelo().getParams(), this.ixls.getModelo().getProceso(), this.ixls.getModelo().getIdXml()), this.ixls.getCampos());
      this.reporteGenerado= xls.procesar();        
      if (this.reporteGenerado && toZipFile()){
        UIBackingUtilities.addCallbackParam("janalOk", true);
      } // if
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
	} // doAceptar
  
  private Boolean toZipFile(){
    Boolean regresar = true;
		Zip zip           = new Zip();
		try {
			if(this.ixls.getComprimir()) {
				String zipName= this.nombre;
				zip.setDebug(true);
				zip.setEliminar(true);
        zip.compactar(JsfBase.getRealPath("").concat(zipName), JsfBase.getRealPath("").concat(EFormatos.XLS.toPath()), getPatron());
			} // if	
		} // try
		catch(Exception e) {
			Error.mensaje(e);
      regresar = false;
		} // catch
    return regresar;
  }
 
  public StreamedContent getDescargar() {
		String contentType= EFormatos.ZIP.getContent();
    InputStream stream= ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(this.nombre);  
		StreamedContent regresar = new DefaultStreamedContent(stream, contentType, getArchivo());				
    return regresar;
	}
  
  public void doCompleto() {
    if(!this.reporteGenerado) 
      JsfBase.addMessage("Reporte", "Ocurrio un error en la generación del reporte.", ETipoMensaje.ERROR);
	}
  
  public String doCancelar() {
    JsfBase.setFlashAttribute("parametrosRegreso", this.ixls.getParametros()== null? null : (HashMap<String, Object>)((HashMap)ixls.getParametros()).clone());
    return this.ixls.getRegresar().concat(Constantes.REDIRECIONAR);
	}
}
