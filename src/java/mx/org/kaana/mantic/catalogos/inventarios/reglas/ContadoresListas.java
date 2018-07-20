package mx.org.kaana.mantic.catalogos.inventarios.reglas;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;

public class ContadoresListas {

	private static final Long INCREMENTO= 10000L;
	private Long totalInventariosArticulos;
	
	public ContadoresListas() {
		init();
	} // ContadoresListas
	
	public ContadoresListas(Long totalInventariosArticulos) {
		this.totalInventariosArticulos= totalInventariosArticulos;		
	} // ContadoresListas

	public Long getTotalInventariosArticulos() {
		return totalInventariosArticulos;
	}

	public void setTotalInventariosArticulos(Long totalInventariosArticulos) {
		this.totalInventariosArticulos = totalInventariosArticulos;
	}
	
	private void init(){
		try {
			this.totalInventariosArticulos= toMaxInventariosArticulos();			
		} // try
		catch (Exception e) {
			Error.mensaje(e);						
		} // catch		
	} // init
	
	private Long toMaxInventariosArticulos() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TcManticInventariosDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxArticuloCodigo
}
