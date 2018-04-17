package mx.org.kaana.mantic.catalogos.articulos.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.bean.RegistroArticulo;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Transaccion;

@Named(value = "manticCatalogosArticulosAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

	private RegistroArticulo registroArticulo;

	public RegistroArticulo getRegistroArticulo() {
		return registroArticulo;
	}

	public void setRegistroArticulo(RegistroArticulo registroArticulo) {
		this.registroArticulo = registroArticulo;
	}
		
	@PostConstruct
	@Override
	protected void init() {
		try {
			this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));						
			this.attrs.put("idArticulo", JsfBase.getFlashAttribute("idCategoria"));		
			doLoad();		
			loadCategorias();
			loadEmpaques();
			doLoadUnidadesMedidas();				
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
	
	public void doLoad() {		
		EAccion eaccion= null;
		Long idArticulo= -1L;
		try {
			eaccion= (EAccion) this.attrs.get("accion");
			this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
			switch(eaccion){
				case AGREGAR:	
					this.registroArticulo= new RegistroArticulo();					
					break;
				case MODIFICAR:
					idArticulo= Long.valueOf(this.attrs.get("idArticulo").toString());
					this.registroArticulo= new RegistroArticulo(idArticulo);					
					break;	
			} // switch
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doLoad
	
	public String doAceptar(){
		TcManticArticulosDto dto= null;
		Transaccion transaccion  = null;
		try {
			dto= loadDto();
			transaccion= new Transaccion(dto, Long.valueOf(this.attrs.get("codigo").toString()), this.attrs.get("observaciones").toString());
			if(transaccion.ejecutar((EAccion) this.attrs.get("accion")))
				JsfBase.addMessage("Se aplico el cambio de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Ocurrió un error al registrar el cambio", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "filtro";
	} // doAccion
	
	private TcManticArticulosDto loadDto() throws Exception{
		TcManticArticulosDto regresar= null;
		try {
			regresar= (TcManticArticulosDto) this.attrs.get("dto");		
			if(EAccion.AGREGAR.equals((EAccion) this.attrs.get("accion"))){				
				regresar.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
			} // if
			else{	
				
			} // if													
			regresar.setIdUsuario(JsfBase.getIdUsuario());									
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadDto		
	
	public String doCancelar(){		
		return "filtro";
	} // doAccion
	
	private void loadEmpaques(){
		List<UISelectItem> empaques= null;
		Map<String, Object>params  = null;
		EAccion eaccion            = null;
		try {	
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			empaques= UISelect.build("TcManticEmpaquesDto",  "row", params, "nombre", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("empaques", empaques);
			eaccion= (EAccion) this.attrs.get("accion");							
			if(eaccion.equals(EAccion.AGREGAR))
				this.registroArticulo.setIdEmpaque((Long) UIBackingUtilities.toFirstKeySelectItem(empaques));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadEmpaques
	
	public void doLoadUnidadesMedidas(){
		List<UISelectItem> unidadesMedidas= null;
		Map<String, Object>params         = null;
		EAccion eaccion                   = null;
		try {
			params= new HashMap<>();			
			params.put("idEmpaque", this.registroArticulo.getIdEmpaque());
			eaccion= (EAccion) this.attrs.get("accion");							
			unidadesMedidas= UISelect.build("VistaEmpaqueUnidadMedidaDto", "empaqueUnidadMedida", params, "nombre", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("unidadesMedidas", unidadesMedidas);
			if(eaccion.equals(EAccion.AGREGAR))
				this.registroArticulo.getArticulo().setIdEmpaqueUnidadMedida((Long) UIBackingUtilities.toFirstKeySelectItem(unidadesMedidas));				
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doLoadUnidadesMedidas
	
	private void loadCategorias(){
		List<UISelectItem> categorias= null;
		Map<String, Object>params    = null;		
		EAccion eaccion              = null;
		try {
			params= new HashMap<>();			
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			categorias= UISelect.build("TcManticCategoriasDto", "row", params, "traza", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("categorias", categorias);
			eaccion= (EAccion) this.attrs.get("accion");
			if(eaccion.equals(EAccion.AGREGAR))
				this.registroArticulo.getArticulo().setIdCategoria((Long) UIBackingUtilities.toFirstKeySelectItem(categorias));
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // loadCategorias
}
