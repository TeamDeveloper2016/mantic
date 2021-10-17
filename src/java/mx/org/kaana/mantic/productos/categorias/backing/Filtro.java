package mx.org.kaana.mantic.productos.categorias.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.productos.backing.Contenedor;
import mx.org.kaana.mantic.productos.categorias.reglas.Transaccion;
import mx.org.kaana.mantic.productos.beans.Categoria;
import org.primefaces.model.TreeNode;

@Named(value = "manticProductosCategoriasFiltro")
@ViewScoped
public class Filtro extends Contenedor implements Serializable {

  private static final long serialVersionUID = 8793667741599428369L;

  private Categoria categoria;
  private EAccion accion;

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }
  
  public Boolean getEliminar() {
    return Objects.equals(this.accion, EAccion.ELIMINAR);  
  }
  
  public Boolean getAgregar() {
    return Objects.equals(this.accion, EAccion.AGREGAR);  
  }
  
  public String getTitulo() {
    return this.accion.getName();
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("idActivo", -1L);
      this.doLoad("", 1L, null);
      this.accion= EAccion.AGREGAR;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  public void doAgregar(UISelectEntity father) {
    this.categoria= new Categoria(
      father.toString("padre").length()== 0? father.toString("nombre"): father.toString("padre").concat(father.toString("nombre")).concat(Constantes.SEPARADOR), // String padre, 
      1L, // Long ultimo, 
      1L, // Long idActivo, 
      JsfBase.getIdUsuario(), // Long idUsuario, 
      -1L, // Long idProductoCategoria, 
      0L, // Long porcentaje, 
      "", // String nombre, 
      father.toLong("nivel")+ 1L, // Long nivel, 
      1L, // Long orden
      father.toLong("idProductoCategoria"),  // Long idPadre
      Constantes.SEPARADOR      
    );
  }
  
  public void doModificar(UISelectEntity father) {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idProductoCategoria", father.toLong("idProductoCategoria"));      
      this.categoria= (Categoria)DaoFactory.getInstance().toEntity(Categoria.class, "TcManticProductosCategoriasDto", "existe", params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }

  public void doAccion() {
		Transaccion transaccion = null;
		try {
      while(this.categoria.getNombre().startsWith(this.categoria.getSeparador()))
        this.categoria.setNombre(this.categoria.getNombre().substring(1));
      while(this.categoria.getNombre().endsWith(this.categoria.getSeparador()))
        this.categoria.setNombre(this.categoria.getNombre().substring(0, this.categoria.getNombre().length()- 1));
			transaccion= new Transaccion(this.categoria);
			if(transaccion.ejecutar(this.accion)) {
				JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "La categoría se "+ this.accion.getName().toLowerCase()+ " correctamente", ETipoMensaje.ERROR);
        this.getRoot().clearParent();
        this.init();
      } // if  
			else
				JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "Ocurrió un error al "+ this.accion.getName().toLowerCase()+ " el registro de la categoría", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doTransaccion	
 
  public String doGaleria() {
		try {
      UISelectEntity data= (UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData();
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Productos/Categorias/filtro");		
			JsfBase.setFlashAttribute("producto", data);
			JsfBase.setFlashAttribute("categoria", data.toString("padre").concat(data.toString("nombre")));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Productos/galeria".concat(Constantes.REDIRECIONAR);
  } // doGaleria
 
  public void doOperacion(String item) {
    this.accion= EAccion.valueOf(item);
 		Map<String, Object> params= new HashMap<>();
    try {
      switch(this.accion) {
        case AGREGAR:
          this.doAgregar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
          break;
        case MODIFICAR:
        case ELIMINAR:
           if(Objects.equals(((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData()).toLong("nivel"), 1L)) {
             JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "Esta categoría no se puede "+ this.accion.getName().toLowerCase(), ETipoMensaje.ALERTA);
             UIBackingUtilities.execute("janal.desbloquear();");
           } // if  
           else {
             this.doModificar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
             TreeNode father= ((TreeNode)this.attrs.get("seleccionado")).getParent();
             this.categoria.setIdPadre(((UISelectEntity)father.getData()).toLong("idProductoCategoria"));
             UIBackingUtilities.execute("PF('widgetCategoria').show();");
          } // else  
          break;
        case SUBIR:
          if(Objects.equals(((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData()).toLong("orden"), 1L)) 
            JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "Esta categoría no se puede "+ this.accion.getName().toLowerCase(), ETipoMensaje.ALERTA);
          else {
            this.doModificar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
            TreeNode father= ((TreeNode)this.attrs.get("seleccionado")).getParent();
            this.categoria.setIdPadre(((UISelectEntity)father.getData()).toLong("idProductoCategoria"));
            this.doAccion();
          } // else  
          break;
        case BAJAR:
          Long maximo= -1L;
    			params.put("padre", ((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData()).toString("padre"));
          Value next= DaoFactory.getInstance().toField("TcManticProductosCategoriasDto", "maximo", params, "siguiente");
          if(next!= null && next.getData()!= null)
            maximo= next.toLong();
          if(Objects.equals(((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData()).toLong("orden"), maximo)) 
            JsfBase.addMessage(Cadena.letraCapital(this.accion.getName()), "Esta categoría no se puede "+ this.accion.getName().toLowerCase(), ETipoMensaje.ALERTA);
          else {
            this.doModificar((UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData());
            TreeNode father= ((TreeNode)this.attrs.get("seleccionado")).getParent();
            this.categoria.setIdPadre(((UISelectEntity)father.getData()).toLong("idProductoCategoria"));
            this.doAccion();
          } // else  
          break;
      } // switch
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
  public void doLoad() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
}