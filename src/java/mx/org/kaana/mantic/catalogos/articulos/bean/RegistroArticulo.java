package mx.org.kaana.mantic.catalogos.articulos.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.articulos.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDescuentosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDimencionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosEspecificacionesDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloClienteDescuentoDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloPrecioSugeridoDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloProveedorDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloTipoVentaDto;
import mx.org.kaana.mantic.db.dto.TrManticEmpaqueUnidadMedidaDto;

public class RegistroArticulo implements Serializable{
	
	private Long idArticulo;
	private Long idEmpaque;
	private String observaciones;
	private TcManticArticulosDto articulo;
	private List<TcManticArticulosCodigosDto> articulosCodigos;
	private List<TcManticArticulosEspecificacionesDto> especificaciones;
	private List<TcManticArticulosDescuentosDto> articulosDescuentos;
	private List<TrManticArticuloClienteDescuentoDto> clientesDescuentos;
	private List<TrManticArticuloPrecioSugeridoDto> preciosSugeridos;
	private List<TrManticArticuloProveedorDto> articulosProveedores;
	private TrManticArticuloTipoVentaDto articuloVenta;
	private List<TcManticArticulosDimencionesDto> articulosDimenciones;

	public RegistroArticulo() {
		this(-1L, 
				new TcManticArticulosDto(),
				new ArrayList<TcManticArticulosCodigosDto>(),
				new ArrayList<TcManticArticulosEspecificacionesDto>(),
				new ArrayList<TcManticArticulosDescuentosDto>(),
				new ArrayList<TrManticArticuloClienteDescuentoDto>(),
				new ArrayList<TrManticArticuloPrecioSugeridoDto>(),
				new ArrayList<TrManticArticuloProveedorDto>(),
				new TrManticArticuloTipoVentaDto(),
				new ArrayList<TcManticArticulosDimencionesDto>(), 
				-1L, null
				);
	} // RegistroArticulo
	
	public RegistroArticulo(Long idArticulo) {
		this.idArticulo = idArticulo;
		init();
	}
	
	public RegistroArticulo(Long idArticulo, TcManticArticulosDto articulo, List<TcManticArticulosCodigosDto> articulosCodigos, List<TcManticArticulosEspecificacionesDto> especificaciones, List<TcManticArticulosDescuentosDto> articulosDescuentos, List<TrManticArticuloClienteDescuentoDto> clientesDescuentos, List<TrManticArticuloPrecioSugeridoDto> preciosSugeridos, List<TrManticArticuloProveedorDto> articulosProveedores, TrManticArticuloTipoVentaDto articuloVenta, List<TcManticArticulosDimencionesDto> articulosDimenciones, Long idEmpaque, String obervaciones) {
		this.idArticulo          = idArticulo;
		this.articulo            = articulo;
		this.articulosCodigos    = articulosCodigos;
		this.especificaciones    = especificaciones;
		this.articulosDescuentos = articulosDescuentos;
		this.clientesDescuentos  = clientesDescuentos;
		this.preciosSugeridos    = preciosSugeridos;
		this.articulosProveedores= articulosProveedores;
		this.articuloVenta       = articuloVenta;
		this.articulosDimenciones= articulosDimenciones;
		this.idEmpaque           = idEmpaque;
	}

	public Long getIdArticulo() {
		return idArticulo;
	}

	public TcManticArticulosDto getArticulo() {
		return articulo;
	}

	public List<TcManticArticulosCodigosDto> getArticulosCodigos() {
		return articulosCodigos;
	}

	public List<TcManticArticulosEspecificacionesDto> getEspecificaciones() {
		return especificaciones;
	}

	public List<TcManticArticulosDescuentosDto> getArticulosDescuentos() {
		return articulosDescuentos;
	}

	public List<TrManticArticuloClienteDescuentoDto> getClientesDescuentos() {
		return clientesDescuentos;
	}

	public List<TrManticArticuloPrecioSugeridoDto> getPreciosSugeridos() {
		return preciosSugeridos;
	}

	public List<TrManticArticuloProveedorDto> getArticulosProveedores() {
		return articulosProveedores;
	}

	public TrManticArticuloTipoVentaDto getArticuloVenta() {
		return articuloVenta;
	}

	public List<TcManticArticulosDimencionesDto> getArticulosDimenciones() {
		return articulosDimenciones;
	}

	public Long getIdEmpaque() {
		return idEmpaque;
	}

	public void setIdEmpaque(Long idEmpaque) {
		this.idEmpaque = idEmpaque;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	private void init(){
		MotorBusqueda motorBusqueda                = null;
		TrManticEmpaqueUnidadMedidaDto unidadMedida= null;
		try {
			motorBusqueda= new MotorBusqueda(this.idArticulo);
			this.articulo= motorBusqueda.toArticulo();
			unidadMedida= motorBusqueda.toEmpaqueUnidadMedida(this.articulo.getIdEmpaqueUnidadMedida());
			this.idEmpaque= unidadMedida.getIdEmpaque();
			initCollections();
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // init
	
	private void initCollections(){
		
	}
}
