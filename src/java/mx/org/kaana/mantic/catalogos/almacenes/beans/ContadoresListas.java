package mx.org.kaana.mantic.catalogos.almacenes.beans;

import java.util.Collections;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;

public class ContadoresListas {

	private static final Long INCREMENTO= 10000L;
	private Long totalAlmacenesDomicilios;
	private Long totalAlmacenesTipoContacto;	
	private Long totalAlmacenesUbicacion;	
	private Long totalAlmacenesArticulo;	

	public ContadoresListas() {
		init();
	} // ContadoresListas
	
	public ContadoresListas(Long totalAlmacenesDomicilios, Long totalAlmacenesTipoContacto, Long totalAlmacenesUbicacion, Long totalAlmacenesArticulo) {
		this.totalAlmacenesDomicilios  = totalAlmacenesDomicilios;
		this.totalAlmacenesTipoContacto= totalAlmacenesTipoContacto;
		this.totalAlmacenesUbicacion   = totalAlmacenesUbicacion;
		this.totalAlmacenesArticulo    = totalAlmacenesArticulo;
	} // ContadoresListas

	public Long getTotalAlmacenesDomicilios() {
		return totalAlmacenesDomicilios;
	}

	public void setTotalAlmacenesDomicilios(Long totalAlmacenesDomicilios) {
		this.totalAlmacenesDomicilios = totalAlmacenesDomicilios;
	}

	public Long getTotalAlmacenesTipoContacto() {
		return totalAlmacenesTipoContacto;
	}

	public void setTotalAlmacenesTipoContacto(Long totalAlmacenesTipoContacto) {
		this.totalAlmacenesTipoContacto = totalAlmacenesTipoContacto;
	}

	public Long getTotalAlmacenesUbicacion() {
		return totalAlmacenesUbicacion;
	}

	public void setTotalAlmacenesUbicacion(Long totalAlmacenesUbicacion) {
		this.totalAlmacenesUbicacion = totalAlmacenesUbicacion;
	}	

	public Long getTotalAlmacenesArticulo() {
		return totalAlmacenesArticulo;
	}

	public void setTotalAlmacenesArticulo(Long totalAlmacenesArticulo) {
		this.totalAlmacenesArticulo = totalAlmacenesArticulo;
	}
	
	private void init(){
		try {
			this.totalAlmacenesDomicilios  = toMaxAlmacenDomicilio();
			this.totalAlmacenesTipoContacto= toMaxAlmacenTiposContactos();			
			this.totalAlmacenesUbicacion   = toMaxAlmacenUbicacion();			
			this.totalAlmacenesArticulo    = toMaxAlmacenArticulo();			
		} // try
		catch (Exception e) {
			Error.mensaje(e);						
		} // catch		
	} // init
	
	private Long toMaxAlmacenDomicilio() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticAlmacenDomicilioDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxArticuloCodigo	
	
	private Long toMaxAlmacenTiposContactos() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticAlmacenTipoContactoDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxAlmacenTiposContactos
	
	private Long toMaxAlmacenUbicacion() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TcManticAlmacenesUbicacionesDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxAlmacenUbicacion
	
	private Long toMaxAlmacenArticulo() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TcManticAlmacenesArticulosDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxAlmacenUbicacion
}
