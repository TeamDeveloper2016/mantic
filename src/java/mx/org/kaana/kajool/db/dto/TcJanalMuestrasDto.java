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
@Table(name="tc_janal_muestras")
public class TcJanalMuestrasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="tipo_vial")
  private Long tipoVial;
  @Column (name="identifica")
  private Long identifica;
  @Column (name="ingreso")
  private String ingreso;
  @Column (name="calle_atras")
  private String calleAtras;
  @Column (name="id_movimiento")
  private Long idMovimiento;
  @Column (name="ap_materno")
  private String apMaterno;
  @Column (name="ageb")
  private String ageb;
  @Column (name="no_int")
  private String noInt;
  @Column (name="oficina")
  private String oficina;
  @Column (name="id_grupo")
  private Long idGrupo;
  @Column (name="colonia")
  private String colonia;
  @Column (name="dominio")
  private String dominio;
  @Column (name="nombre")
  private String nombre;
  @Column (name="prog")
  private Long prog;
  @Column (name="id_entidad")
  private Long idEntidad;
  @Column (name="latitud")
  private String latitud;
  @Column (name="resultado")
  private Long resultado;
  @Column (name="viv_sel")
  private Long vivSel;
  @Column (name="mza")
  private String mza;
  @Column (name="longitud")
  private String longitud;
  @Column (name="y_calle")
  private String ycalle;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_muestra")
  private Long idMuestra;
  @Column (name="ap_paterno")
  private String apPaterno;
  @Column (name="tipo_loc")
  private String tipoLoc;
  @Column (name="rectif")
  private String rectif;
  @Column (name="control")
  private String control;
  @Column (name="localidad")
  private String localidad;
  @Column (name="telefono_1")
  private String telefono1;
  @Column (name="telefono_2")
  private String telefono2;
  @Column (name="loc")
  private String loc;
  @Column (name="codigo_postal")
  private Long codigoPostal;
  @Column (name="telefono_3")
  private String telefono3;
  @Column (name="ent")
  private String ent;
  @Column (name="mun")
  private String mun;
  @Column (name="tam_loc")
  private String tamLoc;
  @Column (name="referencia")
  private String referencia;
  @Column (name="no_ext")
  private String noExt;
  @Column (name="id_entidad_oficina")
  private Long idEntidadOficina;
  @Column (name="folio")
  private String folio;
  @Column (name="upm")
  private String upm;
  @Column (name="consecutivo")
  private Long consecutivo;
  @Column (name="cve_mun")
  private String cveMun;
  @Column (name="entidad")
  private String entidad;
  @Column (name="zona")
  private String zona;
  @Column (name="entre_calle")
  private String entreCalle;
  @Column (name="calle")
  private String calle;

  public TcJanalMuestrasDto() {
    this(new Long(-1L));
  }

  public TcJanalMuestrasDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcJanalMuestrasDto(Long tipoVial, Long identifica, String ingreso, String calleAtras, Long idMovimiento, String apMaterno, String ageb, String noInt, String oficina, Long idGrupo, String colonia, String dominio, String nombre, Long prog, Long idEntidad, String latitud, Long resultado, Long vivSel, String mza, String longitud, String ycalle, Long idMuestra, String apPaterno, String tipoLoc, String rectif, String control, String localidad, String telefono1, String telefono2, String loc, Long codigoPostal, String telefono3, String ent, String mun, String tamLoc, String referencia, String noExt, Long idEntidadOficina, String folio, String upm, Long consecutivo, String cveMun, String entidad, String zona, String entreCalle, String calle) {
    setTipoVial(tipoVial);
    setIdentifica(identifica);
    setIngreso(ingreso);
    setCalleAtras(calleAtras);
    setIdMovimiento(idMovimiento);
    setApMaterno(apMaterno);
    setAgeb(ageb);
    setNoInt(noInt);
    setOficina(oficina);
    setIdGrupo(idGrupo);
    setColonia(colonia);
    setDominio(dominio);
    setNombre(nombre);
    setProg(prog);
    setIdEntidad(idEntidad);
    setLatitud(latitud);
    setResultado(resultado);
    setVivSel(vivSel);
    setMza(mza);
    setLongitud(longitud);
    setYcalle(ycalle);
    setIdMuestra(idMuestra);
    setApPaterno(apPaterno);
    setTipoLoc(tipoLoc);
    setRectif(rectif);
    setControl(control);
    setLocalidad(localidad);
    setTelefono1(telefono1);
    setTelefono2(telefono2);
    setLoc(loc);
    setCodigoPostal(codigoPostal);
    setTelefono3(telefono3);
    setEnt(ent);
    setMun(mun);
    setTamLoc(tamLoc);
    setReferencia(referencia);
    setNoExt(noExt);
    setIdEntidadOficina(idEntidadOficina);
    setFolio(folio);
    setUpm(upm);
    setConsecutivo(consecutivo);
    setCveMun(cveMun);
    setEntidad(entidad);
    setZona(zona);
    setEntreCalle(entreCalle);
    setCalle(calle);
  }
	
  public void setTipoVial(Long tipoVial) {
    this.tipoVial = tipoVial;
  }

  public Long getTipoVial() {
    return tipoVial;
  }

  public void setIdentifica(Long identifica) {
    this.identifica = identifica;
  }

  public Long getIdentifica() {
    return identifica;
  }

  public void setIngreso(String ingreso) {
    this.ingreso = ingreso;
  }

  public String getIngreso() {
    return ingreso;
  }

  public void setCalleAtras(String calleAtras) {
    this.calleAtras = calleAtras;
  }

  public String getCalleAtras() {
    return calleAtras;
  }

  public void setIdMovimiento(Long idMovimiento) {
    this.idMovimiento = idMovimiento;
  }

  public Long getIdMovimiento() {
    return idMovimiento;
  }

  public void setApMaterno(String apMaterno) {
    this.apMaterno = apMaterno;
  }

  public String getApMaterno() {
    return apMaterno;
  }

  public void setAgeb(String ageb) {
    this.ageb = ageb;
  }

  public String getAgeb() {
    return ageb;
  }

  public void setNoInt(String noInt) {
    this.noInt = noInt;
  }

  public String getNoInt() {
    return noInt;
  }

  public void setOficina(String oficina) {
    this.oficina = oficina;
  }

  public String getOficina() {
    return oficina;
  }

  public void setIdGrupo(Long idGrupo) {
    this.idGrupo = idGrupo;
  }

  public Long getIdGrupo() {
    return idGrupo;
  }

  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  public String getColonia() {
    return colonia;
  }

  public void setDominio(String dominio) {
    this.dominio = dominio;
  }

  public String getDominio() {
    return dominio;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setProg(Long prog) {
    this.prog = prog;
  }

  public Long getProg() {
    return prog;
  }

  public void setIdEntidad(Long idEntidad) {
    this.idEntidad = idEntidad;
  }

  public Long getIdEntidad() {
    return idEntidad;
  }

  public void setLatitud(String latitud) {
    this.latitud = latitud;
  }

  public String getLatitud() {
    return latitud;
  }

  public void setResultado(Long resultado) {
    this.resultado = resultado;
  }

  public Long getResultado() {
    return resultado;
  }

  public void setVivSel(Long vivSel) {
    this.vivSel = vivSel;
  }

  public Long getVivSel() {
    return vivSel;
  }

  public void setMza(String mza) {
    this.mza = mza;
  }

  public String getMza() {
    return mza;
  }

  public void setLongitud(String longitud) {
    this.longitud = longitud;
  }

  public String getLongitud() {
    return longitud;
  }

  public void setYcalle(String ycalle) {
    this.ycalle = ycalle;
  }

  public String getYcalle() {
    return ycalle;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setApPaterno(String apPaterno) {
    this.apPaterno = apPaterno;
  }

  public String getApPaterno() {
    return apPaterno;
  }

  public void setTipoLoc(String tipoLoc) {
    this.tipoLoc = tipoLoc;
  }

  public String getTipoLoc() {
    return tipoLoc;
  }

  public void setRectif(String rectif) {
    this.rectif = rectif;
  }

  public String getRectif() {
    return rectif;
  }

  public void setControl(String control) {
    this.control = control;
  }

  public String getControl() {
    return control;
  }

  public void setLocalidad(String localidad) {
    this.localidad = localidad;
  }

  public String getLocalidad() {
    return localidad;
  }

  public void setTelefono1(String telefono1) {
    this.telefono1 = telefono1;
  }

  public String getTelefono1() {
    return telefono1;
  }

  public void setTelefono2(String telefono2) {
    this.telefono2 = telefono2;
  }

  public String getTelefono2() {
    return telefono2;
  }

  public void setLoc(String loc) {
    this.loc = loc;
  }

  public String getLoc() {
    return loc;
  }

  public void setCodigoPostal(Long codigoPostal) {
    this.codigoPostal = codigoPostal;
  }

  public Long getCodigoPostal() {
    return codigoPostal;
  }

  public void setTelefono3(String telefono3) {
    this.telefono3 = telefono3;
  }

  public String getTelefono3() {
    return telefono3;
  }

  public void setEnt(String ent) {
    this.ent = ent;
  }

  public String getEnt() {
    return ent;
  }

  public void setMun(String mun) {
    this.mun = mun;
  }

  public String getMun() {
    return mun;
  }

  public void setTamLoc(String tamLoc) {
    this.tamLoc = tamLoc;
  }

  public String getTamLoc() {
    return tamLoc;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setNoExt(String noExt) {
    this.noExt = noExt;
  }

  public String getNoExt() {
    return noExt;
  }

  public void setIdEntidadOficina(Long idEntidadOficina) {
    this.idEntidadOficina = idEntidadOficina;
  }

  public Long getIdEntidadOficina() {
    return idEntidadOficina;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public String getFolio() {
    return folio;
  }

  public void setUpm(String upm) {
    this.upm = upm;
  }

  public String getUpm() {
    return upm;
  }

  public void setConsecutivo(Long consecutivo) {
    this.consecutivo = consecutivo;
  }

  public Long getConsecutivo() {
    return consecutivo;
  }

  public void setCveMun(String cveMun) {
    this.cveMun = cveMun;
  }

  public String getCveMun() {
    return cveMun;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setZona(String zona) {
    this.zona = zona;
  }

  public String getZona() {
    return zona;
  }

  public void setEntreCalle(String entreCalle) {
    this.entreCalle = entreCalle;
  }

  public String getEntreCalle() {
    return entreCalle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getCalle() {
    return calle;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMuestra();
  }

  @Override
  public void setKey(Long key) {
  	this.idMuestra = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getTipoVial());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdentifica());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIngreso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCalleAtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMovimiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getApMaterno());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAgeb());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNoInt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOficina());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdGrupo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getColonia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDominio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProg());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEntidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLatitud());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getResultado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVivSel());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMza());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLongitud());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getYcalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getApPaterno());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipoLoc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRectif());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getControl());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLocalidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTelefono1());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTelefono2());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLoc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigoPostal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTelefono3());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEnt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMun());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTamLoc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNoExt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEntidadOficina());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUpm());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCveMun());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getZona());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEntreCalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCalle());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("tipoVial", getTipoVial());
		regresar.put("identifica", getIdentifica());
		regresar.put("ingreso", getIngreso());
		regresar.put("calleAtras", getCalleAtras());
		regresar.put("idMovimiento", getIdMovimiento());
		regresar.put("apMaterno", getApMaterno());
		regresar.put("ageb", getAgeb());
		regresar.put("noInt", getNoInt());
		regresar.put("oficina", getOficina());
		regresar.put("idGrupo", getIdGrupo());
		regresar.put("colonia", getColonia());
		regresar.put("dominio", getDominio());
		regresar.put("nombre", getNombre());
		regresar.put("prog", getProg());
		regresar.put("idEntidad", getIdEntidad());
		regresar.put("latitud", getLatitud());
		regresar.put("resultado", getResultado());
		regresar.put("vivSel", getVivSel());
		regresar.put("mza", getMza());
		regresar.put("longitud", getLongitud());
		regresar.put("ycalle", getYcalle());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("apPaterno", getApPaterno());
		regresar.put("tipoLoc", getTipoLoc());
		regresar.put("rectif", getRectif());
		regresar.put("control", getControl());
		regresar.put("localidad", getLocalidad());
		regresar.put("telefono1", getTelefono1());
		regresar.put("telefono2", getTelefono2());
		regresar.put("loc", getLoc());
		regresar.put("codigoPostal", getCodigoPostal());
		regresar.put("telefono3", getTelefono3());
		regresar.put("ent", getEnt());
		regresar.put("mun", getMun());
		regresar.put("tamLoc", getTamLoc());
		regresar.put("referencia", getReferencia());
		regresar.put("noExt", getNoExt());
		regresar.put("idEntidadOficina", getIdEntidadOficina());
		regresar.put("folio", getFolio());
		regresar.put("upm", getUpm());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("cveMun", getCveMun());
		regresar.put("entidad", getEntidad());
		regresar.put("zona", getZona());
		regresar.put("entreCalle", getEntreCalle());
		regresar.put("calle", getCalle());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getTipoVial(), getIdentifica(), getIngreso(), getCalleAtras(), getIdMovimiento(), getApMaterno(), getAgeb(), getNoInt(), getOficina(), getIdGrupo(), getColonia(), getDominio(), getNombre(), getProg(), getIdEntidad(), getLatitud(), getResultado(), getVivSel(), getMza(), getLongitud(), getYcalle(), getIdMuestra(), getApPaterno(), getTipoLoc(), getRectif(), getControl(), getLocalidad(), getTelefono1(), getTelefono2(), getLoc(), getCodigoPostal(), getTelefono3(), getEnt(), getMun(), getTamLoc(), getReferencia(), getNoExt(), getIdEntidadOficina(), getFolio(), getUpm(), getConsecutivo(), getCveMun(), getEntidad(), getZona(), getEntreCalle(), getCalle()
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
    regresar.append("idMuestra~");
    regresar.append(getIdMuestra());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMuestra());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalMuestrasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMuestra()!= null && getIdMuestra()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalMuestrasDto other = (TcJanalMuestrasDto) obj;
    if (getIdMuestra() != other.idMuestra && (getIdMuestra() == null || !getIdMuestra().equals(other.idMuestra))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMuestra() != null ? getIdMuestra().hashCode() : 0);
    return hash;
  }

}


