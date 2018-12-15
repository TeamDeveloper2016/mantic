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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticMasivasArchivosDto;
import static org.apache.commons.io.Charsets.ISO_8859_1;
import static org.apache.commons.io.Charsets.UTF_8;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {
  
  private static final Log LOG = LogFactory.getLog(Transaccion.class);
  private TcManticMasivasArchivosDto masivo;	
	private ECargaMasiva categoria;
	private String messageError;
  
  public Transaccion(TcManticMasivasArchivosDto masivo, ECargaMasiva categoria) {
		this.masivo   = masivo;		
		this.categoria= categoria;
	} // Transaccion

	protected void setMessageError(String messageError) {
		this.messageError=messageError;
	}

	public String getMessageError() {
		return messageError;
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
					regresar = DaoFactory.getInstance().delete(sesion, this.masivo)> -1L;
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
			} // swtich
      monitoreo.terminar();
			monitoreo.setProgreso(0L);
			regresar= true;
		} // if	
		else
			LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ this.masivo.getNombre());
    return regresar;
	}
	
	private Long toFindUnidad(String unidad) {
		Long regresar= 9L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			
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
		StringBuilder encabezado= new StringBuilder();
		try {
      WorkbookSettings workbookSettings = new WorkbookSettings();
      workbookSettings.setEncoding("Cp1252");	
			workbookSettings.setExcelDisplayLanguage("MX");
      workbookSettings.setExcelRegionalSettings("MX");
      workbookSettings.setLocale(new Locale("es", "MX"));
			workbook= Workbook.getWorkbook(archivo, workbookSettings);
			sheet		= workbook.getSheet(0);
			if(sheet != null && sheet.getColumns()>= this.categoria.getColumns() && sheet.getRows()>= 2) {
				if(encabezado.toString().equals(this.categoria.getFields())) {
					//LOG.info("<-------------------------------------------------------------------------------------------------------------->");
					LOG.info("Filas del documento: "+ sheet.getRows());
					int errores= 0;
          for(int fila= 1; fila< sheet.getRows(); fila++) {
						if(sheet.getCell(0, fila)!= null && !sheet.getCell(0, fila).getContents().toUpperCase().startsWith("NOTA")) {
							String contenido= new String(sheet.getCell(2, fila).getContents().getBytes(UTF_8), ISO_8859_1);
							// 0           1          2        3          4          5          6           7        8
							//CODIGO|CODIGOAUXILIAR|NOMBRE|COSTOS/IVA|MENUDEONETO|MEDIONETO|MAYOREONETO|UNIDADMEDIDA|IVA
							double costo  = Numero.getDouble(sheet.getCell(3, fila).getContents()!= null? sheet.getCell(3, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
							double menudeo= Numero.getDouble(sheet.getCell(4, fila).getContents()!= null? sheet.getCell(4, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
							double medio  = Numero.getDouble(sheet.getCell(5, fila).getContents()!= null? sheet.getCell(5, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
							double mayoreo= Numero.getDouble(sheet.getCell(6, fila).getContents()!= null? sheet.getCell(6, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
							double iva    = Numero.getDouble(sheet.getCell(8, fila).getContents()!= null? sheet.getCell(8, fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
							String nombre= new String(contenido.getBytes(ISO_8859_1), UTF_8);
							if((costo> 0 && menudeo> 0 && medio> 0 && mayoreo> 0) && !Cadena.isVacio(nombre)) {
								TcManticArticulosDto articulo= new TcManticArticulosDto(
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
									this.toFindUnidad(sheet.getCell(7, fila).getContents()), // Long idEmpaqueUnidadMedida, 
									1L, // Long idRedondear, 
									menudeo, // Double menudeo, 
									null, // String metaTagTeclado, 
									new Timestamp(Calendar.getInstance().getTimeInMillis()), // Timestamp fecha, 
									JsfBase.getIdUsuario(), //  Long idUsuario, 
									JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), // Long idEmpresa, 
									0D, // Double cantidad, 
									10D, // Double minimo, 
									20D, // Double maximo, 
									10D, // Double limiteMedioMayoreo, 
									20D, // Double limiteMayoreo, 
									Constantes.CODIGO_SAT, // String sat, 
									2L, // Long idServicio, 
									2L, // Long idBarras, 
									"0", // String descuento, 
									"0", // String extra, 
									null // String idFacturama
								);
								DaoFactory.getInstance().insert(sesion, articulo);
								JsfBase.getAutentifica().getMonitoreo().incrementar();
								if(fila% 1000== 0) 
									 sesion.flush();
							} // if
							else {
								errores++;
								LOG.warn(fila+ ": ["+ nombre+ "] costo: ["+ costo+ "] menudeo: ["+ menudeo+ "] menudeo: ["+ medio+ "] menudeo: ["+ mayoreo+ "]");
							} // else	
   					} // if	
					} // for
					LOG.info("Cantidad de filas con error son: "+ errores);
					regresar = true;
			  } // if
			} // if
		} // try
		catch (IOException | BiffException e) {
			throw e;
		} // catch
    finally {
      if(workbook!= null){
        workbook.close();
        workbook = null;
      }
    } // finally
		return regresar;
	} // toVerificaXls		

  private Boolean toClientes(Session sesion, File archivo) throws Exception {	
		return true;
	}
	
  private Boolean toProveedores(Session sesion, File archivo) throws Exception {	
		return true;
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