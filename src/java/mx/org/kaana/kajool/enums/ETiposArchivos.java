package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/09/2015
 *@time 09:57:52 AM
 *@author usuario <usuario.usuario>
 */

public enum ETiposArchivos {
  IMAGEN    ("JPG"),
  POLIGONO	("TXT"),
  COORDENADA("TXT"),
  DBF     	("DBF"),
  ZIP     	("ZIP"),
  PDA     	("JAR"),
  TXT     	("TXT"),
  XLS     	("XLS"),
  XLSX    	("XLSX"),
  DOC     	("DOC"),
  DOCX    	("DOCX"),
  PDF       ("PDF"),
  CSV       ("CSV");

  private String extension;

  private ETiposArchivos(String extension) {
    this.extension = extension;
  }

  public String getExtension() {
    return extension;
  }

}
