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
@Table(name="tc_mantic_historial_iva")
public class TcManticHistorialIvaDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_historial_iva")
  private Long idHistorialIva;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticHistorialIvaDto() {
    this(new Long(-1L));
  }

  public TcManticHistorialIvaDto(Long key) {
    this(null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticHistorialIvaDto(Long idUsuario, Long idHistorialIva, String observaciones, Long idEmpresa, Double importe) {
    setIdUsuario(idUsuario);
    setIdHistorialIva(idHistorialIva);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdHistorialIva(Long idHistorialIva) {
    this.idHistorialIva = idHistorialIva;
  }

  public Long getIdHistorialIva() {
    return idHistorialIva;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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
  	return getIdHistorialIva();
  }

  @Override
  public void setKey(Long key) {
  	this.idHistorialIva = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdHistorialIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idHistorialIva", getIdHistorialIva());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdHistorialIva(), getObservaciones(), getIdEmpresa(), getImporte(), getRegistro()
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
    regresar.append("idHistorialIva~");
    regresar.append(getIdHistorialIva());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdHistorialIva());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticHistorialIvaDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdHistorialIva()!= null && getIdHistorialIva()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticHistorialIvaDto other = (TcManticHistorialIvaDto) obj;
    if (getIdHistorialIva() != other.idHistorialIva && (getIdHistorialIva() == null || !getIdHistorialIva().equals(other.idHistorialIva))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdHistorialIva() != null ? getIdHistorialIva().hashCode() : 0);
    return hash;
  }

}


