package mx.org.kaana.libs.facturama.models.request.complements.foreingtrade;

import mx.org.kaana.libs.facturama.models.request.Issuer;
import java.util.List;

public class ForeingTrade {

	private Issuer Issuer;
	private Receiver Receiver;
	private List<Owner> Owner;
	private List<Recipient> Recipient;
	private String ReasonForTransfer;
	private String OperationType;
	private List<Commodity> Comodity;
	private String RequestCode;
	private String Incoterm;
	private Boolean Subdivision;
	private Double ExchangeRateUSD;
	private Double TotalUSD;
	private Boolean OriginCertificate;
	private String OriginCertificateNumber;
	private String ReliableExporterNumber;
	private String Observations;

	public Issuer getIssuer() {
		return Issuer;
	}

	public void setIssuer(Issuer Issuer) {
		this.Issuer = Issuer;
	}

	public Receiver getReceiver() {
		return Receiver;
	}

	public void setReceiver(Receiver Receiver) {
		this.Receiver = Receiver;
	}

	public List<Owner> getOwner() {
		return Owner;
	}

	public void setOwner(List<Owner> Owner) {
		this.Owner = Owner;
	}

	public List<Recipient> getRecipient() {
		return Recipient;
	}

	public void setRecipient(List<Recipient> Recipient) {
		this.Recipient = Recipient;
	}

	public String getReasonForTransfer() {
		return ReasonForTransfer;
	}

	public void setReasonForTransfer(String ReasonForTransfer) {
		this.ReasonForTransfer = ReasonForTransfer;
	}

	public String getOperationType() {
		return OperationType;
	}

	public void setOperationType(String OperationType) {
		this.OperationType = OperationType;
	}

	public List<Commodity> getComodity() {
		return Comodity;
	}

	public void setComodity(List<Commodity> Comodity) {
		this.Comodity = Comodity;
	}

	public String getRequestCode() {
		return RequestCode;
	}

	public void setRequestCode(String RequestCode) {
		this.RequestCode = RequestCode;
	}

	public String getIncoterm() {
		return Incoterm;
	}

	public void setIncoterm(String Incoterm) {
		this.Incoterm = Incoterm;
	}

	public Boolean getSubdivision() {
		return Subdivision;
	}

	public void setSubdivision(Boolean Subdivision) {
		this.Subdivision = Subdivision;
	}

	public Double getExchangeRateUSD() {
		return ExchangeRateUSD;
	}

	public void setExchangeRateUSD(Double ExchangeRateUSD) {
		this.ExchangeRateUSD = ExchangeRateUSD;
	}

	public Double getTotalUSD() {
		return TotalUSD;
	}

	public void setTotalUSD(Double TotalUSD) {
		this.TotalUSD = TotalUSD;
	}

	public Boolean getOriginCertificate() {
		return OriginCertificate;
	}

	public void setOriginCertificate(Boolean OriginCertificate) {
		this.OriginCertificate = OriginCertificate;
	}

	public String getOriginCertificateNumber() {
		return OriginCertificateNumber;
	}

	public void setOriginCertificateNumber(String OriginCertificateNumber) {
		this.OriginCertificateNumber = OriginCertificateNumber;
	}

	public String getReliableExporterNumber() {
		return ReliableExporterNumber;
	}

	public void setReliableExporterNumber(String ReliableExporterNumber) {
		this.ReliableExporterNumber = ReliableExporterNumber;
	}

	public String getObservations() {
		return Observations;
	}

	public void setObservations(String Observations) {
		this.Observations = Observations;
	}

}
