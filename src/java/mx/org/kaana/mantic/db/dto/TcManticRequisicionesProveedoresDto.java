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
@Table(name="tc_mantic_requisiciones_proveedores")
public class TcManticRequisicionesProveedoresDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_requisicion_proveedor")
  private Long idRequisicionProveedor;
  @Column (name="id_requisicion")
  private Long idRequisicion;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticRequisicionesProveedoresDto() {
    this(new Long(-1L));
  }

  public TcManticRequisicionesProveedoresDto(Long key) {
    this(null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticRequisicionesProveedoresDto(Long idProveedor, Long idRequisicionProveedor, Long idRequisicion) {
    setIdProveedor(idProveedor);
    setIdRequisicionProveedor(idRequisicionProveedor);
    setIdRequisicion(idRequisicion);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setIdRequisicionProveedor(Long idRequisicionProveedor) {
    this.idRequisicionProveedor = idRequisicionProveedor;
  }

  public Long getIdRequisicionProveedor() {
    return idRequisicionProveedor;
  }

  public void setIdRequisicion(Long idRequisicion) {
    this.idRequisicion = idRequisicion;
  }

  public Long getIdRequisicion() {
    return idRequisicion;
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
  	return getIdRequisicionProveedor();
  }

  @Override
  public void setKey(Long key) {
  	this.idRequisicionProveedor = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRequisicionProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRequisicion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("idRequisicionProveedor", getIdRequisicionProveedor());
		regresar.put("idRequisicion", getIdRequisicion());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedor(), getIdRequisicionProveedor(), getIdRequisicion(), getRegistro()
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
    regresar.append("idRequisicionProveedor~");
    regresar.append(getIdRequisicionProveedor());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdRequisicionProveedor());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticRequisicionesProveedoresDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdRequisicionProveedor()!= null && getIdRequisicionProveedor()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticRequisicionesProveedoresDto other = (TcManticRequisicionesProveedoresDto) obj;
    if (getIdRequisicionProveedor() != other.idRequisicionProveedor && (getIdRequisicionProveedor() == null || !getIdRequisicionProveedor().equals(other.idRequisicionProveedor))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdRequisicionProveedor() != null ? getIdRequisicionProveedor().hashCode() : 0);
    return hash;
  }

}


