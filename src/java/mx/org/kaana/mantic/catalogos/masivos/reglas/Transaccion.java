package mx.org.kaana.mantic.catalogos.masivos.reglas;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticMasivasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticMasivasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticMasivasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.db.dto.TcManticTrabajosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorTipoContactoDto;
import static org.apache.commons.io.Charsets.ISO_8859_1;
import static org.apache.commons.io.Charsets.UTF_8;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {
  
  private static final Log LOG = LogFactory.getLog(Transaccion.class);
  private TcManticMasivasArchivosDto masivo;	
	private ECargaMasiva categoria;
	private int errores;
	private String messageError;
  
  public Transaccion(TcManticMasivasArchivosDto masivo, ECargaMasiva categoria) {
		this.masivo   = masivo;		
		this.categoria= categoria;
		this.errores  = 0;
	} // Transaccion

	protected void setMessageError(String messageError) {
		this.messageError=messageError;
	}

	public String getMessageError() {
		return messageError;
	}

	public int getErrores() {
		return errores;
	}
  
	@Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception{
    boolean regresar= false;
    try {
      this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" catalogo de forma masiva.");
      switch (accion) {
				case PROCESAR: 
 					regresar= this.toProcess(sesion);
					break;
				case ELIMINAR: 
					// regresar = DaoFactory.getInstance().delete(sesion, this.masivo)> -1L;
					break;
      } // swtich 
      if (!regresar) {
        throw new Exception(messageError);
      } // if
    } // tyr
		catch (Exception e) {
      throw new Exception(messageError.concat("\n\n") + e.getMessage());
    } // catch
    return regresar;
  }
  
	protected boolean toProcess(Session sesion) throws Exception {
		boolean regresar= false;  
		File file= new File(this.masivo.getAlias());
		if(file.exists()) {
			DaoFactory.getInstance().updateAll(sesion, TcManticMasivasArchivosDto.class, this.masivo.toMap());
		  DaoFactory.getInstance().insert(sesion, this.masivo);
			TcManticMasivasBitacoraDto bitacora= new TcManticMasivasBitacoraDto(
				"", // String justificacion, 
				this.masivo.getIdMasivaArchivo(), // Long idMasivaArchivo, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				-1L, // Long idMasivaBitacora, 
				0L, // Long procesados, 
				1L // Long idMasivaEstatus
			);
		  DaoFactory.getInstance().insert(sesion, bitacora);
			this.toDeleteXls();
			Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
			monitoreo.comenzar(0L);
			monitoreo.setTotal(this.masivo.getTuplas());
			switch (this.categoria) {
				case ARTICULOS:
    			this.toArticulos(sesion, file);
					break;
				case CLIENTES:
					this.toClientes(sesion, file);
					break;
				case PROVEEDORES:
					this.toProveedores(sesion, file);
					break;
				case REFACCIONES:
					this.toRefacciones(sesion, file);
					break;
			} // swtich
      monitoreo.terminar();
			monitoreo.setProgreso(0L);
			bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, this.masivo.getTuplas(), 3L);
		  DaoFactory.getInstance().insert(sesion, bitacora);
			regresar= true;
		} // if	
		else
			LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ this.masivo.getNombre());
    return regresar;
	}
	
	private Long toFindEntidad(Session sesion, String entidad) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			String codigo= entidad.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim();
			params.put("descripcion", codigo.replaceAll("(,| |\\t)+", ".*.*"));
			Value value= DaoFactory.getInstance().toField(sesion, "TcJanalEntidadesDto", "entidad", params, "idEntidad");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField(sesion, "TcJanalEntidadesDto", "primero", params, "idEntidad");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

	private Long toFindMunicipio(Session sesion, Long idEntidad, String municipio) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idEntidad", idEntidad);
			params.put("descripcion", municipio!= null? municipio.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*"): "XYZ");
			Value value= DaoFactory.getInstance().toField(sesion, "TcJanalMunicipiosDto", "municipio", params, "idMunicipio");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField(sesion, "TcJanalMunicipiosDto", "primero", params, "idMunicipio");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private Long toFindLocalidad(Session sesion, Long idMunicipio, String localidad) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idMunicipio", idMunicipio);
			params.put("descripcion", localidad!= null? localidad.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*"): "XYZ");
			Value value= DaoFactory.getInstance().toField(sesion, "TcJanalLocalidadesDto", "localidad", params, "idLocalidad");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField(sesion, "TcJanalLocalidadesDto", "primero", params, "idLocalidad");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private Long toFindUsoCFDI(Session sesion, String clave) {
		Long regresar= 3L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("clave", clave.toUpperCase());
			Value value= DaoFactory.getInstance().toField(sesion, "VistaCargasMasivasDto", "usoCfdi", params, "idUsoCfdi");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private TrManticClienteTipoContactoDto toFindTipoContactoCliente(Session sesion, Long idTipoContacto, Long idCliente) {
		TrManticClienteTipoContactoDto regresar= null;
		Map<String, Object> params  = null;
		try {
			// 1 telefono, 9 correo, 10 correo personal
			params=new HashMap<>();
			params.put("idTipoContacto", idTipoContacto);
			params.put("idCliente", idCliente);
			regresar= (TrManticClienteTipoContactoDto)DaoFactory.getInstance().toEntity(sesion, TrManticClienteTipoContactoDto.class, "VistaCargasMasivasDto", "contactoc", params);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private TrManticProveedorTipoContactoDto toFindTipoContactoProveedor(Session sesion, Long idTipoContacto, Long idProveedor) {
		TrManticProveedorTipoContactoDto regresar= null;
		Map<String, Object> params  = null;
		try {
			// 1 telefono, 9 correo, 10 correo personal
			params=new HashMap<>();
			params.put("idTipoContacto", idTipoContacto);
			params.put("idProveedor", idProveedor);
			regresar= (TrManticProveedorTipoContactoDto)DaoFactory.getInstance().toEntity(sesion, TrManticProveedorTipoContactoDto.class, "VistaCargasMasivasDto", "contactop", params);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private TcManticClientesDto toFindCliente(Session sesion, String rfc) {
		TcManticClientesDto regresar= null;
		Map<String, Object> params  = null;
		try {
			params=new HashMap<>();
			params.put("rfc", rfc);
			regresar= (TcManticClientesDto)DaoFactory.getInstance().toEntity(sesion, TcManticClientesDto.class, "VistaCargasMasivasDto", "cliente", params);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

	private TcManticProveedoresDto toFindProveedor(Session sesion, String rfc) {
		TcManticProveedoresDto regresar= null;
		Map<String, Object> params  = null;
		try {
			params=new HashMap<>();
			params.put("rfc", rfc);
			regresar= (TcManticProveedoresDto)DaoFactory.getInstance().toEntity(sesion, TcManticProveedoresDto.class, "VistaCargasMasivasDto", "proveedor", params);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

	private Long toFindUnidadMedida(Session sesion, String codigo) {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("codigo", codigo);
			Value value= DaoFactory.getInstance().toField(sesion, "VistaCargasMasivasDto", "empaque", params, "idEmpaqueUnidadMedida");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private Long toFindCodigoAuxiliar(Session sesion, String codigo) {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("codigo", codigo);
			Value value= DaoFactory.getInstance().toField(sesion, "VistaCargasMasivasDto", "auxiliar", params, "idArticuloCodigo");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private TcManticArticulosDto toFindArticulo(Session sesion, String codigo) {
		TcManticArticulosDto regresar= null;
		Map<String, Object> params   = null;
		try {
			params=new HashMap<>();
			params.put("codigo", codigo);
			regresar= (TcManticArticulosDto)DaoFactory.getInstance().toEntity(sesion, TcManticArticulosDto.class, "VistaCargasMasivasDto", "articulo", params);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

  private Boolean toArticulos(Session sesion, File archivo) throws Exception {
		Boolean regresar	      = false;
		Workbook workbook	      = null;
		Sheet sheet             = null;
		TcManticArticulosCodigosDto codigos= null;
		TcManticMasivasBitacoraDto bitacora= null;
		try {
      WorkbookSettings workbookSettings = new WorkbookSettings();
      workbookSettings.setEncoding("Cp1252");	
			workbookSettings.setExcelDisplayLanguage("MX");
      workbookSettings.setExcelRegionalSettings("MX");
      workbookSettings.setLocale(new Locale("es", "MX"));
			workbook= Workbook.getWorkbook(archivo, workbookSettings);
			sheet		= workbook.getSheet(0);
			if(sheet != null && sheet.getColumns()>= this.categoria.getColumns() && sheet.getRows()>= 2) {
				//LOG.info("<-------------------------------------------------------------------------------------------------------------->");
				LOG.info("Filas del documento: "+ sheet.getRows());
				this.errores= 0;
				for(int fila= 1; fila< sheet.getRows(); fila++) {
					if(sheet.getCell(0, fila)!= null && sheet.getCell(2, fila)!= null && !sheet.getCell(0, fila).getContents().toUpperCase().startsWith("NOTA") && !Cadena.isVacio(sheet.getCell(0, fila).getContents()) && !Cadena.isVacio(sheet.getCell(2, fila).getContents())) {
						String contenido= new String(sheet.getCell(2, fila).getContents().toUpperCase().getBytes(UTF_8), ISO_8859_1);
						// 0           1          2        3          4          5          6           7        8        9            10               11          12
						//CODIGO|CODIGOAUXILIAR|NOMBRE|COSTOS/IVA|MENUDEONETO|MEDIONETO|MAYOREONETO|UNIDADMEDIDA|IVA|LIMITEMENUDEO|LIMITEMAYOREO|STOCKMINIMO|STOCKMAXIMO
						double costo   = Numero.getDouble(sheet.getCell(3, fila).getContents()!= null? sheet.getCell(3, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double menudeo = Numero.getDouble(sheet.getCell(4, fila).getContents()!= null? sheet.getCell(4, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double medio   = Numero.getDouble(sheet.getCell(5, fila).getContents()!= null? sheet.getCell(5, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double mayoreo = Numero.getDouble(sheet.getCell(6, fila).getContents()!= null? sheet.getCell(6, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double iva     = Numero.getDouble(sheet.getCell(8, fila).getContents()!= null? sheet.getCell(8, fila).getContents().replaceAll("[$, ]", ""): "0", 16D);
						double lmenudeo= Numero.getDouble(sheet.getCell(9, fila).getContents()!= null? sheet.getCell(9, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double lmayoreo= Numero.getDouble(sheet.getCell(10, fila).getContents()!= null? sheet.getCell(10, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double minimo  = Numero.getDouble(sheet.getCell(11, fila).getContents()!= null? sheet.getCell(11, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double maximo  = Numero.getDouble(sheet.getCell(12, fila).getContents()!= null? sheet.getCell(12, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						String nombre= new String(contenido.getBytes(ISO_8859_1), UTF_8);
						if(costo> 0 && menudeo> 0 && medio> 0 && mayoreo> 0) {
							String codigo= new String(sheet.getCell(0, fila).getContents().toUpperCase().getBytes(UTF_8), ISO_8859_1);
							TcManticArticulosDto articulo= this.toFindArticulo(sesion, codigo);
							if(articulo!= null) {
								articulo.setIdCategoria(null);
								articulo.setIdImagen(null);
								articulo.setPrecio(costo);
								articulo.setMenudeo(menudeo);
								articulo.setMedioMayoreo(medio);
								articulo.setMayoreo(mayoreo);
								// si trae nulo, blanco o cero se respeta el valor que tiene el campo
								if(lmenudeo!= 0D)
									articulo.setLimiteMedioMayoreo(lmenudeo);
								if(lmenudeo!= 0D)
									articulo.setLimiteMayoreo(lmayoreo);
								if(minimo!= 0D)
									articulo.setMinimo(minimo);
								if(maximo!= 0D)
									articulo.setMaximo(maximo);
								if(iva!= 0D)
									articulo.setIva(iva< 1? iva* 100: iva);
								if(!Cadena.isVacio(sheet.getCell(7, fila).getContents()))
									articulo.setIdEmpaqueUnidadMedida(this.toFindUnidadMedida(sesion, sheet.getCell(7, fila).getContents()));
								DaoFactory.getInstance().update(sesion, articulo);
							} // if
							else {
								articulo= new TcManticArticulosDto(
									nombre, // String descripcion, 
									"0", // String descuentos, 
									null, // Long idImagen, 
									null, // Long idCategoria, 
									"0", // String extras, 
									null, // String metaTag, 
									nombre, // String nombre, 
									costo, // Double precio, 
									iva, // Double iva, 
									mayoreo, // Double mayoreo, 
									2D, // Double desperdicio, 
									null, // String metaTagDescipcion, 
									1L, // Long idVigente, 
									-1L, // Long idArticulo, 
									0D, // Double stock, 
									medio, // Double medioMayoreo, 
									0D, // Double pesoEstimado, 
									this.toFindUnidadMedida(sesion, sheet.getCell(7, fila).getContents()), // Long idEmpaqueUnidadMedida, 
									1L, // Long idRedondear, 
									menudeo, // Double menudeo, 
									null, // String metaTagTeclado, 
									new Timestamp(Calendar.getInstance().getTimeInMillis()), // Timestamp fecha, 
									JsfBase.getIdUsuario(), //  Long idUsuario, 
									JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), // Long idEmpresa, 
									0D, // Double cantidad, 
									minimo== 0D? 10D: minimo, // Double minimo, 
									maximo== 0D? 20D: maximo, // Double maximo, 
									lmenudeo== 0D? 20D: lmenudeo, // Double limiteMedioMayoreo, 
									lmayoreo== 0D? 50D: lmayoreo, // Double limiteMayoreo, 
									Constantes.CODIGO_SAT, // String sat, 
									2L, // Long idServicio, 
									2L, // Long idBarras, 
									"0", // String descuento, 
									"0", // String extra, 
									null // String idFacturama
								);
								DaoFactory.getInstance().insert(sesion, articulo);
								// insertar el codigo principal del articulo
								codigos= new TcManticArticulosCodigosDto(
									codigo, // String codigo, 
									null, // Long idProveedor, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									1L, // Long idPrincipal, 
									null, // String observaciones, 
									-1L, // Long idArticuloCodigo, 
									1L, // Long orden, 
									articulo.getIdArticulo() // Long idArticulo
								);
								DaoFactory.getInstance().insert(sesion, codigos);
								TcManticMasivasDetallesDto detalle= new TcManticMasivasDetallesDto(
									sheet.getCell(0, fila).getContents(), // String codigo, 
									-1L, // Long idMasivaDetalle, 
									this.masivo.getIdMasivaArchivo(), // Long idMasivaArchivo, 
									"ESTE ARTICULO FUE AGREGADO ["+ sheet.getCell(2, fila).getContents()+ "]" // String observaciones
								);
								DaoFactory.getInstance().insert(sesion, detalle);
								// aqui va el codigo para que se registre en facturama el articulo
								// **
								// **
							} // if
							// buscar si el codigo auxiliar existe para este articulo, en caso de que no insertarlo
							codigo= new String(sheet.getCell(1, fila).getContents().getBytes(UTF_8), ISO_8859_1);
							Long auxiliar= this.toFindCodigoAuxiliar(sesion, codigo);
							if(auxiliar< 0) {
								codigos= new TcManticArticulosCodigosDto(
									codigo, // String codigo, 
									null, // Long idProveedor, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									2L, // Long idPrincipal, 
									null, // String observaciones, 
									-1L, // Long idArticuloCodigo, 
									2L, // Long orden, 
									articulo.getIdArticulo() // Long idArticulo
								);
								DaoFactory.getInstance().insert(sesion, codigos);
							} // if
							JsfBase.getAutentifica().getMonitoreo().incrementar();
							if(fila% this.categoria.getTuplas()== 0) {
								if(bitacora== null) {
								  bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, new Long(fila), 2L);
  								DaoFactory.getInstance().insert(sesion, bitacora);
							  } // if
								else {
									bitacora.setProcesados(new Long(fila));
									bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  								DaoFactory.getInstance().update(sesion, bitacora);
								} // else
								this.commit();
							} // if
						} // if
						else {
							this.errores++;
							LOG.warn(fila+ ": ["+ nombre+ "] costo: ["+ costo+ "] menudeo: ["+ menudeo+ "] menudeo: ["+ medio+ "] menudeo: ["+ mayoreo+ "]");
							TcManticMasivasDetallesDto detalle= new TcManticMasivasDetallesDto(
								sheet.getCell(0, fila).getContents(), // String codigo, 
								-1L, // Long idMasivaDetalle, 
								this.masivo.getIdMasivaArchivo(), // Long idMasivaArchivo, 
								"EL COSTO["+ costo+ "], MENUDEO["+ menudeo+ "], MEDIO MAYOREO["+ medio+ "], MAYOREO["+ mayoreo+ "] ESTAN EN CEROS" // String observaciones
							);
							DaoFactory.getInstance().insert(sesion, detalle);
						} // else	
					} // if	
//					if(fila> 4)
//						throw new KajoolBaseException("Este error fue provocado intencionalmente !");
				} // for
				if(bitacora== null) {
					bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, this.masivo.getTuplas(), 2L);
  				DaoFactory.getInstance().insert(sesion, bitacora);
				} // if
			  else {
					bitacora.setProcesados(this.masivo.getTuplas());
					bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					DaoFactory.getInstance().update(sesion, bitacora);
				} // if
				LOG.warn("Cantidad de filas con error son: "+ this.errores);
				regresar = true;
			} // if
		} // try
		catch (IOException | BiffException e) {
			throw e;
		} // catch
    finally {
      if(workbook!= null) {
        workbook.close();
        workbook = null;
      } // if
    } // finally
		return regresar;
	} // toArticulos		

	private TcManticTrabajosDto toFindTrabajo(Session sesion, String codigo) {
		TcManticTrabajosDto regresar= null;
		Map<String, Object> params  = null;
		try {
			params=new HashMap<>();
			params.put("codigo", codigo);
			regresar= (TcManticTrabajosDto)DaoFactory.getInstance().toEntity(sesion, TcManticTrabajosDto.class, "VistaCargasMasivasDto", "trabajo", params);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
  private Boolean toRefacciones(Session sesion, File archivo) throws Exception {
		Boolean regresar= false;
		Workbook workbook= null;
		Sheet sheet      = null;
		TcManticMasivasBitacoraDto bitacora= null;
		try {
      WorkbookSettings workbookSettings = new WorkbookSettings();
      workbookSettings.setEncoding("Cp1252");	
			workbookSettings.setExcelDisplayLanguage("MX");
      workbookSettings.setExcelRegionalSettings("MX");
      workbookSettings.setLocale(new Locale("es", "MX"));
			workbook= Workbook.getWorkbook(archivo, workbookSettings);
			sheet		= workbook.getSheet(0);
			if(sheet != null && sheet.getColumns()>= this.categoria.getColumns() && sheet.getRows()>= 2) {
				//LOG.info("<-------------------------------------------------------------------------------------------------------------->");
				LOG.info("Filas del documento: "+ sheet.getRows());
				this.errores= 0;
				for(int fila= 1; fila< sheet.getRows(); fila++) {
					if(sheet.getCell(0, fila)!= null && sheet.getCell(2, fila)!= null && !sheet.getCell(0, fila).getContents().toUpperCase().startsWith("NOTA") && !Cadena.isVacio(sheet.getCell(0, fila).getContents()) && !Cadena.isVacio(sheet.getCell(2, fila).getContents())) {
						String contenido= new String(sheet.getCell(2, fila).getContents().toUpperCase().getBytes(UTF_8), ISO_8859_1);
						// 0           1          2        3          4        5
						//CODIGO|CODIGOAUXILIAR|NOMBRE|HERRAMIENTA|COSTOS/IVA|IVA
						double costo   = Numero.getDouble(sheet.getCell(3, fila).getContents()!= null? sheet.getCell(3, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double iva     = Numero.getDouble(sheet.getCell(5, fila).getContents()!= null? sheet.getCell(5, fila).getContents().replaceAll("[$, ]", ""): "0", 16D);
						String nombre= new String(contenido.trim().getBytes(ISO_8859_1), UTF_8);
						if(costo> 0) {
							String codigo= new String(sheet.getCell(0, fila).getContents().toUpperCase().getBytes(UTF_8), ISO_8859_1);
							TcManticTrabajosDto trabajo= this.toFindTrabajo(sesion, codigo);
							if(trabajo!= null) {
								trabajo.setPrecio(costo);
								// si trae nulo, blanco o cero se respeta el valor que tiene el campo
								if(iva!= 0D)
									trabajo.setIva(iva< 1? iva* 100: iva);
								if(!Cadena.isVacio(sheet.getCell(3, fila).getContents()))
									trabajo.setHerramienta(Cadena.isVacio(sheet.getCell(3, fila).getContents())? "": sheet.getCell(3, fila).getContents().toUpperCase().trim());
								DaoFactory.getInstance().update(sesion, trabajo);
							} // if
							else {
								trabajo= new TcManticTrabajosDto(
									nombre, // String descripcion, 
									codigo, // String codigo, 
									costo, // Double precio, 
									iva, // Double iva, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									-1L, // Long idTrabajo, 
									JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), // Long idEmpresa, 
									1L, // Long idVigente, 
									nombre, // String nombre, 
									"", // String sat, 
									Cadena.isVacio(sheet.getCell(3, fila).getContents())? "": sheet.getCell(3, fila).getContents().toUpperCase().trim() // String herramienta
								);
								DaoFactory.getInstance().insert(sesion, trabajo);
								TcManticMasivasDetallesDto detalle= new TcManticMasivasDetallesDto(
									sheet.getCell(0, fila).getContents(), // String codigo, 
									-1L, // Long idMasivaDetalle, 
									this.masivo.getIdMasivaArchivo(), // Long idMasivaArchivo, 
									"ESTE ARTICULO FUE AGREGADO ["+ sheet.getCell(2, fila).getContents()+ "]" // String observaciones
								);
								DaoFactory.getInstance().insert(sesion, detalle);
								// aqui va el codigo para que se registre en facturama el articulo
								// **
								// **
							} // if
							JsfBase.getAutentifica().getMonitoreo().incrementar();
							if(fila% this.categoria.getTuplas()== 0) {
								if(bitacora== null) {
								  bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, new Long(fila), 2L);
  								DaoFactory.getInstance().insert(sesion, bitacora);
							  } // if
								else {
									bitacora.setProcesados(new Long(fila));
									bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  								DaoFactory.getInstance().update(sesion, bitacora);
								} // else
								this.commit();
							} // if
						} // if
						else {
							this.errores++;
							LOG.warn(fila+ ": ["+ nombre+ "] costo: ["+ costo+ "] ");
							TcManticMasivasDetallesDto detalle= new TcManticMasivasDetallesDto(
								sheet.getCell(0, fila).getContents(), // String codigo, 
								-1L, // Long idMasivaDetalle, 
								this.masivo.getIdMasivaArchivo(), // Long idMasivaArchivo, 
								"EL COSTO["+ costo+ "], ESTAN EN CEROS" // String observaciones
							);
							DaoFactory.getInstance().insert(sesion, detalle);
						} // else	
					} // if	
				} // for
				if(bitacora== null) {
					bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, this.masivo.getTuplas(), 2L);
  				DaoFactory.getInstance().insert(sesion, bitacora);
				} // if
			  else {
					bitacora.setProcesados(this.masivo.getTuplas());
					bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					DaoFactory.getInstance().update(sesion, bitacora);
				} // if
				LOG.warn("Cantidad de filas con error son: "+ this.errores);
				regresar = true;
			} // if
		} // try
		catch (IOException | BiffException e) {
			throw e;
		} // catch
    finally {
      if(workbook!= null) {
        workbook.close();
        workbook = null;
      } // if
    } // finally
		return regresar;
	} // toRefacciones		
	
	private void toProcessContactoCliente(Session sesion, String valor, Long idTipoContacto, Long idCliente) throws Exception {
		TrManticClienteTipoContactoDto contacto= this.toFindTipoContactoCliente(sesion, idTipoContacto, idCliente);
		if(contacto== null) {
      contacto= new TrManticClienteTipoContactoDto(
				idCliente, // Long idCliente, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				valor.trim(), // String valor, 
				"", // String observaciones, 
				-1L, // Long idClienteTipoContacto, 
				1L, // Long orden, 
				idTipoContacto // Long idTipoContacto
			);
			DaoFactory.getInstance().insert(sesion, contacto);
		} // if
		else {
			contacto.setValor(valor);
			DaoFactory.getInstance().update(sesion, contacto);
		} // else
	}

  private Boolean toClientes(Session sesion, File archivo) throws Exception {	
		Boolean regresar = false;
		Workbook workbook= null;
		Sheet sheet      = null;
		TcManticMasivasBitacoraDto bitacora= null;
		try {
      WorkbookSettings workbookSettings = new WorkbookSettings();
      workbookSettings.setEncoding("Cp1252");	
			workbookSettings.setExcelDisplayLanguage("MX");
      workbookSettings.setExcelRegionalSettings("MX");
      workbookSettings.setLocale(new Locale("es", "MX"));
			workbook= Workbook.getWorkbook(archivo, workbookSettings);
			sheet		= workbook.getSheet(0);
			if(sheet != null && sheet.getColumns()>= this.categoria.getColumns() && sheet.getRows()>= 2) {
				//LOG.info("<-------------------------------------------------------------------------------------------------------------->");
				LOG.info("Filas del documento: "+ sheet.getRows());
				this.errores= 0;
				for(int fila= 1; fila< sheet.getRows(); fila++) {
					if(sheet.getCell(0, fila)!= null && sheet.getCell(2, fila)!= null && !sheet.getCell(0, fila).getContents().toUpperCase().startsWith("NOTA") && !Cadena.isVacio(sheet.getCell(0, fila).getContents()) && !Cadena.isVacio(sheet.getCell(2, fila).getContents())) {
						String contenido= new String(sheet.getCell(2, fila).getContents().getBytes(UTF_8), ISO_8859_1);
						// 0    1       2          3       4       5       6       7        8         9     10     11       12
						//RFC|CLAVE|RAZONSOCIAL|USOCFDI|TELEFONO|CORREO1|CORREO2|ENTIDAD|MUNICIPIO|COLONIA|CALLE|NUMERO|CODIGOPOSTAL
						String nombre= new String(contenido.toUpperCase().getBytes(ISO_8859_1), UTF_8);
						if(!Cadena.isVacio(sheet.getCell(5, fila).getContents()) && !Cadena.isVacio(sheet.getCell(7, fila).getContents()) && !Cadena.isVacio(sheet.getCell(8, fila).getContents())) {
							String rfc= new String(sheet.getCell(0, fila).getContents().toUpperCase().getBytes(UTF_8), ISO_8859_1);
							TcManticClientesDto cliente= this.toFindCliente(sesion, rfc);
							if(cliente!= null) {
								cliente.setRazonSocial(nombre);
								// si trae nulo, blanco o cero se respeta el valor que tiene el campo
								if(!Cadena.isVacio(sheet.getCell(1, fila).getContents()))
									cliente.setClave(sheet.getCell(1, fila).getContents().trim());
								if(!Cadena.isVacio(sheet.getCell(3, fila).getContents()))
  								cliente.setIdUsoCfdi(this.toFindUsoCFDI(sesion, Cadena.isVacio(sheet.getCell(3, fila).getContents())? "XYZW": sheet.getCell(3, fila).getContents().trim()));
								DaoFactory.getInstance().update(sesion, cliente);
							} // if
							else {
								cliente= new TcManticClientesDto(
									Cadena.isVacio(sheet.getCell(1, fila).getContents())? null: sheet.getCell(1, fila).getContents().toUpperCase().trim(), // String clave, 
									0L, // Long plazoDias, 
									-1L, // Long idCliente, 
									0D, // Double limiteCredito, 
									2L, // Long idCredito, 
									nombre, // String razonSocial, 
									0D, // Double saldo, 
									rfc, // String rfc, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									this.toFindUsoCFDI(sesion, Cadena.isVacio(sheet.getCell(3, fila).getContents())? "XYZW": sheet.getCell(3, fila).getContents().trim()), // Long idUsoCfdi, 
									"", // String observaciones, 
									JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), // Long idEmpresa, 
									1L, // Long idTipoVenta, 
									null // String idFacturama
								);
								DaoFactory.getInstance().insert(sesion, cliente);
								
								Long idEntidad  = this.toFindEntidad(sesion, Cadena.isVacio(sheet.getCell(7, fila).getContents())? "XYZW": sheet.getCell(7, fila).getContents().toUpperCase().trim());
								Long idMunicipio= this.toFindMunicipio(sesion, idEntidad, Cadena.isVacio(sheet.getCell(8, fila).getContents())? "XYZW": sheet.getCell(8, fila).getContents().toUpperCase().trim());
								Long idLocalidad= this.toFindLocalidad(sesion, idMunicipio, "XYZW");
								TcManticDomiciliosDto domicilio= new TcManticDomiciliosDto(
									Cadena.isVacio(sheet.getCell(9, fila).getContents())? "COLONIA NO DEFINIDA": sheet.getCell(9, fila).getContents().toUpperCase().trim(), // String asentamiento, 
									idLocalidad, // Long idLocalidad, 
									Cadena.isVacio(sheet.getCell(12, fila).getContents())? "20000": sheet.getCell(12, fila).getContents().toUpperCase().trim(), // String codigoPostal, 
									null, // String latitud, 
									"", // String entreCalle, 
									Cadena.isVacio(sheet.getCell(10, fila).getContents())? "CALLE NO DEFINIDA": sheet.getCell(10, fila).getContents().toUpperCase().trim(), // String calle, 
									-1L, // Long idDomicilio, 
									Cadena.isVacio(sheet.getCell(12, fila).getContents())? "1": sheet.getCell(12, fila).getContents().toUpperCase().trim(), // String numeroInterior,  
									null, // String ycalle, 
									null, // String longitud, 
									null, // String numeroExterior, 
									JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, // Long idUsuario, 
									"" // String observaciones
								);
								DaoFactory.getInstance().insert(sesion, domicilio);
								TrManticClienteDomicilioDto particular= new TrManticClienteDomicilioDto(
									cliente.getIdCliente(), // Long idCliente, 
									-1L, // Long idClienteDomicilio, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									1L, // Long idTipoDomicilio, 
									domicilio.getIdDomicilio(), // Long idDomicilio, 
									1L, // Long idPrincipal, 
									"" // String observaciones
								);
								DaoFactory.getInstance().insert(sesion, particular);
								particular= new TrManticClienteDomicilioDto(
									cliente.getIdCliente(), // Long idCliente, 
									-1L, // Long idClienteDomicilio, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									2L, // Long idTipoDomicilio, 
									domicilio.getIdDomicilio(), // Long idDomicilio, 
									1L, // Long idPrincipal, 
									"" // String observaciones
								);
								DaoFactory.getInstance().insert(sesion, particular);
					      // aqui va el codigo para que se registre en facturama el cliente
								// **
								// **
							} // if
							// telefono
							if(!Cadena.isVacio(sheet.getCell(4, fila).getContents())) 
								this.toProcessContactoCliente(sesion, sheet.getCell(4, fila).getContents(), 1L, cliente.getIdCliente());
							// correos
							if(!Cadena.isVacio(sheet.getCell(5, fila).getContents())) 
								this.toProcessContactoCliente(sesion, sheet.getCell(5, fila).getContents(), 9L, cliente.getIdCliente());
							if(!Cadena.isVacio(sheet.getCell(6, fila).getContents())) 
								this.toProcessContactoCliente(sesion, sheet.getCell(6, fila).getContents(), 10L, cliente.getIdCliente());
							JsfBase.getAutentifica().getMonitoreo().incrementar();
							if(fila% this.categoria.getTuplas()== 0) {
								if(bitacora== null) {
								  bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, new Long(fila), 2L);
  								DaoFactory.getInstance().insert(sesion, bitacora);
							  } // if
								else {
									bitacora.setProcesados(new Long(fila));
									bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  								DaoFactory.getInstance().update(sesion, bitacora);
								} // else
								this.commit();
							} // if
						} // if
						else {
							this.errores++;
							LOG.warn(fila+ ": ["+ nombre+ "]  LOS DATOS DE ESTA FILA ESTAN EN BLANCO");
							TcManticMasivasDetallesDto detalle= new TcManticMasivasDetallesDto(
								sheet.getCell(0, fila).getContents(), // String rfc, 
								-1L, // Long idMasivaDetalle, 
								this.masivo.getIdMasivaArchivo(), // Long idMasivaArchivo, 
								"EL ["+ sheet.getCell(2, fila).getContents()+ "] ALGUNOS DE SUS DATOS ESTAN VACIOS, CORREO, ENTIDAD, MUNICIPIO" // String observaciones
							);
							DaoFactory.getInstance().insert(sesion, detalle);
						} // else	
					} // if	
				} // for
				if(bitacora== null) {
					bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, this.masivo.getTuplas(), 2L);
  				DaoFactory.getInstance().insert(sesion, bitacora);
				} // if
			  else {
					bitacora.setProcesados(this.masivo.getTuplas());
					bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					DaoFactory.getInstance().update(sesion, bitacora);
				} // if
				LOG.warn("Cantidad de filas con error son: "+ this.errores);
				regresar = true;
			} // if
		} // try
		catch (IOException | BiffException e) {
			throw e;
		} // catch
    finally {
      if(workbook!= null) {
        workbook.close();
        workbook = null;
      } // if
    } // finally
		return regresar;
	} // toClientes

	private void toProcessContactoProveedor(Session sesion, String valor, Long idTipoContacto, Long idProveedor) throws Exception {
		TrManticProveedorTipoContactoDto contacto= this.toFindTipoContactoProveedor(sesion, idTipoContacto, idProveedor);
		if(contacto== null) {
      contacto= new TrManticProveedorTipoContactoDto(
				idProveedor, // Long idProveedor, 
				JsfBase.getIdUsuario(), // Long idUsuario, 
				valor.trim(), // String valor, 
				"", // String observaciones, 
				1L, // Long orden, 
				-1L, // Long idProveedorTipoContacto, 
				idTipoContacto // Long idTipoContacto
			);
			DaoFactory.getInstance().insert(sesion, contacto);
		} // if
		else {
			contacto.setValor(valor);
			DaoFactory.getInstance().update(sesion, contacto);
		} // else
	}

	
  private Boolean toProveedores(Session sesion, File archivo) throws Exception {	
		Boolean regresar = false;
		Workbook workbook= null;
		Sheet sheet      = null;
		TcManticMasivasBitacoraDto bitacora= null;
		try {
      WorkbookSettings workbookSettings = new WorkbookSettings();
      workbookSettings.setEncoding("Cp1252");	
			workbookSettings.setExcelDisplayLanguage("MX");
      workbookSettings.setExcelRegionalSettings("MX");
      workbookSettings.setLocale(new Locale("es", "MX"));
			workbook= Workbook.getWorkbook(archivo, workbookSettings);
			sheet		= workbook.getSheet(0);
			if(sheet != null && sheet.getColumns()>= this.categoria.getColumns() && sheet.getRows()>= 2) {
				//LOG.info("<-------------------------------------------------------------------------------------------------------------->");
				LOG.info("Filas del documento: "+ sheet.getRows());
				this.errores= 0;
				for(int fila= 1; fila< sheet.getRows(); fila++) {
					if(sheet.getCell(0, fila)!= null && sheet.getCell(2, fila)!= null && !sheet.getCell(0, fila).getContents().toUpperCase().startsWith("NOTA") && !Cadena.isVacio(sheet.getCell(0, fila).getContents()) && !Cadena.isVacio(sheet.getCell(2, fila).getContents())) {
						String contenido= new String(sheet.getCell(2, fila).getContents().getBytes(UTF_8), ISO_8859_1);
						// 0    1       2          3       4       5       6        7        8       9     10      11
						//RFC|CLAVE|RAZONSOCIAL|TELEFONO|CORREO1|CORREO2|ENTIDAD|MUNICIPIO|COLONIA|CALLE|NUMERO|CODIGOPOSTAL
						String nombre= new String(contenido.toUpperCase().getBytes(ISO_8859_1), UTF_8);
						if(!Cadena.isVacio(sheet.getCell(4, fila).getContents()) && !Cadena.isVacio(sheet.getCell(6, fila).getContents()) && !Cadena.isVacio(sheet.getCell(7, fila).getContents())) {
							String rfc= new String(sheet.getCell(0, fila).getContents().toUpperCase().getBytes(UTF_8), ISO_8859_1);
							TcManticProveedoresDto proveedor= this.toFindProveedor(sesion, rfc);
							if(proveedor!= null) {
								proveedor.setRazonSocial(nombre);
								// si trae nulo, blanco o cero se respeta el valor que tiene el campo
								if(!Cadena.isVacio(sheet.getCell(1, fila).getContents()))
									proveedor.setClave(sheet.getCell(1, fila).getContents().trim());
								DaoFactory.getInstance().update(sesion, proveedor);
							} // if
							else {
								proveedor= new TcManticProveedoresDto(
									1L, // Long idTipoProveedor, 
									-1L, // Long idProveedor, 
									Cadena.isVacio(sheet.getCell(1, fila).getContents())? null: sheet.getCell(1, fila).getContents().toUpperCase().trim(), // String clave, 
									30L, // Long diasEntrega, 
									"0", // String descuento, 
									rfc.substring(0, 4), // String prefijo, 
									nombre, // String razonSocial, 
									rfc, // String rfc, 
									1L, // Long idTipoDia, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									1L, // Long idTipoMoneda, 
									"", // String observaciones, 
									JsfBase.getAutentifica().getEmpresa().getIdEmpresa() // Long idEmpresa, 
								);
								DaoFactory.getInstance().insert(sesion, proveedor);
								
								Long idEntidad  = this.toFindEntidad(sesion, Cadena.isVacio(sheet.getCell(7, fila).getContents())? "XYZW": sheet.getCell(7, fila).getContents().toUpperCase().trim());
								Long idMunicipio= this.toFindMunicipio(sesion, idEntidad, Cadena.isVacio(sheet.getCell(8, fila).getContents())? "XYZW": sheet.getCell(8, fila).getContents().toUpperCase().trim());
								Long idLocalidad= this.toFindLocalidad(sesion, idMunicipio, "XYZW");
								TcManticDomiciliosDto domicilio= new TcManticDomiciliosDto(
									Cadena.isVacio(sheet.getCell(9, fila).getContents())? "COLONIA NO DEFINIDA": sheet.getCell(9, fila).getContents().toUpperCase().trim(), // String asentamiento, 
									idLocalidad, // Long idLocalidad, 
									Cadena.isVacio(sheet.getCell(12, fila).getContents())? "20000": sheet.getCell(12, fila).getContents().toUpperCase().trim(), // String codigoPostal, 
									null, // String latitud, 
									"", // String entreCalle, 
									Cadena.isVacio(sheet.getCell(10, fila).getContents())? "CALLE NO DEFINIDA": sheet.getCell(10, fila).getContents().toUpperCase().trim(), // String calle, 
									-1L, // Long idDomicilio, 
									Cadena.isVacio(sheet.getCell(12, fila).getContents())? "1": sheet.getCell(12, fila).getContents().toUpperCase().trim(), // String numeroInterior,  
									null, // String ycalle, 
									null, // String longitud, 
									null, // String numeroExterior, 
									JsfBase.getAutentifica()!= null? JsfBase.getIdUsuario(): 1L, // Long idUsuario, 
									"" // String observaciones
								);
								DaoFactory.getInstance().insert(sesion, domicilio);
								TrManticProveedorDomicilioDto particular= new TrManticProveedorDomicilioDto(
									-1L, // Long idProveedorDomicilio, 
									proveedor.getIdProveedor(), // Long idProveedor, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									1L, // Long idTipoDomicilio, 
									domicilio.getIdDomicilio(), // Long idDomicilio, 
									1L, // Long idPrincipal, 
									"" // String observaciones
								);
								DaoFactory.getInstance().insert(sesion, particular);
								particular= new TrManticProveedorDomicilioDto(
									-1L, // Long idProveedorDomicilio, 
									proveedor.getIdProveedor(), // Long idProveedor, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									2L, // Long idTipoDomicilio, 
									domicilio.getIdDomicilio(), // Long idDomicilio, 
									1L, // Long idPrincipal, 
									"" // String observaciones
								);
								DaoFactory.getInstance().insert(sesion, particular);
							} // if
							// telefono
							if(!Cadena.isVacio(sheet.getCell(3, fila).getContents())) 
								this.toProcessContactoProveedor(sesion, sheet.getCell(3, fila).getContents(), 1L, proveedor.getIdProveedor());
							// correos
							if(!Cadena.isVacio(sheet.getCell(4, fila).getContents())) 
								this.toProcessContactoProveedor(sesion, sheet.getCell(4, fila).getContents(), 9L, proveedor.getIdProveedor());
							if(!Cadena.isVacio(sheet.getCell(5, fila).getContents())) 
								this.toProcessContactoProveedor(sesion, sheet.getCell(5, fila).getContents(), 10L, proveedor.getIdProveedor());
							JsfBase.getAutentifica().getMonitoreo().incrementar();
							if(fila% this.categoria.getTuplas()== 0) {
								if(bitacora== null) {
								  bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, new Long(fila), 2L);
  								DaoFactory.getInstance().insert(sesion, bitacora);
							  } // if
								else {
									bitacora.setProcesados(new Long(fila));
									bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  								DaoFactory.getInstance().update(sesion, bitacora);
								} // else
								this.commit();
							} // if
						} // if
						else {
							this.errores++;
							LOG.warn(fila+ ": ["+ nombre+ "]  LOS DATOS DE ESTA FILA ESTAN EN BLANCO");
							TcManticMasivasDetallesDto detalle= new TcManticMasivasDetallesDto(
								sheet.getCell(0, fila).getContents(), // String rfc, 
								-1L, // Long idMasivaDetalle, 
								this.masivo.getIdMasivaArchivo(), // Long idMasivaArchivo, 
								"EL ["+ sheet.getCell(2, fila).getContents()+ "] ALGUNOS DE SUS DATOS ESTAN VACIOS, CORREO, ENTIDAD, MUNICIPIO" // String observaciones
							);
							DaoFactory.getInstance().insert(sesion, detalle);
						} // else	
					} // if	
				} // for
				if(bitacora== null) {
					bitacora= new TcManticMasivasBitacoraDto("", this.masivo.getIdMasivaArchivo(), JsfBase.getIdUsuario(), -1L, this.masivo.getTuplas(), 2L);
  				DaoFactory.getInstance().insert(sesion, bitacora);
				} // if
			  else {
					bitacora.setProcesados(this.masivo.getTuplas());
					bitacora.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					DaoFactory.getInstance().update(sesion, bitacora);
				} // if
				LOG.warn("Cantidad de filas con error son: "+ this.errores);
				regresar = true;
			} // if
		} // try
		catch (IOException | BiffException e) {
			throw e;
		} // catch
    finally {
      if(workbook!= null) {
        workbook.close();
        workbook = null;
      } // if
    } // finally
		return regresar;
	}
	
	public void toDeleteXls() throws Exception {
		List<TcManticMasivasArchivosDto> list= (List<TcManticMasivasArchivosDto>)DaoFactory.getInstance().findViewCriteria(TcManticMasivasArchivosDto.class, this.masivo.toMap(), "all");
		if(list!= null)
			for (TcManticMasivasArchivosDto item: list) {
				LOG.info("Catalogo importado: "+ item.getIdMasivaArchivo()+ " delete file: "+ item.getAlias());
				File file= new File(item.getAlias());
				file.delete();
			} // for
	}	
 	
}