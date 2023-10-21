package mx.org.kaana.mantic.consultas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoDocumento;
import mx.org.kaana.mantic.enums.ETipoMediosPago;

@Named(value= "manticConsultasDiarias")
@ViewScoped
public class Diarias extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
	private FormatLazyModel detalle;
	private FormatLazyModel lazyCredito;
	private FormatLazyModel lazyApartado;
	private FormatLazyModel lazyDisponible;

	private Reporte reporte;
	
	public Reporte getReporte() {
		return reporte;
	}	

	public FormatLazyModel getDetalle() {
		return detalle;
	}	

	public FormatLazyModel getLazyCredito() {
		return lazyCredito;
	}

	public FormatLazyModel getLazyApartado() {
		return lazyApartado;
	}
	
  public FormatLazyModel getLazyDisponible() {
    return lazyDisponible;
  }
  
	public String getCredito() {
		Double sum= 0D;
		if(this.lazyCredito!= null)
			for (IBaseDto item: (List<IBaseDto>)this.lazyCredito.getWrappedData()) {
				Entity row= (Entity)item;
				sum+= row.toDouble("total");
			} // for
	  return Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, sum);
	}
	
	public String getApartado() {
		Double sum= 0D;
		if(this.lazyApartado!= null)
			for (IBaseDto item: (List<IBaseDto>)this.lazyApartado.getWrappedData()) {
				Entity row= (Entity)item;
				sum+= row.toDouble("importe");
			} // for
	  return Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, sum);
	}

	public String getDisponible() {
		Double sum= 0D;
		if(this.lazyDisponible!= null)
			for (IBaseDto item: (List<IBaseDto>)this.lazyDisponible.getWrappedData()) {
				Entity row= (Entity)item;
				sum+= row.toDouble("total");
			} // for
	  return Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, sum);
	}
  
  @PostConstruct
  @Override
  protected void init() {
		Entity total= null;
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", new UISelectEntity(JsfBase.getAutentifica().getEmpresa().getIdEmpresa()));
      this.attrs.put("sortOrder", "order by tc_mantic_ventas.registro desc");
			this.toLoadCatalog();      
			total= new Entity();
			total.put("total", new Value("total", 0L));
			this.attrs.put("total", total);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns       = new ArrayList<>();
		Map<String, Object> params  = this.toPrepare();
		Map<String, Object> credito = null;
		Map<String, Object> apartado= null;
    try {
      columns.add(new Columna("nombreEmpresa", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      this.lazyModel= new FormatCustomLazy("VistaVentasDiariasDto", "ventas", params, columns);
			Entity total  = (Entity)DaoFactory.getInstance().toEntity("VistaVentasDiariasDto", "diariasTotales", params); 
      this.attrs.put("total", total);
      UIBackingUtilities.resetDataTable();
			columns.remove(columns.size()- 1);
			apartado= this.toPrepare(EEstatusVentas.APARTADOS);
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));      
      this.lazyApartado= new FormatCustomLazy("VistaVentasDiariasDto", "apartado", apartado, columns);
			credito= this.toPrepare(EEstatusVentas.CREDITO);
      columns.add(new Columna("devuelto", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("cuando", EFormatoDinamicos.FECHA_HORA_CORTA));      
      this.lazyCredito = new FormatCustomLazy("VistaVentasDiariasDto", "credito", credito, columns);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(credito);
      Methods.clean(apartado);
      Methods.clean(columns);
    } // finally		
  } 

	protected Map<String, Object> toPrepare() {
	  return this.toPrepare(EEstatusVentas.TIMBRADA);
	}
	
	protected Map<String, Object> toPrepare(EEstatusVentas consulta) {
	  Map<String, Object> regresar= new HashMap<>();	
	  Map<String, Object> params  = new HashMap<>();	
		StringBuilder sb= new StringBuilder();		
		StringBuilder sf= new StringBuilder();		
    try {
      sb.append("tc_mantic_ventas.id_tipo_documento=").append(ETipoDocumento.VENTAS_NORMALES.getIdTipoDocumento()).append(" and ");
      switch(consulta) {
        case CREDITO:
          sb.append("tc_mantic_ventas.id_venta_estatus in (").append(EEstatusVentas.CREDITO.getIdEstatusVenta()).append(") and ");
          break;
        case APARTADOS:
          sb.append("tc_mantic_ventas.id_venta_estatus in (").append(EEstatusVentas.APARTADOS.getIdEstatusVenta()).append(") and ");
          break;
        default:
          sb.append("tc_mantic_ventas.id_venta_estatus in (").append(EEstatusVentas.PAGADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.TIMBRADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.TERMINADA.getIdEstatusVenta()).append(") and ");
          break;
      } // switch
      if(!Objects.equals(((UISelectEntity)this.attrs.get("idEmpresa")).getKey(), -1L))
        regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
      else
        regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
      if(!Cadena.isVacio(this.attrs.get("idMedioPago")) && !this.attrs.get("idMedioPago").toString().equals("-1"))
        sf.append("(tc_mantic_tipos_medios_pagos.id_tipo_medio_pago= ").append(this.attrs.get("idMedioPago")).append(") and ");
      if(sf.length()== 0)
        regresar.put("apartado", Constantes.SQL_VERDADERO);
      else	
        regresar.put("apartado", sf.substring(0, sf.length()- 4));
      regresar.put("idCierre", ((UISelectEntity)this.attrs.get("idCierre")).getKey());
      if(sb.length()== 0)
        regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      else	
        regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
    } // try  
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
    } // finally
    return regresar;		
	} // toPrepare
	
	protected void toLoadCatalog() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresa")).getKey());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));			
			this.attrs.put("idEmpresa", this.toDefaultSucursal((List<UISelectEntity>)this.attrs.get("sucursales")));
			this.doLoadCajas();
			this.doLoadMediosPagos();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	} // toLoadCatalog
	
	public void doLoadCajas() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresa")).getKey());
      List<UISelectEntity> cajas= (List<UISelectEntity>)UIEntity.build("TcManticCajasDto", "cajas", params, columns);
      this.attrs.put("cajas", cajas);
			this.attrs.put("idCaja", UIBackingUtilities.toFirstKeySelectEntity(cajas));
      this.doLoadCierres();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} 
	
	public void doLoadCierres() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("consecutivo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			params.put("idCaja", ((UISelectEntity)this.attrs.get("idCaja")).getKey());
      List<UISelectEntity> cierres= (List<UISelectEntity>)UIEntity.build("TcManticCierresDto", "cierres", params, columns, new Long(Constantes.REGISTROS_POR_PAGINA));
      this.attrs.put("cierres", cierres); 
			this.attrs.put("idCierre", UIBackingUtilities.toFirstKeySelectEntity(cierres));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} 

	private void doLoadMediosPagos() {
		List<UISelectItem> mediosPagos= new ArrayList<>();
		try {
			for(ETipoMediosPago record: ETipoMediosPago.values()){
				if(record.isCaja())
					mediosPagos.add(new UISelectItem(record.getIdTipoMedioPago(), record.getNombre()));
			} // for
			mediosPagos.add(0, new UISelectItem(-1L, "TODOS"));
			this.attrs.put("mediosPago", mediosPagos);
			this.attrs.put("idMedioPago", UIBackingUtilities.toFirstKeySelectItem(mediosPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} 
	
  public void doLoadDisponibleCaja() {
    Entity caja               = (Entity)this.attrs.get("caja");      
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= new HashMap<>();
    try {
			params.put("idCaja", caja.toLong("idCaja"));
			params.put("idCierre", ((UISelectEntity)this.attrs.get("idCierre")).getKey());
			params.put("dia", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio")));
      columns.add(new Columna("medioPago", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("disponible", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      columns.add(new Columna("acumulado", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      columns.add(new Columna("saldo", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      columns.add(new Columna("importe", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      this.lazyDisponible = new FormatCustomLazy("VistaConsultasDto", "disponible", params, columns);
      UIBackingUtilities.resetDataTable();
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

	public String toColor(Entity row) {
		return Cadena.isVacio(row.toString("garantia"))? "": "janal-tr-lime";
	} 
  
}
