package mx.org.kaana.kajool.catalogos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 15, 2012
 *@time 12:23:54 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Monitoreo implements Serializable {
	
	private static final long serialVersionUID=6098914907638039562L;
	
	private Long total;
	private Long progreso;
	private boolean corriendo;
	private List<String> messages;

	public Monitoreo() {
		this.messages= new ArrayList<>();
	}
	
	public Long getProgreso() {
		return progreso;
	}

	public void setProgreso(Long progreso) {
		this.progreso=progreso;
	}
	
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total=total;
	}

	public List<String> getMessages() {
		return messages;
	}
	
	public boolean isCorriendo() {
		return corriendo;
	}

	public void comenzar(Long total) {
		this.total= total;
		comenzar();
	}
	
	public void comenzar() {
		this.corriendo= true;
		this.progreso = 0L;
		this.messages.clear();
	}
	
	public void terminar() {
		this.corriendo= false;		
		if(this.total== null || this.total.longValue()<= 0)
			this.progreso= 100L;
		else
		  this.progreso = this.total;
	}

	public void incrementar(int valor) {
		this.progreso= this.progreso+ valor;
	}
	
	public void incrementar() {
		incrementar(1);
	}

	@Override
	protected void finalize() throws Throwable {
		this.messages.clear();
		this.messages= null;
	}

	public void addError(String error) {
	  this.messages.add(error);
	}
	
}
