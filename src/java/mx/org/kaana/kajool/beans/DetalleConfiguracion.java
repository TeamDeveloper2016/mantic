package mx.org.kaana.kajool.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:30:23
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


import java.io.Serializable;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

public class DetalleConfiguracion implements Serializable,IBaseDto {
	private static final long serialVersionUID=5100084965451247285L;
	private String entidades;
	private  Long totalHilos;

	public DetalleConfiguracion () {
	  this.entidades="";
		this.totalHilos=40L;
	}
	
	public String getEntidades() {
		return entidades;
	}

	public void setEntidades(String entidades) {
		this.entidades=entidades;
	}

	public Long getTotalHilos() {
		return totalHilos;
	}

	public void setTotalHilos(Long totalHilos) {
		this.totalHilos=totalHilos;
	}

	@Override
	public Long getKey() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setKey(Long key) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map toMap() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isValid() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object toValue(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toAllKeys() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toKeys() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Class toHbmClass() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toString() {
		return "DetalleConfiguracion {"+"entidades="+entidades+" totalHilos="+totalHilos+'}';
	}
}
	
	
	
	
