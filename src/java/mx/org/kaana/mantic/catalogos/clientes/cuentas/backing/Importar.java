package mx.org.kaana.mantic.catalogos.clientes.cuentas.backing;

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
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import mx.org.kaana.mantic.catalogos.clientes.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosArchivosDto;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticCatalogosClientesCuentasImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353488565639367L;
	private TcManticClientesDeudasDto clienteDeuda;
	private TcManticClientesDto cliente;
	private Long idClienteDeuda;	

	public TcManticClientesDeudasDto getClienteDeuda() {
		return clienteDeuda;
	}

	public TcManticClientesDto getCliente() {
		return cliente;
	}	
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("idClienteDeuda")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.idClienteDeuda= JsfBase.getFlashAttribute("idClienteDeuda")== null? -1L: (Long)JsfBase.getFlashAttribute("idClienteDeuda");
			this.clienteDeuda= (TcManticClientesDeudasDto)DaoFactory.getInstance().findById(TcManticClientesDeudasDto.class, this.idClienteDeuda);
			if(this.clienteDeuda!= null) {			  
				this.cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(TcManticClientesDto.class, this.clienteDeuda.getIdCliente());
				this.doLoad();
			} // if
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_IDENTIFICACION);
			setFile(new Importado());
			this.attrs.put("xml", ""); 
			this.attrs.put("file", ""); 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
	
	@Override
	public void doLoad() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();      
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
			params.put("idClienteDeuda", this.idClienteDeuda);
			this.attrs.put("pagos", UIEntity.build("VistaClientesDto", "pagosDeuda", params, columns));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // toLoadCatalog

  private void doLoadImportados() {
		List<Columna> columns= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
		  this.attrs.put("importados", UIEntity.build("VistaClientesDto", "importados", this.clienteDeuda.toMap(), columns));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    }// finally
  } // doLoadImportados	
	
	public void doFileUpload(FileUploadEvent event) {				
		try {			
			this.doFileUpload(event, this.cliente.getClave(), "cobros");      
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);			
		} // catch
	} // doFileUpload	
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados();
		else
		  if(event.getTab().getTitle().equals("Importar")) 
				this.doLoadFiles();
	}	// doTabChange	

	private void doLoadFiles() {
		TcManticClientesPagosArchivosDto tmp= null;
		if(this.clienteDeuda.getIdClienteDeuda()> 0) {
			Map<String, Object> params=null;
			try {
				params=new HashMap<>();
				params.put("idClienteDeuda", this.idClienteDeuda);				
				params.put("idTipoArchivo", 2L);
				tmp= (TcManticClientesPagosArchivosDto) DaoFactory.getInstance().toEntity(TcManticClientesPagosArchivosDto.class, "VistaClientesDto", "existsPagos", params); 
				if(tmp!= null) {
					setFile(new Importado(tmp.getNombre(), "PDF", EFormatos.PDF, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones()));
  				this.attrs.put("file", getFile().getName()); 
				} // if	
			} // try
			catch (Exception e) {
				Error.mensaje(e);
				JsfBase.addMessageError(e);
			} // catch
			finally {
				Methods.clean(params);
			} // finally
		} // if
	} // doLoadFiles	

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idClienteDeuda", this.idClienteDeuda);
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public void doViewDocument() {
		doViewDocumentFile(Configuracion.getInstance().getPropiedadSistemaServidor("cobros"));
	} // doViewDocument
	
	@Override
	public void doViewFile(String nameXml) {
		toViewFile(nameXml);
	} // doViewFile
	
	public String doAceptar() {
		String regresar        = null;
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(getFile(), this.clienteDeuda, Long.valueOf(this.attrs.get("pago").toString()));
      if(transaccion.ejecutar(EAccion.SUBIR)) {
      	UIBackingUtilities.execute("janal.alert('Se importaron los archivos de forma correcta !');");
				regresar= this.doCancelar();
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	} // doAceptar
}