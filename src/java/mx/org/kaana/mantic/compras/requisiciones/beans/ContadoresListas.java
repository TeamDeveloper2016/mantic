package mx.org.kaana.mantic.compras.requisiciones.beans;

import java.io.Serializable;
import java.util.Collections;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;

public class ContadoresListas implements Serializable{

	private static final Long INCREMENTO= 10000L;
	private Long totalRequisicionProveedor;
	
	public ContadoresListas() {
		init();
	} // 

	public Long getTotalRequisicionProveedor() {
		return totalRequisicionProveedor;
	}	
	
	public ContadoresListas(Long totalRequisicionProveedor) {
		this.totalRequisicionProveedor= totalRequisicionProveedor;
	} // ContadoresListas
	
	private void init(){
		try {
			this.totalRequisicionProveedor= toMaxtotalRequisicionProveedor();			
		} // try
		catch (Exception e) {
			Error.mensaje(e);						
		} // catch		
	} // init
	
	private Long toMaxtotalRequisicionProveedor() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TcManticRequisicionesProveedoresDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxArticuloCodigo
}
