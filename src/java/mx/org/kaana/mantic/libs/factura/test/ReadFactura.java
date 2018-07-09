package mx.org.kaana.mantic.libs.factura.test;

import java.io.File;
import mx.org.kaana.mantic.libs.factura.beans.ComprobanteFiscal;
import mx.org.kaana.mantic.libs.factura.beans.Concepto;
import mx.org.kaana.mantic.libs.factura.reglas.Reader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReadFactura {
  private static final Log LOG=LogFactory.getLog(ReadFactura.class);
	
	public static void main (String ... args) throws Exception {
		Reader reader            = null;
		ComprobanteFiscal factura= null;
		try {
			reader= new Reader(new File("").getAbsolutePath().concat("\\src\\java\\mx\\org\\kaana\\mantic\\libs\\factura\\test\\A24729.xml"));
			//reader= new Reader(new File("").getAbsolutePath().concat("\\src\\java\\mx\\org\\kaana\\mantic\\libs\\factura\\test\\"), "factura.xml");
			factura= reader.execute();
			LOG.info("Factura con folio: " + factura.getFolio());
			LOG.info("Emisor  : " + factura.getEmisor());
			LOG.info("Receptor: " + factura.getReceptor());
			for (Concepto concepto : factura.getConceptos()) {
				LOG.info(concepto);
			} // for
		} // try
		catch (Exception e) {			
			e.printStackTrace();
		} // catch				
	} // main
}
