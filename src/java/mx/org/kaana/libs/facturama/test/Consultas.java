package mx.org.kaana.libs.facturama.test;

import java.util.List;
import mx.org.kaana.libs.facturama.models.response.Cfdi;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.models.response.Item;
import mx.org.kaana.libs.facturama.models.response.Tax;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 30/10/2018
 * @time 10:29:39 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Consultas {
	
	private static final Log LOG=LogFactory.getLog(Consultas.class);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
//		List<Client> clients= CFDIFactory.getInstance().getClients();
//		int count= 0;
//		for (Client client : clients) {
//   		LOG.info(++count+ ".-["+ client.getRfc()+ "] "+ client.getName());	
//		} // for
//		LOG.info("-----------------------------------------------------------------------------");
//		List<Product> products= CFDIFactory.getInstance().getProducts();
//		count= 0;
//		for (Product product : products) {
//   		LOG.info(++count+ ".-["+ product.getName()+ "] "+ product.getDescription()+ " - "+ product.getUnit()+ " ["+ product.getUnitCode()+ "]");	
//		} // for
//		LOG.info("-----------------------------------------------------------------------------");
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
		int count= 0;
		for (CfdiSearchResult cfdi : cfdis) {
   		LOG.info(++count+ ".-["+ cfdi.getFolio()+ "] "+ cfdi.getEmail()+ " -> "+ cfdi.getId());	
			Cfdi detail= CFDIFactory.getInstance().toCfdiDetail(cfdi.getId());
			List<Item> items= detail.getItems();
			for (Item item : items) {
     		LOG.info("("+ item.getUnit()+ ") ["+ item.getUnitValue()+ "]  "+ item.getDescription()+ " - "+ item.getQuantity()+ " - "+ item.getTotal());	
			} // for
			List<Tax> taxs= detail.getTaxes();
			for (Tax tax : taxs) {
     		LOG.info("("+ tax.getType()+ ") ["+ tax.getName()+ "]  "+ tax.getRate()+ " - "+ tax.getTotal());	
			} // for
			break;
		} // for
	}

}
