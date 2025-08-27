package mx.org.kaana.mantic.catalogos.personas.backing;

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
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.personas.beans.RegistroPersona;
import mx.org.kaana.mantic.catalogos.personas.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.personas.reglas.Gestor;
import mx.org.kaana.mantic.enums.ETipoPersona;

@Named(value = "manticCatalogosPersonasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {			
			this.attrs.put("nombres", "");
			this.attrs.put("paterno", "");
			this.attrs.put("materno", "");
			this.attrs.put("rfc", "");
			this.attrs.put("curp", "");
      this.attrs.put("sortOrder", "order by tc_mantic_personas.nombres");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
      this.loadTiposPersonas();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
  
  private void loadTiposPersonas() throws Exception {
    Gestor gestor = new Gestor();
    gestor.loadTiposPersonas();
    this.attrs.put("tiposPersonas", gestor.getTiposPersonas());
    this.attrs.put("tipoPersona", UIBackingUtilities.toFirstKeySelectEntity(gestor.getTiposPersonas()));
  } 
  
  @Override
  public void doLoad() {
    List<Columna> columns= new ArrayList<>();
    try {
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("curp", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("sexo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("tipoPersona", EFormatoDinamicos.MAYUSCULAS));     
      this.attrs.put("idTipoPersona",((UISelectEntity)this.attrs.get("tipoPersona")).getKey().equals(-1L)?toAllTiposPersonas():((UISelectEntity)this.attrs.get("tipoPersona")).getKey());
      this.lazyModel = new FormatCustomLazy("VistaPersonasDto", "row", this.attrs, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad

	private String toAllTiposPersonas () {
    StringBuilder regresar= new StringBuilder();
    List<UISelectEntity> tiposPersonas= (List<UISelectEntity>) this.attrs.get("tiposPersonas");
    for (UISelectEntity tipoPersona: tiposPersonas)
      regresar.append(tipoPersona.getKey()).append(",");
    return regresar.substring(0,regresar.length()-1);
  } 
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("tipoPersona", ETipoPersona.EMPLEADO.getIdTipoPersona());		
			JsfBase.setFlashAttribute("retorno", "filtro");		
			JsfBase.setFlashAttribute("idPersona", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } 
  
  public void doEliminar() {
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		RegistroPersona persona= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			persona= new RegistroPersona();
			persona.setIdPersona(seleccionado.getKey());
			transaccion= new Transaccion(persona);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar persona", "La persona se ha eliminado correctamente", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar persona", "Ocurrió un error al eliminar la persona", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
  } 
  
}
