package mx.org.kaana.mantic.ventas.apartados.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.apartados.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticApartadosPagosDto;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.comun.IBaseTicket;

@Named(value = "manticVentasApartadosAbono")
@ViewScoped
public class Abono extends IBaseTicket implements Serializable {

  private static final long serialVersionUID = 1458632741599428879L;	
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
      this.attrs.put("redireccionar", "false");
      this.attrs.put("idApartado", JsfBase.getFlashAttribute("idApartado"));     
      this.attrs.put("regreso", JsfBase.getFlashAttribute("regreso"));     
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("isMatriz", JsfBase.isAdminEncuestaOrAdmin());
			this.attrs.put("mostrarBanco", false);
      this.attrs.put("resta", "$ 0.00");
			if(JsfBase.isAdminEncuestaOrAdmin())
				this.loadSucursales();							
			this.doLoadCajas();
			this.loadBancos();
			this.loadTiposPagos();
			this.loadApartado();
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadBancos() {
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
	
	private void loadSucursales() {
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
			this.attrs.put("idEmpresa", sucursales.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	public void doLoadCajas() {
		List<UISelectEntity> cajas= null;
		Map<String, Object>params = null;
		List<Columna> columns     = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
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
	
	private void loadApartado() throws Exception {
		Entity apartado          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idApartado", this.attrs.get("idApartado"));			
			params.put("sortOrder", " order by	registro desc");
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			apartado= (Entity) DaoFactory.getInstance().toEntity("VistaApartadosDto", "apartado", params);
			this.attrs.put("apartado", apartado);
      if(Objects.equals(1L, apartado.toLong("idApartadoEstatus")) || Objects.equals(2L, apartado.toLong("idApartadoEstatus"))) {
        UIBackingUtilities.execute("janal.bloquear();PF('dlgPago').show();");
        this.doLoadTopePago();
      } // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	} // loadApartado
	
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
	  Map<String, Object> params= null;	
    try {  	  
			params= new HashMap<>();
			params.put("idApartado", this.attrs.get("idApartado"));			
      columns= new ArrayList<>();  
			columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			columns.add(new Columna("pago", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			this.lazyModel = new FormatCustomLazy("VistaApartadosDto", "pagosApartado", params, columns);
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

	public String doRegresar() {	  
		return "CAJERO".equals(JsfBase.getAutentifica().getPersona().getDescripcionPerfil()) ? "/Paginas/Mantic/Ventas/Caja/accion".concat(Constantes.REDIRECIONAR): "filtro".concat(Constantes.REDIRECIONAR);
	} // doRegresar
	
	public String doCancelar() {	  
		return "/Paginas/Mantic/Ventas/Caja/accion".concat(Constantes.REDIRECIONAR);
	} // doCancelar
	
	public void doRegistrarPago() {
		Transaccion transaccion       = null;
		TcManticApartadosPagosDto pago= null;
		boolean tipoPago              = false;
		try {
			if(validaPago()) {
				pago= new TcManticApartadosPagosDto();
				pago.setIdApartado(Long.valueOf(this.attrs.get("idApartado").toString()));
				pago.setIdUsuario(JsfBase.getIdUsuario());
				pago.setObservaciones((String)this.attrs.get("observaciones"));
				pago.setPago((Double)this.attrs.get("pago"));
				pago.setIdTipoMedioPago(Long.valueOf(this.attrs.get("tipoPago").toString()));
				tipoPago= pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
				transaccion= new Transaccion(pago, Long.valueOf(this.attrs.get("caja").toString()), Long.valueOf(this.attrs.get("idEmpresa").toString()), tipoPago ? -1 : Long.valueOf(this.attrs.get("banco").toString()), tipoPago ? "" : this.attrs.get("referencia").toString());
				if(transaccion.ejecutar(EAccion.AGREGAR)) {
					JsfBase.addMessage("Registrar pago", "Se registro el pago de forma correcta", ETipoMensaje.INFORMACION);
          Entity seleccionado= ((Entity)this.attrs.get("apartado")).clone();
          this.attrs.put("seleccionado", seleccionado);
          this.doTicket();
					this.doLoad();
          this.loadApartado();
          this.attrs.put("redireccionar", "true");
				} // if
				else
					JsfBase.addMessage("Registrar pago", "Ocurrió un error al registrar el pago", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registrar pago", "El pago debe ser menor o igual al saldo restante y mayor a 0.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doRegistrarPago
	
	private boolean validaPago() {
		boolean regresar= false;
		Double pago     = 0D;
		Double saldo    = 0D;
		Entity apartado = null;
		try {
			pago= (Double)this.attrs.get("pago");
			if(pago > 0D) {
				apartado= (Entity) this.attrs.get("apartado");
				saldo= Double.valueOf(apartado.toString("saldo"));
				regresar= pago<= saldo;
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch
		return regresar;
	} // validaPago	
	
	private void loadTiposPagos() {
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
	
	public void doValidaTipoPago() {
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
  
  public void doBeforeTicket() {
    Entity seleccionado= ((Entity)this.attrs.get("apartado")).clone();
    this.attrs.put("seleccionado", seleccionado);
    this.doTicket();  
  } // doBeforeTicket
 
  public void doLoadTopePago() {
    Entity seleccionado= (Entity)this.attrs.get("apartado");
    this.attrs.put("pago", seleccionado.toDouble("saldo"));
    this.attrs.put("resta", Numero.formatear(Numero.MONEDA_SAT_DECIMALES, seleccionado.toDouble("saldo")));
    // UIBackingUtilities.execute("janal.renovate('pago_input', {validaciones: 'requerido|flotante|mayor({\"cuanto\":0}|menor-igual({\"cuanto\":"+ seleccionado.toDouble("saldo")+"})', mascara: 'libre'});");
  } // doLoadTopePago
 
  public void doCalculatePago() {
     Entity apartado= (Entity) this.attrs.get("apartado");   
     Double saldo   = Double.valueOf(apartado.toString("saldo"));
     Double pago    = (Double)this.attrs.get("pago");
     this.attrs.put("resta", Numero.formatear(Numero.MONEDA_SAT_DECIMALES, Numero.toRedondearSat(saldo- pago)));
  }
 
  public String doBackCommonPage() {
    return "CAJERO".equals(JsfBase.getAutentifica().getPersona().getDescripcionPerfil()) ? null: "../Caja/accion".concat(Constantes.REDIRECIONAR);
  }
  
}