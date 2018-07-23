package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "manticCatalogosAlmacenesTransferenciasFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("clave", "");
			this.attrs.put("nombre", "");
      this.attrs.put("codigoArticulo", "");
			this.attrs.put("nombreArticulo", "");
      this.attrs.put("sortOrder", "order by tc_mantic_transferencias.registro");
      this.attrs.put("idPrincipal", 1L);
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> campos = null;
    try {
      campos = new ArrayList<>();
      campos.add(new Columna("nombreOrigen", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("nombreDestino", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      campos.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      campos.add(new Columna("solicito", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaAlmacenesTransferenciasDto", "row", this.attrs, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
    } // finally		
  } // doLoad

  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idTransferencia", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion
  
}
