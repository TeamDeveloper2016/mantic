package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Lob;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tr_janal_modulo")
public class TrJanalModuloDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="gr_apo_mej")
  private Long grApoMej;
  @Column (name="id_caratula")
  private Long idCaratula;
  @Column (name="gr_apo")
  private Long grApo;
  @Column (name="cve_upm")
  private String cveUpm;
  @Column (name="filter_2")
  private String filter2;
  @Column (name="filter_1")
  private String filter1;
  @Column (name="filter_3")
  private String filter3;
  @Column (name="folio_cuest")
  private Long folioCuest;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_modulo")
  private Long idModulo;
  @Column (name="pro_des")
  private Long proDes;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="gr_pro_opo")
  private Long grProOpo;
  @Column (name="gr_trab")
  private Long grTrab;
  @Column (name="id_muestra")
  private Long idMuestra;
  @Column (name="consecutiv")
  private String consecutiv;

  public TrJanalModuloDto() {
    this(new Long(-1L));
  }

  public TrJanalModuloDto(Long key) {
    this(null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TrJanalModuloDto(Long grApoMej, Long idCaratula, Long grApo, String cveUpm, String filter2, String filter1, String filter3, Long folioCuest, Long idModulo, Long proDes, Long idUsuario, Long grProOpo, Long grTrab, Long idMuestra, String consecutiv) {
    setGrApoMej(grApoMej);
    setIdCaratula(idCaratula);
    setGrApo(grApo);
    setCveUpm(cveUpm);
    setFilter2(filter2);
    setFilter1(filter1);
    setFilter3(filter3);
    setFolioCuest(folioCuest);
    setIdModulo(idModulo);
    setProDes(proDes);
    setIdUsuario(idUsuario);
    setGrProOpo(grProOpo);
    setGrTrab(grTrab);
    setIdMuestra(idMuestra);
    setConsecutiv(consecutiv);
  }
	
  public void setGrApoMej(Long grApoMej) {
    this.grApoMej = grApoMej;
  }

  public Long getGrApoMej() {
    return grApoMej;
  }

  public void setIdCaratula(Long idCaratula) {
    this.idCaratula = idCaratula;
  }

  public Long getIdCaratula() {
    return idCaratula;
  }

  public void setGrApo(Long grApo) {
    this.grApo = grApo;
  }

  public Long getGrApo() {
    return grApo;
  }

  public void setCveUpm(String cveUpm) {
    this.cveUpm = cveUpm;
  }

  public String getCveUpm() {
    return cveUpm;
  }

  public void setFilter2(String filter2) {
    this.filter2 = filter2;
  }

  public String getFilter2() {
    return filter2;
  }

  public void setFilter1(String filter1) {
    this.filter1 = filter1;
  }

  public String getFilter1() {
    return filter1;
  }

  public void setFilter3(String filter3) {
    this.filter3 = filter3;
  }

  public String getFilter3() {
    return filter3;
  }

  public void setFolioCuest(Long folioCuest) {
    this.folioCuest = folioCuest;
  }

  public Long getFolioCuest() {
    return folioCuest;
  }

  public void setIdModulo(Long idModulo) {
    this.idModulo = idModulo;
  }

  public Long getIdModulo() {
    return idModulo;
  }

  public void setProDes(Long proDes) {
    this.proDes = proDes;
  }

  public Long getProDes() {
    return proDes;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setGrProOpo(Long grProOpo) {
    this.grProOpo = grProOpo;
  }

  public Long getGrProOpo() {
    return grProOpo;
  }

  public void setGrTrab(Long grTrab) {
    this.grTrab = grTrab;
  }

  public Long getGrTrab() {
    return grTrab;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setConsecutiv(String consecutiv) {
    this.consecutiv = consecutiv;
  }

  public String getConsecutiv() {
    return consecutiv;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdModulo();
  }

  @Override
  public void setKey(Long key) {
  	this.idModulo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getGrApoMej());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCaratula());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getGrApo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCveUpm());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter2());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter1());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter3());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolioCuest());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdModulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProDes());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getGrProOpo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getGrTrab());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutiv());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("grApoMej", getGrApoMej());
		regresar.put("idCaratula", getIdCaratula());
		regresar.put("grApo", getGrApo());
		regresar.put("cveUpm", getCveUpm());
		regresar.put("filter2", getFilter2());
		regresar.put("filter1", getFilter1());
		regresar.put("filter3", getFilter3());
		regresar.put("folioCuest", getFolioCuest());
		regresar.put("idModulo", getIdModulo());
		regresar.put("proDes", getProDes());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("grProOpo", getGrProOpo());
		regresar.put("grTrab", getGrTrab());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("consecutiv", getConsecutiv());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getGrApoMej(), getIdCaratula(), getGrApo(), getCveUpm(), getFilter2(), getFilter1(), getFilter3(), getFolioCuest(), getIdModulo(), getProDes(), getIdUsuario(), getGrProOpo(), getGrTrab(), getIdMuestra(), getConsecutiv()
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
    regresar.append("idModulo~");
    regresar.append(getIdModulo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdModulo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalModuloDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdModulo()!= null && getIdModulo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalModuloDto other = (TrJanalModuloDto) obj;
    if (getIdModulo() != other.idModulo && (getIdModulo() == null || !getIdModulo().equals(other.idModulo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdModulo() != null ? getIdModulo().hashCode() : 0);
    return hash;
  }

  public void setGr_apo_mej(Long grApoMej) {
    this.grApoMej = grApoMej;
  }

  public Long getGr_apo_mej() {
    return grApoMej;
  }

  public void setId_caratula(Long idCaratula) {
    this.idCaratula = idCaratula;
  }

  public Long getId_caratula() {
    return idCaratula;
  }

  public void setGr_apo(Long grApo) {
    this.grApo = grApo;
  }

  public Long getGr_apo() {
    return grApo;
  }

  public void setCve_upm(String cveUpm) {
    this.cveUpm = cveUpm;
  }

  public String getCve_upm() {
    return cveUpm;
  }

  public void setFilter_2(String filter2) {
    this.filter2 = filter2;
  }

  public String getFilter_2() {
    return filter2;
  }

  public void setFilter_1(String filter1) {
    this.filter1 = filter1;
  }

  public String getFilter_1() {
    return filter1;
  }

  public void setFilter_3(String filter3) {
    this.filter3 = filter3;
  }

  public String getFilter_3() {
    return filter3;
  }

  public void setFolio_cuest(Long folioCuest) {
    this.folioCuest = folioCuest;
  }

  public Long getFolio_cuest() {
    return folioCuest;
  }

  public void setId_modulo(Long idModulo) {
    this.idModulo = idModulo;
  }

  public Long getId_modulo() {
    return idModulo;
  }

  public void setPro_des(Long proDes) {
    this.proDes = proDes;
  }

  public Long getPro_des() {
    return proDes;
  }

  public void setId_usuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getId_usuario() {
    return idUsuario;
  }

  public void setGr_pro_opo(Long grProOpo) {
    this.grProOpo = grProOpo;
  }

  public Long getGr_pro_opo() {
    return grProOpo;
  }

  public void setGr_trab(Long grTrab) {
    this.grTrab = grTrab;
  }

  public Long getGr_trab() {
    return grTrab;
  }

  public void setId_muestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getId_muestra() {
    return idMuestra;
  }
  
}


