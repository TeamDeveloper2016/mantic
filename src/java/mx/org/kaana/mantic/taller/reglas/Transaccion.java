package mx.org.kaana.mantic.taller.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;
import org.hibernate.Session;

public class Transaccion extends TransaccionFactura{

	private static final Long ELABORADA= 1L;
	private Totales totales;
	private IBaseDto dto;
  private RegistroServicio registroServicio;
  private String messageError;
	private List<Articulo> articulos;
	private Long idServicio;	

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	} // Transaccion
	
  public Transaccion(RegistroServicio registroServicio) {
    this.registroServicio = registroServicio;
  } // Transaccion

	public Transaccion(List<Articulo> articulos, Long idServicio, Totales totales) {
		this.articulos = articulos;
		this.idServicio= idServicio;		
		this.totales   = totales;
	} // Transaccion
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar                     = false;
		TcManticServiciosDto servicio        = null;
		TcManticServiciosBitacoraDto bitacora= null;
    try {
			if(this.registroServicio!= null)
				this.registroServicio.getServicio().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      switch (accion) {
        case AGREGAR:
          regresar= procesarServicio(sesion);
          break;
        case MODIFICAR:
          regresar= actualizarServicio(sesion);
          break;
        case ELIMINAR:
          regresar= eliminarServicio(sesion);
          break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
				case JUSTIFICAR:
					bitacora= (TcManticServiciosBitacoraDto) this.dto;
					bitacora.setConsecutivo(this.toSiguiente(sesion, bitacora.getIdServicio()).getConsecutivo());
					if(DaoFactory.getInstance().insert(sesion, bitacora)>= 1L){
						servicio= (TcManticServiciosDto) DaoFactory.getInstance().findById(sesion, TcManticServiciosDto.class, bitacora.getIdServicio());
						servicio.setIdServicioEstatus(bitacora.getIdServicioEstatus());
						regresar= DaoFactory.getInstance().update(sesion, servicio)>= 1L;
					} // if
					break;
				case COMPLEMENTAR:
					if(actualizarTotales(sesion))
						regresar= registrarDetalle(sesion);
					break;
      } // switch
      if (!regresar) {
        throw new Exception("");
      } // if
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
	} // ejecutar	
	
	private boolean procesarServicio(Session sesion) throws Exception {
		boolean regresar= false;
		Long idCliente  = -1L;		
		Long idServicio = -1L;		
		Siguiente consecutivo= null;
    try {
      this.messageError = "Error al registrar el servicio";
			consecutivo= toSiguiente(sesion);
			this.registroServicio.getServicio().setConsecutivo(consecutivo.getConsecutivo());
			this.registroServicio.getServicio().setOrden(consecutivo.getOrden());
			this.registroServicio.getServicio().setEjercicio(new Long(Fecha.getAnioActual()));
			this.registroServicio.getServicio().setIdUsuario(JsfBase.getIdUsuario());
			this.registroServicio.getServicio().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.registroServicio.getServicio().setIdServicioEstatus(ELABORADA);
			this.registroServicio.getServicio().setIdCliente(this.registroServicio.getCliente().getIdCliente().equals(-1L) ? null : this.registroServicio.getCliente().getIdCliente());			
			idServicio= DaoFactory.getInstance().insert(sesion, this.registroServicio.getServicio());
			if(idServicio>= 1L){
				if(DaoFactory.getInstance().insert(sesion, loadBitacora(sesion, idServicio, this.registroServicio.getServicio().getObservaciones()))>= 1L){
					if(this.registroServicio.isRegistrarCliente()){
						if(this.registroServicio.getCliente().getIdCliente()== null || this.registroServicio.getCliente().getIdCliente().equals(-1L) || this.registroServicio.getCliente().getIdCliente().equals(this.registroServicio.getIdClienteDefault())){
							idCliente= registraCliente(sesion);
							this.registroServicio.getServicio().setIdCliente(idCliente);
							regresar= DaoFactory.getInstance().update(sesion, this.registroServicio.getServicio())>= 1L;
							sesion.flush();
							registraClienteFacturama(sesion, idCliente);
						} // if			
						else{
							registraContactos(sesion, this.registroServicio.getServicio().getIdCliente());
							sesion.flush();
							actualizarClienteFacturama(sesion, this.registroServicio.getServicio().getIdCliente());
							regresar= true;
						} // else
					} // if
					else
						regresar= true;
				} // if			
			} // if
    } // try
    catch (Exception e) {
			this.messageError= "Ocurrio un error al registrar el cliente";
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente
	
	private void registraClienteFacturama(Session sesion, Long idCliente){		
		CFDIGestor gestor     = null;
		ClienteFactura cliente= null;
		try {
			gestor= new CFDIGestor(idCliente);
			cliente= gestor.toClienteFactura(sesion);
			setCliente(cliente);
			super.procesarCliente(sesion);
		} // try
		catch (Exception e) {			
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch		
	} // registraArticuloFacturama
	
	private TcManticServiciosBitacoraDto loadBitacora(Session sesion, Long idServicio, String observaciones) throws Exception{
		return loadBitacora(sesion, ELABORADA, idServicio, observaciones);
	} // loadBitacora
	
	private TcManticServiciosBitacoraDto loadBitacora(Session sesion, Long idServicioEstatus, Long idServicio, String observaciones) throws Exception{
		TcManticServiciosBitacoraDto regresar= new TcManticServiciosBitacoraDto();
		regresar.setIdServicio(idServicio);
		regresar.setIdServicioEstatus(idServicioEstatus);
		regresar.setIdUsuario(JsfBase.getIdUsuario());
		regresar.setSeguimiento(observaciones);
		regresar.setConsecutivo(this.toSiguiente(sesion, idServicio).getConsecutivo());
		return regresar;
	} // loadBitacora

	private Long registraCliente(Session sesion) throws Exception{
		this.registroServicio.getCliente().setLimiteCredito(0.0D);
		this.registroServicio.getCliente().setIdTipoVenta(EBooleanos.SI.getIdBooleano());
		this.registroServicio.getCliente().setIdCredito(EBooleanos.NO.getIdBooleano());
		this.registroServicio.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		Long idCliente= DaoFactory.getInstance().insert(sesion, this.registroServicio.getCliente());
		int count     =  0;
		for(TrManticClienteTipoContactoDto contacto: loadContactos()){
			contacto.setIdCliente(idCliente);
			contacto.setIdUsuario(JsfBase.getIdUsuario());
			contacto.setOrden(Long.valueOf(count++));
			DaoFactory.getInstance().insert(sesion, contacto);
		}	//for
		return idCliente;
	} // registraCliente
	
	private List<TrManticClienteTipoContactoDto> loadContactos(){
		List<TrManticClienteTipoContactoDto> regresar= new ArrayList<>();
		TrManticClienteTipoContactoDto pivote= new TrManticClienteTipoContactoDto();
		pivote.setIdTipoContacto(ETiposContactos.TELEFONO.getKey());
		pivote.setValor(this.registroServicio.getContactoCliente().getTelefono());
		regresar.add(pivote);
		pivote= new TrManticClienteTipoContactoDto();
		pivote.setIdTipoContacto(ETiposContactos.CORREO.getKey());
		pivote.setValor(this.registroServicio.getContactoCliente().getEmail());
		regresar.add(pivote);
		return regresar;
	} // loadContactos
	
  private boolean actualizarServicio(Session sesion) throws Exception {		
    boolean regresar         = false;
		Long idCliente           = -1L;	
		boolean registrarCliente = false;
		boolean actualizarCliente= false;
    try {
			if(this.registroServicio.isRegistrarCliente()){
				if(this.registroServicio.getCliente().getIdCliente()== null || this.registroServicio.getCliente().getIdCliente().equals(-1L) || this.registroServicio.getCliente().getIdCliente().equals(this.registroServicio.getIdClienteDefault())){
					idCliente= registraCliente(sesion);
					this.registroServicio.getServicio().setIdCliente(idCliente);				
					registrarCliente= true;
				} // if
				else{
					idCliente= this.registroServicio.getCliente().getIdCliente();					
					registraContactos(sesion, idCliente);
					this.registroServicio.getServicio().setIdCliente(idCliente);				
					actualizarCliente= true;
				} // else
			} // if
			else
				this.registroServicio.getServicio().setIdCliente(this.registroServicio.getIdClienteDefault());				
			regresar= DaoFactory.getInstance().update(sesion, this.registroServicio.getServicio())>= 1L;      
			if(registrarCliente){
				sesion.flush();
				registraClienteFacturama(sesion, idCliente);
			} // if
			if(actualizarCliente){
				sesion.flush();
				actualizarClienteFacturama(sesion, idCliente);
			} // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarCliente

	private void actualizarClienteFacturama(Session sesion, Long idCliente){		
		CFDIGestor gestor     = null;
		ClienteFactura cliente= null;
		try {
			gestor= new CFDIGestor(idCliente);
			cliente= gestor.toClienteFactura(sesion);
			setCliente(cliente);
			if(cliente.getIdFacturama()!= null)
				updateCliente(sesion);
			else
				super.procesarCliente(sesion);
		} // try
		catch (Exception e) {			
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch		
	} // actualizarArticuloFacturama
	
	private void registraContactos(Session sesion, Long idCliente) throws Exception{
		List<ClienteTipoContacto> contactos= null;
		ClienteTipoContacto pivote         = null;
		int count       = 0;		
		int exist       = 0;
		try {
			contactos= toClientesTipoContacto(sesion, idCliente);
			count= contactos.size();				
			for(TrManticClienteTipoContactoDto contacto: loadContactos()){
				exist= 0;
				if(!contactos.isEmpty()){
					for(ClienteTipoContacto tipoContacto: contactos){
						if(tipoContacto.getIdTipoContacto().equals(contacto.getIdTipoContacto())){
							exist++;
							pivote= tipoContacto;
							pivote.setValor(contacto.getValor());
							pivote.setIdUsuario(JsfBase.getIdUsuario());
						} // if
					} // for
				} // if					
				if(exist== 0){
					contacto.setIdCliente(idCliente);
					contacto.setIdUsuario(JsfBase.getIdUsuario());
					contacto.setOrden(Long.valueOf(count++));
					DaoFactory.getInstance().insert(sesion, contacto);
				} // if
				else
					DaoFactory.getInstance().update(sesion, (TrManticClienteTipoContactoDto) pivote);				
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
	} // registraContactos
	
  private boolean eliminarServicio(Session sesion) throws Exception {
    boolean regresar = false;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idServicio", this.registroServicio.getIdServicio());
      if (DaoFactory.getInstance().deleteAll(sesion, TcManticServiciosBitacoraDto.class, params) > -1L) {
        if (DaoFactory.getInstance().deleteAll(sesion, TcManticServiciosDetallesDto.class, params) > -1L) {
          regresar = DaoFactory.getInstance().delete(sesion, TcManticServiciosDto.class, this.registroServicio.getIdServicio()) >= 1L;          
        } // if
      } // if
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // eliminarCliente
	
	private List<ClienteTipoContacto> toClientesTipoContacto(Session sesion, Long idCliente) throws Exception {
		List<ClienteTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + idCliente);
			regresar= DaoFactory.getInstance().toEntitySet(sesion, ClienteTipoContacto.class, "TrManticClienteTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticServiciosDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
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
	
	private boolean actualizarTotales(Session sesion) throws Exception{
		boolean regresar             = false;
		TcManticServiciosDto servicio= null;
		try {
			servicio= (TcManticServiciosDto) DaoFactory.getInstance().findById(sesion, TcManticServiciosDto.class, this.idServicio);
			servicio.setDescuento(this.totales.getDescuento$());
			servicio.setDescuentos(this.totales.getDescuentos());
			servicio.setImpuestos(this.totales.getIva());
			servicio.setSubTotal(this.totales.getSubTotal());
			servicio.setTotal(this.totales.getTotal());
			regresar= DaoFactory.getInstance().update(sesion, servicio)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizarTotales
	
	private boolean registrarDetalle(Session sesion) throws Exception{
		boolean regresar    = true;
		List<Articulo> todos= null;
		try {
			todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticServiciosDetallesDto", "detalle", new TcManticServiciosDto(this.idServicio).toMap());
			for (Articulo item: todos) 
				if(this.articulos.indexOf(item)< 0)
					DaoFactory.getInstance().delete(sesion, item);
			for (Articulo articulo: this.articulos) {
				if(articulo.isValid()){
					TcManticServiciosDetallesDto item= articulo.toServicioDetalle();
					item.setIdServicio(this.idServicio);					
					item.setIdUsuario(JsfBase.getIdUsuario());
					if(DaoFactory.getInstance().findIdentically(sesion, TcManticServiciosDetallesDto.class, item.toMap())== null) 
						DaoFactory.getInstance().insert(sesion, item);
				} // if
			} // for
		} // try
		catch (Exception e) {			
			throw e;	
		} // catch		
		return regresar;
	} // registrarDetalle
	
	private Siguiente toSiguiente(Session sesion, Long idServicio) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idServicio", idServicio);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticServiciosBitacoraDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
			else
			  regresar= new Siguiente(next.toLong());
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