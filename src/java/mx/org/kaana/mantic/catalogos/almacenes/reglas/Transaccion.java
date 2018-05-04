package mx.org.kaana.mantic.catalogos.almacenes.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.bean.AlmacenDomicilio;
import mx.org.kaana.mantic.catalogos.almacenes.bean.AlmacenTipoContacto;
import mx.org.kaana.mantic.catalogos.almacenes.bean.RegistroAlmacen;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
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
				this.registroAlmacen.getAlmacenUbicacion().setIdAlmacen(idAlmacen);
				this.registroAlmacen.getAlmacenUbicacion().setIdUsuario(JsfBase.getIdUsuario());			
				if(DaoFactory.getInstance().insert(sesion, this.registroAlmacen.getAlmacenUbicacion())>= 1L){
					if (registraAlmacenDomicilios(sesion, idAlmacen)) {          
						regresar = registraAlmacenTipoContacto(sesion, idAlmacen);
					} // if
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
    boolean validate = false;
    boolean regresar = false;
    try {
      for (AlmacenDomicilio clienteDomicilio : this.registroAlmacen.getAlmacenDomicilio()) {
        clienteDomicilio.setIdAlmacen(idAlmacen);
        clienteDomicilio.setIdUsuario(JsfBase.getIdUsuario());
        dto = (TrManticAlmacenDomicilioDto) clienteDomicilio;
        sqlAccion = clienteDomicilio.getSqlAccion();
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
}
