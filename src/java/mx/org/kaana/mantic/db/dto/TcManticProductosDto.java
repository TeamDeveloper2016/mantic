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
@Table(name="tc_mantic_productos")
public class TcManticProductosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_activo")
  private Long idActivo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_imagen")
  private Long idImagen;
  @Column (name="id_producto_categoria")
  private Long idProductoCategoria;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_producto")
  private Long idProducto;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="orden")
  private Long orden;
  @Column (name="nombre")
  private String nombre;
  @Column (name="id_producto_marca")
  private Long idProductoMarca;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticProductosDto() {
    this(new Long(-1L));
  }

  public TcManticProductosDto(Long key) {
    this(null, null, null, null, null, new Long(-1L), null, null, null, 2L);
    setKey(key);
  }

  public TcManticProductosDto(Long idActivo, Long idUsuario, Long idImagen, Long idProductoCategoria, String observaciones, Long idProducto, Long idEmpresa, Long orden, String nombre, Long idProductoMarca) {
    setIdActivo(idActivo);
    setIdUsuario(idUsuario);
    setIdImagen(idImagen);
    setIdProductoCategoria(idProductoCategoria);
    setObservaciones(observaciones);
    setIdProducto(idProducto);
    setIdEmpresa(idEmpresa);
    setOrden(orden);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    this.idProductoMarca= idProductoMarca;
  }
	
  public void setIdActivo(Long idActivo) {
    this.idActivo = idActivo;
  }

  public Long getIdActivo() {
    return idActivo;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdImagen(Long idImagen) {
    this.idImagen = idImagen;
  }

  public Long getIdImagen() {
    return idImagen;
  }

  public void setIdProductoCategoria(Long idProductoCategoria) {
    this.idProductoCategoria = idProductoCategoria;
  }

  public Long getIdProductoCategoria() {
    return idProductoCategoria;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
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

  public Long getIdProductoMarca() {
    return idProductoMarca;
  }

  public void setIdProductoMarca(Long idProductoMarca) {
    this.idProductoMarca = idProductoMarca;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdProducto();
  }

  @Override
  public void setKey(Long key) {
  	this.idProducto = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdActivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdImagen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProductoCategoria());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProducto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProductoMarca());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idActivo", getIdActivo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idImagen", getIdImagen());
		regresar.put("idProductoCategoria", getIdProductoCategoria());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idProducto", getIdProducto());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("orden", getOrden());
		regresar.put("nombre", getNombre());
		regresar.put("idProductoMarca", getIdProductoMarca());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdActivo(), getIdUsuario(), getIdImagen(), getIdProductoCategoria(), getObservaciones(), getIdProducto(), getIdEmpresa(), getOrden(), getNombre(), getRegistro(), getIdProductoMarca()    
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
    regresar.append("idProducto~");
    regresar.append(getIdProducto());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProducto());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticProductosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProducto()!= null && getIdProducto()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticProductosDto other = (TcManticProductosDto) obj;
    if (getIdProducto() != other.idProducto && (getIdProducto() == null || !getIdProducto().equals(other.idProducto))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProducto() != null ? getIdProducto().hashCode() : 0);
    return hash;
  }

}


