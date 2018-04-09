package mx.org.kaana.kajool.procesos.usuarios.delegar.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 06:51:36 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import org.hibernate.Session;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.pagina.UIMessage;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TrJanalUsuariosDelegaDto;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.usuarios.reglas.GestorSQL;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

	private static final Logger LOG= Logger.getLogger(Transaccion.class);
	private Long idKey;	
	private IBaseDto dto;			
	private String cuenta;
	private String contrasenia;
	
	public Transaccion(Long idKey) {
		this(null, idKey);
	} // Trasaccion
	
	public Transaccion(IBaseDto dto) {
		this(dto, -1L);
	} // Transaccion

	public Transaccion(IBaseDto dto, Long idKey) {
		this(dto, idKey, null, null);
	} // Transaccion
	
	public Transaccion(IBaseDto dto, Long idKey, String cuenta, String contrasenia) {
		this.dto        = dto;
		this.idKey      = idKey;
		this.cuenta     = cuenta;
		this.contrasenia= contrasenia;
	} // Transaccion

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar         = false;
		Map<String, Object>params= null;
		try {
			switch (accion) {								
				case ACTIVAR:
					regresar= activarUsuarioDelega(sesion);					
					break;
				case AGREGAR:
					regresar= procesarUsuarioDelega(sesion);
					break;
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, TrJanalUsuariosDelegaDto.class, this.idKey)>= 1L;															
					break;
				case DESACTIVAR:					
					params= new HashMap<>();
					params.put("activo", 0L);
					regresar= DaoFactory.getInstance().update(sesion, TrJanalUsuariosDelegaDto.class, this.idKey, params)>= 1L;
					break;				
			} // switch
			if(!regresar)
				throw new KajoolBaseException(UIMessage.toMessage("error_".concat(accion.name().toLowerCase())));					
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;			
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	}// ejecutar

	private boolean activarUsuarioDelega(Session sesion) throws Exception{
		boolean regresar               = false;
		TrJanalUsuariosDelegaDto delega= null;
		try {
			delega= (TrJanalUsuariosDelegaDto) DaoFactory.getInstance().findById(sesion, TrJanalUsuariosDelegaDto.class, this.idKey);
			if(delega!= null){
				delega.setActivo(1L);
				delega.setVigenciaIni(vigenciaInicio());
				delega.setVigenciaFin(vigenciaFin());
				regresar= DaoFactory.getInstance().update(sesion, delega)>= 1L;
			} // if					
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // activarUsuarioDelega
	
	private boolean procesarUsuarioDelega(Session sesion) throws Exception{
		boolean regresar= false;
		Long keyPersona = -1L;
		try {			
			keyPersona= this.dto.getKey().equals(-1L) ? insertDto(sesion, this.dto) : this.dto.getKey();			
			regresar= verificaEmpleado(sesion, keyPersona);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // procesarUsuarioDelega

	private Long insertDto(Session sesion, IBaseDto insertDto) throws Exception{
		return DaoFactory.getInstance().insert(sesion, insertDto);
	} // insertDto
	
	private boolean verificaEmpleado(Session sesion, Long keyPersona) throws Exception {		
		GestorSQL gestor= null;
		Entity empleado = null;	
		boolean regresar= false;
		Long keyEmpleado= -1L;
		try {			
			gestor= new GestorSQL(this.idKey, keyPersona);
			empleado= gestor.toEmpleado(sesion);
			if (empleado!= null && !empleado.isEmpty()){				
				keyEmpleado= empleado.toLong("idEmpleado");
				if (gestor.validaUsuarioDelega(sesion, keyEmpleado))
					regresar= addUsuarioDelega(sesion, keyEmpleado);				
				else
					regresar= activarUsuarioDelega(sesion, gestor.getIdUsuarioDelega(sesion, keyEmpleado));				
			} // if
			else{
				sesion.flush();
				keyEmpleado= addEmpleado(sesion, keyPersona);			
				regresar= addUsuarioDelega(sesion, keyEmpleado);
			} // else
		} // try
		catch (Exception e) {
			throw e;
		} // catch				
		return regresar;
	} // verificaEmpleado

	private boolean addUsuarioDelega(Session sesion, Long keyEmpleado) throws Exception {
		boolean regresar               = false;		
		TrJanalUsuariosDelegaDto delega= null;		
		try {
			delega  = loadUsuarioDelega(keyEmpleado);			
			regresar= insertDto(sesion, delega) >= 1L;			
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // addUsuarioDelega

	private TrJanalUsuariosDelegaDto loadUsuarioDelega(Long keyEmpleado) throws Exception {
		TrJanalUsuariosDelegaDto regresar= null;		
		try {
			regresar= new TrJanalUsuariosDelegaDto();
			regresar.setIdUsuario(this.idKey);
			regresar.setIdEmpleado(keyEmpleado);
			regresar.setActivo(1L);
			regresar.setLogin(this.cuenta);			
			regresar.setContrasenia(BouncyEncryption.encrypt(this.contrasenia));
			regresar.setVigenciaFin(vigenciaFin());
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	} // loadUsuarioDelega	
	
	private Long addEmpleado(Session sesion, Long idEmpleado) throws Exception{
		Long regresar                = -1L;
		TcJanalEmpleadosDto empleado = null;						
		TcJanalEmpleadosDto duplicado= null;						
		TcJanalUsuariosDto usuario   = null;
		try {
			usuario= (TcJanalUsuariosDto) DaoFactory.getInstance().findById(sesion, TcJanalUsuariosDto.class, this.idKey);
			if(usuario!= null){
				//empleado= (TcJanalEmpleadosDto) DaoFactory.getInstance().findById(sesion, TcJanalEmpleadosDto.class, usuario.getIdEmpleado());
				duplicado= new TcJanalEmpleadosDto();				
				duplicado.setIdEmpleado(idEmpleado);
				duplicado.setIdEmpleado(this.idKey);				
				duplicado.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));				
				regresar= insertDto(sesion, duplicado);
			} // if			
		} // try
		catch (Exception e) {						
			throw e;
		} // catch				
		return regresar;		
	}	// addEmpleado	
	
	private boolean activarUsuarioDelega (Session sesion, Long keyUsuarioDelega) throws Exception{
		boolean regresar               = false;
		TrJanalUsuariosDelegaDto delega= null;				
		try {
			delega= (TrJanalUsuariosDelegaDto) DaoFactory.getInstance().findById(sesion, TrJanalUsuariosDelegaDto.class, keyUsuarioDelega);			
			if(delega!= null){								
				delega.setActivo(1L);
				delega.setVigenciaIni(vigenciaInicio());
				delega.setVigenciaFin(vigenciaFin());
				regresar= DaoFactory.getInstance().update(sesion, delega)>= 1L;
			} // if
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // activarUsuarioDelega
	
	private Date vigenciaInicio() throws Exception{		
		return vigenciaFin(-1);
	} // vigenciaInicio
	
	private Date vigenciaFin() throws Exception{
		return vigenciaFin(15);
	} // vigenciaFin
	
	private Date vigenciaFin(int dias) throws Exception{
		Date regresar= null;		
		try {			
			regresar= new Date(Calendar.getInstance().getTimeInMillis());
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(regresar);
			calendar.add(Calendar.DATE, dias);
			regresar= new Date(calendar.getTimeInMillis());
		} // try
		catch (Exception e) {						
			throw e;
		} // catch	
		return regresar;
	} // vigenciaFin				
}
