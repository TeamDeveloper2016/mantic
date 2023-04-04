package mx.org.kaana.kalan.catalogos.pacientes.expedientes.backing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "kalanCatalogosPacientesExpedientesGaleria")
@ViewScoped
public class Galeria extends IBaseFilter implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Galeria.class);
  private static final long serialVersionUID= 327353388565639367L;
	private Entity cliente;
	private Long idCliente;	
  private Integer idCriterio;
  private String path;
  
	
	public Entity getCliente() {
		return cliente;
	}

  public String getPath() {
    return path;
  }
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.idCriterio= 0;
			if(JsfBase.getFlashAttribute("idCliente")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.idCliente= JsfBase.getFlashAttribute("idCliente")== null? -1L: (Long)JsfBase.getFlashAttribute("idCliente");
//      this.idCliente= 1L;
      this.toLoadCliente();
      this.toLoadCitas();
      this.toLoadDocumentos();
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes": JsfBase.getFlashAttribute("retorno"));
      Calendar inicio = Calendar.getInstance();
      inicio.add(Calendar.YEAR, -1);
      this.attrs.put("inicio", new Date(inicio.getTimeInMillis()));
			this.attrs.put("termino", new Date(Calendar.getInstance().getTimeInMillis()));
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/expedientes/");      
      this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
		List<Columna> columns    = new ArrayList<>();
		Map<String, Object>params= this.toPrepare();
		try {
      params.put("idCliente", this.cliente.toLong("idCliente"));
      params.put("sortOrder", "order by tc_kalan_expedientes.registro desc ");
      columns.add(new Columna("archivo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("tipo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      this.lazyModel = new FormatLazyModel("VistaExpedientesDto", "lazy", params, columns);
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
  		Methods.clean(params);
      Methods.clean(columns);
    }// finally
  }
  
  private void toLoadCliente() {
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
  
	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap();
		StringBuilder sb            = new StringBuilder("");
		try {
      switch(this.idCriterio) {
        case 0: // BUSCAR POR DOCUMENTO
          if(!Cadena.isVacio(this.attrs.get("nombre")))
            sb.append("(upper(tc_kalan_expedientes.archivo) like '%").append(this.attrs.get("nombre").toString()).append("%') and ");
          break;
        case 1: // BUSCAR POR ID_TIPO_DOCUMENTO
          if(!Cadena.isVacio(this.attrs.get("idTipoDocumento").toString()) && !Objects.equals(this.attrs.get("idTipoDocumento").toString(), "-1"))
            sb.append("(tc_kalan_expedientes.id_tipo_archivo= ").append(this.attrs.get("idTipoDocumento").toString()).append(") and ");
          break;
        case 2: // BUSCAR POR FECHA
          sb.append("(date_format(tc_kalan_expedientes.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("inicio"))).append("') and ");
          sb.append("(date_format(tc_kalan_expedientes.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("termino"))).append("') and ");
          break;
        case 3: // BUSCAR POR ID_CITA
          if(!Cadena.isVacio(this.attrs.get("idCita").toString()) && !Objects.equals(this.attrs.get("idCita").toString(), "-1"))
            sb.append("(tc_kalan_expedientes.id_cita= ").append(this.attrs.get("idCita").toString()).append(") and ");
          break;
      } // switch
			if(Cadena.isVacio(sb)) {
        Calendar inicio = Calendar.getInstance();
        inicio.add(Calendar.YEAR, -1);
				sb.append("(date_format(tc_kalan_expedientes.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, inicio)).append("')");
      } // if  
			else
				sb.delete(sb.length()- 4, sb.length());
			regresar.put(Constantes.SQL_CONDICION, sb.toString());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
  
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
        this.attrs.put("idCita", UIBackingUtilities.toFirstKeySelectEntity(citas));    
      else
        this.attrs.put("idCita", new UISelectEntity(-1L));    
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
  }
  
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
  
	public StreamedContent doFileDownload(Entity item) {
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
 
	public void doTabChange(TabChangeEvent event) {
    switch(event.getTab().getTitle()) {
      case "Nombre":
    		this.idCriterio= 0;
        break;
      case "Tipo":
    		this.idCriterio= 1;
        break;
      case "Fecha":
    		this.idCriterio= 2;
        break;
      case "Cita":
    		this.idCriterio= 3;
        break;
    } // switch
  }  
 
  public void doUpdateInicio() {
    Calendar inicio = Calendar.getInstance();
    Calendar termino= Calendar.getInstance();
    inicio.setTimeInMillis(((Date)this.attrs.get("inicio")).getTime());
    termino.setTimeInMillis(((Date)this.attrs.get("termino")).getTime());
    if(inicio.after(termino)) {
      this.attrs.put("inicio", new Date(termino.getTimeInMillis()));
      this.attrs.put("termino", new Date(inicio.getTimeInMillis()));
    } // if  
  }
          
  public void doUpdateTermino() {
    Calendar inicio = Calendar.getInstance();
    Calendar termino= Calendar.getInstance();
    inicio.setTimeInMillis(((Date)this.attrs.get("inicio")).getTime());
    termino.setTimeInMillis(((Date)this.attrs.get("termino")).getTime());
    if(termino.before(inicio)) {
      this.attrs.put("inicio", new Date(termino.getTimeInMillis()));
      this.attrs.put("termino", new Date(inicio.getTimeInMillis()));
    } // if  
  }
 
  public String doAccion() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
			JsfBase.setFlashAttribute("idCliente", this.cliente.toLong("idCliente"));
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/galeria.jsf");
			regresar= "/Paginas/Kalan/Catalogos/Pacientes/Expedientes/importar".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
  
}