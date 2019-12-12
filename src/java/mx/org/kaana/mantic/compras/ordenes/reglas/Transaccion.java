package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.proveedores.beans.ProveedorTipoContacto;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesDetallesDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorTipoContactoDto;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.Correo;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends Inventarios implements Serializable {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID=-3186367186737677670L;
 
	private TcManticOrdenesComprasDto orden;	
	private List<Articulo> articulos;
	private String messageError;
	private TcManticOrdenesBitacoraDto bitacora;
	private Long idFaltante;
	private Correo correo;	

	public Transaccion(Correo correo, Long idProveedor) {
		super(-1L, idProveedor);
		this.correo     = correo;
	}	// Transaccion
	
	public Transaccion(TcManticOrdenesComprasDto orden, TcManticOrdenesBitacoraDto bitacora) {
		this(orden);
		this.bitacora= bitacora;
	} // Transaccion
	
	public Transaccion(TcManticOrdenesComprasDto orden) {
		this(orden, new ArrayList<Articulo>());
	} // Transaccion

	public Transaccion(TcManticOrdenesComprasDto orden, List<Articulo> articulos) {
		super(orden.getIdAlmacen(), orden.getIdProveedor());
		this.orden    = orden;		
		this.articulos= articulos;
	} // Transaccion

	public Transaccion(Long idFaltante) {
		super(-1L, -1L);
		this.idFaltante= idFaltante;
	} // Transaccion
	
	public String getMessageError() {
		return messageError;
	} // getMessageError

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar                        = false;
		TcManticOrdenesBitacoraDto bitacoraOrden= null;
		Map<String, Object> params              = new HashMap<>();
		try {
			if(this.orden!= null)
				params.put("idOrdenCompra", this.orden.getIdOrdenCompra());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para la orden de compra.");
			if(this.orden!= null && this.orden.getIdCliente()!= null && this.orden.getIdCliente()< 0)
				this.orden.setIdCliente(null);
			switch(accion) {
				case MOVIMIENTOS:
					if(this.orden.isValid()) {
  					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
	  				this.toFillArticulos(sesion);
					} // if
					else {
						Siguiente consecutivo= this.toSiguiente(sesion);
						this.orden.setConsecutivo(consecutivo.getConsecutivo());
						this.orden.setOrden(consecutivo.getOrden());
						this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
						regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
						this.toFillArticulos(sesion);
						bitacoraOrden= new TcManticOrdenesBitacoraDto(this.orden.getIdOrdenEstatus(), "", JsfBase.getIdUsuario(), this.orden.getIdOrdenCompra(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
						regresar= DaoFactory.getInstance().insert(sesion, bitacoraOrden)>= 1L;
					} // else	
      		for (Articulo articulo: this.articulos) 
						articulo.setModificado(false);
					break;
				case AGREGAR:
					Siguiente consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(consecutivo.getConsecutivo());
					this.orden.setOrden(consecutivo.getOrden());
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					this.toFillArticulos(sesion);
					bitacoraOrden= new TcManticOrdenesBitacoraDto(this.orden.getIdOrdenEstatus(), "", JsfBase.getIdUsuario(), this.orden.getIdOrdenCompra(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraOrden)>= 1L;
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					this.toFillArticulos(sesion);
					bitacoraOrden= (TcManticOrdenesBitacoraDto)DaoFactory.getInstance().findFirst(sesion, TcManticOrdenesBitacoraDto.class, this.orden.toMap(), "ultimo");
					if(!bitacoraOrden.getImporte().equals(this.orden.getTotal())) {
  					bitacoraOrden= new TcManticOrdenesBitacoraDto(this.orden.getIdOrdenEstatus(), "", JsfBase.getIdUsuario(), this.orden.getIdOrdenCompra(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
  					regresar= DaoFactory.getInstance().insert(sesion, bitacoraOrden)>= 1L;
					} // if
					break;				
				case ELIMINAR:
					regresar= this.toNotExistsNotas(sesion);
					if(regresar) {
						this.orden.setIdOrdenEstatus(2L);
						// regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticOrdenesDetallesDto.class, params)>= 1L;
						regresar= regresar && DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						bitacoraOrden= new TcManticOrdenesBitacoraDto(2L, "", JsfBase.getIdUsuario(), this.orden.getIdOrdenCompra(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
						regresar= DaoFactory.getInstance().insert(sesion, bitacoraOrden)>= 1L;
					} // if	
					else
       			this.messageError= "No se puede eliminar la orden de compra porque existen notas de entrada asociadas.";
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.orden.setIdOrdenEstatus(this.bitacora.getIdOrdenEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						if(this.orden.getIdOrdenEstatus().equals(7L)) 
							this.toCommonNotaEntrada(sesion, -1L, this.orden.toMap());
					} // if
					break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticFaltantesDto.class, this.idFaltante)>= 1L;
					break;
				case COMPLEMENTAR: 
					regresar= this.agregarContacto(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		if(this.orden!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar

	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaOrdenesComprasDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item.toOrdenDetalle());
		for (Articulo articulo: this.articulos) {
			TcManticOrdenesDetallesDto item= articulo.toOrdenDetalle();
			item.setIdOrdenCompra(this.orden.getIdOrdenCompra());
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticOrdenesDetallesDto.class, item.toMap())== null) 
		    DaoFactory.getInstance().insert(sesion, item);
			else
				if(articulo.isModificado())
		      DaoFactory.getInstance().update(sesion, item);
			articulo.setObservacion("ARTICULO SOLICITADO EN LA ORDEN DE COMPRA ".concat(this.orden.getConsecutivo()).concat(" EL DIA ").concat(Global.format(EFormatoDinamicos.FECHA_HORA_CORTA, this.orden.getRegistro())));
			DaoFactory.getInstance().updateAll(sesion, TcManticFaltantesDto.class, articulo.toMap());
		} // for
	}
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", this.orden.getIdEmpresa());
		  params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticOrdenesComprasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
			else
			  regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private boolean toNotExistsNotas(Session sesion) throws Exception {
		boolean regresar= true;
		Value total= DaoFactory.getInstance().toField(sesion, "TcManticNotasEntradasDto", "existe", this.orden.toMap(), "total");
		if(total.getData()!= null)
		  regresar= total.toLong()<= 0;
		return regresar;
	}
	
	private boolean agregarContacto(Session sesion) throws Exception{
		boolean regresar                         = true;
		List<ProveedorTipoContacto> correos      = null;
		TrManticProveedorTipoContactoDto contacto= null;
		int count                                = 0;
		Long records                             = 1L;
		try {
			correos= toProveedoresTipoContacto();
			if(!correos.isEmpty()){
				for(ProveedorTipoContacto tipoContacto: correos){
					if(tipoContacto.getValor().equals(this.correo.getDescripcion()))
						count++;
				} // for				
				records= correos.size() + 1L;
			} // if
			if(count== 0){
				contacto= new TrManticProveedorTipoContactoDto();
				contacto.setIdProveedor(this.idProveedor);
				contacto.setIdTipoContacto(ETiposContactos.CORREO.getKey());
				contacto.setIdUsuario(JsfBase.getIdUsuario());
				contacto.setValor(this.correo.getDescripcion());
				contacto.setOrden(records);
				regresar= DaoFactory.getInstance().insert(sesion, contacto)>= 1L;
			} // else
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // agregarContacto
	
	public List<ProveedorTipoContacto> toProveedoresTipoContacto() throws Exception {
		List<ProveedorTipoContacto> regresar= null;
		Map<String, Object>params           = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_proveedor=" + this.idProveedor + " and id_tipo_contacto=" + ETiposContactos.CORREO.getKey());
			regresar= DaoFactory.getInstance().toEntitySet(ProveedorTipoContacto.class, "TrManticProveedorTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
} 