package mx.org.kaana.libs.facturama.test;

import java.util.List;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company INEGI
 * @project IKTAN (Sistema de seguimiento y control de proyectos)
 * @date 30/10/2018
 * @time 10:29:39 PM
 * @author Alejandro Jimenez Garcia <alejandro.jimenez@inegi.org.mx>
 */
public class Descargas {
	
	private static final Log LOG=LogFactory.getLog(Descargas.class);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
		int count= 0;
		for (CfdiSearchResult cfdi : cfdis) {
   		LOG.info(++count+ ".- Descargando la factura ["+ cfdi.getFolio()+ "] "+ cfdi.getEmail());	
   		// Descarga de los archivos de la factura
	  	String path = "d:/temporal/mantic/descargas/".concat(cfdi.getRfc()).concat("/");
		  CFDIFactory.getInstance().download(path, cfdi.getRfc().concat("-").concat(cfdi.getFolio()), cfdi.getId());
		} // for
		LOG.info("Ok.");
	}

}
