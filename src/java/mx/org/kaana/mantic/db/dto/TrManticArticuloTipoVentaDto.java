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
@Table(name="tr_mantic_articulo_tipo_venta")
public class TrManticArticuloTipoVentaDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_tipo_venta")
  private Long idArticuloTipoVenta;
  @Column (name="id_registro")
  private Long idRegistro;
  @Column (name="porcentaje")
  private Double porcentaje;
  @Column (name="orden")
  private Long orden;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_tipo_venta")
  private Long idTipoVenta;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticArticuloTipoVentaDto() {
    this(new Long(-1L));
  }

  public TrManticArticuloTipoVentaDto(Long key) {
    this(new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TrManticArticuloTipoVentaDto(Long idArticuloTipoVenta, Long idRegistro, Double porcentaje, Long orden, Long idArticulo, Long idTipoVenta) {
    setIdArticuloTipoVenta(idArticuloTipoVenta);
    setIdRegistro(idRegistro);
    setPorcentaje(porcentaje);
    setOrden(orden);
    setIdArticulo(idArticulo);
    setIdTipoVenta(idTipoVenta);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdArticuloTipoVenta(Long idArticuloTipoVenta) {
    this.idArticuloTipoVenta = idArticuloTipoVenta;
  }

  public Long getIdArticuloTipoVenta() {
    return idArticuloTipoVenta;
  }

  public void setIdRegistro(Long idRegistro) {
    this.idRegistro = idRegistro;
  }

  public Long getIdRegistro() {
    return idRegistro;
  }

  public void setPorcentaje(Double porcentaje) {
    this.porcentaje = porcentaje;
  }

  public Double getPorcentaje() {
    return porcentaje;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setIdTipoVenta(Long idTipoVenta) {
    this.idTipoVenta = idTipoVenta;
  }

  public Long getIdTipoVenta() {
    return idTipoVenta;
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
  	return getIdArticuloTipoVenta();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloTipoVenta = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdArticuloTipoVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPorcentaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idArticuloTipoVenta", getIdArticuloTipoVenta());
		regresar.put("idRegistro", getIdRegistro());
		regresar.put("porcentaje", getPorcentaje());
		regresar.put("orden", getOrden());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idTipoVenta", getIdTipoVenta());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdArticuloTipoVenta(), getIdRegistro(), getPorcentaje(), getOrden(), getIdArticulo(), getIdTipoVenta(), getRegistro()
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
    regresar.append("idArticuloTipoVenta~");
    regresar.append(getIdArticuloTipoVenta());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloTipoVenta());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticArticuloTipoVentaDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloTipoVenta()!= null && getIdArticuloTipoVenta()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticArticuloTipoVentaDto other = (TrManticArticuloTipoVentaDto) obj;
    if (getIdArticuloTipoVenta() != other.idArticuloTipoVenta && (getIdArticuloTipoVenta() == null || !getIdArticuloTipoVenta().equals(other.idArticuloTipoVenta))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloTipoVenta() != null ? getIdArticuloTipoVenta().hashCode() : 0);
    return hash;
  }

}


