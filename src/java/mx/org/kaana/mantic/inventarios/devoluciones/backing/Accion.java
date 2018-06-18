package mx.org.kaana.mantic.inventarios.devoluciones.backing;

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
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.entradas.beans.NotaEntrada;
import mx.org.kaana.mantic.inventarios.devoluciones.reglas.Transaccion;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.inventarios.devoluciones.beans.Devolucion;
import mx.org.kaana.mantic.inventarios.devoluciones.reglas.AdminDevoluciones;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosDevolucionesAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;

	private boolean aplicar;
	
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
	
  @Override
	@PostConstruct
  protected void init() {		
    try {
			this.aplicar=  false;
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idDevolucion", JsfBase.getFlashAttribute("idDevolucion")== null? -1L: JsfBase.getFlashAttribute("idDevolucion"));
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? 47L: JsfBase.getFlashAttribute("idNotaEntrada"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
			TcManticNotasEntradasDto nota= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, (Long)this.attrs.get("idNotaEntrada"));
      switch (eaccion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminDevoluciones(new Devolucion(-1L, (Long)this.attrs.get("idNotaEntrada")), nota.getTipoDeCambio(), nota.getIdSinIva()));
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.setAdminOrden(new AdminDevoluciones((Devolucion)DaoFactory.getInstance().toEntity(Devolucion.class, "TcManticDevolucionesDto", "detalle", this.attrs), nota.getTipoDeCambio(), nota.getIdSinIva()));
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
		EAccion eaccion        = null;
    try {			
			eaccion= (EAccion) this.attrs.get("accion");
			((Devolucion)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			((Devolucion)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
			((Devolucion)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			((Devolucion)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
			this.getAdminOrden().toAdjustArticulos();
			transaccion = new Transaccion(((Devolucion)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos(), this.aplicar);
			if (transaccion.ejecutar(eaccion)) {
				if(eaccion.equals(EAccion.AGREGAR) || eaccion.equals(EAccion.COMPLETO)) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
					if(eaccion.equals(EAccion.AGREGAR))
    			  RequestContext.getCurrentInstance().execute("jsArticulos.back('generó la devolución de entrada', '"+ ((Devolucion)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					else
   			    RequestContext.getCurrentInstance().execute("jsArticulos.back('aplicó la devolución de entrada', '"+ ((Devolucion)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
				} // if	
 				if(!eaccion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : eaccion.equals(EAccion.COMPLETO) ? "aplicó": "modificó").concat(" la devolución de entrada."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idDevolucion", ((Devolucion)this.getAdminOrden().getOrden()).getIdDevolucion());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la devolución de entrada.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
	public String doAplicar() {  
  	this.aplicar= true;
		return this.doAceptar();
	}

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idDevolucion", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada());
    return (String)this.attrs.get("retorno");
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idNotaEntrada", this.attrs.get("idNotaEntrada"));
			if(this.attrs.get("idNotaEntrada")!= null) {
        this.attrs.put("notas", UIEntity.build("VistaDevolucionesDto", "notas", params, columns));
			  List<UISelectEntity> notas= (List<UISelectEntity>)this.attrs.get("notas");
			  if(!notas.isEmpty()) 
				  ((Devolucion)this.getAdminOrden().getOrden()).setIkNotaEntrada(notas.get(0));
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

	public void doTabChange(TabChangeEvent event) {
		//if(event.getTab().getTitle().equals("Faltantes") && this.attrs.get("faltantes")== null) 
    //  this.toLoadFaltantes();
	}
	
}