package mx.org.kaana.mantic.catalogos.empresas.saldar.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.saldar.beans.Egreso;
import mx.org.kaana.mantic.inventarios.entradas.reglas.Importados;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/05/2018
 *@time 02:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticCatalogosEmpresasSaldarAsociar") 
@ViewScoped
public class Asociar extends IBaseFilter implements Serializable {

	private static final Log LOG=LogFactory.getLog(Asociar.class);
	private static final long serialVersionUID=-6770709196941718368L;

	private Long idNotaEntrada;
	private Entity orden;
	private Entity proveedor;
	private List<Egreso> articulos;
  
	public Entity getOrden() {
		return orden;
	}

  public Entity getProveedor() {
    return proveedor;
  }

	public List<Egreso> getArticulos() {
		return articulos;
	}

	@Override
	@PostConstruct
	protected void init() {
    List<Columna> columns = null;    
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      columns = new ArrayList<>();
      columns.add(new Columna("total", EFormatoDinamicos.MILES_CON_DECIMALES));
      if(JsfBase.getFlashAttribute("idNotaEntrada")== null)
      	UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("codigo", "");
      this.attrs.put("buscaPorFecha", false);
      this.idNotaEntrada= JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: (Long)JsfBase.getFlashAttribute("idNotaEntrada");
      params.put("idNotaEntrada", this.idNotaEntrada);      
      this.orden= (Entity)DaoFactory.getInstance().toEntity("TcManticNotasEntradasDto", "detalle", params);
      if(this.orden!= null) {
        UIBackingUtilities.toFormatEntity(this.orden, columns);
        params.put(Constantes.SQL_CONDICION, "id_proveedor= "+ this.orden.toLong("idProveedor"));      
        this.proveedor= (Entity)DaoFactory.getInstance().toEntity("TcManticProveedoresDto", "row", params);
      } // if
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Mantic/Catalogos/Empresas/Saldar/filtro": JsfBase.getFlashAttribute("retorno"));
      this.toLoadPartidas();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	}
	
  @Override
	public void doLoad() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorFecha    = false;
    try {
      this.attrs.put("existe", null);
			columns= new ArrayList<>();
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("dia", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("cantidad", EFormatoDinamicos.MILES_CON_DECIMALES));
			String search= new String((String)this.attrs.get("codigo")); 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorFecha= search.startsWith(".");
				if(buscaPorFecha)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
      params.put("sortOrder", "order by registro desc");
  		params.put("codigo", search);
			if((boolean)this.attrs.get("buscaPorFecha") || buscaPorFecha)
        this.lazyModel= new FormatCustomLazy("TcManticEgresosDto", "porFecha", params, columns);
			else
        this.lazyModel= new FormatCustomLazy("TcManticEgresosDto", "porNombre", params, columns);
      UIBackingUtilities.resetDataTable("encontrados");
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally    
	}
	
  private void toLoadPartidas() {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("sortOrder", "order by tc_mantic_egresos_notas.registro desc");      
      params.put("idNotaEntrada", this.idNotaEntrada);      
      this.articulos= (List<Egreso>)DaoFactory.getInstance().toEntitySet(Egreso.class, "VistaEgresosDto", "notas", params);
      if(this.articulos== null)
        this.articulos= new ArrayList<>();
      else
        for (Egreso item: this.articulos) {
          item.setAccion(ESql.SELECT);
        } // for
      this.attrs.put("total", this.articulos.size());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
	private void updateArticulo(Entity articulo) throws Exception {
    Map<String, Object> params= new HashMap<>();
		try {
      int index= this.articulos.indexOf(new Egreso(articulo.toLong("idEgreso")));
      if(index< 0) {
        Egreso item= new Egreso(
          articulo.toLong("idEgreso"), 
          this.orden.toLong("idNotaEntrada"), 
          articulo.toString("consecutivo"),
          articulo.toString("descripcion"),
          articulo.toDate("fecha"), 
          articulo.toDouble("importe")
        );
        this.articulos.add(item);
      } // if
      else {
        if(Objects.equals(this.articulos.get(index).getAccion(), ESql.SELECT))
          this.attrs.put("existe", "<span class='janal-color-orange'>LA PARTIDA YA ESTA EN LA LISTA DE PARTIDAS</span>");
        this.articulos.get(index).setAccion(ESql.SELECT);
      }	// else 
			this.attrs.put("total", this.articulos.size());
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally	
	}
	
	public String doAceptar() {
		String regresar       = null;
		Importados transaccion= null;
    try {		
			transaccion= new Importados(this.articulos);
			if(transaccion.ejecutar(EAccion.PROCESAR)) {
			  JsfBase.addMessage("Se agregaron las partidas de forma correcta !", ETipoMensaje.INFORMACION);
  			this.attrs.put("total", this.articulos.size());
			} // if	
			else 
				JsfBase.addMessage("Ocurrió un error al agregar las partidas", ETipoMensaje.ALERTA);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
		return regresar;
	}
	
	public void doAgregar(Entity row) {
    try {
      this.attrs.put("existe", null);
      this.updateArticulo(row);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void doEliminar(Egreso row) {
    try {
      this.attrs.put("existe", null);
      if(Objects.equals(row.getAccion(), ESql.INSERT)) 
        this.articulos.remove(row);
      else
        row.setAccion(ESql.DELETE);
      this.attrs.put("total", this.articulos.size());
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public String doCancelar() {   
  	JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

  public String toColor(Egreso row) {
    return Objects.equals(row.getAccion(), ESql.DELETE)? "janal-display-none": "";
  }
  
}
