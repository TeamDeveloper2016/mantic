package mx.org.kaana.mantic.catalogos.empresas.beans;

import java.io.Serializable;
import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;

public class ContadoresListas implements Serializable{
	
	private static final Long INCREMENTO= 10000L;
	private Long totalEmpresasDomicilios;
	private Long totalEmpresasTipoContacto;	
	
	public ContadoresListas() {
		init();
	} // ContadoresListas

	public ContadoresListas(Long totalEmpresasDomicilios, Long totalEmpresasTipoContacto) {
		this.totalEmpresasDomicilios  = totalEmpresasDomicilios;
		this.totalEmpresasTipoContacto= totalEmpresasTipoContacto;
	}

	public Long getTotalEmpresasDomicilios() {
		return totalEmpresasDomicilios;
	}

	public void setTotalEmpresasDomicilios(Long totalEmpresasDomicilios) {
		this.totalEmpresasDomicilios = totalEmpresasDomicilios;
	}

	public Long getTotalEmpresasTipoContacto() {
		return totalEmpresasTipoContacto;
	}

	public void setTotalEmpresasTipoContacto(Long totalEmpresasTipoContacto) {
		this.totalEmpresasTipoContacto = totalEmpresasTipoContacto;
	}
	
	private void init(){
		try {
			this.totalEmpresasDomicilios  = toMaxEmpresaDomicilio();
			this.totalEmpresasTipoContacto= toMaxEmpresaTiposContactos();		
		} // try
		catch (Exception e) {
			Error.mensaje(e);						
		} // catch		
	} // init
	
	private Long toMaxEmpresaDomicilio() throws Exception{
		return toMax("TrManticEmpresaDomicilioDto");
	} // toMaxEmpresaDomicilio
	
	private Long toMaxEmpresaTiposContactos() throws Exception{
		return toMax("TrManticEmpresaTipoContactoDto");
	} // toMaxEmpresaTiposContactos
	
	private Long toMax(String vista) throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField(vista, "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxClienteTiposContactos
}
