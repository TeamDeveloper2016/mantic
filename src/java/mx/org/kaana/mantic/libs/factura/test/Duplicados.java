package mx.org.kaana.mantic.libs.factura.test;

import java.util.List;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;

public class Duplicados {
    
	// 2020-08-05T12:32:08
	// 0123456789012345678
	public static String toDate(String value) {
	  return value.substring(8, 10)+ "/"+ value.substring(5, 7)+ "/"+ value.substring(0, 4);
	}
	
	public static String toHour(String value) {
	  return value.substring(11, 18);
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		List<CfdiSearchResult> items= (List<CfdiSearchResult>)CFDIFactory.getInstance().getCfdis();
		int count= 0;
    for (CfdiSearchResult item : items) {
			
			System.out.println((count++)+ ","+ toDate(item.getDate())+ ","+ toHour(item.getDate())+ ","+ item.getRfc()+ ","+ item.getFolio()+ ", "+ item.getTotal() );
		} // for
	}

}
