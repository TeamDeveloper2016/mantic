package mx.org.kaana.mantic.catalogos.cajas.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{
	
  private TcManticCajasDto caja;
  private String messageError;

	public Transaccion(TcManticCajasDto caja) {
		this.caja= caja;
	}
  
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		 boolean regresar          = false;
     Map<String, Object>params = null;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar= DaoFactory.getInstance().insert(sesion, this.caja)>= 1L;
          break;
        case MODIFICAR:
          regresar= DaoFactory.getInstance().update(sesion, this.caja)>= 1L;
          break;
        case ELIMINAR:
          params= new HashMap<>();
          params.put("idCaja", this.caja.getIdCaja());
          if(DaoFactory.getInstance().toField("TcManticCierresCajasDto", "cajasConCierre", params , "total").toInteger()== 0)
            regresar= DaoFactory.getInstance().delete(sesion, this.caja)>= 1L;
          else{
            this.caja.setIdActiva(2L);
            regresar= DaoFactory.getInstance().update(sesion, this.caja)>= 1L;
          }
          break;				
      } // switch
      if (!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
    } // catch		
    finally {
			Methods.clean(params);
		} // finally
    return regresar;
	} // ejecutar	
}