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
@Table(name="tc_mantic_notas_detalles")
public class TcManticNotasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="codigo")
  private String codigo;
  @Column (name="unidad_medida")
  private String unidadMedida;
  @Column (name="costo")
  private Double costo;
  @Column (name="descuento")
  private String descuento;
  @Column (name="sat")
  private String sat;
  @Column (name="extras")
  private String extras;
  @Column (name="id_nota_entrada")
  private Long idNotaEntrada;
  @Column (name="nombre")
  private String nombre;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="iva")
  private Double iva;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_nota_detalle")
  private Long idNotaDetalle;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="excedentes")
  private Double excedentes;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="id_orden_detalle")
  private Long idOrdenDetalle;
  @Column (name="cantidades")
  private Double cantidades;
  @Column (name="id_aplicar")
  private Long idAplicar;
  @Column (name="declarados")
  private Double declarados;
  @Column (name="diferencia")
  private Double diferencia;
  @Column (name="costo_real")
  private Double costoReal;
  @Column (name="costo_calculado")
  private Double costoCalculado;
  @Column (name="origen")
  private String origen;

  public TcManticNotasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticNotasDetallesDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, null, null, 2L, 0D, 0D, 0D, 0D, "");
    setKey(key);
  }

  public TcManticNotasDetallesDto(String codigo, String unidadMedida, Double costo, String descuento, String sat, String extras, Long idNotaEntrada, String nombre, Double importe, Double iva, Long idNotaDetalle, Double subTotal, Double cantidad, Long idArticulo, Double descuentos, Double impuestos, Long idOrdenDetalle, Double cantidades, Double excedentes, Long idAplicar, Double declarados, Double diferencia, Double costoReal, Double costoCalculado, String origen) {
    setCodigo(codigo);
    setUnidadMedida(unidadMedida);
    setCosto(costo);
    setDescuento(descuento);
    setSat(sat);
    setExtras(extras);
    setIdNotaEntrada(idNotaEntrada);
    setNombre(nombre);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setIva(iva);
    setIdNotaDetalle(idNotaDetalle);
    setSubTotal(subTotal);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setDescuentos(descuentos);
    setExcedentes(excedentes);
    setImpuestos(impuestos);
    setIdOrdenDetalle(idOrdenDetalle);
    setCantidades(cantidades);
		setIdAplicar(idAplicar);
    setDeclarados(declarados);
    setDiferencia(diferencia);
		this.costoReal= costoReal;
		this.costoCalculado= costoCalculado;
		this.origen= origen;
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public String getUnidadMedida() {
    return unidadMedida;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setSat(String sat) {
    this.sat = sat;
  }

  public String getSat() {
    return sat;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
  }

  public void setIdNotaEntrada(Long idNotaEntrada) {
    this.idNotaEntrada = idNotaEntrada;
  }

  public Long getIdNotaEntrada() {
    return idNotaEntrada;
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

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setIdNotaDetalle(Long idNotaDetalle) {
    this.idNotaDetalle = idNotaDetalle;
  }

  public Long getIdNotaDetalle() {
    return idNotaDetalle;
  }

  public void setSubTotal(Double subTotal) {
    this.subTotal = subTotal;
  }

  public Double getSubTotal() {
    return subTotal;
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

  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
  }

	public Double getExcedentes() {
		return excedentes;
	}

	public void setExcedentes(Double excedentes) {
		this.excedentes=excedentes;
	}

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
  }

	public Long getIdOrdenDetalle() {
		return idOrdenDetalle;
	}

	public void setIdOrdenDetalle(Long idOrdenDetalle) {
		this.idOrdenDetalle=idOrdenDetalle;
	}

	public Double getCantidades() {
		return cantidades;
	}

	public void setCantidades(Double cantidades) {
		this.cantidades=cantidades;
	}

	public Long getIdAplicar() {
		return idAplicar;
	}

	public void setIdAplicar(Long idAplicar) {
		this.idAplicar=idAplicar;
	}

	public Double getDeclarados() {
		return declarados;
	}

	public void setDeclarados(Double declarados) {
		this.declarados=declarados;
	}

	public Double getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(Double diferencia) {
		this.diferencia=diferencia;
	}

	public Double getCostoReal() {
		return costoReal;
	}

	public void setCostoReal(Double costoReal) {
		this.costoReal=costoReal;
	}

	public Double getCostoCalculado() {
		return costoCalculado;
	}

	public void setCostoCalculado(Double costoCalculado) {
		this.costoCalculado=costoCalculado;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen=origen;
	}
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdNotaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idNotaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaEntrada());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdNotaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExcedentes());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidades());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdAplicar());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDeclarados());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDiferencia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCostoReal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCostoCalculado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrigen());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("unidadMedida", getUnidadMedida());
		regresar.put("costo", getCosto());
		regresar.put("descuento", getDescuento());
		regresar.put("sat", getSat());
		regresar.put("extras", getExtras());
		regresar.put("idNotaEntrada", getIdNotaEntrada());
		regresar.put("nombre", getNombre());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
		regresar.put("iva", getIva());
		regresar.put("idNotaDetalle", getIdNotaDetalle());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("descuentos", getDescuentos());
		regresar.put("excedentes", getExcedentes());
		regresar.put("impuestos", getImpuestos());
		regresar.put("idOrdenDetalle", getIdOrdenDetalle());
		regresar.put("cantidades", getCantidades());
		regresar.put("idAplicar", getIdAplicar());
		regresar.put("declarados", getDeclarados());
		regresar.put("diferencia", getDiferencia());
		regresar.put("costoReal", getCostoReal());
		regresar.put("costoCalculado", getCostoCalculado());
		regresar.put("origen", getOrigen());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCodigo(), getUnidadMedida(), getCosto(), getDescuento(), getSat(), getExtras(), getIdNotaEntrada(), getNombre(), getImporte(), getRegistro(), getIva(), getIdNotaDetalle(), getSubTotal(), getCantidad(), getIdArticulo(), getDescuentos(), getImpuestos(), getIdOrdenDetalle(), getCantidades(), getExcedentes(), getIdAplicar(), getDeclarados(), getDiferencia(), getCostoReal(), getCostoCalculado(), getOrigen()
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
    regresar.append("idNotaDetalle~");
    regresar.append(getIdNotaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdNotaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticNotasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdNotaDetalle()!= null && getIdNotaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticNotasDetallesDto other = (TcManticNotasDetallesDto) obj;
    if (getIdNotaDetalle() != other.idNotaDetalle && (getIdNotaDetalle() == null || !getIdNotaDetalle().equals(other.idNotaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdNotaDetalle() != null ? getIdNotaDetalle().hashCode() : 0);
    return hash;
  }

}


