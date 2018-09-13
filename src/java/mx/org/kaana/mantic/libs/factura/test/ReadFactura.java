package mx.org.kaana.mantic.libs.factura.test;

import java.io.File;
import mx.org.kaana.libs.reportes.FileSearch;
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
		    FileSearch fileSearch = new FileSearch();
				fileSearch.searchDirectory(new File("D:/Temporal/Mantic/xml"), ".xml");
				int count = fileSearch.getResult().size();
				if (count == 0)
					System.out.println("\nNo result found!");
				else {
					System.out.println("\nFound " + count + " result!\n");
					for (String matched : fileSearch.getResult()) {
						System.out.println("Found : " + matched);
      			reader= new Reader(matched);
						factura= reader.execute();
						LOG.info("<========================================================================================>");
						LOG.info("Archivo: "+ matched);
						LOG.info("Factura con folio: " + factura.getFolio());
						LOG.info("Emisor  : " + factura.getEmisor());
						LOG.info("Receptor: " + factura.getReceptor());
						int x= 0;
						for (Concepto concepto : factura.getConceptos()) {
							LOG.info((++x)+ ".- ("+ concepto.getClaveProdServ()+ ") ["+ concepto.getNoIdentificacion()+ "] "+ concepto.getClaveUnidad()+ " -> "+ concepto.getDescripcion()+ " => "+ concepto.getValorUnitario()+ " => "+ concepto.getDescuento()+ "%");
						} // for
						LOG.info("<========================================================================================>");
					} // for
				}	
//			reader= new Reader("D:\\Temporal\\mantic\\Programa\\ELEKTRON.xml");
//			//reader= new Reader("D:\\Temporal\\mantic\\Programa\\Anbec.xml");
//			//reader= new Reader(new File("").getAbsolutePath().concat("\\src\\java\\mx\\org\\kaana\\mantic\\libs\\factura\\test\\A24729.xml"));
//			//reader= new Reader(new File("").getAbsolutePath().concat("\\src\\java\\mx\\org\\kaana\\mantic\\libs\\factura\\test\\"), "factura.xml");
//			factura= reader.execute();
//			LOG.info("Factura con folio: " + factura.getFolio());
//			LOG.info("Emisor  : " + factura.getEmisor());
//			LOG.info("Receptor: " + factura.getReceptor());
//			for (Concepto concepto : factura.getConceptos()) {
//				LOG.info(concepto);
//			} // for
		} // try
		catch (Exception e) {			
			e.printStackTrace();
		} // catch				
	} // main
}
