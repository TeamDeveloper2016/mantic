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
@Table(name="tr_mantic_almacen_tipo_contacto")
public class TrManticAlmacenTipoContactoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="valor")
  private String valor;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="orden")
  private Long orden;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_almacen_tipo_contacto")
  private Long idAlmacenTipoContacto;
  @Column (name="id_tipo_contacto")
  private Long idTipoContacto;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticAlmacenTipoContactoDto() {
    this(new Long(-1L));
  }

  public TrManticAlmacenTipoContactoDto(Long key) {
    this(null, null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TrManticAlmacenTipoContactoDto(Long idUsuario, Long idAlmacen, String valor, String observaciones, Long orden, Long idAlmacenTipoContacto, Long idTipoContacto) {
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setValor(valor);
    setObservaciones(observaciones);
    setOrden(orden);
    setIdAlmacenTipoContacto(idAlmacenTipoContacto);
    setIdTipoContacto(idTipoContacto);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public String getValor() {
    return valor;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdAlmacenTipoContacto(Long idAlmacenTipoContacto) {
    this.idAlmacenTipoContacto = idAlmacenTipoContacto;
  }

  public Long getIdAlmacenTipoContacto() {
    return idAlmacenTipoContacto;
  }

  public void setIdTipoContacto(Long idTipoContacto) {
    this.idTipoContacto = idTipoContacto;
  }

  public Long getIdTipoContacto() {
    return idTipoContacto;
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
  	return getIdAlmacenTipoContacto();
  }

  @Override
  public void setKey(Long key) {
  	this.idAlmacenTipoContacto = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getValor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacenTipoContacto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoContacto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("valor", getValor());
		regresar.put("observaciones", getObservaciones());
		regresar.put("orden", getOrden());
		regresar.put("idAlmacenTipoContacto", getIdAlmacenTipoContacto());
		regresar.put("idTipoContacto", getIdTipoContacto());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdAlmacen(), getValor(), getObservaciones(), getOrden(), getIdAlmacenTipoContacto(), getIdTipoContacto(), getRegistro()
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
    regresar.append("idAlmacenTipoContacto~");
    regresar.append(getIdAlmacenTipoContacto());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdAlmacenTipoContacto());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticAlmacenTipoContactoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdAlmacenTipoContacto()!= null && getIdAlmacenTipoContacto()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticAlmacenTipoContactoDto other = (TrManticAlmacenTipoContactoDto) obj;
    if (getIdAlmacenTipoContacto() != other.idAlmacenTipoContacto && (getIdAlmacenTipoContacto() == null || !getIdAlmacenTipoContacto().equals(other.idAlmacenTipoContacto))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdAlmacenTipoContacto() != null ? getIdAlmacenTipoContacto().hashCode() : 0);
    return hash;
  }

}


