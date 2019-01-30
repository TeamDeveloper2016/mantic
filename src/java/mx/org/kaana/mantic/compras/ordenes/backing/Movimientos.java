package mx.org.kaana.mantic.compras.ordenes.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.enums.ETipoMovimiento;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/06/2018
 *@time 07:51:53 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "manticComprasOrdenesMovimientos")
@ViewScoped
public class Movimientos extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;

  private ETipoMovimiento tipo;
	
	public String getTitle() {
		return this.tipo.getTitle();
	}
		
	public boolean getVisible() {
	  return !ETipoMovimiento.TRANSFERENCIAS.equals(this.tipo);
	}
	
	public String doTransporto(Entity row) {
		Value regresar= null;
		try {
		  regresar= row.toLong("idTransporto")!= null && row.toLong("idTransporto")> 0L? DaoFactory.getInstance().toField("VistaAlmacenesTransferenciasDto", "transporto", row.toMap(), "nombre"): null;
		} // try
		catch (Exception e) {
			Error.mensaje(e);
      JsfBase.addMessageError(e);
		} // catch
		return regresar== null? "": regresar.toString();
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.tipo= JsfBase.getFlashAttribute("tipo")== null? ETipoMovimiento.ORDENES_COMPRAS: (ETipoMovimiento)JsfBase.getFlashAttribute("tipo");
      this.attrs.put(this.tipo.getIdKey(), JsfBase.getFlashAttribute(this.tipo.getIdKey()));
      this.attrs.put("regreso", JsfBase.getFlashAttribute("regreso"));
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
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("justificacion", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
      this.attrs.put("sortOrder", "order by ".concat(this.tipo.getTable()).concat(".registro desc"));
      this.lazyModel = new FormatCustomLazy(this.tipo.getProceso(), "movimientos", this.attrs, columns);
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
		JsfBase.setFlashAttribute(this.tipo.getIdKey(), this.attrs.get(this.tipo.getIdKey()));
		return ((String)this.attrs.get("regreso")).concat(Constantes.REDIRECIONAR);
	}
	
}
