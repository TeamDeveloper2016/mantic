package mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.enums.EEstatusEmpresas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
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
	
	public Transaccion(TcManticEmpresasDeudasDto deuda, Importado pdf, Long idPago) {
		this(deuda, null, pdf, idPago);
	}
	
	public Transaccion(TcManticEmpresasDeudasDto deuda, Importado xml, Importado pdf, Long idPago) {
		this.deuda = deuda;
		this.pdf   = pdf;
		this.xml   = xml;
		this.idPago= idPago;
	} // Transaccion

	public Transaccion(Entity detalle, Date fecha) {
		this.detalle= detalle;
		this.fecha  = fecha;
	} // Transaccion	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar    = false;
		String observaciones= null;
    try {			
			if(this.pago!= null)
				this.pagoGeneral= this.pago.getPago();
      switch (accion) {
        case AGREGAR:
          regresar = procesarPago(sesion);
          break;       
				case REGISTRAR:
					regresar= true;
					toUpdateDeleteXml(sesion);
					break;
				case MODIFICAR:
					regresar= actualizarDeuda(sesion);
					break;
				case PROCESAR:
					regresar = procesarPagoGeneral(sesion);
					break;
				case COMPLEMENTAR:
					regresar = procesarPagoSegmento(sesion);
					break;
				case SUBIR:
					regresar= true;
					toUpdateDeleteFilePago(sesion);
					break;
				case ACTIVAR:
					TcManticEmpresasDeudasDto deudaReabrir= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.deuda.getIdEmpresaDeuda());
					deudaReabrir.setIdEmpresaEstatus(EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
					observaciones= Cadena.isVacio(deudaReabrir.getObservaciones()) ? this.deuda.getObservaciones() : deudaReabrir.getObservaciones().concat("; ").concat(this.deuda.getObservaciones());
					deudaReabrir.setObservaciones(observaciones);
					regresar= DaoFactory.getInstance().update(sesion, deudaReabrir)>= 1L;
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
	
	private boolean procesarPago(Session sesion) throws Exception{
		boolean regresar               = true;
		TcManticEmpresasDeudasDto deuda= null;
		Double saldo                   = 0D;		
		Siguiente orden                = null;
		try {
			if(this.pago.getPago()> 0) {
				//if(toCierreCaja(sesion, this.pago.getPago())){				
				//this.pago.setIdCierre(this.idCierreActivo);
				if(!this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
					this.pago.setReferencia(this.referencia);
					this.pago.setIdBanco(this.idBanco);
				} // if
				orden= this.toSiguiente(sesion);
				this.pago.setOrden(orden.getOrden());
				this.pago.setConsecutivo(orden.getConsecutivo());
				this.pago.setEjercicio(new Long(Fecha.getAnioActual()));
				if(DaoFactory.getInstance().insert(sesion, this.pago)>= 1L){
					deuda= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.pago.getIdEmpresaDeuda());
					saldo= deuda.getSaldo() + this.pago.getPago();
					deuda.setSaldo(saldo);
					deuda.setIdEmpresaEstatus(saldo >= 0D ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
					regresar= DaoFactory.getInstance().update(sesion, deuda)>= 1L;
				} // if
				//} // toCierreCaja
			} // if
			else
				deuda= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.pago.getIdEmpresaDeuda());			
			procesaNotasEntrada(sesion, deuda);
			procesaNotasCredito(sesion, deuda);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally{
			this.messageError= "Error al registrar el pago";
		} // finally
		return regresar;
	} // procesarPago
	
	private void procesaNotasEntrada(Session sesion, TcManticEmpresasDeudasDto deuda) throws Exception{
		Double saldoPivote                    = 0D;
		Double totalPago                      = 0D;
		TcManticEmpresasDeudasDto empresaDeuda= null;
		TcManticEmpresasPagosDto pagoPivote   = null;    
		try {
			if(!this.notasEntrada.isEmpty()){
				for(Entity notaEntrada: this.notasEntrada){
					if((deuda.getSaldo()*-1) > 0D){
						empresaDeuda= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, notaEntrada.getKey());
						if((empresaDeuda.getSaldo()*-1) > 0D){
							if((deuda.getSaldo()*-1) <= empresaDeuda.getSaldo()){																																
								totalPago= (deuda.getSaldo()*-1);
								deuda.setSaldo(0D);
								deuda.setIdEmpresaEstatus(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());								
								if(DaoFactory.getInstance().update(sesion, deuda)>= 1L){								
									saldoPivote= empresaDeuda.getSaldo() - ((deuda.getSaldo()*-1));									
									empresaDeuda.setSaldo(saldoPivote);
									DaoFactory.getInstance().update(sesion, empresaDeuda);									
								} // if
							} // if
							else{
								totalPago= empresaDeuda.getSaldo();
								saldoPivote= deuda.getSaldo() + empresaDeuda.getSaldo();
								deuda.setSaldo(saldoPivote);
								deuda.setIdEmpresaEstatus(saldoPivote >= 0D ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
								if(DaoFactory.getInstance().update(sesion, deuda)>= 1L){								
									saldoPivote= 0D;
									empresaDeuda.setSaldo(saldoPivote);
									DaoFactory.getInstance().update(sesion, empresaDeuda);
								} // if
							} // else
							pagoPivote= new TcManticEmpresasPagosDto(1L, JsfBase.getIdUsuario(), this.pago.getIdEmpresaDeuda(), this.pago.getObservaciones(), -1L, totalPago, null,  null, null, empresaDeuda.getIdNotaEntrada(), null);
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
	
	private void procesaNotasCredito(Session sesion, TcManticEmpresasDeudasDto deuda) throws Exception{
		Double saldoPivote                  = 0D;
		TcManticCreditosNotasDto creditoNota= null;
		TcManticEmpresasPagosDto pagoPivote = null;  
		Double totalPago                    = 0D;
		try {
			if(!this.notasCredito.isEmpty()){
				for(Entity notaCredito: this.notasCredito){
					if((deuda.getSaldo()*-1)> 0D){
						creditoNota= (TcManticCreditosNotasDto) DaoFactory.getInstance().findById(sesion, TcManticCreditosNotasDto.class, notaCredito.getKey());
						if(creditoNota.getImporte()> 0D){
							if((deuda.getSaldo()*-1) <= creditoNota.getImporte()){		
								totalPago= (deuda.getSaldo()*-1);
								deuda.setSaldo(0D);								
								deuda.setIdEmpresaEstatus(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());
								if(DaoFactory.getInstance().update(sesion, deuda)>= 1L){
									saldoPivote= creditoNota.getImporte()- ((deuda.getSaldo()*-1));																		
									creditoNota.setSaldo(saldoPivote);
									DaoFactory.getInstance().update(sesion, creditoNota);
								} // if
							} // if
							else{
								totalPago= creditoNota.getImporte();
								saldoPivote= deuda.getSaldo() + creditoNota.getImporte();
								deuda.setSaldo(saldoPivote);
								deuda.setIdEmpresaEstatus(saldoPivote >= 0D ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
								if(DaoFactory.getInstance().update(sesion, deuda)>= 1L){								
									saldoPivote= 0D;
									creditoNota.setSaldo(saldoPivote);
									DaoFactory.getInstance().update(sesion, creditoNota);
								} // if
							} // else
							pagoPivote= new TcManticEmpresasPagosDto(1L, JsfBase.getIdUsuario(), this.pago.getIdEmpresaDeuda(), this.pago.getObservaciones(), -1L, totalPago, notaCredito.getKey(),  null, null, null, null);
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
	
	protected void toUpdateDeleteXml(Session sesion) throws Exception {
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
          this.xml.getOriginal()					
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
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.xml.getRuta()), ".".concat(this.xml.getFormat().name()), this.toListFile(sesion, this.xml, 1L));
			} // if	
			if(this.pdf!= null) {
				tmp= new TcManticEmpresasArchivosDto(
					this.xml.getRuta(),
					-1L,
					this.xml.getName(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					this.xml.getFileSize(),
					JsfBase.getIdUsuario(),	
					2L,
					1L,
					this.xml.getObservaciones(),
					this.idPago,																					
					Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.xml.getRuta()).concat(this.xml.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1),
          this.xml.getOriginal()					
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
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
  	} // if	
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
	
	private boolean actualizarDeuda(Session sesion) throws Exception{
		boolean regresar               = false;
		TcManticEmpresasDeudasDto deuda= null;
		Double importe                 = 0.0D;
		try {
			deuda= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.detalle.getKey());
			if(deuda.isValid()){				
				importe= Double.valueOf(String.valueOf(this.detalle.get("importe")));
				deuda.setLimite(new java.sql.Date(this.fecha.getTime()));
				deuda.setImporte(importe);
				deuda.setPagar(importe);
				deuda.setSaldo(-1 * calculateSaldo(sesion, importe, this.detalle.getKey()));
				regresar= DaoFactory.getInstance().update(sesion, deuda)>= 1L;
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // actualizarDeuda
	
	private Double calculateSaldo(Session sesion, Double importe, Long idEmpresaDeuda) throws Exception{
		Double regresar          = 0.0D;
		Double totalPagos        = 0.0D;
		List<Entity>pagos        = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresaDeuda", idEmpresaDeuda);
			pagos= DaoFactory.getInstance().toEntitySet(sesion, "VistaEmpresasDto", "pagosDeuda", params);
			if(!pagos.isEmpty()){
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
	
	private boolean procesarPagoGeneral(Session sesion) throws Exception{		
		boolean regresar         = true;
		List<Entity> deudas      = null;		
		Map<String, Object>params= null;
		Double saldo             = 1D;
		Double saldoDeuda        = 0D;				
		Double pagoParcial       = 0D;				
		Double abono             = 0D;		
		Long idEstatus           = -1L;
		try {
			deudas= toDeudas(sesion);
			for(Entity recordDeuda: deudas){
				if(saldo > 0){					
					saldoDeuda= Double.valueOf(recordDeuda.toString("saldo")) * -1D;
					if(saldoDeuda < this.pago.getPago()){
						pagoParcial= saldoDeuda;
						saldo= this.pago.getPago() - saldoDeuda;						
						this.pago.setPago(saldo);
						abono= 0D;
						idEstatus= EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa();
					} // if
					else{						
						pagoParcial= this.pago.getPago();
						saldo= 0D;
						abono= saldoDeuda - this.pago.getPago();
						idEstatus= this.saldar ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : (saldoDeuda.equals(this.pago.getPago()) ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.PARCIALIZADA.getIdEstatusEmpresa());
					} /// else
					if(registrarPago(sesion, recordDeuda.getKey(), pagoParcial)){
						params= new HashMap<>();
						params.put("saldo", (abono * -1D));
						params.put("idEmpresaEstatus", idEstatus);
						DaoFactory.getInstance().update(sesion, TcManticEmpresasDeudasDto.class, recordDeuda.getKey(), params);
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
	
	private boolean procesarPagoSegmento(Session sesion) throws Exception{		
		boolean regresar         = true;
		List<Entity> deudas      = null;		
		Map<String, Object>params= null;
		Double saldo             = 1D;
		Double saldoDeuda        = 0D;				
		Double pagoParcial       = 0D;				
		Double abono             = 0D;		
		Long idEstatus           = -1L;
		try {
			deudas= toDeudas(sesion);
			for(Entity deuda: deudas){
				for(Entity cuenta: this.cuentas){
					if(deuda.getKey().equals(cuenta.getKey())){
						if(saldo > 0){					
							saldoDeuda= Double.valueOf(deuda.toString("saldo")) * -1D;
							if(saldoDeuda < this.pago.getPago()){
								pagoParcial= saldoDeuda;
								saldo= this.pago.getPago() - saldoDeuda;						
								this.pago.setPago(saldo);
								abono= 0D;
								idEstatus= EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa();
							} // if
							else{						
								pagoParcial= this.pago.getPago();
								saldo= 0D;
								abono= saldoDeuda - this.pago.getPago();
								idEstatus= this.saldar ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : (saldoDeuda.equals(this.pago.getPago()) ? EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa() : EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());
							} /// else
							if(registrarPago(sesion, deuda.getKey(), pagoParcial)){
								params= new HashMap<>();
								params.put("saldo", (abono * -1D));
								params.put("idEmpresaEstatus", idEstatus);
								DaoFactory.getInstance().update(sesion, TcManticEmpresasDeudasDto.class, deuda.getKey(), params);
							}	// if				
						} // if
						else if (this.saldar){
							if(registrarPago(sesion, deuda.getKey(), 0D)){
								params= new HashMap<>();
								params.put("saldo", 0);
								params.put("idEmpresaEstatus", EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa());
								DaoFactory.getInstance().update(sesion, TcManticEmpresasDeudasDto.class, deuda.getKey(), params);
							}	// if				
						}
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
	
	private boolean registrarPago(Session sesion, Long idEmpresaDeuda, Double pagoParcial) throws Exception{
		TcManticEmpresasPagosDto registroPago= null;
		boolean regresar                     = false;
		Siguiente orden                      = null;
		try {
			//if(toCierreCaja(sesion, pagoParcial)){
			registroPago= new TcManticEmpresasPagosDto();
			registroPago.setIdEmpresaDeuda(idEmpresaDeuda);
			registroPago.setIdUsuario(JsfBase.getIdUsuario());
			registroPago.setObservaciones("Pago aplicado a la deuda general del cliente. ".concat(this.pago.getObservaciones()).concat(". Pago general por $").concat(this.pagoGeneral.toString()));
			registroPago.setPago(pagoParcial);
			registroPago.setIdTipoMedioPago(this.pago.getIdTipoMedioPago());
			if(!this.pago.getIdTipoMedioPago().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago())){
				registroPago.setIdBanco(this.idBanco);
				registroPago.setReferencia(this.referencia);
			} // if
			orden= this.toSiguiente(sesion);
			registroPago.setOrden(orden.getOrden());
			registroPago.setConsecutivo(orden.getConsecutivo());
			registroPago.setEjercicio(new Long(Fecha.getAnioActual()));
			regresar= DaoFactory.getInstance().insert(sesion, registroPago)>= 1L;
			//} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarPago
	
	private List<Entity> toDeudas(Session sesion) throws Exception{
		List<Entity> regresar    = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idProveedor", this.idProveedor);
			params.put(Constantes.SQL_CONDICION, " tc_mantic_empresas_deudas.saldo < 0 and tc_mantic_empresas_deudas.id_empresa_estatus not in(".concat(EEstatusEmpresas.LIQUIDADA.getIdEstatusEmpresa().toString()).concat(")"));			
			params.put("sortOrder", "order by tc_mantic_empresas_deudas.registro desc");
			regresar= DaoFactory.getInstance().toEntitySet(sesion, "VistaEmpresasDto", "cuentasProveedor", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDeudas	
	
	private boolean toCierreCaja(Session sesion, Double pago) throws Exception{
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
			abono= pago * -1D;
			switch(medioPago){
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
					this.pdf.getOriginal()
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
				toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
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
	
}
