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
  @Column (name="ruta")
  private String ruta;
  @Column (name="alias")
  private String alias;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_ficticia")
  private Long idFicticia;
  @Column (name="folio")
  private String folio;
  @Column (name="id_venta")
  private Long idVenta;
  @Column (name="nombre")
  private String nombre;
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
  @Column (name="registro")
  private Timestamp registro;

  public TcManticFacturasDto() {
    this(new Long(-1L));
  }

  public TcManticFacturasDto(Long key) {
    this(new Long(-1L), new Date(Calendar.getInstance().getTimeInMillis()), null, null, null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticFacturasDto(Long idFactura, Date ultimoIntento, Timestamp timbrado, String ruta, Long idUsuario, Long idFicticia, String folio, Long idVenta, String nombre, Long intentos, String correos, String comentarios, String observaciones, String idFacturama, String alias) {
    setIdFactura(idFactura);
    setUltimoIntento(ultimoIntento);
    setTimbrado(timbrado);
    setRuta(ruta);
    this.alias= alias;
    setIdUsuario(idUsuario);
    setIdFicticia(idFicticia);
    setFolio(folio);
    setIdVenta(idVenta);
    setNombre(nombre);
    setIntentos(intentos);
		this.correos= correos;
		this.comentarios= comentarios;
		this.observaciones= observaciones;
		this.idFacturama= idFacturama;
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
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

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias=alias;
	}

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdFicticia(Long idFicticia) {
    this.idFicticia = idFicticia;
  }

  public Long getIdFicticia() {
    return idFicticia;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public String getFolio() {
    return folio;
  }

  public void setIdVenta(Long idVenta) {
    this.idVenta = idVenta;
  }

  public Long getIdVenta() {
    return idVenta;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
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

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
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
		regresar.append(getRuta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getAlias());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFicticia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVenta());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
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
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idFactura", getIdFactura());
		regresar.put("ultimoIntento", getUltimoIntento());
		regresar.put("timbrado", getTimbrado());
		regresar.put("ruta", getRuta());
		regresar.put("alias", getAlias());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idFicticia", getIdFicticia());
		regresar.put("folio", getFolio());
		regresar.put("idVenta", getIdVenta());
		regresar.put("nombre", getNombre());
		regresar.put("intentos", getIntentos());
		regresar.put("correos", getCorreos());
		regresar.put("comentarios", getComentarios());
		regresar.put("observaciones", getObservaciones());
		regresar.put("idFacturama", getIdFacturama());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
    getIdFactura(), getUltimoIntento(), getTimbrado(), getRuta(), getAlias(), getIdUsuario(), getIdFicticia(), getFolio(), getIdVenta(), getNombre(), getIntentos(), getCorreos(), getComentarios(), getObservaciones(), getIdFactura(), getRegistro()
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
