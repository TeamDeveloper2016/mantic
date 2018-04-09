package mx.org.kaana.xml;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Nov 17, 2010
 * @time 9:52:19 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.init.Settings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

public final class Modulos {
	
  public enum Sections {
    POJOS, DMLS, BOTH
  };
	
  public enum Paths {
     PACKAGE, MANAGED_BEANS, MESSAGES
  };

  private static final Log LOG = LogFactory.getLog(Modulos.class);
  private static final String ROOT = "modulos";
  private static final String MODULE = "module";
  private static final String ENTITY = "entity";
  private static final String UNIT = "/process/dml/unit[@id=''{0}'']";
  private static final String KEY_ENTITY = "/process/model/entity";
  private static final String KEY_POJOS = "model";
  private static final String KEY_UNIT = "/process/dml/unit";
  private static final String KEY_DMLS = "dml";
  private Document source;
  private String modules;
  private Sections section;

  public Modulos(String modules) {
    this.modules= modules;
  }

  public Modulos(Document source, String modules) {
    this(source, modules, Sections.BOTH);
  }

  public Modulos(Document source, String modules, Sections section) {
    this.source = source;
    this.modules= modules;
    this.section= section;
  }

  private NodeList toModules(Document document) {
    NodeList regresar = null;
    try {
      regresar = document.getElementsByTagName(MODULE);
      LOG.debug("leyendo los nombres de los modulos " + regresar.getLength());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  private boolean init(Document document) {
    boolean regresar = false;
    try {
      NodeList items = document.getElementsByTagName(ROOT);
      if(items.getLength()> 0) {
        Element element=  (Element)items.item(0);
        regresar= element.getAttribute(Constantes.XML_PROYECTO_LOAD)!= null && Boolean.valueOf(element.getAttribute(Constantes.XML_PROYECTO_LOAD));
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    LOG.warn("Parser el archivo de configuracion ".concat(this.modules).concat(" del proyecto ")+ regresar);
    return regresar;
  }

  private boolean checkXmlFile(Document document, boolean validate) {
    boolean regresar = false;
    String key = "";
    try {
      if (validate) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList list = document.getElementsByTagName(ENTITY);
        for (int x = 0; regresar && x < list.getLength(); x++) {
          key = ((Element) list.item(x)).getAttribute("id");
          xpath.evaluate(MessageFormat.format(UNIT, new Object[]{key}), document, XPathConstants.STRING);
        } // for x
      } // if
      regresar = true;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      LOG.info("El submodulo [".concat(key).concat("] no esta correctamente implementado en el XML, falta el dml."));
    } // catch
    return regresar;
  }

  private Element getFirstElement(String key) {
    Element regresar = null;
    try {
      NodeList items = this.source.getElementsByTagName(key);
      if (items.getLength() > 0) {
        LOG.debug("key element [".concat(key).concat("] ").concat(items.item(0).getNodeName()));
        regresar = (Element) items.item(0);
      } // for x
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  private void loadElements(Document dmls, String key, String children) {
    try {
      XPath xpath   = XPathFactory.newInstance().newXPath();
      NodeList items= (NodeList) xpath.evaluate(children, dmls, XPathConstants.NODESET);
      if (items!= null && items.getLength()> 0) {
        Element first = getFirstElement(key);
        LOG.info("Inicializando "+ items.getLength() + " ".concat(key));
        for (int x= 0; x < items.getLength(); x++) {
          first.appendChild(this.source.importNode(items.item(x), true));
          } // for x
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public List<IBaseDto> toDetailXml() {
    DocumentBuilderFactory fabrica= null;
    DocumentBuilder builder       = null;
    String nameXml                = null;
    List<IBaseDto> regresar       = new ArrayList<>();
    NodeList filesXml             = null;
    Element item                  = null;
    InputStream in                = null;
    try {
      fabrica = DocumentBuilderFactory.newInstance();
      builder = fabrica.newDocumentBuilder();
      LOG.debug("Procesando los modulos ".concat(this.modules));
      if (this.source != null && this.modules != null) {
        in= this.getClass().getResourceAsStream(this.modules);
        if(in== null)
          in= this.getClass().getResourceAsStream(Settings.getInstance().toDefaultModules());
        Document files = builder.parse(in);
        NodeList names = toModules(files);
        for (int z = 0; z < names.getLength(); z++) {
          item = (Element) names.item(z);
          filesXml = item.getElementsByTagName("file-xml");
          for (int x = 0; x < filesXml.getLength(); x++) {
            nameXml = filesXml.item(x).getTextContent();
            nameXml = Cadena.reemplazarCaracter(nameXml, '/', File.separatorChar);
            if (item.getAttribute(Constantes.XML_PROYECTO_LOAD)!= null && Boolean.valueOf(item.getAttribute(Constantes.XML_PROYECTO_LOAD)))
              regresar.addAll(readNodos( builder, nameXml,item.getAttribute(Constantes.XML_PROYECTO_ID).toUpperCase()));
          } // for x
        } // for y
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally{
      return regresar;
      //Methods.clean(regresar);
    }//finallyreturn regresar;
  } // toDetailXml

    public List<IBaseDto> readNodos(DocumentBuilder builder, String nameXml, String proyecto)throws Exception{
      Document dmls          = null;
      XPath xpath            = null;
      NodeList items         = null;
      String path            = null;
      List<IBaseDto> regresar= new ArrayList<>();
      IBaseDto ibaseDto      = null;
      Constructor contructor = null;
      InputStream inputStream= null;
      if(proyecto.compareTo("COMUN")==0)
        path= (this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).replace("build/web/WEB-INF/lib/.jar", "src/java/"+nameXml);
      else
        path= (this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).replace("build/web/WEB-INF/lib/.jar", proyecto+"/src/"+nameXml);
      xpath= XPathFactory.newInstance().newXPath();
      inputStream= new FileInputStream(path);
      dmls= builder.parse(inputStream);
      items= (NodeList) xpath.evaluate(KEY_UNIT, dmls, XPathConstants.NODESET);
      LOG.debug("Leyendo "+path);
      if (items!= null && items.getLength()> 0)
        for (int x= 0; x < items.getLength(); x++) {
          for(int i=0; i<items.item(x).getChildNodes().getLength(); i++){
            Node tempNode = items.item(x).getChildNodes().item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
              contructor= Class.forName("mx.org.kaana.kajool.db.dto.TrSentenciasArchivoXmlDto").getConstructor(new Class []{String.class, Long.class , String.class, String.class,String.class});
              ibaseDto= (IBaseDto) contructor.newInstance( tempNode.getNodeName(), new Long(-1L), path,((Element) items.item(x)).getAttribute("id"), ((Element) tempNode).getAttribute("id"));
              regresar.add(ibaseDto);
            }// if
          } // for i
        } // for x
      return regresar;
  }// readNodos

  public void load(List<String> lista, final String ruta, Paths tipo) {		
    DocumentBuilderFactory fabrica= null;
    DocumentBuilder builder       = null;
    try {
      fabrica = DocumentBuilderFactory.newInstance();
      builder = fabrica.newDocumentBuilder();
      InputStream in= this.getClass().getResourceAsStream(this.modules);
      if(in!= null) {
        Document files= builder.parse(in);
        if(init(files)) {
          NodeList names= toModules(files);
          for (int z= 0; z< names.getLength(); z++) {
            Element item= (Element) names.item(z);
            if (item.getAttribute(Constantes.XML_PROYECTO_LOAD)!= null && Boolean.valueOf(item.getAttribute(Constantes.XML_PROYECTO_LOAD)))
              if (lista== null)
                lista= new ArrayList<>();
              else {
                String path= toAttribute(item, tipo);
                if (path!= null)
                  lista.add(path);
              } // else					
          } // for
        } // if
      } // if
      else
        LOG.warn("archivo de configuracion no existe o tiene otro nombre ".concat(this.modules));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }
	
	private String toAttribute(Element item, Paths tipo) {
		String regresar= null;
		try {
			switch(tipo){
				case PACKAGE:
					if (Boolean.valueOf(item.getAttribute(Constantes.XML_PROYECTO_MAPPING)))
						regresar= item.getAttribute(Constantes.XML_PROYECTO_DTO);
					break;
				case MANAGED_BEANS:
					if (Boolean.valueOf(item.getAttribute(Constantes.XML_PROYECTO_BEANS)))
						regresar= item.getAttribute(Constantes.XML_PROYECTO_ID);
					break;
				case MESSAGES:					
						regresar= item.getAttribute(Constantes.XML_PROYECTO_ID).concat(Constantes.TILDE).concat(item.getAttribute(Constantes.XML_PROYECTO_MSG));
					break;
			} // switch					
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
		return regresar;
	}

  public void toBuild() {
    DocumentBuilderFactory fabrica= null;
    DocumentBuilder builder       = null;
    String nameXml                = null;
    InputStream fileInputStream   = null;
    try {
      fabrica = DocumentBuilderFactory.newInstance();
      builder = fabrica.newDocumentBuilder();
      LOG.debug("Procesando los modulos ".concat(this.modules));
      if (this.source != null && this.modules != null) {
        InputStream in= this.getClass().getResourceAsStream(this.modules);
        // kajool default modulos.xml
        if(in== null)
          in= this.getClass().getResourceAsStream(Settings.getInstance().toDefaultModules());
        Document files = builder.parse(in);
        NodeList names = toModules(files);
        for (int z = 0; z < names.getLength(); z++) {
          Element item = (Element) names.item(z);
          NodeList filesXml = item.getElementsByTagName("file-xml");
          for (int x = 0; x < filesXml.getLength(); x++) {
            nameXml = filesXml.item(x).getTextContent();
            nameXml = Cadena.reemplazarCaracter(nameXml, '/', File.separatorChar) ;
            String logged = "Leyendo el modulo [{0}] load: {1} mapping: {2} beans: {3} file: {4}";
            LOG.info(MessageFormat.format(logged, new Object[]{
              item.getAttribute(Constantes.XML_PROYECTO_ID),
              item.getAttribute(Constantes.XML_PROYECTO_LOAD),
              item.getAttribute(Constantes.XML_PROYECTO_MAPPING),
              item.getAttribute(Constantes.XML_PROYECTO_BEANS),
              nameXml}));
            boolean exists = item.getAttribute(Constantes.XML_PROYECTO_LOAD)!= null && Boolean.valueOf(item.getAttribute(Constantes.XML_PROYECTO_LOAD));
            if (exists) {
              fileInputStream = getClass().getResourceAsStream("/".concat(nameXml));
              if (fileInputStream!=null) {
                loadFiles(fileInputStream , builder, item, nameXml) ;
              } // if
              else {
								String  path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
								path= path.substring(0,path.lastIndexOf("/")+1).concat(item.getAttribute(Constantes.XML_PROYECTO_ID).toUpperCase().concat(".jar"));
								try {
									URL url = new URL("jar:file:".concat(path).concat("!").concat("/").concat((nameXml.replace(" ","%20")).replace(File.separator, "/")));
									InputStream is = url.openStream();
									loadFiles(is , builder, item, nameXml) ;
								} // try
								catch(Exception e) {
									LOG.info("No se puede encontrar el archivo [".concat(nameXml).concat("]"));
								} // catch
							} // else
            } // if
          }
        } // for y
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } // sections

	private void loadFiles(InputStream inputStream ,DocumentBuilder builder, Element item, String nameXml) {
		try {
			Document dmls = builder.parse(inputStream);
			if (checkXmlFile(dmls, true))
				loadNodes(dmls);
			else
				LOG.info("El archivo es inconsistente en estructura [".concat(item.getAttribute("id")).concat("] file: ").concat(nameXml.trim()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
	}

  public void toBuildString(String xmlString) {
    Document dmls                  = null;
    DocumentBuilderFactory fabrica = null;
    DocumentBuilder builder        = null;
    try {
      fabrica= DocumentBuilderFactory.newInstance();
      builder= fabrica.newDocumentBuilder();
      dmls   = builder.parse( new InputSource( new StringReader( xmlString ) ) );
      if (checkXmlFile(dmls, true))
        loadNodes(dmls);
      else
        LOG.info("El archivo es inconsistente en estructura] ");
    }
    catch(Exception e) {
      Error.mensaje(e);
    }
  }

  private void loadNodes(Document dmls) throws Exception {
    switch (this.section) {
      case POJOS:
        LOG.debug("procesando pojos con xpath ".concat(KEY_ENTITY));
        loadElements(dmls, KEY_POJOS, KEY_ENTITY);
        break;
      case DMLS:
        LOG.debug("procesando dmls con xpath".concat(KEY_UNIT));
        loadElements(dmls, KEY_DMLS, KEY_UNIT);
        break;
      case BOTH:
        LOG.debug("procesando pojos con xpath ".concat(KEY_ENTITY));
        loadElements(dmls, KEY_POJOS, KEY_ENTITY);
        LOG.debug("procesando dmls con xpath".concat(KEY_UNIT));
        loadElements(dmls, KEY_DMLS, KEY_UNIT);
        break;
    } // switch
  }

  @Override
  public String toString() {
    String regresar = null;
    DOMImplementationRegistry registry = null;
    DOMImplementationLS domImpl = null;
    LSSerializer writer = null;
    try {
      registry = DOMImplementationRegistry.newInstance();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    domImpl = (DOMImplementationLS) registry.getDOMImplementation("LS");
    writer  = domImpl.createLSSerializer();
    regresar= writer.writeToString(this.source);
    return regresar;
  }	
}
