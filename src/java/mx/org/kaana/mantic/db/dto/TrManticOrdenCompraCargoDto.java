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
@Table(name="tr_mantic_orden_compra_cargo")
public class TrManticOrdenCompraCargoDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_orden_compra")
  private Long idOrdenCompra;
  @Column (name="observaciones")
  private String observaciones;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_orden_compra_cargo")
  private Long idOrdenCompraCargo;
  @Column (name="porcentaje")
  private Double porcentaje;
  @Column (name="importe")
  private Double importe;
  @Column (name="id_cargo_compra")
  private Long idCargoCompra;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticOrdenCompraCargoDto() {
    this(new Long(-1L));
  }

  public TrManticOrdenCompraCargoDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TrManticOrdenCompraCargoDto(Long idUsuario, Long idOrdenCompra, String observaciones, Long idOrdenCompraCargo, Double porcentaje, Double importe, Long idCargoCompra) {
    setIdUsuario(idUsuario);
    setIdOrdenCompra(idOrdenCompra);
    setObservaciones(observaciones);
    setIdOrdenCompraCargo(idOrdenCompraCargo);
    setPorcentaje(porcentaje);
    setImporte(importe);
    setIdCargoCompra(idCargoCompra);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdOrdenCompra(Long idOrdenCompra) {
    this.idOrdenCompra = idOrdenCompra;
  }

  public Long getIdOrdenCompra() {
    return idOrdenCompra;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdOrdenCompraCargo(Long idOrdenCompraCargo) {
    this.idOrdenCompraCargo = idOrdenCompraCargo;
  }

  public Long getIdOrdenCompraCargo() {
    return idOrdenCompraCargo;
  }

  public void setPorcentaje(Double porcentaje) {
    this.porcentaje = porcentaje;
  }

  public Double getPorcentaje() {
    return porcentaje;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
  }

  public void setIdCargoCompra(Long idCargoCompra) {
    this.idCargoCompra = idCargoCompra;
  }

  public Long getIdCargoCompra() {
    return idCargoCompra;
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
  	return getIdOrdenCompraCargo();
  }

  @Override
  public void setKey(Long key) {
  	this.idOrdenCompraCargo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenCompraCargo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPorcentaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCargoCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idOrdenCompra", getIdOrdenCompra());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idOrdenCompraCargo", getIdOrdenCompraCargo());
		regresar.put("porcentaje", getPorcentaje());
		regresar.put("importe", getImporte());
		regresar.put("idCargoCompra", getIdCargoCompra());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getIdOrdenCompra(), getObservaciones(), getIdOrdenCompraCargo(), getPorcentaje(), getImporte(), getIdCargoCompra(), getRegistro()
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
    regresar.append("idOrdenCompraCargo~");
    regresar.append(getIdOrdenCompraCargo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdOrdenCompraCargo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticOrdenCompraCargoDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdOrdenCompraCargo()!= null && getIdOrdenCompraCargo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticOrdenCompraCargoDto other = (TrManticOrdenCompraCargoDto) obj;
    if (getIdOrdenCompraCargo() != other.idOrdenCompraCargo && (getIdOrdenCompraCargo() == null || !getIdOrdenCompraCargo().equals(other.idOrdenCompraCargo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdOrdenCompraCargo() != null ? getIdOrdenCompraCargo().hashCode() : 0);
    return hash;
  }

}


