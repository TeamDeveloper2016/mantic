package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/09/2015
 *@time 06:21:57 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class GrupoPerfiles implements Serializable {

	private static final long serialVersionUID= -6681348945349348040L;  	
  
	private Long idMenu;
	private Long idPerfil;	
	private Long idGrupo;
	private Long idEmpleado;
	private Long idUsuario;
	private String nombre;	
	private String cuenta;	
	private String usuarioPerfil;
	private String entidad;
	private boolean tipoAccesoDelega;
	private boolean ultimoNivel;
	private String perfil;
	
	public GrupoPerfiles(Long idPerfil, String nombre, boolean ultimoNivel) {
		this(idPerfil, nombre, ultimoNivel, -1L);
	}
	
	public GrupoPerfiles(Long idPerfil, String nombre, boolean ultimoNivel, String cuenta) {
		this(idPerfil, nombre, ultimoNivel, -1L, cuenta, null, true);
	}

  public GrupoPerfiles(Long idPerfil, String nombre, boolean ultimoNivel, Long idGrupo) {
		this (idPerfil,nombre,ultimoNivel,idGrupo,"");
	}
	
  public GrupoPerfiles(Long idPerfil, String nombre, boolean ultimoNivel, Long idGrupo, String cuenta) {
		this(idPerfil,nombre,ultimoNivel,idGrupo, cuenta, null);
	}
	
  public GrupoPerfiles(Long idPerfil, String nombre, boolean ultimoNivel, Long idGrupo, String cuenta, Long idMenu) {
		this (idPerfil,nombre,ultimoNivel,idGrupo, cuenta, null, false, null, idMenu, -1L, null, null);
	}
	
  public GrupoPerfiles(Long idPerfil, String nombre, boolean ultimoNivel, Long idGrupo, String cuenta, Long idEmpleado, boolean tipoAccesoDelega) {
		this(idPerfil, nombre, ultimoNivel, idGrupo, cuenta, idEmpleado, tipoAccesoDelega, "", -1L, -1L, null, null);
	}
	
  public GrupoPerfiles(Long idPerfil, String nombre, boolean ultimoNivel, Long idGrupo, String cuenta, Long idEmpleado, boolean tipoAccesoDelega, String usuarioPerfil, Long idMenu, Long idUsuario, String entidad, String perfil) {
		this.nombre          = nombre;
		this.ultimoNivel     = ultimoNivel;
		this.idPerfil        = idPerfil;
		this.idGrupo         = idGrupo;
		this.cuenta          = cuenta;
		this.idEmpleado      = idEmpleado;
		this.tipoAccesoDelega= tipoAccesoDelega;
		this.usuarioPerfil   = usuarioPerfil;
		this.idMenu          = idMenu;
		this.idUsuario       = idUsuario;
    this.entidad         = entidad;
    this.perfil          = perfil;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario=idUsuario;
	}

	public Long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Long idPerfil) {
		this.idPerfil= idPerfil;
	}

	public Long getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Long idGrupo) {
		this.idGrupo= idGrupo;
	}

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado= idEmpleado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre= nombre;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta= cuenta;
	}

	public String getUsuarioPerfil() {
		return usuarioPerfil;
	}

	public void setUsuarioPerfil(String usuarioPerfil) {
		this.usuarioPerfil= usuarioPerfil;
	}

	public boolean isTipoAccesoDelega() {
		return tipoAccesoDelega;
	}

	public void setTipoAccesoDelega(boolean tipoAccesoDelega) {
		this.tipoAccesoDelega= tipoAccesoDelega;
	}

	public boolean isUltimoNivel() {
		return ultimoNivel;
	}

	public void setUltimoNivel(boolean ultimoNivel) {
		this.ultimoNivel= ultimoNivel;
	}

	public Long getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(Long idMenu) {
		this.idMenu= idMenu;
	}	

  public String getEntidad() {
    return entidad;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }	  

  public String getPerfil() {
    return perfil;
  }

  public void setPerfil(String perfil) {
    this.perfil = perfil;
  }
    
	@Override
  public String toString() {
    StringBuilder regresar = new StringBuilder();
    regresar.append("[");
    regresar.append(idGrupo);
    regresar.append(",");
    regresar.append(idPerfil);
		regresar.append(",");
    regresar.append(idUsuario);
		regresar.append(",");
    regresar.append(idEmpleado);
    regresar.append("]");
    return regresar.toString();
  }
}
