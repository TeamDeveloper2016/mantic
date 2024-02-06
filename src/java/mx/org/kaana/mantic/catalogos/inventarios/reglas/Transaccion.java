package mx.org.kaana.mantic.catalogos.inventarios.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.inventarios.beans.ArticuloAlmacen;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
	private TcManticInventariosDto inventario;
	private ArticuloAlmacen almacen;
	private String messageError;

	public Transaccion(TcManticInventariosDto inventario, ArticuloAlmacen almacen) {
		this.inventario= inventario;
		this.almacen = almacen;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
    this.messageError= "Error al registrar el inventario, verifique que los datos de captura esten completos.";
		try {
			switch(accion) {
				case AGREGAR:
					regresar= this.toAffectAlmacenes(sesion, accion);
          break;        
				case MODIFICAR:
					regresar= this.toAffectAlmacenes(sesion, accion);
          break;        
				case PROCESAR:
					regresar= this.toAffectAlmacenes(sesion, accion);
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

	private boolean toAffectAlmacenes(Session sesion, EAccion accion) throws Exception {
		Map<String, Object> params= new HashMap<>();
    boolean regresar          = Boolean.FALSE; 
		try {
			this.almacen.setStock(this.inventario.getInicial());
			if(this.almacen.isValid()) 
				DaoFactory.getInstance().update(sesion, this.almacen);
			else {
				params.put("idAlmacen", this.inventario.getIdAlmacen());
				params.put("idArticulo", this.inventario.getIdArticulo());
				TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class, params, "ubicacion");
				if(ubicacion== null) {
					TcManticAlmacenesUbicacionesDto general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
					if(general== null) {
						general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.inventario.getIdAlmacen(), -1L);
						DaoFactory.getInstance().insert(sesion, general);
					} // if	
					this.almacen.setIdAlmacenUbicacion(general.getIdAlmacenUbicacion());
					DaoFactory.getInstance().insert(sesion, this.almacen);
				} // if		
			} // else	
			Long idEmpresa= JsfBase.getAutentifica().getEmpresa().getIdEmpresa();
			params.put("idAlmacen", this.almacen.getIdAlmacen());
			Value empresa= DaoFactory.getInstance().toField(sesion, "TcManticAlmacenesDto", "empresa", params, "idEmpresa");
			if(empresa.getData()!= null)
				idEmpresa= empresa.toLong();
			// QUITAR DE LAS VENTAS PERDIDAS LOS ARTICULOS QUE FUERON YA SURTIDOS EN EL ALMACEN
			params.put("idArticulo", this.inventario.getIdArticulo());
			params.put("idEmpresa", idEmpresa);
			params.put("observaciones", "ESTE ARTICULO FUE CONTADO EL DIA "+ Fecha.getHoyExtendido());
			DaoFactory.getInstance().updateAll(sesion, TcManticFaltantesDto.class, params);
			// afectar el stock global del articulo basado en las diferencias que existian en el almacen origen
			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, this.inventario.getIdArticulo());
			if(global!= null) {
				global.setStock(this.toSumAlmacenArticulo(sesion, this.almacen.getIdArticulo()));
        global.setIdVerificado(this.almacen.getIdVerificado());
  			DaoFactory.getInstance().update(sesion, global);
			} // if
			else
				LOG.error("El articulos ["+ this.inventario.getIdArticulo()+ "] no existe hay que verificarlo !");
			if(!accion.equals(EAccion.PROCESAR)) {
				// generar un registro en la bitacora de movimientos de los articulos 
				TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
					"VER", // String consecutivo, 
					6L, // Long idTipoMovimiento, 
					JsfBase.getIdUsuario(), // Long idUsuario, 
					this.almacen.getIdAlmacen(), // Long idAlmacen, 
					-1L, // Long idMovimiento, 
					0D, // Double cantidad, 
					inventario.getIdArticulo(), // Long idArticulo, 
					this.inventario.getInicial(), // Double stock, 
					Numero.toRedondearSat(this.inventario.getInicial()), // Double calculo
					null // String observaciones
				);
				DaoFactory.getInstance().insert(sesion, movimiento);
			} // if
      
      this.inventario.setStock(this.inventario.getInicial());
      if(this.inventario.isValid())
        regresar= DaoFactory.getInstance().update(sesion, this.inventario)> 0L;
      else
        regresar= DaoFactory.getInstance().insert(sesion, this.inventario)> 0L;
		} // try
		finally {
			Methods.clean(params);
		} // finally
    return regresar;
	}

  private Double toSumAlmacenArticulo(Session sesion, Long idArticulo) throws Exception {
		Double regresar           = 0D;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idArticulo", idArticulo);
			Value value= DaoFactory.getInstance().toField(sesion, "VistaKardexDto", "existencias", params, "total");
			if(value!= null && value.getData()!= null)
				regresar= value.toDouble();
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
