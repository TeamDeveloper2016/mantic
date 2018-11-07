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
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transferir extends IBaseTnx {

  private static final Logger LOG   = Logger.getLogger(Transferir.class);

  private Integer count= 0;
	private String messageError;	
	private StringBuffer clientes;

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
		boolean regresar          = false;
		Map<String, Object> params= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la sincronización de facturas.");
			switch(accion) {				
				case GENERAR:
					this.toDownload(sesion);
					break;
				case PROCESAR:
					this.toDownload(sesion, "");
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

  private TcManticFicticiasDto toFicticia(Session sesion, CfdiSearchResult cfdi, Calendar calendar,Long idCliente) throws Exception {
		Long consecutivo= this.toSiguiente(sesion);
		TcManticFicticiasDto regresar= new TcManticFicticiasDto(
			0D, // Double descuentos, 
			1L, // Long idTipoPago, 
			-1L,// Long idFicticia, 
			"0", // String extras, 
			0D, // Double global, 
			cfdi.getTotal(), // Double total, 
			3L, // Long idFicticiaEstatus, 
			0D, // Double tipoDeCambio, 
			consecutivo, // Long orden, 
			1L, // Long idTipoMedioPago, 
			idCliente, // Long idCliente, 
			"0", // String descuento, 
			null, // Long idBanco, 
			new Long(calendar.get(Calendar.YEAR)), // Long ejercicio, 
			Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true),  // Long consecutivo, 
			JsfBase.getIdUsuario(), //  Long idUsuario, 
			0D,  // Double impuestos, 
			1L,  // Long idUsoCfdi, 
			1L,  // Long idSinIva, 
			0D,  // Double subTotal, 
			cfdi.getUuid(),  // String observaciones, 
			JsfBase.getAutentifica().getEmpresa().getIdEmpresa(),  //  Long idEmpresa, 
			new Date(calendar.getTimeInMillis()),  // Date dia, 
			null //  referencia
		);
		return regresar;
	}
	
	private TcManticFacturasDto toFactura(Session sesion, CfdiSearchResult cfdi, Calendar calendar, Long idFicticia, String path, String name) {
		TcManticFacturasDto regresar= new TcManticFacturasDto(
			-1L, // Long idFactura, 
			new Date(Calendar.getInstance().getTimeInMillis()), // Date ultimoIntento, 
			new Timestamp(calendar.getTimeInMillis()), // Timestamp timbrado, 
			path, // String ruta, 
			JsfBase.getIdUsuario(), // Long idUsuario, 
			idFicticia, // Long idFicticia, 
			cfdi.getFolio(), // String folio, 
			null, // Long idVenta, 
			name, // String nombre, 
			0L, // Long intentos, 
			cfdi.getEmail(), // String correos, 
			"", // String comentarios, 
			"", // String observaciones, 
			cfdi.getId(), // String idFacturama, 
			path.concat(name) // String alias
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
			if(next.getData()!= null)
				regresar= next.toLong();
			else
				LOG.warn("El cliente con RFC ["+ rfc+ "] no existe favor de verificarlo !");
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente

	private void toDownload(Session sesion) throws Exception {
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
 	  Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
    monitoreo.comenzar(0L);
    monitoreo.setTotal(Long.valueOf(cfdis.size()));
		int x= 0;
		for (CfdiSearchResult cfdi : cfdis) {
			this.toProcess(sesion, cfdi);
			if(x % 100== 0)
			  sesion.flush();
      monitoreo.setProgreso((long)(x* 100/ monitoreo.getTotal()));
      monitoreo.incrementar();
		} // for
		this.count= cfdis.size();
	}
	
	private void toDownload(Session sesion, String idFacturama) throws Exception {
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
		int index= cfdis.indexOf(new CfdiSearchResult(idFacturama));
		if(index>= 0) {
			this.toProcess(sesion, cfdis.get(index));
		} // for
		else
			throw new RuntimeException("La factura con id "+ idFacturama+ " no se encuentra generada !");
	}

	private boolean exists(Session sesion, String idFacturama) throws Exception {
		boolean regresar          = false;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idFacturama", idFacturama);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFacturasDto", "facturama", params, "idFactura");
			if(next.getData()!= null)
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

	private void toProcess(Session sesion, CfdiSearchResult cfdi) throws Exception {
		if(!this.exists(sesion, cfdi.getId())) {
			Calendar calendar= Fecha.toCalendar(cfdi.getDate().substring(0, 10), cfdi.getDate().substring(11, 19));
			String path = Configuracion.getInstance().getPropiedadSistemaServidor("facturama")+ calendar.get(Calendar.YEAR)+ "/"+ Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase()+"/"+ cfdi.getRfc().concat("/");
			CFDIFactory.getInstance().download(path, cfdi.getRfc().concat("-").concat(cfdi.getFolio()), cfdi.getId());
			Long idCliente= this.toCliente(sesion, cfdi.getRfc()); 
			if(idCliente< 0) {
				TcManticFicticiasDto ficticia= this.toFicticia(sesion, cfdi, calendar, idCliente);
				DaoFactory.getInstance().insert(sesion, ficticia);
				TcManticFacturasDto factura= this.toFactura(sesion, cfdi, calendar, ficticia.getIdFicticia(), path, cfdi.getRfc().concat("-").concat(cfdi.getFolio()));
				DaoFactory.getInstance().insert(sesion, factura);
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
			if(next.getData()!= null)
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