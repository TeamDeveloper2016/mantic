package mx.org.kaana.mantic.catalogos.inventarios.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
	private TcManticInventariosDto articulo;
	private TcManticAlmacenesArticulosDto almacen;
	private String messageError;

	public Transaccion(TcManticInventariosDto articulo, TcManticAlmacenesArticulosDto almacen) {
		this.articulo= articulo;
		this.almacen = almacen;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
    this.messageError= "Error al registrar el inventario, verifique que los datos de captura esten completos.";
		try {
			switch(accion) {
				case AGREGAR:
					this.toAffectAlmacenes(sesion, accion);
					this.articulo.setStock(this.articulo.getInicial());
          regresar = DaoFactory.getInstance().insert(sesion, this.articulo)> 0L;
          break;        
				case MODIFICAR:
					this.toAffectAlmacenes(sesion, accion);
					this.articulo.setStock(this.articulo.getInicial());
          regresar = DaoFactory.getInstance().update(sesion, this.articulo)> 0L;
          break;        
				case PROCESAR:
					this.toAffectAlmacenes(sesion, accion);
					regresar= true;
          break;        
			} // switch
			if (!regresar) 
        throw new Exception("");      
		} // try
		catch (Exception e) {
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	} 

	private void toAffectAlmacenes(Session sesion, EAccion accion) throws Exception {
		Map<String, Object> params= null;
		double stock              = this.articulo.getInicial();
		try {
			params=new HashMap<>();
			this.almacen.setStock(this.articulo.getInicial());
			if(this.almacen.isValid()) 
				DaoFactory.getInstance().update(sesion, this.almacen);
			else {
				params.put("idAlmacen", this.articulo.getIdAlmacen());
				params.put("idArticulo", this.articulo.getIdArticulo());
				TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class, params, "ubicacion");
				if(ubicacion== null) {
					TcManticAlmacenesUbicacionesDto general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
					if(general== null) {
						general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.articulo.getIdAlmacen(), -1L);
						DaoFactory.getInstance().insert(sesion, general);
					} // if	
					this.almacen.setIdAlmacenUbicacion(general.getIdAlmacenUbicacion());
					DaoFactory.getInstance().insert(sesion, this.almacen);
				} // if		
			} // else	
			// afectar el stock global del articulo basado en las diferencias que existian en el almacen origen
			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.articulo.getIdArticulo());
			if(global!= null) {
				global.setStock(this.toSumAlmacenArticulo(sesion, this.almacen.getIdArticulo()));
  			DaoFactory.getInstance().update(sesion, global);
			} // if
			else
				LOG.error("El articulos ["+ this.articulo.getIdArticulo()+ "] no existe hay que verificarlo !");
			if(!accion.equals(EAccion.PROCESAR)) {
				// generar un registro en la bitacora de movimientos de los articulos 
				TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
					"VER", // String consecutivo, 
					6L, // Long idTipoMovimiento, 
					JsfBase.getIdUsuario(), // Long idUsuario, 
					this.almacen.getIdAlmacen(), // Long idAlmacen, 
					-1L, // Long idMovimiento, 
					0D, // Double cantidad, 
					articulo.getIdArticulo(), // Long idArticulo, 
					stock, // Double stock, 
					Numero.toRedondearSat(stock), // Double calculo
					null // String observaciones
				);
				DaoFactory.getInstance().insert(sesion, movimiento);
			} // if
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}

  private Double toSumAlmacenArticulo(Session sesion, Long idArticulo) {
		Double regresar           = 0D;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idArticulo", idArticulo);
			Value value= DaoFactory.getInstance().toField(sesion, "VistaKardexDto", "existencias", params, "total");
			if(value!= null && value.getData()!= null)
				regresar= value.toDouble();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSumAlmacenArticulo

}
