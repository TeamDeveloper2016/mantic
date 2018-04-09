package mx.org.kaana.libs.pagina.convertidores.entity;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Sep 11, 2013
 * @time 3:59:25 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.UISelectItemIndicador;

@FacesConverter("janal.convertidor.Dual")
public class Dual implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		String[] itemValues     = value.split(Constantes.TILDE);
		Object itemKey          = itemValues[0];
    String itemLabel        = itemValues[1];
    boolean disable         = Boolean.valueOf(itemValues[2]);
    String indicadorGrafica = itemValues[3];
    String indicadorFavorito= itemValues[4];
    boolean vencido         = Boolean.valueOf(itemValues[5]);
    boolean inactivo        = Boolean.valueOf(itemValues[6]);
    boolean activo          = Boolean.valueOf(itemValues[7]);
		return new UISelectItemIndicador(itemKey, itemLabel, disable, indicadorGrafica, indicadorFavorito, vencido, inactivo, activo);		
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object value) {
		String regresar= null;
		if(value!= null) {
			UISelectItemIndicador item= (UISelectItemIndicador) value;
			regresar= item.toString();
		} // if
	  return regresar;
	}
}
