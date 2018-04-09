package mx.org.kaana.kajool.procesos.usuarios.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.enums.EAmbitos;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 06:06:01 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class BaseCombos implements Serializable {

	private static final long serialVersionUID=5491305939734125949L;
	private List<UISelectEntity> organizaciones;
	private List<UISelectEntity> entidades;
	private List<UISelectEntity> ambitos;
	private List<UISelectEntity> organizacionesGrupos;
	private UISelectEntity organizacion;
	private UISelectEntity entidad;
	private UISelectEntity ambito;
	private UISelectEntity organizacionGrupo;
	private Long idEncuesta;

	public BaseCombos(Long idEncuesta) {
		this(null, null, null, idEncuesta);
	}

	public BaseCombos(UISelectEntity organizacion, UISelectEntity entidad, UISelectEntity ambito, Long idEncuesta) {
		this.organizacion=organizacion;
		this.entidad=entidad;
		this.ambito=ambito;
		this.idEncuesta=idEncuesta;
	}

	public List<UISelectEntity> getOrganizaciones() {
		return organizaciones;
	}

	public void setOrganizaciones(List<UISelectEntity> organizaciones) {
		this.organizaciones=organizaciones;
	}

	public List<UISelectEntity> getEntidades() {
		return entidades;
	}

	public void setEntidades(List<UISelectEntity> entidades) {
		this.entidades=entidades;
	}

	public List<UISelectEntity> getAmbitos() {
		return ambitos;
	}

	public void setAmbitos(List<UISelectEntity> ambitos) {
		this.ambitos=ambitos;
	}

	public List<UISelectEntity> getOrganizacionesGrupos() {
		return organizacionesGrupos;
	}

	public void setOrganizacionesGrupos(List<UISelectEntity> organizacionesGrupos) {
		this.organizacionesGrupos=organizacionesGrupos;
	}

	public UISelectEntity getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(UISelectEntity organizacion) {
		this.organizacion=organizacion;
	}

	public UISelectEntity getEntidad() {
		return entidad;
	}

	public void setEntidad(UISelectEntity entidad) {
		this.entidad=entidad;
	}

	public UISelectEntity getAmbito() {
		return ambito;
	}

	public void setAmbito(UISelectEntity ambito) {
		this.ambito=ambito;
	}

	public UISelectEntity getOrganizacionGrupo() {
		return organizacionGrupo;
	}

	public void setOrganizacionGrupo(UISelectEntity organizacionGrupo) {
		this.organizacionGrupo=organizacionGrupo;
	}

	public Long getIdEncuesta() {
		return idEncuesta;
	}

	public void setIdEncuesta(Long idEncuesta) {
		this.idEncuesta=idEncuesta;
	}

	protected void loadOrganizaciones() {
		Map<String, Object> params=new HashMap();
		try {
			params.put("idGrupo", JsfUtilities.getFlashAttribute("idGrupo"));
			this.organizaciones=UIEntity.build("VistaUnidadEjecuturaEntidadOficina", Constantes.DML_SELECT, params);
			if (!this.organizaciones.isEmpty()&&this.ambito==null) {
				this.organizacion=(UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(this.organizaciones);
			} // if
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

	protected void loadAmbitos() {
		Map<String, Object> params=null;
		try {
			params=new HashMap<String, Object>();
			params.put("idOrganizacion", this.organizacion.getKey());
			params.put("idEncuesta", this.idEncuesta);
			params.put("ambito", JsfUtilities.getFlashAttribute("idAmbito"));
			this.ambitos=UIEntity.build("VistaUnidadAmbitoDto", "unidadAmbito", params);
			if (!this.ambitos.isEmpty()&&this.ambito==null) {
				this.ambito=(UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(this.ambitos);
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally		
	}

	protected void loadEntidades() {
		Map<String, Object> params=null;
		try {
			params=new HashMap<String, Object>();
			params.put("idAmbito", this.ambito.getKey());
			params.put("idOrganizacion", this.organizacion.getKey());
			params.put("idEncuesta", this.idEncuesta);
			this.entidades=UIEntity.build("VistaUnidadAmbitoEntidadDto", "entidades", params);
			if (!this.entidades.isEmpty()&&this.entidad==null) {
				this.entidad=(UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(this.entidades);
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

	protected void loadOficinas() {
		Map<String, Object> params=null;
		String idXml="row";
		try {
			params=new HashMap<String, Object>();
			if (JsfBase.getAutentifica().getEmpleado().getDescripcionGrupo()!=null&&JsfUtilities.getFlashAttribute("idAmbito").equals(EAmbitos.OFICINA.getKey().longValue())) {
				params.put("idOrganizacionGrupo", JsfBase.getAutentifica().getEmpleado().getDescripcionGrupo());
				idXml="oficinaUsuario";
			} // if
			params.put("idEntidad", this.entidad.getKey());
			params.put("idEncuesta", this.idEncuesta);
			params.put("idAmbito", this.ambito.getKey());
			params.put("idOrganizacion", this.organizacion.getKey());
			this.organizacionesGrupos=UIEntity.build("OficinasEncuesta", idXml, params);
			if (!this.organizacionesGrupos.isEmpty()) {
				this.organizacionGrupo=(UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(this.organizacionesGrupos);
			} // if
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
}
