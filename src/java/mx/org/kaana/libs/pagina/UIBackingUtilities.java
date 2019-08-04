package mx.org.kaana.libs.pagina;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2015
 *@time 02:43:54 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.application.FacesMessage;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.Global;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

public class UIBackingUtilities {

  protected UIBackingUtilities() {
  }

  public static String toFieldsTokens(Class dto) {
    StringBuilder regresar= new StringBuilder();
    for(Field field: dto.getDeclaredFields()) {
      regresar.append(field.getName());
      regresar.append("|");
    } // for
    return regresar.length()> 0? regresar.toString().substring(0, regresar.length()- 1): "";
  }

  public static List<String> toFieldsList(Class dto) {
    List<String> regresar= new ArrayList<>();
    for(Field field: dto.getDeclaredFields())
      regresar.add(field.getName());
    return regresar;
  }

	public static Object toFirstKeySelectItem(List<UISelectItem> items) {
		Object regresar= -1L;
		if(items!= null && items.size()> 0)
  	  regresar= items.get(0).getValue();
		return regresar;
	} 	

  public static UISelectEntity toFirstKeySelectEntity(List<UISelectEntity> items) {
		UISelectEntity regresar= new UISelectEntity(new Entity(-1L));
		if(items!= null && items.size()> 0)
  	  regresar= items.get(0);
		return regresar;
	} 	

  public static Boolean isEmptyList(List<UISelectItem> list) {
    return list== null || list.size()<= 0;
  }

	public static void resetDataTable(String name) {
    DataTable dataTable= (DataTable)JsfUtilities.findComponent(name);
    if (dataTable!= null)
      dataTable.setFirst(0);		
	}
	
	public static void resetDataTable() {
		resetDataTable("tabla");
	}

  public static List<Entity> toFormatEntitySet(List<Entity> items, List<Columna> columns) {
		items.forEach((item) -> {
			toFormatEntity(item, columns);
		}); // for
		return items;
	} 	

  public static Entity toFormatEntity(Entity entity, List<Columna> columns) {
		if(entity!= null)
			columns.forEach((column) -> { 
				if(entity.containsKey(column.getName())) {
					Value value= new Value(column.getName(), entity.get(column.getName()));          
					value.setData(Global.format(column.getFormat(), value.getData()));
					entity.put(column.getName(), value);          
				} // if
				else
					throw new RuntimeException("No existe la columna "+ column.getName()+ " en la lista de items.");
			});
		return entity;
	}
	
	public static void execute(String script) {
		if(RequestContext.getCurrentInstance()!= null)
      RequestContext.getCurrentInstance().execute(script);
	}
	
	public static void update(String component) {
		RequestContext.getCurrentInstance().update(component);
	}
	
	public static void update(Collection<String> component) {
		RequestContext.getCurrentInstance().update(component);
	}
	
	public static void addCallbackParam(String name, Object params) {
		RequestContext.getCurrentInstance().addCallbackParam(name, params);
	}
	
	public static void showMessageInDialog(FacesMessage message) {
	  RequestContext.getCurrentInstance().showMessageInDialog(message);	
	}
	
	public static RequestContext getCurrentInstance() {
	  return RequestContext.getCurrentInstance();	
	}
	
}
