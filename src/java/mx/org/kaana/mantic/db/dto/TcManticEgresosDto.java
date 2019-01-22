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
@Table(name="tc_mantic_egresos")
public class TcManticEgresosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="fecha")
  private Date fecha;
  @Column (name="id_egreso_estatus")
  private Long idEgresoEstatus;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_egreso")
  private Long idEgreso;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="orden")
  private Long orden;
  @Column (name="importe")
  private Double importe;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticEgresosDto() {
    this(new Long(-1L));
  }

  public TcManticEgresosDto(Long key) {
    this(null, null, new Date(Calendar.getInstance().getTimeInMillis()), null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticEgresosDto(String consecutivo, String descripcion, Date fecha, Long idEgresoEstatus, Long idEgreso, Long idUsuario, Long idEmpresa, Long orden, Double importe, Long ejercicio) {
    setConsecutivo(consecutivo);
    setDescripcion(descripcion);
    setFecha(fecha);
    setIdEgresoEstatus(idEgresoEstatus);
    setIdEgreso(idEgreso);
    setIdUsuario(idUsuario);
    setIdEmpresa(idEmpresa);
    setOrden(orden);
    setImporte(importe);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setIdEgresoEstatus(Long idEgresoEstatus) {
    this.idEgresoEstatus = idEgresoEstatus;
  }

  public Long getIdEgresoEstatus() {
    return idEgresoEstatus;
  }

  public void setIdEgreso(Long idEgreso) {
    this.idEgreso = idEgreso;
  }

  public Long getIdEgreso() {
    return idEgreso;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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

  @Transient
  @Override
  public Long getKey() {
  	return getIdEgreso();
  }

  @Override
  public void setKey(Long key) {
  	this.idEgreso = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEgresoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEgreso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("descripcion", getDescripcion());
		regresar.put("fecha", getFecha());
		regresar.put("idEgresoEstatus", getIdEgresoEstatus());
		regresar.put("idEgreso", getIdEgreso());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("orden", getOrden());
		regresar.put("importe", getImporte());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getDescripcion(), getFecha(), getIdEgresoEstatus(), getIdEgreso(), getIdUsuario(), getIdEmpresa(), getOrden(), getImporte(), getEjercicio(), getRegistro()
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
    regresar.append("idEgreso~");
    regresar.append(getIdEgreso());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEgreso());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEgresosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEgreso()!= null && getIdEgreso()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEgresosDto other = (TcManticEgresosDto) obj;
    if (getIdEgreso() != other.idEgreso && (getIdEgreso() == null || !getIdEgreso().equals(other.idEgreso))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEgreso() != null ? getIdEgreso().hashCode() : 0);
    return hash;
  }

}


