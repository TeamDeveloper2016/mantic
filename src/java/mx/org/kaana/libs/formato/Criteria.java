/*
 * Code write by user.admin
 * Date 9/05/2009
 */

package mx.org.kaana.libs.formato;

import mx.org.kaana.kajool.enums.EFormatoDinamicos;

/**
 *
 * @author alejandro.jimenez
 */
public class Criteria {

	public EFormatoDinamicos format;
	public ITokenCfg cfg;

	public Criteria(EFormatoDinamicos format) {
		this.format= format;
	}

	public Criteria(ITokenCfg cfg) {
		this.cfg = cfg;
	}
	
	public ITokenCfg getCfg() {
		return cfg;
	}

	public void setCfg(ITokenCfg cfg) {
		this.cfg = cfg;
	}

	public EFormatoDinamicos getFormat() {
		return format;
	}

	public void setFormat(EFormatoDinamicos format) {
		this.format = format;
	}
	
  public boolean isBasic() {
		return getCfg()== null;
	}	

}
