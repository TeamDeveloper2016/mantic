package mx.org.kaana.kajool.procesos.mantenimiento.insumos.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TcJanalInsumosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.mantenimiento.insumos.reglas.Transaccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Extensions;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name="kajoolMantenimientoInsumosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= -3565375733676226695L;  
  
  @PostConstruct
  @Override
	protected void init() {
    try {			
      this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {
		List<Columna>campos= null;
    try {
      campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      this.lazyModel= new FormatCustomLazy("TcJanalInsumosDto", this.attrs, campos);
			UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(campos);
		} // finally
  } // doLoad

  public StreamedContent getFile() {
    StreamedContent regresar= null;
    Entity insumo           = null;
    try {
      insumo= (Entity)this.attrs.get("selected");
      InputStream stream = new URL(insumo.toString("ruta")).openStream();
      if(stream== null)
  			JsfBase.addMessage("Error", "El recurso no se encuentra disponible ["+ insumo.toValue("descarga")+ "]");
      else  
        regresar= new DefaultStreamedContent(stream, Extensions.valueOf((String)insumo.toValue("contenido")).getContenTypeLinux(), (String)insumo.toValue("descarga"));
    } // try 
    catch(Exception e) {
			JsfBase.addMessage("Error", "El recurso no se encuentra disponible ["+ insumo.toValue("descarga")+ "]");
      Error.mensaje(e);
    } // catch
    return regresar;  
  }
  
  public void doEliminar(){
    Transaccion transaccion= null;
    try {
      transaccion= new Transaccion(new TcJanalInsumosDto(((Entity)this.attrs.get("selected")).getKey()));
      if(transaccion.ejecutar(EAccion.ELIMINAR))
        JsfBase.addMessage("Insumos", "Se eliminó correctamente el insumo", ETipoMensaje.INFORMACION);
      else
        JsfBase.addMessage("Insumos", "Ocurrio un error al eliminar el insumo", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch    
  } // doEliminar
  
  public void doLoadDialogo(String accion){
    EAccion eaccion            = null;
    TcJanalInsumosDto insumoDto= null;    
    try {
      eaccion= EAccion.valueOf(accion);
      this.attrs.put("accion", eaccion);
      this.attrs.put("tipoAccion", Cadena.nombrePersona(accion));
      switch(eaccion){
        case AGREGAR:
          insumoDto= new TcJanalInsumosDto();
          break;
        case MODIFICAR:
          insumoDto= (TcJanalInsumosDto) DaoFactory.getInstance().findById(TcJanalInsumosDto.class, ((Entity)this.attrs.get("selected")).getKey());
          this.attrs.put("activar", insumoDto.getDisponible().equals(1L));
          break;
      } // switch
      this.attrs.put("insumoDto", insumoDto);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch        
  } // doLoadDialogo
  
  public void doAccion(){
    Transaccion transaccion    = null;
    TcJanalInsumosDto insumoDto= null;
    EAccion accion             = null;
    try {
      insumoDto= (TcJanalInsumosDto) this.attrs.get("insumoDto");
      accion= (EAccion) this.attrs.get("accion");
      insumoDto.setDisponible(Boolean.valueOf(this.attrs.get("activar").toString()) ? 1L : 2L);
      transaccion= new Transaccion(insumoDto);
      if(transaccion.ejecutar(accion))
        JsfBase.addMessage("Insumos", "Se ".concat(accion.equals(EAccion.MODIFICAR) ? "modificó" : "agregó").concat(" el insumo correctamente"), ETipoMensaje.INFORMACION);
      else
        JsfBase.addMessage("Insumos", "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el insumo"), ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {      
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch    
  } // doAcccion
}
