package mx.org.kaana.kajool.db.comun.transform;

import mx.org.kaana.libs.formato.Cadena;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/09/2014
 *@time 04:36:00 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class TransformerSource  extends Transformer {

	
	private static final long serialVersionUID=-310561743815015750L;
	
	@Override
	public abstract Object tuple(Object[] data, String[] fields, String[] bdNames) throws InstantiationException, IllegalAccessException;

	@Override
	public Object transformTuple(Object[] data, String[] fields) {
		Object regresar= null;
    if (isChange()) {
      setBdNames(new String[fields.length]);
      for(int x= 0; x< fields.length; x++) {
        getBdNames()[x]= fields[x];
				if (fields[x].toUpperCase().startsWith("ID_KEY"))
					fields[x]=Cadena.toBeanNameEspecial(fields[x]);
      }
      setChange(false);
    } // if
    try {
      regresar= tuple(data, fields, getBdNames());
    }
    catch(Exception e) {
      regresar= null;
    } // catch
    return regresar;
	}
}
