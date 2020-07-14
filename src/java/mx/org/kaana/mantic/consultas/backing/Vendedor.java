package mx.org.kaana.mantic.consultas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoDocumento;

@Named(value= "manticConsultasVendedor")
@ViewScoped
public class Vendedor extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("fechaInicio", new Date(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("fechaTermino", new Date(Calendar.getInstance().getTimeInMillis()));
			this.toLoadCatalog();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= null;
		Double ventas             = 1D;
		Double utilidad           = 1D;
    try {
			params = this.toPrepare();
      params.put("sortOrder", "order by tc_mantic_empresas.nombre, tc_mantic_personas.nombres, tc_mantic_personas.paterno");
      columns= new ArrayList<>();
      columns.add(new Columna("nombreEmpresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("vendedor", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("ventas", EFormatoDinamicos.MILES_SIN_DECIMALES));
      columns.add(new Columna("devoluciones", EFormatoDinamicos.MILES_SIN_DECIMALES));
      columns.add(new Columna("total", EFormatoDinamicos.MILES_SAT_DECIMALES));
      columns.add(new Columna("utilidad", EFormatoDinamicos.MILES_SAT_DECIMALES));
      columns.add(new Columna("garantias", EFormatoDinamicos.MILES_SAT_DECIMALES));
      columns.add(new Columna("pventas", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("putilidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
			Entity value= (Entity)DaoFactory.getInstance().toEntity("VistaConsultasDto", "totalVendedores", params);
			if(value!= null && !value.isEmpty()) {
				ventas  = value.toDouble("ventas");
				utilidad= value.toDouble("utilidad");
			} // if	
			params.put("pventas", ventas);
			params.put("putilidad", utilidad);
			params.put("inicio", Fecha.formatear(Fecha.FECHA_CORTA, (Date)this.attrs.get("fechaInicio")));
			params.put("termino", Fecha.formatear(Fecha.FECHA_CORTA, (Date)this.attrs.get("fechaTermino")));
      this.lazyModel = new FormatCustomLazy("VistaConsultasDto", "ventasVendedor", params, columns);
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
	
	protected Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		sb.append("tc_mantic_ventas.id_tipo_documento=").append(ETipoDocumento.VENTAS_NORMALES.getIdTipoDocumento()).append(" and ");
		sb.append("tc_mantic_ventas.id_venta_estatus in (").append(EEstatusVentas.PAGADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.TIMBRADA.getIdEstatusVenta()).append(",").append(EEstatusVentas.TERMINADA.getIdEstatusVenta()).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("vendedor")) && !this.attrs.get("vendedor").toString().equals("-1"))
			sb.append("tc_mantic_ventas.id_usuario=").append(this.attrs.get("vendedor")).append(" and ");					
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  regresar.put("fechaInicio", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio")));
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  regresar.put("fechaTermino", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino")));
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	} // toPrepare
	
	protected void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("idEmpresa", this.toDefaultSucursal((List<UISelectEntity>)this.attrs.get("sucursales")));
			this.doLoadVendedores();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doLoadVendedores() {
		try {						
			this.attrs.put("condicionVendedor", !Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1") ? this.attrs.get("idEmpresa") : JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("vendedores", (List<UISelectItem>) UISelect.build("VistaConsultasDto", "vendedor", this.attrs, "nombre",  EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS));
			this.attrs.put("vendedor", new UISelectEntity("-1"));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doLoadVendedores
	
}
