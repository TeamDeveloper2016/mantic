package mx.org.kaana.libs.echarts.test;

import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.echarts.beans.Colors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 28/10/2019
 * @time 10:06:21 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class ColorsDemo {
  private static final Log LOG=LogFactory.getLog(ColorsDemo.class);
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		List<String> colors= new ArrayList<>();
		for (int x=0; x<10; x++) {
			for (int y=0; y<32; y++) {
				colors.add(Colors.toColor(32));
			} // for
			LOG.info(colors);
			colors.clear();
		} // for	
	}

}
