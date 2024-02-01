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
@Table(name="tc_mantic_conteos")
public class TcManticConteosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="conteos")
  private String conteos;
  @Column (name="fecha")
  private String fecha;
  @Column (name="articulos")
  private Long articulos;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_conteo")
  private Long idConteo;
  @Column (name="procesado")
  private Timestamp procesado;
  @Column (name="id_referencia")
  private Long idReferencia;
  @Column (name="nombre")
  private String nombre;
  @Column (name="id_conteo_estatus")
  private Long idConteoEstatus;
  @Column (name="token")
  private String token;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticConteosDto() {
    this(new Long(-1L));
  }

  public TcManticConteosDto(Long key) {
    this(null, null, null, null, new Long(-1L), new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, null, new Long(-1L), new Long(-1L));
    setKey(key);
  }

  public TcManticConteosDto(String conteos, String fecha, Long articulos, Long idUsuario, Long idConteo, Timestamp procesado, Long idReferencia, String nombre, Long idConteoEstatus, String token, Long idEmpresa, Long idAlmacen) {
    setConteos(conteos);
    setFecha(fecha);
    setArticulos(articulos);
    setIdUsuario(idUsuario);
    setIdConteo(idConteo);
    setProcesado(procesado);
    setIdReferencia(idReferencia);
    setNombre(nombre);
    setIdConteoEstatus(idConteoEstatus);
    setToken(token);
    setIdEmpresa(idEmpresa);
    setIdAlmacen(idAlmacen);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setConteos(String conteos) {
    this.conteos = conteos;
  }

  public String getConteos() {
    return conteos;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }

  public void setArticulos(Long articulos) {
    this.articulos = articulos;
  }

  public Long getArticulos() {
    return articulos;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdConteo(Long idConteo) {
    this.idConteo = idConteo;
  }

  public Long getIdConteo() {
    return idConteo;
  }

  public void setProcesado(Timestamp procesado) {
    this.procesado = procesado;
  }

  public Timestamp getProcesado() {
    return procesado;
  }

  public void setIdReferencia(Long idReferencia) {
    this.idReferencia = idReferencia;
  }

  public Long getIdReferencia() {
    return idReferencia;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setIdConteoEstatus(Long idConteoEstatus) {
    this.idConteoEstatus = idConteoEstatus;
  }

  public Long getIdConteoEstatus() {
    return idConteoEstatus;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
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
  	return getIdConteo();
  }

  @Override
  public void setKey(Long key) {
  	this.idConteo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getConteos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArticulos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConteo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProcesado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdConteoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getToken());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("conteos", getConteos());
		regresar.put("fecha", getFecha());
		regresar.put("articulos", getArticulos());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idConteo", getIdConteo());
		regresar.put("procesado", getProcesado());
		regresar.put("idReferencia", getIdReferencia());
		regresar.put("nombre", getNombre());
		regresar.put("idConteoEstatus", getIdConteoEstatus());
		regresar.put("token", getToken());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getConteos(), getFecha(), getArticulos(), getIdUsuario(), getIdConteo(), getProcesado(), getIdReferencia(), getNombre(), getIdConteoEstatus(), getToken(), getIdEmpresa(), getIdAlmacen(), getRegistro()
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
    regresar.append("idConteo~");
    regresar.append(getIdConteo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdConteo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticConteosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdConteo()!= null && getIdConteo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticConteosDto other = (TcManticConteosDto) obj;
    if (getIdConteo() != other.idConteo && (getIdConteo() == null || !getIdConteo().equals(other.idConteo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdConteo() != null ? getIdConteo().hashCode() : 0);
    return hash;
  }

}


