package mx.org.kaana.mantic.db.dto;

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

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_cierres")
public class TcManticCierresDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cierre")
  private Long idCierre;
  @Column (name="id_diferencias")
  private Long idDiferencias;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_cierre_estatus")
  private Long idCierreEstatus;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="orden")
  private Long orden;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="termino")
  private Timestamp termino;

  public TcManticCierresDto() {
    this(new Long(-1L));
  }

  public TcManticCierresDto(Long key) {
    this(null, new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticCierresDto(String consecutivo, Long idCierre, Long idDiferencias, Long idUsuario, Long idCierreEstatus, String observaciones, Long orden, Long ejercicio) {
		this(consecutivo, idCierre, idDiferencias, idUsuario, idCierreEstatus, observaciones, orden, ejercicio, new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}
	
  public TcManticCierresDto(String consecutivo, Long idCierre, Long idDiferencias, Long idUsuario, Long idCierreEstatus, String observaciones, Long orden, Long ejercicio, Timestamp termino) {
    setConsecutivo(consecutivo);
    setIdCierre(idCierre);
    setIdDiferencias(idDiferencias);
    setIdUsuario(idUsuario);
    setIdCierreEstatus(idCierreEstatus);
    setObservaciones(observaciones);
    setOrden(orden);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setTermino(termino);
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setIdCierre(Long idCierre) {
    this.idCierre = idCierre;
  }

  public Long getIdCierre() {
    return idCierre;
  }

  public void setIdDiferencias(Long idDiferencias) {
    this.idDiferencias = idDiferencias;
  }

  public Long getIdDiferencias() {
    return idDiferencias;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdCierreEstatus(Long idCierreEstatus) {
    this.idCierreEstatus = idCierreEstatus;
  }

  public Long getIdCierreEstatus() {
    return idCierreEstatus;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public Timestamp getTermino() {
		return termino;
	}

	public void setTermino(Timestamp termino) {
		this.termino=termino;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdCierre();
  }

  @Override
  public void setKey(Long key) {
  	this.idCierre = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDiferencias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCierreEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTermino());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idCierre", getIdCierre());
		regresar.put("idDiferencias", getIdDiferencias());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idCierreEstatus", getIdCierreEstatus());
		regresar.put("observaciones", getObservaciones());
		regresar.put("orden", getOrden());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("termino", getTermino());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getIdCierre(), getIdDiferencias(), getIdUsuario(), getIdCierreEstatus(), getObservaciones(), getOrden(), getEjercicio(), getRegistro(), getTermino()
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
    regresar.append("idCierre~");
    regresar.append(getIdCierre());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCierre());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCierresDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCierre()!= null && getIdCierre()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCierresDto other = (TcManticCierresDto) obj;
    if (getIdCierre() != other.idCierre && (getIdCierre() == null || !getIdCierre().equals(other.idCierre))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCierre() != null ? getIdCierre().hashCode() : 0);
    return hash;
  }

}


