package mx.org.kaana.mantic.test.depurar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 17/09/2019
 * @time 09:49:09 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Clean {

	private static final Log LOG=LogFactory.getLog(Clean.class);
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File file=new File("D:\\Temporal\\Mantic\\depurar_duplicados_desarrollo.txt");
		BufferedReader br=new BufferedReader(new FileReader(file));
		String st= null;
		int count= 0;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			while ((st= br.readLine())!=null) {
				String[] tokens= st.split(";");
				params.put("idArticulo", tokens[0]);
				params.put("codigo", tokens[1]);
				List<TcManticArticulosCodigosDto> articulos= (List<TcManticArticulosCodigosDto>)DaoFactory.getInstance().toEntitySet(TcManticArticulosCodigosDto.class, "VistaEchartsDemostracionDto", "clean", params);
				int index= 0;
				for (TcManticArticulosCodigosDto item: articulos) {
					if(index> 0) 
						LOG.info("Registro eliminado de la BD ["+ DaoFactory.getInstance().delete(item)+ "] codigo: "+ item.getCodigo());
					index++;
				} // for
				LOG.info("Registro ["+ (count++)+ "]. ");
			} // while
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			// JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

}
