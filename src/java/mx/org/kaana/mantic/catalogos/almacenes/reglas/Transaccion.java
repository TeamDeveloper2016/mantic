package mx.org.kaana.mantic.catalogos.almacenes.reglas;

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
import mx.org.kaana.mantic.catalogos.almacenes.bean.AlmacenDomicilio;
import mx.org.kaana.mantic.catalogos.almacenes.bean.AlmacenTipoContacto;
import mx.org.kaana.mantic.catalogos.almacenes.bean.AlmacenUbicacion;
import mx.org.kaana.mantic.catalogos.almacenes.bean.RegistroAlmacen;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TrManticAlmacenDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticAlmacenTipoContactoDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private IBaseDto dto;
  private RegistroAlmacen registroAlmacen;
  private String messageError;

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	}
	
  public Transaccion(RegistroAlmacen registroAlmacen) {
    this.registroAlmacen = registroAlmacen;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar = procesarAlmacen(sesion);
          break;
        case MODIFICAR:
          regresar = actualizarAlmacen(sesion);
          break;
        case ELIMINAR:
          regresar = eliminarAlmacen(sesion);
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
      throw new Exception(this.messageError);
    } // catch		
    return regresar;
  } // ejecutar

  private boolean procesarAlmacen(Session sesion) throws Exception {
    boolean regresar= false;
    Long idAlmacen= -1L;
    try {
      this.messageError = "Error al registrar el articulo";
      if (eliminarRegistros(sesion)) {
        this.registroAlmacen.getAlmacen().setIdUsuario(JsfBase.getIdUsuario());
        this.registroAlmacen.getAlmacen().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
        idAlmacen = DaoFactory.getInstance().insert(sesion, this.registroAlmacen.getAlmacen());
				if (registraAlmacenDomicilios(sesion, idAlmacen)) {          
					if(registraAlmacenTipoContacto(sesion, idAlmacen))
						regresar = registraAlmacenUbicaciones(sesion, idAlmacen);
				} // if
			} // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarAlmacen

  private boolean actualizarAlmacen(Session sesion) throws Exception {
    boolean regresar= false;
    Long idAlmacen= -1L;
    try {
      idAlmacen = this.registroAlmacen.getIdAlmacen();
      if (registraAlmacenDomicilios(sesion, idAlmacen)) {        
				if (registraAlmacenTipoContacto(sesion, idAlmacen)) {
					if (registraAlmacenUbicaciones(sesion, idAlmacen)) 
						regresar = DaoFactory.getInstance().update(sesion, this.registroAlmacen.getAlmacen()) >= 1L;
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarAlmacen

  private boolean eliminarAlmacen(Session sesion) throws Exception {
    boolean regresar = false;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idCliente", this.registroAlmacen.getIdAlmacen());
      if (DaoFactory.getInstance().deleteAll(sesion, TrManticAlmacenDomicilioDto.class, params) > -1L) {
				if (DaoFactory.getInstance().deleteAll(sesion, TrManticAlmacenTipoContactoDto.class, params) > -1L) {
					if (DaoFactory.getInstance().deleteAll(sesion, TcManticAlmacenesUbicacionesDto.class, params) > -1L) 
						regresar = DaoFactory.getInstance().delete(sesion, TcManticAlmacenesDto.class, this.registroAlmacen.getIdAlmacen()) >= 1L;
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
  } // eliminarAlmacen

  private boolean registraAlmacenDomicilios(Session sesion, Long idAlmacen) throws Exception {
    TrManticAlmacenDomicilioDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
		int countPrincipal = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
			if(this.registroAlmacen.getAlmacenDomicilio().size()== 1)
					this.registroAlmacen.getAlmacenDomicilio().get(0).setIdPrincipal(1L);
      for (AlmacenDomicilio almacenDomicilio : this.registroAlmacen.getAlmacenDomicilio()) {
				if(almacenDomicilio.getIdPrincipal().equals(1L))
					countPrincipal++;
				if(countPrincipal== 0 && this.registroAlmacen.getAlmacenDomicilio().size()-1 == count)
					almacenDomicilio.setIdPrincipal(1L);
        almacenDomicilio.setIdAlmacen(idAlmacen);
        almacenDomicilio.setIdUsuario(JsfBase.getIdUsuario());
				almacenDomicilio.setIdDomicilio(toIdDomicilio(sesion, almacenDomicilio));		
        dto = (TrManticAlmacenDomicilioDto) almacenDomicilio;
        sqlAccion = almacenDomicilio.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdAlmacenDomicilio(-1L);
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
      regresar = count == this.registroAlmacen.getAlmacenDomicilio().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los domicilios, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraAlmacenDomicilios  

  private boolean registraAlmacenTipoContacto(Session sesion, Long idAlmacen) throws Exception {
    TrManticAlmacenTipoContactoDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (AlmacenTipoContacto almacenTipoContacto : this.registroAlmacen.getAlmacenTiposContacto()) {
        almacenTipoContacto.setIdAlmacen(idAlmacen);
        almacenTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
        dto = (TrManticAlmacenTipoContactoDto) almacenTipoContacto;
        sqlAccion = almacenTipoContacto.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdAlmacenTipoContacto(-1L);
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
      regresar = count == this.registroAlmacen.getAlmacenTiposContacto().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar los tipos de contacto, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesTipoContacto
	
  private boolean registraAlmacenUbicaciones(Session sesion, Long idAlmacen) throws Exception {
    TcManticAlmacenesUbicacionesDto dto = null;
    ESql sqlAccion = null;
    int count = 0;
    boolean validate = false;
    boolean regresar = false;
    try {
      for (AlmacenUbicacion almacenUbicacion : this.registroAlmacen.getUbicaciones()) {
				if(almacenUbicacion.getPiso()!= null && !Cadena.isVacio(almacenUbicacion.getPiso())){
					almacenUbicacion.setIdAlmacen(idAlmacen);
					almacenUbicacion.setIdUsuario(JsfBase.getIdUsuario());
					dto = (TcManticAlmacenesUbicacionesDto) almacenUbicacion;
					sqlAccion = almacenUbicacion.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdAlmacenUbicacion(-1L);
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
        } // if
      } // for		
      regresar = count == this.registroAlmacen.getUbicaciones().size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar las ubicaciones, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesTipoContacto

  private boolean eliminarRegistros(Session sesion) throws Exception {
    boolean regresar = true;
    int count = 0;
    try {
      for (IBaseDto dto : this.registroAlmacen.getDeleteList()) {
        if (DaoFactory.getInstance().delete(sesion, dto) >= 1L) {
          count++;
        } // if
      } // for
      regresar = count == this.registroAlmacen.getDeleteList().size();
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
	
	private Long toIdDomicilio(Session sesion, AlmacenDomicilio almacenDomicilio) throws Exception{		
		Entity entityDomicilio= null;
		Long regresar= -1L;
		try {
			entityDomicilio= toDomicilio(sesion, almacenDomicilio);
			if(entityDomicilio!= null)
				regresar= entityDomicilio.getKey();
			else
				regresar= insertDomicilio(sesion, almacenDomicilio);					
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarDomicilio	
	
	private Long insertDomicilio(Session sesion, AlmacenDomicilio almacenDomicilio) throws Exception{
		TcManticDomiciliosDto domicilio= null;
		Long regresar= -1L;
		try {
			domicilio= new TcManticDomiciliosDto();
			domicilio.setIdLocalidad(almacenDomicilio.getIdLocalidad().getKey());
			domicilio.setAsentamiento(almacenDomicilio.getColonia());
			domicilio.setCalle(almacenDomicilio.getCalle());
			domicilio.setCodigoPostal(almacenDomicilio.getCodigoPostal());
			domicilio.setEntreCalle(almacenDomicilio.getEntreCalle());
			domicilio.setIdUsuario(JsfBase.getIdUsuario());
			domicilio.setNumeroExterior(almacenDomicilio.getExterior());
			domicilio.setNumeroInterior(almacenDomicilio.getInterior());
			domicilio.setYcalle(almacenDomicilio.getyCalle());
			regresar= DaoFactory.getInstance().insert(sesion, domicilio);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // insertDomicilio
	
	private Entity toDomicilio(Session sesion, AlmacenDomicilio almacenDomicilio) throws Exception{
		Entity regresar= null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idLocalidad", almacenDomicilio.getIdLocalidad().getKey());
			params.put("codigoPostal", almacenDomicilio.getCodigoPostal());
			params.put("calle", almacenDomicilio.getCalle());
			params.put("numeroExterior", almacenDomicilio.getExterior());
			params.put("numeroInterior", almacenDomicilio.getInterior());
			params.put("asentamiento", almacenDomicilio.getColonia());
			params.put("entreCalle", almacenDomicilio.getEntreCalle());
			params.put("yCalle", almacenDomicilio.getyCalle());
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
