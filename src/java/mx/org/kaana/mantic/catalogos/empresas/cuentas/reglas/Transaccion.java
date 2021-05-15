package mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas;

import java.io.File;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
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
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosControlesDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.enums.EEstatusEmpresas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	private Long idArchivo;
	private List<Entity> cuentas;
	private List<Entity> notasEntrada;
	private List<Entity> notasCredito;
	private String messageError;
	private TcManticEmpresasPagosDto pago;
	private TcManticEmpresasDeudasDto deuda;
	private Importado xml;
	private Importado pdf;
	private Long idPago;
	private Entity detalle;
	private Date fecha;
	private Long idCaja;
	private Long idEmpresa;
	private Long idProveedor;
	private Long idBanco;
	private String referencia;
	private boolean saldar;
	private Long idCierreActivo;
	private Double pagoGeneral;
	private Long idTipoComprobante;	
	private Long idEmpresaPago;
	private TcManticEmpresasPagosControlesDto control;
  

	public Transaccion(Long idArchivo) {
		this.idArchivo= idArchivo;
	} // Transaccion
	
	public Transaccion(TcManticEmpresasPagosDto pago) {
		this(pago, -1L, -1L, -1L, -1L, null, false);
	} // Transaccion	

	public Transaccion(TcManticEmpresasPagosDto pago, Long idCaja, Long idEmpresa, Long idBanco, String referencia, boolean saldar, List<Entity> notasEntrada, List<Entity> notasCredito) {
		this(pago, idCaja, -1L, idEmpresa, idBanco, referencia, null, saldar, notasEntrada, notasCredito);
	}
	
	public Transaccion(TcManticEmpresasPagosDto pago, Long idCaja, Long idProveedor, Long idEmpresa, Long idBanco, String referencia, boolean saldar) {
		this(pago, idCaja, idProveedor, idEmpresa, idBanco, referencia, null, saldar);
	}
	
	public Transaccion(TcManticEmpresasPagosDto pago, Long idCaja, Long idProveedor, Long idEmpresa, Long idBanco, String referencia, List<Entity> cuentas, boolean saldar) {
		this(pago, idCaja, idProveedor, idEmpresa, idBanco, referencia, cuentas, saldar, null, null);
	}
	
	public Transaccion(TcManticEmpresasPagosDto pago, Long idCaja, Long idProveedor, Long idEmpresa, Long idBanco, String referencia, List<Entity> cuentas, boolean saldar, List<Entity> notasEntrada, List<Entity> notasCredito) {
		this.pago        = pago;
		this.idCaja      = idCaja;
		this.idEmpresa   = idEmpresa;
		this.idBanco     = idBanco;
		this.referencia  = referencia;
		this.saldar      = saldar;
		this.idProveedor = idProveedor;
		this.cuentas     = cuentas;
		this.notasEntrada= notasEntrada;
		this.notasCredito= notasCredito;
	} // Transaccion
	
	public Transaccion(TcManticEmpresasDeudasDto deuda, Importado pdf, Long idPago, Long idTipoComprobante, Date fecha) {
		this(deuda, null, pdf, idPago, idTipoComprobante, fecha);
	}
	
	public Transaccion(TcManticEmpresasDeudasDto deuda, Importado xml, Importado pdf, Long idPago, Long idTipoComprobante, Date fecha) {
		this.deuda = deuda;
		this.pdf   = pdf;
		this.xml   = xml;
		this.idPago= idPago;
		this.idTipoComprobante= idTipoComprobante;
		this.fecha = fecha;
	} // Transaccion

	public Transaccion(Entity detalle, Date fecha) {
		this.detalle= detalle;
		this.fecha  = fecha;
	} // Transaccion	

	public Transaccion(Long idEmpresaPago, String referencia) {
    this.idEmpresaPago= idEmpresaPago;
    this.referencia   = referencia;
  }
  
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar    = false;
		String observaciones= null;
    try {			
			if(this.pago!= null)
				this.pagoGeneral= this.pago.getPago();
      switch (accion) {
        case AGREGAR:
          regresar = this.procesarPago(sesion);
          break;       
				case REGISTRAR:
					regresar= this.toUpdateDeleteXml(sesion);
					break;
				case MODIFICAR:
					regresar= this.actualizarDeuda(sesion);
					break;
				case PROCESAR:
					regresar = this.procesarPagoGeneral(sesion);
					break;
				case COMPLEMENTAR:
					regresar = this.procesarPagoSegmento(sesion);
					break;
				case SUBIR:
					regresar= true;
					this.toUpdateDeleteFilePago(sesion);
					break;
				case ACTIVAR:
					TcManticEmpresasDeudasDto deudaReabrir= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.deuda.getIdEmpresaDeuda());
					deudaReabrir.setIdEmpresaEstatus(EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
					observaciones= Cadena.isVacio(deudaReabrir.getObservaciones()) ? this.deuda.getObservaciones() : deudaReabrir.getObservaciones().concat("; ").concat(this.deuda.getObservaciones());
					deudaReabrir.setObservaciones(observaciones);
					regresar= DaoFactory.getInstance().update(sesion, deudaReabrir)>= 1L;
					break;
				case ELIMINAR:
					regresar= this.eliminarPago(sesion);
				case RESTAURAR:
          regresar= this.toDeletePagos(sesion);
          break;
				case DEPURAR:
					regresar= this.toDeleteCuenta(sesion);
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
		boolean regresar               = true;
		TcManticEmpresasDeudasDto item = null;
		Double saldo                   = 0D;		
		Siguiente orden                = null;
		try {
			if(this.toCierreCaja(sesion, this.pago.getPago())) {			
        // INSERTAR EN LA TABLA DE CONTROL DE PAGOS CON EL ESTATUS DE ACTIVO PARA PODER CANCELAR UN PAGO
        this.control= new TcManticEmpresasPagosControlesDto(
          "INDIVIDUAL", // String tipo, 
          1L, // Long idActivo, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          this.pago.getObservaciones(), // String observaciones, 
          -1L, // Long idEmpresaPagoControl, 
          this.pago.getPago() // Double pago
        );
        DaoFactory.getInstance().insert(sesion, this.control);
        this.pago.setIdEmpresaPagoControl(this.control.getIdEmpresaPagoControl());
        
				this.pago.setIdCierre(this.idCierreActivo);
				if(!this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())) {
					this.pago.setReferencia(this.referencia);
					this.pago.setIdBanco(this.idBanco);
				} // if
				orden= this.toSiguiente(sesion);
				this.pago.setOrden(orden.getOrden());
				this.pago.setConsecutivo(orden.getConsecutivo());
				this.pago.setEjercicio(new Long(Fecha.getAnioActual()));
				if(DaoFactory.getInstance().insert(sesion, this.pago)>= 1L) {
					item = (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.pago.getIdEmpresaDeuda());
          this.pago.setComentarios(
            "PAGO INDIVIDUAL $".concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, this.pago.getPago())).concat(
            " [").concat(Global.format(EFormatoDinamicos.FECHA_HORA, this.pago.getRegistro())).concat(
            "] DEUDA $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, item .getImporte())).concat(
            "] SALDO $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, item .getSaldo())).concat(
            " NUEVO SALDO $").concat(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.redondearSat(item .getSaldo()- this.pago.getPago()))));
					saldo= item .getSaldo()- this.pago.getPago();
					item .setSaldo(saldo);
					item .setIdEmpresaEstatus(saldo>= 0D ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
					regresar= DaoFactory.getInstance().update(sesion, item )>= 1L;
          TcManticEmpresasBitacoraDto bitacora= new TcManticEmpresasBitacoraDto(
            "SE REGISTRO UN PAGO [".concat(String.valueOf(this.pago.getPago())).concat("]"), // String justificacion, 
            item .getIdEmpresaEstatus(), // Long idEmpresaEstatus, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            item .getIdEmpresaDeuda(), // Long idEmpresaDeuda
            -1L // Long idClienteBitacora, 
          );
          DaoFactory.getInstance().insert(sesion, bitacora);
				} // if
			} // if toCierreCaja
			else
				item = (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.pago.getIdEmpresaDeuda());			
			this.procesaNotasEntrada(sesion, item );
			this.procesaNotasCredito(sesion, item );			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally{
			this.messageError= "Error al registrar el pago";
		} // finally
		return regresar;
	} // procesarPago
	
	private void procesaNotasEntrada(Session sesion, TcManticEmpresasDeudasDto deuda) throws Exception {
		Double saldoPivote                 = 0D;
		Double totalPago                   = 0D;
		TcManticEmpresasDeudasDto item     = null;
		TcManticEmpresasPagosDto pagoPivote= null;    
		try {
			if(!this.notasEntrada.isEmpty()) {
				for(Entity notaEntrada: this.notasEntrada) {
					if(deuda.getSaldo()> 0D) {
						item     = (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, notaEntrada.getKey());
						if(item     .getSaldo()> 0D) {
							if(deuda.getSaldo()<= item     .getSaldo()) {																																
								totalPago= deuda.getSaldo();
								deuda.setSaldo(0D);
								deuda.setIdEmpresaEstatus(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());								
								if(DaoFactory.getInstance().update(sesion, deuda)>= 1L) {								
									saldoPivote= item     .getSaldo()- deuda.getSaldo();
									item     .setSaldo(saldoPivote);
									DaoFactory.getInstance().update(sesion, item     );									
								} // if
							} // if
              else {
								totalPago= item     .getSaldo();
								saldoPivote= item     .getSaldo()- deuda.getSaldo();
								deuda.setSaldo(saldoPivote);
								deuda.setIdEmpresaEstatus(saldoPivote >= 0D ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
								if(DaoFactory.getInstance().update(sesion, deuda)>= 1L) {								
									saldoPivote= 0D;
									item     .setSaldo(saldoPivote);
									DaoFactory.getInstance().update(sesion, item     );
								} // if
							} // else
							pagoPivote= new TcManticEmpresasPagosDto(1L, JsfBase.getIdUsuario(), this.pago.getIdEmpresaDeuda(), this.pago.getObservaciones(), -1L, totalPago, null,  null, null, item     .getIdNotaEntrada(), null, new Date(Calendar.getInstance().getTimeInMillis()), null, null);
							DaoFactory.getInstance().insert(sesion, pagoPivote);
						} // if
					} // if
				} // for
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // procesaNotasEntrada
	
	private void procesaNotasCredito(Session sesion, TcManticEmpresasDeudasDto item) throws Exception {
		Double saldoPivote                  = 0D;
		TcManticCreditosNotasDto creditoNota= null;
		TcManticEmpresasPagosDto pagoPivote = null;  
		Double totalPago                    = 0D;
		try { 
			if(!this.notasCredito.isEmpty()) {
				for(Entity notaCredito: this.notasCredito) {
					if(item.getSaldo()> 0D) {
						creditoNota= (TcManticCreditosNotasDto) DaoFactory.getInstance().findById(sesion, TcManticCreditosNotasDto.class, notaCredito.getKey());
						if(creditoNota.getImporte()> 0D) {
							if(item.getSaldo()<= creditoNota.getImporte()) {		
								totalPago= item.getSaldo();
								item.setSaldo(0D);								
								item.setIdEmpresaEstatus(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());
								if(DaoFactory.getInstance().update(sesion, item)>= 1L) {
									saldoPivote= creditoNota.getImporte()- item.getSaldo();
									creditoNota.setSaldo(saldoPivote);
									DaoFactory.getInstance().update(sesion, creditoNota);
								} // if
							} // if
							else{
								totalPago= creditoNota.getImporte();
								saldoPivote= creditoNota.getImporte()- item.getSaldo();
								item.setSaldo(saldoPivote);
								item.setIdEmpresaEstatus(saldoPivote >= 0D ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
								if(DaoFactory.getInstance().update(sesion, item)>= 1L) {								
									saldoPivote= 0D;
									creditoNota.setSaldo(saldoPivote);
									DaoFactory.getInstance().update(sesion, creditoNota);
								} // if
							} // else
							pagoPivote= new TcManticEmpresasPagosDto(1L, JsfBase.getIdUsuario(), this.pago.getIdEmpresaDeuda(), this.pago.getObservaciones(), -1L, totalPago, notaCredito.getKey(),  null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()), null, null);
							DaoFactory.getInstance().insert(sesion, pagoPivote);
						} // if
					} // if
				} // for
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // procesaNotasCredito
	
	protected boolean toUpdateDeleteXml(Session sesion) throws Exception {
		TcManticEmpresasArchivosDto tmp= null;
		if(this.deuda.getIdEmpresaDeuda()!= -1L) {
			if(this.xml!= null) {
				tmp= new TcManticEmpresasArchivosDto(
					this.xml.getRuta(),
					-1L,
					this.xml.getName(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					this.xml.getFileSize(),
					JsfBase.getIdUsuario(),	
					1L,
					1L,
					this.xml.getObservaciones(),
					this.idPago,																					
					Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.xml.getRuta()).concat(this.xml.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
          this.xml.getOriginal(),
					this.idTipoComprobante,
					new java.sql.Date(this.fecha.getTime())
				);
				TcManticEmpresasArchivosDto exists= (TcManticEmpresasArchivosDto)DaoFactory.getInstance().toEntity(TcManticEmpresasArchivosDto.class, "TcManticEmpresasArchivosDto", "identically", tmp.toMap());
				File reference= new File(tmp.getAlias());
				if(exists== null && reference.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticEmpresasArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!reference.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
				//this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.xml.getRuta()), ".".concat(this.xml.getFormat().name()), this.toListFile(sesion, this.xml, 1L));
			} // if	
			if(this.pdf!= null) {
				tmp= new TcManticEmpresasArchivosDto(
					this.pdf.getRuta(),
					-1L,
					this.pdf.getName(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					this.pdf.getFileSize(),
					JsfBase.getIdUsuario(),	
					2L,
					1L,
					this.pdf.getObservaciones(),
					this.idPago,																					
					Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.pdf.getRuta()).concat(this.pdf.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
          this.pdf.getOriginal(),
					this.idTipoComprobante,
					new java.sql.Date(this.fecha.getTime())
				);
				TcManticEmpresasArchivosDto exists= (TcManticEmpresasArchivosDto)DaoFactory.getInstance().toEntity(TcManticEmpresasArchivosDto.class, "TcManticEmpresasArchivosDto", "identically", tmp.toMap());
				File reference= new File(tmp.getAlias());
				if(exists== null && reference.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticEmpresasArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!reference.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
				//this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
  	} // if	
    return true;
	}

	public void toDeleteXmlPdf() throws Exception {
		List<TcManticEmpresasArchivosDto> list= (List<TcManticEmpresasArchivosDto>)DaoFactory.getInstance().findViewCriteria(TcManticEmpresasArchivosDto.class, this.deuda.toMap(), "all");
		if(list!= null)
			for (TcManticEmpresasArchivosDto item: list) {
				File file= new File(item.getAlias());
				file.delete();
			} // for
	}	// if	
	
	private List<Nombres> toListFile(Session sesion, Importado tmp, Long idTipoArchivo) throws Exception {
		List<Nombres> regresar= null;
		Map<String, Object> params=null;
		try {
			params  = new HashMap<>();
			params.put("idTipoArchivo", idTipoArchivo);
			params.put("ruta", tmp.getRuta());
			regresar= (List<Nombres>)DaoFactory.getInstance().toEntitySet(sesion, Nombres.class, "TcManticEmpresasArchivosDto", "listado", params);
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
	
	private void toDeleteAll(String path, String type, List<Nombres> listado) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(path), type.toLowerCase());
    if(fileSearch.getResult().size()> 0) {
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
				if(listado.indexOf(new Nombres(name))< 0) {
				  File file= new File(matched);
				  file.delete();
				} // if
      } // for
		} // if
	} // toDeleteAll
	
	private boolean actualizarDeuda(Session sesion) throws Exception {
		boolean regresar              = false;
		TcManticEmpresasDeudasDto item= null;
		Double importe                = 0.0D;
		try {
			item= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.detalle.getKey());
			if(item.isValid()) {				
				importe= Double.valueOf(String.valueOf(this.detalle.get("importe")));
				item.setLimite(new java.sql.Date(this.fecha.getTime()));
				item.setImporte(importe);
				item.setPagar(importe);
				item.setSaldo(this.calculateSaldo(sesion, importe, this.detalle.getKey()));
				regresar= DaoFactory.getInstance().update(sesion, item)>= 1L;
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // actualizarDeuda
	
	private Double calculateSaldo(Session sesion, Double importe, Long idEmpresaDeuda) throws Exception {
		Double regresar          = 0.0D;
		Double totalPagos        = 0.0D;
		List<Entity>pagos        = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresaDeuda", idEmpresaDeuda);
			pagos= DaoFactory.getInstance().toEntitySet(sesion, "VistaEmpresasDto", "pagosDeuda", params);
			if(!pagos.isEmpty()) {
				for(Entity record: pagos)
					totalPagos= totalPagos + record.toDouble("pago");
				regresar= importe - totalPagos;
			} // if
			else
				regresar= importe;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // calculateSaldo
	
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
      this.control= new TcManticEmpresasPagosControlesDto(
        "GENERAL", // String tipo, 
        1L, // Long idActivo, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        this.pago.getObservaciones(), // String observaciones, 
        -1L, // Long idEmpresaPagoControl, 
        this.pago.getPago() // Double pago
      );
      DaoFactory.getInstance().insert(sesion, this.control);
			for(Entity item: deudas) {
				if(saldo> 0) {
					saldoDeuda= Numero.toRedondear(item.toDouble("saldo"));
					if(saldoDeuda< this.pago.getPago()) {
						pagoParcial= saldoDeuda;
						saldo= this.pago.getPago()- saldoDeuda;						
						this.pago.setPago(saldo);
						abono= 0D;
						idEstatus= EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa();
					} // if
          else {						
						pagoParcial= this.pago.getPago();
						saldo= 0D;
						abono= saldoDeuda- this.pago.getPago();
						idEstatus= this.saldar? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa(): (saldoDeuda.equals(this.pago.getPago())? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
					} /// else
					if(this.registrarPago(sesion, item, pagoParcial, this.control.getIdEmpresaPagoControl())) {
						params= new HashMap<>();
						params.put("saldo", abono);
						params.put("idEmpresaEstatus", idEstatus);
						DaoFactory.getInstance().update(sesion, TcManticEmpresasDeudasDto.class, item.getKey(), params);
            TcManticEmpresasBitacoraDto bitacora= new TcManticEmpresasBitacoraDto(
              null, // String justificacion, 
              EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa(), // Long idEmpresaEstatus, 
              JsfBase.getIdUsuario(), // Long idUsuario, 
              item.getKey(), // Long idEmpresaDeuda
              -1L // Long idEmpresaBitacora, 
            );
            DaoFactory.getInstance().insert(sesion, bitacora);
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
      this.control= new TcManticEmpresasPagosControlesDto(
        "SEGMENTO", // String tipo, 
        1L, // Long idActivo, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        this.pago.getObservaciones(), // String observaciones, 
        -1L, // Long idEmpresaPagoControl, 
        this.pago.getPago() // Double pago
      );
      DaoFactory.getInstance().insert(sesion, this.control);
			for(Entity item: deudas) {
				for(Entity cuenta: this.cuentas) {
					if(item.getKey().equals(cuenta.getKey())) {
						if(saldo> 0) {
							saldoDeuda= Numero.toRedondearSat(item.toDouble("saldo"));
							if(saldoDeuda< this.pago.getPago()) {
								pagoParcial= saldoDeuda;
								saldo= this.pago.getPago() - saldoDeuda;						
								this.pago.setPago(saldo);
								abono= 0D;
								idEstatus= EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa();
							} // if
              else {
								pagoParcial= this.pago.getPago();
								saldo= 0D;
								abono= saldoDeuda- this.pago.getPago();
								idEstatus= this.saldar? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa(): (saldoDeuda.equals(this.pago.getPago())? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa(): EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
							} /// else
							if(this.registrarPago(sesion, item, pagoParcial, this.control.getIdEmpresaPagoControl())) {
								params= new HashMap<>();
								params.put("saldo", abono);
								params.put("idEmpresaEstatus", idEstatus);
								DaoFactory.getInstance().update(sesion, TcManticEmpresasDeudasDto.class, item.getKey(), params);
							}	// if				
						} // if
						else 
              if (this.saldar) {
							  if(this.registrarPago(sesion, item, 0D, this.control.getIdEmpresaPagoControl())) {
								  params= new HashMap<>();
								  params.put("saldo", 0);
								  params.put("idEmpresaEstatus", EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());
								  DaoFactory.getInstance().update(sesion, TcManticEmpresasDeudasDto.class, item.getKey(), params);
                  TcManticEmpresasBitacoraDto bitacora= new TcManticEmpresasBitacoraDto(
                    null, // String justificacion, 
                    EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa(), // Long idEmpresaEstatus, 
                    JsfBase.getIdUsuario(), // Long idUsuario, 
                    item.getKey(), // Long idEmpresaDeuda
                    -1L // Long idEmpresaBitacora, 
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
	
	private boolean registrarPago(Session sesion, Entity deuda, Double pagoParcial, Long idEmpresaPagoControl) throws Exception {
		TcManticEmpresasPagosDto registroPago= null;
		boolean regresar                     = false;
		Siguiente orden                      = null;
		try {
      Long idEmpresaDeuda= deuda.getKey();
			if(this.toCierreCaja(sesion, pagoParcial)) {
        registroPago= new TcManticEmpresasPagosDto();
        registroPago.setIdEmpresaPagoControl(idEmpresaPagoControl);
        registroPago.setIdEmpresaDeuda(idEmpresaDeuda);
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
        orden= this.toSiguiente(sesion);
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
			params.put("idProveedor", this.idProveedor);
			params.put(Constantes.SQL_CONDICION, " tc_mantic_empresas_deudas.saldo> 0 and tc_mantic_empresas_deudas.id_empresa_estatus in (1, 2, 3)");			
			params.put("sortOrder", "order by dias desc");
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "VistaEmpresasDto", "cuentasProveedor", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDeudas	
	
	private boolean toCierreCaja(Session sesion, Double pago) throws Exception {
		mx.org.kaana.mantic.ventas.caja.reglas.Transaccion cierre= null;
		VentaFinalizada datosCierre= null;
		boolean regresar           = false;
		ETipoMediosPago medioPago  = null;
		Double abono               = 0D;
		try {
			medioPago= ETipoMediosPago.fromIdTipoPago(this.pago.getIdTipoMedioPago());
			datosCierre= new VentaFinalizada();
			datosCierre.getTicketVenta().setIdEmpresa(this.idEmpresa);
			datosCierre.setIdCaja(this.idCaja);
			abono= pago* -1D;
			switch(medioPago) {
				case CHEQUE:
					datosCierre.getTotales().setCheque(abono);
					break;
				case EFECTIVO:
					datosCierre.getTotales().setEfectivo(abono);
					break;
				case TARJETA_CREDITO:
					datosCierre.getTotales().setCredito(abono);
					break;
				case TRANSFERENCIA:
					datosCierre.getTotales().setTransferencia(abono);
					break;
				case TARJETA_DEBITO:
					datosCierre.getTotales().setDebito(abono);
					break;
				case VALES_DESPENSA:
					datosCierre.getTotales().setVales(abono);
					break;
			} // switch						
			cierre= new mx.org.kaana.mantic.ventas.caja.reglas.Transaccion(datosCierre);
			if(cierre.verificarCierreCaja(sesion)) {
				this.idCierreActivo= cierre.getIdCierreVigente();
				regresar= cierre.alterarCierreCaja(sesion, this.pago.getIdTipoMedioPago());
			} // if
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // toCierreCaja
	
	protected void toUpdateDeleteFilePago(Session sesion) throws Exception {
		TcManticEmpresasArchivosDto tmp= null;
		if(this.deuda.getIdEmpresaDeuda()!= -1L) {			
			if(this.pdf!= null) {
				tmp= new TcManticEmpresasArchivosDto(
					this.pdf.getRuta(),
					-1L,
					this.pdf.getName(),
					Long.valueOf(Fecha.getAnioActual()),
					this.pdf.getFileSize(),
					JsfBase.getIdUsuario(),
					2L,
					1L,
					this.pdf.getObservaciones(),
					this.idPago,	
					Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.pdf.getRuta()).concat(this.pdf.getName()),
					Long.valueOf(Fecha.getMesActual()),
					this.pdf.getOriginal(),
					this.idTipoComprobante,
					new java.sql.Date(this.fecha.getTime())
				);
				TcManticEmpresasArchivosDto exists= (TcManticEmpresasArchivosDto)DaoFactory.getInstance().toEntity(TcManticEmpresasArchivosDto.class, "TcManticEmpresasArchivosDto", "identically", tmp.toMap());
				File reference= new File(tmp.getAlias());
				if(exists== null && reference.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticEmpresasArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else {
					if(exists!= null)
						DaoFactory.getInstance().delete(sesion, exists);
					DaoFactory.getInstance().insert(sesion, tmp);
				} // else
				sesion.flush();
				//toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
  	} // if	
	} // toUpdateDeleteXml
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", this.idEmpresa);
			Value next= DaoFactory.getInstance().toField(sesion, "VistaTcManticEmpresasPagosDto", "siguiente", params, "siguiente");
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

	private boolean eliminarPago(Session sesion) throws Exception {
		boolean regresar                   = false;
		TcManticEmpresasArchivosDto archivo= null;		
		try {
			archivo= (TcManticEmpresasArchivosDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasArchivosDto.class, this.idArchivo);			
			File file= new File(archivo.getAlias());
			if(file.exists())
				file.delete();			
			regresar= DaoFactory.getInstance().delete(sesion, archivo)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // eliminarPago

  private Boolean toDeletePagos(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idEmpresaPagoControl", this.idEmpresaPago);      
      List<Entity> items = (List<Entity>)DaoFactory.getInstance().toEntitySet(sesion, "VistaEmpresasDto", "eliminarPagos", params);
      if(items!= null && !items.isEmpty()) {
        for (Entity item: items) {
          // borrar todos los archivos asociados a los pagos 
          List<TcManticEmpresasPagosArchivosDto> archivos= (List<TcManticEmpresasPagosArchivosDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticEmpresasPagosArchivosDto.class, "TcManticEmpresasPagosArchivosDto", "pago", item.toMap());
          if(archivos!= null && !archivos.isEmpty()) {
            for (TcManticEmpresasPagosArchivosDto archivo: archivos) {
              File evidencia= new File(archivo.getAlias());
              if(evidencia.exists())
                evidencia.delete();
            } // for
            DaoFactory.getInstance().deleteAll(sesion, TcManticEmpresasPagosArchivosDto.class, item.toMap());
          } // if  
          DaoFactory.getInstance().delete(sesion, TcManticEmpresasPagosDto.class, item.toLong("idEmpresaPago"));
          TcManticEmpresasDeudasDto cuenta= (TcManticEmpresasDeudasDto)DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, item.toLong("idEmpresaDeuda"));
          cuenta.setSaldo(Numero.toRedondearSat(cuenta.getSaldo()+ item.toDouble("abonado")));
          if(Objects.equals(cuenta.getImporte(), cuenta.getSaldo()))
            cuenta.setIdEmpresaEstatus(EEstatusEmpresas.INICIADA.getIdEstatusEmpresa());
          else
            if(!Objects.equals(cuenta.getImporte(), cuenta.getSaldo()))
              cuenta.setIdEmpresaEstatus(EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
          DaoFactory.getInstance().update(sesion, cuenta);
          TcManticEmpresasBitacoraDto bitacora= new TcManticEmpresasBitacoraDto(
            this.referencia.concat(", SE ELIMINO EL PAGO [").concat(String.valueOf(item.toDouble("abonado"))).concat("]"), // String justificacion, 
            cuenta.getIdEmpresaEstatus(), // Long idEmpresaEstatus, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            cuenta.getIdEmpresaDeuda(), // Long idEmpresaDeuda
            -1L // Long idEmpresaBitacora, 
          );
          DaoFactory.getInstance().insert(sesion, bitacora);
        } // for
        DaoFactory.getInstance().delete(sesion, TcManticEmpresasPagosControlesDto.class, this.idEmpresaPago);
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
      params.put("idEmpresaDeuda", this.idEmpresaPago);      
      TcManticEmpresasDeudasDto item= (TcManticEmpresasDeudasDto)DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.idEmpresaPago);
      item.setIdEmpresaEstatus(EEstatusEmpresas.CANCELADA.getIdEstatusEmpresa());
      DaoFactory.getInstance().update(sesion, item);
      TcManticEmpresasBitacoraDto bitacora= new TcManticEmpresasBitacoraDto(
        this.referencia, // String justificacion, 
        item.getIdEmpresaEstatus(), // Long idClienteEstatus, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        item.getIdEmpresaDeuda(), // Long idEmpresaDeuda
        -1L // Long idEmpresaBitacora, 
      );
      DaoFactory.getInstance().insert(sesion, bitacora);
      TcManticNotasEntradasDto nota= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(sesion, TcManticNotasEntradasDto.class, item.getIdNotaEntrada());
      nota.setIdNotaEstatus(4L);// CANCELADA
      DaoFactory.getInstance().update(sesion, nota);
      //Long idNotaBitacora, String justificacion, Long idUsuario, Long idNotaEntrada, Long idNotaEstatus, String consecutivo, Double importe
      TcManticNotasBitacoraDto movimiento= new TcManticNotasBitacoraDto(
        -1L, // Long idNotaBitacora, 
        this.referencia.concat(", SE CANCELO LA CUENTA POR COBRAR"), // String justificacion, 
        JsfBase.getIdUsuario(), // Long idUsuario, 
        nota.getIdNotaEntrada(), // Long idNotaEntrada, 
        nota.getIdNotaEstatus(), // Long idNotaEstatus, 
        nota.getConsecutivo(), // Long consecutivo, 
        nota.getTotal() // Double importe
      );
      DaoFactory.getInstance().insert(sesion, movimiento);
      // FALTA AFECTAR LAS EXITENCIAS EN EL ALMACEN RESPECTIVO PORQUE LA VENTA SE CANCELO
      this.toAffectAlmacenes(sesion, nota);
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
  
  
  public void toAffectAlmacenes(Session sesion, TcManticNotasEntradasDto nota) throws Exception {
    Map<String, Object> params= null;
    try {      
      Double stock= null;
      params = new HashMap<>();      
      params.put("idNotaEntrada", nota.getIdNotaEntrada());      
      List<TcManticNotasDetallesDto> items= (List<TcManticNotasDetallesDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticNotasDetallesDto.class, "TcManticNotasDetallesDto", "detalle", params);
      if(items!= null && !items.isEmpty()) {
        for (TcManticNotasDetallesDto item : items) {
          params.put("idAlmacen", nota.getIdAlmacen());
			    params.put("idArticulo", item.getIdArticulo());
          // ACTUALIZAR EL STOCK DEL ALMACEN 
			    TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class,  params, "ubicacion");
          stock= ubicacion.getStock();
          ubicacion.setStock(Numero.toRedondearSat(ubicacion.getStock()+ item.getCantidad()));
				  DaoFactory.getInstance().update(sesion, ubicacion);
          // generar un registro en la bitacora de movimientos de los articulos 
          TcManticMovimientosDto entrada= new TcManticMovimientosDto(
            nota.getConsecutivo(), // String consecutivo, 
            3L, // Long idTipoMovimiento, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            nota.getIdAlmacen(), // Long idAlmacen, 
            -1L, // Long idMovimiento, 
            item.getCantidad(), // Double cantidad, 
            item.getIdArticulo(), // Long idArticulo, 
            stock, // Double stock, 
            ubicacion.getStock(), // Double calculo
            "FUE UN ERROR DE CAPTURA"+ (this.referencia!= null? ", ".concat(this.referencia): "") // String observaciones
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
  
}
