package mx.org.kaana.libs.pagina;

import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.formato.Cadena;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jun 21, 2012
 * @time 10:07:53 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class ListaCombo {

	public static List<UISelectItem> getStaticList(int numElementos) {
		List<UISelectItem> regresar=new ArrayList<>();
		for (long i=1; i<=numElementos; i++) {
			regresar.add(new UISelectItem(i, String.valueOf(i)));
		} //for
		return regresar;
	}

	public static List<UISelectItem> getStaticListString(int numElementos) {
		List<UISelectItem> regresar=new ArrayList<>();
		String n=null;
		for (long i=1; i<= numElementos; i++) {
			n=Cadena.rellenar(Long.toString(i), 2, '0', true);
			regresar.add(new UISelectItem(n, n));
		} //for
		return regresar;
	}
}
