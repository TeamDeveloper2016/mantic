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
@Table(name="tc_mantic_productos_categorias")
public class TcManticProductosCategoriasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="padre")
  private String padre;
  @Column (name="ultimo")
  private Long ultimo;
  @Column (name="id_activo")
  private Long idActivo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_producto_categoria")
  private Long idProductoCategoria;
  @Column (name="porcentaje")
  private Long porcentaje;
  @Column (name="nombre")
  private String nombre;
  @Column (name="nivel")
  private Long nivel;
  @Column (name="orden")
  private Long orden;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticProductosCategoriasDto() {
    this(new Long(-1L));
  }

  public TcManticProductosCategoriasDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null, 1L);
    setKey(key);
  }

  public TcManticProductosCategoriasDto(String padre, Long ultimo, Long idActivo, Long idUsuario, Long idProductoCategoria, Long porcentaje, String nombre, Long nivel, Long orden) {
    setPadre(padre);
    setUltimo(ultimo);
    setIdActivo(idActivo);
    setIdUsuario(idUsuario);
    setIdProductoCategoria(idProductoCategoria);
    setPorcentaje(porcentaje);
    setNombre(nombre);
    setNivel(nivel);
    setOrden(orden);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setPadre(String padre) {
    this.padre = padre;
  }

  public String getPadre() {
    return padre;
  }

  public void setUltimo(Long ultimo) {
    this.ultimo = ultimo;
  }

  public Long getUltimo() {
    return ultimo;
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

  public void setIdProductoCategoria(Long idProductoCategoria) {
    this.idProductoCategoria = idProductoCategoria;
  }

  public Long getIdProductoCategoria() {
    return idProductoCategoria;
  }

  public void setPorcentaje(Long porcentaje) {
    this.porcentaje = porcentaje;
  }

  public Long getPorcentaje() {
    return porcentaje;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNivel(Long nivel) {
    this.nivel = nivel;
  }

  public Long getNivel() {
    return nivel;
  }

  public Long getOrden() {
    return orden;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
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
  	return getIdProductoCategoria();
  }

  @Override
  public void setKey(Long key) {
  	this.idProductoCategoria = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getPadre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUltimo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdActivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProductoCategoria());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPorcentaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNivel());
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
		regresar.put("padre", getPadre());
		regresar.put("ultimo", getUltimo());
		regresar.put("idActivo", getIdActivo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idProductoCategoria", getIdProductoCategoria());
		regresar.put("porcentaje", getPorcentaje());
		regresar.put("nombre", getNombre());
		regresar.put("nivel", getNivel());
		regresar.put("orden", getOrden());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getPadre(), getUltimo(), getIdActivo(), getIdUsuario(), getIdProductoCategoria(), getPorcentaje(), getNombre(), getNivel(), getOrden(), getRegistro()
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
    regresar.append("idProductoCategoria~");
    regresar.append(getIdProductoCategoria());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProductoCategoria());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticProductosCategoriasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProductoCategoria()!= null && getIdProductoCategoria()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticProductosCategoriasDto other = (TcManticProductosCategoriasDto) obj;
    if (getIdProductoCategoria() != other.idProductoCategoria && (getIdProductoCategoria() == null || !getIdProductoCategoria().equals(other.idProductoCategoria))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProductoCategoria() != null ? getIdProductoCategoria().hashCode() : 0);
    return hash;
  }

}


