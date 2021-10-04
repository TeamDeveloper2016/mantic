package mx.org.kaana.mantic.productos.reglas;

import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.ESql.DELETE;
import static mx.org.kaana.kajool.enums.ESql.INSERT;
import static mx.org.kaana.kajool.enums.ESql.UPDATE;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private Producto producto;	
	private String messageError;

	public Transaccion(Producto producto) {
		this.producto= producto;		
	}

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		try {
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el registro del producto");
			switch(accion){
				case AGREGAR:
          regresar= this.toAgregarProducto(sesion);
					break;
				case MODIFICAR:
          regresar= this.toModificarProducto(sesion);
					break;				
				case ELIMINAR:
          regresar= this.toEliminarProducto(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	// ejecutar
	
  private Boolean toAgregarProducto(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
    try {      
      regresar= DaoFactory.getInstance().insert(sesion, this.producto.getProducto())> 0L;
      if(regresar) {
        this.toArticulos(sesion);
        this.toCaracteristicas(sesion);
      } // if  
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
  private Boolean toModificarProducto(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
    try {      
      regresar= DaoFactory.getInstance().update(sesion, this.producto.getProducto())> 0L;
      if(regresar) {
        this.toArticulos(sesion);
        this.toCaracteristicas(sesion);
      } // if  
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
  private Boolean toEliminarProducto(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
    try {      
      for (Partida item: this.producto.getArticulos()) {
        regresar= DaoFactory.getInstance().delete(sesion, item)> 0L;
      } // for
      for (Caracteristica item: this.producto.getCaracteristicas()) {
        regresar= DaoFactory.getInstance().delete(sesion, item)> 0L;
      } // for
      regresar= DaoFactory.getInstance().delete(sesion, this.producto.getProducto())> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
  private Boolean toArticulos(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
    try {      
      for (Partida item: this.producto.getArticulos()) {
        switch(item.getAction()) {
          case INSERT:
            item.setIdProducto(this.producto.getProducto().getIdProducto());
            regresar= DaoFactory.getInstance().insert(sesion, item)> 0L;
            break;
          case UPDATE:
            regresar= DaoFactory.getInstance().update(sesion, item)> 0L;
            break;
          case DELETE:
            regresar= DaoFactory.getInstance().delete(sesion, item)> 0L;
            break;
        } // switch
        if(item.getPrincipal() && !Objects.equals(item.getIdImagen(), this.producto.getProducto().getIdImagen())) 
          DaoFactory.getInstance().update(sesion, this.producto.getProducto());
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
  private Boolean toCaracteristicas(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
    try {      
      for (Caracteristica item: this.producto.getCaracteristicas()) {
        switch(item.getAction()) {
          case INSERT:
            item.setIdProducto(this.producto.getProducto().getIdProducto());
            regresar= DaoFactory.getInstance().insert(sesion, item)> 0L;
            break;
          case UPDATE:
            regresar= DaoFactory.getInstance().update(sesion, item)> 0L;
            break;
          case DELETE:
            regresar= DaoFactory.getInstance().delete(sesion, item)> 0L;
            break;
        } // switch
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
}