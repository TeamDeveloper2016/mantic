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
@Table(name="tr_janal_menus_perfiles")
public class TcJanalMenusPerfilesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_PERFIL")
  private Long idPerfil;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="ID_MENU_GRUPO")
  private Long idMenuGrupo;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMenuPerfil_sequence")
  //@SequenceGenerator(name="idMenuPerfil_sequence",sequenceName="SEQ_TR_JANAL_MENUS_PERFILES" , allocationSize=1 )
  @Column (name="ID_MENU_PERFIL")
  private Long idMenuPerfil;

  public TcJanalMenusPerfilesDto() {
    this(new Long(-1L));
  }

  public TcJanalMenusPerfilesDto(Long key) {
    this(null, null, null, null);
    setKey(key);
  }

  public TcJanalMenusPerfilesDto(Long idPerfil, Long idUsuario, Long idMenuGrupo, Long idMenuPerfil) {
    setIdPerfil(idPerfil);
    setIdUsuario(idUsuario);
    setIdMenuGrupo(idMenuGrupo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdMenuPerfil(idMenuPerfil);
  }
	
  public void setIdPerfil(Long idPerfil) {
    this.idPerfil = idPerfil;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdMenuGrupo(Long idMenuGrupo) {
    this.idMenuGrupo = idMenuGrupo;
  }

  public Long getIdMenuGrupo() {
    return idMenuGrupo;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIdMenuPerfil(Long idMenuPerfil) {
    this.idMenuPerfil = idMenuPerfil;
  }

  public Long getIdMenuPerfil() {
    return idMenuPerfil;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMenuPerfil();
  }

  @Override
  public void setKey(Long key) {
  	this.idMenuPerfil = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMenuGrupo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMenuPerfil());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idPerfil", getIdPerfil());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idMenuGrupo", getIdMenuGrupo());
		regresar.put("registro", getRegistro());
		regresar.put("idMenuPerfil", getIdMenuPerfil());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdPerfil(), getIdUsuario(), getIdMenuGrupo(), getRegistro(), getIdMenuPerfil()
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
    regresar.append("idMenuPerfil~");
    regresar.append(getIdMenuPerfil());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMenuPerfil());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalMenusPerfilesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMenuPerfil()!= null && getIdMenuPerfil()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalMenusPerfilesDto other = (TcJanalMenusPerfilesDto) obj;
    if (getIdMenuPerfil() != other.idMenuPerfil && (getIdMenuPerfil() == null || !getIdMenuPerfil().equals(other.idMenuPerfil))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMenuPerfil() != null ? getIdMenuPerfil().hashCode() : 0);
    return hash;
  }

}


