package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.usuarios.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 01:15:57 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.Map;
import mx.org.kaana.libs.formato.Numero;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalMensajesDto;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesUsuariosDto;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
  private TcJanalMensajesDto dto;
  private Long idUsuarioDestino;
  private Long idUsuarioRemite;
  private Map<String, Object> params;

  public Transaccion(TcJanalMensajesDto dto, Map<String, Object> params) {
    this.dto   = dto;
    this.params= params;
  }

  public Transaccion(TcJanalMensajesDto dto, Long idUsuarioDestino, Long idUsuarioRemite) {
    this.dto             = dto;
    this.idUsuarioDestino= idUsuarioDestino;
    this.idUsuarioRemite = idUsuarioRemite;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar                            = false;
    TrJanalMensajesUsuariosDto dtoMensajeUsuario= null;
    Long idMensaje                              = 0L;
    try {
      switch(accion) {
        case AGREGAR :
          idMensaje= dto.getIdMensaje()>0?dto.getIdMensaje():DaoFactory.getInstance().insert(sesion, this.dto);
          if(idMensaje >= 1){
            dtoMensajeUsuario= new TrJanalMensajesUsuariosDto();
            dtoMensajeUsuario.setIdBooleano(2L);
            dtoMensajeUsuario.setIdMensaje(idMensaje);
            dtoMensajeUsuario.setIdUsuario(this.idUsuarioDestino);
            dtoMensajeUsuario.setIdUsuarioModifica(this.idUsuarioRemite);
            dtoMensajeUsuario.setRegistro(this.dto.getRegistro());
            regresar= DaoFactory.getInstance().insert(sesion, dtoMensajeUsuario)>0L;
          } // if
          else
            regresar= false;
        break;
        case MODIFICAR :
          params.put("idBooleano", 2L);
          dtoMensajeUsuario= new TrJanalMensajesUsuariosDto(Numero.getLong(params.get("idMensajeUsuario").toString()));
          if(DaoFactory.getInstance().update(sesion, TcJanalMensajesDto.class, this.dto.getKey(), params) >= 1L)
            regresar= DaoFactory.getInstance().update(sesion, TrJanalMensajesUsuariosDto.class, dtoMensajeUsuario.getKey(), params) > 0L;
          else
            regresar= false;
        break;
      } // switch
			if(!regresar)
				throw new RuntimeException("No se modifico ningun registro");
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }


}
