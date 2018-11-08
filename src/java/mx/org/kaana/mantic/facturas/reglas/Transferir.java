package mx.org.kaana.mantic.facturas.reglas;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.response.Cfdi;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.models.response.Tax;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
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
	private String messageError;	
	private StringBuffer clientes;
	private List<Client> clients;

	public Transferir() { 		
		this.count       = 0;
		this.messageError= "";
		this.clientes    = new StringBuffer();
	} // Transaccion

	public Integer getCount() {
		return count;
	}
	
	public String getMessageError() {
		return messageError;
	} // Transaccion

	public String getClientes() {
		return clientes.length()> 0? clientes.substring(0, clientes.length()- 1): clientes.toString();
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
					regresar= this.toDownload(sesion, "");
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		LOG.info("Se proceso de forma correcta la sincronización de facturas, transferidas: "+ this.count);
		return regresar;
	}	// ejecutar

  private TcManticFicticiasDto toFicticia(Session sesion, CfdiSearchResult cfdi, Cfdi detail, Calendar calendar,Long idCliente) throws Exception {
		Long consecutivo= this.toSiguiente(sesion);
		double taxes    = 0;
		for (Tax tax: detail.getTaxes()) {
			taxes+= tax.getTotal();
		} // for
		TcManticFicticiasDto regresar= new TcManticFicticiasDto(
			detail.getDiscount(), // Double descuentos, 
			1L, // Long idTipoPago, 
			-1L,// Long idFicticia, 
			"0", // String extras, 
			0D, // Double global, 
			cfdi.getTotal(), // Double total, 
			3L, // Long idFicticiaEstatus, 
			detail.getExchangeRate(), // Double tipoDeCambio, 
			consecutivo, // Long orden, 
			1L, // Long idTipoMedioPago, 
			idCliente, // Long idCliente, 
			"0", // String descuento, 
			null, // Long idBanco, 
			new Long(calendar.get(Calendar.YEAR)), // Long ejercicio, 
			Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true),  // Long consecutivo, 
			JsfBase.getIdUsuario(), //  Long idUsuario, 
			taxes, // Double impuestos, 
			1L,  // Long idUsoCfdi, 
			1L,  // Long idSinIva, 
			detail.getSubtotal(),  // Double subTotal, 
			detail.getObservations(),  // String observaciones, 
			JsfBase.getAutentifica().getEmpresa().getIdEmpresa(),  //  Long idEmpresa, 
			new Date(calendar.getTimeInMillis()),  // Date dia, 
			detail.getPaymentAccountNumber() //  referencia
		);
		return regresar;
	}
	
	private TcManticFacturasDto toFactura(CfdiSearchResult cfdi, Calendar calendar, Long idFicticia) {
		TcManticFacturasDto regresar= new TcManticFacturasDto(
			-1L, // Long idFactura, 
			new Date(Calendar.getInstance().getTimeInMillis()), // Date ultimoIntento, 
			new Timestamp(calendar.getTimeInMillis()), // Timestamp timbrado, 
			JsfBase.getIdUsuario(), // Long idUsuario, 
			idFicticia, // Long idFicticia, 
			cfdi.getFolio(), // String folio, 
			null, // Long idVenta, 
			0L, // Long intentos, 
			cfdi.getEmail(), // String correos, 
			"", // String comentarios, 
			"", // String observaciones, 
			cfdi.getId() // String idFacturama, 
		);
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
				TcManticClientesDto cliente= new TcManticClientesDto(
					rfc, // String clave, 
					0L, // Long plazoDias, 
					-1L, // Long idCliente, 
					0D, // Double limiteCredito, 
					2L, // Long idCredito, 
					client.getName(), // String razonSocial, 
					0D, // Double saldo, 
					rfc, // String rfc, 
					JsfBase.getIdUsuario(), // Long idUsuario, 
					1L, // Long idUsoCfdi, 
					client.getCfdiUse(), // String observaciones, 
					JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), //  Long idEmpresa, 
					1L, // Long idTipoVenta, 
					client.getId() // String idFacturama
				);
				DaoFactory.getInstance().insert(sesion, cliente);
				if(!Cadena.isVacio(client.getEmail())) {
					TrManticClienteTipoContactoDto contacto= new TrManticClienteTipoContactoDto(
						cliente.getIdCliente(), // Long idCliente, 
						JsfBase.getIdUsuario(), //Long idUsuario, 
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
						JsfBase.getIdUsuario(), //Long idUsuario, 
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
						JsfBase.getIdUsuario(), //Long idUsuario, 
						client.getEmailOp2(), // String valor, 
						"", // String observaciones, 
						-1L, // Long idClienteTipoContacto, 
						3L, // Long orden, 
						11L // Long idTipoContacto
					);
					DaoFactory.getInstance().insert(sesion, contacto);
				} // if
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
	} // toSiguiente

	private boolean toDownload(Session sesion) throws Exception {
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
 	  Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
    monitoreo.comenzar(0L);
    monitoreo.setTotal(Long.valueOf(cfdis.size()));
		int x= 0;
		for (CfdiSearchResult cfdi: cfdis) {
			this.toProcess(sesion, cfdi, CFDIFactory.getInstance().toCfdiDetail(cfdi.getId()));
			if(x % 50== 0)
			  sesion.flush();
      monitoreo.setProgreso((long)(x* 100/ monitoreo.getTotal()));
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

	private boolean exists(Session sesion, String idFacturama) throws Exception {
		boolean regresar          = false;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idFacturama", idFacturama);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFacturasDto", "facturama", params, "idFactura");
			if(next!= null && next.getData()!= null)
				regresar= true;
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente

	private void toProcess(Session sesion, CfdiSearchResult cfdi, Cfdi detail) throws Exception {
		if(!this.exists(sesion, cfdi.getId())) {
			Calendar calendar= Fecha.toCalendar(cfdi.getDate().substring(0, 10), cfdi.getDate().substring(11, 19));
			String path = Configuracion.getInstance().getPropiedadSistemaServidor("facturama")+ calendar.get(Calendar.YEAR)+ "/"+ Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase()+"/"+ cfdi.getRfc().concat("/");
			CFDIFactory.getInstance().download(path, cfdi.getRfc().concat("-").concat(cfdi.getFolio()), cfdi.getId());
			Long idCliente= this.toCliente(sesion, cfdi.getRfc()); 
			if(idCliente> 0) {
				TcManticFicticiasDto ficticia= this.toFicticia(sesion, cfdi, detail, calendar, idCliente);
				DaoFactory.getInstance().insert(sesion, ficticia);
				TcManticFacturasDto factura= this.toFactura(cfdi, calendar, ficticia.getIdFicticia());
				DaoFactory.getInstance().insert(sesion, factura);
				TcManticFacturasArchivosDto xml= new TcManticFacturasArchivosDto(
					factura.getIdFactura(), 
					path, 
					null, 
					cfdi.getRfc().concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase()), 
					new Long(calendar.get(Calendar.YEAR)),
					null, 
					-1L,
					0L,
					JsfBase.getIdUsuario(),
					1L, // idTipoArchivo XML
					1L, // idPrincipal
					detail.getCfdiType()+ "|"+ detail.getPaymentMethod()+ "|"+ detail.getPaymentConditions(), // observaciones, 
					path.concat(cfdi.getRfc()).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase()), 
					new Long(calendar.get(Calendar.MONTH)+ 1), 
					"" // comentarios
				);
				DaoFactory.getInstance().insert(sesion, xml);
				TcManticFacturasArchivosDto pdf= new TcManticFacturasArchivosDto(
					factura.getIdFactura(), 
					path, 
					null, 
					cfdi.getRfc().concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.PDF.name().toLowerCase()), 
					new Long(calendar.get(Calendar.YEAR)),
					null, 
					-1L,
					0L,
					JsfBase.getIdUsuario(),
					2L, // idTipoArchivo PDF
					1L, // idPrincipal
					detail.getCfdiType()+ "|"+ detail.getPaymentMethod()+ "|"+ detail.getPaymentConditions(), // observaciones, 
					path.concat(cfdi.getRfc()).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.PDF.name().toLowerCase()), 
					new Long(calendar.get(Calendar.MONTH)+ 1), 
					"" // comentarios
				);
				DaoFactory.getInstance().insert(sesion, pdf);
				TcManticFicticiasBitacoraDto bitacora= new TcManticFicticiasBitacoraDto(ficticia.getConsecutivo(), "FACTURA REGISTRADA DE FORMA AUTOMATICA", 3l, 1L, ficticia.getIdFicticia(), -1L, ficticia.getTotal());
				DaoFactory.getInstance().insert(sesion, bitacora);
			} // if
			else
				this.clientes.append(cfdi.getRfc()).append("-").append(cfdi.getEmail()).append(Constantes.SEPARADOR);
		} // if
	}
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFicticiasDto", "siguiente", params, "siguiente");
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
	} // toSiguiente

} 