package mx.org.kaana.mantic.taller.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticServiciosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private IBaseDto dto;
  private RegistroServicio registroServicio;
  private String messageError;

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	}
	
  public Transaccion(RegistroServicio registroServicio) {
    this.registroServicio = registroServicio;
  }
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
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
	
	private boolean procesarServicio(Session sesion) throws Exception {
		boolean regresar= false;
		Long idCliente  = -1L;		
    try {
      this.messageError = "Error al registrar el articulo";
			this.registroServicio.getServicio().setIdUsuario(JsfBase.getIdUsuario());
			this.registroServicio.getServicio().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			regresar = DaoFactory.getInstance().insert(sesion, this.registroServicio.getServicio())>= 1L;
			if(this.registroServicio.getServicio().getIdCliente()== null || this.registroServicio.getServicio().getIdCliente().equals(-1L)){
				idCliente= registraCliente(sesion);
				this.registroServicio.getServicio().setIdCliente(idCliente);
				regresar= DaoFactory.getInstance().update(sesion, this.registroServicio.getServicio())>= 1L;
			} // if			
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarCliente

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
			regresar= DaoFactory.getInstance().update(sesion, this.registroServicio.getServicio())>= 1L;      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // actualizarCliente

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
}