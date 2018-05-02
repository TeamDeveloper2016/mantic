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
@Table(name="tc_mantic_tipos_proveedores")
public class TcManticTiposProveedoresDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_proveedor")
  private Long idTipoProveedor;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="id_tipo_dia")
  private Long idTipoDia;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="dias")
  private Long dias;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposProveedoresDto() {
    this(new Long(-1L));
  }

  public TcManticTiposProveedoresDto(Long key) {
    this(new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticTiposProveedoresDto(Long idTipoProveedor, String descripcion, Long idTipoDia, Long idUsuario, Long dias, String nombre) {
    setIdTipoProveedor(idTipoProveedor);
    setDescripcion(descripcion);
    setIdTipoDia(idTipoDia);
    setIdUsuario(idUsuario);
    setDias(dias);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdTipoProveedor(Long idTipoProveedor) {
    this.idTipoProveedor = idTipoProveedor;
  }

  public Long getIdTipoProveedor() {
    return idTipoProveedor;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdTipoDia(Long idTipoDia) {
    this.idTipoDia = idTipoDia;
  }

  public Long getIdTipoDia() {
    return idTipoDia;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDias(Long dias) {
    this.dias = dias;
  }

  public Long getDias() {
    return dias;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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
  	return getIdTipoProveedor();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoProveedor = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdTipoProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoDia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idTipoProveedor", getIdTipoProveedor());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idTipoDia", getIdTipoDia());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("dias", getDias());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdTipoProveedor(), getDescripcion(), getIdTipoDia(), getIdUsuario(), getDias(), getNombre(), getRegistro()
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
    regresar.append("idTipoProveedor~");
    regresar.append(getIdTipoProveedor());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoProveedor());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposProveedoresDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoProveedor()!= null && getIdTipoProveedor()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposProveedoresDto other = (TcManticTiposProveedoresDto) obj;
    if (getIdTipoProveedor() != other.idTipoProveedor && (getIdTipoProveedor() == null || !getIdTipoProveedor().equals(other.idTipoProveedor))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoProveedor() != null ? getIdTipoProveedor().hashCode() : 0);
    return hash;
  }

}


