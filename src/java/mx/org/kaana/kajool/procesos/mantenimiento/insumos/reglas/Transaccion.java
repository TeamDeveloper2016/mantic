package mx.org.kaana.kajool.procesos.mantenimiento.insumos.reglas;

/**
 *@company INEGI
 *@project IKTAN (Sistema de Seguimiento y Control de proyectos estadísticos)
 *@date 7/12/2016
 *@time 11:09:34 AM 
 *@author Jorge Alberto Vazquez Serafin <jorge.vazquezser@inegi.gob.mx>
 */

import java.util.Collections;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TcJanalInsumosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
  private static final String RUTA= "ftp://upload%40kaana.jvmhost.net:kajool2016@ftp.kaana.jvmhost.net/"; 
  private TcJanalInsumosDto dto;

	public Transaccion (TcJanalInsumosDto dto) {
		this.dto= dto;
  } // Transaccion
 
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;
    try {
      switch(accion) {
        case AGREGAR :
          actualizaValoresDto(sesion);
          regresar= DaoFactory.getInstance().insert(sesion, this.dto) >= 1;
        break;
        case MODIFICAR :
          actualizaValoresDto(sesion);
          regresar= DaoFactory.getInstance().update(sesion, this.dto) >= 1;
        break;
        case ELIMINAR :
          regresar= DaoFactory.getInstance().delete(sesion, this.dto) >= 1;
        break;
      } // switch
			if(!regresar)
				throw new RuntimeException("No se modifico ningun registro");
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try   
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // ejecutar
  
  private void actualizaValoresDto(Session sesion) throws Exception{
    String descarga= null;
    try {
      descarga= this.dto.getDescarga();
      this.dto.setRuta(RUTA.concat(descarga));      
      this.dto.setContenido(descarga.substring(descarga.lastIndexOf(".")+1, descarga.length()).toUpperCase());
      this.dto.setVersion(descarga.substring(descarga.lastIndexOf("-")+1, descarga.lastIndexOf(".")));
      this.dto.setOrden(toMaxOrden(sesion));
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
  } // actualizaValoresDto
  
  private Long toMaxOrden(Session sesion) throws Exception{
    Long regresar= -1L;
    Entity orden = null;
    try {      
      orden= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcJanalInsumosDto", "maxOrden", Collections.EMPTY_MAP);
      if(orden!= null)
        regresar= Long.valueOf(orden.toString("orden"));
    } // try
    catch (Exception e) {      
      throw e;
    } // catch
    return regresar;
  } // toMaxOrden
} 