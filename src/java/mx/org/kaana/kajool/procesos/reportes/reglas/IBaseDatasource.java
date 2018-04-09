package mx.org.kaana.kajool.procesos.reportes.reglas;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.procesos.reportes.beans.IBaseRenglonAva;
import net.sf.jasperreports.engine.JRException;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 9/09/2015
 * @time 05:04:06 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class IBaseDatasource implements Serializable {

	private static final long serialVersionUID=4806462556205538042L;
	private List<IBaseRenglonAva> records;
	private Integer indice;

	public IBaseDatasource(List<IBaseRenglonAva> records) {
		this.records=records;
		this.indice=-1;
	}

	protected List<IBaseRenglonAva> getRecords() {
		return records;
	}

	protected Integer getIndice() {
		return indice;
	}

	protected void incrementar() {
		++this.indice;
	}

	protected boolean hasNext() throws JRException {
		incrementar();
		return getIndice()<getRecords().size();
	}

	protected Long toNivel() {
		return getIndice().longValue();
	}

	public Long getTotal() {
		return ((Number) getRecords().size()).longValue();
	}

	public void reload() {
		this.indice=-1;
	}
}
