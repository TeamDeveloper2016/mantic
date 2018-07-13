package mx.org.kaana.mantic.compras.ordenes.scriplet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Letras;
import mx.org.kaana.libs.reportes.scriptlets.BarraProgreso;
import net.sf.jasperreports.engine.JRScriptletException;

public class OrdenesDetalles  extends BarraProgreso implements Serializable{
  
  @Override
  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();
     Letras letras            = null;
     QRCodeWriter writer      = new QRCodeWriter();
     BitMatrix matrix         = null;
      try {
        letras = new Letras();
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0); /* default = 4 */
        setVariableValue("LETRAS", letras.getMoneda(getFieldValue("TOTAL_ORDEN").toString(), Boolean.FALSE));
        matrix = writer.encode(getParameterValue("NOMBRE_REPORTE").toString().concat(":").concat(getFieldValue("CONSECUTIVO").toString()).toString().concat("-").concat("http://bonanzaj.jvmhost.net/MANTIC/"), BarcodeFormat.QR_CODE, 400, 400, hints);
        setVariableValue("CODE_QR", MatrixToImageWriter.toBufferedImage(matrix) );
      } // try
      catch (Exception e) {
        Error.mensaje(e);
      } // catch   
  }  

}
