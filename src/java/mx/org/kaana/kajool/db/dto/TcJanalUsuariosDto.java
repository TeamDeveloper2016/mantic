package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
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
@Table(name="tc_janal_usuarios")
public class TcJanalUsuariosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_PERFIL")
  private Long idPerfil;
  @Column (name="ID_USUARIO_MODIFICA")
  private Long idUsuarioModifica;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idUsuario_sequence")
  //@SequenceGenerator(name="idUsuario_sequence",sequenceName="SEQ_TC_JANAL_USUARIOS" , allocationSize=1 )
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="ACTIVO")
  private Long activo;
  @Column (name="REGISTRO")
  private Timestamp registro;	
	@Column (name="ID_EMPLEADO")
  private Long idEmpleado;
	@Column (name="ID_ENTIDAD")
  private Long idEntidad;

  public TcJanalUsuariosDto() {
    this(new Long(-1L));
  }

  public TcJanalUsuariosDto(Long key) {
    this(null, null, null, null, null, null);
    setKey(key);
  }

  public TcJanalUsuariosDto(Long idPerfil, Long idUsuarioModifica, Long idUsuario, Long activo, Long idEmpleado, Long idEntidad) {
    setIdPerfil(idPerfil);
    setIdUsuarioModifica(idUsuarioModifica);
    setIdUsuario(idUsuario);
    setActivo(activo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setIdEmpleado(idEmpleado);
  }

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado=idEmpleado;
	}

	public Long getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(Long idEntidad) {
		this.idEntidad=idEntidad;
	}
	
  public void setIdPerfil(Long idPerfil) {
    this.idPerfil = idPerfil;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setIdUsuarioModifica(Long idUsuarioModifica) {
    this.idUsuarioModifica = idUsuarioModifica;
  }

  public Long getIdUsuarioModifica() {
    return idUsuarioModifica;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setActivo(Long activo) {
    this.activo = activo;
  }

  public Long getActivo() {
    return activo;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdUsuario();
  }

  @Override
  public void setKey(Long key) {
  	this.idUsuario = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);		
		regresar.append(getIdUsuarioModifica());
		regresar.append(Constantes.SEPARADOR);		
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getActivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());		
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idPerfil", getIdPerfil());		
		regresar.put("idUsuarioModifica", getIdUsuarioModifica());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("activo", getActivo());
		regresar.put("registro", getRegistro());		
		regresar.put("idEntidad", getIdEntidad());
		regresar.put("idEmpleado", getIdEmpleado());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdPerfil(), getIdEmpleado(), getIdEntidad(), getIdUsuarioModifica(), getIdUsuario(), getActivo(), getRegistro()
    };
    return regresar;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idUsuario~");
    regresar.append(getIdUsuario());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdUsuario());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalUsuariosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdUsuario()!= null && getIdUsuario()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalUsuariosDto other = (TcJanalUsuariosDto) obj;
    if (getIdUsuario() != other.idUsuario && (getIdUsuario() == null || !getIdUsuario().equals(other.idUsuario))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdUsuario() != null ? getIdUsuario().hashCode() : 0);
    return hash;
  }

}


