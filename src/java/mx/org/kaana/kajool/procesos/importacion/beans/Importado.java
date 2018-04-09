package mx.org.kaana.kajool.procesos.importacion.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.EFormatos;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 05:14:07 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Importado implements Serializable {

	private static final long serialVersionUID=-3671748558385047604L;
	private String name;
	private String content;
	private EFormatos format;
	private Long size;

	public Importado(String name, String content, EFormatos format, Long size) {
		this.name=name;
		this.content=content;
		this.format=format;
		this.size=size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content=content;
	}

	public EFormatos getFormat() {
		return format;
	}

	public void setFormat(EFormatos format) {
		this.format=format;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size=size;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=37*hash+(this.name!=null ? this.name.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Importado other=(Importado) obj;
		if ((this.name==null) ? (other.name!=null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Importado{"+"name="+name+", content="+content+", format="+format+", size="+size+'}';
	}
}
