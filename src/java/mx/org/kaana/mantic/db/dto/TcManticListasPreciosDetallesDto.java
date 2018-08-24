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
@Table(name="tc_mantic_listas_precios_detalles")
public class TcManticListasPreciosDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_lista_precio")
  private Long idListaPrecio;
  @Column (name="descripcion")
  private String descripcion;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_lista_precio_detalle")
  private Long idListaPrecioDetalle;
  @Column (name="codigo")
  private String codigo;
  @Column (name="precio")
  private Double precio;
  @Column (name="auxiliar")
  private String auxiliar;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="costo")
  private Double costo;

  public TcManticListasPreciosDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticListasPreciosDetallesDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticListasPreciosDetallesDto(Long idListaPrecio, String descripcion, Long idListaPrecioDetalle, String codigo, Double precio, String auxiliar, Double costo) {
    setIdListaPrecio(idListaPrecio);
    setDescripcion(descripcion);
    setIdListaPrecioDetalle(idListaPrecioDetalle);
    setCodigo(codigo);
    setPrecio(precio);
    setAuxiliar(auxiliar);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setCosto(costo);
  }
	
  public void setIdListaPrecio(Long idListaPrecio) {
    this.idListaPrecio = idListaPrecio;
  }

  public Long getIdListaPrecio() {
    return idListaPrecio;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdListaPrecioDetalle(Long idListaPrecioDetalle) {
    this.idListaPrecioDetalle = idListaPrecioDetalle;
  }

  public Long getIdListaPrecioDetalle() {
    return idListaPrecioDetalle;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Double getPrecio() {
    return precio;
  }

  public void setAuxiliar(String auxiliar) {
    this.auxiliar = auxiliar;
  }

  public String getAuxiliar() {
    return auxiliar;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo=costo;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdListaPrecioDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idListaPrecioDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdListaPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdListaPrecioDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAuxiliar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idListaPrecio", getIdListaPrecio());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idListaPrecioDetalle", getIdListaPrecioDetalle());
		regresar.put("codigo", getCodigo());
		regresar.put("precio", getPrecio());
		regresar.put("auxiliar", getAuxiliar());
		regresar.put("registro", getRegistro());
		regresar.put("costo", getCosto());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getIdListaPrecio(), getDescripcion(), getIdListaPrecioDetalle(), getCodigo(), getPrecio(), getAuxiliar(), getRegistro(), getCosto()
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
    regresar.append("idListaPrecioDetalle~");
    regresar.append(getIdListaPrecioDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdListaPrecioDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticListasPreciosDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdListaPrecioDetalle()!= null && getIdListaPrecioDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticListasPreciosDetallesDto other = (TcManticListasPreciosDetallesDto) obj;
    if (getIdListaPrecioDetalle() != other.idListaPrecioDetalle && (getIdListaPrecioDetalle() == null || !getIdListaPrecioDetalle().equals(other.idListaPrecioDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdListaPrecioDetalle() != null ? getIdListaPrecioDetalle().hashCode() : 0);
    return hash;
  }

}


