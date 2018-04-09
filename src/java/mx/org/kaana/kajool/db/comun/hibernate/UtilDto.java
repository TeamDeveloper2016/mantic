package mx.org.kaana.kajool.db.comun.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;

public final class UtilDto {

	private UtilDto() {
	}

	public static String toString(Object objeto) {
		return toString(objeto, true);
	} // toString

	public static String toString(Object objeto, boolean caracterInicial) {
		StringBuilder sb=new StringBuilder();
		if (caracterInicial) {
			sb.append("[");
		}
		Field fields[]=objeto.getClass().getDeclaredFields();
		Method method=null;
		for (int x=0; x<fields.length; x++) {
			if (!fields[x].getName().equals("serialVersionUID")) {
				String nameMethod="get"+fields[x].getName().toUpperCase().charAt(0)+fields[x].getName().substring(1);
				try {
					method=objeto.getClass().getDeclaredMethod(nameMethod, new Class[]{});
					if (method!=null) {
						sb.append(getFormatType(method.invoke(objeto, new Object[]{})));
						sb.append(Constantes.SEPARADOR);
					} // if
				}
				catch (Exception e) {
					Error.mensaje(e);
				} // try
				//} // if
			} // for
		}
		if (caracterInicial) {
			sb.replace(sb.length()-1, sb.length(), "]");
		}
		else {
			sb.replace(sb.length()-1, sb.length(), "");
		}
		return sb.toString();
	} // toString

	public static Map toMap(Object objeto) {
		Map regresar=new HashMap();
		Field fields[]=objeto.getClass().getDeclaredFields();
		Method method=null;
		for (int x=0; x<fields.length; x++) {
			if (!fields[x].getName().equals("serialVersionUID")) {
				String nameMethod="get"+fields[x].getName().toUpperCase().charAt(0)+fields[x].getName().substring(1);
				try {
					method=objeto.getClass().getDeclaredMethod(nameMethod, new Class[]{});
					if (method!=null) {
						regresar.put(fields[x].getName(), method.invoke(objeto, new Object[]{}));
					} // if
				}
				catch (Exception e) {
					Error.mensaje(e);
				} // try
			} // if
		} // for
		return regresar;
	} // toMap

	public static Object[] toArray(Object objeto) {
		Field fields[]=objeto.getClass().getDeclaredFields();
		Object[] regresar=new Object[fields.length];
		Method method=null;
		for (int x=0; x<fields.length; x++) {
			if (!fields[x].getName().equals("serialVersionUID")) {
				String nameMethod="get"+fields[x].getName().toUpperCase().charAt(0)+fields[x].getName().substring(1);
				try {
					method=
						objeto.getClass().getDeclaredMethod(nameMethod, new Class[]{});
					if (method!=null) {
						regresar[x]=method.invoke(objeto, new Object[]{});
					} // if
				}
				catch (Exception e) {
					Error.mensaje(e);
				} // try
			} // if
		} // for
		return regresar;
	} // toArray

	public static String getFormatType(Object object) {
		String regresar="NULL";
		if (object!=null) {
			if (object instanceof Timestamp) {
				regresar=Fecha.getFechaHoraBD((java.util.Date) object);
			}
			else if (object instanceof Time) {
				regresar=Fecha.getHoraBD((java.util.Date) object);
			}
			else if (object instanceof Date) {
				regresar=Fecha.getFechaBD((java.util.Date) object);
			}
			else {
				regresar=object.toString();
			}
		} // if
		return regresar;
	}
}
