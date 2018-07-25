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
@Table(name="tc_mantic_almacenes")
public class TcManticAlmacenesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="clave")
  private String clave;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_responsable")
  private Long idResponsable;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;
	@Column (name="id_principal")
  private Long idPrincipal;

  public TcManticAlmacenesDto() {
    this(new Long(-1L));
  }

  public TcManticAlmacenesDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticAlmacenesDto(String descripcion, String clave, Long idUsuario, Long idAlmacen, Long idEmpresa, Long idResponsable, String nombre, Long idPrincipal) {
    setDescripcion(descripcion);
    setClave(clave);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setIdEmpresa(idEmpresa);
    setIdResponsable(idResponsable);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setIdPrincipal(idPrincipal);
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
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

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdResponsable(Long idResponsable) {
    this.idResponsable = idResponsable;
  }

  public Long getIdResponsable() {
    return idResponsable;
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

	public Long getIdPrincipal() {
		return idPrincipal;
	}

	public void setIdPrincipal(Long idPrincipal) {
		this.idPrincipal = idPrincipal;
	}
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdAlmacen();
  }

  @Override
  public void setKey(Long key) {
  	this.idAlmacen = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdResponsable());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPrincipal());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("clave", getClave());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idResponsable", getIdResponsable());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
		regresar.put("idPrincipal", getIdPrincipal());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getClave(), getIdUsuario(), getIdAlmacen(), getIdEmpresa(), getIdResponsable(), getNombre(), getRegistro(), getIdPrincipal()
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
    regresar.append("idAlmacen~");
    regresar.append(getIdAlmacen());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdAlmacen());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticAlmacenesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdAlmacen()!= null && getIdAlmacen()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticAlmacenesDto other = (TcManticAlmacenesDto) obj;
    if (getIdAlmacen() != other.idAlmacen && (getIdAlmacen() == null || !getIdAlmacen().equals(other.idAlmacen))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdAlmacen() != null ? getIdAlmacen().hashCode() : 0);
    return hash;
  }
	
}
