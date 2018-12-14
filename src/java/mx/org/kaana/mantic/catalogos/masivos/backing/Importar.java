package mx.org.kaana.mantic.catalogos.masivos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.mantic.catalogos.comun.IBaseImportar;
import mx.org.kaana.mantic.catalogos.masivos.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.db.dto.TcManticMasivasArchivosDto;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

@Named(value= "manticCatalogosMasivosImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG=LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 318633488565639363L;
	
	private TcManticMasivasArchivosDto masivo;
	private ECargaMasiva categoria;

	public TcManticMasivasArchivosDto getMasivo() {
		return masivo;
	}

	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_MASIVO);
			this.categoria= ECargaMasiva.ARTICULOS;
			this.attrs.put("observaciones", ""); 
			this.attrs.put("xls", ""); 
			this.masivo = new TcManticMasivasArchivosDto(
				-1L, // Long idMasivaArchivo, 
				Configuracion.getInstance().getPropiedadSistemaServidor("masivos"), // String ruta, 
				categoria.getId(), // Long idTipoMasivo, 
				1L, // Long idMasivaEstatus, 
				"", // String nombre, 
				0L, // Long tamanio, 
			  JsfBase.getIdUsuario(), // Long idUsuario, 
				8L, // Long idTipoArchivo, 
				0L, // Long tuplas, 
				"", // String observaciones, 
				"", // String alias, 
				JsfBase.getAutentifica().getEmpresa().getIdEmpresa()
			);
      setArticulos(new ArrayList<>());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
  
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados("VistaCargasMasivasDto", "importados", this.attrs);
	}		

	public void doFileUpload(FileUploadEvent event) {
    this.doFileUploadMasivo(event, this.masivo.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("masivos"), this.masivo, this.categoria);
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
      Transaccion transaccion= new Transaccion(this.masivo, this.getXls());
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
		switch(this.masivo.getIdTipoMasivo().intValue()) {
			case 1: this.categoria= ECargaMasiva.ARTICULOS;
				break;
			case 2: this.categoria= ECargaMasiva.CLIENTES;
				break;
			case 3: this.categoria= ECargaMasiva.PROVEEDORES;
				break;
		} // switch
	}

	public void doImageUpload(FileUploadEvent event) {
		
	} 
	
}