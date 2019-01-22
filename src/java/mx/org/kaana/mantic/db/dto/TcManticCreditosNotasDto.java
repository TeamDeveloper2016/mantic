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
@Table(name="tc_mantic_creditos_notas")
public class TcManticCreditosNotasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="importe")
  private Double importe;
  @Column (name="ejercicio")
  private Long ejercicio;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="consecutivo")
  private String consecutivo;
  @Column (name="id_credito_estatus")
  private Long idCreditoEstatus;
  @Column (name="id_tipo_credito_nota")
  private Long idTipoCreditoNota;
  @Column (name="id_devolucion")
  private Long idDevolucion;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="folio")
  private String folio;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="orden")
  private Long orden;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_credito_nota")
  private Long idCreditoNota;
  @Column (name="fecha")
  private Date fecha;
  @Column (name="saldo")
  private Double saldo;

  public TcManticCreditosNotasDto() {
    this(new Long(-1L));
  }

  public TcManticCreditosNotasDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), 0.0);
    setKey(key);
  }

  public TcManticCreditosNotasDto(Long idProveedor, Long idNotaEntrada, Double importe, Long ejercicio, String consecutivo, Long idCreditoEstatus, Long idTipoCreditoNota, Long idDevolucion, Long idUsuario, String folio, String observaciones, Long idEmpresa, Long orden, Long idCreditoNota, Date fecha, Double saldo) {
    setIdProveedor(idProveedor);
    setIdNotaEntrada(idNotaEntrada);
    setImporte(importe);
    setEjercicio(ejercicio);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setConsecutivo(consecutivo);
    setIdCreditoEstatus(idCreditoEstatus);
    setIdTipoCreditoNota(idTipoCreditoNota);
    setIdDevolucion(idDevolucion);
    setIdUsuario(idUsuario);
    setFolio(folio);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setOrden(orden);
    setIdCreditoNota(idCreditoNota);
		setFecha(fecha);
		setSaldo(saldo);
  }
	
  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setIdNotaEntrada(Long idNotaEntrada) {
    this.idNotaEntrada = idNotaEntrada;
  }

  public Long getIdNotaEntrada() {
    return idNotaEntrada;
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

  public void setIdCreditoEstatus(Long idCreditoEstatus) {
    this.idCreditoEstatus = idCreditoEstatus;
  }

  public Long getIdCreditoEstatus() {
    return idCreditoEstatus;
  }

  public void setIdTipoCreditoNota(Long idTipoCreditoNota) {
    this.idTipoCreditoNota = idTipoCreditoNota;
  }

  public Long getIdTipoCreditoNota() {
    return idTipoCreditoNota;
  }

  public void setIdDevolucion(Long idDevolucion) {
    this.idDevolucion = idDevolucion;
  }

  public Long getIdDevolucion() {
    return idDevolucion;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public String getFolio() {
    return folio;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setOrden(Long orden) {
    this.orden = orden;
  }

  public Long getOrden() {
    return orden;
  }

  public void setIdCreditoNota(Long idCreditoNota) {
    this.idCreditoNota = idCreditoNota;
  }

  public Long getIdCreditoNota() {
    return idCreditoNota;
  }

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha=fecha;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo=saldo;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdCreditoNota();
  }

  @Override
  public void setKey(Long key) {
  	this.idCreditoNota = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getEjercicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getConsecutivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCreditoEstatus());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoCreditoNota());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDevolucion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCreditoNota());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSaldo());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("importe", getImporte());
		regresar.put("ejercicio", getEjercicio());
		regresar.put("registro", getRegistro());
		regresar.put("consecutivo", getConsecutivo());
		regresar.put("idCreditoEstatus", getIdCreditoEstatus());
		regresar.put("idTipoCreditoNota", getIdTipoCreditoNota());
		regresar.put("idDevolucion", getIdDevolucion());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("folio", getFolio());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("orden", getOrden());
		regresar.put("idCreditoNota", getIdCreditoNota());
		regresar.put("fecha", getFecha());
		regresar.put("saldo", getSaldo());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getIdProveedor(), getIdNotaEntrada(), getImporte(), getEjercicio(), getRegistro(), getConsecutivo(), getIdCreditoEstatus(), getIdTipoCreditoNota(), getIdDevolucion(), getIdUsuario(), getFolio(), getObservaciones(), getIdEmpresa(), getOrden(), getIdCreditoNota(), getFecha(), getSaldo()
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
    regresar.append("idCreditoNota~");
    regresar.append(getIdCreditoNota());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCreditoNota());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticCreditosNotasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCreditoNota()!= null && getIdCreditoNota()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticCreditosNotasDto other = (TcManticCreditosNotasDto) obj;
    if (getIdCreditoNota() != other.idCreditoNota && (getIdCreditoNota() == null || !getIdCreditoNota().equals(other.idCreditoNota))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCreditoNota() != null ? getIdCreditoNota().hashCode() : 0);
    return hash;
  }

}


