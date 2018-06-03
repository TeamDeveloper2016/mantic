package mx.org.kaana.mantic.enums;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 30/05/2014
 * @time 03:29:43 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ETipoEmpresa {

  MATRIZ(1L),
  SUCURSAL(2L);

  private Long idTipoEmpresa;

  private ETipoEmpresa(Long idTipoEmpresa) {
    this.idTipoEmpresa = idTipoEmpresa;
  }

  public Long getIdTipoEmpresa() {
    return idTipoEmpresa;
  }
}