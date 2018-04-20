package mx.org.kaana.mantic.enums;


/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 30/05/2014
 * @time 03:29:43 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ETipoPersona {

  SISTEMAS(1L),
  USUARIO(2L),
  AGENTE_VENTAS(3L),
  RESPONSABLE(4L),
  REPRESENTANTE_LEGAL(5L),
  EMPLEADO(6L);

  private Long idTipoPersona;

  private ETipoPersona(Long idTipoPersona) {
    this.idTipoPersona = idTipoPersona;
  }

  public Long getIdTipoPersona() {
    return idTipoPersona;
  }

}
