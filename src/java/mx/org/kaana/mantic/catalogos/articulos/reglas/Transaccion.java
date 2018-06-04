
package mx.org.kaana.mantic.catalogos.articulos.reglas;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.mantic.db.dto.TcManticImagenesDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.articulos.beans.ArticuloCodigo;
import mx.org.kaana.mantic.catalogos.articulos.beans.ArticuloProveedor;
import mx.org.kaana.mantic.catalogos.articulos.beans.Descuento;
import mx.org.kaana.mantic.catalogos.articulos.beans.DescuentoEspecial;
import mx.org.kaana.mantic.catalogos.articulos.beans.Especificacion;
import mx.org.kaana.mantic.catalogos.articulos.beans.PrecioSugerido;
import mx.org.kaana.mantic.catalogos.articulos.beans.RegistroArticulo;
import mx.org.kaana.mantic.catalogos.articulos.beans.TipoVenta;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDescuentosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDimencionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosEspecificacionesDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloGrupoDescuentoDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloPrecioSugeridoDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloProveedorDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloTipoVentaDto;
import mx.org.kaana.mantic.enums.ETipoImagen;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Long ACTIVO  = 1L;
	private static final Long INACTIVO= 0L;
	private RegistroArticulo articulo;	
	private String messageError;

	public Transaccion(RegistroArticulo articulo) {
		this.articulo     = articulo;		
	} // Transaccion

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			switch(accion){
				case AGREGAR:
					regresar= procesarArticulo(sesion);
					break;
				case MODIFICAR:
					regresar= actualizarArticulo(sesion);
					break;				
				case ELIMINAR:
					regresar= eliminarArticulo(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception(this.messageError);
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		return regresar;
	} // ejecutar
	
	private boolean eliminarArticulo(Session sesion) throws Exception{
		boolean regresar         = false;		
		Map<String, Object>params= null;
		try {			
			params= new HashMap<>();
			params.put("idArticulo", this.articulo.getIdArticulo());
			if(DaoFactory.getInstance().deleteAll(sesion, TcManticArticulosCodigosDto.class, params)> -1L){
				if(DaoFactory.getInstance().deleteAll(sesion, TcManticArticulosEspecificacionesDto.class, params)> -1L){
					if(DaoFactory.getInstance().deleteAll(sesion, TcManticArticulosDescuentosDto.class, params)> -1L){
						if(DaoFactory.getInstance().deleteAll(sesion, TrManticArticuloGrupoDescuentoDto.class, params)> -1L){
							if(DaoFactory.getInstance().deleteAll(sesion, TrManticArticuloPrecioSugeridoDto.class, params)> -1L){
								if(DaoFactory.getInstance().deleteAll(sesion, TrManticArticuloProveedorDto.class, params)> -1L){
									if(DaoFactory.getInstance().deleteAll(sesion, TrManticArticuloTipoVentaDto.class, params)> -1L){
										if(DaoFactory.getInstance().deleteAll(sesion, TcManticArticulosDimencionesDto.class, params)> -1L){
											if(DaoFactory.getInstance().deleteAll(sesion, TcManticImagenesDto.class, params)> -1L){
												regresar= DaoFactory.getInstance().delete(sesion, TcManticArticulosDto.class, this.articulo.getIdArticulo())>= 1L;
				}	}	}	}	}	}	}	}	} // if		
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // eliminarArticulo
	
	private boolean procesarArticulo(Session sesion) throws Exception{
		boolean regresar= false;
		Long idArticulo = -1L;
		Long idImagen   = -1L;
		try {
			this.messageError= "Error al registrar el articulo";
			if(eliminarRegistros(sesion)){
				this.articulo.getArticulo().setIdRedondear(this.articulo.isRedondear() ? ACTIVO : INACTIVO);
				this.articulo.getArticulo().setIdUsuario(JsfBase.getIdUsuario());
				this.articulo.getArticulo().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
				this.articulo.getArticulo().setIdVigente(1L);
				this.articulo.getArticulo().setStock(0L);
				idArticulo= DaoFactory.getInstance().insert(sesion, this.articulo.getArticulo());
				if(registraCodigos(sesion, idArticulo)){
					if(registraEspecificaciones(sesion, idArticulo)){
						if(registraDescuentos(sesion, idArticulo)){
							if(registraClientesDescuentos(sesion, idArticulo)){
								if(registraPreciosSugeridos(sesion, idArticulo)){
									if(registraArticulosProveedor(sesion, idArticulo)){
										if(registraArticulosTipoVenta(sesion, idArticulo)){
											this.articulo.getArticuloDimencion().setIdArticulo(idArticulo);											
											if(DaoFactory.getInstance().insert(sesion, this.articulo.getArticuloDimencion()) >= 1L){												
												idImagen= DaoFactory.getInstance().insert(sesion, loadImage());
												this.articulo.getArticulo().setIdImagen(idImagen);
												regresar= DaoFactory.getInstance().update(sesion, this.articulo.getArticulo())>= 1L;
			} } } } } } } } } // if
		} // try
		catch (Exception e) {						
			throw e;
		} // catch				
		return regresar;
	} // procesarArticulo
	
	private boolean actualizarArticulo(Session sesion) throws Exception{
		boolean regresar= false;
		Long idArticulo = -1L;
		try {
			idArticulo= this.articulo.getIdArticulo();
			if(registraCodigos(sesion, idArticulo)){
					if(registraEspecificaciones(sesion, idArticulo)){
						if(registraDescuentos(sesion, idArticulo)){
							if(registraClientesDescuentos(sesion, idArticulo)){
								if(registraPreciosSugeridos(sesion, idArticulo)){
									if(registraArticulosProveedor(sesion, idArticulo)){
										if(registraArticulosTipoVenta(sesion, idArticulo)){
											if(DaoFactory.getInstance().update(sesion, this.articulo.getArticuloDimencion()) >= 1L){												
												if(DaoFactory.getInstance().update(sesion, loadImage())>= 1L)
													regresar= DaoFactory.getInstance().update(sesion, this.articulo.getArticulo())>= 1L;
			} } } } } } } }
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizarArticulo	
	
	private TcManticImagenesDto loadImage(){
		TcManticImagenesDto regresar= null;
		Long tipoImagen             = null;
		String name                 = null;
		try {
			regresar= new TcManticImagenesDto();
			name= this.articulo.getImportado().getName();
			tipoImagen= ETipoImagen.valueOf(name.substring(name.lastIndexOf(".")+1, name.length()).toUpperCase()).getIdTipoImagen();
			regresar.setNombre(name);
			regresar.setIdTipoImagen(tipoImagen);
			regresar.setIdUsuario(JsfBase.getIdUsuario());
			regresar.setArchivo(name);
			regresar.setTamanio(this.articulo.getImportado().getFileSize());
			regresar.setRuta(this.articulo.getImportado().getRuta());			
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar imagen";
		} // finally
		return regresar;
	} // loadImage
	
	private boolean eliminarRegistros(Session sesion) throws Exception{
		boolean regresar= true;
		int count       = 0;
		try {
			for(IBaseDto dto: this.articulo.getDeleteList()){
				if(DaoFactory.getInstance().delete(sesion, dto)>= 1L)
					count++;
			} // for
			regresar= count== this.articulo.getDeleteList().size();
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al eliminar registros";
		} // finally
		return regresar;
	} // eliminarRegistros
	
	private boolean registraCodigos(Session sesion, Long idArticulo) throws Exception{
		TcManticArticulosCodigosDto dto= null;
		ESql sqlAccion                 = null;		
		int count                      = 0;
		boolean validate               = false;
		boolean regresar               = false;
		try {
			for(ArticuloCodigo codigo: this.articulo.getArticulosCodigos()){
				codigo.setIdArticulo(idArticulo);				
				codigo.setIdUsuario(JsfBase.getIdUsuario());
				codigo.setObservaciones(this.articulo.getObservaciones());
				codigo.setIdPrincipal(count==0 ? 1L : 0L);
				codigo.setOrden(count + 1L);
				dto= (TcManticArticulosCodigosDto) codigo;				
				sqlAccion= codigo.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdArticuloCodigo(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.articulo.getArticulosCodigos().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar codigo, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraCodigos
	
	private boolean registraEspecificaciones(Session sesion, Long idArticulo) throws Exception{
		TcManticArticulosEspecificacionesDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(Especificacion especificacion: this.articulo.getEspecificaciones()){
				especificacion.setIdArticulo(idArticulo);				
				especificacion.setIdUsuario(JsfBase.getIdUsuario());
				dto= (TcManticArticulosEspecificacionesDto) especificacion;				
				sqlAccion= especificacion.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdArticuloEspecificacion(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.articulo.getEspecificaciones().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar especificaciones, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraEspecificaciones
	
	private boolean registraDescuentos(Session sesion, Long idArticulo) throws Exception{
		TcManticArticulosDescuentosDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(Descuento descuento: this.articulo.getArticulosDescuentos()){
				descuento.setIdArticulo(idArticulo);				
				descuento.setIdUsuario(JsfBase.getIdUsuario());
				descuento.setVigenciaInicial(new Timestamp(descuento.getVigenciaIni().getTime()));
				descuento.setVigenciaFinal(new Timestamp(descuento.getVigenciaFin().getTime()));
				dto= (TcManticArticulosDescuentosDto) descuento;				
				sqlAccion= descuento.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdArticuloDescuento(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.articulo.getArticulosDescuentos().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar descuentos, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraDescuentos
	
	private boolean registraClientesDescuentos(Session sesion, Long idArticulo) throws Exception{
		TrManticArticuloGrupoDescuentoDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(DescuentoEspecial descuentoEspecial: this.articulo.getClientesDescuentos()){
				descuentoEspecial.setIdArticulo(idArticulo);				
				descuentoEspecial.setIdUsuario(JsfBase.getIdUsuario());
				descuentoEspecial.setVigenciaInicial(new Timestamp(descuentoEspecial.getVigenciaIni().getTime()));
				descuentoEspecial.setVigenciaFinal(new Timestamp(descuentoEspecial.getVigenciaFin().getTime()));
				dto= (TrManticArticuloGrupoDescuentoDto) descuentoEspecial;				
				sqlAccion= descuentoEspecial.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdArticuloGrupoDescuento(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.articulo.getClientesDescuentos().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar descuentos para clientes, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraClientesDescuentos
	
	private boolean registraPreciosSugeridos(Session sesion, Long idArticulo) throws Exception{
		TrManticArticuloPrecioSugeridoDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(PrecioSugerido precioSugerido: this.articulo.getPreciosSugeridos()){
				precioSugerido.setIdArticulo(idArticulo);				
				precioSugerido.setIdUsuario(JsfBase.getIdUsuario());				
				dto= (TrManticArticuloPrecioSugeridoDto) precioSugerido;				
				sqlAccion= precioSugerido.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdArticuloPrecioSugerido(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.articulo.getPreciosSugeridos().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar precios sugeridos, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraPreciosSugeridos
	
	private boolean registraArticulosProveedor(Session sesion, Long idArticulo) throws Exception{
		TrManticArticuloProveedorDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(ArticuloProveedor articuloProveedor: this.articulo.getArticulosProveedores()){
				articuloProveedor.setIdArticulo(idArticulo);				
				articuloProveedor.setIdUsuario(JsfBase.getIdUsuario());				
				dto= (TrManticArticuloProveedorDto) articuloProveedor;				
				sqlAccion= articuloProveedor.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdArticuloProveedor(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.articulo.getArticulosProveedores().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar articulos de proveedor, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraArticulosProveedor
	
	private boolean registraArticulosTipoVenta(Session sesion, Long idArticulo) throws Exception{
		TrManticArticuloTipoVentaDto dto= null;
		ESql sqlAccion  = null;		
		int count       = 0;
		boolean validate= false;
		boolean regresar= false;
		try {
			for(TipoVenta tipoVenta: this.articulo.getArticulosTiposVenta()){
				tipoVenta.setIdArticulo(idArticulo);				
				dto= (TrManticArticuloTipoVentaDto) tipoVenta;				
				sqlAccion= tipoVenta.getSqlAccion();
				switch(sqlAccion){
					case INSERT:
						dto.setIdArticuloTipoVenta(-1L);
						validate= registrar(sesion, dto);
						break;
					case UPDATE:
						validate= actualizar(sesion, dto);
						break;
				} // switch
				if(validate)
					count++;
			} // for		
			regresar= count== this.articulo.getArticulosTiposVenta().size();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			this.messageError= "Error al registrar tipos de ventas, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // registraArticulosTipoVenta
	
	private boolean registrar(Session sesion, IBaseDto dto) throws Exception{
		return DaoFactory.getInstance().insert(sesion, dto) >= 1L;
	} // registrar
	
	private boolean actualizar(Session sesion, IBaseDto dto) throws Exception{
		return DaoFactory.getInstance().update(sesion, dto) >= 1L;
	} // actualizar
}
