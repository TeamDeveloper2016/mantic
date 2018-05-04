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
@Table(name="tc_mantic_tipos_monedas")
public class TcManticTiposMonedasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="simbolo")
  private String simbolo;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_tipo_moneda")
  private Long idTipoMoneda;
  @Column (name="siglas")
  private String siglas;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticTiposMonedasDto() {
    this(new Long(-1L));
  }

  public TcManticTiposMonedasDto(Long key) {
    this(null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticTiposMonedasDto(String descripcion, String simbolo, Long idUsuario, Long idTipoMoneda, String siglas, String nombre) {
    setDescripcion(descripcion);
    setSimbolo(simbolo);
    setIdUsuario(idUsuario);
    setIdTipoMoneda(idTipoMoneda);
    setSiglas(siglas);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setSimbolo(String simbolo) {
    this.simbolo = simbolo;
  }

  public String getSimbolo() {
    return simbolo;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdTipoMoneda(Long idTipoMoneda) {
    this.idTipoMoneda = idTipoMoneda;
  }

  public Long getIdTipoMoneda() {
    return idTipoMoneda;
  }

  public void setSiglas(String siglas) {
    this.siglas = siglas;
  }

  public String getSiglas() {
    return siglas;
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
  	return getIdTipoMoneda();
  }

  @Override
  public void setKey(Long key) {
  	this.idTipoMoneda = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSimbolo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoMoneda());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSiglas());
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
		regresar.put("descripcion", getDescripcion());
		regresar.put("simbolo", getSimbolo());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTipoMoneda", getIdTipoMoneda());
		regresar.put("siglas", getSiglas());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescripcion(), getSimbolo(), getIdUsuario(), getIdTipoMoneda(), getSiglas(), getNombre(), getRegistro()
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
    regresar.append("idTipoMoneda~");
    regresar.append(getIdTipoMoneda());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTipoMoneda());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTiposMonedasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTipoMoneda()!= null && getIdTipoMoneda()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTiposMonedasDto other = (TcManticTiposMonedasDto) obj;
    if (getIdTipoMoneda() != other.idTipoMoneda && (getIdTipoMoneda() == null || !getIdTipoMoneda().equals(other.idTipoMoneda))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTipoMoneda() != null ? getIdTipoMoneda().hashCode() : 0);
    return hash;
  }

}


