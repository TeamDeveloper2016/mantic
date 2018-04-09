package mx.org.kaana.kajool.procesos.indicadores.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 01:20:56 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.ENumerosIndicador;
import mx.org.kaana.libs.formato.Numero;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolIndicadoresFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= -1906955492872452615L;

  @PostConstruct
  @Override
	protected void init() {
    try {			
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  @Override
  public void doLoad() {
		List<Columna>campos= null;
    try {
      campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("porcCompletos", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      this.lazyModel= new FormatLazyModel("VistaIndicadorDto", "totales", this.attrs, campos);
			UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(campos);
		} // finally
  } // doLoad

  public boolean doPintado(String numero, Value porcentaje){
    boolean regresar           = false;
    Double valor               = null;
    ENumerosIndicador indicador= null;
    try {
      if(porcentaje!= null){
        indicador= ENumerosIndicador.valueOf(numero);
        valor= Double.valueOf(porcentaje.toString());
        regresar= valor > indicador.getPorcentaje();
      } // if
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    return regresar;
  } // doPintado
  
  public String toTotal(String entity) {
		long total         = 0L;
		String regresar    = "";
		List<Entity>records= null;
		try {
			records= (List<Entity>) this.lazyModel.getWrappedData();
			for(Entity record: records)
				total= total+ Long.valueOf(record.toString(entity));
		  regresar= Numero.formatear(Numero.MILES_SIN_DECIMALES, total);
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // toTotal
  
  public String toPorcentaje(String entity) {
		double total         = 0L;
    int count          = 0;
		String regresar    = "";
		List<Entity>records= null;
		try {
			records= (List<Entity>) this.lazyModel.getWrappedData();
			for(Entity record: records) {
				total= total+ Double.valueOf(record.toString(entity));
        count++;
      } // for   
		  regresar= Numero.formatear(Numero.MILES_CON_DECIMALES, total/ count)+ "%";
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // toPorcentaje
  
}
