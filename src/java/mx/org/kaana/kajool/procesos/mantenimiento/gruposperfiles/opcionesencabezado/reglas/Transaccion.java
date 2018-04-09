package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesencabezado.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/09/2015
 *@time 08:44:09 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TrJanalMenusEncPerfilDto;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
  private List<Entity> listaOpciones;
  private List<Entity> listaOpcionesEliminar;
  private Long idPerfil;

  public Transaccion(List<Entity> listaOpciones, List<Entity> listaOpcionesEliminar, Long idPerfil) {
    this.listaOpciones = listaOpciones;
    this.listaOpcionesEliminar = listaOpcionesEliminar;
    this.idPerfil = idPerfil;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    try {
      switch(accion) {
        case AGREGAR :
          eleiminarPerfiles(sesion);
          agregarPerfiles(sesion);
        break;
      } // switch
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? [encabezados]."));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return true;
  } // ejecutar

  private void agregarPerfiles(Session session) throws Exception {
    try {
      for (Entity opcionEncabezado : this.listaOpciones) {
        TrJanalMenusEncPerfilDto dto = new TrJanalMenusEncPerfilDto();
        dto.setIdPerfil(this.idPerfil);
        dto.setIdMenuEncabezado(opcionEncabezado.get("idMenuEncabezado").toLong());
        if(DaoFactory.getInstance().insert(session, dto)< 1L)
          throw new RuntimeException("Error al insertar");
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // agregarPerfiles

  private void eleiminarPerfiles(Session session) throws Exception {
    try {
      for (Entity opcionEncabezado : this.listaOpcionesEliminar) {
        if(DaoFactory.getInstance().delete(session, TrJanalMenusEncPerfilDto.class, opcionEncabezado.getKey())< 1L)
          throw new RuntimeException("Error al eliminar");
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // eleiminarPerfiles

}
