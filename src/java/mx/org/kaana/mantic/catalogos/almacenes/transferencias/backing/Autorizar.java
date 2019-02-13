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
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.beans.Confronta;
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.AdminAutorizacion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDto;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
  private static final Log LOG=LogFactory.getLog(Autorizar.class);
	
	private int typeOfCase;
	private EAccion accion;
	private List<UISelectEntity> casoUno;
	private List<UISelectEntity> casoDos;
	private List<UISelectEntity> casoTres;
	
	public List<UISelectEntity> getItems() {
		List<UISelectEntity> regresar= casoUno;
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
			this.casoUno= new ArrayList<>();
			this.casoUno.add(new UISelectEntity(new Entity(0L, "SELECCIONE")));
			this.casoUno.add(new UISelectEntity(new Entity(1L, "IGNORAR CAMBIOS")));
			this.casoUno.add(new UISelectEntity(new Entity(2L, "RESTAR ORIGEN")));
			this.casoDos= new ArrayList<>();
			this.casoDos.add(new UISelectEntity(new Entity(0L, "SELECCIONE")));
			this.casoDos.add(new UISelectEntity(new Entity(1L, "IGNORAR CAMBIOS"))); 
			this.casoDos.add(new UISelectEntity(new Entity(2L, "AFECTAR ORIGEN"))); // 3
			this.casoDos.add(new UISelectEntity(new Entity(3L, "AFECTAR DESTINO"))); // 4
			this.casoTres= new ArrayList<>();
			this.casoTres.add(new UISelectEntity(new Entity(0L, "SELECCIONE")));
			this.casoTres.add(new UISelectEntity(new Entity(1L, "IGNORAR CAMBIOS")));
			this.casoTres.add(new UISelectEntity(new Entity(2L, "REGRESAR ORIGEN"))); // 5
			this.casoTres.add(new UISelectEntity(new Entity(3L, "SUMAR DESTINO"))); // 6
			this.typeOfCase= 1;
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.CALCULAR: (EAccion)JsfBase.getFlashAttribute("accion");
			this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      this.attrs.put("idConfronta", JsfBase.getFlashAttribute("idConfronta")== null? -1L: JsfBase.getFlashAttribute("idConfronta"));
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Mantic/Catalogos/Almacenes/Confrontas/filtro": JsfBase.getFlashAttribute("retorno"));
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
  		return "/Paginas/Mantic/Catalogos/Almacenes/Confrontas/filtro".concat(Constantes.REDIRECIONAR);
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
    Transaccion transaccion= null;
		String regresar        = null;		
		try {
			transaccion = new Transaccion((TcManticConfrontasDto)this.getAdminOrden().getOrden(), this.getAdminOrden().getArticulos());
			for (Articulo articulo : this.getAdminOrden().getArticulos()) {
				this.doPrepareItems(articulo);
  		  articulo.setIdRedondear(articulo.getIdAplicar());
				switch(this.typeOfCase) {
					 case 1:
						 break;
					 case 2:
						 if(articulo.getIdAplicar()> 1L)
						   articulo.setIdRedondear(articulo.getIdAplicar()+ 1L);
						 break;
					 case 3:
						 if(articulo.getIdAplicar()> 1L)
						   articulo.setIdRedondear(articulo.getIdAplicar()+ 3L);
						 break;
				 } // switch				
//				LOG.info("idAplicar: "+ articulo.getIdAplicar()+ " =>  "+ articulo.getIdRedondear());
			} // for 
			if(transaccion.ejecutar(this.accion)) {
 			  UIBackingUtilities.execute("janal.back(' autoriz\\u00F3 la confronta ', '"+ ((Confronta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
  			JsfBase.setFlashAttribute("IdConfronta", ((Confronta)this.getAdminOrden().getOrden()).getIdConfronta());
  		  regresar = ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al autorizar la confronta de articulos.", ETipoMensaje.ALERTA);      			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
      JsfBase.addMessageError(e);
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
