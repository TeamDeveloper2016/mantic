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
@Table(name="tc_mantic_trabajos")
public class TcManticTrabajosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="codigo")
  private String codigo;
  @Column (name="precio")
  private Double precio;
  @Column (name="iva")
  private Double iva;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_trabajo")
  private Long idTrabajo;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_vigente")
  private Long idVigente;
  @Column (name="nombre")
  private String nombre;
  @Column (name="sat")
  private String sat;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="herramienta")
  private String herramienta;

  public TcManticTrabajosDto() {
    this(new Long(-1L));
  }

  public TcManticTrabajosDto(Long key) {
    this(null, null, null, null, null, new Long(-1L), null, null, null, null, null);
    setKey(key);
  }

  public TcManticTrabajosDto(String descripcion, String codigo, Double precio, Double iva, Long idUsuario, Long idTrabajo, Long idEmpresa, Long idVigente, String nombre, String sat, String herramienta) {
    setDescripcion(descripcion);
    setCodigo(codigo);
    setPrecio(precio);
    setIva(iva);
    setIdUsuario(idUsuario);
    setIdTrabajo(idTrabajo);
    setIdEmpresa(idEmpresa);
    setIdVigente(idVigente);
    setNombre(nombre);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		this.sat= sat;
		this.herramienta= herramienta;
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Double getPrecio() {
    return precio;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdTrabajo(Long idTrabajo) {
    this.idTrabajo = idTrabajo;
  }

  public Long getIdTrabajo() {
    return idTrabajo;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdVigente(Long idVigente) {
    this.idVigente = idVigente;
  }

  public Long getIdVigente() {
    return idVigente;
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

	public String getSat() {
		return sat;
	}

	public void setSat(String sat) {
		this.sat = sat;
	}	

	public String getHerramienta() {
		return herramienta;
	}

	public void setHerramienta(String herramienta) {
		this.herramienta=herramienta;
	}
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdTrabajo();
  }

  @Override
  public void setKey(Long key) {
  	this.idTrabajo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTrabajo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVigente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getHerramienta());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("codigo", getCodigo());
		regresar.put("precio", getPrecio());
		regresar.put("iva", getIva());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idTrabajo", getIdTrabajo());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idVigente", getIdVigente());
		regresar.put("nombre", getNombre());
		regresar.put("registro", getRegistro());
		regresar.put("sat", getSat());
		regresar.put("herramienta", getHerramienta());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getDescripcion(), getCodigo(), getPrecio(), getIva(), getIdUsuario(), getIdTrabajo(), getIdEmpresa(), getIdVigente(), getNombre(), getRegistro(), getSat(), getHerramienta()
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
    regresar.append("idTrabajo~");
    regresar.append(getIdTrabajo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdTrabajo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticTrabajosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdTrabajo()!= null && getIdTrabajo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticTrabajosDto other = (TcManticTrabajosDto) obj;
    if (getIdTrabajo() != other.idTrabajo && (getIdTrabajo() == null || !getIdTrabajo().equals(other.idTrabajo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdTrabajo() != null ? getIdTrabajo().hashCode() : 0);
    return hash;
  }
}