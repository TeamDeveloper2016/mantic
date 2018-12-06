package mx.org.kaana.libs.facturama.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.facturama.models.Product;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 4/12/2018
 * @time 09:41:20 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Productos {

	private static final Log LOG=LogFactory.getLog(Productos.class);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		List<Product> productos=CFDIFactory.getInstance().getProducts();
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			int count= 0;
			for (Product producto : productos) {
				System.out.println("update tc_mantic_articulos set id_facturama='"+ producto.getId()+ "' where codigo= '"+ producto.getIdentificationNumber()+ "';");
//				LOG.info((count++)+ "\t"+ producto.getId()+ "\t"+ producto.getCodeProdServ()+ "\t"+ producto.getCuentaPredial()+ "\t'"+ producto.getIdentificationNumber()+ "'\t"+ producto.getName());
//  			params.put(Constantes.SQL_CONDICION, "codigo= '"+ producto.getIdentificationNumber()+ "'");
//				TcManticArticulosDto dto= (TcManticArticulosDto)DaoFactory.getInstance().findFirst(TcManticArticulosDto.class, "row", params);
//				if(dto!= null) {
//				   LOG.info(count+ " encontrado "+ dto.getIdArticulo());
//				   if(dto.getIdFacturama()!= null) 
//  				   LOG.info(count+ " ya actualizado "+ dto.getIdArticulo());
//					 else {
//					   dto.setIdFacturama(producto.getId());
//					   DaoFactory.getInstance().update(dto);
//					 } // else
//				} // if
//				else
// 			   LOG.error(producto.getIdentificationNumber());
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

}
