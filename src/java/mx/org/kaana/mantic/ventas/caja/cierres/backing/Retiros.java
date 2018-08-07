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
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticCierresRetirosDto;
import org.primefaces.context.RequestContext;

@Named(value = "manticVentasCajaCierresRetiros")
@ViewScoped
public class Retiros extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
	private Reporte reporte;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			if(JsfBase.getFlashAttribute("idCierre")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idCierre", JsfBase.getFlashAttribute("idCierre"));
      this.attrs.put("idCaja", JsfBase.getFlashAttribute("idCaja"));
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
		  this.doLoad();
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
      params.put("idCierre", this.attrs.get("idCierre"));
      params.put("sortOrder", "order by tc_mantic_cierres_retiros.consecutivo ");
      columns = new ArrayList<>();
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("caja", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));
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

  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "retiros");		
			JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "efectivo".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion((Long)this.attrs.get("idCierre"), (TcManticCierresRetirosDto)DaoFactory.getInstance().findById(TcManticCierresRetirosDto.class, seleccionado.toLong("idCierreRetiro")));
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "El retiro de efectivo se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar", "Ocurri� un error al eliminar el retiro de efectivo.", ETipoMensaje.ALERTA);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar  
	
	public void doReporte(String nombre) throws Exception {

  } // doReporte
	
	public void doVerificarReporte() {
		RequestContext rc= RequestContext.getCurrentInstance();
		if(this.reporte.getTotal()> 0L)
			rc.execute("start(" + this.reporte.getTotal() + ")");		
		else{
			rc.execute("generalHide()");		
			JsfBase.addMessage("Generar reporte","No se encontraron registros para el reporte", ETipoMensaje.ALERTA);
		} // else
	} // doVerificarReporte		

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
  public String getSuma() {
		Double sum= 0D;
		for (IBaseDto item: (List<IBaseDto>)lazyModel.getWrappedData()) {
			Entity row= (Entity)item;
			sum+= new Double(row.toString("importe"));
		} // for
	  return Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, sum);
	}	

}
