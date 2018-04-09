package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.grupos.reglas;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalMensajesDto;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesGruposDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETiposMensajes;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/09/2015
 *@time 15:33:37
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


public class Transaccion extends IBaseTnx implements Serializable{
  private static final long serialVersionUID = 357607600327291543L;

  private TcJanalMensajesDto dto;
  private TrJanalMensajesGruposDto dtoMensajesGrupos;

  public Transaccion(TcJanalMensajesDto dto) {
    this.dto = dto;
  }

  public Transaccion(TrJanalMensajesGruposDto dtoMensajesGrupos) {
    this.dtoMensajesGrupos = dtoMensajesGrupos;
  }

  public Transaccion(TcJanalMensajesDto dto, TrJanalMensajesGruposDto dtoMensajesGrupos) {
    this.dto              = dto;
    this.dtoMensajesGrupos= dtoMensajesGrupos;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    long idMensajes = -1L;
    boolean regresar= true;
    switch (accion) {
      case AGREGAR:
        this.dto.setIdTipoMensaje(ETiposMensajes.GRUPO.getKey());
        idMensajes = DaoFactory.getInstance().insert(sesion, this.dto).intValue();
        this.dtoMensajesGrupos.setIdMensaje(idMensajes);
        this.dtoMensajesGrupos.setIdUsuario(1L);
        regresar = DaoFactory.getInstance().insert(sesion, this.dtoMensajesGrupos).intValue() > 0;
        break;
      case MODIFICAR:
        regresar = DaoFactory.getInstance().update(sesion, this.dto).intValue() > 0;
        break;
      case ELIMINAR:
        regresar = DaoFactory.getInstance().delete(sesion, this.dtoMensajesGrupos).intValue() > 0;
        break;
    } //switch
    return regresar;
  } //ejecutar


}
