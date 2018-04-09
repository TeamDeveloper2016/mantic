package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.grupos.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 28, 2015
 *@time 11:44:12 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalGruposDto;
import mx.org.kaana.kajool.db.dto.TcJanalPerfilesDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private TcJanalGruposDto dto;

  public Transaccion (TcJanalGruposDto dto) {
    this.dto = dto;
  }

  public Transaccion (Long idGrupo) {
    this(new TcJanalGruposDto(idGrupo));
  }

  @Override
  public boolean ejecutar(Session session, EAccion accion) throws Exception, RuntimeException {
    boolean regresar= false;
    try {
	    switch (accion) {
        case AGREGAR:
          regresar = DaoFactory.getInstance().insert(session, this.dto).intValue() > 0;
          break;
        case ELIMINAR:
          if (!exitenPerfiles())
            regresar = DaoFactory.getInstance().delete(session, this.dto).intValue() > 0;
          else
            throw new RuntimeException("No se puede eliminar el grupo, ya que tiene perfiles asociados !");
          break;
        case MODIFICAR:
          regresar = DaoFactory.getInstance().update(session, this.dto).intValue() > 0;
          break;
      } // switch
    } // try
    catch (Exception e) {
	    throw e;
    } // catch
    return regresar;
  } // ejcutar

  private boolean exitenPerfiles() throws Exception {
    boolean regresar = false;
    Map params       = null;
    try {
      params= new HashMap();
      params.put(Constantes.SQL_CONDICION,"id_grupo = ".concat( this.dto.getKey().toString()));
      IBaseDto perfiles= DaoFactory.getInstance().findFirst(TcJanalPerfilesDto.class, params);
      regresar = perfiles!=null? true:false;
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally{
      Methods.clean(params);
    } // finally
    return regresar;
  } // existenPerfiles
}
