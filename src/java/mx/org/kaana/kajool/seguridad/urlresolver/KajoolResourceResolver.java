package mx.org.kaana.kajool.seguridad.urlresolver;

import java.net.URL;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ResourceResolver;
import mx.org.kaana.libs.Constantes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Arrays;

/**
 * @company Instituto Nacional de Estadistica y Geografia
 * @project KAJOOL (Control system polls)
 * @date 20/03/2014
 * @time 04:32:51 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolResourceResolver extends ResourceResolver {

  private static final Log LOG = LogFactory.getLog(KajoolResourceResolver.class);

  private static final String RESOURCES_PATH = "mx.org.kaana.seguridad.urlresolver.RESOURCES_PATH";
  private ResourceResolver parent;
  private String[] basePath;

  public KajoolResourceResolver(ResourceResolver parent) {
    this.parent  = parent;
    this.basePath= parseFromConfig();
    LOG.info(" configurando url resolver "+ Arrays.asList(this.basePath));
  }

  @Override
  public URL resolveUrl(String path) {
    URL regresar = parent.resolveUrl(path); // use default search first
    if (regresar == null) {
      // checking custom locations in order they are specified in web.xml (web-fragment.xml)
      LOG.debug(" recurso solicitado url: "+ path);
      for (int i = 0; i < basePath.length; ++i) {
        regresar = getClass().getClassLoader().getResource(basePath[i]+ path);
        if (regresar != null)
          return regresar;
      } // for
    } // if
    return regresar;
  }

  /* NOMBRE_RESOURCES esta constante almanca el nombre de los recursos para las paginas .xhtml */
  private static String[] parseFromConfig() {
    FacesContext ctx    = FacesContext.getCurrentInstance();
    String value = ctx != null ? ctx.getExternalContext().getInitParameter(RESOURCES_PATH) : null;
    if (value != null) {
      String[] tokens = value.split("[".concat(Constantes.SEPARADOR).concat("]"));
      for (int i = 0; i < tokens.length; ++i) {
        if (tokens[i] == null)
          tokens[i] = "";
        else
          tokens[i] = tokens[i].trim();
      } // for
      return tokens;
    } // if
    else
      return new String[] {Constantes.NOMBRE_RESOURCES, "/META-INF/resources"};
  }

}
