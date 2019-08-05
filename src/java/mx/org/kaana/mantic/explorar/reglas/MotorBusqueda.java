package mx.org.kaana.mantic.explorar.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;

public class MotorBusqueda implements Serializable{

	private static final long serialVersionUID = -7817547788962607914L;	
	private Long idUsuario;

	public MotorBusqueda(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public boolean toExistePedido() throws Exception{
		boolean regresar= false;
		Entity pedido   = null;
		try {
			pedido= toPedidoAbierto();
			regresar= pedido!= null && pedido.isValid();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toExistePedido
	
	public String toFieldPedido(String field) throws Exception{
		String regresar= null;
		Entity pedido  = null;
		try {
			pedido= toPedidoAbierto();
			if(pedido.isValid())
				regresar= pedido.toString(field);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPedidoAbierto
	
	public Entity toPedidoAbierto() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idUsuario", this.idUsuario);
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			regresar= (Entity) DaoFactory.getInstance().toEntity("TcManticPedidosDto", "abierto", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toPedidoAbierto
	
	public Long toTotalArticulos() throws Exception{
		Long regresar            = 0L;
		Entity pedido            = null;		
		Map<String, Object>params= null;
		try {
			pedido= toPedidoAbierto();
			if(pedido.isValid()){
				params= new HashMap<>();
				params.put("idPedido", pedido.toLong("idPedido"));				
				regresar= DaoFactory.getInstance().toField("TcManticPedidosDetallesDto", "totalArticulos", params, "total").toLong();
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toTotalArticulos
}
