package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 6:31:01 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalPerfilesDto;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.db.dto.TrJanalPerfilesJerarquiasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private List<TrJanalPerfilesJerarquiasDto> jerarquias;
  private TcJanalPerfilesDto tcJanalPerfilesDto;
  private Long idPerfil;
	
  public Transaccion(TcJanalPerfilesDto tcJanalPerfilesDto) {
    this(null, tcJanalPerfilesDto, tcJanalPerfilesDto.getIdPerfil());
  } // Transaccion

	public Transaccion(List<TrJanalPerfilesJerarquiasDto> jerarquias, Long idPerfil) {
		this(jerarquias, null, idPerfil);
	} // Transaccion

	public Transaccion(List<TrJanalPerfilesJerarquiasDto> jerarquias, TcJanalPerfilesDto tcJanalPerfilesDto, Long idPerfil) {
		this.jerarquias        = jerarquias;
		this.tcJanalPerfilesDto= tcJanalPerfilesDto;
		this.idPerfil          = idPerfil;
	}	// Transaccion
	
  @Override
  public boolean ejecutar(Session session, EAccion accion) throws Exception ,RuntimeException {
    boolean regresar          = false;
    Map<String, Object> params= null;
    try {
      switch (accion) {
        case AGREGAR:
          regresar= DaoFactory.getInstance().insert(session, this.tcJanalPerfilesDto).intValue() > 0;
          break;
        case MODIFICAR:
          regresar= DaoFactory.getInstance().update(session, this.tcJanalPerfilesDto).intValue() > 0;
          break;
        case PROCESAR:
					params= new HashMap<>();
					params.put("idMenu", this.tcJanalPerfilesDto.getIdMenu());
          regresar= DaoFactory.getInstance().update(session, TcJanalPerfilesDto.class, this.tcJanalPerfilesDto.getIdPerfil(), params) > 0L;
          break;
        case ELIMINAR:
          if (!existenUsuarios())
            regresar= DaoFactory.getInstance().delete(session, this.tcJanalPerfilesDto).intValue() >  0;
          else
            throw new RuntimeException("No se puede eliminar el perfil, ya que tiene usuarios asociados");
          break;
				case GENERAR:
					params= new HashMap<>();
					params.put("idPerfil",this.idPerfil);
					DaoFactory.getInstance().deleteAll(session, TrJanalPerfilesJerarquiasDto.class, params);
					for (TrJanalPerfilesJerarquiasDto item: this.jerarquias) 
						DaoFactory.getInstance().insert(session, item);					
					break;
      } // switch
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // ejecutar

  private boolean existenUsuarios() throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_perfil = ".concat(this.idPerfil.toString()));
      regresar = DaoFactory.getInstance().findFirst(TcJanalUsuariosDto.class, params) != null;
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // existenUsuarios
}
