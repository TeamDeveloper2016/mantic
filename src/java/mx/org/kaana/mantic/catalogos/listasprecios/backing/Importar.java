package mx.org.kaana.mantic.catalogos.listasprecios.backing;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.catalogos.comun.IBaseImportar;
import mx.org.kaana.mantic.catalogos.listasprecios.reglas.Transaccion;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

@Named(value= "manticCatalogosListaPreciosImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG=LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 318633488565639367L;
	
	private TcManticListasPreciosDto lista;
	private TcManticProveedoresDto proveedor;
	private Long idListaPrecio;
  private boolean importXls;
  private boolean importPdf;

	public TcManticListasPreciosDto getOrden() {
		return lista;
	}

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}
  
	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("idListaPrecio")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      idListaPrecio= JsfBase.getFlashAttribute("idListaPrecio")== null? -1L: (Long)JsfBase.getFlashAttribute("idListaPrecio");
			this.lista= (TcManticListasPreciosDto)DaoFactory.getInstance().findById(TcManticListasPreciosDto.class, idListaPrecio);
			if(this.lista!= null) {
			  this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.lista.getIdProveedor());
        this.doLoadImportados("VistaListasArchivosDto", "importados", this.lista.toMap());
			} // if
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_LISTA_ARCHIVOS);
			this.attrs.put("xls", ""); 
			this.attrs.put("pdf", "");
      setArticulos(new ArrayList<>());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados("VistaListasArchivosDto", "importados", this.lista.toMap());
		else
		  if(event.getTab().getTitle().equals("Importar")) {
				this.doLoadFiles("TcManticListasPreciosArchivosDto", this.lista.getIdListaPrecio(), "idListaPrecio");
      }
	}		

	public void doFileUpload(FileUploadEvent event) {
		this.doFileUpload(event, this.lista.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"), this.proveedor.getClave(), this.lista.getKey().toString());
	} // doFileUpload	
	
	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"));
	}

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"));
	}
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idListaPrecio", this.idListaPrecio);
    //return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public String doAceptar(){
		String regresar= null;
		try {
      this.getXls().setObservaciones((String)this.attrs.get("observaciones"));
      this.getPdf().setObservaciones((String)this.attrs.get("observaciones"));
			Transaccion transaccion= new Transaccion(this.lista,getArticulos(), this.getXls(),this.getPdf());
      if(transaccion.ejecutar(EAccion.COMPLEMENTAR)) {
      	RequestContext.getCurrentInstance().execute("janal.alert('Se importaron los archivos de forma correcta !');");
				regresar= this.doCancelar();
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	}	
  
  public void doCompleto() {
		JsfBase.addMessage("Detalle del mensaje", "Se agregaron correctamente los artículos.", ETipoMensaje.INFORMACION);		
	} // doCompleto

}