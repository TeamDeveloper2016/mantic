package mx.org.kaana.mantic.ventas.caja.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;

public class Pago implements Serializable{

	private static final long serialVersionUID= -1010567299549184249L;
	private Totales totales;	
	private UISelectEntity bancoCredito;
	private UISelectEntity bancoDebito;
	private UISelectEntity bancoCheque;
	private UISelectEntity bancoTransferencia;
	private Double efectivo;
	private Double credito;
	private Double debito;
	private Double cheque;
	private Double vales;	
	private Double transferencia;	
	private String referenciaCredito;
	private String referenciaDebito;
	private String referenciaCheque;
	private String referenciaTransferencia;
	private String pago$;
	private String cambio$;
	private Double pago;
	private Double cambio;
	private Double limiteCredito;
	private Double limiteDebito;
	private Double limiteCheque;
	private Double limiteVales;
	private Double limiteTransferencia;
	
	public Pago(){
		this(new Totales());
	}
	
	public Pago(Totales totales){
		this(totales, 0D, 0D, 0D, 0D, 0D, 0D);
	}			

	public Pago(Totales totales, Double efectivo, Double credito, Double debito, Double cheque, Double vales, Double transferencia) {
		this(totales, new UISelectEntity("-1"), new UISelectEntity("-1"), new UISelectEntity("-1"), new UISelectEntity("-1"), efectivo, credito, debito, cheque, vales, "", "", "", transferencia, "");		
	}

	public Pago(Totales totales, UISelectEntity bancoTransferencia, UISelectEntity bancoCredito, UISelectEntity bancoDebito, UISelectEntity bancoCheque, Double efectivo, Double credito, Double debito, Double cheque, Double vales, String referenciaCredito, String referenciaDebito, String referenciaCheque, Double transferencia, String referenciaTransferencia) {
		this.totales           = totales;
		this.bancoTransferencia= bancoTransferencia;
		this.bancoCredito      = bancoCredito;
		this.bancoDebito       = bancoDebito;
		this.bancoCheque       = bancoCheque;
		this.efectivo          = efectivo;
		this.credito           = credito;
		this.debito            = debito;
		this.cheque            = cheque;
		this.vales             = vales;
		this.referenciaCredito = referenciaCredito;
		this.referenciaDebito  = referenciaDebito;
		this.referenciaCheque  = referenciaCheque;
		this.transferencia     = transferencia;
		this.referenciaTransferencia= referenciaTransferencia;
	}
	
	public Totales getTotales() {
		return totales;
	}

	public void setTotales(Totales totales) {
		this.totales = totales;
	}		

	public void setPago(Double pago) {
		this.pago= pago;
	}
	
	public Double getPago() {
		return (this.efectivo + this.credito + this.debito + this.cheque + this.vales + this.transferencia);
	}
	
	public void setPago$(String pago$) {
		this.pago$= pago$;
	}
	
	public String getPago$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondearSat(getPago()));
	}	

	public void setCambio$(String cambio$) {
		this.cambio$= cambio$;
	}
	
	public String getCambio$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondearSat(getCambio()));
	}
	
	public void setCambio(Double cambio) {
		this.cambio= cambio;
	}
	
	public Double getCambio() {
		Double regresar= getPago() - this.totales.getTotal();
		return regresar < 0 ?  0: regresar;
	}

	public Double getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(Double efectivo) {
		this.efectivo = efectivo;
	}

	public Double getCredito() {
		return credito;
	}

	public void setCredito(Double credito) {
		this.credito = credito;
	}

	public Double getDebito() {
		return debito;
	}

	public void setDebito(Double debito) {
		this.debito = debito;
	}

	public Double getCheque() {
		return cheque;
	}

	public void setCheque(Double cheque) {
		this.cheque = cheque;
	}

	public Double getVales() {
		return vales;
	}

	public void setVales(Double vales) {
		this.vales = vales;
	}

	public Double getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Double transferencia) {
		this.transferencia = transferencia;
	}
	
	public Double getLimiteCredito() {
		Double regresar= 0D;
		if((this.cheque + this.debito + this.transferencia) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.cheque + this.debito + this.vales + this.transferencia);
		return regresar < 0 ? 0 : regresar;
	}	

	public Double getLimiteDebito() {
		Double regresar= 0D;
		if((this.cheque + this.credito + this.transferencia) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.cheque + this.credito + this.vales + + this.transferencia);
		return regresar < 0 ? 0 : regresar;
	}

	public Double getLimiteCheque() {
		Double regresar= 0D;
		if((this.debito + this.credito + this.transferencia) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.credito + this.debito + this.vales + + this.transferencia);
		return regresar < 0 ? 0 : regresar;
	}

	public Double getLimiteVales() {
		Double regresar= this.totales.getTotal() - (this.cheque + this.debito + this.credito + + this.transferencia);
		return regresar < 0D ? 0D : regresar;
	}

	public Double getLimiteTransferencia() {
		Double regresar= 0D;
		if((this.debito + this.credito + this.cheque) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.credito + this.debito + this.vales + this.cheque);
		return regresar < 0 ? 0 : regresar;
	}

	public void setLimiteTransferencia(Double limiteTransferencia) {
		this.limiteTransferencia = limiteTransferencia;
	}	
	
	public void setLimiteCredito(Double limiteCredito) {
		this.limiteCredito= limiteCredito;
	}
	
	public void setLimiteDebito(Double limiteDebito) {
		this.limiteDebito= limiteDebito;
	}
	
	public void setLimiteCheque(Double limiteCheque) {
		this.limiteCheque= limiteCheque;
	}
	
	public void setLimiteVales(Double limiteVales) {
		this.limiteVales= limiteVales;
	}

	public UISelectEntity getBancoTransferencia() {
		return bancoTransferencia;
	}

	public void setBancoTransferencia(UISelectEntity bancoTransferencia) {
		this.bancoTransferencia = bancoTransferencia;
	}
	
	public UISelectEntity getBancoCredito() {
		return bancoCredito;
	}

	public void setBancoCredito(UISelectEntity bancoCredito) {
		this.bancoCredito = bancoCredito;
	}

	public UISelectEntity getBancoDebito() {
		return bancoDebito;
	}

	public void setBancoDebito(UISelectEntity bancoDebito) {
		this.bancoDebito = bancoDebito;
	}

	public UISelectEntity getBancoCheque() {
		return bancoCheque;
	}

	public void setBancoCheque(UISelectEntity bancoCheque) {
		this.bancoCheque = bancoCheque;
	}

	public String getReferenciaCredito() {
		return referenciaCredito;
	}

	public void setReferenciaCredito(String referenciaCredito) {
		this.referenciaCredito = referenciaCredito;
	}

	public String getReferenciaDebito() {
		return referenciaDebito;
	}

	public void setReferenciaDebito(String referenciaDebito) {
		this.referenciaDebito = referenciaDebito;
	}

	public String getReferenciaCheque() {
		return referenciaCheque;
	}

	public void setReferenciaCheque(String referenciaCheque) {
		this.referenciaCheque = referenciaCheque;
	}	

	public String getReferenciaTransferencia() {
		return referenciaTransferencia;
	}

	public void setReferenciaTransferencia(String referenciaTransferencia) {
		this.referenciaTransferencia = referenciaTransferencia;
	}	
}