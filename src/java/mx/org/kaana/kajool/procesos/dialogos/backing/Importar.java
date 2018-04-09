package mx.org.kaana.kajool.procesos.dialogos.backing;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.procesos.importacion.beans.Importado;
import mx.org.kaana.kajool.procesos.importacion.reglas.UIArchivo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 31/01/2013
 *@time 01:19:16 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name="kajoolDialogosImportar")
@ViewScoped
public class Importar  implements Serializable{
  private static final long serialVersionUID=6517903661799863480L;
  private static final Log LOG = LogFactory.getLog(Importar.class);

  private UIArchivo archivo;
  private Importado seleccionado;

  public Importado getSeleccionado() {
    return seleccionado;
  }

  public void setSeleccionado(Importado seleccionado) {
    this.seleccionado=seleccionado;
  }

  public UIArchivo getArchivo() {
    return archivo;
  }

  public void setArchivo(UIArchivo archivo) {
    this.archivo=archivo;
  }

  @PostConstruct
  private void init(){
    this.archivo = new UIArchivo();
    this.seleccionado =  new Importado("", "", EFormatos.FREE, 0L);
  }

  public void doEliminar(){
     this.archivo.doEliminarLista(this.seleccionado);
  }

}
