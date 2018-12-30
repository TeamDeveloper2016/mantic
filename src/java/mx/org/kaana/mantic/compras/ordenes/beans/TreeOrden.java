package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.mantic.enums.EDocumentosOrden;

public class TreeOrden implements Serializable, IBaseDto {

	private static final long serialVersionUID=4222652089397624991L;

	private Long id;
	private EDocumentosOrden tipo;
	private String claveEmpresa;
	private String nombreEmpresa;
	private String tituloEmpresa;
	private String consecutivo;
	private String proveedor;
	private Double importe;
	private String estatus;
	private Timestamp registro;	
	private String factura;	
	private Timestamp fechaFactura;	
	private Timestamp fechaRecepcion;	
	private Timestamp fechaDocumento;	
	private Timestamp fechaEntrega;	
	private String folio;	
	private String almacen;	
	private String persona;	
	private boolean ultimoNivel;  
	private Long idNotaEntrada;

	public TreeOrden() {
		this(-1L);
	}
	
	public TreeOrden(Long id) {
		this(id, false);
	}	
	
	public TreeOrden(Long id, boolean ultimoNivel) {
		this(id, null, null, null, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, new Timestamp(Calendar.getInstance().getTimeInMillis()), new Timestamp(Calendar.getInstance().getTimeInMillis()), new Timestamp(Calendar.getInstance().getTimeInMillis()), new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, ultimoNivel);
	}	

	public TreeOrden(Long id, EDocumentosOrden tipo, String claveEmpresa, String nombreEmpresa, String tituloEmpresa, String consecutivo, String proveedor, Double importe, String estatus, Timestamp registro, String factura, Timestamp fechaFactura, Timestamp fechaRecepcion, Timestamp fechaDocumento, Timestamp fechaEntrega, String folio, String almacen, boolean ultimoNivel) {
		this.id=id;
		this.tipo=tipo;
		this.claveEmpresa=claveEmpresa;
		this.nombreEmpresa=nombreEmpresa;
		this.tituloEmpresa=tituloEmpresa;
		this.consecutivo=consecutivo;
		this.proveedor=proveedor;
		this.importe=importe;
		this.estatus=estatus;
		this.registro=registro;
		this.factura=factura;
		this.fechaFactura=fechaFactura;
		this.fechaRecepcion=fechaRecepcion;
		this.fechaDocumento=fechaDocumento;
		this.fechaEntrega=fechaEntrega;
		this.folio=folio;
		this.almacen=almacen;
		this.ultimoNivel=ultimoNivel;
		this.idNotaEntrada= -1L;
	}


	public String getClaveEmpresa() {
		return claveEmpresa;
	}

	public void setClaveEmpresa(String claveEmpresa) {
		this.claveEmpresa = claveEmpresa;
	}

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getTituloEmpresa() {
		return tituloEmpresa;
	}

	public void setTituloEmpresa(String tituloEmpresa) {
		this.tituloEmpresa = tituloEmpresa;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
	
	public boolean isUltimoNivel() {
		return ultimoNivel;
	}

	public void setUltimoNivel(boolean ultimoNivel) {
		this.ultimoNivel = ultimoNivel;
	}	

	public EDocumentosOrden getTipo() {
		return tipo;
	}

	public void setTipo(EDocumentosOrden tipo) {
		this.tipo = tipo;
	}

	public String getFactura() {
		return factura;
	}

	public void setFactura(String factura) {
		this.factura=factura;
	}

	public Timestamp getRegistro() {
		return registro;
	}

	public void setRegistro(Timestamp registro) {
		this.registro=registro;
	}

	public Timestamp getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(Timestamp fechaFactura) {
		this.fechaFactura=fechaFactura;
	}

	public Timestamp getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Timestamp fechaRecepcion) {
		this.fechaRecepcion=fechaRecepcion;
	}

	public Timestamp getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(Timestamp fechaDocumento) {
		this.fechaDocumento=fechaDocumento;
	}

	public Timestamp getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Timestamp fechaEntrega) {
		this.fechaEntrega=fechaEntrega;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio=folio;
	}

	public String getAlmacen() {
		return almacen;
	}

	public void setAlmacen(String almacen) {
		this.almacen=almacen;
	}

	public String getPersona() {
		return persona;
	}

	public void setPersona(String persona) {
		this.persona=persona;
	}

	public Long getIdNotaEntrada() {
		return idNotaEntrada;
	}

	public void setIdNotaEntrada(Long idNotaEntrada) {
		this.idNotaEntrada=idNotaEntrada;
	}

	@Override
	public Long getKey() {
		return this.id;
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
		return getKey()!= null && !getKey().equals(-1L);
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
		return null;
	}

	@Override
	public String toString() {
		return "TreeOrden{"+"id="+id+", tipo="+tipo+", claveEmpresa="+claveEmpresa+", nombreEmpresa="+nombreEmpresa+", tituloEmpresa="+tituloEmpresa+", consecutivo="+consecutivo+", proveedor="+proveedor+", importe="+importe+", estatus="+estatus+", registro="+registro+", factura="+factura+", fechaFactura="+fechaFactura+", fechaRecepcion="+fechaRecepcion+", fechaDocumento="+fechaDocumento+", fechaEntrega="+fechaEntrega+", folio="+folio+", almacen="+almacen+", persona="+persona+", ultimoNivel="+ultimoNivel+", idNotaEntrada="+ idNotaEntrada+ '}';
	}

}
