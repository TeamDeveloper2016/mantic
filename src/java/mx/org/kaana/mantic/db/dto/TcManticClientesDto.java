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
@Table(name="tc_mantic_clientes")
public class TcManticClientesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="clave")
  private String clave;
  @Column (name="plazo_dias")
  private Long plazoDias;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_cliente")
  private Long idCliente;
  @Column (name="limite_credito")
  private Double limiteCredito;
  @Column (name="id_credito")
  private Long idCredito;
  @Column (name="razon_social")
  private String razonSocial;
  @Column (name="saldo")
  private Double saldo;
  @Column (name="rfc")
  private String rfc;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_uso_cfdi")
  private Long idUsoCfdi;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="id_tipo_venta")
  private Long idTipoVenta;
  @Column (name="id_facturama")
  private String idFacturama;

  public TcManticClientesDto() {
    this(new Long(-1L));
  }

  public TcManticClientesDto(Long key) {
    this(null, 0L, new Long(-1L), 0D, null, null, 0.0D, null, null, 3L, null, null, null, null);
    setKey(key);
  }

  public TcManticClientesDto(String clave, Long plazoDias, Long idCliente, Double limiteCredito, Long idCredito, String razonSocial, Double saldo, String rfc, Long idUsuario, Long idUsoCfdi, String observaciones, Long idEmpresa, Long idTipoVenta, String idFacturama) {
    setClave(clave);
    setPlazoDias(plazoDias);
    setIdCliente(idCliente);
    setLimiteCredito(limiteCredito);
    setIdCredito(idCredito);
    setRazonSocial(razonSocial);
    setSaldo(saldo);
    setRfc(rfc);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIdUsuario(idUsuario);
    setIdUsoCfdi(idUsoCfdi);
    setObservaciones(observaciones);
    setIdEmpresa(idEmpresa);
    setIdTipoVenta(idTipoVenta);
		this.idFacturama= idFacturama;
  }
	
  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }

  public void setPlazoDias(Long plazoDias) {
    this.plazoDias = plazoDias;
  }

  public Long getPlazoDias() {
    return plazoDias;
  }

  public void setIdCliente(Long idCliente) {
    this.idCliente = idCliente;
  }

  public Long getIdCliente() {
    return idCliente;
  }

  public void setLimiteCredito(Double limiteCredito) {
    this.limiteCredito = limiteCredito;
  }

  public Double getLimiteCredito() {
    return limiteCredito;
  }

  public void setIdCredito(Long idCredito) {
    this.idCredito = idCredito;
  }

  public Long getIdCredito() {
    return idCredito;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

  public Double getSaldo() {
    return saldo;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  public String getRfc() {
    return rfc;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsoCfdi(Long idUsoCfdi) {
    this.idUsoCfdi = idUsoCfdi;
  }

  public Long getIdUsoCfdi() {
    return idUsoCfdi;
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

  public void setIdTipoVenta(Long idTipoVenta) {
    this.idTipoVenta = idTipoVenta;
  }

  public Long getIdTipoVenta() {
    return idTipoVenta;
  }

	public String getIdFacturama() {
		return idFacturama;
	}

	public void setIdFacturama(String idFacturama) {
		this.idFacturama=idFacturama;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdCliente();
  }

  @Override
  public void setKey(Long key) {
  	this.idCliente = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getClave());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPlazoDias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCliente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLimiteCredito());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCredito());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRazonSocial());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSaldo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRfc());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsoCfdi());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdTipoVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturama());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("clave", getClave());
		regresar.put("plazoDias", getPlazoDias());
		regresar.put("idCliente", getIdCliente());
		regresar.put("limiteCredito", getLimiteCredito());
		regresar.put("idCredito", getIdCredito());
		regresar.put("razonSocial", getRazonSocial());
		regresar.put("saldo", getSaldo());
		regresar.put("rfc", getRfc());
		regresar.put("registro", getRegistro());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idUsoCfdi", getIdUsoCfdi());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("idTipoVenta", getIdTipoVenta());
		regresar.put("idFacturama", getIdFacturama());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getClave(), getPlazoDias(), getIdCliente(), getLimiteCredito(), getIdCredito(), getRazonSocial(), getSaldo(), getRfc(), getRegistro(), getIdUsuario(), getIdUsoCfdi(), getObservaciones(), getIdEmpresa(), getIdTipoVenta(), getIdFacturama()
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
    regresar.append("idCliente~");
    regresar.append(getIdCliente());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdCliente());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticClientesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdCliente()!= null && getIdCliente()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticClientesDto other = (TcManticClientesDto) obj;
    if (getIdCliente() != other.idCliente && (getIdCliente() == null || !getIdCliente().equals(other.idCliente))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdCliente() != null ? getIdCliente().hashCode() : 0);
    return hash;
  }
}