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
@Table(name="tc_mantic_articulos_bitacora")
public class TcManticArticulosBitacoraDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="iva")
  private Double iva;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="mayoreo")
  private Double mayoreo;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_bitacora")
  private Long idArticuloBitacora;
  @Column (name="costo")
  private Double costo;
  @Column (name="menudeo")
  private Double menudeo;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="medio_mayoreo")
  private Double medioMayoreo;
  @Column (name="limite_medio_mayoreo")
  private Double limiteMedioMayoreo;
  @Column (name="limite_mayoreo")
  private Double limiteMayoreo;
  @Column (name="descuento")
  private String descuento;
  @Column (name="extras")
  private String extras;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticArticulosBitacoraDto() {
    this(new Long(-1L));
  }

  public TcManticArticulosBitacoraDto(Long key) {
    this(16D, null, 0D, new Long(-1L), 0D, 0D, null, null, 0D, 0D, 3D, 5D, "0", "0");
    setKey(key);
  }

  public TcManticArticulosBitacoraDto(Double iva, Long idUsuario, Double mayoreo, Long idArticuloBitacora, Double menudeo, Double cantidad, Long idArticulo, Long idNotaEntrada, Double medioMayoreo, Double costo, Double limiteMedioMayoreo, Double limiteMayoreo, String descuento, String extras) {
    setIva(iva);
    setIdUsuario(idUsuario);
    setMayoreo(mayoreo);
    setIdArticuloBitacora(idArticuloBitacora);
    setCosto(costo);
    setMenudeo(menudeo);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setIdNotaEntrada(idNotaEntrada);
    setMedioMayoreo(medioMayoreo);
    setLimiteMedioMayoreo(limiteMedioMayoreo);
    setLimiteMayoreo(limiteMayoreo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		this.descuento= descuento;
		this.extras   = extras;
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

  public void setMayoreo(Double mayoreo) {
    this.mayoreo = mayoreo;
  }

  public Double getMayoreo() {
    return mayoreo;
  }

  public void setIdArticuloBitacora(Long idArticuloBitacora) {
    this.idArticuloBitacora = idArticuloBitacora;
  }

  public Long getIdArticuloBitacora() {
    return idArticuloBitacora;
  }

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo=costo;
	}

  public void setMenudeo(Double menudeo) {
    this.menudeo = menudeo;
  }

  public Double getMenudeo() {
    return menudeo;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setIdNotaEntrada(Long idNotaEntrada) {
    this.idNotaEntrada = idNotaEntrada;
  }

  public Long getIdNotaEntrada() {
    return idNotaEntrada;
  }

  public void setMedioMayoreo(Double medioMayoreo) {
    this.medioMayoreo = medioMayoreo;
  }

  public Double getMedioMayoreo() {
    return medioMayoreo;
  }

	public Double getLimiteMedioMayoreo() {
		return limiteMedioMayoreo;
	}

	public void setLimiteMedioMayoreo(Double limiteMedioMayoreo) {
		this.limiteMedioMayoreo=limiteMedioMayoreo;
	}

	public Double getLimiteMayoreo() {
		return limiteMayoreo;
	}

	public void setLimiteMayoreo(Double limiteMayoreo) {
		this.limiteMayoreo=limiteMayoreo;
	}

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento=descuento;
	}

	public String getExtras() {
		return extras;
	}

	public void setExtras(String extras) {
		this.extras=extras;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdArticuloBitacora();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloBitacora = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloBitacora());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMenudeo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMedioMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLimiteMedioMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLimiteMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("iva", getIva());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("mayoreo", getMayoreo());
		regresar.put("idArticuloBitacora", getIdArticuloBitacora());
		regresar.put("costo", getCosto());
		regresar.put("menudeo", getMenudeo());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("medioMayoreo", getMedioMayoreo());
		regresar.put("limiteMedioMayoreo", getLimiteMedioMayoreo());
		regresar.put("limiteMayoreo", getLimiteMayoreo());
		regresar.put("descuento", getDescuento());
		regresar.put("extras", getExtras());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[] {
      getIva(), getIdUsuario(), getMayoreo(), getIdArticuloBitacora(), getCosto(), getMenudeo(), getCantidad(), getIdArticulo(), getIdNotaEntrada(), getMedioMayoreo(), getRegistro(), getLimiteMedioMayoreo(), getLimiteMayoreo(), getDescuento(), getExtras()
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
    regresar.append("idArticuloBitacora~");
    regresar.append(getIdArticuloBitacora());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloBitacora());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticArticulosBitacoraDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloBitacora()!= null && getIdArticuloBitacora()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticArticulosBitacoraDto other = (TcManticArticulosBitacoraDto) obj;
    if (getIdArticuloBitacora() != other.idArticuloBitacora && (getIdArticuloBitacora() == null || !getIdArticuloBitacora().equals(other.idArticuloBitacora))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloBitacora() != null ? getIdArticuloBitacora().hashCode() : 0);
    return hash;
  }

}


