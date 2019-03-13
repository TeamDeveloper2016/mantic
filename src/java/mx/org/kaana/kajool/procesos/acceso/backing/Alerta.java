package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 13/08/2018
 * @time 10:31:19 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "kajoolAccesoAlerta")
@ViewScoped
public class Alerta extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-8923064340050821460L;

	@Override
	protected void init() {
	}

	public String getCheckCaja() {
			StringBuilder regresar= new StringBuilder("");
			Map<String, Object> params=null;
			try {
				params=new HashMap<>();
    		if(JsfBase.isAdminEncuestaOrAdmin()) {
					regresar.append("<br/><div class=\"\"><table class=\"janal-color-cyan janal-wid-100\"><thead><tr><th class=\"janal-column-center\">Empresa</th><th class=\"janal-column-center\">Caja</th><th class=\"janal-column-center\">Ventas</th><th class=\"janal-column-center\">Saldo</th><th class=\"janal-column-center\">Limite</th><th class=\"janal-column-center\">Registro</th></tr></thead><tbody>");
					if(JsfBase.getAutentifica().getEmpresa().isMatriz())
						params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
					else
						params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
					params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
					List<Entity> cajas= DaoFactory.getInstance().toEntitySet("VistaCierresCajasDto", "global", params);
					for (Entity caja : cajas) {
						regresar.append("<tr><td class=\"janal-column-left janal-color-yellow\">");
						regresar.append(caja.toString("nombreEmpresa"));
						regresar.append("</td><td class=\"janal-column-left janal-color-yellow janal-wid-10\">");
						regresar.append(caja.toString("caja"));
						regresar.append("</td><td class=\"janal-column-right janal-color-yellow janal-wid-13\">");
						regresar.append(Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, Numero.toRedondearSat(caja.toDouble("disponible")+ caja.toDouble("acumulado"))));
						regresar.append("<td class=\"janal-column-right janal-color-cyan janal-wid-13\">");
						regresar.append(Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, Numero.toRedondearSat(caja.toDouble("saldo"))));
						regresar.append("</td><td class=\"janal-column-center janal-color-white janal-wid-13\">");
						regresar.append(Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, Numero.toRedondearSat(caja.toDouble("limite"))));
						regresar.append("</td><td class=\"janal-column-center janal-color-white janal-wid-15\">");
						regresar.append(Global.format(EFormatoDinamicos.FECHA_HORA_CORTA, caja.toTimestamp("registro")));
						regresar.append("</td></tr>");
					} // for
					regresar.append("</tbody></table></div>");
					if(cajas!= null && !cajas.isEmpty())
						UIBackingUtilities.execute("janal.notificacion();");
     		} // if	
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar.toString();
	}
	
}
