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
import mx.org.kaana.libs.recurso.LoadImages;
import org.primefaces.model.StreamedContent;

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

  public static List<UISelectEntity> build(List<? extends IBaseDto> dtos, List<Columna> formato) {
    List<UISelectEntity> regresar= new ArrayList<>();
    Entity entity                = null;
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
            entity.put(column.getName(), value);          
          } // if
          else
            throw new RuntimeException("No existe la columna "+ column.getName()+ " en la lista de items.");
        regresar.add(new UISelectEntity(entity));
      } // for
    } // if
    Methods.clean(dtos);
    return regresar;    
  } 
	
  public static List<UISelectEntity> buildImage(List<? extends IBaseDto> dtos, List<Columna> formato) {
    List<UISelectEntity> regresar= new ArrayList<>();
    Entity entity                = null;
		StreamedContent sc           = null;
    if (dtos!= null && dtos.size()>0)  {
      for (IBaseDto item : dtos) {
        if(!(item instanceof Entity)) {
          entity= new Entity();
          Map<String, Object> fields= item.toMap();
          for (String field : fields.keySet()) 
            entity.put(field, new Value(field, fields.get(field)));
        } // if  
        else
          entity= (Entity) item;
				try {
					entity.put("image", new Value("image", LoadImages.getImage(entity.toLong("idArticulo"))));
				} // try
				catch (Exception e) {
					Error.mensaje(e);					
				} // catch								
        for (Columna column : formato){ 
          if(entity.containsKey(column.getName())) {
            Value value= new Value(column.getName(), entity.get(column.getName()));          
            value.setData(Global.format(column.getFormat(), value.getData()));
            entity.put(column.getName(), value);          
          } // if
          else
            throw new RuntimeException("No existe la columna "+ column.getName()+ " en la lista de items.");
				} // for				
        regresar.add(new UISelectEntity(entity));
      } // for
    } // if
    Methods.clean(dtos);
    return regresar;    
  } 
  
  public static List<UISelectEntity> build(List<? extends IBaseDto> dtos) {
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

  public static List<UISelectEntity> build(Class dto, Map<String, Object> params, List<Columna> formato, Long records) {
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
      
  public static List<UISelectEntity> build(Class dto, Map<String, Object> params, List<Columna> formato) {
		return build(dto, params, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectEntity> build(Class dto, Map params) {
    return build(dto, params, Collections.EMPTY_LIST);
  }  
  
  public static List<UISelectEntity> build(String proceso, String id, Map<String, Object> params, List<Columna> formato, Long records) {
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
	
  public static List<UISelectEntity> buildImage(String proceso, String id, Map<String, Object> params, List<Columna> formato, Long records) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos         = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   			
      regresar= buildImage(dtos, formato);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
  
  public static List<UISelectEntity> build(String proceso, String id, Map<String, Object> params, List<Columna> formato) {
		return build(proceso, id, params, formato, Constantes.SQL_MAXIMO_REGISTROS);
	}
	
  public static List<UISelectEntity> build(String proceso, String id, Map params) {
		return build(proceso, id, params, Collections.EMPTY_LIST);
	}
	
  public static List<UISelectEntity> build(String proceso, String id) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return build(proceso, id, params);
  } 
  
  public static List<UISelectEntity> build(String proceso, Map params) {
    return build(proceso, Constantes.DML_SELECT, params);
  } 
    
  public static List<UISelectEntity> build(String proceso, Map<String, Object> params, List<Columna> formato) {
    return build(proceso, Constantes.DML_SELECT, params, formato);
  } 
    
  public static List<UISelectEntity> build(String proceso, Map<String, Object> params, String fields, List<Columna> formato) {
    return build(proceso, Constantes.DML_SELECT, params, formato);
  } 

  public static List<UISelectEntity> todos(List<? extends IBaseDto> dtos, List<Columna> formato, String name) {
		List<UISelectEntity> regresar= new ArrayList<>();
		Entity todos = new Entity();
		IBaseDto primer = null;
    if (dtos!= null && dtos.size()> 0) {
			primer  = dtos.get(0);
				for(String field :((Entity)primer).toMap().keySet()) {
					if (field.equals(name))
					  todos.put(name, new Value(name, "TODOS"));
					else 
						todos.put(field, new Value(field,((Value)(((Entity)primer).toMap()).get(field)).getData() instanceof String ? "": -1L));
				} // for
			regresar.add(new UISelectEntity(todos));
			regresar.addAll(build(dtos,formato));				
 
    } // if
    return regresar;        
  } 
  public static List<UISelectEntity> seleccione(List<? extends IBaseDto> dtos, List<Columna> formato, String name) {
		List<UISelectEntity> regresar= new ArrayList<>();
		Entity todos = new Entity();
		IBaseDto primer = null;
    if (dtos!= null && dtos.size()>0)  {
			primer  = dtos.get(0);
				for(String field :((Entity)primer).toMap().keySet()) {
					if (field.equals(name))
					  todos.put(name, new Value(name, "SELECCIONE"));
					else 
						todos.put(field, new Value(field,((Value)(((Entity)primer).toMap()).get(field)).getData() instanceof String ? "":-1L) );
				} // for
			regresar.add(new UISelectEntity(todos));
			regresar.addAll(build(dtos,formato));				
 
    } // if
    return regresar;        
  } 

  public static List<UISelectEntity> personalizado(List<? extends IBaseDto> dtos, List<Columna> formato, String label, String descripcion) {
    List<UISelectEntity> regresar= new ArrayList<>();
		Entity todos = new Entity();
		IBaseDto primer = null;
    if (dtos!= null && dtos.size()>0)  {
			primer  = dtos.get(0);
				for(String field :((Entity)primer).toMap().keySet()) {
					if (field.equals(label))
					  todos.put(label, new Value(label, descripcion));
					else 
						todos.put(field, new Value(field,((Value)(((Entity)primer).toMap()).get(field)).getData() instanceof String ? "": -1L));
				} // for
			regresar.add(new UISelectEntity(todos));
			regresar.addAll(build(dtos,formato));				
 
    } // if
    return regresar;
  }
	
  public static List<UISelectEntity> todos(List<? extends IBaseDto> dtos, String name) {
    return todos(dtos, Collections.EMPTY_LIST,  name);
  }
  
  public static List<UISelectEntity> seleccione(List<? extends IBaseDto> dtos, String name) {
    return todos(dtos, Collections.EMPTY_LIST,  name);
  }
	
  public static List<UISelectEntity> todos(Class dto, Long records, String name) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos          = null;
    try {
      dtos    = DaoFactory.getInstance().findAll(dto, records);   
      regresar= todos(dtos, Collections.EMPTY_LIST, name);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
	
  public static List<UISelectEntity> seleccione(Class dto, Long records, String name) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos          = null;
    try {
      dtos    = DaoFactory.getInstance().findAll(dto, records);   
      regresar= seleccione(dtos, Collections.EMPTY_LIST, name);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
	
	public static List<UISelectEntity> todos(String proceso, String id, Map params, List<Columna> formato, Long records, String name) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos          = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar= todos(dtos, formato,name);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
	
	public static List<UISelectEntity> seleccione(String proceso, String id, Map params, List<Columna> formato, Long records, String name) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos          = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar= seleccione(dtos, formato,name);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
	
	public static List<UISelectEntity> todos(String proceso, String id, Map params, List<Columna> formato, String name) {
		return todos(proceso, id, params, formato, Constantes.SQL_MAXIMO_REGISTROS,name);
	}
	
	public static List<UISelectEntity> seleccione(String proceso, String id, Map params, List<Columna> formato, String name) {
		return seleccione(proceso, id, params, formato, Constantes.SQL_MAXIMO_REGISTROS,name);
	}
	
  public static List<UISelectEntity> todos(String proceso, String id, Map<String, Object> params, String name) {
		return todos(proceso, id, params, Collections.EMPTY_LIST, name);
	}
	
  public static List<UISelectEntity> seleccione(String proceso, String id, Map<String, Object> params, String name) {
		return seleccione(proceso, id, params, Collections.EMPTY_LIST, name);
	}
	
  public static List<UISelectEntity> todos(String proceso, String id, String name) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return todos(proceso, id, params, name);
  } 
  
  public static List<UISelectEntity> seleccione(String proceso, String id, String name) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return seleccione(proceso, id, params, name);
  } 
  
  public static List<UISelectEntity> todos(String proceso, Map<String, Object> params, String name) {
    return todos(proceso, Constantes.DML_SELECT, params, name);
  } 
    
  public static List<UISelectEntity> seleccione(String proceso, Map<String, Object> params, String name) {
    return seleccione(proceso, Constantes.DML_SELECT, params, name);
  } 
    
  public static List<UISelectEntity> todos(String proceso, Map<String, Object> params, List<Columna> formato, String name) {
    return todos(proceso, Constantes.DML_SELECT, params, formato, name);
  } 
    
  public static List<UISelectEntity> seleccione(String proceso, Map<String, Object> params, List<Columna> formato, String name) {
    return seleccione(proceso, Constantes.DML_SELECT, params, formato, name);
  } 
    
  public static List<UISelectEntity> todos(String proceso, Map<String, Object> params, String fields, List<Columna> formato, String name) {
    return todos(proceso, Constantes.DML_SELECT, params, formato, name);
  } 
	
  public static List<UISelectEntity> seleccione(String proceso, Map<String, Object> params, String fields, List<Columna> formato, String name) {
    return seleccione(proceso, Constantes.DML_SELECT, params, formato, name);
  } 
	public static List<UISelectEntity> personalizado(List<? extends IBaseDto> dtos, String label, String descripcion) {
    return personalizado(dtos, Collections.EMPTY_LIST, label, descripcion);
  }
  
	public static List<UISelectEntity> personalizado(String proceso, String id, Map params, Map<Object, String> labels, String name) {
		return personalizado(proceso, id, params, Collections.EMPTY_LIST, labels, name);
	}
	
	public static List<UISelectEntity> personalizado(String proceso, String id, Map params, List<Columna> formato, Map<Object, String> labels, String name) {
		return personalizado(proceso, id, params, formato, Constantes.SQL_MAXIMO_REGISTROS, labels, name);
	}
	
  public static List<UISelectEntity> personalizado(Class dto, Long records, String label, String descripcion) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos          = null;
    try {
      dtos    = DaoFactory.getInstance().findAll(dto, records);   
      regresar= personalizado(dtos, Collections.EMPTY_LIST, label, descripcion);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
	
  public static List<UISelectEntity> personalizado(Class dto, String label, String descripcion) {
		return personalizado(dto, Constantes.SQL_MAXIMO_REGISTROS, label, descripcion);
	}

  public static List<UISelectEntity> personalizado(Class dto, Map<String, Object> params, List<Columna> formato, Long records, String label, String descripcion) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos      = null;
    try {
      dtos    = DaoFactory.getInstance().findViewCriteria(dto, params, records);   
      regresar= personalizado(dtos, formato, label, descripcion);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
      
  public static List<UISelectEntity> personalizado(Class dto, Map<String, Object> params, List<Columna> formato, String label, String descripcion) {
		return personalizado(dto, params, formato, Constantes.SQL_MAXIMO_REGISTROS, label, descripcion);
	}
	
  public static List<UISelectEntity> personalizado(Class dto, Map<String, Object> params, String label, String descripcion) {
    return personalizado(dto, params, Collections.EMPTY_LIST, label, descripcion);
  }
  
  public static List<UISelectEntity> personalizado(String proceso, String id, Map<String, Object> params, List<Columna> formato, Long records, String label, String descripcion) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos          = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar= personalizado(dtos, formato, label, descripcion);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
  
  public static List<UISelectEntity> personalizado(String proceso, String id, Map<String, Object> params, List<Columna> formato, String label, String descripcion) {
		return personalizado(proceso, id, params, formato, Constantes.SQL_MAXIMO_REGISTROS, label, descripcion);
	}
	
  public static List<UISelectEntity> personalizado(String proceso, String id, Map<String, Object> params, String label, String descripcion) {
		return personalizado(proceso, id, params, Collections.EMPTY_LIST, label, descripcion);
	}
  
  public static List<UISelectEntity> personalizado(String proceso, String id, String label, String descripcion) {
		Map<String, Object> params= new HashMap<>();
		params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    return personalizado(proceso, id, params, label, descripcion);
  } 
  
  public static List<UISelectEntity> personalizado(String proceso, Map<String, Object> params, String label,String descripcion) {
    return personalizado(proceso, Constantes.DML_SELECT, params, label, descripcion);
  } 
    
  public static List<UISelectEntity> personalizado(String proceso, Map<String, Object> params, List<Columna> formato, String label, String descripcion) {
    return personalizado(proceso, Constantes.DML_SELECT, params, formato, label, descripcion);
  } 
    
  public static List<UISelectEntity> personalizado(String proceso, Map<String, Object> params, String fields, List<Columna> formato, String label, String descripcion) {
    return personalizado(proceso, Constantes.DML_SELECT, params, formato, label, descripcion);
  } 
	
	public static List<UISelectEntity> personalizado(String proceso, String id, Map params, List<Columna> formato, Long records, Map<Object, String> labels, String descripcion) {
    List<UISelectEntity> regresar= null;
    List<IBaseDto> dtos        = null;
    try {
      dtos    = DaoFactory.getInstance().toEntitySet(proceso, id, params, records);   
      regresar= personalizado(dtos, formato, labels, descripcion);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch 
    finally {
      Methods.clean(dtos);
    } // finally  
    return regresar;
  }
  
	public static List<UISelectEntity> personalizado(List<? extends IBaseDto> dtos, List<Columna> formato, Map<Object, String> labels, String descripcion) {
    List<UISelectEntity> regresar= new ArrayList<>();
		Entity todos = null;
		IBaseDto primer = null;
    if (dtos!= null && dtos.size()>0)  {
			primer  = dtos.get(0);
			for(Map.Entry<Object, String> entry : labels.entrySet()) {
				todos= new Entity();
				for(String field :((Entity)primer).toMap().keySet()) {									
					if (field.equals(descripcion)) {
						todos.put(descripcion, new Value(descripcion, entry.getValue()));
            todos.put("idKey", new Value("idKey", -1L));
					} // if
				} // map
				regresar.add(new UISelectEntity(todos));
			} // map		
			regresar.addAll(build(dtos,formato));				 
    } // if
    else{
      for(Map.Entry<Object, String> entry : labels.entrySet()) {
        todos= new Entity();
        todos.put(descripcion, new Value(descripcion, entry.getValue()));
        todos.put("idKey", new Value("idKey", entry.getKey()));
        regresar.add(new UISelectEntity(todos));
      } // for
    } // else
    return regresar;
  }
	
  public static UISelectEntity buildTodosItem(String name) {
    Entity entity= new Entity();
    entity.put("idKey", new Value("idKey", -1L));
		entity.put(name, new Value(name, "TODOS"));
		return new UISelectEntity(entity);
  } 
	
}