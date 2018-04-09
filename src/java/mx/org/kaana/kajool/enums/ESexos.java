package mx.org.kaana.kajool.enums;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/09/2015
 *@time 01:46:52 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum ESexos {
  HOMBRE("HOMBRE", "H", 1L),
  MUJER ("MUJER" , "M", 2L);

  private String clave;
  private String abreviatura;
  private Long idSexo;

  private ESexos(String clave, String abreviatura, Long idSexo) {
    this.clave      = clave;
    this.abreviatura= abreviatura;
    this.idSexo     = idSexo;
  }

  public String getClave() {
    return clave;
  }

  public String getAbreviatura() {
    return abreviatura;
  }

  public Long getIdSexo(){
    return idSexo;
  }

}
