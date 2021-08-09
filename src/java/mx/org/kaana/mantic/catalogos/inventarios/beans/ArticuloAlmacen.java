package mx.org.kaana.mantic.catalogos.inventarios.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/08/2021
 *@time 06:18:06 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ArticuloAlmacen extends TcManticAlmacenesArticulosDto implements Serializable {

  private static final long serialVersionUID = 5762232736243735164L;

  private Long idVerificado;

  public ArticuloAlmacen() {
    super();
    this.idVerificado= 2L;
  }
  
  public ArticuloAlmacen(Double minimo, Long idAlmacenArticulo, Long idUsuario, Long idAlmacen, Double maximo, Long idAlmacenUbicacion, Long idArticulo, Double stock, Long idVerificado) {
    super(minimo, idAlmacenArticulo, idUsuario, idAlmacen, maximo, idAlmacenUbicacion, idArticulo, stock);
    this.idVerificado= idVerificado;
  }
  
  public Long getIdVerificado() {
    return idVerificado;
  }

  public void setIdVerificado(Long idVerificado) {
    this.idVerificado = idVerificado;
  }

  @Override
  public Class toHbmClass() {
    return TcManticAlmacenesArticulosDto.class;
  }
  
}
