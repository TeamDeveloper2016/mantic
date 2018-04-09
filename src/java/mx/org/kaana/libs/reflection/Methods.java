package mx.org.kaana.libs.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Global;

public final class Methods {

  private Methods() {
  }
	
  public static void executeSet(Object component, String name, Class[] params, Object[] values) {
    try {
      if(name!= null && !Cadena.isVacio(name)) {
        Method method = component.getClass().getDeclaredMethod(name, params);
        if (method!= null)
          method.invoke(component, values);
        else
            throw new RuntimeException("El nombre de atributo [".concat(name).concat("] no existe."));
      } // if
    } // try-catch
    catch (Exception e) {
      throw new RuntimeException("El nombre de atributo [".concat(name).concat("] no existe."));
    } // try
  }

  public static void executeSetSubClass(Object component, String name, Class[] params, Object[] values) {
    try {
      if(name!= null && !Cadena.isVacio(name)) {
        Method method = component.getClass().getSuperclass().getDeclaredMethod(name, params);
        if (method!= null)
          method.invoke(component, values);
        else
            throw new RuntimeException("El nombre de atributo [".concat(name).concat("] no existe."));
      } // if
    } // try-catch
    catch (Exception e) {
      throw new RuntimeException("El nombre de atributo [".concat(name).concat("] no existe."));
    } // try
  }
	
	public static Object getValueSubClass(Object component, String name) {
    Object regresar = null;
    if(name!= null && name.trim().length()> 0) {
      name= "get".concat(Cadena.toNameBean(name));
      regresar= executeGetSubClass(component, name);
    } // if
    return regresar;
  }
	
	public static Object executeGetSubClass(Object component, String name) {
    Object regresar = null;
    try {
      if(name!= null && !Cadena.isVacio(name)) {
        Method method = component.getClass().getSuperclass().getDeclaredMethod(Cadena.getNameMethod(name), new Class[]{});
        if (method != null)
          regresar = method.invoke(component, new Object[]{});
        else
         throw new RuntimeException("El nombre de atributo [".concat(name).concat("] no existe."));
      } // if
    } // try-catch
    catch (Exception e) {
      throw new RuntimeException("El nombre de atributo [".concat(name).concat("] no existe."));
    } // try
    return regresar;
  }

  public static Object executeGet(Object component, String name) {
    Object regresar = null;
    try {
      if(name!= null && !Cadena.isVacio(name)) {
        Method method = component.getClass().getDeclaredMethod(Cadena.getNameMethod(name), new Class[]{});
        if (method != null)
          regresar = method.invoke(component, new Object[]{});
        else
         throw new RuntimeException("El nombre de atributo [".concat(name).concat("] no existe."));
      } // if
    } // try-catch
    catch (Exception e) {
      throw new RuntimeException("El nombre de atributo [".concat(name).concat("] no existe."));
    } // try
    return regresar;
  }


  public static Object getValue(Object component, String name) {
    Object regresar = null;
    if(name!= null && name.trim().length()> 0) {
      name= "get".concat(Cadena.toNameBean(name));
      regresar= executeGet(component, name);
    } // if
    return regresar;
  }

  public static Object execute(Object component, String name, Class[] params, Object[] values) {
    Object regresar= null;
    try {
      Method method = component.getClass().getDeclaredMethod(name, params);
      if (method != null) {
        regresar= method.invoke(component, values);
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static void setValue(Object component, String name, Class[] params, Object[] values) {
    if(name!= null && name.trim().length()> 0) {
      name= "set".concat(Cadena.toNameBean(name));
      executeSet(component, name, params, values);
    } // if
  }

  public static void setValue(Object component, String name, Object[] values) throws NoSuchFieldException {
    if(name!= null && name.trim().length()> 0) {
      Field field= component.getClass().getDeclaredField(name);
      name= "set".concat(Cadena.toNameBean(name));
      executeSet(component, name, new Class[] {field.getType()}, values);
    } // if
  }

  public static void setValueSubClass(Object component, String name, Object[] values) throws NoSuchFieldException {
    if(name!= null && name.trim().length()> 0) {
      Field field= component.getClass().getSuperclass().getDeclaredField(name);
      name= "set".concat(Cadena.toNameBean(name));
      executeSetSubClass(component, name, new Class[] {field.getType()}, values);
    } // if
  }

  public static Class getTypeField(Object component, String name) {
    Class regresar = null;
    try {
      if(name!= null && name.trim().length()> 0) {
        //name= "get".concat(Cadena.toNameBean(name));
        Field [] fields = component.getClass().getDeclaredFields();
        for(Field atributo : fields) {
          if(atributo.getName().equals(name))  {
            regresar = atributo.getType();
          }
        }
      }
    }
    catch(Exception e) {
      Error.mensaje(e);
    }
    return regresar;
  }

  public static Object getObjectMatch(Class clase, String value) {
    if (clase.getSimpleName().equals("Long"))
      return new Long(value);
    else
      return value;
  }

  private static void searchInterface(Object component, String name, Class[] params, Object[] values, Exception e_) {
    boolean finded = false;
    try {
      Class[] interfaces = component.getClass().getInterfaces();
      for (Class base: interfaces) {
        try {
          Method method = base.getDeclaredMethod(name, params);
          finded=method != null;
          if (finded) {
            Object object = method.invoke(component, values);
          } // if
        } // try
        catch (Exception _e) {
          Error.mensaje(_e);
          _e=null;
        } // try
      } // for
      if (!finded)
        throw new Exception("No se encontrol el metodo en las interfaces. !");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public static String getValues(Object component, List<String> names, String token) {
    StringBuilder sb= new StringBuilder();
    for(String field: names) {
      Object value= getValue(component, field);
      sb.append(value!= null? value.toString(): "");
      sb.append(token);
    } // for
    return sb.substring(0, sb.length()- token.length());
  }

  public static String getValues(Object component, List<String> names) {
    return getValues(component, names, " ");
  }

  public static String toMessage(IBaseDto item, String name, char separator) {
    StringBuilder sb= new StringBuilder();
    StringTokenizer st= new StringTokenizer(name, Constantes.SEPARADOR);
    while (st.hasMoreTokens()) {
      String token= st.nextToken();
      sb.append(Cadena.toEmpty(Methods.getValue(item, token)));
      sb.append(separator);
    } // while
    if(sb.length()> 0)
      sb.deleteCharAt(sb.length()- 1);
    return sb.toString();
  }

  public static String toMessage(IBaseDto item, String name) {
    return toMessage(item, name, ' ');
  }

  public static String recortar(String value) {
    if(value.length()>=70) {
      value= value.substring(0, 70).concat("...");
    }
    return value;
  }

  public static String toHiddens(IBaseDto item, String nameFields, char token) {
    StringBuilder sb= new StringBuilder();
    String[] fields= nameFields.split("["+ token+ "]");
    for (String field: fields) {
      String value= Global.format(EFormatoDinamicos.LIBRE, Methods.getValue(item, field));
      if(value!= null && !value.equalsIgnoreCase("null")) {
        sb.append(value);
        sb.append(token);
      } // if
    } // for
    if(sb.length()> 0)
      sb.deleteCharAt(sb.length()- 1);
    return sb.toString();
  } // toFields

  public static String toHiddens(IBaseDto item, String formats) {
    return toHiddens(item, formats, ' ');
  }

  public static String ajustar(String value) {
    return Cadena.letraCapital(recortar(value));
  }

  public static String toExtras(IBaseDto item, String nameFields, char token) {
    StringBuilder sb= new StringBuilder();
    String[] fields= nameFields.split("["+ token+ "]");
    for (String field: fields) {
      if(field.indexOf("~")>= 0)
        sb.append(field.replace('~','='));
      else {
        if(!Cadena.isVacio(field)) {
        String value= Global.format(EFormatoDinamicos.LIBRE, Methods.getValue(item, field.substring(2,field.length())));
        if(value!= null && !value.equalsIgnoreCase("null")) {
            sb.append(field);
            sb.append("=");
            sb.append(value);
          } // if
        } // if
      } // else
      sb.append(token);
    } // for
    if(sb.length()> 0)
      sb.deleteCharAt(sb.length()- 1);
    return sb.toString();
  } // toFields

  public static String toExtras(IBaseDto item, String formats) {
    return toExtras(item, formats, ' ');
  }

  public static String toExtras(String nameFields, char token) {
    StringBuilder sb= new StringBuilder();
    if(nameFields!= null && nameFields.length()> 0) {
      String[] fields= nameFields.split("["+ token+ "]");
      for (String field: fields) {
        if(field.indexOf("~")>= 0)
          sb.append(field.replace('~','='));
        sb.append(token);
      } // for
      if(sb.length()> 0)
        sb.deleteCharAt(sb.length()- 1);
    } // if
    return sb.toString();
  } // toFields

  public static String toExtras(String formats) {
    return toExtras(formats, ' ');
  }

  public static Object clean(Object instancia) {
    try {
      if (instancia!= null) {
        if (instancia instanceof AbstractMap)
          ((AbstractMap)instancia).clear();
        else
          if (instancia instanceof Collection)
            ((Collection)instancia).clear();
          else
            if (instancia instanceof AbstractCollection)
              ((AbstractCollection)instancia).clear();
      } // if
    } // try
    catch ( Exception e) {
      Error.mensaje(e);
    } // catch
    return instancia;
  }

  public static Class<?>[] getMethodParameters(Object component, String name) {
  	Class<?>[] parametros = null;
  	Method[] metodos = component.getClass().getDeclaredMethods();
  	for(Method metodo : metodos) {
  		if(metodo.getName().equals(name)) {
  			parametros = metodo.getParameterTypes();
  			break;
  		} // if
  	} // for-each
  	
  	return parametros;
  }
	
	public static String toNameTable (Class<? extends IBaseDto> dto) {
	  String regresar = "";
		boolean table   = false;
		try {		
  	  for (Annotation annotacion : dto.getDeclaredAnnotations() ){
		    if(annotacion instanceof Table) {
				  table= true;						
				}
				if(table && annotacion instanceof Table) {
				  regresar= ((Table)annotacion).name();
				  break;
				} // if			
				if(!Cadena.isVacio(regresar))
					break;
			} // for
		} // try
		catch (Exception e) {
		  Error.mensaje(e);
		} // catch
		return regresar;
	}
	
	public static String toKeyName (Class<? extends IBaseDto> dto, boolean nombreBean) {
	  String regresar= null;
		boolean id     = false;
		try {
			for (Field field: dto.getDeclaredFields()) {
				for (Annotation anotacion: field.getDeclaredAnnotations()) {
					if(anotacion instanceof Id) {
						id= true;						
					}
					if(id && anotacion instanceof Column) {
						regresar= ((Column)anotacion).name();
						break;
					} // if
				} // for
				if(!Cadena.isVacio(regresar))
					break;
			} // for	
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
		return Cadena.isVacio(regresar)? "": nombreBean?Cadena.toBeanName(regresar):regresar;		
	}

	public static String toKeyName(Class<? extends IBaseDto> dto) {
		return toKeyName(dto, true);
	}	
}
