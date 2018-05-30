package mx.org.kaana.mantic.comun.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.libs.Constantes;

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
  private String extras;
  private Double importe;
  private Timestamp registro;
  private String propio;
  private Double iva;
  private Double impuestos;
  private Double subTotal;
  private Long cantidad;
  private Double descuentos;
  private String nombre;
  private String sat;
  private String unidadMedida;

  public ArticuloDetalle() {
    this(new Long(-1L));
  }

  public ArticuloDetalle(Long key) {
		this(key, "", 0D, "", "", 0D, new Timestamp(Calendar.getInstance().getTimeInMillis()), "", 16D, 0D, 0D, 1L, 0D, "", "", "");
  }

	public ArticuloDetalle(Long idArticulo, String codigo, Double costo, String descuento, String extras, Double importe, Timestamp registro, String propio, Double iva, Double impuestos, Double subTotal, Long cantidad, Double descuentos, String nombre, String sat, String unidadMedida) {
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
    return costo;
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
    return importe;
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

  public void setCantidad(Long cantidad) {
    this.cantidad = cantidad;
  }

  public Long getCantidad() {
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
  	return regresar;
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
