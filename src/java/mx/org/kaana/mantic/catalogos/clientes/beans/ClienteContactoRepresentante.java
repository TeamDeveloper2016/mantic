package mx.org.kaana.mantic.catalogos.clientes.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.db.dto.TrManticClienteRepresentanteDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClienteContactoRepresentante extends TrManticClienteRepresentanteDto implements Serializable{
	
	private static final long serialVersionUID = -5420245452200987754L;
  private static final Log LOG = LogFactory.getLog(ClienteContactoRepresentante.class);
  
  private static final String[] CONTRASENIAS = {
    "m4nzan4", "m4rtill0", "el3ctr0n", "silic0n", "cerr0j0", "3scuel4", 
    "av3n1da", "c4ndado", "esc4l3ra", "b1cicl3ta", "armidill0", "br1nc0lin", 
    "c4l3ndario", "m1ercole$", "c0mplicad0", "d1ficil", "r0mp3r", "s4lud4r",
    "ec0nom1co", "s0ldadur4", "pod$dor4", "t4lach3", "c0ncr3to", "solt4r",
    "esc0ba$", "c3ment0", "s0ld4dura", "p3gam3nto", "cl4vo$", "p1nza$"
  };
  
	private ESql sqlAccion;
	private Boolean nuevo;
	private Boolean principal;
	private String nombres;
	private String paterno;
	private String materno;
	private List<PersonaTipoContacto> contactos;
	private Long consecutivo;
	private Boolean modificar;
  private Long idPersona;
  private Long idCuenta;
  private Long crear;
  private String cuenta;
  private String contrasenia;
  private Long activo;

	public ClienteContactoRepresentante() {
		this(-1L);
	}

	public ClienteContactoRepresentante(Long key) {
		this(key, ESql.UPDATE);
	}
	
	public ClienteContactoRepresentante(Long key, ESql sqlAccion) {
		this(key, sqlAccion, false);
	}
	
	public ClienteContactoRepresentante(Long key, ESql sqlAccion, Boolean nuevo) {
		this(key, sqlAccion, nuevo, false, null, new ArrayList<PersonaTipoContacto>(), null, null, null, false);
	}
	
	public ClienteContactoRepresentante(Long key, ESql sqlAccion, Boolean nuevo, Boolean principal, String nombres, List<PersonaTipoContacto> contactos, Long consecutivo, String paterno, String materno, Boolean modificar) {
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
    this.crear      = 2L;
    this.cuenta     = "";
    this.contrasenia= "";
    this.idPersona  = -1L;
    this.idCuenta   = -1L;
    this.activo     = 2L;
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

  public Long getCrear() {
    return crear;
  }

  public void setCrear(Long crear) {
    this.crear = crear;
  }

  public String getCuenta() {
    return cuenta;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  public String getContrasenia() {
    return contrasenia;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public Long getIdPersona() {
    return idPersona;
  }

  public void setIdPersona(Long idPersona) {
    this.idPersona = idPersona;
  }

  public Long getIdCuenta() {
    return idCuenta;
  }

  public void setIdCuenta(Long idCuenta) {
    this.idCuenta = idCuenta;
  }

  public Long getActivo() {
    return activo;
  }

  public void setActivo(Long activo) {
    this.activo = activo;
  }

  public String toContrasenia() {
    int minimum= 0;
    int maximum= CONTRASENIAS.length;
    int index= minimum + (int)(Math.random() * (maximum - minimum));
    return CONTRASENIAS[index];
  }
  
  public String toCuenta() {
    return "cliente.".concat(Cadena.rellenar(String.valueOf(this.idPersona), 4, '0', true));  
  }
  
  public static void main(String ... args) {
    ClienteContactoRepresentante cliente= new ClienteContactoRepresentante();
    LOG.info(cliente.toCuenta());    
    for (int x = 0; x < 10; x++) {
      LOG.info(cliente.toContrasenia());    
    } // for
  }
  
}