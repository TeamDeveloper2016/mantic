package mx.org.kaana.mantic.libs.factura.test;

import java.io.File;
import mx.org.kaana.mantic.libs.factura.beans.ComprobanteFiscal;
import mx.org.kaana.mantic.libs.factura.reglas.Reader;

public class ReadFactura {

	public static void main (String ... args) throws Exception{
		Reader reader            = null;
		ComprobanteFiscal factura= null;
		try {
			reader= new Reader(new File("").getAbsolutePath().concat("\\src\\java\\mx\\org\\kaana\\mantic\\libs\\factura\\test\\"), "factura.xml");
			factura= reader.execute();
			System.out.println("Se cargo la factura con folio: " + factura.getFolio());
		} // try
		catch (Exception e) {			
			e.printStackTrace();
		} // catch				
	} // main
}
