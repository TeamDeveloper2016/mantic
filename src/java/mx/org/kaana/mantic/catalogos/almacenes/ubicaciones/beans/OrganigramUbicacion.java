package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.mantic.enums.ENivelUbicacion;

public class OrganigramUbicacion implements Serializable, IBaseDto {

	private static final long serialVersionUID=4222652089397624991L;

	private Long idKey;	
	private Long idAlmacen;		
	private String idEmpresa;		
	private String empresa;				
	private String almacen;				
	private String piso;				
	private String cuarto;						
	private String anaquel;					
	private String charola;					
	private String descripcion;					
	private boolean ultimoNivel;  	
	private ENivelUbicacion nivel;
	private Timestamp registro;			
	private Long articulos;
	private Long idAlmacenUbicacion;

	public OrganigramUbicacion() {
		this(-1L);
	} // OrganigramUbicacion

	public OrganigramUbicacion(Long idKey) {
		this(idKey, null, -1L, "ALMACENES (ESTRUCTURA)", null, null, null, null, null, false, ENivelUbicacion.EMPRESA, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, 0L, -1L);
	}	// OrganigramUbicacion
	
	public OrganigramUbicacion(Long idKey, String idEmpresa, Long idAlmacen, String empresa, String almacen, String piso, String cuarto, String anaquel, String charola, boolean ultimoNivel, ENivelUbicacion nivel, Timestamp registro, String descripcion, Long articulos, Long idAlmacenUbicacion) {
		this.idKey      = idKey;
		this.idEmpresa  = idEmpresa;
		this.idAlmacen  = idAlmacen;
		this.empresa    = empresa;
		this.almacen    = almacen;
		this.piso       = piso;
		this.cuarto     = cuarto;
		this.anaquel    = anaquel;
		this.charola    = charola;
		this.ultimoNivel= ultimoNivel;
		this.nivel      = nivel;
		this.registro   = registro;
		this.descripcion= descripcion;
		this.articulos  = articulos;
		this.idAlmacenUbicacion= idAlmacenUbicacion;
	}	// OrganigramUbicacion	
	
	@Override
	public Long getKey() {
		return this.idKey;
	}

	@Override
	public void setKey(Long key) {
		this.idKey= key;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(Long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getAlmacen() {
		return almacen;
	}

	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getCuarto() {
		return cuarto;
	}

	public void setCuarto(String cuarto) {
		this.cuarto = cuarto;
	}

	public String getAnaquel() {
		return anaquel;
	}

	public void setAnaquel(String anaquel) {
		this.anaquel = anaquel;
	}

	public String getCharola() {
		return charola;
	}

	public void setCharola(String charola) {
		this.charola = charola;
	}

	public boolean isUltimoNivel() {
		return ultimoNivel;
	}

	public void setUltimoNivel(boolean ultimoNivel) {
		this.ultimoNivel = ultimoNivel;
	}

	public ENivelUbicacion getNivel() {
		return nivel;
	}

	public void setNivel(ENivelUbicacion nivel) {
		this.nivel = nivel;
	}

	public Timestamp getRegistro() {
		return registro;
	}

	public void setRegistro(Timestamp registro) {
		this.registro = registro;
	}	

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	

	public Long getArticulos() {
		return articulos;
	}

	public void setArticulos(Long articulos) {
		this.articulos = articulos;
	}	

	public Long getIdAlmacenUbicacion() {
		return idAlmacenUbicacion;
	}

	public void setIdAlmacenUbicacion(Long idAlmacenUbicacion) {
		this.idAlmacenUbicacion = idAlmacenUbicacion;
	}	
	
	@Override
	public Map<String, Object> toMap() {
		Map regresar = new HashMap();
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("empresa", getEmpresa());
		regresar.put("almacen", getAlmacen());
		regresar.put("piso", getPiso());
		regresar.put("cuarto", getCuarto());
		regresar.put("anaquel", getAnaquel());
		regresar.put("charola", getCharola());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idAlmacenUbicacion", getIdAlmacenUbicacion());
  	return regresar;
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
		return idKey.toString();
	} // toString
}