package mx.org.kaana.kajool.procesos.reportes.backing;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.scriptlets.Reporte;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoFormato;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.seguridad.jarfile.SearchFileJar;
import mx.org.kaana.kajool.procesos.reportes.reglas.IBaseDatasource;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporte;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporteDataSource;
import mx.org.kaana.xml.Dml;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 9/09/2015
 * @time 04:30:59 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class BaseReportes extends IBaseAttribute implements Serializable {

	protected EFormatos idFormato;
	protected Long idTitulos;
	protected String nombre;
	protected Long total;
	protected String automatico;
	protected List<UISelectItem> formatos;
	protected IReporte ireporte;
	protected Boolean paginacionXls;
	protected String habilitarXls;

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public Long getTotal() {
		return total;
	}

	public String getAutomatico() {
		return automatico;
	}

	public List<UISelectItem> getFormatos() {
		return formatos;
	}

	public Boolean getPaginacionXls() {
		return paginacionXls;
	}

	public void setPaginacionXls(Boolean paginacionXls) {
		this.paginacionXls=paginacionXls;
	}

	public String getHabilitarXls() {
		return habilitarXls;
	}

	public void setHabilitarXls(String habilitarXls) {
		this.habilitarXls=habilitarXls;
	}

	protected void loadResourceFileJasper(Map<String, Object> parametros) throws Exception {
		Map<String, Object> params= null;
    JasperReport subreport    = null;
		try {
			params=new HashMap<>();
			for (String key : parametros.keySet()) {
				if (key.startsWith(Constantes.TILDE)) {
					subreport=(JasperReport) JRLoader.loadObject(new File(JsfBase.getRealPath((String)parametros.get(key))));
          if(subreport== null)
  					subreport=(JasperReport) JRLoader.loadObject(SearchFileJar.getInstance().toInputStream((String) parametros.get(key)));
					params.put(key.substring(1), subreport);
				} // if				
			} // for			
			for (String key : params.keySet()) {
				parametros.remove(Constantes.TILDE.concat(key));
			} // for
			parametros.putAll(params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
	}

	public String getArchivo() {
		return this.nombre.substring(this.nombre.lastIndexOf(File.separatorChar)+1);
	}

	public Boolean getVer() {
		return Boolean.FALSE;
	}

	public void doCompleto() {
		JsfBase.addMessage("Detalle del mensaje", "Se generó correctamente el reporte.", ETipoMensaje.INFORMACION);
	} // doCompleto

	@Override
	protected void init() {
	}

	protected void llenarFormatos() {
		for (EFormatos formato : EFormatos.values()) {
			if (formato.getType().equals(ETipoFormato.IREPORT)) {
				this.formatos.add(new UISelectItem(formato, formato.name()));
			} // if
		} // for
	}

	protected StreamedContent getDescargar() throws Exception {
		Zip zip=null;
		String contentType=null;
		StreamedContent regresar=null;
		String zipName=null;
		InputStream inputStream=null;
		try {
			zip=new Zip();
			if (this.ireporte.getComprimir()) {
				zipName=this.nombre.substring(0, this.nombre.lastIndexOf(".")+1).concat(EFormatos.ZIP.name().toLowerCase());
				zip.setDebug(true);
				zip.setEliminar(true);
				zip.compactar(JsfBase.getRealPath(zipName), JsfBase.getRealPath(this.idFormato.toPath()), getArchivo());
				this.nombre=zipName;
				contentType=EFormatos.ZIP.getContent();
				inputStream=((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(this.nombre);
				regresar=new DefaultStreamedContent(inputStream, contentType, getArchivo());
			} // if	
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	} // getDescargar

	protected Reporte reporteDataSource(String source, String nombreArchivo) throws Exception {
		Reporte regresar=null;
		try {
			regresar=new Reporte(source.substring(0, source.lastIndexOf('.')), JsfBase.getRealPath(this.idFormato.toPath()).concat(File.separator), this.ireporte.getParametros(), nombreArchivo, ((IReporteDataSource) this.ireporte).getJrDataSource(), this.paginacionXls);
			if (this.ireporte instanceof IBaseDatasource) {
				((IBaseDatasource) this.ireporte).reload();
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	}

	protected Reporte reporteConnection(String source, String nombreArchivo) throws Exception {
		Reporte regresar=null;
		String sql=null;
		try {
			sql=Dml.getInstance().getSelect(this.ireporte.getProceso(), this.ireporte.getIdXml(), this.ireporte.getParams());
			this.ireporte.getParametros().put(Constantes.REPORTE_SQL, sql);
			regresar=new Reporte(source.substring(0, source.lastIndexOf('.')), JsfBase.getRealPath(this.idFormato.toPath()).concat(File.separator), this.ireporte.getParametros(), nombreArchivo, this.paginacionXls);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	} // reporteConnection	

	protected void doSeleccionarReporte() {
		if (this.idFormato.name().equals(EFormatos.XLS.name())||this.idFormato.name().equals(EFormatos.XLSX.name())) {
			this.habilitarXls=Boolean.toString(false);
		} // if
		else {
			this.habilitarXls=Boolean.toString(true);
			this.paginacionXls=false;
		} // else
	} // doSeleccionarReporte 	
}
