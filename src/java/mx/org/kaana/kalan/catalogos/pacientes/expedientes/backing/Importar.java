package mx.org.kaana.kalan.catalogos.pacientes.expedientes.backing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kalan.catalogos.pacientes.expedientes.beans.Expediente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import mx.org.kaana.kalan.catalogos.pacientes.expedientes.reglas.Transaccion;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.mantic.catalogos.comun.IBaseImportar;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "kalanCatalogosPacientesExpedientesImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353388565639367L;
	private Entity cliente;
	private Long idCliente;	
	private Expediente documento;	
	private List<Expediente> documentos;	
  private String path;
  
	
	public Importado getDocumento() {
		return documento;
	}

	public Entity getCliente() {
		return cliente;
	}

  public List<Expediente> getDocumentos() {
    return documentos;
  }

  public String getPath() {
    return path;
  }
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("idCliente")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.idCliente= JsfBase.getFlashAttribute("idCliente")== null? -1L: (Long)JsfBase.getFlashAttribute("idCliente");
      this.toLoad();
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_LOGOTIPOS);
			this.documento = new Expediente();
      this.documentos= new ArrayList();
      this.toLoadDocumentos();
      this.toLoadCitas();
      this.attrs.put("elementos", this.documentos.size());
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/expedientes/");      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  private void toLoad() {
    List<Columna> columns    = new ArrayList<>();	
		Map<String, Object>params= new HashMap<>();
    try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      params.put(Constantes.SQL_CONDICION, "(tc_mantic_clientes.id_cliente= "+ this.idCliente+ ")");
      columns.add(new Columna("inicio", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));    
      this.cliente= (Entity)DaoFactory.getInstance().toEntity("VistaClientesCitasDto", "clientes", params);
      if(this.cliente!= null && !this.cliente.isEmpty())
        UIBackingUtilities.toFormatEntity(this.cliente, columns);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally		
  } // doLoad
  
  private void toLoadDocumentos() {
    List<String> tipos        = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("agrupador", "2, 7");
      List<UISelectItem> items= UISelect.seleccione("TcManticTiposArchivosDto", "grupo", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS, "idKey");
      this.attrs.put("documentos", items);
      this.attrs.put("idTipoDocumento", UIBackingUtilities.toFirstKeySelectItem(items));
      if(items!= null && !items.isEmpty()) {
         for (UISelectItem item: items) {
          tipos.add(item.getValue()+ " ".concat(Constantes.SEPARADOR).concat(" ").concat(item.getLabel()));
        } // for
      } // if
      this.attrs.put("tipos", tipos);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }
  
  private void toLoadCitas() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      Calendar inicio = Calendar.getInstance();
      inicio.add(Calendar.DATE, -360);
      params.put("idCliente", this.cliente.toLong("idCliente"));
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      params.put(Constantes.SQL_CONDICION, "(date_format(tc_kalan_citas.inicio, '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, inicio)).concat("')"));
      params.put("sortOrder", "order by tc_kalan_citas.inicio desc");
      columns.add(new Columna("inicio", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));    
      columns.add(new Columna("termino", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));    
      columns.add(new Columna("servicios", EFormatoDinamicos.MAYUSCULAS));    
      List<UISelectEntity> citas= (List<UISelectEntity>) UIEntity.seleccione("VistaClientesCitasDto", "lazy", params, columns, "consecutivo");
      this.attrs.put("citas", citas);    
      if(citas!= null && !citas.isEmpty())
        this.documento.setIkCita(UIBackingUtilities.toFirstKeySelectEntity(citas));    
      else
        this.documento.setIkCita(new UISelectEntity(-1L));    
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
  }
  
	public void doFileUpload(FileUploadEvent event) {
		StringBuilder path= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
		String nameFile   = Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result       = null;		
		Long fileSize     = 0L;
		try {			
      path.append(Configuracion.getInstance().getPropiedadSistemaServidor("path.expedientes"));
      temp.append(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString());
      temp.append("/");
      temp.append(this.idCliente);
      temp.append("/");      
      temp.append(Fecha.getHoyEstandar());
      temp.append("/");      
			path.append(temp.toString());
			result= new File(path.toString());		
			if (!result.exists())
				result.mkdirs();
      String ruta= path.toString();
      path.append(nameFile);
			result = new File(path.toString());
			if (result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();						
			this.documento.setName(nameFile);
			this.documento.setContent(event.getFile().getContentType());
			this.documento.setFormat(EFormatos.JPG);
			this.documento.setSize(fileSize.equals(0L)? fileSize: fileSize/1024);
			this.documento.setMedicion(event.getFile().equals(0L)? " BYTES": " KB");
			this.documento.setRuta(temp.toString());
			this.documento.setOriginal(event.getFile().getFileName().toUpperCase());
      this.toSaveFileRecord(event.getFile().getFileName().toUpperCase(), ruta, path.toString(), this.documento.getName());            
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
			if(result!= null)
			  result.delete();
		} // catch
	} // doFileUpload	
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Documentos")) 
			this.doLoadImportados();
	}	// doTabChange	

  private void doLoadImportados() {
		List<Columna> columns     = new ArrayList<>();
  	Map<String, Object> params= new HashMap<>();
		try {
      params.put("idCliente", this.cliente.toLong("idCliente"));
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      params.put("sortOrder", "order by tc_kalan_expedientes.registro desc ");
      columns.add(new Columna("archivo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("tipo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
		  this.attrs.put("importados", UIEntity.build("VistaExpedientesDto", "lazy", params, columns));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
  		Methods.clean(params);
      Methods.clean(columns);
    }// finally
  } // doLoadImportados
  
  @Override
	public StreamedContent doFileDownload(UISelectEntity item) {
		StreamedContent regresar= null;
		try {
			File reference= new File(item.toString("alias"));
			if(reference.exists()) {
				InputStream stream = new FileInputStream(reference);
  		  regresar= new DefaultStreamedContent(stream, EFormatos.JPG.getContent(), item.toString("nombre"));
	  	} // if
			else {
				LOG.warn("No existe el archivo: "+ item.toString("alias"));
        JsfBase.addMessage("No existe el archivo: "+ item.toString("nombre")+ ", favor de verificarlo.");
			} // else	
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		return regresar;
	} // doFileDownload

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCliente", this.idCliente);
  	JsfBase.setFlashAttribute("idClienteProcess", this.idCliente);
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

	public String doAceptar() {
		String regresar        = null;
		Transaccion transaccion= null;
		try {
      if(this.documentos.size()> 0) {
        transaccion= new Transaccion(this.cliente, this.documentos);
        if(transaccion.ejecutar(EAccion.REGISTRAR)) {
          JsfBase.addMessage("Documento", "Se importó el documento de forma correcta !", ETipoMensaje.INFORMACION);
          this.doLoadImportados();
          this.documentos.clear();
          this.documento= new Expediente();
          this.attrs.put("elementos", this.documentos.size());
        } // if
        else
          JsfBase.addMessage("Documento", "Ocurrio un error al importar los documentos, intente de nueva cuenta !", ETipoMensaje.INFORMACION);
      } // if
      else
        JsfBase.addMessage("Documento", "No exiten documentos que importar !", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	} // doAceptar

  public void doUpdateDocumento() {
    List<UISelectItem> items= (List<UISelectItem>)this.attrs.get("documentos");
    if(items!= null && !items.isEmpty()) {
      int index= items.indexOf(new UISelectItem(this.documento.getIdComodin()));
      if(index>= 0)
        this.documento.setComodin(items.get(index).getLabel());
    } // if
  }
  
  public void doAddDocumento() {
    this.documentos.add(this.documento);
    this.documento= new Expediente();
    this.attrs.put("elementos", this.documentos.size());
  }
  
  public void doRemoveDocumento(Expediente item) {
    item.setIdEstatus(2L);
  }
  
  public void doRecoverDocumento(Expediente item) {
    item.setIdEstatus(1L);
  }
  
  public void doCleanDocumento(UISelectEntity item) {
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(item.toLong("idExpediente"));
      if(transaccion.ejecutar(EAccion.ELIMINAR)) {
      	JsfBase.addMessage("Documento", "Se eliminó el documento de forma correcta !", ETipoMensaje.INFORMACION);
        List<UISelectEntity> importados= (List<UISelectEntity>)this.attrs.get("importados");
        if(importados!= null && !importados.isEmpty())
          importados.remove(item);
      } // if  
      else
      	JsfBase.addMessage("Documento", "Ocurrio un error al eliminar el documento, intente de nueva cuenta !", ETipoMensaje.ALERTA);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
  }
 
  public void doValueChangeTipo(ValueChangeEvent event) {
    try {      
      String[] values= event.getNewValue().toString().split("[|]");
      if(values!= null && values.length> 1 && !Objects.equals(values[0].trim(),"-1")) {
        this.attrs.put("ikComodin", new Long(values[0].trim()));
        this.attrs.put("comodin", values[1].trim());      
      } // if  
      else
        this.attrs.put("ikComodin", null);      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }
  
  public void doChangeTipo(Expediente row) {
    try {      
      if(!Cadena.isVacio(this.attrs.get("ikComodin"))) {
        row.setIdComodin((Long)this.attrs.get("ikComodin"));
        row.setComodin((String)this.attrs.get("comodin"));
        this.attrs.put("ikComodin", null);
        this.attrs.put("comodin", null);
      } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }  

  public void doChangeDescripcion(Importado row) {
    row.setObservaciones(row.getObservaciones()!= null? row.getObservaciones().toUpperCase(): "");
  }
  
  @Override
  protected void finalize() throws Throwable {
    Methods.clean(documentos);
    super.finalize(); 
  }
  
}