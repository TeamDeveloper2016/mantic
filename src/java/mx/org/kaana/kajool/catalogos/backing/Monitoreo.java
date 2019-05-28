package mx.org.kaana.kajool.catalogos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import mx.org.kaana.kajool.catalogos.beans.Elapsed;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 15, 2012
 *@time 12:23:54 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Monitoreo implements Serializable {
	
	private static final long serialVersionUID=6098914907638039562L;
	
	private String id;
	private Long total;
	private Long progreso;
	private boolean corriendo;
	private List<String> messages;
	
	private Calendar start;
	private Calendar finished;
  private Elapsed elapsed;
	
	public Monitoreo() {
		this("");
	}
	
	public Monitoreo(String id) {
		this.id      = id;
		this.messages= new ArrayList<>();
		this.elapsed = new Elapsed();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id=id;
	}
	
	public Long getProgreso() {
		return progreso;
	}

	public Integer getPorcentaje() {
		return new Integer((int)(this.getTotal()<= 0? 0: this.getProgreso()* 100/ this.getTotal()));
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

	public Elapsed getElapsed() {
		return elapsed;
	}

	public void comenzar(Long total) {
		this.total= total;
		comenzar();
	}
	
	public void comenzar() {
		this.corriendo= true;
		this.progreso = 0L;
		this.messages.clear();
		this.start   = Calendar.getInstance();
		this.finished= Calendar.getInstance();
	}
	
	public void terminar() {
		this.corriendo= false;		
		if(this.total== null || this.total<= 0)
			this.progreso= 100L;
		else
		  this.progreso = this.total;
		this.finished= Calendar.getInstance();
	}
  
	public void incrementar(int valor) {
		this.progreso= this.progreso+ valor;
		this.finished= Calendar.getInstance();
		this.elapsed.calcualte(this.start, this.finished, this.progreso, this.total);
	}
	
	public void incrementar() {
		incrementar(1);
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.messages.clear();
			this.messages= null;
			this.elapsed = null;
		}
		finally {
			super.finalize();
		}
	}

	public void addError(String error) {
	  this.messages.add(error);
	}

	@Override
	public String toString() {
		return "Monitoreo{"+"total="+total+", progreso="+progreso+", corriendo="+corriendo+'}';
	}
	
}
