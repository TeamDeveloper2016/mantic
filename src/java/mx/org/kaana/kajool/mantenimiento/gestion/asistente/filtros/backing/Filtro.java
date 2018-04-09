package mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 2/12/2014
 * @time 05:22:50 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EComponente;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoDato;
import static mx.org.kaana.kajool.enums.ETipoDato.DATE;
import static mx.org.kaana.kajool.enums.ETipoDato.LONG;
import static mx.org.kaana.kajool.enums.ETipoDato.TEXT;
import mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.reglas.Generador;
import mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Campo;
import mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans.Parametro;
import mx.org.kaana.xml.Dml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@ManagedBean(name = "kajoolMantenimientoGestionAsistenteFiltrosFiltro")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

	private static final Log LOG=LogFactory.getLog(Filtro.class);
	private static final long serialVersionUID=-7247665974788846507L;
	
	private EFormatos idFormato;
	private List<Campo> campos;
	private List<Campo> camposTabla;
	private List<Campo> camposCriterio;
	private List<Campo> camposCombo;
	private List<Parametro> parametros;
	private List<Parametro> parametrosCombo;
	private List<String> ordenar;
	private List<String> busqueda;
	private List<UISelectItem> encuestas;
	private List<UISelectItem> selects;
	private List<UISelectItem> units;
	private List<Entity> displayTabla;

	public List<Campo> getCamposCombo() {
		return camposCombo;
	}

    public List<Campo> getCamposTabla() {
      return camposTabla;
    }

  public List<Campo> getCamposCriterio() {
    return camposCriterio;
  }

  public List<Entity> getDisplayTabla() {
    return displayTabla;
  }

	public List<Parametro> getParametrosCombo() {
		return parametrosCombo;
	}
	
	public EFormatos getIdFormato() {
		return idFormato;
	}

	public List<UISelectItem> getSelects() {
		return selects;
	}

	public List<UISelectItem> getUnits() {
		return units;
	}
	
	public List<Campo> getCampos() {

		return campos;
	}
	
	public List<Parametro> getParametros() {
		return parametros;
	}

	public List<String> getOrdenar() {
		return ordenar;
	}

	public void setOrdenar(List<String> ordenar) {
		this.ordenar=ordenar;
	}

	public List<String> getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(List<String> busqueda) {
		this.busqueda=busqueda;
	}

	public List<UISelectItem> getEncuestas() {
		return encuestas;
	}

	@PostConstruct
	@Override
	protected void init() {
		try {
			this.attrs.put("unit","INSERTAR");
			this.attrs.put("select","row");
			this.attrs.put("archivo", "");
			this.attrs.put("converter", "Fecha");
			this.idFormato = EFormatos.ZIP;
			this.units = new ArrayList<>();
			this.selects = new ArrayList<>();
			this.displayTabla = new ArrayList<>();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = Dml.getInstance().getDocumento();
			if(doc!= null){
        doc.getDocumentElement().normalize();
        if (doc.hasChildNodes())
          loadIdsUnit(doc.getChildNodes());
      } // if
      LOG.debug(JsfUtilities.getFacesContext().getCurrentPhaseId());
			campos =  new ArrayList<>();
			camposTabla =  new ArrayList<>();
			camposCriterio =  new ArrayList<>();
			this.attrs.put("nombreFiltro", "Filtro");
			this.attrs.put(Constantes.SQL_CONDICION, "activo=1");
      if(!Dml.getInstance().getSelect("TcEncuestasDto", "row", this.attrs).equals(""))
        this.encuestas = UISelect.build("TcEncuestasDto", this.attrs, "nombre");
      else{
        this.encuestas = new ArrayList();
        this.encuestas.add(new UISelectItem(-1L, "Teclear proyecto"));
      }
			this.attrs.put("encuesta", UIBackingUtilities.toFirstKeySelectItem(encuestas));
      this.attrs.put("encuestaTexto","");
			this.attrs.put("proceso", "");
			this.attrs.put("autor", "kajool.kaana");
			this.attrs.put("correo", "team.developer@kaana.org.mx");
			doActualizarRuta();
			RequestContext.getCurrentInstance().execute("descargarHide();");
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // init

	public String onFlowProcess(FlowEvent event) {
		FlowEvent nuevoTab;
    String regresar="";
		try {
      regresar= event.getNewStep();
			if(event.getOldStep().equals("tabSql")){
				LOG.debug("Buscando parametros para la consulta");
				parametros = null;
				parametros = obtenerParametros("wizard");
				if(parametros.isEmpty()) {
          nuevoTab =  new FlowEvent(event.getComponent(), "tabParametros", "tabCampos");
					regresar= onFlowProcess(nuevoTab);
				} // if
			} // if
			else if(event.getOldStep().equals("tabParametros") && event.getNewStep().equals("tabCampos")){
				LOG.debug("Analizando consulta sql");
				campos = obtenerCampos("wizard");
				if(campos.isEmpty())
					regresar= event.getOldStep();
			} // else if
			else if(event.getOldStep().equals("tabCampos") && event.getNewStep().equals("tabOrden")){
				ordenar = new ArrayList<>();
				busqueda = new ArrayList<>();
                camposTabla.clear();
                camposCriterio.clear();
				LOG.debug("Obteniento campos de busqueda y tabla");
				for(Campo campo:this.campos) {
					if(campo.getTabla().equals(true)){
						ordenar.add(campo.getNombre());
                        camposTabla.add(campo);
                    }//if
					if(campo.getBusqueda().equals(true)){
						busqueda.add(campo.getNombre());
                        camposCriterio.add(campo);
                    }//if
				} // for
			} // else if
			else if(event.getOldStep().equals("tabOrden") && event.getNewStep().equals("tabDatosGenerales")){
				LOG.debug("Añadiendo orden a lista de campos");
				for(String nombreCampo:this.ordenar){
					for(Campo campo:this.campos) {
						if(campo.getNombre().equals(nombreCampo))
							campo.setOrdenTabla(Numero.getLong(String.valueOf(this.ordenar.indexOf(nombreCampo))));
					} // for
				} // for
				for(String nombreCampo:this.busqueda){
					for(Campo campo:this.campos){
						if(campo.getNombre().equals(nombreCampo))
							campo.setOrdenBusqueda(Numero.getLong(String.valueOf(this.busqueda.indexOf(nombreCampo))));
					} // for
				} // for
              Collections.sort(camposTabla, new Comparator() {
                @Override
                public int compare(Object campo1, Object campo2) {
                  return new Integer(((Campo) campo1).getOrdenTabla().compareTo(new Long(((Campo) campo2).getOrdenTabla())));
                }
              });
              Collections.sort(camposCriterio, new Comparator() {
                @Override
                public int compare(Object campo1, Object campo2) {
                  return new Integer(((Campo) campo1).getOrdenBusqueda().compareTo(new Long(((Campo) campo2).getOrdenBusqueda())));
                }
              });
			} // else if
			if(event.getOldStep().equals("tabSqlCombo")){
				LOG.debug("Buscando parametros para la consulta");
				parametrosCombo = null;
				parametrosCombo = obtenerParametros("dialogo");
				if(parametrosCombo.isEmpty()) {
					nuevoTab =  new FlowEvent(event.getComponent(), "tabParametrosCombo", "tabCamposCombo");
					regresar= onFlowProcess(nuevoTab);
				} // if
			} // if
			else if(event.getOldStep().equals("tabParametrosCombo") && event.getNewStep().equals("tabCamposCombo")){
				LOG.debug("Analizando consulta sql");
				camposCombo = obtenerCampos("dialogo");
				if(camposCombo.isEmpty()){
					regresar= event.getOldStep();
        }
			} // else if
			
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
    RequestContext.getCurrentInstance().execute("centrarDialogo();");
		return regresar;
	} // onFlowProcess
	
	public void doAsignarCampo(){
		try {
			for(Campo campo: this.campos){
				if(campo.getNombre().equals(this.attrs.get("campoSeleccionado").toString())){
					if(this.attrs.get("tipoSelect").equals("UISelect.build"))
						campo.setInitSelectOneMenu("lista".concat(Cadena.letraCapital(campo.getNombre())).concat("= UISelect.build(\"").concat(this.attrs.get("nombreVistaCombo").toString().concat("\", this.attrs, \"").concat(campo.getNombre()).concat("\");")));
					else
						campo.setInitSelectOneMenu("lista".concat(Cadena.letraCapital(campo.getNombre())).concat("= UISelect.free(\"").concat(this.attrs.get("nombreVistaCombo").toString().concat("\", this.attrs, \"").concat(campo.getNombre()).concat("\",\"").concat(this.attrs.get("value").toString()).concat("\");")));
					break;
				} // if
			} // for
			this.attrs.put("unitCombo","INSERTAR UNIT");
			this.attrs.put("selectCombo","row");
			this.attrs.put("sqlCombo","");
			this.attrs.put("nombreVistaCombo","");
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // doAsignarCampo
	
	private String obtenerConsulta(String tipo) throws Exception {
		String regresar							= null;
		Generador generador         = null;
		Map<String,Object> variables= null;
		try {
			if(!parametros.isEmpty() || parametros!=null) {
				variables = new HashMap<>();
				for(Parametro parametro: parametros)
					variables.put(parametro.getNombre(), parametro.getValor());
				generador = new Generador();
				regresar = generador.getSentencia(tipo.equals("wizard")?this.attrs.get("sql").toString():this.attrs.get("sqlCombo").toString(), variables);
			} // if
			else
				regresar = tipo.equals("wizard")?this.attrs.get("sql").toString():this.attrs.get("sqlCombo").toString();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(variables);
		} // finally
		return regresar;
	} // obtener Consulta
	
	public List<Campo> obtenerCampos(String tipo){
		List<Campo> regresar = new ArrayList();
		String sql           = null;
		Entity registro      = null;
		try {
			LOG.debug("Obteniendo campos de consulta");
			sql =  obtenerConsulta(tipo);
			registro = (Entity)DaoFactory.getInstance().toEntity(sql);
      if(registro!= null)
        for(Object campo:registro.toArray()){
          if(((Value)campo).getData() instanceof String){
            regresar.add(new Campo(((Value)campo).getName(),ETipoDato.TEXT));
          }
          else if(((Value)campo).getData() instanceof Long || ((Value)campo).getData() instanceof BigDecimal){
            regresar.add(new Campo(((Value)campo).getName(),ETipoDato.LONG));
          }
          else if(((Value)campo).getData() instanceof Date){
            regresar.add(new Campo(((Value)campo).getName(),ETipoDato.DATE));
          }
        } // for
      else
        JsfUtilities.addMessage("La consulta no devuelve registro para analizar sus campos");
		} // try
		catch (Exception e) {
			JsfUtilities.addMessage("La consulta sql no es correcta, favor de verificarla !!!");
			Error.mensaje(e);
		} // catch
		return regresar;
	} // obtenerCampos
	
	public List<Parametro> obtenerParametros(String tipo) {
		List<Parametro> regresar = null;
		try {
			regresar = new ArrayList<>();
			Generador generador = new Generador();
			for(String parametro: generador.getParametros(tipo.equals("wizard")?this.attrs.get("sql").toString():this.attrs.get("sqlCombo").toString())) {
				regresar.add(new Parametro(parametro));
			} // for
		} // try
		catch (Exception e) {
			JsfUtilities.addMessage("La consulta sql no es correcta");
			Error.mensaje(e);
		} // catch
		return regresar;
	} // obtenerParametros
	
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
			rutaBacking.append(".backing");
			this.attrs.put("rutaBacking", rutaBacking.toString());
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}
	
	private void cargarVariables() throws Exception{
		StringBuilder selects		  		 = null;
		StringBuilder validacion	  	 = null;
		StringBuilder criterios  			 = null;
		StringBuilder columnas			   = null;
		StringBuilder atributos		  	 = null;
		StringBuilder getters		  		 = null;
		StringBuilder formatos  			 = null;
		StringBuilder idsCriterios	   = null;
		StringBuilder inicializaciones = null;
		Generador generador	           = null;
		Map<String,Object> variables   = null;
		boolean isFormatted				  	 = false;
		try {
			this.attrs.put("fecha", Fecha.formatear("dd/MM/yyyy", new Date(Calendar.getInstance().getTimeInMillis())));
			this.attrs.put("fechaCalendar", new Date());
			this.attrs.put("hora", Fecha.formatear("hh:mm aa", new Date(Calendar.getInstance().getTimeInMillis())));
			this.attrs.put("backing", Cadena.toBeanName(this.attrs.get("rutaPagina").toString().replace("/", Constantes.ARCHIVO_PATRON_SEPARADOR).concat(Constantes.ARCHIVO_PATRON_SEPARADOR).concat(this.attrs.get("nombreFiltro").toString())));
			this.attrs.put("paquete",this.attrs.get("rutaBacking").toString());
			selects					 = new StringBuilder();
			validacion			 = new StringBuilder();
			criterios			   = new StringBuilder();
			columnas			   = new StringBuilder();
			atributos			   = new StringBuilder();
			getters			     = new StringBuilder();
			formatos			   = new StringBuilder();
			idsCriterios		 = new StringBuilder();
			inicializaciones = new StringBuilder();
			variables			   = new HashMap<>();
			generador				 = new Generador();
			for(String nombreCampo: this.busqueda) {
				for(Campo campo: this.campos) {
					if(campo.getBusqueda() && nombreCampo.equals(campo.getNombre())) {
						variables.clear();
						variables.put("criterio", campo.getNombre());
						variables.put("alias", campo.getAlias());
						variables.put("backing", this.attrs.get("backing"));
						if(campo.getComponente().equals(EComponente.SELECT_ONE_MENU)) {
							variables.put("initSelectOneMenu", campo.getInitSelectOneMenu());
							variables.put("lista", Cadena.letraCapital(campo.getNombre()));
							atributos.append(generador.getSentencia(EComponente.ATRIBUTOS.getSintaxis(), variables)).append("\n");
							getters.append(generador.getSentencia(EComponente.GETTERS.getSintaxis(), variables)).append("\n");
							idsCriterios.append(campo.getNombre()).append(" ");
						} // if
						else if(campo.getComponente().equals(EComponente.DATE)){
							variables.put("converter",campo.getConverter());
						}
						validacion.append(generador.getSentencia(EComponente.VALIDACION.getSintaxis(), variables)).append(",\n");
						criterios.append(generador.getSentencia(campo.getComponente().getSintaxis(), variables)).append("\n");
						selects.append(campo.getBusqueda()?" ".concat(variables.get("criterio").toString()):"");
						inicializaciones.append(campo.getComponente().equals(EComponente.TEXT_FIELD)?generador.getSentencia(EComponente.INIT_TEXT_FIELD.getSintaxis(), variables):campo.getComponente().equals(EComponente.SELECT_ONE_MENU)?generador.getSentencia(EComponente.INIT_SELECT_ONE_MENU.getSintaxis(), variables):generador.getSentencia(EComponente.INIT_DATE.getSintaxis(), variables)).append("\n");
						break;
					} // if
				} // for
		} // for
			for(String nombreCampo: this.ordenar) {
				for(Campo campo: this.campos) {
					if(campo.getTabla() && nombreCampo.equals(campo.getNombre())) {
						variables.clear();
						variables.put("alias", campo.getAlias());
						variables.put("alineacion", campo.getAlineacion());
						variables.put("contenido", campo.getContenido());
						variables.put("nombre", campo.getNombre());
						columnas.append(generador.getSentencia(EComponente.COLUMN.getSintaxis(), variables)).append("\n");
						if(!campo.getFormato().equals(EFormatoDinamicos.LIBRE)) {
							variables.put("formato",campo.getFormato().name());
							formatos.append(generador.getSentencia(EComponente.FORMAT.getSintaxis(), variables)).append("\n");
							isFormatted = true;
						} // if
						break;
					} // if
				} // for
			} // for
			validacion.delete(validacion.lastIndexOf(",\n")!=-1?validacion.lastIndexOf(",\n"):validacion.length(), validacion.length());
			criterios.delete(criterios.lastIndexOf("<p:spacer/>")!=-1?criterios.lastIndexOf("<p:spacer/>"):criterios.length(), criterios.length());
			columnas.delete(columnas.lastIndexOf("\n")!=-1?columnas.lastIndexOf("\n"):columnas.length(), columnas.length());
			formatos.delete(formatos.lastIndexOf("\n")!=-1?formatos.lastIndexOf("\n"):formatos.length(), formatos.length());
			inicializaciones.delete(inicializaciones.lastIndexOf("\n")!=-1?inicializaciones.lastIndexOf("\n"):inicializaciones.length(), inicializaciones.length());
			this.attrs.put("validaciones", validacion.toString());
			this.attrs.put("criterios", criterios.toString());
			this.attrs.put("columnas", columnas.toString());
			this.attrs.put("selectsCriterios",selects.toString());
			this.attrs.put("atributos", atributos.toString());
			this.attrs.put("getters", getters.toString());
			this.attrs.put("formatos", formatos.toString());
			this.attrs.put("coleccion", isFormatted?"columnas":"Collections.emptyList()");
			this.attrs.put("idsCriterios", idsCriterios.toString());
			this.attrs.put("inicializaciones", inicializaciones.toString());
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(variables);
			Methods.clean(validacion);
			Methods.clean(criterios);
			Methods.clean(columnas);
			Methods.clean(atributos);
			Methods.clean(getters);
			Methods.clean(formatos);
			Methods.clean(selects);
		} // finally
	} // cargarVariables
	
    private Map toMapParams(List<Parametro> lista) {
       Map<String, Object> regresar= null;
      try {
        regresar = new HashMap<>();
        for (Parametro item : lista) {
          regresar.put(item.getNombre(),item.getValor());
        } // for
      } // try
      catch (Exception e) {
        Error.mensaje(e);
        JsfUtilities.addMessageError(e);
      } // catch
      return regresar;
    }

	public void doAceptar() {
		Generador generador = null;
		try {
			cargarVariables();
			generador = new Generador(this.attrs.get("nombreFiltro").toString(), this.attrs.get("rutaPagina").toString(), this.attrs, "Filtro.txt", "ClaseFiltro.txt");
			generador.generar();
			this.attrs.put("patron", generador.getPatron());
			this.attrs.put("textoBacking", generador.getTextoBacking());
			this.attrs.put("textoPagina", generador.getTextoPagina());
			this.attrs.put("archivo",Constantes.ARCHIVO_PATRON_SEPARADOR.concat(this.attrs.get("patron").toString()).concat("Filtro.").concat(EFormatos.ZIP.name().toLowerCase() ));
			if (RequestContext.getCurrentInstance()!=null) {
				RequestContext.getCurrentInstance().update("tab:textoPagina");
				RequestContext.getCurrentInstance().update("tab:textoBacking");
				RequestContext.getCurrentInstance().update("tab:VistaPreliminar");
				RequestContext.getCurrentInstance().execute("descargarShow();");
				JsfUtilities.addMessage("El filtro fue generado correctamente.");
			}

            displayTabla= DaoFactory.getInstance().toEntitySet(Cadena.replaceParams(this.attrs.get("sql").toString(), toMapParams(parametros)), 3L);
            if(displayTabla!=null){
              for(int i=0; i< camposCriterio.size(); i++)
                camposCriterio.get(i).setValorContenido(displayTabla.get(0).toString(camposCriterio.get(i).getNombre()));
            }//if
        } // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // doAceptar
	
	public boolean isString(ETipoDato tipo){
		return tipo.equals(TEXT);
	} // isString
	
	public boolean isDate(ETipoDato tipo){
		return tipo.equals(DATE);
	} // isDate
	
	public boolean isLong(ETipoDato tipo){
		return tipo.equals(LONG);
	} // isLong
	
	public EFormatoDinamicos getEnumerador(String enumerador){
		return EFormatoDinamicos.valueOf(enumerador.toUpperCase());
	} // getEnumerador
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(busqueda);
		Methods.clean(campos);
		Methods.clean(parametros);
		Methods.clean(encuestas);
		Methods.clean(ordenar);
	}

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
	
	public void actualizarSelects(String tipo){
		try {
			this.selects = new ArrayList<>();
			this.attrs.put(tipo.equals("wizard")?"nombreVista":"nombreVistaCombo", this.attrs.get("unit"));						
			Document doc = Dml.getInstance().getDocumento();
			doc.getDocumentElement().normalize();
			XPathFactory xPFabrica = XPathFactory.newInstance();
      XPath xPath = xPFabrica.newXPath();
      Object dato =   xPath.evaluate("/kajool/dml/unit[@id='"+ (tipo.equals("wizard")?this.attrs.get("unit").toString():this.attrs.get("unitCombo").toString())+"']", Dml.getInstance().getDocumento(), XPathConstants.NODESET);
			loadIdsSelect((NodeList)dato);
			if(!selects.isEmpty()){
				this.attrs.put((tipo.equals("wizard")?"select":"selectCombo"), UIBackingUtilities.toFirstKeySelectItem(selects));
				actualizaConsulta(tipo);
			}
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // actualizarSelects
	
	public void actualizaConsulta(String tipo){
		try {
			Document doc = Dml.getInstance().getDocumento();
			doc.getDocumentElement().normalize();
			XPathFactory xPFabrica = XPathFactory.newInstance();
      XPath xPath = xPFabrica.newXPath();
      Object dato =   xPath.evaluate("/kajool/dml/unit[@id='"+(tipo.equals("wizard")?this.attrs.get("unit").toString():this.attrs.get("unitCombo").toString())+"']/select[@id='"+this.attrs.get((tipo.equals("wizard")?"select":"selectCombo"))+"']", Dml.getInstance().getDocumento(), XPathConstants.NODE);
			this.attrs.put(tipo.equals("wizard")?"sql":"sqlCombo",((Node)dato).getTextContent().toString());
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // actualizaConsulta
	
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
			zipName= EFormatos.TXT.toPath().concat(Constantes.ARCHIVO_PATRON_SEPARADOR).concat(this.attrs.get("patron").toString()).concat("Filtro.").concat(EFormatos.ZIP.name().toLowerCase());
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
	
	public void onTabChange(TabChangeEvent event) {
		try {
			if(event.getTab().getTitle().equals("Descargar"))
				RequestContext.getCurrentInstance().execute("descargar();");
      else {
				RequestContext.getCurrentInstance().execute("reload('tab:textoPagina','tab:textoBacking');");
				RequestContext.getCurrentInstance().execute("refrescar();");
			} // else
		} // try
		catch (Exception e) {
			JsfUtilities.addMessageError(e);
			Error.mensaje(e);
		} // catch
  }
	
	public void doAsignarConverter(){
		try {
			for(Campo campo: this.campos){
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
	
	public void doGuardarCampoSeleccionado(String nombre){
		this.attrs.put("campoSeleccionado", nombre);
    RequestContext.getCurrentInstance().execute("centrarDialogo();");
	}

  public void doCheckComponente(Campo campos){
    if(campos.getComponente().equals(EComponente.DATE)){
      doGuardarCampoSeleccionado(campos.getNombre());
      RequestContext.getCurrentInstance().execute("PF('dialogoConverter').show();");
    } // if
    else
      RequestContext.getCurrentInstance().execute("janal.desbloquear()");
  } // doCheckComponente
}
