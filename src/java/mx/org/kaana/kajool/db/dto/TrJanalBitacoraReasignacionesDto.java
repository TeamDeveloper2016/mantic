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
 *@date 12/10/2016
 *@time 10:16:15 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@Entity
@Table(name="tr_janal_bitacora_reasignaciones")
public class TrJanalBitacoraReasignacionesDto implements IBaseDto, Serializable{

  private static final long serialVersionUID = 5757403667153752921L;

  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column (name="ID_BITACORA_REASIGNACION")
  private Long idBitacoraReasignacion;
  @Column (name="ID_USUARIO")
  private Long idUsuario;
  @Column (name="ID_USUARIO_ANT")
  private Long idUsuarioAnt;
  @Column (name="ID_USUARIO_ACT")
  private Long idUsuarioAct;
  @Column (name="ID_MUESTRA")
  private Long idMuestra;
  @Column (name="ID_TIPO_MOVIMIENTO")
  private Long idTipoMovimiento;
  @Column (name="REGISTRO")
  private Timestamp registro;

  public TrJanalBitacoraReasignacionesDto() {
    this(-1L);
  }

  public TrJanalBitacoraReasignacionesDto(Long idBitacoraReasignacion) {
    this(idBitacoraReasignacion, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }

  public TrJanalBitacoraReasignacionesDto(Long idBitacoraReasignacion, Long idUsuario, Long idUsuarioAnt, Long idUsuarioAct, Long idMuestra, Long idTipoMovimiento, Timestamp registro) {
    setIdBitacoraReasignacion(idBitacoraReasignacion);
    setIdUsuario(idUsuario);
    setIdUsuarioAnt(idUsuarioAnt);
    setIdUsuarioAct(idUsuarioAct);
    setIdMuestra(idMuestra);
    setIdTipoMovimiento(idTipoMovimiento);
    setRegistro(registro);
  }

  public Long getIdBitacoraReasignacion() {
    return idBitacoraReasignacion;
  }

  public void setIdBitacoraReasignacion(Long idBitacoraReasignacion) {
    this.idBitacoraReasignacion = idBitacoraReasignacion;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuarioAnt() {
    return idUsuarioAnt;
  }

  public void setIdUsuarioAnt(Long idUsuarioAnt) {
    this.idUsuarioAnt = idUsuarioAnt;
  }

  public Long getIdUsuarioAct() {
    return idUsuarioAct;
  }

  public void setIdUsuarioAct(Long idUsuarioAct) {
    this.idUsuarioAct = idUsuarioAct;
  }

  public Long getIdMuestra() {
    return idMuestra;
  }

  public void setIdMuestra(Long idMuestra) {
    this.idMuestra = idMuestra;
  }

  public Long getIdTipoMovimiento() {
    return idTipoMovimiento;
  }

  public void setIdTipoMovimiento(Long idTipoMovimiento) {
    this.idTipoMovimiento = idTipoMovimiento;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  @Override
  public Long getKey() {
    return this.idBitacoraReasignacion;
  }

  @Override
  public void setKey(Long key) {
    this.idBitacoraReasignacion= key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdBitacoraReasignacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuarioAnt());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuarioAct());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMuestra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMovimiento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map<String, Object> toMap() {
    Map regresar = new HashMap();
		regresar.put("idBitacoraReasignacion", getIdBitacoraReasignacion());		
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idUsuarioAnt", getIdUsuarioAnt());
		regresar.put("idUsuarioAct", getIdUsuarioAct());
		regresar.put("idMuestra", getIdMuestra());
		regresar.put("idTipoMovimiento", getIdTipoMovimiento());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdBitacoraReasignacion(), getIdUsuario(), getIdUsuarioAnt(), getIdUsuarioAct(), getIdMuestra(), getIdTipoMovimiento(), getRegistro()
    };
    return regresar;
  }

  @Override
  public boolean isValid() {
    return getIdBitacoraReasignacion()!= null && getIdBitacoraReasignacion()!=-1L;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idBitacoraReasignacion~");
    regresar.append(getIdBitacoraReasignacion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdBitacoraReasignacion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrJanalBitacoraReasignacionesDto.class;
  }
}
