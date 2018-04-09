package mx.org.kaana.kajool.template.backing;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoFormato;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.reportes.reglas.IBaseDatasource;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporte;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporteDataSource;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.xml.Dml;
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

@ManagedBean(name = "kajoolTemplateReporte")
@ViewScoped
public class Reporte extends IBaseAttribute implements Serializable{
  
  private static final long serialVersionUID = -741532999302110919L;
  private List<UISelectItem> formatos;
  private EFormatos idFormato;
	private Long idTitulos;	
	private String nombre;
	private Long total;
	private IReporte ireporte;	

	public EFormatos getIdFormato() {
		return idFormato;
	}

	public void setIdFormato(EFormatos idFormato) {
		this.idFormato=idFormato;
	}

	public Long getIdTitulos() {
		return idTitulos;
	}

	public void setIdTitulos(Long idTitulos) {
		this.idTitulos=idTitulos;
	}

	public List<UISelectItem> getFormatos() {
		return formatos;
	}

	public Long getTotal() {
		return total;
	}

	public String getArchivo() {
		return this.nombre.substring(this.nombre.lastIndexOf(File.separatorChar)+1);
	}

	@PostConstruct
	protected void init() {
		try {
			this.formatos=new ArrayList<>();
			this.total=0L;
			this.nombre="";			
			this.attrs.put("xls",false);			
			for (EFormatos formato : EFormatos.values()) {
				if (formato.getType().equals(ETipoFormato.IREPORT)&&!formato.equals(EFormatos.DOC)&&!formato.equals(EFormatos.PPT)&&!formato.equals(EFormatos.JXL)) {
					this.formatos.add(new UISelectItem(formato, formato.name()));
				}
			}
			this.idFormato=EFormatos.PDF;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Detalle", "Error: "+e, ETipoMensaje.ERROR);
		} // catch
	} // init

	public void toAsignarReporte(IReporte ireporte) {
		this.ireporte=ireporte;
		this.idTitulos=ireporte.getTitulo();
		this.idFormato=ireporte.getFormato();
		if (ireporte instanceof IReporteDataSource) {
			this.total=1L;
		}
		else {
			this.total=toSize();
		}
		this.nombre=this.idFormato.toPath().concat(Archivo.toFormatNameFile(ireporte.getNombre().concat("."))).concat(this.idFormato.name().toLowerCase());
	} // toAsiganarReporte

	public StreamedContent getDescargar() {
		Zip zip                 = null;
		String contentType      = null;
		StreamedContent regresar= null;
		try {
			zip= new Zip();
			contentType=this.idFormato.getContent();
			if (this.ireporte.getComprimir()) {
				String zipName=this.nombre.substring(0, this.nombre.lastIndexOf(".")+1).concat(EFormatos.ZIP.name().toLowerCase());
				zip.setDebug(true);
				zip.setEliminar(true);
				zip.compactar(JsfBase.getRealPath(zipName), JsfBase.getRealPath(this.idFormato.toPath()), getArchivo());
				this.nombre=zipName;
				contentType=EFormatos.ZIP.getContent();
				InputStream stream=((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(this.nombre);
				regresar=new DefaultStreamedContent(stream, contentType, getArchivo());
			} // if	
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	} // getDescargar

	public Long toSize() {
		Long regresar= 0L;
		try {
			regresar= DaoFactory.getInstance().toSize(this.ireporte.getProceso(), this.ireporte.getIdXml(), this.ireporte.getParams());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
			regresar=100L;
		} // catch
		return regresar;
	} // toSize
	
	 public InputStream toInputStream(String name) {
    ClassLoader loader = this.getClass().getClassLoader();
    return loader.getResourceAsStream(name);
  }

	private void loadResourceFileJasper() throws Exception {
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			for (String key : this.ireporte.getParametros().keySet()) {
				if (key.startsWith(Constantes.TILDE)) {
					params.put(key.substring(1), toInputStream((String) this.ireporte.getParametros().get(key)));
				} // if				
			} // for			
			for (String key : params.keySet()) {
				this.ireporte.getParametros().remove(Constantes.TILDE.concat(key));
			}
			this.ireporte.getParametros().putAll(params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
	}
	
	private mx.org.kaana.libs.reportes.scriptlets.Reporte reporteConnection(String source, String nombreArchivo) throws Exception {
		mx.org.kaana.libs.reportes.scriptlets.Reporte regresar=null;
		String sql=null;
		try {
			sql= Dml.getInstance().getSelect(this.ireporte.getProceso(), this.ireporte.getIdXml(), this.ireporte.getParams());
			this.ireporte.getParametros().put(Constantes.REPORTE_SQL, sql);
			regresar=new mx.org.kaana.libs.reportes.scriptlets.Reporte(source.substring(0, source.lastIndexOf('.')), JsfBase.getRealPath(this.idFormato.toPath()).concat(File.separator), this.ireporte.getParametros(), nombreArchivo, (Boolean)this.attrs.get ("xls"));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
		return regresar;
	} // reporteConnection

	private mx.org.kaana.libs.reportes.scriptlets.Reporte reporteDataSource(String source, String nombreArchivo) throws Exception {
		mx.org.kaana.libs.reportes.scriptlets.Reporte regresar=null;		
		try {
			regresar=new mx.org.kaana.libs.reportes.scriptlets.Reporte(source.substring(0, source.lastIndexOf('.')), JsfBase.getRealPath(this.idFormato.toPath()).concat(File.separator), this.ireporte.getParametros(), nombreArchivo, ((IReporteDataSource) this.ireporte).getJrDataSource(),(Boolean)this.attrs.get ("xls"));
			if (this.ireporte instanceof IBaseDatasource)
			((IBaseDatasource) this.ireporte).reload();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
		return regresar;
	} // reporteDataSource

	public void doAceptar() throws Exception {
		mx.org.kaana.libs.reportes.scriptlets.Reporte reporteGenerar=null;
		String source=null;
		String fileName=null;
		InputStream input   = null;
		try {
			loadResourceFileJasper();
			fileName=Archivo.toFormatNameFile(this.ireporte.getNombre());
			this.nombre=this.idFormato.toPath().concat(fileName.concat(".")).concat(this.idFormato.name().toLowerCase());
			String sql=Dml.getInstance().getSelect(this.ireporte.getProceso(), this.ireporte.getIdXml(), this.ireporte.getParams());
			this.ireporte.getParametros().put(Constantes.REPORTE_VERSION, Configuracion.getInstance().getPropiedad("sistema.version"));
			this.ireporte.getParametros().put(Constantes.REPORTE_SQL, sql);
			this.ireporte.getParametros().put(Constantes.REPORTE_REGISTROS, this.total);
			this.ireporte.getParametros().put(Constantes.REPORTE_IMAGENES, JsfBase.getRealPath(Constantes.RUTA_IMAGENES).concat(File.separator));
			this.ireporte.getParametros().put(Constantes.REPORTE_TITULOS, this.idTitulos);
			input        = toInputStream(this.ireporte.getJrxml().concat(".jasper"));
			source=JsfBase.getRealPath(this.ireporte.getJrxml().concat(".jasper"));
			this.ireporte.getParametros().put(Constantes.REPORTE_SUBREPORTE, source.substring(0, source.lastIndexOf(File.separator)+File.separator.length()));
			if (ireporte instanceof IReporteDataSource) {
				reporteGenerar=reporteDataSource(source, fileName);
			}
			else {
				reporteGenerar=reporteConnection(source, fileName);
			}
			if(this.ireporte.getJrxml().startsWith(Constantes.NOMBRE_DE_APLICACION)) {
			  reporteGenerar.procesar(this.idFormato, input);
      } // if 
      else
			  reporteGenerar.procesar(this.idFormato);
			if (RequestContext.getCurrentInstance()!=null) {
				RequestContext.getCurrentInstance().addCallbackParam("janalOk", true);
			}
			Monitoreo monitoreo=JsfBase.getAutentifica().getMonitoreo();
			monitoreo.comenzar(this.total);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Detalle del error", "No se pudo generar el reporte "+e, ETipoMensaje.ERROR);
		} // catch
	} // doAceptar

	public void doCompleto() {
		JsfBase.addMessage("Detalle del mensaje", "Se generó correctamente el reporte.", ETipoMensaje.INFORMACION);
	} // doCompleto
}
