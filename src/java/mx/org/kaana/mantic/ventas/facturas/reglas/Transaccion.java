package mx.org.kaana.mantic.ventas.facturas.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import mx.org.kaana.mantic.enums.EEstatusFicticias;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoPago;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import mx.org.kaana.mantic.facturas.beans.Correo;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends TransaccionFactura {

  private static final Logger LOG= Logger.getLogger(Transaccion.class);	
	
	private TicketVenta orden;		
	private String messageError;	
	private String justificacion;
	private Correo correo;
	private Long idCliente;
	private Long idEstatusFactura;
	private TcManticFacturasDto facturaPrincipal;

	public Transaccion(Correo correo, Long idCliente) {
		this.correo   = correo;
		this.idCliente= idCliente;
	}	// Transaccion		
	
	public Transaccion(TicketVenta orden, Long idEstatusFactura, String justificacion) { 		
		this.orden           = orden;		
		this.justificacion   = justificacion;
		this.idEstatusFactura= idEstatusFactura;
	} // Transaccion
	
	public String getMessageError() {
		return messageError;
	} // Transaccion

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}	
	
	public TcManticVentasDto getOrden() {
		return orden;
	}	

	public TcManticFacturasDto getFacturaPrincipal() {
		return facturaPrincipal;
	}
		
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar           = false;
		Map<String, Object> params = null;
		TcManticFacturasDto factura= null;
		Long idFactura             = null;
		Long idEstatus             = null;
		try {									
			switch(accion) {																						
				case MODIFICAR:
					this.messageError= "Ocurrio un error al generar la factura.";
					if(this.idEstatusFactura.equals(EEstatusFicticias.TIMBRADA.getIdEstatusFicticia()) && this.checkTotal(sesion)){
						idEstatus= EEstatusFacturas.TIMBRADA.getIdEstatusFactura();
						if(this.orden.getIdFactura()!= null && !this.orden.getIdFactura().equals(-1L)){
							idFactura= this.orden.getIdFactura();
							factura= (TcManticFacturasDto) DaoFactory.getInstance().findById(sesion, TcManticFacturasDto.class, idFactura);
							factura.setCorreos(this.orden.getCorreos());
							factura.setComentarios(this.justificacion);
							factura.setTimbrado(new Timestamp(Calendar.getInstance().getTimeInMillis()));							
							factura.setIdFacturaEstatus(idEstatus);
							regresar= DaoFactory.getInstance().update(sesion, factura)>= 1L;
							this.facturaPrincipal= factura;
						} // if
						else{							
							idFactura= registrarFactura(sesion);						
							this.orden.setIdFactura(idFactura);
							regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						} // else								
						registrarBitacoraFactura(sesion, idFactura, idEstatus, this.justificacion);
						if(this.generarTimbradoFactura(sesion, this.orden.getIdVenta(), idFactura, this.orden.getCorreos())){
							if(registraBitacora(sesion, this.orden.getIdVenta(), EEstatusVentas.TIMBRADA.getIdEstatusVenta(), "Se realizó el timbrado de la factura asociada a la venta.")){
								this.orden.setIdVentaEstatus(EEstatusVentas.TIMBRADA.getIdEstatusVenta());
								regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
							} // if
						} // if
					} // if
					else if(this.idEstatusFactura.equals(EEstatusFicticias.CANCELADA.getIdEstatusFicticia())) {
						idEstatus= EEstatusFacturas.CANCELADA.getIdEstatusFactura();
						this.messageError= "Ocurrio un error al cancelar la factura.";
						params= new HashMap<>();
						params.put("idFactura", this.orden.getIdFactura());
						factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
						if(factura!= null && factura.getIdFacturama()!= null) {
							CFDIFactory.getInstance().cfdiRemove(factura.getIdFacturama());
							factura.setCancelada(new Timestamp(Calendar.getInstance().getTimeInMillis()));
							factura.setIdFacturaEstatus(idEstatus);
							regresar= DaoFactory.getInstance().update(sesion, factura)>= 0;
							registrarBitacoraFactura(sesion, factura.getIdFactura(), idEstatus, "Cancelación de factura.");
						} // if
						else
							throw new Exception("No fue posible cancelar la factura, por favor vuelva a intentarlo !");															
					} // else if
					break;												
				case COMPLEMENTAR: 
					regresar= agregarContacto(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
		if(this.orden!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar		
	
	protected boolean registraBitacora(Session sesion, Long idVenta, Long idVentaEstatus, String justificacion) throws Exception {
		TcManticVentasBitacoraDto bitVenta= new TcManticVentasBitacoraDto(-1L, justificacion, JsfBase.getIdUsuario(), idVenta, idVentaEstatus, this.orden.getConsecutivo(), this.orden.getTotal());
		return DaoFactory.getInstance().insert(sesion, bitVenta)>= 1L;
	} // registrarBitacora
	
	private Long registrarFactura(Session sesion) throws Exception{
		Long regresar              = -1L;
		TcManticFacturasDto factura= null;
		try {			
			factura= new TcManticFacturasDto();
			factura.setIdUsuario(JsfBase.getIdUsuario());
			factura.setIntentos(0L);
			factura.setCorreos(this.orden.getCorreos());
			factura.setComentarios(this.justificacion);
			factura.setTimbrado(new Timestamp(Calendar.getInstance().getTimeInMillis()));			
			factura.setObservaciones(this.justificacion);
			factura.setIdFacturaEstatus(EEstatusFacturas.TIMBRADA.getIdEstatusFactura());
			regresar= DaoFactory.getInstance().insert(sesion, factura);
			this.facturaPrincipal= factura;
		} // try
		finally {
			setMessageError("Error al registrar la factura.");
		} // finally
		return regresar;
	} // registrarFactura
	
	private boolean agregarContacto(Session sesion) throws Exception{
		boolean regresar                       = true;
		List<ClienteTipoContacto> correos      = null;
		TrManticClienteTipoContactoDto contacto= null;
		int count                              = 0;
		Long records                           = 1L;
		try {
			correos= toClientesTipoContacto();
			if(!correos.isEmpty()){
				for(ClienteTipoContacto tipoContacto: correos){
					if(tipoContacto.getValor().equals(this.correo.getDescripcion()))
						count++;
				} // for				
				records= correos.size() + 1L;
			} // if
			if(count== 0){
				contacto= new TrManticClienteTipoContactoDto();
				contacto.setIdCliente(this.idCliente);
				contacto.setIdTipoContacto(ETiposContactos.CORREO.getKey());
				contacto.setIdUsuario(JsfBase.getIdUsuario());
				contacto.setValor(this.correo.getDescripcion().toUpperCase());
				contacto.setOrden(records);
				regresar= DaoFactory.getInstance().insert(sesion, contacto)>= 1L;
			} // else
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // agregarContacto
	
	public List<ClienteTipoContacto> toClientesTipoContacto() throws Exception {
		List<ClienteTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente + " and id_tipo_contacto=" + ETiposContactos.CORREO.getKey());
			regresar= DaoFactory.getInstance().toEntitySet(ClienteTipoContacto.class, "TrManticClienteTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
	private boolean generarTimbradoFactura(Session sesion, Long idVenta, Long idFactura, String correos) throws Exception {
		boolean regresar          = false;
		TransaccionFactura factura= null;
		CFDIGestor gestor         = null;
		try {
			actualizarClienteFacturama(sesion);
			sesion.flush();
			gestor= new CFDIGestor(idVenta);			
			factura= new TransaccionFactura();
			factura.setArticulos(gestor.toDetalleCfdiVentas(sesion));
			factura.setCliente(gestor.toClienteCfdiVenta(sesion));
			factura.getCliente().setIdFactura(idFactura);
			factura.getCliente().setMetodoPago(ETipoPago.fromIdTipoPago(this.orden.getIdTipoPago()).getClave());
			regresar= factura.generarCfdi(sesion);				
		} // try
		catch (Exception e) {			
			this.messageError= "Error al realizar el timbrado de la factura.";
			throw e;
		} // catch	
		return regresar;
	} // generarTimbradoFactura

	private void actualizarClienteFacturama(Session sesion) throws Exception{		
		CFDIGestor gestor= new CFDIGestor(this.orden.getIdCliente());
		ClienteFactura cliente= gestor.toClienteFacturaUpdateVenta(sesion, this.orden.getIdClienteDomicilio());
		setCliente(cliente);
		if(cliente.getIdFacturama()!= null)
			updateCliente(sesion);
		else
			super.procesarCliente(sesion);		
	} // actualizarArticuloFacturama
	
	private boolean checkTotal(Session sesion) throws Exception {
		boolean regresar = false;
		Double sumTotal  = 0D;
		Double sumDetalle= 0D;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idVenta", this.orden.getIdVenta());
			Value detalle= DaoFactory.getInstance().toField(sesion, "TcManticVentasDetallesDto", "total", params, "total");
			if(detalle!= null && detalle.getData()!= null)
				sumDetalle= detalle.toDouble();
			Value total= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "total", params, "total");
			if(total!= null && total.getData()!= null)
				sumTotal= total.toDouble();
		} // try
		finally {
			Methods.clean(params);
		} // finally
		regresar= Objects.equals(sumTotal, sumDetalle);
		if(!regresar) {
			LOG.warn("Diferencias en los importes de la factura: "+ this.orden.getIdVenta()+ " verificar situacion, total ["+ sumTotal+ "] detalle["+ sumDetalle+ "]");
			throw new KajoolBaseException("No se puede timbrar porque el importe total difiere de los importes del detalle de la factura !");	
		} // if	
		return regresar;
	}	// checkTotal
} 