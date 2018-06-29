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
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.creditos.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
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
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;

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
	
  @Override
	@PostConstruct
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.idTipoCreditoNota= JsfBase.getFlashAttribute("idTipoCreditoNota")== null? -1L: (Long)JsfBase.getFlashAttribute("idTipoCreditoNota");
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idCreditoNota", JsfBase.getFlashAttribute("idCreditoNota")== null? -1L: JsfBase.getFlashAttribute("idCreditoNota"));
      this.attrs.put("idDevolucion", JsfBase.getFlashAttribute("idDevolucion")== null? -1L: JsfBase.getFlashAttribute("idDevolucion"));
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
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
			transaccion = new Transaccion(this.orden, (Double)this.attrs.get("importe"));
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
   			  RequestContext.getCurrentInstance().execute("janal.back('Con n\u00FAmero de consecutivo: "+ this.orden.getConsecutivo()+ "');");
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
						if(devoluciones!= null && !devoluciones.isEmpty()) 
							if(this.accion.equals(EAccion.AGREGAR))
							  this.orden.setIkDevolucion(devoluciones.get(0));
						  else
                this.orden.setIkDevolucion(devoluciones.get(devoluciones.indexOf(this.orden.getIkDevolucion())));							
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
						this.attrs.put("notas", UIEntity.build("VistaDevolucionesDto", "notas", params, columns));
						List<UISelectEntity> notas= (List<UISelectEntity>)this.attrs.get("notas");
						if(notas!= null && !notas.isEmpty()) 
 							if(this.accion.equals(EAccion.AGREGAR))
  							this.orden.setIkNotaEntrada(notas.get(0));
						  else
                this.orden.setIkNotaEntrada(notas.get(notas.indexOf(this.orden.getIkNotaEntrada())));							
					} // if	
					break;
				case 3:
					params.put("idProveedor", this.attrs.get("idProveedor"));
					this.attrs.put("importe", 99999999D);
					if((Long)this.attrs.get("idProveedor")> 0L) {
						this.attrs.put("proveedores", UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
						List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
						if(proveedores!= null && !proveedores.isEmpty()) 
							if(this.accion.equals(EAccion.AGREGAR))
	    					this.orden.setIkProveedor(proveedores.get(0));
						  else
                this.orden.setIkProveedor(proveedores.get(proveedores.indexOf(this.orden.getIkProveedor())));							
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

}