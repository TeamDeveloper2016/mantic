package mx.org.kaana.kajool.mantenimiento.gestion.asistente.motor.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 3, 2012
 *@time 11:12:38 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.motor.Generacion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.motor.beans.DetalleMotor;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.comun.UIBusqueda;
import org.primefaces.event.TabChangeEvent;
import mx.org.kaana.kajool.enums.ETipoGeneracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.ui.UIUtilities;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name="kajoolTemplateMotorGeneracion")
@ViewScoped
public class TemplateMotorGeneracion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID=8927310803364775843L;
  private static final Log LOG							= LogFactory.getLog(TemplateMotorGeneracion.class);
  private UIBusqueda busqueda;
  private UIBusqueda busquedaTablas;
  private List<UISelectItem> listaCamposLLave;
	
  private static final String PKG = "mx.org.kaana.kajool.db";

  public void setListaCamposLLave(List<UISelectItem> listaCamposLLave) {
    this.listaCamposLLave = listaCamposLLave;
  }

  public UIBusqueda getBusquedaTablas() {
    return busquedaTablas;
  }

  public void setBusquedaTablas(UIBusqueda busquedaTablas) {
    this.busquedaTablas = busquedaTablas;
  }

  public List<UISelectItem> getListaCamposLLave() {
    return listaCamposLLave;
  }

  public UIBusqueda getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(UIBusqueda busqueda) {
		this.busqueda = busqueda;
	}
	
  @PostConstruct
	@Override
  protected void init() {
    this.busqueda= new UIBusqueda("", this.getClass(), Collections.EMPTY_LIST, false);
    this.busquedaTablas= new UIBusqueda("", this.getClass(), Collections.EMPTY_LIST, false);
		this.attrs.put("detalleMotor", new DetalleMotor());
		((DetalleMotor)this.attrs.get("detalleMotor")).setEsSinonimo("1");
		((DetalleMotor)this.attrs.get("detalleMotor")).setPaquete(PKG);
		((DetalleMotor)this.attrs.get("detalleMotor")).setEncuestaSeleccionadaTexto("");
    //this.detalleMotor = new DetalleMotor();
		//this.detalleMotor.setEsSinonimo("1");
    //this.detalleMotor.setPaquete(PKG);
		this.attrs.put("resultadoXmlVisible", false);
		this.attrs.put("soloLecturaSinonimo", false);
		this.attrs.put("tabSeleccionado", "Por tabla");
    //this.resultadoXmlVisible = this.soloLecturaSinonimo = false;    		
    construirBusqueda();
  }


  private void construirBusqueda() {
    try {
      this.busquedaTablas = new UIBusqueda("Tabla", this.getClass(), Arrays.asList("table_name"), Arrays.asList("tableName"), false);
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    }
  }

  private void configNombreTabla() {
		((DetalleMotor)this.attrs.get("detalleMotor")).setTablaSeleccionada(Numero.getLong( this.busquedaTablas.getSeleccionado().getValue().toString() ) );
		//((DetalleMotor)this.attrs.get("detalleMotor")).setNombreTabla(this.busquedaTablas.getSeleccionado().getLabel());			
    //this.detalleMotor.setTablaSeleccionada(Numero.getLong( this.busquedaTablas.getSeleccionado().getValue().toString() ) );
    //this.detalleMotor.setNombreTabla(this.busquedaTablas.getSeleccionado().getLabel());
  }

  private void configNombreEncuesta(String encuestaSeleccionada) {
    if (encuestaSeleccionada.equals(""))
			((DetalleMotor)this.attrs.get("detalleMotor")).setNombreEncuesta("No aplica");
      //this.detalleMotor.setNombreEncuesta("No aplica");
    else
			((DetalleMotor)this.attrs.get("detalleMotor")).setNombreEncuesta(encuestaSeleccionada);
      //this.detalleMotor.setNombreEncuesta(UISelect.buscarEtiqueta(this.listaEncuestas, encuestaSeleccionada));
  }

  protected void configNombreCampoLlave() {
    ((DetalleMotor)this.attrs.get("detalleMotor")).setCampoLlave(UISelect.buscarEtiqueta(this.listaCamposLLave, ((DetalleMotor)this.attrs.get("detalleMotor")).getCampoLlaveSeleccionado()));
    //this.detalleMotor.setCampoLlave(UISelect.buscarEtiqueta(this.listaCamposLLave, this.detalleMotor.getCampoLlaveSeleccionado()));
  }

  public void onCambiarSinonimo() {
    this.attrs.put("soloLecturaSinonimo", ((DetalleMotor)this.attrs.get("detalleMotor")).getEsSinonimo().equals("0"));
    //this.soloLecturaSinonimo = this.detalleMotor.getEsSinonimo().equals("0");
    if (((Boolean)this.attrs.get("soloLecturaSinonimo"))) {
      ((DetalleMotor)this.attrs.get("detalleMotor")).setTablaSinonimo("");
      ((DetalleMotor)this.attrs.get("detalleMotor")).setLlaveSinonimo("");
      //this.detalleMotor.setTablaSinonimo("");
      //this.detalleMotor.setLlaveSinonimo("");
    }
  }

  public void doGenerar(ETipoGeneracion tipoGeneracion) {
    Generacion generacion = null;
    try {
      ((DetalleMotor)this.attrs.get("detalleMotor")).setEsNemonico("0");
      ((DetalleMotor)this.attrs.get("detalleMotor")).setConDao("0");
      generacion = new Generacion(tipoGeneracion, ((DetalleMotor)this.attrs.get("detalleMotor")),false);
      generacion.registrar();
      JsfUtilities.addMessage("Los archivos fueron generados exitosamente.");
    } // try
    catch(Exception e){
      JsfUtilities.addMessage("Error: No se generaron los archivos correspondientes. \n" + e.getMessage(), ETipoMensaje.ERROR);
    }
  }

  public void doGenerarTabla() {
    configNombreEncuesta(((DetalleMotor)this.attrs.get("detalleMotor")).getEncuestaSeleccionadaTexto());
    //configNombreEncuesta(this.detalleMotor.getEncuestaSeleccionada());
    configNombreTabla();
    doGenerar(ETipoGeneracion.TABLA);
    this.attrs.put("resultadoXmlVisible",  !Cadena.isVacio(((DetalleMotor)this.attrs.get("detalleMotor")).getResultadoXML()));
    //this.resultadoXmlVisible =  !Cadena.isVacio(this.detalleMotor.getResultadoXML());
  }

  public void doGenerarConsulta() {
    configNombreEncuesta(((DetalleMotor)this.attrs.get("detalleMotor")).getEncuestaSeleccionadaTexto());
    configNombreCampoLlave();
    doGenerar(ETipoGeneracion.CONSULTA);
    this.attrs.put("resultadoXmlConsultaVisible", !Cadena.isVacio(((DetalleMotor)this.attrs.get("detalleMotor")).getResultadoXMLConsulta()));
    //this.resultadoXmlConsultaVisible =  !Cadena.isVacio(this.detalleMotor.getResultadoXMLConsulta());
  }

  public void doGenerarEncuesta() {
    doGenerar(ETipoGeneracion.ENCUESTA);
  }
	
  public void doValidarConsulta() {
    Generacion generacion             = null;
    List<String> definicionCamposLlave= null;
    try {
      configNombreEncuesta(((DetalleMotor)this.attrs.get("detalleMotor")).getEncuestaSeleccionadaTexto());
      generacion = new Generacion(((DetalleMotor)this.attrs.get("detalleMotor")),false);
      //configNombreEncuesta(this.detalleMotor.getEncuestaSeleccionadaEncuesta());
      //generacion = new Generacion(this.detalleMotor);
      definicionCamposLlave = generacion.getDefinicionCamposLlave();
      this.listaCamposLLave = new ArrayList<>();
      for (int i = 0; i < definicionCamposLlave.size(); i += 1) {
        this.listaCamposLLave.add(new UISelectItem( new Long(i+1), definicionCamposLlave.get(i)));
      } // for
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
  }
	
	public void doActualizarPaquete() {
		configNombreEncuesta(((DetalleMotor)this.attrs.get("detalleMotor")).getEncuestaSeleccionadaTexto());
		if(((DetalleMotor)this.attrs.get("detalleMotor")).getEncuestaSeleccionadaTexto().equals(""))			
			((DetalleMotor)this.attrs.get("detalleMotor")).setPaquete("mx.org.kaana.kajool.db");
		else
			((DetalleMotor)this.attrs.get("detalleMotor")).setPaquete("mx.org.kaana.".concat(((DetalleMotor)this.attrs.get("detalleMotor")).getNombreEncuesta().toLowerCase()).concat(".db"));
//		configNombreEncuesta(this.detalleMotor.getEncuestaSeleccionada());
//		if(this.detalleMotor.getEncuestaSeleccionada().equals(-1L))			
//			this.detalleMotor.setPaquete("mx.org.kaana.kajool.db");
//		else
//			this.detalleMotor.setPaquete("mx.org.kaana.".concat(this.detalleMotor.getNombreEncuesta().toLowerCase()).concat(".db"));
	}

  public void doBusqueda(ActionEvent event) {
		this.busqueda.clear();
    getBusqueda().buscar(getBusqueda().getSeleccionado().getLabel());
  }
	
  public void doAsignar(ActionEvent event) {
		LOG.debug(this.busqueda.getSeleccionado());
		this.busqueda.update((String)this.busqueda.getSeleccionado().getValue());
		doLimpiar(event);
  }
	
	public void doLimpiar(ActionEvent event) {
    this.busqueda.clear();
	}

	public void doCambiar(ValueChangeEvent event) {
	  LOG.debug(event.getSource());
		((DetalleMotor)this.attrs.get("detalleMotor")).setNombreTabla(recuperarLabel(event.getNewValue().toString()));				
	}
	
	public String recuperarLabel(String id) {
		String regresar= null;
		for (Iterator it=this.busquedaTablas.getResultado().iterator(); it.hasNext();) {
			Object item= it.next();
			if(((SelectItem)item).getValue().toString().equals(id))
				regresar= ((SelectItem)item).getLabel();
		} // for
		return regresar;
	}

  public void onTabChange(TabChangeEvent event) {
		try {
      if(event.getTab().getTitle().equals("Descargar"))
				RequestContext.getCurrentInstance().execute("descargar();");
      else{
        RequestContext.getCurrentInstance().update("resultados:resultadoDto");
        RequestContext.getCurrentInstance().update("resultados:resultadoXml");
        RequestContext.getCurrentInstance().execute("reload('fuentes:resultados:resultadoDto','fuentes:resultados:resultadoXml');");
        RequestContext.getCurrentInstance().execute("refrescar();");
      } // else
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
  } // onTabChange

  public StreamedContent getDescargar() throws Exception {
		Zip zip                 = null;
		String contentType      = null;
		StreamedContent regresar= null;
		String zipName          = null;
		InputStream inputStream = null;
		String path             = null;
		try {
      this.attrs.put("patron", Archivo.toFormatNameFile(""));
      generarTemporal("Dto");
      generarTemporal("Xml");
			zip= new Zip();
			zipName= EFormatos.TXT.toPath().concat(Constantes.ARCHIVO_PATRON_SEPARADOR).concat(this.attrs.get("patron").toString()).concat(Cadena.toClassNameEspecial(((DetalleMotor)this.attrs.get("detalleMotor")).getNombreTabla().equals("")?((DetalleMotor)this.attrs.get("detalleMotor")).getTablaSinonimo():((DetalleMotor)this.attrs.get("detalleMotor")).getNombreTabla())).concat("Dto.").concat(EFormatos.ZIP.name().toLowerCase());
			zipName= Cadena.reemplazarCaracter(zipName, '/' , File.separatorChar);
			zip.setDebug(true);
			zip.setEliminar(true);
			path = JsfUtilities.getRealPath(EFormatos.TXT.toPath()).concat(File.separator);
			zip.compactar(JsfUtilities.getRealPath(zipName), path, this.attrs.get("patron").toString().concat("?"));
			this.attrs.put("archivo", zipName);
			contentType= EFormatos.ZIP.getContent();
			inputStream= ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(this.attrs.get("archivo").toString());
			regresar   = new DefaultStreamedContent(inputStream, contentType, this.attrs.get("archivo").toString().substring(this.attrs.get("archivo").toString().lastIndexOf(File.separatorChar)+ 1));	
		} // try
		catch(Exception e) {
			throw e;			
		} // catch
    return regresar;
	} // getDescargar

  private void generarTemporal(String generar){
    FileWriter writer = null;
		String path				= null;
		String nombre			= null;
		String fileName   = null;
    try{
      fileName= this.attrs.get("patron").toString().concat(generar.equals("Dto")?Cadena.toClassNameEspecial(((DetalleMotor)this.attrs.get("detalleMotor")).getNombreTabla().equals("")?((DetalleMotor)this.attrs.get("detalleMotor")).getTablaSinonimo():((DetalleMotor)this.attrs.get("detalleMotor")).getNombreTabla()).concat("Dto"):"Copiar_en_xml");
      nombre=fileName.concat(".").concat(generar.equals("Dto")?"java":"txt");
      nombre= Cadena.reemplazarCaracter(nombre, '/' , File.separatorChar);
      path = JsfUtilities.getRealPath(EFormatos.TXT.toPath()).concat(File.separator);
      writer = new FileWriter(path.concat(nombre));
      writer.write(generar.equals("Dto")?((DetalleMotor)this.attrs.get("detalleMotor")).getResultadoDto():((DetalleMotor)this.attrs.get("detalleMotor")).getResultadoXML());
      writer.flush();
      writer.close();
    } // try
    catch(IOException e){
      Error.mensaje(e);
    } // catch
    finally{
      Methods.clean(writer);
    } // finally
  } // generarTemporal
}
