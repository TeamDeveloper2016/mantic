package mx.org.kaana.mantic.sincronizar.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticApartadosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.db.dto.TcManticApartadosPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.db.dto.TrManticVentaMedioPagoDto;
import mx.org.kaana.mantic.sincronizar.enums.EDocumento;
import mx.org.kaana.mantic.sincronizar.reglas.ISincronizar;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/05/2019
 *@time 10:27:41 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Ventas implements Serializable, ISincronizar {

	private static final long serialVersionUID=2970307152164147431L;
	private static final Log LOG= LogFactory.getLog(Ventas.class);
	
	private TcManticVentasDto venta; 
	private List<TcManticVentasDetallesDto> vdetalle;
	private List<TcManticVentasBitacoraDto> vbitacora;
	private List<TrManticVentaMedioPagoDto> vmedios;
	private List<TcManticClientesDeudasDto> vdeudas;
	private List<TcManticClientesPagosDto> vpagos;

	private List<TcManticApartadosDto> apartados;
	private List<TcManticApartadosBitacoraDto> abitacora;
	private List<TcManticApartadosPagosDto> apagos;

	public Ventas(Long idVenta) {
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idVenta", idVenta);
			this.venta   = (TcManticVentasDto)DaoFactory.getInstance().findById(TcManticVentasDto.class, idVenta);
			this.vdetalle = (List<TcManticVentasDetallesDto>)DaoFactory.getInstance().findViewCriteria(TcManticVentasDetallesDto.class, params, "detalle");
			this.vbitacora= (List<TcManticVentasBitacoraDto>)DaoFactory.getInstance().findViewCriteria(TcManticVentasBitacoraDto.class, params, "detalle");
			this.vmedios  = (List<TrManticVentaMedioPagoDto>)DaoFactory.getInstance().findViewCriteria(TrManticVentaMedioPagoDto.class, params, "detalle");
			this.vdeudas  = (List<TcManticClientesDeudasDto>)DaoFactory.getInstance().findViewCriteria(TcManticClientesDeudasDto.class, params, "detalle");
			if(this.vdeudas!= null && !this.vdeudas.isEmpty()) {
				for (TcManticClientesDeudasDto deuda: this.vdeudas) {
    			params.put("idClienteDeuda", deuda.getIdClienteDeuda());
  			  this.vpagos= (List<TcManticClientesPagosDto>)DaoFactory.getInstance().findViewCriteria(TcManticClientesPagosDto.class, params, "detalle");
				} // for
		  } // if
			this.apartados = (List<TcManticApartadosDto>)DaoFactory.getInstance().findViewCriteria(TcManticApartadosDto.class, params, "detalle");
			if(this.apartados!= null && !this.apartados.isEmpty()) {
   			params.put("idApartado", this.apartados.get(0).getIdApartado());
			  this.abitacora= (List<TcManticApartadosBitacoraDto>)DaoFactory.getInstance().findViewCriteria(TcManticApartadosBitacoraDto.class, params, "detalle");
			  this.apagos   = (List<TcManticApartadosPagosDto>)DaoFactory.getInstance().findViewCriteria(TcManticApartadosPagosDto.class, params, "detalle");
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

	@Override
	public EDocumento getDocumento() {
		return EDocumento.VENTAS;
	}

	public TcManticVentasDto getVenta() {
		return venta;
	}

	public List<TcManticVentasDetallesDto> getVdetalle() {
		return vdetalle;
	}

	public List<TcManticVentasBitacoraDto> getVbitacora() {
		return vbitacora;
	}

	public List<TrManticVentaMedioPagoDto> getVmedios() {
		return vmedios;
	}

	public List<TcManticClientesDeudasDto> getVdeudas() {
		return vdeudas;
	}

	public List<TcManticClientesPagosDto> getVpagos() {
		return vpagos;
	}

	public List<TcManticApartadosDto> getApartados() {
		return apartados;
	}

	public List<TcManticApartadosBitacoraDto> getAbitacora() {
		return abitacora;
	}

	public List<TcManticApartadosPagosDto> getApagos() {
		return apagos;
	}

	public String toEncode64() {
	  return Base64.encodeBase64String(this.toString().getBytes());
	}
	
	@Override
	public String toString() {
		String regresar= null;
		try {
  		regresar= Decoder.toJson(this);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		return regresar;
	}

  public static void main(String ... args) {
	  Ventas ventas= new Ventas(58L);
		String bs64  = ventas.toEncode64();
		LOG.info(ventas.toString());
		LOG.info(ventas.toEncode64());
		LOG.info(new String(Base64.decodeBase64(bs64)));
	}	
	
}
