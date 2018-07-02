package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComprobanteFiscal implements Serializable{
	
	private static final long serialVersionUID = -9138574770788187110L;
	private Emisor emisor;
	private Receptor receptor;
	private List<Concepto> conceptos;
	private Impuesto impuesto;
	private TimbreFiscalDigital timbreFiscalDigital;
	private String xsi;
	private String schemaLocation;
	private String version;
	private String folio;
	private String fecha;
	private String sello;
	private String formaPago;
	private String noCertificado;
	private String certificado;
	private String subTotal;
	private String moneda;
	private String tipoCambio;
	private String total;
	private String tipoDeComprobante;
	private String metodoPago;
	private String lugarExpedicion;
	private String cfdi;

	public ComprobanteFiscal() {
		this(new Emisor(), new Receptor(), new ArrayList<Concepto>(), new Impuesto(), new TimbreFiscalDigital(), null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
	}

	public ComprobanteFiscal(Emisor emisor, Receptor receptor, List<Concepto> conceptos, Impuesto impuesto, TimbreFiscalDigital timbreFiscalDigital, String xsi, String schemaLocation, String version, String folio, String fecha, String sello, String formaPago, String noCertificado, String certificado, String subTotal, String moneda, String tipoCambio, String total, String tipoDeComprobante, String metodoPago, String lugarExpedicion, String cfdi) {
		this.emisor           = emisor;
		this.receptor         = receptor;
		this.conceptos        = conceptos;
		this.impuesto         = impuesto;
		this.timbreFiscalDigital= timbreFiscalDigital;
		this.xsi              = xsi;
		this.schemaLocation   = schemaLocation;
		this.version          = version;
		this.folio            = folio;
		this.fecha            = fecha;
		this.sello            = sello;
		this.formaPago        = formaPago;
		this.noCertificado    = noCertificado;
		this.certificado      = certificado;
		this.subTotal         = subTotal;
		this.moneda           = moneda;
		this.tipoCambio       = tipoCambio;
		this.total            = total;
		this.tipoDeComprobante= tipoDeComprobante;
		this.metodoPago       = metodoPago;
		this.lugarExpedicion  = lugarExpedicion;
		this.cfdi             = cfdi;
	}

	public String getXsi() {
		return xsi;
	}

	public void setXsi(String xsi) {
		this.xsi = xsi;
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}

	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getSello() {
		return sello;
	}

	public void setSello(String sello) {
		this.sello = sello;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getNoCertificado() {
		return noCertificado;
	}

	public void setNoCertificado(String noCertificado) {
		this.noCertificado = noCertificado;
	}

	public String getCertificado() {
		return certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTipoDeComprobante() {
		return tipoDeComprobante;
	}

	public void setTipoDeComprobante(String tipoDeComprobante) {
		this.tipoDeComprobante = tipoDeComprobante;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getLugarExpedicion() {
		return lugarExpedicion;
	}

	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
	}

	public String getCfdi() {
		return cfdi;
	}

	public void setCfdi(String cfdi) {
		this.cfdi = cfdi;
	}	

	public Emisor getEmisor() {
		return emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public Receptor getReceptor() {
		return receptor;
	}

	public void setReceptor(Receptor receptor) {
		this.receptor = receptor;
	}

	public List<Concepto> getConceptos() {
		return conceptos;
	}

	public void setConceptos(List<Concepto> conceptos) {
		this.conceptos = conceptos;
	}

	public Impuesto getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(Impuesto impuesto) {
		this.impuesto = impuesto;
	}

	public TimbreFiscalDigital getTimbreFiscalDigital() {
		return timbreFiscalDigital;
	}

	public void setTimbreFiscalDigital(TimbreFiscalDigital timbreFiscalDigital) {
		this.timbreFiscalDigital = timbreFiscalDigital;
	}	

	@Override
	public String toString() {
		return "ComprobanteFiscal{"+"emisor="+emisor+", receptor="+receptor+", conceptos="+conceptos+", impuesto="+impuesto+", timbreFiscalDigital="+timbreFiscalDigital+", xsi="+xsi+", schemaLocation="+schemaLocation+", version="+version+", folio="+folio+", fecha="+fecha+", sello="+sello+", formaPago="+formaPago+", noCertificado="+noCertificado+", certificado="+certificado+", subTotal="+subTotal+", moneda="+moneda+", tipoCambio="+tipoCambio+", total="+total+", tipoDeComprobante="+tipoDeComprobante+", metodoPago="+metodoPago+", lugarExpedicion="+lugarExpedicion+", cfdi="+cfdi+'}';
	}
	
}