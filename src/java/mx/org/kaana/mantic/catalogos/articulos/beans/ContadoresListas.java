package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;

public class ContadoresListas {
	
	private static final Long INCREMENTO= 10000L;
	private Long totalArticulosCodigos;
	private Long totalEspecificaciones;
	private Long totalDescuentos;
	private Long totalDescuentosEspeciales;
	private Long totalArticulosProveedor;
	private Long totalPreciosSugeridos;
	private Long totalTiposVentas;

	public ContadoresListas() {
		init();
	} // 

	public ContadoresListas(Long totalArticulosCodigos, Long totalEspecificaciones, Long totalDescuentos, Long totalDescuentosEspeciales, Long totalArticulosProveedor, Long totalPreciosSugeridos, Long totalTiposVentas) {
		this.totalArticulosCodigos    = totalArticulosCodigos;
		this.totalEspecificaciones    = totalEspecificaciones;
		this.totalDescuentos          = totalDescuentos;
		this.totalDescuentosEspeciales= totalDescuentosEspeciales;
		this.totalArticulosProveedor  = totalArticulosProveedor;
		this.totalPreciosSugeridos    = totalPreciosSugeridos;
		this.totalTiposVentas         = totalTiposVentas;
	}		

	public Long getTotalArticulosCodigos() {
		return totalArticulosCodigos;
	}

	public void setTotalArticulosCodigos(Long totalArticulosCodigos) {
		this.totalArticulosCodigos = totalArticulosCodigos;
	}	

	public Long getTotalEspecificaciones() {
		return totalEspecificaciones;
	}

	public void setTotalEspecificaciones(Long totalEspecificaciones) {
		this.totalEspecificaciones = totalEspecificaciones;
	}

	public Long getTotalDescuentos() {
		return totalDescuentos;
	}

	public void setTotalDescuentos(Long totalDescuentos) {
		this.totalDescuentos = totalDescuentos;
	}

	public Long getTotalDescuentosEspeciales() {
		return totalDescuentosEspeciales;
	}

	public void setTotalDescuentosEspeciales(Long totalDescuentosEspeciales) {
		this.totalDescuentosEspeciales = totalDescuentosEspeciales;
	}

	public Long getTotalArticulosProveedor() {
		return totalArticulosProveedor;
	}

	public void setTotalArticulosProveedor(Long totalArticulosProveedor) {
		this.totalArticulosProveedor = totalArticulosProveedor;
	}

	public Long getTotalPreciosSugeridos() {
		return totalPreciosSugeridos;
	}

	public void setTotalPreciosSugeridos(Long totalPreciosSugeridos) {
		this.totalPreciosSugeridos = totalPreciosSugeridos;
	}	

	public Long getTotalTiposVentas() {
		return totalTiposVentas;
	}

	public void setTotalTiposVentas(Long totalTiposVentas) {
		this.totalTiposVentas = totalTiposVentas;
	}
	
	private void init(){
		try {
			this.totalArticulosCodigos    = toMaxArticuloCodigo();
			this.totalEspecificaciones    = toMaxEspecificaciones();
			this.totalDescuentos          = toMaxDescuentos();
			this.totalDescuentosEspeciales= toMaxDescuentosEspeciales();
			this.totalArticulosProveedor  = toMaxArticulosProveedor();
			this.totalPreciosSugeridos    = toMaxPreciosSugeridos();
			this.totalTiposVentas         = toMaxTiposVentas();
		} // try
		catch (Exception e) {
			Error.mensaje(e);						
		} // catch		
	} // init
	
	private Long toMaxArticuloCodigo() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TcManticArticulosCodigosDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toMaxArticuloCodigo
	
	private Long toMaxEspecificaciones() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TcManticArticulosEspecificacionesDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toMaxArticuloCodigo
	
	private Long toMaxDescuentos() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TcManticArticulosDescuentosDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toMaxArticuloCodigo
	
	private Long toMaxDescuentosEspeciales() throws Exception{
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticArticuloGrupoDescuentoDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toMaxArticuloCodigo	

	private Long toMaxArticulosProveedor() throws Exception {
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticArticuloProveedorDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toMaxArticulosProveedor

	private Long toMaxPreciosSugeridos() throws Exception {
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticArticuloPrecioSugeridoDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toMaxPreciosSugeridos
	
	private Long toMaxTiposVentas() throws Exception {
		Long regresar= 0L;
		Value maximo = null;
		try {
			maximo= DaoFactory.getInstance().toField("TrManticArticuloTipoVentaDto", "maximo", Collections.EMPTY_MAP, "maximo");
			if(maximo.getData()!= null)
				regresar= Long.valueOf(maximo.toString()) + INCREMENTO;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} //
}
