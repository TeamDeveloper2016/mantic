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
@Table(name="tr_mantic_articulos_precios_ofertados")
public class TrManticArticulosPreciosOfertadosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="precio")
  private Double precio;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_precio_ofertado")
  private Long idArticuloPrecioOfertado;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="descuento")
  private Double descuento;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticArticulosPreciosOfertadosDto() {
    this(new Long(-1L));
  }

  public TrManticArticulosPreciosOfertadosDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TrManticArticulosPreciosOfertadosDto(Long idProveedor, Double precio, Long idArticuloPrecioOfertado, Long idUsuario, Double descuento, String observaciones, Long idArticulo) {
    setIdProveedor(idProveedor);
    setPrecio(precio);
    setIdArticuloPrecioOfertado(idArticuloPrecioOfertado);
    setIdUsuario(idUsuario);
    setDescuento(descuento);
    setObservaciones(observaciones);
    setIdArticulo(idArticulo);
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

  public void setIdArticuloPrecioOfertado(Long idArticuloPrecioOfertado) {
    this.idArticuloPrecioOfertado = idArticuloPrecioOfertado;
  }

  public Long getIdArticuloPrecioOfertado() {
    return idArticuloPrecioOfertado;
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

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdArticuloPrecioOfertado();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloPrecioOfertado = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloPrecioOfertado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
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
		regresar.put("idArticuloPrecioOfertado", getIdArticuloPrecioOfertado());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descuento", getDescuento());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedor(), getPrecio(), getIdArticuloPrecioOfertado(), getIdUsuario(), getDescuento(), getObservaciones(), getIdArticulo(), getRegistro()
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
    regresar.append("idArticuloPrecioOfertado~");
    regresar.append(getIdArticuloPrecioOfertado());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloPrecioOfertado());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticArticulosPreciosOfertadosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloPrecioOfertado()!= null && getIdArticuloPrecioOfertado()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticArticulosPreciosOfertadosDto other = (TrManticArticulosPreciosOfertadosDto) obj;
    if (getIdArticuloPrecioOfertado() != other.idArticuloPrecioOfertado && (getIdArticuloPrecioOfertado() == null || !getIdArticuloPrecioOfertado().equals(other.idArticuloPrecioOfertado))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloPrecioOfertado() != null ? getIdArticuloPrecioOfertado().hashCode() : 0);
    return hash;
  }

}


