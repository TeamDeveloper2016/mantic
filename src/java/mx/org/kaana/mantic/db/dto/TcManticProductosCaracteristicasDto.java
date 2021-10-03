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
@Table(name="tc_mantic_productos_caracteristicas")
public class TcManticProductosCaracteristicasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_producto")
  private Long idProducto;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_producto_caracteristica")
  private Long idProductoCaracteristica;
  @Column (name="orden")
  private Long orden;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticProductosCaracteristicasDto() {
    this(new Long(-1L));
  }

  public TcManticProductosCaracteristicasDto(Long key) {
    this(null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticProductosCaracteristicasDto(String descripcion, Long idUsuario, Long idProducto, Long idProductoCaracteristica, Long orden) {
    setDescripcion(descripcion);
    setIdUsuario(idUsuario);
    setIdProducto(idProducto);
    setIdProductoCaracteristica(idProductoCaracteristica);
    setOrden(orden);
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

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProductoCaracteristica(Long idProductoCaracteristica) {
    this.idProductoCaracteristica = idProductoCaracteristica;
  }

  public Long getIdProductoCaracteristica() {
    return idProductoCaracteristica;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
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
  	return getIdProductoCaracteristica();
  }

  @Override
  public void setKey(Long key) {
  	this.idProductoCaracteristica = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProducto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProductoCaracteristica());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
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
		regresar.put("idProducto", getIdProducto());
		regresar.put("idProductoCaracteristica", getIdProductoCaracteristica());
		regresar.put("orden", getOrden());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getIdUsuario(), getIdProducto(), getIdProductoCaracteristica(), getOrden(), getRegistro()
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
    regresar.append("idProductoCaracteristica~");
    regresar.append(getIdProductoCaracteristica());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProductoCaracteristica());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticProductosCaracteristicasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProductoCaracteristica()!= null && getIdProductoCaracteristica()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticProductosCaracteristicasDto other = (TcManticProductosCaracteristicasDto) obj;
    if (getIdProductoCaracteristica() != other.idProductoCaracteristica && (getIdProductoCaracteristica() == null || !getIdProductoCaracteristica().equals(other.idProductoCaracteristica))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProductoCaracteristica() != null ? getIdProductoCaracteristica().hashCode() : 0);
    return hash;
  }

}


