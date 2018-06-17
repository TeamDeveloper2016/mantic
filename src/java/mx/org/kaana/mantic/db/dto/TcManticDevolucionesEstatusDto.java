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
@Table(name="tc_mantic_devoluciones_estatus")
public class TcManticDevolucionesEstatusDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_devolucion_estatus")
  private Long idDevolucionEstatus;
  @Column (name="id_justificacion")
  private Long idJustificacion;
  @Column (name="estatus_asociados")
  private String estatusAsociados;
  @Column (name="decripcion")
  private String decripcion;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticDevolucionesEstatusDto() {
    this(new Long(-1L));
  }

  public TcManticDevolucionesEstatusDto(Long key) {
    this(new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticDevolucionesEstatusDto(Long idDevolucionEstatus, Long idJustificacion, String estatusAsociados, String decripcion, String nombre) {
    setIdDevolucionEstatus(idDevolucionEstatus);
    setIdJustificacion(idJustificacion);
    setEstatusAsociados(estatusAsociados);
    setDecripcion(decripcion);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdDevolucionEstatus(Long idDevolucionEstatus) {
    this.idDevolucionEstatus = idDevolucionEstatus;
  }

  public Long getIdDevolucionEstatus() {
    return idDevolucionEstatus;
  }

  public void setIdJustificacion(Long idJustificacion) {
    this.idJustificacion = idJustificacion;
  }

  public Long getIdJustificacion() {
    return idJustificacion;
  }

  public void setEstatusAsociados(String estatusAsociados) {
    this.estatusAsociados = estatusAsociados;
  }

  public String getEstatusAsociados() {
    return estatusAsociados;
  }

  public void setDecripcion(String decripcion) {
    this.decripcion = decripcion;
  }

  public String getDecripcion() {
    return decripcion;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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
  	return getIdDevolucionEstatus();
  }

  @Override
  public void setKey(Long key) {
  	this.idDevolucionEstatus = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdDevolucionEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstatusAsociados());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDecripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idDevolucionEstatus", getIdDevolucionEstatus());
		regresar.put("idJustificacion", getIdJustificacion());
		regresar.put("estatusAsociados", getEstatusAsociados());
		regresar.put("decripcion", getDecripcion());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdDevolucionEstatus(), getIdJustificacion(), getEstatusAsociados(), getDecripcion(), getNombre(), getRegistro()
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
    regresar.append("idDevolucionEstatus~");
    regresar.append(getIdDevolucionEstatus());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDevolucionEstatus());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDevolucionesEstatusDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDevolucionEstatus()!= null && getIdDevolucionEstatus()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDevolucionesEstatusDto other = (TcManticDevolucionesEstatusDto) obj;
    if (getIdDevolucionEstatus() != other.idDevolucionEstatus && (getIdDevolucionEstatus() == null || !getIdDevolucionEstatus().equals(other.idDevolucionEstatus))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDevolucionEstatus() != null ? getIdDevolucionEstatus().hashCode() : 0);
    return hash;
  }

}


