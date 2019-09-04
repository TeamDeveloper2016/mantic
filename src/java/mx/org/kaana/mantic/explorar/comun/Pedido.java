package mx.org.kaana.mantic.explorar.comun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.explorar.reglas.MotorBusqueda;
import mx.org.kaana.mantic.explorar.reglas.Transaccion;

public abstract class Pedido extends IBaseAttribute implements Serializable{
	
	private static final long serialVersionUID = -3990912822820692539L;	
	
	public void initPedido(){
		try {
			loadPedido();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // initPedido
	
	public List<UISelectEntity> doCompleteArticulo(String query) {
		this.attrs.put("codigoFiltro", query);
    this.doUpdateArticulosFiltro();
		return (List<UISelectEntity>)this.attrs.get("articulosFiltro");
	}	// doCompleteArticulo
	
	public void doUpdateArticulosFiltro() {
		List<Columna> columns         = null;
    Map<String, Object> params    = null;
		List<UISelectEntity> articulos= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params= new HashMap<>();
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);
			String search= (String) this.attrs.get("codigoFiltro"); 
			if(!Cadena.isVacio(search)) 
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim().toUpperCase().replaceAll("(,| |\\t)+", ".*.*");			
			else
				search= "WXYZ";
  		params.put("codigo", search);			        
      articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombreTipoArticulo", params, columns, 20L);
      this.attrs.put("articulosFiltro", articulos);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}	// doUpdateArticulos
	
	protected void loadPedido(){
		Entity pedido          = null;
		MotorBusqueda motor    = null;
		Transaccion transaccion= null;
		try {
			motor= new MotorBusqueda(JsfBase.getIdUsuario());
			if(motor.toExistePedido()){
				pedido= motor.toPedidoAbierto();
				this.attrs.put("pedidoCount", motor.toTotalArticulos());							
				this.attrs.put("subTotal", pedido.toString("subTotal"));							
				this.attrs.put("total", pedido.toString("total"));							
				this.attrs.put("idPedido", pedido.toLong("idPedido"));							
			} // if
			else{
				this.attrs.put("pedidoCount", 0);
				this.attrs.put("subTotal", 0);							
				this.attrs.put("total", 0);
				transaccion= new Transaccion();
				transaccion.ejecutar(EAccion.ACTIVAR);
				pedido= motor.toPedidoAbierto();
				this.attrs.put("idPedido", pedido.toLong("idPedido"));							
			} // else
			this.attrs.put("cantidad", 1);							
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // loadPedido
	
	public String	doCancelar() {
    return "filtro".concat(Constantes.REDIRECIONAR);
	} // doCancelar
}