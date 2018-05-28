package mx.org.kaana.mantic.catalogos.categorias.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.mantic.db.dto.TcManticCategoriasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.mantic.catalogos.categorias.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.categorias.reglas.Transaccion;

@Named(value = "manticCatalogosCategoriasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

	@PostConstruct
	@Override
	protected void init() {
		try {
			this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));						
			this.attrs.put("idCategoria", JsfBase.getFlashAttribute("idCategoria"));						
			loadNodos();
			doLoad();			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
	
	public void doLoad() {
		EAccion eaccion       = null;
		MotorBusqueda busqueda= null;		
		Long idCategoriaParent= null;
		try {
			eaccion= (EAccion) this.attrs.get("accion");
			this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
			switch(eaccion){
				case AGREGAR:
					this.attrs.put("dto", new TcManticCategoriasDto());
					break;
				case MODIFICAR:
				case CONSULTAR:
					busqueda= new MotorBusqueda(Long.valueOf(this.attrs.get("idCategoria").toString()));
					this.attrs.put("dto", busqueda.toCategoria());					
					idCategoriaParent= busqueda.toParent();
					this.attrs.put("nodo", idCategoriaParent);
					this.attrs.put("parentNodo", idCategoriaParent);
					break;
			} // switch
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doLoad
	
	public String doAceptar(){
		TcManticCategoriasDto dto= null;
		Transaccion transaccion  = null;
		try {
			dto= loadDto();
			transaccion= new Transaccion(dto);
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
	
	private TcManticCategoriasDto loadDto() throws Exception{
		TcManticCategoriasDto regresar= null;
		TcManticCategoriasDto parent  = null;
		Long idCategoria              = -1L;		
		Long idCategoriaParent        = -1L;		
		try {
			regresar= (TcManticCategoriasDto) this.attrs.get("dto");		
			idCategoria= Long.valueOf(this.attrs.get("nodo").toString());									
			parent= toParent(idCategoria);
			regresar.setTraza(parent!= null ? parent.getTraza().concat(" >> ").concat(regresar.getNombre()) : regresar.getNombre());			
			if(EAccion.AGREGAR.equals((EAccion) this.attrs.get("accion"))){				
				regresar.setIdCfgClaves(1L);
				regresar.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());	
				regresar.setClave(toClave(idCategoria));				
				regresar.setNivel(parent!= null ? parent.getNivel() + 1L : 1L);							
			} // if
			else{	
				idCategoriaParent= Long.valueOf(this.attrs.get("parentNodo").toString());			
				if(!idCategoria.equals(idCategoriaParent)){												
					regresar.setClave(toClave(idCategoria));		
					regresar.setNivel(parent!= null ? parent.getNivel() + 1L : 1L);							
				} // if								
			} // if													
			regresar.setIdUsuario(JsfBase.getIdUsuario());									
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadDto
	
	private String toClave(Long idCategoria) throws Exception{
		MotorBusqueda motor= null;		
		String regresar    = null;
		try {
			motor= new MotorBusqueda(idCategoria);									
			regresar= motor.toNextClave();
		} // try
		catch (Exception e) {						
			throw e;			
		} // catch
		return regresar;		
	} // toClave
	
	private TcManticCategoriasDto toParent(Long idCategoria) throws Exception{
		TcManticCategoriasDto regresar= null;
		MotorBusqueda motor           = null;
		try {
			motor= new MotorBusqueda(idCategoria);
			regresar= motor.toCategoria();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;						
	} // toParent
	
	public String doCancelar(){		
		return "filtro";
	} // doAccion
	
	private void loadNodos(){
		List<UISelectItem> nodos = null;
		Map<String, Object>params= null;
		EAccion eaccion          = null;
		try {
			params= new HashMap<>();			
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			eaccion= (EAccion) this.attrs.get("accion");
			if(eaccion.equals(EAccion.MODIFICAR))
				params.put(Constantes.SQL_CONDICION, "id_categoria <> " + this.attrs.get("idCategoria").toString());
			nodos= UISelect.build("TcManticCategoriasDto", "row", params, "traza", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("nodos", nodos);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // loadNodos
}
