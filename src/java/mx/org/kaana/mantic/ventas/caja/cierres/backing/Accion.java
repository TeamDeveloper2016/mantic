package mx.org.kaana.mantic.ventas.caja.cierres.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.ventas.caja.cierres.beans.Denominacion;
import mx.org.kaana.mantic.ventas.caja.cierres.beans.Importe;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticVentasCajaCierresAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 323323488565639361L;
  
	private List<Importe> importes;
	private List<Denominacion> denominaciones;
	private List<Denominacion> fondos;
	private FormatCustomLazy lazyModel;
	private EAccion accion;

	public List<Importe> getImportes() {
		return importes;
	}

	public List<Denominacion> getDenominaciones() {
		return denominaciones;
	}

	public List<Denominacion> getFondos() {
		return fondos;
	}

	public FormatCustomLazy getLazyModel() {
		return lazyModel;
	}

	public String getCalculate() {
		this.doLoadAmbos();
		return "";
	}
		
  @Override
	@PostConstruct
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idCierre", JsfBase.getFlashAttribute("idCierre")== null? -1L: JsfBase.getFlashAttribute("idCierre"));
      this.attrs.put("idCaja", JsfBase.getFlashAttribute("idCaja")== null? -1L: JsfBase.getFlashAttribute("idCaja"));
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa")== null? -1L: JsfBase.getFlashAttribute("idEmpresa"));
      this.attrs.put("sucursales", this.attrs.get("idEmpresa"));
  		this.attrs.put("totalCreditos", 0L);
			this.attrs.put("efectivo", 0D);
			this.attrs.put("total", 0D);
			this.attrs.put("disponible", 0D);
			this.attrs.put("continuar", this.accion.equals(EAccion.CONSULTAR)? "": "none");
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
  		this.attrs.put("limite", 3000D);
  		this.attrs.put("idEfectivo", 1);
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
			this.importes= (List<Importe>)DaoFactory.getInstance().toEntitySet(Importe.class, "VistaCierresCajasDto", "importes", this.attrs);
			switch(this.accion) {
				case AGREGAR:
					this.denominaciones= (List<Denominacion>)DaoFactory.getInstance().toEntitySet(Denominacion.class, "TcManticMonedasDto", "denominacion", this.attrs);
					break;	
				case CONSULTAR:
					this.denominaciones= (List<Denominacion>)DaoFactory.getInstance().toEntitySet(Denominacion.class, "VistaCierresCajasDto", "denominacion", this.attrs);
  		    this.attrs.put("idEfectivo", 2);
					this.fondos        = (List<Denominacion>)DaoFactory.getInstance().toEntitySet(Denominacion.class, "VistaCierresCajasDto", "denominacion", this.attrs);
					break;	
			} // switch
			this.toLoadEmpresas();
			this.toLoadCuentas();
			this.toLoadCreditos();
			this.doCalculate();
  		for (Importe importe: this.importes) {
	   		if(importe.getIdTipoMedioPago().equals(1L)) {
					this.attrs.put("disponible", importe.getDisponible()> importe.getSaldo()? importe.getSaldo(): importe.getDisponible());
				} // if
			} // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Cierre transaccion= null;
    String regresar   = null;
    try {			
			TcManticCierresDto cierre= (TcManticCierresDto)DaoFactory.getInstance().findById(TcManticCierresDto.class, (Long)this.attrs.get("idCierre"));
			cierre.setObservaciones((String)this.attrs.get("observaciones"));
			transaccion = new Cierre((Long)this.attrs.get("idCaja"), (Double)this.attrs.get("efectivo"), cierre, this.importes, this.denominaciones);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
 				  regresar = "fondo".concat(Constantes.REDIRECIONAR);
					JsfBase.setFlashAttribute("accion", EAccion.PROCESAR);
					JsfBase.setFlashAttribute("idCaja", this.attrs.get("idCaja"));
					JsfBase.setFlashAttribute("idCierre", transaccion.getIdApertura());
					JsfBase.setFlashAttribute("idEmpresa", this.attrs.get("idEmpresa"));
    			RequestContext.getCurrentInstance().execute("janal.alert('Se gener\\u00F3 el cierre de caja, con consecutivo: "+ cierre.getConsecutivo()+ "');");
				} // if	
				else
    			JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR)? "agreg�": this.accion.equals(EAccion.COMPLETO) ? "aplic�": "modific�").concat(" el cierre de caja."), ETipoMensaje.INFORMACION);
			} // if
			else 
				JsfBase.addMessage("Ocurri� un error al realizar el cierre de caja.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
    return "filtro".concat(Constantes.REDIRECIONAR);
  } 
	
	private void toLoadEmpresas() {
		List<Columna> columns     = null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			List<UISelectEntity> sucursales= (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", this.attrs, columns);
      this.attrs.put("sucursales", sucursales);
			this.doLoadCajas();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
    }// finally
	}
	
	public void doLoadCajas() {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			List<UISelectEntity> cajas= (List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "unica", this.attrs, columns);
      this.attrs.put("cajas", cajas);
 			this.attrs.put("temporal", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("cajas")));
			if(this.attrs.get("temporal")!= null)
				this.attrs.put("limite", ((UISelectEntity)this.attrs.get("temporal")).toDouble("limite"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
    }// finally
	}

	private void toLoadCuentas() {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("dia", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      this.attrs.put("cuentas", UIEntity.build("VistaCierresCajasDto", "abiertas", this.attrs, columns));
 			this.attrs.put("idCuenta", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("cuentas")));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
    }// finally
	}

	private void toLoadCreditos() {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      this.attrs.put("creditos", UIEntity.build("VistaCierresCajasDto", "creditos", this.attrs, columns));
 			this.attrs.put("idCredito", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("creditos")));
  		this.attrs.put("totalCreditos", ((List<UISelectEntity>)this.attrs.get("creditos")).size());
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
    }// finally
	}

	public void doCalculate() {
		Double sum  = 0D;
		for (Denominacion denominacion: this.denominaciones) {
			denominacion.setImporte(Numero.toRedondearSat(denominacion.getDenominacion()* denominacion.getCantidad()));
			sum+= denominacion.getImporte();
		} // for
    this.attrs.put("efectivo", Numero.toRedondearSat(sum));		
		for (Importe importe: this.importes) {
			importe.toCalculate();
			if(importe.getIdTipoMedioPago().equals(1L)) 
				importe.setImporte(Numero.toRedondearSat(sum));
		} // for
		this.doTotales();
	}
	
	public void doTotales() {
		Double total= 0D;
		for (Importe importe: this.importes) {
			importe.toCalculate();
			total+= importe.getImporte();
		} // for
    this.attrs.put("total", Numero.toRedondearSat(total));		
	}

  public void doContinuar() {
		this.attrs.put("continuar", "");
	}	
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Retiros/Abonos"))
			this.doLoadRetirosAbonos();
	}

	private void doLoadRetirosAbonos() {
    List<Columna> columns     = null;
		Map<String, Object> params= new HashMap<>();
    try {
      params.put("idCierre", this.attrs.get("idCierre"));
      params.put("sortOrder", "order by tc_mantic_cierres_retiros.id_abono, tc_mantic_cierres_retiros.consecutivo ");
      columns = new ArrayList<>();
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("caja", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      this.lazyModel = new FormatCustomLazy("VistaCierresCajasDto", "retiros", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally		
  } // doLoad

  private void doLoadAmbos() {
		Double abonos = 0D;
		Double retiros= 0D;
		if(this.lazyModel!= null)
			for (IBaseDto item: (List<IBaseDto>)this.lazyModel.getWrappedData()) {
				Entity row= (Entity)item;
				if(row.toLong("idAbono").equals(1L))
					abonos+= new Double(row.toString("importe"));
				else
					retiros+= new Double(row.toString("importe"));
			} // for
	  this.attrs.put("abonos", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, abonos));
	  this.attrs.put("retiros", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, retiros));
	}	

}