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
@Table(name="tc_mantic_transferencias_multiples_detalles")
public class TcManticTransferenciasMultiplesDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="codigo")
  private String codigo;
  @Column (name="id_transferencia_multiple")
  private Long idTransferenciaMultiple;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="nombre")
  private String nombre;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_transferencia_multiple_detalle")
  private Long idTransferenciaMultipleDetalle;
  @Column (name="caja")
  private Long caja;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTransferenciasMultiplesDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticTransferenciasMultiplesDetallesDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticTransferenciasMultiplesDetallesDto(String codigo, Double cantidad, Long idTransferenciaMultiple, Long idArticulo, Long idTransferenciaMultipleDetalle, String nombre, Long idAlmacen, Long caja) {
    setCodigo(codigo);
    setIdTransferenciaMultiple(idTransferenciaMultiple);
    setIdAlmacen(idAlmacen);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setNombre(nombre);
    setIdTransferenciaMultipleDetalle(idTransferenciaMultipleDetalle);
    setCaja(caja);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setIdTransferenciaMultiple(Long idTransferenciaMultiple) {
    this.idTransferenciaMultiple = idTransferenciaMultiple;
  }

  public Long getIdTransferenciaMultiple() {
    return idTransferenciaMultiple;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setIdTransferenciaMultipleDetalle(Long idTransferenciaMultipleDetalle) {
    this.idTransferenciaMultipleDetalle = idTransferenciaMultipleDetalle;
  }

  public Long getIdTransferenciaMultipleDetalle() {
    return idTransferenciaMultipleDetalle;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public Long getCaja() {
    return caja;
  }

  public void setCaja(Long caja) {
    this.caja = caja;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdTransferenciaMultipleDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idTransferenciaMultipleDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaMultiple());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTransferenciaMultipleDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCaja());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("idTransferenciaMultiple", getIdTransferenciaMultiple());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("nombre", getNombre());
		regresar.put("idTransferenciaMultipleDetalle", getIdTransferenciaMultipleDetalle());
		regresar.put("caja", getCaja());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getCodigo(), getIdTransferenciaMultiple(), getIdAlmacen(), getCantidad(), getIdArticulo(), getNombre(), getIdTransferenciaMultipleDetalle(), getCaja(), getRegistro()
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
    regresar.append("idTransferenciaMultipleDetalle~");
    regresar.append(getIdTransferenciaMultipleDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTransferenciaMultipleDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTransferenciasMultiplesDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTransferenciaMultipleDetalle()!= null && getIdTransferenciaMultipleDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTransferenciasMultiplesDetallesDto other = (TcManticTransferenciasMultiplesDetallesDto) obj;
    if (getIdTransferenciaMultipleDetalle() != other.idTransferenciaMultipleDetalle && (getIdTransferenciaMultipleDetalle() == null || !getIdTransferenciaMultipleDetalle().equals(other.idTransferenciaMultipleDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTransferenciaMultipleDetalle() != null ? getIdTransferenciaMultipleDetalle().hashCode() : 0);
    return hash;
  }

}


