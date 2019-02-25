package mx.org.kaana.libs.pagina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.IFormatosKajool;
import mx.org.kaana.libs.formato.Global;

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
	
	public static List<UISelectItem> build(List<IBaseDto> dtos, List<String> fields, String token, IFormatosKajool formato, String value) {
    List<UISelectItem> regresar= new ArrayList<>();
    if (dtos!= null && dtos.size()>0)  {
      for(IBaseDto item: dtos) {
				String text= toValues(item, fields, token);
				//if(formato.equals(EFormatoDinamicos.LETRA_CAPITAL))
				//	text= Methods.ajustar(text);
        regresar.add(new UISelectItem(value!= null? item.toValue(value): item.getKey(), Global.format(formato, text)));
      } // for 
    } // if
    return regresar;    
  }
  
	public static List<UISelectItem> personalizado(List<IBaseDto> dtos, List<String> fields, String token, IFormatosKajool formato, String value, Map<Object,String> items) {
    List<UISelectItem> regresar= new ArrayList<>();
    for(Map.Entry<Object,String> item : items.entrySet())
      regresar.add(new UISelectItem(item.getKey(), item.getValue()));
    regresar.addAll(build(dtos, fields, token, formato, value));
    return regresar;    
  }
  
	public static List<UISelectItem> todos(List<IBaseDto> dtos, List<String> fields, String token, IFormatosKajool formato, String value) {
    List<UISelectItem> regresar= new ArrayList<>();
    regresar.add(new UISelectItem(-1L, "TODOS"));
    regresar.addAll(build(dtos, fields, token, formato, value));
    return regresar;    
  }
  
	public static List<UISelectItem> seleccione(List<IBaseDto> dtos, List<String> fields, String token, IFormatosKajool formato, String value) {
    List<UISelectItem> regresar= new ArrayList<>();
    regresar.add(new UISelectItem(-1L, "SELECCIONE"));
    regresar.addAll(build(dtos, fields, token, formato, value));
    return regresar;    
  }
  
  public static List<UISelectItem> build(List<IBaseDto> dtos, List<String> fields, String token, IFormatosKajool formato) {
    return build(dtos, fields, token, formato, null);    
  }
  
  public static List<UISelectItem> personalizado(List<IBaseDto> dtos, List<String> fields, String token, IFormatosKajool formato, Map<Object,String> items) {
    return personalizado(dtos, fields, token, formato, null, items);    
  }
  
  public static List<UISelectItem> todos(List<IBaseDto> dtos, List<String> fields, String token, IFormatosKajool formato) {
    return todos(dtos, fields, token, formato, null);    
  }
  
  public static List<UISelectItem> seleccione(List<IBaseDto> dtos, List<String> fields, String token, IFormatosKajool formato) {
    return seleccione(dtos, fields, token, formato, null);    
  }
  
  public static List<UISelectItem> build(List<IBaseDto> dtos, List<String> fields, String token) {
    return build(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }

  public static List<UISelectItem> personalizado(List<IBaseDto> dtos, List<String> fields, String token, Map<Object,String> items) {
    return personalizado(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL, items);
  }
  
  public static List<UISelectItem> todos(List<IBaseDto> dtos, List<String> fields, String token) {
    return todos(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }

  public static List<UISelectItem> seleccione(List<IBaseDto> dtos, List<String> fields, String token) {
    return seleccione(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }

  public static List<UISelectItem> build(List<IBaseDto> dtos, List<String> fields) {
    return build(dtos, fields, " ");
  }

  public static List<UISelectItem> todos(List<IBaseDto> dtos, List<String> fields) {
    return todos(dtos, fields, " ");
  }

  public static List<UISelectItem> personalizado(List<IBaseDto> dtos, List<String> fields, Map<Object,String> items) {
    return personalizado(dtos, fields, " ", items);
  }

  public static List<UISelectItem> seleccione(List<IBaseDto> dtos, List<String> fields) {
    return seleccione(dtos, fields, " ");
  }

  public static List<UISelectItem> build(List<IBaseDto> dtos, String fields) {
    return build(dtos, Cadena.toList(fields));
  }
  
  public static List<UISelectItem> todos(List<IBaseDto> dtos, String fields) {
    return todos(dtos, Cadena.toList(fields));
  }
  
  public static List<UISelectItem> personalizado(List<IBaseDto> dtos, String fields, Map<Object,String> items) {
    return personalizado(dtos, Cadena.toList(fields), items);
  }
  
  public static List<UISelectItem> seleccione(List<IBaseDto> dtos, String fields) {
    return seleccione(dtos, Cadena.toList(fields));
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
	
  public static List<UISelectItem> personalizado(Class dto, List<String> fields, String token, Long records, Map<Object,String> items) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findAll(dto, records);   
      regresar= personalizado(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL, items);
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
	
  public static List<UISelectItem> todos(Class dto, List<String> fields, String token, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findAll(dto, records);   
      regresar= todos(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
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
	
  public static List<UISelectItem> seleccione(Class dto, List<String> fields, String token, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findAll(dto, records);   
      regresar= seleccione(dtos, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
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

  public static List<UISelectItem> personalizado(Class dto, List<String> fields, String token, Map<Object,String> items) {
		return personalizado(dto, fields, token, Constantes.SQL_MAXIMO_REGISTROS, items);
	}
  
  public static List<UISelectItem> todos(Class dto, List<String> fields, String token) {
		return todos(dto, fields, token, Constantes.SQL_MAXIMO_REGISTROS);
	}

  public static List<UISelectItem> seleccione(Class dto, List<String> fields, String token) {
		return seleccione(dto, fields, token, Constantes.SQL_MAXIMO_REGISTROS);
	}

  public static List<UISelectItem> build(Class dto, List<String> fields) {
		return build(dto, fields, " ");
	}
  
  public static List<UISelectItem> todos(Class dto, List<String> fields, Map<Object,String> items) {
		return personalizado(dto, fields, " ", items);
	}
  
  public static List<UISelectItem> todos(Class dto, List<String> fields) {
		return todos(dto, fields, " ");
	}
  
  public static List<UISelectItem> seleccione(Class dto, List<String> fields) {
		return seleccione(dto, fields, " ");
	}

  public static List<UISelectItem> build(Class dto, String fields, String token) {
    return build(dto, Cadena.toList(fields), token);
  }
  
  public static List<UISelectItem> todos(Class dto, String fields, String token) {
    return todos(dto, Cadena.toList(fields), token);
  }
  
  public static List<UISelectItem> personalizado(Class dto, String fields, String token, Map<Object,String> items) {
    return personalizado(dto, Cadena.toList(fields), token, items);
  }
  
  public static List<UISelectItem> seleccione(Class dto, String fields, String token) {
    return seleccione(dto, Cadena.toList(fields), token);
  }

  public static List<UISelectItem> build(Class dto, String fields) {
    return build(dto, fields, " ");
  }
  
  public static List<UISelectItem> todos(Class dto, String fields) {
    return todos(dto, fields, " ");
  }
  
  public static List<UISelectItem> personalizado(Class dto, String fields, Map<Object,String> items) {
    return personalizado(dto, fields, " ", items);
  }
  
  public static List<UISelectItem> seleccione(Class dto, String fields) {
    return seleccione(dto, fields, " ");
  }

  public static List<UISelectItem> build(Class dto, Map params, List<String> fields, String token, IFormatosKajool formato, Long records) {
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
      
  public static List<UISelectItem> todos(Class dto, Map params, List<String> fields, String token, IFormatosKajool formato, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findViewCriteria(dto, params, records);   
      regresar= todos(dtos, fields, token, formato);
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
      
  public static List<UISelectItem> personalizado(Class dto, Map params, List<String> fields, String token, IFormatosKajool formato, Long records, Map<Object,String> items) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findViewCriteria(dto, params, records);   
      regresar= personalizado(dtos, fields, token, formato, items);
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
      
  public static List<UISelectItem> seleccione(Class dto, Map params, List<String> fields, String token, IFormatosKajool formato, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().findViewCriteria(dto, params, records);   
      regresar= seleccione(dtos, fields, token, formato);
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
      
  public static List<UISelectItem> build(Class dto, Map params, List<String> fields, String token, IFormatosKajool formato) {
		return build(dto, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
  
  public static List<UISelectItem> todos(Class dto, Map params, List<String> fields, String token, IFormatosKajool formato) {
		return todos(dto, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
  
  public static List<UISelectItem> personalizado(Class dto, Map params, List<String> fields, String token, IFormatosKajool formato, Map<Object,String> items) {
		return personalizado(dto, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS, items);
	}
  
  public static List<UISelectItem> seleccione(Class dto, Map params, List<String> fields, String token, IFormatosKajool formato) {
		return seleccione(dto, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectItem> build(Class dto, Map params, List<String> fields, String token) {
    return build(dto, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> todos(Class dto, Map params, List<String> fields, String token) {
    return todos(dto, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> personalizado(Class dto, Map params, List<String> fields, String token, Map<Object,String> items) {
    return personalizado(dto, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, items);
  }  
  
  public static List<UISelectItem> seleccione(Class dto, Map params, List<String> fields, String token) {
    return seleccione(dto, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> build(Class dto, Map params, List<String> fields) {
    return build(dto, params, fields, " ");
  }  
  
  public static List<UISelectItem> todos(Class dto, Map params, List<String> fields) {
    return todos(dto, params, fields, " ");
  }  
  
  public static List<UISelectItem> personalizado(Class dto, Map params, List<String> fields, Map<Object,String> items) {
    return personalizado(dto, params, fields, " ", items);
  }  
  
  public static List<UISelectItem> seleccione(Class dto, Map params, List<String> fields) {
    return seleccione(dto, params, fields, " ");
  }  
  
  public static List<UISelectItem> build(Class dto, Map params, String fields, String token, IFormatosKajool formato) {
    return build(dto, params, Cadena.toList(fields), token, formato);
  }
  
  public static List<UISelectItem> todos(Class dto, Map params, String fields, String token, IFormatosKajool formato) {
    return todos(dto, params, Cadena.toList(fields), token, formato);
  }
  
  public static List<UISelectItem> personalizado(Class dto, Map params, String fields, String token, IFormatosKajool formato, Map<Object,String> items) {
    return personalizado(dto, params, Cadena.toList(fields), token, formato, items);
  }
  
  public static List<UISelectItem> seleccione(Class dto, Map params, String fields, String token, IFormatosKajool formato) {
    return seleccione(dto, params, Cadena.toList(fields), token, formato);
  }
  
  public static List<UISelectItem> build(Class dto, Map params, String fields, String token) {
    return build(dto, params, Cadena.toList(fields), token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> todos(Class dto, Map params, String fields, String token) {
    return todos(dto, params, Cadena.toList(fields), token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> personalizado(Class dto, Map params, String fields, String token, Map<Object,String> items) {
    return personalizado(dto, params, Cadena.toList(fields), token, EFormatoDinamicos.LETRA_CAPITAL, items);
  }  
  
  public static List<UISelectItem> seleccione(Class dto, Map params, String fields, String token) {
    return seleccione(dto, params, Cadena.toList(fields), token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> build(Class dto, Map params, String fields) {
    return build(dto, params, fields, " ");
  }
  
  public static List<UISelectItem> todos(Class dto, Map params, String fields) {
    return todos(dto, params, fields, " ");
  }
  
  public static List<UISelectItem> personalizado(Class dto, Map params, String fields, Map<Object,String> items) {
    return personalizado(dto, params, fields, " ", items);
  }
  
  public static List<UISelectItem> seleccione(Class dto, Map params, String fields) {
    return seleccione(dto, params, fields, " ");
  }

  public static List<UISelectItem> build(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, Long records) {
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
  
  public static List<UISelectItem> todos(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar= todos(dtos, fields, token, formato);
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
  
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, Long records, Map<Object,String> items) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar= personalizado(dtos, fields, token, formato, items);
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
  
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar= seleccione(dtos, fields, token, formato);
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
  
  public static List<UISelectItem> build(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato) {
		return build(proceso, id, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectItem> todos(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato) {
		return todos(proceso, id, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, Map<Object,String> items) {
		return personalizado(proceso, id, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS, items);
	}
	
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato) {
		return seleccione(proceso, id, params, fields, token, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectItem> build(String proceso, String id, Map params, List<String> fields, String token) {
		return build(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
	}
	
  public static List<UISelectItem> todos(String proceso, String id, Map params, List<String> fields, String token) {
		return todos(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
	}
	
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, List<String> fields, String token, Map<Object,String> items) {
		return personalizado(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, items);
	}
	
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, List<String> fields, String token) {
		return seleccione(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
	}
	
  public static List<UISelectItem> build(String proceso, String id, Map params, List<String> fields) {
		return build(proceso, id, params, fields, " ");
	}
	
  public static List<UISelectItem> todos(String proceso, String id, Map params, List<String> fields) {
		return todos(proceso, id, params, fields, " ");
	}
	
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, List<String> fields, Map<Object,String> items) {
		return personalizado(proceso, id, params, fields, " ", items);
	}
	
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, List<String> fields) {
		return seleccione(proceso, id, params, fields, " ");
	}
	
  public static List<UISelectItem> build(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato) {
    return build(proceso, id, params, Cadena.toList(fields), token, formato);
  }
  
  public static List<UISelectItem> todos(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato) {
    return todos(proceso, id, params, Cadena.toList(fields), token, formato);
  }
  
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, Map<Object,String> items) {
    return personalizado(proceso, id, params, Cadena.toList(fields), token, formato, items);
  }
  
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato) {
    return seleccione(proceso, id, params, Cadena.toList(fields), token, formato);
  }
  
  public static List<UISelectItem> build(String proceso, String id, Map params, String fields, String token) {
    return build(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> todos(String proceso, String id, Map params, String fields, String token) {
    return todos(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, String fields, String token, Map<Object,String> items) {
    return personalizado(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, items);
  }  
  
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, String fields, String token) {
    return seleccione(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL);
  }  
  
  public static List<UISelectItem> build(String proceso, String id, Map params, String fields, IFormatosKajool formato) {
    return build(proceso, id, params, fields, " ", formato);
  }
  
  public static List<UISelectItem> todos(String proceso, String id, Map params, String fields, IFormatosKajool formato) {
    return todos(proceso, id, params, fields, " ", formato);
  }
  
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, String fields, IFormatosKajool formato, Map<Object,String> items) {
    return personalizado(proceso, id, params, fields, " ", formato, items);
  }
  
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, String fields, IFormatosKajool formato) {
    return seleccione(proceso, id, params, fields, " ", formato);
  }
  
  public static List<UISelectItem> build(String proceso, String id, Map params, String fields) {
    return build(proceso, id, params, fields, " ");
  }
	
  public static List<UISelectItem> todos(String proceso, String id, Map params, String fields) {
    return todos(proceso, id, params, fields, " ");
  }
	
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, String fields, Map<Object,String> items) {
    return personalizado(proceso, id, params, fields, " ", items);
  }
	
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, String fields) {
    return seleccione(proceso, id, params, fields, " ");
  }
	
  public static List<UISelectItem> build(String proceso, String id, Map params, String fields, IFormatosKajool formato, Long records) {
    return build(proceso, id, params, Cadena.toList(fields), " ", formato, records);
  }
  
  public static List<UISelectItem> todos(String proceso, String id, Map params, String fields, IFormatosKajool formato, Long records) {
    return todos(proceso, id, params, Cadena.toList(fields), " ", formato, records);
  }
  
  public static List<UISelectItem> personalizado(String proceso, String id, Map params, String fields, IFormatosKajool formato, Long records, Map<Object,String> items) {
    return personalizado(proceso, id, params, Cadena.toList(fields), " ", formato, records, items);
  }
  
  public static List<UISelectItem> seleccione(String proceso, String id, Map params, String fields, IFormatosKajool formato, Long records) {
    return seleccione(proceso, id, params, Cadena.toList(fields), " ", formato, records);
  }
  
  public static List<UISelectItem> build(String proceso, String id, String fields) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return build(proceso, id, params, fields);
  } 
  
  public static List<UISelectItem> todos(String proceso, String id, String fields) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return todos(proceso, id, params, fields);
  } 
  
  public static List<UISelectItem> personalizado(String proceso, String id, String fields, Map<Object,String> items) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return personalizado(proceso, id, params, fields, items);
  } 
  
  public static List<UISelectItem> seleccione(String proceso, String id, String fields) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return seleccione(proceso, id, params, fields);
  } 
  
  public static List<UISelectItem> build(String proceso, Map params, String fields) {
    return build(proceso, Constantes.DML_SELECT, params, fields);
  } 
    
  public static List<UISelectItem> todos(String proceso, Map params, String fields) {
    return todos(proceso, Constantes.DML_SELECT, params, fields);
  } 
    
  public static List<UISelectItem> personalizado(String proceso, Map params, String fields, Map<Object,String> items) {
    return personalizado(proceso, Constantes.DML_SELECT, params, fields, items);
  } 
    
  public static List<UISelectItem> seleccione(String proceso, Map params, String fields) {
    return seleccione(proceso, Constantes.DML_SELECT, params, fields);
  } 
    
  public static List<UISelectItem> build(String proceso, Map params, String fields, String token, IFormatosKajool formato) {
    return build(proceso, Constantes.DML_SELECT, params, fields, token, formato);
  } 
    
  public static List<UISelectItem> todos(String proceso, Map params, String fields, String token, IFormatosKajool formato) {
    return todos(proceso, Constantes.DML_SELECT, params, fields, token, formato);
  } 
    
  public static List<UISelectItem> personalizado(String proceso, Map params, String fields, String token, IFormatosKajool formato, Map<Object,String> items) {
    return personalizado(proceso, Constantes.DML_SELECT, params, fields, token, formato, items);
  } 
    
  public static List<UISelectItem> seleccione(String proceso, Map params, String fields, String token, IFormatosKajool formato) {
    return seleccione(proceso, Constantes.DML_SELECT, params, fields, token, formato);
  } 
    
  public static List<UISelectItem> build(String proceso, Map params, String fields, IFormatosKajool formato) {
    return build(proceso, Constantes.DML_SELECT, params, fields, " ", formato);
  } 
	
  public static List<UISelectItem> todos(String proceso, Map params, String fields, IFormatosKajool formato) {
    return todos(proceso, Constantes.DML_SELECT, params, fields, " ", formato);
  } 
	
  public static List<UISelectItem> personalizado(String proceso, Map params, String fields, IFormatosKajool formato, Map<Object,String> items) {
    return personalizado(proceso, Constantes.DML_SELECT, params, fields, " ", formato, items);
  } 
	
  public static List<UISelectItem> seleccione(String proceso, Map params, String fields, IFormatosKajool formato) {
    return seleccione(proceso, Constantes.DML_SELECT, params, fields, " ", formato);
  } 
	
  public static String buscarEtiqueta(List<UISelectItem> origen, Object buscar) {    
    int posicion        = -1;
    String regresar     = null;
    String mensajeError = null;
    if (origen == null || origen.isEmpty())
      mensajeError = "La lista enviada esta vacia o es nula";
    if (mensajeError != null)
      throw new IllegalArgumentException(mensajeError);
    posicion = origen.indexOf(new UISelectItem(buscar, ""));
    regresar = ((UISelectItem)origen.get(posicion)).getLabel();
    return regresar;    
  } // buscarEtiqueta
  
  public static String buscarEtiquetas(List<UISelectItem> origen, Object[] busquedas){
    StringBuilder regresar= new StringBuilder();
    for(Object busqueda: busquedas)
      regresar.append(buscarEtiqueta(origen, Long.valueOf(busqueda.toString()))).append(",");
    if (regresar.length()!=0)
      regresar.deleteCharAt(regresar.length()-1);
    return regresar.toString();
  } // buscarEtiquetas
  
  public static Long buscarIdKey(List<UISelectItem> origen, String buscar) {    
    Long regresar       = -1L;
    String mensajeError = null;
    if ( origen == null)
      mensajeError = "La lista enviada  es nula";
    else 
			if (buscar == null || buscar.length() == 0)
        mensajeError = "Elemento a buscar no valido :"+ buscar;    
    if (mensajeError != null)
      throw new IllegalArgumentException(mensajeError);
    for(UISelectItem item : origen) {
      if (item.getLabel().equals(buscar)) {
        regresar = (Long) item.getValue();
        break;
      } // if
    } // for
    return regresar;    
  }
  
  public static boolean existeLabelEnLista(List<UISelectItem> origen, String buscar) {    
    boolean regresar    = false;
    String mensajeError = null;
    if ( origen == null)
      mensajeError = "La lista enviada  es nula";
    else 
			if (buscar == null || buscar.length() == 0)
        mensajeError = "Elemento a buscar no valido :"+ buscar;    
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

  public static List<UISelectItem> free(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, String value, Long records) {
    List<UISelectItem> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar= build(dtos, fields, token, formato, value);
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
  
  public static List<UISelectItem> todosFree(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, String value, Long records) {
    List<UISelectItem> regresar= new ArrayList();
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar.add(new UISelectItem(-1L, "TODOS"));
      regresar.addAll(build(dtos, fields, token, formato, value));
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
  
  public static List<UISelectItem> personalizadoFree(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, String value, Long records, Map<Object,String> items) {
    List<UISelectItem> regresar= new ArrayList();
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      for(Map.Entry<Object,String> item : items.entrySet())
        regresar.add(new UISelectItem(item.getKey(), item.getValue()));
      regresar.addAll(build(dtos, fields, token, formato, value));
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

  public static List<UISelectItem> seleccioneFree(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, String value, Long records) {
    List<UISelectItem> regresar= new ArrayList();
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar.add(new UISelectItem(-1L, "SELECCIONE"));
      regresar.addAll(build(dtos, fields, token, formato, value));
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

	public static List<UISelectItem> free(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, String value) {
		return free(proceso, id, params, fields, token, formato, value, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
	public static List<UISelectItem> todosFree(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, String value) {
		return todosFree(proceso, id, params, fields, token, formato, value, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
	public static List<UISelectItem> personalizadoFree(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, id, params, fields, token, formato, value, Constantes.SQL_MAXIMO_REGISTROS, items);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, Map params, List<String> fields, String token, IFormatosKajool formato, String value) {
		return seleccioneFree(proceso, id, params, fields, token, formato, value, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, List<String> fields, String token, String value) {
		return free(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value);
	}
	
	public static List<UISelectItem> todosFree(String proceso, String id, Map params, List<String> fields, String token, String value) {
		return todosFree(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value);
	}
	
	public static List<UISelectItem> personalizadoFree(String proceso, String id, Map params, List<String> fields, String token, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value, items);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, Map params, List<String> fields, String token, String value) {
		return seleccioneFree(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, List<String> fields, String value) {
		return free(proceso, id, params, fields, " ", value);
	}
	
	public static List<UISelectItem> todosFree(String proceso, String id, Map params, List<String> fields, String value) {
		return todosFree(proceso, id, params, fields, " ", value);
	}
	
	public static List<UISelectItem> personalizadoFree(String proceso, String id, Map params, List<String> fields, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, id, params, fields, " ", value, items);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, Map params, List<String> fields, String value) {
		return seleccioneFree(proceso, id, params, fields, " ", value);
	}
	
	public static List<UISelectItem> free(String proceso, String id, List<String> fields, String value) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return free(proceso, id, params, fields, value);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, List<String> fields, String value) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return seleccioneFree(proceso, id, params, fields, value);
	}
	
	public static List<UISelectItem> todosFree(String proceso, String id, List<String> fields, String value) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return todosFree(proceso, id, params, fields, value);
	}
	
	public static List<UISelectItem> personalizadoFree(String proceso, String id, List<String> fields, String value, Map<Object,String> items) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return personalizadoFree(proceso, id, params, fields, value, items);
	}
	
	public static List<UISelectItem> free(String proceso, List<String> fields, String value) {
		return free(proceso, Constantes.DML_SELECT, fields, value);
	}

	public static List<UISelectItem> todosFree(String proceso, List<String> fields, String value) {
		return todosFree(proceso, Constantes.DML_SELECT, fields, value);
	}
  
	public static List<UISelectItem> personalizadoFree(String proceso, List<String> fields, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, Constantes.DML_SELECT, fields, value, items);
	}

	public static List<UISelectItem> seleccioneFree(String proceso, List<String> fields, String value) {
		return seleccioneFree(proceso, Constantes.DML_SELECT, fields, value);
	}

	public static List<UISelectItem> free(String proceso, Map params, List<String> fields, String value) {
		return free(proceso, Constantes.DML_SELECT, params, fields, value);
	}
  
	public static List<UISelectItem> todosFree(String proceso, Map params, List<String> fields, String value) {
		return todosFree(proceso, Constantes.DML_SELECT, params, fields, value);
	}
  
	public static List<UISelectItem> personalizadoFree(String proceso, Map params, List<String> fields, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, Constantes.DML_SELECT, params, fields, value, items);
	}
  
	public static List<UISelectItem> seleccioneFree(String proceso, Map params, List<String> fields, String value) {
		return seleccioneFree(proceso, Constantes.DML_SELECT, params, fields, value);
	}

	public static List<UISelectItem> free(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, String value) {
		return free(proceso, id, params, Cadena.toList(fields), token, formato, value, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
	public static List<UISelectItem> todosFree(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, String value) {
		return todosFree(proceso, id, params, Cadena.toList(fields), token, formato, value, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
	public static List<UISelectItem> personalizadoFree(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, id, params, Cadena.toList(fields), token, formato, value, Constantes.SQL_MAXIMO_REGISTROS, items);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, String value) {
		return seleccioneFree(proceso, id, params, Cadena.toList(fields), token, formato, value, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, String value, Long totalRegistros) {
		return free(proceso, id, params, Cadena.toList(fields), token, formato, value, totalRegistros);
	}
	
	public static List<UISelectItem> todosFree(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, String value, Long totalRegistros) {
		return todosFree(proceso, id, params, Cadena.toList(fields), token, formato, value, totalRegistros);
	}
	
	public static List<UISelectItem> personalizadoFree(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, String value, Long totalRegistros, Map<Object,String> items) {
		return personalizadoFree(proceso, id, params, Cadena.toList(fields), token, formato, value, totalRegistros, items);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, Map params, String fields, String token, IFormatosKajool formato, String value, Long totalRegistros) {
		return seleccioneFree(proceso, id, params, Cadena.toList(fields), token, formato, value, totalRegistros);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, String fields, String token, String value) {
		return free(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value);
	}
	
	public static List<UISelectItem> todosFree(String proceso, String id, Map params, String fields, String token, String value) {
		return todosFree(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value);
	}
	
	public static List<UISelectItem> personalizadoFree(String proceso, String id, Map params, String fields, String token, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value, items);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, Map params, String fields, String token, String value) {
		return seleccioneFree(proceso, id, params, fields, token, EFormatoDinamicos.LETRA_CAPITAL, value);
	}
	
	public static List<UISelectItem> free(String proceso, String id, Map params, String fields, String value) {
		return free(proceso, id, params, fields, " ", value);
	}
	
  public static List<UISelectItem> todosFree(String proceso, String id, Map params, String fields, String value) {
		return todosFree(proceso, id, params, fields, " ", value);
	}
	
  public static List<UISelectItem> personalizadoFree(String proceso, String id, Map params, String fields, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, id, params, fields, " ", value, items);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, Map params, String fields, String value) {
		return seleccioneFree(proceso, id, params, fields, " ", value);
	}
	
	public static List<UISelectItem> free(String proceso, String id, String fields, String value) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return free(proceso, id, params, fields, value);
	}
	
	public static List<UISelectItem> todosFree(String proceso, String id, String fields, String value) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return todosFree(proceso, id, params, fields, value);
	}
	
	public static List<UISelectItem> personalizadoFree(String proceso, String id, String fields, String value, Map<Object,String> items) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return personalizadoFree(proceso, id, params, fields, value, items);
	}
	
	public static List<UISelectItem> seleccioneFree(String proceso, String id, String fields, String value) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		return seleccioneFree(proceso, id, params, fields, value);
	}
	
	public static List<UISelectItem> free(String proceso, String fields, String value) {
		return free(proceso, Constantes.DML_SELECT, fields, value);
	}

	public static List<UISelectItem> todosFree(String proceso, String fields, String value) {
		return todosFree(proceso, Constantes.DML_SELECT, fields, value);
	}
  
	public static List<UISelectItem> personalizadoFree(String proceso, String fields, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, Constantes.DML_SELECT, fields, value, items);
	}

	public static List<UISelectItem> seleccioneFree(String proceso, String fields, String value) {
		return seleccioneFree(proceso, Constantes.DML_SELECT, fields, value);
	}

	public static List<UISelectItem> free(String proceso, Map params, String fields, String value) {
		return free(proceso, Constantes.DML_SELECT, params, fields, value);
	}	
  
	public static List<UISelectItem> todosFree(String proceso, Map params, String fields, String value) {
		return todosFree(proceso, Constantes.DML_SELECT, params, fields, value);
	}	
  
	public static List<UISelectItem> todosFree(String proceso, Map params, String fields, String value, Map<Object,String> items) {
		return personalizadoFree(proceso, Constantes.DML_SELECT, params, fields, value, items);
	}	
  
	public static List<UISelectItem> seleccioneFree(String proceso, Map params, String fields, String value) {
		return seleccioneFree(proceso, Constantes.DML_SELECT, params, fields, value);
	}	
  
	public static UISelectItem buildTodosItem() {
		return new UISelectItem(-1L, "TODOS");
	}	
	
}
