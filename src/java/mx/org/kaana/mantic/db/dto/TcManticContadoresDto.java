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
@Table(name="tc_mantic_contadores")
public class TcManticContadoresDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="semilla")
  private String semilla;
  @Column (name="contadores")
  private String contadores;
  @Column (name="articulos")
  private Long articulos;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_contador")
  private Long idContador;
  @Column (name="nombre")
  private String nombre;
  @Column (name="version")
  private String version;
  @Column (name="token")
  private String token;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="fecha")
  private String fecha;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_almacen")
  private Long idAlmacen;
  @Column (name="procesado")
  private Timestamp procesado;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_referencia")
  private Long idReferencia;
  @Column (name="id_contador_estatus")
  private Long idContadorEstatus;
  @Column (name="id_trabaja")
  private Long idTrabaja;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="orden")
  private Long orden;

  public TcManticContadoresDto() {
    this(new Long(-1L));
  }

  public TcManticContadoresDto(Long key) {
    this(null, null, null, new Long(-1L), null, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, 1L, null, null, null, null);
    setKey(key);
  }

  public TcManticContadoresDto(String semilla, String contadores, Long articulos, Long idContador, String nombre, String version, String token, String fecha, Long idUsuario, Long idAlmacen, Timestamp procesado, Long idEmpresa, Long idReferencia, Long idContadorEstatus, Long idTrabaja, String consecutivo, Long ejercicio, Long orden) {
    setSemilla(semilla);
    setContadores(contadores);
    setArticulos(articulos);
    setIdContador(idContador);
    setNombre(nombre);
    setVersion(version);
    setToken(token);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setFecha(fecha);
    setIdUsuario(idUsuario);
    setIdAlmacen(idAlmacen);
    setProcesado(procesado);
    setIdEmpresa(idEmpresa);
    setIdReferencia(idReferencia);
    setIdContadorEstatus(idContadorEstatus);
    setIdTrabaja(idTrabaja);
    setConsecutivo(consecutivo);
    setEjercicio(ejercicio);
    setOrden(orden);
  }
	
  public void setSemilla(String semilla) {
    this.semilla = semilla;
  }

  public String getSemilla() {
    return semilla;
  }

  public void setContadores(String contadores) {
    this.contadores = contadores;
  }

  public String getContadores() {
    return contadores;
  }

  public void setArticulos(Long articulos) {
    this.articulos = articulos;
  }

  public Long getArticulos() {
    return articulos;
  }

  public void setIdContador(Long idContador) {
    this.idContador = idContador;
  }

  public Long getIdContador() {
    return idContador;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setProcesado(Timestamp procesado) {
    this.procesado = procesado;
  }

  public Timestamp getProcesado() {
    return procesado;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdReferencia(Long idReferencia) {
    this.idReferencia = idReferencia;
  }

  public Long getIdReferencia() {
    return idReferencia;
  }

  public void setIdContadorEstatus(Long idContadorEstatus) {
    this.idContadorEstatus = idContadorEstatus;
  }

  public Long getIdContadorEstatus() {
    return idContadorEstatus;
  }

  public void setIdTrabaja(Long idTrabaja) {
    this.idTrabaja = idTrabaja;
  }

  public Long getIdTrabaja() {
    return idTrabaja;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getOrden() {
    return orden;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdContador();
  }

  @Override
  public void setKey(Long key) {
  	this.idContador = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getSemilla());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getContadores());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArticulos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdContador());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVersion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getToken());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAlmacen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProcesado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdReferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdContadorEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTrabaja());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("semilla", getSemilla());
		regresar.put("contadores", getContadores());
		regresar.put("articulos", getArticulos());
		regresar.put("idContador", getIdContador());
		regresar.put("nombre", getNombre());
		regresar.put("version", getVersion());
		regresar.put("token", getToken());
		regresar.put("registro", getRegistro());
		regresar.put("fecha", getFecha());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idAlmacen", getIdAlmacen());
		regresar.put("procesado", getProcesado());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idReferencia", getIdReferencia());
		regresar.put("idContadorEstatus", getIdContadorEstatus());
		regresar.put("idTrabaja", getIdTrabaja());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("orden", getOrden());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getSemilla(), getContadores(), getArticulos(), getIdContador(), getNombre(), getVersion(), getToken(), getRegistro(), getFecha(), getIdUsuario(), getIdAlmacen(), getProcesado(), getIdEmpresa(), getIdReferencia(), getIdContadorEstatus(), getIdTrabaja(), getConsecutivo(), getEjercicio(), getOrden()
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
    regresar.append("idContador~");
    regresar.append(getIdContador());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdContador());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticContadoresDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdContador()!= null && getIdContador()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticContadoresDto other = (TcManticContadoresDto) obj;
    if (getIdContador() != other.idContador && (getIdContador() == null || !getIdContador().equals(other.idContador))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdContador() != null ? getIdContador().hashCode() : 0);
    return hash;
  }

}


