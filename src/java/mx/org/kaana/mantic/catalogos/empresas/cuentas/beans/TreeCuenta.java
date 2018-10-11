package mx.org.kaana.mantic.catalogos.empresas.cuentas.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

public class TreeCuenta implements Serializable, IBaseDto {

	private static final long serialVersionUID=4222652089397624991L;

	private Long idKey;
	private String consecutivo;
	private Double importe;
	private Double saldo;
	private Double pago;
	private Long idEmpresa;
	private Long idEmpresaEstatus;
	private String persona;	
	private Timestamp registro;	
	private String proveedor;	
	private String almacen;				
	private String clave;				
	private String nombre;				
	private Long dias;
	private boolean ultimoNivel;  

	public TreeCuenta() {
		this(-1L);
	}
	
	public TreeCuenta(Long idKey) {
		this(idKey, false);
	}	
	
	public TreeCuenta(Long idKey, boolean ultimoNivel) {
		this(idKey, null, null, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, ultimoNivel);
	}	

	public TreeCuenta(Long idKey, String consecutivo, Double importe, Double saldo, Double pago, Long idEmpresa, Long idEmpresaEstatus, String persona, Timestamp registro, String proveedor, String almacen, String clave, String nombre, Long dias, boolean ultimoNivel) {
		this.idKey = idKey;
		this.consecutivo = consecutivo;
		this.importe = importe;
		this.saldo = saldo;
		this.pago = pago;
		this.idEmpresa = idEmpresa;
		this.idEmpresaEstatus = idEmpresaEstatus;
		this.persona = persona;
		this.registro = registro;
		this.proveedor = proveedor;
		this.almacen = almacen;
		this.clave = clave;
		this.nombre = nombre;
		this.dias = dias;
		this.ultimoNivel = ultimoNivel;
	}

	public Long getIdKey() {
		return idKey;
	}

	public void setIdKey(Long idKey) {
		this.idKey = idKey;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getPago() {
		return pago;
	}

	public void setPago(Double pago) {
		this.pago = pago;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdEmpresaEstatus() {
		return idEmpresaEstatus;
	}

	public void setIdEmpresaEstatus(Long idEmpresaEstatus) {
		this.idEmpresaEstatus = idEmpresaEstatus;
	}

	public String getPersona() {
		return persona;
	}

	public void setPersona(String persona) {
		this.persona = persona;
	}

	public Timestamp getRegistro() {
		return registro;
	}

	public void setRegistro(Timestamp registro) {
		this.registro = registro;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getAlmacen() {
		return almacen;
	}

	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getDias() {
		return dias;
	}

	public void setDias(Long dias) {
		this.dias = dias;
	}

	public boolean isUltimoNivel() {
		return ultimoNivel;
	}

	public void setUltimoNivel(boolean ultimoNivel) {
		this.ultimoNivel = ultimoNivel;
	}	

	@Override
	public Long getKey() {
		return this.idKey;
	}

	@Override
	public void setKey(Long key) {
		this.idKey= key;
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
		return "Mindmap {"
					+ "idKey="+idKey+", "
					+ "consecutivo="+consecutivo+", "
					+ "importe="+importe+", "
					+ "saldo="+saldo+", "
					+ "pago="+pago+", "
					+ "idEmpresa="+idEmpresa+", "
					+ "idEmpresaEstatus="+idEmpresaEstatus+", "					
					+ "persona="+persona+", "
					+ "registro="+registro+", "
					+ "proveedor="+proveedor+", "										
					+ "almacen="+almacen+", "
					+ "clave="+clave+", "
					+ "nombre="+nombre+", "
					+ "dias="+dias+", "					
					+ "ultimoNivel="+ultimoNivel+'}';
	} // toString
}