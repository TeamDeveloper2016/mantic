package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 28/04/2010
 *@time 04:20:15 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum ESql {

  INSERT, UPDATE, DELETE, SELECT, UPSERT;

	public Long getKey () {
	  return Long.valueOf(ordinal()+1);
	}

}
