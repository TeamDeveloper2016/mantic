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
@Table(name="tc_mantic_facturas")
public class TcManticFacturasDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_factura")
  private Long idFactura;
  @Column (name="ultimo_intento")
  private Date ultimoIntento;
  @Column (name="timbrado")
  private Timestamp timbrado;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_factura_estatus")
  private Long idFacturaEstatus;
  @Column (name="folio")
  private String folio;
  @Column (name="intentos")
  private Long intentos;
  @Column (name="correos")
  private String correos;
  @Column (name="comentarios")
  private String comentarios;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="id_facturama")
  private String idFacturama;
	
  @Column (name="cadena_original")
  private String cadenaOriginal;
  @Column (name="sello_sat")
  private String selloSat;
  @Column (name="sello_cfdi")
  private String selloCfdi;
  @Column (name="certificado_sat")
  private String certificadoSat;
  @Column (name="certificado_digital")
  private String certificadoDigital;
  @Column (name="certificacion")
  private Timestamp certificacion;
  @Column (name="folio_fiscal")
  private String folioFiscal;
	
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="cancelada")
  private Timestamp cancelada;

  public TcManticFacturasDto() {
    this(new Long(-1L));
  }

  public TcManticFacturasDto(Long key) {
    this(new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, 0L, null, null, null, null);
    setKey(key);
  }

  public TcManticFacturasDto(Long idFactura, Date ultimoIntento, Timestamp timbrado, Long idUsuario, String folio, Long intentos, String correos, String comentarios, String observaciones, String idFacturama) {
    this(new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), timbrado, idUsuario, folio, intentos, correos, comentarios, observaciones, idFacturama, null, null, null, null, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, 1L);
	}
	
  public TcManticFacturasDto(Long idFactura, Date ultimoIntento, Timestamp timbrado, Long idUsuario, String folio, Long intentos, String correos, String comentarios, String observaciones, String idFacturama, String cadenaOriginal, String selloSat, String selloCfdi, String certificadoSat, String certificadoDigital, Timestamp certificacion, String folioFiscal, Long idFacturaEstatus) {
    setIdFactura(idFactura);
    setUltimoIntento(ultimoIntento);
    setTimbrado(timbrado);
    setIdUsuario(idUsuario);
    setFolio(folio);
    setIntentos(intentos);
		this.correos= correos;
		this.comentarios= comentarios;
		this.observaciones= observaciones;
		this.idFacturama= idFacturama;
		this.cadenaOriginal= cadenaOriginal;
		this.selloSat= selloSat;
		this.selloCfdi= selloCfdi;
		this.certificadoSat= certificadoSat;
		this.certificadoDigital= certificadoDigital;
		this.certificacion= certificacion;
		this.folioFiscal= folioFiscal;
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		this.cancelada= new Timestamp(Calendar.getInstance().getTimeInMillis());
		setIdFacturaEstatus(idFacturaEstatus);
  }
	
  public void setIdFactura(Long idFactura) {
    this.idFactura = idFactura;
  }

  public Long getIdFactura() {
    return idFactura;
  }

  public void setUltimoIntento(Date ultimoIntento) {
    this.ultimoIntento = ultimoIntento;
  }

  public Date getUltimoIntento() {
    return ultimoIntento;
  }

  public void setTimbrado(Timestamp timbrado) {
    this.timbrado = timbrado;
  }

  public Timestamp getTimbrado() {
    return timbrado;
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

  public void setIntentos(Long intentos) {
    this.intentos = intentos;
  }

  public Long getIntentos() {
    return intentos;
  }

	public String getCorreos() {
		return correos;
	}

	public void setCorreos(String correos) {
		this.correos=correos;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios=comentarios;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones=observaciones;
	}

	public String getIdFacturama() {
		return idFacturama;
	}

	public void setIdFacturama(String idFacturama) {
		this.idFacturama=idFacturama;
	}

	public String getCadenaOriginal() {
		return cadenaOriginal;
	}

	public void setCadenaOriginal(String cadenaOriginal) {
		this.cadenaOriginal=cadenaOriginal;
	}

	public String getSelloSat() {
		return selloSat;
	}

	public void setSelloSat(String selloSat) {
		this.selloSat=selloSat;
	}

	public String getSelloCfdi() {
		return selloCfdi;
	}

	public void setSelloCfdi(String selloCfdi) {
		this.selloCfdi=selloCfdi;
	}

	public String getCertificadoSat() {
		return certificadoSat;
	}

	public void setCertificadoSat(String certificadoSat) {
		this.certificadoSat=certificadoSat;
	}

	public String getCertificadoDigital() {
		return certificadoDigital;
	}

	public void setCertificadoDigital(String certificadoDigital) {
		this.certificadoDigital=certificadoDigital;
	}

	public Timestamp getCertificacion() {
		return certificacion;
	}

	public void setCertificacion(Timestamp certificacion) {
		this.certificacion=certificacion;
	}

	public String getFolioFiscal() {
		return folioFiscal;
	}

	public void setFolioFiscal(String folioFiscal) {
		this.folioFiscal=folioFiscal;
	}

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public Timestamp getCancelada() {
		return cancelada;
	}

	public void setCancelada(Timestamp cancelada) {
		this.cancelada=cancelada;
	}

	public Long getIdFacturaEstatus() {
		return idFacturaEstatus;
	}

	public void setIdFacturaEstatus(Long idFacturaEstatus) {
		this.idFacturaEstatus = idFacturaEstatus;
	}	
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdFactura();
  }

  @Override
  public void setKey(Long key) {
  	this.idFactura = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdFactura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUltimoIntento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTimbrado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIntentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCorreos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getComentarios());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFactura());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCadenaOriginal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSelloSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSelloCfdi());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCertificadoSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCertificadoDigital());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCertificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolioFiscal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCancelada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturaEstatus());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idFactura", getIdFactura());
		regresar.put("ultimoIntento", getUltimoIntento());
		regresar.put("timbrado", getTimbrado());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("folio", getFolio());
		regresar.put("intentos", getIntentos());
		regresar.put("correos", getCorreos());
		regresar.put("comentarios", getComentarios());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idFacturama", getIdFacturama());
		regresar.put("cadenaOriginal", getCadenaOriginal());
		regresar.put("selloSat", getSelloSat());
		regresar.put("selloCfdi", getSelloCfdi());
		regresar.put("certificadoSat", getCertificadoSat());
		regresar.put("certificadoDigital", getCertificadoDigital());
		regresar.put("certificacion", getCertificacion());
		regresar.put("folioFiscal", getFolioFiscal());
		regresar.put("registro", getRegistro());
		regresar.put("cancelada", getCancelada());
		regresar.put("idFacturaEstatus", getIdFacturaEstatus());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getIdFactura(), getUltimoIntento(), getTimbrado(), getIdUsuario(), getFolio(), getIntentos(), getCorreos(), getComentarios(), getObservaciones(), getIdFactura(), getCadenaOriginal(), getSelloSat(), getSelloCfdi(), getCertificadoSat(), getCertificadoDigital(), getCertificacion(), getFolioFiscal(), getRegistro(), getCancelada(), getIdFacturaEstatus()
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
    regresar.append("idFactura~");
    regresar.append(getIdFactura());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFactura());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFacturasDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFactura()!= null && getIdFactura()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFacturasDto other = (TcManticFacturasDto) obj;
    if (getIdFactura() != other.idFactura && (getIdFactura() == null || !getIdFactura().equals(other.idFactura))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFactura() != null ? getIdFactura().hashCode() : 0);
    return hash;
  }
}