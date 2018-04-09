package mx.org.kaana.kajool.reglas.comun.acciones.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 27, 2012
 *@time 10:45:06 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;

public class AccionAccesoRapido implements Serializable {
	
  private static final long serialVersionUID=2902648450852836334L;

  private String action;
  private String icon;
  private String description;
  private String help;
	private String rendered;
	private String helpShort;

  public AccionAccesoRapido(String description, String help, String action, String icon, String rendered, String helpShort){
    this.description = description;
    this.action      = action;
    this.icon        = icon;
    this.help        = help;
		this.rendered    = rendered;
		this.helpShort   = helpShort;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action= action;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description= description;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon= icon;
  }

  public String getHelp() {
    return help;
  }

  public void setHelp(String help) {
    this.help= help;
  }

	public String getRendered() {
		return rendered;
	}

	public void setRendered(String rendered) {
		this.rendered= rendered;
	}

	public String getHelpShort() {
		return helpShort;
	}

	public void setHelpShort(String helpShort) {
		this.helpShort= helpShort;
	}	
}
