package mx.org.kaana.kajool.db.dto;

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
@Table(name="tc_janal_cuentas_movil")
public class TcJanalCuentasMovilDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_entidad")
  private Long idEntidad;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cuenta_movil")
  private Long idCuentaMovil;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="contrasenia")
  private String contrasenia;
  @Column (name="cuenta")
  private String cuenta;

  public TcJanalCuentasMovilDto() {
    this(new Long(-1L));
  }

  public TcJanalCuentasMovilDto(Long key) {
    this(null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcJanalCuentasMovilDto(Long idEntidad, Long idCuentaMovil, String contrasenia, String cuenta) {
    setIdEntidad(idEntidad);
    setIdCuentaMovil(idCuentaMovil);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setContrasenia(contrasenia);
    setCuenta(cuenta);
  }
	
  public void setIdEntidad(Long idEntidad) {
    this.idEntidad = idEntidad;
  }

  public Long getIdEntidad() {
    return idEntidad;
  }

  public void setIdCuentaMovil(Long idCuentaMovil) {
    this.idCuentaMovil = idCuentaMovil;
  }

  public Long getIdCuentaMovil() {
    return idCuentaMovil;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public String getContrasenia() {
    return contrasenia;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  public String getCuenta() {
    return cuenta;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdCuentaMovil();
  }

  @Override
  public void setKey(Long key) {
  	this.idCuentaMovil = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdEntidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCuentaMovil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getContrasenia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuenta());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idEntidad", getIdEntidad());
		regresar.put("idCuentaMovil", getIdCuentaMovil());
		regresar.put("registro", getRegistro());
		regresar.put("contrasenia", getContrasenia());
		regresar.put("cuenta", getCuenta());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdEntidad(), getIdCuentaMovil(), getRegistro(), getContrasenia(), getCuenta()
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
    regresar.append("idCuentaMovil~");
    regresar.append(getIdCuentaMovil());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCuentaMovil());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalCuentasMovilDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCuentaMovil()!= null && getIdCuentaMovil()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalCuentasMovilDto other = (TcJanalCuentasMovilDto) obj;
    if (getIdCuentaMovil() != other.idCuentaMovil && (getIdCuentaMovil() == null || !getIdCuentaMovil().equals(other.idCuentaMovil))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCuentaMovil() != null ? getIdCuentaMovil().hashCode() : 0);
    return hash;
  }

}


