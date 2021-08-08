package mx.org.kaana.mantic.ventas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.beans.Imagen;

@Named(value= "manticVentasGaleria")
@ViewScoped
public class Galeria extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;
	
  private List<Imagen> images;  
  private String path;

  public List<Imagen> getImages() {
    return images;
  }
  
  public String getPath() {
    return path;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/1/");
      this.attrs.put("codigo", "");
      this.attrs.put("buscaPorCodigo", false);
      this.attrs.put("viewDetail", false);
      this.attrs.put("item", new Entity());
      
      this.images = new ArrayList<>();
      for (int i = 1; i <= 12; i++) {
        this.images.add(new Imagen(new Long(i), "sin-foto.png", "SIN-FOTO_"+ i));
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
  	int buscarCodigoPor       = 2;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
			String search= (String) this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				if((boolean)this.attrs.get("buscaPorCodigo"))
			    buscarCodigoPor= 0;
				if(search.startsWith("."))
					buscarCodigoPor= 2;
				else 
					if(search.startsWith(":"))
						buscarCodigoPor= 1;
				if(search.startsWith(".") || search.startsWith(":"))
					search= search.trim().substring(1);				
				search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);	
			switch(buscarCodigoPor) {      
				case 0: 
          params.put("sortOrder", "order by tc_mantic_articulos_codigos.codigo");
          this.lazyModel = new FormatCustomLazy("VistaGaleriaDto", "porCodigoIgual", params, columns);
					break;
				case 1: 
          params.put("sortOrder", "order by tc_mantic_articulos_codigos.codigo");          
          this.lazyModel = new FormatCustomLazy("VistaGaleriaDto",  "porCodigo", params, columns);
					break;
				case 2:
    			params.put("sortOrder", "order by tc_mantic_articulos.nombre");
          this.lazyModel = new FormatCustomLazy("VistaGaleriaDto", "porNombre", params, columns);
          break;
			} // switch
      UIBackingUtilities.resetDataGrid();
      this.attrs.put("viewDetail", false);
      this.attrs.put("item", new Entity());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally		
  } // doLoad

  public void doViewDetail(Entity row) {
    this.attrs.put("viewDetail", true);
    this.attrs.put("item", row);
  }
  
	@Override
	protected void finalize() throws Throwable {
    super.finalize();		
	}	// finalize
  
}
