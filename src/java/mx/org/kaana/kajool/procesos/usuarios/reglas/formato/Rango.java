package mx.org.kaana.kajool.procesos.usuarios.reglas.formato;

import java.util.List;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 2/09/2015
 * @time 12:17:46 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public abstract class Rango {

	private int start;
	private int end;

	public Rango(int start, int end) {
		setStart(start);
		setEnd(end);
	}

	public abstract List<String> getSerie();

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start=start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end=end;
	}
}
