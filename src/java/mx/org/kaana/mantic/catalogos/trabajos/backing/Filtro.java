package mx.org.kaana.mantic.catalogos.trabajos.backing;

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
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.catalogos.trabajos.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticTrabajosDto;

@Named(value = "manticCatalogosTrabajosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("codigo", "");
      this.attrs.put("nombre", "");      
      this.attrs.put("descripcion", "");      
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());      
      this.attrs.put("sortOrder", "order by tc_mantic_trabajos.codigo");
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
      campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));            
      campos.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));            
      campos.add(new Columna("precio", EFormatoDinamicos.MONEDA_SAT_DECIMALES));            
      this.lazyModel = new FormatCustomLazy("VistaTrabajosDto", "row", this.attrs, campos);
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
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idTrabajo", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
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
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion(new TcManticTrabajosDto(seleccionado.getKey()));
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar trabajo", "El trabajo se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar trabajo", "Ocurrió un error al eliminar el trabajo.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar
	
  public String doMasivo() {
    JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Trabajos/filtro");
    JsfBase.setFlashAttribute("idTipoMasivo", ECargaMasiva.REFACCIONES.getId());
    return "/Paginas/Mantic/Catalogos/Masivos/importar".concat(Constantes.REDIRECIONAR);
	}

}
