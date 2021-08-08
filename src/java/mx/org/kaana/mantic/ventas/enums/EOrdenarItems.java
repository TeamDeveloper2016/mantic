package mx.org.kaana.mantic.ventas.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/08/2021
 *@time 11:42:50 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EOrdenarItems {
  MIN_PRICE("tc_mantic_articulos.menudeo asc"), 
  MAX_PRICE("tc_mantic_articulos.menudeo desc"), 
  TEXT_ASC("tc_mantic_articulos.nombre asc"), 
  TEXT_DESC("tc_mantic_articulos.nombre desc"),
  CODE_ASC("tc_mantic_articulos_codigos.codigo asc"), 
  CODE_DESC("tc_mantic_articulos_codigos.codigo desc");
  
  private final String sort;
  
  private EOrdenarItems(String sort) {
    this.sort= sort;
  }

  public String getSort() {
    return "order by ".concat(sort);
  }
  
}
