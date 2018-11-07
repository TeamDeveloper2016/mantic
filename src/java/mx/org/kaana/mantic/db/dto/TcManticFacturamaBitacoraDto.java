package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
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
@Table(name="tc_mantic_facturama_bitacora")
public class TcManticFacturamaBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_key")
  private Long idKey;
  @Column (name="codigo")
  private String codigo;
  @Column (name="proceso")
  private String proceso;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_facturama_bitacora")
  private Long idFacturamaBitacora;
  @Column (name="observacion")
  private String observacion;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticFacturamaBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticFacturamaBitacoraDto(Long key) {
    this(null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticFacturamaBitacoraDto(Long idKey, String codigo, String proceso, Long idFacturamaBitacora, String observacion) {
    setIdKey(idKey);
    setCodigo(codigo);
    setProceso(proceso);
    setIdFacturamaBitacora(idFacturamaBitacora);
    setObservacion(observacion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdKey(Long idKey) {
    this.idKey = idKey;
  }

  public Long getIdKey() {
    return idKey;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setProceso(String proceso) {
    this.proceso = proceso;
  }

  public String getProceso() {
    return proceso;
  }

  public void setIdFacturamaBitacora(Long idFacturamaBitacora) {
    this.idFacturamaBitacora = idFacturamaBitacora;
  }

  public Long getIdFacturamaBitacora() {
    return idFacturamaBitacora;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }

  public String getObservacion() {
    return observacion;
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
  	return getIdFacturamaBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idFacturamaBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdKey());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProceso());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturamaBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idKey", getIdKey());
		regresar.put("codigo", getCodigo());
		regresar.put("proceso", getProceso());
		regresar.put("idFacturamaBitacora", getIdFacturamaBitacora());
		regresar.put("observacion", getObservacion());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdKey(), getCodigo(), getProceso(), getIdFacturamaBitacora(), getObservacion(), getRegistro()
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
    regresar.append("idFacturamaBitacora~");
    regresar.append(getIdFacturamaBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFacturamaBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFacturamaBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFacturamaBitacora()!= null && getIdFacturamaBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFacturamaBitacoraDto other = (TcManticFacturamaBitacoraDto) obj;
    if (getIdFacturamaBitacora() != other.idFacturamaBitacora && (getIdFacturamaBitacora() == null || !getIdFacturamaBitacora().equals(other.idFacturamaBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFacturamaBitacora() != null ? getIdFacturamaBitacora().hashCode() : 0);
    return hash;
  }

}


