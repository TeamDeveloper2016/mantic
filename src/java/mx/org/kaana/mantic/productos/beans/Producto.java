package mx.org.kaana.mantic.productos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
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
  
  private General producto;
  private List<Partida> articulos;
  private List<Caracteristica> caracteristicas;
  private UISelectEntity ikEmpresa;
  private UISelectEntity ikMarca;
  
  public Producto() throws Exception {
    this(-1L);
  }
  
  public Producto(Long idProducto) throws Exception {
    this(idProducto, "menudeo");
  }
  
  public Producto(Long idProducto, String cliente) throws Exception {
    this.init(idProducto, cliente);
  }

  public General getProducto() {
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
    if(this.ikEmpresa!= null)
      this.producto.setIdEmpresa(this.ikEmpresa.getKey());
  }

  public UISelectEntity getIkMarca() {
    return ikMarca;
  }

  public void setIkMarca(UISelectEntity ikMarca) {
    this.ikMarca = ikMarca;
    if(ikMarca!= null)
      this.producto.setIdProductoMarca(ikMarca.getKey());
  }

  private void init(Long idProducto, String cliente) throws Exception {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idProducto", idProducto);      
      if(Objects.equals(-1L, idProducto)) {
        this.producto= new General();
        this.producto.setIdImagen(-1L);
        this.setIkEmpresa(new UISelectEntity(-1L));
        this.setIkMarca(new UISelectEntity(2L));
      } // if
      else {
        this.producto = (General)DaoFactory.getInstance().toEntity(General.class, "VistaProductosDto", "producto", params);
        this.articulos= (List<Partida>)DaoFactory.getInstance().toEntitySet(Partida.class, "VistaProductosDto", "articulos", params, -1L);
        if(this.articulos!= null)
          for (Partida item: this.articulos) {
            item.setPrincipal(Objects.equals(this.producto.getIdImagen(), item.getIdImagen()));
            item.setAnterior(ESql.SELECT);
            item.setAction(ESql.SELECT);
            item.toLoadCodigos(Boolean.FALSE, cliente);
            if(item.getPrincipal())
              this.producto.setArchivo(item.getArchivo());
          } // for
        this.caracteristicas= (List<Caracteristica>)DaoFactory.getInstance().toEntitySet(Caracteristica.class, "TcManticProductosCaracteristicasDto", "caracteristicas", params, -1L);
        if(this.caracteristicas!= null)
          for (Caracteristica item: this.caracteristicas) {
            item.setAnterior(ESql.SELECT);
            item.setAction(ESql.SELECT);
          } // for
        this.setIkEmpresa(new UISelectEntity(this.producto.getIdEmpresa()));
        this.setIkMarca(new UISelectEntity(this.producto.getIdProductoMarca()));
      } // else
      if(this.articulos== null)
        this.articulos= new ArrayList<>();
      if(this.caracteristicas== null)
        this.caracteristicas= new ArrayList<>();
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
  public void addPartida(Partida partida) {  
    int index= this.articulos.indexOf(partida);
    if(index< 0) {
      partida.setOrden(this.articulos.size()+ 1L);
      this.articulos.add(partida);
    } // if  
    else
      if(Objects.equals(partida.getAction(), ESql.DELETE))
        partida.setAction(ESql.UPDATE);
    this.checkDefaultImage();
  }
  
  public void removePartida(Partida partida) {  
    if(Objects.equals(partida.getAction(), ESql.INSERT))
      this.articulos.remove(partida);
    else  
      partida.setAction(ESql.DELETE);
  }
  
  public void doRecoverPartida(Partida partida) {  
    partida.setAction(partida.getAnterior());
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
    int index= this.caracteristicas.indexOf(caracteristica);
    if(index< 0) {
      caracteristica.setOrden(this.caracteristicas.size()+ 1L);
      this.caracteristicas.add(caracteristica);
    } // if  
    else
      if(Objects.equals(caracteristica.getAction(), ESql.DELETE))
        caracteristica.setAction(ESql.UPDATE);
  }
  
  public void removeCaracteristica(Caracteristica caracteristica) {  
    if(Objects.equals(caracteristica.getAction(), ESql.INSERT))
      this.caracteristicas.remove(caracteristica);
    else  
      caracteristica.setAction(ESql.DELETE);
  }
  
  public void doRecoverCaracteristica(Caracteristica caracteristica) {  
    caracteristica.setAction(caracteristica.getAnterior());
  }
  
  public void upCaracteristica(Caracteristica caracteristica) {  
    if(caracteristica.getOrden()> 1) {
      List<Caracteristica> items= new ArrayList<>();
      Long index= caracteristica.getOrden()- 1L;
      for (Caracteristica item: this.caracteristicas) {
        if(Objects.equals(item.getOrden(), index)) {
          item.setOrden(item.getOrden()+ 1L);
          this.updateCaracteristica(item);
          items.add(caracteristica);
          items.add(item);
        } // if  
        else
          if(!Objects.equals(item.getIdProductoCaracteristica(), caracteristica.getIdProductoCaracteristica())) 
            items.add(item);
      } // for
      caracteristica.setOrden(caracteristica.getOrden()- 1L);
      this.updateCaracteristica(caracteristica);
      this.caracteristicas.clear();
      this.caracteristicas.addAll(items);
      items= null;
    } // if  
  }
  
  public void downCaracteristica(Caracteristica caracteristica) {  
    if(caracteristica.getOrden()< this.caracteristicas.size()) {
      List<Caracteristica> items= new ArrayList<>();
      Long index= caracteristica.getOrden()+ 1L;
      for (Caracteristica item: this.caracteristicas) {
        if(Objects.equals(item.getOrden(), index)) {
          item.setOrden(item.getOrden()- 1L);
          this.updateCaracteristica(item);
          items.add(item);
          items.add(caracteristica);
        } // if  
        else
          if(!Objects.equals(item.getIdProductoCaracteristica(), caracteristica.getIdProductoCaracteristica())) 
            items.add(item);
      } // for
      caracteristica.setOrden(caracteristica.getOrden()+ 1L);
      this.updateCaracteristica(caracteristica);
      this.caracteristicas.clear();
      this.caracteristicas.addAll(items);
      items= null;
    } // if  
  }
  
  public void updateCaracteristica(Caracteristica caracteristica) {  
    if(!Objects.equals(caracteristica.getAction(), ESql.INSERT)) {
      caracteristica.setAnterior(ESql.UPDATE);
      caracteristica.setAction(ESql.UPDATE);
    } // if
  }

  public void toUpdatePrincipal(Partida partida) {
    for (Partida item: articulos) {
      if(!Objects.equals(item.getIdArticulo(), partida.getIdArticulo()))
        item.setPrincipal(Boolean.FALSE);
    } // for
    if(partida.getPrincipal())
      this.producto.setIdImagen(partida.getIdImagen());
    else
      this.producto.setIdImagen(-1L);
  } 
  
  private void checkDefaultImage() {
    if(this.producto.getIdImagen()== null || this.producto.getIdImagen()<= 0L)  
      for (Partida item: this.articulos) {
        if(item.getIdImagen()> 0L) {
          item.setPrincipal(Boolean.TRUE);
          this.producto.setIdImagen(item.getIdImagen());
          this.producto.setArchivo(item.getArchivo());
          break;
        } // if  
      } // for
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize(); 
    Methods.clean(this.articulos);
    Methods.clean(this.caracteristicas);
  }

}
