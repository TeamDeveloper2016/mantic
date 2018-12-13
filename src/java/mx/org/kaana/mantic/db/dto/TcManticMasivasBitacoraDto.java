package mx.org.kaana.mantic.db.dto;

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
@Table(name="tc_mantic_masivas_bitacora")
public class TcManticMasivasBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_masiva_archivo")
  private Long idMasivaArchivo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_masiva_bitacora")
  private Long idMasivaBitacora;
  @Column (name="procesados")
  private Long procesados;
  @Column (name="id_masiva_estatus")
  private Long idMasivaEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticMasivasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticMasivasBitacoraDto(Long key) {
    this(null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticMasivasBitacoraDto(String justificacion, Long idMasivaArchivo, Long idUsuario, Long idMasivaBitacora, Long procesados, Long idMasivaEstatus) {
    setJustificacion(justificacion);
    setIdMasivaArchivo(idMasivaArchivo);
    setIdUsuario(idUsuario);
    setIdMasivaBitacora(idMasivaBitacora);
    setProcesados(procesados);
    setIdMasivaEstatus(idMasivaEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdMasivaArchivo(Long idMasivaArchivo) {
    this.idMasivaArchivo = idMasivaArchivo;
  }

  public Long getIdMasivaArchivo() {
    return idMasivaArchivo;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdMasivaBitacora(Long idMasivaBitacora) {
    this.idMasivaBitacora = idMasivaBitacora;
  }

  public Long getIdMasivaBitacora() {
    return idMasivaBitacora;
  }

  public void setProcesados(Long procesados) {
    this.procesados = procesados;
  }

  public Long getProcesados() {
    return procesados;
  }

  public void setIdMasivaEstatus(Long idMasivaEstatus) {
    this.idMasivaEstatus = idMasivaEstatus;
  }

  public Long getIdMasivaEstatus() {
    return idMasivaEstatus;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdMasivaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idMasivaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMasivaArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMasivaBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProcesados());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMasivaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idMasivaArchivo", getIdMasivaArchivo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idMasivaBitacora", getIdMasivaBitacora());
		regresar.put("procesados", getProcesados());
		regresar.put("idMasivaEstatus", getIdMasivaEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdMasivaArchivo(), getIdUsuario(), getIdMasivaBitacora(), getProcesados(), getIdMasivaEstatus(), getRegistro()
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
    regresar.append("idMasivaBitacora~");
    regresar.append(getIdMasivaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMasivaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticMasivasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMasivaBitacora()!= null && getIdMasivaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticMasivasBitacoraDto other = (TcManticMasivasBitacoraDto) obj;
    if (getIdMasivaBitacora() != other.idMasivaBitacora && (getIdMasivaBitacora() == null || !getIdMasivaBitacora().equals(other.idMasivaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMasivaBitacora() != null ? getIdMasivaBitacora().hashCode() : 0);
    return hash;
  }

}


