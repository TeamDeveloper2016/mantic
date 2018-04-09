package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;

public class Cliente implements Serializable {
	
	private static final long serialVersionUID=-9129670100143864291L;

	private String cuenta;
	private String contrasenia;
	private String error;
  private String recuperar;
	private String curp;
	private String nueva;
	private String confirma;	
	
	public Cliente(String cuenta, String contrasenia, String error, String nueva, String confirma) {
		this.cuenta     = cuenta;
		this.contrasenia= contrasenia;
		this.error      = error;
		this.nueva      = nueva;
		this.confirma   = confirma;
	}

	public Cliente(String recuperar, String curp) {
    this.recuperar= recuperar;
		this.curp     = curp;		
	}	

	public String getNueva() {
		return nueva;
	}

	public void setNueva(String nueva) {
		this.nueva=nueva;
	}

	public String getConfirma() {
		return confirma;
	}

	public void setConfirma(String confirma) {
		this.confirma=confirma;
	}
	
	public String getCuenta() {		
		return cuenta;
	}
	
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	public String getContrasenia() {
		return contrasenia;
	}
	
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;		
	}

  public String getRecuperar() {
    return recuperar;
  }

  public void setRecuperar(String recuperar) {
    this.recuperar = recuperar;
  }

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp=curp;
	}
	
}
