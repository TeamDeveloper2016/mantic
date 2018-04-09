package mx.org.kaana.kajool.procesos.plantillas.beans;

import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/03/2013
 *@time 06:50:58 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Persona implements IBaseDto{
	private String nombres;
	private String apellidoMat;
	private String apellidoPat;
	private String curp;
	private String rfc;
	private Long numeroEmpleado;
	private String perfil;
	private String login;
	private String claveOper;
	private Long nivel;

	public Persona() {
		this(null,null,null,null,null,null,null,null,null,null);
	}

	public Persona(String nombres, String apellidoMat, String apellidoPat, String curp, String rfc, Long numeroEmpleado, String perfil, String login, String claveOper, Long nivel) {
		this.nombres=nombres;
		this.apellidoMat=apellidoMat;
		this.apellidoPat=apellidoPat;
		this.curp=curp;
		this.rfc=rfc;
		this.numeroEmpleado=numeroEmpleado;
		this.perfil=perfil;
		this.login=login;
		this.claveOper=claveOper;
		this.nivel=nivel;
	}


	public String getNombres() {
		return nombres;
	}

	public String getApellidoMat() {
		return apellidoMat;
	}

	public String getApellidoPat() {
		return apellidoPat;
	}

	public String getCurp() {
		return curp;
	}

	public String getRfc() {
		return rfc;
	}

	public Long getNumeroEmpleado() {
		return numeroEmpleado;
	}

	public String getPerfil() {
		return perfil;
	}

	public String getLogin() {
		return login;
	}

	public String getClaveOper() {
		return claveOper;
	}

	public Long getNivel() {
		return nivel;
	}

	@Override
	public Long getKey() {
		return this.numeroEmpleado;
	}

	@Override
	public void setKey(Long key) {
		this.numeroEmpleado = key;
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

	public void setNombres(String nombres) {
		this.nombres=nombres;
	}

	public void setApellidoMat(String apellidoMat) {
		this.apellidoMat=apellidoMat;
	}

	public void setApellidoPat(String apellidoPat) {
		this.apellidoPat=apellidoPat;
	}

	public void setCurp(String curp) {
		this.curp=curp;
	}

	public void setRfc(String rfc) {
		this.rfc=rfc;
	}

	public void setNumeroEmpleado(Long numeroEmpleado) {
		this.numeroEmpleado=numeroEmpleado;
	}

	public void setPerfil(String perfil) {
		this.perfil=perfil;
	}

	public void setLogin(String login) {
		this.login=login;
	}

	public void setClaveOper(String claveOper) {
		this.claveOper=claveOper;
	}

	public void setNivel(Long nivel) {
		this.nivel=nivel;
	}
}
