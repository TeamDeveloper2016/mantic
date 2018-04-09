package mx.org.kaana.kajool.procesos.usuarios.delegar.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 11/09/2015
 * @time 05:38:11 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.procesos.acceso.beans.Empleado;
import mx.org.kaana.kajool.procesos.usuarios.delegar.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "kajoolUsuariosDelegarFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final Log LOG= LogFactory.getLog(Filtro.class);
	private static final long serialVersionUID= 5873712623540641804L;

	@PostConstruct
	@Override
	protected void init() {
		try {
			this.attrs.put("parametro", "");
      this.attrs.put("sortOrder","order by tr_janal_usuarios_delega.registro");
			loadUsuario();
			doLoad();
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // init

	@Override
	public void doLoad() {
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("vigenciaIni", EFormatoDinamicos.FECHA_CORTA));
			campos.add(new Columna("vigenciaFin", EFormatoDinamicos.FECHA_CORTA));
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.lazyModel= new FormatCustomLazy("VistaDelegaPrivilegiosDto", "usuariosConDelega", this.attrs, campos);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // doLoad

	private void loadUsuario() throws Exception {
		Empleado empleado= null;
		try {
			empleado= JsfBase.getAutentifica().getEmpleado();
      this.attrs.put("idUsuario", empleado.getIdUsuario());
			this.attrs.put("nombreCompleto", empleado.getNombreCompleto());
			this.attrs.put("nombrePerfil", empleado.getDescripcionPerfil());
		} // try
		catch (Exception e) {
			throw e;
		} // catch				
	} //loadUsuario
	
	public void doEliminar(){		
		try {									
			doEvento(EAccion.ELIMINAR.name());
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doEliminar
	
	public void doEvento(String evento){		
		Transaccion transaccion  = null;		
		EAccion accion           = null;
		Map<String, Object>params= null;
		try {			
			params= new HashMap<>();
			accion= EAccion.valueOf(evento.toUpperCase());
			transaccion= new Transaccion(((Entity)this.attrs.get("selected")).getKey());						
			params.put("parametro", "usuario");
			if(transaccion.ejecutar(accion))
				JsfBase.addMsgProperties("correcto_".concat(evento.toLowerCase()), params, ETipoMensaje.INFORMACION);		
			else
				JsfBase.addMsgProperties("error_".concat(evento.toLowerCase()), params, ETipoMensaje.ERROR);						
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doEvento
	
	public boolean isMostrarMenu(){
		boolean regresar= false;
		Entity selected = null;
		try {
			selected= (Entity) this.attrs.get("selected");
			if(selected!= null)
				regresar= (selected.toLong("activo").equals(0L));
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // isMostrarCaptura
}
