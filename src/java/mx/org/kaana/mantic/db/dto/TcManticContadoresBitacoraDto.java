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
@Table(name="tc_mantic_contadores_bitacora")
public class TcManticContadoresBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="justificacion")
  private String justificacion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_contador")
  private Long idContador;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_contador_bitacora")
  private Long idContadorBitacora;
  @Column (name="id_contador_estatus")
  private Long idContadorEstatus;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticContadoresBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticContadoresBitacoraDto(Long key) {
    this(null, null, null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticContadoresBitacoraDto(String justificacion, Long idUsuario, Long idContador, Long idContadorBitacora, Long idContadorEstatus) {
    setJustificacion(justificacion);
    setIdUsuario(idUsuario);
    setIdContador(idContador);
    setIdContadorBitacora(idContadorBitacora);
    setIdContadorEstatus(idContadorEstatus);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setJustificacion(String justificacion) {
    this.justificacion = justificacion;
  }

  public String getJustificacion() {
    return justificacion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdContador(Long idContador) {
    this.idContador = idContador;
  }

  public Long getIdContador() {
    return idContador;
  }

  public void setIdContadorBitacora(Long idContadorBitacora) {
    this.idContadorBitacora = idContadorBitacora;
  }

  public Long getIdContadorBitacora() {
    return idContadorBitacora;
  }

  public void setIdContadorEstatus(Long idContadorEstatus) {
    this.idContadorEstatus = idContadorEstatus;
  }

  public Long getIdContadorEstatus() {
    return idContadorEstatus;
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
  	return getIdContadorBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idContadorBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getJustificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdContador());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdContadorBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdContadorEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("justificacion", getJustificacion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idContador", getIdContador());
		regresar.put("idContadorBitacora", getIdContadorBitacora());
		regresar.put("idContadorEstatus", getIdContadorEstatus());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getJustificacion(), getIdUsuario(), getIdContador(), getIdContadorBitacora(), getIdContadorEstatus(), getRegistro()
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
    regresar.append("idContadorBitacora~");
    regresar.append(getIdContadorBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdContadorBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticContadoresBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdContadorBitacora()!= null && getIdContadorBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticContadoresBitacoraDto other = (TcManticContadoresBitacoraDto) obj;
    if (getIdContadorBitacora() != other.idContadorBitacora && (getIdContadorBitacora() == null || !getIdContadorBitacora().equals(other.idContadorBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdContadorBitacora() != null ? getIdContadorBitacora().hashCode() : 0);
    return hash;
  }

}


