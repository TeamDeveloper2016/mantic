package mx.org.kaana.kajool.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name="tr_janal_movimientos")
public class TrJanalMovimientosDto implements IBaseDto, Serializable{

  private static final long serialVersionUID = -5865689845526761127L;

  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column (name="ID_MOVIMIENTO")
  private Long idMovimiento;
  @Column (name="ID_MUESTRA")
  private Long idMuestra;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="ID_ESTATUS")
  private Long idEstatus;
  @Column (name="REGISTRO")
  private Timestamp registro;
  @Column (name="ID_SUPERVISA")
  private Long idSupervisa;
  @Column (name="OBSERVACIONES")
  private String observaciones;

  public TrJanalMovimientosDto() {
    this(-1L);
  }

  public TrJanalMovimientosDto(Long idMovimiento) {
    this(idMovimiento, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null);
  }

  public TrJanalMovimientosDto(Long idMovimiento, Long idMuestra, Long idUsuario, Long idEstatus, Timestamp registro, Long idSupervisa, String observaciones) {
    setIdMovimiento(idMovimiento);
    setIdMuestra(idMuestra);
    setIdUsuario(idUsuario);
    setIdEstatus(idEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdSupervisa(idSupervisa);
    setObservaciones(observaciones);
  }

  public Long getIdMovimiento() {
    return idMovimiento;
  }

  public void setIdMovimiento(Long idMovimiento) {
    this.idMovimiento = idMovimiento;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdEstatus() {
    return idEstatus;
  }

  public void setIdEstatus(Long idEstatus) {
    this.idEstatus = idEstatus;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Long getIdSupervisa() {
    return idSupervisa;
  }

  public void setIdSupervisa(Long idSupervisa) {
    this.idSupervisa = idSupervisa;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  @Override
  public Long getKey() {
    return idMovimiento;
  }

  @Override
  public void setKey(Long key) {
    this.idMovimiento= key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdMovimiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSupervisa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map<String, Object> toMap() {
    Map regresar = new HashMap();
		regresar.put("idMovimiento", getIdMovimiento());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEstatus", getIdEstatus());
		regresar.put("registro", getRegistro());
		regresar.put("idSupervisa", getIdSupervisa());
		regresar.put("observaciones", getObservaciones());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdMovimiento(), getIdMuestra(), getIdUsuario(), getIdEstatus(), getRegistro(), getIdSupervisa(), getObservaciones()
    };
    return regresar;
  }

  @Override
  public boolean isValid() {
    return getIdMovimiento()!= null && getIdMovimiento()!=-1L;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idMovimiento~");
    regresar.append(getIdMovimiento());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdMovimiento());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalMovimientosDto.class;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrJanalMovimientosDto other = (TrJanalMovimientosDto) obj;
    if (getIdMovimiento()!= other.idMovimiento && (getIdMovimiento()== null || !getIdMovimiento().equals(other.idMovimiento))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMovimiento()!= null ? getIdMovimiento().hashCode() : 0);
    return hash;
  }
}
