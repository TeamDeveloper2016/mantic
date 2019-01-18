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
@Table(name="tc_mantic_egresos_bitacora")
public class TcManticEgresosBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_egreso_estatus")
  private Long idEgresoEstatus;
  @Column (name="id_egreso")
  private Long idEgreso;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_egreso_bitacora")
  private Long idEgresoBitacora;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticEgresosBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticEgresosBitacoraDto(Long key) {
    this(null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticEgresosBitacoraDto(String justificacion, Long idEgresoEstatus, Long idEgreso, Long idUsuario, Long idEgresoBitacora) {
    setJustificacion(justificacion);
    setIdEgresoEstatus(idEgresoEstatus);
    setIdEgreso(idEgreso);
    setIdUsuario(idUsuario);
    setIdEgresoBitacora(idEgresoBitacora);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
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

  public void setIdEgresoBitacora(Long idEgresoBitacora) {
    this.idEgresoBitacora = idEgresoBitacora;
  }

  public Long getIdEgresoBitacora() {
    return idEgresoBitacora;
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
  	return getIdEgresoBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idEgresoBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEgresoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEgreso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEgresoBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idEgresoEstatus", getIdEgresoEstatus());
		regresar.put("idEgreso", getIdEgreso());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEgresoBitacora", getIdEgresoBitacora());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdEgresoEstatus(), getIdEgreso(), getIdUsuario(), getIdEgresoBitacora(), getRegistro()
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
    regresar.append("idEgresoBitacora~");
    regresar.append(getIdEgresoBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEgresoBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEgresosBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEgresoBitacora()!= null && getIdEgresoBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEgresosBitacoraDto other = (TcManticEgresosBitacoraDto) obj;
    if (getIdEgresoBitacora() != other.idEgresoBitacora && (getIdEgresoBitacora() == null || !getIdEgresoBitacora().equals(other.idEgresoBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEgresoBitacora() != null ? getIdEgresoBitacora().hashCode() : 0);
    return hash;
  }

}


