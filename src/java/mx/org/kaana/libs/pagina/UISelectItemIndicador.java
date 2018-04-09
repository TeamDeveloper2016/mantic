package mx.org.kaana.libs.pagina;

import mx.org.kaana.libs.Constantes;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/06/2015
 *@time 11:22:53 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class UISelectItemIndicador extends UISelectItem {
	
	private static final long serialVersionUID= 7683721345254968758L;
	private String indicadorGrafica;
	private String indicadorFavorito;
	private boolean vencido;
	private boolean inactivo;
	private boolean activo;
	
	public UISelectItemIndicador(Object key, String label) {
		this(key, label, false, null, null, false, false, false);
	}	

	public UISelectItemIndicador(Object key, String label, boolean disable, String indicadorGrafica, String indicadorFavorito, boolean vencido, boolean inactivo, boolean activo) {
		super(key, label, disable);
		this.indicadorGrafica = indicadorGrafica;
		this.indicadorFavorito= indicadorFavorito;
		this.vencido          = vencido;
		this.inactivo         = inactivo;
		this.activo           = activo;
	}

	public String getIndicadorGrafica() {
		return indicadorGrafica;
	}

	public void setIndicadorGrafica(String indicadorGrafica) {
		this.indicadorGrafica= indicadorGrafica;
	}

	public String getIndicadorFavorito() {
		return indicadorFavorito;
	}

	public void setIndicadorFavorito(String indicadorFavorito) {
		this.indicadorFavorito= indicadorFavorito;
	}
	
	public boolean isVencido() {
		return vencido;
	}

	public void setVencido(boolean vencido) {
		this.vencido= vencido;
	}

	public boolean isInactivo() {
		return inactivo;
	}

	public void setInactivo(boolean inactivo) {
		this.inactivo= inactivo;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo= activo;
	}	

	@Override
	public String toString() {
		StringBuilder regresar= new StringBuilder();
		regresar.append(getValue());
		regresar.append(Constantes.TILDE);
		regresar.append(getLabel());
		regresar.append(Constantes.TILDE);
		regresar.append(isDisabled());
		regresar.append(Constantes.TILDE);
		regresar.append(getIndicadorGrafica());
		regresar.append(Constantes.TILDE);
		regresar.append(getIndicadorFavorito());
		regresar.append(Constantes.TILDE);
		regresar.append(isVencido());
		regresar.append(Constantes.TILDE);
		regresar.append(isInactivo());
		regresar.append(Constantes.TILDE);
		regresar.append(isActivo());			
		return regresar.toString();
	}	
}
