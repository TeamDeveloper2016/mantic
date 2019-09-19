package mx.org.kaana.mantic.catalogos.empresas.reglas;

import java.util.HashMap;
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
import mx.org.kaana.mantic.catalogos.empresas.beans.EmpresaDomicilio;
import mx.org.kaana.mantic.catalogos.empresas.beans.EmpresaTipoContacto;
import mx.org.kaana.mantic.catalogos.empresas.beans.RegistroEmpresa;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDto;
import mx.org.kaana.mantic.db.dto.TrManticEmpresaDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticEmpresaTipoContactoDto;
import mx.org.kaana.mantic.enums.ETipoEmpresa;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{
	
	private IBaseDto dto;
  private RegistroEmpresa registroEmpresa;
  private String messageError;

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	}

	public Transaccion(RegistroEmpresa registroEmpresa) {
		this.registroEmpresa = registroEmpresa;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		 boolean regresar = false;
    try {
			if(this.registroEmpresa!= null)
				this.registroEmpresa.getEmpresa().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      switch (accion) {
        case AGREGAR:
          regresar = procesarEmpresa(sesion);
          break;
        case MODIFICAR:
          regresar = actualizarEmpresa(sesion);
          break;
        case ELIMINAR:
          regresar = eliminarEmpresa(sesion);
          break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
      } // switch
      if (!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
	} // ejecutar
	
	private boolean procesarEmpresa(Session sesion) throws Exception {
    boolean regresar= false;
    Long idEmpresa  = -1L;
    try {
      this.messageError = "Error al registrar el articulo";
      if (eliminarRegistros(sesion)) {
        this.registroEmpresa.getEmpresa().setIdUsuarios(JsfBase.getIdUsuario());
        this.registroEmpresa.getEmpresa().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
        idEmpresa = DaoFactory.getInstance().insert(sesion, this.registroEmpresa.getEmpresa());
        if (registraEmpresaDomicilios(sesion, idEmpresa)) {
					regresar = registraEmpresaTipoContacto(sesion, idEmpresa);
					if(this.registroEmpresa.getEmpresa().getIdTipoEmpresa().equals(ETipoEmpresa.MATRIZ.getIdTipoEmpresa())){
						this.registroEmpresa.getEmpresa().setIdEmpresaDepende(idEmpresa);
						regresar= DaoFactory.getInstance().update(sesion, this.registroEmpresa.getEmpresa())>= 1L;
					} // if
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente

  private boolean actualizarEmpresa(Session sesion) throws Exception {
    boolean regresar = false;
    Long idEmpresa = -1L;
    try {
      idEmpresa = this.registroEmpresa.getIdEmpresa();
      if (registraEmpresaDomicilios(sesion, idEmpresa)) {
				if (registraEmpresaTipoContacto(sesion, idEmpresa)) {
					regresar = DaoFactory.getInstance().update(sesion, this.registroEmpresa.getEmpresa()) >= 1L;
				} // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarCliente

  private boolean eliminarEmpresa(Session sesion) throws Exception {
    boolean regresar = false;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idEmpresa", this.registroEmpresa.getIdEmpresa());
      if (DaoFactory.getInstance().deleteAll(sesion, TrManticEmpresaDomicilioDto.class, params) > -1L) {
				if (DaoFactory.getInstance().deleteAll(sesion, TrManticEmpresaTipoContactoDto.class, params) > -1L) {
					regresar = DaoFactory.getInstance().delete(sesion, TcManticEmpresasDto.class, this.registroEmpresa.getIdEmpresa()) >= 1L;
				}
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

  private boolean registraEmpresaDomicilios(Session sesion, Long idEmpresa) throws Exception {
    TrManticEmpresaDomicilioDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    int countPrincipal = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
			if(this.registroEmpresa.getEmpresasDomicilio().size()== 1)
					this.registroEmpresa.getEmpresasDomicilio().get(0).setIdPrincipal(1L);
      for (EmpresaDomicilio empresaDomicilio : this.registroEmpresa.getEmpresasDomicilio()) {								
				if(empresaDomicilio.getIdPrincipal().equals(1L))
					countPrincipal++;
				if(countPrincipal== 0 && this.registroEmpresa.getEmpresasDomicilio().size()-1 == count)
					empresaDomicilio.setIdPrincipal(1L);
        empresaDomicilio.setIdEmpresa(idEmpresa);
        empresaDomicilio.setIdUsuario(JsfBase.getIdUsuario());
				empresaDomicilio.setIdDomicilio(toIdDomicilio(sesion, empresaDomicilio));		
        dto = (TrManticEmpresaDomicilioDto) empresaDomicilio;
        sqlAccion = empresaDomicilio.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdEmpresaDomicilio(-1L);
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
      regresar = count == this.registroEmpresa.getEmpresasDomicilio().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los domicilios, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesDomicilios
	
	private boolean registraEmpresaTipoContacto(Session sesion, Long idEmpresa) throws Exception {
    TrManticEmpresaTipoContactoDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (EmpresaTipoContacto empresaTipoContacto : this.registroEmpresa.getEmpresasTiposContacto()) {
				if(empresaTipoContacto.getValor()!= null && !Cadena.isVacio(empresaTipoContacto.getValor())){
					empresaTipoContacto.setIdEmpresa(idEmpresa);
					empresaTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
					dto = (TrManticEmpresaTipoContactoDto) empresaTipoContacto;
					sqlAccion = empresaTipoContacto.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdEmpresaTipoContacto(-1L);
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
      regresar = count == this.registroEmpresa.getEmpresasTiposContacto().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los tipos de contacto, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesTipoContacto
	
	private boolean registrar(Session sesion, IBaseDto dto) throws Exception {
    return registrarSentencia(sesion, dto) >= 1L;
  } // registrar
  
	private Long registrarSentencia(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().insert(sesion, dto);
  } // registrar

  private boolean actualizar(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().update(sesion, dto) >= 1L;
  } // actualizar
	
	private Long toIdDomicilio(Session sesion, EmpresaDomicilio empresaDomicilio) throws Exception{		
		Entity entityDomicilio= null;
		Long regresar= -1L;
		try {
			entityDomicilio= toDomicilio(sesion, empresaDomicilio);
			if(entityDomicilio!= null)
				regresar= entityDomicilio.getKey();
			else
				regresar= insertDomicilio(sesion, empresaDomicilio);					
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarDomicilio	
	
	private Long insertDomicilio(Session sesion, EmpresaDomicilio empresaDomicilio) throws Exception{
		TcManticDomiciliosDto domicilio= null;
		Long regresar= -1L;
		try {
			domicilio= new TcManticDomiciliosDto();
			domicilio.setIdLocalidad(empresaDomicilio.getIdLocalidad().getKey());
			domicilio.setAsentamiento(empresaDomicilio.getColonia());
			domicilio.setCalle(empresaDomicilio.getCalle());
			domicilio.setCodigoPostal(empresaDomicilio.getCodigoPostal());
			domicilio.setEntreCalle(empresaDomicilio.getEntreCalle());
			domicilio.setIdUsuario(JsfBase.getIdUsuario());
			domicilio.setNumeroExterior(empresaDomicilio.getExterior());
			domicilio.setNumeroInterior(empresaDomicilio.getInterior());
			domicilio.setYcalle(empresaDomicilio.getyCalle());
			regresar= DaoFactory.getInstance().insert(sesion, domicilio);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // insertDomicilio
	
	private Entity toDomicilio(Session sesion, EmpresaDomicilio empresaDomicilio) throws Exception{
		Entity regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idLocalidad", empresaDomicilio.getIdLocalidad().getKey());
			params.put("codigoPostal", empresaDomicilio.getCodigoPostal());
			params.put("calle", empresaDomicilio.getCalle());
			params.put("numeroExterior", empresaDomicilio.getExterior());
			params.put("numeroInterior", empresaDomicilio.getInterior());
			params.put("asentamiento", empresaDomicilio.getColonia());
			params.put("entreCalle", empresaDomicilio.getEntreCalle());
			params.put("yCalle", empresaDomicilio.getyCalle());
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
	
	private boolean eliminarRegistros(Session sesion) throws Exception {
    boolean regresar = true;
    int count = 0;
    try {
      for (IBaseDto dto : this.registroEmpresa.getDeleteList()) {
        if (DaoFactory.getInstance().delete(sesion, dto) >= 1L) {
          count++;
        }
      } // for
      regresar = count == this.registroEmpresa.getDeleteList().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al eliminar registros";
    } // finally
    return regresar;
  } // eliminarRegistros
}
