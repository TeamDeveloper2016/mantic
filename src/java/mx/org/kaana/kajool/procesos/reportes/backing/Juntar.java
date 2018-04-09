package mx.org.kaana.kajool.procesos.reportes.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/11/2016
 * @time 10:40:02 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.PhaseId;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reportes.scriptlets.Reporte;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoFormato;
import mx.org.kaana.kajool.procesos.reportes.beans.Definicion;
import mx.org.kaana.kajool.seguridad.jarfile.SearchFileJar;
import mx.org.kaana.kajool.procesos.reportes.reglas.IJuntar;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.scriptlets.JuntarPdfs;
import mx.org.kaana.xml.Dml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "kajoolReportesJuntar")
@ViewScoped
public class Juntar extends BaseReportes implements Serializable {

	private static final Log LOG=LogFactory.getLog(Juntar.class);
  private static final long serialVersionUID = 3873843217900255403L;
	private IJuntar ijuntar;

	@PostConstruct
	protected void init() {
    try {
      if(JsfBase.getFacesContext().getCurrentPhaseId()!= PhaseId.RESTORE_VIEW) {
        this.ijuntar   = (IJuntar)JsfBase.getFlashAttribute(Constantes.REPORTE_REFERENCIA); 
        this.idFormato = EFormatos.PDF;
        this.total     = ((Number)ijuntar.getDefiniciones().size()).longValue()+1L;
        this.nombre    = EFormatos.PDF.toPath().concat(Archivo.toFormatNameFile(this.ijuntar.getNombre().concat("."))).concat(EFormatos.PDF.name().toLowerCase());
				this.formatos  = new ArrayList<UISelectItem>();
				for(EFormatos formato: EFormatos.values())
					if(formato.getType().equals(ETipoFormato.IREPORT))
						this.formatos.add(new UISelectItem(formato, formato.name()));
				if(this.ijuntar.getAutomatico())
					this.automatico= "setTimeout(\"$('#aceptar').click()\", 1000);";
      }
    } // try
    catch(Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	}

  public void doAceptar() throws Exception {
    String fileName       = null;
    String sql            = null;
    String source         = null;
    List<String>listaPDFs = null;
    JuntarPdfs juntar     = null;
    InputStream input     = null; 
    try {
      Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
			monitoreo.comenzar(((Number)(this.ijuntar.getDefiniciones().size()+ 1)).longValue());
      listaPDFs= new ArrayList<String>();
      for(Definicion definicion: this.ijuntar.getDefiniciones()) {
        fileName   = Archivo.toFormatNameFile(ijuntar.getNombre());
        this.nombre= EFormatos.PDF.toPath().concat(fileName.concat(".")).concat(EFormatos.PDF.name().toLowerCase());
        sql        = Dml.getInstance().getSelect(definicion.getProceso(), definicion.getIdXml(), definicion.getParams());
        loadResourceFileJasper(definicion.getParametros());
        definicion.getParametros().put(Constantes.REPORTE_SQL, sql);
        definicion.getParametros().put(Constantes.REPORTE_REGISTROS, DaoFactory.getInstance().toSize(definicion.getProceso(), definicion.getIdXml(), definicion.getParams()));
        definicion.getParametros().put(Constantes.REPORTE_IMAGENES,  JsfBase.getRealPath(Constantes.RUTA_IMAGENES).concat(File.separator));
        definicion.getParametros().put(Constantes.REPORTE_TITULOS,   0L);
        source= JsfBase.getRealPath(definicion.getJrxml().concat(".jasper"));
        input = SearchFileJar.getInstance().toInputStream(definicion.getJrxml().concat(".jasper"));
        definicion.getParametros().put(Constantes.REPORTE_SUBREPORTE, source.substring(0, source.lastIndexOf(File.separator)+ File.separator.length()));
        Reporte reporte= new Reporte(source.substring(0, source.lastIndexOf('.')), JsfBase.getRealPath(EFormatos.PDF.toPath()).concat(File.separator), definicion.getParametros(), fileName);
        if(definicion.getJrxml().startsWith(Constantes.NOMBRE_DE_APLICACION)) {
          LOG.warn("Reporte: "+ definicion.getJrxml().concat(".jasper"));
          LOG.warn("Referencia: "+ input);
  			  reporte.procesar(this.idFormato, input);
        } // if 
        else  
          reporte.procesar(EFormatos.PDF);
        listaPDFs.add(JsfBase.getRealPath(this.nombre));
        monitoreo.incrementar();
      } // for 
      fileName= Archivo.toFormatNameFile(ijuntar.getNombre());
      this.nombre= JsfBase.getRealPath(EFormatos.PDF.toPath().concat(fileName.concat(".")).concat(EFormatos.PDF.name().toLowerCase()));
      juntar= new JuntarPdfs(listaPDFs, this.nombre, this.ijuntar.getIntercalar());
      if(juntar.concatenar()) {
        monitoreo.incrementar();
        if(RequestContext.getCurrentInstance()!= null)
          RequestContext.getCurrentInstance().addCallbackParam("janalOk", true);
      } // if
      else {
        monitoreo.terminar();
        throw new RuntimeException("Ocurrio un error en la generación del reporte. "+ this.nombre);
      } // else
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
    finally {
      Methods.clean(listaPDFs);
    } // finally
	} // doAceptar
  
  public StreamedContent getDescargar() {
		Zip zip           = new Zip();
		String contentType= EFormatos.PDF.getContent();
		try {
			if(this.ijuntar.getComprimir()) {
				String zipName= getArchivo().substring(0, getArchivo().lastIndexOf(".")+ 1).concat(EFormatos.ZIP.name().toLowerCase());
				zip.setDebug(true);
				zip.setEliminar(true);
        zip.compactar(JsfBase.getRealPath(zipName), JsfBase.getRealPath(EFormatos.PDF.toPath()), getArchivo());
				this.nombre= zipName;
				contentType= EFormatos.ZIP.getContent();
			} // if	
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
    InputStream stream= ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(this.nombre);  
    return new DefaultStreamedContent(stream, contentType, getArchivo());		
	}
  
  public String doCancelar() {
    return this.ijuntar.getRegresar();
	}
  
}
