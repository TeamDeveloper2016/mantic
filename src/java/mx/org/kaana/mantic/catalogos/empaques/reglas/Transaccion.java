package mx.org.kaana.mantic.catalogos.empaques.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.empaques.beans.EmpaqueUnidad;
import mx.org.kaana.mantic.db.dto.TcManticEmpaquesDto;
import mx.org.kaana.mantic.db.dto.TcManticUnidadesMedidasDto;
import mx.org.kaana.mantic.db.dto.TrManticEmpaqueUnidadMedidaDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private EmpaqueUnidad empaqueUnidad;
	private String message;
	
	public Transaccion(EmpaqueUnidad empaqueUnidad) {
		this.empaqueUnidad= empaqueUnidad;
	} // Transaccion	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {			
			switch(accion){
				case AGREGAR:			
					regresar= registrarEmpaqueUnidad(sesion);
					break;
				case MODIFICAR:				
					break;								
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.message.concat(" ").concat(e.getMessage()));
		} // catch		
		return regresar;
	} // ejecutar	
	
	private boolean registrarEmpaqueUnidad(Session sesion) throws Exception{
		boolean regresar= false;
		try {
			if(this.empaqueUnidad.getEmpaqueExistente() && this.empaqueUnidad.getUnidadExistente())
				regresar= registrarRelacion(sesion);
			else
				regresar= registrarCatalogos(sesion);
		} // try
		catch (Exception e) {	
			throw e;
		} // catch		
		return regresar;
	} // registrarEmpaqueUnidad
	
	private boolean registrarRelacion(Session sesion) throws Exception{
		TrManticEmpaqueUnidadMedidaDto relacion= null;
		boolean regresar= false;
		try {
			relacion= new TrManticEmpaqueUnidadMedidaDto();
			relacion.setIdUsuario(JsfBase.getIdUsuario());
			relacion.setObservaciones(this.empaqueUnidad.getObservaciones());
			relacion.setIdEmpaque(this.empaqueUnidad.getIdEmpaque());
			relacion.setIdUnidadMedida(this.empaqueUnidad.getIdUnidad());
			regresar= DaoFactory.getInstance().insert(sesion, relacion) >= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarRelacion
	
	private boolean registrarCatalogos(Session sesion) throws Exception{
		boolean regresar                 = false;
		TcManticEmpaquesDto empaque      = null;
		TcManticUnidadesMedidasDto unidad= null;
		try {
			if(!this.empaqueUnidad.getEmpaqueExistente()){
				empaque= new TcManticEmpaquesDto();
				empaque.setClave(this.empaqueUnidad.getClaveEmpaque());
				empaque.setDescripcion(this.empaqueUnidad.getDescripcionEmpaque());
				empaque.setIdUsuario(JsfBase.getIdUsuario());
				empaque.setNombre(this.empaqueUnidad.getNombreEmpaque());
				this.empaqueUnidad.setIdEmpaque(DaoFactory.getInstance().insert(sesion, empaque));
			} // if
			if(!this.empaqueUnidad.getUnidadExistente()){
				unidad= new TcManticUnidadesMedidasDto();				
				unidad.setClave(this.empaqueUnidad.getClaveUnidad());
				unidad.setDescripcion(this.empaqueUnidad.getDescripcionUnidad());
				unidad.setIdUsuario(JsfBase.getIdUsuario());
				unidad.setNombre(this.empaqueUnidad.getNombreUnidad());
				unidad.setProporcion(this.empaqueUnidad.getProporcion());
				this.empaqueUnidad.setIdUnidad(DaoFactory.getInstance().insert(sesion, unidad));
			} // if
			regresar= registrarRelacion(sesion);				
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarCatalogos
}
