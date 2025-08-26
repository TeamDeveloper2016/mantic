package mx.org.kaana.mantic.contadores.beans;


import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticContadoresDetallesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/08/2025
 *@time 11:49:27 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Producto extends TcManticContadoresDetallesDto implements Serializable {

  private static final long serialVersionUID = -6519742635547643149L;

	private UISelectEntity ikArticulo;
  private ESql sql;

  public Producto() {
    super(new Long((int)(Math.random()* -10000)));
    this.sql= ESql.INSERT;
  }

  public UISelectEntity getIkArticulo() {
    return ikArticulo;
  }

  public void setIkArticulo(UISelectEntity ikArticulo) {
    this.ikArticulo = ikArticulo;
    if(!Objects.equals(ikArticulo, null))
      this.setIdArticulo(ikArticulo.getKey());
  }

  public ESql getSql() {
    return sql;
  }

  public void setSql(ESql sql) {
    this.sql = sql;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + Objects.hashCode(this.getIdArticulo());
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) 
      return true;
    if (obj == null) 
      return false;
    if (getClass() != obj.getClass()) 
      return false;
    final Producto other = (Producto) obj;
    if (!Objects.equals(this.getIdArticulo(), other.getIdArticulo())) 
      return false;
    return true;
  }
  
  @Override
  public Class toHbmClass() {
    return TcManticContadoresDetallesDto.class;
  }
  
}
