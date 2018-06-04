package mx.org.kaana.mantic.catalogos.empresas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.empresas.beans.RegistroEmpresa;
import mx.org.kaana.mantic.enums.ETipoEmpresa;

@Named(value = "manticCatalogosEmpresasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("clave", "");
      this.attrs.put("nombre", "");
      this.attrs.put("titulo", "");
      this.attrs.put("sortOrder", "order by tc_mantic_empresas.nombre");
			loadTiposEmpresa();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> campos = null;
    try {
      campos = new ArrayList<>();
      campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));            
      this.lazyModel = new FormatCustomLazy("VistaEmpresasDto", "row", this.attrs, campos);
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

	private void loadTiposEmpresa(){
		List<UISelectItem> tiposEmpresas= null;
		String all                      = "";
		try {
			tiposEmpresas= new ArrayList<>();
			for(ETipoEmpresa tipoEmpresa: ETipoEmpresa.values()){
				tiposEmpresas.add(new UISelectItem(tipoEmpresa.getIdTipoEmpresa(), tipoEmpresa.name().toUpperCase()));
				all= all.concat(tipoEmpresa.getIdTipoEmpresa().toString().concat(","));
			} // for
			tiposEmpresas.add(0, new UISelectItem(all.substring(0, all.length()-1), "TODOS"));
			this.attrs.put("tiposEmpresa", tiposEmpresas);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposEmpresa
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idEmpresa", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		RegistroEmpresa registro= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			registro= new RegistroEmpresa();
			registro.setIdEmpresa(seleccionado.getKey());
			transaccion= new Transaccion(registro);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar empresa", "La empresa se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar empresa", "Ocurrió un error al eliminar la empresa.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar
}
