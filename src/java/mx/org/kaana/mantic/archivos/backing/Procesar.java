package mx.org.kaana.mantic.archivos.backing;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
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
import mx.org.kaana.mantic.archivos.beans.Concepto;
import mx.org.kaana.mantic.archivos.beans.Documento;
import mx.org.kaana.mantic.archivos.beans.Referencia;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.comun.IBaseStorage;
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

  private List<Documento> listado;
  private List<Referencia> referencias;
  private List<Concepto> conceptos;
  private String source;

  public List<Documento> getListado() {
    return listado;
  }

  public List<Referencia> getReferencias() {
    return referencias;
  }

  public List<Concepto> getConceptos() {
    return conceptos;
  }
          
	@PostConstruct
  @Override
  protected void init() {		
    try {
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_XML);
      this.listado= new ArrayList<>();
      this.referencias= new ArrayList<>();
      this.conceptos= new ArrayList<>();
      this.source= Configuracion.getInstance().getPropiedadSistemaServidor("sat");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {		
    try {
      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public String doAceptar() {  
    return null;
  } // doAccion

	public void doFileUpload(FileUploadEvent event) {
		StringBuilder path= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
		String nameFile   = Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result       = null;		
		Long fileSize     = 0L;
		try {
      Documento documento= new Documento(event.getFile().getFileName().toUpperCase());
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
            new Importado(nameFile, event.getFile().getContentType(), EFormatos.XML, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), null, event.getFile().getFileName().toUpperCase(), 13L) // Importado xml      
          ));
          this.toReadFactura(result, Boolean.TRUE, 1D);
          // CARGAR LOS DATOS DE LA FACTURA
          this.referencias.add(new Referencia(
            this.getFactura().getFolio(), // String folio, 
            this.getEmisor().getRfc(), // String rfc
            this.getEmisor().getNombre(), // String emisor, 
            this.getFactura().getTimbreFiscalDigital().getUuid(), // String uuid, 
            Fecha.toFechaSat(this.getFactura().getFecha()), // String fecha, 
            this.getFactura().getTipoDeComprobante(), // String tipo, 
            this.getFactura().getTotal(), // String total, 
            this.getReceptor().getNombre(), // String receptor
            event.getFile().getFileName().toUpperCase() // String archivo     
          ));
          // CARGAR LOS DATOS DE LOS CONCEPTOS
          for (mx.org.kaana.mantic.libs.factura.beans.Concepto item: this.getFactura().getConceptos()) {
            this.conceptos.add(new Concepto(
              this.getFactura().getFolio(), // String folio      
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
              event.getFile().getFileName().toUpperCase() // String archivo
            ));
          } // for
          this.toSaveFileRecord(event.getFile().getFileName().toUpperCase(), ruta, path.toString(), this.listado.get(this.listado.size()- 1).getXml().getName());            
        } // if
      } // if
      else {
  			JsfBase.addMessage("Precaución:", "El archivo ya esta cargado ".concat(event.getFile().getFileName().toUpperCase()), ETipoMensaje.ERROR);
        LOG.error("EL ARCHIVO YA ESTA CARGADO EN LA LISTA ".concat(event.getFile().getFileName().toUpperCase()));
      } // if  
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
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
		if(event.getTab().getTitle().equals("Importar")) {
      
    } // if
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
	
  
}