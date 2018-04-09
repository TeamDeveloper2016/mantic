package mx.org.kaana.kajool.procesos.usuarios.delegar.beans;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/09/2015
 *@time 11:27:38 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Informe implements Serializable {
	
	private static final long serialVersionUID= 2485037573924838588L;	
	private static final String H= "HOMBRE";
	private static final String M= "MUJER";
	private String curp;
	private String nombres;
	private String primerApellido;
	private String segundoApellido;	
	private String nombreCompleto;
	private String sexo;
	private String cuenta;
	private String contrasenia;
	private String perfil;

	public Informe() {
		this("", "", "", "", "", "", "", "", "");
	}	// Informe	

	public Informe(String curp, String nombres, String primerApellido, String segundoApellido, String nombreCompleto, String sexo, String cuenta, String contrasenia, String perfil) {
		this.curp             = curp;
		this.nombres          = nombres;
		this.primerApellido   = primerApellido;
		this.segundoApellido  = segundoApellido;
		this.nombreCompleto   = nombreCompleto;
		this.sexo             = sexo;
		this.cuenta           = cuenta;
		this.contrasenia      = contrasenia;
		this.perfil           = perfil;
	} // Informe

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp= curp;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres= nombres;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido= primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido= segundoApellido;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto= nombreCompleto;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo= sexo;
	}
	
	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta= cuenta;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia= contrasenia;
	}	

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil= perfil;
	}	
}
