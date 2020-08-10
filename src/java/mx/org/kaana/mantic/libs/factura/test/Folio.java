package mx.org.kaana.mantic.libs.factura.test;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company INEGI
 *@project IKTAN (Sistema de seguimiento y control de proyectos)
 *@date 8/08/2020
 *@time 11:59:30 AM 
 *@author Alejandro Jimenez Garcia <alejandro.jimenez@inegi.org.mx>
 */

public class Folio implements Serializable {
	
  private int folio;
  private String duplicado;
  private String ticket;
	private String rfc;
	private double total;
	

	public Folio(int folio, String duplicado, String rfc, double total) {
		this.folio=folio;
		this.duplicado=duplicado;
		this.ticket= "0".equals(ticket)? null: ticket;
		this.rfc=rfc;
		this.total=total;
	}

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio=folio;
	}

	public String getDuplicado() {
		return duplicado;
	}

	public void setDuplicado(String duplicado) {
		this.duplicado=duplicado;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket=ticket;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc=rfc;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total=total;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=23*hash+Objects.hashCode(this.folio);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Folio other=(Folio) obj;
		if (!Objects.equals(this.folio, other.folio)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Folio{"+"folio="+folio+", duplicado="+duplicado+", ticket="+ticket+", rfc="+rfc+", total="+total+'}';
	}
	
}
