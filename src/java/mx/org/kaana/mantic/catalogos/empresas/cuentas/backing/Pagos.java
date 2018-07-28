package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "manticCatalogosEmpresasCuentasPagos")
@ViewScoped
public class Pagos extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("proveedor", "");
			this.attrs.put("empresa", "");
			this.attrs.put("almacen", "");
      this.attrs.put("sortOrder", "order by tc_mantic_ventas.consecutivo, tc_mantic_empresas_deudas.registro desc");
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());			
			this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa")== null ? JsfBase.getAutentifica().getEmpresa().getIdEmpresa() : Long.valueOf(JsfBase.getFlashAttribute("idEmpresa").toString()));
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				loadSucursales();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
	  Map<String, Object> params= null;	
    List<Columna> columns     = null;
    try {			
  	  params = toPrepare();	
			params.put("empresa", this.attrs.get("empresa"));
			params.put("idEmpresa", this.attrs.get("idEmpresa").toString().equals("-1") ? this.attrs.get("allEmpresa") : this.attrs.get("idEmpresa"));			
			params.put("almacen", this.attrs.get("almacen"));			
			params.put("proveedor", this.attrs.get("proveedor"));			
      columns = new ArrayList<>();
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));      
      columns.add(new Columna("saldo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));    
      columns.add(new Columna("pago", EFormatoDinamicos.MONEDA_SAT_DECIMALES));    
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_EXTENDIDA));    			
			this.lazyModel = new FormatCustomLazy("VistaEmpresasDto", "pagosBusqueda", params, columns);
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

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_notas_entradas.consecutivo= ").append(this.attrs.get("consecutivo")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_empresas_deudas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_empresas_deudas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("vencidos")) && this.attrs.get("vencidos").toString().equals("1"))
  		sb.append("(now()> tc_mantic_empresas_deudas.limite) and ");
		if(!Cadena.isVacio(this.attrs.get("dias")))
  		sb.append("(datediff(tc_mantic_empresas_deudas.limite, now())>= ").append(this.attrs.get("dias")).append(") and ");		
		regresar.put("idEmpresa", this.attrs.get("idEmpresa"));		
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	}	

	public String doRegresar() {
		return "filtro".concat(Constantes.REDIRECIONAR);
	} // doRegresar
	
	private void loadSucursales(){
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		String allEmpresa              = "";
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			if(!sucursales.isEmpty()){
				for(UISelectEntity sucursal: sucursales)
					allEmpresa= allEmpresa.concat(sucursal.getKey().toString()).concat(",");
				this.attrs.put("allEmpresa", allEmpresa.substring(0, allEmpresa.length()-1));
			} // 
			this.attrs.put("sucursales", sucursales);						
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
}