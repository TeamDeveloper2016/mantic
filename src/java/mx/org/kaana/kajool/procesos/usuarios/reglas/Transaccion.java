package mx.org.kaana.kajool.procesos.usuarios.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import static mx.org.kaana.kajool.enums.EAccion.ACTIVAR;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import org.hibernate.Session;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 10:43:46 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Transaccion extends IBaseTnx {

  private TcJanalUsuariosDto usuario;
  private TcManticPersonasDto persona;

  
  public Transaccion(TcJanalUsuariosDto usuario) {
   this(usuario,null);
  }

  public Transaccion(TcManticPersonasDto persona) {
   this(null,persona);
  }
  
  public Transaccion(TcJanalUsuariosDto usuario, TcManticPersonasDto persona) {
    this.usuario = usuario;
    this.persona = persona;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar          = false;
    IBaseDto usuarioExiste    = null;
    Map<String, Object> params= null;
		params = new HashMap<>();
		switch (accion) {
			case AGREGAR:
				params.put("idPersona", this.persona.getIdPersona());
				params.put("idPerfil", this.usuario.getIdPerfil());
				usuarioExiste= (TcJanalUsuariosDto) DaoFactory.getInstance().findIdentically(sesion, TcJanalUsuariosDto.class, params);
				if (usuarioExiste== null) {
					if(this.toSearch(sesion, persona.getCuenta()))
						throw new RuntimeException("La cuenta ya existe verifiquela de favor !\n");
					else {
  					this.usuario.setIdPersona(persona.getIdPersona());
	  			  DaoFactory.getInstance().insert(sesion, this.usuario);
		  			this.persona.setContrasenia(BouncyEncryption.encrypt(this.persona.getContrasenia()));
			  		regresar = DaoFactory.getInstance().update(sesion, this.persona)>= 1;
					} // else	
				} // if
				else 
					regresar= false;
				break;
			case MODIFICAR:
				params.put("idPersona", this.persona.getIdPersona());
				params.put("idPerfil", this.usuario.getIdPerfil());
				params.put("idUsuarioModifica", this.usuario.getIdUsuarioModifica());
				usuarioExiste= (TcJanalUsuariosDto) DaoFactory.getInstance().findIdentically(sesion, TcJanalUsuariosDto.class, params);
				if (usuarioExiste== null) 
					regresar = DaoFactory.getInstance().update(sesion, this.usuario, params)>= 1L;
				this.persona.setContrasenia(BouncyEncryption.encrypt(this.persona.getContrasenia()));
				regresar = DaoFactory.getInstance().update(sesion, this.persona) >= 1L;
				this.persona.setContrasenia(BouncyEncryption.decrypt(this.persona.getContrasenia()));
				break;
			case ELIMINAR:
				regresar = DaoFactory.getInstance().delete(sesion, this.usuario.toHbmClass(), this.usuario.getKey()) >= 1L;
				break;
			case ACTIVAR:
				params.put("activo", this.usuario.getActivo());
				regresar = DaoFactory.getInstance().update(sesion, this.usuario, params) >= 1L;
				break;
			case RESTAURAR:
				params.put("contrasenia", BouncyEncryption.encrypt(this.persona.getPaterno()));
				regresar = DaoFactory.getInstance().update(sesion, this.persona, params) >= 1;
				break;
			case REGISTRAR:
			case COMPLEMENTAR:
				params.put("contrasenia", BouncyEncryption.encrypt(this.persona.getContrasenia()));
				regresar = DaoFactory.getInstance().update(sesion, this.persona, params) >= 1;
				break;
		} // switch
    return regresar;
  } // ejecutar

  private boolean toSearch(Session sesion, String cuenta) {
    boolean regresar          = false;
    Map<String, Object> params= null;
		List<TcManticPersonasDto> listaTrUsuariosDto= null;
    try {
      params = new HashMap<>();
      params.put("cuenta", cuenta);
      listaTrUsuariosDto = DaoFactory.getInstance().findViewCriteria(sesion, TcManticPersonasDto.class, params, "findUsuario");
      regresar = !listaTrUsuariosDto.isEmpty();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }	
}
