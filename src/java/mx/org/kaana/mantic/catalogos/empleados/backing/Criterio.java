package mx.org.kaana.mantic.catalogos.empleados.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.personas.beans.RegistroPersona;
import mx.org.kaana.mantic.enums.ETipoPersona;

@Named(value = "manticCatalogosEmpleadosCriterio")
@ViewScoped
public class Criterio extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.toLoadPuestos();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } 

  @Override
  public void doLoad() {
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= this.toPrepare();
    try {
      params.put("sortOrder", "order by tc_mantic_personas.nombres");
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("sexo", EFormatoDinamicos.MAYUSCULAS));      
      this.lazyModel = new FormatCustomLazy("VistaPersonasDto", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } 
  
	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap<>();
		StringBuilder sb            = new StringBuilder("");
		try {
  		sb.append("(tc_mantic_personas.id_tipo_persona in (").append(ETipoPersona.EMPLEADO.getIdTipoPersona()).append(")) and ");	
      if(!Cadena.isVacio(this.attrs.get("nombre"))) { 
        String nombre= ((String)this.attrs.get("nombre")).replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*");
        sb.append("(concat(tc_mantic_personas.nombres, ' ', tc_mantic_personas.paterno, ' ', tc_mantic_personas.materno) regexp '.*").append(nombre).append(".*') and ");				
      } // if	
      if(!Cadena.isVacio(this.attrs.get("rfc"))) 
        sb.append("(tc_mantic_personas.rfc like '").append(this.attrs.get("rfc")).append("%') and ");				
			if(!Cadena.isVacio(this.attrs.get("idPuesto")) && !Objects.equals((Long)this.attrs.get("idPuesto"), -1L))
				sb.append("(tr_mantic_empresa_personal.id_puesto in (").append(this.attrs.get("idPuesto")).append(")) and ");	
			if(!Cadena.isVacio(this.attrs.get("idTipoSexo")))
				sb.append("(tc_mantic_personas.id_tipo_persona in (").append(this.attrs.get("idTipoSexo")).append(")) and ");	
			if(!Cadena.isVacio(this.attrs.get("idActivo")))
				sb.append("(tr_mantic_empresa_personal.id_activo in (").append(this.attrs.get("idActivo")).append(")) and ");	
  		regresar.put(Constantes.SQL_CONDICION, Cadena.isVacio(sb.toString())? Constantes.SQL_VERDADERO: sb.substring(0, sb.length()- 4));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} 

  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("tipoPersona", this.attrs.get("idTipoPersona"));		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Empleados/filtro");		
			JsfBase.setFlashAttribute("idPersona", (eaccion.equals(EAccion.CONSULTAR) || eaccion.equals(EAccion.MODIFICAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Catalogos/Personas/accion".concat(Constantes.REDIRECIONAR);
  } 

	private void toLoadPuestos() {
		List<UISelectItem> puestos= null;
    Map<String, Object> params= new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, "id_puesto!= 10");
      puestos = UISelect.seleccione("TcManticPuestosDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS, "idPuesto");
			if(!puestos.isEmpty()) {
				this.attrs.put("puestos", puestos);
				this.attrs.put("idPuesto", UIBackingUtilities.toFirstKeySelectItem(puestos));
			} // if
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
	} 

  public void doEliminar() {
		mx.org.kaana.mantic.catalogos.personas.reglas.Transaccion transaccion= null;
		Entity seleccionado    = (Entity) this.attrs.get("seleccionado");
		RegistroPersona persona= null;
		try {
			persona= new RegistroPersona();
			persona.setIdPersona(seleccionado.getKey());
			transaccion= new mx.org.kaana.mantic.catalogos.personas.reglas.Transaccion(persona);
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