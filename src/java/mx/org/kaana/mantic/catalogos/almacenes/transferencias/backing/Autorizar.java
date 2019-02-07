package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.beans.Confronta;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.AdminAutorizacion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.enums.ETipoMovimiento;

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
	
	private EAccion accion;
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
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
		return row.getInicial()!= 0D && !row.getObservacion().equals("*")? "janal-tr-diferencias": row.getObservacion().equals("*")? "janal-tr-nuevo": "";
	} 
	
	public String doAceptar() {
		String regresar=null;		
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}	
}
