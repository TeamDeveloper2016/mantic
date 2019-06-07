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
@Table(name="tc_mantic_unidades_medidas")
public class TcManticUnidadesMedidasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="clave")
  private String clave;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_unidad_medida")
  private Long idUnidadMedida;
  @Column (name="nombre")
  private String nombre;
  @Column (name="descripcion")
  private String descripcion;
	@Column (name="id_usuario")
  private Long idUsuario;
	@Column (name="registro")
  private Timestamp registro;
	@Column (name="proporcion")
  private Long proporcion;

  public TcManticUnidadesMedidasDto() {
    this(new Long(-1L));
  }

  public TcManticUnidadesMedidasDto(Long key) {
    this(null, new Long(-1L), null, null, -1L, -1L);
    setKey(key);
  }

  public TcManticUnidadesMedidasDto(String clave, Long idUnidadMedida, String nombre, String descripcion, Long idUsuario, Long proporcion) {
    setClave(clave);
    setIdUnidadMedida(idUnidadMedida);
    setNombre(nombre);
		setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setIdUsuario(idUsuario);
		setDescripcion(descripcion);
		setProporcion(proporcion);
  }
	
  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }

  public void setIdUnidadMedida(Long idUnidadMedida) {
    this.idUnidadMedida = idUnidadMedida;
  }

  public Long getIdUnidadMedida() {
    return idUnidadMedida;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Timestamp getRegistro() {
		return registro;
	}

	public void setRegistro(Timestamp registro) {
		this.registro = registro;
	}

	public Long getProporcion() {
		return proporcion;
	}

	public void setProporcion(Long proporcion) {
		this.proporcion = proporcion;
	}	
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdUnidadMedida();
  }

  @Override
  public void setKey(Long key) {
  	this.idUnidadMedida = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProporcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("clave", getClave());
		regresar.put("idUnidadMedida", getIdUnidadMedida());
		regresar.put("nombre", getNombre());
		regresar.put("descripcion", getDescripcion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("registro", getRegistro());
		regresar.put("proporcion", getProporcion());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
			getClave(), getIdUnidadMedida(), getNombre(), getDescripcion(), getIdUsuario(), getRegistro(), getProporcion()
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
    regresar.append("idUnidadMedida~");
    regresar.append(getIdUnidadMedida());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdUnidadMedida());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticUnidadesMedidasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdUnidadMedida()!= null && getIdUnidadMedida()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticUnidadesMedidasDto other = (TcManticUnidadesMedidasDto) obj;
    if (getIdUnidadMedida() != other.idUnidadMedida && (getIdUnidadMedida() == null || !getIdUnidadMedida().equals(other.idUnidadMedida))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdUnidadMedida() != null ? getIdUnidadMedida().hashCode() : 0);
    return hash;
  }
}