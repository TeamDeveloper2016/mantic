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
@Table(name="tc_mantic_articulos")
public class TcManticArticulosDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="descuentos")
  private String descuentos;
  @Column (name="id_imagen")
  private Long idImagen;
  @Column (name="id_categoria")
  private Long idCategoria;
  @Column (name="extras")
  private String extras;
  @Column (name="meta_tag")
  private String metaTag;
  @Column (name="nombre")
  private String nombre;
  @Column (name="precio")
  private Double precio;
  @Column (name="iva")
  private Double iva;
  @Column (name="mayoreo")
  private Double mayoreo;
  @Column (name="desperdicio")
  private Double desperdicio;
  @Column (name="meta_tag_descipcion")
  private String metaTagDescipcion;
  @Column (name="id_vigente")
  private Long idVigente;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="stock")
  private Double stock;
  @Column (name="minimo")
  private Double minimo;
  @Column (name="maximo")
  private Double maximo;
  @Column (name="medio_mayoreo")
  private Double medioMayoreo;
  @Column (name="peso_estimado")
  private Double pesoEstimado;
  @Column (name="id_empaque_unidad_medida")
  private Long idEmpaqueUnidadMedida;
  @Column (name="id_redondear")
  private Long idRedondear;
  @Column (name="menudeo")
  private Double menudeo;
  @Column (name="meta_tag_teclado")
  private String metaTagTeclado;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="fecha")
  private Timestamp fecha;
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="id_empresa")
  private Long idEmpresa;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="limite_medio_mayoreo")
  private Double limiteMedioMayoreo;
  @Column (name="limite_mayoreo")
  private Double limiteMayoreo;
  @Column (name="sat")
  private String sat;
  @Column (name="id_articulo_tipo")
  private Long idArticuloTipo;
  @Column (name="id_barras")
  private Long idBarras;
  @Column (name="descuento")
  private String descuento;
  @Column (name="extra")
  private String extra;
  @Column (name="id_facturama")
  private String idFacturama;
  @Column (name="actualizado")
  private Timestamp actualizado;

  public TcManticArticulosDto() {
    this(new Long(-1L));
  }

  public TcManticArticulosDto(Long key) {
    this(null, null, null, null, null, null, null, 10D, 16D, 13D, 0D, null, null, new Long(-1L), 0D, 15D, 0D, null, null, 17D, null, new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null, 3D, 5D, 3D, 8D, Constantes.CODIGO_SAT, 1L, 2L, "0", "0", null);
    setKey(key);
  }

  public TcManticArticulosDto(String descripcion, String descuentos, Long idImagen, Long idCategoria, String extras, String metaTag, String nombre, Double precio, Double iva, Double mayoreo, Double desperdicio, String metaTagDescipcion, Long idVigente, Long idArticulo, Double stock, Double medioMayoreo, Double pesoEstimado, Long idEmpaqueUnidadMedida, Long idRedondear, Double menudeo, String metaTagTeclado, Timestamp fecha, Long idUsuario, Long idEmpresa, Double cantidad, Double minimo, Double maximo, Double limiteMedioMayoreo, Double limiteMayoreo, String sat, Long idArticuloTipo, Long idBarras, String descuento, String extra, String idFacturama) {
    setDescripcion(descripcion);
    setDescuentos(descuentos);
    setIdImagen(idImagen);
    setIdCategoria(idCategoria);
    setExtras(extras);
    setMetaTag(metaTag);
    setNombre(nombre);
    setPrecio(precio);
    setIva(iva);
    setMayoreo(mayoreo);
    setDesperdicio(desperdicio);
    setMetaTagDescipcion(metaTagDescipcion);
    setIdVigente(idVigente);
    setIdArticulo(idArticulo);
    setStock(stock);
    setMinimo(minimo);
    setMaximo(maximo);
    setMedioMayoreo(medioMayoreo);
    setPesoEstimado(pesoEstimado);
    setIdEmpaqueUnidadMedida(idEmpaqueUnidadMedida);
    setIdRedondear(idRedondear);
    setMenudeo(menudeo);
    setMetaTagTeclado(metaTagTeclado);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setActualizado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setFecha(fecha);
    setIdUsuario(idUsuario);
    setIdEmpresa(idEmpresa);
    setCantidad(cantidad);
    setLimiteMedioMayoreo(limiteMedioMayoreo);
    setLimiteMayoreo(limiteMayoreo);
    setSat(sat);
    this.idArticuloTipo= idArticuloTipo;
    this.idBarras   = idBarras;
		this.descuento  = descuento;
		this.extra      = extra;
		this.idFacturama= idFacturama;
  }
	
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescuentos(String descuentos) {
    this.descuentos = descuentos;
  }

  public String getDescuentos() {
    return descuentos;
  }

  public void setIdImagen(Long idImagen) {
    this.idImagen = idImagen;
  }

  public Long getIdImagen() {
    return idImagen;
  }

  public void setIdCategoria(Long idCategoria) {
    this.idCategoria = idCategoria;
  }

  public Long getIdCategoria() {
    return idCategoria;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
  }

  public void setMetaTag(String metaTag) {
    this.metaTag = metaTag;
  }

  public String getMetaTag() {
    return metaTag;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Double getPrecio() {
    return precio== null || precio<= 0D? 1D: precio;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setMayoreo(Double mayoreo) {
    this.mayoreo = mayoreo;
  }

  public Double getMayoreo() {
    return mayoreo;
  }

  public void setDesperdicio(Double desperdicio) {
    this.desperdicio = desperdicio;
  }

  public Double getDesperdicio() {
    return desperdicio;
  }

  public void setMetaTagDescipcion(String metaTagDescipcion) {
    this.metaTagDescipcion = metaTagDescipcion;
  }

  public String getMetaTagDescipcion() {
    return metaTagDescipcion;
  }

  public void setIdVigente(Long idVigente) {
    this.idVigente = idVigente;
  }

  public Long getIdVigente() {
    return idVigente;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setStock(Double stock) {
    this.stock = stock;
  }

  public Double getStock() {
    return stock;
  }

	public Double getMinimo() {
		return minimo;
	}

	public void setMinimo(Double minimo) {
		this.minimo=minimo;
	}

	public Double getMaximo() {
		return maximo;
	}

	public void setMaximo(Double maximo) {
		this.maximo=maximo;
	}
	
  public void setMedioMayoreo(Double medioMayoreo) {
    this.medioMayoreo = medioMayoreo;
  }

  public Double getMedioMayoreo() {
    return medioMayoreo;
  }

  public void setPesoEstimado(Double pesoEstimado) {
    this.pesoEstimado = pesoEstimado;
  }

  public Double getPesoEstimado() {
    return pesoEstimado;
  }

  public void setIdEmpaqueUnidadMedida(Long idEmpaqueUnidadMedida) {
    this.idEmpaqueUnidadMedida = idEmpaqueUnidadMedida;
  }

  public Long getIdEmpaqueUnidadMedida() {
    return idEmpaqueUnidadMedida;
  }

  public void setIdRedondear(Long idRedondear) {
    this.idRedondear = idRedondear;
  }

  public Long getIdRedondear() {
    return idRedondear;
  }

  public void setMenudeo(Double menudeo) {
    this.menudeo = menudeo;
  }

  public Double getMenudeo() {
    return menudeo;
  }

  public void setMetaTagTeclado(String metaTagTeclado) {
    this.metaTagTeclado = metaTagTeclado;
  }

  public String getMetaTagTeclado() {
    return metaTagTeclado;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setFecha(Timestamp fecha) {
    this.fecha = fecha;
  }

  public Timestamp getFecha() {
    return fecha;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
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

	public String getSat() {
		return sat;
	}

	public void setSat(String sat) {
		this.sat=sat;
	}

	public Long getIdArticuloTipo() {
		return idArticuloTipo;
	}

	public void setIdArticuloTipo(Long idArticuloTipo) {
		this.idArticuloTipo=idArticuloTipo;
	}

	public Long getIdBarras() {
		return idBarras;
	}

	public void setIdBarras(Long idBarras) {
		this.idBarras=idBarras;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento=descuento;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra=extra;
	}

	public String getIdFacturama() {
		return idFacturama;
	}

	public void setIdFacturama(String idFacturama) {
		this.idFacturama=idFacturama;
	}

	public Timestamp getActualizado() {
		return actualizado;
	}

	public void setActualizado(Timestamp actualizado) {
		this.actualizado=actualizado;
	}
	
  @Transient
  @Override
  public Long getKey() {
  	return getIdArticulo();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticulo = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdImagen());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdCategoria());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMetaTag());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDesperdicio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMetaTagDescipcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdVigente());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getStock());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMinimo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMaximo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMedioMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPesoEstimado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpaqueUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdRedondear());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMenudeo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getMetaTagTeclado());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdEmpresa());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLimiteMedioMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getLimiteMayoreo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloTipo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdBarras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFacturama());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getActualizado());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descripcion", getDescripcion());
		regresar.put("descuentos", getDescuentos());
		regresar.put("idImagen", getIdImagen());
		regresar.put("idCategoria", getIdCategoria());
		regresar.put("extras", getExtras());
		regresar.put("metaTag", getMetaTag());
		regresar.put("nombre", getNombre());
		regresar.put("precio", getPrecio());
		regresar.put("iva", getIva());
		regresar.put("mayoreo", getMayoreo());
		regresar.put("desperdicio", getDesperdicio());
		regresar.put("metaTagDescipcion", getMetaTagDescipcion());
		regresar.put("idVigente", getIdVigente());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("stock", getStock());
		regresar.put("minimo", getMinimo());
		regresar.put("maximo", getMaximo());
		regresar.put("medioMayoreo", getMedioMayoreo());
		regresar.put("pesoEstimado", getPesoEstimado());
		regresar.put("idEmpaqueUnidadMedida", getIdEmpaqueUnidadMedida());
		regresar.put("idRedondear", getIdRedondear());
		regresar.put("menudeo", getMenudeo());
		regresar.put("metaTagTeclado", getMetaTagTeclado());
		regresar.put("registro", getRegistro());
		regresar.put("fecha", getFecha());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("idEmpresa", getIdEmpresa());
		regresar.put("cantidad", getCantidad());
		regresar.put("limiteMedioMayoreo", getLimiteMedioMayoreo());
		regresar.put("limiteMayoreo", getLimiteMayoreo());
		regresar.put("sat", getSat());
		regresar.put("idArticuloTipo", getIdArticuloTipo());
		regresar.put("idBarras", getIdBarras());
		regresar.put("descuento", getDescuento());
		regresar.put("extra", getExtra());
		regresar.put("idFacturama", getIdFacturama());
		regresar.put("actualizado", getActualizado());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
      getDescripcion(), getDescuentos(), getIdImagen(), getIdCategoria(), getExtras(), getMetaTag(), getNombre(), getPrecio(), getIva(), getMayoreo(), getDesperdicio(), getMetaTagDescipcion(), getIdVigente(), getIdArticulo(), getStock(), getMedioMayoreo(), getPesoEstimado(), getIdEmpaqueUnidadMedida(), getIdRedondear(), getMenudeo(), getMetaTagTeclado(), getRegistro(), getFecha(), getIdUsuario(), getIdEmpresa(), getCantidad(), getMinimo(), getMaximo(), getLimiteMedioMayoreo(), getLimiteMayoreo(), getSat(), getIdArticuloTipo(), getIdBarras(), getDescuento(), getExtras(), getIdFacturama(), getActualizado()
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
    regresar.append("idArticulo~");
    regresar.append(getIdArticulo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticulo());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticArticulosDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticulo()!= null && getIdArticulo()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticArticulosDto other = (TcManticArticulosDto) obj;
    if (getIdArticulo() != other.idArticulo && (getIdArticulo() == null || !getIdArticulo().equals(other.idArticulo))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticulo() != null ? getIdArticulo().hashCode() : 0);
    return hash;
  }

}


