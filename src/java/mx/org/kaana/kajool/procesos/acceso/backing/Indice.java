package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
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
@RequestScoped
@Named(value = "kajoolAccesoIndice")
public class Indice extends IBaseImportar implements Serializable {

  private static final long serialVersionUID = 5323749709626263801L;
  
  @Inject 
  private TemaActivo temaActivo;
  private static final Log LOG = LogFactory.getLog(Indice.class);
	private StreamedContent pdfFileDownload;
	private StreamedContent xmlFileDownload;

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
		return this.pdfFileDownload; 
	}
	
	public StreamedContent getXmlFileDownload() {
		return this.xmlFileDownload; 
	}
	
  @Override
  @PostConstruct
  protected void init() {
		this.attrs.put("rfc", "BCO131129C26");
		this.attrs.put("folio", "201900001");
		this.attrs.put("codigo", "201900001");
		this.attrs.put("pdfFile", null);
		this.attrs.put("xmlFile", null);
  }

  public void doRecoverTicket() {
		Map<String, Object> params= new HashMap<>();
		try {
			String codigo= this.toDecodeHash((String)this.attrs.get("codigo"));
			String encode= this.attrs.get("encode")== null? "": (String)this.attrs.get("encode");
			if(true || encode.equals(codigo)) {
				params.put("rfc", this.attrs.get("rfc"));
				params.put("folio", this.attrs.get("folio"));
				params.put("idTipoArchivo", "2");
				this.attrs.put("pdfFile", DaoFactory.getInstance().toEntity("VistaFicticiasDto", "descargas", params)); 			
				params.put("idTipoArchivo", "1");
				this.attrs.put("xmlFile", DaoFactory.getInstance().toEntity("VistaFicticiasDto", "descargas", params)); 			
				if(this.attrs.get("xmlFile")== null || this.attrs.get("pdfFile")== null) {
					JsfBase.addAlert("Error", "La factura no existe con los datos propocionados !", ETipoMensaje.ERROR);
					this.attrs.put("codigo", "");
				}	 // if
				else {
					this.pdfFileDownload= this.toPdfFileDownload((Entity)this.attrs.get("pdfFile"));
					this.xmlFileDownload= this.toXmlFileDownload((Entity)this.attrs.get("xmlFile"));
					UIBackingUtilities.execute("$('#download').click();");
				} // if	
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
