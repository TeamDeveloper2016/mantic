package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
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
@Table(name="tr_janal_menus_enc_perfil")
public class TrJanalMenusEncPerfilDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_MENU_ENCABEZADO")
  private Long idMenuEncabezado;
  @Column (name="ID_PERFIL")
  private Long idPerfil;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idMenuEncPerfil_sequence")
  //@SequenceGenerator(name="idMenuEncPerfil_sequence",sequenceName="SEQ_TR_JANAL_MENUS_ENC_PERFIL" , allocationSize=1 )
  @Column (name="ID_MENU_ENC_PERFIL")
  private Long idMenuEncPerfil;

  public TrJanalMenusEncPerfilDto() {
    this(new Long(-1L));
  }

  public TrJanalMenusEncPerfilDto(Long key) {
    this(null, null, null);
    setKey(key);
  }

  public TrJanalMenusEncPerfilDto(Long idMenuEncabezado, Long idPerfil, Long idMenuEncPerfil) {
    setIdMenuEncabezado(idMenuEncabezado);
    setIdPerfil(idPerfil);
    setIdMenuEncPerfil(idMenuEncPerfil);
  }
	
  public void setIdMenuEncabezado(Long idMenuEncabezado) {
    this.idMenuEncabezado = idMenuEncabezado;
  }

  public Long getIdMenuEncabezado() {
    return idMenuEncabezado;
  }

  public void setIdPerfil(Long idPerfil) {
    this.idPerfil = idPerfil;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setIdMenuEncPerfil(Long idMenuEncPerfil) {
    this.idMenuEncPerfil = idMenuEncPerfil;
  }

  public Long getIdMenuEncPerfil() {
    return idMenuEncPerfil;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMenuEncPerfil();
  }

  @Override
  public void setKey(Long key) {
  	this.idMenuEncPerfil = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdMenuEncabezado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMenuEncPerfil());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idMenuEncabezado", getIdMenuEncabezado());
		regresar.put("idPerfil", getIdPerfil());
		regresar.put("idMenuEncPerfil", getIdMenuEncPerfil());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdMenuEncabezado(), getIdPerfil(), getIdMenuEncPerfil()
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
    regresar.append("idMenuEncPerfil~");
    regresar.append(getIdMenuEncPerfil());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMenuEncPerfil());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalMenusEncPerfilDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMenuEncPerfil()!= null && getIdMenuEncPerfil()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalMenusEncPerfilDto other = (TrJanalMenusEncPerfilDto) obj;
    if (getIdMenuEncPerfil() != other.idMenuEncPerfil && (getIdMenuEncPerfil() == null || !getIdMenuEncPerfil().equals(other.idMenuEncPerfil))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMenuEncPerfil() != null ? getIdMenuEncPerfil().hashCode() : 0);
    return hash;
  }

}
