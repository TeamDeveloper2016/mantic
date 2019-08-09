package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.primefaces.model.StreamedContent;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolAccesoIndice")
public class Indice extends IBaseImportar implements Serializable {

  private static final Log LOG = LogFactory.getLog(Indice.class);
  private static final long serialVersionUID = 5323749709626263801L;
  
  @Inject 
  private TemaActivo temaActivo;
	private Integer salt;

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

	public Integer getSalt() {
		return salt;
	}
	
  @Override
  @PostConstruct
  protected void init() {
		this.attrs.put("rfc", "");
		this.attrs.put("folio", "");
		this.attrs.put("codigo", "");
		this.attrs.put("pdfFile", null);
		this.attrs.put("xmlFile", null);
		this.salt= (int)(Math.random()* 10000);
  }

  public void doRecoverTicket() {
		Map<String, Object> params= new HashMap<>();
		try {
			String codigo= this.toDecodeHash((String)this.attrs.get("codigo"));
			String encode= this.attrs.get("encode")== null? "": (String)this.attrs.get("encode");
			if(encode.equals(codigo)) {
				params.put("rfc", this.attrs.get("rfc"));
				params.put("folio", this.attrs.get("folio"));
				params.put("idTipoArchivo", "2");
				this.attrs.put("pdfFile", DaoFactory.getInstance().toEntity("VistaFicticiasDto", "descargas", params)); 			
				params.put("idTipoArchivo", "1");
				this.attrs.put("xmlFile", DaoFactory.getInstance().toEntity("VistaFicticiasDto", "descargas", params)); 			
				if(this.attrs.get("xmlFile")== null || this.attrs.get("pdfFile")== null) {
					JsfBase.addAlert("Error", "La factura no existe con los datos propocionados !", ETipoMensaje.ERROR);
				}	 // if
				else 
					UIBackingUtilities.execute("$(\"div[role='1']\").removeClass('Container100');$(\"div[role]\").addClass('Container50');$(\"div[role='2']\").show();$('#download').click();");
			} // if
			else 
				JsfBase.addAlert("Error", "El código de verificación, esta incorrecto !", ETipoMensaje.ERROR);
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

	@Override
	public void doLoad() {
	}
	
  public void doViewPdfDocument() { 
		this.toCopyDocument(((Entity)this.attrs.get("pdfFile")).toString("alias"), ((Entity)this.attrs.get("pdfFile")).toString("archivo"));
	}

	public void doViewXmlDocument() {
		this.toViewFile(((Entity)this.attrs.get("xmlFile")).toString("alias"));
	}
	
  private String toDecodeHash(String value) {
		int hash = 5381;
		value = value.toUpperCase();
		for(int i = 0; i < value.length(); i++) {
			hash = ((hash << 5) + hash) + value.charAt(i);
		} // for
		return String.valueOf(hash);
	}		
}