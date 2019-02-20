package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.beans.OrganigramUbicacion;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {
  
	private static final Logger LOG    = Logger.getLogger(Transaccion.class);
  private OrganigramUbicacion ubicacion;
  private String nombre;	
  private String descripcion;	
  private String message;	
	private Entity articulo;	

	public Transaccion(OrganigramUbicacion ubicacion) {
		this(ubicacion, null, null);
	} // Transaccion
	
	public Transaccion(OrganigramUbicacion ubicacion, String descripcion, String nombre) {
		this.ubicacion  = ubicacion;
		this.descripcion= descripcion;
		this.nombre     = nombre;
	}	// Transaccion

	public Transaccion(Entity articulo) {
		this.articulo= articulo;		
	}	
	
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;
		TcManticAlmacenesArticulosDto almacenUbicacion= null;
    try {						
      switch (accion) {
        case AGREGAR:
          regresar = agregarUbicacion(sesion);
          break;        
        case ELIMINAR:
          regresar = eliminarUbicacion(sesion);
          break;       
				case MOVIMIENTOS:
					regresar = modificarUbicacion(sesion);
					break;
				case MODIFICAR:
					almacenUbicacion= (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticAlmacenesArticulosDto.class, this.articulo.toLong("idAlmacenArticulo"));
					almacenUbicacion.setIdAlmacen(this.articulo.toLong("idAlmacen"));
					almacenUbicacion.setIdAlmacenUbicacion(this.articulo.toLong("idAlmacenUbicacion"));
					regresar= DaoFactory.getInstance().update(sesion, almacenUbicacion)>= 1L;
					break;
				case ASIGNAR:
					almacenUbicacion= new TcManticAlmacenesArticulosDto();
					almacenUbicacion.setIdAlmacen(this.articulo.toLong("idAlmacen"));
					almacenUbicacion.setIdAlmacenUbicacion(this.articulo.toLong("idAlmacenUbicacion"));
					almacenUbicacion.setIdArticulo(this.articulo.getKey());
					almacenUbicacion.setIdUsuario(JsfBase.getIdUsuario());
					almacenUbicacion.setStock(0D);
					almacenUbicacion.setMaximo(0D);
					almacenUbicacion.setMinimo(0D);					
					regresar= DaoFactory.getInstance().insert(sesion, almacenUbicacion)>= 1L;
					break;
      } // switch
      if (!regresar) 
        throw new Exception(this.message);
    } // try
    catch (Exception e) {
      throw new Exception(e.getMessage());
    } // catch		
    return regresar;
  } // ejecutar

  private boolean agregarUbicacion(Session sesion) throws Exception {
    boolean regresar                                = false;    
		TcManticAlmacenesUbicacionesDto almacenUbicacion= null;
    try {
			almacenUbicacion= new TcManticAlmacenesUbicacionesDto();
			almacenUbicacion.setDescripcion(this.descripcion);
			almacenUbicacion.setIdAlmacen(this.ubicacion.getIdAlmacen());
			almacenUbicacion.setIdUsuario(JsfBase.getIdUsuario());
      switch(this.ubicacion.getNivel()){													
				case PISO:					
					almacenUbicacion.setPiso(this.nombre);
					almacenUbicacion.setNivel(1L);
					break;
				case CUARTO:
					almacenUbicacion.setPiso(this.ubicacion.getPiso());
					almacenUbicacion.setCuarto(this.nombre);
					almacenUbicacion.setNivel(2L);
					break;
				case ANAQUEL:
					almacenUbicacion.setPiso(this.ubicacion.getPiso());
					almacenUbicacion.setCuarto(this.ubicacion.getCuarto());
					almacenUbicacion.setAnaquel(this.nombre);
					almacenUbicacion.setNivel(3L);
					break;
				case CHAROLA:
					almacenUbicacion.setPiso(this.ubicacion.getPiso());
					almacenUbicacion.setCuarto(this.ubicacion.getCuarto());
					almacenUbicacion.setAnaquel(this.ubicacion.getAnaquel());
					almacenUbicacion.setCharola(this.nombre);
					almacenUbicacion.setNivel(4L);
					break;
			} // switch			
			regresar= DaoFactory.getInstance().insert(sesion, almacenUbicacion)>= 1L;
    } // try
    catch (Exception e) {
			this.message= "Ocurrió un error al agregar la ubicacion en el almacen.";
      throw e;
    } // catch		
    return regresar;
  } // procesarAlmacen  
	
	private boolean eliminarUbicacion(Session sesion) throws Exception{
		boolean regresar= false;
		try {
			regresar= DaoFactory.getInstance().delete(sesion, TcManticAlmacenesUbicacionesDto.class, this.ubicacion.getIdAlmacenUbicacion())>= 1L;
		} // try
		catch (Exception e) {			
			this.message= "Ocurrió un error al eliminar la ubicacion en el almacen. Verifique que no tenga articulos asociados,";
			throw e;
		} // catch		
		return regresar;
	} // eliminarUbicacion
	
	private boolean modificarUbicacion(Session sesion) throws Exception{
		boolean regresar                                          = true;
		TcManticAlmacenesUbicacionesDto almacenUbicacion          = null;
		List<TcManticAlmacenesUbicacionesDto> almacenesUbicaciones= null;
		Map<String, Object>params                                 = null;
		StringBuilder sb                                          = null;
		try {
			sb= new StringBuilder();
			params= new HashMap<>();
			almacenUbicacion= (TcManticAlmacenesUbicacionesDto) DaoFactory.getInstance().findById(sesion, TcManticAlmacenesUbicacionesDto.class, this.ubicacion.getIdAlmacenUbicacion());
			params.put("idAlmacen", almacenUbicacion.getIdAlmacen());
			params.put("nivel", almacenUbicacion.getNivel());
			params.put("descripcion", this.descripcion);
			switch(almacenUbicacion.getNivel().intValue()){
				case 1:					
					params.put("piso", this.nombre);
					sb.append(" and piso= '").append(almacenUbicacion.getPiso()).append("'");
					break;
				case 2:
					params.put("piso", almacenUbicacion.getPiso());
					sb.append(" and piso= '").append(almacenUbicacion.getPiso()).append("'");
					params.put("cuarto", this.nombre);
					sb.append(" and cuarto= '").append(almacenUbicacion.getCuarto()).append("'");
					break;
				case 3:
					params.put("piso", almacenUbicacion.getPiso());
					sb.append(" and piso= '").append(almacenUbicacion.getPiso()).append("'");
					params.put("cuarto", almacenUbicacion.getCuarto());
					sb.append(" and cuarto= '").append(almacenUbicacion.getCuarto()).append("'");
					params.put("anaquel", this.nombre);
					sb.append(" and anaquel= '").append(almacenUbicacion.getAnaquel()).append("'");
					break;
				case 4:
					params.put("piso", almacenUbicacion.getPiso());
					sb.append(" and piso= '").append(almacenUbicacion.getPiso()).append("'");
					params.put("cuarto", almacenUbicacion.getCuarto());
					sb.append(" and cuarto= '").append(almacenUbicacion.getCuarto()).append("'");
					params.put("anaquel", almacenUbicacion.getAnaquel());
					sb.append(" and anaquel= '").append(almacenUbicacion.getAnaquel()).append("'");
					params.put("charola", this.nombre);
					sb.append(" and charola= '").append(almacenUbicacion.getCharola()).append("'");
					break;
			} // switch
			params.put(Constantes.SQL_CONDICION, sb.toString());
			almacenesUbicaciones= DaoFactory.getInstance().toEntitySet(sesion, TcManticAlmacenesUbicacionesDto.class, "TcManticAlmacenesUbicacionesDto", "dependencia", params);
			for(TcManticAlmacenesUbicacionesDto pivote: almacenesUbicaciones){
				params.put("nivel", pivote.getNivel());
				DaoFactory.getInstance().update(sesion, TcManticAlmacenesUbicacionesDto.class, pivote.getIdAlmacenUbicacion(), params);
			} // for
		} // try
		catch (Exception e) {			
			this.message= "Ocurrió un error al eliminar la ubicacion en el almacen. Verifique que no tenga articulos asociados,";
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // eliminarUbicacion
}
