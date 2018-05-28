package mx.org.kaana.mantic.catalogos.proveedores.beans;

import java.util.Collections;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;

public class ContadoresListas {
	
	private static final Long INCREMENTO= 10000L;
	private Long totalProveedoresDomicilios;
	private Long totalProveedoresAgentes;
	private Long totalProveedoresPago;
	private Long totalProveedoresTipoContacto;	
	private Long totalPersonasTipoContacto;	
	private Long totalProveedoresServicio;	
	private Long totalProveedoresTransferencia;	
	
	public ContadoresListas() {
		init();
	} // ContadoresListas
	
	public ContadoresListas(Long totalProveedoresDomicilios, Long totalProveedoresAgentes, Long totalProveedoresTipoContacto, Long totalPersonasTipoContacto, Long totalProveedoresPago, Long totalProveedoresServicio, Long totalProveedoresTransferencia) {
		this.totalProveedoresDomicilios   = totalProveedoresDomicilios;
		this.totalProveedoresAgentes      = totalProveedoresAgentes;
		this.totalProveedoresTipoContacto = totalProveedoresTipoContacto;
		this.totalPersonasTipoContacto    = totalPersonasTipoContacto;	
		this.totalProveedoresPago         = totalProveedoresPago;
		this.totalProveedoresServicio     = totalProveedoresServicio;
		this.totalProveedoresTransferencia= totalProveedoresTransferencia;
	} // ContadoresListas

	public Long getTotalProveedoresDomicilios() {
		return totalProveedoresDomicilios;
	}

	public void setTotalProveedoresDomicilios(Long totalProveedoresDomicilios) {
		this.totalProveedoresDomicilios = totalProveedoresDomicilios;
	}

	public Long getTotalProveedoresAgentes() {
		return totalProveedoresAgentes;
	}

	public void setTotalProveedoresAgentes(Long totalProveedoresAgentes) {
		this.totalProveedoresAgentes = totalProveedoresAgentes;
	}

	public Long getTotalProveedoresPago() {
		return totalProveedoresPago;
	}

	public void setTotalProveedoresPago(Long totalProveedoresPago) {
		this.totalProveedoresPago = totalProveedoresPago;
	}

	public Long getTotalProveedoresTipoContacto() {
		return totalProveedoresTipoContacto;
	}

	public void setTotalProveedoresTipoContacto(Long totalProveedoresTipoContacto) {
		this.totalProveedoresTipoContacto = totalProveedoresTipoContacto;
	}

	public Long getTotalPersonasTipoContacto() {
		return totalPersonasTipoContacto;
	}

	public void setTotalPersonasTipoContacto(Long totalPersonasTipoContacto) {
		this.totalPersonasTipoContacto = totalPersonasTipoContacto;
	}

	public Long getTotalProveedoresServicio() {
		return totalProveedoresServicio;
	}

	public void setTotalProveedoresServicio(Long totalProveedoresServicio) {
		this.totalProveedoresServicio = totalProveedoresServicio;
	}

	public Long getTotalProveedoresTransferencia() {
		return totalProveedoresTransferencia;
	}

	public void setTotalProveedoresTransferencia(Long totalProveedoresTransferencia) {
		this.totalProveedoresTransferencia = totalProveedoresTransferencia;
	}
	
	private void init(){
		try {
			this.totalProveedoresDomicilios   = toMaxProveedorDomicilio();
			this.totalProveedoresAgentes      = toMaxProveedorAgentes();
			this.totalProveedoresTipoContacto = toMaxProveedorTiposContactos();		
			this.totalPersonasTipoContacto    = toMaxPersonaTiposContactos();
			this.totalProveedoresPago         = toMaxProveedorPago();
			this.totalProveedoresServicio     = toMaxProveedorBanco();
			this.totalProveedoresTransferencia= toMaxProveedorBanco();
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);						
		} // catch		
	} // init
	
	private Long toMaxProveedorPago() throws Exception{
		return toMax("TrManticProveedorPagoDto");
	}
	
	private Long toMaxProveedorDomicilio() throws Exception{		
		return toMax("TrManticProveedorDomicilioDto");
	} // toMaxArticuloCodigo
	
	private Long toMaxProveedorAgentes() throws Exception{		
		return toMax("TrManticProveedorAgenteDto");
	} // toMaxProveedorRepresentantes
	
	private Long toMaxProveedorTiposContactos() throws Exception{		
		return toMax("TrManticProveedorTipoContactoDto");
	} // toMaxProveedorTiposContactos	
	
	private Long toMaxPersonaTiposContactos() throws Exception{		
		return toMax("TrManticPersonaTipoContactoDto");
	} // toMaxProveedorTiposContactos
	
	private Long toMaxProveedorBanco() throws Exception{		
		return toMax("TcManticProveedoresBancosDto");
	} // toMaxProveedorTiposContactos
	
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
	} // toMax
}
