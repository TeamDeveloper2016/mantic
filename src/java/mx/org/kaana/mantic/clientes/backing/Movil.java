package mx.org.kaana.mantic.clientes.backing;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Xls;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.ventas.backing.Accion;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value= "manticClientesMovil")
@ViewScoped
public class Movil extends Accion implements Serializable {

	private static final Logger LOG            = Logger.getLogger(Movil.class);
  private static final long serialVersionUID = 327393488565639361L;
  
  private String path;

  public String getPath() {
    return path;
  }
  
	@PostConstruct
  @Override
  protected void init() {		
    try { 
      super.init();
      MotorBusqueda motor= new MotorBusqueda(-1L);
      TcManticClientesDto cliente= motor.toCliente(JsfBase.getAutentifica().getPersona().getIdPersona());
      if(cliente!= null)
        this.doAsignaClienteInicial(cliente.getIdCliente());
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");
      this.attrs.put("buscaPorCodigo", false);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // init

	@Override
  public void doLoad() {
    super.doLoad();	
  } // doLoad

  @Override
  public String doCancelar() {
    this.init();
    return null;
  }
  
  public StreamedContent getArchivo() {
		StreamedContent regresar  = null;
		Xls xls                   = null;
		String template           = "PRECIOS";
		Map<String, Object> params= null;
    List<IBaseDto> partidas   = this.getAdminOrden().getPartidas();
		try {
			String salida  = EFormatos.XLS.toPath().concat(Archivo.toFormatNameFile(template).concat(".")).concat(EFormatos.XLS.name().toLowerCase());
  		String fileName= JsfBase.getRealPath("").concat(salida);
      xls= new Xls(fileName, "PROPIO,NOMBRE,CANTIDAD,COSTO,IVA,IMPORTE");	
			if(xls.procesar(true, partidas)) {
		    String contentType= EFormatos.XLS.getContent();
        InputStream stream= ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(salida);  
		    regresar          = new DefaultStreamedContent(stream, contentType, Archivo.toFormatNameFile(template).concat(".").concat(EFormatos.XLS.name().toLowerCase()));				
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
      partidas.clear();
		} // finally
    return regresar; 
  }
  
}