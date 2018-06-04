package mx.org.kaana.mantic.catalogos.clientes.beans;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;

public class ContadoresListas {

	private static final Long INCREMENTO= 10000L;
	private Long totalClientesDomicilios;
	private Long totalClientesRepresentantes;
	private Long totalClientesTipoContacto;	
	private Long totalPersonasTipoContacto;	

	public ContadoresListas() {
		init();
	} // ContadoresListas
	
	public ContadoresListas(Long totalClientesDomicilios, Long totalClientesRepresentantes, Long totalClientesTipoContacto, Long totalPersonasTipoContacto) {
		this.totalClientesDomicilios    = totalClientesDomicilios;
		this.totalClientesRepresentantes= totalClientesRepresentantes;
		this.totalClientesTipoContacto  = totalClientesTipoContacto;
		this.totalPersonasTipoContacto  = totalPersonasTipoContacto;	
	} // ContadoresListas

	public Long getTotalClientesDomicilios() {
		return totalClientesDomicilios;
	}

	public void setTotalClientesDomicilios(Long totalClientesDomicilios) {
		this.totalClientesDomicilios = totalClientesDomicilios;
	}

	public Long getTotalClientesRepresentantes() {
		return totalClientesRepresentantes;
	}

	public void setTotalClientesRepresentantes(Long totalClientesRepresentantes) {
		this.totalClientesRepresentantes = totalClientesRepresentantes;
	}

	public Long getTotalClientesTipoContacto() {
		return totalClientesTipoContacto;
	}

	public void setTotalClientesTipoContacto(Long totalClientesTipoContacto) {
		this.totalClientesTipoContacto = totalClientesTipoContacto;
	}

	public Long getTotalPersonasTipoContacto() {
		return totalPersonasTipoContacto;
	}

	public void setTotalPersonasTipoContacto(Long totalPersonasTipoContacto) {
		this.totalPersonasTipoContacto = totalPersonasTipoContacto;
	}
	
	private void init(){
		try {
			this.totalClientesDomicilios    = toMaxClienteDomicilio();
			this.totalClientesRepresentantes= toMaxClienteRepresentantes();
			this.totalClientesTipoContacto  = toMaxClienteTiposContactos();		
			this.totalPersonasTipoContacto  = toMaxPersonaTiposContactos();
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);						
		} // catch		
	} // init
	
	private Long toMaxClienteDomicilio() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticClienteDomicilioDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxArticuloCodigo
	
	private Long toMaxClienteRepresentantes() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticClienteRepresentanteDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxClienteRepresentantes
	
	private Long toMaxClienteTiposContactos() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticClienteTipoContactoDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxClienteTiposContactos	
	
	private Long toMaxPersonaTiposContactos() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticPersonaTipoContactoDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxClienteTiposContactos
}
