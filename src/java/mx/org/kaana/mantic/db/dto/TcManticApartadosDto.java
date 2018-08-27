package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Date;
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
@Table(name="tc_mantic_apartados")
public class TcManticApartadosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="paterno")
  private String paterno;
  @Column (name="saldo")
  private Double saldo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_apartado")
  private Long idApartado;
  @Column (name="nombre")
  private String nombre;
  @Column (name="importe")
  private Double importe;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="materno")
  private String materno;
  @Column (name="domicilio")
  private String domicilio;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_apartado_estatus")
  private Long idApartadoEstatus;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="celular")
  private String celular;
  @Column (name="orden")
  private Long orden;
  @Column (name="telefono")
  private String telefono;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="abonado")
  private Double abonado;
  @Column (name="vencimiento")
  private Date vencimiento;

  public TcManticApartadosDto() {
    this(new Long(-1L));
  }

  public TcManticApartadosDto(Long key) {
    this(null, null, new Long(-1L), null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Date(Calendar.getInstance().getTimeInMillis()));
    setKey(key);
  }

  public TcManticApartadosDto(String paterno, Double saldo, Long idApartado, String nombre, Double importe, Long ejercicio, String consecutivo, String materno, String domicilio, Long idUsuario, Long idApartadoEstatus, String observaciones, String celular, Long orden, String telefono, Long idVenta, Double abonado, Date vencimiento) {
    setPaterno(paterno);
    setSaldo(saldo);
    setIdApartado(idApartado);
    setNombre(nombre);
    setImporte(importe);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setMaterno(materno);
    setDomicilio(domicilio);
    setIdUsuario(idUsuario);
    setIdApartadoEstatus(idApartadoEstatus);
    setObservaciones(observaciones);
    setCelular(celular);
    setOrden(orden);
    setTelefono(telefono);
    setIdVenta(idVenta);
    setAbonado(abonado);
		setVencimiento(vencimiento);
  }

	public Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}	
	
  public void setPaterno(String paterno) {
    this.paterno = paterno;
  }

  public String getPaterno() {
    return paterno;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

  public Double getSaldo() {
    return saldo;
  }

  public void setIdApartado(Long idApartado) {
    this.idApartado = idApartado;
  }

  public Long getIdApartado() {
    return idApartado;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
  }

  public void setEjercicio(Long ejercicio) {
    this.ejercicio = ejercicio;
  }

  public Long getEjercicio() {
    return ejercicio;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setMaterno(String materno) {
    this.materno = materno;
  }

  public String getMaterno() {
    return materno;
  }

  public void setDomicilio(String domicilio) {
    this.domicilio = domicilio;
  }

  public String getDomicilio() {
    return domicilio;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdApartadoEstatus(Long idApartadoEstatus) {
    this.idApartadoEstatus = idApartadoEstatus;
  }

  public Long getIdApartadoEstatus() {
    return idApartadoEstatus;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getCelular() {
    return celular;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
  }

  public void setAbonado(Double abonado) {
    this.abonado = abonado;
  }

  public Double getAbonado() {
    return abonado;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdApartado();
  }

  @Override
  public void setKey(Long key) {
  	this.idApartado = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getPaterno());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSaldo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdApartado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMaterno());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDomicilio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdApartadoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCelular());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTelefono());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAbonado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getVencimiento());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("paterno", getPaterno());
		regresar.put("saldo", getSaldo());
		regresar.put("idApartado", getIdApartado());
		regresar.put("nombre", getNombre());
		regresar.put("importe", getImporte());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("materno", getMaterno());
		regresar.put("domicilio", getDomicilio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idApartadoEstatus", getIdApartadoEstatus());
		regresar.put("observaciones", getObservaciones());
		regresar.put("celular", getCelular());
		regresar.put("orden", getOrden());
		regresar.put("telefono", getTelefono());
		regresar.put("idVenta", getIdVenta());
		regresar.put("abonado", getAbonado());
		regresar.put("vencimiento", getVencimiento());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getPaterno(), getSaldo(), getIdApartado(), getNombre(), getImporte(), getEjercicio(), getRegistro(), getConsecutivo(), getMaterno(), getDomicilio(), getIdUsuario(), getIdApartadoEstatus(), getObservaciones(), getCelular(), getOrden(), getTelefono(), getIdVenta(), getAbonado(), getVencimiento()
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
    regresar.append("idApartado~");
    regresar.append(getIdApartado());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdApartado());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticApartadosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdApartado()!= null && getIdApartado()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticApartadosDto other = (TcManticApartadosDto) obj;
    if (getIdApartado() != other.idApartado && (getIdApartado() == null || !getIdApartado().equals(other.idApartado))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdApartado() != null ? getIdApartado().hashCode() : 0);
    return hash;
  }
}