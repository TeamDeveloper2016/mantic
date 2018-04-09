package mx.org.kaana.libs.formato;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import mx.org.kaana.libs.Constantes;

public class FormatosFile {

  private String extensions;
  private String separator;

  public FormatosFile(String extensions) {
    this(extensions, Constantes.SEPARADOR);
  }

  public FormatosFile(String extensions, String separator) {
    setExtensions(extensions);
    setSeparator(separator);
  }

  public void setSeparator(String separator) {
    this.separator = separator;
  }

  public String getSeparator() {
    return separator;
  }

  public void setExtensions(String extensions) {
    this.extensions = extensions;
  }

  public String getExtensions() {
    return extensions;
  }

  public List<Extensions> toFormat() {
    List<Extensions> regresar = new ArrayList<Extensions>();
    StringTokenizer tokens = new StringTokenizer(getExtensions(), "|", false);
    String token = null;
    Extensions extension = null;
    while (tokens.hasMoreTokens()) {
      token = tokens.nextToken();
      extension = Extensions.valueOf(token.toUpperCase());
      if (extension != null)
        regresar.add(extension);
    } // while
    return regresar;
  }
}
