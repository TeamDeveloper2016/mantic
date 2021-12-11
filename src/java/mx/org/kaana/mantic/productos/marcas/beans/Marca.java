package mx.org.kaana.mantic.productos.marcas.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticProductosMarcasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 6/12/2021
 *@time 01:48:18 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Marca extends TcManticProductosMarcasDto implements Serializable {

  private static final long serialVersionUID = 1589024467211077735L;
  private Importado importado;

  public Importado getImportado() {
    return importado;
  }

  public void setImportado(Importado importado) {
    this.importado = importado;
  }
  
  @Override
  public Class toHbmClass() {
    return TcManticProductosMarcasDto.class;
  }

}
