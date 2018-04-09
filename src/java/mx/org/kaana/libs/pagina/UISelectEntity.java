package mx.org.kaana.libs.pagina;

import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.kajool.db.comun.sql.Entity;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 07-mar-2014
 *@time 19:38:53
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class UISelectEntity extends Entity {

  private static final long serialVersionUID = 7413388339609153092L;

  private Long key;

  public UISelectEntity(String key) {
    this.key = Numero.getLong(key, -1L);
  }

  public UISelectEntity(Entity entity) {
    this.putAll(entity);
    this.key= super.getKey();
  }

  @Override
  public Long getKey() {
    return this.key;
  }

  @Override
  public void setKey(Long key) {
    this.key= key;
  }

  @Override
  public String toString() {
    return this.key.toString();
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + (this.key != null ? this.key.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final UISelectEntity other = (UISelectEntity) obj;
    if (this.key != other.key && (this.key == null || !this.key.equals(other.key)))
      return false;
    return true;
  }

}
