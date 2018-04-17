package mx.org.kaana.mantic.catalogos.articulos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import mx.org.kaana.libs.formato.Error;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TcManticCategoriasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.categorias.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.categorias.reglas.Transaccion;

@ManagedBean(name="manticCatalogosArticulosFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable{

	@PostConstruct
	@Override
	protected void init() {
		try {
			this.attrs.put("codigo", "");			
			this.attrs.put("nombre", "");			
			this.attrs.put("descripcion", "");						
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
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
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));			
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
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idArticulo", eaccion.equals(EAccion.MODIFICAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
	} // doAccion
	
	public void doEliminar(){
		Transaccion transaccion= null;
		Entity categoria       = null;
		MotorBusqueda motor    = null;
		try {
			categoria= (Entity) this.attrs.get("seleccionado");
			motor= new MotorBusqueda(categoria.getKey());
			if(motor.isChild()){
				transaccion= new Transaccion(new TcManticCategoriasDto(categoria.getKey()));
				if(transaccion.ejecutar(EAccion.ELIMINAR))
					JsfBase.addMessage("Eliminar categoría", "El artículo se ha eliminado correctamente.", ETipoMensaje.ERROR);
				else
					JsfBase.addMessage("Eliminar categoría", "Ocurrió un error al eliminar la artículo.", ETipoMensaje.ERROR);					
			} // if
			else
				JsfBase.addMessage("Eliminar categoría", "No es posible eliminar el artículo, contiene dependencias.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doEliminar
}
