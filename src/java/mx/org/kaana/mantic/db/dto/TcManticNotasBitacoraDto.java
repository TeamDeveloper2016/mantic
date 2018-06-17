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
@Table(name="tc_mantic_notas_bitacora")
public class TcManticNotasBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_nota_bitacora")
  private Long idNotaBitacora;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="id_nota_estatus")
  private Long idNotaEstatus;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="importe")
  private Double importe;

  public TcManticNotasBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticNotasBitacoraDto(Long key) {
    this(new Long(-1L), null, null, null, null, "", 0D);
    setKey(key);
  }

  public TcManticNotasBitacoraDto(Long idNotaBitacora, String justificacion, Long idUsuario, Long idNotaEntrada, Long idNotaEstatus, String consecutivo, Double importe) {
    setIdNotaBitacora(idNotaBitacora);
    setJustificacion(justificacion);
    setIdUsuario(idUsuario);
    setIdNotaEntrada(idNotaEntrada);
    setIdNotaEstatus(idNotaEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setImporte(importe);
}
	
  public void setIdNotaBitacora(Long idNotaBitacora) {
    this.idNotaBitacora = idNotaBitacora;
  }

  public Long getIdNotaBitacora() {
    return idNotaBitacora;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdNotaEntrada(Long idNotaEntrada) {
    this.idNotaEntrada = idNotaEntrada;
  }

  public Long getIdNotaEntrada() {
    return idNotaEntrada;
  }

  public void setIdNotaEstatus(Long idNotaEstatus) {
    this.idNotaEstatus = idNotaEstatus;
  }

  public Long getIdNotaEstatus() {
    return idNotaEstatus;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo=consecutivo;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe=importe;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdNotaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idNotaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdNotaBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idNotaBitacora", getIdNotaBitacora());
		regresar.put("justificacion", getJustificacion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("idNotaEstatus", getIdNotaEstatus());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("importe", getImporte());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdNotaBitacora(), getJustificacion(), getIdUsuario(), getIdNotaEntrada(), getIdNotaEstatus(), getRegistro(), getConsecutivo(), getImporte()
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
    regresar.append("idNotaBitacora~");
    regresar.append(getIdNotaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdNotaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticNotasBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdNotaBitacora()!= null && getIdNotaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticNotasBitacoraDto other = (TcManticNotasBitacoraDto) obj;
    if (getIdNotaBitacora() != other.idNotaBitacora && (getIdNotaBitacora() == null || !getIdNotaBitacora().equals(other.idNotaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdNotaBitacora() != null ? getIdNotaBitacora().hashCode() : 0);
    return hash;
  }

}


