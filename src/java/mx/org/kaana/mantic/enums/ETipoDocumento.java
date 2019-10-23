package mx.org.kaana.mantic.enums;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 19/05/2018
 * @time 08:43:43 PM
 * @author One Developer 2016 <one.developer@kaana.org.mx>
 */

public enum ETipoDocumento {

  FACTURAS_FICTICIAS    (1L),
  VENTAS_NORMALES       (2L),
	COTIZACIONES_FICTICIAS(3L);

  private Long idTipoDocumento;

  private ETipoDocumento(Long idTipoDocumento) {
    this.idTipoDocumento = idTipoDocumento;
  }

  public Long getIdTipoDocumento() {
    return idTipoDocumento;
  }
}