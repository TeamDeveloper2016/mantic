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
@Table(name="tc_mantic_servicios_bitacora")
public class TcManticServiciosBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="seguimiento")
  private String seguimiento;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_servicio_bitacora")
  private Long idServicioBitacora;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_servicio_estatus")
  private Long idServicioEstatus;
  @Column (name="id_servicio")
  private Long idServicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="importe")
  private Double importe;

  public TcManticServiciosBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticServiciosBitacoraDto(Long key) {
    this(null, new Long(-1L), null, null, null, "", 0D);
    setKey(key);
  }

  public TcManticServiciosBitacoraDto(String seguimiento, Long idServicioBitacora, Long idUsuario, Long idServicioEstatus, Long idServicio, String consecutivo, Double importe) {
    setSeguimiento(seguimiento);
    setIdServicioBitacora(idServicioBitacora);
    setIdUsuario(idUsuario);
    setIdServicioEstatus(idServicioEstatus);
    setIdServicio(idServicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setImporte(importe);
  }
	
  public void setSeguimiento(String seguimiento) {
    this.seguimiento = seguimiento;
  }

  public String getSeguimiento() {
    return seguimiento;
  }

  public void setIdServicioBitacora(Long idServicioBitacora) {
    this.idServicioBitacora = idServicioBitacora;
  }

  public Long getIdServicioBitacora() {
    return idServicioBitacora;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdServicioEstatus(Long idServicioEstatus) {
    this.idServicioEstatus = idServicioEstatus;
  }

  public Long getIdServicioEstatus() {
    return idServicioEstatus;
  }

  public void setIdServicio(Long idServicio) {
    this.idServicio = idServicio;
  }

  public Long getIdServicio() {
    return idServicio;
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
  	return getIdServicioBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idServicioBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getSeguimiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdServicioBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdServicioEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdServicio());
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
		regresar.put("seguimiento", getSeguimiento());
		regresar.put("idServicioBitacora", getIdServicioBitacora());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idServicioEstatus", getIdServicioEstatus());
		regresar.put("idServicio", getIdServicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("importe", getImporte());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getSeguimiento(), getIdServicioBitacora(), getIdUsuario(), getIdServicioEstatus(), getIdServicio(), getRegistro(), getConsecutivo(), getImporte()
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
    regresar.append("idServicioBitacora~");
    regresar.append(getIdServicioBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdServicioBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticServiciosBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdServicioBitacora()!= null && getIdServicioBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticServiciosBitacoraDto other = (TcManticServiciosBitacoraDto) obj;
    if (getIdServicioBitacora() != other.idServicioBitacora && (getIdServicioBitacora() == null || !getIdServicioBitacora().equals(other.idServicioBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdServicioBitacora() != null ? getIdServicioBitacora().hashCode() : 0);
    return hash;
  }

}


