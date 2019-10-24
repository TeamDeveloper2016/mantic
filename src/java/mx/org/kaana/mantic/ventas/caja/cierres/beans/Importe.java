package mx.org.kaana.mantic.ventas.caja.cierres.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/08/2018
 *@time 11:24:31 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Importe extends TcManticCierresCajasDto implements Cloneable, Serializable {

	private static final long serialVersionUID=-377455166380643306L;

	private String caja;
	private String empresa;
	private String medioPago;
	private Double diferencia;
	private boolean omitir;
	
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

	public Double getDiferencia() {
		return this.diferencia;
	}

	public Boolean getOmitir() {
		return omitir;
	}

	public void setOmitir(Boolean omitir) {
		this.omitir=omitir;
	}

	public String getDiferencia$() {
		if(this.diferencia== null)
			this.diferencia= 0D;
		return "<span class='"+ (this.diferencia<0? "janal-color-orange": this.diferencia> 0? "janal-color-blue": "janal-color-green")+ "'><strong>"+ Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, Numero.toRedondearSat(this.diferencia))+ "</span></strong>";
	}
	
	@Override
	public Class toHbmClass() {
		return TcManticCierresCajasDto.class;
	}

	public void toCalculate() {
		this.diferencia= Numero.toRedondearSat(this.getImporte()- this.getSaldo());
	}

	@Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
	
}
