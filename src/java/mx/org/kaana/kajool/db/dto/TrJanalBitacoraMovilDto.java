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
@Table(name="tr_janal_bitacora_movil")
public class TrJanalBitacoraMovilDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_bitacora_movil")
  private Long idBitacoraMovil;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="cuestionario")
  private String cuestionario;
  @Column (name="registro_integracion")
  private Timestamp registroIntegracion;
  @Column (name="id_estatus")
  private Long idEstatus;
  @Column (name="cuenta")
  private String cuenta;
  @Column (name="id_muestra")
  private Long idMuestra;
  @Column (name="visitas")
  private String visitas;

  public TrJanalBitacoraMovilDto() {
    this(new Long(-1L));
  }

  public TrJanalBitacoraMovilDto(Long key) {
    this(new Long(-1L), null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, null);
    setKey(key);
  }

  public TrJanalBitacoraMovilDto(Long idBitacoraMovil, String cuestionario, Timestamp registroIntegracion, Long idEstatus, String cuenta, Long idMuestra, String visitas) {
    setIdBitacoraMovil(idBitacoraMovil);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setCuestionario(cuestionario);
    setRegistroIntegracion(registroIntegracion);
    setIdEstatus(idEstatus);
    setCuenta(cuenta);
    setIdMuestra(idMuestra);
    setVisitas(visitas);
  }
	
  public void setIdBitacoraMovil(Long idBitacoraMovil) {
    this.idBitacoraMovil = idBitacoraMovil;
  }

  public Long getIdBitacoraMovil() {
    return idBitacoraMovil;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setCuestionario(String cuestionario) {
    this.cuestionario = cuestionario;
  }

  public String getCuestionario() {
    return cuestionario;
  }

  public void setRegistroIntegracion(Timestamp registroIntegracion) {
    this.registroIntegracion = registroIntegracion;
  }

  public Timestamp getRegistroIntegracion() {
    return registroIntegracion;
  }

  public void setIdEstatus(Long idEstatus) {
    this.idEstatus = idEstatus;
  }

  public Long getIdEstatus() {
    return idEstatus;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  public String getCuenta() {
    return cuenta;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setVisitas(String visitas) {
    this.visitas = visitas;
  }

  public String getVisitas() {
    return visitas;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdBitacoraMovil();
  }

  @Override
  public void setKey(Long key) {
  	this.idBitacoraMovil = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdBitacoraMovil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuestionario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistroIntegracion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVisitas());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idBitacoraMovil", getIdBitacoraMovil());
		regresar.put("registro", getRegistro());
		regresar.put("cuestionario", getCuestionario());
		regresar.put("registroIntegracion", getRegistroIntegracion());
		regresar.put("idEstatus", getIdEstatus());
		regresar.put("cuenta", getCuenta());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("visitas", getVisitas());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdBitacoraMovil(), getRegistro(), getCuestionario(), getRegistroIntegracion(), getIdEstatus(), getCuenta(), getIdMuestra(), getVisitas()
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
    regresar.append("idBitacoraMovil~");
    regresar.append(getIdBitacoraMovil());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdBitacoraMovil());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalBitacoraMovilDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdBitacoraMovil()!= null && getIdBitacoraMovil()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalBitacoraMovilDto other = (TrJanalBitacoraMovilDto) obj;
    if (getIdBitacoraMovil() != other.idBitacoraMovil && (getIdBitacoraMovil() == null || !getIdBitacoraMovil().equals(other.idBitacoraMovil))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdBitacoraMovil() != null ? getIdBitacoraMovil().hashCode() : 0);
    return hash;
  }

}


