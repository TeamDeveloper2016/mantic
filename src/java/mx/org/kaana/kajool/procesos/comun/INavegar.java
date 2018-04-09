package mx.org.kaana.kajool.procesos.comun;

import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.kajool.enums.EPerfiles;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/09/2015
 *@time 03:29:49 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract  class INavegar extends IBaseAttribute{
	private static final long serialVersionUID=5171277734923467740L;
	
	protected abstract void doAvanzar();
	protected abstract void doRetroceder();

  public final Long getIdAlumno(){
		return this.attrs.get("idAlumno")!= null? (Long)this.attrs.get("idAlumno"): -1L;
	}

  public final Long getIdEmpleado(){
		return this.attrs.get("idEmpleado")!=null? (Long)this.attrs.get("idEmpleado"):-1L;
	}

  public final void setIdAlumno(Long idAlumno){
		this.attrs.put("idAlumno", idAlumno);
	}

  public final void setIdEmpleado(Long idEmpleado){
		this.attrs.put("idEmpleado", idEmpleado);
	}

  public final EPerfiles getTipoPersona(){
		return (EPerfiles)this.attrs.get("tipoPersona");
	}

  public final void setIdPersona(Long idPersona){
		this.attrs.put("idPersona", idPersona);
	}

  public final Long getIdPersona(){
		return this.attrs.get("idPersona")!= null? (Long)this.attrs.get("idPersona"):-1L;
	}
	
	public final void doNavegar(String accion){
		if(accion.equals("AVANZAR"))
			doAvanzar();
		else
			doRetroceder();
	}//doNavegarbn

}
