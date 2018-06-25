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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.creditos.reglas.Transaccion;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.inventarios.creditos.beans.NotaCredito;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosCreditosAccion")
@ViewScoped
public class Accion extends IBaseAttribute
	implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;

	private EAccion accion;
	private NotaCredito orden;

	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}

	public NotaCredito getOrden() {
		return orden;
	}

	public void setOrden(NotaCredito orden) {
		this.orden=orden;
	}
	
  @Override
	@PostConstruct
  protected void init() {		
    try {
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idCreditoNota", JsfBase.getFlashAttribute("idCreditoNota")== null? -1L: JsfBase.getFlashAttribute("idCreditoNota"));
      this.attrs.put("idDevolucion", JsfBase.getFlashAttribute("idDevolucion")== null? 47L: JsfBase.getFlashAttribute("idDevolucion"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
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
			TcManticDevolucionesDto devolucion= (TcManticDevolucionesDto)DaoFactory.getInstance().findById(TcManticDevolucionesDto.class, (Long)this.attrs.get("idDevolucion"));
      switch (this.accion) {
        case AGREGAR:											
          this.orden= new NotaCredito(-1L, -1L/*devolucion.getIdDevolucion()*/);
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.orden= (NotaCredito)DaoFactory.getInstance().toEntity(NotaCredito.class, "TcManticCreditosNotasDto", "detalle", this.attrs);
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
			transaccion = new Transaccion(this.orden);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
   			  RequestContext.getCurrentInstance().execute("jsArticulos.back('generó la nota de crédito', '"+ this.orden.getConsecutivo()+ "');");
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
  	JsfBase.setFlashAttribute("idCreditoNota",this.orden.getIdCreditoNota());
    return (String)this.attrs.get("retorno");
  } 

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idDevolucion", this.attrs.get("idDevolucion"));
			if(this.attrs.get("idDevolucion")!= null) {
        this.attrs.put("devoluciones", UIEntity.build("VistasCreditosNotasDto", "devoluciones", params, columns));
			  List<UISelectEntity> devoluciones= (List<UISelectEntity>)this.attrs.get("devoluciones");
			  if(devoluciones!= null && !devoluciones.isEmpty()) 
				  this.orden.setIkDevolucion(devoluciones.get(0));
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

}