package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 15/06/2018
 *@time 09:59:37 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class Inventarios extends IBaseTnx implements Serializable {

	private static final long serialVersionUID=-3572599320057390307L;
	
	private Long idAlmacen;
	protected Long idProveedor;

	public Inventarios(Long idAlmacen, Long idProveedor) {
		this.idAlmacen  = idAlmacen;
		this.idProveedor= idProveedor;
	}
	
	protected void toAffectAlmacenes(Session sesion, String consecutivo, Long idNotaEntrada, TcManticNotasDetallesDto item, Articulo codigos) throws Exception {
		Map<String, Object> params= null;
		double stock= 0D;
		try {
			params= new HashMap<>();
			params.put("idAlmacen", this.idAlmacen);
			params.put("idArticulo", item.getIdArticulo());
			params.put("idProveedor", this.idProveedor);
			TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class,  params, "ubicacion");
			if(ubicacion== null) {
			  TcManticAlmacenesUbicacionesDto general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
				if(general== null) {
  				general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.idAlmacen, -1L);
					DaoFactory.getInstance().insert(sesion, general);
				} // if	
			  Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "TcManticArticulosDto", "inventario", params);
				TcManticAlmacenesArticulosDto articulo= new TcManticAlmacenesArticulosDto(entity.toDouble("minimo"), -1L, general.getIdUsuario(), general.getIdAlmacen(), entity.toDouble("maximo"), general.getIdAlmacenUbicacion(), item.getIdArticulo(), item.getCantidad());
				DaoFactory.getInstance().insert(sesion, articulo);
		  } // if
			else { 
				stock= ubicacion.getStock();
				ubicacion.setStock(ubicacion.getStock()+ item.getCantidad());
				DaoFactory.getInstance().update(sesion, ubicacion);
			} // if

			// generar un registro en la bitacora de movimientos de los articulos 
			TcManticMovimientosDto entrada= new TcManticMovimientosDto(
			  consecutivo, // String consecutivo, 
				1L, // Long idTipoMovimiento, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				this.idAlmacen, // Long idAlmacen, 
				-1L, // Long idMovimiento, 
				item.getCantidad(), // Double cantidad, 
				item.getIdArticulo(), // Long idArticulo, 
				stock, // Double stock, 
				Numero.toRedondearSat(stock+ item.getCantidad()), // Double calculo
				null // String observaciones
		  );
			DaoFactory.getInstance().insert(sesion, entrada);
			
			// registar el cambio de precios en la bitacora de articulo 
			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
			
			TcManticArticulosBitacoraDto movimiento= new TcManticArticulosBitacoraDto(global.getIva(), JsfBase.getIdUsuario(), global.getMayoreo(), -1L, global.getMenudeo(), global.getCantidad(), global.getIdArticulo(), idNotaEntrada, global.getMedioMayoreo(), global.getPrecio(), global.getLimiteMedioMayoreo(), global.getLimiteMayoreo(), global.getDescuento(), global.getExtra());			
			DaoFactory.getInstance().insert(sesion, movimiento);
			
			// afectar el inventario general de articulos dentro del almacen
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().findFirst(sesion, TcManticInventariosDto.class, "inventario", params);
			if(inventario== null)
				DaoFactory.getInstance().insert(sesion, new TcManticInventariosDto(JsfBase.getIdUsuario(), this.idAlmacen, item.getCantidad(), -1L, item.getIdArticulo(), 0D, item.getCantidad(), 0D, new Long(Calendar.getInstance().get(Calendar.YEAR)), 1L));
			else {
				inventario.setEntradas(inventario.getEntradas()+ item.getCantidad());
				inventario.setStock(inventario.getStock()+ item.getCantidad());
				DaoFactory.getInstance().update(sesion, inventario);
			} // else
			
			// afectar los precios del catalogo de articulos
			if(!Cadena.isVacio(codigos.getSat()))
			  global.setSat(codigos.getSat());
			// esto aplica para cuando el precio que llega es mayor al registrado dejar el nuevo
			Descuentos descuentos= new Descuentos(item.getCosto(), item.getDescuento());
			double costo= descuentos.getFactor()== 0D? item.getCosto(): descuentos.toImporte();
			// si esta marcado como afectar los costos se aplicara el cambio en el catalogo de articulos
			if(codigos.getIdAplicar().equals(1L) || costo> global.getPrecio()) {
				// aplicar el descuento sobre el valor del costo del articulo para afectar el catalogo
				double menudeo= Numero.toRedondearSat((global.getMenudeo()* 100/ global.getPrecio())/ 100);
				double medio  = Numero.toRedondearSat((global.getMedioMayoreo()* 100/ global.getPrecio())/ 100);
				double mayoreo= Numero.toRedondearSat((global.getMayoreo()* 100/ global.getPrecio())/ 100);
				
			  global.setPrecio(Numero.toRedondearSat(costo));
			  global.setMenudeo(Numero.toAjustarDecimales(global.getPrecio()* menudeo, global.getIdRedondear().equals(1L)));
			  global.setMedioMayoreo(Numero.toAjustarDecimales(global.getPrecio()* medio, global.getIdRedondear().equals(1L)));
			  global.setMayoreo(Numero.toAjustarDecimales(global.getPrecio()* mayoreo, global.getIdRedondear().equals(1L)));
				global.setDescuento(item.getDescuento());
				global.setExtra(item.getExtras());
			} // if	
			else {
			  global.setPrecio(Numero.toRedondearSat(costo));
				// ajustar solo los decimales cuando sea redondear 
				if(global.getIdRedondear().equals(1L)) {
					global.setMenudeo(Numero.toAjustarDecimales(global.getMenudeo(), true));
					global.setMedioMayoreo(Numero.toAjustarDecimales(global.getMedioMayoreo(), true));
					global.setMayoreo(Numero.toAjustarDecimales(global.getMayoreo(), true));
				} // if
			} // else
			// siempre se modifica el costo del catalogo de articulo 
			global.setActualizado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			global.setStock(global.getStock()+ item.getCantidad());
			DaoFactory.getInstance().update(sesion, global);
			
			// afectar el catalogo de codigos del proveedor y si se encuentra entonces actualizarlo en caso de ser diferente al que se tenia
			if(!Cadena.isVacio(codigos.getCodigo())) {
				TcManticArticulosCodigosDto remplazo= (TcManticArticulosCodigosDto)DaoFactory.getInstance().findFirst(sesion, TcManticArticulosCodigosDto.class, "codigo", params);
				if(remplazo== null) {
					Value next= DaoFactory.getInstance().toField(sesion, "TcManticArticulosCodigosDto", "siguiente", params, "siguiente");
					if(next.getData()== null)
						next.setData(1L);
					DaoFactory.getInstance().insert(sesion, new TcManticArticulosCodigosDto(codigos.getCodigo(), this.idProveedor, JsfBase.getIdUsuario(), 2L, "", -1L, next.toLong(), codigos.getIdArticulo()));
				} // if	
				else 
					if(!Objects.equals(remplazo.getCodigo(), codigos.getCodigo())) {
					  remplazo.setCodigo(codigos.getCodigo());
					  DaoFactory.getInstance().update(sesion, remplazo);
  				} // else	
			} // if	
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}	

	protected void toCommonNotaEntrada(Session sesion, Long idNotaEntrada, Map<String, Object> params) throws Exception {
		List<TcManticNotasEntradasDto> notas= (List<TcManticNotasEntradasDto>)DaoFactory.getInstance().findViewCriteria(sesion, TcManticNotasEntradasDto.class, params, "consulta");
		// Recuperar todas las notas de entrada abiertas para cerrarlas y aplicarlas
		for (TcManticNotasEntradasDto nota: notas) {
			if(!nota.getIdNotaEstatus().equals(3L) && !nota.getIdNotaEntrada().equals(idNotaEntrada)) {
				nota.setIdNotaEstatus(3L);
				DaoFactory.getInstance().update(sesion, nota);
				TcManticNotasBitacoraDto registro= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), nota.getIdNotaEntrada(), nota.getIdNotaEstatus(), nota.getConsecutivo(), nota.getTotal());
				DaoFactory.getInstance().insert(sesion, registro);
        
				// Afectar todas las notas de entrada que ya fueron aceptadas para agregarlas como cuentas por pagar
				TcManticEmpresasDeudasDto deuda= new TcManticEmpresasDeudasDto(1L, JsfBase.getIdUsuario(), -1L, "", JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), nota.getDeuda()* -1L, nota.getIdNotaEntrada(), nota.getFechaPago(), nota.getDeuda()- nota.getExcedentes(), nota.getDeuda()- nota.getExcedentes());
				DaoFactory.getInstance().insert(sesion, deuda);
				
    		// Recuperar el detalle de las notas de entrada para afectas inventarios 
    		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaNotasEntradasDto", "detalle", nota.toMap());
				for (Articulo articulo: todos) {
					TcManticNotasDetallesDto item= articulo.toNotaDetalle();
					// Si la cantidad es mayor a cero realizar todo el proceso para el articulos, si no ignorarlo porque el articulo no se surtio
					if(articulo.getCantidad()> 0L)
    		    this.toAffectAlmacenes(sesion, nota.getConsecutivo(), nota.getIdNotaEntrada(), item, articulo);
				} // for
			} // if	
		} // for
	}
	
}
