package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.primefaces.event.TabChangeEvent;

@Named(value = "manticCatalogosEmpresasCuentasProrroga")
@ViewScoped
public class Prorroga extends IBaseImportar implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;	
	private Date prorroga;

	public Date getProrroga() {
		return prorroga;
	}

	public void setProrroga(Date prorroga) {
		this.prorroga = prorroga;
	}		
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
			if(JsfBase.getFlashAttribute("idEmpresaDeuda")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("sortOrder", "order by	tc_mantic_empresas_deudas.registro desc");
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));     
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
			this.attrs.put("xml", ""); 
			this.attrs.put("pdf", ""); 
			loadTiposPagos();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init	
	
  @Override
  public void doLoad() {
    Entity deuda             = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));			
			params.put("sortOrder", this.attrs.get("sortOrder"));
			deuda= (Entity) DaoFactory.getInstance().toEntity("VistaEmpresasDto", "cuentas", params);
			this.prorroga= deuda.toDate("limite");
			this.attrs.put("deuda", deuda);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
      JsfBase.addMessageError(e);
		} // catch
		finally{
			Methods.clean(params);
		} // finally	
  } // doLoad

	public String doRegresar() {	  
		JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
		return "saldos".concat(Constantes.REDIRECIONAR);
	} // doRegresar		

	private void loadTiposPagos() {
		List<UISelectEntity> tiposPagos= null;
		Map<String, Object>params      = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			tiposPagos= UIEntity.build("TcManticTiposMediosPagosDto", "row", params);
			this.attrs.put("tiposPagos", tiposPagos);
			this.attrs.put("tipoPago", UIBackingUtilities.toFirstKeySelectEntity(tiposPagos));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposPagos
	
	public String doAceptar() {
		String regresar        = null;
		Transaccion transaccion= null;
		Entity deuda           = null;
		try {
			if(validaImporte()){
				deuda= (Entity) this.attrs.get("deuda");
				transaccion= new Transaccion(deuda, this.prorroga);
				if(transaccion.ejecutar(EAccion.MODIFICAR)) {
					JsfBase.addMessage("Modificar cuenta por pagar", "Se realizó la modificación de forma correcta", ETipoMensaje.INFORMACION);
       		JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
					regresar= "saldos".concat(Constantes.REDIRECIONAR);
				} // if
				else
					JsfBase.addMessage("Modificar cuenta por pagar", "Ocurrió un error al realizar la modificación", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Modificar cuenta por pagar", "Error al modificar la cuenta", ETipoMensaje.ERROR);
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
		Double saldo  = null;
		try {
			deuda= (Entity) this.attrs.get("deuda");
			importe= Double.valueOf(String.valueOf(deuda.get("importe")));
			saldo= Double.valueOf(deuda.toString("saldo"));
			regresar= importe >= saldo;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		return regresar;
	} // validaImporte
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
 			this.doLoadImportados("VistaNotasEntradasDto", "importados", ((Entity)this.attrs.get("deuda")).toMap());
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