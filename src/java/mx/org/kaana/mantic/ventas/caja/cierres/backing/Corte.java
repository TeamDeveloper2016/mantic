package mx.org.kaana.mantic.ventas.caja.cierres.backing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "manticVentasCajaCierresCorte")
@ViewScoped
public class Corte extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428323L;
	protected FormatLazyModel retiros;
	protected FormatLazyModel devoluciones;

	public FormatLazyModel getRetiros() {
		return retiros;
	}

	public FormatLazyModel getDevoluciones() {
		return devoluciones;
	}
	
	public String getTotal() {
		Double total= (Double)this.attrs.get("ventas")- (Double)this.attrs.get("garantias");
		if((Double)this.attrs.get("abonos")> 0)
			total+= (Double)this.attrs.get("abonos");
		else
			total-= (Double)this.attrs.get("abonos");
		StringBuilder sb= new StringBuilder();
		sb.append(Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, total));
		sb.append(" = ");
		sb.append(this.attrs.get("ventas"));
		sb.append(" - devoluciones (");
		sb.append(this.attrs.get("garantias"));
		sb.append(")");
		if((Double)this.attrs.get("abonos")> 0)
  		sb.append(" + abonos");
		else
  		sb.append(" - retiros"); 
		sb.append(" (");
		sb.append(this.attrs.get("abonos"));
		sb.append(")");
		return sb.toString();
	}
	
	public String getSumaTickets() {
		return (String)this.attrs.get("fventas");
	}
	
	public String getSumaRetiros() {
		return (String)this.attrs.get("fabonos");
	}
	
	public String getSumaDevoluciones() {
		return (String)this.attrs.get("fgarantias");
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idCierre", JsfBase.getFlashAttribute("idCierre"));
			this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));
			this.attrs.put("idCaja", JsfBase.getFlashAttribute("idCaja"));
			this.attrs.put("inicio", JsfBase.getFlashAttribute("inicio"));
			this.attrs.put("termino", JsfBase.getFlashAttribute("termino"));
			this.attrs.put("nombreEmpresa", JsfBase.getFlashAttribute("nombreEmpresa"));
			this.attrs.put("caja", JsfBase.getFlashAttribute("caja"));
			this.attrs.put("acumulado", JsfBase.getFlashAttribute("acumulado"));
		  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= new HashMap<>();
    try {
      params.put("idEmpresa", this.attrs.get("idEmpresa"));
      params.put("idCierre", this.attrs.get("idCierre"));
      params.put("inicio", Fecha.formatear(Fecha.FECHA_HORA_LARGA, (Timestamp)this.attrs.get("inicio")));
			if(((Timestamp)this.attrs.get("inicio")).equals((Timestamp)this.attrs.get("termino")))
        params.put("termino", Fecha.formatear(Fecha.FECHA_HORA_LARGA, new Timestamp(Calendar.getInstance().getTimeInMillis())));
			else	
        params.put("termino", Fecha.formatear(Fecha.FECHA_HORA_LARGA, (Timestamp)this.attrs.get("termino")));
			
      columns = new ArrayList<>();
      columns.add(new Columna("importe", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			this.toSumaTotales("ventas", "ventas", params);
      this.lazyModel   = new FormatCustomLazy("VistaCortesCajasDto", params, columns);
			this.toSumaTotales("abonos", "abonos", params);
      this.retiros     = new FormatCustomLazy("VistaCortesCajasDto", "retiros", params, columns);
			this.toSumaTotales("garantias", "garantias", params);
      this.devoluciones= new FormatCustomLazy("VistaCortesCajasDto", "devoluciones", params, columns);
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
  } // doLoad

	public String doRegresar() {
		JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
		return "filtro".concat(Constantes.REDIRECIONAR);
	} 
	
	private void toSumaTotales(String concepto, String idXml, Map<String, Object> params) throws Exception {
		Double total= 0D;
		Value value= (Value)DaoFactory.getInstance().toField("VistaCortesCajasDto", idXml, params, "total");
		if(value!= null && value.getData()!= null)
  		total= value.toDouble();
 		this.attrs.put(concepto, total);
 		this.attrs.put("f"+ concepto, Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, total));
	}
	
}
