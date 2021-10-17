package mx.org.kaana.mantic.productos.beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticProductosCaracteristicasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/10/2021
 *@time 07:27:00 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Caracteristica extends TcManticProductosCaracteristicasDto implements Serializable {

  private static final long serialVersionUID = 245348724560955248L;
  
  private ESql action;
  private ESql anterior;

  public Caracteristica() {
    this(new Random().nextLong(), "caracteristica_1");
  }

  public Caracteristica(String descripcion) {
    this(new Random().nextLong(), descripcion);
  }

  public Caracteristica(Long key, String descripcion) {
    super(key);
    if(Objects.equals(this.getKey(), 0))
      this.setKey(new Random().nextLong());
    if(this.getKey()> 0)
      this.setKey(this.getKey()* -1L);
    this.action  = ESql.INSERT;    
    this.anterior= ESql.INSERT;    
    this.setDescripcion(descripcion);
  }
    
  public ESql getAction() {
    return action;
  }

  public void setAction(ESql action) {
    this.action = action;
  }

  public ESql getAnterior() {
    return anterior;
  }

  public void setAnterior(ESql anterior) {
    this.anterior = anterior;
  }
  
  @Override
  public Class toHbmClass() {
    return TcManticProductosCaracteristicasDto.class;
  }
  
}
