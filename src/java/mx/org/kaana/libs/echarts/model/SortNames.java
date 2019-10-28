package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 28/10/2019
 *@time 04:19:09 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class SortNames implements Serializable {

	private static final long serialVersionUID=-3437709081656808092L;
	
	private static final String[] NAMES_MONTHS= {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
	private static final String[] NAMES_WEEKS = {"DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB"};
	private static final String[] NAMES_STATES= {"AGS", "BC", "BCN", "BCS", "CAM", "COA", "COL", "CHA", "CHI", "DF", "CDMX", "DGO", "GTO", "GRO", "HGO", "JAL", "MEX", "MIC", "MOR", "NAY", "NL", "NVL", "OAX", "PUE", "QRO", "ROO", "SLP", "SIN", "SON", "TAB", "TAM", "TLA", "VER", "YUC", "ZAC"};

	public static List<String> toNamesMonths(List<String> names) {
		List<String> regresar= new ArrayList<>();
		for (int x=0; x<names.size(); x++) 
			names.set(x, names.get(x).toUpperCase());
		for (String item: NAMES_MONTHS) {
			if(names.contains(item))
				regresar.add(item);
		} // for
		return regresar;
	}
	
	public static List<String> toNamesWeeks(List<String> names) {
		List<String> regresar= new ArrayList<>();
		for (int x=0; x<names.size(); x++) 
			names.set(x, names.get(x).toUpperCase());
		for (String item: NAMES_WEEKS) {
			if(names.contains(item))
				regresar.add(item);
		} // for
		return regresar;
	}
	
	public static List<String> toNamesStates(List<String> names) {
		List<String> regresar= new ArrayList<>();
		for (int x=0; x<names.size(); x++) 
			names.set(x, names.get(x).toUpperCase());
		for (String item: NAMES_STATES) {
			if(names.contains(item))
				regresar.add(item);
		} // for
		return regresar;
	}

}
