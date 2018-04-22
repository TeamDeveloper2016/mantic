package mx.org.kaana.mantic.catalogos.almacenes.backing;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;

@Named(value="manticCatalogosAlamcesFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable{

	private static final long serialVersionUID = 8793667741599428879L;

	@PostConstruct
	@Override
	protected void init() {
		try {
			
			doLoad();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
	
	@Override
	public void doLoad() {
		List<Columna>campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));			
			this.lazyModel= new FormatCustomLazy("VistaArticulosDto","row", this.attrs, campos);
			UIBackingUtilities.resetDataTable();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		finally {
			Methods.clean(campos);
		} // finally		
	} // doLoad
	
	public String doAccion(String accion){
		EAccion eaccion= null;
		try {
			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
	} // doAccion
	
	public void doEliminar(){
		
	
		try {
			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doEliminar
}
