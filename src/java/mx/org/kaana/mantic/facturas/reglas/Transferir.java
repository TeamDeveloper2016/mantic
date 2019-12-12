package mx.org.kaana.mantic.facturas.reglas;

import java.io.File;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.response.Cfdi;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.models.response.Complement;
import mx.org.kaana.libs.facturama.models.response.Item;
import mx.org.kaana.libs.facturama.models.response.Tax;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transferir extends IBaseTnx {

  private static final Logger LOG= Logger.getLogger(Transferir.class);

  private Integer count= 0;
	private String idFacturama;
	private String messageError;	
	private StringBuffer clientes;
	private List<Client> clients;

	public Transferir() { 		
		this(null);
	} // Transaccion
	
	public Transferir(String idFacturama) { 		
		this.count       = 0;
		this.messageError= "";
		this.clientes    = new StringBuffer();
		this.idFacturama = idFacturama;
	} // Transaccion

	public Integer getCount() {
		return count;
	}
	
	public String getMessageError() {
		return messageError;
	} // Transaccion

	public String getClientes() {
		return clientes.toString();
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		this.clients    =	CFDIFactory.getInstance().getClients();
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la sincronización de facturas.");
			switch(accion) {				
				case GENERAR:
					regresar= this.toDownload(sesion);
					break;
				case PROCESAR:
					regresar= this.toDownload(sesion, this.idFacturama);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		LOG.info("Se proceso de forma correcta la sincronización de facturas, transferidas: "+ this.count);
		return regresar;
	}	// ejecutar

  private TcManticFicticiasDto toFicticia(Session sesion, CfdiSearchResult cfdi, Cfdi detail, Calendar calendar, Long idCliente, Long idFactura) throws Exception {
		Siguiente consecutivo= this.toSiguiente(sesion, calendar);
		Siguiente cuenta     = this.toSiguienteCuenta(sesion, calendar);
		double taxes         = 0;
		for (Tax tax: detail.getTaxes()) {
			taxes+= tax.getTotal();
		} // for
		Long year= new Long(calendar.get(Calendar.YEAR));
		TcManticFicticiasDto regresar= new TcManticFicticiasDto(
			detail.getDiscount(), // Double descuentos, 
			1L, // Long idTipoPago, 
			-1L,// Long idFicticia, 
			"0", // String extras, 
			0D, // Double global, 
			cfdi.getTotal(), // Double total, 
			3L, // Long idFicticiaEstatus, 
			detail.getExchangeRate(), // Double tipoDeCambio, 
			cuenta.getOrden(), // Long orden, 
			1L, // Long idTipoMedioPago, 
			idCliente, // Long idCliente, 
			toIdClienteDomicilio(sesion, idCliente),
			"0", // String descuento, 
			null, // Long idBanco, 
			year, // Long ejercicio, 
			cuenta.toConsecutivo(),// String consecutivo, 
			JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, //  Long idUsuario, 
			taxes, // Double impuestos, 
			1L,  // Long idUsoCfdi, 
			1L,  // Long idSinIva, 
			detail.getSubtotal(),  // Double subTotal, 
			(Cadena.isVacio(detail.getObservations())? "": detail.getObservations())+ " ESTA FACTURA FUE RECUPERADA DE FACTURAMA !",  // String observaciones, 
			JsfBase.getAutentifica()!= null? JsfBase.getAutentifica().getEmpresa().getIdEmpresa(): 1L,  //  Long idEmpresa, 
			new Date(calendar.getTimeInMillis()),  // Date dia, 
			detail.getPaymentAccountNumber(), //  referencia
			idFactura // id_factura
		);
		regresar.setTicket(consecutivo.getConsecutivo());
		regresar.setCticket(consecutivo.getOrden());
		regresar.setIdFacturar(1L);
		LOG.info("----------------------------------------------");
		LOG.info("CONSECUTIVO:"+ cuenta+ " TICKET"+ consecutivo);
		return regresar;
	}
	
	private Long toIdClienteDomicilio(Session sesion, Long idCliente) throws Exception {
		Long regresar                        = null;
		TrManticClienteDomicilioDto principal= null;
		Map<String, Object>params            = null;
		try {
			params= new HashMap<>();
			params.put("idCliente", idCliente);
			principal= (TrManticClienteDomicilioDto) DaoFactory.getInstance().toEntity(sesion, TrManticClienteDomicilioDto.class, "TrManticClienteDomicilioDto", "principalCliente", params);
			if(principal!= null)
				regresar= principal.getKey();
			else {
				params.clear();
				params.put(Constantes.SQL_CONDICION, "id_cliente=".concat(idCliente.toString()));
				principal= (TrManticClienteDomicilioDto) DaoFactory.getInstance().toEntity(sesion, TrManticClienteDomicilioDto.class, "TrManticClienteDomicilioDto", params);
				if(principal!= null)
					regresar= principal.getIdClienteDomicilio();
			  else
					regresar= 1L;
			} // else
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toIdClienteDomicilio
	
	private TcManticFacturasDto toFactura(CfdiSearchResult cfdi, Cfdi detail, Calendar calendar) {
		Complement complement = detail.getComplement();
		Calendar certificacion= Fecha.toCalendar(complement.getTaxStamp().getDate().substring(0, 10), complement.getTaxStamp().getDate().substring(11, 19));
		TcManticFacturasDto regresar= new TcManticFacturasDto(
			-1L, // Long idFactura, 
			new Date(Calendar.getInstance().getTimeInMillis()), // Date ultimoIntento, 
			new Timestamp(calendar.getTimeInMillis()), // Timestamp timbrado, 
			JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, // Long idUsuario, 
			cfdi.getFolio(), // String folio, 
			0L, // Long intentos, 
			cfdi.getEmail(), // String correos, 
			"ESTA FACTURA FUE RECUPERADA DE FACTURAMA !", // String comentarios, 
			"", // String observaciones, 
			cfdi.getId(), // String idFacturama
			"", // String cadenaOriginal
			complement.getTaxStamp().getSatSign(), // String selloSat
			complement.getTaxStamp().getCfdiSign(), // String selloCfdi
			complement.getTaxStamp().getSatCertNumber(), // String certificadoSat
			detail.getCertNumber(), // String certificadoDigital
			new Timestamp(certificacion.getTimeInMillis()), // Timestamp certificacion
			complement.getTaxStamp().getUuid(),
			EEstatusFacturas.TIMBRADA.getIdEstatusFactura()
		);
		return regresar;
	}
	
	protected boolean registrarBitacoraFactura(Session sesion, Long idFactura, Long idFacturaEstatus, String justificacion) throws Exception{
		boolean regresar= false;
		TcManticFacturasBitacoraDto bitacora= null;
		try {
			bitacora= new TcManticFacturasBitacoraDto();
			bitacora.setIdFactura(idFactura);
			bitacora.setIdFacturaEstatus(idFacturaEstatus);
			bitacora.setIdUsuario(JsfBase.getIdUsuario());
			bitacora.setJustificacion(justificacion);
			regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacoraFactura
	
	private Long toFindEntidad(Session sesion, String entidad) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			String codigo= entidad.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim();
			params.put("descripcion", codigo.replaceAll("(,| |\\t)+", ".*.*"));
			Value value= DaoFactory.getInstance().toField(sesion, "TcJanalEntidadesDto", "entidad", params, "idEntidad");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField(sesion, "TcJanalEntidadesDto", "primero", params, "idEntidad");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

	private Long toFindMunicipio(Session sesion, Long idEntidad, String municipio) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idEntidad", idEntidad);
			params.put("descripcion", municipio!= null? municipio.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*"): "XYZ");
			Value value= DaoFactory.getInstance().toField(sesion, "TcJanalMunicipiosDto", "municipio", params, "idMunicipio");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField(sesion, "TcJanalMunicipiosDto", "primero", params, "idMunicipio");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private Long toFindLocalidad(Session sesion, Long idMunicipio, String localidad) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idMunicipio", idMunicipio);
			params.put("descripcion", localidad!= null? localidad.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*"): "XYZ");
			Value value= DaoFactory.getInstance().toField(sesion, "TcJanalLocalidadesDto", "localidad", params, "idLocalidad");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField(sesion, "TcJanalLocalidadesDto", "primero", params, "idLocalidad");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

	private Long toCliente(Session sesion, String rfc) throws Exception {
		Long regresar             = -1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("rfc", rfc);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticClientesDto", "rfc", params, "idCliente");
			if(next!= null && next.getData()!= null)
				regresar= next.toLong();
			else {
				LOG.warn("El cliente con RFC ["+ rfc+ "] no existe favor de verificarlo !");
				Client client= this.clients.get(this.clients.indexOf(new Client(rfc)));
				this.clientes.append("{").append(rfc).append(Constantes.SEPARADOR).append(client.getName()).append(Constantes.SEPARADOR).append(client.getEmail()).append("}");
				TcManticClientesDto cliente= new TcManticClientesDto(
					rfc, // String clave, 
					0L, // Long plazoDias, 
					-1L, // Long idCliente, 
					0D, // Double limiteCredito, 
					2L, // Long idCredito, 
					client.getName(), // String razonSocial, 
					0D, // Double saldo, 
					rfc, // String rfc, 
					JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, // Long idUsuario, 
					1L, // Long idUsoCfdi, 
					client.getCfdiUse(), // String observaciones, 
					JsfBase.getAutentifica()!= null? JsfBase.getAutentifica().getEmpresa().getIdEmpresa(): 1L, //  Long idEmpresa, 
					1L, // Long idTipoVenta, 
					client.getId() // String idFacturama
				);
				DaoFactory.getInstance().insert(sesion, cliente);
				if(!Cadena.isVacio(client.getEmail())) {
					TrManticClienteTipoContactoDto contacto= new TrManticClienteTipoContactoDto(
						cliente.getIdCliente(), // Long idCliente, 
						JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, //Long idUsuario, 
						client.getEmail(), // String valor, 
						"", // String observaciones, 
						-1L, // Long idClienteTipoContacto, 
						1L, // Long orden, 
						9L // Long idTipoContacto
					);
					DaoFactory.getInstance().insert(sesion, contacto);
				} // if
				if(!Cadena.isVacio(client.getEmailOp1())) {
					TrManticClienteTipoContactoDto contacto= new TrManticClienteTipoContactoDto(
						cliente.getIdCliente(), // Long idCliente, 
						JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, //Long idUsuario, 
						client.getEmailOp1(), // String valor, 
						"", // String observaciones, 
						-1L, // Long idClienteTipoContacto, 
						2L, // Long orden, 
						10L // Long idTipoContacto
					);
					DaoFactory.getInstance().insert(sesion, contacto);
				} // if
				if(!Cadena.isVacio(client.getEmailOp2())) {
					TrManticClienteTipoContactoDto contacto= new TrManticClienteTipoContactoDto(
						cliente.getIdCliente(), // Long idCliente, 
						JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, //Long idUsuario, 
						client.getEmailOp2(), // String valor, 
						"", // String observaciones, 
						-1L, // Long idClienteTipoContacto, 
						3L, // Long orden, 
						11L // Long idTipoContacto
					);
					DaoFactory.getInstance().insert(sesion, contacto);
				} // if
					
				  Long idEntidad  = this.toFindEntidad(sesion, client.getAddress().getState());
			    Long idMunicipio= this.toFindMunicipio(sesion, idEntidad, client.getAddress().getMunicipality());
			    Long idLocalidad= this.toFindLocalidad(sesion, idMunicipio, client.getAddress().getLocality());
					TcManticDomiciliosDto domicilio= new TcManticDomiciliosDto(
					  client.getAddress().getNeighborhood(), // String asentamiento, 
						idLocalidad, // Long idLocalidad, 
						client.getAddress().getZipCode(), // String codigoPostal, 
						null, // String latitud, 
						"", // String entreCalle, 
						client.getAddress().getStreet(), // String calle, 
						-1L, // Long idDomicilio, 
						client.getAddress().getInteriorNumber(), // String numeroInterior,  
						"", // String ycalle, 
						null, // String longitud, 
						client.getAddress().getExteriorNumber(), // String numeroExterior, 
						JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, // Long idUsuario, 
						"" // String observaciones
					);
					DaoFactory.getInstance().insert(sesion, domicilio);
					TrManticClienteDomicilioDto particular= new TrManticClienteDomicilioDto(
					  cliente.getIdCliente(), // Long idCliente, 
					  -1L, // Long idClienteDomicilio, 
						JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, // Long idUsuario, 
						1L, // Long idTipoDomicilio, 
						domicilio.getIdDomicilio(), // Long idDomicilio, 
						1L, // Long idPrincipal, 
						"" // String observaciones
					);
					DaoFactory.getInstance().insert(sesion, particular);
					particular= new TrManticClienteDomicilioDto(
					  cliente.getIdCliente(), // Long idCliente, 
					  -1L, // Long idClienteDomicilio, 
						JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, // Long idUsuario, 
						2L, // Long idTipoDomicilio, 
						domicilio.getIdDomicilio(), // Long idDomicilio, 
						1L, // Long idPrincipal, 
						"" // String observaciones
					);
					DaoFactory.getInstance().insert(sesion, particular);
				regresar= cliente.getIdCliente();
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toCliente

	private boolean toDownload(Session sesion) throws Exception {
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
 	  Monitoreo monitoreo= JsfBase.getAutentifica()!= null? JsfBase.getAutentifica().getMonitoreo(): new Monitoreo();
    monitoreo.comenzar(Long.valueOf(cfdis.size()));
		int x= 0;
		for (CfdiSearchResult cfdi: cfdis) {
			this.toProcess(sesion, cfdi, CFDIFactory.getInstance().toCfdiDetail(cfdi.getId()));
			if(x % 50== 0)
			  sesion.flush();
      monitoreo.incrementar();
			x++;
		} // for
		this.count= cfdis.size();
		return true;
	}
	
	private boolean toDownload(Session sesion, String idFacturama) throws Exception {
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
		int index= cfdis.indexOf(new CfdiSearchResult(idFacturama));
		if(index>= 0) {
			this.toProcess(sesion, cfdis.get(index), CFDIFactory.getInstance().toCfdiDetail(cfdis.get(index).getId()));
		} // for
		else
			throw new RuntimeException("La factura con id "+ idFacturama+ " no se encuentra generada !");
		return true;
	}

	private Long exists(Session sesion, String idFacturama) throws Exception {
		Long regresar             = -1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idFacturama", idFacturama);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFacturasDto", "facturama", params, "idFactura");
			if(next!= null && next.getData()!= null)
				regresar= next.toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // exists
	
	private boolean files(Session sesion, Long idFactura) throws Exception {
		boolean regresar          = false;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idFactura", idFactura);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFacturasArchivosDto", "total", params, "total");
			if(next!= null && next.getData()!= null)
				regresar= next.toLong()> 0;
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // exists

  private String toCadenaOriginal(String xml) throws Exception {
		StreamSource source       = new StreamSource(new File(xml));
		StreamSource stylesource  = new StreamSource(this.getClass().getResourceAsStream("/mx/org/kaana/mantic/libs/factura/cadenaoriginal_3_3.xslt"));
		TransformerFactory factory= TransformerFactory.newInstance();
		Transformer transformer   = factory.newTransformer(stylesource);
		StreamResult result       = new StreamResult(new StringWriter());
		transformer.transform(source, result);
		return result.getWriter().toString();
	}
	
	private void toDetail(Session sesion, Long idFicticia, Cfdi detail) throws Exception {
		List<Item> items= detail.getItems();
		int count= 1;
		for (Item item : items) {
			double impuesto= Double.valueOf(Numero.redondearSat((item.getTotal()* 1.16)- item.getTotal()));
			double importe = Double.valueOf(Numero.redondearSat(item.getTotal()+ impuesto));
		  TcManticFicticiasDetallesDto detalle= new TcManticFicticiasDetallesDto(
			  0D, // Double descuentos, 
				"", // String codigo, 
				item.getUnit(), // String unidadMedida, 
				item.getUnitValue(), // Double costo, 
				idFicticia, // Long idFicticia, 
				"0", // String descuento, 
				Constantes.CODIGO_SAT, // String sat, 
				"0", // String extras, 
				0D, // Double utilidad, 
				item.getDescription(), // String nombre, 
				importe, // Double importe, 
				-1L, // Long idFicticiaDetalle, 
				item.getUnitValue(), // Double precio, 
				16D, // Double iva, 
				impuesto, // Double impuestos, 
				item.getTotal(), // Double subTotal, 
				item.getQuantity(), // Double cantidad, 
				new Long(count), // Long idArticulo,
				item.getUnitValue()
			);
			DaoFactory.getInstance().insert(sesion, detalle);
			count++;
		} // for
	}

	private void toUpdateData(Session sesion, CfdiSearchResult cfdi, Cfdi detail, Long idFactura, String path) throws Exception {
		TcManticFacturasDto factura= (TcManticFacturasDto)DaoFactory.getInstance().findById(sesion, TcManticFacturasDto.class, idFactura);
		if(factura!= null && factura.getSelloSat()== null && factura.getCadenaOriginal()== null) {
			LOG.warn("Actualizando datos de la factura ["+ cfdi.getFolio()+ "] del cliente ["+ cfdi.getRfc()+ "] porque estaba incompleto el registro !");
			Complement complement = detail.getComplement();
			factura.setComentarios("ESTA FACTURA FUE RECUPERADA DE FACTURAMA !");
			factura.setSelloCfdi(complement.getTaxStamp().getCfdiSign());
			factura.setSelloSat(complement.getTaxStamp().getSatSign());
			factura.setCertificadoDigital(detail.getCertNumber());
			factura.setCertificadoSat(complement.getTaxStamp().getSatCertNumber());
			factura.setFolioFiscal(complement.getTaxStamp().getUuid());
			factura.setCadenaOriginal(this.toCadenaOriginal(path.concat(cfdi.getRfc()).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase())));
			factura.setIdFacturaEstatus(EEstatusFacturas.TIMBRADA.getIdEstatusFactura());
			factura.setIntentos(factura.getIntentos()+1L);
			DaoFactory.getInstance().update(sesion, factura);
			registrarBitacoraFactura(sesion, idFactura, EEstatusFacturas.TIMBRADA.getIdEstatusFactura(), factura.getComentarios());
		} // if
	}
	
	private void toSaveFiles(Session sesion, CfdiSearchResult cfdi, Cfdi detail, Long idFactura, Calendar calendar, String path) throws Exception {
		if(!this.files(sesion, idFactura)) { 
			TcManticFacturasArchivosDto xml= new TcManticFacturasArchivosDto(
				idFactura, 
				path, 
				null, 
				cfdi.getRfc().concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase()), 
				new Long(calendar.get(Calendar.YEAR)),
				null, 
				-1L,
				0L,
				JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L,
				1L, // idTipoArchivo XML
				1L, // idPrincipal
				detail.getCfdiType()+ "|"+ detail.getPaymentMethod()+ "|"+ detail.getSerie(), // observaciones, 
				path.concat(cfdi.getRfc()).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase()), 
				new Long(calendar.get(Calendar.MONTH)+ 1), 
				"" // comentarios
			);
			DaoFactory.getInstance().insert(sesion, xml);
			TcManticFacturasArchivosDto pdf= new TcManticFacturasArchivosDto(
				idFactura,
				path, 
				null, 
				cfdi.getRfc().concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.PDF.name().toLowerCase()), 
				new Long(calendar.get(Calendar.YEAR)),
				null, 
				-1L,
				0L,
				JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L,
				2L, // idTipoArchivo PDF
				1L, // idPrincipal
				detail.getCfdiType()+ "|"+ detail.getPaymentMethod()+ "|"+ detail.getPaymentConditions(), // observaciones, 
				path.concat(cfdi.getRfc()).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.PDF.name().toLowerCase()), 
				new Long(calendar.get(Calendar.MONTH)+ 1), 
				"" // comentarios
			);
			DaoFactory.getInstance().insert(sesion, pdf);		
		} // if	
	}
	
	private void toProcess(Session sesion, CfdiSearchResult cfdi, Cfdi detail) throws Exception {
		Calendar calendar= Fecha.toCalendar(cfdi.getDate().substring(0, 10), cfdi.getDate().substring(11, 19));
		String path = Configuracion.getInstance().getPropiedadSistemaServidor("facturama").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat("/")+ calendar.get(Calendar.YEAR)+ "/"+ Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase()+"/"+ cfdi.getRfc().concat("/");
		CFDIFactory.getInstance().download(path, cfdi.getRfc().concat("-").concat(cfdi.getFolio()), cfdi.getId());
		Long idFactura= this.exists(sesion, cfdi.getId());
		if(idFactura> 0) {
			this.toSaveFiles(sesion, cfdi, detail, idFactura, calendar, path);
			this.toUpdateData(sesion, cfdi, detail, idFactura, path);
		} // if
		else {	
			Long idCliente= this.toCliente(sesion, cfdi.getRfc()); 
			if(idCliente> 0) {
				// GENERA LA FACTURA CON TODOS LOS DATOS FISCALES 
				TcManticFacturasDto factura= this.toFactura(cfdi, detail, calendar);
				// GENERA LA CADENA ORIGINAL BASA EN EL ARCHIVO XML QUE SE DESCARGO
				factura.setCadenaOriginal(this.toCadenaOriginal(path.concat(cfdi.getRfc()).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase())));
				DaoFactory.getInstance().insert(sesion, factura);
				registrarBitacoraFactura(sesion, factura.getIdFactura(), factura.getIdFacturaEstatus(), factura.getComentarios());
				// GENERA EL REGISTRO DETALLADO DE LA FACTURA
				TcManticFicticiasDto ficticia= this.toFicticia(sesion, cfdi, detail, calendar, idCliente, factura.getIdFactura());
				DaoFactory.getInstance().insert(sesion, ficticia);
				this.toSaveFiles(sesion, cfdi, detail, factura.getIdFactura(), calendar, path);
				this.toDetail(sesion, ficticia.getIdFicticia(), detail);
				TcManticFicticiasBitacoraDto bitacora= new TcManticFicticiasBitacoraDto(ficticia.getConsecutivo(), "FACTURA REGISTRADA DE FORMA AUTOMATICA", 3l, 1L, ficticia.getIdFicticia(), -1L, ficticia.getTotal());
				DaoFactory.getInstance().insert(sesion, bitacora);
				// sesion.flush();
			} // if
		} // if
	}
	
	private Siguiente toSiguiente(Session sesion, Calendar dia) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", new Long(dia.get(Calendar.YEAR)));
			params.put("idEmpresa", JsfBase.getAutentifica()!= null? JsfBase.getAutentifica().getEmpresa().getIdEmpresa(): 1L);
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFicticiasDto", "siguiente", params, "siguiente");
			if(next!= null && next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente

	private Siguiente toSiguienteCuenta(Session sesion, Calendar dia) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", new Long(dia.get(Calendar.YEAR)));
			params.put("dia", Fecha.formatear(Fecha.FECHA_ESTANDAR, dia));
			params.put("idEmpresa", JsfBase.getAutentifica()!= null? JsfBase.getAutentifica().getEmpresa().getIdEmpresa(): 1L);
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFicticiasDto", "cuenta", params, "siguiente");
			if(next!= null && next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toCuenta
	
} 