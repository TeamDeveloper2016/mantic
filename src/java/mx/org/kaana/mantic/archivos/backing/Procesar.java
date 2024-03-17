package mx.org.kaana.mantic.archivos.backing;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.archivos.beans.Concepto;
import mx.org.kaana.mantic.archivos.beans.Documento;
import mx.org.kaana.mantic.archivos.beans.Referencia;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.comun.IBaseStorage;
import mx.org.kaana.mantic.db.dto.TcManticDocumentosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticDocumentosDto;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticArchivosProcesar")
@ViewScoped
public class Procesar extends IBaseImportar implements IBaseStorage, Serializable {

	private static final Log LOG              = LogFactory.getLog(Procesar.class);
  private static final long serialVersionUID= 327393488565639327L;

  private FormatCustomLazy lazyDetalle;  
  private List<Documento> listado;
  private List<String> todos;
  private String source;
  private Integer count;
  private String semilla;

  public FormatCustomLazy getLazyDetalle() {
    return lazyDetalle;
  }

  public List<Documento> getListado() {
    return listado;
  }

  public List<String> getTodos() {
    return todos;
  }
          
	@PostConstruct
  @Override
  protected void init() {		
    Map<String, Object> params= new HashMap<>();
    try {
      this.semilla    = Fecha.getRegistro();
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_XML);
      this.listado    = new ArrayList<>();
      this.todos      = new ArrayList<>();
      this.source     = Configuracion.getInstance().getPropiedadSistemaServidor("sat");
      this.count      = 1;
      String fecha= Fecha.getRegistro(-1);
      params.put("semilla", fecha.substring(0, 8).concat("235959999"));
      DaoFactory.getInstance().deleteAll(TcManticDocumentosDetallesDto.class, params);
      DaoFactory.getInstance().deleteAll(TcManticDocumentosDto.class, params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } 

  @Override
  public void doLoad() {		
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("folio", EFormatoDinamicos.MAYUSCULAS));                 
      params.put("semilla", this.semilla);
      this.lazyModel = new FormatCustomLazy("TcManticDocumentosDto", params, columns);
      UIBackingUtilities.resetDataTable("referencias");
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  } 

  public String doAceptar() {  
    return null;
  } // doAccion

	public void doFileUpload(FileUploadEvent event) {
		StringBuilder path= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
    String original   = event.getFile().getFileName().toUpperCase();
		String nameFile   = Archivo.toFormatNameFile(this.count+ "_"+ original);
    File result       = null;		
		Long fileSize     = 0L;
    this.count++;
		try {
      Documento documento= new Documento(original);
      int position= this.listado.indexOf(documento);
      if(position< 0) {
        path.append(this.source);
        temp.append("Eliminar/");
        path.append(temp.toString());
        result= new File(path.toString());		
        if (!result.exists())
          result.mkdirs();
        String ruta= path.toString();
        path.append(nameFile);
        result = new File(path.toString());
        if (result.exists())
          result.delete();			      
        this.toWriteFile(result, event.getFile().getInputstream(), nameFile.endsWith(EFormatos.XML.name()));
        fileSize= event.getFile().getSize();			
        if(nameFile.endsWith(EFormatos.XML.name())) {
          // CARGAR LA REFERENCIA AL DOCUMENTO QUE FUE IMPORTADO PARA MOSTRARLO
          this.listado.add(new Documento(
            event.getFile().getFileName().toUpperCase(), // String original, 
            nameFile, // String name, 
            ruta, // String path, 
            new Importado(nameFile, event.getFile().getContentType(), EFormatos.XML, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), null, original, 13L) // Importado xml      
          ));
          this.toReadFactura(result, Boolean.TRUE, 1D);
          // CARGAR LOS DATOS DE LA FACTURA
          Referencia referencia= new Referencia(
            this.getFactura().getFolio(), // String folio, 
            this.getEmisor().getRfc(), // String rfc
            this.getEmisor().getNombre(), // String emisor, 
            this.getFactura().getSello(), // String uuid, 
            Fecha.toFechaSat(this.getFactura().getFecha()), // String fecha, 
            this.getFactura().getTipoDeComprobante(), // String tipo, 
            this.getFactura().getTotal(), // String total, 
            this.getReceptor().getNombre(), // String receptor
            event.getFile().getFileName().toUpperCase() // String archivo     
          );
          referencia.setSemilla(this.semilla);
          DaoFactory.getInstance().insert(referencia);
          // CARGAR LOS DATOS DE LOS CONCEPTOS
          for (mx.org.kaana.mantic.libs.factura.beans.Concepto item: this.getFactura().getConceptos()) {
            Concepto concepto= new Concepto(
              this.getFactura().getFolio(), // String folio      
              this.getFactura().getTipoDeComprobante(), // String tipo
              item.getClaveProdServ(), // String claveProducto, 
              item.getNoIdentificacion(), // String noIdentificacion, 
              item.getDescripcion(), // String descripcion, 
              item.getClaveUnidad(), // String claveUnidad, 
              item.getUnidad(), // String unidad, 
              String.valueOf(Numero.redondearSat(Numero.getDouble(item.getValorUnitario(), 0D))), // String precioUnitario, 
              String.valueOf(Numero.redondearSat(Numero.getDouble(item.getCantidad(), 0D))), // String cantidad, 
              String.valueOf(Numero.getDouble(item.getTraslado().getTasaCuota(), 0.16D)* 100), // String tasaImpuesto, 
              String.valueOf(Numero.redondearSat(Numero.getDouble(item.getTraslado().getBase(), 0D))), // String subtotal, 
              String.valueOf(Numero.redondearSat(Numero.getDouble(item.getDescuento(), 0D))), // String descuento
              String.valueOf(Numero.redondearSat(Numero.getDouble(item.getTraslado().getImporte(), 0D))), // String iva, 
              String.valueOf(Numero.redondearSat(Numero.getDouble(item.getImporte(), 0D)+ Numero.getDouble(item.getTraslado().getImporte(), 0D))), // String total, 
              event.getFile().getFileName().toUpperCase(), // String archivo
              Fecha.toFechaSat(this.getFactura().getFecha()), // String fecha
              this.getEmisor().getNombre() // String proveedor
            );
            concepto.setIdDocumento(referencia.getIdDocumento());
            DaoFactory.getInstance().insert(concepto);
          } // for
          this.toSaveFileRecord(original, ruta, path.toString(), this.listado.get(this.listado.size()- 1).getXml().getName());            
          this.getFactura().getConceptos().clear();
        } // if
      } // if
      else {
  			JsfBase.addMessage("Precaución:", "El archivo ya esta cargado ".concat(original), ETipoMensaje.ERROR);
        LOG.error("EL ARCHIVO YA ESTA CARGADO EN LA LISTA ".concat(original));
      } // if  
		} // try
		catch (Exception e) {
      this.todos.add(original);
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo ["+ original+ "] no pudo ser importado !", ETipoMensaje.ERROR);
			if(result!= null)
			  result.delete();
		} // catch    
  } // doFileUpload	
  
	public void doAutoSaveOrden() {
	  this.toSaveRecord();	
	}
	
	@Override
	public void toSaveRecord() {
	  UIBackingUtilities.execute("jsArticulos.back('guard\\u00F3 los archivos', '[SAT]');");
  }

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Documento(s)")) 
      this.doLoad();
    else
		  if(event.getTab().getTitle().equals("Concepto(s)")) 
        this.doLoadDetalle();
  }  
  
  public void doCleanFiles() {
    try {
      String path= Configuracion.getInstance().getPropiedadSistemaServidor("sat").concat("Eliminar/");
      Archivo.delteFiles(path);
    } // try 
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
  }
 
	public void doViewXml(Documento item) {
		this.toViewFile(this.source.concat(item.getXml().getRuta()).concat(item.getXml().getName()));
	}
  
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); 
    this.doCleanFiles();
  }

  public void doLoadDetalle() {		
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("folio", EFormatoDinamicos.MAYUSCULAS));                 
      params.put("semilla", this.semilla);
      this.lazyDetalle = new FormatCustomLazy("TcManticDocumentosDetallesDto", params, columns);
      UIBackingUtilities.resetDataTable("conceptos");
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  } 
  
}