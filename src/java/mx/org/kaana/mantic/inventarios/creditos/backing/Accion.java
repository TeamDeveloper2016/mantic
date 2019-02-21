package mx.org.kaana.mantic.inventarios.creditos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.creditos.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import mx.org.kaana.mantic.inventarios.creditos.beans.NotaCredito;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosCreditosAccion")
@ViewScoped
public class Accion extends IBaseImportar implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;

	private TcManticProveedoresDto proveedor;
	private Long idTipoCreditoNota;
	private EAccion accion;
	private NotaCredito orden;

	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}

	public Boolean getModificar() {
		return this.accion.equals(EAccion.MODIFICAR);
	}

	public NotaCredito getOrden() {
		return orden;
	}

	public void setOrden(NotaCredito orden) {
		this.orden=orden;
	}

	public Long getIdTipoCreditoNota() {
		return idTipoCreditoNota;
	}

	public Boolean getDiferente() {
	  return this.getEmisor()!= null && this.proveedor!= null &&	!this.getEmisor().getRfc().equals(this.proveedor.getRfc());
	}

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}
	
  @Override
	@PostConstruct
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.idTipoCreditoNota= JsfBase.getFlashAttribute("idTipoCreditoNota")== null? -1L: (Long)JsfBase.getFlashAttribute("idTipoCreditoNota");
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idCreditoNota", JsfBase.getFlashAttribute("idCreditoNota")== null? -1L: JsfBase.getFlashAttribute("idCreditoNota"));
      this.attrs.put("idDevolucion", JsfBase.getFlashAttribute("idDevolucion")== null? -1L: JsfBase.getFlashAttribute("idDevolucion"));
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
      this.attrs.put("idProveedor", -1L);
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_FACTURA);
			this.attrs.put("carpeta", "TEMPORAL");
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:											
          this.orden= new NotaCredito(-1L, (Long)this.attrs.get("idDevolucion"));
					this.orden.setIdTipoCreditoNota(this.idTipoCreditoNota);
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.orden= (NotaCredito)DaoFactory.getInstance().toEntity(NotaCredito.class, "TcManticCreditosNotasDto", "detalle", this.attrs);
					this.orden.setIkDevolucion(new UISelectEntity(new Entity(this.orden.getIdDevolucion())));
					this.orden.setIkNotaEntrada(new UISelectEntity(new Entity(this.orden.getIdNotaEntrada())));
					this.orden.setIkProveedor(new UISelectEntity(new Entity(this.orden.getIdProveedor())));
					this.attrs.put("idTipoCreditoNota", this.orden.getIdTipoCreditoNota());
          break;
      } // switch
			this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			transaccion = new Transaccion(this.orden, (Double)this.attrs.get("importe"), this.getXml(), this.getPdf());
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
   			  UIBackingUtilities.execute("janal.back(' gener\\u00F3 la nota de crédito ', '"+ this.orden.getConsecutivo()+ "');");
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : this.accion.equals(EAccion.COMPLETO) ? "aplicó": "modificó").concat(" la nota de credito."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idCreditoNota", ((NotaCredito)this.orden).getIdCreditoNota());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la nota de crédito.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCreditoNota", this.attrs.get("idCreditoNota"));
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } 

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		Value importe             = null; 
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			switch(this.idTipoCreditoNota.intValue()) {
				case 1:
					importe= DaoFactory.getInstance().toField("VistaCreditosNotasDto", "parcial", this.attrs, "total");
					if(importe.getData()!= null)
						this.attrs.put("parcial", importe.toDouble());
					else
						this.attrs.put("parcial", 0D);
    			TcManticDevolucionesDto devolucion= (Long)this.attrs.get("idDevolucion")< 0L? new TcManticDevolucionesDto(): (TcManticDevolucionesDto)DaoFactory.getInstance().findById(TcManticDevolucionesDto.class, (Long)this.attrs.get("idDevolucion"));
					this.orden.setImporte(Numero.toRedondearSat(devolucion.getTotal()- (Double)this.attrs.get("parcial")));
					this.attrs.put("importe", this.orden.getImporte());
					this.attrs.put("total", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, devolucion.getTotal()));
					this.attrs.put("parcial", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, (Double)this.attrs.get("parcial")));
					params.put("idDevolucion", this.attrs.get("idDevolucion"));
					if((Long)this.attrs.get("idDevolucion")> 0L) {
						this.attrs.put("devoluciones", UIEntity.build("VistaCreditosNotasDto", "devoluciones", params, columns));
						List<UISelectEntity> devoluciones= (List<UISelectEntity>)this.attrs.get("devoluciones");
						if(devoluciones!= null && !devoluciones.isEmpty()) {
							if(this.accion.equals(EAccion.AGREGAR))
							  this.orden.setIkDevolucion(devoluciones.get(0));
						  else
                this.orden.setIkDevolucion(devoluciones.get(devoluciones.indexOf(this.orden.getIkDevolucion())));							
						  this.attrs.put("carpeta", this.orden.getIkDevolucion().toString("clave"));
      			  this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.orden.getIkDevolucion().toLong("idProveedor"));
						} // if	
					} // if	
					break;
				case 2:
					importe= DaoFactory.getInstance().toField("VistaCreditosNotasDto", "total", this.attrs, "total");
					if(importe.getData()!= null)
						this.attrs.put("parcial", importe.toDouble());
					else
						this.attrs.put("parcial", 0D);
    			TcManticNotasEntradasDto notaEntrada= (Long)this.attrs.get("idNotaEntrada")< 0L? new TcManticNotasEntradasDto(): (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, (Long)this.attrs.get("idNotaEntrada"));
					this.orden.setImporte(Numero.toRedondearSat(notaEntrada.getTotal()- (Double)this.attrs.get("parcial")));
					this.attrs.put("importe", this.orden.getImporte());
					this.attrs.put("total", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, notaEntrada.getTotal()));
					this.attrs.put("parcial", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, (Double)this.attrs.get("parcial")));
					params.put("idNotaEntrada", this.attrs.get("idNotaEntrada"));
					if((Long)this.attrs.get("idNotaEntrada")> 0L) {
						this.attrs.put("notas", UIEntity.build("VistaCreditosNotasDto", "notas", params, columns));
						List<UISelectEntity> notas= (List<UISelectEntity>)this.attrs.get("notas");
						if(notas!= null && !notas.isEmpty()) {
 							if(this.accion.equals(EAccion.AGREGAR))
  							this.orden.setIkNotaEntrada(notas.get(0));
						  else
                this.orden.setIkNotaEntrada(notas.get(notas.indexOf(this.orden.getIkNotaEntrada())));							
   						this.attrs.put("carpeta", this.orden.getIkNotaEntrada().toString("clave"));
    			  this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.orden.getIkNotaEntrada().toLong("idProveedor"));
						} // if	
					} // if	
					break;
				case 3:
					params.put("idProveedor", this.attrs.get("idProveedor"));
					this.orden.setImporte(0D);
					columns.remove(columns.size()- 1);
					this.attrs.put("proveedores", UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
					List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
					if(proveedores!= null && !proveedores.isEmpty()) {
						if(this.accion.equals(EAccion.AGREGAR))
							this.orden.setIkProveedor(proveedores.get(0));
						else
							this.orden.setIkProveedor(proveedores.get(proveedores.indexOf(this.orden.getIkProveedor())));							
 						this.attrs.put("carpeta", this.orden.getIkProveedor().toString("clave"));
    			  this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.orden.getIkProveedor().toLong("idProveedor"));
					} // if	
					break;
			} // switch
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

	public void doFileUpload(FileUploadEvent event) {
		this.doFileUpload(event, this.orden.getRegistro().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos"), (String)this.attrs.get("carpeta"));
		if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.XML.name())) {
			this.orden.setFolio(this.getFactura().getFolio());
			this.doCheckFolio();
		} // if
	} // doFileUpload	
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados("VistaCreditosNotasDto", "importados", this.orden.toMap());
		else
		  if(event.getTab().getTitle().equals("Importar"))
			  this.doLoadFiles("TcManticCreditosArchivosDto", this.orden.getIdCreditoNota(), "idCreditoNota", true, 1D);
	}
	
	public StreamedContent doFileDownload() {
		return this.doPdfFileDownload(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos"));
	}
	
	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos"));
	}
	
	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos"));
	}
	
	public void doUpdateRfc() {
		this.doUpdateRfc(this.proveedor);
	}

	public void doCheckFolio() {
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("folio", this.orden.getFolio());
			params.put("idProveedor", this.orden.getIdProveedor());
			params.put("idCreditoNota", this.orden.getIdCreditoNota());
			int month= Calendar.getInstance().get(Calendar.MONTH);
			if(month<= 5) {
				params.put("inicio", Calendar.getInstance().get(Calendar.YEAR)+ "0101");
				params.put("termino", Calendar.getInstance().get(Calendar.YEAR)+ "0630");
			} // if
			else {
				params.put("inicio", Calendar.getInstance().get(Calendar.YEAR)+ "0701");
				params.put("termino", Calendar.getInstance().get(Calendar.YEAR)+ "1231");
			} // else
			Entity entity= (Entity)DaoFactory.getInstance().toEntity("TcManticCreditosNotasDto", "folio", params);
			if(entity!= null && entity.size()> 0) 
				UIBackingUtilities.execute("$('#contenedorGrupos\\\\:folio').val('');janal.show([{summary: 'Error:', detail: 'El folio ["+ this.orden.getFolio()+ "] se registró en la nota de credito "+ entity.toString("consecutivo")+ ", el dia "+ Global.format(EFormatoDinamicos.FECHA_HORA, entity.toTimestamp("registro"))+ " hrs.'}]);");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
	
}