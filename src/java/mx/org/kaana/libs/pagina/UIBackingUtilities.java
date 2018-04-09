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
import java.util.List;

import org.primefaces.component.datatable.DataTable;

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

  public static Object toFirstKeySelectEntity(List<UISelectEntity> items) {
		Object regresar= -1L;
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
	
}
