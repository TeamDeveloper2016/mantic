package mx.org.kaana.mantic.catalogos.masivos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
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
	protected FormatLazyModel lazyModel;
	private ECargaMasiva categoria;

	public TcManticMasivasArchivosDto getMasivo() {
		return masivo;
	}

	public FormatLazyModel getLazyModel() {
		return lazyModel;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_MASIVO);
			this.attrs.put("procesados", 0L);
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
					case 4:
						this.categoria= ECargaMasiva.REFACCIONES;
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
			this.doLoadArhivos("VistaCargasMasivasDto", "importados", this.attrs);
	}		
	

	protected void doLoadArhivos(String proceso, String idXml, Map<String, Object> params) {
		List<Columna> columns= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
		  this.lazyModel= new FormatCustomLazy(proceso, idXml, params, columns);
			UIBackingUtilities.resetDataTable();
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    } // finally
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
		String regresar        = null;
		Transaccion transaccion= null;
		try {
		  this.masivo.setObservaciones(this.attrs.get("observaciones")!= null? (String)this.attrs.get("observaciones"): null);
      transaccion= new Transaccion(this.masivo, this.categoria);
      if(transaccion.ejecutar(EAccion.PROCESAR)) {
        RequestContext.getCurrentInstance().execute("janal.alert('Cátalogo procesado de forma correcta ["+ this.masivo.getTuplas()+ "], registros erroneos ["+ transaccion.getErrores()+ "]';");
				this.setXls(null);
				this.attrs.put("xls", ""); 
				this.masivo = new TcManticMasivasArchivosDto(
					-1L, // Long idMasivaArchivo, 
					null, // String ruta, 
					categoria.getId(), // Long idTipoMasivo, 
					1L, // Long idMasivaEstatus, 
					null, // String nombre, 
					0L, // Long tamanio, 
					JsfBase.getIdUsuario(), // Long idUsuario, 
					8L, // Long idTipoArchivo, 
					0L, // Long tuplas, 
					null, // String observaciones, 
					null, // String alias, 
					JsfBase.getAutentifica().getEmpresa().getIdEmpresa(),
					1L
				);
      } //if
      else
    		JsfBase.addMessage("Error:", "Ocurrio un error en la cargar masiva del cátalogo !", ETipoMensaje.ERROR);		
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
		if(transaccion!= null)
			this.attrs.put("procesados", transaccion.getProcesados());
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
			case 4: this.categoria= ECargaMasiva.REFACCIONES;
				break;
		} // switch
	}

	public void doImageUpload(FileUploadEvent event) {
		
	} 

	public String doMovimientos() {
		JsfBase.setFlashAttribute("idMasivaArchivo", ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "filtro");
		return "movimientos".concat(Constantes.REDIRECIONAR);
	}
	
	public String doDetalles() {
		JsfBase.setFlashAttribute("idMasivaArchivo", ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "filtro");
		return "detalles".concat(Constantes.REDIRECIONAR);
	}
	
}