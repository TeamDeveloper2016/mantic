package mx.org.kaana.kajool.db.comun.transform;

import mx.org.kaana.kajool.db.comun.sql.SourceEntity;
import mx.org.kaana.kajool.db.comun.sql.Value;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/09/2014
 *@time 04:08:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class TransformSourceEntity extends TransformerSource {
	
	private static final long serialVersionUID=7173625677753139212L;	
	
	@Override
  public Object tuple(Object[] data, String[] fields, String[] bdNames) throws InstantiationException, IllegalAccessException {
    SourceEntity regresar = new SourceEntity();
    for (int x=0; x< data.length; x++) {
      regresar.put(fields[x], new Value(fields[x], data[x], bdNames[x]));
    } // for
    return regresar;
  }
	
}
