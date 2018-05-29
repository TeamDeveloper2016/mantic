package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesDetallesDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
 
	private TcManticOrdenesComprasDto orden;	
	private List<Articulo> articulos;
	private String messageError;
	private TcManticOrdenesBitacoraDto bitacora;

	public Transaccion(TcManticOrdenesBitacoraDto bitacora) {
		this.bitacora= bitacora;
	}
	
	public Transaccion(TcManticOrdenesComprasDto orden) {
		this(orden, new ArrayList<Articulo>());
	}

	public Transaccion(TcManticOrdenesComprasDto orden, List<Articulo> articulos) {
		this.orden    = orden;		
		this.articulos= articulos;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		TcManticOrdenesBitacoraDto bitacoraOrden= null;
		Map<String, Object> params= new HashMap<>();
		try {
			if(this.orden!= null)
				params.put("idOrdenCompra", this.orden.getIdOrdenCompra());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para la orden de compra.");
			switch(accion) {
				case AGREGAR:
					Long consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.orden.setOrden(consecutivo);
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
					if(this.orden.getIdCliente()< 0)
						this.orden.setIdCliente(null);
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitacoraOrden= new TcManticOrdenesBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdOrdenCompra(), this.orden.getIdOrdenEstatus());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraOrden)>= 1L;
					toFillArticulos(sesion);
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					toFillArticulos(sesion);
					break;				
				case ELIMINAR:
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticOrdenesDetallesDto.class, params)>= 1L;
					regresar= regresar && DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					bitacoraOrden= new TcManticOrdenesBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdOrdenCompra(), 2L);
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraOrden)>= 1L;
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L){
						this.orden= (TcManticOrdenesComprasDto) DaoFactory.getInstance().findById(sesion, TcManticOrdenesComprasDto.class, this.bitacora.getIdOrdenCompra());
						this.orden.setIdOrdenEstatus(this.bitacora.getIdOrdenEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					} // if
					break;
			} // switch
			if(!regresar)
        throw new Exception(this.messageError);
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError);
		} // catch		
		LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar

	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticOrdenesDetallesDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item);
		for (Articulo articulo: this.articulos) {
			TcManticOrdenesDetallesDto item= articulo.toOrdenDetalle();
			item.setIdOrdenCompra(this.orden.getIdOrdenCompra());
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticOrdenesDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
		} // for
	}
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			regresar= DaoFactory.getInstance().toField(sesion, "TcManticOrdenesComprasDto", "siguiente", params, "siguiente").toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
} 