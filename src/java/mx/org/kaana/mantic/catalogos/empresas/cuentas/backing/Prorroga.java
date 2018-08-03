package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "manticCatalogosEmpresasCuentasProrroga")
@ViewScoped
public class Prorroga extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;	
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
      this.attrs.put("sortOrder", "order by	tc_mantic_empresas_deudas.registro desc");
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));     
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
			loadTiposPagos();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init	
	
  @Override
  public void doLoad() {
    Entity deuda             = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));			
			params.put("sortOrder", this.attrs.get("sortOrder"));
			deuda= (Entity) DaoFactory.getInstance().toEntity("VistaEmpresasDto", "cuentas", params);
			this.attrs.put("deuda", deuda);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
      JsfBase.addMessageError(e);
		} // catch
		finally{
			Methods.clean(params);
		} // finally	
  } // doLoad

	public String doRegresar() {	  
		return "saldos".concat(Constantes.REDIRECIONAR);
	} // doRegresar		

	private void loadTiposPagos(){
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposPagos", tiposPagos);
			this.attrs.put("tipoPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposPagos
}