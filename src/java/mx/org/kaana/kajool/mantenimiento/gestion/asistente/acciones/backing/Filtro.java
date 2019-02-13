package mx.org.kaana.kajool.mantenimiento.gestion.asistente.acciones.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/03/2015
 *@time 02:33:59 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EComponente;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.EMascaras;
import mx.org.kaana.kajool.enums.ETipoDato;
import mx.org.kaana.kajool.enums.EValidaciones;
import mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.reglas.Generador;
import mx.org.kaana.kajool.mantenimiento.gestion.asistente.acciones.beans.Campo;
import mx.org.kaana.kajool.mantenimiento.gestion.asistente.acciones.reglas.DtoFinder;
import mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Parametro;
import mx.org.kaana.xml.Dml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@ManagedBean(name="kajoolMantenimientoGestionAccionTemplate")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

	private static final Log LOG = LogFactory.getLog(Filtro.class);
	private static final long serialVersionUID=-7279310379030305455L;
  private List<Campo> campos;
  private List<UISelectItem> encuestas;
  private List<UISelectItem> validaciones;
  private List<UISelectItem> mascaras;
	private EFormatos idFormato;
	private List<Parametro> parametrosCombo;
	private List<mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo> camposCombo;
	private List<UISelectItem> selects;
	private List<UISelectItem> units;
  private DualListModel<UISelectItem> pickList;
  private List<Campo> camposSeleccionados;

  public List<Campo> getCampos() {
    return campos;
  }

  public List<Campo> getCamposSeleccionados() {
    return camposSeleccionados;
  }
	
  public List<Parametro> getParametrosCombo() {
		return parametrosCombo;
	}

	public List<mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo> getCamposCombo() {
		return camposCombo;
	}

	public List<UISelectItem> getSelects() {
		return selects;
	}

	public List<UISelectItem> getUnits() {
		return units;
	}

	public List<UISelectItem> getEncuestas() {
		return encuestas;
	}

	public void setEncuestas(List<UISelectItem> encuestas) {
		this.encuestas=encuestas;
	}
	
	public EFormatos getIdFormato() {
		return idFormato;
	}
	
	public DualListModel<UISelectItem> getPickList() {
		return pickList;
	}

	public void setPickList(DualListModel<UISelectItem> campos) {
		this.pickList=campos;
	}

	public List<UISelectItem> getValidaciones() {
		return validaciones;
	}

	public List<UISelectItem> getMascaras() {
		return mascaras;
	}

  public void setCampos(List<Campo> campos) {
    this.campos = campos;
  }

  public void setParametrosCombo(List<Parametro> parametrosCombo) {
    this.parametrosCombo = parametrosCombo;
  }

  public void setCamposCombo(List<mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo> camposCombo) {
    this.camposCombo = camposCombo;
  }

  public void setCamposSeleccionados(List<Campo> camposSeleccionados) {
    this.camposSeleccionados = camposSeleccionados;
  }

  @PostConstruct
	@Override
	protected void init() {
    try {
			LOG.debug(JsfUtilities.getFacesContext().getCurrentPhaseId());
			this.campos = new ArrayList();
			this.attrs.put("tipo", "Consulta");
			this.attrs.put("nombreAccion", "Accion");
			this.attrs.put(Constantes.SQL_CONDICION, "activo=1");
			this.attrs.put("paramsAgregado", false);
			this.attrs.put("converter", "Fecha");
			this.attrs.put("titulo", "Titulo de la página");
			this.attrs.put("proceso", "Proceso");
			this.attrs.put("crearBean", false);
      this.attrs.put("nombreBean","Bean");
      if(!Dml.getInstance().getSelect("TcEncuestasDto", "row", this.attrs).equals(""))
        this.encuestas = UISelect.build("TcEncuestasDto", this.attrs, "nombre");
      else{
        this.encuestas = new ArrayList();
        this.encuestas.add(new UISelectItem(-1L, "Teclear Proyecto"));
      }
			this.units = new ArrayList<>();
			this.selects = new ArrayList<>();			
			Document doc = Dml.getInstance().getDocumento();
      if(doc!= null){
        doc.getDocumentElement().normalize();
        if (doc.hasChildNodes())
          loadIdsUnit(doc.getChildNodes());
      } // if
			this.mascaras = new ArrayList();
			for(int i=0; i<EMascaras.values().length;i++){
				this.mascaras.add(new UISelectItem(EMascaras.values()[i].getDescripcion(), EMascaras.values()[i].getDescripcion().toUpperCase()));
			}
			this.validaciones = new ArrayList();
			for(int i=0; i<EValidaciones.values().length;i++){
				this.validaciones.add(new UISelectItem(EValidaciones.values()[i].getDescripcion(), EValidaciones.values()[i].getDescripcion().toUpperCase()));
			}
			this.attrs.put("encuesta", UIBackingUtilities.toFirstKeySelectItem(encuestas));
			this.attrs.put("encuestaTexto","");
			this.attrs.put("autor", "kajool.kaana");
			this.attrs.put("correo", "team.developer@kaana.org.mx");
			doActualizarRuta();
			UIBackingUtilities.execute("descargarHide();");
    } // try
    catch(Exception e) {
			JsfUtilities.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init
	
	public String onFlowProcess(FlowEvent event) {
		FlowEvent nuevoTab;
		try {
			if(event.getOldStep().equals("tabCampos")){
        this.camposSeleccionados= new ArrayList();
        for(UISelectItem lista: this.pickList.getTarget()){
          for(Campo campo: this.campos){
            if(lista.getLabel().equals(campo.getLlave())){
              this.camposSeleccionados.add(campo);
              break;
            } // if
          } // for
        } // for
      } // if
      else if(event.getOldStep().equals("tabReferencia")){
        obtenerCamposDto();
      } // else if
      else if(event.getOldStep().equals("tabSqlCombo")){
				LOG.debug("Buscando parametros para la consulta");
				parametrosCombo = null;
				parametrosCombo = obtenerParametros();
				if(parametrosCombo.isEmpty()) {
					nuevoTab =  new FlowEvent(event.getComponent(), "tabParametrosCombo", "tabCamposCombo");
					return onFlowProcess(nuevoTab);
				} // if
			} // else if
			else if(event.getOldStep().equals("tabParametrosCombo") && event.getNewStep().equals("tabCamposCombo")){
				LOG.debug("Analizando consulta sql");
				camposCombo = obtenerCampos();
				if(camposCombo.isEmpty())
					return event.getOldStep();
			} // else if
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
		return event.getNewStep();
	} // onFlowProcess
	
	public void onRowEdit(RowEditEvent event) {
	} // onRowEdit
	
	public boolean isTransaccion(){
		boolean regresar = false;
		if(this.attrs.get("tipo").toString().equals("Transaccion"))
			regresar=true;
		return regresar;
	} // isTransaccion
	
	protected void cargarVariables() throws Exception{
		StringBuilder validacion			= null;
		StringBuilder criterios				= null;
		StringBuilder botones					= null;
		StringBuilder atributos		  	= null;
		StringBuilder atributosBean 	= null;
		StringBuilder gettersBean   	= null;
		StringBuilder settersBean   	= null;
		StringBuilder getters		  		= null;
		StringBuilder idsCriterios	  = null;
		StringBuilder inicializaciones= null;
		StringBuilder imports					= null;
		StringBuilder params					= null;
		StringBuilder clearParams			= null;
		Generador generador						= null;
		Map<String,Object> variables	= null;
		String tipoDato             	= null;
		try {
			this.attrs.put("fecha", Fecha.formatear("dd/MM/yyyy", new Date(Calendar.getInstance().getTimeInMillis())));
			this.attrs.put("hora", Fecha.formatear("hh:mm aa", new Date(Calendar.getInstance().getTimeInMillis())));
			this.attrs.put("backing", Cadena.toBeanName(this.attrs.get("rutaPagina").toString().replace("/", Constantes.ARCHIVO_PATRON_SEPARADOR).concat(Constantes.ARCHIVO_PATRON_SEPARADOR).concat(this.attrs.get("nombreAccion").toString())));
			this.attrs.put("paquete",this.attrs.get("rutaBacking").toString());
			this.attrs.put("paqueteBean",this.attrs.get("rutaBeans").toString());
			this.attrs.put("paqueteReglas",this.attrs.get("rutaReglas").toString());
			this.attrs.put("importTransaccion",isTransaccion()?"import ".concat(this.attrs.get("rutaReglas").toString()).concat(".Transaccion;"):"");
			this.attrs.put("nombreDto", (this.attrs.get("dto") instanceof String)? this.attrs.get("dto").toString():((String [])this.attrs.get("dto"))[0].substring(((String [])this.attrs.get("dto"))[0].lastIndexOf(".")+1));
			validacion			 = new StringBuilder();
			criterios			   = new StringBuilder();
			variables			   = new HashMap<>();
			botones		   	   = new StringBuilder();
			generador				 = new Generador();
			atributos			   = new StringBuilder();
			atributosBean	   = new StringBuilder();
			settersBean  	   = new StringBuilder();
			gettersBean	     = new StringBuilder();
			getters			     = new StringBuilder();
			idsCriterios		 = new StringBuilder();
			inicializaciones = new StringBuilder();
			params					 = new StringBuilder();
			clearParams			 = new StringBuilder();
			imports = new StringBuilder();
			for(Campo campo: this.camposSeleccionados) {
				variables.clear();
				variables.put("alias", campo.getAlias());
				variables.put("criterio", campo.getNombre());
				variables.put("backing", this.attrs.get("backing"));
				variables.put("validaciones", doObtenerStringValidacion(campo.getValidacion()));
				variables.put("mascara", campo.getMascara());
				validacion.append(generador.getSentencia(EComponente.VALIDACION_ACCION.getSintaxis(), variables)).append(",\n");
        if (campo.getTipoComponente()==EComponente.TEXT_FIELD_ACCION){
          tipoDato= campo.getTipoDato().getSimpleName().substring(campo.getTipoDato().getSimpleName().lastIndexOf(".")+1);
          variables.put("size", tipoDato.equals("String")?"80":tipoDato.equals("Long") ?"10":"20");
				  criterios.append(generador.getSentencia(EComponente.TEXT_FIELD_ACCION.getSintaxis(), variables)).append("\n");
				} // if
				else if (campo.getTipoComponente()==EComponente.SELECT_ONE_MENU_ACCION){
          if(this.attrs.get("paramsAgregado")!= null && this.attrs.get("paramsAgregado").equals(false)){
						params.append(generador.getSentencia(EComponente.PARAMS.getSintaxis(), variables));
						clearParams.append(generador.getSentencia(EComponente.CLEAR_PARAMS.getSintaxis(), variables));
						imports.append("import java.util.HashMap;\nimport java.util.Map;\nimport mx.org.kaana.libs.pagina.UISelect;").append("\n");
					} // if
					variables.put("initSelectOneMenu", campo.getInitSelectOneMenu());
				  variables.put("lista", Cadena.letraCapital(campo.getNombre()));
					atributos.append(generador.getSentencia(EComponente.ATRIBUTOS.getSintaxis(), variables)).append("\n");
					getters.append(generador.getSentencia(EComponente.GETTERS.getSintaxis(), variables)).append("\n");
					idsCriterios.append(campo.getNombre()).append(" ");
					criterios.append(generador.getSentencia(EComponente.SELECT_ONE_MENU_ACCION.getSintaxis(), variables)).append("\n");
					inicializaciones.append(generador.getSentencia(EComponente.INIT_SELECT_ONE_MENU_ACCION.getSintaxis(),variables)).append("\n");
				} // else if
				else{
					variables.put("converter",campo.getConverter());
					criterios.append(generador.getSentencia(EComponente.DATE_ACCION.getSintaxis(), variables)).append("\n");
				} // else
        if(this.attrs.get("crearBean").equals(true)){
          variables.put("nombreAtributo", campo.getNombre());
          variables.put("nombreAtributoGet", Cadena.toNameBean(campo.getNombre()));
          variables.put("tipoDato", campo.getTipoDato().getSimpleName());
          atributosBean.append(generador.getSentencia(EComponente.ATRIBUTOS_BEAN.getSintaxis(),variables)).append("\n");
          gettersBean.append(generador.getSentencia(EComponente.GETTERS_BEAN.getSintaxis(),variables)).append("\n");
          settersBean.append(generador.getSentencia(EComponente.SETTERS_BEAN.getSintaxis(),variables)).append("\n");
        } // if
			} // for
			if (this.attrs.get("tipo").toString().equals("Transaccion")){
				botones.append(generador.getSentencia(EComponente.BOTON_ACEPTAR.getSintaxis(), variables)).append("\n");
        this.attrs.put("importDto",isTransaccion()?"import ".concat((this.attrs.get("dto") instanceof String)? this.attrs.get("dto").toString():((String [])this.attrs.get("dto"))[0].substring(5)).concat(";"):"");
				imports.append("import ").append(this.attrs.get("rutaBacking").toString().substring(0,this.attrs.get("rutaBacking").toString().lastIndexOf("backing")-1)).append(".reglas.Transaccion").append(";\n");
			} // if
			botones.append(generador.getSentencia(EComponente.BOTON_CANCELAR.getSintaxis(), variables)).append("\n");
			this.attrs.put(Constantes.SQL_CONDICION, "id_encuesta=".concat(this.attrs.get("encuesta").toString()));
			imports.append("import mx.org.kaana.").append(this.attrs.get("encuesta").equals(-1L)?this.attrs.get("encuestaTexto").toString().toLowerCase():DaoFactory.getInstance().toField("TcEncuestasDto", attrs,"nombre").toString().toLowerCase()).append(".db.dto.").append(this.attrs.get("nombreDto")).append(";");
			validacion.delete(validacion.lastIndexOf(",\n")!=-1?validacion.lastIndexOf("\n"):validacion.length(), validacion.length());
			atributos.delete(atributos.lastIndexOf("\n")!=-1?atributos.lastIndexOf("\n"):atributos.length(), atributos.length());
			atributosBean.delete(atributosBean.lastIndexOf("\n")!=-1?atributosBean.lastIndexOf("\n"):atributosBean.length(), atributosBean.length());
			gettersBean.delete(gettersBean.lastIndexOf("\n")!=-1?gettersBean.lastIndexOf("\n"):gettersBean.length(), gettersBean.length());
			settersBean.delete(settersBean.lastIndexOf("\n")!=-1?settersBean.lastIndexOf("\n"):settersBean.length(), settersBean.length());
			getters.delete(getters.lastIndexOf("\n")!=-1?getters.lastIndexOf("\n"):getters.length(), getters.length());
		  inicializaciones.delete(inicializaciones.lastIndexOf("\n")!=-1?inicializaciones.lastIndexOf("\n"):inicializaciones.length(), inicializaciones.length());
		  criterios.delete(criterios.lastIndexOf("<p:spacer/>")!=-1?criterios.lastIndexOf("<p:spacer/>"):criterios.length(), criterios.length());
			botones.delete(botones.lastIndexOf("\n")!=-1?botones.lastIndexOf("\n"):botones.length(), botones.length());
			this.attrs.put("validaciones", validacion.toString());
			this.attrs.put("criterios", criterios.toString());
			this.attrs.put("atributos", atributos.toString());
			this.attrs.put("atributosBean", atributosBean.toString());
			this.attrs.put("gettersBean", gettersBean.toString());
			this.attrs.put("settersBean", settersBean.toString());
			this.attrs.put("inicializaciones", inicializaciones.toString());
			this.attrs.put("getters", getters.toString());
			this.attrs.put("botones",botones.toString());
			this.attrs.put("imports",imports.toString());
			this.attrs.put("params",params.toString());
			this.attrs.put("clearParams",clearParams.toString());
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(variables);
			Methods.clean(validacion);
			Methods.clean(criterios);
			Methods.clean(atributos);
			Methods.clean(inicializaciones);
			Methods.clean(getters);
			Methods.clean(botones);
			Methods.clean(imports);
			Methods.clean(params);
			Methods.clean(clearParams);
		} // finally
	} // cargarVariables
	
	public void doAceptar() {
		Generador generador = null;
		try {
			cargarVariables();
			generador = new Generador(this.attrs.get("nombreAccion").toString(), this.attrs.get("rutaPagina").toString(), this.attrs,"Accion.txt", "ClaseAccion.txt", isTransaccion()?"Transaccion.txt":null, Boolean.parseBoolean(this.attrs.get("crearBean").toString()));
			generador.generar();
			this.attrs.put("patron", generador.getPatron());
			this.attrs.put("textoBacking", generador.getTextoBacking());
			this.attrs.put("textoPagina", generador.getTextoPagina());
			this.attrs.put("textoTransaccion", generador.getTextoTransaccion());
			this.attrs.put("textoBean", generador.getTextoBean());
			this.attrs.put("archivo",Constantes.ARCHIVO_PATRON_SEPARADOR.concat(this.attrs.get("patron").toString()).concat("Accion.").concat(EFormatos.ZIP.name().toLowerCase() ));
			if (UIBackingUtilities.getCurrentInstance()!=null) {
				UIBackingUtilities.update("tab:textoPagina");
				UIBackingUtilities.update("tab:textoBacking");
				UIBackingUtilities.update("tab:textoTransaccion");
				UIBackingUtilities.update("tab:textoBean");
				UIBackingUtilities.execute("descargarShow();");
				JsfUtilities.addMessage("La acción fue generado correctamente.");
			} // if
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // doAceptar
	
	public StreamedContent getDescargar() throws Exception {
		Zip zip                 = null;
		String contentType      = null;
		StreamedContent regresar= null;
		String zipName          = null;
		InputStream inputStream = null;
		String path             = null;
		try {
			doAceptar();
      zip= new Zip();
			zipName= EFormatos.TXT.toPath().concat(Constantes.ARCHIVO_PATRON_SEPARADOR).concat(this.attrs.get("patron").toString()).concat("Accion.").concat(EFormatos.ZIP.name().toLowerCase());
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
	
	public void doActualizarRuta(){
		StringBuilder rutaPagina = null;
		StringBuilder rutaBacking = null;
		String encuesta    = null;
		try {
			LOG.debug("Actualizando rutas");
			rutaPagina = new StringBuilder();
      if(this.attrs.get("encuesta").equals(-1L))
        encuesta= this.attrs.get("encuestaTexto").toString();
      else{
        this.attrs.put("idEncuesta",this.attrs.get("encuesta"));
        encuesta = ((Entity)DaoFactory.getInstance().toEntity("TcEncuestasDto", "encuestas", this.attrs)).toString("nombre");
      } // else
      this.attrs.put("nombreEncuesta", encuesta);
			rutaPagina.append(encuesta.length()>0?encuesta.substring(0, 1).toUpperCase():"");
			rutaPagina.append(encuesta.length()>0?encuesta.substring(1, encuesta.length()):"");
      rutaPagina.append("/");
			rutaPagina.append(this.attrs.get("proceso").toString());
			this.attrs.put("rutaPagina", rutaPagina.toString());
			rutaBacking = new StringBuilder("mx.org.kaana.");
			rutaBacking.append(encuesta.toLowerCase());
			rutaBacking.append(".");
			rutaBacking.append(encuesta.toUpperCase().equals("")?"procesos.":"");
			rutaBacking.append(this.attrs.get("proceso").toString().toLowerCase().replace("/", "."));
      this.attrs.put("rutaReglas", rutaBacking.toString().concat(".reglas"));
      this.attrs.put("rutaBeans", rutaBacking.toString().concat(".beans"));
			rutaBacking.append(".backing");
			this.attrs.put("rutaBacking", rutaBacking.toString());
      doLoadDtos(EAccion.PROCESAR);
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // doActualizarRuta
	
	public void onTabChange(TabChangeEvent event) {
		try {
			if(event.getTab().getTitle().equals("Descargar"))
				UIBackingUtilities.execute("descargar();");
      else {
				UIBackingUtilities.execute("reload('tab:textoPagina','tab:textoBacking','tab:textoTransaccion', 'tab:textoBean');");
				UIBackingUtilities.execute("refrescar();");
			} // else
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
  } // omnTabChange
	
	public EComponente getComponente(String componente){
		EComponente regresar = null;
		try {
			regresar = EComponente.valueOf(componente.toUpperCase());
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
		return regresar;
	} // getComponente
	
	public String doObtenerStringValidacion(String [] validaciones){
		StringBuilder regresar=null;
		try {
			regresar= new StringBuilder();
			if(validaciones!=null && validaciones.length>0){
				for(int i=0;i<validaciones.length;i++){
					regresar.append(validaciones[i].toLowerCase()).append("|");
				}
				regresar.deleteCharAt(regresar.lastIndexOf("|"));
			} // if
			else{
				regresar.append("libre");
			} // else
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
		return regresar.toString();
	}
	
	public List<Parametro> obtenerParametros() {
		List<Parametro> regresar = null;
		try {
			regresar = new ArrayList<>();
			Generador generador = new Generador();
			for(String parametro: generador.getParametros(this.attrs.get("sqlCombo").toString())) {
				regresar.add(new Parametro(parametro));
			} // for
		} // try
		catch (Exception e) {
			JsfUtilities.addMessage("La consulta sql no es correcta");
			Error.mensaje(e);
		} // catch
		return regresar;
	} // obtenerParametros
	
	private String obtenerConsulta() throws Exception {
		String regresar							= null;
		Generador generador         = null;
		Map<String,Object> variables= null;
		try {
			if(!parametrosCombo.isEmpty() || parametrosCombo!=null) {
				variables = new HashMap<>();
				for(Parametro parametro: parametrosCombo)
					variables.put(parametro.getNombre(), parametro.getValor());
				generador = new Generador();
				regresar = generador.getSentencia(this.attrs.get("sqlCombo").toString(), variables);
			} // if
			else
				regresar = this.attrs.get("sqlCombo").toString();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(variables);
		} // finally
		return regresar;
	} // obtener Consulta
	
	public List<mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo> obtenerCampos(){
		List<mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo> regresar = new ArrayList();
		String sql           = null;
		Entity registro      = null;
		try {
			LOG.debug("Obteniendo campos de consulta");
			sql =  obtenerConsulta();
			registro = (Entity)DaoFactory.getInstance().toEntity(sql);
			for(Object campo:registro.toArray()){
				if(((Value)campo).getData() instanceof String){
					regresar.add(new mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo(((Value)campo).getName(),ETipoDato.TEXT));
				}
				else if(((Value)campo).getData() instanceof Long || ((Value)campo).getData() instanceof BigDecimal){
					regresar.add(new mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo(((Value)campo).getName(),ETipoDato.LONG));
				}
				else if(((Value)campo).getData() instanceof Date){
					regresar.add(new mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo(((Value)campo).getName(),ETipoDato.DATE));
				}
			} // for
		} // try
		catch (Exception e) {
			JsfUtilities.addMessage("La consulta sql no es correcta, favor de verificarla !!!");
			Error.mensaje(e);
		} // catch
		return regresar;
	} // obtenerCampos
	
	public void doAsignarCampo(){
		try {
			for(Campo campo: this.camposSeleccionados){
				if(campo.getNombre().equals(this.attrs.get("campoSeleccionado").toString())){
					if(this.attrs.get("tipoSelect").equals("UISelect.build"))
						campo.setInitSelectOneMenu("lista".concat(Cadena.letraCapital(campo.getNombre())).concat("= UISelect.build(\"").concat(this.attrs.get("nombreVistaCombo").toString().concat("\", params, \"").concat(this.attrs.get("campoCombo").toString()).concat("\");")));
					else
						campo.setInitSelectOneMenu("lista".concat(Cadena.letraCapital(campo.getNombre())).concat("= UISelect.free(\"").concat(this.attrs.get("nombreVistaCombo").toString().concat("\", params, \"").concat(this.attrs.get("campoCombo").toString()).concat("\",\"").concat(this.attrs.get("value").toString()).concat("\");")));
					break;
				} // if
			} // for
			this.attrs.put("unitCombo","INSERTAR UNIT");
			this.attrs.put("selectCombo","row");
			this.attrs.put("sqlCombo","x");
			this.attrs.put("nombreVistaCombo","x");
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // doAsignarCampo
	
	protected void loadIdsUnit(NodeList nodeList){
		try {
			for(int i=0;i<nodeList.getLength();i++){
				Node tempNode = nodeList.item(i);
				if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
					if(tempNode.getNodeName().equals("unit")){
						if (tempNode.hasAttributes()) {
							NamedNodeMap nodeMap = tempNode.getAttributes();
							for (int j = 0; j < nodeMap.getLength(); j++) {
								Node node = nodeMap.item(j);
								this.units.add(new UISelectItem(node.getNodeValue(),node.getNodeValue()));
							} // for
						} // if
					} // if
					if (tempNode.hasChildNodes()) {
						loadIdsUnit(tempNode.getChildNodes());
					} // if
				}
			} // for
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // loadIdsUnit
	
	protected void loadIdsSelect(NodeList nodeList){
		try {
			for(int i=0;i<nodeList.getLength();i++){
				Node tempNode = nodeList.item(i);
				if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
					if(tempNode.getNodeName().equals("select")){
						if (tempNode.hasAttributes()) {
							NamedNodeMap nodeMap = tempNode.getAttributes();
							for (int j = 0; j < nodeMap.getLength(); j++) {
								Node node = nodeMap.item(j);
								this.selects.add(new UISelectItem(node.getNodeValue(),node.getNodeValue()));
							} // for
						} // if
					} // if
					if (tempNode.hasChildNodes()){
						loadIdsSelect(tempNode.getChildNodes());
					} // if
				} // if
			} // for*/
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // loadIdsUnit
	
	public void actualizarSelects(){
		try {
			this.selects = new ArrayList<>();
			this.attrs.put("nombreVistaCombo", this.attrs.get("unitCombo"));						
			Document doc = Dml.getInstance().getDocumento();
			doc.getDocumentElement().normalize();
			XPathFactory xPFabrica = XPathFactory.newInstance();
      XPath xPath = xPFabrica.newXPath();
      Object dato =   xPath.evaluate("/kajool/dml/unit[@id='"+ (this.attrs.get("unitCombo").toString())+"']", Dml.getInstance().getDocumento(), XPathConstants.NODESET);
			loadIdsSelect((NodeList)dato);
			if(!selects.isEmpty()){
				this.attrs.put("selectCombo", UIBackingUtilities.toFirstKeySelectItem(selects));
				actualizaConsulta();
			}
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // actualizarSelects
	
	public void actualizaConsulta(){
		try {
			Document doc = Dml.getInstance().getDocumento();
			doc.getDocumentElement().normalize();
			XPathFactory xPFabrica = XPathFactory.newInstance();
      XPath xPath = xPFabrica.newXPath();
      Object dato =   xPath.evaluate("/kajool/dml/unit[@id='"+this.attrs.get("unitCombo").toString()+"']/select[@id='"+this.attrs.get("selectCombo")+"']", Dml.getInstance().getDocumento(), XPathConstants.NODE);
			this.attrs.put("sqlCombo",((Node)dato).getTextContent().toString());
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // actualizaConsulta
	
	public void doGuardarCampoSeleccionado(String nombre){
		this.attrs.put("campoSeleccionado", nombre);
	}

	public void doAsignarConverter(){
		try {
			for(Campo campo: this.camposSeleccionados){
				if(campo.getNombre().equals(this.attrs.get("campoSeleccionado").toString())){
				  campo.setConverter(this.attrs.get("converter").toString());
				} // if
			} // for
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}
	
	@Override
	protected void finalize() throws Throwable {
		Methods.clean(pickList);
		Methods.clean(camposSeleccionados);
		Methods.clean(campos);
		Methods.clean(encuestas);
		Methods.clean(validaciones);
		Methods.clean(mascaras);
		Methods.clean(parametrosCombo);
		Methods.clean(camposCombo);
		Methods.clean(selects);
		Methods.clean(units);
	}

  public String getContextRoot() {
    return JsfUtilities.doContextRoot();
  }

  public String getKajoolVersion() {
    return Configuracion.getInstance().getPropiedad("sistema.janal.version");
  }

  public void doLoadDtos(EAccion accion){
    try {
      this.attrs.put(accion.equals(EAccion.PROCESAR)?"dtosEncuesta":"dtosSeco", DtoFinder.getInstance().loadDtos(accion.equals(EAccion.PROCESAR)?this.attrs.get("nombreEncuesta").toString():Constantes.NOMBRE_));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } // doLoadDatos

  protected void obtenerCamposDto(){
    Class dto            = null;
    List<Campo> agregados= null;
    List<UISelectItem> source= null;
    List<UISelectItem> target= null;
    try {
      this.campos= new ArrayList();
      source = new ArrayList();
      target = new ArrayList();
      agregados   = new ArrayList();
      if(this.attrs.get("dto") instanceof String){
        dto= obtenerClaseDto(this.attrs.get("dto").toString());
          for(Field field: dto.getDeclaredFields()){
            if(field.getName().indexOf("serialVersionUID")<0)
              this.campos.add(new Campo(field.getName(), field.getType(), field.getDeclaringClass(),field.getDeclaringClass().getSimpleName().concat(".").concat(field.getName())));
          } // for
          for(Campo campo: this.campos){
            if(isCampoId(campo)){
              dto= buscarDto(campo.getNombre());
              if(dto!=null)
                for(Field field: dto.getDeclaredFields()){
                  if(field.getName().indexOf("serialVersionUID")<0)
                    agregados.add(new Campo(field.getName(), field.getType(), field.getDeclaringClass(),field.getDeclaringClass().getSimpleName().concat(".").concat(field.getName())));
                } // for
            } // if
          } // for
          for(Campo campo:agregados){
            if(this.campos.indexOf(campo)<0)
              this.campos.add(campo);
          } // for
      }
      else
        for(String clase: ((String[])this.attrs.get("dto"))){
          dto= obtenerClaseDto(clase);
          for(Field field: dto.getDeclaredFields()){
            if(field.getName().indexOf("serialVersionUID")<0)
              this.campos.add(new Campo(field.getName(), field.getType(), field.getDeclaringClass(),field.getDeclaringClass().getSimpleName().concat(".").concat(field.getName())));
          } // for
          for(Campo campo: this.campos){
            if(isCampoId(campo)){
              dto= buscarDto(campo.getNombre());
              if(dto!=null)
                for(Field field: dto.getDeclaredFields()){
                  if(field.getName().indexOf("serialVersionUID")<0)
                    agregados.add(new Campo(field.getName(), field.getType(), field.getDeclaringClass(),field.getDeclaringClass().getSimpleName().concat(".").concat(field.getName())));
                } // for
            } // if
          } // for
          for(Campo campo:agregados){
            if(this.campos.indexOf(campo)<0)
              this.campos.add(campo);
          } // for
        }
      for(Campo campo: this.campos){
        source.add(new UISelectItem(campo.getLlave(), campo.getLlave()));
      } // for
      this.pickList= new DualListModel<>(source, target);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } // obtenerCamposDto


  private Class obtenerClaseDto(String clase) throws Exception{
    Class regresar             = null;
    Iterator<Class<?>> iterator= null;
    try {
      iterator = ((Set<Class<?>>)this.attrs.get("dtosEncuesta")).iterator();
      while (iterator.hasNext()) {
        regresar=((Class<?>)iterator.next());
        if(regresar.getSimpleName().equals(clase.substring(clase.lastIndexOf(".")+1)))
          break;
      } // while
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // obtenerClaseDto

  private Class buscarDto(String campoId) throws Exception{
    Class regresar                = null;
    Iterator<Class<?>> iterator   = null;
    StringBuilder idDelimitado    = null;
    String [] elementosId         = null;
    try {
      idDelimitado= new StringBuilder();
      for(int i=0; i< campoId.length(); i++){
        if(isMayuscula(campoId.substring(i,i+1))){
          idDelimitado.append(",");
        } // if
        idDelimitado.append(campoId.substring(i,i+1));
      } // for
      elementosId= idDelimitado.toString().substring(3).split(",");
      iterator = ((Set<Class<?>>)this.attrs.get("dtosEncuesta")).iterator();
      regresar= revisaListado(iterator, elementosId);
      if(regresar==null){
        doLoadDtos(EAccion.REPROCESAR);
        iterator = ((Set<Class<?>>)this.attrs.get("dtosSeco")).iterator();
        regresar= revisaListado(iterator, elementosId);
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // obtenerClaseDto

  private boolean isMayuscula(String caracter){
    boolean regresar= false;
    String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    try {
      if(mayusculas.indexOf(caracter)>=0)
        regresar=true;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public Class revisaListado(Iterator iterator, String [] elementosId){
    Class regresar                = null;
    String nombreDto              = null;
    String [] elementosNombre     = null;
    StringBuilder nombreDelimitado= null;
    boolean encontrado            = false;
    try {
      while (iterator.hasNext()) {
        regresar=((Class<?>)iterator.next());
        nombreDto= regresar.getSimpleName();
        nombreDelimitado= new StringBuilder();
        for(int i=nombreDto.substring(0, 2).equals("Rh")?4:2; i< nombreDto.length(); i++){
          if(isMayuscula(nombreDto.substring(i,i+1))){
            nombreDelimitado.append(",");
          } // if
          nombreDelimitado.append(nombreDto.substring(i,i+1));
        } // for
        elementosNombre= nombreDelimitado.toString().substring(1).split(",");
        if(elementosId.length == (elementosNombre.length-1))
          for(int i=0; i<elementosId.length; i++){
            if(elementosNombre[i].indexOf(elementosId[i])!=-1)
              encontrado= true;
            else{
              encontrado= false;
              break;
            }
          } // for
        if(encontrado)
            break;
        else
          regresar=null;
      } // while
    }  // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  } // revisaListado

  public boolean isCampoId(Campo campo){
    return (campo.getNombre().substring(0,2).equals("id") && isMayuscula(campo.getNombre().substring(2,3)));
  }
}
