package mx.org.kaana.kajool.procesos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/10/2016
 *@time 11:07:50 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class UsuariosEnLinea implements Serializable {

  private static final long serialVersionUID= -4728324187077016226L;
  private static final Log LOG              = LogFactory.getLog(UsuariosEnLinea.class);

  private boolean multiSesion;
  private Calendar hora;	
  private HashMap cuentas;	
	private Map<Long, Grupo> grupos;

	public UsuariosEnLinea() throws Exception {
		this.cuentas    = new HashMap<>();
		this.grupos     = new HashMap<>();
    this.hora       = Calendar.getInstance();
    this.multiSesion= false;				
		LOG.info("[REGISTRO USUARIOS [" + this.hora.getTime().toString()+ "]]");
	} // UsuariosEnLinea

  public HashMap getCuentas() {
		return cuentas;
	}

	public void setCuentas(HashMap cuentas) {
		this.cuentas= cuentas;
	}

	public Calendar getHora() {
		return hora;
	}

	public Calendar getHora(Calendar hora) {
		return hora;
	}

	public void setHora(Calendar hora) {
		this.hora= hora;
	}

	public Map<Long, Grupo> getGrupos() {
		return grupos;
	}

	public boolean isMultiSesion() {
		return multiSesion;
	}

	public void setMultiSesion(boolean multiSesion) {
		this.multiSesion= multiSesion;
	}

  public HashMap getSessiones() {
		return this.cuentas;
	} // getSessiones

	public void delete(String sesion) {
		if(this.cuentas.containsKey(sesion)) {
			this.cuentas.remove(sesion);
			LOG.info("[SESSION ELIMINADA [" + sesion + "]]");
		} // if
	} //  delete

	public void deleteCuenta(String sesion, String cuenta) {
		Usuario usuario= null;
		if(this.cuentas.containsKey(sesion)) {
			HashMap cuentaRegistradas= (HashMap) this.cuentas.get(sesion);
			if(cuentaRegistradas.containsKey(cuenta)) {
				usuario = (Usuario) cuentaRegistradas.get(cuenta);
				if (this.grupos.containsKey(usuario.getIdGrupo())) {
					this.grupos.get(usuario.getIdGrupo()).remainUsuarioPerfil(usuario.getIdPerfil());
					if (this.grupos.get(usuario.getIdGrupo()).getPerfiles().isEmpty())
						this.grupos.remove(usuario.getIdGrupo());					
				} // if
				cuentaRegistradas.remove(cuenta);
				LOG.info("[-REGISTRO DE CUENTAS [" + sesion + "]]");
			} // if
			else
				LOG.warn("[*REGISTRO DE CUENTAS [" + cuenta + "]]");
		} // if
	} //  delete

	public void add(String sesion) {
		try {
			if(!this.cuentas.containsKey(sesion)) {
				this.cuentas.put(sesion, new HashMap());
				LOG.info("[SESSION REGISTRADA [" + sesion + "]");
			} // if
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
	} // add

	public void addCuenta(String sesion, String cuenta) {
		try {
			if(this.cuentas.containsKey(sesion)) {
				HashMap cuentaRegistradas= (HashMap)this.cuentas.get(sesion);
				if(!cuentaRegistradas.containsKey(cuenta)) {
					Usuario usuario= new Usuario(sesion, cuenta, -1);
					cuentaRegistradas.put(cuenta, usuario);
					LOG.info("[+REGISTRO DE CUENTAS [" + cuenta + "]]");
				}
				else
					LOG.warn("[*REGISTRO DE CUENTAS [" + cuenta + "]]");
			} // if
		} // try
		catch(Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
	} // addCuenta

	public void addCuenta(String sesion, Usuario usuario) {
		try {
			if(this.cuentas.containsKey(sesion)) {
				HashMap cuentaRegistrada= (HashMap) this.cuentas.get(sesion);
				if(!cuentaRegistrada.containsKey(usuario.getCuenta())) {
					cuentaRegistrada.put(usuario.getCuenta(), usuario);
					if (this.grupos.containsKey(usuario.getIdGrupo()))
						this.grupos.get(usuario.getIdGrupo()).addPerfil(usuario.getIdPerfil(), usuario.getPerfil());					
          else{
						this.grupos.put(usuario.getIdGrupo(), new Grupo(usuario.getIdGrupo(), usuario.getGrupo()));					
            this.grupos.get(usuario.getIdGrupo()).addPerfil(usuario.getIdPerfil(), usuario.getPerfil());
          } // else
					LOG.info("[+REGISTRO DE CUENTAS] ".concat(usuario.toString()));
				}
				else
					LOG.warn("[*REGISTRO DE CUENTAS] "+ usuario.toString());
			} // if
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
	} // addCuenta	

	public HashMap getSesion(String sesion) {
		HashMap regresar= null;
		if(this.cuentas.containsKey(sesion))
			regresar= (HashMap)this.cuentas.get(sesion);
		return regresar;
	}  // getSesion

	public HashMap getCuentas(String sesion) {
		HashMap regresar= null;
		if(this.cuentas.containsKey(sesion))
			regresar= (HashMap)this.cuentas.get(sesion);
		return regresar;
	} // getCuentas

	public Usuario getCuenta(String sesion, String cuenta) {
		Usuario regresar       = null;
		HashMap cuentaSession  = null;
		if(this.cuentas.containsKey(sesion)) {
			cuentaSession= (HashMap)this.cuentas.get(sesion);
			if(cuentaSession.containsKey(cuenta))
				regresar= (Usuario) cuentaSession.get(cuenta);
		} // if
		return regresar;
	} // getCuenta

	public long getComienzo() {
		return this.hora.getTimeInMillis();
	} // getComenzo

	public void removeUsuarioPerfil (Long idEncuesta, Long idPerfil) {
		if (this.grupos.containsKey(idEncuesta)) {
			this.grupos.get(idEncuesta).remainUsuarioPerfil(idPerfil);
			if (this.grupos.get(idEncuesta).getPerfiles().isEmpty())
				this.grupos.remove(idEncuesta);
		} // if
	} // removeUsuarioPerfil

	public void refresh (Long idEncuesta, Long idPerfil, String descripcionEncuesta, String descripcion) {
		if(this.grupos.containsKey(idEncuesta))
			this.grupos.get(idEncuesta).addPerfil(idPerfil,descripcion);
		else {
			this.grupos.put(idEncuesta, new Grupo(idEncuesta, descripcionEncuesta));
			this.grupos.get(idEncuesta).addPerfil(idPerfil, descripcion);
		} // else
	}

  public void clearMessage(){
    Usuario usuario= null;
    try {
      Iterator sesion= this.cuentas.entrySet().iterator();
      while (sesion.hasNext()) {
        Map.Entry sesionEntry  = (java.util.Map.Entry)  sesion.next();
        Map usrCuentas = (HashMap) sesionEntry.getValue();
        Iterator cuenta =  usrCuentas.entrySet().iterator();
         while(cuenta.hasNext()){
           Map.Entry cuentaEntry = (java.util.Map.Entry)cuenta.next();
           usuario= (Usuario) cuentaEntry.getValue();
           usuario.setMensaje(-1);
         } // while
      } // while
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  }
  
@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("+REGISTRO USUARIOS [");
		sb.append(this.hora.getTime().toString());
		sb.append("]");
		if(!this.cuentas.isEmpty()) {
			Iterator listado= this.cuentas.entrySet().iterator();
  		sb.append("{");
			while(listado.hasNext()) {
				sb.append("(");
				Map.Entry cuentasEntry= (Map.Entry)listado.next();
				HashMap cuentasRegistradas= (HashMap)cuentasEntry.getValue();
				sb.append(cuentasEntry.getKey().toString());
				if(!cuentasRegistradas.isEmpty()) {
					Iterator nombres=  cuentasRegistradas.entrySet().iterator();
					while(nombres.hasNext()) {
						sb.append("<");
						Map.Entry usuarioEntry= (Map.Entry)nombres.next();
						Usuario usuario= (Usuario) usuarioEntry.getValue();
						sb.append(usuario.toString());
						sb.append(">");
					} // while
				} // if
				sb.append(")");
			} // while
			sb.append("}");
		} // if
		else
			sb.append("No existen usuarios registrado en el sitio web.");
		return sb.toString();
	} // toString

	@Override
	public void finalize() {
    LOG.info("-REGISTRO USUARIOS ["+ Calendar.getInstance().getTime().toString()+ "]");
    this.cuentas.clear();
	  this.cuentas= null;
	  this.hora= null;	
	 } // finalize
  
}
