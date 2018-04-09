package mx.org.kaana.kajool.procesos.importacion.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.procesos.importacion.beans.Importado;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jan 22, 2013
 *@time 4:29:08 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Imagenes implements Serializable, IImportar {

  private static final long serialVersionUID=4292612381428609341L;
  private List<Importado> files;
  private Contenido contenido;

  public Imagenes() {
    this.files= new ArrayList<Importado>();
    this.contenido= new Contenido();
  }

  @Override
  public String getTitle() {
    return "Seleccione las imagenes a importar";
  }

  @Override
  public String getReturn() {
    return "proceso";
  }

  @Override
  public String getContinue() {
    return "proceso";
  }

  @Override
  public String getFormats() {
    return "gif|jpe?g|png";
  }

  @Override
  public IValidar validate() {
    return this.contenido;
  }

  @Override
  public List<Importado> getFiles() {
    return files;
  }

  @Override
  public boolean isMultiple() {
    return false;
  }

}
