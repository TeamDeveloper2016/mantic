package mx.org.kaana.mantic.explorar.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.explorar.comun.Pedido;
import org.primefaces.model.StreamedContent;

@Named(value = "manticExplorarFiltro")
@ViewScoped
public class Filtro extends Pedido implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;	
	
	private List<Entity> lazyModel;	

	public List<Entity> getLazyModel() {
		return lazyModel;
	}	
	
	@PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
	
	public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= this.toPrepare();
    try {
      columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = DaoFactory.getInstance().toEntitySet("VistaOrdenesComprasDto", "porTodos", params);
			UIBackingUtilities.toFormatEntitySet(this.lazyModel, columns);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally		
  } // doLoad
	
	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap<>();
		StringBuilder sb            = null;
		try {
			sb= new StringBuilder();						
			if(this.attrs.get("nombre")!= null && ((UISelectEntity)this.attrs.get("nombre")).getKey()> 0L) 
				sb.append("tc_mantic_articulos.id_articulo=").append(((UISelectEntity)this.attrs.get("nombre")).getKey()).append(" and ");						
  		else 
	  		if(!Cadena.isVacio(JsfBase.getParametro("nombre_input"))) { 
					String nombre= JsfBase.getParametro("nombre_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
		  		sb.append("(tc_mantic_articulos.nombre regexp '.*").append(nombre.toUpperCase()).append(".*' or tc_mantic_articulos.descripcion regexp '.*").append(nombre.toUpperCase()).append(".*') and ");				
				} // if	
			if(Cadena.isVacio(sb.toString()))
				regresar.put("condicion", Constantes.SQL_VERDADERO);
			else
			  regresar.put("condicion", sb.substring(0, sb.length()- 4));			
  	  regresar.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCondicion		  
  
	public StreamedContent doPrepareImage(Entity row) {
		StreamedContent regresar= null;
		try {
			regresar= LoadImages.getImage(row.toLong("idKey"));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		return regresar;
	} // doPrepareImage
	
	public String doAceptar() {
		String regresar= "accion";		
    JsfBase.setFlashAttribute("retorno", "filtro");
    JsfBase.setFlashAttribute("articulo", this.attrs.get("articulo"));
		return regresar.concat(Constantes.REDIRECIONAR);
	} // doAceptar	
}