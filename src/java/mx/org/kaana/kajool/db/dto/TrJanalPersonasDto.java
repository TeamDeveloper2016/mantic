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
@Table(name="tr_janal_personas")
public class TrJanalPersonasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="v_resid")
  private String vresid;
  @Column (name="cve_upm")
  private String cveUpm;
  @Column (name="sexo")
  private String sexo;
  @Column (name="filter_2")
  private String filter2;
  @Column (name="rg")
  private String rg;
  @Column (name="filter_1")
  private String filter1;
  @Column (name="filter_3")
  private String filter3;
  @Column (name="folio_cuest")
  private Long folioCuest;
  @Column (name="salud")
  private String salud;
  @Column (name="nombre")
  private String nombre;
  @Column (name="edad")
  private String edad;
  @Column (name="otros")
  private String otros;
  @Column (name="educ")
  private String educ;
  @Column (name="id_muestra")
  private Long idMuestra;
  @Column (name="a_paterno")
  private String apaterno;
  @Column (name="ninguno")
  private String ninguno;
  @Column (name="alim")
  private String alim;
  @Column (name="a_materno")
  private String amaterno;
  @Column (name="id_caratula")
  private Long idCaratula;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_persona")
  private Long idPersona;
  @Column (name="esc")
  private String esc;
  @Column (name="parent")
  private String parent;
  @Column (name="activo")
  private String activo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="a_apoyo")
  private String aapoyo;
  @Column (name="c_resid")
  private String cresid;
  @Column (name="consecutiv")
  private String consecutiv;
  @Column (name="ver_ini")
  private Long verIni;
  @Column (name="ver_fin")
  private Long verFin;

  public TrJanalPersonasDto() {
    this(new Long(-1L));
  }

  public TrJanalPersonasDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TrJanalPersonasDto(String vresid, String cveUpm, String sexo, String filter2, String rg, String filter1, String filter3, Long folioCuest, String salud, String nombre, String edad, String otros, String educ, Long idMuestra, String apaterno, String ninguno, String alim, String amaterno, Long idCaratula, Long idPersona, String esc, String parent, String activo, Long idUsuario, String aapoyo, String cresid, String consecutiv, Long verIni, Long verFin) {
    setVresid(vresid);
    setCveUpm(cveUpm);
    setSexo(sexo);
    setFilter2(filter2);
    setRg(rg);
    setFilter1(filter1);
    setFilter3(filter3);
    setFolioCuest(folioCuest);
    setSalud(salud);
    setNombre(nombre);
    setEdad(edad);
    setOtros(otros);
    setEduc(educ);
    setIdMuestra(idMuestra);
    setApaterno(apaterno);
    setNinguno(ninguno);
    setAlim(alim);
    setAmaterno(amaterno);
    setIdCaratula(idCaratula);
    setIdPersona(idPersona);
    setEsc(esc);
    setParent(parent);
    setActivo(activo);
    setIdUsuario(idUsuario);
    setAapoyo(aapoyo);
    setCresid(cresid);
    setConsecutiv(consecutiv);
    setVerIni(verIni);
    setVerFin(verFin);
  }
	
  public void setVresid(String vresid) {
    this.vresid = vresid;
  }

  public String getVresid() {
    return vresid;
  }

  public void setCveUpm(String cveUpm) {
    this.cveUpm = cveUpm;
  }

  public String getCveUpm() {
    return cveUpm;
  }

  public void setSexo(String sexo) {
    this.sexo = sexo;
  }

  public String getSexo() {
    return sexo;
  }

  public void setFilter2(String filter2) {
    this.filter2 = filter2;
  }

  public String getFilter2() {
    return filter2;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getRg() {
    return rg;
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

  public void setSalud(String salud) {
    this.salud = salud;
  }

  public String getSalud() {
    return salud;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setEdad(String edad) {
    this.edad = edad;
  }

  public String getEdad() {
    return edad;
  }

  public void setOtros(String otros) {
    this.otros = otros;
  }

  public String getOtros() {
    return otros;
  }

  public void setEduc(String educ) {
    this.educ = educ;
  }

  public String getEduc() {
    return educ;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setApaterno(String apaterno) {
    this.apaterno = apaterno;
  }

  public String getApaterno() {
    return apaterno;
  }

  public void setNinguno(String ninguno) {
    this.ninguno = ninguno;
  }

  public String getNinguno() {
    return ninguno;
  }

  public void setAlim(String alim) {
    this.alim = alim;
  }

  public String getAlim() {
    return alim;
  }

  public void setAmaterno(String amaterno) {
    this.amaterno = amaterno;
  }

  public String getAmaterno() {
    return amaterno;
  }

  public void setIdCaratula(Long idCaratula) {
    this.idCaratula = idCaratula;
  }

  public Long getIdCaratula() {
    return idCaratula;
  }

  public void setIdPersona(Long idPersona) {
    this.idPersona = idPersona;
  }

  public Long getIdPersona() {
    return idPersona;
  }

  public void setEsc(String esc) {
    this.esc = esc;
  }

  public String getEsc() {
    return esc;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getParent() {
    return parent;
  }

  public void setActivo(String activo) {
    this.activo = activo;
  }

  public String getActivo() {
    return activo;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setAapoyo(String aapoyo) {
    this.aapoyo = aapoyo;
  }

  public String getAapoyo() {
    return aapoyo;
  }

  public void setCresid(String cresid) {
    this.cresid = cresid;
  }

  public String getCresid() {
    return cresid;
  }

  public void setConsecutiv(String consecutiv) {
    this.consecutiv = consecutiv;
  }

  public String getConsecutiv() {
    return consecutiv;
  }

  public Long getVerIni() {
    return verIni;
  }

  public void setVerIni(Long verIni) {
    this.verIni = verIni;
  }

  public Long getVerFin() {
    return verFin;
  }

  public void setVerFin(Long verFin) {
    this.verFin = verFin;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdPersona();
  }

  @Override
  public void setKey(Long key) {
  	this.idPersona = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getVresid());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCveUpm());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSexo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter2());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRg());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter1());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter3());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolioCuest());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSalud());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEdad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOtros());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEduc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getApaterno());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNinguno());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlim());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAmaterno());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCaratula());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPersona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEsc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getParent());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getActivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAapoyo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCresid());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutiv());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVerIni());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVerFin());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("vresid", getVresid());
		regresar.put("cveUpm", getCveUpm());
		regresar.put("sexo", getSexo());
		regresar.put("filter2", getFilter2());
		regresar.put("rg", getRg());
		regresar.put("filter1", getFilter1());
		regresar.put("filter3", getFilter3());
		regresar.put("folioCuest", getFolioCuest());
		regresar.put("salud", getSalud());
		regresar.put("nombre", getNombre());
		regresar.put("edad", getEdad());
		regresar.put("otros", getOtros());
		regresar.put("educ", getEduc());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("apaterno", getApaterno());
		regresar.put("ninguno", getNinguno());
		regresar.put("alim", getAlim());
		regresar.put("amaterno", getAmaterno());
		regresar.put("idCaratula", getIdCaratula());
		regresar.put("idPersona", getIdPersona());
		regresar.put("esc", getEsc());
		regresar.put("parent", getParent());
		regresar.put("activo", getActivo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("aapoyo", getAapoyo());
		regresar.put("cresid", getCresid());
		regresar.put("consecutiv", getConsecutiv());
		regresar.put("verIni", getVerIni());
		regresar.put("verFin", getVerFin());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getVresid(), getCveUpm(), getSexo(), getFilter2(), getRg(), getFilter1(), getFilter3(), getFolioCuest(), getSalud(), getNombre(), getEdad(), getOtros(), getEduc(), getIdMuestra(), getApaterno(), getNinguno(), getAlim(), getAmaterno(), getIdCaratula(), getIdPersona(), getEsc(), getParent(), getActivo(), getIdUsuario(), getAapoyo(), getCresid(), getConsecutiv(), getVerIni(), getVerFin()
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
    regresar.append("idPersona~");
    regresar.append(getIdPersona());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdPersona());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalPersonasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdPersona()!= null && getIdPersona()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalPersonasDto other = (TrJanalPersonasDto) obj;
    if (getIdPersona() != other.idPersona && (getIdPersona() == null || !getIdPersona().equals(other.idPersona))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdPersona() != null ? getIdPersona().hashCode() : 0);
    return hash;
  }
  
  public boolean isNuevo() {
    return this.activo!= null? this.activo.equals("1") || this.activo.equals("2"): false;
  }
  
  public boolean isResidente() {
    return this.activo!= null? this.activo.equals("3"): false;
  }
  
  public boolean isActivo() {
    return this.activo!= null? this.activo.equals("1") || this.activo.equals("3"): false;
  }
  
  public boolean isDepurado() {
    return this.activo!= null? this.activo.equals("2"): false;
  }

  public void setV_resid(String vresid) {
    this.vresid = vresid;
  }

  public String getV_resid() {
    return vresid;
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

  public void setId_muestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getId_muestra() {
    return idMuestra;
  }

  public void setA_paterno(String apaterno) {
    this.apaterno = apaterno;
  }

  public String getA_paterno() {
    return apaterno;
  }

  public void setA_materno(String amaterno) {
    this.amaterno = amaterno;
  }

  public String getA_materno() {
    return amaterno;
  }

  public void setId_caratula(Long idCaratula) {
    this.idCaratula = idCaratula;
  }

  public Long getId_caratula() {
    return idCaratula;
  }

  public void setId_persona(Long idPersona) {
    this.idPersona = idPersona;
  }

  public Long getId_persona() {
    return idPersona;
  }

  public void setId_usuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getId_usuario() {
    return idUsuario;
  }

  public void setA_apoyo(String aapoyo) {
    this.aapoyo = aapoyo;
  }

  public String getA_apoyo() {
    return aapoyo;
  }

  public void setC_resid(String cresid) {
    this.cresid = cresid;
  }

  public String getC_resid() {
    return cresid;
  }

  public Long getVer_ini() {
    return verIni;
  }

  public void setVer_ini(Long verIni) {
    this.verIni = verIni;
  }

  public Long getVer_fin() {
    return verFin;
  }

  public void setVer_fin(Long verFin) {
    this.verFin = verFin;
  }

}


