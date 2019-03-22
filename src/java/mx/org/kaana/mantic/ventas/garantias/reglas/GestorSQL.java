package mx.org.kaana.mantic.ventas.garantias.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.reflection.Methods;

public class GestorSQL implements Serializable{
	
	private static final long serialVersionUID= -5177029300710740725L;
	private Long idVenta;
	private Long idCliente;

	public GestorSQL(Long idVenta) {
		this(idVenta, -1L);
	}
	
	public GestorSQL(Long idVenta, Long idCliente) {
		this.idVenta  = idVenta;
		this.idCliente= idCliente;
	}
	
	public Entity toDeudaVenta() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idVenta", this.idVenta);
			params.put("idCliente", this.idCliente);
			regresar= (Entity) DaoFactory.getInstance().toEntity("TcManticClientesDeudasDto", "deudaVenta", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDeudaVenta	
	
	public double toSaldoCliente() throws Exception{
		double regresar          = 0D;
		Map<String, Object>params= null;
		Entity saldo             = null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.idCliente);
			params.put("idVenta", this.idVenta);
			saldo= (Entity) DaoFactory.getInstance().toEntity("TcManticClientesDeudasDto", "saldoClienteExcluirVenta", params);
			if(saldo!= null)
				regresar= saldo.toDouble("saldo");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSaldoCliente
}