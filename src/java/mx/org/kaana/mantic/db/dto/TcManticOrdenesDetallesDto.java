package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
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
@Table(name="tc_mantic_ordenes_detalles")
public class TcManticOrdenesDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_orden_compra")
  private Long idOrdenCompra;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_orden_detalle")
  private Long idOrdenDetalle;
  @Column (name="id_articulo")
  private Long idArticulo;

  public TcManticOrdenesDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticOrdenesDetallesDto(Long key) {
    this(null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticOrdenesDetallesDto(Long idOrdenCompra, Long idOrdenDetalle, Long idArticulo) {
    setIdOrdenCompra(idOrdenCompra);
    setIdOrdenDetalle(idOrdenDetalle);
    setIdArticulo(idArticulo);
  }
	
  public void setIdOrdenCompra(Long idOrdenCompra) {
    this.idOrdenCompra = idOrdenCompra;
  }

  public Long getIdOrdenCompra() {
    return idOrdenCompra;
  }

  public void setIdOrdenDetalle(Long idOrdenDetalle) {
    this.idOrdenDetalle = idOrdenDetalle;
  }

  public Long getIdOrdenDetalle() {
    return idOrdenDetalle;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdOrdenDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idOrdenDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdOrdenCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idOrdenCompra", getIdOrdenCompra());
		regresar.put("idOrdenDetalle", getIdOrdenDetalle());
		regresar.put("idArticulo", getIdArticulo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdOrdenCompra(), getIdOrdenDetalle(), getIdArticulo()
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
    regresar.append("idOrdenDetalle~");
    regresar.append(getIdOrdenDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdOrdenDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticOrdenesDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdOrdenDetalle()!= null && getIdOrdenDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticOrdenesDetallesDto other = (TcManticOrdenesDetallesDto) obj;
    if (getIdOrdenDetalle() != other.idOrdenDetalle && (getIdOrdenDetalle() == null || !getIdOrdenDetalle().equals(other.idOrdenDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdOrdenDetalle() != null ? getIdOrdenDetalle().hashCode() : 0);
    return hash;
  }

}


