package mx.org.kaana.mantic.compras.ordenes.scriplet;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Letras;
import mx.org.kaana.libs.reportes.scriptlets.BarraProgreso;
import net.sf.jasperreports.engine.JRScriptletException;

public class OrdenesDetalles  extends BarraProgreso implements Serializable{
  
  @Override
  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();
    Letras letras  = null;
    try {
      letras = new Letras();
      setVariableValue("LETRAS", letras.getMoneda(getFieldValue("TOTAL_ORDEN").toString(), Boolean.FALSE));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } 
  
}
