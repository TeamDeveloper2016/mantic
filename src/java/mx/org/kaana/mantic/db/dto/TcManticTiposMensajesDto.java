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
@Table(name="tc_mantic_tipos_mensajes")
public class TcManticTiposMensajesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_mensaje")
  private Long idTipoMensaje;
  @Column (name="nombres")
  private String nombres;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposMensajesDto() {
    this(new Long(-1L));
  }

  public TcManticTiposMensajesDto(Long key) {
    this(null, new Long(-1L), null);
    setKey(key);
  }

  public TcManticTiposMensajesDto(String descripcion, Long idTipoMensaje, String nombres) {
    setDescripcion(descripcion);
    setIdTipoMensaje(idTipoMensaje);
    setNombres(nombres);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setIdTipoMensaje(Long idTipoMensaje) {
    this.idTipoMensaje = idTipoMensaje;
  }

  public Long getIdTipoMensaje() {
    return idTipoMensaje;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getNombres() {
    return nombres;
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
  	return getIdTipoMensaje();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoMensaje = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMensaje());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombres());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("idTipoMensaje", getIdTipoMensaje());
		regresar.put("nombres", getNombres());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getIdTipoMensaje(), getNombres(), getRegistro()
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
    regresar.append("idTipoMensaje~");
    regresar.append(getIdTipoMensaje());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoMensaje());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposMensajesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoMensaje()!= null && getIdTipoMensaje()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposMensajesDto other = (TcManticTiposMensajesDto) obj;
    if (getIdTipoMensaje() != other.idTipoMensaje && (getIdTipoMensaje() == null || !getIdTipoMensaje().equals(other.idTipoMensaje))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoMensaje() != null ? getIdTipoMensaje().hashCode() : 0);
    return hash;
  }

}


