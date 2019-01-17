package mx.org.kaana.mantic.facturas.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

public class ClienteFactura implements Serializable, IBaseDto {

	private static final long serialVersionUID = -6145604204242433218L;
	//Cliente
	private Long id;	
	private String idFacturama;
	private String correo;
	private String rfc;
	private String nombre;
	private String tipoCfdi;
	// Cfdi
	private Long idFactura;
	private String observaciones;
	private String abreviaturaCfdi;
	private String medioPago;
	private String metodoPago;
	private String usoCfdi;
	//Direccion
	private String calle;
	private String numeroExterior;
	private String numeroInterior;
	private String colonia;
	private String codigoPostal;
	private String localidad;
	private String municipio;
	private String estado;
	private String pais;

	public ClienteFactura() {
		this(null);
	}	

	public ClienteFactura(Long id) {
		this(id, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	public ClienteFactura(Long id, String idFacturama, String correo, String rfc, String nombre, String tipoCfdi, String calle, String numeroExterior, String numeroInterior, String colonia, String codigoPostal, String localidad, String municipio, String estado, String pais) {
		this(id, idFacturama, correo, rfc, nombre, tipoCfdi, calle, numeroExterior, numeroInterior, colonia, codigoPostal, localidad, municipio, estado, pais, null, null, null, null, null, null);
	}
	
	public ClienteFactura(Long id, String idFacturama, String correo, String rfc, String nombre, String tipoCfdi, String calle, String numeroExterior, String numeroInterior, String colonia, String codigoPostal, String localidad, String municipio, String estado, String pais, Long idFactura, String observaciones, String abreviaturaCfdi, String medioPago, String metodoPago, String usoCfdi) {
		this.id             = id;
		this.idFacturama    = idFacturama;
		this.correo         = correo;
		this.rfc            = rfc;
		this.nombre         = nombre;
		this.tipoCfdi       = tipoCfdi;
		this.calle          = calle;
		this.numeroExterior = numeroExterior;
		this.numeroInterior = numeroInterior;
		this.colonia        = colonia;
		this.codigoPostal   = codigoPostal;
		this.localidad      = localidad;
		this.municipio      = municipio;
		this.estado         = estado;
		this.pais           = pais;
		this.idFactura      = idFactura;
		this.observaciones  = observaciones;
		this.abreviaturaCfdi= abreviaturaCfdi;
		this.medioPago      = medioPago;
		this.metodoPago     = metodoPago;
		this.usoCfdi        = usoCfdi;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoCfdi() {
		return tipoCfdi;
	}

	public void setTipoCfdi(String tipoCfdi) {
		this.tipoCfdi = tipoCfdi;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumeroExterior() {
		return numeroExterior;
	}

	public void setNumeroExterior(String numeroExterior) {
		this.numeroExterior = numeroExterior;
	}

	public String getNumeroInterior() {
		return numeroInterior;
	}

	public void setNumeroInterior(String numeroInterior) {
		this.numeroInterior = numeroInterior;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}	

	public String getIdFacturama() {
		return idFacturama;
	}

	public void setIdFacturama(String idFacturama) {
		this.idFacturama = idFacturama;
	}	

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getAbreviaturaCfdi() {
		return abreviaturaCfdi;
	}

	public void setAbreviaturaCfdi(String abreviaturaCfdi) {
		this.abreviaturaCfdi = abreviaturaCfdi;
	}

	public String getMedioPago() {
		return medioPago;
	}

	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getUsoCfdi() {
		return usoCfdi;
	}

	public void setUsoCfdi(String usoCfdi) {
		this.usoCfdi = usoCfdi;
	}	
	
	@Override
	public int hashCode() {
		int hash=5;
		hash=29*hash+Objects.hashCode(this.rfc);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final ClienteFactura other=(ClienteFactura) obj;
		if (!Objects.equals(this.rfc, other.rfc)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getKey() {
		if(id!= null)
			return id;
		else
			return -1L;
	}

	@Override
	public void setKey(Long key) {
		this.id= key;
	}

	@Override
	public Map<String, Object> toMap() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public boolean isValid() {
		return this.id!= null && Long.valueOf(this.id) > -1L;
	}

	@Override
	public Object toValue(String name) {
		return null;
	}

	@Override
	public String toAllKeys() {
		return null;
	}

	@Override
	public String toKeys() {
		return null;
	}

	@Override
	public Class toHbmClass() {
		return ClienteFactura.class;
	}
}
