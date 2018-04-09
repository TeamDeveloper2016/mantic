package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.enums.ETipoDato;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 01:48:52 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class OperadorRelacional implements Serializable {
	
  private static OperadorRelacional instance;
  private static Object mutex;
	
	private static final List<Operador> text;
	private static final List<Operador> numeric;
	private static final List<Operador> date;
	private static final List<Operador> time;
	private static final List<Operador> timestamp;
  private static final long serialVersionUID = -8590312454949712253L;

  static {
    mutex = new Object();
		text= new ArrayList<>();
    text.add(new Operador(0, "Igual a", "= ''{0}''"));
    text.add(new Operador(1, "Mayor a", "> ''{0}''"));
    text.add(new Operador(2, "Menor a", "< ''{0}''"));
    text.add(new Operador(3, "Mayor igual a", ">= ''{0}''"));
    text.add(new Operador(4, "Menor igual a", "<= ''{0}''"));
    text.add(new Operador(5, "Diferente a", "<> ''{0}''"));
    text.add(new Operador(6, "Comienza con", "like ''{0}%''"));
    text.add(new Operador(7, "Termina con", "like ''%{0}''"));
    text.add(new Operador(8, "Contiene a", "like ''%{0}%''"));
		
		numeric= new ArrayList<>();
    numeric.add(new Operador(0, "Igual a", "= {0}"));
    numeric.add(new Operador(1, "Mayor a", "> {0}"));
    numeric.add(new Operador(2, "Menor a", "< {0}"));
    numeric.add(new Operador(3, "Mayor igual a", ">= {0}"));
    numeric.add(new Operador(4, "Menor igual a", "<= {0}"));
    numeric.add(new Operador(5, "Diferente a", "<> {0}"));
		
		date= new ArrayList<>();
    date.add(new Operador(0, "Igual a", "= to_date(''{0}'', ''yyyy-mm-dd'')"));
    date.add(new Operador(1, "Mayor a", "> to_date(''{0}'', ''yyyy-mm-dd'')"));
    date.add(new Operador(2, "Menor a", "< to_date(''{0}'', ''yyyy-mm-dd'')"));
    date.add(new Operador(3, "Mayor igual a", ">= to_date(''{0}'', ''yyyy-mm-dd'')"));
    date.add(new Operador(4, "Menor igual a", "<= to_date(''{0}'', ''yyyy-mm-dd'')"));
    date.add(new Operador(5, "Diferente a", "<> to_date(''{0}'', ''yyyy-mm-dd'')"));

		time= new ArrayList<>();
    time.add(new Operador(0, "Igual a", "= to_timestamp(to_char(''1970-01-01 ''||''{0}''),''YYYY-MM-DD HH24:MI:SS'')"));
    time.add(new Operador(1, "Mayor a", "> to_timestamp(to_char(''1970-01-01 ''||''{0}''),''YYYY-MM-DD HH24:MI:SS'')"));
    time.add(new Operador(2, "Menor a", "< to_timestamp(to_char(''1970-01-01 ''||''{0}''),''YYYY-MM-DD HH24:MI:SS'')"));
    time.add(new Operador(3, "Mayor igual a", ">= to_timestamp(to_char(''1970-01-01 ''||''{0}''),''YYYY-MM-DD HH24:MI:SS'')"));
    time.add(new Operador(4, "Menor igual a", "<= to_timestamp(to_char(''1970-01-01 ''||''{0}''),''YYYY-MM-DD HH24:MI:SS'')"));
    time.add(new Operador(5, "Diferente a", "<> to_timestamp(to_char(''1970-01-01 ''||''{0}''),''YYYY-MM-DD HH24:MI:SS'')"));

		timestamp= new ArrayList<>();
    timestamp.add(new Operador(0, "Igual a", "= to_timestamp(''{0}'', ''YYYY-MM-DD HH24:MI:SS.FF'')"));
    timestamp.add(new Operador(1, "Mayor a", "> to_timestamp(''{0}'', ''YYYY-MM-DD HH24:MI:SS.FF'')"));
    timestamp.add(new Operador(2, "Menor a", "< to_timestamp(''{0}'', ''YYYY-MM-DD HH24:MI:SS.FF'')"));
    timestamp.add(new Operador(3, "Mayor igual a", ">= to_timestamp(''{0}'', ''YYYY-MM-DD HH24:MI:SS.FF'')"));
    timestamp.add(new Operador(4, "Menor igual a", "<= to_timestamp(''{0}'', ''YYYY-MM-DD HH24:MI:SS.FF'')"));
    timestamp.add(new Operador(5, "Diferente a", "<> to_timestamp(''{0}'', ''YYYY-MM-DD HH24:MI:SS.FF'')"));
  }

	private OperadorRelacional() {
	}

  public static OperadorRelacional getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new OperadorRelacional();
      }
    } // if
    return instance;
  }
	
  public List<Operador> toFieldText() {
		return this.text;
	}
	
  public List<Operador> toFieldNumeric() {
		return this.numeric;
	}
	
  public List<Operador> toFieldDate() {
		return this.date;
	}
	
  public List<Operador> toFieldTime() {
		return this.time;
	}
	
  public List<Operador> toFieldTimestamp() {
		return this.timestamp;
	}
	
	public List<Operador> toList(ETipoDato type) {
		List<Operador> regresar= null;
    switch(type) {
			case TEXT:
				regresar= toFieldText();
				break;
			case DOUBLE:
				regresar= toFieldNumeric();
				break;
			case LONG:
				regresar= toFieldNumeric();
				break;
			case BLOB:
				regresar= toFieldText();
				break;
			case DATE:
				regresar= toFieldDate();
				break;
			case TIME:
				regresar= toFieldTime();
				break;
			case TIMESTAMP:
				regresar= toFieldTimestamp();
				break;
		} // switch
		return regresar;
	}
	
}
