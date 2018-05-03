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
@Table(name="tc_mantic_notas_detalles")
public class TcManticNotasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="costo")
  private Double costo;
  @Column (name="descuento")
  private Double descuento;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_nota_detalle")
  private Long idNotaDetalle;
  @Column (name="cantidad")
  private Long cantidad;
  @Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticNotasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticNotasDetallesDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticNotasDetallesDto(Double costo, Double descuento, Long idNotaDetalle, Long cantidad, Long idNotaEntrada, Long idArticulo, Double importe) {
    setCosto(costo);
    setDescuento(descuento);
    setIdNotaDetalle(idNotaDetalle);
    setCantidad(cantidad);
    setIdNotaEntrada(idNotaEntrada);
    setIdArticulo(idArticulo);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setDescuento(Double descuento) {
    this.descuento = descuento;
  }

  public Double getDescuento() {
    return descuento;
  }

  public void setIdNotaDetalle(Long idNotaDetalle) {
    this.idNotaDetalle = idNotaDetalle;
  }

  public Long getIdNotaDetalle() {
    return idNotaDetalle;
  }

  public void setCantidad(Long cantidad) {
    this.cantidad = cantidad;
  }

  public Long getCantidad() {
    return cantidad;
  }

  public void setIdNotaEntrada(Long idNotaEntrada) {
    this.idNotaEntrada = idNotaEntrada;
  }

  public Long getIdNotaEntrada() {
    return idNotaEntrada;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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
  	return getIdNotaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idNotaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("costo", getCosto());
		regresar.put("descuento", getDescuento());
		regresar.put("idNotaDetalle", getIdNotaDetalle());
		regresar.put("cantidad", getCantidad());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCosto(), getDescuento(), getIdNotaDetalle(), getCantidad(), getIdNotaEntrada(), getIdArticulo(), getImporte(), getRegistro()
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
    regresar.append("idNotaDetalle~");
    regresar.append(getIdNotaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdNotaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticNotasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdNotaDetalle()!= null && getIdNotaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticNotasDetallesDto other = (TcManticNotasDetallesDto) obj;
    if (getIdNotaDetalle() != other.idNotaDetalle && (getIdNotaDetalle() == null || !getIdNotaDetalle().equals(other.idNotaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdNotaDetalle() != null ? getIdNotaDetalle().hashCode() : 0);
    return hash;
  }

}


