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
@Table(name="tc_mantic_transferencias_multiples_bitacora")
public class TcManticTransferenciasMultiplesBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_transferencia_multiple_estatus")
  private Long idTransferenciaMultipleEstatus;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_transferencia_multiple")
  private Long idTransferenciaMultiple;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_transporto")
  private Long idTransporto;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_transferencia_multiple_bitacora")
  private Long idTransferenciaMultipleBitacora;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTransferenciasMultiplesBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticTransferenciasMultiplesBitacoraDto(Long key) {
    this(null, null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticTransferenciasMultiplesBitacoraDto(Long idTransferenciaMultipleEstatus, String justificacion, Long idTransferenciaMultiple, Long idUsuario, Long idTransporto, Long idTransferenciaMultipleBitacora) {
    setIdTransferenciaMultipleEstatus(idTransferenciaMultipleEstatus);
    setJustificacion(justificacion);
    setIdTransferenciaMultiple(idTransferenciaMultiple);
    setIdUsuario(idUsuario);
    setIdTransporto(idTransporto);
    setIdTransferenciaMultipleBitacora(idTransferenciaMultipleBitacora);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdTransferenciaMultipleEstatus(Long idTransferenciaMultipleEstatus) {
    this.idTransferenciaMultipleEstatus = idTransferenciaMultipleEstatus;
  }

  public Long getIdTransferenciaMultipleEstatus() {
    return idTransferenciaMultipleEstatus;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdTransferenciaMultiple(Long idTransferenciaMultiple) {
    this.idTransferenciaMultiple = idTransferenciaMultiple;
  }

  public Long getIdTransferenciaMultiple() {
    return idTransferenciaMultiple;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdTransporto(Long idTransporto) {
    this.idTransporto = idTransporto;
  }

  public Long getIdTransporto() {
    return idTransporto;
  }

  public void setIdTransferenciaMultipleBitacora(Long idTransferenciaMultipleBitacora) {
    this.idTransferenciaMultipleBitacora = idTransferenciaMultipleBitacora;
  }

  public Long getIdTransferenciaMultipleBitacora() {
    return idTransferenciaMultipleBitacora;
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
  	return getIdTransferenciaMultipleBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idTransferenciaMultipleBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdTransferenciaMultipleEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaMultiple());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransporto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaMultipleBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idTransferenciaMultipleEstatus", getIdTransferenciaMultipleEstatus());
		regresar.put("justificacion", getJustificacion());
		regresar.put("idTransferenciaMultiple", getIdTransferenciaMultiple());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTransporto", getIdTransporto());
		regresar.put("idTransferenciaMultipleBitacora", getIdTransferenciaMultipleBitacora());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdTransferenciaMultipleEstatus(), getJustificacion(), getIdTransferenciaMultiple(), getIdUsuario(), getIdTransporto(), getIdTransferenciaMultipleBitacora(), getRegistro()
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
    regresar.append("idTransferenciaMultipleBitacora~");
    regresar.append(getIdTransferenciaMultipleBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTransferenciaMultipleBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasMultiplesBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTransferenciaMultipleBitacora()!= null && getIdTransferenciaMultipleBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTransferenciasMultiplesBitacoraDto other = (TcManticTransferenciasMultiplesBitacoraDto) obj;
    if (getIdTransferenciaMultipleBitacora() != other.idTransferenciaMultipleBitacora && (getIdTransferenciaMultipleBitacora() == null || !getIdTransferenciaMultipleBitacora().equals(other.idTransferenciaMultipleBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTransferenciaMultipleBitacora() != null ? getIdTransferenciaMultipleBitacora().hashCode() : 0);
    return hash;
  }

}


