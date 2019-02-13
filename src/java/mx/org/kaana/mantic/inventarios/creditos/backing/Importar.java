package mx.org.kaana.mantic.inventarios.creditos.backing;

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
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import mx.org.kaana.mantic.inventarios.creditos.reglas.Importados;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosCreditosImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG=LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353488565639367L;
	
	private TcManticCreditosNotasDto orden;
	private TcManticProveedoresDto proveedor;
	private Long idCreditoNota;

	public TcManticCreditosNotasDto getOrden() {
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
		TcManticDevolucionesDto devolucion  = null;
		TcManticNotasEntradasDto notaEntrada= null;
    try {
			if(JsfBase.getFlashAttribute("idCreditoNota")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      idCreditoNota= JsfBase.getFlashAttribute("idCreditoNota")== null? -1L: (Long)JsfBase.getFlashAttribute("idCreditoNota");
			this.orden= (TcManticCreditosNotasDto)DaoFactory.getInstance().findById(TcManticCreditosNotasDto.class, idCreditoNota);
			if(this.orden!= null) {
  			switch(this.orden.getIdTipoCreditoNota().intValue()) {
	  			case 1:
  			    devolucion = (TcManticDevolucionesDto)DaoFactory.getInstance().findById(TcManticDevolucionesDto.class, this.orden.getIdDevolucion());
  			    notaEntrada= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, devolucion.getIdNotaEntrada());
  			    this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, notaEntrada.getIdProveedor());
					  break;
	  			case 2:
  			    notaEntrada= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, this.orden.getIdNotaEntrada());
  			    this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, notaEntrada.getIdProveedor());
					  break;
	  			case 3:
  			    this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.orden.getIdProveedor());
					  break;
				} // switch
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
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idProveedor", this.proveedor.getIdProveedor());
			this.attrs.put("proveedores", UIEntity.build("VistaCreditosNotasDto", "proveedores", params, columns));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados("VistaCreditosNotasDto", "importados", this.orden.toMap());
		else
		  if(event.getTab().getTitle().equals("Importar")) 
				this.doLoadFiles("TcManticCreditosArchivosDto", this.orden.getIdCreditoNota(), "idCreditoNota");
	}		

  public void doFileUpload(FileUploadEvent event) {
		this.doFileUpload(event, this.orden.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos"), this.proveedor.getClave());
	} // doFileUpload	
	
	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos"));
	}

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos"));
	}
	
	public void doUpdateRfc() {
		this.doUpdateRfc(this.proveedor);
	}
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCreditoNota", this.idCreditoNota);
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

	public String doAceptar() {
		String regresar= null;
		try {
			if(this.getXml()!= null && Cadena.isVacio(this.getXml().getObservaciones()))
			  this.getXml().setObservaciones(this.attrs.get("observaciones")!= null? (String)this.attrs.get("observaciones"): null);
			if(this.getPdf()!= null && Cadena.isVacio(this.getPdf().getObservaciones()))
			  this.getPdf().setObservaciones(this.attrs.get("observaciones")!= null? (String)this.attrs.get("observaciones"): null);
			Importados importados= new Importados(this.orden, this.getXml(), this.getPdf());
      if(importados.ejecutar(EAccion.AGREGAR)) {
      	UIBackingUtilities.execute("janal.alert('Se importaron los archivos de forma correcta !');");
				regresar= this.doCancelar();
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	}
}