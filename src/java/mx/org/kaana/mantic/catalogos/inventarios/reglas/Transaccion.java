package mx.org.kaana.mantic.catalogos.inventarios.reglas;

import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.inventarios.beans.ArticuloInventario;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private IBaseDto dto;
	private List<ArticuloInventario> inventarios;
	private String messageError;

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	}

	public Transaccion(List<ArticuloInventario> inventarios) {
		this.inventarios = inventarios;
	}	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			switch(accion){
				case AGREGAR:
          regresar = registraInventarios(sesion);
          break;        
        case DEPURAR:
          regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
          break;
			} // switch
			if (!regresar) 
        throw new Exception("");      
		} // try
		catch (Exception e) {
			throw new Exception(this.messageError.concat("<br/>")+ e.getMessage());
		} // catch		
		return regresar;
	} // ejecutar	
	
	private boolean registraInventarios(Session sesion) throws Exception {
    TcManticInventariosDto dto= null;
    ESql sqlAccion            = null;
    int count                 = 0;
    boolean validate          = false;
    boolean regresar          = false;
    try {
      for (ArticuloInventario articuloInventario : this.inventarios) {
				if(articuloInventario.getEjercicio()!= null && !Cadena.isVacio(articuloInventario.getEjercicio())){					
					dto = (TcManticInventariosDto) articuloInventario;
					sqlAccion = articuloInventario.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdUsuario(JsfBase.getIdUsuario());
							dto.setSalidas(0D);
							dto.setStock(articuloInventario.getEntradas()-articuloInventario.getSalidas());
							dto.setIdInventario(-1L);
							validate = registrar(sesion, dto);
							break;
						case UPDATE:
							validate = actualizar(sesion, dto);
							break;
					} // switch
				} // if
				else
					validate= true;
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.inventarios.size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al registrar el inventario, verifique que los datos de captura esten completos.";
    } // finally
    return regresar;
  } // registraClientesTipoContacto
	
	private boolean registrar(Session sesion, IBaseDto dto) throws Exception {
    return registrarSentencia(sesion, dto) >= 1L;
  } // registrar
  
	private Long registrarSentencia(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().insert(sesion, dto);
  } // registrar

  private boolean actualizar(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().update(sesion, dto) >= 1L;
  } // actualizar
}
