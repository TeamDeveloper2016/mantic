package mx.org.kaana.mantic.catalogos.articulos.backing;

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

@Named(value= "manticCatalogosArticulosImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG=LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 318633488565639367L;
	
	private TcManticListasPreciosDto lista;
	private TcManticProveedoresDto proveedor;

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
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			this.lista = new TcManticListasPreciosDto();
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("idTipoMasivo", 0L);
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_MASIVO);
			this.attrs.put("observaciones", ""); 
			this.attrs.put("xls", ""); 
      setArticulos(new ArrayList<>());
			this.doLoadImportados("VistaCargasMasivasDto", "importados", this.attrs);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
  
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados("VistaListasArchivosDto", "importados", this.lista.toMap());
	}		

	public void doFileUpload(FileUploadEvent event) {
    this.doFileUpload(event, this.lista.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("masivos"), this.proveedor.getClave());
	  // RequestContext.getCurrentInstance().execute("janal.show([{summary: 'Error:', detail: 'Solo se pueden importar catalogos en formato PDF ["+ event.getFile().getFileName().toUpperCase()+ "].'}]);"); 
	} // doFileUpload	
	
	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios"));
	}
	
  public String doCancelar() {   
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public String doAceptar() {
		String regresar= null;
		try {
			if("0".equals((String)this.attrs.get("tipo"))) {
        this.lista.setIdProveedor(((UISelectEntity)attrs.get("idProveedor")).getKey());
				this.lista.setNombre("");
			} // if	
			else 
				this.lista.setIdProveedor(null);
      this.lista.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.lista.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
      Transaccion transaccion= new Transaccion(this.lista, getArticulos(), this.getXls(), this.getPdf());
      if(transaccion.ejecutar(EAccion.COMPLEMENTAR)) {
        RequestContext.getCurrentInstance().execute("janal.alert('Se actualizo y se importaron los catalogos de forma correcta !');");
        regresar= this.doCancelar();
      }//if
      else
        RequestContext.getCurrentInstance().execute("janal.alert('Se deben de seleccionar archivo en formato XLS');");
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
		
	}

	public void doImageUpload(FileUploadEvent event) {
		
	} 
	
}