package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@Named(value = "kajoolEncabezado")
@ViewScoped
public class Encabezado extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 5323749709626263793L;
  private static final Log LOG = LogFactory.getLog(Encabezado.class);

	@Override
	@PostConstruct
	protected void init() {
		this.attrs.put("codigo", "");
		this.attrs.put("buscarPor", "");
		this.attrs.put("buscaPorCodigo", false);
	  this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
	}
	
	@Override
	public void doLoad() {
    List<Columna> columns= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("limiteMedioMayoreo", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("limiteMayoreo", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      this.lazyModel = new FormatCustomLazy("VistaOrdenesComprasDto", (String)this.attrs.get("idXml"), this.attrs, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally			
	}

	public void doChange() {
		String codigo= (String)this.attrs.get("buscarPor");
		boolean buscaPorCodigo= codigo.startsWith(".");
		if(buscaPorCodigo)
			codigo= codigo.trim().substring(1);
		codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
		this.attrs.put("codigo", codigo);
		this.attrs.put("idXml", "porNombre");
		if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
			this.attrs.put("idXml", "porCodigo");
		this.doLoad();
	}

  public void doRowSelectEvent(SelectEvent event) {
		Entity entity= (Entity)event.getObject();
		LOG.debug("doRowSelectEvent: "+ entity.getKey());
  }	
	
}
