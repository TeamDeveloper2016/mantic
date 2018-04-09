
package mx.org.kaana.kajool.reglas.comun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EFiltersWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.SortOrder;
import mx.org.kaana.libs.formato.Error;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 27/05/2015
 *@time 01:46:23 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FormatCustomLazy extends FormatLazyModel<IBaseDto> {

  private static final Log LOG = LogFactory.getLog(FormatCustomLazy.class);
	private static final long serialVersionUID=-6690889390042439951L;

  private EFiltersWith like;

  public FormatCustomLazy(String proceso, Map<String, Object> params, List<Columna> columns) {
    this(proceso, "lazy", params, columns,-1L);
  }
	
  public FormatCustomLazy(String proceso, Map<String, Object> params, List<Columna> columns, Long idFuenteDato) {
    this(proceso, "lazy", params, columns,idFuenteDato);
  }

  public FormatCustomLazy(String proceso, String idXml, Map<String, Object> params, List<Columna> columns) {
    this(proceso, idXml, params, columns, EFiltersWith.START_WITH,-1L);
  }
	
	public FormatCustomLazy(String proceso, String idXml, Map<String, Object> params, List<Columna> columns, Long idFuenteDato) {
    this(proceso, idXml, params, columns, EFiltersWith.START_WITH);
  }

  public FormatCustomLazy(String proceso, String idXml, Map<String, Object> params, List<Columna> columns, EFiltersWith like) {
    this(proceso, idXml, params, columns, like,-1L);
  }

	
	public FormatCustomLazy(String proceso, String idXml, Map<String, Object> params, List<Columna> columns, EFiltersWith like, Long idFuenteDato) {
    super(proceso, idXml, params, columns,idFuenteDato);
    this.like= like;
  }
	
  @Override
  public List<IBaseDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
    this.getParams().put("filters", "");
    if(!this.getParams().containsKey("sortOrder"))
      this.getParams().put("sortOrder", "");
    if(sortField!= null)
      this.getParams().put("sortOrder", "order by ".concat(this.format(sortField)).concat(SortOrder.DESCENDING.equals(sortOrder)? " desc": ""));
    if(filters.size()> 0)
      this.getParams().put("filters", " and (".concat(toFilters(filters)).concat(")"));
    LOG.info("Lazy params: "+ this.getParams());
    return super.load(first, pageSize, sortField, sortOrder, filters);
  }

  protected String toFilters(Map<String, Object> filters, EFiltersWith like) {
    StringBuilder regresar= new StringBuilder("");
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      for (String key : filters.keySet()) {
        params.put("value", filters.get(key));
        String start= this.format(key);
        if(start.startsWith("$"))
          regresar.append(Cadena.replaceParams(start.substring(1), params));
        else
          regresar.append(start).append(Cadena.replaceParams(this.like.getFormat(), params));
        regresar.append(" and ");
      } // for
      if(regresar.length()> 3)
        regresar.delete(regresar.length()- 5, regresar.length());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar.toString();
  }

  protected String toFilters(Map<String, Object> filters) {
    return toFilters(filters, this.like);
  }

  private String format(String msg) {
    String regresar= Cadena.eliminar(msg, (char)39);
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("punto", ".");
      params.put("apostrofe", "'");
      params.put("fecha", "'dd/mm/yyyy'");
      params.put("hora", "'HH:MM:SS'");
      params.put("registro", "'dd/mm/yyyy HH:MM:SS'");
      regresar= Cadena.toSqlName(Cadena.replaceParams(regresar, params, true));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
}
