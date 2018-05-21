package mx.org.kaana.mantic.enums;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 19/05/2018
 * @time 08:43:43 PM
 * @author One Developer 2016 <one.developer@kaana.org.mx>
 */

public enum ETipoVenta {

  MENUDEO      (1L),
  MEDIO_MAYOREO(2L),
	MAYOREO      (3L);

  private Long idTipoVenta;

  private ETipoVenta(Long idTipoVenta) {
    this.idTipoVenta = idTipoVenta;
  }

  public Long getIdTipoVenta() {
    return idTipoVenta;
  }
}