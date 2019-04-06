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
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.compras.ordenes.reglas.ArticulosLazyLoad;
import mx.org.kaana.mantic.compras.ordenes.reglas.Transaccion;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import mx.org.kaana.mantic.libs.pagina.IFilterImportar;
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
public class Diferencias extends IFilterImportar implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;
  private Reporte reporte;
	
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
      this.attrs.put("idProveedor", JsfBase.getFlashAttribute("idProveedor"));
      this.attrs.put("idAlmacen", JsfBase.getFlashAttribute("idAlmacen"));
      this.attrs.put("tipoDiferencia", 0);
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
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));      
      columns.add(new Columna("precio", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      columns.add(new Columna("costoCalculado", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("cantidades", EFormatoDinamicos.NUMERO_CON_DECIMALES));      
      columns.add(new Columna("importes", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("porcentaje", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      this.attrs.put("sortOrder", "order by nombre");
			switch((Integer)this.attrs.get("tipoDiferencia")) {
				case 0: // TODOS
					this.attrs.put("seleccionado", null);
					this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
					break;
				case 1: // DIFERENCIA POR PRECIO
					this.attrs.put(Constantes.SQL_CONDICION, " tc_mantic_ordenes_detalles.importes!= 0 and tc_mantic_ordenes_detalles.cantidad!= tc_mantic_ordenes_detalles.cantidades");
					break;
				case 2: // DIFERENCIA POR CANTIDAD
					this.attrs.put(Constantes.SQL_CONDICION, " tc_mantic_ordenes_detalles.cantidades!= 0 and tc_mantic_ordenes_detalles.cantidad!= tc_mantic_ordenes_detalles.cantidades");
					break;
				case 3: // PARTIDAS NO SOLICITADAS
					this.attrs.put("seleccionado", null);
					this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_FALSO);
					break;
				case 4: // PARTIDAS NO SURTIDAS
					this.attrs.put("seleccionado", null);
					this.attrs.put(Constantes.SQL_CONDICION, " tc_mantic_ordenes_detalles.cantidad= tc_mantic_ordenes_detalles.cantidades");
					break;
			} // switch
      this.lazyModel = new FormatCustomLazy("VistaOrdenesComprasDto", "confronta", this.attrs, columns);
      UIBackingUtilities.resetDataTable();
			this.attrs.put("seleccionado", null);
			this.doRowSelectEvent();
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
			transaccion = new Transaccion(this.orden, bitacora);
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
    		UIBackingUtilities.execute("alert('Se aplicarón las diferencias en la orden de compra.');");
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
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idAplicar", (boolean)afectado.toBoolean("afectar")? 1L: 2L);
			DaoFactory.getInstance().update(TcManticNotasDetallesDto.class, afectado.getKey(), params);
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
		Entity seleccionado= (Entity)this.attrs.get("seleccionado");
    List<Columna> columns     = null;
	  Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));      
      columns.add(new Columna("costoCalculado", EFormatoDinamicos.NUMERO_SAT_DECIMALES));      
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("porcentaje", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
		  params.put(Constantes.SQL_CONDICION, " ");
			params.put("idOrdenCompra", this.attrs.get("idOrdenCompra"));
			if(seleccionado!= null && ((Integer)this.attrs.get("tipoDiferencia")== 1 || (Integer)this.attrs.get("tipoDiferencia")== 2)) 
				params.put(Constantes.SQL_CONDICION, " and tc_mantic_notas_detalles.id_articulo= "+ seleccionado.toLong("idArticulo"));
			else
				if((Integer)this.attrs.get("tipoDiferencia")!= 0 && (Integer)this.attrs.get("tipoDiferencia")!= 3)
  			  params.put("idOrdenCompra", -1L);
			if((Integer)this.attrs.get("tipoDiferencia")== 3)
  		  params.put(Constantes.SQL_CONDICION, " and tc_mantic_notas_detalles.id_orden_detalle is null ");
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
  
  public void doReporte(String nombre) throws Exception{
		Parametros comunes = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
		try{		
      reporteSeleccion= EReportes.valueOf(nombre);
      if(!reporteSeleccion.equals(EReportes.ORDENES_COMPRA)){
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), Long.valueOf(this.attrs.get("idAlmacen").toString()), Long.valueOf(this.attrs.get("idProveedor").toString()), -1L);
      }
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, this.attrs, parametros));		
      doVerificarReporte();
      this.reporte.doAceptar();			
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
} // doReporte
	
	public void doVerificarReporte() {
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L)
			rc.execute("start(" + this.reporte.getTotal() + ")");		
		else{
			rc.execute("generalHide()");		
			JsfBase.addMessage("Generar reporte","No se encontraron registros para el reporte", ETipoMensaje.ALERTA);
		} // else
	} // doVerificarReporte		
	
	public String doOrdenColor(Entity row) {
		return !row.toDouble("unidades").equals(0D) || !row.toDouble("valores").equals(0D)? "janal-tr-diferencias": "";
	} 
	
	public String doNotaColor(Entity row) {
		return row.toString("nuevo").equals("*")? row.toDouble("diferencia")!= 0? "janal-tr-error": "janal-tr-nuevo": "";
	} 

  public void doChangeArticulos() {
		this.doLoad();
	}	

	public String doCostos(Entity row) {
		String regresar= "<i class='fa fa-fw fa-question-circle janal-color-green' style='float:right;' title='\n\nCosto: "+ Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, row.toDouble("costo"))+ 
			"\nCosto real: $"+ Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, row.toDouble("costoReal"))+ 
			"\nCosto calculado: $" + row.toString("costoCalculado")+ 
			"\n\nDescuento: $" + row.toString("descuento")+ " %"+
			"\nExtras: " + row.toString("extras")+  " %"+
			"'\n\n'></i>";
		return regresar;
	}

  public void doFaltanteArticulo() {
		try {
			Entity faltante= (Entity)this.attrs.get("faltante");
			if(faltante!= null) {
				TcManticFaltantesDto existe= (TcManticFaltantesDto)DaoFactory.getInstance().findFirst(TcManticFaltantesDto.class, "existe", faltante.toMap());
				if(existe== null) {
					existe= new TcManticFaltantesDto(JsfBase.getIdUsuario(), -1L, "", faltante.toDouble("solicitar"), 1L, faltante.toLong("idArticulo"), faltante.toLong("idEmpresa"));
					if(DaoFactory.getInstance().insert(existe)> 0L)
						JsfBase.addMessage("Agregado:", "El articulo fue agregado a la relación de faltantes. !", ETipoMensaje.INFORMACION);
				}	// if
				else {
					existe.setCantidad(existe.getCantidad()+ faltante.toDouble("solicitar"));
					if(DaoFactory.getInstance().update(existe)> 0L) 
						JsfBase.addMessage("Agregado:", "El articulo fue actualizado en la relación de faltantes. !", ETipoMensaje.INFORMACION);
				} // else	
			} // if	
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	

  public void doViewObservaciones(Entity row) {
    this.attrs.put("seleccionado", row);		
	}
	
  public String doViewPorcentajes(Entity row) {
		StringBuilder regresar= new StringBuilder();
		try {
			List<Entity> items= DaoFactory.getInstance().toEntitySet("VistaOrdenesComprasDto", "porcentajes", row);
			if(items!= null && !items.isEmpty())
			  for (Entity item: items) {
					regresar.append("Nota entrada: ").append(item.toString("consecutivo")).append("<br/>Porcentaje: ").append(this.doDecimalSat(item.toDouble("porcentaje"))).append(" %<br/>Observaciones: ").append(item.toString("observaciones")).append("<br/><br/>");
				} // for
			else
				regresar.append("No tiene ningúna nota de entrada asociada !            ");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar.substring(0, regresar.length()- 10);
	}
	
}
