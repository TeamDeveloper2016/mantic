package mx.org.kaana.libs.pagina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.reglas.comun.Columna;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 07-mar-2014
 *@time 19:38:53
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class UIEntity {
	
  private UIEntity() {
  }

  public static List<UISelectEntity> build(List<IBaseDto> dtos, List<Columna> formato) {
    List<UISelectEntity> regresar= new ArrayList<UISelectEntity>();
    Entity entity            = null;
    if (dtos!= null && dtos.size()>0)  {
      for (IBaseDto item : dtos) {
        if(!(item instanceof Entity)) {
          entity= new Entity();
          Map<String, Object> fields= item.toMap();
          for (String field : fields.keySet())
            entity.put(field, new Value(field, fields.get(field)));
        } // if
        else
          entity= (Entity)item;
        for (Columna column : formato)
          if(entity.containsKey(column.getName())) {
            Value value= new Value(column.getName(), entity.get(column.getName()));
            value.setData(Global.format(column.getFormat(), value.getData()));
          } // if
          else
            throw new RuntimeException("No existe la columna "+ column.getName()+ " en la lista de items.");
        regresar.add(new UISelectEntity(entity));
      } // for
    } // if
    Methods.clean(dtos);
    return regresar;
  }

  public static List<UISelectEntity> build(List<IBaseDto> dtos) {
    return build(dtos, Collections.EMPTY_LIST);
  }

  public static List<UISelectEntity> build(Class dto, Long records) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findAll(dto, records);
      regresar= build(dtos, Collections.EMPTY_LIST);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(dtos);
    } // finally
    return regresar;
  }
	
  public static List<UISelectEntity> build(Class dto) {
		return build(dto, Constantes.SQL_MAXIMO_REGISTROS);
	}

  public static List<UISelectEntity> build(Class dto, Map params, List<Columna> formato, Long records) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos      = null;
    try {
      dtos    = DaoFactory.getInstance().findViewCriteria(dto, params, records);
      regresar= build(dtos, formato);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(dtos);
    } // finally
    return regresar;
  }

  public static List<UISelectEntity> build(Class dto, Map params, List<Columna> formato) {
		return build(dto, params, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectEntity> build(Class dto, Map params) {
    return build(dto, params, Collections.EMPTY_LIST);
  }

  public static List<UISelectEntity> build(String proceso, String id, Map params, List<Columna> formato, Long records) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);
      regresar= build(dtos, formato);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(dtos);
    } // finally
    return regresar;
  }

  public static List<UISelectEntity> build(String proceso, String id, Map params, List<Columna> formato) {
		return build(proceso, id, params, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectEntity> build(String proceso, String id, Map params) {
		return build(proceso, id, params, Collections.EMPTY_LIST);
	}
	
  public static List<UISelectEntity> build(String proceso, String id) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return build(proceso, id, params);
  }

  public static List<UISelectEntity> build(String proceso, Map params) {
    return build(proceso, Constantes.DML_SELECT, params);
  }

  public static List<UISelectEntity> build(String proceso, Map params, List<Columna> formato) {
    return build(proceso, Constantes.DML_SELECT, params, formato);
  }

  public static List<UISelectEntity> build(String proceso, Map params, String fields, List<Columna> formato) {
    return build(proceso, Constantes.DML_SELECT, params, formato);
  }
	
}
