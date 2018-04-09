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
@Table(name="tr_janal_caratula")
public class TrJanalCaratulaDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="modificacion")
  private Timestamp modificacion;
  @Column (name="nom_tit")
  private String nomTit;
  @Column (name="cve_upm")
  private String cveUpm;
  @Column (name="filter_2")
  private String filter2;
  @Column (name="num_ext")
  private String numExt;
  @Column (name="filter_1")
  private String filter1;
  @Column (name="telef")
  private String telef;
  @Column (name="entre_ca1")
  private String entreCa1;
  @Column (name="filter_3")
  private String filter3;
  @Column (name="fin_captura")
  private Timestamp finCaptura;
  @Column (name="entre_ca2")
  private String entreCa2;
  @Column (name="folio_cuest")
  private Long folioCuest;
  @Column (name="colonia")
  private String colonia;
  @Column (name="mat_tit")
  private String matTit;
  @Column (name="id_muestra")
  private Long idMuestra;
  @Column (name="status")
  private String status;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_caratula")
  private Long idCaratula;
  @Column (name="loc")
  private String loc;
  @Column (name="t_cuest")
  private Long tcuest;
  @Column (name="ent")
  private String ent;
  @Column (name="num_int")
  private String numInt;
  @Column (name="calle_atra")
  private String calleAtra;
  @Column (name="mun")
  private String mun;
  @Column (name="referencia")
  private String referencia;
  @Column (name="cp")
  private String cp;
  @Column (name="pat_tit")
  private String patTit;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="fuente")
  private Long fuente;
  @Column (name="inicio_captura")
  private Timestamp inicioCaptura;
  @Column (name="creacion")
  private Timestamp creacion;
  @Column (name="calle")
  private String calle;
  @Column (name="consecutiv")
  private String consecutiv;

  public TrJanalCaratulaDto() {
    this(new Long(-1L));
  }

  public TrJanalCaratulaDto(Long key) {
    this(new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null);
    setKey(key);
  }

  public TrJanalCaratulaDto(Timestamp modificacion, String nomTit, String cveUpm, String filter2, String numExt, String filter1, String telef, String entreCa1, String filter3, Timestamp finCaptura, String entreCa2, Long folioCuest, String colonia, String matTit, Long idMuestra, String status, Long idCaratula, String loc, Long tcuest, String ent, String numInt, String calleAtra, String mun, String referencia, String cp, String patTit, Long idUsuario, Long fuente, Timestamp inicioCaptura, Timestamp creacion, String calle, String consecutiv) {
    setModificacion(modificacion);
    setNomTit(nomTit);
    setCveUpm(cveUpm);
    setFilter2(filter2);
    setNumExt(numExt);
    setFilter1(filter1);
    setTelef(telef);
    setEntreCa1(entreCa1);
    setFilter3(filter3);
    setFinCaptura(finCaptura);
    setEntreCa2(entreCa2);
    setFolioCuest(folioCuest);
    setColonia(colonia);
    setMatTit(matTit);
    setIdMuestra(idMuestra);
    setStatus(status);
    setIdCaratula(idCaratula);
    setLoc(loc);
    setTcuest(tcuest);
    setEnt(ent);
    setNumInt(numInt);
    setCalleAtra(calleAtra);
    setMun(mun);
    setReferencia(referencia);
    setCp(cp);
    setPatTit(patTit);
    setIdUsuario(idUsuario);
    setFuente(fuente);
    setInicioCaptura(inicioCaptura);
    setCreacion(creacion);
    setCalle(calle);
    setConsecutiv(consecutiv);
  }
	
  public void setModificacion(Timestamp modificacion) {
    this.modificacion = modificacion;
  }

  public Timestamp getModificacion() {
    return modificacion;
  }

  public void setNomTit(String nomTit) {
    this.nomTit = nomTit;
  }

  public String getNomTit() {
    return nomTit;
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

  public void setNumExt(String numExt) {
    this.numExt = numExt;
  }

  public String getNumExt() {
    return numExt;
  }

  public void setFilter1(String filter1) {
    this.filter1 = filter1;
  }

  public String getFilter1() {
    return filter1;
  }

  public void setTelef(String telef) {
    this.telef = telef;
  }

  public String getTelef() {
    return telef;
  }

  public void setEntreCa1(String entreCa1) {
    this.entreCa1 = entreCa1;
  }

  public String getEntreCa1() {
    return entreCa1;
  }

  public void setFilter3(String filter3) {
    this.filter3 = filter3;
  }

  public String getFilter3() {
    return filter3;
  }

  public void setFinCaptura(Timestamp finCaptura) {
    this.finCaptura = finCaptura;
  }

  public Timestamp getFinCaptura() {
    return finCaptura;
  }

  public void setEntreCa2(String entreCa2) {
    this.entreCa2 = entreCa2;
  }

  public String getEntreCa2() {
    return entreCa2;
  }

  public void setFolioCuest(Long folioCuest) {
    this.folioCuest = folioCuest;
  }

  public Long getFolioCuest() {
    return folioCuest;
  }

  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  public String getColonia() {
    return colonia;
  }

  public void setMatTit(String matTit) {
    this.matTit = matTit;
  }

  public String getMatTit() {
    return matTit;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setIdCaratula(Long idCaratula) {
    this.idCaratula = idCaratula;
  }

  public Long getIdCaratula() {
    return idCaratula;
  }

  public void setLoc(String loc) {
    this.loc = loc;
  }

  public String getLoc() {
    return loc;
  }

  public void setTcuest(Long tcuest) {
    this.tcuest = tcuest;
  }

  public Long getTcuest() {
    return tcuest;
  }

  public void setEnt(String ent) {
    this.ent = ent;
  }

  public String getEnt() {
    return ent;
  }

  public void setNumInt(String numInt) {
    this.numInt = numInt;
  }

  public String getNumInt() {
    return numInt;
  }

  public void setCalleAtra(String calleAtra) {
    this.calleAtra = calleAtra;
  }

  public String getCalleAtra() {
    return calleAtra;
  }

  public void setMun(String mun) {
    this.mun = mun;
  }

  public String getMun() {
    return mun;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  public String getCp() {
    return cp;
  }

  public void setPatTit(String patTit) {
    this.patTit = patTit;
  }

  public String getPatTit() {
    return patTit;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setFuente(Long fuente) {
    this.fuente = fuente;
  }

  public Long getFuente() {
    return fuente;
  }

  public void setInicioCaptura(Timestamp inicioCaptura) {
    this.inicioCaptura = inicioCaptura;
  }

  public Timestamp getInicioCaptura() {
    return inicioCaptura;
  }

  public void setCreacion(Timestamp creacion) {
    this.creacion = creacion;
  }

  public Timestamp getCreacion() {
    return creacion;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getCalle() {
    return calle;
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
  	return getIdCaratula();
  }

  @Override
  public void setKey(Long key) {
  	this.idCaratula = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getModificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNomTit());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCveUpm());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter2());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNumExt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter1());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTelef());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntreCa1());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter3());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFinCaptura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntreCa2());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolioCuest());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getColonia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMatTit());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getStatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCaratula());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLoc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTcuest());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEnt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNumInt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCalleAtra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMun());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCp());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPatTit());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFuente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getInicioCaptura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCreacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutiv());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("modificacion", getModificacion());
		regresar.put("nomTit", getNomTit());
		regresar.put("cveUpm", getCveUpm());
		regresar.put("filter2", getFilter2());
		regresar.put("numExt", getNumExt());
		regresar.put("filter1", getFilter1());
		regresar.put("telef", getTelef());
		regresar.put("entreCa1", getEntreCa1());
		regresar.put("filter3", getFilter3());
		regresar.put("finCaptura", getFinCaptura());
		regresar.put("entreCa2", getEntreCa2());
		regresar.put("folioCuest", getFolioCuest());
		regresar.put("colonia", getColonia());
		regresar.put("matTit", getMatTit());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("status", getStatus());
		regresar.put("idCaratula", getIdCaratula());
		regresar.put("loc", getLoc());
		regresar.put("tcuest", getTcuest());
		regresar.put("ent", getEnt());
		regresar.put("numInt", getNumInt());
		regresar.put("calleAtra", getCalleAtra());
		regresar.put("mun", getMun());
		regresar.put("referencia", getReferencia());
		regresar.put("cp", getCp());
		regresar.put("patTit", getPatTit());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("fuente", getFuente());
		regresar.put("inicioCaptura", getInicioCaptura());
		regresar.put("creacion", getCreacion());
		regresar.put("calle", getCalle());
		regresar.put("consecutiv", getConsecutiv());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getModificacion(), getNomTit(), getCveUpm(), getFilter2(), getNumExt(), getFilter1(), getTelef(), getEntreCa1(), getFilter3(), getFinCaptura(), getEntreCa2(), getFolioCuest(), getColonia(), getMatTit(), getIdMuestra(), getStatus(), getIdCaratula(), getLoc(), getTcuest(), getEnt(), getNumInt(), getCalleAtra(), getMun(), getReferencia(), getCp(), getPatTit(), getIdUsuario(), getFuente(), getInicioCaptura(), getCreacion(), getCalle(), getConsecutiv()
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
    regresar.append("idCaratula~");
    regresar.append(getIdCaratula());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCaratula());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalCaratulaDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCaratula()!= null && getIdCaratula()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalCaratulaDto other = (TrJanalCaratulaDto) obj;
    if (getIdCaratula() != other.idCaratula && (getIdCaratula() == null || !getIdCaratula().equals(other.idCaratula))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCaratula() != null ? getIdCaratula().hashCode() : 0);
    return hash;
  }


  public void setNom_tit(String nomTit) {
    this.nomTit = nomTit;
  }

  public String getNom_tit() {
    return nomTit;
  }

  public void setCve_upm(String cveUpm) {
    this.cveUpm = cveUpm;
  }

  public String getCve_upm() {
    return cveUpm;
  }

  public void setNum_ext(String numExt) {
    this.numExt = numExt;
  }

  public String getNum_ext() {
    return numExt;
  }

  public void setEntre_ca1(String entreCa1) {
    this.entreCa1 = entreCa1;
  }

  public String getEntre_ca1() {
    return entreCa1;
  }

  public void setFin_captura(Timestamp finCaptura) {
    this.finCaptura = finCaptura;
  }

  public Timestamp getFin_captura() {
    return finCaptura;
  }

  public void setEntre_ca2(String entreCa2) {
    this.entreCa2 = entreCa2;
  }

  public String getEntre_ca2() {
    return entreCa2;
  }

  public void setFolio_cuest(Long folioCuest) {
    this.folioCuest = folioCuest;
  }

  public Long getFolio_cuest() {
    return folioCuest;
  }

  public void setMat_tit(String matTit) {
    this.matTit = matTit;
  }

  public String getMat_tit() {
    return matTit;
  }

  public void setId_muestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getId_muestra() {
    return idMuestra;
  }

  public void setId_caratula(Long idCaratula) {
    this.idCaratula = idCaratula;
  }

  public Long getId_caratula() {
    return idCaratula;
  }

  public void setNum_int(String numInt) {
    this.numInt = numInt;
  }

  public String getNum_int() {
    return numInt;
  }

  public void setCalle_atra(String calleAtra) {
    this.calleAtra = calleAtra;
  }

  public String getCalle_atra() {
    return calleAtra;
  }

  public void setPat_tit(String patTit) {
    this.patTit = patTit;
  }

  public String getPat_tit() {
    return patTit;
  }

  public void setId_usuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getId_usuario() {
    return idUsuario;
  }

  public void setInicio_captura(Timestamp inicioCaptura) {
    this.inicioCaptura = inicioCaptura;
  }

  public Timestamp getInicio_captura() {
    return inicioCaptura;
  }

  public void setFilter_1(String filter1) {
    this.filter1 = filter1;
  }

  public String getFilter_1() {
    return filter1;
  }

  public void setFilter_2(String filter2) {
    this.filter2 = filter2;
  }

  public String getFilter_2() {
    return filter2;
  }

  public void setFilter_3(String filter3) {
    this.filter3 = filter3;
  }

  public String getFilter_3() {
    return filter3;
  }

}


