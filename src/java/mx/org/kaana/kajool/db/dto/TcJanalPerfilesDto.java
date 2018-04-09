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
@Table(name="tc_janal_perfiles")
public class TcJanalPerfilesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idPerfil_sequence")
  //@SequenceGenerator(name="idPerfil_sequence",sequenceName="SEQ_TC_JANAL_PERFILES" , allocationSize=1 )
  @Column (name="ID_PERFIL")
  private Long idPerfil;
  @Column (name="ACCESO")
  private Long acceso;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="DESCRIPCION")
  private String descripcion;
  @Column (name="ID_MENU")
  private Long idMenu;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Column (name="ID_GRUPO")
  private Long idGrupo;

  public TcJanalPerfilesDto() {
    this(new Long(-1L));
  }

  public TcJanalPerfilesDto(Long key) {
    this(null, null, null, null, null, null);
    setKey(key);
  }

  public TcJanalPerfilesDto(Long idPerfil, Long acceso, Long idUsuario, String descripcion, Long idMenu, Long idGrupo) {
    setIdPerfil(idPerfil);
    setAcceso(acceso);
    setIdUsuario(idUsuario);
    setDescripcion(descripcion);
    setIdMenu(idMenu);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdGrupo(idGrupo);
  }
	
  public void setIdPerfil(Long idPerfil) {
    this.idPerfil = idPerfil;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setAcceso(Long acceso) {
    this.acceso = acceso;
  }

  public Long getAcceso() {
    return acceso;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdMenu(Long idMenu) {
    this.idMenu = idMenu;
  }

  public Long getIdMenu() {
    return idMenu;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIdGrupo(Long idGrupo) {
    this.idGrupo = idGrupo;
  }

  public Long getIdGrupo() {
    return idGrupo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdPerfil();
  }

  @Override
  public void setKey(Long key) {
  	this.idPerfil = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAcceso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMenu());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGrupo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idPerfil", getIdPerfil());
		regresar.put("acceso", getAcceso());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idMenu", getIdMenu());
		regresar.put("registro", getRegistro());
		regresar.put("idGrupo", getIdGrupo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdPerfil(), getAcceso(), getIdUsuario(), getDescripcion(), getIdMenu(), getRegistro(), getIdGrupo()
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
    regresar.append("idPerfil~");
    regresar.append(getIdPerfil());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPerfil());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalPerfilesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPerfil()!= null && getIdPerfil()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalPerfilesDto other = (TcJanalPerfilesDto) obj;
    if (getIdPerfil() != other.idPerfil && (getIdPerfil() == null || !getIdPerfil().equals(other.idPerfil))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPerfil() != null ? getIdPerfil().hashCode() : 0);
    return hash;
  }

}
