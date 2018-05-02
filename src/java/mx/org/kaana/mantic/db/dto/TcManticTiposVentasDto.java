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
@Table(name="tc_mantic_tipos_ventas")
public class TcManticTiposVentasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="porcentaje_utilidad")
  private Long porcentajeUtilidad;
  @Column (name="nombre")
  private String nombre;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_venta")
  private Long idTipoVenta;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposVentasDto() {
    this(new Long(-1L));
  }

  public TcManticTiposVentasDto(Long key) {
    this(null, null, null, null, new Long(-1L));
    setKey(key);
  }

  public TcManticTiposVentasDto(String descripcion, Long idUsuario, Long porcentajeUtilidad, String nombre, Long idTipoVenta) {
    setDescripcion(descripcion);
    setIdUsuario(idUsuario);
    setPorcentajeUtilidad(porcentajeUtilidad);
    setNombre(nombre);
    setIdTipoVenta(idTipoVenta);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setPorcentajeUtilidad(Long porcentajeUtilidad) {
    this.porcentajeUtilidad = porcentajeUtilidad;
  }

  public Long getPorcentajeUtilidad() {
    return porcentajeUtilidad;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setIdTipoVenta(Long idTipoVenta) {
    this.idTipoVenta = idTipoVenta;
  }

  public Long getIdTipoVenta() {
    return idTipoVenta;
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
  	return getIdTipoVenta();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoVenta = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPorcentajeUtilidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("porcentajeUtilidad", getPorcentajeUtilidad());
		regresar.put("nombre", getNombre());
		regresar.put("idTipoVenta", getIdTipoVenta());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getIdUsuario(), getPorcentajeUtilidad(), getNombre(), getIdTipoVenta(), getRegistro()
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
    regresar.append("idTipoVenta~");
    regresar.append(getIdTipoVenta());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoVenta());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposVentasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoVenta()!= null && getIdTipoVenta()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposVentasDto other = (TcManticTiposVentasDto) obj;
    if (getIdTipoVenta() != other.idTipoVenta && (getIdTipoVenta() == null || !getIdTipoVenta().equals(other.idTipoVenta))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoVenta() != null ? getIdTipoVenta().hashCode() : 0);
    return hash;
  }

}


