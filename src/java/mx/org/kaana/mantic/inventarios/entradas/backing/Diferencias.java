package mx.org.kaana.mantic.inventarios.entradas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "manticNotasEntradasDiferencias")
@ViewScoped
public class Diferencias extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;
	
	protected FormatLazyModel lazyDevoluciones;

	public FormatLazyModel getLazyDevoluciones() {
		return lazyDevoluciones;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada"));
		  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_SIN_DECIMALES));      
      columns.add(new Columna("costo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));      
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      this.attrs.put("sortOrder", "order by tc_mantic_devoluciones.consecutivo, tc_mantic_notas_detalles.nombre");
      this.lazyDevoluciones = new FormatCustomLazy("VistaDevolucionesDto", "consulta", this.attrs, columns);
      columns.add(new Columna("cantidades", EFormatoDinamicos.NUMERO_SIN_DECIMALES));      
      this.attrs.put("sortOrder", "order by tc_mantic_notas_detalles.nombre");
      this.lazyModel = new FormatCustomLazy("VistaDevolucionesDto", "confronta", this.attrs, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad
	
	public String doRegresar() {
		JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
		return "filtro".concat(Constantes.REDIRECIONAR);
	}
	
}
