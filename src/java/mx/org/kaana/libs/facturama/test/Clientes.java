package mx.org.kaana.libs.facturama.test;

import java.util.Calendar;
import java.util.List;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.formato.Fecha;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 30/10/2018
 * @time 10:29:39 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Clientes {
	
	private static final Log LOG=LogFactory.getLog(Clientes.class);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
		List<Client> clients        =	CFDIFactory.getInstance().getClients();
		int count= 0;
		for (CfdiSearchResult cfdi : cfdis) {
			Client client= clients.get(clients.indexOf(new Client(cfdi.getRfc())));
			//LOG.warn(++count+ ".- "+ client.getName()+ "  ["+ client.getRfc()+ "]");
			LOG.warn(client.getAddress().getState()+ " - "+ client.getAddress().getMunicipality()+ " - "+ client.getAddress().getLocality()+ " - "+ client.getAddress().getNeighborhood());
			// break;
		} // for
		LOG.info("Ok.");
	}

}
