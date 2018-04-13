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
@Table(name="tr_mantic_empaque_unidad_medida")
public class TrManticEmpaqueUnidadMedidaDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_unidad_medida")
  private Long idUnidadMedida;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_empaque_unidad_medida")
  private Long idEmpaqueUnidadMedida;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empaque")
  private Long idEmpaque;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticEmpaqueUnidadMedidaDto() {
    this(new Long(-1L));
  }

  public TrManticEmpaqueUnidadMedidaDto(Long key) {
    this(null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TrManticEmpaqueUnidadMedidaDto(Long idUnidadMedida, Long idUsuario, Long idEmpaqueUnidadMedida, String observaciones, Long idEmpaque) {
    setIdUnidadMedida(idUnidadMedida);
    setIdUsuario(idUsuario);
    setIdEmpaqueUnidadMedida(idEmpaqueUnidadMedida);
    setObservaciones(observaciones);
    setIdEmpaque(idEmpaque);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUnidadMedida(Long idUnidadMedida) {
    this.idUnidadMedida = idUnidadMedida;
  }

  public Long getIdUnidadMedida() {
    return idUnidadMedida;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdEmpaqueUnidadMedida(Long idEmpaqueUnidadMedida) {
    this.idEmpaqueUnidadMedida = idEmpaqueUnidadMedida;
  }

  public Long getIdEmpaqueUnidadMedida() {
    return idEmpaqueUnidadMedida;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdEmpaque(Long idEmpaque) {
    this.idEmpaque = idEmpaque;
  }

  public Long getIdEmpaque() {
    return idEmpaque;
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
  	return getIdEmpaqueUnidadMedida();
  }

  @Override
  public void setKey(Long key) {
  	this.idEmpaqueUnidadMedida = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpaqueUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpaque());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUnidadMedida", getIdUnidadMedida());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEmpaqueUnidadMedida", getIdEmpaqueUnidadMedida());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpaque", getIdEmpaque());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUnidadMedida(), getIdUsuario(), getIdEmpaqueUnidadMedida(), getObservaciones(), getIdEmpaque(), getRegistro()
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
    regresar.append("idEmpaqueUnidadMedida~");
    regresar.append(getIdEmpaqueUnidadMedida());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEmpaqueUnidadMedida());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticEmpaqueUnidadMedidaDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEmpaqueUnidadMedida()!= null && getIdEmpaqueUnidadMedida()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticEmpaqueUnidadMedidaDto other = (TrManticEmpaqueUnidadMedidaDto) obj;
    if (getIdEmpaqueUnidadMedida() != other.idEmpaqueUnidadMedida && (getIdEmpaqueUnidadMedida() == null || !getIdEmpaqueUnidadMedida().equals(other.idEmpaqueUnidadMedida))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEmpaqueUnidadMedida() != null ? getIdEmpaqueUnidadMedida().hashCode() : 0);
    return hash;
  }

}


