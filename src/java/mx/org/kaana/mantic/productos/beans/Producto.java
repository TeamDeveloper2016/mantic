package mx.org.kaana.mantic.productos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticProductosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 30/09/2021
 *@time 03:26:38 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Producto implements Serializable {

  private static final long serialVersionUID = 7546017419511660227L;
  private static final Log LOG = LogFactory.getLog(Producto.class);
  
  private TcManticProductosDto producto;
  private List<Partida> articulos;
  private List<Caracteristica> caracteristicas;
  private UISelectEntity ikEmpresa;
  
  public Producto() throws Exception {
    this.init(-1L);
  }
  
  public Producto(Long idProducto) throws Exception {
    this.init(idProducto);
  }

  public TcManticProductosDto getProducto() {
    return producto;
  }

  public List<Partida> getArticulos() {
    return articulos;
  }

  public List<Caracteristica> getCaracteristicas() {
    return caracteristicas;
  }

  public UISelectEntity getIkEmpresa() {
    return ikEmpresa;
  }

  public void setIkEmpresa(UISelectEntity ikEmpresa) {
    this.ikEmpresa = ikEmpresa;
  }

  private void init(Long idProducto) throws Exception {
    try {      
      if(Objects.equals(-1L, idProducto)) {
        this.producto = new TcManticProductosDto();
        this.articulos= new ArrayList<>();
        this.caracteristicas= new ArrayList<>();
        this.setIkEmpresa(new UISelectEntity(-1L));
      } // if
      else {
        this.producto = (TcManticProductosDto)DaoFactory.getInstance().findById(TcManticProductosDto.class, idProducto);
        this.articulos= new ArrayList<>();
        this.caracteristicas= new ArrayList<>();
        this.setIkEmpresa(new UISelectEntity(this.producto.getIdEmpresa()));
      } // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
  }
  
  public void addPartida(Partida partida) throws Exception {  
    int index= this.articulos.indexOf(partida);
		try {
      if(index< 0) {
        partida.setOrden(this.articulos.size()+ 1L);
        this.articulos.add(partida);
      } // if  
      else
        if(Objects.equals(partida.getAction(), ESql.INSERT))
          partida.setAction(ESql.UPDATE);
	  } // try
		catch (Exception e) {
      throw e;
		} // catch
  }
  
  public void removePartida(Partida partida) {  
    if(Objects.equals(partida.getAction(), ESql.INSERT))
      this.articulos.remove(partida);
    else  
      partida.setAction(ESql.DELETE);
  }
  
  public void updatePartida(Partida partida) {  
    if(!Objects.equals(partida.getAction(), ESql.INSERT)) {
      partida.setAnterior(ESql.UPDATE);
      partida.setAction(ESql.UPDATE);
    } // if 
  }

  public void upPartida(Partida partida) {  
    if(partida.getOrden()> 1) {
      List<Partida> items= new ArrayList<>();
      Long index= partida.getOrden()- 1L;
      for (Partida articulo: this.articulos) {
        if(Objects.equals(articulo.getOrden(), index)) {
          articulo.setOrden(articulo.getOrden()+ 1L);
          this.updatePartida(articulo);
          items.add(partida);
          items.add(articulo);
        } // if  
        else
          if(!Objects.equals(articulo.getIdArticulo(), partida.getIdArticulo())) 
            items.add(articulo);
      } // for
      partida.setOrden(partida.getOrden()- 1L);
      this.updatePartida(partida);
      this.articulos.clear();
      this.articulos.addAll(items);
      items= null;
    } // if  
  }
  
  public void downPartida(Partida partida) {  
    if(partida.getOrden()< this.articulos.size()) {
      List<Partida> items= new ArrayList<>();
      Long index= partida.getOrden()+ 1L;
      for (Partida articulo: this.articulos) {
        if(Objects.equals(articulo.getOrden(), index)) {
          articulo.setOrden(articulo.getOrden()- 1L);
          this.updatePartida(articulo);
          items.add(articulo);
          items.add(partida);
        } // if  
        else
          if(!Objects.equals(articulo.getIdArticulo(), partida.getIdArticulo())) 
            items.add(articulo);
      } // for
      partida.setOrden(partida.getOrden()+ 1L);
      this.updatePartida(partida);
      this.articulos.clear();
      this.articulos.addAll(items);
      items= null;
    } // if  
  }
  
  public void addCaracteristica(Caracteristica caracteristica) {  
    boolean exists= Boolean.FALSE;
    for (Partida item: this.articulos) {
      if(Objects.equals(item.getDescripcion(), caracteristica.getDescripcion())) {
        exists= Boolean.TRUE;
        item.setAction(ESql.UPDATE);
        break;
      } // if  
    } // for
    if(!exists)
      this.caracteristicas.add(caracteristica);
  }
  
  public void removeCaracteristica(Caracteristica caracteristica) {  
    for (Partida item: this.articulos) {
      if(Objects.equals(item.getDescripcion(), caracteristica.getDescripcion())) {
        if(Objects.equals(caracteristica.getAction(), ESql.INSERT))
          this.articulos.remove(item);
        else  
          item.setAction(ESql.DELETE);
        break;
      } // if  
    } // for
  }
  
  public void updateCaracteristica(Caracteristica caracteristica) {  
    if(!Objects.equals(caracteristica.getAction(), ESql.INSERT)) {
      caracteristica.setAnterior(ESql.UPDATE);
      caracteristica.setAction(ESql.UPDATE);
    } // if
  }
  
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); 
    Methods.clean(this.articulos);
    Methods.clean(this.caracteristicas);
  }

}
