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
@Table(name="tc_mantic_refacciones")
public class TcManticRefaccionesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="codigo")
  private String codigo;
  @Column (name="herramienta")
  private String herramienta;
  @Column (name="costo")
  private Double costo;
  @Column (name="sat")
  private String sat;
  @Column (name="nombre")
  private String nombre;
  @Column (name="id_descontinuado")
  private Long idDescontinuado;
  @Column (name="actualizado")
  private Timestamp actualizado;
  @Column (name="registro")
  private Timestamp registro;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_refaccion")
  private Long idRefaccion;
  @Column (name="iva")
  private Double iva;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="fabricante")
  private String fabricante;
  @Column (name="id_vigente")
  private Long idVigente;

  public TcManticRefaccionesDto() {
    this(new Long(-1L));
  }

  public TcManticRefaccionesDto(Long key) {
    this(null, null, null, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), new Long(-1L), null, null, null, null);
    setKey(key);
  }

  public TcManticRefaccionesDto(Long idEmpresa, Long idProveedor, String codigo, String herramienta, Double costo, String sat, String nombre, Long idDescontinuado, Timestamp actualizado, Long idRefaccion, Double iva, Long idUsuario, String fabricante, Long idVigente) {
    this.idEmpresa= idEmpresa;
    setIdProveedor(idProveedor);
    setCodigo(codigo);
    setHerramienta(herramienta);
    setCosto(costo);
    setSat(sat);
    setNombre(nombre);
    setIdDescontinuado(idDescontinuado);
    setActualizado(actualizado);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdRefaccion(idRefaccion);
    setIva(iva);
    setIdUsuario(idUsuario);
    setFabricante(fabricante);
    setIdVigente(idVigente);
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }
	
  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setHerramienta(String herramienta) {
    this.herramienta = herramienta;
  }

  public String getHerramienta() {
    return herramienta;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setSat(String sat) {
    this.sat = sat;
  }

  public String getSat() {
    return sat;
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

  public void setIdRefaccion(Long idRefaccion) {
    this.idRefaccion = idRefaccion;
  }

  public Long getIdRefaccion() {
    return idRefaccion;
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

  public void setFabricante(String fabricante) {
    this.fabricante = fabricante;
  }

  public String getFabricante() {
    return fabricante;
  }

  public void setIdVigente(Long idVigente) {
    this.idVigente = idVigente;
  }

  public Long getIdVigente() {
    return idVigente;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdRefaccion();
  }

  @Override
  public void setKey(Long key) {
  	this.idRefaccion = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getHerramienta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDescontinuado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getActualizado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRefaccion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFabricante());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVigente());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("codigo", getCodigo());
		regresar.put("herramienta", getHerramienta());
		regresar.put("costo", getCosto());
		regresar.put("sat", getSat());
		regresar.put("nombre", getNombre());
		regresar.put("idDescontinuado", getIdDescontinuado());
		regresar.put("actualizado", getActualizado());
		regresar.put("registro", getRegistro());
		regresar.put("idRefaccion", getIdRefaccion());
		regresar.put("iva", getIva());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("fabricante", getFabricante());
		regresar.put("idVigente", getIdVigente());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdEmpresa(), getIdProveedor(), getCodigo(), getHerramienta(), getCosto(), getSat(), getNombre(), getIdDescontinuado(), getActualizado(), getRegistro(), getIdRefaccion(), getIva(), getIdUsuario(), getFabricante(), getIdVigente()
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
    regresar.append("idRefaccion~");
    regresar.append(getIdRefaccion());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdRefaccion());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticRefaccionesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdRefaccion()!= null && getIdRefaccion()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticRefaccionesDto other = (TcManticRefaccionesDto) obj;
    if (getIdRefaccion() != other.idRefaccion && (getIdRefaccion() == null || !getIdRefaccion().equals(other.idRefaccion))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdRefaccion() != null ? getIdRefaccion().hashCode() : 0);
    return hash;
  }

}


