package mx.org.kaana.libs.reportes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @company Instituto Nacional de Estadistica y Geografia
 * @project KAJOOL (Control system polls)
 * @date 23/05/2014
 * @time 10:40:30 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class FileSearch {

  private String fileNameToSearch;
  private List<String> result = new ArrayList<String>();

  public String getFileNameToSearch() {
    return fileNameToSearch;
  }

  public void setFileNameToSearch(String fileNameToSearch) {
    this.fileNameToSearch = fileNameToSearch;
  }

  public List<String> getResult() {
    return result;
  }

  public void searchDirectory(File directory, String fileNameToSearch) {
    setFileNameToSearch(fileNameToSearch);
    if (directory.isDirectory())
      search(directory);
    else
      System.out.println(directory.getAbsoluteFile() + " is not a directory!");
  }

  private void search(File file) {
    if (file.isDirectory()) {
      System.out.println("Searching directory ... " + file.getAbsoluteFile());
      //do you have permission to read this directory?	
      if (file.canRead()) {
        for (File temp: file.listFiles()) {
          if (temp.isDirectory())
            search(temp);
          else
            if (temp.getName().toLowerCase().endsWith(getFileNameToSearch()))
              result.add(temp.getAbsoluteFile().toString());
        } // for
      } // if
      else
        System.out.println(file.getAbsoluteFile() + "Permission Denied");
    } // if
  }

  public static void main(String[] args) {
    FileSearch fileSearch = new FileSearch();
    //try different directory and filename :)
    fileSearch.searchDirectory(new File("D:/Plataforma/Netbeans/JasperReport"), ".jasper");
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
