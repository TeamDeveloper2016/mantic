package mx.org.kaana.mantic.test.collections;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/07/2018
 *@time 01:14:57 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Ciclos {
	private static final Log LOG=LogFactory.getLog(Ciclos.class);
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		List<Integer> list= new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		for (Integer item : list) {
			LOG.info(item);
			//list.remove(item);
		} // for
		String name= "c:\\hola\\como\\estas\\hola.pdf";
		LOG.info(name.substring(name.lastIndexOf("\\")+ 1));
	}

}
