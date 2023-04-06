package mx.org.kaana.kalan.catalogos.pacientes.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.RegistroCliente;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named(value = "kalanCatalogosPacientesAccion")
@ViewScoped
public class Accion extends mx.org.kaana.mantic.catalogos.clientes.backing.Accion implements Serializable {

  private static final Log LOG = LogFactory.getLog(Accion.class);
  private static final long serialVersionUID = -7668102942302148046L;
  
  @PostConstruct
  @Override
  protected void init() {
    super.init();
  } 
  
  @Override
  public String doAceptar() {
    RegistroCliente cliente= this.getRegistroCliente();
    if(Cadena.isVacio(cliente.getCliente().getRfc())) 
      cliente.getCliente().setRfc(this.toLoadRfc());
    return super.doAceptar();
  } 
  
  private String toLoadRfc() {
    String regresar= Fecha.getNombreMesCorto(Fecha.getMesActual()- 1).concat(Fecha.getHoyEstandar().substring(2));
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("mes", Fecha.getHoyEstandar().substring(0, 6));
      Entity entity= (Entity)DaoFactory.getInstance().toEntity("TcManticClientesDto", "total", params);
      if(entity!= null && !entity.isEmpty())
        regresar= regresar.concat(Cadena.rellenar(entity.toString("total"), 3, '0', true));
      else
        regresar= regresar.concat(Cadena.rellenar("1", 3, '0', true));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch	
    finally {
      Methods.clean(params);
    }// finally
    return regresar;
  }
  
}
