package mx.org.kaana.mantic.comun.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UISelectEntity;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/05/2018
 *@time 02:44:19 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ArticuloDetalle implements IBaseDto, Serializable {

	private static final long serialVersionUID=-5206675651493710475L;

  private Long idArticulo;
  private String codigo;
  private Double costo;
  private String descuento;
  private String descuentoDescripcion;
  private String extras;
  private Double importe;
  private Timestamp registro;
  private String propio;
  private Double iva;
  private Double impuestos;
  private Double subTotal;
  private Double cantidad;
  private Double cantidadGarantia;
  private Double descuentos;
  private Double excedentes;
  private String nombre;
  private String sat;
  private String unidadMedida;
	private Long idOrdenDetalle;
	private Double solicitados;
	private Double valor;
	private Long idRedondear;
	private Double total;
	private Long idComodin;
	private Long idAplicar;
	private boolean aplicar;
	private boolean disponible;
	private Double precio;
	private Double utilidad;	
	private String origen;	
	private UISelectEntity ikAplicar;

  public ArticuloDetalle() {
    this(new Long(-1L));
  }

  public ArticuloDetalle(Long key) {
		this(key, "", 0D, "", "", 0D, new Timestamp(Calendar.getInstance().getTimeInMillis()), "", 16D, 0D, 0D, 1D, 0D, "", "", "", 0D, 2L, "");
  }

	public ArticuloDetalle(Long idArticulo, String codigo, Double costo, String descuento, String extras, Double importe, Timestamp registro, String propio, Double iva, Double impuestos, Double subTotal, Double cantidad, Double descuentos, String nombre, String sat, String unidadMedida, Double excedentes, Long idAplicar, String origen) {
		this(idArticulo, codigo, costo, descuento, extras, importe, registro, propio, iva, impuestos, subTotal, cantidad, descuentos, nombre, sat, unidadMedida, null, 0D, 1L, -1L, excedentes, idAplicar, origen);
	}
	
	public ArticuloDetalle(Long idArticulo, String codigo, Double costo, String descuento, String extras, Double importe, Timestamp registro, String propio, Double iva, Double impuestos, Double subTotal, Double cantidad, Double descuentos, String nombre, String sat, String unidadMedida, Long idOrdenDetalle, Double solicitados, Long idRedondear, Long idComodin, Double excedentes, Long idAplicar, String origen) {
		this(idArticulo, codigo, costo, descuento, extras, importe, registro, propio, iva, impuestos, subTotal, cantidad, descuentos, nombre, sat, unidadMedida, idOrdenDetalle, solicitados, idRedondear, idComodin, excedentes, idAplicar, 0D, 0D, origen);
	}
	
	public ArticuloDetalle(Long idArticulo, String codigo, Double costo, String descuento, String extras, Double importe, Timestamp registro, String propio, Double iva, Double impuestos, Double subTotal, Double cantidad, Double descuentos, String nombre, String sat, String unidadMedida, Long idOrdenDetalle, Double solicitados, Long idRedondear, Long idComodin, Double excedentes, Long idAplicar, Double precio, Double utilidad, String origen) {
		this(idArticulo, codigo, costo, descuento, extras, importe, registro, propio, iva, impuestos, subTotal, cantidad, descuentos, nombre, sat, unidadMedida, idOrdenDetalle, solicitados, idRedondear, idComodin, excedentes, idAplicar, precio, utilidad, null, origen);
	}
	
	public ArticuloDetalle(Long idArticulo, String codigo, Double costo, String descuento, String extras, Double importe, Timestamp registro, String propio, Double iva, Double impuestos, Double subTotal, Double cantidad, Double descuentos, String nombre, String sat, String unidadMedida, Long idOrdenDetalle, Double solicitados, Long idRedondear, Long idComodin, Double excedentes, Long idAplicar, Double precio, Double utilidad, String descuentoDescripcion, String origen) {
		this.idArticulo=idArticulo;
		this.codigo=codigo;
		this.costo=costo;
		this.descuento=descuento;
		this.extras=extras;
		this.importe=importe;
		this.registro=registro;
		this.propio=propio;
		this.iva=iva;
		this.impuestos=impuestos;
		this.subTotal=subTotal;
		this.cantidad=cantidad;
		this.descuentos=descuentos;
		this.nombre=nombre;
		this.sat=sat;
		this.unidadMedida=unidadMedida;
    this.idOrdenDetalle= idOrdenDetalle;
		this.solicitados= solicitados;
		this.valor= costo;
		this.idRedondear= idRedondear;
		this.total= importe;
		this.idComodin= idComodin;
		this.excedentes= excedentes;
		this.idAplicar= idAplicar;
		this.aplicar= idAplicar== null || idAplicar.equals(1L);
		this.disponible= true;
		this.precio= precio;
		this.utilidad= utilidad;
		this.descuentoDescripcion= descuentoDescripcion;
		this.cantidadGarantia= cantidad;
	  this.origen= origen;
		this.ikAplicar= new UISelectEntity(new Entity(0L));
	}
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return Numero.toRedondearSat(costo);
  }

  public String getCosto$() {
		return Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, this.getCosto());
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return Numero.toRedondearSat(importe);
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setPropio(String propio) {
    this.propio = propio;
  }

  public String getPropio() {
    return propio;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
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
    return Numero.toRedondearSat(cantidad);
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public String getSat() {
		return sat;
	}

	public void setSat(String sat) {
		this.sat=sat;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida=unidadMedida;
	}

	public Long getIdOrdenDetalle() {
		return idOrdenDetalle;
	}

	public void setIdOrdenDetalle(Long idOrdenDetalle) {
		this.idOrdenDetalle=idOrdenDetalle;
	}

	public Double getSolicitados() {
		return solicitados;
	}

	public void setSolicitados(Double solicitados) {
		this.solicitados=solicitados;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor=valor;
	}
	
	public Long getIdRedondear() {
		return idRedondear;
	}

	public void setIdRedondear(Long idRedondear) {
		this.idRedondear=idRedondear;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total=total;
	}

	public Long getIdComodin() {
		return idComodin;
	}

	public void setIdComodin(Long idComodin) {
		this.idComodin= idComodin;
	}

	public Double getExcedentes() {
		return excedentes;
	}

	public void setExcedentes(Double excedentes) {
		this.excedentes=excedentes;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getUtilidad() {
		return utilidad;
	}

	public void setUtilidad(Double utilidad) {
		this.utilidad = utilidad;
	}

	public String getDescuentoDescripcion() {
		return descuentoDescripcion;
	}
	
	public void setDescuentoDescripcion(String descuentoDescripcion) {
		this.descuentoDescripcion = descuentoDescripcion;
	}	

	public Double getCantidadGarantia() {
		return cantidadGarantia;
	}

	public void setCantidadGarantia(Double cantidadGarantia) {
		this.cantidadGarantia = cantidadGarantia;
	}	

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen=origen;
	}
	
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
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPropio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSolicitados());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getValor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdComodin());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUtilidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuentoDescripcion());
    regresar.append("]");
  	return regresar.toString();
  }

	@Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("costo", getCosto());
		regresar.put("descuento", getDescuento());
		regresar.put("extras", getExtras());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
		regresar.put("propio", getPropio());
		regresar.put("iva", getIva());
		regresar.put("impuestos", getImpuestos());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("descuentos", getDescuentos());
		regresar.put("nombre", getNombre());
		regresar.put("idOrdenDetalle", getIdOrdenDetalle());
		regresar.put("solicitados", getSolicitados());
		regresar.put("valor", getValor());
		regresar.put("total", getTotal());
		regresar.put("idComodin", getIdComodin());
		regresar.put("precio", getPrecio());
		regresar.put("utilidad", getUtilidad());
		regresar.put("descuentoDescripcion", getDescuentoDescripcion());
  	return regresar;
  }

  public boolean isComodin() {
  	return this.idComodin!= null && this.idComodin!=-1L;
  }

	public Long getIdAplicar() {
		return idAplicar;
	}

	public void setIdAplicar(Long idAplicar) {
		this.idAplicar=idAplicar;
		if(this.idAplicar!= null)
			this.aplicar= this.idAplicar.equals(1L);
	} 

	public boolean isAplicar() {
		return aplicar;
	}

	public void setAplicar(boolean aplicar) {
		this.aplicar=aplicar;
		if(this.aplicar)
			this.idAplicar= 1L;
		else
			this.idAplicar= 2L;
	}

	public UISelectEntity getIkAplicar() {
		return ikAplicar;
	}

	public void setIkAplicar(UISelectEntity ikAplicar) {
		this.ikAplicar=ikAplicar;
		if(this.ikAplicar!= null)
		  this.setIdAplicar(this.ikAplicar.getKey());
	}
	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible=disponible;
	}

	@Override
  public boolean isValid() {
  	return this.idArticulo!= null && this.idArticulo!=-1L;
  }

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public Object toValue(String name) {
		return null;
	}

	@Override
	public String toAllKeys() {
		return null;
	}

	@Override
	public String toKeys() {
		return null;
	}

	@Override
	public Class toHbmClass() {
		return null;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=53*hash+Objects.hashCode(this.idArticulo);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final ArticuloDetalle other=(ArticuloDetalle) obj;
		if (!Objects.equals(this.idArticulo, other.idArticulo)) {
			return false;
		}
		return true;
	}	
	
}
