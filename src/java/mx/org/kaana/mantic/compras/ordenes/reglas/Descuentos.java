package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import mx.org.kaana.libs.formato.Cadena;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/05/2018
 *@time 01:29:41 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Descuentos implements Serializable {
	
	private static final long serialVersionUID=-2814164383587346854L;
	private static final Log LOG=LogFactory.getLog(Descuentos.class);
	
	private double importe;
	private String porcentajes;
	private char token;
	private List<String> tokens;

	public Descuentos(double importe) {
		this(importe, "0");
	}

	public Descuentos(double importe, String porcentajes) {
		this(importe, porcentajes, ',');
	}

	public Descuentos(double importe, String porcentajes, char token) {
		this.importe    = importe;
		this.porcentajes= porcentajes;
		this.token      = token;
		split();
	}
  
	private void split() {
		this.tokens= Arrays.asList(this.porcentajes.split(String.valueOf(this.token)));
		int count= 0;
		while(count< this.tokens.size()) 
			if(Cadena.isVacio(this.tokens.get(count)) || !NumberUtils.isNumber(this.tokens.get(count)))
				this.tokens.set(count, "0");
			else		
				count++;	
	}
	
	public double getImporte() {
		double regresar= 0D;
		return this.tokens.stream().map((item) -> (this.importe* (Double.valueOf(item)/100))).reduce(regresar, (accumulator, value) -> accumulator+value); 
	}

  public double toImporte(String porcentajes, char token) {
		this.porcentajes= porcentajes;
		this.token      = token;
		split();
	  return getImporte();
	}
	
  public double toImporte(String porcentajes) {
		return toImporte(porcentajes, ',');
	}	
	
  public static void main(String ... args) {
		Descuentos descuentos= new Descuentos(500D, "a,10.00,5.00");
		LOG.info(descuentos.getImporte());
	}	

}
