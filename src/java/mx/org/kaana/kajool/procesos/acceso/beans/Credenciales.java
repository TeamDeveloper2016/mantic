package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/09/2015
 *@time 02:17:55 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Credenciales implements Serializable {
	
	private static final long serialVersionUID= 1302551454338750663L;
	private String ip;
  private String cuenta;
  private String contrasenia;	
	private String cuentaInicial;
  private Long perfiles;	
	private Long perfilesDelega;
  private boolean registro;
	private boolean accesoDelega;	
	private boolean grupoPerfiles;
	private boolean menuEncabezado;

	public Credenciales() {
		this("", "");
	} // Credenciales

	public Credenciales(String cuenta, String contrasenia) {
		this(null, cuenta, contrasenia, cuenta, 0L, 0L,false, false, false, true);
	} // Credenciales
	
	public Credenciales(String ip, String cuenta, String contrasenia, String cuentaInicial, Long perfiles, Long perfilesDelega, boolean registro, boolean accesoDelega, boolean grupoPerfiles, boolean menuEncabezado) {
		this.ip            = ip;
		this.cuenta        = cuenta;
		this.contrasenia   = contrasenia;
		this.cuentaInicial = cuentaInicial;
		this.perfiles      = perfiles;
		this.perfilesDelega= perfilesDelega;
		this.registro      = registro;
		this.accesoDelega  = accesoDelega;
		this.grupoPerfiles = grupoPerfiles;
		this.menuEncabezado= menuEncabezado;
	} // Credenciales

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip= ip;
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

	public String getCuentaInicial() {
		return cuentaInicial;
	}

	public void setCuentaInicial(String cuentaInicial) {
		this.cuentaInicial= cuentaInicial;
	}

	public Long getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(Long perfiles) {
		this.perfiles= perfiles;
	}

	public Long getPerfilesDelega() {
		return perfilesDelega;
	}

	public void setPerfilesDelega(Long perfilesDelega) {
		this.perfilesDelega= perfilesDelega;
	}

	public boolean isRegistro() {
		return registro;
	}

	public void setRegistro(boolean registro) {
		this.registro= registro;
	}

	public boolean isAccesoDelega() {
		return accesoDelega;
	}

	public void setAccesoDelega(boolean accesoDelega) {
		this.accesoDelega= accesoDelega;
	}

	public boolean isGrupoPerfiles() {
		return grupoPerfiles;
	}

	public void setGrupoPerfiles(boolean grupoPerfiles) {
		this.grupoPerfiles= grupoPerfiles;
	}

	public boolean isMenuEncabezado() {
		return menuEncabezado;
	}

	public void setMenuEncabezado(boolean menuEncabezado) {
		this.menuEncabezado= menuEncabezado;
	}		
	
	public boolean validaPerfilesUsuario(){
		return (this.perfiles>= 1L && this.perfilesDelega>= 1L) || this.perfiles> 1L || this.perfilesDelega> 1L;
	}
}
