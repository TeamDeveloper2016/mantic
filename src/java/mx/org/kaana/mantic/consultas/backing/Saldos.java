package mx.org.kaana.mantic.consultas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named(value = "manticConsultasSaldos")
@ViewScoped
public class Saldos extends IBaseFilter implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Saldos.class);
  private static final long serialVersionUID= 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
				this.loadSucursales();
//				this.doLoad();
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
    try {
  	  params = this.toPrepare();	
      params.put("sortOrder", "order by	dias desc");
      columns= new ArrayList<>();
      columns.add(new Columna("deuda", EFormatoDinamicos.MILES_SAT_DECIMALES));    
      columns.add(new Columna("pagado", EFormatoDinamicos.MILES_SAT_DECIMALES));      
      columns.add(new Columna("saldo", EFormatoDinamicos.MILES_SAT_DECIMALES));    
      columns.add(new Columna("vencido", EFormatoDinamicos.MILES_SAT_DECIMALES));    
      columns.add(new Columna("limiteCredito", EFormatoDinamicos.MILES_SAT_DECIMALES));    
      columns.add(new Columna("dias", EFormatoDinamicos.MILES_SIN_DECIMALES));    
			this.lazyModel = new FormatCustomLazy("VistaCuentasPorCobrarDto", params, columns);
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
		StringBuilder sb            = new StringBuilder("");
	  UISelectEntity cliente      = (UISelectEntity)this.attrs.get("cliente");
		List<UISelectEntity>clientes= (List<UISelectEntity>)this.attrs.get("clientes");
		if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
			sb.append("tc_mantic_clientes.razon_social like '%").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial")).append("%' and ");			
		else if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input")))
  		sb.append("tc_mantic_clientes.razon_social like '%").append(JsfBase.getParametro("razonSocial_input")).append("%' and ");						
		if(!Cadena.isVacio(this.attrs.get("vencidos")) && !this.attrs.get("vencidos").toString().equals("-1"))
      if(this.attrs.get("vencidos").toString().equals("1"))
  		  sb.append("((datediff(tc_mantic_clientes_deudas.limite, now())* -1)>= 0) and ");
      else
  		  sb.append("((datediff(tc_mantic_clientes_deudas.limite, now())* -1)< 0) and ");
		if(!Cadena.isVacio(this.attrs.get("dias")))
  		sb.append("((datediff(tc_mantic_clientes_deudas.limite, now())* -1)>= ").append(this.attrs.get("dias")).append(") and ");
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

	private void loadSucursales() {
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			this.attrs.put("sucursales", sucursales);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	public String toColor(Entity row) {
		// return row.toLong("idManual").equals(1L)? "janal-tr-orange": "";
		return "";
	}
	
	public List<UISelectEntity> doCompleteCliente(String codigo) {
 		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= new String(codigo).replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
			if(buscaPorCodigo)
        this.attrs.put("clientes", UIEntity.build("VistaCuentasPorCobrarDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("clientes", UIEntity.build("VistaCuentasPorCobrarDto", "porNombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("clientes");
	} // doCompleteCliente			

}