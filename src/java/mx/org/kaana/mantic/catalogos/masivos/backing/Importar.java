package mx.org.kaana.mantic.catalogos.masivos.backing;

import java.io.Serializable;
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
			if(JsfBase.getFlashAttribute("idTipoMasivo")!= null)
				switch(((Long)JsfBase.getFlashAttribute("idTipoMasivo")).intValue()) {
					case 1:
						this.categoria= ECargaMasiva.ARTICULOS;
						break;
					case 2:
						this.categoria= ECargaMasiva.CLIENTES;
						break;
					case 3:
						this.categoria= ECargaMasiva.PROVEEDORES;
						break;
				} // switch
			else
				this.categoria= ECargaMasiva.ARTICULOS;
			this.attrs.put("xls", ""); 
			this.masivo = new TcManticMasivasArchivosDto(
				-1L, // Long idMasivaArchivo, 
				Configuracion.getInstance().getPropiedadSistemaServidor("masivos"), // String ruta, 
				categoria.getId(), // Long idTipoMasivo, 
				1L, // Long idMasivaEstatus, 
				null, // String nombre, 
				0L, // Long tamanio, 
			  JsfBase.getIdUsuario(), // Long idUsuario, 
				8L, // Long idTipoArchivo, 
				0L, // Long tuplas, 
				"", // String observaciones, 
				null, // String alias, 
				JsfBase.getAutentifica().getEmpresa().getIdEmpresa(),
				1L
			);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
  
	public void doTabChange(TabChangeEvent event) {
		this.attrs.put("idTipoMasivo", this.masivo.getIdTipoMasivo());
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados("VistaCargasMasivasDto", "importados", this.attrs);
	}		

	public void doFileUpload(FileUploadEvent event) {
		try {
      this.doFileUploadMasivo(event, this.masivo.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("masivos"), this.masivo, this.categoria);
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
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
      Transaccion transaccion= new Transaccion(this.masivo, this.categoria);
      if(transaccion.ejecutar(EAccion.PROCESAR)) {
        RequestContext.getCurrentInstance().execute("janal.alert('Se proceso el catalogo de forma correcta');");
				this.setXls(null);
				this.attrs.put("xls", ""); 
				this.masivo = new TcManticMasivasArchivosDto(
					-1L, // Long idMasivaArchivo, 
					Configuracion.getInstance().getPropiedadSistemaServidor("masivos"), // String ruta, 
					categoria.getId(), // Long idTipoMasivo, 
					1L, // Long idMasivaEstatus, 
					null, // String nombre, 
					0L, // Long tamanio, 
					JsfBase.getIdUsuario(), // Long idUsuario, 
					8L, // Long idTipoArchivo, 
					0L, // Long tuplas, 
					"", // String observaciones, 
					null, // String alias, 
					JsfBase.getAutentifica().getEmpresa().getIdEmpresa(),
					1L
				);
      } //if
      else
    		JsfBase.addMessage("Error:", "Ocurrio un error en la cargar del catalogo !", ETipoMensaje.ERROR);		
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	}	
  
  public void doCompleto() {
		// JsfBase.addMessage("Detalle del mensaje", "Se proceso correctamente el catalogo !.", ETipoMensaje.INFORMACION);		
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