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
@Table(name="tr_janal_perfiles_jerarquias")
public class TrJanalPerfilesJerarquiasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="ID_PERFIL")
  private Long idPerfil;
  @Column (name="ORDEN")
  private Long orden;
  @Column (name="ID_PERFIL_ALTA")
  private Long idPerfilAlta;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idPerfilJerarquia_sequence")
  //@SequenceGenerator(name="idPerfilJerarquia_sequence",sequenceName="SEQ_TR_JANAL_perfiles_jerarquias" , allocationSize=1 )
  @Column (name="ID_PERFIL_JERARQUIA")
  private Long idPerfilJerarquia;

  public TrJanalPerfilesJerarquiasDto() {
    this(new Long(-1L));
  }

  public TrJanalPerfilesJerarquiasDto(Long key) {
    this(null, null, null, null);
    setKey(key);
  }

  public TrJanalPerfilesJerarquiasDto(Long idPerfil, Long orden, Long idPerfilAlta, Long idPerfilJerarquia) {
    setIdPerfil(idPerfil);
    setOrden(orden);
    setIdPerfilAlta(idPerfilAlta);
    setIdPerfilJerarquia(idPerfilJerarquia);
  }
	
  public void setIdPerfil(Long idPerfil) {
    this.idPerfil = idPerfil;
  }

  public Long getIdPerfil() {
    return idPerfil;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdPerfilAlta(Long idPerfilAlta) {
    this.idPerfilAlta = idPerfilAlta;
  }

  public Long getIdPerfilAlta() {
    return idPerfilAlta;
  }

  public void setIdPerfilJerarquia(Long idPerfilJerarquia) {
    this.idPerfilJerarquia = idPerfilJerarquia;
  }

  public Long getIdPerfilJerarquia() {
    return idPerfilJerarquia;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdPerfilJerarquia();
  }

  @Override
  public void setKey(Long key) {
  	this.idPerfilJerarquia = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPerfilAlta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPerfilJerarquia());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idPerfil", getIdPerfil());
		regresar.put("orden", getOrden());
		regresar.put("idPerfilAlta", getIdPerfilAlta());
		regresar.put("idPerfilJerarquia", getIdPerfilJerarquia());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdPerfil(), getOrden(), getIdPerfilAlta(), getIdPerfilJerarquia()
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
    regresar.append("idPerfilJerarquia~");
    regresar.append(getIdPerfilJerarquia());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPerfilJerarquia());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalPerfilesJerarquiasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPerfilJerarquia()!= null && getIdPerfilJerarquia()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalPerfilesJerarquiasDto other = (TrJanalPerfilesJerarquiasDto) obj;
    if (getIdPerfilJerarquia() != other.idPerfilJerarquia && (getIdPerfilJerarquia() == null || !getIdPerfilJerarquia().equals(other.idPerfilJerarquia))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPerfilJerarquia() != null ? getIdPerfilJerarquia().hashCode() : 0);
    return hash;
  }

}


