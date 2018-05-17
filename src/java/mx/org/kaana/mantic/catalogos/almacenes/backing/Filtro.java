package mx.org.kaana.mantic.catalogos.almacenes.backing;

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
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.bean.RegistroAlmacen;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.Transaccion;

@Named(value = "manticCatalogosAlmacenesFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("clave", "");
			this.attrs.put("nombre", "");
			this.attrs.put("descripcion", "");			
      this.attrs.put("sortOrder", "order by tc_mantic_almacenes.nombre");
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
      campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("calle", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("asentamiento", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("codigoPostal", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("entidad", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("municipio", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaAlmacenesDto", "row", this.attrs, campos);
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
			JsfBase.setFlashAttribute("idAlmacen", eaccion.equals(EAccion.MODIFICAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		RegistroAlmacen registro= null;
    try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			registro= new RegistroAlmacen();
			registro.setIdAlmacen(seleccionado.getKey());
			transaccion= new Transaccion(registro);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar cliente", "El cliente se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar cliente", "Ocurrió un error al eliminar el cliente.", ETipoMensaje.ERROR);								
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminar
}
