package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.beans.Confronta;
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.AdminAutorizacion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDto;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/06/2018
 *@time 07:51:53 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "manticCatalogosAlmacenesTransferenciasAutorizar")
@ViewScoped
public class Autorizar extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;
	
	private int typeOfCase;
	private EAccion accion;
	private static List<UISelectItem> casoUno;
	private static List<UISelectItem> casoDos;
	private static List<UISelectItem> casoTres;
	
	static {
		casoUno= new ArrayList<>();
		casoUno.add(new UISelectItem(0, "SELECCIONE"));
		casoUno.add(new UISelectItem(1, "IGNORAR CAMBIOS"));
		casoUno.add(new UISelectItem(2, "RESTAR ORIGEN"));
		casoDos= new ArrayList<>();
		casoDos.add(new UISelectItem(0, "SELECCIONE"));
		casoDos.add(new UISelectItem(1, "IGNORAR CAMBIOS"));
		casoDos.add(new UISelectItem(3, "AFECTAR ORIGEN"));
		casoDos.add(new UISelectItem(4, "AFECTAR DESTINO"));
		casoTres= new ArrayList<>();
		casoTres.add(new UISelectItem(0, "SELECCIONE"));
		casoTres.add(new UISelectItem(1, "IGNORAR CAMBIOS"));
		casoTres.add(new UISelectItem(5, "REGRESAR ORIGEN"));
		casoTres.add(new UISelectItem(6, "SUMAR DESTINO"));
	}

	public List<UISelectItem> getItems() {
		List<UISelectItem> regresar= casoUno;
		switch(this.typeOfCase) {
			case 1:
				regresar= casoUno;
				break;
			case 2:
				regresar= casoDos;
				break;
			case 3:
				regresar= casoTres;
				break;
		} // switch
		return regresar;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.typeOfCase= 1;
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.CALCULAR: (EAccion)JsfBase.getFlashAttribute("accion");
			this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      this.attrs.put("idConfronta", JsfBase.getFlashAttribute("idConfronta")== null? -1L: JsfBase.getFlashAttribute("idConfronta"));
      this.attrs.put("idTransferencia", JsfBase.getFlashAttribute("idTransferencia")== null? -1L: JsfBase.getFlashAttribute("idTransferencia"));
		  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    try {
			this.setAdminOrden(new AdminAutorizacion((Confronta)DaoFactory.getInstance().toEntity(Confronta.class, "TcManticConfrontasDto", "detalle", this.attrs)));
 			this.attrs.put("sinIva", false);
			this.attrs.put("paginator", this.getAdminOrden().getArticulos().size()> Constantes.REGISTROS_LOTE_TOPE);
			this.doResetDataTable();
			this.attrs.put("seleccionado", null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoad

	public String doRegresar() {
		JsfBase.setFlashAttribute("idTransferencia", this.attrs.get("idTransferencia"));
		JsfBase.setFlashAttribute("idConfronta", this.attrs.get("idConfronta"));
		if(this.attrs.get("idConfronta")== null)
  		return "filtro".concat(Constantes.REDIRECIONAR);
		else
  		return "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/Confrontas/filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doTransferencia() {
		JsfBase.setFlashAttribute("idTransferencia", this.attrs.get("idTransferencia"));
		return "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doConfronta() {
		JsfBase.setFlashAttribute("idConfronta", this.attrs.get("idConfronta"));
		return "/Paginas/Mantic/Catalogos/Almacenes/Confrontas/filtro".concat(Constantes.REDIRECIONAR);
	}

	public String doMovimientos() {
		JsfBase.setFlashAttribute("tipo", ETipoMovimiento.TRANSFERENCIAS);
		JsfBase.setFlashAttribute(ETipoMovimiento.TRANSFERENCIAS.getIdKey(), ((Entity)this.attrs.get("filtrado")).toLong(ETipoMovimiento.TRANSFERENCIAS.getIdKey()));
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/diferencias");
		return "/Paginas/Mantic/Compras/Ordenes/movimientos".concat(Constantes.REDIRECIONAR);
	}

	public String doOrdenColor(Articulo row) {
		this.doPrepareItems(row);
		return row.getInicial()!= 0D && !row.getObservacion().equals("*")? "janal-tr-diferencias": row.getObservacion().equals("*")? "janal-tr-nuevo": "";
	} 
	
	public String doAceptar() {
    Transaccion transaccion   = null;
		String regresar           = null;		
		try {
			transaccion = new Transaccion((TcManticConfrontasDto)this.getAdminOrden().getOrden(), this.getAdminOrden().getArticulos());
			if (transaccion.ejecutar(this.accion)) {
 			  RequestContext.getCurrentInstance().execute("janal.back(' gener\\u00F3 la confronta ', '"+ ((Confronta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
  			JsfBase.setFlashAttribute("idTransferencia", ((Confronta)this.getAdminOrden().getOrden()).getIdTransferencia());
  		  regresar = ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al autorizar la confronta de articulos.", ETipoMensaje.ALERTA);      			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		return regresar;
	}	
	
	public void doPrepareItems(Articulo row) {
		if(row.getObservacion().equals("*"))
			this.typeOfCase= 1;
		else
			if(row.getCuantos()!= 0D && row.getCantidad()!= 0D)
  			this.typeOfCase= 2;
		  else
  			this.typeOfCase= 3;
	}	
	
}
