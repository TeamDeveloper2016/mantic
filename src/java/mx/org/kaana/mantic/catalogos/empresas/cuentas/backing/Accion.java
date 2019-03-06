package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.Serializable;
import java.sql.Date;
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
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.entradas.beans.NotaEntrada;
import mx.org.kaana.mantic.inventarios.entradas.reglas.AdminNotas;
import mx.org.kaana.mantic.inventarios.entradas.reglas.Transaccion;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
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

@Named(value= "manticCatalogosEmpresasCuentasAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Accion.class);
  private static final long serialVersionUID= 327393488565639364L;
	
	private EAccion accion;	
	private boolean aplicar;
	private TcManticProveedoresDto proveedor;

	public String getAgregar() {
		return this.accion.equals(EAccion.COMPLETO)? "none": "";
	}

	public String getConsultar() {
		return this.accion.equals(EAccion.CONSULTAR)? "none": "";
	}

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}

	public Boolean getIsAplicar() {
		Boolean regresar= true;
		try {
			regresar= JsfBase.isAdminEncuestaOrAdmin();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	}
	
	public Boolean getDiferente() {
	  return this.getEmisor()!= null && this.proveedor!= null &&	!this.getEmisor().getRfc().equals(this.proveedor.getRfc());
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			this.aplicar  =  false;
			if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.accion   = JsfBase.getFlashAttribute("accion")== null? EAccion.COMPLETO: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda")== null? -1L: JsfBase.getFlashAttribute("idEmpresaDeuda"));     
			this.attrs.put("idOrdenCompra", -1L);
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "saldos": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_FACTURA);
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
		Double deuda= 0.0D;
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.equals(EAccion.COMPLETO)? EAccion.AGREGAR.name(): this.accion.equals(EAccion.COMPLEMENTAR)? EAccion.MODIFICAR.name(): this.accion.name()));
      switch (this.accion) {
        case COMPLETO:											
          this.setAdminOrden(new AdminNotas(new NotaEntrada()));
					((NotaEntrada)this.getAdminOrden().getOrden()).setIdNotaTipo(3L);
					((NotaEntrada)this.getAdminOrden().getOrden()).setIkAlmacen(new UISelectEntity(new Entity(-1L)));
					((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(new UISelectEntity(new Entity(-1L)));
					this.doCalculateFechaPagoInit();
          break;
        case COMPLEMENTAR:					
        case CONSULTAR:					
					NotaEntrada notaEntrada= (NotaEntrada)DaoFactory.getInstance().toEntity(NotaEntrada.class, "TcManticNotasEntradasDto", "detalle", this.attrs);
					notaEntrada.setIdOrdenCompra(null);
          this.setAdminOrden(new AdminNotas(notaEntrada));
					((NotaEntrada)this.getAdminOrden().getOrden()).setIkAlmacen(new UISelectEntity(new Entity(notaEntrada.getIdAlmacen())));
					((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(new UISelectEntity(new Entity(notaEntrada.getIdProveedor())));          
					deuda= ((NotaEntrada)this.getAdminOrden().getOrden()).getDeuda();
					this.setAdminOrden(new AdminNotas(notaEntrada));
          break;
      } // switch
			this.toLoadCatalog();
			if(this.accion.equals(EAccion.COMPLEMENTAR) ||	this.accion.equals(EAccion.CONSULTAR))
				((NotaEntrada)this.getAdminOrden().getOrden()).setDeuda(deuda);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAplicar() {  
		this.aplicar= true;
		return this.doAceptar();
	}
	
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			((NotaEntrada)this.getAdminOrden().getOrden()).setDescuentos(0D);
			((NotaEntrada)this.getAdminOrden().getOrden()).setExcedentes(0D);
			((NotaEntrada)this.getAdminOrden().getOrden()).setImpuestos(0D);
			((NotaEntrada)this.getAdminOrden().getOrden()).setSubTotal(0D);
			((NotaEntrada)this.getAdminOrden().getOrden()).setTotal(((NotaEntrada)this.getAdminOrden().getOrden()).getDeuda());
			transaccion = new Transaccion(((NotaEntrada)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos(), this.aplicar, this.getXml(), this.getPdf());
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.COMPLETO) || this.aplicar) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
					if(this.accion.equals(EAccion.COMPLETO))
    			  UIBackingUtilities.execute("jsArticulos.back('gener\\u00F3 la nota de entrada manual', '"+ ((NotaEntrada)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					else
   			    UIBackingUtilities.execute("jsArticulos.back('aplic\\u00F3 la nota de entrada manual', '"+ ((NotaEntrada)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.COMPLETO) ? "agregó" : "modificó").concat(" la nota de entrada manual."), ETipoMensaje.INFORMACION);
    		JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
  			JsfBase.setFlashAttribute("idNotaEntrada", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la nota de entrada manual.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idNotaEntrada", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada());
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idOrdenCompra", this.attrs.get("idOrdenCompra"));
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty() && this.accion.equals(EAccion.COMPLETO)) 
				((NotaEntrada)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("proveedores", UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			int index= 0;
			if(!proveedores.isEmpty()) {
				if(this.accion.equals(EAccion.COMPLETO))
			   ((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(proveedores.get(0));
				else {
				  index= proveedores.indexOf(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedor());
				  ((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(proveedores.get(index));
				} // else
		    this.attrs.put("proveedor", proveedores.get(index));
			  this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, ((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedor().getKey());
				this.toLoadCondiciones(new UISelectEntity(new Entity(this.proveedor.getIdProveedor())));
			} // if	
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
	
	public void doUpdateProveedor() {
		try {			
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			UISelectEntity temporal= ((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedor();
			temporal= proveedores.get(proveedores.indexOf(temporal));			
			this.attrs.put("proveedor", temporal);
			this.toLoadCondiciones(proveedores.get(proveedores.indexOf((UISelectEntity)((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedor())));			
		}	
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Importar"))
			this.doLoadFiles("TcManticNotasArchivosDto", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada(), "idNotaEntrada", false, this.getAdminOrden().getTipoDeCambio());
	}
	
	public void doFileUpload(FileUploadEvent event) {
		this.doFileUpload(event, ((NotaEntrada)this.getAdminOrden().getOrden()).getFechaFactura().getTime(), Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"), this.proveedor.getClave(), true, this.getAdminOrden().getTipoDeCambio());
		if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.XML.name())) {
		  ((NotaEntrada)this.getAdminOrden().getOrden()).setFactura(this.getFactura().getFolio());
		  ((NotaEntrada)this.getAdminOrden().getOrden()).setFechaFactura(Fecha.toDateDefault(this.getFactura().getFecha()));
	  	this.doCheckFolio();
			this.doCalculatePagoFecha();
		} // if
	} // doFileUpload	
	
	public void doCheckFolio() {
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("factura", ((NotaEntrada)this.getAdminOrden().getOrden()).getFactura());
			params.put("idProveedor", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdProveedor());
			params.put("idNotaEntrada", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada());
			int month= Calendar.getInstance().get(Calendar.MONTH);
			if(month<= 5) {
				params.put("inicio", Calendar.getInstance().get(Calendar.YEAR)+ "0101");
				params.put("termino", Calendar.getInstance().get(Calendar.YEAR)+ "0630");
			} // if
			else {
				params.put("inicio", Calendar.getInstance().get(Calendar.YEAR)+ "0701");
				params.put("termino", Calendar.getInstance().get(Calendar.YEAR)+ "1231");
			} // else
			Entity entity= (Entity)DaoFactory.getInstance().toEntity("TcManticNotasEntradasDto", "folio", params);
			if(entity!= null && entity.size()> 0) 
				UIBackingUtilities.execute("$('#contenedorGrupos\\\\:factura').val('');janal.show([{summary: 'Error:', detail: 'El folio ["+ ((NotaEntrada)this.getAdminOrden().getOrden()).getFactura()+ "] se registró en la nota de entrada "+ entity.toString("consecutivo")+ ", el dia "+ Global.format(EFormatoDinamicos.FECHA_HORA, entity.toTimestamp("registro"))+ " hrs.'}]);");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doCheckFolio
	
	public void doCalculateFechaPago() {
		this.doCalculateFechaPagoInit();
	} // doCalculateFechaPago
	
	public void doCalculateFechaPagoInit() {		
		Date fechaFactura= ((NotaEntrada)this.getAdminOrden().getOrden()).getFechaFactura();
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(fechaFactura.getTime());
		if(((NotaEntrada)this.getAdminOrden().getOrden()).getDiasPlazo()== null)
			((NotaEntrada)this.getAdminOrden().getOrden()).setDiasPlazo(1L);
		calendar.add(Calendar.DATE, ((NotaEntrada)this.getAdminOrden().getOrden()).getDiasPlazo().intValue()- 1);
		((NotaEntrada)this.getAdminOrden().getOrden()).setFechaPago(new Date(calendar.getTimeInMillis()));
	}

	public void doCalculatePagoFecha() {
		Date fechaFactura= ((NotaEntrada)this.getAdminOrden().getOrden()).getFechaFactura();
		Date fechaPago   = ((NotaEntrada)this.getAdminOrden().getOrden()).getFechaPago();
		((NotaEntrada)this.getAdminOrden().getOrden()).setDiasPlazo(new Long(Fecha.getBetweenDays(fechaFactura, fechaPago)));
	}

	public StreamedContent doFileDownload() {
		return this.doPdfFileDownload(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"));
	}	
	
	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"));
	}

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"));
	}

	private void toLoadCondiciones(UISelectEntity proveedor) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idProveedor", proveedor.getKey());
      this.attrs.put("condiciones", UIEntity.build("VistaOrdenesComprasDto", "condiciones", params, columns));
			List<UISelectEntity> condiciones= (List<UISelectEntity>) this.attrs.get("condiciones");			
			if(!condiciones.isEmpty()) {				
				if(this.accion.equals(EAccion.COMPLETO))
				  ((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedorPago(condiciones.get(0));
				else {
					Entity entity= new UISelectEntity(new Entity(((NotaEntrada)this.getAdminOrden().getOrden()).getIdProveedorPago()));
				  ((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedorPago(condiciones.get(condiciones.indexOf(entity)));
				} // if					
				((NotaEntrada)this.getAdminOrden().getOrden()).setDiasPlazo(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedorPago().toLong("plazo")+ 1);
        ((NotaEntrada)this.getAdminOrden().getOrden()).setDescuento(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedorPago().toString("descuento"));
        this.doUpdatePorcentaje();
			} // if
			this.doCalculateFechaPagoInit();
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	} // toLoadCondiciones
	
	@Override
	public void doUpdatePorcentaje() {
		if(!getAdminOrden().getArticulos().isEmpty())
			super.doUpdatePorcentaje();
	} // doUpdatePorcentaje
	
	public void doUpdatePlazo() {
		if(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedorPago()!= null) {
			List<UISelectEntity> condiciones= (List<UISelectEntity>) this.attrs.get("condiciones");
      ((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedorPago(condiciones.get(condiciones.indexOf(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedorPago())));
			((NotaEntrada)this.getAdminOrden().getOrden()).setDiasPlazo(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedorPago().toLong("plazo")+ 1);
      this.doCalculateFechaPagoInit();
      ((NotaEntrada)this.getAdminOrden().getOrden()).setDescuento(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedorPago().toString("descuento"));
			this.doUpdatePorcentaje();
		} // if
	}	// doUpdatePlazo
	
	@Override
	public void doUpdateArticulo(String codigo, Integer index) {
	}
	
	@Override
	public void doDeleteArticulo(Integer index) {
	}

	@Override
  public void doFindArticulo(Integer index) {
	}

	public void doUpdateRfc() {
		this.doUpdateRfc(this.proveedor);
	}
}