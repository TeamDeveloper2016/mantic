package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class TimbreFiscalDigital implements Serializable{
	
	private static final long serialVersionUID = 5502304340385552527L;
	private String tfd;
	private String xsi;
	private String schemaLocation;
	private String version; 
	private String uuid;
	private String fechaTimbrado;
	private String rfcProvCertif;
	private String selloCfd;
	private String noCertificadoSat;
	private String selloSat;	

	public TimbreFiscalDigital() {
		this(null, null, null, null, null, null, null, null, null, null);
	}

	public TimbreFiscalDigital(String tfd, String xsi, String schemaLocation, String version, String uuid, String fechaTimbrado, String rfcProvCertif, String selloCfd, String noCertificadoSat, String selloSat) {
		this.tfd             = tfd;
		this.xsi             = xsi;
		this.schemaLocation  = schemaLocation;
		this.version         = version;
		this.uuid            = uuid;
		this.fechaTimbrado   = fechaTimbrado;
		this.rfcProvCertif   = rfcProvCertif;
		this.selloCfd        = selloCfd;
		this.noCertificadoSat= noCertificadoSat;
		this.selloSat        = selloSat;
	}

	public String getTfd() {
		return tfd;
	}

	public void setTfd(String tfd) {
		this.tfd = tfd;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public String getRfcProvCertif() {
		return rfcProvCertif;
	}

	public void setRfcProvCertif(String rfcProvCertif) {
		this.rfcProvCertif = rfcProvCertif;
	}

	public String getSelloCfd() {
		return selloCfd;
	}

	public void setSelloCfd(String selloCfd) {
		this.selloCfd = selloCfd;
	}

	public String getNoCertificadoSat() {
		return noCertificadoSat;
	}

	public void setNoCertificadoSat(String noCertificadoSat) {
		this.noCertificadoSat = noCertificadoSat;
	}

	public String getSelloSat() {
		return selloSat;
	}

	public void setSelloSat(String selloSat) {
		this.selloSat = selloSat;
	}	

	@Override
	public String toString() {
		return "TimbreFiscalDigital{"+"tfd="+tfd+", xsi="+xsi+", schemaLocation="+schemaLocation+", version="+version+", uuid="+uuid+", fechaTimbrado="+fechaTimbrado+", rfcProvCertif="+rfcProvCertif+", selloCfd="+selloCfd+", noCertificadoSat="+noCertificadoSat+", selloSat="+selloSat+'}';
	}
	
}