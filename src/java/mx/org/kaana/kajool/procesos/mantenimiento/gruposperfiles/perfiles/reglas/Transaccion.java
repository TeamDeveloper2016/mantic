package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 6:31:01 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalPerfilesDto;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.PROCESAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private TcJanalPerfilesDto tcJanalPerfilesDto;
  private Long idPerfil;
	
  public Transaccion(TcJanalPerfilesDto tcJanalPerfilesDto) {
    this.tcJanalPerfilesDto=tcJanalPerfilesDto;
    this.idPerfil= tcJanalPerfilesDto.getIdPerfil();
  }

  @Override
  public boolean ejecutar(Session session, EAccion accion) throws Exception ,RuntimeException {
    boolean regresar          = false;
    Map<String, Object> params= new HashMap();
    try {
      switch (accion) {
        case AGREGAR:
          regresar= DaoFactory.getInstance().insert(session, this.tcJanalPerfilesDto).intValue() >  0;
          break;
        case MODIFICAR:
          regresar= DaoFactory.getInstance().update(session, this.tcJanalPerfilesDto).intValue() >  0;
          break;
        case PROCESAR:
					params.put("idMenu", this.tcJanalPerfilesDto.getIdMenu());
          regresar= DaoFactory.getInstance().update(session, TcJanalPerfilesDto.class, this.tcJanalPerfilesDto.getIdPerfil(), params)>0L;
          break;
        case ELIMINAR:
          if (!existenUsuarios())
            regresar= DaoFactory.getInstance().delete(session, this.tcJanalPerfilesDto).intValue() >  0;
          else
            throw new RuntimeException("No se puede eliminar el perfil, ya que tiene usuarios asociados");
          break;
      } // switch
    } // try
    catch (Exception e) {
      throw e;
    }// catch
    return regresar;
  }// ejecutar

  private boolean existenUsuarios() throws Exception {
    boolean regresar           = false;
    Map<String, Object> params = null;
    try {
      params = new HashMap();
      params.put(Constantes.SQL_CONDICION,"id_perfil = ".concat(idPerfil.toString()));
      regresar = (DaoFactory.getInstance().findFirst(TcJanalUsuariosDto.class, params))!=null? true:false;
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }// existenUsuarios
}
