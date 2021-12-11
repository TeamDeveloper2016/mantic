package mx.org.kaana.mantic.catalogos.articulos.reglas;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.ArticuloImagen;
import mx.org.kaana.mantic.catalogos.articulos.beans.CodigoArticulo;
import mx.org.kaana.mantic.catalogos.articulos.beans.Imagen;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosImagenesDto;
import mx.org.kaana.mantic.db.dto.TcManticImagenesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public final class Replicar extends IBaseTnx implements Serializable {
	
	private static final Log LOG=LogFactory.getLog(Replicar.class);
	private static final long serialVersionUID=8680186650062307089L;

	private final Entity articulo;
	private final List<CodigoArticulo> articulos;
  private final List<Imagen> delete;
	private String messageError;

	public Replicar(Entity articulo, List<CodigoArticulo> articulos) {
		this.articulo = articulo;
		this.articulos= articulos;
    this.delete   = new ArrayList<>();
	} // Replicar

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
		this.messageError= "Ocurrio un error al replicar las imagenes, verifiquelo por favor !";
		try {
      this.delete.clear();
      for (CodigoArticulo item: this.articulos) {
				TcManticArticulosDto reference= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
				if(reference!= null) 
          regresar= this.toDeleteImagen(sesion, reference, this.articulo.toLong("idImagen"));
				else
					LOG.error("EL ARTICULO ["+ item.getIdArticulo()+ "] NO EXISTE EN EL CATALOGO, FAVOR DE VERIFICAR !");
			} // for
      this.deleteSource(sesion);
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch	
		return regresar;
	} // ejecutar
	
  private boolean toDeleteImagen(Session sesion, TcManticArticulosDto reference, Long idImagen) throws Exception {
    return this.toDeleteImagen(sesion, -1L, String.valueOf(reference.getIdArticulo()), reference, idImagen); 
  }
  
  public boolean toDeleteImagen(Session sesion, Long idProducto, String idArticulo, TcManticArticulosDto reference, Long idImagen) throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
    List<ArticuloImagen> items= null;
    try {      
      params= new HashMap<>();      
      params.put("idArticulo", reference.getIdArticulo()); 
      items= (List<ArticuloImagen>)DaoFactory.getInstance().toEntitySet(sesion, ArticuloImagen.class, "TcManticArticulosImagenesDto", "imagenes", params);
  		if(items!= null && !items.isEmpty()) {
        for (ArticuloImagen item: items) {
          if(Objects.equals(item.getIdPrincipal(), 1L)) {
            this.delete.add(new Imagen(item.getIdImagen(), idProducto, idArticulo, item.getOriginal()));
            item.setIdImagen(idImagen);
            item.setCosto(reference.getPrecio());
            item.setMenudeo(reference.getMenudeo());
            DaoFactory.getInstance().update(sesion, item);
          } // if  
        } // for
      } // if
      else {
        TcManticArticulosImagenesDto item= new TcManticArticulosImagenesDto(
          -1L, // Long idArticuloImagen, 
          reference.getPrecio(), // Double costo, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          idImagen, // Long idImagen, 
          1L, // Long idPrincipal, 
          reference.getMenudeo(), // Double menudeo, 
          1L, // Long orden, 
          reference.getIdArticulo(), // Long idArticulo, 
          "MODELO_1", // String nombre, 
          "MODELO_1" // String modelo
        );
        DaoFactory.getInstance().insert(sesion, item);
      } // else
      reference.setIdImagen(idImagen);
      DaoFactory.getInstance().update(sesion, reference);
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(items);
    } // finally
    return regresar;
  }
  
	private boolean deleteImagen(Session sesion, Long idProducto, String idArticulo, Long idImagen) throws Exception {
		boolean regresar         = Boolean.FALSE;
		List<Entity> items       = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idArticulo", idArticulo);
			params.put("idProducto", idProducto);
			params.put("idImagen", idImagen);
			items= DaoFactory.getInstance().toEntitySet(sesion, "TcManticProductosDto", "findImage", params);
			regresar = items!=null && (items.isEmpty() || items.size()<= 1);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // deleteImagen 	

  public Boolean deleteSource(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
		Map<String, Object>params= null;
		try {
      sesion.flush();
			params= new HashMap<>();
      for (Imagen item: this.delete) {
        if(this.deleteImagen(sesion, item.getIdProducto(), item.getArticulos(), item.getIdImage()))  {
          params.put("idImagen", item.getIdImage()); 
          DaoFactory.getInstance().delete(sesion, new TcManticImagenesDto(item.getIdImage()));
          File file= new File(item.getOriginal());
          if(file.exists())
            file.delete();
        } // if  
      } // for
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

}
