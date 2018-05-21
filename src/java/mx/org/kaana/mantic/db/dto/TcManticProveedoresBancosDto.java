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
@Table(name="tc_mantic_proveedores_bancos")
public class TcManticProveedoresBancosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="convenio_cuenta")
  private String convenioCuenta;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_proveedor_banca")
  private Long idProveedorBanca;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_tipo_cuenta")
  private Long idTipoCuenta;
  @Column (name="clabe_referencia")
  private String clabeReferencia;
  @Column (name="id_banco")
  private Long idBanco;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticProveedoresBancosDto() {
    this(new Long(-1L));
  }

  public TcManticProveedoresBancosDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticProveedoresBancosDto(Long idProveedor, String convenioCuenta, Long idProveedorBanca, Long idUsuario, String observaciones, Long idTipoCuenta, String clabeReferencia, Long idBanco) {
    setIdProveedor(idProveedor);
    setConvenioCuenta(convenioCuenta);
    setIdProveedorBanca(idProveedorBanca);
    setIdUsuario(idUsuario);
    setObservaciones(observaciones);
    setIdTipoCuenta(idTipoCuenta);
    setClabeReferencia(clabeReferencia);
    setIdBanco(idBanco);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setConvenioCuenta(String convenioCuenta) {
    this.convenioCuenta = convenioCuenta;
  }

  public String getConvenioCuenta() {
    return convenioCuenta;
  }

  public void setIdProveedorBanca(Long idProveedorBanca) {
    this.idProveedorBanca = idProveedorBanca;
  }

  public Long getIdProveedorBanca() {
    return idProveedorBanca;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdTipoCuenta(Long idTipoCuenta) {
    this.idTipoCuenta = idTipoCuenta;
  }

  public Long getIdTipoCuenta() {
    return idTipoCuenta;
  }

  public void setClabeReferencia(String clabeReferencia) {
    this.clabeReferencia = clabeReferencia;
  }

  public String getClabeReferencia() {
    return clabeReferencia;
  }

  public void setIdBanco(Long idBanco) {
    this.idBanco = idBanco;
  }

  public Long getIdBanco() {
    return idBanco;
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
  	return getIdProveedorBanca();
  }

  @Override
  public void setKey(Long key) {
  	this.idProveedorBanca = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConvenioCuenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedorBanca());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoCuenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getClabeReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBanco());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("convenioCuenta", getConvenioCuenta());
		regresar.put("idProveedorBanca", getIdProveedorBanca());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idTipoCuenta", getIdTipoCuenta());
		regresar.put("clabeReferencia", getClabeReferencia());
		regresar.put("idBanco", getIdBanco());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedor(), getConvenioCuenta(), getIdProveedorBanca(), getIdUsuario(), getObservaciones(), getIdTipoCuenta(), getClabeReferencia(), getIdBanco(), getRegistro()
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
    regresar.append("idProveedorBanca~");
    regresar.append(getIdProveedorBanca());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdProveedorBanca());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticProveedoresBancosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdProveedorBanca()!= null && getIdProveedorBanca()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticProveedoresBancosDto other = (TcManticProveedoresBancosDto) obj;
    if (getIdProveedorBanca() != other.idProveedorBanca && (getIdProveedorBanca() == null || !getIdProveedorBanca().equals(other.idProveedorBanca))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdProveedorBanca() != null ? getIdProveedorBanca().hashCode() : 0);
    return hash;
  }
}