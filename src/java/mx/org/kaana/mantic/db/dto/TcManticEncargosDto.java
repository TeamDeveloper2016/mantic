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
@Table(name="tc_mantic_encargos")
public class TcManticEncargosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="codigo")
  private String codigo;
  @Column (name="costo")
  private Double costo;
  @Column (name="iva")
  private Double iva;
  @Column (name="sat")
  private String sat;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_encargo")
  private Long idEncargo;
  @Column (name="id_vigente")
  private Long idVigente;
  @Column (name="nombre")
  private String nombre;
  @Column (name="id_descontinuado")
  private Long idDescontinuado;
  @Column (name="linea")
  private String linea;
  @Column (name="actualizado")
  private Timestamp actualizado;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="precio")
  private Double precio;

  public TcManticEncargosDto() {
    this(new Long(-1L));
  }

  public TcManticEncargosDto(Long key) {
    this(null, null, null, null, null, null, new Long(-1L), null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), 0D);
    setKey(key);
  }

  public TcManticEncargosDto(Long idEmpresa, String codigo, Double costo, Double iva, String sat, Long idUsuario, Long idEncargo, Long idVigente, String nombre, Long idDescontinuado, String linea, Timestamp actualizado, Double precio) {
    this.idEmpresa= idEmpresa;
    setCodigo(codigo);
    setCosto(costo);
    setIva(iva);
    setSat(sat);
    setIdUsuario(idUsuario);
    setIdEncargo(idEncargo);
    setIdVigente(idVigente);
    setNombre(nombre);
    setIdDescontinuado(idDescontinuado);
    setLinea(linea);
    setActualizado(actualizado);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    this.precio= precio;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setSat(String sat) {
    this.sat = sat;
  }

  public String getSat() {
    return sat;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdEncargo(Long idEncargo) {
    this.idEncargo = idEncargo;
  }

  public Long getIdEncargo() {
    return idEncargo;
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

  public void setIdDescontinuado(Long idDescontinuado) {
    this.idDescontinuado = idDescontinuado;
  }

  public Long getIdDescontinuado() {
    return idDescontinuado;
  }

  public void setLinea(String linea) {
    this.linea = linea;
  }

  public String getLinea() {
    return linea;
  }

  public void setActualizado(Timestamp actualizado) {
    this.actualizado = actualizado;
  }

  public Timestamp getActualizado() {
    return actualizado;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public Double getPrecio() {
    return precio;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdEncargo();
  }

  @Override
  public void setKey(Long key) {
  	this.idEncargo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEncargo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVigente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDescontinuado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLinea());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getActualizado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("codigo", getCodigo());
		regresar.put("costo", getCosto());
		regresar.put("iva", getIva());
		regresar.put("sat", getSat());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEncargo", getIdEncargo());
		regresar.put("idVigente", getIdVigente());
		regresar.put("nombre", getNombre());
		regresar.put("idDescontinuado", getIdDescontinuado());
		regresar.put("linea", getLinea());
		regresar.put("actualizado", getActualizado());
		regresar.put("registro", getRegistro());
		regresar.put("precio", getPrecio());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdEmpresa(), getCodigo(), getCosto(), getIva(), getSat(), getIdUsuario(), getIdEncargo(), getIdVigente(), getNombre(), getIdDescontinuado(), getLinea(), getActualizado(), getRegistro(), getPrecio()
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
    regresar.append("idEncargo~");
    regresar.append(getIdEncargo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdEncargo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticEncargosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdEncargo()!= null && getIdEncargo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticEncargosDto other = (TcManticEncargosDto) obj;
    if (getIdEncargo() != other.idEncargo && (getIdEncargo() == null || !getIdEncargo().equals(other.idEncargo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdEncargo() != null ? getIdEncargo().hashCode() : 0);
    return hash;
  }

}


