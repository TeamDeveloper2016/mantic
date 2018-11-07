package mx.org.kaana.mantic.facturas.backing;

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
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import mx.org.kaana.mantic.inventarios.entradas.reglas.Importados;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticFacturasImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG=LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353488565639367L;
	
	private Long idFactura;
	private Long idFicticia;

	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("idFactura")== null || JsfBase.getFlashAttribute("idFicticia")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
			else {
        this.idFactura = JsfBase.getFlashAttribute("idFactura")== null? -1L: (Long)JsfBase.getFlashAttribute("idFactura");
        this.idFicticia= JsfBase.getFlashAttribute("idFicticia")== null? -1L: (Long)JsfBase.getFlashAttribute("idFicticia");
				this.attrs.put("factura", DaoFactory.getInstance().findById(TcManticFacturasDto.class, this.idFactura));
				this.attrs.put("ficticia", DaoFactory.getInstance().findById(TcManticFicticiasDto.class, this.idFicticia));
			} // if
			this.doLoadImportados("VistaFicticiasDto", "importados", Variables.toMap("idFactura~"+ this.idFactura)); 
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_FACTURA);
			this.attrs.put("xml", ""); 
			this.attrs.put("pdf", ""); 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("facturama"));
	}

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("facturama"));
	}
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idFactura", this.idFactura);
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	
}