package mx.org.kaana.libs.pagina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;

public final class UISelect {
	
  private UISelect() {
  }

  public static String toValues(IBaseDto component, List<String> names, String token) {
    StringBuilder sb= new StringBuilder();
    for(String field: names) {
      Object value= component.toValue(field);
      sb.append(value!= null? value.toString(): "");
      sb.append(token);
    } // for
    return sb.substring(0, sb.length()- token.length());
  }	
	
	private static List<UISelectItem> buildFree(List<IBaseDto> dtos, List<String> fields, String token, EFormatoDinamicos formato, String value) {
    List<UISelectItem> regresar= new ArrayList<>();
    if (dtos!= null && dtos.size()>0)  {
      for(IBaseDto item: dtos) {
        switch (formato)  {
          case LETRA_CAPITAL:
            regresar.add(new UISelectItem(value!= null? item.toValue(value): item.getKey(), Methods.ajustar(toValues(item, fields, token))));
            break;
          case MAYUSCULAS:
            regresar.add(new UISelectItem(value!= null? item.toValue(value): item.getKey(), toValues(item, fields, token).toString().toUpperCase()));
            break;
          case LIBRE:
            regresar.add(new UISelectItem(value!= null? item.toValue(value): item.getKey(), toValues(item, fields, token).toString()));
            break;
        } // switch
      } // for
    } // if
    return regresar;
  }

  public static List<UISelectItem> build(List<IBaseDto> dtos, List<String> fields, String token, EFormatoDinamicos formato, String value) {
    List<UISelectItem> regresar= new ArrayList<>();
    if (dtos!= null && dtos.size()>0)  {
      for(IBaseDto item: dtos) {
        switch (formato)  {
          case LETRA_CAPITAL:
            regresar.add(new UISelectItem(value!= null? item.toValue(value) : item.getKey(), Methods.ajustar(toValues(item, fields, token))));
            break;
          case MAYUSCULAS:
            regresar.add(new UISelectItem(value!= null? item.toValue(value): item.getKey(), toValues(item, fields, token).toString().toUpperCase()));
            break;
          case LIBRE:
            regresar.add(new UISelectItem(value!= null? item.toValue(value): item.getKey(), toValues(item, fields, token).toString()));
            break;
        } // switch
      } // for
    } // if
    return regresar;
  }

  public static List<UISelectItem> build(List<IBaseDto> dtos, List<String> fields, String token, EFormatoDinamicos formato) {
    return build(dtos, fields, token, formato, null);
  }

  public static List<UISelectItem> build(List<IBaseDto> dtos, List<String> fields, String token) {
    return build(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }

  public static List<UISelectItem> build(List<IBaseDto> dtos, List<String> fields) {
    return build(dtos, fields, " ");
  }

  public static List<UISelectItem> build(List<IBaseDto> dtos, String fields) {
    return build(dtos, Cadena.toList(fields));
  }

  public static List<UISelectItem> build(Class dto, List<String> fields, String token, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findAll(dto, records);
      regresar= build(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      if (dtos != null)
        dtos.clear();
      dtos = null;
    } // finally
    return regresar;
  }
	
  public static List<UISelectItem> build(Class dto, List<String> fields, String token) {
		return build(dto, fields, token, Constantes.SQL_MAXIMO_REGISTROS);
	}

  public static List<UISelectItem> build(Class dto, List<String> fields) {
		return build(dto, fields, " ");
	}

  public static List<UISelectItem> build(Class dto, String fields, String token) {
    return build(dto, Cadena.toList(fields), token);
  }

  public static List<UISelectItem> build(Class dto, String fields) {
    return build(dto, fields, " ");
  }

  public static List<UISelectItem> build(Class dto, Map params, List<String> fields, String token, EFormatoDinamicos formato, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findViewCriteria(dto, params, records);
      regresar= build(dtos, fields, token, formato);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      if (dtos != null)
        dtos.clear();
      dtos = null;
    } // finally
    return regresar;
  }

  public static List<UISelectItem> build(Class dto, Map params, List<String> fields, String token, EFormatoDinamicos formato) {
		return build(dto, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectItem> build(Class dto, Map params, List<String> fields, String token) {
    return build(dto, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }

  public static List<UISelectItem> build(Class dto, Map params, List<String> fields) {
    return build(dto, params, fields, " ");
  }

  public static List<UISelectItem> build(Class dto, Map params, String fields, String token, EFormatoDinamicos formato) {
    return build(dto, params, Cadena.toList(fields), token, formato);
  }

  public static List<UISelectItem> build(Class dto, Map params, String fields, String token) {
    return build(dto, params, Cadena.toList(fields), token, EFormatoDinamicos.LETRA_CAPITAL);
  }

  public static List<UISelectItem> build(Class dto, Map params, String fields) {
    return build(dto, params, fields, " ");
  }

  public static List<UISelectItem> build(String proceso, String id, Map params, List<String> fields, String token, EFormatoDinamicos formato, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);
      regresar= build(dtos, fields, token, formato);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      if (dtos != null)
        dtos.clear();
      dtos = null;
    } // finally
    return regresar;
  }

  public static List<UISelectItem> build(String proceso, String id, Map params, List<String> fields, String token, EFormatoDinamicos formato) {
		return build(proceso, id, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectItem> build(String proceso, String id, Map params, List<String> fields, String token) {
		return build(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
	}
	
  public static List<UISelectItem> build(String proceso, String id, Map params, List<String> fields) {
		return build(proceso, id, params, fields, " ");
	}
	
  public static List<UISelectItem> build(String proceso, String id, Map params, String fields, String token, EFormatoDinamicos formato) {
    return build(proceso, id, params, Cadena.toList(fields), token, formato);
  }

  public static List<UISelectItem> build(String proceso, String id, Map params, String fields, String token) {
    return build(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }

  public static List<UISelectItem> build(String proceso, String id, Map params, String fields, EFormatoDinamicos formato) {
    return build(proceso, id, params, fields, " ", formato);
  }

  public static List<UISelectItem> build(String proceso, String id, Map params, String fields) {
    return build(proceso, id, params, fields, " ");
  }
	
  public static List<UISelectItem> build(String proceso, String id, Map params, String fields, EFormatoDinamicos formato, Long records) {
    return build(proceso, id, params, Cadena.toList(fields), " ", formato, records);
  }

  public static List<UISelectItem> build(String proceso, String id, String fields) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return build(proceso, id, params, fields);
  }

  public static List<UISelectItem> build(String proceso, Map params, String fields) {
    return build(proceso, Constantes.DML_SELECT, params, fields);
  }

  public static List<UISelectItem> build(String proceso, Map params, String fields, String token, EFormatoDinamicos formato) {
    return build(proceso, Constantes.DML_SELECT, params, fields, token, formato);
  }

  public static List<UISelectItem> build(String proceso, Map params, String fields, EFormatoDinamicos formato) {
    return build(proceso, Constantes.DML_SELECT, params, fields, " ", formato);
  }
	
  public static String buscarEtiqueta(List<UISelectItem> origen, Long buscar) {
    int posicion        = -1;
    String regresar     = null;
    String mensajeError = null;
    if (origen == null || origen.isEmpty())
      mensajeError = "La lista enviada está vacía o es nula";
    else if (buscar == null || buscar < 0L)
      mensajeError = "Elemento a buscar no válido : " + buscar;
    if (mensajeError != null)
      throw new IllegalArgumentException(mensajeError);
    posicion = origen.indexOf(new UISelectItem(buscar, ""));
    regresar = ((UISelectItem)origen.get(posicion)).getLabel();
    return regresar;
  } // buscarEtiqueta

  public static boolean existeLabelEnLista(List<UISelectItem> origen, String buscar) {
    int posicion        = -1;
    boolean regresar    = false;
    String mensajeError = null;
    if ( origen == null)
      mensajeError = "La lista enviada  es nula";
    else
			if (buscar == null || buscar.length() == 0)
        mensajeError = "Elemento a buscar no válido :"+ buscar;
    if (mensajeError != null)
      throw new IllegalArgumentException(mensajeError);
    for(UISelectItem item : origen) {
      if (item.getLabel().equals(buscar)) {
        regresar = true;
        break;
      } // if
    } // for
    return regresar;
  }

  public static List<UISelectItem> free(String proceso, String id, Map params, List<String> fields, String token, EFormatoDinamicos formato, String value, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);
      regresar= buildFree(dtos, fields, token, formato, value);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      if (dtos != null)
        dtos.clear();
      dtos = null;
    } // finally
    return regresar;
  }

	public static List<UISelectItem> free(String proceso, String id, Map params, List<String> fields, String token, EFormatoDinamicos formato, String value) {
		return free(proceso, id, params, fields, token, formato, value, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, List<String> fields, String token, String value) {
		return free(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, List<String> fields, String value) {
		return free(proceso, id, params, fields, " ", value);
	}
	
	public static List<UISelectItem> free(String proceso, String id, List<String> fields, String value) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return free(proceso, id, params, fields, value);
	}
	
	public static List<UISelectItem> free(String proceso, List<String> fields, String value) {
		return free(proceso, Constantes.DML_SELECT, fields, value);
	}

	public static List<UISelectItem> free(String proceso, Map params, List<String> fields, String value) {
		return free(proceso, Constantes.DML_SELECT, params, fields, value);
	}

	public static List<UISelectItem> free(String proceso, String id, Map params, String fields, String token, EFormatoDinamicos formato, String value) {
		return free(proceso, id, params, Cadena.toList(fields), token, formato, value, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, String fields, String token, EFormatoDinamicos formato, String value, Long totalRegistros) {
		return free(proceso, id, params, Cadena.toList(fields), token, formato, value, totalRegistros);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, String fields, String token, String value) {
		return free(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, String fields, String value) {
		return free(proceso, id, params, fields, " ", value);
	}
	
	public static List<UISelectItem> free(String proceso, String id, String fields, String value) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return free(proceso, id, params, fields, value);
	}
	
	public static List<UISelectItem> free(String proceso, String fields, String value) {
		return free(proceso, Constantes.DML_SELECT, fields, value);
	}

	public static List<UISelectItem> free(String proceso, Map params, String fields, String value) {
		return free(proceso, Constantes.DML_SELECT, params, fields, value);
	}	
}
