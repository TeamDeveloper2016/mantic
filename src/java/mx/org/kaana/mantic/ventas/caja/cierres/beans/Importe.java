package mx.org.kaana.mantic.ventas.caja.cierres.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/08/2018
 *@time 11:24:31 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Importe extends TcManticCierresCajasDto implements Serializable {

	private static final long serialVersionUID=-377455166380643306L;

	private String caja;
	private String empresa;
	private String medioPago;

	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja=caja;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa=empresa;
	}

	public String getMedioPago() {
		return medioPago;
	}

	public void setMedioPago(String medioPago) {
		this.medioPago=medioPago;
	}
	
	@Override
	public Class toHbmClass() {
		return TcManticCierresCajasDto.class;
	}

}
