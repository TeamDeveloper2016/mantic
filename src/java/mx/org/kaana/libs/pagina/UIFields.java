package mx.org.kaana.libs.pagina;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;

import org.hibernate.Session;

public final class UIFields {

  private UIFields() {
  }

  public static Object toField(Class dto, Long key, String field) {
    Object regresar= null;
    try {
      if(key!= null && field!= null) {
        IBaseDto bean= DaoFactory.getInstance().findById(dto, key);
        if(bean!= null)
          regresar  = Methods.getValue(bean, field);
        bean        = null;
      } // if
    } // try
    catch (Exception e)  {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static Object toField(Class dto, Map params, String field) {
    Object regresar= null;
    try {
      if(params!= null && field!= null) {
        IBaseDto bean= DaoFactory.getInstance().findFirst(dto, params);
        if(bean!= null)
          regresar  = Methods.getValue(bean, field);
        bean        = null;
      } // if
    } // try
    catch (Exception e)  {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static List toFields(Class dto, Map params, List<String> fields) {
    List regresar= new ArrayList();
    try {
      if(params!= null && fields!= null) {
        IBaseDto bean= DaoFactory.getInstance().findFirst(dto, params);
        if(bean!= null)
          for(String field: fields) {
            regresar.add(Methods.getValue(bean, field));
          } // for
        bean= null;
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static List toFields(Class dto, Map params, String fields) {
    return toFields(dto, params, Cadena.toList(fields));
  }

  public static List toFields(Class dto, Map params) {
    return toFields(dto, params, UIBackingUtilities.toFieldsList(dto));
  }

  public static List toFields(Class dto, Long key, List<String> fields) {
    List regresar= new ArrayList();
    try {
      if(key!= null && fields!= null) {
        IBaseDto bean= DaoFactory.getInstance().findById(dto, key);
        if(bean!= null)
          for(String field: fields) {
            regresar.add(Methods.getValue(bean, field));
          } // for
        bean= null;
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static List toFields(Class dto, Long key, String fields) {
    return toFields(dto, key, Cadena.toList(fields));
  }

  public static List toFields(Class dto, Long key) {
    return toFields(dto, key, UIBackingUtilities.toFieldsList(dto));
  }

  public static String toStringFields(Class dto, Map params, List<String> fields, String token) {
    StringBuffer regresar= new StringBuffer();
    try {
      if(params!= null && fields!= null) {
        IBaseDto bean= DaoFactory.getInstance().findFirst(dto, params);
        if(bean!= null)
          for(String field: fields) {
            Object value= Methods.getValue(bean, field);
            regresar.append(value!= null? value.toString(): "");
            regresar.append(token);
          } // for
        if(regresar.length()> 0)
          regresar.delete(regresar.length()- token.length()- 1, regresar.length());
        bean= null;
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar.toString();
  }

  public static String toStringFields(Class dto, Map params, String fields, String token) {
    return toStringFields(dto, params, Cadena.toList(fields), token);
  }

  public static String toStringFields(Class dto, Map params, String fields) {
    return toStringFields(dto, params, Cadena.toList(fields), " ");
  }

  public static String toStringFields(Class dto, Map params) {
    return toStringFields(dto, params, UIBackingUtilities.toFieldsList(dto), " ");
  }

  public static String toStringFields(Class dto, Long key, List<String> fields, String token) {
    StringBuffer regresar= new StringBuffer();
    try {
      if(key!= null && fields!= null) {
        IBaseDto bean= DaoFactory.getInstance().findById(dto, key);
        if(bean!= null)
          for(String field: fields) {
            Object value= Methods.getValue(bean, field);
            regresar.append(value!= null? value.toString(): "");
            regresar.append(token);
          } // for
        if(regresar.length()> 0)
          regresar.delete(regresar.length()- token.length()- 1, regresar.length());
        bean= null;
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar.toString();
  }

  public static String toStringFields(Class dto, Long key, String fields, String token) {
    return toStringFields(dto, key, Cadena.toList(fields), token);
  }

  public static String toStringFields(Class dto,  Long key, String fields) {
    return toStringFields(dto, key, Cadena.toList(fields), " ");
  }

  public static String toStringFields(Class dto, Long key) {
    return toStringFields(dto, key, UIBackingUtilities.toFieldsList(dto), " ");
  }


  public static Object toField(Session session, Class dto, Long key, String field) {
    Object regresar= null;
    try {
      if(key!= null && field!= null) {
        IBaseDto bean= DaoFactory.getInstance().findById(session, dto, key);
        if(bean!= null)
          regresar  = Methods.getValue(bean, field);
        bean        = null;
      } // if
    } // try
    catch (Exception e)  {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static Object toField(Session session, Class dto, Map params, String field) {
    Object regresar= null;
    try {
      if(params!= null && field!= null) {
        IBaseDto bean= DaoFactory.getInstance().findFirst(session, dto, params);
        if(bean!= null)
          regresar  = Methods.getValue(bean, field);
        bean        = null;
      } // if
    } // try
    catch (Exception e)  {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static List toFields(Session session, Class dto, Map params, List<String> fields) {
    List regresar= new ArrayList();
    try {
      if(params!= null && fields!= null) {
        IBaseDto bean= DaoFactory.getInstance().findFirst(session, dto, params);
        if(bean!= null)
          for(String field: fields) {
            regresar.add(Methods.getValue(bean, field));
          } // for
        bean= null;
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static List toFields(Session session, Class dto, Map params, String fields) {
    return toFields(session, dto, params, Cadena.toList(fields));
  }

  public static List toFields(Session session, Class dto, Map params) {
    return toFields(session, dto, params, UIBackingUtilities.toFieldsList(dto));
  }

  public static List toFields(Session session, Class dto, Long key, List<String> fields) {
    List regresar= new ArrayList();
    try {
      if(key!= null && fields!= null) {
        IBaseDto bean= DaoFactory.getInstance().findById(session, dto, key);
        if(bean!= null)
          for(String field: fields) {
            regresar.add(Methods.getValue(bean, field));
          } // for
        bean= null;
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public static List toFields(Session session, Class dto, Long key, String fields) {
    return toFields(session, dto, key, Cadena.toList(fields));
  }

  public static List toFields(Session session, Class dto, Long key) {
    return toFields(session, dto, key, UIBackingUtilities.toFieldsList(dto));
  }

  public static String toStringFields(Session session, Class dto, Map params, List<String> fields, String token) {
    StringBuffer regresar= new StringBuffer();
    try {
      if(params!= null && fields!= null) {
        IBaseDto bean= DaoFactory.getInstance().findFirst(session, dto, params);
        if(bean!= null)
          for(String field: fields) {
            Object value= Methods.getValue(bean, field);
            regresar.append(value!= null? value.toString(): "");
            regresar.append(token);
          } // for
        if(regresar.length()> 0)
          regresar.delete(regresar.length()- token.length()- 1, regresar.length());
        bean= null;
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar.toString();
  }

  public static String toStringFields(Session session, Class dto, Map params, String fields, String token) {
    return toStringFields(session, dto, params, Cadena.toList(fields), token);
  }

  public static String toStringFields(Session session, Class dto, Map params, String fields) {
    return toStringFields(session, dto, params, Cadena.toList(fields), " ");
  }

  public static String toStringFields(Session session, Class dto, Map params) {
    return toStringFields(session, dto, params, UIBackingUtilities.toFieldsList(dto), " ");
  }

  public static String toStringFields(Session session, Class dto, Long key, List<String> fields, String token) {
    StringBuffer regresar= new StringBuffer();
    try {
      if(key!= null && fields!= null) {
        IBaseDto bean= DaoFactory.getInstance().findById(session, dto, key);
        if(bean!= null)
          for(String field: fields) {
            Object value= Methods.getValue(bean, field);
            regresar.append(value!= null? value.toString(): "");
            regresar.append(token);
          } // for
        if(regresar.length()> 0)
          regresar.delete(regresar.length()- token.length()- 1, regresar.length());
        bean= null;
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar.toString();
  }

  public static String toStringFields(Session session, Class dto, Long key, String fields, String token) {
    return toStringFields(session, dto, key, Cadena.toList(fields), token);
  }

  public static String toStringFields(Session session, Class dto,  Long key, String fields) {
    return toStringFields(session, dto, key, Cadena.toList(fields), " ");
  }

  public static String toStringFields(Session session, Class dto, Long key) {
    return toStringFields(session, dto, key, UIBackingUtilities.toFieldsList(dto), " ");
  }

}
