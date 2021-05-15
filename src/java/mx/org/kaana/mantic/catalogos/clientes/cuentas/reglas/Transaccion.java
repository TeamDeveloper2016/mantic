package mx.org.kaana.mantic.catalogos.clientes.cuentas.reglas;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosControlesDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EEstatusClientes;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends TransaccionFactura{

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
	private List<Entity> cuentas;
	private String messageError;
	private TcManticClientesPagosDto pago;
	private Long idCliente;
	private Long idCaja;
	private Long idEmpresa;
	private Long idCierreActivo;
	private Long idBanco;
	private String referencia;
	private Double pagoGeneral;
	private boolean saldar;
	private TcManticClientesDeudasDto clienteDeuda;
	private Long idClientePago;
	private Importado file;
	private TcManticClientesPagosControlesDto control;
	private Date limite;

	public Transaccion(TcManticClientesPagosDto pago, Long idCaja, Long idEmpresa, Long idBanco, String referencia, boolean saldar) {
		this(pago, idCaja, -1L, idEmpresa, idBanco, referencia, saldar);
	} // Transaccion
	
	public Transaccion(TcManticClientesPagosDto pago, Long idCaja, Long idCliente, Long idEmpresa, Long idBanco, String referencia, boolean saldar) {
		this(pago, idCaja, idCliente, idEmpresa, idBanco, referencia, null, saldar);
	}
	
	public Transaccion(TcManticClientesPagosDto pago, Long idCaja, Long idCliente, Long idEmpresa, Long idBanco, String referencia, List<Entity> cuentas, boolean saldar) {
		this.pago      = pago;
		this.idCliente = idCliente;
		this.idCaja    = idCaja;
		this.idEmpresa = idEmpresa;
		this.idBanco   = idBanco;
		this.referencia= referencia;
		this.cuentas   = cuentas;
		this.saldar    = saldar;
	} // Transaccion
	
	public Transaccion(Importado file, TcManticClientesDeudasDto clienteDeuda, Long idClientePago) {
		this.file         = file;
		this.clienteDeuda = clienteDeuda;
		this.idClientePago= idClientePago;
	} // Transaccion

	public Transaccion(Long idClientePago, String referencia, Date limite) {
    this.idClientePago= idClientePago;
    this.referencia   = referencia;
    this.limite       = limite;
  }
  
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
    try {			
			if(this.pago!= null)
				this.pagoGeneral= this.pago.getPago();
      switch (accion) {
        case AGREGAR:					
						regresar= this.procesarPago(sesion);
          break;       
        case PROCESAR:					
						regresar= this.procesarPagoGeneral(sesion);
          break;       
				case COMPLEMENTAR: 
					regresar= this.procesarPagoSegmento(sesion);
					break;
				case SUBIR:
					regresar= true;
					this.toUpdateDeleteFilePago(sesion);
					break;
				case ELIMINAR:
					regresar= this.toDeletePagos(sesion);
					break;
				case DEPURAR:
					regresar= this.toDeleteCuenta(sesion);
					break;
				case MODIFICAR:
					regresar= this.toUpdateVencimiento(sesion);
					break;
      } // switch
      if (!regresar) 
        throw new Exception("");      
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
	} // ejecutar
	
	private boolean procesarPago(Session sesion) throws Exception {
		boolean regresar               = false;
		TcManticClientesDeudasDto deuda= null;
		Double saldo                   = 0D;
		Siguiente orden                = null;
		try {
			if(this.toCierreCaja(sesion, this.pago.getPago())) {
        // INSERTAR EN LA TABLA DE CONTROL DE PAGOS CON EL ESTATUS DE ACTIVO PARA PODER CANCELAR UN PAGO
        this.control= new TcManticClientesPagosControlesDto(
          -1L, // Long idClientePagoControl, 
          1L, // Long idActivo, 
          "INDIVIDUAL", // String tipo, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          this.pago.getPago(), // Double pago
          this.pago.getObservaciones() // String Observaciones
        );
        DaoFactory.getInstance().insert(sesion, this.control);
        this.pago.setIdClientePagoControl(this.control.getIdClientePagoControl());
        
				this.pago.setIdCierre(this.idCierreActivo);				
				if(!this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
					this.pago.setReferencia(this.referencia);
					this.pago.setIdBanco(this.idBanco);
				} // if
				orden= this.toSiguiente(sesion, this.idCliente);
				this.pago.setOrden(orden.getOrden());
				this.pago.setConsecutivo(orden.getConsecutivo());
				this.pago.setEjercicio(new Long(Fecha.getAnioActual()));
				if(DaoFactory.getInstance().insert(sesion, this.pago)>= 1L) {
					deuda= (TcManticClientesDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDeudasDto.class, this.pago.getIdClienteDeuda());
          this.pago.setComentarios(
            "PAGO INDIVIDUAL $".concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, this.pago.getPago())).concat(
            " [").concat(Global.format(EFormatoDinamicos.FECHA_HORA, this.pago.getRegistro())).concat(
            "] DEUDA $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, deuda.getImporte())).concat(
            "] SALDO $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, deuda.getSaldo())).concat(
            " NUEVO SALDO $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.redondearSat(deuda.getSaldo()- this.pago.getPago()))));
					saldo= deuda.getSaldo()- this.pago.getPago();
					deuda.setSaldo(saldo);
					deuda.setIdClienteEstatus(this.saldar? EEstatusClientes.SALDADA.getIdEstatus(): saldo.equals(0D)? EEstatusClientes.FINALIZADA.getIdEstatus(): EEstatusClientes.PARCIALIZADA.getIdEstatus());
					regresar= DaoFactory.getInstance().update(sesion, deuda)>= 1L;
          TcManticClientesBitacoraDto bitacora= new TcManticClientesBitacoraDto(
            -1L, // Long idClienteBitacora, 
            deuda.getIdClienteEstatus(), // Long idClienteEstatus, 
            "SE REGISTRO UN PAGO [".concat(String.valueOf(this.pago.getPago())).concat("]"), // String justificacion, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            deuda.getIdClienteDeuda() // Long idClienteDeuda
          );
          DaoFactory.getInstance().insert(sesion, bitacora);
					this.actualizarSaldoCatalogoCliente(sesion, deuda.getIdCliente(), this.pago.getPago(), false);
          DaoFactory.getInstance().update(sesion, this.pago);
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally{
			this.messageError= "Error al registrar el pago";
		} // finally
		return regresar;
	} // procesarPago
	
	private boolean procesarPagoGeneral(Session sesion) throws Exception {		
		boolean regresar         = true;
		List<Entity> deudas      = null;		
		Map<String, Object>params= null;
		Double saldo             = 1D;
		Double saldoDeuda        = 0D;				
		Double pagoParcial       = 0D;				
		Double abono             = 0D;		
		Long idEstatus           = -1L;
		try {
			deudas= this.toDeudas(sesion);
      // INSERTAR EN LA TABLA DE CONTROL DE PAGOS CON EL ESTATUS DE ACTIVO PARA PODER CANCELAR UN PAGO
      this.control= new TcManticClientesPagosControlesDto(
        -1L, // Long idClientePagoControl, 
        1L, // Long idActivo, 
        "GENERAL", // String tipo, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        this.pago.getPago(), // Double pago
        this.pago.getObservaciones() // String Observaciones
      );
      DaoFactory.getInstance().insert(sesion, this.control);
			for(Entity deuda: deudas) {
				if(saldo> 0) {	
					saldoDeuda= Numero.toRedondear(deuda.toDouble("saldo"));
					if(saldoDeuda< this.pago.getPago()){
						pagoParcial= saldoDeuda;
						saldo= this.pago.getPago() - saldoDeuda;						
						this.pago.setPago(saldo);
						abono= 0D;
						idEstatus= EEstatusClientes.FINALIZADA.getIdEstatus();
					} // if
          else {		
						pagoParcial= this.pago.getPago();
						saldo= 0D;
						abono= saldoDeuda- this.pago.getPago();
						idEstatus= this.saldar? EEstatusClientes.SALDADA.getIdEstatus(): (saldoDeuda.equals(this.pago.getPago())? EEstatusClientes.FINALIZADA.getIdEstatus() : EEstatusClientes.PARCIALIZADA.getIdEstatus());
					} /// else
					if(this.registrarPago(sesion, deuda, pagoParcial, this.control.getIdClientePagoControl())) {
						params= new HashMap<>();
						params.put("saldo", abono);
						params.put("idClienteEstatus", idEstatus);
						DaoFactory.getInstance().update(sesion, TcManticClientesDeudasDto.class, deuda.getKey(), params);
            TcManticClientesBitacoraDto bitacora= new TcManticClientesBitacoraDto(
              -1L, // Long idClienteBitacora, 
              idEstatus, // Long idClienteEstatus, 
              "SE REGISTRO UN PAGO [".concat(String.valueOf(pagoParcial)).concat("]"), // String justificacion, 
              JsfBase.getIdUsuario(), // Long idUsuario, 
              deuda.getKey() // Long idClienteDeuda
            );
            DaoFactory.getInstance().insert(sesion, bitacora);
						this.actualizarSaldoCatalogoCliente(sesion, this.idCliente, pagoParcial, false);
					}	// if				
				} // if
			} // for
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			this.messageError= "Error al registrar el pago";
			Methods.clean(params);			
		} // finally
		return regresar;
	} // procesarPagoGeneral
	
	private boolean registrarPago(Session sesion, Entity deuda, Double pagoParcial, Long idClientePagoControl) throws Exception {
		TcManticClientesPagosDto registroPago= null;
		boolean regresar                     = false;
		Siguiente orden	                     = null;
		try {
      Long idClienteDeuda= deuda.getKey();
			if(this.toCierreCaja(sesion, pagoParcial)) {
				registroPago= new TcManticClientesPagosDto();
        registroPago.setIdClientePagoControl(idClientePagoControl);
				registroPago.setIdClienteDeuda(idClienteDeuda);
				registroPago.setIdUsuario(JsfBase.getIdUsuario()); 
				registroPago.setComentarios(
          "PAGO GENERAL $".concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, this.pagoGeneral)).concat(
          " [").concat(Global.format(EFormatoDinamicos.FECHA_HORA, registroPago.getRegistro())).concat(
          "] DEUDA $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, deuda.toDouble("importe"))).concat(
          " SALDO $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, deuda.toDouble("saldo"))).concat(
          " APLICADO $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, pagoParcial)).concat(
          " NUEVO SALDO $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.redondearSat(deuda.toDouble("saldo")- pagoParcial))));
				registroPago.setPago(pagoParcial);
				registroPago.setIdTipoMedioPago(this.pago.getIdTipoMedioPago());
				registroPago.setIdCierre(this.idCierreActivo);
				if(!this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())) {
					registroPago.setIdBanco(this.idBanco);
					registroPago.setReferencia(this.referencia);
				} // if
				orden= this.toSiguiente(sesion, this.idCliente);
				registroPago.setOrden(orden.getOrden());
				registroPago.setConsecutivo(orden.getConsecutivo());
				registroPago.setEjercicio(new Long(Fecha.getAnioActual()));
				regresar= DaoFactory.getInstance().insert(sesion, registroPago)>= 1L;
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarPago
	
	private List<Entity> toDeudas(Session sesion) throws Exception {
		List<Entity> regresar    = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.idCliente);
			params.put(Constantes.SQL_CONDICION, " tc_mantic_clientes_deudas.saldo> 0 and tc_mantic_clientes_deudas.id_cliente_estatus in(1, 2)");			
			params.put("sortOrder", "order by dias desc");
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "VistaClientesDto", "cuentas", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDeudas	
	
	private boolean toCierreCaja(Session sesion, Double pago) throws Exception {
		mx.org.kaana.mantic.ventas.caja.reglas.Transaccion cierre= null;
		VentaFinalizada datosCierre= null;
		boolean regresar= false;
		try {
			datosCierre= new VentaFinalizada();
			datosCierre.getTicketVenta().setIdEmpresa(this.idEmpresa);
			datosCierre.setIdCaja(this.idCaja);
			datosCierre.getTotales().setEfectivo(pago);
			cierre= new mx.org.kaana.mantic.ventas.caja.reglas.Transaccion(datosCierre);
			if(cierre.verificarCierreCaja(sesion)){
				this.idCierreActivo= cierre.getIdCierreVigente();
				regresar= cierre.alterarCierreCaja(sesion, this.pago.getIdTipoMedioPago());
			} // if
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // toCierreCaja
	
	private boolean procesarPagoSegmento(Session sesion) throws Exception {		
		boolean regresar         = true;
		List<Entity> deudas      = null;		
		Map<String, Object>params= null;
		Double saldo             = 1D;
		Double saldoDeuda        = 0D;				
		Double pagoParcial       = 0D;				
		Double abono             = 0D;		
		Long idEstatus           = -1L;
		try {
			deudas= this.toDeudas(sesion);
      // INSERTAR EN LA TABLA DE CONTROL DE PAGOS CON EL ESTATUS DE ACTIVO PARA PODER CANCELAR UN PAGO
      this.control= new TcManticClientesPagosControlesDto(
        -1L, // Long idClientePagoControl, 
        1L, // Long idActivo, 
        "SEGMENTO", // String tipo, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        this.pago.getPago(), // Double pago
        this.pago.getObservaciones() // String Observaciones
      );
      DaoFactory.getInstance().insert(sesion, this.control);
			for(Entity deuda: deudas) {
				for(Entity cuenta: this.cuentas) {
					if(deuda.getKey().equals(cuenta.getKey())) {
						if(saldo> 0) {					
							saldoDeuda= Numero.toRedondearSat(deuda.toDouble("saldo"));
							if(saldoDeuda< this.pago.getPago()){
								pagoParcial= saldoDeuda;
								saldo= this.pago.getPago() - saldoDeuda;						
								this.pago.setPago(saldo);
								abono= 0D;
								idEstatus= EEstatusClientes.FINALIZADA.getIdEstatus();
							} // if
              else {						
								pagoParcial= this.pago.getPago();
								saldo= 0D;
								abono= saldoDeuda- this.pago.getPago();
								idEstatus= this.saldar? EEstatusClientes.SALDADA.getIdEstatus(): (saldoDeuda.equals(this.pago.getPago())? EEstatusClientes.FINALIZADA.getIdEstatus(): EEstatusClientes.PARCIALIZADA.getIdEstatus());
							} /// else
							if(this.registrarPago(sesion, deuda, pagoParcial, this.control.getIdClientePagoControl())){
								params= new HashMap<>();
								params.put("saldo", abono);
								params.put("idClienteEstatus", idEstatus);
								DaoFactory.getInstance().update(sesion, TcManticClientesDeudasDto.class, deuda.getKey(), params);
                TcManticClientesBitacoraDto bitacora= new TcManticClientesBitacoraDto(
                  -1L, // Long idClienteBitacora, 
                  idEstatus, // Long idClienteEstatus, 
                  "SE REGISTRO UN PAGO [".concat(String.valueOf(pagoParcial)).concat("]"), // String justificacion, 
                  JsfBase.getIdUsuario(), // Long idUsuario, 
                  deuda.getKey() // Long idClienteDeuda
                );
                DaoFactory.getInstance().insert(sesion, bitacora);
								this.actualizarSaldoCatalogoCliente(sesion, this.idCliente, pagoParcial, false);
							}	// if				
						} // if
						else 
              if (this.saldar) {
                if(this.registrarPago(sesion, deuda, 0D, this.control.getIdClientePagoControl())) {
                  params= new HashMap<>();
                  params.put("saldo", 0);
                  params.put("idClienteEstatus", EEstatusClientes.SALDADA.getIdEstatus());
                  DaoFactory.getInstance().update(sesion, TcManticClientesDeudasDto.class, deuda.getKey(), params);
                  TcManticClientesBitacoraDto bitacora= new TcManticClientesBitacoraDto(
                    -1L, // Long idClienteBitacora, 
                    EEstatusClientes.FINALIZADA.getIdEstatus(), // Long idClienteEstatus, 
                    null, // String justificacion, 
                    JsfBase.getIdUsuario(), // Long idUsuario, 
                    deuda.getKey() // Long idClienteDeuda
                  );
                  DaoFactory.getInstance().insert(sesion, bitacora);
                }	// if				
              } // if
					} // if
				} // for
			} // for
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			this.messageError= "Error al registrar el pago";
			Methods.clean(params);			
		} // finally
		return regresar;
	} // procesarPagoGeneral
	
	protected void toUpdateDeleteFilePago(Session sesion) throws Exception {
		TcManticClientesPagosArchivosDto tmp= null;
		if(this.clienteDeuda.getIdClienteDeuda()!= -1L) {			
			if(this.file!= null) {
				tmp= new TcManticClientesPagosArchivosDto(
					this.file.getRuta(),
					this.file.getFileSize(),
					JsfBase.getIdUsuario(),
					2L,
					1L,
					this.file.getObservaciones(),
					this.idClientePago,	
					Configuracion.getInstance().getPropiedadSistemaServidor("cobros").concat(this.file.getRuta()).concat(this.file.getName()),
					-1L,																				
					this.file.getName()					
				);
				TcManticClientesPagosArchivosDto exists= (TcManticClientesPagosArchivosDto)DaoFactory.getInstance().toEntity(TcManticClientesPagosArchivosDto.class, "TcManticClientesPagosArchivosDto", "identically", tmp.toMap());
				File reference= new File(tmp.getAlias());
				if(exists== null && reference.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticClientesPagosArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else {
					if(exists!= null)
						DaoFactory.getInstance().delete(sesion, exists);
					DaoFactory.getInstance().insert(sesion, tmp);
				} // else
				sesion.flush();
				toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("cobros").concat(this.file.getRuta()), ".".concat(this.file.getFormat().name()), this.toListFile(sesion, this.file, 2L));
			} // if	
  	} // if	
	} // toUpdateDeleteXml
	
	private void toDeleteAll(String path, String type, List<Nombres> listado) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(path), type.toLowerCase());
    if(fileSearch.getResult().size()> 0){
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
				if(listado.indexOf(new Nombres(name))< 0) {
				  File file= new File(matched);
				  file.delete();
				} // if
      } // for
		} // if
	} // toDeleteAll
	
	private List<Nombres> toListFile(Session sesion, Importado tmp, Long idTipoArchivo) throws Exception {
		List<Nombres> regresar= null;
		Map<String, Object> params=null;
		try {
			params  = new HashMap<>();
			params.put("idTipoArchivo", idTipoArchivo);
			params.put("ruta", tmp.getRuta());
			regresar= (List<Nombres>)DaoFactory.getInstance().toEntitySet(sesion, Nombres.class, "TcManticClientesArchivosDto", "listado", params);
			regresar.add(new Nombres(tmp.getName()));
		} // try  // try 
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toListFile
	
	private Siguiente toSiguiente(Session sesion, Long idCliente) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", ((TcManticClientesDto)DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, idCliente)).getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "VistaTcManticClientesPagosDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente
  
  private Boolean toDeletePagos(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idClientePagoControl", this.idClientePago);      
      List<Entity> items = (List<Entity>)DaoFactory.getInstance().toEntitySet(sesion, "VistaClientesDto", "eliminarPagos", params);
      if(items!= null && !items.isEmpty()) {
        TcManticClientesDto cliente= (TcManticClientesDto)DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, items.get(0).toLong("idCliente"));
        for (Entity item: items) {
          cliente.setSaldo(Numero.toRedondearSat(cliente.getSaldo()+ item.toDouble("abonado")));
          // borrar todos los archivos asociados a los pagos 
          List<TcManticClientesPagosArchivosDto> archivos= (List<TcManticClientesPagosArchivosDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticClientesPagosArchivosDto.class, "TcManticClientesPagosArchivosDto", "pago", item.toMap());
          if(archivos!= null && !archivos.isEmpty()) {
            for (TcManticClientesPagosArchivosDto archivo: archivos) {
              File evidencia= new File(archivo.getAlias());
              if(evidencia.exists())
                evidencia.delete();
            } // for
            DaoFactory.getInstance().deleteAll(sesion, TcManticClientesPagosArchivosDto.class, item.toMap());
          } // if  
          DaoFactory.getInstance().delete(sesion, TcManticClientesPagosDto.class, item.toLong("idClientePago"));
          TcManticClientesDeudasDto deuda= (TcManticClientesDeudasDto)DaoFactory.getInstance().findById(sesion, TcManticClientesDeudasDto.class, item.toLong("idClienteDeuda"));
          deuda.setSaldo(Numero.toRedondearSat(deuda.getSaldo()+ item.toDouble("abonado")));
          if(Objects.equals(deuda.getImporte(), deuda.getSaldo()))
            deuda.setIdClienteEstatus(EEstatusClientes.INICIADA.getIdEstatus());
          else
            if(!Objects.equals(deuda.getImporte(), deuda.getSaldo()))
              deuda.setIdClienteEstatus(EEstatusClientes.PARCIALIZADA.getIdEstatus());
          DaoFactory.getInstance().update(sesion, deuda);
          TcManticClientesBitacoraDto bitacora= new TcManticClientesBitacoraDto(
            -1L, // Long idClienteBitacora, 
            deuda.getIdClienteEstatus(), // Long idClienteEstatus, 
            this.referencia.concat(", SE ELIMINO EL PAGO [").concat(String.valueOf(item.toDouble("abonado"))).concat("]"), // String justificacion, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            deuda.getIdClienteDeuda() // Long idClienteDeuda
          );
          DaoFactory.getInstance().insert(sesion, bitacora);
        } // for
        DaoFactory.getInstance().delete(sesion, TcManticClientesPagosControlesDto.class, this.idClientePago);
        DaoFactory.getInstance().update(sesion, cliente);
      } // if
      regresar= Boolean.TRUE;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
 
  private Boolean toDeleteCuenta(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idClienteDeuda", this.idClientePago);      
      TcManticClientesDeudasDto deuda= (TcManticClientesDeudasDto)DaoFactory.getInstance().findById(sesion, TcManticClientesDeudasDto.class, this.idClientePago);
      deuda.setIdClienteEstatus(EEstatusClientes.CANCELADA.getIdEstatus());
      DaoFactory.getInstance().update(sesion, deuda);
      TcManticClientesBitacoraDto bitacora= new TcManticClientesBitacoraDto(
        -1L, // Long idClienteBitacora, 
        deuda.getIdClienteEstatus(), // Long idClienteEstatus, 
        this.referencia, // String justificacion, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        deuda.getIdClienteDeuda() // Long idClienteDeuda
      );
      DaoFactory.getInstance().insert(sesion, bitacora);
      TcManticClientesDto cliente= (TcManticClientesDto)DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, deuda.getIdCliente());
      cliente.setSaldo(Numero.toRedondearSat(cliente.getSaldo()- deuda.getImporte()));
      DaoFactory.getInstance().update(sesion, cliente);
      TcManticVentasDto venta= (TcManticVentasDto)DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, deuda.getIdVenta());
      venta.setIdVentaEstatus(EEstatusVentas.CANCELADA.getIdEstatusVenta());
      DaoFactory.getInstance().update(sesion, venta);
      TcManticVentasBitacoraDto movimiento= new TcManticVentasBitacoraDto(
        -1L, // Long idVentaBitacora, 
        this.referencia.concat(", SE CANCELO LA CUENTA POR COBRAR"), // String justificacion, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        venta.getIdVenta(), // Long idVenta, 
        venta.getIdVentaEstatus(), // Long idVentaEstatus, 
        venta.getCticket(), // Long consecutivo, 
        venta.getTotal() // Double importe
      );
      DaoFactory.getInstance().insert(sesion, movimiento);
      // FALTA AFECTAR LAS EXITENCIAS EN EL ALMACEN RESPECTIVO PORQUE LA VENTA SE CANCELO
      this.toAffectAlmacenes(sesion, venta);
      regresar= Boolean.TRUE;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } 
  
  public void toAffectAlmacenes(Session sesion, TcManticVentasDto venta) throws Exception {
    Map<String, Object> params= null;
    try {      
      Double stock= null;
      params = new HashMap<>();      
      params.put("idVenta", venta.getIdVenta());      
      List<TcManticVentasDetallesDto> items= (List<TcManticVentasDetallesDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticVentasDetallesDto.class, "TcManticVentasDetallesDto", "detalle", params);
      if(items!= null && !items.isEmpty()) {
        for (TcManticVentasDetallesDto item : items) {
          params.put("idAlmacen", venta.getIdAlmacen());
			    params.put("idArticulo", item.getIdArticulo());
          // ACTUALIZAR EL STOCK DEL ALMACEN 
			    TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class,  params, "ubicacion");
          stock= ubicacion.getStock();
          ubicacion.setStock(Numero.toRedondearSat(ubicacion.getStock()+ item.getCantidad()));
				  DaoFactory.getInstance().update(sesion, ubicacion);
          // generar un registro en la bitacora de movimientos de los articulos 
          TcManticMovimientosDto entrada= new TcManticMovimientosDto(
            venta.getTicket(), // String consecutivo, 
            3L, // Long idTipoMovimiento, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            venta.getIdAlmacen(), // Long idAlmacen, 
            -1L, // Long idMovimiento, 
            item.getCantidad(), // Double cantidad, 
            item.getIdArticulo(), // Long idArticulo, 
            stock, // Double stock, 
            ubicacion.getStock(), // Double calculo
            "FUE UN ERROR DE CAPTURA" // String observaciones
          );
          DaoFactory.getInstance().insert(sesion, entrada);
          // ACTUALIZAR EL STOCK DEL INVENTARIO DEL ARTICULO POR ALMACEN
    			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().findFirst(sesion, TcManticInventariosDto.class, "inventario", params);
          inventario.setEntradas(inventario.getEntradas()+ item.getCantidad()+ (inventario.getStock()< 0D? 0D: Math.abs(inventario.getStock())));
          inventario.setStock((inventario.getStock()< 0D? 0D: inventario.getStock())+ item.getCantidad());
          DaoFactory.getInstance().update(sesion, inventario);
          // ACTUALIZAR EL STOCK GLOBAL DEL ARTICULO
     			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
          global.setActualizado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          global.setStock(global.getStock()+ item.getCantidad());
          DaoFactory.getInstance().update(sesion, global);
        } // for
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }

  private boolean toUpdateVencimiento(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idClienteDeuda", this.idClientePago);      
      TcManticClientesDeudasDto deuda= (TcManticClientesDeudasDto)DaoFactory.getInstance().findById(sesion, TcManticClientesDeudasDto.class, this.idClientePago);
      deuda.setLimite(this.limite);
      regresar= DaoFactory.getInstance().update(sesion, deuda)> 0L;  
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

}