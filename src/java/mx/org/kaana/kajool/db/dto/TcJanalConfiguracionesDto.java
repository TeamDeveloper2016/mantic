package mx.org.kaana.kajool.db.dto;

import mx.org.kaana.libs.reflection.Methods;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Entity
@Table(name="tc_janal_configuraciones")
public class TcJanalConfiguracionesDto implements IBaseDto, Serializable {

  private static final long serialVersionUID = -4492163580509111843L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idConfiguracion_sequence")
  //@SequenceGenerator(name="idConfiguracion_sequence",sequenceName="SEQ_TC_JANAL_CONFIGURACIONES", allocationSize=1)
  @Column(name="ID_CONFIGURACION")
  private Long idConfiguracion;
  @Column(name="VALOR")
  private String valor;
	@Column(name="DESCRIPCION")
	private String descripcion;
	@Column(name="REGISTRO")
	private Timestamp registro;
	@Column(name="ID_USUARIO")
	private Long idUsuario;
  @Column(name="LLAVE")
  private String llave;

	public TcJanalConfiguracionesDto() {
		this(new Long(-1L));
	}

  public TcJanalConfiguracionesDto(Long key) {
		this(null, new Long(-1L), null, null, null, null);
    setKey(key);
	}

	public TcJanalConfiguracionesDto(String llave, Long idConfiguracion, String valor, String descripcion, Timestamp registro, Long idUsuario) {
		setLlave(llave);
		setIdConfiguracion(idConfiguracion);
		setValor(valor);
		setDescripcion(descripcion);
		setIdUsuario(idUsuario);
		setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}

  public Timestamp getRegistro() {
    return registro;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public void setLlave(String llave) {
		this.llave = llave;
	}

	public String getLlave() {
		return llave;
	}

	public void setIdConfiguracion(Long idConfiguracion) {
		this.idConfiguracion = idConfiguracion;
	}

	public Long getIdConfiguracion() {
		return idConfiguracion;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion=descripcion;
	}

  @Transient
  @Override
	public Long getKey() {
  	return getIdConfiguracion();
  }

  @Transient
  @Override
  public void setKey(Long key) {
  	this.idConfiguracion = key;
  }

  @Transient
  @Override
  public boolean isValid() {
  	return getIdConfiguracion()!= null && getIdConfiguracion()!=-1L;
  }

  @Transient
  @Override
  public Map<String,Object> toMap() {
    Map <String,Object> regresar= new HashMap<String,Object>();
    regresar.put("llave",getLlave());
    regresar.put("idConfiguracion",getIdConfiguracion());
    regresar.put("valor",getValor());
		regresar.put("descripcion", getDescripcion());
		regresar.put("registro", getRegistro());
		regresar.put("idUsuario", getIdUsuario());
    return regresar;
  }

  @Transient
  @Override
  public Object[] toArray() {
    Object[]  regresar= new Object[]{
    getLlave(), getIdConfiguracion(), getValor(), getDescripcion(), getRegistro(), getIdUsuario()};
    return regresar;
  }

  @Transient
  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Transient
  @Override
  public String toAllKeys() {
   StringBuilder regresar= new StringBuilder();
   regresar.append("|");
   regresar.append("idConfiguracion~");
   regresar.append(getIdConfiguracion());
   regresar.append("|");
   regresar.append(getRegistro());
   regresar.append("|");
   regresar.append(getIdUsuario());
   regresar.append("|");
   return regresar.toString();
  }

  @Transient
  @Override
  public String toKeys() {
   StringBuilder regresar= new StringBuilder();
   regresar.append(getIdConfiguracion());
   return regresar.toString();
  }

  @Transient
  @Override
  public Class toHbmClass() {
    return TcJanalConfiguracionesDto.class;
  }

  @Transient
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalConfiguracionesDto other = (TcJanalConfiguracionesDto)obj;
    if (getIdConfiguracion() != other.idConfiguracion &&
        (getIdConfiguracion() == null ||
         !getIdConfiguracion().equals(other.idConfiguracion))) {
      return false;
    }
    return true;
  }

  @Transient
  @Override
  public int hashCode() {
    int hash = 7;
    hash =
        67 * hash + (getIdConfiguracion() != null ? getIdConfiguracion().hashCode() :
                     0);
    return hash;
  }

}
