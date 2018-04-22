package mx.org.kaana.kajool.procesos.usuarios.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import static mx.org.kaana.kajool.enums.EAccion.ACTIVAR;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
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
  protected boolean ejecutar(Session session, EAccion accion) throws Exception {
    boolean regresar = false;
    IBaseDto usuarioExiste = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      switch (accion) {
        case AGREGAR:
          if (!this.persona.isValid()) {
            this.persona.setContrasenia(BouncyEncryption.encrypt(this.persona.getCurp().substring(0, 10)));
            this.persona.setIdTipoPersona(ETipoPersona.USUARIO.getIdTipoPersona());
            DaoFactory.getInstance().insert(session, this.persona);
            this.usuario.setIdPersona(this.persona.getIdPersona());
          }
          params.put("idPersona", this.usuario.getIdPersona());
          params.put("idPerfil", this.usuario.getIdPerfil());
          usuarioExiste = (TcJanalUsuariosDto) DaoFactory.getInstance().findIdentically(session, TcJanalUsuariosDto.class, params);
          regresar = usuarioExiste.isValid();
          if (!regresar) {
            regresar = DaoFactory.getInstance().insert(session, this.usuario) >= 1L;
          }
          break;
        case MODIFICAR:
          params.put("idPerfil", this.usuario.getIdPerfil());
          params.put("idUsuarioModifica", this.usuario.getIdUsuarioModifica());
          regresar = DaoFactory.getInstance().update(session, this.usuario, params) >= 1L;
          regresar = DaoFactory.getInstance().update(session, this.persona, params) >= 1L;
          break;
        case ELIMINAR:
          regresar = DaoFactory.getInstance().delete(session, this.usuario.toHbmClass(), this.usuario.getKey()) >= 1L;
          break;
        case ACTIVAR:
          params.put("activo", this.usuario.getActivo());
          regresar = DaoFactory.getInstance().update(session, this.usuario, params) >= 1L;
          break;
        case RESTAURAR:
          params.put("contrasenia", BouncyEncryption.encrypt(this.persona.getCurp().substring(0, 10)));
          regresar = DaoFactory.getInstance().update(session, this.persona, params) >= 1;
          break;
        case REGISTRAR:
        case COMPLEMENTAR:
          params.put("contrasenia", BouncyEncryption.encrypt(this.persona.getContrasenia()));
          regresar = DaoFactory.getInstance().update(session, this.persona, params) >= 1;
          break;
      } // switch
    } // try
    catch (Exception e) {
      throw e;
    } // catch    		
    return regresar;
  } // ejecutar

}
