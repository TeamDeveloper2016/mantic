package mx.org.kaana.mantic.ventas.enums;

import mx.org.kaana.libs.Constantes;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/08/2021
 *@time 09:56:51 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum ECategorias {

  NINGUNA(Constantes.SQL_VERDADERO), 
  FONTANERIA("upper(tc_mantic_articulos.nombre) like 'F%'"), 
  HERRERIA("upper(tc_mantic_articulos.nombre) like 'H%'"), 
  PISOS("upper(tc_mantic_articulos.nombre) like 'P%'"), 
  JARDINERIA("upper(tc_mantic_articulos.nombre) like 'J%'"), 
  HERRAMIENTAS("upper(tc_mantic_articulos.nombre) like 'R%'"), 
  ELECTRICIDAD("upper(tc_mantic_articulos.nombre) like 'E%'"), 
  LUBRICANTES("upper(tc_mantic_articulos.nombre) like 'L%'"),
  TLAPALERIA("upper(tc_mantic_articulos.nombre) like 'T%'");
  
  private final String text;
  
  private ECategorias(String text) {
    this.text= text;
  }

  public String getText() {
    return text;
  }

}
