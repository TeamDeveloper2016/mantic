package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
import java.sql.Date;
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

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tr_janal_visitas")
public class TrJanalVisitasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_resultado")
  private Long idResultado;
  @Column (name="nom_ent")
  private String nomEnt;
  @Column (name="fecha")
  private Date fecha;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_visita")
  private Long idVisita;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="hr_ini")
  private Timestamp hrIni;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="hr_ter")
  private Timestamp hrTer;
  @Column (name="id_muestra")
  private Long idMuestra;
  @Column (name="visita")
  private Long visita;

  public TrJanalVisitasDto() {
    this(new Long(-1L));
  }

  public TrJanalVisitasDto(Long key) {
    this(null, null, new Date(Calendar.getInstance().getTimeInMillis()), new Long(-1L), null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null);
    setKey(key);
  }

  public TrJanalVisitasDto(Long idResultado, String nomEnt, Date fecha, Long idVisita, Long idUsuario, Timestamp hrIni, String observaciones, Timestamp hrTer, Long idMuestra, Long visita) {
    setIdResultado(idResultado);
    setNomEnt(nomEnt);
    setFecha(fecha);
    setIdVisita(idVisita);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdUsuario(idUsuario);
    setHrIni(hrIni);
    setObservaciones(observaciones);
    setHrTer(hrTer);
    setIdMuestra(idMuestra);
    setVisita(visita);
  }
	
  public void setIdResultado(Long idResultado) {
    this.idResultado = idResultado;
  }

  public Long getIdResultado() {
    return idResultado;
  }

  public void setNomEnt(String nomEnt) {
    this.nomEnt = nomEnt;
  }

  public String getNomEnt() {
    return nomEnt;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setIdVisita(Long idVisita) {
    this.idVisita = idVisita;
  }

  public Long getIdVisita() {
    return idVisita;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setHrIni(Timestamp hrIni) {
    this.hrIni = hrIni;
  }

  public Timestamp getHrIni() {
    return hrIni;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setHrTer(Timestamp hrTer) {
    this.hrTer = hrTer;
  }

  public Timestamp getHrTer() {
    return hrTer;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setVisita(Long visita) {
    this.visita = visita;
  }

  public Long getVisita() {
    return visita;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdVisita();
  }

  @Override
  public void setKey(Long key) {
  	this.idVisita = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdResultado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNomEnt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVisita());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getHrIni());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getHrTer());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVisita());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idResultado", getIdResultado());
		regresar.put("nomEnt", getNomEnt());
		regresar.put("fecha", getFecha());
		regresar.put("idVisita", getIdVisita());
		regresar.put("registro", getRegistro());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("hrIni", getHrIni());
		regresar.put("observaciones", getObservaciones());
		regresar.put("hrTer", getHrTer());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("visita", getVisita());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdResultado(), getNomEnt(), getFecha(), getIdVisita(), getRegistro(), getIdUsuario(), getHrIni(), getObservaciones(), getHrTer(), getIdMuestra(), getVisita()
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
    regresar.append("idVisita~");
    regresar.append(getIdVisita());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdVisita());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalVisitasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdVisita()!= null && getIdVisita()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalVisitasDto other = (TrJanalVisitasDto) obj;
    if (getIdVisita() != other.idVisita && (getIdVisita() == null || !getIdVisita().equals(other.idVisita))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdVisita() != null ? getIdVisita().hashCode() : 0);
    return hash;
  }

  public void setId_resultado(Long idResultado) {
    this.idResultado = idResultado;
  }

  public Long getId_resultado() {
    return idResultado;
  }

  public void setNom_ent(String nomEnt) {
    this.nomEnt = nomEnt;
  }

  public String getNom_ent() {
    return nomEnt;
  }

  public void setId_visita(Long idVisita) {
    this.idVisita = idVisita;
  }

  public Long getId_visita() {
    return idVisita;
  }

  public void setId_usuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getId_usuario() {
    return idUsuario;
  }

  public void setHr_ini(Timestamp hrIni) {
    this.hrIni = hrIni;
  }

  public Timestamp getHr_ini() {
    return hrIni;
  }

  public void setHr_ter(Timestamp hrTer) {
    this.hrTer = hrTer;
  }

  public Timestamp getHr_ter() {
    return hrTer;
  }

  public void setId_muestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getId_muestra() {
    return idMuestra;
  }
  
}


