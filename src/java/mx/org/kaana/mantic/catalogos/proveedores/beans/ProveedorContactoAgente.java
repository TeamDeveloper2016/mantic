package mx.org.kaana.mantic.catalogos.proveedores.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorAgenteDto;

public class ProveedorContactoAgente extends TrManticProveedorAgenteDto implements Serializable{
	
	private static final long serialVersionUID = 4187548310102440062L;
	private ESql sqlAccion;
	private Boolean nuevo;
	private Boolean principal;
	private String nombres;
	private String paterno;
	private String materno;
	private List<PersonaTipoContacto> contactos;
	private Long consecutivo;
	private Boolean modificar;

	public ProveedorContactoAgente() {
		this(-1L);
	}

	public ProveedorContactoAgente(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ProveedorContactoAgente(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ProveedorContactoAgente(Long key, ESql sqlAccion, Boolean nuevo) {
		this(key, sqlAccion, nuevo, false, null, new ArrayList<PersonaTipoContacto>(), null, null, null, false);
	}
	
	public ProveedorContactoAgente(Long key, ESql sqlAccion, Boolean nuevo, Boolean principal, String nombres, List<PersonaTipoContacto> contactos, Long consecutivo, String paterno, String materno, Boolean modificar) {
		super(key);
		this.sqlAccion  = sqlAccion;
		this.nuevo      = nuevo;
		this.principal  = principal;					
		this.nombres    = nombres;
		this.contactos  = contactos;
		this.consecutivo= consecutivo;
		this.paterno    = paterno;
		this.materno    = materno;
		this.modificar  = modificar;
	}

	public ESql getSqlAccion() {
		return sqlAccion;
	}

	public void setSqlAccion(ESql sqlAccion) {
		this.sqlAccion = sqlAccion;
	}

	public Boolean getNuevo() {
		return nuevo;
	}

	public void setNuevo(Boolean nuevo) {
		this.nuevo = nuevo;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}
	
	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	
	public List<PersonaTipoContacto> getContactos() {
		return contactos;
	}

	public void setContactos(List<PersonaTipoContacto> contactos) {
		this.contactos = contactos;
	}

	public Long getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getPaterno() {
		return paterno;
	}

	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}

	public String getMaterno() {
		return materno;
	}

	public void setMaterno(String materno) {
		this.materno = materno;
	}

	public Boolean getModificar() {
		return modificar;
	}

	public void setModificar(Boolean modificar) {
		this.modificar = modificar;
	}
}
