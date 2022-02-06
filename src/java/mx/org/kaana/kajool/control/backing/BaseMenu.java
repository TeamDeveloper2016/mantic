package mx.org.kaana.kajool.control.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/02/2022
 *@time 01:13:40 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class BaseMenu extends IBaseImportar implements Serializable {

  private static final Log LOG = LogFactory.getLog(BaseMenu.class);
  private static final long serialVersionUID = 7498799884513473226L;

  @Inject 
  private TemaActivo temaActivo;
  private MenuModel model;

  public TemaActivo getTemaActivo() {
    return temaActivo;
  }

  public void setTemaActivo(TemaActivo temaActivo) {
    this.temaActivo = temaActivo;
  }
	
  public Boolean getExistsXml() {
    return this.attrs.get("xmlFile")!= null;
  }

  public Boolean getExistsPdf() {
    return this.attrs.get("pdfFile")!= null;
  }

	public StreamedContent getPdfFileDownload() {
		return this.toPdfFileDownload((Entity)this.attrs.get("pdfFile")); 
	}
	
	public StreamedContent getXmlFileDownload() {
		return this.toXmlFileDownload((Entity)this.attrs.get("xmlFile")); 
	}

  public MenuModel getModel() {
    return model;
  }
  
  @Override
  @PostConstruct
  protected void init() {
		this.attrs.put("rfc", ""); // ATT1811272YA
		this.attrs.put("folio", ""); // 2021040490
		this.attrs.put("seguridad", ""); // 443c2f2e2d2c291708 12144402
		this.attrs.put("codigo", "");
		this.attrs.put("pdfFile", null);
		this.attrs.put("xmlFile", null);
    this.doLoad();
  }
  
  @Override
	public void doLoad() {
    List<Columna> columns     = null;    
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);      
      params.put("sortOrder", "order by tc_mantic_productos_marcas.nombre");
      columns = new ArrayList<>();
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      List<Entity> marcas= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosMarcasDto", "lazy", params);
      List<Entity> menu  = (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosCategoriasDto", "menu", params);
      // MENU DINAMICO
      this.model = new DefaultMenuModel();
      DefaultMenuItem item = new DefaultMenuItem("Inicio");
      item.setIcon("fa fa-home");
      item.setUrl("/indice.jsf".concat(Constantes.REDIRECIONAR));
      this.model.addElement(item);      
      DefaultSubMenu sub= new DefaultSubMenu("Categorías");
      sub.setIcon("fa fa-picture-o");
      if(menu!= null && !menu.isEmpty()) {
        for (Entity entity: menu) {
          item= new DefaultMenuItem(entity.toString("categoria"));
          item.setIcon("fa ".concat(entity.toString("icon")));
//          item.setCommand("#{kajoolAccesoIndice.doOpcion(0)}");
//          item.setOncomplete("showOpcion(0); categoria('".concat(entity.toString("nombre")).concat("');"));
          sub.addElement(item);      
        } // for
      } // if  
      this.model.addElement(sub);
      sub= new DefaultSubMenu("Marcas");
      sub.setIcon("fa fa-dashcube");
      if(marcas!= null && !marcas.isEmpty()) {
        for (Entity entity: marcas) {
          item= new DefaultMenuItem(entity.toString("descripcion"));
          item.setIcon("fa fa-picture-o");
//          item.setCommand("#{kajoolAccesoIndice.doOpcion(1)}");
//          item.setOncomplete("showOpcion(1); procesar('".concat(entity.toString("descripcion")).concat("');"));
          sub.addElement(item);      
        } // for
      } // if  
      this.model.addElement(sub);
      sub= new DefaultSubMenu("Facturación");
      sub.setIcon("fa fa-qrcode");
      item = new DefaultMenuItem("Desargar");
      item.setIcon("fa fa-download");
      item.setUrl("/Control/descargar.jsf".concat(Constantes.REDIRECIONAR));
      sub.addElement(item);      
      item = new DefaultMenuItem("Generar");
      item.setIcon("fa fa-print");
      item.setUrl("/Control/generar.jsf".concat(Constantes.REDIRECIONAR));
      sub.addElement(item);      
      this.model.addElement(sub);      
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	}  

  protected void toLoadDocumentos() {
		Map<String, Object> params= new HashMap<>();
		try {
      params.put("rfc", this.attrs.get("rfc"));
      params.put("folio", this.attrs.get("folio"));
      params.put("idTipoArchivo", "2");
      this.attrs.put("pdfFile", DaoFactory.getInstance().toEntity("VistaFicticiasDto", "descargas", params)); 			
      params.put("idTipoArchivo", "1");
      this.attrs.put("xmlFile", DaoFactory.getInstance().toEntity("VistaFicticiasDto", "descargas", params)); 			
      if(this.attrs.get("xmlFile")== null || this.attrs.get("pdfFile")== null) 
        JsfBase.addAlert("Error", "La factura no existe con los datos propocionados !", ETipoMensaje.ERROR);
      else 
        UIBackingUtilities.execute("$(\"div[role='1']\").removeClass('Container100');$(\"div[role]\").addClass('Container50');$(\"div[role='2']\").show();$('#download').click();");
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		this.attrs.put("codigo", "");
  }
  
  protected void doViewPdfDocument() { 
		this.toCopyDocument(((Entity)this.attrs.get("pdfFile")).toString("alias"), ((Entity)this.attrs.get("pdfFile")).toString("archivo"));
	}

	protected void doViewXmlDocument() {
		this.toViewFile(((Entity)this.attrs.get("xmlFile")).toString("alias"));
	}  

  public void doSearchItem() {
    LOG.info(this.attrs.get("codigo"));
  } 

  public void doSearchMain(String codigo) {
    LOG.info(codigo);
  }
  
}
