package mx.org.kaana.mantic.inventarios.entradas.backing;

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
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import mx.org.kaana.mantic.inventarios.entradas.reglas.Importados;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosEntradasImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353488565639367L;
	
	protected TcManticNotasEntradasDto orden;
	private TcManticProveedoresDto proveedor;
	private Long idNotaEntrada;

	public TcManticNotasEntradasDto getOrden() {
		return orden;
	}

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}
	
	public Boolean getDiferente() {
	  return this.getEmisor()!= null && this.proveedor!= null &&	!this.getEmisor().getRfc().equals(this.proveedor.getRfc());
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("idNotaEntrada")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      // 3969L
      this.idNotaEntrada= JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: (Long)JsfBase.getFlashAttribute("idNotaEntrada");
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda")== null? -1L: (Long)JsfBase.getFlashAttribute("idEmpresaDeuda"));
			this.orden= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, idNotaEntrada);
			if(this.orden!= null) {
			  this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.orden.getIdProveedor());
				this.doLoad();
			} // if
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

	@Override
	public void doLoad() {	
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idOrdenCompra", this.orden.getIdOrdenCompra());
      if(!Cadena.isVacio(this.orden.getIdOrdenCompra()))
			  this.attrs.put("ordenes", UIEntity.build("VistaNotasEntradasDto", "ordenes", params, columns));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // doLoad

	public void doTabChange(TabChangeEvent event) {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      if(event.getTab().getTitle().equals("Archivos")) {
        params.put("idNotaEntrada", this.orden.getIdNotaEntrada());      
        params.put("idTipoDocumento", 13L);      
        this.doLoadImportados("VistaNotasEntradasDto", "importados", params);
      } // if  
      //else
      //	if(event.getTab().getTitle().equals("Importar")) 
      //		this.doLoadFiles("TcManticNotasArchivosDto", this.orden.getIdNotaEntrada(), "idNotaEntrada");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
	} // doTabChange		

	public void doFileUpload(FileUploadEvent event) {
		this.doFileUpload(event, this.orden.getFechaFactura().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"), this.proveedor.getClave());
	} // doFileUpload	
	
	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"));
	} // doViewDocument

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"));
	} // doViewFile
	
	public void doUpdateRfc() {
		this.doUpdateRfc(this.proveedor);
	} // doUpdateRfc
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idNotaEntrada", this.idNotaEntrada);
  	JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public String doAceptar() {
		String regresar      = null;
		Importados importados= null;
		try {
			if(this.getXml()!= null && Cadena.isVacio(this.getXml().getObservaciones()))
			  this.getXml().setObservaciones(this.attrs.get("observaciones")!= null? (String)this.attrs.get("observaciones"): null);
			if(this.getPdf()!= null && Cadena.isVacio(this.getPdf().getObservaciones()))
			  this.getPdf().setObservaciones(this.attrs.get("observaciones")!= null? (String)this.attrs.get("observaciones"): null);
			importados= new Importados(this.orden, this.getXml(), this.getPdf());
      if(importados.ejecutar(EAccion.AGREGAR)) {
      	UIBackingUtilities.execute("janal.alert('Se actualizo y se importaron los catalogos de forma correcta !');");
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