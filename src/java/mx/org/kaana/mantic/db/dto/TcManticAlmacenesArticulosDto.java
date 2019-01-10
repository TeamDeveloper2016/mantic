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
@Table(name="tc_mantic_almacenes_articulos")
public class TcManticAlmacenesArticulosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="minimo")
  private Double minimo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_almacen_articulo")
  private Long idAlmacenArticulo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="maximo")
  private Double maximo;
  @Column (name="id_almacen_ubicacion")
  private Long idAlmacenUbicacion;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="stock")
  private Double stock;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticAlmacenesArticulosDto() {
    this(new Long(-1L));
  }

  public TcManticAlmacenesArticulosDto(Long key) {
    this(null, new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticAlmacenesArticulosDto(Double minimo, Long idAlmacenArticulo, Long idUsuario, Long idAlmacen, Double maximo, Long idAlmacenUbicacion, Long idArticulo, Double stock) {
    setMinimo(minimo);
    setIdAlmacenArticulo(idAlmacenArticulo);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setMaximo(maximo);
    setIdAlmacenUbicacion(idAlmacenUbicacion);
    setIdArticulo(idArticulo);
    setStock(stock);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setMinimo(Double minimo) {
    this.minimo = minimo;
  }

  public Double getMinimo() {
    return minimo;
  }

  public void setIdAlmacenArticulo(Long idAlmacenArticulo) {
    this.idAlmacenArticulo = idAlmacenArticulo;
  }

  public Long getIdAlmacenArticulo() {
    return idAlmacenArticulo;
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

  public void setMaximo(Double maximo) {
    this.maximo = maximo;
  }

  public Double getMaximo() {
    return maximo;
  }

  public void setIdAlmacenUbicacion(Long idAlmacenUbicacion) {
    this.idAlmacenUbicacion = idAlmacenUbicacion;
  }

  public Long getIdAlmacenUbicacion() {
    return idAlmacenUbicacion;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setStock(Double stock) {
    this.stock = stock;
  }

  public Double getStock() {
    return stock;
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
  	return getIdAlmacenArticulo();
  }

  @Override
  public void setKey(Long key) {
  	this.idAlmacenArticulo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getMinimo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacenArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMaximo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacenUbicacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getStock());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("minimo", getMinimo());
		regresar.put("idAlmacenArticulo", getIdAlmacenArticulo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("maximo", getMaximo());
		regresar.put("idAlmacenUbicacion", getIdAlmacenUbicacion());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("stock", getStock());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getMinimo(), getIdAlmacenArticulo(), getIdUsuario(), getIdAlmacen(), getMaximo(), getIdAlmacenUbicacion(), getIdArticulo(), getStock(), getRegistro()
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
    regresar.append("idAlmacenArticulo~");
    regresar.append(getIdAlmacenArticulo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdAlmacenArticulo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticAlmacenesArticulosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdAlmacenArticulo()!= null && getIdAlmacenArticulo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticAlmacenesArticulosDto other = (TcManticAlmacenesArticulosDto) obj;
    if (getIdAlmacenArticulo() != other.idAlmacenArticulo && (getIdAlmacenArticulo() == null || !getIdAlmacenArticulo().equals(other.idAlmacenArticulo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdAlmacenArticulo() != null ? getIdAlmacenArticulo().hashCode() : 0);
    return hash;
  }

}


