package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas;

import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 30/09/2015
 * @time 05:49:05 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);

  private IBaseDto dto;

  public Transaccion(IBaseDto dto) {
    this.dto = dto;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;
    switch(accion) {
     case AGREGAR:
       regresar	= DaoFactory.getInstance().insert(sesion, this.dto) >= 1L;
       break;
     case MODIFICAR:
       regresar	= DaoFactory.getInstance().update(sesion, this.dto) >= 1L;
       break;
     case ELIMINAR:
       regresar	= DaoFactory.getInstance().delete(sesion, this.dto) >= 1L;
       break;
    } // switch
    if(!regresar)
      throw new RuntimeException("No se realizo la trasacción con éxito para el catalogo ".concat(Cadena.toSqlName(this.dto.toHbmClass().getSimpleName())));
    LOG.info("La transaccion de ".concat(accion.name()).concat(" para el catalogo de  ").concat(this.dto.toHbmClass().getSimpleName()).concat(" se realizo ["+ regresar+ "]."));
    return regresar;
  }

}
