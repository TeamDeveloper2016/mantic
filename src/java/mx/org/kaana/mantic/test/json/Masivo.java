package mx.org.kaana.mantic.test.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.facturama.models.Product;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 8/12/2018
 * @time 09:38:39 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Masivo {

	private static final Log LOG=LogFactory.getLog(Masivo.class);

	private static final Type REVIEW_TYPE=new TypeToken<List<Product>>() {}.getType();

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws FileNotFoundException, Exception {
		String json="D:\\Temporal\\Mantic\\produccion\\respaldos\\productos_json.txt";
		List<Product> articulos=new Gson().fromJson(new JsonReader(new FileReader(json)), REVIEW_TYPE);
		int count  = 1;
		int update = 0;
		Long result= 0L;
		for (Product item : articulos) {
			try {
			  result= DaoFactory.getInstance().execute("update tc_mantic_articulos set id_facturama= '"+ item.getId()+ "' where upper(codigo)= '"+ (item.getIdentificationNumber()== null? "XYZW": item.getIdentificationNumber().toUpperCase())+ "';");
			} // try
			catch(Exception e) {
				e.printStackTrace();
				result= -1L;
			} // catch
			if(result== null || result<= 0)
		    LOG.error("Este articulo ["+ item.getName()+ "] no se pudo actualizar, posicion #"+ count+ " codigo ["+ item.getIdentificationNumber()+ "] !");
			else {
			  LOG.info("["+ (count)+ " de "+ articulos.size()+ "].- "+ item.getName()+ " actualizado de forma correcta");
				update++;
			} // else
			count++;
 		} // for
	  LOG.info("Registros actualizado de forma correcta ["+ update+ " de "+ articulos.size()+ "] Ok");
	}

}
