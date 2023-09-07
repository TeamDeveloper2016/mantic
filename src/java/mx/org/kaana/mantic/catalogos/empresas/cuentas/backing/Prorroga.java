package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.primefaces.event.TabChangeEvent;

@Named(value = "manticCatalogosEmpresasCuentasProrroga")
@ViewScoped
public class Prorroga extends IBaseImportar implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;	
  
  private Entity deuda;
	private Date prorroga;
	private Date fechaRecepcion;
	private UISelectEntity ikRecibio;
	private UISelectEntity ikProveedorPago;
  private TcManticNotasEntradasDto nota;

  public UISelectEntity getIkRecibio() {
    return ikRecibio;
  }

  public void setIkRecibio(UISelectEntity ikRecibio) {
    this.ikRecibio = ikRecibio;
  }

	public Date getProrroga() {
		return prorroga;
	}

	public void setProrroga(Date prorroga) {
		this.prorroga = prorroga;
	}		

  public Date getFechaRecepcion() {
    return fechaRecepcion;
  }

  public void setFechaRecepcion(Date fechaRecepcion) {
    this.fechaRecepcion = fechaRecepcion;
  }

  public UISelectEntity getIkProveedorPago() {
    return ikProveedorPago;
  }

  public void setIkProveedorPago(UISelectEntity ikProveedorPago) {
    this.ikProveedorPago = ikProveedorPago;
  }

  public TcManticNotasEntradasDto getNota() {
    return nota;
  }

  @PostConstruct
  @Override
  protected void init() {
    try {			
			if(JsfBase.getFlashAttribute("idEmpresaDeuda")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));     
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
      this.nota= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, (Long)JsfBase.getFlashAttribute("idNotaEntrada"));
//      this.attrs.put("idEmpresa", 1L);     
//      this.attrs.put("idEmpresaDeuda", 9045L);     
//      this.nota= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, 8932L);
			this.attrs.put("xml", ""); 
			this.attrs.put("pdf", ""); 
			this.doLoad();
      this.toLoadCondiciones();
      this.toLoadPersonas();
			this.toLoadTiposPagos();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init	
	
  @Override
  public void doLoad() {
		Map<String, Object>params= new HashMap<>();
		try {
			params.put("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));			
			params.put("sortOrder", "order by	tc_mantic_empresas_deudas.registro desc");
			this.deuda= (Entity) DaoFactory.getInstance().toEntity("VistaEmpresasDto", "cuentas", params);
			this.prorroga= deuda.toDate("limite");
      if(deuda.toDate("fechaRecepcion")== null)
        this.fechaRecepcion= new Date(Calendar.getInstance().getTimeInMillis());
      else
        this.fechaRecepcion= deuda.toDate("fechaRecepcion");
      this.attrs.put("idRevisado", deuda.toLong("idRevisado"));       
			this.attrs.put("deuda", deuda);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
      JsfBase.addMessageError(e);
		} // catch
		finally{
			Methods.clean(params);
		} // finally	
  }

	public String doRegresar() {	  
		JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
		return "saldos".concat(Constantes.REDIRECIONAR);
	} // doRegresar		

  private void toLoadPersonas() {
    List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> personas= UIEntity.seleccione("VistaAlmacenesTransferenciasDto", "solicito", params, columns, "nombres");
      this.attrs.put("personas", personas);
      if(this.deuda.toLong("idRecibio")== null)
        this.ikRecibio= UIBackingUtilities.toFirstKeySelectEntity(personas);
      else {
        int index= personas.indexOf(new UISelectEntity(this.deuda.toLong("idRecibio")));
        if(index>= 0)
          this.ikRecibio= personas.get(index);
        else
    	    this.ikRecibio= UIBackingUtilities.toFirstKeySelectEntity(personas);
      } // if  
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally 
  }
	  
	private void toLoadTiposPagos() {
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = new HashMap<>();
		try {
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposPagos", tiposPagos);
			this.attrs.put("tipoPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} 
	
	private void toLoadCondiciones() {
		List<UISelectEntity> condiciones= null;
		Map<String, Object>params       = new HashMap<>();
		try {
			params.put("idProveedor", this.deuda.toLong("idProveedor"));
			condiciones= UIEntity.build("VistaOrdenesComprasDto", "condiciones", params);
			this.attrs.put("condiciones", condiciones);
      if(this.deuda.toLong("idProveedorPago")== null) {
        if(this.nota!= null && this.nota.getIdProveedorPago()!= null) {
          int index= condiciones.indexOf(new UISelectEntity(this.nota.getIdProveedorPago()));
          if(index>= 0)
            this.ikProveedorPago= condiciones.get(index);
          else
    	      this.ikProveedorPago= UIBackingUtilities.toFirstKeySelectEntity(condiciones);
        } // if
        else
  	      this.ikProveedorPago= UIBackingUtilities.toFirstKeySelectEntity(condiciones);
      } // if  
      else {
        int index= condiciones.indexOf(new UISelectEntity(this.deuda.toLong("idProveedorPago")));
        if(index>= 0)
          this.ikProveedorPago= condiciones.get(index);
        else
    	    this.ikProveedorPago= UIBackingUtilities.toFirstKeySelectEntity(condiciones);
      } // if  
      this.doCalculateFechaPago();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} 
  
  public void doUpdatePlazo() {
    List<UISelectEntity> condiciones= (List<UISelectEntity>) this.attrs.get("condiciones");
    int index= condiciones.indexOf(this.ikProveedorPago);
    if(index>= 0) {
      this.ikProveedorPago= condiciones.get(index);
      this.doCalculateFechaPago();		
		} // if
		else 
      this.ikProveedorPago= new UISelectEntity(-1L);
	}	  
	
	public void doCalculateFechaPago() {
    Long diasPlazo= 1L;
  	Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(this.fechaRecepcion.getTime());
		if(this.ikProveedorPago!= null && this.ikProveedorPago.size()> 1)
 		  diasPlazo= this.ikProveedorPago.toLong("plazo");
		calendar.add(Calendar.DATE, diasPlazo.intValue()- 1);
		this.prorroga= new Date(calendar.getTimeInMillis());
	}
  
	public String doAceptar() {
		String regresar        = null;
		Transaccion transaccion= null;
		try {
			if(this.validaImporte()) {
				transaccion= new Transaccion(this.deuda, this.prorroga, this.fechaRecepcion, (Long)this.attrs.get("idRevisado"), this.ikRecibio.getKey(), this.ikProveedorPago.getKey());
				if(transaccion.ejecutar(EAccion.MODIFICAR)) {
					JsfBase.addMessage("Modificar cuenta por pagar", "Se realizó la modificación de forma correcta", ETipoMensaje.INFORMACION);
       		JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
					regresar= "saldos".concat(Constantes.REDIRECIONAR);
				} // if
				else
					JsfBase.addMessage("Modificar cuenta por pagar", "Ocurrió un error al realizar la modificación", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Modificar cuenta por pagar", "El importe tiene que ser mayor a cero", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	} // doAceptar
	
	private boolean validaImporte() {
		boolean regresar= false;
		Entity deuda    = null;
		Double importe  = null;
		try {
			deuda   = (Entity) this.attrs.get("deuda");
			importe = Numero.toRedondearSat(Double.valueOf(String.valueOf(deuda.get("importe"))));
			regresar= importe>= 1D;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		return regresar;
	} // validaImporte
	
	public void doTabChange(TabChangeEvent event) {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      if(event.getTab().getTitle().equals("Archivos")) {
        params.put("idNotaEntrada", ((Entity)this.attrs.get("deuda")).toLong("idNotaEntrada"));      
        params.put("idTipoDocumento", 13L);      
 			  this.doLoadImportados("VistaNotasEntradasDto", "importados", params);
      } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
	}

	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"));
	}

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas"));
	}
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idNotaEntrada", ((Entity)this.attrs.get("deuda")).toLong("idNotaEntrada"));
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
}