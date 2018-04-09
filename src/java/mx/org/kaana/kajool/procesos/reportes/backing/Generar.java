package mx.org.kaana.kajool.procesos.reportes.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 9/09/2015
 * @time 04:23:02 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reportes.scriptlets.Reporte;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.seguridad.jarfile.SearchFileJar;
import mx.org.kaana.kajool.procesos.reportes.reglas.IBaseDatasource;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporte;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporteDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "kajoolReportesGenerar")
@ViewScoped
public class Generar extends BaseReportes implements Serializable {

	private static final Log LOG=LogFactory.getLog(Generar.class);
	private static final long serialVersionUID=7814879253720995049L;

	@PostConstruct
	@Override
	protected void init() {
		try {
			this.habilitarXls=Boolean.toString(true);
			if (JsfBase.getFlashAttribute(Constantes.REPORTE_REFERENCIA)!=null) {
				this.ireporte=(IReporte) JsfBase.getFlashAttribute(Constantes.REPORTE_REFERENCIA);
			} // if
			if (this.ireporte!=null) {
				this.automatico="";
				this.idTitulos=this.ireporte.getTitulo();
				this.idFormato=this.ireporte.getFormato();
				this.total=toSize();
				this.nombre=this.idFormato.toPath().concat(Archivo.toFormatNameFile(this.ireporte.getNombre().concat("."))).concat(this.idFormato.name().toLowerCase());
				this.formatos=new ArrayList<UISelectItem>();
				llenarFormatos();
				if (this.ireporte.getAutomatico()) {
					this.automatico="setTimeout(\"$('#aceptar').click()\", 1000);";
				} // if
			} // if
			else {
				this.nombre=this.idFormato.toPath().concat(Archivo.toFormatNameFile("default.")).concat(this.idFormato.name().toLowerCase());
			}
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // init

	public void doAceptar() throws Exception {
		String source=null;
		String nombreArchivo=null;
		Reporte reporte=null;
		Monitoreo monitoreo=null;
		InputStream input=null;
		try {
			loadResourceFileJasper(this.ireporte.getParametros());
			nombreArchivo=Archivo.toFormatNameFile(this.ireporte.getNombre());
			source=JsfBase.getRealPath(this.ireporte.getJrxml().concat(".jasper"));
			input=SearchFileJar.getInstance().toInputStream(this.ireporte.getJrxml().concat(".jasper"));
			this.nombre=this.idFormato.toPath().concat(nombreArchivo.concat(".")).concat(this.idFormato.name().toLowerCase());
			this.ireporte.getParametros().put(Constantes.REPORTE_VERSION, Configuracion.getInstance().getPropiedad("sistema.version"));
			this.ireporte.getParametros().put(Constantes.REPORTE_REGISTROS, this.total);
			this.ireporte.getParametros().put(Constantes.REPORTE_IMAGENES, JsfBase.getRealPath(Constantes.RUTA_IMAGENES).concat(File.separator));
			this.ireporte.getParametros().put(Constantes.REPORTE_TITULOS, this.idTitulos);
			this.ireporte.getParametros().put(Constantes.REPORTE_SUBREPORTE, source.substring(0, source.lastIndexOf(File.separator)+File.separator.length()));
			if (ireporte instanceof IReporteDataSource) {
				reporte=reporteDataSource(source, nombreArchivo);
			} // if
			else {
				reporte=reporteConnection(source, nombreArchivo);
			} // else
			if (this.ireporte.getJrxml().startsWith(Constantes.NOMBRE_RESOURCES)) {
				LOG.warn("Reporte: "+this.ireporte.getJrxml().concat(".jasper"));
				LOG.warn("Referencia: "+input);
				reporte.procesar(this.idFormato, input);
			} // if
			else {
				reporte.procesar(this.idFormato);
			} // else
			if (RequestContext.getCurrentInstance()!=null) {
				RequestContext.getCurrentInstance().addCallbackParam("janalOK", true);
			} // if
			monitoreo=JsfBase.getAutentifica().getMonitoreo();
			monitoreo.comenzar(this.total);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}

	@Override
	public StreamedContent getDescargar() {
		StreamedContent regresar=null;
		try {
			regresar=super.getDescargar();
		}// try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		}// catch
		return regresar;
	}

	private Long toSize() throws Exception {
		Long regresar=0L;
		try {
			if (this.ireporte instanceof IReporteDataSource) {
				regresar=((IBaseDatasource) this.ireporte).getTotal();
			} // if
			else {
				regresar=DaoFactory.getInstance().toSize(this.ireporte.getProceso(), this.ireporte.getIdXml(), this.ireporte.getParams());
			} // else
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	} // toSize

	public String doCancelar() {
		JsfBase.setFlashAttribute("parametrosFiltro", (HashMap<String, Object>) ((HashMap) ireporte.getParams()).clone());
		return this.ireporte.getRegresar();
	} // doCancelar

	@Override
	public void doSeleccionarReporte() {
		super.doSeleccionarReporte();
	} // doSeleccionarReporte
}
