package mx.org.kaana.mantic.catalogos.categorias.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import mx.org.kaana.libs.formato.Error;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;

@ManagedBean(name="manticCatalogosCategoriasFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable{

	@PostConstruct
	@Override
	protected void init() {
		try {
			this.attrs.put("nombre", "");
			this.attrs.put("descripcion", "");
			this.attrs.put(Constantes.SQL_CONDICION, "id_empresa=1");
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
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));			
			this.lazyModel= new FormatLazyModel("TcManticCategoriasDto", "find", this.attrs, campos);
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
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idCategoria", eaccion.equals(EAccion.MODIFICAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
	} // doAccion
}
