package mx.org.kaana.kajool.procesos.mantenimiento.insumos.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolMantenimientoInsumosCuentas")
@ViewScoped
public class Cuentas extends Comun implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Cuentas.class);	
  private static final long serialVersionUID= -3565375733676226695L;
  
  @PostConstruct
  @Override
	protected void init() {
    try {			
      loadEntidades();
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
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      this.attrs.put(Constantes.SQL_CONDICION, "tc_janal_cuentas_movil.id_entidad="+ this.attrs.get("entidad"));
      this.lazyModel= new FormatCustomLazy("TcJanalCuentasMovilDto", this.attrs, campos);
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

}
