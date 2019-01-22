package mx.org.kaana.mantic.egresos.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.egresos.reglas.Transaccion;
import mx.org.kaana.mantic.enums.ECuentasEgresos;

@Named(value = "manticEgresosCuentas")
@ViewScoped
public class Cuentas extends mx.org.kaana.mantic.egresos.backing.Filtro implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {    	      
			this.attrs.put("idCuenta", JsfBase.getFlashAttribute("idCuenta"));
			this.attrs.put("eCuentaEgreso", JsfBase.getFlashAttribute("eCuentaEgreso"));			
			this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));     
      this.attrs.put("idProveedor", JsfBase.getFlashAttribute("idProveedor"));     
      this.attrs.put("idEmpresaDeuda", JsfBase.getFlashAttribute("idEmpresaDeuda"));     
      this.attrs.put("retornoPrincipal", JsfBase.getFlashAttribute("retornoPrincipal"));  
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));			
			super.init();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init		    
	
	public String doAsociar(){
		String regresar        = null;		
		Transaccion transaccion= null;		
		try {
			transaccion= new Transaccion((Long) this.attrs.get("idCuenta"), (ECuentasEgresos) this.attrs.get("eCuentaEgreso"), ((Entity) this.attrs.get("seleccionado")).getKey());
			if(transaccion.ejecutar(EAccion.ASIGNAR))
				JsfBase.addMessage("Asociar egreso", "El egreso se asocio de forma correcta.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Asociar egreso", "Ocurrio un error al asociar el egreso.", ETipoMensaje.INFORMACION);
			setFlashValues();
			regresar= this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doDetalle
	
	public String doCancelar(){
		String regresar= null;
		try {
			setFlashValues();
			regresar= this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doCancelar
	
	private void setFlashValues(){
		try {
			JsfBase.setFlashAttribute("retorno", this.attrs.get("retornoPrincipal"));
			JsfBase.setFlashAttribute("iEmpresa", this.attrs.get("idEmpresa"));
			JsfBase.setFlashAttribute("iProveedor", this.attrs.get("idProveedor"));
			JsfBase.setFlashAttribute("idEmpresaDeuda", this.attrs.get("idEmpresaDeuda"));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // setFlashValues
}