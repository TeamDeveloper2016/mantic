package mx.org.kaana.kajool.procesos.usuarios.reglas.formato;

import java.util.ArrayList;
import java.util.List;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 2/09/2015
 * @time 12:18:53 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Letras extends Rango {

	public Letras(char start, char end) {
		super(start, end);
	}

	@Override
	public List<String> getSerie() {
		List<String> regresar=new ArrayList<String>();
		for (int x=getStart(); x<=getEnd(); x++) {
			regresar.add(String.valueOf((char) x));
		} // for
		return regresar;
	}
}
