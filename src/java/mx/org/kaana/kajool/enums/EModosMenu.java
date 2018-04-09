package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EModosMenu {
        
  JSON_LINEAL      (1L),
  JSON_ESTRUCTURADO(2L),
  HTML             (3L);
  
  private Long value;

  private EModosMenu(Long value) {
    this.value = value;
  }

  public Long getValue() {
    return value;
  }
  
}
