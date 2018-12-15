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
@Table(name="tc_mantic_masivas_detalles")
public class TcManticMasivasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="codigo")
  private String codigo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_masiva_detalle")
  private Long idMasivaDetalle;
  @Column (name="id_masiva_archivo")
  private Long idMasivaArchivo;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticMasivasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticMasivasDetallesDto(Long key) {
    this(null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticMasivasDetallesDto(String codigo, Long idMasivaDetalle, Long idMasivaArchivo, String observaciones) {
    setCodigo(codigo);
    setIdMasivaDetalle(idMasivaDetalle);
    setIdMasivaArchivo(idMasivaArchivo);
    setObservaciones(observaciones);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setIdMasivaDetalle(Long idMasivaDetalle) {
    this.idMasivaDetalle = idMasivaDetalle;
  }

  public Long getIdMasivaDetalle() {
    return idMasivaDetalle;
  }

  public void setIdMasivaArchivo(Long idMasivaArchivo) {
    this.idMasivaArchivo = idMasivaArchivo;
  }

  public Long getIdMasivaArchivo() {
    return idMasivaArchivo;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
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
  	return getIdMasivaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idMasivaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMasivaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMasivaArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("idMasivaDetalle", getIdMasivaDetalle());
		regresar.put("idMasivaArchivo", getIdMasivaArchivo());
		regresar.put("observaciones", getObservaciones());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCodigo(), getIdMasivaDetalle(), getIdMasivaArchivo(), getObservaciones(), getRegistro()
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
    regresar.append("idMasivaDetalle~");
    regresar.append(getIdMasivaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMasivaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticMasivasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMasivaDetalle()!= null && getIdMasivaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticMasivasDetallesDto other = (TcManticMasivasDetallesDto) obj;
    if (getIdMasivaDetalle() != other.idMasivaDetalle && (getIdMasivaDetalle() == null || !getIdMasivaDetalle().equals(other.idMasivaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMasivaDetalle() != null ? getIdMasivaDetalle().hashCode() : 0);
    return hash;
  }

}


