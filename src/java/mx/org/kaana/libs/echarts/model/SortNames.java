package mx.org.kaana.libs.echarts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 28/10/2019
 *@time 04:19:09 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class SortNames implements Serializable {

	private static final long serialVersionUID=-3437709081656808092L;
	
	public static final String[] NAMES_DEMOS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	public static final String[] NAMES_MONTHS= {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
	public static final String[] NAMES_WEEKS = {"DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB"};
	public static final String[] NAMES_STATES= {"AGS", "BC", "BCN", "BCS", "CAM", "COA", "COL", "CHA", "CHI", "DF", "CDMX", "DGO", "GTO", "GRO", "HGO", "JAL", "MEX", "MIC", "MOR", "NAY", "NL", "NVL", "OAX", "PUE", "QRO", "ROO", "SLP", "SIN", "SON", "TAB", "TAM", "TLA", "VER", "YUC", "ZAC"};

	public static List<String> toNamesMonths(List<String> labels) {
		return toSort(labels, Arrays.asList(NAMES_WEEKS));
	}
	
	public static List<String> toNamesWeeks(List<String> labels) {
		return toSort(labels, Arrays.asList(NAMES_WEEKS));
	}
	
	public static List<String> toNamesStates(List<String> labels) {
		return toSort(labels, Arrays.asList(NAMES_STATES));
	}

	public static List<String> toSort(List<String> labels, String[] names) {
		return toSort(labels, Arrays.asList(names));
	}

	public static List<String> toSort(List<String> labels, List<String> names) {
		List<String> regresar= new ArrayList<>();
		for (int x=0; x< labels.size(); x++) 
			labels.set(x, labels.get(x).toUpperCase());
		for(String item: names) {
			if(labels.contains(item.toUpperCase()))
				regresar.add(item.toUpperCase());
		} // for
		return regresar;
	}

}
