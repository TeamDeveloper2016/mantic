package mx.org.kaana.kajool.control.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolControlArticulos")
public class Articulos extends BaseMenu implements Serializable {

  private static final Log LOG = LogFactory.getLog(Articulos.class);
  private static final long serialVersionUID = 5323749709626263802L;
  
  private String pathImage;
  
	public String getPathImage() {
		return pathImage;
	}
  
  @Override
  @PostConstruct
  protected void init() {
    super.init();
    this.attrs.put("codigo", JsfBase.getFlashAttribute("codigo")== null? "": JsfBase.getFlashAttribute("codigo"));
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.pathImage= dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");
    if(!Cadena.isVacio(this.attrs.get("codigo")))
      this.doLoadArticulos((String)this.attrs.get("codigo"));
  }

  @Override
  public void doLoad() {
  }
  
  public void doLoadArticulos(String codigo) {
    List<Columna> columns     = null;
		Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			codigo= !Cadena.isVacio(codigo)? codigo.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim(): "";
  		params.put("codigo", codigo);		
      params.put("sortOrder", "order by tc_mantic_articulos.nombre, tc_mantic_articulos.actualizado");
      if(!Cadena.isVacio(codigo))
        this.lazyModel= new FormatCustomLazy("VistaOrdenesComprasDto", "porAmbos", params, columns);
      UIBackingUtilities.resetDataTable();
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally    
  }
  
  public void doLoadDetalle() {
    Entity row= (Entity)this.attrs.get("seleccionado");
  }
  
}