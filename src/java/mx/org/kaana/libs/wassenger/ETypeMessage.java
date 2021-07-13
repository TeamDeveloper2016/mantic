package mx.org.kaana.libs.wassenger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/06/2021
 *@time 02:25:24 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum ETypeMessage {
  
  BIENVENIDA(1L), PROVEEDOR(2L), CLIENTE(3L), CONTRATISTA(4L), RESIDENTE(5L), ADMINISTRADOR(6L);
  
  private final Long id;
  
  private ETypeMessage(Long id) {
    this.id= id;
  }

  public Long getId() {
    return id;
  }

}
