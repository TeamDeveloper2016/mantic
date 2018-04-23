
package mx.org.kaana.mantic.catalogos.articulos.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.bean.ArticuloCodigo;
import mx.org.kaana.mantic.catalogos.articulos.bean.ArticuloDimencion;
import mx.org.kaana.mantic.catalogos.articulos.bean.ArticuloProveedor;
import mx.org.kaana.mantic.catalogos.articulos.bean.Descuento;
import mx.org.kaana.mantic.catalogos.articulos.bean.DescuentoEspecial;
import mx.org.kaana.mantic.catalogos.articulos.bean.Especificacion;
import mx.org.kaana.mantic.catalogos.articulos.bean.Importado;
import mx.org.kaana.mantic.catalogos.articulos.bean.PrecioSugerido;
import mx.org.kaana.mantic.catalogos.articulos.bean.TipoVenta;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticImagenesDto;
import mx.org.kaana.mantic.db.dto.TrManticEmpaqueUnidadMedidaDto;

public class MotorBusqueda implements Serializable{

	private static final long serialVersionUID= 5366287658013154045L;
	private static final String BYTES         = " Bytes";	
	private static final String K_BYTES       = " Kb";	
	private Long idArticulo;

	public MotorBusqueda(Long idArticulo) {
		this.idArticulo = idArticulo;
	}

	public TcManticArticulosDto toArticulo() throws Exception {
		TcManticArticulosDto regresar= null;
		try {
			regresar= (TcManticArticulosDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idArticulo);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toArticulo
	
	public TrManticEmpaqueUnidadMedidaDto toEmpaqueUnidadMedida(Long idEmpaqueUnidadMedida) throws Exception{
		TrManticEmpaqueUnidadMedidaDto regresar= null;
		try {
			regresar= (TrManticEmpaqueUnidadMedidaDto) DaoFactory.getInstance().findById(TrManticEmpaqueUnidadMedidaDto.class, idEmpaqueUnidadMedida);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toEmpaqueUnidadMedida
	
	public List<ArticuloCodigo> toArticulosCodigos() throws Exception {
		List<ArticuloCodigo> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_articulo=" + this.idArticulo);
			regresar= DaoFactory.getInstance().toEntitySet(ArticuloCodigo.class, "TcManticArticulosCodigosDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticulosCodigos
	
	public List<Especificacion> toArticulosEspecificaciones() throws Exception {
		List<Especificacion> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_articulo=" + this.idArticulo);
			regresar= DaoFactory.getInstance().toEntitySet(Especificacion.class, "TcManticArticulosEspecificacionesDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticulosEspecificaciones
	
	public List<Descuento> toArticulosDescuentos() throws Exception {
		List<Descuento> regresar = null;
		List<Descuento> pivote   = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_articulo=" + this.idArticulo);
			pivote= DaoFactory.getInstance().toEntitySet(Descuento.class, "TcManticArticulosDescuentosDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			regresar= new ArrayList<>();
			if(!pivote.isEmpty()){
				for(Descuento record: pivote){
					record.setVigenciaIni(record.getVigenciaInicial());
					record.setVigenciaFin(record.getVigenciaFinal());
					regresar.add(record);
				} // for
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticulosDescuentos
	
	public List<DescuentoEspecial> toArticulosDescuentosEspeciales() throws Exception {
		List<DescuentoEspecial> regresar= null;
		List<DescuentoEspecial> pivote  = null;
		Map<String, Object>params       = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_articulo=" + this.idArticulo);
			pivote= DaoFactory.getInstance().toEntitySet(DescuentoEspecial.class, "TrManticArticuloClienteDescuentoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			regresar= new ArrayList<>();
			if(!pivote.isEmpty()){
				for(DescuentoEspecial record: pivote){
					record.setVigenciaIni(record.getVigenciaInicial());
					record.setVigenciaFin(record.getVigenciaFinal());
					regresar.add(record);
				} // for
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticulosDescuentosEspeciales
	
	public List<PrecioSugerido> toArticulosPreciosSugeridos() throws Exception {
		List<PrecioSugerido> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_articulo=" + this.idArticulo);
			regresar= DaoFactory.getInstance().toEntitySet(PrecioSugerido.class, "TrManticArticuloPrecioSugeridoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticulosPreciosSugeridos
	
	public List<ArticuloProveedor> toArticulosProveedor() throws Exception {
		List<ArticuloProveedor> regresar= null;
		List<ArticuloProveedor> pivote  = null;
		Map<String, Object>params       = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_articulo=" + this.idArticulo);
			pivote= DaoFactory.getInstance().toEntitySet(ArticuloProveedor.class, "TrManticArticuloProveedorDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
			regresar= new ArrayList<>();
			if(!pivote.isEmpty()){
				for(ArticuloProveedor record: pivote){
					record.setCompra(record.getFechaCompra());
					regresar.add(record);
				} // for
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticulosProveedor
	
	public List<TipoVenta> toArticulosTipoVenta() throws Exception {
		List<TipoVenta> regresar = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_articulo=" + this.idArticulo);
			regresar= DaoFactory.getInstance().toEntitySet(TipoVenta.class, "TrManticArticuloTipoVentaDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticulosTipoVenta
	
	public ArticuloDimencion toArticuloDimencion() throws Exception {
		ArticuloDimencion regresar= null;
		Map<String, Object>params = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_articulo=" + this.idArticulo);
			regresar= (ArticuloDimencion) DaoFactory.getInstance().toEntity(ArticuloDimencion.class, "TcManticArticulosDimencionesDto", "row", params);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toArticuloDimencion
	
	public Importado toArticuloImagen(Long idImagen) throws Exception {
		Importado regresar        = null;
		TcManticImagenesDto imagen= null;
		try {
			imagen= (TcManticImagenesDto) DaoFactory.getInstance().findById(TcManticImagenesDto.class, idImagen);
			if(imagen!= null)
				regresar= new Importado(imagen.getNombre(), imagen.getArchivo(), EFormatos.FREE, imagen.getTamanio(), imagen.getTamanio(), BYTES, imagen.getRuta());			
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
		return regresar;
	} // toArticuloImagen
}
