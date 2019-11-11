package mx.org.kaana.mantic.catalogos.cajas.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.ventas.caja.cierres.beans.Denominacion;
import mx.org.kaana.mantic.ventas.caja.cierres.beans.Importe;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import org.hibernate.Session;

public class Transaccion extends Cierre {

	private static final long serialVersionUID=-2683257979185702406L;
	
  private TcManticCajasDto caja;
  private String messageError;
  private TcManticCierresCajasDto cierreCaja;

  public TcManticCierresCajasDto getCierreCaja() {
    return cierreCaja;
  }

  public void setCierreCaja(TcManticCierresCajasDto cierreCaja) {
    this.cierreCaja = cierreCaja;
  }

  public Transaccion(TcManticCajasDto caja, Long idCaja, Double inicial, TcManticCierresDto cierre, List<Importe> importes, List<Denominacion> denominaciones) {
    super(idCaja, inicial, cierre, importes, denominaciones);
    this.caja = caja;
  }
  
  public Transaccion(TcManticCajasDto caja, Long idCaja) {
    super(idCaja);
    this.caja = caja;
  }

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar         = false;
    Map<String, Object>params= null;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar= DaoFactory.getInstance().insert(sesion, this.caja)>= 1L;
          sesion.flush();
          setIdCaja(this.caja.getIdCaja());
          regresar= this.toRegistrar(sesion);
          break;
        case MODIFICAR:
          regresar= DaoFactory.getInstance().update(sesion, this.caja)>= 1L;
          sesion.flush();
          setIdCaja(this.caja.getIdCaja());
          regresar= toModificaRegistro(sesion, cierreCaja);
          break;
        case ELIMINAR:
          params= new HashMap<>();
          params.put("idCaja", this.caja.getIdCaja());
          regresar= this.toEliminar(sesion);
          sesion.flush();
          if(DaoFactory.getInstance().toField("TcManticCierresCajasDto", "cajasConCierre", params , "total").toInteger()== 0)
            regresar= DaoFactory.getInstance().delete(sesion, this.caja)>= 1L;
					else {
            this.caja.setIdActiva(2L);
            regresar= DaoFactory.getInstance().update(sesion, this.caja)>= 1L;
          } // else
          break;				
      } // switch
      if (!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    finally {
			Methods.clean(params);
		} // finally
    return regresar;
	} // ejecutar	
	
}