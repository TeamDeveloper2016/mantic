package mx.org.kaana.mantic.contadores.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticContadoresDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/08/2025
 *@time 11:49:27 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Contador extends TcManticContadoresDto implements Serializable {

  private static final long serialVersionUID = -6569742635547643149L;

	private UISelectEntity ikEmpresa;
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikTrabaja;
  private List<Producto> productos;

  public Contador() throws Exception {
    this(-1L);
  }

  public Contador(Long key) throws Exception {
    super(key);
    this.setIkEmpresa(new UISelectEntity(1L));
    this.setIkAlmacen(new UISelectEntity(1L));
    this.setIkTrabaja(new UISelectEntity(1L));
    this.productos= new ArrayList<>();
  }

  public UISelectEntity getIkEmpresa() {
    return ikEmpresa;
  }

  public void setIkEmpresa(UISelectEntity ikEmpresa) {
    this.ikEmpresa = ikEmpresa;
    if(!Objects.equals(ikEmpresa, null))
      this.setIdEmpresa(ikEmpresa.getKey());
  }

  public UISelectEntity getIkAlmacen() {
    return ikAlmacen;
  }

  public void setIkAlmacen(UISelectEntity ikAlmacen) {
    this.ikAlmacen = ikAlmacen;
    if(!Objects.equals(ikAlmacen, null))
      this.setIdAlmacen(ikAlmacen.getKey());
  }

  public UISelectEntity getIkTrabaja() {
    return ikTrabaja;
  }

  public void setIkTrabaja(UISelectEntity ikTrabaja) {
    this.ikTrabaja = ikTrabaja;
    if(!Objects.equals(ikTrabaja, null))
      this.setIdTrabaja(ikTrabaja.getKey());
  }

  public List<Producto> getProductos() {
    return productos;
  }

  public void setProductos(List<Producto> productos) {
    this.productos = productos;
  }

  public Boolean add(Producto producto) {  
    Boolean regresar= Boolean.FALSE;
    int index= this.productos.indexOf(producto);
    if(index< 0) 
      this.productos.add(producto);
    else
      if(Objects.equals(producto.getSql(), ESql.DELETE))
        producto.setSql(ESql.UPDATE);
      else
        regresar= Boolean.TRUE;
    return regresar;
  }
  
  public void remove(Producto producto) {  
    if(Objects.equals(producto.getSql(), ESql.INSERT))
      this.productos.remove(producto);
    else  
      producto.setSql(ESql.DELETE);
  }
  
  public void recover(Producto producto) {  
    if(Objects.equals(producto.getSql(), ESql.DELETE))
      producto.setSql(ESql.UPDATE);
  }

  public void toLoadProductos() {
    Map<String, Object> params = new HashMap<>();
    try {      
      params.put("idContador", this.getIdContador());      
      this.productos= (List<Producto>)DaoFactory.getInstance().toEntitySet(Producto.class, "TcManticContadoresDetallesDto", "detalle", params);
      for (Producto item: this.productos) {
        item.setIkArticulo(new UISelectEntity(item.getIdArticulo()));
        if(Objects.equals(this.getIdContadorEstatus(), 3L)) // INTEGRANDO
          item.setSql(ESql.UPDATE);
        else
          item.setSql(ESql.SELECT);
      } // for  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  } 
  
  @Override
  public Class toHbmClass() {
    return TcManticContadoresDto.class;
  }
  
}
