package mx.org.kaana.mantic.explorar.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticPedidosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticPedidosDto;
import mx.org.kaana.mantic.explorar.beans.Item;
import mx.org.kaana.mantic.explorar.comun.Pedido;
import mx.org.kaana.mantic.explorar.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/05/2018
 *@time 02:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticExplorarDetalle")
@ViewScoped
public class Detalle extends Pedido implements Serializable {

	private static final long serialVersionUID= -6770709196941718388L;
	private static final Log LOG              = LogFactory.getLog(Detalle.class);

	@Override
	@PostConstruct
	protected void init() {    
		try {
			super.initPedido();
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      doLoad();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);		
		} // catch				
	} // init
	
	public void doLoad(){
		List<Columna> columns     = null;
		Map<String, Object> params= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("precio", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			params=new HashMap<>();
			params.put("idUsuario", JsfBase.getIdUsuario());
			Entity pedido= (Entity)DaoFactory.getInstance().toEntity("VistaPedidosDto", params);			
			this.attrs.put("pedido", pedido);
			if(pedido!= null && !pedido.isEmpty()) {
	  		params.put("idPedido", pedido.toLong("idPedido"));
				List<Item> detalle= (List<Item>)DaoFactory.getInstance().toEntitySet(Item.class, "VistaPedidosDto", "pedido", params);
		  	if(detalle!= null && !detalle.isEmpty()) 
	  			this.attrs.put("detalle", detalle);		
				else
					this.attrs.put("detalle", new ArrayList<>());		
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(columns);
		} // finally
	} // doLoad
	
	public StreamedContent doPrepareImage(Item row) {
		StreamedContent regresar= null;
		try {
			regresar= LoadImages.getImage(row.getIdArticulo());
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		return regresar;
	} // doPrepareImage

	public String doItemDelete(Item row) {
		Transaccion transaccion= null;
		try {			
			transaccion= new Transaccion((TcManticPedidosDetallesDto)row);
			if(transaccion.ejecutar(EAccion.ELIMINAR)){
				JsfBase.addMessage("Eliminar articulo", "Se eliminó el articulo de forma correcta.", ETipoMensaje.INFORMACION);
				init();
				UIBackingUtilities.execute("updateCountVal(".concat(this.attrs.get("pedidoCount").toString()).concat(");"));
			} // if
			else
				JsfBase.addMessage("Eliminar articulo", "Ocurrió un error al eliminar el articulo.", ETipoMensaje.ERROR);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch 
		return "detalle".concat(Constantes.REDIRECIONAR);
	} // doItemDelete

	public void doItemChange(Item row) {
		Transaccion transaccion= null;
		try {			
			if(!Objects.equals(row.getOriginal(), row.getCantidad())) {
			  transaccion= new Transaccion((TcManticPedidosDetallesDto)row);
			  if(transaccion.ejecutar(EAccion.MODIFICAR)){
					JsfBase.addMessage("Modificar cantidad de articulo", "Se modificó la cantidad del articulo de forma correcta.", ETipoMensaje.INFORMACION);
					init();
				} // if
				else
					JsfBase.addMessage("Modificar cantidad de articulo", "Ocurrió un error al modificar la cantidad del articulo.", ETipoMensaje.ERROR);
			} // if	
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} // doItemChange

	public String doBusqueda() {
		String regresar               = null;
		String criterio               = null;
		List<UISelectEntity> articulos= null;
		try {			
			if(this.attrs.get("nombre")!= null && ((UISelectEntity)this.attrs.get("nombre")).getKey()> 0L){
				articulos= (List<UISelectEntity>) this.attrs.get("articulosFiltro");				
				JsfBase.setFlashAttribute("articulo", ((Entity)articulos.get(articulos.indexOf((UISelectEntity)this.attrs.get("nombre")))));
				regresar= "accion".concat(Constantes.REDIRECIONAR);
			} // if
  		else if(!Cadena.isVacio(this.attrs.get("nombreHidden"))){ 
				criterio= this.attrs.get("nombreHidden").toString();		  			
				JsfBase.setFlashAttribute("criterio", criterio!= null ? criterio.toUpperCase() : criterio);
				regresar= "filtro".concat(Constantes.REDIRECIONAR);
			} // else
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doBusqueda

	public String doAceptar() {		
		Transaccion transaccion= null;
		Entity pedido          = null;
		try {
			pedido= (Entity) this.attrs.get("pedido");
			transaccion= new Transaccion(new TcManticPedidosDto(pedido.getKey()));
			if(transaccion.ejecutar(EAccion.PROCESAR))
				JsfBase.addMessage("Cerrar pedidio", "El pedido se cerró de forma correcta.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cerrar pedidio", "Ocurrió un error al cerrar el pedido.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return "listado".concat(Constantes.REDIRECIONAR);
	}	// Transaccion
}