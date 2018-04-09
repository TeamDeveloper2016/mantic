package mx.org.kaana.kajool.procesos.usuarios.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 05:43:52 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import mx.org.kaana.libs.pagina.UISelectEntity;

public class UIOrganizacionAmbito extends BaseCombos implements Serializable {

	private static final long serialVersionUID=2640919214876299955L;

	public UIOrganizacionAmbito(Long idEncuesta) {
		this(null, null, null, idEncuesta, false);
	}

	public UIOrganizacionAmbito(UISelectEntity organizacion, UISelectEntity entidad, UISelectEntity ambito, Long idEncuesta) {
		super(organizacion, entidad, ambito, idEncuesta);
	}

	public UIOrganizacionAmbito(UISelectEntity organizacion, UISelectEntity entidad, UISelectEntity ambito, Long idEncuesta, boolean automatico) {
		super(organizacion, entidad, ambito, idEncuesta);
		if (automatico) {
			init();
		} // if
	}

	private void init() {
		loadOrganizaciones();
		loadAmbitos();
		loadEntidades();
		loadOficinas();
	}

	public void loadAmbitos(UISelectEntity organizacion) {
		setOrganizacion(organizacion);
		loadAmbitos();
	}

	@Override
	public void loadEntidades() {
		super.loadEntidades();
	}

	@Override
	public void loadOficinas() {
		super.loadOficinas();
	}

	@Override
	public void loadOrganizaciones() {
		super.loadOrganizaciones();
	}
}
