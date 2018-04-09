package mx.org.kaana.kajool.procesos.usuarios.reglas.beans;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 07:51:21 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;

public class CriteriosBusqueda implements Serializable {

	private static final long serialVersionUID=-4121538166956277598L;
	private String criterioNombre;
	private Long criterioPerfil;
	private UISelectEntity criterioEntidad;	
	private List<UISelectItem> listaPerfiles;
	protected List<UISelectEntity> listaEntidades;
	private String titleTabNombre;
	private String titleTabPerfil;
	private String titleTabEntidad;

	public CriteriosBusqueda() {
		setCriterioNombre("");
		setCriterioEntidad(null);
		setCriterioPerfil(-1L);
		setListaEntidades(new ArrayList<UISelectEntity>());
		setListaPerfiles(new ArrayList<UISelectItem>());
		setTitleTabNombre("Por nombre");
		setTitleTabPerfil("Por perfil");
		setTitleTabEntidad("Por entidad");
	}

	public String getTitleTabNombre() {
		return titleTabNombre;
	}

	public void setTitleTabNombre(String titleTabNombre) {
		this.titleTabNombre=titleTabNombre;
	}

	public String getTitleTabEntidad() {
		return titleTabEntidad;
	}

	public void setTitleTabEntidad(String titleTabEntidad) {
		this.titleTabEntidad=titleTabEntidad;
	}

	public String getTitleTabPerfil() {
		return titleTabPerfil;
	}

	public void setTitleTabPerfil(String titleTabPerfil) {
		this.titleTabPerfil=titleTabPerfil;
	}

	public String getCriterioNombre() {
		return criterioNombre;
	}

	public void setCriterioNombre(String criterioNombre) {
		this.criterioNombre=criterioNombre;
	}

	public Long getCriterioPerfil() {
		return criterioPerfil;
	}

	public void setCriterioPerfil(Long criterioPerfil) {
		this.criterioPerfil=criterioPerfil;
	}

	public UISelectEntity getCriterioEntidad() {
		return criterioEntidad;
	}

	public void setCriterioEntidad(UISelectEntity criterioEntidad) {
		this.criterioEntidad=criterioEntidad;
	}

	public List<UISelectEntity> getListaEntidades() {
		return listaEntidades;
	}

	public void setListaEntidades(List<UISelectEntity> listaEntidades) {
		this.listaEntidades=listaEntidades;
	}

	public List<UISelectItem> getListaPerfiles() {
		return listaPerfiles;
	}

	public void setListaPerfiles(List<UISelectItem> listaPerfiles) {
		this.listaPerfiles=listaPerfiles;
	}
	
}
