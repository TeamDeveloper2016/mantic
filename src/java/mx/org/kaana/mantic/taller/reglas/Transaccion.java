package mx.org.kaana.mantic.taller.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private static final Long ELABORADA= 1L;
	private IBaseDto dto;
  private RegistroServicio registroServicio;
  private String messageError;
	private List<Articulo> articulos;
	private Long idServicio;
	private Long idTrabajo;

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	}
	
  public Transaccion(RegistroServicio registroServicio) {
    this.registroServicio = registroServicio;
  }

	public Transaccion(List<Articulo> articulos, Long idServicio, Long idTrabajo) {
		this.articulos = articulos;
		this.idServicio= idServicio;
		this.idTrabajo = idTrabajo;
	}
	
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
					bitacora.setConsecutivo(Cadena.rellenar(toSiguiente(sesion, bitacora.getIdServicio()).toString(), 5, '0', true));
					if(DaoFactory.getInstance().insert(sesion, bitacora)>= 1L){
						servicio= (TcManticServiciosDto) DaoFactory.getInstance().findById(sesion, TcManticServiciosDto.class, bitacora.getIdServicio());
						servicio.setIdServicioEstatus(bitacora.getIdServicioEstatus());
						regresar= DaoFactory.getInstance().update(sesion, servicio)>= 1L;
					} // if
					break;
				case COMPLEMENTAR:
					regresar= registrarDetalle(sesion);
					break;
      } // switch
      if (!regresar) {
        throw new Exception("");
      } // if
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
    } // catch		
    return regresar;
	} // ejecutar	
	
	private boolean procesarServicio(Session sesion) throws Exception {
		boolean regresar= false;
		Long idCliente  = -1L;		
		Long idServicio = -1L;		
		Long consecutivo= -1L;
    try {
      this.messageError = "Error al registrar el articulo";
			consecutivo= toSiguiente(sesion);
			this.registroServicio.getServicio().setConsecutivo(Fecha.getAnioActual() + Cadena.rellenar(consecutivo.toString(), 5, '0', true));
			this.registroServicio.getServicio().setOrden(consecutivo);
			this.registroServicio.getServicio().setEjercicio(new Long(Fecha.getAnioActual()));
			this.registroServicio.getServicio().setIdUsuario(JsfBase.getIdUsuario());
			this.registroServicio.getServicio().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.registroServicio.getServicio().setIdServicioEstatus(ELABORADA);
			this.registroServicio.getServicio().setIdCliente(this.registroServicio.getCliente().getIdCliente());			
			idServicio= DaoFactory.getInstance().insert(sesion, this.registroServicio.getServicio());
			if(idServicio>= 1L){
				if(DaoFactory.getInstance().insert(sesion, loadBitacora(sesion, idServicio, this.registroServicio.getServicio().getObservaciones()))>= 1L){
					if(this.registroServicio.getServicio().getIdCliente()== null || this.registroServicio.getServicio().getIdCliente().equals(-1L)){
						idCliente= registraCliente(sesion);
						this.registroServicio.getServicio().setIdCliente(idCliente);
						regresar= DaoFactory.getInstance().update(sesion, this.registroServicio.getServicio())>= 1L;
					} // if			
					else{
						registraContactos(sesion, this.registroServicio.getServicio().getIdCliente());
						regresar= true;
					} // else
				} // if			
			} // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente
	
	private TcManticServiciosBitacoraDto loadBitacora(Session sesion, Long idServicio, String observaciones) throws Exception{
		return loadBitacora(sesion, ELABORADA, idServicio, observaciones);
	} // loadBitacora
	
	private TcManticServiciosBitacoraDto loadBitacora(Session sesion, Long idServicioEstatus, Long idServicio, String observaciones) throws Exception{
		TcManticServiciosBitacoraDto regresar= new TcManticServiciosBitacoraDto();
		regresar.setIdServicio(idServicio);
		regresar.setIdServicioEstatus(idServicioEstatus);
		regresar.setIdUsuario(JsfBase.getIdUsuario());
		regresar.setSeguimiento(observaciones);
		regresar.setConsecutivo(Cadena.rellenar((toSiguiente(sesion, idServicio)).toString(), 5, '0', true));
		return regresar;
	} // loadBitacora

	private Long registraCliente(Session sesion) throws Exception{
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
    boolean regresar= false;
		Long idCliente  = -1L;		
    try {
			if(this.registroServicio.getServicio().getIdCliente()== null || this.registroServicio.getServicio().getIdCliente().equals(-1L)){
				idCliente= registraCliente(sesion);
				this.registroServicio.getServicio().setIdCliente(idCliente);				
			} // if
			else{
				idCliente= this.registroServicio.getServicio().getIdCliente();
				registraContactos(sesion, idCliente);
			} // else
			regresar= DaoFactory.getInstance().update(sesion, this.registroServicio.getServicio())>= 1L;      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarCliente

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
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticServiciosDto", "siguiente", params, "siguiente");
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
					item.setIdTrabajo(this.idTrabajo);
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
	
	private Long toSiguiente(Session sesion, Long idServicio) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idServicio", idServicio);
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticServiciosBitacoraDto", "siguiente", params, "siguiente");
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