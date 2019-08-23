package mx.org.kaana.mantic.respaldos.backing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticRespaldosDto;
import mx.org.kaana.mantic.respaldos.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticRespaldosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final Log LOG=LogFactory.getLog(Filtro.class);
  private static final long serialVersionUID = 8895632457861235874L;

  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("sortOrder", "order by tc_mantic_respaldos.registro desc");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    Map<String, Object> params= null;	
    List<Columna> columns     = null;
    try {			
  	  params = toPrepare();
      columns = new ArrayList<>();     
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("observaciones", EFormatoDinamicos.LIBRE));    
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA)); 
      columns.add(new Columna("eliminado", EFormatoDinamicos.FECHA_HORA_CORTA)); 
      params.put("sortOrder", this.attrs.get("sortOrder"));
			this.lazyModel = new FormatCustomLazy("VistaRespaldosBdDto", "respaldos", params , columns);
      UIBackingUtilities.resetDataTable();			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally		
  } // doLoad

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_respaldos.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_respaldos.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	}
	
  public void doCrearResaldo(){
		Transaccion transaccion = null;
		try {
			transaccion= new Transaccion(this.attrs.get("observaciones").toString());
			if(transaccion.ejecutar(EAccion.AGREGAR))
				JsfBase.addMessage("Crear respaldo", "Se realizo el respaldo de la BD de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Crear respaldo", "Ocurrio un error al realizar el respaldo de la BD", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			this.attrs.put("observaciones", "");
		} // finally
	}	// doCrearResaldo
  
  public StreamedContent doFileDownload(Entity file) {
		StreamedContent regresar= null;
		try {
			File reference= new File(file.toString("alias"));
			if(reference.exists()) {
		    InputStream stream = new FileInputStream(reference);			
		    regresar= new DefaultStreamedContent(stream, EFormatos.ZIP.getContent(), file.toString("nombre"));
				this.checkDonwloadBackup(new  TcManticRespaldosDto(file.toLong("idRespaldo")));
			} // if
			else {
				LOG.warn("No existe el archivo: "+ file.toString("alias"));
        JsfBase.addMessage("No existe el archivo: "+ file.toString("nombre")+ ", favor de verificarlo.");
			} // else	
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		return regresar;
	} // doFileDownload
 
	private void checkDonwloadBackup(TcManticRespaldosDto respaldo) {
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(respaldo);
			if(!transaccion.ejecutar(EAccion.BAJAR))
        JsfBase.addMessage("No se pudo registrar en bitacora el registro de la descarga !");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
	
}