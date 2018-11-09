package mx.org.kaana.mantic.test.printer;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.PrinterName;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 28/09/2018
 * @time 11:43:37 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Zebra {

	private static final String ZEBRA="SH-PT6N2";

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		PrintService psZebra=null;
		String sPrinterName=null;
		try {
			PrintService[] services=PrintServiceLookup.lookupPrintServices(null, null);
			for (int i=0; i<services.length; i++) {
				PrintServiceAttribute attr=services[i].getAttribute(PrinterName.class);
				sPrinterName=((PrinterName) attr).getValue();
				System.out.println("Found printer: "+sPrinterName+"\n");
				if (sPrinterName.toUpperCase().contains(ZEBRA)) {
					psZebra=services[i];
					break;
				} // if
			} // for
			if (psZebra==null) {
				System.out.println("ZEBRA printer is not found.");
				return;
			} // if
			System.out.println("Found printer: "+sPrinterName);
			DocPrintJob job=psZebra.createPrintJob();
			String s="";
			s=s+"N\r\n";
			s=s+"Q380,24\r\n";
			s=s+"R203,20\r\n";
			s=s+"S2\r\n";
			s=s+"A60,0,0,2,3,2,N,\"00-0000-00\"\r\n";
			s=s+"B8,140,0,UA0,2,3,100,B,\"00000000000\"\r\n";
			s=s+"B10,260,0,3,2,4,50,N,\"00-0000-00\"\r\n";
			s=s+"P1\r\n";
			byte[] by=s.getBytes();
			DocFlavor flavor=DocFlavor.BYTE_ARRAY.AUTOSENSE;
			Doc doc=new SimpleDoc(by, flavor, null);
			job.print(doc, null);
		} // try
		catch (Exception e) {
			e.printStackTrace();
		} // catch
	}

}
