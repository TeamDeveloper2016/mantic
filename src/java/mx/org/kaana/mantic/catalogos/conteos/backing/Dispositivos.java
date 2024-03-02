package mx.org.kaana.mantic.catalogos.conteos.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.mantic.catalogos.conteos.reglas.Transaccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticConteosDto;

@Named(value = "manticCatalogosConteosDispositivos")
@ViewScoped
public class Dispositivos extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428179L;
  
  private EAccion accion;
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.accion= EAccion.ELIMINAR;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= this.toPrepare();
    try {
      columns.add(new Columna("imei", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("dispositivo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      columns.add(new Columna("vigencia", EFormatoDinamicos.FECHA_HORA_CORTA));
      params.put("sortOrder", "order by tc_mantic_dispositivos.registro desc");
      this.lazyModel = new FormatCustomLazy("VistaConteosDto", "dispositivos", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad
	
	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap<>();
		StringBuilder sb            = new StringBuilder("");
		try {
			if(!Cadena.isVacio(JsfBase.getParametro("imei")))
				sb.append("upper(tc_mantic_dispositivos.imei) like upper('%").append(JsfBase.getParametro("imei")).append("%') and ");						
			if(!Cadena.isVacio(JsfBase.getParametro("nombre")))
				sb.append("upper(tc_mantic_dispositivos.nombre) like upper('%").append(JsfBase.getParametro("nombre")).append("%') and ");						
			if(!Cadena.isVacio(JsfBase.getParametro("dispositivo")))
				sb.append("upper(tc_mantic_dispositivos.dispositivo) like upper('%").append(JsfBase.getParametro("dispositivo")).append("%') and ");						
			if(!Cadena.isVacio(JsfBase.getParametro("version")))
				sb.append("upper(tc_mantic_dispositivos.version) like upper('%").append(JsfBase.getParametro("version")).append("%') and ");						
			if(!Cadena.isVacio(JsfBase.getParametro("cuenta")))
				sb.append("upper(tc_mantic_dispositivos.cuenta) like upper('%").append(JsfBase.getParametro("cuenta")).append("%') and ");						
			if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
				sb.append("(date_format(tc_mantic_dispositivos.registro, '%Y%m%d%h%i%S')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
			if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
				sb.append("(date_format(tc_mantic_dispositivos.registro, '%Y%m%d%h%i%S')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
			if(Cadena.isVacio(sb.toString()))
				regresar.put("condicion", Constantes.SQL_VERDADERO);
			else
			  regresar.put("condicion", sb.substring(0, sb.length()- 4));			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCondicion

  public void doEliminar() {
    Transaccion transaccion= null;
    Entity seleccionado    = (Entity) this.attrs.get("seleccionado");
    try {
//      TcManticConteosDto conteo= (TcManticConteosDto)DaoFactory.getInstance().findById(TcManticConteosDto.class, seleccionado.getKey());
//      transaccion = new Transaccion(conteo);
//      if (transaccion.ejecutar(this.accion)) 
//        JsfBase.addMessage("Eliminar", "El dispositivo fue eliminado", ETipoMensaje.ERROR);
//      else
//        JsfBase.addMessage("Eliminar", "Ocurri� un error al eliminar el dispositivo", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } 
  
}