package mx.org.kaana.mantic.test.files;

import java.io.File;
import mx.org.kaana.libs.reportes.FileSearch;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 13/07/2018
 * @time 10:00:01 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Search {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
    FileSearch fileSearch = new FileSearch();
    //try different directory and filename :)
    fileSearch.searchDirectory(new File("d:/temporal/facturas/PILARBLANCO\\2018\\JULIO\\PCO060104B16\\"), ".xml");
    int count = fileSearch.getResult().size();
    if (count == 0)
      System.out.println("\nNo result found!");
    else {
      System.out.println("\nFound " + count + " result!\n");
      for (String matched : fileSearch.getResult()) {
        System.out.println("Found : " + matched);
      } // for
    }	
	}

}
