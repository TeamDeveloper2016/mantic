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
@Table(name="tc_mantic_productos_detalles")
public class TcManticProductosDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="id_datos")
  private Long idDatos;
  @Column (name="medida")
  private String medida;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_producto_detalle")
  private Long idProductoDetalle;
  @Column (name="id_producto")
  private Long idProducto;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="especificacion")
  private String especificacion;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="id_articulo_codigo")
  private Long idArticuloCodigo;

  public TcManticProductosDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticProductosDetallesDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null, null, -1L);
    setKey(key);
  }

  public TcManticProductosDetallesDto(String descripcion, Long idDatos, String medida, Long idUsuario, Long idProductoDetalle, Long idProducto, Long orden, Long idArticulo, String especificacion, Long idArticuloCodigo) {
    setDescripcion(descripcion);
    setIdDatos(idDatos);
    setMedida(medida);
    setIdUsuario(idUsuario);
    setIdProductoDetalle(idProductoDetalle);
    setIdProducto(idProducto);
    setOrden(orden);
    setIdArticulo(idArticulo);
    setEspecificacion(especificacion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    this.idArticuloCodigo= idArticuloCodigo;
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdDatos(Long idDatos) {
    this.idDatos = idDatos;
  }

  public Long getIdDatos() {
    return idDatos;
  }

  public void setMedida(String medida) {
    this.medida = medida;
  }

  public String getMedida() {
    return medida;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdProductoDetalle(Long idProductoDetalle) {
    this.idProductoDetalle = idProductoDetalle;
  }

  public Long getIdProductoDetalle() {
    return idProductoDetalle;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setEspecificacion(String especificacion) {
    this.especificacion = especificacion;
  }

  public String getEspecificacion() {
    return especificacion;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public Long getIdArticuloCodigo() {
    return idArticuloCodigo;
  }

  public void setIdArticuloCodigo(Long idArticuloCodigo) {
    this.idArticuloCodigo = idArticuloCodigo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdProductoDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idProductoDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDatos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProductoDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProducto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEspecificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("idDatos", getIdDatos());
		regresar.put("medida", getMedida());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idProductoDetalle", getIdProductoDetalle());
		regresar.put("idProducto", getIdProducto());
		regresar.put("orden", getOrden());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("especificacion", getEspecificacion());
		regresar.put("idArticuloCodigo", getIdArticuloCodigo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getDescripcion(), getIdDatos(), getMedida(), getIdUsuario(), getIdProductoDetalle(), getIdProducto(), getOrden(), getIdArticulo(), getEspecificacion(), getIdArticuloCodigo(), getRegistro()
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
    regresar.append("idProductoDetalle~");
    regresar.append(getIdProductoDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProductoDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticProductosDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProductoDetalle()!= null && getIdProductoDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticProductosDetallesDto other = (TcManticProductosDetallesDto) obj;
    if (getIdProductoDetalle() != other.idProductoDetalle && (getIdProductoDetalle() == null || !getIdProductoDetalle().equals(other.idProductoDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProductoDetalle() != null ? getIdProductoDetalle().hashCode() : 0);
    return hash;
  }

}


