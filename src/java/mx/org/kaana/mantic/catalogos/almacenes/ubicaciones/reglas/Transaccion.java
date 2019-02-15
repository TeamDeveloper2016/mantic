package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas;

import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.beans.OrganigramUbicacion;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {
  
  private OrganigramUbicacion ubicacion;
  private String descripcion;	

	public Transaccion(OrganigramUbicacion ubicacion, String descripcion) {
		this.ubicacion  = ubicacion;
		this.descripcion= descripcion;
	}	// Transaccion

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {						
      switch (accion) {
        case AGREGAR:
          regresar = agregarUbicacion(sesion);
          break;        
      } // switch
      if (!regresar) 
        throw new Exception("");
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
      switch(this.ubicacion.getNivel()){				
				case PISO:
				case CUARTO:
				case ANAQUEL:
				case CHAROLA:
					break;
			} // switch
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // procesarAlmacen  
}
