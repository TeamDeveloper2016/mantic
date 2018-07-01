package mx.org.kaana.mantic.compras.ordenes.scriplet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Letras;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.scriptlets.BarraProgreso;
import net.sf.jasperreports.engine.JRScriptletException;

public class OrdenesDetalles  extends BarraProgreso implements Serializable{
  
  @Override
  public void afterDetailEval() throws JRScriptletException {
   super.afterDetailEval();
   Letras letras             = null;
   Map<String, Object>params = null;
   Value tels_almacen       = null;
   Value tels_proveedor     = null;
   Value emails_almacen     = null;
   Value emails_prveedor    = null;
    try {
      letras = new Letras();
      params= new HashMap<>();
      tels_almacen=DaoFactory.getInstance().toField("VistaInformacionAlmacen", "telefonos", params, "tels");
      tels_proveedor=DaoFactory.getInstance().toField("VistaInformacionProveedor", "telefonos", params, "tels");
      emails_almacen=DaoFactory.getInstance().toField("VistaInformacionAlmacen", "emails", params, "emails");
      emails_prveedor=DaoFactory.getInstance().toField("VistaInformacionProveedor", "emails", params, "emails");
			params.put("idOrdenCompra", getFieldValue("ID_KEY").toString());			
      setVariableValue("PROVEEDOR_REG", getFieldValue("PROVEEDOR_REGION").toString());
      setVariableValue("ALMACEN_REG", getFieldValue("ALMACEN_REGION").toString());
      setVariableValue("LETRAS", letras.getMoneda(getFieldValue("TOTAL_ORDEN").toString(), Boolean.FALSE));
      setVariableValue("TELEFONOS_ALMACEN", tels_almacen.getData() != null?tels_almacen.getData(): " N.E. " );
      setVariableValue("EMAILS_ALMACEN", emails_almacen.getData() != null?emails_almacen.getData(): " N.E. " );
      setVariableValue("TELEFONOS_PROVEEDOR", tels_proveedor.getData() != null?tels_proveedor.getData(): " N.E. " );
      setVariableValue("EMAILS_PROVEEDOR", emails_prveedor.getData() != null?emails_prveedor.getData(): " N.E. " );
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch   
    finally {
			Methods.clean(params);
		} // finally
  }  
}
