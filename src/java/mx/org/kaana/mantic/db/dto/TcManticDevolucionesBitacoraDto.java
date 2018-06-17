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
@Table(name="tc_mantic_devoluciones_bitacora")
public class TcManticDevolucionesBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_devolucion_estatus")
  private Long idDevolucionEstatus;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_devolucion")
  private Long idDevolucion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_devolucion_bitacora")
  private Long idDevolucionBitacora;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="importe")
  private Double importe;

  public TcManticDevolucionesBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticDevolucionesBitacoraDto(Long key) {
    this(null, null, null, null, new Long(-1L), "", 0D);
    setKey(key);
  }

  public TcManticDevolucionesBitacoraDto(Long idDevolucionEstatus, String justificacion, Long idDevolucion, Long idUsuario, Long idDevolucionBitacora, String consecutivo, Double importe) {
    setIdDevolucionEstatus(idDevolucionEstatus);
    setJustificacion(justificacion);
    setIdDevolucion(idDevolucion);
    setIdUsuario(idUsuario);
    setIdDevolucionBitacora(idDevolucionBitacora);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setImporte(importe);
  }
	
  public void setIdDevolucionEstatus(Long idDevolucionEstatus) {
    this.idDevolucionEstatus = idDevolucionEstatus;
  }

  public Long getIdDevolucionEstatus() {
    return idDevolucionEstatus;
  }

  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdDevolucion(Long idDevolucion) {
    this.idDevolucion = idDevolucion;
  }

  public Long getIdDevolucion() {
    return idDevolucion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdDevolucionBitacora(Long idDevolucionBitacora) {
    this.idDevolucionBitacora = idDevolucionBitacora;
  }

  public Long getIdDevolucionBitacora() {
    return idDevolucionBitacora;
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
  	return getIdDevolucionBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idDevolucionBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdDevolucionEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDevolucion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDevolucionBitacora());
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
		regresar.put("idDevolucionEstatus", getIdDevolucionEstatus());
		regresar.put("justificacion", getJustificacion());
		regresar.put("idDevolucion", getIdDevolucion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idDevolucionBitacora", getIdDevolucionBitacora());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("importe", getImporte());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdDevolucionEstatus(), getJustificacion(), getIdDevolucion(), getIdUsuario(), getIdDevolucionBitacora(), getRegistro(), getConsecutivo(), getImporte()
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
    regresar.append("idDevolucionBitacora~");
    regresar.append(getIdDevolucionBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDevolucionBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDevolucionesBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDevolucionBitacora()!= null && getIdDevolucionBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDevolucionesBitacoraDto other = (TcManticDevolucionesBitacoraDto) obj;
    if (getIdDevolucionBitacora() != other.idDevolucionBitacora && (getIdDevolucionBitacora() == null || !getIdDevolucionBitacora().equals(other.idDevolucionBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDevolucionBitacora() != null ? getIdDevolucionBitacora().hashCode() : 0);
    return hash;
  }

}


