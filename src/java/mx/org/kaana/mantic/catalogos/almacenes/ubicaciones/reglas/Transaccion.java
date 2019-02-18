package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
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
    boolean regresar = false;
		TcManticAlmacenesArticulosDto ubicacion= null;
    try {						
      switch (accion) {
        case AGREGAR:
          regresar = agregarUbicacion(sesion);
          break;        
        case ELIMINAR:
          regresar = eliminarUbicacion(sesion);
          break;       
				case MODIFICAR:
					ubicacion= (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticAlmacenesArticulosDto.class, this.articulo.toLong("idAlmacenArticulo"));
					ubicacion.setIdAlmacen(this.articulo.toLong("idAlmacen"));
					ubicacion.setIdAlmacenUbicacion(this.articulo.toLong("idAlmacenUbicacion"));
					regresar= DaoFactory.getInstance().update(sesion, ubicacion)>= 1L;
					break;
				case ASIGNAR:
					ubicacion= new TcManticAlmacenesArticulosDto();
					ubicacion.setIdAlmacen(this.articulo.toLong("idAlmacen"));
					ubicacion.setIdAlmacenUbicacion(this.articulo.toLong("idAlmacenUbicacion"));
					ubicacion.setIdArticulo(this.articulo.getKey());
					ubicacion.setIdUsuario(JsfBase.getIdUsuario());
					ubicacion.setStock(0D);
					ubicacion.setMaximo(0D);
					ubicacion.setMinimo(0D);					
					regresar= DaoFactory.getInstance().insert(sesion, ubicacion)>= 1L;
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
    boolean regresar                         = false;    
		TcManticAlmacenesUbicacionesDto ubicacion= null;
    try {
			ubicacion= new TcManticAlmacenesUbicacionesDto();
			ubicacion.setDescripcion(this.descripcion);
			ubicacion.setIdAlmacen(this.ubicacion.getIdAlmacen());
			ubicacion.setIdUsuario(JsfBase.getIdUsuario());
      switch(this.ubicacion.getNivel()){													
				case PISO:					
					ubicacion.setPiso(this.nombre);
					ubicacion.setNivel(1L);
					break;
				case CUARTO:
					ubicacion.setPiso(this.ubicacion.getPiso());
					ubicacion.setCuarto(this.nombre);
					ubicacion.setNivel(2L);
					break;
				case ANAQUEL:
					ubicacion.setPiso(this.ubicacion.getPiso());
					ubicacion.setCuarto(this.ubicacion.getCuarto());
					ubicacion.setAnaquel(this.nombre);
					ubicacion.setNivel(3L);
					break;
				case CHAROLA:
					ubicacion.setPiso(this.ubicacion.getPiso());
					ubicacion.setCuarto(this.ubicacion.getCuarto());
					ubicacion.setAnaquel(this.ubicacion.getAnaquel());
					ubicacion.setCharola(this.nombre);
					ubicacion.setNivel(4L);
					break;
			} // switch			
			regresar= DaoFactory.getInstance().insert(sesion, ubicacion)>= 1L;
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
}
