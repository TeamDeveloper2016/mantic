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
	private Double efectivo;
	private Double credito;
	private Double debito;
	private Double cheque;
	private Double vales;	
	private String referenciaCredito;
	private String referenciaDebito;
	private String referenciaCheque;
	
	public Pago(){
		this(new Totales());
	}
	
	public Pago(Totales totales){
		this(totales, 0D, 0D, 0D, 0D, 0D);
	}			

	public Pago(Totales totales, Double efectivo, Double credito, Double debito, Double cheque, Double vales) {
		this(totales, new UISelectEntity("-1"), new UISelectEntity("-1"), new UISelectEntity("-1"), efectivo, credito, debito, cheque, vales, "", "", "");		
	}

	public Pago(Totales totales, UISelectEntity bancoCredito, UISelectEntity bancoDebito, UISelectEntity bancoCheque, Double efectivo, Double credito, Double debito, Double cheque, Double vales, String referenciaCredito, String referenciaDebito, String referenciaCheque) {
		this.totales          = totales;
		this.bancoCredito     = bancoCredito;
		this.bancoDebito      = bancoDebito;
		this.bancoCheque      = bancoCheque;
		this.efectivo         = efectivo;
		this.credito          = credito;
		this.debito           = debito;
		this.cheque           = cheque;
		this.vales            = vales;
		this.referenciaCredito= referenciaCredito;
		this.referenciaDebito = referenciaDebito;
		this.referenciaCheque = referenciaCheque;
	}
	
	public Totales getTotales() {
		return totales;
	}

	public void setTotales(Totales totales) {
		this.totales = totales;
	}		

	public Double getPago() {
		return (this.efectivo + this.credito + this.debito + this.cheque + this.vales);
	}
	
	public String getPago$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(getPago()));
	}	

	public String getCambio$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(getCambio()));
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

	public Double getLimiteCredito() {
		Double regresar= 0D;
		if((this.cheque + this.debito) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.cheque + this.debito + this.vales);
		return regresar < 0 ? 0 : regresar;
	}	

	public Double getLimiteDebito() {
		Double regresar= 0D;
		if((this.cheque + this.credito) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.cheque + this.credito + this.vales);
		return regresar < 0 ? 0 : regresar;
	}

	public Double getLimiteCheque() {
		Double regresar= 0D;
		if((this.debito + this.credito) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.credito + this.debito + this.vales);
		return regresar < 0 ? 0 : regresar;
	}

	public Double getLimiteVales() {
		Double regresar= this.totales.getTotal() - (this.cheque + this.debito + this.credito);
		return regresar < 0D ? 0D : regresar;
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
}