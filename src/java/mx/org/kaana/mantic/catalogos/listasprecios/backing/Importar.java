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
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.catalogos.comun.IBaseImportar;
import mx.org.kaana.mantic.catalogos.listasprecios.reglas.Transaccion;
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
      this.idListaPrecio= JsfBase.getFlashAttribute("idListaPrecio")== null? -1L: (Long)JsfBase.getFlashAttribute("idListaPrecio");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("tipo", "0");
      this.attrs.put("registros", 0);
      this.doLoadProveedores();
			if(this.idListaPrecio== -1L) {
				this.lista = new TcManticListasPreciosDto();
				this.lista.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
				this.lista.setIdUsuario(JsfBase.getIdUsuario());
				this.lista.setLogotipo(Configuracion.getInstance().getEmpresa("icon"));
        this.attrs.put("isDeshabilitado", false);
        this.lista.setIdProveedor((Long)this.attrs.get("idProveedor"));
        this.doLoadImportados("VistaListasArchivosDto", "importados", this.attrs);
      } // if
			else {
        this.lista= (TcManticListasPreciosDto)DaoFactory.getInstance().findById(TcManticListasPreciosDto.class, idListaPrecio);
        this.attrs.put("isDeshabilitado", true);
				if(lista.getIdProveedor()!= null) {
          this.attrs.put("ikProveedor",(((List<UISelectEntity>)this.attrs.get("proveedores")).get(((List<UISelectEntity>)this.attrs.get("proveedores")).indexOf(new UISelectEntity(this.lista.getIdProveedor().toString())))));
					this.attrs.put("idProveedor", this.lista.getIdProveedor());
			  } // if	
				else
          this.attrs.put("tipo", "1");
        this.doLoadImportados("VistaListasArchivosDto", "importados", this.lista.toMap());
      } // else
      this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.lista.getIdProveedor());
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_LISTA_ARCHIVOS);
			this.attrs.put("logotipos", Constantes.PATRON_IMPORTAR_LOGOTIPOS);
			this.attrs.put("observaciones", ""); 
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
      proveedores = UIEntity.build("TcManticProveedoresDto", "sucursales", params);
      this.attrs.put("proveedores", proveedores);
      this.attrs.put("ikProveedor", (((List<UISelectEntity>)this.attrs.get("proveedores")).get(0)));
			this.attrs.put("idProveedor", (((List<UISelectEntity>)this.attrs.get("proveedores")).get(0)).getKey());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }
  
  public void doActualizarProveedor() {
    try {
      if(this.attrs.get("ikProveedor")!= null) {
			  this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, ((UISelectEntity)this.attrs.get("ikProveedor")).getKey());
        this.lista.setIdProveedor(this.proveedor.getIdProveedor());
				this.attrs.put("idProveedor", this.lista.getIdProveedor());
        this.doLoadImportados("VistaListasArchivosDto", "importados", this.attrs);
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  }

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados("VistaListasArchivosDto", "importados", this.attrs);
	}		

	public void doFileUpload(FileUploadEvent event) {
		if("0".equals((String)this.attrs.get("tipo"))) {
			if(Cadena.isVacio(this.proveedor.getClave()))
				UIBackingUtilities.execute("janal.show([{summary: 'Error:', detail: 'No tiene definido una CLAVE el proveedor, por favor defina una clave.'}]);"); 
			else
		    this.doFileUpload(event, this.lista.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"), this.proveedor.getClave());
		} // if	
		else
			if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.PDF.name())) {
  			if(!Cadena.isVacio(lista.getNombre()))
  				this.doFileUpload(event, this.lista.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"), lista.getNombre());
			}	// IF
	    else
				UIBackingUtilities.execute("janal.show([{summary: 'Error:', detail: 'Solo se pueden importar catalogos en formato PDF ["+ event.getFile().getFileName().toUpperCase()+ "].'}]);"); 
		this.attrs.put("registros", this.getArticulos().size());
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
			if("0".equals((String)this.attrs.get("tipo")))
				this.lista.setNombre("");
			if(this.getXls()!= null && Cadena.isVacio(this.getXls().getObservaciones()))
			  this.getXls().setObservaciones(this.attrs.get("observaciones")!= null? (String)this.attrs.get("observaciones"): null);
			if(this.getPdf()!= null && Cadena.isVacio(this.getPdf().getObservaciones()))
			  this.getPdf().setObservaciones(this.attrs.get("observaciones")!= null? (String)this.attrs.get("observaciones"): null);
      Transaccion transaccion= new Transaccion(this.lista, this.getArticulos(), this.getXls(), this.getPdf());
      if(transaccion.ejecutar(EAccion.COMPLEMENTAR)) {
        UIBackingUtilities.execute("janal.alert('Se actualizo y se importaron los catalogos de forma correcta !');");
        regresar= this.doCancelar();
      } // if
      else
        UIBackingUtilities.execute("janal.alert('Se deben de seleccionar archivo en formato XLS/PDF');");
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

	public void doChangeTipo() {
		if("0".equals((String)this.attrs.get("tipo")))
		  this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_LISTA_ARCHIVOS);
	  else	
		  this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_CATALOGOS);
	}

	public void doImageUpload(FileUploadEvent event) {
		if("1".equals((String)this.attrs.get("tipo"))) {
		  this.doImageUpload(event, "proveedores");
			this.lista.setLogotipo(this.getFile().getName());
		} // if	
	} 
	
}