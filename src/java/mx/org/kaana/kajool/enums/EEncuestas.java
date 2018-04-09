package mx.org.kaana.kajool.enums;

import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 28, 2012
 *@time 9:55:19 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EEncuestas {

  JANAL;

	private final Long idEncuesta;

  private EEncuestas() {
    this.idEncuesta= toIdEncuesta();
  } // EEncuestas

  private Long toIdEncuesta() {
		Long id= -1L;
		Value encuesta;
		try {
			encuesta= DaoFactory.getInstance().toField("TcEncuestasDto", Variables.toMap(Constantes.SQL_CONDICION.concat("~nombre='").concat(name().toLowerCase()).concat("'")), "idKey");
			id= encuesta != null ?encuesta.toLong():-1L;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		return id;
	} // toIdEncuesta

  public Long getIdEncuesta() {
    return idEncuesta;
  } // getIdEncuesta
}
