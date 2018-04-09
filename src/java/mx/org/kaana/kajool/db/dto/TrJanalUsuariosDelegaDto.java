package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 10:51:18 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

@Entity
@Table(name = "tr_janal_usuarios_delega")
public class TrJanalUsuariosDelegaDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=1L;
	@Column(name = "ID_EMPLEADO")
	private Long idEmpleado;
	@Column(name = "LOGIN")
	private String login;	
	@Column(name = "CONTRASENIA")
	private String contrasenia;	
	@Column(name = "ID_USUARIO")
	private Long idUsuario;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idUsuarioDelega_sequence")
	//@SequenceGenerator(name = "idUsuarioDelega_sequence", sequenceName = "SEQ_TR_JANAL_USUARIOS_DELEGA", allocationSize = 1)
	@Column(name = "ID_USUARIO_DELEGA")
	private Long idUsuarioDelega;
	@Column(name = "VIGENCIA_FIN")
	private Date vigenciaFin;
	@Column(name = "ACTIVO")
	private Long activo;
	@Column(name = "VIGENCIA_INI")
	private Date vigenciaIni;
	@Column(name = "REGISTRO")
	private Timestamp registro;

	public TrJanalUsuariosDelegaDto() {
		this(new Long(-1L));
	}

	public TrJanalUsuariosDelegaDto(Long key) {
		this(null, null, null, null, new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), null, new Date(Calendar.getInstance().getTimeInMillis()));
		setKey(key);
	}

	public TrJanalUsuariosDelegaDto(Long idEmpleado, String contrasenia, String login, Long idUsuario, Long idUsuarioDelega, Date vigenciaFin, Long activo, Date vigenciaIni) {
		setIdEmpleado(idEmpleado);
		setContrasenia(contrasenia);
		setLogin(login);
		setIdUsuario(idUsuario);
		setIdUsuarioDelega(idUsuarioDelega);
		setVigenciaFin(vigenciaFin);
		setActivo(activo);
		setVigenciaIni(vigenciaIni);
		setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}

  public Long getIdEmpleado() {
    return idEmpleado;
  }

  public void setIdEmpleado(Long idEmpleado) {
    this.idEmpleado = idEmpleado;
  }

	public void setLogin(String login) {
		this.login=login;
	}

	public String getLogin() {
		return login;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario=idUsuario;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuarioDelega(Long idUsuarioDelega) {
		this.idUsuarioDelega=idUsuarioDelega;
	}

	public Long getIdUsuarioDelega() {
		return idUsuarioDelega;
	}

	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin=vigenciaFin;
	}

	public Date getVigenciaFin() {
		return vigenciaFin;
	}

	public void setActivo(Long activo) {
		this.activo=activo;
	}

	public Long getActivo() {
		return activo;
	}

	public void setVigenciaIni(Date vigenciaIni) {
		this.vigenciaIni=vigenciaIni;
	}

	public Date getVigenciaIni() {
		return vigenciaIni;
	}

	public void setRegistro(Timestamp registro) {
		this.registro=registro;
	}

	public Timestamp getRegistro() {
		return registro;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia=contrasenia;
	}

	@Transient
	@Override
	public Long getKey() {
		return getIdUsuarioDelega();
	}

	@Override
	public void setKey(Long key) {
		this.idUsuarioDelega=key;
	}

	@Override
	public String toString() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("[");
		regresar.append(getIdEmpleado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getContrasenia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLogin());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuarioDelega());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigenciaFin());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getActivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVigenciaIni());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idEmpleado", getIdEmpleado());
		regresar.put("numEmpleado", getContrasenia());
		regresar.put("login", getLogin());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idUsuarioDelega", getIdUsuarioDelega());
		regresar.put("vigenciaFin", getVigenciaFin());
		regresar.put("activo", getActivo());
		regresar.put("vigenciaIni", getVigenciaIni());
		regresar.put("registro", getRegistro());
		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdEmpleado(), getContrasenia(), getLogin(), getIdUsuario(), getIdUsuarioDelega(), getVigenciaFin(), getActivo(), getVigenciaIni(), getRegistro()
		};
		return regresar;
	}

	@Override
	public Object toValue(String name) {
		return Methods.getValue(this, name);
	}

	@Override
	public String toAllKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("|");
		regresar.append("idUsuarioDelega~");
		regresar.append(getIdUsuarioDelega());
		regresar.append("|");
		return regresar.toString();
	}

	@Override
	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(getIdUsuarioDelega());
		return regresar.toString();
	}

	@Override
	public Class toHbmClass() {
		return TrJanalUsuariosDelegaDto.class;
	}

	@Override
	public boolean isValid() {
		return getIdUsuarioDelega()!=null&&getIdUsuarioDelega()!=-1L;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final TrJanalUsuariosDelegaDto other=(TrJanalUsuariosDelegaDto) obj;
		if (getIdUsuarioDelega()!=other.idUsuarioDelega&&(getIdUsuarioDelega()==null||!getIdUsuarioDelega().equals(other.idUsuarioDelega))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(getIdUsuarioDelega()!=null ? getIdUsuarioDelega().hashCode() : 0);
		return hash;
	}
}
