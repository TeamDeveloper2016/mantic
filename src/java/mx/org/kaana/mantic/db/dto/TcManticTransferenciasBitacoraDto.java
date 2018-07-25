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
 *@date 21/07/2018
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tc_mantic_transferencias_bitacora")
public class TcManticTransferenciasBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_transferencia_bitacora")
  private Long idTransferenciaBitacora;
	@Column (name="id_transferencia")
  private Long idTransferencia;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_transferencia_estatus")
  private Long idTransferenciaEstatus;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="registro")
  private Timestamp registro;
  
  
  public TcManticTransferenciasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticTransferenciasBitacoraDto(Long key) {
    this(new Long(-1L),new Long(-1L),new Long(-1L),new Long(-1L),null);
    setKey(key);
  }

  public TcManticTransferenciasBitacoraDto(Long idTransferenciaBitacora, Long idTransferencia, Long idUsuario, Long idTransferenciaEstatus, String justificacion) {
    setIdTransferencia(idTransferencia);
    setIdTransferenciaBitacora(idTransferenciaBitacora);
    setIdUsuario(idUsuario);
    setIdTransferenciaEstatus(idTransferenciaEstatus);
    setJustificacion(justificacion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));;
  }

  public Long getIdTransferenciaBitacora() {
    return idTransferenciaBitacora;
  }

  public void setIdTransferenciaBitacora(Long idTransferenciaBitacora) {
    this.idTransferenciaBitacora = idTransferenciaBitacora;
  }

  public Long getIdTransferencia() {
    return idTransferencia;
  }

  public void setIdTransferencia(Long idTransferencia) {
    this.idTransferencia = idTransferencia;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdTransferenciaEstatus() {
    return idTransferenciaEstatus;
  }

  public void setIdTransferenciaEstatus(Long idTransferenciaEstatus) {
    this.idTransferenciaEstatus = idTransferenciaEstatus;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdTransferencia();
  }

  @Override
  public void setKey(Long key) {
  	this.idTransferencia = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdTransferenciaBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idTransferenciaBitacora", getIdTransferenciaBitacora());
    regresar.put("idTransferencia", getIdTransferencia());
    regresar.put("idTransferenciaEstatus", getIdTransferenciaEstatus());
    regresar.put("idUsuario", getIdUsuario());
    regresar.put("justificacion", getJustificacion());
    regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdTransferenciaBitacora(),
      getIdTransferencia(),
      getIdTransferenciaEstatus(),
      getIdUsuario(),
      getJustificacion(),
      getRegistro()
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
    regresar.append("idTransferenciaBitacora~");
    regresar.append(getIdTransferenciaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTransferenciaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTransferenciaBitacora()!= null && getIdTransferenciaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTransferenciasBitacoraDto other = (TcManticTransferenciasBitacoraDto) obj;
    if (getIdTransferenciaBitacora() != other.idTransferenciaBitacora && (getIdTransferenciaBitacora() == null || !getIdTransferenciaBitacora().equals(other.idTransferenciaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTransferenciaBitacora()!= null ? getIdTransferenciaBitacora().hashCode() : 0);
    return hash;
  }

}


