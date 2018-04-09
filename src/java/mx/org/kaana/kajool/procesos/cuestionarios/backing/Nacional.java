package mx.org.kaana.kajool.procesos.cuestionarios.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/11/2016
 *@time 10:51:10 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EReporte;
import mx.org.kaana.kajool.procesos.enums.EEstatus;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolCuestionariosNacional")
@ViewScoped
public class Nacional extends Filtro implements Serializable {
  
  private static final long serialVersionUID= 7926500880048046228L;
	private static final Log LOG              = LogFactory.getLog(Nacional.class);	
  
  @PostConstruct
  @Override
	protected void init() {
    try {			
      this.attrs.put("control", "");
      this.attrs.put("folio", "");     
      this.attrs.put("idEstatus", 0L);
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {
		List<Columna>campos= null;
    try {
      campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));      
      this.lazyModel= new FormatLazyModel("VistaCuestionariosDto", "nacional", this.attrs, campos);
			UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(campos);
		} // finally
  } // doLoad      

  @Override
  public String doImprimir() {
    return super.doImprimir(EReporte.CUESTIONARIO_NACIONAL);
  } // doImprimir

  @Override
  public String doReportes() {
    return super.doReportes(EReporte.CUESTIONARIO_NACIONAL);
  } // doReportes    
}
