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
@Table(name="tc_mantic_articulos_especificaciones")
public class TcManticArticulosEspecificacionesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="valor")
  private String valor;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_especificacion")
  private Long idArticuloEspecificacion;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="nombre")
  private String nombre;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticArticulosEspecificacionesDto() {
    this(new Long(-1L));
  }

  public TcManticArticulosEspecificacionesDto(Long key) {
    this(null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticArticulosEspecificacionesDto(Long idUsuario, String valor, Long idArticuloEspecificacion, Long idArticulo, String nombre) {
    setIdUsuario(idUsuario);
    setValor(valor);
    setIdArticuloEspecificacion(idArticuloEspecificacion);
    setIdArticulo(idArticulo);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public String getValor() {
    return valor;
  }

  public void setIdArticuloEspecificacion(Long idArticuloEspecificacion) {
    this.idArticuloEspecificacion = idArticuloEspecificacion;
  }

  public Long getIdArticuloEspecificacion() {
    return idArticuloEspecificacion;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
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
  	return getIdArticuloEspecificacion();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloEspecificacion = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getValor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloEspecificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
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
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("valor", getValor());
		regresar.put("idArticuloEspecificacion", getIdArticuloEspecificacion());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdUsuario(), getValor(), getIdArticuloEspecificacion(), getIdArticulo(), getNombre(), getRegistro()
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
    regresar.append("idArticuloEspecificacion~");
    regresar.append(getIdArticuloEspecificacion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloEspecificacion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticArticulosEspecificacionesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloEspecificacion()!= null && getIdArticuloEspecificacion()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticArticulosEspecificacionesDto other = (TcManticArticulosEspecificacionesDto) obj;
    if (getIdArticuloEspecificacion() != other.idArticuloEspecificacion && (getIdArticuloEspecificacion() == null || !getIdArticuloEspecificacion().equals(other.idArticuloEspecificacion))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloEspecificacion() != null ? getIdArticuloEspecificacion().hashCode() : 0);
    return hash;
  }

}


