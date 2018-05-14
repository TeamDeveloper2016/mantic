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
@Table(name="tr_mantic_articulo_precio_sugerido")
public class TrManticArticuloPrecioSugeridoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="precio")
  private Double precio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="descuento")
  private Double descuento;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_precio_sugerido")
  private Long idArticuloPrecioSugerido;
  @Column (name="id_leido")
  private Long idLeido;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticArticuloPrecioSugeridoDto() {
    this(new Long(-1L));
  }

  public TrManticArticuloPrecioSugeridoDto(Long key) {
    this(null, null, null, null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TrManticArticuloPrecioSugeridoDto(Long idProveedor, Double precio, Long idUsuario, Double descuento, String observaciones, Long idArticulo, Long idArticuloPrecioSugerido, Long idLeido) {
    setIdProveedor(idProveedor);
    setPrecio(precio);
    setIdUsuario(idUsuario);
    setDescuento(descuento);
    setObservaciones(observaciones);
    setIdArticulo(idArticulo);
    setIdArticuloPrecioSugerido(idArticuloPrecioSugerido);
    setIdLeido(idLeido);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Double getPrecio() {
    return precio;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDescuento(Double descuento) {
    this.descuento = descuento;
  }

  public Double getDescuento() {
    return descuento;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setIdArticuloPrecioSugerido(Long idArticuloPrecioSugerido) {
    this.idArticuloPrecioSugerido = idArticuloPrecioSugerido;
  }

  public Long getIdArticuloPrecioSugerido() {
    return idArticuloPrecioSugerido;
  }

  public void setIdLeido(Long idLeido) {
    this.idLeido = idLeido;
  }

  public Long getIdLeido() {
    return idLeido;
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
  	return getIdArticuloPrecioSugerido();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloPrecioSugerido = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloPrecioSugerido());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdLeido());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("precio", getPrecio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descuento", getDescuento());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idArticuloPrecioSugerido", getIdArticuloPrecioSugerido());
		regresar.put("idLeido", getIdLeido());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedor(), getPrecio(), getIdUsuario(), getDescuento(), getObservaciones(), getIdArticulo(), getIdArticuloPrecioSugerido(), getIdLeido(), getRegistro()
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
    regresar.append("idArticuloPrecioSugerido~");
    regresar.append(getIdArticuloPrecioSugerido());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloPrecioSugerido());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticArticuloPrecioSugeridoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloPrecioSugerido()!= null && getIdArticuloPrecioSugerido()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticArticuloPrecioSugeridoDto other = (TrManticArticuloPrecioSugeridoDto) obj;
    if (getIdArticuloPrecioSugerido() != other.idArticuloPrecioSugerido && (getIdArticuloPrecioSugerido() == null || !getIdArticuloPrecioSugerido().equals(other.idArticuloPrecioSugerido))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloPrecioSugerido() != null ? getIdArticuloPrecioSugerido().hashCode() : 0);
    return hash;
  }

}


