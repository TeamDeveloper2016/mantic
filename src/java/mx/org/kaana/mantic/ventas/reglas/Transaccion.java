package mx.org.kaana.mantic.ventas.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDetallesDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx {

  private static final Logger LOG  = Logger.getLogger(Transaccion.class);
	private static final String VENTA= "VENTA";
	private TcManticVentasDto orden;	
	private List<Articulo> articulos;
	private String messageError;
	private TcManticVentasBitacoraDto bitacora;

	public Transaccion(TcManticVentasBitacoraDto bitacora) {
		this.bitacora= bitacora;
	}
	
	public Transaccion(TcManticVentasDto orden) {
		this(orden, new ArrayList<Articulo>());
	}

	public Transaccion(TcManticVentasDto orden, List<Articulo> articulos) {
		this.orden    = orden;		
		this.articulos= articulos;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar                  = false;
		TcManticVentasBitacoraDto bitVenta= null;
		Map<String, Object> params        = null;
		Long idEstatusVenta               = null;
		try {
			idEstatusVenta= EEstatusVentas.ELABORADA.getIdEstatusVenta();
			params= new HashMap<>();
			if(this.orden!= null)
				params.put("idVenta", this.orden.getIdVenta());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el ticket de venta.");
			switch(accion) {
				case AGREGAR:
				case REGISTRAR:					
					if(accion.equals(EAccion.AGREGAR))									
						idEstatusVenta= EEstatusVentas.ABIERTA.getIdEstatusVenta();
					Long consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(consecutivo);
					this.orden.setOrden(consecutivo);
					this.orden.setIdVentaEstatus(idEstatusVenta);
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
					if(this.orden.getIdCliente()< 0)
						this.orden.setIdCliente(toClienteDefault(sesion));
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitVenta= new TcManticVentasBitacoraDto(this.orden.getIdVentaEstatus(), "", JsfBase.getIdUsuario(), this.orden.getIdVenta(), idEstatusVenta);
					regresar= DaoFactory.getInstance().insert(sesion, bitVenta)>= 1L;
					toFillArticulos(sesion);
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					toFillArticulos(sesion);
					break;				
				case ELIMINAR:
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticVentasDetallesDto.class, params)>= 1L;
					regresar= regresar && DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					bitVenta= new TcManticVentasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdVenta(), 2L);
					regresar= DaoFactory.getInstance().insert(sesion, bitVenta)>= 1L;
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L){
						this.orden= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.bitacora.getIdVenta());
						this.orden.setIdVentaEstatus(this.bitacora.getIdVenta());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					} // if
					break;									
			} // switch
			if(!regresar)
        throw new Exception(this.messageError);
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar

	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticVentasDetallesDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item);
		for (Articulo articulo: this.articulos) {
			TcManticVentasDetallesDto item= articulo.toVentaDetalle();
			item.setIdVenta(this.orden.getIdVenta());
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticVentasDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
		} // for
	}
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		Value siguiente           = null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("dia", Fecha.getHoyEstandar());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			siguiente= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguiente", params, "siguiente");
			if(siguiente.getData()!= null)
				regresar= siguiente.toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private Long toClienteDefault(Session sesion) throws Exception{
		Long regresar            = -1L;
		Entity cliente           = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("clave", VENTA);
			params.put("sucursales", JsfBase.getAutentifica().getIdsSucursales());
			cliente= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcManticClientesDto", "clienteDefault", params);
			if(cliente!= null)
				regresar= cliente.getKey();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClienteDefault
} 