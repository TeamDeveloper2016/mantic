package mx.org.kaana.mantic.ventas.caja.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;
import mx.org.kaana.mantic.enums.ETipoMediosPago;

public class Pago implements Serializable{

	private static final long serialVersionUID= -1010567299549184249L;
	private Totales totales;	
	private Long idBanco;
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
	private String referencia;
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
	private Double difEfectivo;
	private Double difCredito;
	private Double difDebito;
	private Double difTransferencia;
	private Double difCheque;
	private Double abono;
	
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
		this.difCheque         = 0D;
		this.difCredito        = 0D;
		this.difDebito         = 0D;
		this.difEfectivo       = 0D;
		this.difTransferencia  = 0D;
		this.abono             = 0D;
		this.referencia        = "";
		this.idBanco           = -1L;
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
		Double regresar= 0D;
		regresar= regresar + (this.abono != null ? this.abono : 0D);
		regresar= regresar + (this.efectivo != null ? this.efectivo : 0D);
		regresar= regresar + (this.credito != null ? this.credito : 0D);
		regresar= regresar + (this.debito != null ? this.debito : 0D);
		regresar= regresar + (this.cheque != null ? this.cheque : 0D);
		regresar= regresar + (this.vales != null ? this.vales : 0D);
		regresar= regresar + (this.transferencia != null ? this.transferencia : 0D);
		return regresar;
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

	public Double getAbono() {
		return abono;
	}

	public void setAbono(Double abono) {
		this.abono = abono != null ? abono : 0.0D;
	}	
	
	public Double getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(Double efectivo) {
		this.efectivo = efectivo != null ? efectivo : 0.0D;
	}

	public Double getCredito() {
		return credito;
	}

	public void setCredito(Double credito) {
		this.credito = credito!= null ? credito : 0.0D;
	}

	public Double getDebito() {
		return debito;
	}

	public void setDebito(Double debito) {
		this.debito = debito != null ? debito : 0.0D;
	}

	public Double getCheque() {
		return cheque;
	}

	public void setCheque(Double cheque) {
		this.cheque = cheque != null ? cheque : 0.0D;
	}

	public Double getVales() {
		return vales;
	}

	public void setVales(Double vales) {
		this.vales = vales != null ? vales : 0.0D;
	}

	public Double getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Double transferencia) {
		this.transferencia = transferencia != null ? transferencia : 0.0D;
	}
	
	public Double getLimiteCredito() {
		Double regresar= 0D;
		if((this.cheque + this.debito + this.transferencia + this.abono) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.cheque + this.debito + this.vales + this.transferencia + this.abono);
		return regresar < 0 ? 0 : regresar;
	}	

	public Double getLimiteDebito() {
		Double regresar= 0D;
		if((this.cheque + this.credito + this.transferencia + this.abono) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.cheque + this.credito + this.vales + this.transferencia + this.abono);
		return regresar < 0 ? 0 : regresar;
	}

	public Double getLimiteCheque() {
		Double regresar= 0D;
		if((this.debito + this.credito + this.transferencia + this.abono) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.credito + this.debito + this.vales + this.transferencia + this.abono);
		return regresar < 0 ? 0 : regresar;
	}

	public Double getLimiteVales() {
		Double regresar= this.totales.getTotal() - (this.cheque + this.debito + this.credito + this.transferencia + this.abono);
		return regresar < 0D ? 0D : regresar;
	}

	public Double getLimiteTransferencia() {
		Double regresar= 0D;
		if((this.debito + this.credito + this.cheque + this.abono) <= 0)
			regresar= this.totales.getTotal();
		else
			regresar= this.totales.getTotal() - (this.credito + this.debito + this.vales + this.cheque + this.abono);
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

	public Double getDifEfectivo() {
		return getPago().equals(0D) ? 0D : Numero.toRedondear(getTotales().getTotal() - getPago());
	}

	public void setDifEfectivo(Double difEfectivo) {
		this.difEfectivo = difEfectivo;
	}

	public Double getDifCredito() {
		return getPago().equals(0D) ? 0D : Numero.toRedondear(getTotales().getTotal() - getPago());
	}

	public void setDifCredito(Double difCredito) {
		this.difCredito = difCredito;
	}

	public Double getDifDebito() {
		return getPago().equals(0D) ? 0D : Numero.toRedondear(getTotales().getTotal() - getPago());
	}

	public void setDifDebito(Double difDebito) {
		this.difDebito = difDebito;
	}

	public Double getDifTransferencia() {
		return getPago().equals(0D) ? 0D : Numero.toRedondear(getTotales().getTotal() - getPago());
	}

	public void setDifTransferencia(Double difTransferencia) {
		this.difTransferencia = difTransferencia;
	}

	public Double getDifCheque() {
		return getPago().equals(0D) ? 0D : Numero.toRedondear(getTotales().getTotal() - getPago());
	}

	public void setDifCheque(Double difCheque) {
		this.difCheque = difCheque;
	}	
	
	public Long getIdTipoMedioPago(){
		Long regresar= -1L;
		if((this.efectivo > 0D && this.debito<= 0 && this.credito<= 0 && this.transferencia<= 0 && this.cheque<= 0) || (this.efectivo > 0D && (this.debito>= 0 || this.credito>= 0 || this.transferencia>= 0 || this.cheque>= 0))){
			regresar= ETipoMediosPago.EFECTIVO.getIdTipoMedioPago();
		} // if
		else if(this.debito > 0D && this.efectivo<= 0 && this.credito<= 0 && this.transferencia<= 0 && this.cheque<= 0){
			regresar= ETipoMediosPago.TARJETA_DEBITO.getIdTipoMedioPago();
			this.idBanco= this.bancoDebito.getKey();
			this.referencia= this.referenciaDebito;
		} // else id
		else if(this.credito > 0D && this.efectivo<= 0 && this.debito<= 0 && this.transferencia<= 0 && this.cheque<= 0){
			regresar= ETipoMediosPago.TARJETA_CREDITO.getIdTipoMedioPago();
			this.idBanco= this.bancoCredito.getKey();
			this.referencia= this.referenciaCredito;
		} // else id
		else if(this.transferencia > 0D && this.efectivo<= 0 && this.credito<= 0 && this.debito<= 0 && this.cheque<= 0){
			regresar= ETipoMediosPago.TRANSFERENCIA.getIdTipoMedioPago();
			this.idBanco= this.bancoTransferencia.getKey();
			this.referencia= this.referenciaTransferencia;
		} // else id
		else if(this.cheque > 0D && this.efectivo<= 0 && this.credito<= 0 && this.transferencia<= 0 && this.debito<= 0){
			regresar= ETipoMediosPago.CHEQUE.getIdTipoMedioPago();
			this.idBanco= this.bancoCheque.getKey();
			this.referencia= this.referenciaCheque;
		} // else id
		return regresar;
	} // getIdTipoMedioPago
	
	public Long getIdBanco(){
		return idBanco;
	}
	
	public String getReferencia(){
		return referencia;
	}
}