package mx.org.kaana.mantic.catalogos.listasprecios.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
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

	public TcManticListasPreciosDto getLista() {
		return lista;
	}

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}
  
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      doLoadProveedores();
			if(JsfBase.getFlashAttribute("idListaPrecio")== null){
				this.lista = new TcManticListasPreciosDto();
        this.attrs.put("isDeshabilitado", false);
        if(this.attrs.get("idProveedor")!= null) {
          this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, ((UISelectEntity)this.attrs.get("idProveedor")).getKey());
          this.lista.setIdProveedor(this.proveedor.getIdProveedor());
          this.doLoadImportados("VistaListasArchivosDto", "importados", this.lista.toMap());
        }
      }
      else{
        idListaPrecio= JsfBase.getFlashAttribute("idListaPrecio")== null? -1L: (Long)JsfBase.getFlashAttribute("idListaPrecio");
        this.lista= (TcManticListasPreciosDto)DaoFactory.getInstance().findById(TcManticListasPreciosDto.class, idListaPrecio);
        this.attrs.put("idProveedor",(((List<UISelectEntity>)this.attrs.get("proveedores")).get(((List<UISelectEntity>)this.attrs.get("proveedores")).indexOf(new UISelectEntity(this.lista.getIdProveedor().toString())))));
        this.attrs.put("isDeshabilitado", true);
        this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.lista.getIdProveedor());
        this.doLoadImportados("VistaListasArchivosDto", "importados", this.lista.toMap());
      }
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
  
  public void doLoadProveedores() {
    Map<String, Object> params      = null;
    List<UISelectEntity> proveedores= null;
    try {
      params = new HashMap();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      if(JsfBase.getFlashAttribute("idListaPrecio") == null)
      proveedores = UIEntity.build("TcManticProveedoresDto", "listasPrecios", params);
      else
        proveedores = UIEntity.build("TcManticProveedoresDto", "sucursales", params);
      attrs.put("proveedores", proveedores);
      attrs.put("idProveedor", (((List<UISelectEntity>)this.attrs.get("proveedores")).get(0)));
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
    finally {
      Methods.clean(params);
    }
  }
  
  public void doActualizarProveedor() {
    try {
      if(this.attrs.get("idProveedor")!= null) {
			  this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, ((UISelectEntity)this.attrs.get("idProveedor")).getKey());
        this.lista.setIdProveedor(this.proveedor.getIdProveedor());
        this.doLoadImportados("VistaListasArchivosDto", "importados", this.lista.toMap());
      }
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
  }

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados("VistaListasArchivosDto", "importados", this.lista.toMap());
		else
		  if(event.getTab().getTitle().equals("Importar")) {
				this.doLoadFiles("TcManticListasPreciosArchivosDto", this.lista.getIdListaPrecio(), "idListaPrecio");
      }
	}		

	public void doFileUpload(FileUploadEvent event) {
		this.doFileUpload(event, this.lista.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"), this.proveedor.getClave());
	} // doFileUpload	
	
	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"));
	}

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"));
	}
	
  public String doCancelar() {   
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public String doAceptar() {
		String regresar= null;
		try {
      if(this.getXls()!= null)
        this.getXls().setObservaciones((String)this.attrs.get("observaciones"));
      if(this.getPdf()!= null)
        this.getPdf().setObservaciones((String)this.attrs.get("observaciones"));
      this.lista.setIdProveedor(((UISelectEntity)attrs.get("idProveedor")).getKey());
      this.lista.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
      Transaccion transaccion= new Transaccion(this.lista,getArticulos(), this.getXls(),this.getPdf());
      if(transaccion.ejecutar(EAccion.COMPLEMENTAR)) {
        RequestContext.getCurrentInstance().execute("janal.alert('Se importaron los archivos de forma correcta !');");
        regresar= this.doCancelar();
      }//if
      else
        RequestContext.getCurrentInstance().execute("janal.alert('Debe importar archivo xls');");
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