package mx.org.kaana.mantic.compras.ordenes.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.reglas.ArticulosLazyLoad;
import mx.org.kaana.mantic.compras.ordenes.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/06/2018
 *@time 07:51:53 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "manticComprasOrdenesDiferencias")
@ViewScoped
public class Diferencias extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;
	
	private TcManticOrdenesComprasDto orden;
	protected FormatLazyModel lazyNotas;

	public FormatLazyModel getLazyNotas() {
		return lazyNotas;
	}

	public TcManticOrdenesComprasDto getOrden() {
		return orden;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("idOrdenCompra", JsfBase.getFlashAttribute("idOrdenCompra"));
      this.orden= (TcManticOrdenesComprasDto)DaoFactory.getInstance().findById(TcManticOrdenesComprasDto.class, (Long)JsfBase.getFlashAttribute("idOrdenCompra"));
		  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_SIN_DECIMALES));      
      columns.add(new Columna("costo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));      
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			this.attrs.put(Constantes.SQL_CONDICION, " ");
      this.attrs.put("sortOrder", "order by tc_mantic_notas_entradas.consecutivo, tc_mantic_notas_detalles.nombre");
      this.lazyNotas = new ArticulosLazyLoad("VistaOrdenesComprasDto", "consulta", this.attrs, columns);
      columns.add(new Columna("cantidades", EFormatoDinamicos.NUMERO_CON_DECIMALES));      
      columns.add(new Columna("importes", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("porcentaje", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      this.attrs.put("sortOrder", "order by nombre");
      this.lazyModel = new FormatCustomLazy("VistaOrdenesComprasDto", "confronta", this.attrs, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad

	public String doAceptar() {
		Transaccion transaccion= null;
		String regreso         = "filtro".concat(Constantes.REDIRECIONAR);
		try {
			TcManticOrdenesBitacoraDto bitacora= new TcManticOrdenesBitacoraDto(7L, (String)this.attrs.get("justificacion"), JsfBase.getIdUsuario(), this.orden.getIdOrdenCompra(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
			transaccion = new Transaccion(orden, bitacora);
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
    		RequestContext.getCurrentInstance().execute("alert('Se aplicarón las diferencias en la orden de compra.');");
			else {
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus.", ETipoMensaje.ERROR);
				regreso= null;
			} // else	
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		JsfBase.setFlashAttribute("idOrdenCompra", this.attrs.get("idOrdenCompra"));
		return regreso;
	}

	public String doRegresar() {
		JsfBase.setFlashAttribute("idOrdenCompra", this.attrs.get("idOrdenCompra"));
		return "filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doOrdenCompra() {
		JsfBase.setFlashAttribute("idOrdenCompra", this.attrs.get("idOrdenCompra"));
		return "/Paginas/Mantic/Compras/Ordenes/filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doNotaEntrada() {
		JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
		return "/Paginas/Mantic/Inventarios/Entradas/filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doDevolucion() {
		JsfBase.setFlashAttribute("idNotaEntrada", ((Entity)this.attrs.get("filtrado")).getKey());
		return "/Paginas/Mantic/Inventarios/Devoluciones/filtro".concat(Constantes.REDIRECIONAR);
	}	
	
	public String doMovimientos() {
		JsfBase.setFlashAttribute("tipo", ETipoMovimiento.NOTAS_ENTRADAS);
		JsfBase.setFlashAttribute(ETipoMovimiento.NOTAS_ENTRADAS.getIdKey(), ((Entity)this.attrs.get("filtrado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Compras/Ordenes/filtro");
		return "/Paginas/Mantic/Compras/Ordenes/movimientos".concat(Constantes.REDIRECIONAR);
	}

  public void doChangeAplicar(Entity afectado) {
		Entity entity= (Entity)this.attrs.get("seleccionado");
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idAplicar", (boolean)entity.toBoolean("afectar")? 1L: 2L);
			DaoFactory.getInstance().update(TcManticNotasDetallesDto.class, entity.getKey(), params);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}	
	
	public void doRowSelectEvent() {
		Entity entity= (Entity)this.attrs.get("seleccionado");
    List<Columna> columns     = null;
	  Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_SIN_DECIMALES));      
      columns.add(new Columna("costo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));      
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			params.put("idOrdenCompra", entity.toLong("idOrdenCompra"));
			params.put(Constantes.SQL_CONDICION, " and tc_mantic_notas_detalles.id_articulo= "+ entity.toLong("idArticulo"));
      params.put("sortOrder", "order by tc_mantic_notas_entradas.consecutivo, tc_mantic_notas_detalles.nombre");
      this.lazyNotas = new ArticulosLazyLoad("VistaOrdenesComprasDto", "consulta", params, columns);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally	
	}
	
	public String toColor(Entity row) {
		return !row.toDouble("unidades").equals(0D) || !row.toDouble("valores").equals(0D)? "janal-tr-orange": "";
	} 
	
}
