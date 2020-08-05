package mx.org.kaana.mantic.libs.factura.test;

import java.io.File;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.libs.factura.beans.ComprobanteFiscal;
import mx.org.kaana.mantic.libs.factura.reglas.Reader;

public class Duplicados {
	
	private static final String SQL= "select tc_mantic_ventas.ticket from tc_mantic_facturas inner join tc_mantic_ventas on tc_mantic_facturas.id_factura= tc_mantic_ventas.id_factura where tc_mantic_facturas.folio=";

	// 2020-08-05T12:32:08
	// 0123456789012345678
	public static String toDate(String value) {
		return value.substring(8, 10)+"/"+value.substring(5, 7)+"/"+value.substring(0, 4);
	}

	public static String toHour(String value) {
		return value.substring(11, 18);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
//		List<CfdiSearchResult> items= (List<CfdiSearchResult>)CFDIFactory.getInstance().getCfdis();
		int index= 0;
//    for (CfdiSearchResult item : items) {
//			System.out.println((count++)+ "|"+ toDate(item.getDate())+ "|"+ toHour(item.getDate())+ "|"+ item.getRfc()+ "|"+ item.getFolio()+ ", "+ item.getTotal() );
//		} // for
		Reader reader            = null;
		ComprobanteFiscal factura= null;
		try {
			FileSearch fileSearch=new FileSearch();
			fileSearch.searchDirectory(new File("D:/Temporal/Mantic/facturas"), ".xml");
			int count=fileSearch.getResult().size();
			if (count==0)
				System.out.println("\nNo result found!");
			else {
				String value= "-";
				for (String matched: fileSearch.getResult()) {
					// System.out.println(++index+ " found: "+ matched);
					++index;
					// if(index> 590 && index< 592) {
     			reader = new Reader(matched);
					factura= reader.execute();
					// value  = "-"; 
					// Value ticket= DaoFactory.getInstance().toField(SQL+ " '"+ factura.getFolio()+ "'");
					// if(ticket!= null && ticket.getData()!= null)
					//	value= ticket.toString();
          System.out.println(index+ "|"+ toDate(factura.getFecha())+ "|"+ toHour(factura.getFecha())+ "|"+ factura.getReceptor().getRfc()+ "|"+ factura.getReceptor().getNombre()+ "|"+ factura.getFolio()+ "|"+ factura.getTotal()+ "|"+ value);
					// } // if
				} // for
			} // else	
		} // try
		catch (Exception e) {			
			e.printStackTrace();
		} // catch				
	}
	
}