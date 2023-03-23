package mx.org.kaana.kalan.db.dto;

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
@Table(name="tc_kalan_citas_detalles")
public class TcKalanCitasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cita_detalle")
  private Long idCitaDetalle;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_cita")
  private Long idCita;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="registro")
  private Timestamp registro;

  public TcKalanCitasDetallesDto() {
    this(new Long(-1L));
  }

  public TcKalanCitasDetallesDto(Long key) {
    this(new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcKalanCitasDetallesDto(Long idCitaDetalle, Long idUsuario, Long idCita, Long idArticulo) {
    setIdCitaDetalle(idCitaDetalle);
    setIdUsuario(idUsuario);
    setIdCita(idCita);
    setIdArticulo(idArticulo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdCitaDetalle(Long idCitaDetalle) {
    this.idCitaDetalle = idCitaDetalle;
  }

  public Long getIdCitaDetalle() {
    return idCitaDetalle;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdCita(Long idCita) {
    this.idCita = idCita;
  }

  public Long getIdCita() {
    return idCita;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
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
  	return getIdCitaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idCitaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdCitaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCita());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idCitaDetalle", getIdCitaDetalle());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idCita", getIdCita());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdCitaDetalle(), getIdUsuario(), getIdCita(), getIdArticulo(), getRegistro()
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
    regresar.append("idCitaDetalle~");
    regresar.append(getIdCitaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCitaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcKalanCitasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCitaDetalle()!= null && getIdCitaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcKalanCitasDetallesDto other = (TcKalanCitasDetallesDto) obj;
    if (getIdCitaDetalle() != other.idCitaDetalle && (getIdCitaDetalle() == null || !getIdCitaDetalle().equals(other.idCitaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCitaDetalle() != null ? getIdCitaDetalle().hashCode() : 0);
    return hash;
  }

}


