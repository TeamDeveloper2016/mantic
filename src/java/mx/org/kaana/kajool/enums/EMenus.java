package mx.org.kaana.kajool.enums;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 05:28:10 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EMenus {

	ENCABEZADO(),
	SENTINEL(),
	MMENU();	

  private EMenus () {
  }

  public String getVariableSesion() {
		return name().toLowerCase();
	}


}
