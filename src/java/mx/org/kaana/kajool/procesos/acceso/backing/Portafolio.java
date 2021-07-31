package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.org.kaana.kajool.procesos.acceso.beans.Producto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolAccesoPortafolio")
public class Portafolio extends IBaseImportar implements Serializable {

  private static final Log LOG = LogFactory.getLog(Portafolio.class);
  private static final long serialVersionUID = 5323749709626263802L;
  
  private List<Producto> productos;
  private Producto item;

  public List<Producto> getProductos() {
    return productos;
  }

  public void setProductos(List<Producto> productos) {
    this.productos = productos;
  }

  public Producto getItem() {
    return item;
  }

  public void setItem(Producto item) {
    this.item = item;
  }

  @Inject 
  private TemaActivo temaActivo;

  public TemaActivo getTemaActivo() {
    return temaActivo;
  }

  public void setTemaActivo(TemaActivo temaActivo) {
    this.temaActivo = temaActivo;
  }

  @Override
  @PostConstruct
  protected void init() {
    this.productos= new ArrayList<>();
    this.productos.add(new Producto(0L, "PEGAT", "Programas Estad�sticos y Geogr�ficos en el �mbito Territorial", "Programas Estad�sticos y Geogr�ficos en el �mbito Territorial", "pegat.png", "", "pegat-a.png"));
    this.productos.add(new Producto(1L, "SSPI", "Sistema de Seguimiento a los Programas de Informaci�n", "Sistema de Seguimiento a los Programas de Informaci�n", "sspi.png", "", "sspi-a.png"));
    this.productos.add(new Producto(2L, "SIAT", "Sistema de Incidencias en el �mbito Territorial", "Sistema de Incidencias en el �mbito Territorial", "siat.png", "", "siat-a.png"));
    this.productos.add(new Producto(3L, "SSAC", "Sistema de Seguimiento a las Acciones de Promoci�n y Concertaci�n", "Sistema de Seguimiento a las Acciones de Promoci�n y Concertaci�n", "ssac.png", "", "ssac-a.png"));
    this.productos.add(new Producto(4L, "SIPP", "Sistema de Integraci�n de Proyectos para su Promoci�n", "Sistema de Integraci�n de Proyectos para su Promoci�n", "sipp.png", "", "sipp-a.png"));
    this.productos.add(new Producto(6L, "COMITES", "Sistema de Seguimiento a Comit�s y Convenios", "Sistema de Seguimiento a Comit�s y Convenios", "comites.png", "", "comites-a.png"));
    this.productos.add(new Producto(6L, "FEGEM", "Sistema de administraci�n de invitados para el Foro de Estad�stica y Geograf�a para Estados y Municipios", "Sistema de administraci�n de invitados para el Foro de Estad�stica y Geograf�a para Estados y Municipios", "fegem.png", "", "fegem-a.png"));
    this.item= this.productos.get(0);
  }

	@Override
	public void doLoad() {
	}
 
  public void doProductoSelect(Integer index) {
    this.item= this.productos.get(index);
  }
  
  public void doProductoItem(Producto item) {
    this.item= item;
  }
  
  public String doTableroMove(Producto item) {
    String regresar= null;
    return regresar;
  }
  
}