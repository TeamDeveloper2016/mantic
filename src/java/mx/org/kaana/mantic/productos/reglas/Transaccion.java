package mx.org.kaana.mantic.productos.reglas;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import static mx.org.kaana.kajool.enums.ESql.DELETE;
import static mx.org.kaana.kajool.enums.ESql.INSERT;
import static mx.org.kaana.kajool.enums.ESql.UPDATE;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.ArticuloImagen;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Replicar;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticImagenesDto;
import mx.org.kaana.mantic.db.dto.TcManticProductosDto;
import mx.org.kaana.mantic.enums.ETipoImagen;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private static final Log LOG = LogFactory.getLog(Transaccion.class);
  
	private Producto producto;	
	private String messageError;
  private Entity articulo;
  private Partida partida;
  private ArticuloImagen imagen;

	public Transaccion(Producto producto, ArticuloImagen imagen) {
		this.producto= producto;		
    this.imagen  = imagen;
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
			switch(accion) {
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
    Boolean regresar          = Boolean.FALSE;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("categoria", this.producto.getProducto().getCategoria());
      this.producto.getProducto().setIdUsuario(JsfBase.getIdUsuario());
      Value value= DaoFactory.getInstance().toField("TcManticProductosCategoriasDto", "identico", params, "idKey");
      this.producto.getProducto().setIdProductoCategoria(value!= null && value.getData()!= null? value.toLong(): -1L);
      Siguiente sigue= this.toSiguiente(sesion);
      this.producto.getProducto().setOrden(sigue.getOrden());
      regresar= DaoFactory.getInstance().insert(sesion, this.producto.getProducto())> 0L;
      LOG.error("SE INSERTO EL PRODUCTO "+ params);
      if(regresar) {
        this.toArticulos(sesion);
        this.toCaracteristicas(sesion);
        this.checkImage(sesion);
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
    Boolean regresar          = Boolean.FALSE;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("categoria", this.producto.getProducto().getCategoria());
      Long idProductoCategoria= -1L;
      Value value= DaoFactory.getInstance().toField("TcManticProductosCategoriasDto", "identico", params, "idKey");
      if(value!= null && value.getData()!= null)
        idProductoCategoria= value.toLong();
      if(!Objects.equals(this.producto.getProducto().getIdProductoCategoria(), idProductoCategoria)) {
        // RENUMERAR LOS HIJOS DEL LA CATEGORIA Y CALCULAR EL MAXIMO DE LA NUEVA CATEGORIA
        params.put("idProductoCategoria", this.producto.getProducto().getIdProductoCategoria());
        List<TcManticProductosDto> items= (List<TcManticProductosDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticProductosDto.class, "TcManticProductosDto", "hijos", params);
        int count= 1;
        for (TcManticProductosDto item: items) {
          if(!Objects.equals(item.getIdProducto(), this.producto.getProducto().getIdProducto())) {
            item.setOrden(new Long(count++));
            DaoFactory.getInstance().update(sesion, item);
          } // if  
        } // for
        this.producto.getProducto().setIdProductoCategoria(idProductoCategoria);
        Siguiente sigue= this.toSiguiente(sesion);
        this.producto.getProducto().setOrden(sigue.getOrden());
      } // if
      regresar= DaoFactory.getInstance().update(sesion, this.producto.getProducto())> 0L;
      if(regresar) {
        this.toArticulos(sesion);
        this.toCaracteristicas(sesion);
        this.checkImage(sesion);
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
          case UPDATE:
            regresar= DaoFactory.getInstance().update(sesion, item)> 0L;
            break;
          case INSERT:
            item.setIdProducto(this.producto.getProducto().getIdProducto());
            item.setIdUsuario(JsfBase.getIdUsuario());
            item.setIdDatos(1L);
            regresar= DaoFactory.getInstance().insert(sesion, item)> 0L;
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
          case UPDATE:
            regresar= DaoFactory.getInstance().update(sesion, item)> 0L;
            break;
          case INSERT:
            item.setIdProducto(this.producto.getProducto().getIdProducto());
            item.setIdUsuario(JsfBase.getIdUsuario());
            regresar= DaoFactory.getInstance().insert(sesion, item)> 0L;
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
 
  public Boolean checkImage(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      StringBuilder sb= new StringBuilder();
      if(this.imagen!=null && this.producto.getArticulos()!= null && !this.producto.getArticulos().isEmpty()) {
        Long idArticulo= -1L;
        for (Partida item: this.producto.getArticulos()) {
          if(!Objects.equals(item.getAction(), ESql.DELETE))
            idArticulo= item.getIdArticulo();
          sb.append(item.getIdArticulo()).append(", ");
        } // for  
        sb.delete(sb.length()- 2, sb.length());
        if(!Objects.equals(idArticulo, -1L)) {
          if(this.imagen.getImportado().getId()< 0) {
            String name= this.imagen.getImportado().getName();
            String file= Archivo.toFormatNameFile(idArticulo.toString().concat(".").concat(name.substring(name.lastIndexOf(".")+ 1, name.length())), "IMG");
            Long tipo  = ETipoImagen.valueOf(name.substring(name.lastIndexOf(".")+ 1, name.length()).toUpperCase()).getIdTipoImagen();
            TcManticImagenesDto foto= new TcManticImagenesDto(
              file, // String archivo, 
              this.imagen.getImportado().getRuta(), // String ruta, 
              this.imagen.getImportado().getFileSize(), // Long tamanio, 
              JsfBase.getIdUsuario(), // Long idUsuario, 
              -1L, // Long idImagen, 
              tipo, // Long idTipoImagen, 
              name, // String nombre, 
              this.imagen.getImportado().getRuta().concat(file) // String alias
            ); 
            this.imagen.setIdImagen(DaoFactory.getInstance().insert(sesion, foto));      
            File archivo= new File(foto.getRuta().concat(foto.getNombre()));			
            if(archivo.exists()) {
              Archivo.copy(foto.getRuta().concat(foto.getNombre()), foto.getRuta().concat(foto.getArchivo()), true);												
              archivo.delete();
            } // if          
            this.producto.getProducto().setIdImagen(this.imagen.getIdImagen());
            DaoFactory.getInstance().update(sesion, this.producto.getProducto());
          } // if
          else
            this.imagen.setIdImagen(this.imagen.getImportado().getId());
          Replicar replicar= new Replicar(new Entity(idArticulo), Collections.EMPTY_LIST);
          for (Partida item: this.producto.getArticulos()) {
            TcManticArticulosDto reference= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
            if(reference!= null) {
              if(!Objects.equals(reference.getIdImagen(), this.imagen.getIdImagen()))
                regresar= replicar.toDeleteImagen(sesion, this.producto.getProducto().getIdProducto(), sb.toString(), reference, this.imagen.getIdImagen());
            } // if  
            else
              LOG.error("EL ARTICULO ["+ item.getIdArticulo()+ "] NO EXISTE EN EL CATALOGO, FAVOR DE VERIFICAR !");
          } // for
          replicar.deleteSource(sesion);
          this.toCheckDeleteFile(sesion, this.imagen.getImportado().getName());
        } // if
      } // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
    return regresar;
  }

}