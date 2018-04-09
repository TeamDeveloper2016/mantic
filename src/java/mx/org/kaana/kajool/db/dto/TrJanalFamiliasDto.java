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

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tr_janal_familias")
public class TrJanalFamiliasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="t_progra")
  private String tprogra;
  @Column (name="e_conc")
  private Long econc;
  @Column (name="cap_t_cap")
  private String capTcap;
  @Column (name="ap_soc")
  private String apSoc;
  @Column (name="t_alim")
  private String talim;
  @Column (name="a_esc_be")
  private String aescBe;
  @Column (name="mp_tmpo")
  private Long mpTmpo;
  @Column (name="filter_2")
  private String filter2;
  @Column (name="e_empleo")
  private Long eempleo;
  @Column (name="filter_1")
  private String filter1;
  @Column (name="filter_3")
  private String filter3;
  @Column (name="s_esc_m")
  private String sescM;
  @Column (name="folio_cuest")
  private Long folioCuest;
  @Column (name="or_fi_pf")
  private String orFiPf;
  @Column (name="mp_inf")
  private Long mpInf;
  @Column (name="s_c_fv")
  private Long scfv;
  @Column (name="s_esc_c")
  private String sescC;
  @Column (name="a_otorga")
  private Long aotorga;
  @Column (name="e_trto_t")
  private Long etrtoT;
  @Column (name="id_muestra")
  private Long idMuestra;
  @Column (name="ap_soc_sat")
  private String apSocSat;
  @Column (name="mp_fr_re")
  private Long mpFrRe;
  @Column (name="at_pp_sat")
  private Long atPpSat;
  @Column (name="am_sat")
  private Long amSat;
  @Column (name="t_adic")
  private String tadic;
  @Column (name="s_s_pla")
  private Long sspla;
  @Column (name="s_oprt_m")
  private Long soprtM;
  @Column (name="am_salud")
  private Long amSalud;
  @Column (name="e_oport")
  private Long eoport;
  @Column (name="a_sat_t")
  private Long asatT;
  @Column (name="s_s_t_pl")
  private Long sstpl;
  @Column (name="s_p_emb")
  private Long spemb;
  @Column (name="t_edu")
  private String tedu;
  @Column (name="t_planfa")
  private String tplanfa;
  @Column (name="cve_upm")
  private String cveUpm;
  @Column (name="cap_t_sat")
  private Long capTsat;
  @Column (name="cap_t")
  private Long capT;
  @Column (name="am_otorg")
  private Long amOtorg;
  @Column (name="e_nivel")
  private Long enivel;
  @Column (name="a_sat")
  private Long asat;
  @Column (name="a_oprt")
  private Long aoprt;
  @Column (name="at_pp")
  private Long atPp;
  @Column (name="or_fi")
  private String orFi;
  @Column (name="s_p_fv_s")
  private Long spfvS;
  @Column (name="s_p_adic")
  private Long spadic;
  @Column (name="at_pp_cap")
  private Long atPpCap;
  @Column (name="e_esc_e")
  private String eescE;
  @Column (name="t_salud")
  private String tsalud;
  @Column (name="t_especi")
  private String tespeci;
  @Column (name="t_otros")
  private String totros;
  @Column (name="s_sat_c")
  private Long ssatC;
  @Column (name="s_s_t_c")
  private Long sstc;
  @Column (name="or_fi_sat")
  private String orFiSat;
  @Column (name="e_otorga")
  private Long eotorga;
  @Column (name="mp_asist")
  private Long mpAsist;
  @Column (name="am_oport")
  private Long amOport;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_familia")
  private Long idFamilia;
  @Column (name="e_sat")
  private Long esat;
  @Column (name="am_alim")
  private Long amAlim;
  @Column (name="e_cont")
  private Long econt;
  @Column (name="a_mejora")
  private Long amejora;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="s_esc_as")
  private String sescAs;
  @Column (name="mp_trto")
  private Long mpTrto;
  @Column (name="am_esc")
  private String amEsc;
  @Column (name="s_pl_fv_a")
  private Long splFvA;
  @Column (name="ap_soc_nv")
  private String apSocNv;
  @Column (name="consecutiv")
  private String consecutiv;
  @Column (name="s_otroga")
  private Long sotroga;
  
  public TrJanalFamiliasDto() {
    this(new Long(-1L));
  }

  public TrJanalFamiliasDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TrJanalFamiliasDto(String tprogra, Long econc, String capTcap, String apSoc, String talim, String aescBe, Long mpTmpo, String filter2, Long eempleo, String filter1, String filter3, String sescM, Long folioCuest, String orFiPf, Long mpInf, Long scfv, String sescC, Long aotorga, Long etrtoT, Long idMuestra, String apSocSat, Long mpFrRe, Long atPpSat, Long amSat, String tadic, Long sspla, Long soprtM, Long amSalud, Long eoport, Long asatT, Long sstpl, Long spemb, String tedu, String tplanfa, String cveUpm, Long capTsat, Long capT, Long amOtorg, Long enivel, Long asat, Long aoprt, Long atPp, String orFi, Long spfvS, Long spadic, Long atPpCap, String eescE, String tsalud, String tespeci, String totros, Long ssatC, Long sstc, String orFiSat, Long eotorga, Long mpAsist, Long amOport, Long idFamilia, Long esat, Long amAlim, Long econt, Long amejora, Long idUsuario, String sescAs, Long mpTrto, String amEsc, Long splFvA, String apSocNv, String consecutiv, Long sotroga) {
    setTprogra(tprogra);
    setEconc(econc);
    setCapTcap(capTcap);
    setApSoc(apSoc);
    setTalim(talim);
    setAescBe(aescBe);
    setMpTmpo(mpTmpo);
    setFilter2(filter2);
    setEempleo(eempleo);
    setFilter1(filter1);
    setFilter3(filter3);
    setSescM(sescM);
    setFolioCuest(folioCuest);
    setOrFiPf(orFiPf);
    setMpInf(mpInf);
    setScfv(scfv);
    setSescC(sescC);
    setAotorga(aotorga);
    setEtrtoT(etrtoT);
    setIdMuestra(idMuestra);
    setApSocSat(apSocSat);
    setMpFrRe(mpFrRe);
    setAtPpSat(atPpSat);
    setAmSat(amSat);
    setTadic(tadic);
    setSspla(sspla);
    setSoprtM(soprtM);
    setAmSalud(amSalud);
    setEoport(eoport);
    setAsatT(asatT);
    setSstpl(sstpl);
    setSpemb(spemb);
    setTedu(tedu);
    setTplanfa(tplanfa);
    setCveUpm(cveUpm);
    setCapTsat(capTsat);
    setCapT(capT);
    setAmOtorg(amOtorg);
    setEnivel(enivel);
    setAsat(asat);
    setAoprt(aoprt);
    setAtPp(atPp);
    setOrFi(orFi);
    setSpfvS(spfvS);
    setSpadic(spadic);
    setAtPpCap(atPpCap);
    setEescE(eescE);
    setTsalud(tsalud);
    setTespeci(tespeci);
    setTotros(totros);
    setSsatC(ssatC);
    setSstc(sstc);
    setOrFiSat(orFiSat);
    setEotorga(eotorga);
    setMpAsist(mpAsist);
    setAmOport(amOport);
    setIdFamilia(idFamilia);
    setEsat(esat);
    setAmAlim(amAlim);
    setEcont(econt);
    setAmejora(amejora);
    setIdUsuario(idUsuario);
    setSescAs(sescAs);
    setMpTrto(mpTrto);
    setAmEsc(amEsc);
    setSplFvA(splFvA);
    setApSocNv(apSocNv);
    setConsecutiv(consecutiv);
    setSotroga(sotroga);
  }
	
  public void setTprogra(String tprogra) {
    this.tprogra = tprogra;
  }

  public String getTprogra() {
    return tprogra;
  }

  public void setEconc(Long econc) {
    this.econc = econc;
  }

  public Long getEconc() {
    return econc;
  }

  public void setCapTcap(String capTcap) {
    this.capTcap = capTcap;
  }

  public String getCapTcap() {
    return capTcap;
  }

  public void setApSoc(String apSoc) {
    this.apSoc = apSoc;
  }

  public String getApSoc() {
    return apSoc;
  }

  public void setTalim(String talim) {
    this.talim = talim;
  }

  public String getTalim() {
    return talim;
  }

  public void setAescBe(String aescBe) {
    this.aescBe = aescBe;
  }

  public String getAescBe() {
    return aescBe;
  }

  public void setMpTmpo(Long mpTmpo) {
    this.mpTmpo = mpTmpo;
  }

  public Long getMpTmpo() {
    return mpTmpo;
  }

  public void setFilter2(String filter2) {
    this.filter2 = filter2;
  }

  public String getFilter2() {
    return filter2;
  }

  public void setEempleo(Long eempleo) {
    this.eempleo = eempleo;
  }

  public Long getEempleo() {
    return eempleo;
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

  public void setSescM(String sescM) {
    this.sescM = sescM;
  }

  public String getSescM() {
    return sescM;
  }

  public void setFolioCuest(Long folioCuest) {
    this.folioCuest = folioCuest;
  }

  public Long getFolioCuest() {
    return folioCuest;
  }

  public void setOrFiPf(String orFiPf) {
    this.orFiPf = orFiPf;
  }

  public String getOrFiPf() {
    return orFiPf;
  }

  public void setMpInf(Long mpInf) {
    this.mpInf = mpInf;
  }

  public Long getMpInf() {
    return mpInf;
  }

  public void setScfv(Long scfv) {
    this.scfv = scfv;
  }

  public Long getScfv() {
    return scfv;
  }

  public void setSescC(String sescC) {
    this.sescC = sescC;
  }

  public String getSescC() {
    return sescC;
  }

  public void setAotorga(Long aotorga) {
    this.aotorga = aotorga;
  }

  public Long getAotorga() {
    return aotorga;
  }

  public void setEtrtoT(Long etrtoT) {
    this.etrtoT = etrtoT;
  }

  public Long getEtrtoT() {
    return etrtoT;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setApSocSat(String apSocSat) {
    this.apSocSat = apSocSat;
  }

  public String getApSocSat() {
    return apSocSat;
  }

  public void setMpFrRe(Long mpFrRe) {
    this.mpFrRe = mpFrRe;
  }

  public Long getMpFrRe() {
    return mpFrRe;
  }

  public void setAtPpSat(Long atPpSat) {
    this.atPpSat = atPpSat;
  }

  public Long getAtPpSat() {
    return atPpSat;
  }

  public void setAmSat(Long amSat) {
    this.amSat = amSat;
  }

  public Long getAmSat() {
    return amSat;
  }

  public void setTadic(String tadic) {
    this.tadic = tadic;
  }

  public String getTadic() {
    return tadic;
  }

  public void setSspla(Long sspla) {
    this.sspla = sspla;
  }

  public Long getSspla() {
    return sspla;
  }

  public void setSoprtM(Long soprtM) {
    this.soprtM = soprtM;
  }

  public Long getSoprtM() {
    return soprtM;
  }

  public void setAmSalud(Long amSalud) {
    this.amSalud = amSalud;
  }

  public Long getAmSalud() {
    return amSalud;
  }

  public void setEoport(Long eoport) {
    this.eoport = eoport;
  }

  public Long getEoport() {
    return eoport;
  }

  public void setAsatT(Long asatT) {
    this.asatT = asatT;
  }

  public Long getAsatT() {
    return asatT;
  }

  public void setSstpl(Long sstpl) {
    this.sstpl = sstpl;
  }

  public Long getSstpl() {
    return sstpl;
  }

  public void setSpemb(Long spemb) {
    this.spemb = spemb;
  }

  public Long getSpemb() {
    return spemb;
  }

  public void setTedu(String tedu) {
    this.tedu = tedu;
  }

  public String getTedu() {
    return tedu;
  }

  public void setTplanfa(String tplanfa) {
    this.tplanfa = tplanfa;
  }

  public String getTplanfa() {
    return tplanfa;
  }

  public void setCveUpm(String cveUpm) {
    this.cveUpm = cveUpm;
  }

  public String getCveUpm() {
    return cveUpm;
  }

  public void setCapTsat(Long capTsat) {
    this.capTsat = capTsat;
  }

  public Long getCapTsat() {
    return capTsat;
  }

  public void setCapT(Long capT) {
    this.capT = capT;
  }

  public Long getCapT() {
    return capT;
  }

  public void setAmOtorg(Long amOtorg) {
    this.amOtorg = amOtorg;
  }

  public Long getAmOtorg() {
    return amOtorg;
  }

  public void setEnivel(Long enivel) {
    this.enivel = enivel;
  }

  public Long getEnivel() {
    return enivel;
  }

  public void setAsat(Long asat) {
    this.asat = asat;
  }

  public Long getAsat() {
    return asat;
  }

  public void setAoprt(Long aoprt) {
    this.aoprt = aoprt;
  }

  public Long getAoprt() {
    return aoprt;
  }

  public void setAtPp(Long atPp) {
    this.atPp = atPp;
  }

  public Long getAtPp() {
    return atPp;
  }

  public void setOrFi(String orFi) {
    this.orFi = orFi;
  }

  public String getOrFi() {
    return orFi;
  }

  public void setSpfvS(Long spfvS) {
    this.spfvS = spfvS;
  }

  public Long getSpfvS() {
    return spfvS;
  }

  public void setSpadic(Long spadic) {
    this.spadic = spadic;
  }

  public Long getSpadic() {
    return spadic;
  }

  public void setAtPpCap(Long atPpCap) {
    this.atPpCap = atPpCap;
  }

  public Long getAtPpCap() {
    return atPpCap;
  }

  public void setEescE(String eescE) {
    this.eescE = eescE;
  }

  public String getEescE() {
    return eescE;
  }

  public void setTsalud(String tsalud) {
    this.tsalud = tsalud;
  }

  public String getTsalud() {
    return tsalud;
  }

  public void setTespeci(String tespeci) {
    this.tespeci = tespeci;
  }

  public String getTespeci() {
    return tespeci;
  }

  public void setTotros(String totros) {
    this.totros = totros;
  }

  public String getTotros() {
    return totros;
  }

  public void setSsatC(Long ssatC) {
    this.ssatC = ssatC;
  }

  public Long getSsatC() {
    return ssatC;
  }

  public void setSstc(Long sstc) {
    this.sstc = sstc;
  }

  public Long getSstc() {
    return sstc;
  }

  public void setOrFiSat(String orFiSat) {
    this.orFiSat = orFiSat;
  }

  public String getOrFiSat() {
    return orFiSat;
  }

  public void setEotorga(Long eotorga) {
    this.eotorga = eotorga;
  }

  public Long getEotorga() {
    return eotorga;
  }

  public void setMpAsist(Long mpAsist) {
    this.mpAsist = mpAsist;
  }

  public Long getMpAsist() {
    return mpAsist;
  }

  public void setAmOport(Long amOport) {
    this.amOport = amOport;
  }

  public Long getAmOport() {
    return amOport;
  }

  public void setIdFamilia(Long idFamilia) {
    this.idFamilia = idFamilia;
  }

  public Long getIdFamilia() {
    return idFamilia;
  }

  public void setEsat(Long esat) {
    this.esat = esat;
  }

  public Long getEsat() {
    return esat;
  }

  public void setAmAlim(Long amAlim) {
    this.amAlim = amAlim;
  }

  public Long getAmAlim() {
    return amAlim;
  }

  public void setEcont(Long econt) {
    this.econt = econt;
  }

  public Long getEcont() {
    return econt;
  }

  public void setAmejora(Long amejora) {
    this.amejora = amejora;
  }

  public Long getAmejora() {
    return amejora;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setSescAs(String sescAs) {
    this.sescAs = sescAs;
  }

  public String getSescAs() {
    return sescAs;
  }

  public void setMpTrto(Long mpTrto) {
    this.mpTrto = mpTrto;
  }

  public Long getMpTrto() {
    return mpTrto;
  }

  public void setAmEsc(String amEsc) {
    this.amEsc = amEsc;
  }

  public String getAmEsc() {
    return amEsc;
  }

  public void setSplFvA(Long splFvA) {
    this.splFvA = splFvA;
  }

  public Long getSplFvA() {
    return splFvA;
  }

  public void setApSocNv(String apSocNv) {
    this.apSocNv = apSocNv;
  }

  public String getApSocNv() {
    return apSocNv;
  }

  public void setConsecutiv(String consecutiv) {
    this.consecutiv = consecutiv;
  }

  public String getConsecutiv() {
    return consecutiv;
  }

  public void setSotroga(Long sotroga) {
    this.sotroga = sotroga;
  }

  public Long getSotroga() {
    return sotroga;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdFamilia();
  }

  @Override
  public void setKey(Long key) {
  	this.idFamilia = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getTprogra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEconc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCapTcap());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getApSoc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTalim());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAescBe());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMpTmpo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter2());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEempleo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter1());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFilter3());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSescM());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolioCuest());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrFiPf());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMpInf());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getScfv());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSescC());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAotorga());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEtrtoT());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getApSocSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMpFrRe());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAtPpSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAmSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTadic());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSspla());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSoprtM());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAmSalud());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEoport());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAsatT());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSstpl());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSpemb());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTedu());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTplanfa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCveUpm());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCapTsat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCapT());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAmOtorg());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEnivel());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAsat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAoprt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAtPp());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrFi());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSpfvS());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSpadic());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAtPpCap());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEescE());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTsalud());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTespeci());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotros());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSsatC());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSstc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrFiSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEotorga());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMpAsist());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAmOport());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFamilia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEsat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAmAlim());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEcont());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAmejora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSescAs());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMpTrto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAmEsc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSplFvA());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getApSocNv());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutiv());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSotroga());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("tprogra", getTprogra());
		regresar.put("econc", getEconc());
		regresar.put("capTcap", getCapTcap());
		regresar.put("apSoc", getApSoc());
		regresar.put("talim", getTalim());
		regresar.put("aescBe", getAescBe());
		regresar.put("mpTmpo", getMpTmpo());
		regresar.put("filter2", getFilter2());
		regresar.put("eempleo", getEempleo());
		regresar.put("filter1", getFilter1());
		regresar.put("filter3", getFilter3());
		regresar.put("sescM", getSescM());
		regresar.put("folioCuest", getFolioCuest());
		regresar.put("orFiPf", getOrFiPf());
		regresar.put("mpInf", getMpInf());
		regresar.put("scfv", getScfv());
		regresar.put("sescC", getSescC());
		regresar.put("aotorga", getAotorga());
		regresar.put("etrtoT", getEtrtoT());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("apSocSat", getApSocSat());
		regresar.put("mpFrRe", getMpFrRe());
		regresar.put("atPpSat", getAtPpSat());
		regresar.put("amSat", getAmSat());
		regresar.put("tadic", getTadic());
		regresar.put("sspla", getSspla());
		regresar.put("soprtM", getSoprtM());
		regresar.put("amSalud", getAmSalud());
		regresar.put("eoport", getEoport());
		regresar.put("asatT", getAsatT());
		regresar.put("sstpl", getSstpl());
		regresar.put("spemb", getSpemb());
		regresar.put("tedu", getTedu());
		regresar.put("tplanfa", getTplanfa());
		regresar.put("cveUpm", getCveUpm());
		regresar.put("capTsat", getCapTsat());
		regresar.put("capT", getCapT());
		regresar.put("amOtorg", getAmOtorg());
		regresar.put("enivel", getEnivel());
		regresar.put("asat", getAsat());
		regresar.put("aoprt", getAoprt());
		regresar.put("atPp", getAtPp());
		regresar.put("orFi", getOrFi());
		regresar.put("spfvS", getSpfvS());
		regresar.put("spadic", getSpadic());
		regresar.put("atPpCap", getAtPpCap());
		regresar.put("eescE", getEescE());
		regresar.put("tsalud", getTsalud());
		regresar.put("tespeci", getTespeci());
		regresar.put("totros", getTotros());
		regresar.put("ssatC", getSsatC());
		regresar.put("sstc", getSstc());
		regresar.put("orFiSat", getOrFiSat());
		regresar.put("eotorga", getEotorga());
		regresar.put("mpAsist", getMpAsist());
		regresar.put("amOport", getAmOport());
		regresar.put("idFamilia", getIdFamilia());
		regresar.put("esat", getEsat());
		regresar.put("amAlim", getAmAlim());
		regresar.put("econt", getEcont());
		regresar.put("amejora", getAmejora());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("sescAs", getSescAs());
		regresar.put("mpTrto", getMpTrto());
		regresar.put("amEsc", getAmEsc());
		regresar.put("splFvA", getSplFvA());
		regresar.put("apSocNv", getApSocNv());
		regresar.put("consecutiv", getConsecutiv());
		regresar.put("sotroga", getSotroga());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getTprogra(), getEconc(), getCapTcap(), getApSoc(), getTalim(), getAescBe(), getMpTmpo(), getFilter2(), getEempleo(), getFilter1(), getFilter3(), getSescM(), getFolioCuest(), getOrFiPf(), getMpInf(), getScfv(), getSescC(), getAotorga(), getEtrtoT(), getIdMuestra(), getApSocSat(), getMpFrRe(), getAtPpSat(), getAmSat(), getTadic(), getSspla(), getSoprtM(), getAmSalud(), getEoport(), getAsatT(), getSstpl(), getSpemb(), getTedu(), getTplanfa(), getCveUpm(), getCapTsat(), getCapT(), getAmOtorg(), getEnivel(), getAsat(), getAoprt(), getAtPp(), getOrFi(), getSpfvS(), getSpadic(), getAtPpCap(), getEescE(), getTsalud(), getTespeci(), getTotros(), getSsatC(), getSstc(), getOrFiSat(), getEotorga(), getMpAsist(), getAmOport(), getIdFamilia(), getEsat(), getAmAlim(), getEcont(), getAmejora(), getIdUsuario(), getSescAs(), getMpTrto(), getAmEsc(), getSplFvA(), getApSocNv(), getConsecutiv(), getSotroga()
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
    regresar.append("idFamilia~");
    regresar.append(getIdFamilia());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFamilia());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalFamiliasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFamilia()!= null && getIdFamilia()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalFamiliasDto other = (TrJanalFamiliasDto) obj;
    if (getIdFamilia() != other.idFamilia && (getIdFamilia() == null || !getIdFamilia().equals(other.idFamilia))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFamilia() != null ? getIdFamilia().hashCode() : 0);
    return hash;
  }

  public String[] getTsaludMultiple() {
    return new String[] {tsalud};
  }

  public void setTsaludMultiple(String[] tsaludMultiple) {
    this.tsalud = tsaludMultiple== null? null: tsaludMultiple.length> 0? tsaludMultiple[0]: null;
  }

  public String[] getTalimMultiple() {
    return new String[] {talim};
  }

  public void setTalimMultiple(String[] talimMultiple) {
    this.talim = talimMultiple== null? null: talimMultiple.length> 0? talimMultiple[0]: null;
  }

  public String[] getTadicMultiple() {
    return new String[] {tadic};
  }

  public void setTadicMultiple(String[] tadicMultiple) {
    this.tadic = tadicMultiple== null? null: tadicMultiple.length> 0? tadicMultiple[0]: null;
  }

  public String[] getTplanfaMultiple() {
    return new String[] {tplanfa};
  }

  public void setTplanfaMultiple(String[] tplanfaMultiple) {
    this.tplanfa = tplanfaMultiple== null? null: tplanfaMultiple.length> 0? tplanfaMultiple[0]: null;
  }

  public String[] getTeduMultiple() {
    return new String[] {tedu};
  }

  public void setTeduMultiple(String[] teduMultiple) {
    this.tedu = teduMultiple== null? null: teduMultiple.length> 0? teduMultiple[0]: null;
  }

  public String[] getTprograMultiple() {
    return new String[] {tprogra};
  }

  public void setTprograMultiple(String[] tprograMultiple) {
    this.tprogra = tprograMultiple== null? null: tprograMultiple.length> 0? tprograMultiple[0]: null;
  }

  public String[] getTotrosMultiple() {
    return new String[] {totros};
  }

  public void setTotrosMultiple(String[] totrosMultiple) {
    this.totros = totrosMultiple== null? null: totrosMultiple.length> 0 ? totrosMultiple[0]: null;
  }


  public void setE_conc(Long econc) {
    this.econc = econc;
  }

  public Long getE_conc() {
    return econc;
  }

  public void setCap_t_cap(String capTcap) {
    this.capTcap = capTcap;
  }

  public String getCap_t_cap() {
    return capTcap;
  }

  public void setAp_soc(String apSoc) {
    this.apSoc = apSoc;
  }

  public String getAp_soc() {
    return apSoc;
  }

  public void setT_alim(String talim) {
    this.talim = talim;
  }

  public String getT_alim() {
    return talim;
  }

  public void setA_esc_be(String aescBe) {
    this.aescBe = aescBe;
  }

  public String getA_esc_be() {
    return aescBe;
  }

  public void setMp_tmpo(Long mpTmpo) {
    this.mpTmpo = mpTmpo;
  }

  public Long getMp_tmpo() {
    return mpTmpo;
  }

  public void setE_empleo(Long eempleo) {
    this.eempleo = eempleo;
  }

  public Long getE_empleo() {
    return eempleo;
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

  public void setS_esc_m(String sescM) {
    this.sescM = sescM;
  }

  public String getS_esc_m() {
    return sescM;
  }

  public void setFolio_cuest(Long folioCuest) {
    this.folioCuest = folioCuest;
  }

  public Long getFolio_cuest() {
    return folioCuest;
  }

  public void setOr_fi_pf(String orFiPf) {
    this.orFiPf = orFiPf;
  }

  public String getOr_fi_pf() {
    return orFiPf;
  }

  public void setMp_inf(Long mpInf) {
    this.mpInf = mpInf;
  }

  public Long getMp_inf() {
    return mpInf;
  }

  public void setS_c_fv(Long scfv) {
    this.scfv = scfv;
  }

  public Long getS_c_fv() {
    return scfv;
  }

  public void setS_esc_c(String sescC) {
    this.sescC = sescC;
  }

  public String getS_esc_c() {
    return sescC;
  }

  public void setA_otorga(Long aotorga) {
    this.aotorga = aotorga;
  }

  public Long getA_otorga() {
    return aotorga;
  }

  public void setE_trto_t(Long etrtoT) {
    this.etrtoT = etrtoT;
  }

  public Long getE_trto_t() {
    return etrtoT;
  }

  public void setId_muestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getId_muestra() {
    return idMuestra;
  }

  public void setAp_soc_sat(String apSocSat) {
    this.apSocSat = apSocSat;
  }

  public String getAp_soc_sat() {
    return apSocSat;
  }

  public void setMp_fr_re(Long mpFrRe) {
    this.mpFrRe = mpFrRe;
  }

  public Long getMp_fr_re() {
    return mpFrRe;
  }

  public void setAt_pp_sat(Long atPpSat) {
    this.atPpSat = atPpSat;
  }

  public Long getAt_pp_sat() {
    return atPpSat;
  }

  public void setAm_sat(Long amSat) {
    this.amSat = amSat;
  }

  public Long getAm_sat() {
    return amSat;
  }

  public void setT_adic(String tadic) {
    this.tadic = tadic;
  }

  public String getT_adic() {
    return tadic;
  }

  public void setS_s_pla(Long sspla) {
    this.sspla = sspla;
  }

  public Long getS_s_pla() {
    return sspla;
  }

  public void setS_oprt_m(Long soprtM) {
    this.soprtM = soprtM;
  }

  public Long getS_oprt_m() {
    return soprtM;
  }

  public void setAm_salud(Long amSalud) {
    this.amSalud = amSalud;
  }

  public Long getAm_salud() {
    return amSalud;
  }

  public void setE_oport(Long eoport) {
    this.eoport = eoport;
  }

  public Long getE_oport() {
    return eoport;
  }

  public void setA_sat_t(Long asatT) {
    this.asatT = asatT;
  }

  public Long getA_sat_t() {
    return asatT;
  }

  public void setS_s_t_pl(Long sstpl) {
    this.sstpl = sstpl;
  }

  public Long getS_s_t_pl() {
    return sstpl;
  }

  public void setS_p_emb(Long spemb) {
    this.spemb = spemb;
  }

  public Long getS_p_emb() {
    return spemb;
  }

  public void setT_edu(String tedu) {
    this.tedu = tedu;
  }

  public String getT_edu() {
    return tedu;
  }

  public void setT_planfa(String tplanfa) {
    this.tplanfa = tplanfa;
  }

  public String getT_planfa() {
    return tplanfa;
  }

  public void setCve_upm(String cveUpm) {
    this.cveUpm = cveUpm;
  }

  public String getCve_upm() {
    return cveUpm;
  }

  public void setCap_t_sat(Long capTsat) {
    this.capTsat = capTsat;
  }

  public Long getCap_t_sat() {
    return capTsat;
  }

  public void setCap_t(Long capT) {
    this.capT = capT;
  }

  public Long getCap_t() {
    return capT;
  }

  public void setAm_otorg(Long amOtorg) {
    this.amOtorg = amOtorg;
  }

  public Long getAm_otorg() {
    return amOtorg;
  }

  public void setE_nivel(Long enivel) {
    this.enivel = enivel;
  }

  public Long getE_nivel() {
    return enivel;
  }

  public void setA_sat(Long asat) {
    this.asat = asat;
  }

  public Long getA_sat() {
    return asat;
  }

  public void setA_oprt(Long aoprt) {
    this.aoprt = aoprt;
  }

  public Long getA_oprt() {
    return aoprt;
  }

  public void setAt_pp(Long atPp) {
    this.atPp = atPp;
  }

  public Long getAt_pp() {
    return atPp;
  }

  public void setOr_fi(String orFi) {
    this.orFi = orFi;
  }

  public String getOr_fi() {
    return orFi;
  }

  public void setS_p_fv_s(Long spfvS) {
    this.spfvS = spfvS;
  }

  public Long getS_p_fv_s() {
    return spfvS;
  }

  public void setS_p_adic(Long spadic) {
    this.spadic = spadic;
  }

  public Long getS_p_adic() {
    return spadic;
  }

  public void setAt_pp_cap(Long atPpCap) {
    this.atPpCap = atPpCap;
  }

  public Long getAt_pp_cap() {
    return atPpCap;
  }

  public void setE_esc_e(String eescE) {
    this.eescE = eescE;
  }

  public String getE_esc_e() {
    return eescE;
  }

  public void setT_salud(String tsalud) {
    this.tsalud = tsalud;
  }

  public String getT_salud() {
    return tsalud;
  }

  public void setT_especi(String tespeci) {
    this.tespeci = tespeci;
  }

  public String getT_especi() {
    return tespeci;
  }

  public void setT_otros(String totros) {
    this.totros = totros;
  }

  public String getT_otros() {
    return totros;
  }

  public void setS_sat_c(Long ssatC) {
    this.ssatC = ssatC;
  }

  public Long getS_sat_c() {
    return ssatC;
  }

  public void setS_s_t_c(Long sstc) {
    this.sstc = sstc;
  }

  public Long getS_s_t_c() {
    return sstc;
  }

  public void setOr_fi_sat(String orFiSat) {
    this.orFiSat = orFiSat;
  }

  public String getOr_fi_sat() {
    return orFiSat;
  }

  public void setE_otorga(Long eotorga) {
    this.eotorga = eotorga;
  }

  public Long getE_otorga() {
    return eotorga;
  }

  public void setMp_asist(Long mpAsist) {
    this.mpAsist = mpAsist;
  }

  public Long getMp_asist() {
    return mpAsist;
  }

  public void setAm_oport(Long amOport) {
    this.amOport = amOport;
  }

  public Long getAm_oport() {
    return amOport;
  }

  public void setId_familia(Long idFamilia) {
    this.idFamilia = idFamilia;
  }

  public Long getId_familia() {
    return idFamilia;
  }

  public void setE_sat(Long esat) {
    this.esat = esat;
  }

  public Long getE_sat() {
    return esat;
  }

  public void setAm_alim(Long amAlim) {
    this.amAlim = amAlim;
  }

  public Long getAm_alim() {
    return amAlim;
  }

  public void setE_cont(Long econt) {
    this.econt = econt;
  }

  public Long getE_cont() {
    return econt;
  }

  public void setA_mejora(Long amejora) {
    this.amejora = amejora;
  }

  public Long getA_mejora() {
    return amejora;
  }

  public void setId_usuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getId_usuario() {
    return idUsuario;
  }

  public void setS_esc_as(String sescAs) {
    this.sescAs = sescAs;
  }

  public String getS_esc_as() {
    return sescAs;
  }

  public void setMp_trto(Long mpTrto) {
    this.mpTrto = mpTrto;
  }

  public Long getMp_trto() {
    return mpTrto;
  }

  public void setAm_esc(String amEsc) {
    this.amEsc = amEsc;
  }

  public String getAm_esc() {
    return amEsc;
  }

  public void setS_pl_fv_a(Long splFvA) {
    this.splFvA = splFvA;
  }

  public Long getS_pl_fv_a() {
    return splFvA;
  }

  public void setAp_soc_nv(String apSocNv) {
    this.apSocNv = apSocNv;
  }

  public String getAp_soc_nv() {
    return apSocNv;
  }

  public void setS_otroga(Long sotroga) {
    this.sotroga = sotroga;
  }

  public Long getS_otroga() {
    return sotroga;
  }
  
}


