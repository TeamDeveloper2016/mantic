package mx.org.kaana.mantic.catalogos.personas.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.procesos.usuarios.reglas.RandomCuenta;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaDomicilio;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.catalogos.personas.beans.RegistroPersona;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import mx.org.kaana.mantic.db.dto.TrManticEmpresaPersonalDto;
import mx.org.kaana.mantic.db.dto.TrManticPersonaDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticPersonaTipoContactoDto;
import org.hibernate.Session;

public class Transaccion  extends IBaseTnx{

	private IBaseDto dto;
	private RegistroPersona persona;	
	private String messageError;
	private String cuenta;
	private Long idEmpresa;
	private Long idPuesto;

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	}
	
	public Transaccion(RegistroPersona persona) {
		this(persona, null, null);
	} // Transaccion
	
	public Transaccion(RegistroPersona persona, Long idEmpresa, Long idPuesto) {
		this.persona  = persona;		
		this.idEmpresa= idEmpresa;
		this.idPuesto = idPuesto;
	} // Transaccion

	public String getCuenta() {
		return cuenta;
	}		
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el registro de la persona");
			switch(accion){
				case AGREGAR:
					regresar = procesarCliente(sesion);					
					break;
				case MODIFICAR:
					regresar = actualizarCliente(sesion);					
					break;				
				case ELIMINAR:
					regresar = eliminarCliente(sesion);					
					break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception(this.messageError);
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError);
		} // catch		
		return regresar;
	}	// ejecutar
	
	private void toCuenta(){
		RandomCuenta random= null;
		try {
			random= new RandomCuenta(this.persona.getPersona().getNombres(), this.persona.getPersona().getPaterno(), this.persona.getPersona().getMaterno(), -1L);
      this.cuenta= random.getCuentaGenerada();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
	} // toCuenta
	
	private boolean procesarCliente(Session sesion) throws Exception {
    boolean regresar   = false;
    Long idPersona     = -1L;
		Encriptar encriptar= null;
    try {
      this.messageError = "Error al registrar el articulo";
      if (eliminarRegistros(sesion)) {
				encriptar= new Encriptar();
				toCuenta();
				this.persona.getPersona().setContrasenia(encriptar.encriptar(this.persona.getPersona().getRfc()));
				this.persona.getPersona().setCuenta(this.cuenta);
        this.persona.getPersona().setIdUsuario(JsfBase.getIdUsuario());
        idPersona = DaoFactory.getInstance().insert(sesion, this.persona.getPersona());
				if(registraPersonaEmpresa(sesion, idPersona)){
					if (registraPersonasDomicilios(sesion, idPersona)) {
						regresar = registraPersonasTipoContacto(sesion, idPersona);
					} // if
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente
	
	private boolean actualizarCliente(Session sesion) throws Exception {
    boolean regresar= false;
    Long idPersona  = -1L;
    try {
      idPersona= this.persona.getIdPersona();
			this.cuenta= this.persona.getPersona().getCuenta();
      if (registraPersonasDomicilios(sesion, idPersona)) {
				if (registraPersonasTipoContacto(sesion, idPersona)) {
					regresar = DaoFactory.getInstance().update(sesion, this.persona.getPersona()) >= 1L;
				} // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarCliente

  private boolean eliminarCliente(Session sesion) throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
    try {
      params = new HashMap<>();
      params.put("idPersona", this.persona.getIdPersona());
      if (DaoFactory.getInstance().deleteAll(sesion, TrManticPersonaDomicilioDto.class, params) > -1L) {
				if (DaoFactory.getInstance().deleteAll(sesion, TrManticPersonaTipoContactoDto.class, params) > -1L) {
					if (DaoFactory.getInstance().deleteAll(sesion, TrManticEmpresaPersonalDto.class, params) > -1L) {
						regresar = DaoFactory.getInstance().delete(sesion, TcManticPersonasDto.class, this.persona.getIdPersona()) >= 1L;
					} // if
				} // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // eliminarCliente
	
	private boolean registraPersonaEmpresa(Session sesion, Long idPersona) throws Exception{
		boolean regresar                          = false;
		TrManticEmpresaPersonalDto empresaPersonal= null;
		try {
			empresaPersonal= new TrManticEmpresaPersonalDto();
			empresaPersonal.setIdPersona(idPersona);
			empresaPersonal.setIdEmpresa(this.idEmpresa);
			empresaPersonal.setIdPuesto(this.idPuesto);
			empresaPersonal.setIdUsuario(JsfBase.getIdUsuario());
			regresar= DaoFactory.getInstance().insert(sesion, empresaPersonal)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registraPersonaEmpresa
	
	private boolean registraPersonasDomicilios(Session sesion, Long idPersona) throws Exception {
    TrManticPersonaDomicilioDto dto= null;
    ESql sqlAccion= null;
    int count= 0;
    boolean validate= false;
    boolean regresar= false;
    try {
      for (PersonaDomicilio clienteDomicilio : this.persona.getPersonasDomicilio()) {
        clienteDomicilio.setIdPersona(idPersona);
        clienteDomicilio.setIdUsuario(JsfBase.getIdUsuario());
        dto = (TrManticPersonaDomicilioDto) clienteDomicilio;
        sqlAccion = clienteDomicilio.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdPersonaDomicilio(-1L);
            validate = registrar(sesion, dto);
            break;
          case UPDATE:
            validate = actualizar(sesion, dto);
            break;
        } // switch
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.persona.getPersonasDomicilio().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los domicilios, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesDomicilios

  private boolean registraPersonasTipoContacto(Session sesion, Long idPersona) throws Exception {
    TrManticPersonaTipoContactoDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (PersonaTipoContacto personaTipoContacto : this.persona.getPersonasTiposContacto()) {
        personaTipoContacto.setIdPersona(idPersona);
        personaTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
        dto = (TrManticPersonaTipoContactoDto) personaTipoContacto;
        sqlAccion = personaTipoContacto.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdPersonaTipoContacto(-1L);
            validate = registrar(sesion, dto);
            break;
          case UPDATE:
            validate = actualizar(sesion, dto);
            break;
        } // switch
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.persona.getPersonasTiposContacto().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los tipos de contacto, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesTipoContacto
	
	private boolean eliminarRegistros(Session sesion) throws Exception {
    boolean regresar = true;
    int count = 0;
    try {
      for (IBaseDto dto : this.persona.getDeleteList()) {
        if (DaoFactory.getInstance().delete(sesion, dto) >= 1L) {
          count++;
        }
      } // for
      regresar = count == this.persona.getDeleteList().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al eliminar registros";
    } // finally
    return regresar;
  } // eliminarRegistros

  private boolean registrar(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().insert(sesion, dto) >= 1L;
  } // registrar

  private boolean actualizar(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().update(sesion, dto) >= 1L;
  } // actualizar
}