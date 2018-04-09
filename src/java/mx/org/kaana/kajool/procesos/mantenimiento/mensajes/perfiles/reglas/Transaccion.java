package mx.org.kaana.kajool.procesos.mantenimiento.mensajes.perfiles.reglas;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalMensajesDto;
import mx.org.kaana.kajool.db.dto.TrJanalMensajesPerfilesDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;
/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 4/09/2015
 *@time 11:52:01 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable{

  private static final long serialVersionUID = -8112342802019200793L;
  private TcJanalMensajesDto dto;
  private TrJanalMensajesPerfilesDto dtoPerfil;

  public Transaccion(TcJanalMensajesDto dto, TrJanalMensajesPerfilesDto dtoPerfil) {
    this.dto       = dto;
    this.dtoPerfil = dtoPerfil;
  }

  public Transaccion(TrJanalMensajesPerfilesDto dtoPerfil) {
    this.dtoPerfil = dtoPerfil;
  }

  public Transaccion(TcJanalMensajesDto dto) {
    this.dto = dto;
  }


  @Override
  protected boolean ejecutar(Session sn, EAccion accion) throws Exception {
    long idMensaje  = -1L;
    boolean regresar= true;
    switch (accion) {
      case AGREGAR:
        idMensaje= dto.getIdMensaje()>0?dto.getIdMensaje():DaoFactory.getInstance().insert(sn, this.dto).intValue();
        this.dtoPerfil.setIdMensaje(idMensaje);
        regresar= DaoFactory.getInstance().insert(sn, this.dtoPerfil).intValue() > 0;
        break;
      case MODIFICAR:
        regresar = DaoFactory.getInstance().update(sn, this.dto).intValue() > 0;
        break;
      case ELIMINAR:
        regresar = DaoFactory.getInstance().delete(sn, this.dtoPerfil).intValue() > 0;
        break;
    } //switch
    return regresar;
  } //ejecutar
}
