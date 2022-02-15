package mx.org.kaana.kajool.control.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.org.kaana.kajool.control.bean.Portal;
import mx.org.kaana.kajool.control.enums.EBusqueda;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.menu.MenuModel;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/02/2022
 *@time 01:13:40 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@ViewScoped
@Named(value = "kajoolControlBaseMenu")
public class BaseMenu extends IBaseImportar implements Serializable {

  private static final Log LOG = LogFactory.getLog(BaseMenu.class);
  private static final long serialVersionUID = 7498799884513473226L;

  @Inject 
  private TemaActivo temaActivo;
  
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

  public MenuModel getMenuGeneral() {
    return Portal.getInstance().getMenu();
  }
  
  public MenuModel getMenuCategorias() {
    return Portal.getInstance().getCategorias();
  }
  
  public MenuModel getMegaCategorias() {
    return Portal.getInstance().getMegaCategorias();
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
        UIBackingUtilities.execute("$(\"div[panel='1']\").removeClass('Container100');$(\"div[panel]\").addClass('Container50');$(\"div[panel='2']\").show();$('#download').click();");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
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

  public void doSearchMain(String codigo) {
    JsfBase.setFlashAttribute("codigo", codigo);
  }

  public void doSearchOther(String codigo, String busqueda) {
    JsfBase.setFlashAttribute("codigo", codigo);
    JsfBase.setFlashAttribute("busqueda", EBusqueda.valueOf(busqueda));
  }

}
