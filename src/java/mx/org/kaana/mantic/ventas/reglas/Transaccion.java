package mx.org.kaana.mantic.ventas.reglas;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteDomicilio;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDetallesDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoVenta;
import mx.org.kaana.mantic.ventas.beans.ClienteVenta;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx {

  private static final Logger LOG  = Logger.getLogger(Transaccion.class);
	private static final String VENTA= "VENTA";
	private ClienteVenta clienteVenta;
	private TcManticVentasBitacoraDto bitacora;
	private TcManticVentasDto orden;	
	private List<Articulo> articulos;
	private String messageError;	
	private String justificacion;
	private Long idClienteNuevo;
	private Date vigencia;
	private boolean aplicar;

	public Transaccion(TcManticVentasBitacoraDto bitacora) {
		this.bitacora= bitacora;
	} // Transaccion
	
	public Transaccion(TcManticVentasDto orden) {
		this(orden, "");
	}
	
	public Transaccion(TcManticVentasDto orden, String justificacion) {
		this(orden, new ArrayList<Articulo>(), justificacion);
	} // Transaccion

	public Transaccion(TcManticVentasDto orden, List<Articulo> articulos) {
		this(orden, articulos, new Date(Calendar.getInstance().getTimeInMillis()));
	}
	
	public Transaccion(TcManticVentasDto orden, List<Articulo> articulos, Date vigencia) {
		this(orden, articulos, "", vigencia);
	}
	
	public Transaccion(TcManticVentasDto orden, List<Articulo> articulos, String justificacion) { 
		this(orden, articulos, justificacion, new Date(Calendar.getInstance().getTimeInMillis()));
	}
	
	public Transaccion(TcManticVentasDto orden, List<Articulo> articulos, String justificacion, Date vigencia) {
		this.orden        = orden;		
		this.articulos    = articulos;
		this.justificacion= justificacion;
		this.vigencia     = vigencia;
		this.aplicar      = false;
	} // Transaccion

	public Transaccion(ClienteVenta clienteVenta) {
		this.clienteVenta = clienteVenta;
	}	
	
	public String getMessageError() {
		return messageError;
	} // Transaccion

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}	
	
	public TcManticVentasDto getOrden() {
		return orden;
	}	

	public void setClienteVenta(ClienteVenta clienteVenta) {
		this.clienteVenta = clienteVenta;
	}	

	public Long getIdClienteNuevo() {
		return idClienteNuevo;
	}

	public boolean isAplicar() {
		return aplicar;
	}

	public void setAplicar(boolean aplicar) {
		this.aplicar = aplicar;
	}	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		Map<String, Object> params= null;
		Long idEstatusVenta       = null;
		Long consecutivo          = 1L;
		try {
			idEstatusVenta= EEstatusVentas.ELABORADA.getIdEstatusVenta();
			params= new HashMap<>();
			if(this.orden!= null)
				params.put("idVenta", this.orden.getIdVenta());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el ticket de venta.");
			switch(accion) {
				case GENERAR:
					idEstatusVenta= EEstatusVentas.COTIZACION.getIdEstatusVenta();
					this.orden.setVigencia(this.vigencia);
					regresar= this.orden.getIdVenta()!= null && !this.orden.getIdVenta().equals(-1L) ? actualizarVenta(sesion, idEstatusVenta) : registrarVentaCotizacion(sesion, idEstatusVenta, true);					
					break;
				case AGREGAR:
				case REGISTRAR:			
				case DESACTIVAR:
					idEstatusVenta= accion.equals(EAccion.AGREGAR) ? EEstatusVentas.ABIERTA.getIdEstatusVenta() : (accion.equals(EAccion.DESACTIVAR) ? this.orden.getIdVentaEstatus() : idEstatusVenta);
					regresar= this.orden.getIdVenta()!= null && !this.orden.getIdVenta().equals(-1L) ? actualizarVenta(sesion, idEstatusVenta) : registrarVenta(sesion, idEstatusVenta);					
					break;
				case MODIFICAR:
					regresar= actualizarVenta(sesion, EEstatusVentas.ABIERTA.getIdEstatusVenta());					
					break;				
				case ELIMINAR:
					idEstatusVenta= EEstatusVentas.CANCELADA.getIdEstatusVenta();
					this.orden= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.orden.getIdVenta());
					this.orden.setIdVentaEstatus(idEstatusVenta);					
					if(DaoFactory.getInstance().update(sesion, this.orden)>= 1L)
						regresar= registraBitacora(sesion, this.orden.getIdVenta(), idEstatusVenta, this.justificacion);					
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L){
						this.orden= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.bitacora.getIdVenta());
						this.orden.setIdVentaEstatus(this.bitacora.getIdVentaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					} // if
					break;				
				case ASIGNAR:
					regresar= procesarCliente(sesion);
					break;					
				case TRANSFORMACION:
					regresar= actualizarCliente(sesion);
					break;
				case REPROCESAR:
				case COPIAR:
					regresar= actualizarVenta(sesion, accion.equals(EAccion.REPROCESAR) ? EEstatusVentas.PAGADA.getIdEstatusVenta() : EEstatusVentas.CREDITO.getIdEstatusVenta());				
					break;		
				case NO_APLICA:
					params= new HashMap<>();
					params.put("idVenta", this.orden.getIdVenta());
					if(DaoFactory.getInstance().deleteAll(sesion, TcManticVentasBitacoraDto.class, params)>= 0){
						if(DaoFactory.getInstance().deleteAll(sesion, TcManticVentasDetallesDto.class, params)>= 0)
							regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					} // if					
					break;
				case PROCESAR:				
					consecutivo= this.toSiguiente(sesion);			
					this.orden.setConsecutivo(consecutivo);			
					this.orden.setOrden(consecutivo);
					this.orden.setIdUsuario(JsfBase.getIdUsuario());
					if(this.orden.isValid())
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					else
						regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					if(regresar){
						regresar= registraBitacora(sesion, this.orden.getIdVenta(), this.orden.getIdVentaEstatus(), "Registro de venta express");
						if(regresar && !this.aplicar)
							registrarDeuda(sesion, this.orden.getTotal());
					} // if					
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		if(this.orden!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar

	private boolean registrarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		return registrarVentaCotizacion(sesion, idEstatusVenta, false);
	}
	
	private boolean registrarVentaCotizacion(Session sesion, Long idEstatusVenta, boolean cotizacion) throws Exception{
		boolean regresar          = false;
		Long consecutivo          = -1L;
		Long consecutivoCotizacion= -1L;
		try {			
			if(cotizacion){
				consecutivoCotizacion= this.toSiguienteCotizacion(sesion);
				this.orden.setCcotizacion(consecutivoCotizacion);
				this.orden.setCotizacion(Fecha.getAnioActual() + Cadena.rellenar(consecutivoCotizacion.toString(), 5, '0', true));
			} // if
			consecutivo= this.toSiguiente(sesion);			
			this.orden.setConsecutivo(consecutivo);			
			this.orden.setOrden(consecutivo);
			this.orden.setIdVentaEstatus(idEstatusVenta);
			this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
			if(this.orden.getIdCliente()< 0)
				this.orden.setIdCliente(toClienteDefault(sesion));
			regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
			regresar= registraBitacora(sesion, this.orden.getIdVenta(), idEstatusVenta, "");
			toFillArticulos(sesion);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // registrarVenta
	
	private boolean actualizarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		try {						
			this.orden.setIdVentaEstatus(idEstatusVenta);			
			if(this.orden.getIdCliente()< 0)
				this.orden.setIdCliente(toClienteDefault(sesion));
			regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
			if(registraBitacora(sesion, this.orden.getIdVenta(), idEstatusVenta, "")){
				params= new HashMap<>();
				params.put("idVenta", this.orden.getIdVenta());
				regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticVentasDetallesDto.class, params)>= 1;
				toFillArticulos(sesion);
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally			
		return regresar;
	} // actualizarVenta
	
	protected boolean registraBitacora(Session sesion, Long idVenta, Long idVentaEstatus, String justificacion) throws Exception{
		boolean regresar                  = false;
		TcManticVentasBitacoraDto bitVenta= null;
		try {
			bitVenta= new TcManticVentasBitacoraDto(-1L, justificacion, JsfBase.getIdUsuario(), idVenta, idVentaEstatus, this.orden.getConsecutivo(), this.orden.getTotal());
			regresar= DaoFactory.getInstance().insert(sesion, bitVenta)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacora
	
	protected void toFillArticulos(Session sesion) throws Exception {
		toFillArticulos(sesion, this.articulos);
	} // toFillArticulos
	
	protected void toFillArticulos(Session sesion, List<Articulo> detalleArt) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticVentasDetallesDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(detalleArt.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item);
		for (Articulo articulo: detalleArt) {
			if(articulo.isValid()){
				TcManticVentasDetallesDto item= articulo.toVentaDetalle();
				item.setIdVenta(this.orden.getIdVenta());
				if(DaoFactory.getInstance().findIdentically(sesion, TcManticVentasDetallesDto.class, item.toMap())== null) 
					DaoFactory.getInstance().insert(sesion, item);
				else
					DaoFactory.getInstance().update(sesion, item);
			} // if
		} // for
	} // toFillArticulos
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("dia", Fecha.getHoyEstandar());
			params.put("idEmpresa", this.orden.getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguiente", params, "siguiente");
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
	
	private Long toSiguienteCotizacion(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", this.orden.getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguienteCotizacion", params, "siguiente");
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
	
	protected Long toClienteDefault(Session sesion) throws Exception{
		Long regresar            = -1L;
		Entity cliente           = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("clave", VENTA);
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			cliente= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcManticClientesDto", "clienteDefault", params);
			if(cliente!= null)
				regresar= cliente.getKey();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClienteDefault
	
	protected boolean procesarCliente(Session sesion) throws Exception {
    boolean regresar= false;
    Long idCliente  = -1L;
    try {
      this.messageError = "Error al registrar el cliente";
			this.clienteVenta.getCliente().setIdCredito(2L);
			this.clienteVenta.getCliente().setLimiteCredito(0D);
			this.clienteVenta.getCliente().setPlazoDias(15L);
			this.clienteVenta.getCliente().setIdUsuario(JsfBase.getIdUsuario());
			this.clienteVenta.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			idCliente = DaoFactory.getInstance().insert(sesion, this.clienteVenta.getCliente());
			this.idClienteNuevo= idCliente;
			if(updateDomicilioPrincipal(sesion, this.clienteVenta.getCliente().getIdCliente())){
				if (registraClientesDomicilios(sesion, idCliente)) 
					regresar = registraClientesTipoContacto(sesion, idCliente);			
			}
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente
	
	protected boolean actualizarCliente(Session sesion) throws Exception {
    boolean regresar= false;
		TrManticClienteDomicilioDto domicilio= null;
    try {
      this.messageError = "Error al registrar el cliente";						
			if(DaoFactory.getInstance().update(sesion, this.clienteVenta.getCliente())>= 1L){
				domicilio= (TrManticClienteDomicilioDto) DaoFactory.getInstance().findById(sesion, TrManticClienteDomicilioDto.class, this.clienteVenta.getDomicilio().getIdClienteDomicilio());
				domicilio.setIdDomicilio(toIdDomicilio(sesion, toClienteDomicilio(), false));
				domicilio.setIdTipoDomicilio(this.clienteVenta.getDomicilio().getIdTipoDomicilio());
				domicilio.setIdPrincipal(1L);
				if(updateDomicilioPrincipal(sesion, this.clienteVenta.getCliente().getIdCliente())){
					if (DaoFactory.getInstance().update(sesion, domicilio)>= 1L) 
						regresar = registraClientesTipoContacto(sesion, this.clienteVenta.getCliente().getIdCliente());			
				} // if
			} // if			
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente
	
	private boolean registraClientesDomicilios(Session sesion, Long idCliente) throws Exception {
    TrManticClienteDomicilioDto dto  = null;
		ClienteDomicilio clienteDomicilio= null;
    boolean regresar                 = true;
    try {			
			clienteDomicilio= toClienteDomicilio();
			clienteDomicilio.setIdCliente(idCliente);
			clienteDomicilio.setIdUsuario(JsfBase.getIdUsuario());
			clienteDomicilio.setIdDomicilio(toIdDomicilio(sesion, clienteDomicilio));		
			clienteDomicilio.setIdClienteDomicilio(-1L);
			clienteDomicilio.setIdPrincipal(1L);
			dto= (TrManticClienteDomicilioDto) clienteDomicilio;
			regresar= DaoFactory.getInstance().insert(sesion, dto)>= 1L;			
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los domicilios, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesDomicilios
	
	private boolean updateDomicilioPrincipal(Session sesion, Long idCliente) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", idCliente);
			regresar= DaoFactory.getInstance().execute(ESql.UPDATE, sesion, "TrManticClienteDomicilioDto", "principal", params)>= 0L;
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;		
	} // updateDomicilioPrincipal
	
	private ClienteDomicilio toClienteDomicilio() throws Exception{
		ClienteDomicilio regresar= null;
		try {
			regresar= new ClienteDomicilio();
			regresar.setIdPrincipal(1L);			
			regresar.setDomicilio(this.clienteVenta.getDomicilio().getDomicilio());
			regresar.setIdDomicilio(this.clienteVenta.getDomicilio().getDomicilio().getKey());
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setIdTipoDomicilio(this.clienteVenta.getDomicilio().getIdTipoDomicilio());
			regresar.setConsecutivo(1L);
			regresar.setIdEntidad(this.clienteVenta.getDomicilio().getIdEntidad());
			regresar.setIdMunicipio(this.clienteVenta.getDomicilio().getIdMunicipio());
			regresar.setIdLocalidad(this.clienteVenta.getDomicilio().getLocalidad());
			regresar.setCodigoPostal(this.clienteVenta.getDomicilio().getCodigoPostal());
			regresar.setCalle(this.clienteVenta.getDomicilio().getCalle());
			regresar.setExterior(this.clienteVenta.getDomicilio().getNumeroExterior());
			regresar.setInterior(this.clienteVenta.getDomicilio().getNumeroInterior());
			regresar.setEntreCalle(this.clienteVenta.getDomicilio().getEntreCalle());
			regresar.setyCalle(this.clienteVenta.getDomicilio().getYcalle());
			regresar.setColonia(this.clienteVenta.getDomicilio().getAsentamiento());
			regresar.setNuevoCp(this.clienteVenta.getDomicilio().getCodigoPostal()!= null && !Cadena.isVacio(this.clienteVenta.getDomicilio().getCodigoPostal()));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // setValuesClienteDomicilio
	
	private Long toIdDomicilio(Session sesion, ClienteDomicilio clienteDomicilio) throws Exception{		
		return toIdDomicilio(sesion, clienteDomicilio, true);
	}
	
	private Long toIdDomicilio(Session sesion, ClienteDomicilio clienteDomicilio, boolean agregar) throws Exception{		
		Entity entityDomicilio= null;
		Long regresar= -1L;
		try {
			if(agregar)
				entityDomicilio= toDomicilio(sesion, clienteDomicilio);
			else
				entityDomicilio= toDomicilio(sesion, this.clienteVenta.getDomicilio());
			if(entityDomicilio!= null)
				regresar= entityDomicilio.getKey();
			else{
				regresar= insertDomicilio(sesion, clienteDomicilio);					
			}
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarDomicilio	
	
	private Long insertDomicilio(Session sesion, ClienteDomicilio clienteDomicilio) throws Exception{
		TcManticDomiciliosDto domicilio= null;
		Long regresar= -1L;
		try {
			domicilio= new TcManticDomiciliosDto();
			domicilio.setIdLocalidad(clienteDomicilio.getIdLocalidad().getKey());
			domicilio.setAsentamiento(clienteDomicilio.getColonia());
			domicilio.setCalle(clienteDomicilio.getCalle());
			domicilio.setCodigoPostal(clienteDomicilio.getCodigoPostal());
			domicilio.setEntreCalle(clienteDomicilio.getEntreCalle());
			domicilio.setIdUsuario(JsfBase.getIdUsuario());
			domicilio.setNumeroExterior(clienteDomicilio.getExterior());
			domicilio.setNumeroInterior(clienteDomicilio.getInterior());
			domicilio.setYcalle(clienteDomicilio.getyCalle());
			regresar= DaoFactory.getInstance().insert(sesion, domicilio);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // insertDomicilio
	
	private Entity toDomicilio(Session sesion, ClienteDomicilio clienteDomicilio) throws Exception{
		Entity regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idLocalidad", clienteDomicilio.getIdLocalidad().getKey());
			params.put("codigoPostal", clienteDomicilio.getCodigoPostal());
			params.put("calle", clienteDomicilio.getCalle());
			params.put("numeroExterior", clienteDomicilio.getExterior());
			params.put("numeroInterior", clienteDomicilio.getInterior());
			params.put("asentamiento", clienteDomicilio.getColonia());
			params.put("entreCalle", clienteDomicilio.getEntreCalle());
			params.put("yCalle", clienteDomicilio.getyCalle());
			regresar= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcManticDomiciliosDto", "domicilioExiste", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDomicilio
	
	private Entity toDomicilio(Session sesion, Domicilio clienteDomicilio) throws Exception{
		Entity regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idLocalidad", clienteDomicilio.getIdLocalidad());
			params.put("codigoPostal", clienteDomicilio.getCodigoPostal());
			params.put("calle", clienteDomicilio.getCalle());
			params.put("numeroExterior", clienteDomicilio.getNumeroExterior());
			params.put("numeroInterior", clienteDomicilio.getNumeroInterior());
			params.put("asentamiento", clienteDomicilio.getAsentamiento());
			params.put("entreCalle", clienteDomicilio.getEntreCalle());
			params.put("yCalle", clienteDomicilio.getYcalle());
			regresar= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcManticDomiciliosDto", "domicilioExiste", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDomicilio
	
	public boolean registraClientesTipoContacto(Session sesion, Long idCliente) throws Exception {
    int count       = 0;
    boolean validate= false;
    boolean regresar= false;
		TrManticClienteTipoContactoDto pivote= null;
    try {
      for (TrManticClienteTipoContactoDto clienteTipoContacto : this.clienteVenta.getContacto()) {
				if(clienteTipoContacto.getValor()!= null && !Cadena.isVacio(clienteTipoContacto.getValor())){
					if(clienteTipoContacto.getIdClienteTipoContacto().equals(-1L)){
						clienteTipoContacto.setIdCliente(idCliente);
						clienteTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
						clienteTipoContacto.setIdClienteTipoContacto(-1L);
						validate = registrarSentencia(sesion, clienteTipoContacto);		
					}	// if				
					else{
						pivote= (TrManticClienteTipoContactoDto) DaoFactory.getInstance().findById(sesion, TrManticClienteTipoContactoDto.class, clienteTipoContacto.getIdClienteTipoContacto());
						pivote.setValor(clienteTipoContacto.getValor());
						validate = actualizarSentencia(sesion, pivote);		
					} // else
				} // if
				else
					validate= true;
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.clienteVenta.getContacto().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los tipos de contacto, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesTipoContacto
	
	private boolean registrarSentencia(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().insert(sesion, dto) >= 1L;
  } // registrar
	
	private boolean actualizarSentencia(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().update(sesion, dto) >= 1L;
  } // registrar
	
	protected void registrarDeuda(Session sesion, Double importe) throws Exception{
		TcManticClientesDeudasDto deuda= null;
		try {
			deuda= new TcManticClientesDeudasDto();
			deuda.setIdVenta(getOrden().getIdVenta());
			deuda.setIdCliente(getOrden().getIdCliente());
			deuda.setIdUsuario(JsfBase.getIdUsuario());
			deuda.setImporte(importe);
			deuda.setSaldo(importe);
			deuda.setLimite(toLimiteCredito(sesion));
			deuda.setIdClienteEstatus(1L);
			DaoFactory.getInstance().insert(sesion, deuda);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // registrarDeuda
	
	public Date toLimiteCredito(Session sesion) throws Exception{
		Date regresar              = null;
		TcManticClientesDto cliente= null;
		Long addDias               = 15L;
		Calendar calendar          = null;
		try {			
			cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, this.orden.getIdCliente());
			addDias= cliente.getPlazoDias();			
			calendar= Calendar.getInstance();
			regresar= new Date(calendar.getTimeInMillis());			
			calendar.setTime(regresar);
			calendar.add(Calendar.DAY_OF_YEAR, addDias.intValue());
			regresar= new Date(calendar.getTimeInMillis());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toLimiteCredito
} 