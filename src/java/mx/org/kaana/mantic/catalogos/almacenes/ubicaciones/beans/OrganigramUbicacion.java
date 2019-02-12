package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

public class OrganigramUbicacion implements Serializable, IBaseDto {

	private static final long serialVersionUID=4222652089397624991L;

	private Long idKey;
	private Long idEmpresa;	
	private String consecutivo;		
	private String clave;				
	private String nombre;					
	private String persona;		
	private String almacen;					
	private boolean ultimoNivel;  
	private Timestamp registro;		

	public OrganigramUbicacion() {
		this(-1L);
	}
	
	public OrganigramUbicacion(Long idKey) {
		this(idKey, false);
	}	
	
	public OrganigramUbicacion(Long idKey, boolean ultimoNivel) {
		this(idKey, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, ultimoNivel);
	}	

	public OrganigramUbicacion(Long idKey, String consecutivo, Long idEmpresa, String persona, Timestamp registro, String almacen, String clave, String nombre, boolean ultimoNivel) {
		this.idKey      = idKey;
		this.consecutivo= consecutivo;		
		this.idEmpresa  = idEmpresa;		
		this.persona    = persona;
		this.registro   = registro;		
		this.almacen    = almacen;
		this.clave      = clave;
		this.nombre     = nombre;		
		this.ultimoNivel= ultimoNivel;
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

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
					+ "idEmpresa="+idEmpresa+", "
					+ "persona="+persona+", "
					+ "registro="+registro+", "
					+ "almacen="+almacen+", "
					+ "clave="+clave+", "
					+ "nombre="+nombre+", "
					+ "ultimoNivel="+ultimoNivel+'}';
	} // toString
}