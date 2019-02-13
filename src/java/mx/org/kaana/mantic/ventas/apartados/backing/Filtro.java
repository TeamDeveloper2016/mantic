package mx.org.kaana.mantic.ventas.apartados.backing;

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
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticApartadosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.apartados.reglas.Transaccion;
import mx.org.kaana.mantic.ventas.comun.IBaseTicket;
import org.primefaces.context.RequestContext;

@Named(value = "manticVentasApartados")
@ViewScoped
public class Filtro extends IBaseTicket implements Serializable {

  private static final long serialVersionUID = 8793578963299428879L;
  private TcManticApartadosDto apartado;
  private Reporte reporte;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("sortOrder", "order by	registro desc");
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				loadSucursales();
      //doLoadEstatus();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {
  	  params = toPrepare();	
      columns= new ArrayList<>();
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_CON_DECIMALES));      
      columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_CON_DECIMALES));   
      columns.add(new Columna("abonado", EFormatoDinamicos.MONEDA_CON_DECIMALES));   
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));    
			this.lazyModel = new FormatCustomLazy("VistaTcManticApartadosDto", "apartados", params, columns);
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
		String search= new String((String)this.attrs.get("cliente"));
    if(!Cadena.isVacio(search)) {
		  search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
  		sb.append("upper(cliente) regexp '.*").append(search).append(".*'");
    }
    if(!Cadena.isVacio(this.attrs.get("dias")))
  		sb.append((!Cadena.isVacio(this.attrs.get("cliente"))?" and ":" ").concat("dias =")).append(this.attrs.get("dias"));
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append(((!Cadena.isVacio(this.attrs.get("cliente"))||!Cadena.isVacio(this.attrs.get("dias")))?" and ":" ").concat("(date_format(registro, '%Y%m%d')>= '")).append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("')");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append(((!Cadena.isVacio(this.attrs.get("cliente"))||!Cadena.isVacio(this.attrs.get("dias")) ||!Cadena.isVacio(this.attrs.get("fechaInicio")))?" and ":" ").concat("(date_format(registro, '%Y%m%d')>= '")).append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("')");	
		if(!Cadena.isVacio(this.attrs.get("vencidos")) && this.attrs.get("vencidos").toString().equals("1"))
  		sb.append(((!Cadena.isVacio(this.attrs.get("cliente"))||!Cadena.isVacio(this.attrs.get("dias"))||!Cadena.isVacio(this.attrs.get("fechaInicio"))||!Cadena.isVacio(this.attrs.get("fechaTermino")))?" and ":" ").concat("(now()> vencimiento)"));
    if(!Cadena.isVacio(this.attrs.get("vencidos")) && this.attrs.get("vencidos").toString().equals("2"))
  		sb.append(((!Cadena.isVacio(this.attrs.get("cliente"))||!Cadena.isVacio(this.attrs.get("dias"))||!Cadena.isVacio(this.attrs.get("fechaInicio"))||!Cadena.isVacio(this.attrs.get("fechaTermino")))?" and ":" ").concat("(now()< vencimiento)"));
    if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb);
		return regresar;		
	}
	
	private void loadSucursales(){
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			this.attrs.put("sucursales", sucursales);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	public String doPago(){
		String regresar    = null;
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
			JsfBase.setFlashAttribute("idApartado", seleccionado.getKey());
			regresar= "abono".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doPago
  
  public void doLoadEstatus(String estatus){
		Map<String, Object>params      = null;
    List<UISelectEntity> allEstatus= null;
		List<Columna> columns          = null;
    Entity seleccionado            = null;
		try {
      seleccionado= (Entity)this.attrs.get("seleccionado");
      columns= new ArrayList<>();
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "nombre = '".concat(estatus).concat("'"));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			allEstatus=(List<UISelectEntity>) UIEntity.build("TcManticApartadosEstatusDto", params, columns);
			this.attrs.put("allEstatus", allEstatus);
      this.attrs.put("estatus", allEstatus.get(0));
      this.attrs.put("mostrarCantidades", (((UISelectEntity)this.attrs.get("estatus")).getKey().equals(4L)));
      if((boolean)this.attrs.get("mostrarCantidades")){
        this.apartado= (TcManticApartadosDto) DaoFactory.getInstance().findById(TcManticApartadosDto.class, seleccionado.getKey());
        this.attrs.put("porcentajeRetenido", 10D);
        this.attrs.put("cantidadRetenida",((this.apartado.getAbonado()*10D)/100D));
        this.attrs.put("importeDevuelto",(this.apartado.getAbonado()-(Double)this.attrs.get("cantidadRetenida")));
        this.attrs.put("disabledCantidades", true);	
        loadBancos();
        loadTiposPagos();
        doLoadCajas();
      }
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
		Transaccion transaccion      = null;
		TcManticApartadosBitacoraDto bitacora= null;
		Entity seleccionado               = null;
		try {	
			seleccionado= (Entity)this.attrs.get("seleccionado");
			bitacora= new TcManticApartadosBitacoraDto(-1L, (String)this.attrs.get("justificacion"), JsfBase.getIdUsuario(), Long.valueOf(this.attrs.get("estatus").toString()), seleccionado.getKey(),Double.valueOf(this.attrs.get("porcentajeRetenido").toString()), Double.valueOf(this.attrs.get("cantidadRetenida").toString()), Double.valueOf(this.attrs.get("importeDevuelto").toString()));
			if(!(boolean)this.attrs.get("disabledCantidades"))
        transaccion= new Transaccion(bitacora, seleccionado.toLong("idVenta"));
      else{
        transaccion= new Transaccion(Double.valueOf(this.attrs.get("importeDevuelto").toString()), ((UISelectEntity)this.attrs.get("caja")).getKey(),this.attrs.get("banco")!=null?((UISelectEntity)this.attrs.get("banco")).getKey():-1L, ((UISelectEntity)this.attrs.get("tipoPago")).getKey(), seleccionado.toLong("idVenta"), (this.attrs.get("referencia")!=null?this.attrs.get("referencia").toString():null), bitacora, JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      }
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
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
  
  public void doActiveTexts(){
		boolean activar= true;
		try {
			activar= (boolean) this.attrs.get("devolucion");		
			this.attrs.put("disabledCantidades", !activar);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doActiveTexts
  
  public void doUpdatePorcentaje(){
		try {
      this.attrs.put("cantidadRetenida",((this.apartado.getAbonado()*Double.valueOf(this.attrs.get("porcentajeRetenido").toString()))/100D));
      this.attrs.put("importeDevuelto",(this.apartado.getAbonado()-Double.valueOf(this.attrs.get("cantidadRetenida").toString())));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doUpdatePorcentaje
  
  public void doUpdateCantidad(){
		try {
      this.attrs.put("porcentajeRetenido", (((Double.valueOf(this.attrs.get("cantidadRetenida").toString())*100D)/this.apartado.getAbonado())));
      this.attrs.put("importeDevuelto",(this.apartado.getAbonado()-Double.valueOf(this.attrs.get("cantidadRetenida").toString())));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doUpdateCanticad
  
  private void loadTiposPagos(){
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cobro_caja=1");
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposPagos", tiposPagos);
			this.attrs.put("tipoPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposPagos
  
  public void doValidaTipoPago(){
		Long tipoPago= -1L;
		try {
			tipoPago= Long.valueOf(this.attrs.get("tipoPago").toString());
			this.attrs.put("mostrarBanco", !ETipoMediosPago.EFECTIVO.getIdTipoMedioPago().equals(tipoPago));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doValidaTipoPago
  
  private void loadBancos(){
		List<UISelectEntity> bancos= null;
		Map<String, Object> params = null;
		List<Columna> campos       = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos= new ArrayList<>();
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			bancos= UIEntity.build("TcManticBancosDto", "row", params, campos, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("bancos", bancos);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadBancos
  
  	public void doLoadCajas(){
		List<UISelectEntity> cajas= null;
		Map<String, Object>params = null;
		List<Columna> columns     = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("idEmpresa",JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			cajas=(List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "cajas", params, columns);
			this.attrs.put("cajas", cajas);
			this.attrs.put("caja", cajas.get(0));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
	} // loadCajas
    
    public void doReporte(String nombre) throws Exception{
    Parametros comunes = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
		try{		
      params= toPrepare();
      seleccionado = ((Entity)this.attrs.get("seleccionado"));
      params.put("sortOrder", "order by	registro desc");
      reporteSeleccion= EReportes.valueOf(nombre);
      if(reporteSeleccion.equals(EReportes.APARTADO_DETALLE)){
        params.put("idApartado", seleccionado.toLong("idKey"));
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), -1L, -1L , seleccionado.toLong("idCliente"));
      }
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
      doVerificarReporte();
      this.reporte.doAceptar();			
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // doReporte
  
  public void doVerificarReporte() {
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L)
			rc.execute("start(" + this.reporte.getTotal() + ")");		
		else{
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
		} // else
	} // doVerificarReporte	

}