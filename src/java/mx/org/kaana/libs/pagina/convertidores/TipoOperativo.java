package mx.org.kaana.libs.pagina.convertidores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.convertidores.beans.ClaveOperativo;



/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Nov 1, 2012
 *@time 8:29:44 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


@FacesConverter("janal.convertidor.TipoOperativo")
public class TipoOperativo implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
		String clave[] = string.split(Constantes.SEPARADOR_SPLIT);
		return new ClaveOperativo(Numero.getLong(clave[1]), clave[0]);
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object value) {
		String regresar= null;
		if(value!= null) {
			ClaveOperativo item = (ClaveOperativo)value;
			regresar = item.getClave().concat(Constantes.SEPARADOR).concat(item.getIdTipo().toString());
		}
	  return regresar;
	}

}
