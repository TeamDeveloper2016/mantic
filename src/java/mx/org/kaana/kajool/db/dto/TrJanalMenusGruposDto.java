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
@Table(name="tr_janal_menus_grupos")
public class TrJanalMenusGruposDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMenuGrupo_sequence")
  //@SequenceGenerator(name="idMenuGrupo_sequence",sequenceName="SEQ_TR_JANAL_MENUS_GRUPOS" , allocationSize=1 )
  @Column (name="ID_MENU_GRUPO")
  private Long idMenuGrupo;
  @Column (name="ID_MENU")
  private Long idMenu;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Column (name="ID_GRUPO")
  private Long idGrupo;

  public TrJanalMenusGruposDto() {
    this(new Long(-1L));
  }

  public TrJanalMenusGruposDto(Long key) {
    this(null, null, null, null);
    setKey(key);
  }

  public TrJanalMenusGruposDto(Long idUsuario, Long idMenuGrupo, Long idMenu, Long idGrupo) {
    setIdUsuario(idUsuario);
    setIdMenuGrupo(idMenuGrupo);
    setIdMenu(idMenu);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdGrupo(idGrupo);
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
  	return getIdMenuGrupo();
  }

  @Override
  public void setKey(Long key) {
  	this.idMenuGrupo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMenuGrupo());
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
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idMenuGrupo", getIdMenuGrupo());
		regresar.put("idMenu", getIdMenu());
		regresar.put("registro", getRegistro());
		regresar.put("idGrupo", getIdGrupo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdMenuGrupo(), getIdMenu(), getRegistro(), getIdGrupo()
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
    regresar.append("idMenuGrupo~");
    regresar.append(getIdMenuGrupo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMenuGrupo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalMenusGruposDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMenuGrupo()!= null && getIdMenuGrupo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalMenusGruposDto other = (TrJanalMenusGruposDto) obj;
    if (getIdMenuGrupo() != other.idMenuGrupo && (getIdMenuGrupo() == null || !getIdMenuGrupo().equals(other.idMenuGrupo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMenuGrupo() != null ? getIdMenuGrupo().hashCode() : 0);
    return hash;
  }

}
