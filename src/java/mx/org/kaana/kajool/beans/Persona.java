package mx.org.kaana.kajool.beans;

import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Persona implements IBaseDto{
	private Long numeroEmpleado;
	private Long nivel;
	private String nombres;
	private String apellidoMat;
	private String apellidoPat;
	private String curp;
	private String rfc;	
	private String perfil;
	private String login;
	private String clave;
	private Long idClaveOperativa;

	public Persona() {
		this(null,null,null,null,null,null,null,null,null,null,null);
	}

	public Persona(String nombres, String apellidoMat, String apellidoPat, String curp, String rfc, Long numeroEmpleado, String perfil, String login, String clave, Long nivel, Long idClaveOperativa) {
		this.nombres         = nombres;
		this.apellidoMat     = apellidoMat;
		this.apellidoPat     = apellidoPat;
		this.curp            = curp;
		this.rfc             = rfc;
		this.numeroEmpleado  = numeroEmpleado;
		this.perfil          = perfil;
		this.login           = login;
		this.clave           = clave;
		this.nivel           = nivel;
		this.idClaveOperativa= idClaveOperativa;
	}

	@Override
	public Long getKey() {
		return this.numeroEmpleado;
	}

	@Override
	public void setKey(Long key) {
		this.numeroEmpleado= key;
	}	

	public String getNombres() {
		return nombres;
	}
	
	public void setNombres(String nombres) {
		this.nombres= nombres;
	}

	public String getApellidoMat() {
		return apellidoMat;
	}

	public void setApellidoMat(String apellidoMat) {
		this.apellidoMat= apellidoMat;
	}
	
	public String getApellidoPat() {
		return apellidoPat;
	}
	
	public void setApellidoPat(String apellidoPat) {
		this.apellidoPat= apellidoPat;
	}

	public String getCurp() {
		return curp;
	}
	
	public void setCurp(String curp) {
		this.curp= curp;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc= rfc;
	}
	
	public Long getNumeroEmpleado() {
		return numeroEmpleado;
	}
	
	public void setNumeroEmpleado(Long numeroEmpleado) {
		this.numeroEmpleado= numeroEmpleado;
	}

	public String getPerfil() {
		return perfil;
	}
	
	public void setPerfil(String perfil) {
		this.perfil= perfil;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login= login;
	}
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave= clave;
	}
	
	public Long getNivel() {
		return nivel;
	}
	
	public void setNivel(Long nivel) {
		this.nivel= nivel;
	}

	@Override
	public Map toMap() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Long getIdClaveOperativa() {
		return idClaveOperativa;
	}

	public void setIdClaveOperativa(Long idClaveOperativa) {
		this.idClaveOperativa= idClaveOperativa;
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

}
