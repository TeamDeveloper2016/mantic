package mx.org.kaana.mantic.egresos.backing;

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
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.db.dto.TcManticEgresosBitacoraDto;
import mx.org.kaana.mantic.egresos.beans.ZipEgreso;
import mx.org.kaana.mantic.egresos.reglas.Transaccion;
import mx.org.kaana.mantic.enums.ECuentasEgresos;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticEgresosFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
		Long idEgreso= null;
    try {    	      
      this.attrs.put("descripcion", "");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());  
			loadEstatus();
			idEgreso= (Long) JsfBase.getFlashAttribute("idEgreso");
			if(idEgreso!= null){				
				this.attrs.put("idEgreso", idEgreso);
				doLoad();
			} // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadEstatus(){
		List<UISelectItem>estatus= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			estatus= UISelect.build("TcManticEgresosEstatusDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS);
			estatus.add(0, new UISelectItem(-1L, "TODOS"));
			this.attrs.put("estatus", estatus);
			this.attrs.put("idEstatus", UIBackingUtilities.toFirstKeySelectItem(estatus));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadEstatus
	
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= null;
    try {
      columns = new ArrayList<>();
			params= new HashMap<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));			
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));			
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));			
			params.put(Constantes.SQL_CONDICION, toCondicion());			
			params.put("sortOrder", "order by tc_mantic_egresos.registro desc, consecutivo desc");			
      this.lazyModel = new FormatCustomLazy("VistaEgresosDto", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad
	
	private String toCondicion(){
		String regresar        = null;
		String search          = null;
		StringBuilder condicion= null;
		try {			
			condicion= new StringBuilder("");			
			if(!Cadena.isVacio(this.attrs.get("idEgreso")) && !Long.valueOf(this.attrs.get("idEgreso").toString()).equals(-1L)){
				condicion.append("tc_mantic_egresos.id_egreso=").append(this.attrs.get("idEgreso")).append(" and ");							
				this.attrs.put("idEgreso", "");
			} // if
			if(!Cadena.isVacio(this.attrs.get("idEstatus")) && !Long.valueOf(this.attrs.get("idEstatus").toString()).equals(-1L))
				condicion.append("tc_mantic_egresos.id_egreso_estatus=").append(this.attrs.get("idEstatus")).append(" and ");						
			if(!Cadena.isVacio(this.attrs.get("fecha")))
				condicion.append("(date_format(tc_mantic_egresos.fecha, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fecha"))).append("') and ");	
			if(!Cadena.isVacio(this.attrs.get("fechaFinal")))
				condicion.append("(date_format(tc_mantic_egresos.fecha, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaFinal"))).append("') and ");	
			if(this.attrs.get("importeTicket")!= null && !Cadena.isVacio(this.attrs.get("importeTicket")) && !this.attrs.get("importeTicket").toString().equals("0.00"))
				condicion.append("tc_mantic_egresos.importe like '%").append(Cadena.eliminaCaracter(this.attrs.get("importeTicket").toString(), ',')).append("%' and ");					
			search= (String) this.attrs.get("descripcion");
			if(!Cadena.isVacio(search)) {
				search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();				
				condicion.append("upper(tc_mantic_egresos.descripcion) regexp upper('.*").append(search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*")).append(".*') and ");						
			} // if									
			if(Cadena.isVacio(condicion))
				regresar= Constantes.SQL_VERDADERO;
			else
				regresar= condicion.substring(0, condicion.length()-4);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCondicion

  public String doAccion(String accion) {
    EAccion eaccion = null;
    try {
      eaccion = EAccion.valueOf(accion.toUpperCase());
      JsfBase.setFlashAttribute("accion", eaccion);
      JsfBase.setFlashAttribute("idEgreso", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR) || eaccion.equals(EAccion.COPIAR) || eaccion.equals(EAccion.ACTIVAR)) ? ((Entity) this.attrs.get("seleccionado")).getKey() : -1L);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion	  		
	
  public String doMasivo() {
    JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Egresos/filtro");
    JsfBase.setFlashAttribute("idTipoMasivo", ECargaMasiva.EGRESOS.getId());
    return "/Paginas/Mantic/Catalogos/Masivos/importar".concat(Constantes.REDIRECIONAR);
	} // doMasivo	
	
	public void doRegistraNota(){
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
			transaccion= new Transaccion(seleccionado.getKey(), this.attrs.get("nota").toString());
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
				JsfBase.addMessage("Registrar nota", "Se registro de forma correcta la nota.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Registrar nota", "Ocurrió un error al registrar la nota.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doRegistraNota
	
	public String doDetalle(){
		String regresar= null;		
		try {
			JsfBase.setFlashAttribute("idEgreso", ((Entity)this.attrs.get("seleccionado")).getKey());
			regresar= "detalle".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doDetalle
	
	public String doImportar(){
		String regresar= null;
		try {
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Egresos/filtro");		
			JsfBase.setFlashAttribute("idEgreso",((Entity)this.attrs.get("seleccionado")).getKey());
			regresar= "importar".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doImportar
	
	public StreamedContent getFile() {
		StreamedContent regresar= null;		
		Entity seleccionado     = null;				
		try {			
			seleccionado= (Entity) this.attrs.get("seleccionado");						
			regresar= this.toZipFile(toAllFiles(seleccionado), seleccionado.toString("descripcion"), seleccionado.getKey());
		} // try 
		catch (Exception e) {
			Error.mensaje(e);
		} // catch		
    return regresar;		
	} // doDescargaArchivos
	
	private List<ZipEgreso> toAllFiles(Entity seleccionado) throws Exception{
		List<ZipEgreso> regresar  = null;
		List<String> namesFiles   = null;
		Map<String, Object> params= null;		
		List<Nombres> list        = null;
		ZipEgreso pivote          = null;
		try {			
			regresar= new ArrayList<>();
			params= new HashMap<>();
			params.put("idEgreso", seleccionado.getKey());
			for(ECuentasEgresos cuentaEgreso: ECuentasEgresos.values()){
				list= (List<Nombres>)DaoFactory.getInstance().toEntitySet(Nombres.class, "VistaEgresosDto", cuentaEgreso.getIdXml(), params);
				if(!list.isEmpty()){
					pivote= new ZipEgreso();
					pivote.setCarpeta(cuentaEgreso.getTitle());
					namesFiles= new ArrayList<>();
					for(Nombres file: list)
						namesFiles.add(file.getAlias());
					pivote.setFiles(namesFiles);
					regresar.add(pivote);
				} // if
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toAllFiles
	
	private StreamedContent toZipFile(List<ZipEgreso> files, String descripcion, Long idEgreso) {
		String zipName                 = null;		
		String name                    = null;		
		InputStream stream             = null;
		String temporal                = null;
		DefaultStreamedContent regresar= null;
		try {
			temporal= Archivo.toFormatNameFile("EGRESO_").concat(Cadena.reemplazarCaracter(descripcion, ' ', '_').toUpperCase());
			Zip zip= new Zip();			
			name= "/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.ZIP.name()).concat("/").concat(temporal));
			zipName= name.concat(".").concat(EFormatos.ZIP.name().toLowerCase());
			zip.setDebug(true);
			zip.setEliminar(false);
			for(ZipEgreso zipEgreso: files)
				zipEgreso.setCarpeta(JsfBase.getRealPath(name).concat("/").concat(zipEgreso.getCarpeta()));			
			zip.compactar(JsfBase.getRealPath(zipName), files, loadNotas(idEgreso));
  	  stream = new FileInputStream(new File(JsfBase.getRealPath(zipName)));
			regresar= new DefaultStreamedContent(stream, EFormatos.ZIP.getContent(), temporal.concat(".").concat(EFormatos.ZIP.name().toLowerCase()));		
		} // try // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	} // toZipFile
	
	private List<String> loadNotas(Long idEgreso) throws Exception{
		List<String> regresar    = null;
		List<Entity> notas       = null;
		Map<String, Object>params= null;
		try {
			regresar= new ArrayList<>();
			params= new HashMap<>();
			params.put("idEgreso", idEgreso);
			notas= DaoFactory.getInstance().toEntitySet("VistaEgresosDto", "notas", params, Constantes.SQL_TODOS_REGISTROS);
			if(!notas.isEmpty()){
				regresar.add("Comentario, Registro, Usuario ".concat("\n"));
				for(Entity nota: notas)
					regresar.add(nota.toString("comentario").concat(", ").concat(nota.toString("registro")).concat(", ").concat(nota.toString("persona")).concat("\n"));				
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadNotas
	
	public void doLoadEstatus(){
		Entity seleccionado          = null;
		Map<String, Object>params    = null;
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_egreso_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticEgresosEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("allEstatus", allEstatus);
			this.attrs.put("estatus", allEstatus.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadEstatus
	
	public void doActualizarEstatus() {
		Transaccion transaccion            = null;
		TcManticEgresosBitacoraDto bitacora= null;
		Entity seleccionado                = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");			
			bitacora= new TcManticEgresosBitacoraDto((String)this.attrs.get("justificacion"), Long.valueOf(this.attrs.get("estatus").toString()), seleccionado.getKey(), JsfBase.getIdUsuario(), -1L);
			transaccion= new Transaccion(bitacora);
			if(transaccion.ejecutar(EAccion.MODIFICAR))
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally {
			this.attrs.put("justificacion", "");
		} // finally
	}	// doActualizaEstatus
}