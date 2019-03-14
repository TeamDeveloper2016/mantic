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
@Table(name="tc_mantic_movimientos")
public class TcManticMovimientosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_tipo_movimiento")
  private Long idTipoMovimiento;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_movimiento")
  private Long idMovimiento;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="stock")
  private Double stock;
  @Column (name="calculo")
  private Double calculo;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticMovimientosDto() {
    this(new Long(-1L));
  }

  public TcManticMovimientosDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticMovimientosDto(String consecutivo, Long idTipoMovimiento, Long idUsuario, Long idAlmacen, Long idMovimiento, Double cantidad, Long idArticulo, Double stock, Double calculo, String observaciones) {
    setConsecutivo(consecutivo);
    setIdTipoMovimiento(idTipoMovimiento);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setIdMovimiento(idMovimiento);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setStock(stock);
    setCalculo(calculo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		this.observaciones= observaciones;
  }
	
  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setIdTipoMovimiento(Long idTipoMovimiento) {
    this.idTipoMovimiento = idTipoMovimiento;
  }

  public Long getIdTipoMovimiento() {
    return idTipoMovimiento;
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

  public void setIdMovimiento(Long idMovimiento) {
    this.idMovimiento = idMovimiento;
  }

  public Long getIdMovimiento() {
    return idMovimiento;
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

  public void setStock(Double stock) {
    this.stock = stock;
  }

  public Double getStock() {
    return stock;
  }

  public void setCalculo(Double calculo) {
    this.calculo = calculo;
  }

  public Double getCalculo() {
    return calculo;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones=observaciones;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdMovimiento();
  }

  @Override
  public void setKey(Long key) {
  	this.idMovimiento = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMovimiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMovimiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getStock());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCalculo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idTipoMovimiento", getIdTipoMovimiento());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("idMovimiento", getIdMovimiento());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("stock", getStock());
		regresar.put("calculo", getCalculo());
		regresar.put("registro", getRegistro());
		regresar.put("observaciones", getObservaciones());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getConsecutivo(), getIdTipoMovimiento(), getIdUsuario(), getIdAlmacen(), getIdMovimiento(), getCantidad(), getIdArticulo(), getStock(), getCalculo(), getObservaciones(), getRegistro()
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
    regresar.append("idMovimiento~");
    regresar.append(getIdMovimiento());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMovimiento());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticMovimientosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdMovimiento()!= null && getIdMovimiento()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticMovimientosDto other = (TcManticMovimientosDto) obj;
    if (getIdMovimiento() != other.idMovimiento && (getIdMovimiento() == null || !getIdMovimiento().equals(other.idMovimiento))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMovimiento() != null ? getIdMovimiento().hashCode() : 0);
    return hash;
  }

}


