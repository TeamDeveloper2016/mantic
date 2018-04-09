package mx.org.kaana.kajool.reglas.comun;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jun 11, 2012
 * @time 2:58:36 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Condicion {

	private List<IBaseDto> valores;
	private static final Log LOG = LogFactory.getLog(Condicion.class);

	public Condicion(IBaseDto valor) {
		setValores(toList(valor));
	}

	public Condicion(List<IBaseDto> valores) {
		setValores(valores);
	}

	public void setValores(List<IBaseDto> valores) {
		this.valores = valores;
	}

	public List<IBaseDto> getValores() {
		return valores;
	}

	public String getNotIn(String fieldDB) {
		return getInNot(" not in ", fieldDB, true);
	}

	private String getInNot(String operador, String fieldDB, boolean includeField) {
		String regresar = "";
		String atributo = "";
		if (getValores() != null) {
			for (IBaseDto ibd : getValores()) {
				atributo = getFieldDB(fieldDB);
				atributo = Cadena.toBeanName(atributo);
				regresar = regresar.concat(
						String.valueOf(Methods.getValue(ibd, atributo))).concat(",");
			} // for
			if (!regresar.equals("")) {
				regresar = regresar.substring(0, regresar.length() - 1);
				if (includeField)
					regresar = " ".concat(fieldDB).concat(operador).concat("(")
							.concat(regresar).concat(")");
			} // if
		} // if
		return regresar;
	}

	public String getInField(String fieldDB) {
		return getInNot(" in ", fieldDB, true);
	}

	public String getIn(String fieldDB) {
		return getInNot(null, fieldDB, false);
	}

	private String get(String operador, String fieldDB) {
		String regresar = "";
		String atributo = "";
		if (getValores() != null) {
			for (IBaseDto ibd : getValores()) {
				atributo = getFieldDB(fieldDB);
				atributo = Cadena.toBeanName(atributo);
				regresar = regresar.concat(fieldDB.concat("=")
						.concat(String.valueOf(Methods.getValue(ibd, atributo)))
						.concat(operador));
			} // for
			if (!regresar.equals("")) {
				regresar = regresar.substring(0, regresar.length() - 4);
			} // if
		} // if
		return regresar;
	}

	private String getFieldDB(String fieldDB) {
		String regresar = fieldDB;
		if (fieldDB.indexOf(".") != -1) {
			regresar = fieldDB.substring(fieldDB.indexOf(".") + 1, fieldDB.length());
		} // if
		return regresar;
	}

	public String getAnd(String fieldDB) {
		return get(" and ", fieldDB);
	}

	public String getOr(String fieldDB) {
		return get(" or ", fieldDB);
	}

	private List<IBaseDto> toList(IBaseDto dto) {
		List<IBaseDto> regresar = new ArrayList<IBaseDto>();
		regresar.add(dto);
		return regresar;
	}
	
}
