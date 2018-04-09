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

public enum EProyectos {

  ;

	private final Long idProyecto;

  private EProyectos() {
    this.idProyecto= toIdProyecto();
  }

  private Long toIdProyecto() {
		Long id= -1L;
		Value proyecto;
		try {
			proyecto = DaoFactory.getInstance().toField("TcJanalProyectosDto", Variables.toMap(Constantes.SQL_CONDICION.concat("~descripcion='").concat(name().toLowerCase()).concat("'")), "idKey");
			id = proyecto != null ?proyecto.toLong():-1L;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		return id;
	}

  public Long getIdProyecto() {
    return idProyecto;
  }
}
