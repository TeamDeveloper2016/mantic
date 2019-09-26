package mx.org.kaana.kajool.template.backing;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.reportes.backing.BaseReportes;
import mx.org.kaana.kajool.procesos.reportes.beans.Definicion;
import mx.org.kaana.kajool.procesos.reportes.reglas.IJuntar;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporte;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporteDataSource;
import mx.org.kaana.kajool.seguridad.jarfile.SearchFileJar;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reportes.scriptlets.JuntarPdfs;
import mx.org.kaana.xml.Dml;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "kajoolTemplateReporte")
@ViewScoped
public class Reporte extends BaseReportes implements Serializable{
  
  private static final long serialVersionUID = -741532999302110919L;
  private IJuntar ijuntar;	
  private List<String>listaPDFs;

	@PostConstruct
	@Override
	protected void init() {
		try {
			this.formatos     = new ArrayList<>();
			this.total        = 0L;
			this.nombre       = "";			
			this.paginacionXls= false;			
			this.previsualizar= false;			
			this.idFormato    = EFormatos.PDF;
			this.habilitarXls = Boolean.toString(true);
      this.prefijo      = Constantes.ARCHIVO_PATRON_NOMBRE;
      this.fileName     = "";
      //doLoadReport();
      //this.cron = false;
			llenarFormatos();	
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Detalle", "Error: "+e, ETipoMensaje.ERROR);
		} // catch
	} // init

	public void toAsignarReporte(IReporte ireporte) {
    toAsignarReporte(ireporte, Constantes.ARCHIVO_PATRON_NOMBRE, "");
  } // toAsignarReporte
  
	public void toAsignarReporte(IReporte ireporte, String nombre) {
		toAsignarReporte(ireporte, Constantes.ARCHIVO_PATRON_NOMBRE, nombre);
	} // toAsignarReporte
	
	public void toAsignarReporte(IReporte ireporte, String prefijo, String nombre) {
		this.ireporte = ireporte;
		this.idTitulos= ireporte.getTitulo();		
    this.total    = ireporte instanceof IReporteDataSource ? 1L : toSize();		
    this.prefijo  = prefijo;
    this.idFormato= ireporte.getFormato();
    this.fileName = Cadena.isVacio(nombre) ? Archivo.toFormatNameFile(ireporte.getNombre(), this.prefijo) : nombre;
		this.nombre   = this.idFormato.toPath().concat(this.fileName).concat(".").concat(this.idFormato.name().toLowerCase());
	} // toAsiganarReporte

	public void toAsignarReportes(IJuntar ijuntar) {
    try {
      this.ijuntar = ijuntar;
      this.total= 1L;
      this.idTitulos= 0L;		
      this.prefijo  = Constantes.ARCHIVO_PATRON_NOMBRE;
      this.fileName = Archivo.toFormatNameFile(ijuntar.getNombre(), this.prefijo);
      this.nombre   = this.idFormato.toPath().concat(this.fileName).concat(".").concat(this.idFormato.name().toLowerCase());
    } // try
    catch(Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // toAsiganarReporte
	
	@Override
	public StreamedContent getDescargar()  { 
		StreamedContent regresar= null;
		try {
      if(this.ijuntar!= null) 
        regresar= getDescargarVarios();
      else 
        regresar= super.getDescargar();
		}// try
		catch (Exception e) {
		  Error.mensaje(e);
			JsfBase.addMessageError(e);
		}// catch
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
			regresar= 100L;
		} // catch
		return regresar;
	} // toSize
	
	public void doAceptar() {
    try {
      if(this.ijuntar!= null)
        if(this.ijuntar.getDefiniciones().size()> 1L)
          this.doAceptarVarios();
        else
          this.doAceptarVariosPdf();
      else
        this.doAceptarSimple();
    }
    catch (Exception e) {
	 	  Error.mensaje(e);
		  JsfBase.addMessage("Detalle del error", "No se pudo generar el reporte "+ e, ETipoMensaje.ERROR);
	  } // catch
  } // doAceptar
	
	public void doAceptarSimple() throws Exception {
		doAceptarSimple(JsfBase.getRealPath(this.ireporte.getJrxml().concat(".jasper")), JsfBase.getRealPath(Constantes.RUTA_IMAGENES).concat(File.separator), JsfBase.getRealPath());
	} // doAceptarSimple
	
	public void doAceptarSimple(String source, String imagenes, String path) throws Exception {
		mx.org.kaana.libs.reportes.scriptlets.Reporte reporteGenerar= null;		
		InputStream input= null;
		try {
			this.loadResourceFileJasper(this.ireporte.getParametros());        
      if(this.nombre.equals("")) {
        this.nombre=this.idFormato.toPath().concat(this.fileName.concat(".")).concat(this.idFormato.name().toLowerCase());
        this.nombre= Cadena.reemplazarCaracter(this.nombre, '/', File.separatorChar);      
      } // if
			String sql=Dml.getInstance().getSelect(this.ireporte.getProceso(), this.ireporte.getIdXml(), this.ireporte.getParams());
			this.ireporte.getParametros().put(Constantes.REPORTE_VERSION, Configuracion.getInstance().getPropiedad("sistema.version"));
			this.ireporte.getParametros().put(Constantes.REPORTE_SQL, sql);
			this.ireporte.getParametros().put(Constantes.REPORTE_REGISTROS, this.total);
			this.ireporte.getParametros().put(Constantes.REPORTE_IMAGENES, imagenes);
			this.ireporte.getParametros().put(Constantes.REPORTE_TITULOS, this.idTitulos);      
      this.ireporte.getParametros().put(Constantes.REPORTE_SUBREPORTE, source.substring(0, source.lastIndexOf(File.separator)+File.separator.length()));
			input = SearchFileJar.getInstance().toInputStream(this.ireporte.getJrxml().concat(".jasper"));
			if (ireporte instanceof IReporteDataSource) 
				reporteGenerar=reporteDataSource(source, this.fileName);
			else 
				reporteGenerar=reporteConnection(source, this.fileName, path);
			if(this.ireporte.getJrxml().startsWith(Constantes.NOMBRE_DE_APLICACION)) 
				reporteGenerar.procesar(this.idFormato, input);
			else 
				reporteGenerar.procesar(this.idFormato);			
			if (UIBackingUtilities.getCurrentInstance()!= null)
				UIBackingUtilities.addCallbackParam("janalOK", true);
			if (previsualizar) {
				this.ireporte.setParams((Map<String, Object>) ((HashMap)ireporte.getParams()).clone());
				this.attrs.put("reportePrevisualizar", JsfBase.getContext().concat("/").concat(this.getNombre()).concat("?pfdrid_c=true"));
				this.attrs.put("reporteFileName", this.fileName);
				UIBackingUtilities.update("dlgPrevisualizar");
				UIBackingUtilities.execute("PF('dialogoPrevisualizar').show();");
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // doAceptarSimple
  
  private void doAceptarVarios() throws Exception {
    mx.org.kaana.libs.reportes.scriptlets.Reporte reporteGenerar= null;
    String fileName       = null;
    String sql            = null;
    String source         = null;
    JuntarPdfs juntar     = null;
    InputStream input     = null; 
    try {
      listaPDFs= new ArrayList<String>();
      if(this.nombre.equals("")){
        this.nombre=this.idFormato.toPath().concat(this.fileName.concat(".")).concat(this.idFormato.name().toLowerCase());
        this.nombre= Cadena.reemplazarCaracter(this.nombre, '/' , File.separatorChar);      
      } // if
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
        reporteGenerar= new mx.org.kaana.libs.reportes.scriptlets.Reporte(source.substring(0, source.lastIndexOf('.')), JsfBase.getRealPath("/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.PDF.name()))).concat(File.separator), definicion.getParametros(), fileName);
        if(definicion.getJrxml().startsWith(Constantes.NOMBRE_DE_APLICACION))       
  			  reporteGenerar.procesar(this.idFormato, input);
        else  
          reporteGenerar.procesar(EFormatos.PDF);
        listaPDFs.add(JsfBase.getRealPath(this.nombre));
  			if (UIBackingUtilities.getCurrentInstance()!= null)
	  			UIBackingUtilities.addCallbackParam("janalOK", true);
      } // for 
      fileName= Archivo.toFormatNameFile(ijuntar.getNombre());
      this.nombre= JsfBase.getRealPath("/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.PDF.name())).concat(File.separator).concat(fileName.concat(".")).concat(EFormatos.PDF.name().toLowerCase()));
      if(!this.ijuntar.getSeparar()) {
        juntar= new JuntarPdfs(listaPDFs, this.nombre, this.ijuntar.getIntercalar());
        if(!juntar.concatenar()) {
          throw new RuntimeException(" Ocurrio un error en la generación del reporte. "+ this.nombre);
        } // if
      }
		} // try
		catch(Exception e) {
			throw e;
		} // catch
	} // doAceptarVarios
  
  private void doAceptarVariosPdf() throws Exception {
    String fileName       = null;
    JuntarPdfs juntar     = null;
    List<Entity> facturas = null;
    try {
      listaPDFs= new ArrayList<String>();
      if(this.nombre.equals("")){
        this.nombre=this.idFormato.toPath().concat(this.fileName.concat(".")).concat(this.idFormato.name().toLowerCase());
        this.nombre= Cadena.reemplazarCaracter(this.nombre, '/' , File.separatorChar);      
      } // if
      fileName   = Archivo.toFormatNameFile(ijuntar.getNombre());
      this.nombre= EFormatos.PDF.toPath().concat(fileName.concat(".")).concat(EFormatos.PDF.name().toLowerCase());
      facturas= DaoFactory.getInstance().toEntitySet(this.ijuntar.getDefiniciones().get(0).getProceso(), this.ijuntar.getDefiniciones().get(0).getIdXml(), this.ijuntar.getDefiniciones().get(0).getParams(),Constantes.SQL_TODOS_REGISTROS);
      for(Entity factura: facturas)
        listaPDFs.add(factura.toString("alias"));
      if (UIBackingUtilities.getCurrentInstance()!= null)
        UIBackingUtilities.addCallbackParam("janalOK", true); 
      fileName= Archivo.toFormatNameFile(ijuntar.getNombre());
      this.nombre= JsfBase.getRealPath("/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.PDF.name())).concat(File.separator).concat(fileName.concat(".")).concat(EFormatos.PDF.name().toLowerCase()));
      juntar= new JuntarPdfs(listaPDFs, this.nombre, this.ijuntar.getIntercalar());
      if(!juntar.concatenar()) {
        throw new RuntimeException(" Ocurrio un error en la generación del reporte. "+ this.nombre);
      } // if
		} // try
		catch(Exception e) {
			throw e;
		} // catch
	} // doAceptarVariosPdf
	
	@Override
	public void doCompleto() {
		JsfBase.addMessage("Detalle del mensaje", "Se generó correctamente el reporte.", ETipoMensaje.INFORMACION);		
		UIBackingUtilities.execute("hideBarra();");
	} // doCompleto
	
	
	@Override
	public void doSeleccionarReporte() {
		this.nombre= "";
		super.doSeleccionarReporte();		
	} 
	
  private StreamedContent getDescargarVarios() {
		Zip zip           = new Zip();
		String contentType= EFormatos.PDF.getContent();
		try {
			if(this.ijuntar.getComprimir()) {
				String zipName= getArchivo().substring(0, getArchivo().lastIndexOf(".")+ 1).concat(EFormatos.ZIP.name().toLowerCase());
				zip.setDebug(true);
				zip.setEliminar(true);
        if(!this.ijuntar.getSeparar())
          zip.compactar(JsfBase.getRealPath("/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.PDF.name()))).concat(File.separator).concat(zipName), JsfBase.getRealPath("/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.PDF.name()))), getArchivo());
        else {
          zip.compactar(JsfBase.getRealPath("/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.PDF.name()))).concat(File.separator).concat(zipName) , JsfBase.getRealPath("/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.PDF.name()))), "*".concat(this.ijuntar.getNombre().concat(".pdf")));
        }
				this.nombre= zipName;
				contentType= EFormatos.ZIP.getContent();
			} // if	
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
    InputStream stream= ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.PDF.name())).concat(File.separator).concat(this.nombre));  
    return new DefaultStreamedContent(stream, contentType, getArchivo());	
	}
  
  public void clean() {
    this.ijuntar= null;
    this.ireporte= null;
  } // clean
}
