package mx.org.kaana.mantic.enums;

/**
 *@company KAANA
 *@project IKTAN (Sistema de Seguimiento y Control de proyectos estadísticos)
 *@date 03/04/2018
 *@time 12:26 PM
 *@author Jorge Alberto Vazquez Serafin <jorge.vazquezser@inegi.org.mx>
 */

public enum EExcepciones {

	MANTIC;
	
  public String getKey() {
    return this.name().toLowerCase();
  }
}
