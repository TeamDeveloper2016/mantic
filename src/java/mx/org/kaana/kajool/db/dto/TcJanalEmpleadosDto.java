package mx.org.kaana.kajool.db.dto;

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

@Entity
@Table(name="tc_janal_empleados")
public class TcJanalEmpleadosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idPerfil_sequence")
  //@SequenceGenerator(name="idPerfil_sequence",sequenceName="SEQ_TC_JANAL_PERFILES" , allocationSize=1 )
  @Column (name="ID_EMPLEADO")
  private Long idEmpleado;
  @Column (name="NOMBRES")
  private String nombres;
  @Column (name="PRIMER_APELLIDO")
  private String primerApellido;
  @Column (name="SEGUNDO_APELLIDO")
  private String segundoApellido;
  @Column (name="CURP")
  private String curp;
	@Column (name="ID_SEXO")
  private Long idSexo;
	@Column (name="CONTRASENIA")
  private String contrasenia;
	@Column (name="CUENTA")
  private String cuenta;
  @Column (name="ESTILO")
  private String estilo;
	@Column (name="CORREO")
  private String correo;
	@Column (name="REGISTRO")
  private Timestamp registro;	
	
  public TcJanalEmpleadosDto() {
    this(new Long(-1L));
  }

  public TcJanalEmpleadosDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcJanalEmpleadosDto(Long idEmpleado, String nombres, String primerApellido, String segundoApellido, String curp, Long idSexo, String correo, String estilo, String cuenta, String contrasenia) {
    setIdEmpleado(idEmpleado);
		setNombres(nombres);
		setPrimerApellido(primerApellido);
		setSegundoApellido(segundoApellido);
		setIdSexo(idSexo);
		setCurp(curp);
		setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		setCuenta(cuenta);
		setContrasenia(contrasenia);
		setCorreo(correo);
		setEstilo(estilo);
  }

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia=contrasenia;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta=cuenta;
	}

	public String getEstilo() {
		return estilo;
	}

	public void setEstilo(String estilo) {
		this.estilo=estilo;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo=correo;
	}

	public Timestamp getRegistro() {
		return registro;
	}

	public void setRegistro(Timestamp registro) {
		this.registro=registro;
	}

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado=idEmpleado;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres=nombres;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido=primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido=segundoApellido;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp=curp;
	}

	public Long getIdSexo() {
		return idSexo;
	}

	public void setIdSexo(Long idSexo) {
		this.idSexo=idSexo;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdEmpleado();
  }

  @Override
  public void setKey(Long key) {
  	this.idEmpleado = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdEmpleado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombres());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrimerApellido());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSegundoApellido());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCurp());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdSexo());				
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCuenta());				
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getContrasenia());				
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCorreo());				
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEstilo());				
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());				
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idEmpleado", getIdEmpleado());
		regresar.put("nombres", getNombres());
		regresar.put("primerApellido", getPrimerApellido());
		regresar.put("segundoApellido", getSegundoApellido());
		regresar.put("curp", getCurp());
		regresar.put("idSexo", getIdSexo());
		regresar.put("cuenta", getCuenta());
		regresar.put("contrasenia", getContrasenia());
		regresar.put("correo", getCorreo());
		regresar.put("registro", getRegistro());
		regresar.put("estilo", getEstilo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdEmpleado(), getNombres(), getPrimerApellido(), getSegundoApellido(), getIdSexo(), getCurp(), getCuenta(), getContrasenia(), getEstilo(), getCorreo()
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
    regresar.append("idEmpleado~");
    regresar.append(getIdEmpleado());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEmpleado());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcJanalEmpleadosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEmpleado()!= null && getIdEmpleado()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcJanalEmpleadosDto other = (TcJanalEmpleadosDto) obj;
    if (getIdEmpleado() != other.idEmpleado && (getIdEmpleado() == null || !getIdEmpleado().equals(other.idEmpleado))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEmpleado() != null ? getIdEmpleado().hashCode() : 0);
    return hash;
  }

}
