package mx.org.kaana.mantic.catalogos.masivos.reglas;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
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
			monitoreo.incrementar(this.masivo.getTuplas().intValue());
      monitoreo.terminar();
			monitoreo.setProgreso(0L);
			regresar= true;
		} // if	
		else
			LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ this.masivo.getNombre());
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
          // this.articulos = new ArrayList<>();
					//LOG.info("<-------------------------------------------------------------------------------------------------------------->");
					LOG.info("Filas del documento: "+ sheet.getRows());
					int errores= 0;
          for(int fila= 1; fila< sheet.getRows(); fila++) {
            //(idListaPrecio,descripcion, idListaPrecioDetalle, codigo, precio, auxiliar) 
					  String contenido= new String(sheet.getCell(2,fila).getContents().getBytes(UTF_8), ISO_8859_1);
						//LOG.info(fila+ " -> "+ contenido+ " => "+ cleanString(contenido)+ " -> "+ new String(contenido.getBytes(ISO_8859_1), UTF_8));
						double costo = Numero.getDouble(sheet.getCell(3,fila).getContents()!= null? sheet.getCell(3,fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						double precio= Numero.getDouble(sheet.getCell(4,fila).getContents()!= null? sheet.getCell(4,fila).getContents().replaceAll("[$, ]", ""): "0", 0D);
						String nombre= new String(contenido.getBytes(ISO_8859_1), UTF_8);
						if((precio> 0 || costo> 0) && !Cadena.isVacio(nombre)) {
//							getArticulos().add(new TcManticListasPreciosDetallesDto(
//								-1L,
//								nombre,
//								-1L,
//								sheet.getCell(0,fila).getContents(),
//								precio,
//								sheet.getCell(1,fila).getContents(),
//								costo)
//							);
//        for(TcManticListasPreciosDetallesDto articulo:this.articulos){
//          articulo.setIdListaPrecio(this.lista.getIdListaPrecio());
//          DaoFactory.getInstance().insert(sesion, articulo);
//          monitoreo.setProgreso((long)(reg* 100/ monitoreo.getTotal()));
//          monitoreo.incrementar();
//          i++;
//          reg++;
//          if(i==1000){
//            sesion.flush();
//            i=0;
//          }
//        }
						} // if
						else {
							errores++;
							LOG.warn(fila+ ": ["+ nombre+ "] costo: ["+ costo+ "] precio: ["+ precio+ "]");
						} // else	
          } // for
					LOG.info("Cantidad de filas con error son: "+ errores);
          regresar = true;
        }
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