package mx.org.kaana.kajool.procesos.usuarios.delegar.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 11:00:28 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
//import mx.org.kaana.kajool.db.dto.TcKajoolPersonasDto;
import mx.org.kaana.kajool.enums.EStepsUsuario;
import mx.org.kaana.kajool.procesos.acceso.beans.Empleado;
import mx.org.kaana.kajool.procesos.usuarios.delegar.beans.Informe;
import mx.org.kaana.kajool.procesos.usuarios.delegar.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.usuarios.reglas.GestorSQL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "kajoolUsuariosDelegarGeneral")
@ViewScoped
public class General extends DatosDelega implements Serializable {

	private static final Log LOG=LogFactory.getLog(General.class);
	private static final long serialVersionUID= 5847925465489375142L;
	private static final Long SIN_ASIGNAR     = -1L;
	private List<UISelectEntity> usuarios;
	private DatosDelega datosDelega;

	public List<UISelectEntity> getUsuarios() {
		return usuarios;
	}

	public DatosDelega getDatosDelega() {
		return datosDelega;
	}

	public void setDatosDelega(DatosDelega datosDelega) {
		this.datosDelega= datosDelega;
	}

	@Override
	@PostConstruct
	protected void init() {
		Empleado empleado= null;
		try {
			empleado= JsfBase.getAutentifica().getEmpleado();
			this.usuarios= new ArrayList<>();			
			this.attrs.put("nombreCompleto", empleado.getNombreCompleto());
			this.attrs.put("nombrePerfil", empleado.getDescripcionPerfil());
			this.attrs.put("idUsuario", empleado.getIdUsuario());
			this.attrs.put("parametro", "");
			this.attrs.put("credenciales", false);
			this.attrs.put("informe", new Informe());
			this.attrs.put("step", 0);
			initDelega();			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // init

	public void doBuscarEmpleados() {
		try {
			this.attrs.put("parametro", this.attrs.get("parametro").toString().toUpperCase());
			this.attrs.put("idPersona", JsfBase.getAutentifica().getEmpleado().getIdUsuario());
			this.usuarios= UIEntity.build("TcKajoolPersonasDto", "busquedaPersonaDelega", this.attrs);					
			if (!this.usuarios.isEmpty())
				this.attrs.put("usuariosDelega", UIBackingUtilities.toFirstKeySelectEntity(this.usuarios));				
			else	
				JsfBase.addMessage("No se encontraron coincidencias con los datos capturados");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // doBuscarEmpleados

	public void doAvanzar() {
		Integer actual    = null;
		boolean ultimo    = false;
		EStepsUsuario step= null;
		try {
			actual= Numero.getInteger(this.attrs.get("step").toString());
			ultimo= actual.equals(EStepsUsuario.VISTA_PREVIA.getKey());
			step  = EStepsUsuario.getStep(actual);
			switch (step) {
				case BUSQUEDA:
					busqueda(actual);
					break;
				case VALIDACION:
					loadInforme();					
					break;
				case VISTA_PREVIA:
					saveInformacion();					
					break;
			} // switch
			if (!ultimo){
				actual= Numero.getInteger(this.attrs.get("step").toString());
				this.attrs.put("step", ++actual);	
			} // if
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch					
	} // doAvanzar

	public void doRetroceder() {
		Integer actual    = 0;
		EStepsUsuario step= null;
		try {
			actual= Integer.valueOf(this.attrs.get("step").toString());
			step= EStepsUsuario.getStep(actual);
			switch (step) {				
				case VALIDACION:										
				case VISTA_PREVIA:
					this.usuarios= new ArrayList<>();
					this.attrs.put("parametro", "");			
					//if(this.persona.getKey()> SIN_ASIGNAR)
						this.attrs.put("step", --actual);
					break;
			} // switch
			if (actual> 0)
				this.attrs.put("step", --actual);		
			else
				RequestContext.getCurrentInstance().execute("doFiltro();");			
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);
			throw e;
		} // catch		
	} // doRetroceder
	
	private void busqueda(Integer actual) throws Exception{
		Entity delega= (Entity) this.attrs.get("usuariosDelega");
		if (delega== null || delega.getKey().equals(SIN_ASIGNAR)) {
			//this.persona= new TcKajoolPersonasDto();
			this.entidades= null;
			this.entidadesResidencia= null;
			this.municipios= null;						
		} // if
		else {
			this.attrs.put("idPersona", delega.getKey());
			initDelega();
			this.attrs.put("step", ++actual);
			loadInforme();
		} // else					
	} // busqueda
	
	private void loadInforme() throws Exception{
		GestorSQL gestor= null;
		Long idPersona  = null;
		Entity usuario  = null;				
		try {
			//idPersona= this.persona.getIdPersona();
			if(idPersona> SIN_ASIGNAR){
				gestor= new GestorSQL(idPersona);
				usuario= gestor.toUsuario();								
			} // if
			if(usuario== null)
				generarCuenta();							
			else{
				this.attrs.put("credenciales", true);
				this.attrs.put("login", usuario.toString("cuenta"));
				this.attrs.put("contrasenia", BouncyEncryption.decrypt(usuario.toString("contrasenia")));
			} // else
			generaInforme();
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // loadInforme
	
	private void generaInforme(){
		Informe informe= null;		
		try {
			//informe= new Informe(this.persona, this.attrs.get("login").toString(), this.attrs.get("contrasenia").toString(), JsfBase.getAutentifica().getEmpleado().getDescripcionPerfil());			
			//this.attrs.put("informe", informe);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch				
	} // generaInforme
	
	private void saveInformacion() {
		Transaccion transaccion= null;		
		boolean proceso        = false;
		Informe informe        = null;
		Long idUsuario         = -1L;
		try {		
			informe= (Informe) this.attrs.get("informe");
			if(informe.getContrasenia().equals(this.attrs.get("confirmacionContrasenia").toString())){
				idUsuario= Long.valueOf(this.attrs.get("idUsuario").toString());
				//this.persona.setIdUsuario(idUsuario);
				//transaccion= new Transaccion(this.persona, idUsuario, this.attrs.get("login").toString(), informe.getContrasenia());			
				proceso= transaccion.ejecutar(EAccion.AGREGAR);
				lanzarMensaje(proceso);
				RequestContext.getCurrentInstance().execute("doFiltro();");
			} // if
			else{
				informe.setContrasenia("");
				this.attrs.put("confirmacionContrasenia", "");
				this.attrs.put("informe", informe);
				JsfBase.addMessage("Contraseña", "Las contraseñas no coiniciden, favor de capturarlas nuevamente");
			} // else
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // saveInformacion				

	private void lanzarMensaje(boolean respuesta) {
		if (respuesta)
			JsfBase.addMsgProperties("correcto_agregar", ETipoMensaje.INFORMACION);		
		else
			JsfBase.addMsgProperties("error_agregar", ETipoMensaje.ERROR);		
	} // lanzarMensaje
	
	public String doCancelar() {
		String regresar= null;
		try {
			regresar= "/Paginas/Usuarios/Delegar/filtro.jsf".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doCancelar		
	
	@Override
	protected void finalize() throws Throwable {
		LOG.info("Liberando recursos");
		Methods.clean(this.usuarios);
		super.finalize();		
	} // finalize	
}
