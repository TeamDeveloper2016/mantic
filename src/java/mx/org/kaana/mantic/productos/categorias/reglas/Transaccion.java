package mx.org.kaana.mantic.productos.categorias.reglas;

import com.google.common.base.Objects;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticProductosCategoriasDto;
import mx.org.kaana.mantic.productos.beans.Categoria;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private Categoria categoria;	
	private String messageError;

	public Transaccion(Categoria categoria) {
		this.categoria= categoria;		
	}

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el registro del categoría");
			switch(accion){
				case AGREGAR:
          regresar= this.toAgregarCategoria(sesion);
					break;
				case MODIFICAR:
          regresar= this.toModificarCategoria(sesion);
					break;				
				case ELIMINAR:
          regresar= this.toEliminarCategoria(sesion);
					break;
				case SUBIR:
          regresar= this.toSubirCategoria(sesion);
					break;
				case BAJAR:
          regresar= this.toBajarCategoria(sesion);
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
	
  private Boolean toAgregarCategoria(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    Categoria clon  = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      String[] categorias= this.categoria.getNombre().split("["+ this.categoria.getSeparador()+ "]");
      for (String item: categorias) {
        if(item.trim().length()> 0) {
          params.put("padre", this.categoria.getPadre());
          params.put("nivel", this.categoria.getNivel());
          params.put("nombre", item);
          TcManticProductosCategoriasDto igual= (TcManticProductosCategoriasDto)DaoFactory.getInstance().toEntity(sesion, TcManticProductosCategoriasDto.class, "TcManticProductosCategoriasDto", "igual", params);
          if(igual== null && item.trim().length()> 0) {
            Siguiente sigue= this.toSiguiente(sesion);
            this.categoria.setNombre(item.trim().toUpperCase());
            this.categoria.setOrden(sigue.getOrden());
            regresar= DaoFactory.getInstance().insert(sesion, this.categoria)> 0L;
            if(regresar) {
              TcManticProductosCategoriasDto padre= (TcManticProductosCategoriasDto)DaoFactory.getInstance().findById(sesion, TcManticProductosCategoriasDto.class, this.categoria.getIdPadre());
              if(padre!= null) {
                padre.setUltimo(2L);
                DaoFactory.getInstance().update(sesion, this.categoria);
              } // if
            } // if  
          } // if  
          if(Objects.equal(this.categoria.getSeparador(), Constantes.SEPARADOR)) {
            clon= this.categoria.clone();
            clon.setPadre(this.categoria.getPadre().length()== 0? this.categoria.getPadre(): this.categoria.getPadre().concat(item).concat(Constantes.SEPARADOR));
            clon.setNivel(this.categoria.getNivel()+ 1L);
            clon.setIdPadre(igual!= null? igual.getIdProductoCategoria(): this.categoria.getIdProductoCategoria());
          } // if
          else {
            clon= this.categoria.clone();
          } // if  
          this.categoria= clon;
        } // if  
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private Boolean toModificarCategoria(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      params.put("padre", this.categoria.getPadre().concat(this.categoria.getTexto()));
      params.put("nivel", this.categoria.getNivel());
      regresar= DaoFactory.getInstance().update(sesion, this.categoria)> 0L;
      if(!Objects.equal(this.categoria.getValor(), this.categoria.getPorcentaje()) || !Objects.equal(this.categoria.getTexto(), this.categoria.getNombre())) {
        List<TcManticProductosCategoriasDto> items= (List<TcManticProductosCategoriasDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticProductosCategoriasDto.class, "TcManticProductosCategoriasDto", "porcentajes", params);
        if(items!= null && !items.isEmpty()) {
          for (TcManticProductosCategoriasDto item: items) {
            if(!Objects.equal(this.categoria.getTexto(), this.categoria.getNombre()))
              item.setPadre(item.getPadre().replace(Constantes.SEPARADOR.concat(this.categoria.getTexto()).concat(Constantes.SEPARADOR), Constantes.SEPARADOR.concat(this.categoria.getNombre()).concat(Constantes.SEPARADOR)));
            if(!Objects.equal(this.categoria.getValor(), this.categoria.getPorcentaje()))
              item.setPorcentaje(this.categoria.getPorcentaje());
            DaoFactory.getInstance().update(sesion, item);
          } // for
        } // if
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
  
  private Boolean toEliminarCategoria(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      regresar= DaoFactory.getInstance().delete(sesion, this.categoria)> 0L;
      if(regresar) {
        TcManticProductosCategoriasDto padre= (TcManticProductosCategoriasDto)DaoFactory.getInstance().findById(sesion, TcManticProductosCategoriasDto.class, this.categoria.getIdPadre());
        if(padre!= null) {
          params.put("padre", padre.getPadre());
          params.put("nivel", padre.getNivel());
          List<UISelectEntity> items= (List<UISelectEntity>)UIEntity.build("TcManticProductosCategoriasDto", "hijos", params, Collections.EMPTY_LIST);
          if(items== null || items.isEmpty()) {
            padre.setUltimo(1L);
            DaoFactory.getInstance().update(sesion, padre);
          } // if  
        } // if
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
  
  private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar= null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      params.put("padre", this.categoria.getPadre());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticProductosCategoriasDto", "siguiente", params, "siguiente");
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
 
  public Boolean toSubirCategoria(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      params.put("idProducto", this.categoria.getPadre());
//      params.put("idEmpresa", this.articulo.toLong("idEmpresa"));
//      if(this.partida.getOrden()> 1L) {
//        params.put("valor", this.partida.getOrden()+ 1L);
//        params.put("orden", this.partida.getOrden()- 1L);
//        regresar= DaoFactory.getInstance().updateAll(sesion, TcManticProductosDto.class, params, "orden")> 0L;
//        params.put("orden", this.partida.getOrden()- 1L);
//        regresar= DaoFactory.getInstance().updateAll(sesion, TcManticProductosDto.class, params)> 0L;
//      } // if
//      else
//        regresar= Boolean.TRUE;
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
  }
          
  public Boolean toBajarCategoria(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      params.put("idProducto", this.categoria.getPadre());
//      Long maximo= this.toMaximo(sesion);
//      if(this.partida.getOrden()< maximo) {
//        params.put("valor", this.partida.getOrden()- 1L);
//        params.put("orden", this.partida.getOrden()+ 1L);
//        regresar= DaoFactory.getInstance().updateAll(sesion, TcManticProductosDto.class, params, "orden")> 0L;
//        params.put("orden", this.partida.getOrden()+ 1L);
//        regresar= DaoFactory.getInstance().updateAll(sesion, TcManticProductosDto.class, params)> 0L;
//      } // if
//      else
//        regresar= Boolean.TRUE;
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
			params.put("padre", this.categoria.getPadre());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticProductosCategoriasDto", "maximo", params, "siguiente");
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