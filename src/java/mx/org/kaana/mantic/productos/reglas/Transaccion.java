package mx.org.kaana.mantic.productos.reglas;

import com.google.common.base.Objects;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.ESql.DELETE;
import static mx.org.kaana.kajool.enums.ESql.INSERT;
import static mx.org.kaana.kajool.enums.ESql.UPDATE;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticProductosDto;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private Producto producto;	
	private String messageError;
  private Entity articulo;
  private Partida partida;

	public Transaccion(Producto producto) {
		this.producto= producto;		
	}

  public Transaccion(Entity articulo) {
    this.articulo= articulo;
  }
  
  public Transaccion(Entity articulo, Partida partida) {
    this.articulo= articulo;
    this.partida = partida;
  }

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
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
				case SUBIR:
          regresar= this.toSubirProducto(sesion);
					break;
				case BAJAR:
          regresar= this.toBajarProducto(sesion);
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
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("categoria", this.producto.getCategoria());
      this.producto.getProducto().setIdUsuario(JsfBase.getIdUsuario());
      this.producto.getProducto().setIdProductoCategoria(DaoFactory.getInstance().toField("TcManticProductosCategoriasDto", "identico", params, "idKey").toLong());
      Siguiente sigue= this.toSiguiente(sesion);
      this.producto.getProducto().setOrden(sigue.getOrden());
      regresar= DaoFactory.getInstance().insert(sesion, this.producto.getProducto())> 0L;
      if(regresar) {
        this.toArticulos(sesion);
        this.toCaracteristicas(sesion);
      } // if  
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
		finally {
			Methods.clean(params);
		} // finally
    return regresar;
  }
  
  private Boolean toModificarProducto(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("categoria", this.producto.getCategoria());
      this.producto.getProducto().setIdProductoCategoria(DaoFactory.getInstance().toField("TcManticProductosCategoriasDto", "identico", params, "idKey").toLong());
      regresar= DaoFactory.getInstance().update(sesion, this.producto.getProducto())> 0L;
      if(regresar) {
        this.toArticulos(sesion);
        this.toCaracteristicas(sesion);
      } // if  
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
		finally {
			Methods.clean(params);
		} // finally
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
            item.setIdUsuario(JsfBase.getIdUsuario());
            item.setIdDatos(1L);
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
  
  private Boolean toCaracteristicas(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
    try {      
      for (Caracteristica item: this.producto.getCaracteristicas()) {
        switch(item.getAction()) {
          case INSERT:
            item.setIdProducto(this.producto.getProducto().getIdProducto());
            item.setIdUsuario(JsfBase.getIdUsuario());
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
 
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar= null;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("idEmpresa", this.producto.getProducto().getIdEmpresa());
			params.put("categoria", this.producto.getProducto().getIdProductoCategoria());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticProductosDto", "siguiente", params, "siguiente");
			if(next!= null && next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
			else
			  regresar= new Siguiente(1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
 
  public Boolean toSubirProducto(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      params.put("idProducto", this.articulo.toLong("idProducto"));
      params.put("idEmpresa", this.articulo.toLong("idEmpresa"));
      params.put("idProductoCategoria", this.articulo.toLong("idProductoCategoria"));
      if(this.articulo.toLong("orden")> 1L) {
        params.put("valor", this.articulo.toLong("orden"));
        params.put("orden", this.articulo.toLong("orden")- 1L);
        regresar= DaoFactory.getInstance().updateAll(sesion, TcManticProductosDto.class, params, "orden")> 0L;
        params.put("orden", this.articulo.toLong("orden")- 1L);
        regresar= DaoFactory.getInstance().updateAll(sesion, TcManticProductosDto.class, params)> 0L;
      } // if
      else
        regresar= Boolean.TRUE;
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
  }
          
  public Boolean toBajarProducto(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      params.put("idProducto", this.articulo.toLong("idProducto"));
      params.put("idEmpresa", this.articulo.toLong("idEmpresa"));
      Long maximo= this.toMaximo(sesion);
      if(this.articulo.toLong("orden")< maximo) {
        params.put("valor", this.articulo.toLong("orden"));
        params.put("orden", this.articulo.toLong("orden")+ 1L);
        regresar= DaoFactory.getInstance().updateAll(sesion, TcManticProductosDto.class, params, "orden")> 0L;
        params.put("orden", this.articulo.toLong("orden")+ 1L);
        regresar= DaoFactory.getInstance().updateAll(sesion, TcManticProductosDto.class, params)> 0L;
      } // if
      else
        regresar= Boolean.TRUE;
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
  }
 
	private Long toMaximo(Session sesion) throws Exception {
		Long regresar= null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idEmpresa", this.articulo.toLong("idEmpresa"));
			params.put("categoria", this.articulo.toLong("idProductoCategoria"));
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticProductosDto", "maximo", params, "siguiente");
			if(next!= null && next.getData()!= null)
			  regresar= next.toLong();
			else
			  regresar= 0L;
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
  
}