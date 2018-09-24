package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
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
	private String importe;
	private String estatus;
	private String registro;	
	private String factura;	
	private String fechaFactura;	
	private boolean ultimoNivel;  

	public TreeOrden() {
		this(-1L);
	}
	
	public TreeOrden(Long id) {
		this(id, false);
	}	
	
	public TreeOrden(Long id, boolean ultimoNivel) {
		this(id, null, null, null, null, null, null, null, null, ultimoNivel, null, null);
	}	

	public TreeOrden(Long id, String claveEmpresa, String nombreEmpresa, String tituloEmpresa, String consecutivo, String proveedor, String importe, String estatus, String registro, boolean ultimoNivel, String factura, String fechaFactura) {
		this.id           = id;
		this.claveEmpresa = claveEmpresa;
		this.nombreEmpresa= nombreEmpresa;
		this.tituloEmpresa= tituloEmpresa;
		this.consecutivo  = consecutivo;
		this.proveedor    = proveedor;
		this.importe      = importe;
		this.estatus      = estatus;
		this.registro     = registro;
		this.ultimoNivel  = ultimoNivel;
		this.factura      = factura;
		this.fechaFactura = fechaFactura;
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

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
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

	public String getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura=fechaFactura;
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
		return "TreeOrden{"+"id="+id+", tipo="+tipo+", claveEmpresa="+claveEmpresa+", nombreEmpresa="+nombreEmpresa+", tituloEmpresa="+tituloEmpresa+", consecutivo="+consecutivo+", proveedor="+proveedor+", importe="+importe+", estatus="+estatus+", registro="+registro+", factura="+factura+", fechaFactura="+fechaFactura+", ultimoNivel="+ultimoNivel+'}';
	}

}
