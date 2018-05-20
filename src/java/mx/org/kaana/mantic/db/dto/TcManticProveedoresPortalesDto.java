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
@Table(name="tc_mantic_proveedores_portales")
public class TcManticProveedoresPortalesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_proveedor_portal")
  private Long idProveedorPortal;
  @Column (name="pagina")
  private String pagina;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="cuenta")
  private String cuenta;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="contrasenia")
  private String contrasenia;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticProveedoresPortalesDto() {
    this(new Long(-1L));
  }

  public TcManticProveedoresPortalesDto(Long key) {
    this(new Long(-1L), null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticProveedoresPortalesDto(Long idProveedorPortal, String pagina, Long idProveedor, Long idUsuario, String cuenta, String observaciones, String contrasenia) {
    setIdProveedorPortal(idProveedorPortal);
    setPagina(pagina);
    setIdProveedor(idProveedor);
    setIdUsuario(idUsuario);
    setCuenta(cuenta);
    setObservaciones(observaciones);
    setContrasenia(contrasenia);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProveedorPortal(Long idProveedorPortal) {
    this.idProveedorPortal = idProveedorPortal;
  }

  public Long getIdProveedorPortal() {
    return idProveedorPortal;
  }

  public void setPagina(String pagina) {
    this.pagina = pagina;
  }

  public String getPagina() {
    return pagina;
  }

  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  public String getCuenta() {
    return cuenta;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public String getContrasenia() {
    return contrasenia;
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
  	return getIdProveedorPortal();
  }

  @Override
  public void setKey(Long key) {
  	this.idProveedorPortal = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedorPortal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPagina());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getContrasenia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedorPortal", getIdProveedorPortal());
		regresar.put("pagina", getPagina());
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("cuenta", getCuenta());
		regresar.put("observaciones", getObservaciones());
		regresar.put("contrasenia", getContrasenia());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedorPortal(), getPagina(), getIdProveedor(), getIdUsuario(), getCuenta(), getObservaciones(), getContrasenia(), getRegistro()
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
    regresar.append("idProveedorPortal~");
    regresar.append(getIdProveedorPortal());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProveedorPortal());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticProveedoresPortalesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProveedorPortal()!= null && getIdProveedorPortal()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticProveedoresPortalesDto other = (TcManticProveedoresPortalesDto) obj;
    if (getIdProveedorPortal() != other.idProveedorPortal && (getIdProveedorPortal() == null || !getIdProveedorPortal().equals(other.idProveedorPortal))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProveedorPortal() != null ? getIdProveedorPortal().hashCode() : 0);
    return hash;
  }
}