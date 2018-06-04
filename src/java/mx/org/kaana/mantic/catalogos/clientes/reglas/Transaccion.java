package mx.org.kaana.mantic.catalogos.clientes.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteContactoRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteDomicilio;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.beans.RegistroCliente;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteRepresentanteDto;
import mx.org.kaana.mantic.db.dto.TrManticPersonaTipoContactoDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final String ESTILO= "sentinel";
  private IBaseDto dto;
  private RegistroCliente registroCliente;
  private String messageError;

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	}
	
  public Transaccion(RegistroCliente registroCliente) {
    this.registroCliente = registroCliente;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {
			if(this.registroCliente!= null)
				this.registroCliente.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      switch (accion) {
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
      if (!regresar) {
        throw new Exception(this.messageError);
      }
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
    } // catch		
    return regresar;
  } // ejecutar

  private boolean procesarCliente(Session sesion) throws Exception {
    boolean regresar = false;
    Long idCliente = -1L;
    try {
      this.messageError = "Error al registrar el articulo";
      if (eliminarRegistros(sesion)) {
        this.registroCliente.getCliente().setIdUsuario(JsfBase.getIdUsuario());
        this.registroCliente.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
        idCliente = DaoFactory.getInstance().insert(sesion, this.registroCliente.getCliente());
        if (registraClientesDomicilios(sesion, idCliente)) {
          if (registraClientesRepresentantes(sesion, idCliente)) {
            regresar = registraClientesTipoContacto(sesion, idCliente);
          }
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente

  private boolean actualizarCliente(Session sesion) throws Exception {
    boolean regresar = false;
    Long idCliente = -1L;
    try {
      idCliente = this.registroCliente.getIdCliente();
      if (registraClientesDomicilios(sesion, idCliente)) {
        if (registraClientesRepresentantes(sesion, idCliente)) {
          if (registraClientesTipoContacto(sesion, idCliente)) {
            regresar = DaoFactory.getInstance().update(sesion, this.registroCliente.getCliente()) >= 1L;
          }
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarCliente

  private boolean eliminarCliente(Session sesion) throws Exception {
    boolean regresar = false;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idCliente", this.registroCliente.getIdCliente());
      if (DaoFactory.getInstance().deleteAll(sesion, TrManticClienteDomicilioDto.class, params) > -1L) {
        if (DaoFactory.getInstance().deleteAll(sesion, TrManticClienteRepresentanteDto.class, params) > -1L) {
          if (DaoFactory.getInstance().deleteAll(sesion, TrManticClienteTipoContactoDto.class, params) > -1L) {
            regresar = DaoFactory.getInstance().delete(sesion, TcManticClientesDto.class, this.registroCliente.getIdCliente()) >= 1L;
          }
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

  private boolean registraClientesDomicilios(Session sesion, Long idCliente) throws Exception {
    TrManticClienteDomicilioDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    int countPrincipal = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
			if(this.registroCliente.getClientesDomicilio().size()== 1)
					this.registroCliente.getClientesDomicilio().get(0).setIdPrincipal(1L);
      for (ClienteDomicilio clienteDomicilio : this.registroCliente.getClientesDomicilio()) {								
				if(clienteDomicilio.getIdPrincipal().equals(1L))
					countPrincipal++;
				if(countPrincipal== 0 && this.registroCliente.getClientesDomicilio().size()-1 == count)
					clienteDomicilio.setIdPrincipal(1L);
        clienteDomicilio.setIdCliente(idCliente);
        clienteDomicilio.setIdUsuario(JsfBase.getIdUsuario());
				clienteDomicilio.setIdDomicilio(toIdDomicilio(sesion, clienteDomicilio));		
        dto = (TrManticClienteDomicilioDto) clienteDomicilio;
        sqlAccion = clienteDomicilio.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdClienteDomicilio(-1L);
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
      regresar = count == this.registroCliente.getClientesDomicilio().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los domicilios, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesDomicilios

  private boolean registraClientesRepresentantes(Session sesion, Long idCliente) throws Exception {
    TrManticClienteRepresentanteDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    int countPrincipal = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
			if(this.registroCliente.getPersonasTiposContacto().size()== 1)
					this.registroCliente.getPersonasTiposContacto().get(0).setIdPrincipal(1L);
      for (ClienteContactoRepresentante clienteRepresentante : this.registroCliente.getPersonasTiposContacto()) {
				if(clienteRepresentante.getIdPrincipal().equals(1L))
					countPrincipal++;
				if(countPrincipal== 0 && this.registroCliente.getPersonasTiposContacto().size()-1 == count)
					clienteRepresentante.setIdPrincipal(1L);
        clienteRepresentante.setIdCliente(idCliente);
        clienteRepresentante.setIdUsuario(JsfBase.getIdUsuario());
        clienteRepresentante.setIdRepresentante(addRepresentante(sesion, clienteRepresentante));
        dto = (TrManticClienteRepresentanteDto) clienteRepresentante;
        sqlAccion = clienteRepresentante.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdClienteRepresentante(-1L);
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
      regresar = count == this.registroCliente.getPersonasTiposContacto().size();
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los representantes, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesRepresentantes
	
	private Long addRepresentante(Session sesion, ClienteContactoRepresentante clienteRepresentante) throws Exception{
		Long regresar= -1L;
		TcManticPersonasDto representante= null;
		try {
			representante= new TcManticPersonasDto();
			representante.setNombres(clienteRepresentante.getNombres());
			representante.setPaterno(clienteRepresentante.getPaterno());
			representante.setMaterno(clienteRepresentante.getMaterno());
			representante.setIdTipoPersona(ETipoPersona.REPRESENTANTE_LEGAL.getIdTipoPersona());	
			representante.setIdTipoSexo(1L);
			representante.setEstilo(ESTILO);
			representante.setIdPersonaTitulo(1L);		
			regresar= DaoFactory.getInstance().insert(sesion, representante);
			if(regresar > -1L)
				registraPersonasTipoContacto(sesion, regresar, clienteRepresentante.getContactos());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError = "Error al registrar los representantes, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // addRepresentante

	private boolean registraPersonasTipoContacto(Session sesion, Long idPersona, List<PersonaTipoContacto> tiposContactos) throws Exception {
    TrManticPersonaTipoContactoDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (PersonaTipoContacto personaTipoContacto : tiposContactos) {
				if(personaTipoContacto.getValor()!= null && !Cadena.isVacio(personaTipoContacto.getValor())){
					personaTipoContacto.setIdPersona(idPersona);
					personaTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
					personaTipoContacto.setOrden(count+1L);
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
				} // if
				else
					validate= true;
        if (validate) 
          count++;        
      } // for		
      regresar = count == tiposContactos.size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los tipos de contacto, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesTipoContacto
	
  private boolean registraClientesTipoContacto(Session sesion, Long idCliente) throws Exception {
    TrManticClienteTipoContactoDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (ClienteTipoContacto clienteTipoContacto : this.registroCliente.getClientesTiposContacto()) {
				if(clienteTipoContacto.getValor()!= null && !Cadena.isVacio(clienteTipoContacto.getValor())){
					clienteTipoContacto.setIdCliente(idCliente);
					clienteTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
					dto = (TrManticClienteTipoContactoDto) clienteTipoContacto;
					sqlAccion = clienteTipoContacto.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdClienteTipoContacto(-1L);
							validate = registrar(sesion, dto);
							break;
						case UPDATE:
							validate = actualizar(sesion, dto);
							break;
					} // switch
				} // if
				else
					validate= true;
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.registroCliente.getClientesTiposContacto().size();
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
      for (IBaseDto dto : this.registroCliente.getDeleteList()) {
        if (DaoFactory.getInstance().delete(sesion, dto) >= 1L) {
          count++;
        }
      } // for
      regresar = count == this.registroCliente.getDeleteList().size();
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
    return registrarSentencia(sesion, dto) >= 1L;
  } // registrar
  
	private Long registrarSentencia(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().insert(sesion, dto);
  } // registrar

  private boolean actualizar(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().update(sesion, dto) >= 1L;
  } // actualizar
	
	private Long toIdDomicilio(Session sesion, ClienteDomicilio clienteDomicilio) throws Exception{		
		Entity entityDomicilio= null;
		Long regresar= -1L;
		try {
			entityDomicilio= toDomicilio(sesion, clienteDomicilio);
			if(entityDomicilio!= null)
				regresar= entityDomicilio.getKey();
			else
				regresar= insertDomicilio(sesion, clienteDomicilio);					
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
}