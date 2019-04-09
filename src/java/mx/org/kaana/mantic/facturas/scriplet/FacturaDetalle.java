package mx.org.kaana.mantic.facturas.scriplet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Letras;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.scriptlets.BarraProgreso;
import net.sf.jasperreports.engine.JRScriptletException;

public class FacturaDetalle extends BarraProgreso implements Serializable{

	private static final long serialVersionUID= 6191179382089789177L;
	private final String QR_HACIENDA_TOKEN    = "https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?&id={FOLIO_FISCAL}&re={RFC_EMISOR}&rr={RFC_RECEPTOR}&tt={IMPORTE_TOTAL}&fe={SELLO_DIGITAL}";
	
	private Map<String, Object> params;

	public FacturaDetalle() {
		this.params= new HashMap<>();
	}
	
  @Override
  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();
    Letras letras      = null;
    QRCodeWriter writer= new QRCodeWriter();
    BitMatrix matrix   = null;
		String codigoQR    = null;
		try {
      letras = new Letras();
      Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
      hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
      hints.put(EncodeHintType.MARGIN, 0); /* default = 4 */
      if(!getFieldValue("TOTAL_FINAL").toString().isEmpty())
        setVariableValue("LETRAS", letras.getMoneda(getFieldValue("TOTAL_FINAL").toString(), Boolean.FALSE));
			params.put("FOLIO_FISCAL", this.getFieldValue("FOLIO_FISCAL"));
			params.put("RFC_EMISOR", this.getParameterValue("REPORTE_EMPRESA_RFC"));
			params.put("RFC_RECEPTOR", this.getParameterValue("REPORTE_CLIENTE_RFC"));
			BigDecimal it=((BigDecimal)this.getFieldValue("TOTAL_FINAL"));
			params.put("IMPORTE_TOTAL", Numero.redondearSat(it.doubleValue()));
			String sd=((String)this.getFieldValue("SELLO_SAT"));
			if(sd== null)
				sd= "www.ferreteriabonanza.com";
			params.put("SELLO_DIGITAL", sd.substring(sd.length()- 8, sd.length()));
			codigoQR= Cadena.replaceParams(this.QR_HACIENDA_TOKEN, this.params);
      matrix = writer.encode(codigoQR, BarcodeFormat.QR_CODE, 400, 400, hints);
      setVariableValue("CODE_QR", MatrixToImageWriter.toBufferedImage(matrix));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch   
  }  

	@Override
	protected void finalize() throws Throwable {
		Methods.clean(this.params);
	}

	
}
