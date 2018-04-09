package mx.org.kaana.kajool.procesos.reportes.backing;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.kajool.procesos.reportes.reglas.IFoxPro;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Texto;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@ManagedBean(name="kajoolReportesText")
@ViewScoped
public class Text implements Serializable{
  
  private static final long serialVersionUID = -8996674159700658651L;
  private EFormatos idFormato;
  private IFoxPro ifoxpro;
  private String automatico;
  private String nombre;
  private Long total;
  
  public Long getTotal() {
    return total;
  }

  public EFormatos getIdFormato() {
    return idFormato;
  }

  public String getAutomatico() {
    return automatico;
  }

  public void setAutomatico(String automatico) {
    this.automatico=automatico;
  }
  
   
  public Boolean getVer() {
		return Boolean.FALSE;
	}
  
  public String getArchivo() {
    return this.nombre.substring(this.nombre.lastIndexOf(File.separatorChar)+ 1);
	}

  public String getArchivoDbf() {
    String nombreArchivo = this.nombre.substring(this.nombre.lastIndexOf(File.separatorChar)+ 1);       
    return nombreArchivo.substring(0, nombreArchivo.lastIndexOf(".")+ 1).concat(EFormatos.TXT.name().toLowerCase());
	}
  
  public String getPatron() {
    return "*".concat(ifoxpro.getPatron().concat(".").concat(EFormatos.TXT.name().toLowerCase()));
  }
   

  @PostConstruct
	private void init() {
    try {
      //if(JsfBase.getFacesContext().isPostback())
        this.ifoxpro    = (IFoxPro)JsfBase.getFlashAttribute((Constantes.REPORTE_REFERENCIA));
        this.idFormato  = EFormatos.TXT;
        this.total      = ((Number)ifoxpro.getDefiniciones().size()).longValue();
        this.nombre     = EFormatos.TXT.toPath().concat(Archivo.toFormatNameFile(this.ifoxpro.getNombre().concat("."))).concat(EFormatos.ZIP.name().toLowerCase());
        this.automatico = "setTimeout(\"$('#aceptar').click()\", 1000);"; 
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
	}

	public void doAceptar() throws Exception {
    String fileName      = null;
    List<String>listaDBFs= null;
    try {
      Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
			monitoreo.comenzar(((Number)(this.ifoxpro.getDefiniciones().size()+ 1)).longValue());
      listaDBFs= new ArrayList<>();
      Integer index = 0;
      for(Modelo definicion: this.ifoxpro.getDefiniciones()) {			
        fileName = JsfBase.getRealPath(EFormatos.TXT.toPath().concat(Archivo.toFormatNameFile(ifoxpro.getNombreDBF(index)).concat(".")).concat(EFormatos.TXT.name().toLowerCase()));
        Texto dbase= new Texto(fileName, new Modelo(definicion.getParams(), definicion.getProceso(), definicion.getIdXml()));
	  	  dbase.procesar(true);        
        listaDBFs.add(fileName);
        monitoreo.incrementar();
        index++; 
      } // for 
      if (toZipFile()){
			  if(RequestContext.getCurrentInstance()!= null)
          RequestContext.getCurrentInstance().addCallbackParam("janalOk", true);
      } // if
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
    finally {
      Methods.clean(listaDBFs);
    } // finally
	} // doAceptar

  private Boolean toZipFile(){
    Boolean regresar= true;
		Zip zip         = new Zip();
		try {
			if(this.ifoxpro.getComprimir()) {
				String zipName= this.nombre;
				zip.setDebug(true);
				zip.setEliminar(true);
        zip.compactar(JsfBase.getRealPath(zipName), JsfBase.getRealPath(EFormatos.TXT.toPath()), getPatron());
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
		JsfBase.addMessage("Detalle del mensaje", "Se generó correctamente la exportación.", ETipoMensaje.INFORMACION);
	}
  
  public String doCancelar() {
    return this.ifoxpro.getRegresar();
	}
}
