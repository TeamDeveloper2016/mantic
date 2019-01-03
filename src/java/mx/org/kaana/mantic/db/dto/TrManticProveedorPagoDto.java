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
@Table(name="tr_mantic_proveedor_pago")
public class TrManticProveedorPagoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_proveedor_pago")
  private Long idProveedorPago;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="clave")
  private String clave;
  @Column (name="id_tipo_pago")
  private Long idTipoPago;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="descuento")
  private String descuento;
  @Column (name="plazo")
  private Long plazo;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticProveedorPagoDto() {
    this(new Long(-1L));
  }

  public TrManticProveedorPagoDto(Long key) {
    this(new Long(-1L), null, null, null, null, "0.00", null, 0L);
    setKey(key);
  }

  public TrManticProveedorPagoDto(Long idProveedorPago, Long idProveedor, String clave, Long idTipoPago, Long idUsuario, String descuento, String observaciones, Long plazo) {
    setIdProveedorPago(idProveedorPago);
    setIdProveedor(idProveedor);
    setClave(clave);
    setIdTipoPago(idTipoPago);
    setIdUsuario(idUsuario);
    setDescuento(descuento);
    setObservaciones(observaciones);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setPlazo(plazo);
  }

	public Long getPlazo() {
		return plazo;
	}

	public void setPlazo(Long plazo) {
		this.plazo = plazo;
	}
	
  public void setIdProveedorPago(Long idProveedorPago) {
    this.idProveedorPago = idProveedorPago;
  }

  public Long getIdProveedorPago() {
    return idProveedorPago;
  }

  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }

  public void setIdTipoPago(Long idTipoPago) {
    this.idTipoPago = idTipoPago;
  }

  public Long getIdTipoPago() {
    return idTipoPago;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
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
  	return getIdProveedorPago();
  }

  @Override
  public void setKey(Long key) {
  	this.idProveedorPago = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedorPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoPago());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPlazo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedorPago", getIdProveedorPago());
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("clave", getClave());
		regresar.put("idTipoPago", getIdTipoPago());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descuento", getDescuento());
		regresar.put("observaciones", getObservaciones());
		regresar.put("registro", getRegistro());
		regresar.put("plazo", getPlazo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedorPago(), getIdProveedor(), getClave(), getIdTipoPago(), getIdUsuario(), getDescuento(), getObservaciones(), getRegistro(), getPlazo()
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
    regresar.append("idProveedorPago~");
    regresar.append(getIdProveedorPago());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProveedorPago());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticProveedorPagoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProveedorPago()!= null && getIdProveedorPago()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticProveedorPagoDto other = (TrManticProveedorPagoDto) obj;
    if (getIdProveedorPago() != other.idProveedorPago && (getIdProveedorPago() == null || !getIdProveedorPago().equals(other.idProveedorPago))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProveedorPago() != null ? getIdProveedorPago().hashCode() : 0);
    return hash;
  }
}