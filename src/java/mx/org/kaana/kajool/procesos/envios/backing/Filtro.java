package mx.org.kaana.kajool.procesos.envios.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.db.dto.TrJanalBitacoraMovilDto;
import mx.org.kaana.kajool.db.dto.TrJanalVisitasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.beans.Cuestionario;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.EEstatus;
import mx.org.kaana.kajool.procesos.enums.EEstatusSemaforos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.servicios.ws.publicar.beans.VisitasMovil;
import mx.org.kaana.kajool.servicios.ws.publicar.reglas.Transaccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.pagina.JsfUtilities;

/**
 *@company INEGI
 *@project IKTAN (Sistema de seguimiento y control de proyectos)
 *@date 25/11/2016
 *@time 09:51:26 AM 
 *@author Usuario <usuario.usuario@inegi.org.mx>
 */

@ManagedBean(name="kajoolEnviosFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable  {
	
	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= -3565375733676226495L;

	@Override
	public void doLoad() {
		List<Columna>campos= null;
    try {
      campos= new ArrayList<>();
			campos.add(new Columna("nombreCompleto", EFormatoDinamicos.MAYUSCULAS));			
			campos.add(new Columna("cuenta", EFormatoDinamicos.MINUSCULAS));			
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			campos.add(new Columna("registroIntegracion", EFormatoDinamicos.FECHA_HORA));
      this.lazyModel= new FormatLazyModel("VistaEnviosDto", "row", this.attrs, campos);
			UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(campos);
		} // finally
	}

	@Override
	protected void init() {
		try {			
      this.attrs.put("cuenta", "");
			doLoad();
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	}
	
	public String doSemaforo(Value estatus){		
		EEstatusSemaforos semaforo= EEstatusSemaforos.toIdEstatus(estatus.toLong());		
	  return semaforo.getSemaforo().getNombre();											
  } // doSemaforo	

	public void doIntegrar() {
		Entity entity										= null;
		TrJanalBitacoraMovilDto bitacora= null;
		Transaccion transaccion         = null;
		try {
			entity= (Entity) this.attrs.get("selected");
			if(entity!= null) {							
        bitacora= (TrJanalBitacoraMovilDto) DaoFactory.getInstance().findById(TrJanalBitacoraMovilDto.class, entity.getKey());				
	      transaccion= new Transaccion(bitacora);
				transaccion.ejecutar(EAccion.PROCESAR);
				JsfUtilities.addMessage(transaccion.getRespuestaMovil().getMensaje());			
			} // if
			doLoad();
		} // try	
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
	}
	
	public void doIntegrarTodos() {
		Integer integrados                     = 0;
		List<TrJanalBitacoraMovilDto> bitacoras= null;
		Transaccion transaccion                = null;
		try {
			this.attrs.put(Constantes.SQL_CONDICION, "id_estatus=".concat(EEstatus.ASIGNADO.getKey().toString()));
			bitacoras= DaoFactory.getInstance().findViewCriteria(TrJanalBitacoraMovilDto.class, attrs);
			if(bitacoras.size()> 0) {
				for(TrJanalBitacoraMovilDto bitacora: bitacoras) {
					transaccion= new Transaccion(bitacora);
					transaccion.ejecutar(EAccion.PROCESAR);
					if(transaccion.getRespuestaMovil().getCodigo().equals("02")) 
						integrados++;
				} // for				
			} // if
			if(integrados> 0)
				JsfUtilities.addMessage("Se integraron ".concat(integrados.toString()).concat(" de ").concat(String.valueOf(bitacoras.size()).concat(" envios con éxito.")));
			else	
				JsfUtilities.addMessage("No éxisten envíos disponibles para integrar.");
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
		finally {
			Methods.clean(bitacoras);
		} // finally
	}
	
}
