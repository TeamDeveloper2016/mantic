/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.proveedores.reglas;

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
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.catalogos.proveedores.beans.ProveedorBanca;
import mx.org.kaana.mantic.catalogos.proveedores.beans.ProveedorCondicionPago;
import mx.org.kaana.mantic.catalogos.proveedores.beans.ProveedorContactoAgente;
import mx.org.kaana.mantic.catalogos.proveedores.beans.ProveedorDomicilio;
import mx.org.kaana.mantic.catalogos.proveedores.beans.ProveedorTipoContacto;
import mx.org.kaana.mantic.catalogos.proveedores.beans.RegistroProveedor;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresBancosDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticPersonaTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorPagoDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorAgenteDto;
import mx.org.kaana.mantic.enums.ETipoPago;
import mx.org.kaana.mantic.enums.ETipoPersona;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private static final Log LOG = LogFactory.getLog(Transaccion.class);
  private static final String ESTILO= "sentinel";
	private IBaseDto dto;
	private RegistroProveedor registroProveedor;
	private String messageError;
	
	public Transaccion(IBaseDto dto) {
		this.dto= dto;
	}
	
  public Transaccion(RegistroProveedor registroProveedor) {
    this.registroProveedor= registroProveedor;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {
			if(this.registroProveedor!= null)
				this.registroProveedor.getProveedor().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      switch (accion) {
        case AGREGAR:
          regresar = procesarProveedor(sesion);
          break;
        case MODIFICAR:
          regresar = actualizarProveedor(sesion);
          break;
        case ELIMINAR:
          regresar = eliminarProveedor(sesion);
          break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
      } // switch
      if (!regresar) {
        throw new Exception(this.messageError);
      } // if
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
  } // ejecutar
	
	private boolean procesarProveedor(Session sesion) throws Exception {
    boolean regresar = false;
    Long idProveedor = -1L;
    try {
      this.messageError = "Error al registrar el articulo";
      if (eliminarRegistros(sesion)) {
        this.registroProveedor.getProveedor().setIdUsuario(JsfBase.getIdUsuario());
        this.registroProveedor.getProveedor().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
        idProveedor = DaoFactory.getInstance().insert(sesion, this.registroProveedor.getProveedor());
        if (registraProveedoresDomicilios(sesion, idProveedor)) {
          if (registraProveedoresAgentes(sesion, idProveedor)) {
            if(registraProveedoresTipoContacto(sesion, idProveedor)){
							if(registraProveedoresServicios(sesion, idProveedor)){
								if(registraProveedoresTransferencia(sesion, idProveedor)){
									if(registraProveedoresFormaPago(sesion, idProveedor, this.registroProveedor.getProveedor().getClave())){
										this.registroProveedor.getPortal().setIdProveedor(idProveedor);
										regresar= DaoFactory.getInstance().insert(sesion, this.registroProveedor.getPortal())>= 1L;
									} // if
								} // if
							} // if
						} // if
          } // if
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarProveedor

  private boolean actualizarProveedor(Session sesion) throws Exception {
    boolean regresar = false;
    Long idProveedor = -1L;
    try {
      idProveedor = this.registroProveedor.getIdProveedor();
      if (registraProveedoresDomicilios(sesion, idProveedor)) {
        if (registraProveedoresAgentes(sesion, idProveedor)) {
          if (registraProveedoresTipoContacto(sesion, idProveedor)) {
						if(registraProveedoresFormaPago(sesion, idProveedor, this.registroProveedor.getProveedor().getClave())){
							if(registraProveedoresServicios(sesion, idProveedor)){
								if(registraProveedoresTransferencia(sesion, idProveedor)){
									if(this.registroProveedor.getPortal().isValid()){
										if(DaoFactory.getInstance().update(sesion, this.registroProveedor.getPortal())>= 0L){
											regresar = DaoFactory.getInstance().update(sesion, this.registroProveedor.getProveedor()) >= 1L;
										} // if
									} // if
									else{										
										regresar = DaoFactory.getInstance().update(sesion, this.registroProveedor.getProveedor()) >= 1L;
										if(regresar && this.registroProveedor.getPortal()!= null && !Cadena.isVacio(this.registroProveedor.getPortal())){
											this.registroProveedor.getPortal().setIdProveedor(idProveedor);
											regresar= DaoFactory.getInstance().insert(sesion, this.registroProveedor.getPortal())>= 1L;
										} // if
									} // else
								} // if
							} // if
						} // if
          } // if
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarProveedor

  private boolean eliminarProveedor(Session sesion) throws Exception {
    boolean regresar = false;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idProveedor", this.registroProveedor.getIdProveedor());
      if (DaoFactory.getInstance().deleteAll(sesion, TrManticProveedorDomicilioDto.class, params) > -1L) {
        if (DaoFactory.getInstance().deleteAll(sesion, TrManticProveedorAgenteDto.class, params) > -1L) {
          if (DaoFactory.getInstance().deleteAll(sesion, TrManticProveedorTipoContactoDto.class, params) > -1L) {
            if(DaoFactory.getInstance().deleteAll(sesion, TrManticProveedorPagoDto.class, params)> -1L)
						  regresar = DaoFactory.getInstance().delete(sesion, TcManticProveedoresDto.class, this.registroProveedor.getIdProveedor()) >= 1L;
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
  } // eliminarProveedor

  private boolean registraProveedoresDomicilios(Session sesion, Long idProveedor) throws Exception {
    TrManticProveedorDomicilioDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    int countPrincipal = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
			if(this.registroProveedor.getProveedoresDomicilio().size()== 1)
					this.registroProveedor.getProveedoresDomicilio().get(0).setIdPrincipal(1L);
      for (ProveedorDomicilio proveedorDomicilio : this.registroProveedor.getProveedoresDomicilio()) {								
				if(proveedorDomicilio.getIdPrincipal().equals(1L))
					countPrincipal++;
				if(countPrincipal== 0 && this.registroProveedor.getProveedoresDomicilio().size()-1 == count)
					proveedorDomicilio.setIdPrincipal(1L);
        proveedorDomicilio.setIdProveedor(idProveedor);
        proveedorDomicilio.setIdUsuario(JsfBase.getIdUsuario());
				proveedorDomicilio.setIdDomicilio(toIdDomicilio(sesion, proveedorDomicilio));		
        dto = (TrManticProveedorDomicilioDto) proveedorDomicilio;
        sqlAccion = proveedorDomicilio.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdProveedorDomicilio(-1L);
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
      regresar = count == this.registroProveedor.getProveedoresDomicilio().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los domicilios, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraProveedoresDomicilios

  private boolean registraProveedoresAgentes(Session sesion, Long idProveedor) throws Exception {
    TrManticProveedorAgenteDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    int countPrincipal = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
			if(this.registroProveedor.getPersonasTiposContacto().size()== 1)
					this.registroProveedor.getPersonasTiposContacto().get(0).setIdPrincipal(1L);
      for (ProveedorContactoAgente proveedorRepresentante : this.registroProveedor.getPersonasTiposContacto()) {				
				if(proveedorRepresentante.getIdPrincipal().equals(1L))
					countPrincipal++;
				if(countPrincipal== 0 && this.registroProveedor.getPersonasTiposContacto().size()-1 == count)
					proveedorRepresentante.setIdPrincipal(1L);
        proveedorRepresentante.setIdProveedor(idProveedor);
        proveedorRepresentante.setIdUsuario(JsfBase.getIdUsuario());
        proveedorRepresentante.setIdAgente(addAgente(sesion, proveedorRepresentante));
        dto = (TrManticProveedorAgenteDto) proveedorRepresentante;
        sqlAccion = proveedorRepresentante.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdProveedorAgente(-1L);
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
      regresar = count == this.registroProveedor.getPersonasTiposContacto().size();
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los agentes, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraProveedoresAgentes
	
	private Long addAgente(Session sesion, ProveedorContactoAgente proveedorAgente) throws Exception{
		Long regresar= -1L;
		TcManticPersonasDto representante= null;
		try {
			representante= new TcManticPersonasDto();
			representante.setNombres(proveedorAgente.getNombres());
			representante.setPaterno(proveedorAgente.getPaterno());
			representante.setMaterno(proveedorAgente.getMaterno());
			representante.setIdTipoPersona(ETipoPersona.REPRESENTANTE_LEGAL.getIdTipoPersona());	
			representante.setIdTipoSexo(1L);
			representante.setEstilo(ESTILO);
			representante.setIdPersonaTitulo(1L);		
			regresar= DaoFactory.getInstance().insert(sesion, representante);
			if(regresar > -1L)
				registraPersonasTipoContacto(sesion, regresar, proveedorAgente.getContactos());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError = "Error al registrar el agente, verifique que no haya duplicados";
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
  } // registraProveedoresTipoContacto
	
  private boolean registraProveedoresTipoContacto(Session sesion, Long idProveedor) throws Exception {
    TrManticProveedorTipoContactoDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    int orden = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (ProveedorTipoContacto proveedorTipoContacto : this.registroProveedor.getProveedoresTipoContacto()) {
				if(proveedorTipoContacto.getValor()!= null && !Cadena.isVacio(proveedorTipoContacto.getValor())){
					proveedorTipoContacto.setOrden(orden + 1L);
					proveedorTipoContacto.setIdProveedor(idProveedor);
					proveedorTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
					dto = (TrManticProveedorTipoContactoDto) proveedorTipoContacto;
					sqlAccion = proveedorTipoContacto.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdProveedorTipoContacto(-1L);
							validate = registrar(sesion, dto);
							break;
						case UPDATE:
							validate = actualizar(sesion, dto);
							break;
					} // switch
					orden++;
				} // if
				else
					validate= true;
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.registroProveedor.getProveedoresTipoContacto().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los tipos de contacto, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraProveedoresTipoContacto
	
  private boolean registraProveedoresServicios(Session sesion, Long idProveedor) throws Exception {
    TcManticProveedoresBancosDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (ProveedorBanca proveedorBanca : this.registroProveedor.getProveedoresServicio()) {
				if(proveedorBanca.getConvenioCuenta()!= null && !Cadena.isVacio(proveedorBanca.getConvenioCuenta())){
					proveedorBanca.setIdProveedor(idProveedor);
					proveedorBanca.setIdUsuario(JsfBase.getIdUsuario());
					dto = (TcManticProveedoresBancosDto) proveedorBanca;
					sqlAccion = proveedorBanca.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdProveedorBanca(-1L);
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
      regresar = count == this.registroProveedor.getProveedoresServicio().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar el servicio de banco, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraProveedoresServicios
	
  private boolean registraProveedoresTransferencia(Session sesion, Long idProveedor) throws Exception {
    TcManticProveedoresBancosDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (ProveedorBanca proveedorBanca : this.registroProveedor.getProveedoresTransferencia()) {
				if(proveedorBanca.getConvenioCuenta()!= null && !Cadena.isVacio(proveedorBanca.getConvenioCuenta())){
					proveedorBanca.setIdProveedor(idProveedor);
					proveedorBanca.setIdUsuario(JsfBase.getIdUsuario());
					dto = (TcManticProveedoresBancosDto) proveedorBanca;
					sqlAccion = proveedorBanca.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdProveedorBanca(-1L);
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
      regresar = count == this.registroProveedor.getProveedoresTransferencia().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar el servicio de banco, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraProveedoresTransferencia
	
  private boolean registraProveedoresFormaPago(Session sesion, Long idProveedor, String claveProveedor) throws Exception {
    TrManticProveedorPagoDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (ProveedorCondicionPago proveedorCondicionPago : this.registroProveedor.getProveedoresCondicionPago()) {
				if(proveedorCondicionPago.getDescuento()!= null && !Cadena.isVacio(proveedorCondicionPago.getDescuento())){
					proveedorCondicionPago.setIdProveedor(idProveedor);
					proveedorCondicionPago.setIdUsuario(JsfBase.getIdUsuario());
					if(proveedorCondicionPago.getClave()== null || Cadena.isVacio(proveedorCondicionPago.getClave()))
						proveedorCondicionPago.setClave(ETipoPago.fromIdTipoPago(proveedorCondicionPago.getIdTipoPago()).name().concat("-")+ proveedorCondicionPago.getPlazo());
					dto = (TrManticProveedorPagoDto) proveedorCondicionPago;
					sqlAccion = proveedorCondicionPago.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdProveedorPago(-1L);
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
      regresar = count == this.registroProveedor.getProveedoresCondicionPago().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los tipos de pago, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraProveedoresTipoContacto

  private boolean eliminarRegistros(Session sesion) throws Exception {
    boolean regresar = true;
    int count = 0;
    try {
      for (IBaseDto dto : this.registroProveedor.getDeleteList()) {
        if (DaoFactory.getInstance().delete(sesion, dto) >= 1L) 
          count++;
      } // for
      regresar = count == this.registroProveedor.getDeleteList().size();
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
	
	private Long toIdDomicilio(Session sesion, ProveedorDomicilio proveedorDomicilio) throws Exception{		
		Entity entityDomicilio= null;
		Long regresar= -1L;
		try {
			entityDomicilio= toDomicilio(sesion, proveedorDomicilio);
			if(entityDomicilio!= null)
				regresar= entityDomicilio.getKey();
			else
				regresar= insertDomicilio(sesion, proveedorDomicilio);					
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarDomicilio	
	
	private Long insertDomicilio(Session sesion, ProveedorDomicilio proveedorDomicilio) throws Exception{
		TcManticDomiciliosDto domicilio= null;
		Long regresar= -1L;
		try {
			domicilio= new TcManticDomiciliosDto();
			domicilio.setIdLocalidad(proveedorDomicilio.getIdLocalidad().getKey());
			domicilio.setAsentamiento(proveedorDomicilio.getColonia());
			domicilio.setCalle(proveedorDomicilio.getCalle());
			domicilio.setCodigoPostal(proveedorDomicilio.getCodigoPostal());
			domicilio.setEntreCalle(proveedorDomicilio.getEntreCalle());
			domicilio.setIdUsuario(JsfBase.getIdUsuario());
			domicilio.setNumeroExterior(proveedorDomicilio.getExterior());
			domicilio.setNumeroInterior(proveedorDomicilio.getInterior());
			domicilio.setYcalle(proveedorDomicilio.getyCalle());
			regresar= DaoFactory.getInstance().insert(sesion, domicilio);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // insertDomicilio
	
	private Entity toDomicilio(Session sesion, ProveedorDomicilio proveedorDomicilio) throws Exception{
		Entity regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idLocalidad", proveedorDomicilio.getIdLocalidad().getKey());
			params.put("codigoPostal", proveedorDomicilio.getCodigoPostal());
			params.put("calle", proveedorDomicilio.getCalle());
			params.put("numeroExterior", proveedorDomicilio.getExterior());
			params.put("numeroInterior", proveedorDomicilio.getInterior());
			params.put("asentamiento", proveedorDomicilio.getColonia());
			params.put("entreCalle", proveedorDomicilio.getEntreCalle());
			params.put("yCalle", proveedorDomicilio.getyCalle());
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
